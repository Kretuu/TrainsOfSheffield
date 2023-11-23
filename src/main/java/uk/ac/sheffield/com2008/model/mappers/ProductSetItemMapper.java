package uk.ac.sheffield.com2008.model.mappers;

import uk.ac.sheffield.com2008.model.domain.data.ProductSetItem;
import uk.ac.sheffield.com2008.model.entities.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductSetItemMapper implements RowMapper<ProductSetItem> {

    public ProductSetItem mapResultSetToEntity(ResultSet resultSet) throws SQLException {

        Product product = new ProductMapper().mapResultSetToEntity(resultSet);
        int quantity = resultSet.getInt("quantity");
        return new ProductSetItem(product, quantity);
    }
}
