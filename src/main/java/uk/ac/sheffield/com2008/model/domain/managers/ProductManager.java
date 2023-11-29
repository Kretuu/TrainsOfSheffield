package uk.ac.sheffield.com2008.model.domain.managers;

import uk.ac.sheffield.com2008.exceptions.InvalidProductQuantityException;
import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.domain.data.OrderLine;
import uk.ac.sheffield.com2008.model.domain.data.ProductSetItem;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.products.ProductSet;
import uk.ac.sheffield.com2008.model.entities.products.Track;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductManager {
    /**
     * Pass in a list of product set items you want.
     * This function will verify that what you are doing is legal for this object,
     * then push a Product into the database configured as a TrainSet
     */
    public static void createTrainSet(
            String productCode,
            String name,
            float price,
            Product.Gauge gauge,
            String brand,
            boolean isSet,
            int stock,
            String setName,
            ArrayList<ProductSetItem> setItems
    ){

        //need 1+ Locomotives

        //needs 1+  RollingStock

        // needs 1 controller

        //needs 1 starter oval set

        //needs 0+ extension track packs
    }


    /**
     * Pass in a list of product set items (prod->quantity) you want.
     * This function will verify that what you are doing is legal for this object,
     * then push a Product into the database configured as a TrackPack
     */
    public static void createExtensionTrackPack(
            String productCode,
            String name,
            float price,
            Product.Gauge gauge,
            String brand,
            boolean isSet,
            int stock,
            String setName,
            ArrayList<ProductSetItem> setItems
    ){
        //check if every item passed in is of type Track
        for (ProductSetItem psi : setItems) {
            if(!(psi.getProduct() instanceof Track)){
                throw new RuntimeException("Tried adding a Product not of type Track to an ExtensionTrackPack");
            }
        }

        //check if this set will contain more than just 1 item
        int itemCount = 0;
        for (ProductSetItem psi : setItems) {
            itemCount += psi.getQuantity();
        }
        if(itemCount < 2){throw new RuntimeException("Tried creating extension track pack with less than 2 items");}

        /*TODO: Create it using PRODUCTDAO.
        will need to make new ProductSet, and ProductSetItems
         */
    }

    /**
     *Dont know if this event needs to be a thing? Wouldnt we already have the
     * 3 types of StarterOvals in the database?
     */
    public static void createStarterOvalTrackPack(
            String productCode,
            String name,
            float price,
            Product.Gauge gauge,
            String brand,
            boolean isSet,
            int stock,
            String setName,
            ArrayList<ProductSetItem> setItems
    ){

    }

    public static List<ProductSetItem> fetchProductSetItems(ProductSet productSet) throws SQLException {
        return ProductDAO.getProductSetItems(productSet);
    }

    public static void updateStock(List<OrderLine> orderLines) throws SQLException, InvalidProductQuantityException {
        String[] productCodes = orderLines.stream()
                .map(orderLine -> orderLine.getProduct().getProductCode()).toArray(String[]::new);
        Map<String, OrderLine> productMap = orderLines.stream()
                .collect(Collectors.toMap(o -> o.getProduct().getProductCode(), o -> o));
        List<Product> databaseProducts = ProductDAO.getProductsByCodes(productCodes);

        for(Product dbProduct : databaseProducts) {
            int currentStock = dbProduct.getStock() - productMap.get(dbProduct.getProductCode()).getQuantity();
            if(currentStock < 0) throw new InvalidProductQuantityException();
            ProductDAO.updateProductStocks(dbProduct, currentStock);
        }

    }

}
