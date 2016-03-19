package utils;

import javafx.scene.control.TextField;

public class NumberUtils {

    public static boolean isDouble(TextField input, String message) {
        if (input != null) {
            try {
                double d = Double.parseDouble(input.getText());
            } catch (NumberFormatException e) {
                System.err.println("Error: '" + message + "' is not a number");
                return false;
            }
            return true;
        } else {
            throw new IllegalArgumentException("You don't initialize input = null");
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
