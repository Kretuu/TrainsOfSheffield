package uk.ac.sheffield.com2008.model.dao;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.model.mappers.OrderMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDAO {

    /**
     * The basket is equivalent to a given users pending order
     * @param user
     * @return
     */
    public static Order getUsersBasket(User user){
        String query = "SELECT * FROM Orders WHERE userUUID = ? " + " AND status = ?";
        Order order = null;

        try {
            ResultSet resultSet = DatabaseConnectionHandler.select(query, user.getUuid(), "PENDING");
            if(resultSet.next()){
                order = OrderMapper.mapResultSetToOrder(resultSet);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        String query = "INSERT INTO Orders (dateOrdered, status, totalPrice, userUUID) VALUES (?, ?, ?, ?, ?)";

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

}
