package uk.ac.sheffield.com2008.model.mappers;

import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.products.Locomotive;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<Product> {
    public Product mapResultSetToEntity(ResultSet resultSet) throws SQLException {

        String productCode = resultSet.getString("Products.productCode");
        String name = resultSet.getString("name");
        float price = resultSet.getFloat("price");
        Product.Gauge gauge = Product.Gauge.valueOf(resultSet.getString("gauge"));
        String brand = resultSet.getString("brand");
        boolean isSet = resultSet.getBoolean("isSet");
        int stock = resultSet.getInt("stock");

        char productType = productCode.charAt(0);
        String[] nameAttributes = name.split(",");

        //could refactor this so that each subclass has a function that takes the name
        //string and returns a list of objects?

        switch(productType){
            case 'L':
                String brClass = nameAttributes[0];
                String individualName = nameAttributes[1].equals("NULL") ? null : nameAttributes[1];
                int era = Integer.parseInt(nameAttributes[2]);
                Locomotive.DCCType dccType = Locomotive.DCCType.valueOf(nameAttributes[3]);
                return new Locomotive(productCode, name, price, gauge, brand, isSet, stock,
                        brClass, individualName, era, dccType);
            default:
                return new Product(productCode, name, price, gauge, brand, isSet, stock);

        }
    }
}
