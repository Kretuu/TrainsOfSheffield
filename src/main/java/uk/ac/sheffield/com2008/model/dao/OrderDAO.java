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
     * @param user
     * @return
     */
    public static Order getUsersBasket(User user){
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        parameters.put("userUUID", user.getUuid());
        parameters.put("status", "PENDING");

        return getFirstOrderByFields(parameters);
    }

    public static List<Order> getUserOrdersHistory(User user) {
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        parameters.put("userUUID", user.getUuid());

        return getOrderListByFields(parameters);
    }


    /**
     * Get order matching given parameters.
     * @param fieldsMap parameters where key is column name in database and value is corresponding value
     * @return Order if there was found matching order, null otherwise
     */
    private static Order getFirstOrderByFields(LinkedHashMap<String, String> fieldsMap) {
        List<Order> orders = getOrderListByFields(fieldsMap);
        if(orders.isEmpty()) return null;
        return orders.get(0);
    }

    private static List<Order> getOrderListByFields(LinkedHashMap<String, String> fieldsMap) {
        //Building query with all parameters provided in map. Using StringBuilder to improve performance.
        StringBuilder orderQueryBuilder = new StringBuilder();
        orderQueryBuilder.append("SELECT * FROM Orders ")
                .append("LEFT OUTER JOIN OrderLines OL on Orders.orderNumber = OL.orderNumber ")
                .append("LEFT OUTER JOIN Products ON OL.productCode = Products.productCode ")
                .append("WHERE ");
        fieldsMap.forEach((key, value) -> orderQueryBuilder.append(key).append(" = ? AND "));
        orderQueryBuilder.setLength(orderQueryBuilder.length() - 5);

        String query = orderQueryBuilder.toString();
        String[] parameters = fieldsMap.values().toArray(new String[0]);


        List<Order> orders;
        try {
            OrderMapper mapper = new OrderMapper();
            orders = DatabaseConnectionHandler.select(mapper, query, (Object[]) parameters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("orders");
        System.out.println(orders);

        //Return null if no order matching given parameters was found.
        if(orders.isEmpty()) return orders;

        Order currentOrder = null;
        int currentOrderId = 0;
        List<Order> parsedOrders = new ArrayList<>();
        for(Order order : orders) {
            if(currentOrder != null && order.getOrderNumber() == currentOrderId) {
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
     * @param user the user who the new order belongs to
     */
    public static void createOrder(User user){
        String query = "INSERT INTO Orders (dateOrdered, status, totalPrice, userUUID) VALUES (?, ?, ?, ?)";

        try {
            DatabaseConnectionHandler.insert(
                    query,
                    null,
                    "PENDING",
                    0,
                    user.getUuid()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new orderline into the database
     * @param order the order this line is a part of
     */
    public static void createOrderLine(Order order, Product product){
        String query = "INSERT INTO OrderLines (orderNumber, quantity, orderPrice, productCode) VALUES (?, ?, ?, ?)";
        try {
            DatabaseConnectionHandler.insert(
                    query,
                    order.getOrderNumber(),
                    order.getProductQuantity(product),
                    order.getOrderLinePrice(product),
                    product.getProductCode()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Given an Order and Product, updates the orderLine in the dabatase
     * to match the quantity and totalprice of the Product in the Order
     */
    public static void updateOrderLineEntirely(Order order, OrderLine orderline){
        String updateQuery = "UPDATE OrderLines SET quantity = ?, orderPrice = ? WHERE orderNumber = ? AND productCode = ?";
        try {
            DatabaseConnectionHandler.update(
                    updateQuery,
                    orderline.getQuantity(),
                    orderline.getPrice(),
                    order.getOrderNumber(),
                    orderline.getProduct().getProductCode()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void updateOrderTotalPrice(Order order){
        String updateQuery = "UPDATE Orders SET totalPrice = ? WHERE orderNumber = ?";
        try {
            DatabaseConnectionHandler.update(
                    updateQuery,
                    order.getTotalPrice(),
                    order.getOrderNumber()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * update the entire order by specifying each column
     */
    public static void updateOrderEntirely(Order order){
        String updateQuery = "UPDATE Orders SET dateOrdered = ?, status = ?, totalPrice = ? WHERE orderNumber = ?";
        try {
            DatabaseConnectionHandler.update(
                    updateQuery,
                    order.getDateOrdered(),
                    order.getStatus().toString(),
                    order.getTotalPrice(),
                    order.getOrderNumber()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * update every single orderlines price and quantity in db for this order
     */
    public static void updateAllOrderlinesEntirely(Order order){
        for(OrderLine orderLine : order.getOrderLines()){
            updateOrderLineEntirely(order, orderLine);
        }
    }

    /**
     * Removes given orderline from the database
     * @param order
     * @param orderline
     */
    public static void deleteOrderline(Order order, OrderLine orderline){
        String deleteQuery = "DELETE FROM OrderLines WHERE orderNumber = ? AND productCode = ?";
        try {
            DatabaseConnectionHandler.update(
                    deleteQuery,
                    order.getOrderNumber(),
                    orderline.getProduct().getProductCode()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Order> getAllOrders() {
        String query = "SELECT * FROM Orders";
        List<Order> orders;

        try {
            OrderMapper mapper = new OrderMapper();
            orders = DatabaseConnectionHandler.select(mapper, query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }

}
