package uk.ac.sheffield.com2008.model.entities.products;

import uk.ac.sheffield.com2008.model.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class RollingStock extends Product {

    private String mark; //company or standard, (GWR,LNER, BR Mk1, BR Mk2, BR Mk3)
    private String kind; //Corridor, Open, Mail Van, Shisha Lounge
    public enum Class_{THIRD, SECOND, FIRST, STANDARD, NULL};
    private Class_ class_;
    private int era; //1-11

    public RollingStock(
            String productCode,
            String name,
            float price,
            Gauge gauge,
            String brand,
            boolean isSet,
            int stock,
            String mark,
            String kind,
            Class_ class_,
            int era) {
        super(productCode, name, price, gauge, brand, isSet, stock);
        this.mark = mark;
        this.kind = kind;
        this.class_ = class_;
        this.era = era;
    }

    /**
     * returns the presentable way of displaying a Locomotive
     * Used for PRINTING the name out in the GUI
     * @return
     */
    public String printName(){
        String printOut = getProductCode() + ", " + mark + ", " + kind;
        if(class_ != null){
            printOut += " " + classPrintable(class_);
        }
        printOut += " - Era " + era;
        return printOut;
    }

    private String classPrintable(Class_ class_){
        String classString = String.valueOf(class_);
        String firstLetter = classString.substring(0,1);
        String endLetters = classString.substring(1);
        endLetters = endLetters.toLowerCase();

        // join the two substrings
        classString = firstLetter + endLetters;
        return classString;
    }

    /**
     * returns the name that should go in the database
     * @return
     */
    public String deriveName(){
        String output = mark + ",";
        if(class_ != null){
            output += class_ + ",";
        }else{
            output += "NULL,";
        }
        output += era;
        return output;
    }

    /**
     * Takes the database name
     * @param name name in database format (e.g "LNER,First,Corridor,DCCFITTED")
     * @return list of objects that match the field needed for this class in the constructor
     */
    public static List<Object> parseName(String name){
        String[] nameAttributes = name.split(",");
        List<Object> output = new ArrayList<>();

        output.add(nameAttributes[0]);
        output.add(nameAttributes[1].equals("NULL") ? null : nameAttributes[1]);
        output.add(Class_.valueOf(nameAttributes[2]));
        output.add(Integer.parseInt(nameAttributes[3]));
        return output;
    }
}
