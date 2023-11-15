package uk.ac.sheffield.com2008.model.dao;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.model.mappers.OrderMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDAO {

    public static Order getUsersBasket(User user){
        return getOrderByField("userUUID", user.getUuid());
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

}
