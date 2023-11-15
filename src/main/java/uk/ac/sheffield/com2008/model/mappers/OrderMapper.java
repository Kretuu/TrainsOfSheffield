package uk.ac.sheffield.com2008.model.mappers;

import uk.ac.sheffield.com2008.model.entities.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper {
    /**
     * Maps a given resultSet of one row into an Order instance
     * @param resultSet row in the query result
     * @return Order instance
     * @throws SQLException
     */
    public static Order mapResultSetToOrder(ResultSet resultSet) throws SQLException {
        Order order = null;
        Order.Status status = Order.Status.valueOf(resultSet.getString("status"));
        order = new Order(
                resultSet.getInt("orderNumber"),
                resultSet.getDate("dateOrdered"),
                resultSet.getFloat("totalPrice"),
                status);

        return order;
    }
}
