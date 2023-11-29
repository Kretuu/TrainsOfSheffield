package uk.ac.sheffield.com2008.model.dao;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.model.domain.data.ProductSetItem;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.products.ProductSet;
import uk.ac.sheffield.com2008.model.entities.products.TrainSet;
import uk.ac.sheffield.com2008.model.mappers.ProductMapper;
import uk.ac.sheffield.com2008.model.mappers.ProductSetItemMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public static Product getProductByCode(String code) throws SQLException {
        return getProductByField("productCode", code);
    }

    public static List<Product> getAllProducts() throws SQLException {
        String query = "SELECT * FROM Products";

        ProductMapper mapper = new ProductMapper();
        return DatabaseConnectionHandler.select(mapper, query);
    }

    public static List<Product> getProductsByCodes(String... productCodes) throws SQLException {
        if (productCodes.length < 1) return new ArrayList<>();

        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM Products WHERE productCode IN (");
        for (int i = 0; i < productCodes.length; i++) {
            stringBuilder.append("?, ");
        }
        stringBuilder.setLength(stringBuilder.length() - 2);
        stringBuilder.append(")");
        String query = stringBuilder.toString();

        ProductMapper mapper = new ProductMapper();
        return DatabaseConnectionHandler.select(mapper, query, (Object[]) productCodes);
    }

    /**
     * Gets first product that satisfies query
     *
     * @param fieldName
     * @param value
     * @return
     */
    private static Product getProductByField(String fieldName, Object value) throws SQLException {
        String query = "SELECT * FROM Products WHERE " + fieldName + " = ?";

        ProductMapper mapper = new ProductMapper();
        List<Product> productList = DatabaseConnectionHandler.select(mapper, query, value);

        if (productList.isEmpty()) return null;

        return productList.get(0);
    }

    public static List<Product> getProductsByCategory(String initialLetter) throws SQLException {
        String query = "SELECT * FROM Products WHERE productCode LIKE ?";
        ProductMapper mapper = new ProductMapper();
        String categoryWildcard = initialLetter + "%";
        return DatabaseConnectionHandler.select(mapper, query, categoryWildcard);
    }


    //TODO: Creating a new product that is "isSet" means also creating a new ProductSet db row

    public static List<ProductSetItem> getProductSetItems(ProductSet productSet) throws SQLException {
        StringBuilder setItemsQueryBuilder = new StringBuilder();
        setItemsQueryBuilder.append("SELECT * FROM ProductSets ")
                .append("LEFT OUTER JOIN ProductSetItems PSI on ProductSets.setId = PSI.setId ")
                .append("LEFT OUTER JOIN Products ON PSI.productCode = Products.productCode ")
                .append("WHERE ProductSets.productCode = ?");

        String query = setItemsQueryBuilder.toString();

        ProductSetItemMapper mapper = new ProductSetItemMapper();
        return DatabaseConnectionHandler.select(mapper, query, productSet.getProductCode());
    }

    public static void updateProductStocks(Product product, int quantity) throws SQLException {
        String updateQuery = "UPDATE Products SET stock = ? WHERE productCode = ?";
        DatabaseConnectionHandler.update(updateQuery, quantity, product.getProductCode());
    }


    /**
     * Insert product into database
     * @param product said product
     */
    public static void createProduct(Product product){
        if(product.isSet() && product instanceof ProductSet){
            ProductSet set = (ProductSet) product;
            // create a new set linking to this product code
            // with this products set name

            //create new set items that link to this set id
            System.out.println("will create trainset of name:" + set.getName());
            System.out.println("set name:" + set.getSetName());
            return;
        }


        String insertQuery = "INSERT INTO Products (productCode, name, price, gauge, brand, isSet, stock)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            DatabaseConnectionHandler.insert(
                    insertQuery,
                    product.getProductCode(),
                    product.getName(),
                    product.getPrice(),
                    product.getGauge().toString(),
                    product.getBrand(),
                    product.isSet(),
                    product.getStock()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}


