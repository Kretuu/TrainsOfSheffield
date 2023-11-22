package uk.ac.sheffield.com2008.model.entities.products;

import uk.ac.sheffield.com2008.model.domain.data.ProductSetItem;

import java.util.ArrayList;

public class ExtensionTrackPack extends TrackPack{

    public ExtensionTrackPack(
            String productCode,
            String name,
            float price,
            Gauge gauge,
            String brand,
            boolean isSet,
            int stock,
            String setName,
            TrackPackType type,
            ArrayList<ProductSetItem> setItems
    ){
        super(productCode,name,price,gauge,brand,isSet,stock, setName, type, setItems);
    }
}
