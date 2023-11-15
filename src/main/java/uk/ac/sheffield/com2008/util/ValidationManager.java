package uk.ac.sheffield.com2008.util;

public class ValidationManager {
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
}
