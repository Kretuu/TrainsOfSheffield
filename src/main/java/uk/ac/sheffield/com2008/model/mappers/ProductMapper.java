package uk.ac.sheffield.com2008.model.mappers;

import uk.ac.sheffield.com2008.model.entities.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<Product> {
    public Product mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Product.Gauge gauge = Product.Gauge.valueOf(resultSet.getString("gauge"));
        return new Product(
                resultSet.getString("Products.productCode"),
                resultSet.getString("name"),
                resultSet.getFloat("price"),
                gauge,
                resultSet.getString("brand"),
                resultSet.getBoolean("isSet"),
                resultSet.getInt("stock"));
    }
}
