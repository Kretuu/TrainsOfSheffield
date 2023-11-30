package uk.ac.sheffield.com2008.model.entities.products;

import uk.ac.sheffield.com2008.model.domain.data.ProductSetItem;
import uk.ac.sheffield.com2008.model.entities.Product;

import java.util.ArrayList;

public class TrainSet extends ProductSet {
    public TrainSet(
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

    public TrainSet(String productCode, long setId, ProductSetItem productSetItem) {
        super(productCode, setId, productSetItem);
    }

    public String validateSet() {
        String errMsg = "<html><p>A Train Set must contain:</p>\n" +
                "<ul>\n" +
                "  <li>1+ Locomotives</li>\n" +
                "  <li>1+ Rolling Stock</li>\n" +
                "  <li>1 Controller</li>\n" +
                "  <li>1+ Track Packs</li>\n" +
                "</ul></html>";

        boolean haslocomotive = false;
        boolean hasRollingStock = false;
        boolean hasController = false;
        boolean hasTrackPack = false;

        for (ProductSetItem setItem : setItems) {
            Product product = setItem.getProduct();
            if (product instanceof Locomotive) {
                haslocomotive = true;
            }
            if (product instanceof RollingStock) {
                hasRollingStock = true;
            }
            if (product instanceof Controller) {
                hasController = true;
                if (setItem.getQuantity() > 1) {
                    return errMsg;
                }
            }
            if(product instanceof TrackPack){
                hasTrackPack = true;
            }

            if (product instanceof Track) {
                return errMsg;
            }
        }

        if (!(haslocomotive && hasController && hasRollingStock && hasTrackPack)) {
            return errMsg;
        }

        return null;
    }
}
