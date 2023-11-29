package uk.ac.sheffield.com2008.model.mappers;

import uk.ac.sheffield.com2008.model.domain.data.ProductSetItem;
import uk.ac.sheffield.com2008.model.entities.products.ProductSet;
import uk.ac.sheffield.com2008.model.entities.products.TrackPack;
import uk.ac.sheffield.com2008.model.entities.products.TrainSet;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductSetMapper implements RowMapper<ProductSet> {
    @Override
    public ProductSet mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        ProductSetItemMapper productSetItemMapper = new ProductSetItemMapper();
        String productCode = resultSet.getString("PS.productCode");
        long setId = resultSet.getLong("PS.setId");

        if(resultSet.getString("name") != null) {
            ProductSetItem productSetItem = productSetItemMapper.mapResultSetToEntity(resultSet);
            switch (productCode.charAt(0)) {
                case 'M' -> {
                    return new TrainSet(productCode, setId, productSetItem);
                }
                case 'P' -> {
                    return new TrackPack(productCode, setId, productSetItem);
                }
            }
        }

        return null;
    }
}
