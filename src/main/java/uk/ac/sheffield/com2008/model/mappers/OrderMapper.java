package uk.ac.sheffield.com2008.model.mappers;

import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.entities.Order;
import uk.ac.sheffield.com2008.model.entities.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper implements RowMapper<Order> {
    /**
     * Maps a given resultSet of one row into an Order instance
     *
     * @param resultSet row in the query result
     * @return Order instance
     * @throws SQLException
     */
    public Order mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Order order = new Order(
                resultSet.getInt("orderNumber"),
                resultSet.getDate("dateOrdered"),
                resultSet.getFloat("totalPrice"),
                Order.Status.valueOf(resultSet.getString("status")),
                resultSet.getString("userUUID"));
        //If order is empty, just return what we have
        if (resultSet.getMetaData().getColumnCount() < 6 || resultSet.getString("OL.productCode") == null) return order;

        Product product = new ProductMapper().mapResultSetToEntity(resultSet);
        OrderLine orderLine = new OrderLine(
                resultSet.getFloat("orderPrice"),
                resultSet.getInt("quantity"),
                product
        );

        order.addOrderLine(orderLine);
        return order;
    }
}
