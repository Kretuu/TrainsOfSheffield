package uk.ac.sheffield.com2008.model.entities.products;

import uk.ac.sheffield.com2008.model.domain.data.ProductSetItem;
import uk.ac.sheffield.com2008.model.entities.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * a ProductSet will be loaded initially as empty. If you want to retrieve
 * the products it contains, use the ProductManager.
 */
public abstract class ProductSet extends Product {

    //protected int setId;
    protected String setName;
    private long setId;
    protected List<ProductSetItem> setItems;

    protected ProductSet(
            String productCode,
            String name,
            float price,
            Gauge gauge,
            String brand,
            boolean isSet,
            int stock,
            String setName,
            List<ProductSetItem> setItems) {
        super(productCode, name, price, gauge, brand, isSet, stock);
        this.setName = setName;
        this.setItems = setItems;
    }

    protected ProductSet(String productCode, long setId, ProductSetItem productSetItem) {
        super(productCode);
        this.setId = setId;
        this.setItems = new ArrayList<>() {{ add(productSetItem); }};
    }

    /**
     * returns the presentable way of displaying a Locomotive
     * Used for PRINTING the name out in the GUI
     * @return
     */
    public String printName(){
        return getProductCode() + ", " + setName;
    }

    /**
     * returns the name that should go in the database
     * @return
     */
    public String deriveName(){
        return setName;
    }

    /**
     * Takes the database name
     * @param name name in database format (e.g "The Mega Track Pack,EXTENSION")
     * @return list of objects that match the field needed for this class in the constructor
     */
    public static List<Object> parseName(String name){
        String[] nameAttributes = name.split(",");
        List<Object> output = new ArrayList<>();

        output.add(nameAttributes[0]);
        return output;
    }


    public void setSetItems(List<ProductSetItem> setItems){
        this.setItems = setItems;
    }

    public List<ProductSetItem> getSetItems() {
        return this.setItems;
    }

    public String getSetName(){
        return setName;
    }

    public abstract String validateSet();

    public long getSetId(){
        return setId;
    }

    public void setSetName(String setName){
        this.setName = setName;
    }
}
