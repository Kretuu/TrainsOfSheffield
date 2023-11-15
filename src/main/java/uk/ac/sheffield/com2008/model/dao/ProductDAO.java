package uk.ac.sheffield.com2008.model.dao;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.User;
import uk.ac.sheffield.com2008.model.mappers.ProductMapper;
import uk.ac.sheffield.com2008.model.mappers.UserMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public static Product getProductByCode(String code) {
        return getProductByField("productCode", code);
    }

    public static ArrayList<Product> getAllProducts(){
        String query = "SELECT * FROM Products";
        ArrayList<Product> products = new ArrayList<>();

        try {
            ResultSet resultSet = DatabaseConnectionHandler.select(query);
            while(resultSet.next()){
                products.add(ProductMapper.mapResultSetToProduct(resultSet));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    /**
     * Gets first product that satisfies query
     * @param fieldName
     * @param value
     * @return
     */
    private static Product getProductByField(String fieldName, Object value) {
        String query = "SELECT * FROM Products WHERE " + fieldName + " = ?";
        Product product = null;

        try {
            ResultSet resultSet = DatabaseConnectionHandler.select(query, value);
            if(resultSet.next()){
                product = ProductMapper.mapResultSetToProduct(resultSet);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return product;
    }
}
