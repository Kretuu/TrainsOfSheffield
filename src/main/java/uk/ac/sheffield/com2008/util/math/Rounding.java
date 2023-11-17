package uk.ac.sheffield.com2008.util.math;

public class Rounding {

    public static float roundToDecimalPlaces(float num, int places){
        if (places < 0) {
            throw new IllegalArgumentException("Decimal places cannot be negative");
        }

        float scale = (float) Math.pow(10, places);
        return Math.round(num * scale) / scale;
    }
}
