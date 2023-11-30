package uk.ac.sheffield.com2008.util;

public class ValidationManager {
    public static boolean isBankingCardValid(double cardNumber) {
        String cardNumberString = String.valueOf(cardNumber);

        int sum = 0;
        boolean alternate = false;

        for (int i = cardNumberString.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumberString.charAt(i));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = digit - 9;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        return sum % 10 == 0;
    }

    public static boolean isStringNumeric(String text) {
        try {
            Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
