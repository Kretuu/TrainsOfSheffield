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

    // Method to get the initial letter based on the selected category
    public static String getInitialLetter(String selectedCategory) {
        if ("Locomotive".equals(selectedCategory)) {
            return "L";
        } else if ("Controller".equals(selectedCategory)) {
            return "C";
        } else if ("Rolling Stock".equals(selectedCategory)) {
            return "S";
        }else if ("Track".equals(selectedCategory)) {
            return "R";
        }else if ("Train Set".equals(selectedCategory)) {
            return "M";
        }else if ("Track Pack".equals(selectedCategory)) {
            return "P";
        } else {
            return "";
        }
    }
}
