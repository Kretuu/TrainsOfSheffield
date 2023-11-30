package uk.ac.sheffield.com2008.model.dao;

import uk.ac.sheffield.com2008.database.DatabaseConnectionHandler;
import uk.ac.sheffield.com2008.model.domain.data.ProductSetItem;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.products.ProductSet;
import uk.ac.sheffield.com2008.model.mappers.ProductMapper;
import uk.ac.sheffield.com2008.model.mappers.ProductSetMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductDAO {
    public static Product getProductByCode(String code) throws SQLException {
        return getProductByField("productCode", code);
    }

    public static List<Product> getAllProducts() throws SQLException {
        String query = "SELECT * FROM Products WHERE isDiscontinued <> 1";

        ProductMapper mapper = new ProductMapper();
        return DatabaseConnectionHandler.select(mapper, query);
    }

    public static List<Product> getProductsByCodes(String... productCodes) throws SQLException {
        if (productCodes.length < 1) return new ArrayList<>();

        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM Products WHERE productCode IN (");
        stringBuilder.append("?, ".repeat(productCodes.length));
        stringBuilder.setLength(stringBuilder.length() - 2);
        stringBuilder.append(") AND isDiscontinued <> 1");
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
        String query = "SELECT * FROM Products WHERE " + fieldName + " = ? AND isDiscontinued <> 1";

        ProductMapper mapper = new ProductMapper();
        List<Product> productList = DatabaseConnectionHandler.select(mapper, query, value);

        if (productList.isEmpty()) return null;

        return productList.get(0);
    }

    public static List<Product> getProductsByCategory(String initialLetter) throws SQLException {
        String query = "SELECT * FROM Products WHERE productCode LIKE ? AND isDiscontinued <> 1";
        ProductMapper mapper = new ProductMapper();
        String categoryWildcard = initialLetter + "%";
        return DatabaseConnectionHandler.select(mapper, query, categoryWildcard);
    }

    public static void deleteProduct(Product product) throws SQLException {
        String query = "DELETE FROM Products WHERE productCode = ?";
        DatabaseConnectionHandler.delete(query, product.getProductCode());
    }

    public static boolean productExistInNonPendingOrders(Product product) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder()
                .append("SELECT COUNT(*) FROM Orders INNER JOIN OrderLines OL ON Orders.orderNumber = OL.orderNumber ")
                .append("INNER JOIN Products P ON OL.productCode = P.productCode WHERE Orders.status <> 'PENDING' ")
                .append("AND P.productCode = ?");
        String query = queryBuilder.toString();

        long result = DatabaseConnectionHandler.count(query, product.getProductCode());
        return result > 0;
    }

    public static void discontinueProduct(Product product) throws SQLException {
        String query = "UPDATE Products SET isDiscontinued = 1 WHERE productCode = ?";
        DatabaseConnectionHandler.update(query, product.getProductCode());
    }


    //TODO: Creating a new product that is "isSet" means also creating a new ProductSet db row
    public static List<ProductSet> fetchProductSetItems(List<ProductSet> productSets) throws SQLException {
        if(productSets.isEmpty()) return new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder()
                .append("SELECT * FROM ProductSets PS LEFT OUTER JOIN ProductSetItems PSI ON ")
                .append("PS.setId = PSI.setId LEFT OUTER JOIN Products ON PSI.productCode = Products.productCode ")
                .append("WHERE PS.productCode IN (");

        String[] productCodes = productSets.stream().map(ProductSet::getProductCode).toArray(String[]::new);
        queryBuilder.append("?, ".repeat(productSets.size()));
        queryBuilder.setLength(queryBuilder.length() - 2);
        queryBuilder.append(")");
        String query = queryBuilder.toString();

        ProductSetMapper mapper = new ProductSetMapper();
        List<ProductSet> productSetItems = DatabaseConnectionHandler.select(mapper, query, (Object[]) productCodes);

        if (productSetItems.isEmpty()) return productSets;
        Map<String, ProductSet> productSetMap = productSets.stream()
                .collect(Collectors.toMap(ProductSet::getProductCode, p -> p));

        ProductSet currentSet = null;
        String currentProductCode = "";
        List<ProductSet> parsedProductSets = new ArrayList<>();
        for (ProductSet productSet : productSetItems) {
            if (currentSet != null && productSet.getProductCode().equals(currentProductCode)) {
                currentSet.getSetItems().addAll(productSet.getSetItems());
                continue;
            }
            currentProductCode = productSet.getProductCode();
            currentSet = productSetMap.get(currentProductCode);
            currentSet.setSetItems(productSet.getSetItems());
            parsedProductSets.add(currentSet);
        }

        return parsedProductSets;
    }

    public static void updateProductStocks(Product product, int quantity) throws SQLException {
        String updateQuery = "UPDATE Products SET stock = ? WHERE productCode = ?";
        DatabaseConnectionHandler.update(updateQuery, quantity, product.getProductCode());
    }


    /**
     * Insert product into database
     * @param product said product
     */
    public static void createProduct(Product product) throws SQLException{

        String insertQuery = "INSERT INTO Products (productCode, name, price, gauge, brand, isSet, stock)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";
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


        if(product.isSet() && product instanceof ProductSet) {
            ProductSet set = (ProductSet) product;

            // create a new set linking to this product code
            String newSetQuery = "INSERT INTO ProductSets (productCode)" +
                    " VALUES (?)";
            DatabaseConnectionHandler.insert(
                    newSetQuery,
                    product.getProductCode());

            String reselectSetQuery = "SELECT * FROM ProductSets PS WHERE productCode = (?)";
            ProductSetMapper mapper = new ProductSetMapper();

            List<ProductSet> pSet = DatabaseConnectionHandler.select(
                    mapper,
                    reselectSetQuery,
                    product.getProductCode());
            if(pSet.isEmpty()) throw new RuntimeException();
            int setId = (int) pSet.get(0).getSetId();

            //create new set items that link to this set id
            ArrayList<ProductSetItem> setItems = (ArrayList<ProductSetItem>) set.getSetItems();
            StringBuilder newSetItemQuery = new StringBuilder()
                    .append("INSERT INTO ProductSetItems (setId, productCode, quantity) VALUES ");
            LinkedList<Object> params = new LinkedList<>();
            for (ProductSetItem setItem : setItems) {
                newSetItemQuery.append("(?, ?, ?), ");
                params.add(setId);
                params.add(setItem.getProduct().getProductCode());
                params.add(setItem.getQuantity());
            }
            newSetItemQuery.setLength(newSetItemQuery.length() - 2);

            DatabaseConnectionHandler.insert(
                    newSetItemQuery.toString(), params.toArray()
            );

        }
    }

    public static void updateProduct(Product product) throws SQLException{
        StringBuilder updateQueryBuilder = new StringBuilder();
        updateQueryBuilder.append("UPDATE Products SET ")
                .append("name = ?, ")
                .append("price = ?, ")
                .append("gauge = ?, ")
                .append("brand = ?, ")
                .append("isSet = ?, ")
                .append("stock = ? ")
                .append("WHERE productCode = ?");

        String updateQuery = updateQueryBuilder.toString();

        DatabaseConnectionHandler.update(
                updateQuery,
                product.getName(),
                product.getPrice(),
                product.getGauge().toString(),
                product.getBrand(),
                product.isSet(),
                product.getStock(),
                product.getProductCode()
        );


        if(product.isSet() && product instanceof ProductSet) {
            ProductSet set = (ProductSet) product;

            //get the set id associated with this product set
            String reselectSetQuery = "SELECT * FROM ProductSets PS WHERE productCode = (?)";
            ProductSetMapper mapper = new ProductSetMapper();

            List<ProductSet> pSet = DatabaseConnectionHandler.select(
                    mapper,
                    reselectSetQuery,
                    product.getProductCode());
            if (pSet.isEmpty()) throw new RuntimeException();
            int setId = (int) pSet.get(0).getSetId();

            //delete all set items associated to this set id currently in database
            String deleteAllSetItemsQuery = "DELETE FROM ProductSetItems WHERE setId = ?";
            DatabaseConnectionHandler.delete(
                    deleteAllSetItemsQuery,
                    setId
            );

            //create new set items that link to this set id
            ArrayList<ProductSetItem> setItems = (ArrayList<ProductSetItem>) set.getSetItems();
            StringBuilder newSetItemQuery = new StringBuilder()
                    .append("INSERT INTO ProductSetItems (setId, productCode, quantity) VALUES ");
            LinkedList<Object> params = new LinkedList<>();
            for (ProductSetItem setItem : setItems) {
                newSetItemQuery.append("(?, ?, ?), ");
                params.add(setId);
                params.add(setItem.getProduct().getProductCode());
                params.add(setItem.getQuantity());
            }
            newSetItemQuery.setLength(newSetItemQuery.length() - 2);

            DatabaseConnectionHandler.insert(
                    newSetItemQuery.toString(), params.toArray()
            );

        }
    }


}
