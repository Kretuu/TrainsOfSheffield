package uk.ac.sheffield.com2008.model.domain.managers;

import uk.ac.sheffield.com2008.model.dao.ProductDAO;
import uk.ac.sheffield.com2008.model.domain.data.ProductSetItem;
import uk.ac.sheffield.com2008.model.entities.Product;
import uk.ac.sheffield.com2008.model.entities.products.ProductSet;
import uk.ac.sheffield.com2008.model.entities.products.Track;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductManager {

    /**
     * Pass in a hashmap of products->quantities you want.
     * This function will verify that what you are doing is legal for this object,
     * then return a TrackPack
     * @return a TrackPack that is configured as an Extension Track Pack
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
            HashMap<Product, Integer> setItems
    ){
        //check if every item passed in is of type Track
        for (Product key : setItems.keySet()) {
            if(!(key instanceof Track)){
                throw new RuntimeException("Tried adding a Product not of type Track to an ExtensionTrackPack");
            }
        }

        //check if this set will contain more than just 1 item
        int itemCount = 0;
        for (int value : setItems.values()) {
            itemCount += value;
        }
        if(itemCount < 2){throw new RuntimeException("Tried creating extension track pack with less than 2 items");}

        //TODO: Create it using ORDERDAO
    }

    public static ArrayList<ProductSetItem> fetchProductSetItems(ProductSet productSet){
        return (ArrayList<ProductSetItem>) ProductDAO.getProductSetItems(productSet);
    }
}
