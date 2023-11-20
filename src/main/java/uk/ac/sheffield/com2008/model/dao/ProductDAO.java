package uk.ac.sheffield.com2008.model.dao;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.mappers.ProductMapper;

import java.sql.SQLException;
import java.util.List;

public class ProductDAO {
    public static Product getProductByCode(String code) {
        return getProductByField("productCode", code);
    }

    public static List<Product> getAllProducts() {
        String query = "SELECT * FROM Products";
        List<Product> products;

        try {
            ProductMapper mapper = new ProductMapper();
            products = DatabaseConnectionHandler.select(mapper, query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    public static List<Product> getProductsByCodes(String... productCodes) {
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM Products WHERE productCode IN (");
        for (int i = 0; i < productCodes.length; i++) {
            stringBuilder.append("?, ");
        }
        stringBuilder.setLength(stringBuilder.length() - 2);
        stringBuilder.append(")");
        String query = stringBuilder.toString();

        try {
            ProductMapper mapper = new ProductMapper();
            return DatabaseConnectionHandler.select(mapper, query, (Object[]) productCodes);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets first product that satisfies query
     *
     * @param fieldName
     * @param value
     * @return
     */
    private static Product getProductByField(String fieldName, Object value) {
        String query = "SELECT * FROM Products WHERE " + fieldName + " = ?";

        List<Product> productList;
        try {
            ProductMapper mapper = new ProductMapper();
            productList = DatabaseConnectionHandler.select(mapper, query, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (productList.isEmpty()) return null;

        return productList.get(0);
    }

    public static List<Product> getProductsByCategory(String initialLetter){
        String query = "SELECT * FROM Products WHERE productCode LIKE ?";
        List<Product> productList;
        try {
            ProductMapper mapper = new ProductMapper();
            String categoryWildcard = initialLetter + "%";
            productList = DatabaseConnectionHandler.select(mapper, query, categoryWildcard);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //System.out.println("Query: " + query + " | Category: " + initialLetter);
        //System.out.println("Result: " + productList);
        return productList;
    }
}


