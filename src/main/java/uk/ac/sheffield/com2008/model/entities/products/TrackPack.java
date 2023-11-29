package uk.ac.sheffield.com2008.model.entities.products;

import uk.ac.sheffield.com2008.model.domain.data.ProductSetItem;
import uk.ac.sheffield.com2008.model.entities.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is for where once we have our trackpacks configured as an Extension or StarterOval
 * we can manipulate them how we like.
 *
 * If we load a TrackPack from the Database, we assume it is already configured.
 */
public class TrackPack extends ProductSet {

    public enum TrackPackType {STARTER,EXTENSION};
    protected TrackPackType trackPackType;
    public TrackPack(
            String productCode,
            String name,
            float price,
            Gauge gauge,
            String brand,
            boolean isSet,
            int stock,
            String setName,
            TrackPackType type,
            ArrayList<ProductSetItem> setItems) {
        super(productCode, name, price, gauge, brand, isSet, stock, setName, setItems);
        this.trackPackType = type;
    }

    public String deriveName(){
        return setName + "," + trackPackType.toString();
    }

    /**
     * Takes the database name
     * @param name name in database format (e.g "Mega Cool Track Pack,EXTENSION")
     * @return list of objects that match the field needed for this class in the constructor
     */
    public static List<Object> parseName(String name){
        String[] nameAttributes = name.split(",");
        List<Object> output = new ArrayList<>();

        output.add(nameAttributes[0]);
        output.add(nameAttributes[1]);
        return output;
    }

}
