package uk.ac.sheffield.com2008.config;

import java.util.HashMap;

public class Symbols {
    private static HashMap<String, String> encodings = new HashMap<>();

    static{
        encodings.put("£", "\u00A3");
    }

    /**
     * @return raw char value of special character
     */
    public static String getChar(String c){
        if(!encodings.containsKey(c)){
            throw new RuntimeException("character is not defined. Consider addding it to config.Symbols.java");
        }
        return encodings.get(c);
    }
}
