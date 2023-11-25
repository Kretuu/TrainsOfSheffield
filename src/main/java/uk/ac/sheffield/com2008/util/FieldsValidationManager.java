package uk.ac.sheffield.com2008.util;

public class FieldsValidationManager {
    public static String validatePassword(String text) {
        if(text.length() < 8 || PatternMatcher.matchRegex("^(?=.*\s).+$", text))
            return "At least 8 characters with no space";
        if(!PatternMatcher.matchRegex("^(?=.*[A-Z]).+$", text))
            return "At least 1 upper case letter";
        if(!PatternMatcher.matchRegex("^(?=.*[a-z]).+$", text))
            return "At least 1 lower case letter";
        if(!PatternMatcher.matchRegex("^(?=.*[-+_!@#$%^&*., ?]).+$", text))
            return "At least 1 special character";
        return null;
    }

    public static String validateEmail(String text) {
        if(PatternMatcher.matchRegex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", text)) {
            return null;
        }
        return "Invalid email address";
    }

    public static String validateConfirmPassword(String pass1, String pass2) {
        if(pass1.equals(pass2)) return null;
        return "Passwords do not match";
    }

    public static String validateHouseNo(String text) {
        if(ValidationManager.isStringNumeric(text)) {
            return null;
        }

        return "House number must be a number";
    }

    public static String validatePostcode(String text) {
        if(text.length() < 8 && 4 < text.length()
                && PatternMatcher.matchRegex("^[A-Z]{1,2}[0-9]{1,2}[A-Z]{0,1}\s[0-9][A-Z]{2}$", text)
        )
            return null;

        return "Postcode is invalid";
    }

    public static String validateTown(String text) {
        if(PatternMatcher.matchRegex("^[A-Za-z]{3,}$", text))
            return null;

        return "Town is invalid";
    }

    public static String validateSecurityCode(String text) {
        if(PatternMatcher.matchRegex("^[0-9]{3,4}$", text)) {
            return null;
        }
        return "Security code is invalid";
    }

    public static String validateBankingCard(String text) {
        String normalisedText = text.replaceAll("\\s", "");
        if(!ValidationManager.isStringNumeric(normalisedText)) return "Card number can contain only numbers";
        double cardNumber = Double.parseDouble(normalisedText);

        if(ValidationManager.isBankingCardValid(cardNumber)) return null;
        return "Banking card number is not valid";
    }
}
