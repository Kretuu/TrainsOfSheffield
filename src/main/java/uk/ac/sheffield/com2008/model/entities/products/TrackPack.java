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

    /**
     * Ensures track pack is composed of correct items
     * @return errors or null
     */
    public String validateSet(){
        String errorMsg = null;

        if(trackPackType == TrackPackType.STARTER){
            errorMsg = "<html><p>Starter Oval must contain:</p>\n" +
                    "<ul>\n" +
                    "  <li>8 Same Curved Tracks</li>\n" +
                    "  <li>2 Same Straight Tracks</li>\n" +
                    "</ul></html>";

            //should contain 2 items.
            if(setItems.size() != 2){
                return errorMsg;
            }
            //confirm the curved and straight tracks amounts are correct
            int curvedSetItems = 0;
            int straightSetItems = 0;
            for (ProductSetItem setItem : setItems) {
                Product product = setItem.getProduct();
                if (product instanceof Track) {
                    Track track = (Track) product;
                    if (track.getTrackType() == Track.TrackType.CURVE) {
                        curvedSetItems++;
                        if(setItem.getQuantity()!= 8){
                            return errorMsg;
                        }
                    }
                    else{
                        straightSetItems++;
                        if(setItem.getQuantity()!= 2){
                            return errorMsg;
                        }
                    }
                }
                else{
                    return errorMsg;
                }

                if(curvedSetItems > 1 || straightSetItems > 1){
                    return errorMsg;
                }
            }

        }else{
            errorMsg = "<html><p>Extension Track Pack must contain:</p>\n" +
                    "<ul>\n" +
                    "  <li>2 or more Track pieces</li>\n" +
                    "</ul></html>";
            if(setItems.size() < 2){
                return errorMsg;
            }
            for (ProductSetItem setItem : setItems) {
                Product product = setItem.getProduct();
                if (!(product instanceof Track)) {
                    return errorMsg;
                }
            }
        }

        return null; //valid
    }
}
