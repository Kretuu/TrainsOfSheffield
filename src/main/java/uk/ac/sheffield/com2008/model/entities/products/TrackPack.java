package uk.ac.sheffield.com2008.model.entities.products;

import uk.ac.sheffield.com2008.model.domain.data.ProductSetItem;
import uk.ac.sheffield.com2008.model.entities.Product;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is for where once we have our trackpacks configured as an Extension or StarterOval
 * we can manipulate them how we like.
 *
 * If we load a TrackPack from the Database, we assume it is already configured.
 */
public class TrackPack extends ProductSet {

    public TrackPack(
            String productCode,
            String name,
            float price,
            Gauge gauge,
            String brand,
            boolean isSet,
            int stock,
            String setName,
            ArrayList<ProductSetItem> setItems) {
        super(productCode, name, price, gauge, brand, isSet, stock, setName, setItems);
    }
}
