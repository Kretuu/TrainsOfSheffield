package uk.ac.sheffield.com2008.model.mappers;

import uk.ac.sheffield.com2008.model.domain.BankingCard;
import uk.ac.sheffield.com2008.model.entities.PersonalDetails;
import uk.ac.sheffield.com2008.model.entities.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper {
    public static Product mapResultSetToProduct(ResultSet resultSet) throws SQLException {
        Product product = null;
        Product.Gauge gauge = Product.Gauge.valueOf(resultSet.getString("Products.gauge"));
        product = new Product(
                resultSet.getString("productCode"),
                resultSet.getString("name"),
                resultSet.getFloat("price"),
                gauge,
                resultSet.getString("brand"),
                resultSet.getBoolean("isSet"),
                resultSet.getInt("stock"));

        return product;
    }
}
