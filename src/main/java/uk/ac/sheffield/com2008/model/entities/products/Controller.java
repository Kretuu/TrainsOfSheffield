package uk.ac.sheffield.com2008.model.entities.products;

import uk.ac.sheffield.com2008.model.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class Controller extends Product {
    private String descriptor;
    private PowerType powerType;
    public Controller(
            String productCode,
            String name,
            float price,
            Gauge gauge,
            String brand,
            boolean isSet,
            int stock,
            String descriptor,
            PowerType powerType) {
        super(productCode, name, price, gauge, brand, isSet, stock);
        this.descriptor = descriptor;
        this.powerType = powerType;
    }

    /**
     * Takes the database name
     *
     * @param name name in database format (e.g "DCC Controller,DIGITAL")
     * @return list of objects that match the field needed for this class in the constructor
     */
    public static List<Object> parseName(String name) {
        String[] nameAttributes = name.split(",");
        List<Object> output = new ArrayList<>();

        output.add(nameAttributes[0]);
        output.add(PowerType.valueOf(nameAttributes[1]));
        return output;
    }

    /**
     * returns the presentable way of displaying a Controller
     * Used for PRINTING the name out in the GUI
     *
     * @return
     */
    public String printName() {
        return getProductCode() + ", " + descriptor;
    }

    /**
     * returns the name that should go in the database
     *
     * @return
     */
    public String deriveName() {
        return descriptor + "," + powerType;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public PowerType getPowerType() {
        return powerType;
    }

    public void setPowerType(PowerType powerType) {
        this.powerType = powerType;
    }

    public enum PowerType {
        ANALOGUE("Analogue"),
        DIGITAL("Digital");

        private final String name;

        PowerType(String name) {
            this.name = name;
        }

        public static Controller.PowerType deriveType(String name) {
            for (Controller.PowerType d : Controller.PowerType.values()) {
                if (d.deriveName().equals(name)) {
                    return d;
                }
            }
            return null;
        }

        public String deriveName() {
            return this.name;
        }

    }
}
