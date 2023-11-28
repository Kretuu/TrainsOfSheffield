package uk.ac.sheffield.com2008.model.entities.products;

import uk.ac.sheffield.com2008.model.domain.data.ProductSetItem;
import uk.ac.sheffield.com2008.model.domain.managers.ProductManager;
import uk.ac.sheffield.com2008.model.entities.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * a ProductSet will be loaded initially as empty. If you want to retrieve
 * the products it contains, use the ProductManager.
 */
public abstract class ProductSet extends Product {

    //protected int setId;
    protected String setName;
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

    /**
     * Prints the contents of this product set
     */
    public void PrintFullSet() {
        System.out.println(setName + "CONTAINS: ");
        setSetItems(fetchContainedItems());
        setItems.forEach(setItem -> {
            Product product = setItem.getProduct();
            System.out.println("\t " + product.getProductCode() + " " + product.getName() + " Qty: " + setItem.getQuantity());
        });
    }

    public List<ProductSetItem> fetchContainedItems() {
        List<ProductSetItem> setItems = null;
        try {
            setItems = ProductManager.fetchProductSetItems(this);
        } catch (SQLException e) {
            //TODO
            throw new RuntimeException(e);
        }
        if(this.setItems.isEmpty()){
            setSetItems(setItems);
        }
        return setItems;
    }

    public void setSetItems(List<ProductSetItem> setItems){
        this.setItems = setItems;
    }

    public List<ProductSetItem> getSetItems() {
        if(this.setItems.isEmpty()){
            return fetchContainedItems();
        }
        PrintFullSet();
        return this.setItems;
    }
}
