package uk.ac.sheffield.com2008.model.entities.products;

import uk.ac.sheffield.com2008.model.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class Track extends Product {

    private String descriptor; //eg Single, Double, Left-Hand Crossover, etc..
    public enum TrackType {STRAIGHT, CURVE};
    private TrackType trackType;
    public Track(String productCode,
                 String name,
                 float price,
                 Gauge gauge,
                 String brand,
                 boolean isSet,
                 int stock,
                 String descriptor,
                 TrackType trackType) {
        super(productCode, name, price, gauge, brand, isSet, stock);
        this.descriptor = descriptor;
        this.trackType = trackType;
    }

    /**
     * returns the presentable way of displaying a Locomotive
     * Used for PRINTING the name out in the GUI
     * @return
     */
    public String printName(){
        return getProductCode() + ", " + descriptor;
    }

    /**
     * returns the name that should go in the database
     * @return
     */
    public String deriveName(){
        return descriptor + trackType;
    }

    /**
     * Takes the database name
     * @param name name in database format (e.g "3rd Radius Single Curve,CURVE")
     * @return list of objects that match the field needed for this class in the constructor
     */
    public static List<Object> parseName(String name){
        String[] nameAttributes = name.split(",");
        List<Object> output = new ArrayList<>();

        output.add(nameAttributes[0]);
        output.add(TrackType.valueOf(nameAttributes[1]));
        return output;
    }
}
