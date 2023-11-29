package uk.ac.sheffield.com2008.model.dao;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.model.mappers.OrderMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class OrderDAO {

    /**
     * The basket is equivalent to a given users pending order
     *
     * @param user
     * @return
     */
    public static Order getUsersBasket(User user) throws SQLException {
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        parameters.put("userUUID", user.getUuid());
        parameters.put("status", "PENDING");

        return getFirstOrderByFields(parameters);
    }

    public static List<Order> getUserOrdersHistory(User user) throws SQLException {
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        parameters.put("userUUID", user.getUuid());

        return getOrderListByFields(parameters);
    }


    /**
     * Get order matching given parameters.
     *
     * @param fieldsMap parameters where key is column name in database and value is corresponding value
     * @return Order if there was found matching order, null otherwise
     */
    public static Order getFirstOrderByFields(LinkedHashMap<String, String> fieldsMap) throws SQLException {
        List<Order> orders = getOrderListByFields(fieldsMap);
        if (orders.isEmpty()) return null;
        return orders.get(0);
    }

    public static List<Order> getOrderListByFields(LinkedHashMap<String, String> fieldsMap) throws SQLException {
        //Building query with all parameters provided in map. Using StringBuilder to improve performance.
        StringBuilder orderQueryBuilder = new StringBuilder();
        orderQueryBuilder.append("SELECT * FROM Orders ")
                .append("LEFT OUTER JOIN OrderLines OL on Orders.orderNumber = OL.orderNumber ")
                .append("LEFT OUTER JOIN Products ON OL.productCode = Products.productCode ");
        if (fieldsMap.size() > 0) {
            orderQueryBuilder.append("WHERE ");
            fieldsMap.forEach((key, value) -> orderQueryBuilder.append(key).append(" = ? AND "));
            orderQueryBuilder.setLength(orderQueryBuilder.length() - 5);
        }


        String query = orderQueryBuilder.toString();
        String[] parameters = fieldsMap.values().toArray(new String[0]);


        OrderMapper mapper = new OrderMapper();
        List<Order> orders = DatabaseConnectionHandler.select(mapper, query, (Object[]) parameters);


        //Return null if no order matching given parameters was found.
        if (orders.isEmpty()) return orders;

        Order currentOrder = null;
        int currentOrderId = 0;
        List<Order> parsedOrders = new ArrayList<>();
        for (Order order : orders) {
            if (currentOrder != null && order.getOrderNumber() == currentOrderId) {
                currentOrder.getOrderLines().addAll(order.getOrderLines());
                continue;
            }
            currentOrder = order;
            currentOrderId = order.getOrderNumber();
            parsedOrders.add(currentOrder);
        }

        return parsedOrders;
    }

    /**
     * Insert a fresh new order into the Database
     *
     * @param user the user who the new order belongs to
     */
    public static void createOrder(User user) throws SQLException {
        String query = "INSERT INTO Orders (dateOrdered, status, totalPrice, userUUID) VALUES (?, ?, ?, ?)";


        DatabaseConnectionHandler.insert(
                query,
                null,
                "PENDING",
                0,
                user.getUuid()
        );

    }

    /**
     * Adds a new orderline into the database
     *
     * @param order the order this line is a part of
     */
    public static void createOrderLine(Order order, Product product) throws SQLException {
        String query = "INSERT INTO OrderLines (orderNumber, quantity, orderPrice, productCode) VALUES (?, ?, ?, ?)";

        DatabaseConnectionHandler.insert(
                query,
                order.getOrderNumber(),
                order.getProductQuantity(product),
                order.getOrderLinePrice(product),
                product.getProductCode()
        );

    }

    /**
     * Given an Order and Product, updates the orderLine in the dabatase
     * to match the quantity and totalprice of the Product in the Order
     */
    public static void updateOrderLineEntirely(Order order, OrderLine orderline) throws SQLException {
        String updateQuery = "UPDATE OrderLines SET quantity = ?, orderPrice = ? WHERE orderNumber = ? AND productCode = ?";

        DatabaseConnectionHandler.update(
                updateQuery,
                orderline.getQuantity(),
                orderline.getPrice(),
                order.getOrderNumber(),
                orderline.getProduct().getProductCode()
        );

    }


    public static void updateOrderTotalPrice(Order order) throws SQLException {
        String updateQuery = "UPDATE Orders SET totalPrice = ? WHERE orderNumber = ?";

        DatabaseConnectionHandler.update(
                updateQuery,
                order.getTotalPrice(),
                order.getOrderNumber()
        );

    }

    /**
     * update the entire order by specifying each column
     */
    public static void updateOrderEntirely(Order order) throws SQLException {
        String updateQuery = "UPDATE Orders SET dateOrdered = ?, status = ?, totalPrice = ? WHERE orderNumber = ?";

        DatabaseConnectionHandler.update(
                updateQuery,
                order.getDateOrdered(),
                order.getStatus().toString(),
                order.getTotalPrice(),
                order.getOrderNumber()
        );

    }

    /**
     * update every single orderlines price and quantity in db for this order
     */
    public static void updateAllOrderlinesEntirely(Order order) throws SQLException {
        for (OrderLine orderLine : order.getOrderLines()) {
            updateOrderLineEntirely(order, orderLine);
        }
    }

    /**
     * Removes given orderline from the database
     *
     * @param order
     * @param orderline
     */
    public static void deleteOrderline(Order order, OrderLine orderline) throws SQLException {
        String deleteQuery = "DELETE FROM OrderLines WHERE orderNumber = ? AND productCode = ?";
        DatabaseConnectionHandler.update(
                deleteQuery,
                order.getOrderNumber(),
                orderline.getProduct().getProductCode()
        );
    }

    public static List<Order> getAllOrders() throws SQLException {
        return getOrderListByFields(new LinkedHashMap<>());

//        String query = "SELECT * FROM Orders";
//        List<Order> orders;
//
//        try {
//            OrderMapper mapper = new OrderMapper();
//            orders = DatabaseConnectionHandler.select(mapper, query);
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return orders;
    }

//    public static void updateOrderStatus(Order order) throws SQLException {
//        String updateQuery = "UPDATE Orders SET status = ? WHERE orderNumber = ?";
//
//            DatabaseConnectionHandler.update(updateQuery, order.getStatus().toString(), order.getOrderNumber());
//
//    }

    public static void deleteOrder(Order order) throws SQLException {
        // First, delete order lines associated with the order
        String deleteOrderLinesQuery = "DELETE FROM OrderLines WHERE orderNumber = ?";
        DatabaseConnectionHandler.update(deleteOrderLinesQuery, order.getOrderNumber());

        // Then, delete the order
        String deleteOrderQuery = "DELETE FROM Orders WHERE orderNumber = ?";
        DatabaseConnectionHandler.update(deleteOrderQuery, order.getOrderNumber());

    }

    public static List<Order> getFulfilledOrders() throws SQLException {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("status", "FULFILLED");
        return getOrderListByFields(params);
    }

}
