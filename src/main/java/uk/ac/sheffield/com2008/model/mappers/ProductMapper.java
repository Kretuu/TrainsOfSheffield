package uk.ac.sheffield.com2008.model.mappers;

import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.products.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        //could refactor this so that each subclass has a function that takes the name
        //string and returns a list of objects?

        switch(productType){
            case 'L':{
                List<Object> parsedParams = Locomotive.parseName(name);
                return new Locomotive(productCode, name, price, gauge, brand, isSet, stock,
                        (String) parsedParams.get(0),
                        (String) parsedParams.get(1),
                        (int) parsedParams.get(2),
                        (Locomotive.DCCType) parsedParams.get(3));}
            case 'S':{
                List<Object> parsedParams = RollingStock.parseName(name);
                return new RollingStock(productCode, name, price, gauge, brand, isSet, stock,
                        (String) parsedParams.get(0),
                        (String) parsedParams.get(1),
                        (RollingStock.Class_) parsedParams.get(2),
                        (int) parsedParams.get(3));}
            case 'R':{
                List<Object> parsedParams = Track.parseName(name);
                return new Track(productCode, name, price, gauge, brand, isSet, stock,
                        (String) parsedParams.get(0),
                        (Track.TrackType) parsedParams.get(1));
            }
            case 'C':{
                List<Object> parsedParams = Controller.parseName(name);
                return new Controller(productCode, name, price, gauge, brand, isSet, stock,
                        (String) parsedParams.get(0),
                        (Controller.PowerType) parsedParams.get(1));
            }
            case 'M':{
                List<Object> parsedParams = TrainSet.parseName(name);
                return new TrainSet(productCode, name, price, gauge, brand, isSet, stock,
                        (String) parsedParams.get(0),
                        new ArrayList<>());
            }
            case 'P':{
                List<Object> parsedParams = TrackPack.parseName(name);
                TrackPack.TrackPackType type = TrackPack.TrackPackType.valueOf((String) parsedParams.get(1));
                return new TrackPack(productCode, name, price, gauge, brand, isSet, stock,
                        (String) parsedParams.get(0),
                        type,
                        new ArrayList<>());
            }
            default:
                throw new RuntimeException("Product read of invalid type (check the product codes)");

        }
    }
}
