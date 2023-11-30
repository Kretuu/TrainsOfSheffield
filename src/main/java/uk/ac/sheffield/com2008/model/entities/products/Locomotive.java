package uk.ac.sheffield.com2008.model.entities.products;

import uk.ac.sheffield.com2008.model.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class Locomotive extends Product {

    private String brClass; //e.g 01, 02, A3
    private String individualName; //e.g "The Flying Scotsman"
    private int era; // ERA 1-11
    private DCCType dccType;

    public Locomotive(
            String productCode,
            String name,
            float price,
            Gauge gauge,
            String brand,
            boolean isSet,
            int stock,
            String brClass,
            String individualName,
            int era,
            DCCType dccType) {
        super(productCode, name, price, gauge, brand, isSet, stock);
        this.brClass = brClass;
        this.individualName = individualName;
        this.era = era;
        this.dccType = dccType;
    }

    /**
     * Takes the database name
     *
     * @param name name in database format (e.g "A3,'The Flying Scotsman',6,DCCFITTED")
     * @return list of objects that match the field needed for this class in the constructor
     */
    public static List<Object> parseName(String name) {
        String[] nameAttributes = name.split(",");
        List<Object> output = new ArrayList<>();

        output.add(nameAttributes[0]);
        output.add(nameAttributes[1].equals("NULL") ? null : nameAttributes[1]);
        output.add(Integer.parseInt(nameAttributes[2]));
        output.add(Locomotive.DCCType.valueOf(nameAttributes[3]));
        return output;
    }

    /**
     * returns the presentable way of displaying a Locomotive
     * Used for PRINTING the name out in the GUI
     *
     * @return
     */
    public String printName() {
        String printOut = getProductCode() + ", Class " + brClass + ", ";
        if (individualName != null) {
            printOut += individualName + " ";
        }
        printOut += " - Era " + era + ", " + dccTypePrintable(dccType);
        return printOut;
    }

    private String dccTypePrintable(DCCType dccType) {
        return switch (dccType) {
            case ANALOGUE -> "Analogue";
            case DCCREADY -> "DCC-Ready";
            case DCCFITTED -> "DCC-Fitted";
            case DCCSOUND -> "DCC-Sound";
        };
    }

    /**
     * returns the name that should go in the database
     *
     * @return
     */
    public String deriveName() {
        String output = brClass + ",";
        if (individualName != null) {
            output += individualName + ",";
        } else {
            output += "NULL,";
        }
        output += era + "," + DCCType.valueOf(String.valueOf(dccType));
        return output;
    }

    public String getBrClass() {
        return brClass;
    }

    public void setBrClass(String brClass) {
        this.brClass = brClass;
    }

    public String getIndividualName() {
        return individualName;
    }

    public void setIndividualName(String individualName) {
        this.individualName = individualName;
    }

    public int getEra() {
        return era;
    }

    public void setEra(int era) {
        this.era = era;
    }

    public DCCType getDccType() {
        return dccType;
    }

    public void setDccType(DCCType dccType) {
        this.dccType = dccType;
    }

    public enum DCCType {
        ANALOGUE("Analogue"),
        DCCREADY("DCC-Ready"),
        DCCFITTED("DCC-Fitted"),
        DCCSOUND("DCC-Sound");

        private final String name;

        DCCType(String name) {
            this.name = name;
        }

        public static DCCType deriveType(String name) {
            for (DCCType d : DCCType.values()) {
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
