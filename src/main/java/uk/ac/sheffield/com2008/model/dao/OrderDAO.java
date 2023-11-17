package uk.ac.sheffield.com2008.model.dao;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.model.mappers.OrderMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAO {

    /**
     * The basket is equivalent to a given users pending order
     * @param user
     * @return
     */
    public static Order getUsersBasket(User user){
        String query = "SELECT * FROM Orders WHERE userUUID = ? " + " AND status = ?";
        Order order = null;

        //Get basket itself
        try {
            ResultSet resultSet = DatabaseConnectionHandler.select(query, user.getUuid(), "PENDING");
            if(resultSet.next()){
                order = OrderMapper.mapResultSetToOrder(resultSet);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(order == null){
            return null;
        }

        //get orderlines of basket
        String orderlinesQuery = "SELECT * FROM OrderLines WHERE orderNumber = ?";
        try {
            ResultSet resultSet = DatabaseConnectionHandler.select(orderlinesQuery, order.getOrderNumber());
            while(resultSet.next()){

                //get product associated with orderline
                Product product = ProductDAO.getProductByCode(resultSet.getString("productCode"));
                order.addProduct(
                        product,
                        resultSet.getInt("quantity")
                );
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        order.PrintFullOrder();

        return order;
    }


    private static Order getOrderByField(String fieldName, Object value) {
        String query = "SELECT * FROM Orders WHERE " + fieldName + " = ?";
        Order order = null;

        try {
            ResultSet resultSet = DatabaseConnectionHandler.select(query, value);
            if(resultSet.next()){
                order = OrderMapper.mapResultSetToOrder(resultSet);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return order;
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
    public static void updateOrderLineQuantity(Order order, Product product){
        String updateQuery = "UPDATE OrderLines SET quantity = ?, orderPrice = ? WHERE orderNumber = ? AND productCode = ?";
        try {
            DatabaseConnectionHandler.update(
                    updateQuery,
                    order.getProductQuantity(product),
                    order.getOrderLinePrice(product),
                    order.getOrderNumber(),
                    product.getProductCode()
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

}
