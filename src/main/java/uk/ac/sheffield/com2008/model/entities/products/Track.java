package uk.ac.sheffield.com2008.model.entities.products;

import uk.ac.sheffield.com2008.model.entities.Product;

public class Track extends Product {

    public enum TrackType {STRAIGHT, CURVED};
    public Track(String productCode,
                 String name,
                 float price,
                 Gauge gauge,
                 String brand,
                 boolean isSet,
                 int stock) {
        super(productCode, name, price, gauge, brand, isSet, stock);
    }
}
