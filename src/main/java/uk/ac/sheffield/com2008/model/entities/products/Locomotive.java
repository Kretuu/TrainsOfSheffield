package uk.ac.sheffield.com2008.model.entities.products;

import uk.ac.sheffield.com2008.model.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class Locomotive extends Product {

    private String brClass; //e.g 01, 02, A3
    private String individualName; //e.g "The Flying Scotsman"
    private int era; // ERA 1-11
    public enum DCCType{ ANALOGUE, DCCREADY, DCCFITTED, DCCSOUND};
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
     * returns the presentable way of displaying a Locomotive
     * Used for PRINTING the name out in the GUI
     * @return
     */
    public String printName(){
        String printOut = getProductCode() + " " + getBrand() + ", Class " + brClass + ", ";
        if(individualName != null){
            printOut += individualName + " ";
        }
        printOut += " - Era " + era + ", " + dccTypePrintable(dccType);
        return printOut;
    }

    private String dccTypePrintable(DCCType dccType){
        return switch (dccType) {
            case ANALOGUE -> "Analogue";
            case DCCREADY -> "DCC-Ready";
            case DCCFITTED -> "DCC-Fitted";
            case DCCSOUND -> "DCC-Sound";
        };
    }

    /**
     * returns the name that should go in the database
     * @return
     */
    public String deriveName(){
        String output = brClass + ",";
        if(individualName != null){
            output += individualName = ",";
        }else{
            output += "NULL,";
        }
        output += era + "," + DCCType.valueOf(String.valueOf(dccType));
        return output;
    }

    /**
     * Takes the database name
     * @param name name in database format (e.g "A3,'The Flying Scotsman',6,DCCFITTED")
     * @return list of objects that match the field needed for this class in the constructor
     */
    public static List<Object> parseName(String name){
        String[] nameAttributes = name.split(",");
        List<Object> output = new ArrayList<>();

        output.add(nameAttributes[0]);
        output.add(nameAttributes[1].equals("NULL") ? null : nameAttributes[1]);
        output.add(Integer.parseInt(nameAttributes[2]));
        output.add(Locomotive.DCCType.valueOf(nameAttributes[3]));
        return output;
    }

}
