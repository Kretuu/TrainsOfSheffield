package uk.ac.sheffield.com2008.model.mappers;

import uk.ac.sheffield.com2008.model.domain.data.ProductSetItem;
import uk.ac.sheffield.com2008.model.entities.products.ProductSet;
import uk.ac.sheffield.com2008.model.entities.products.TrackPack;
import uk.ac.sheffield.com2008.model.entities.products.TrainSet;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductSetMapper implements RowMapper<ProductSet> {
    @Override
    public ProductSet mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        ProductSetItemMapper productSetItemMapper = new ProductSetItemMapper();
        String productCode = resultSet.getString("PS.productCode");
        long setId = resultSet.getLong("PS.setId");

        ResultSetMetaData metaData = resultSet.getMetaData();
        List<String> columnNames = new ArrayList<>();
        for(int i = 1; i <= metaData.getColumnCount(); i++) {
           columnNames.add(metaData.getColumnName(i));
        }
        System.out.println(columnNames);


        ProductSetItem productSetItem = columnNames.stream().anyMatch(name -> name.equals("name"))
                ? productSetItemMapper.mapResultSetToEntity(resultSet) : null;
        switch (productCode.charAt(0)) {
            case 'M' -> {
                return new TrainSet(productCode, setId, productSetItem);
            }
            case 'P' -> {
                return new TrackPack(productCode, setId, productSetItem);
            }
        }


        return null;
    }
}
