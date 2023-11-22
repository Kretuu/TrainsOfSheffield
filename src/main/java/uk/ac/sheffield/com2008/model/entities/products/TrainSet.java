package uk.ac.sheffield.com2008.model.entities.products;

import uk.ac.sheffield.com2008.model.domain.data.ProductSetItem;

import java.util.ArrayList;

public class TrainSet extends ProductSet{
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
}
