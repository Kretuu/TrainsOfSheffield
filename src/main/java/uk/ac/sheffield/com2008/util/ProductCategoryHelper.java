package uk.ac.sheffield.com2008.util;

public class ProductCategoryHelper {
    public static String deriveCategory(String productCode) {
        // Check if the productCode starts with the letter 'L'
        if (productCode.startsWith("L")) {
            return "Locomotive";
        } else if (productCode.startsWith("C")) {
            return "Controller";
        } else if (productCode.startsWith("R")) {
            return "Track";
        } else if (productCode.startsWith("S")) {
            return "Rolling Stock";
        } else if (productCode.startsWith("M")) {
            return "Train Set";
        } else if (productCode.startsWith("P")) {
            return "Track Pack";
        } else {
            return "Other Category";
        }
    }
}
