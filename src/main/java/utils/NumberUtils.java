package main.java.utils;

import javafx.scene.control.TextField;

public class NumberUtils {

    public static boolean isDouble(TextField input, String message) {
        if (input != null) {
            try {
                final double d = Double.parseDouble(input.getText());
            } catch (NumberFormatException e) {
                System.err.println("Error: '" + message + "' is not a number");
                return false;
            }
            return true;
        } else {
            throw new IllegalArgumentException("You don't initialize input = null");
        }
    }

    public static boolean textFieldsAreDouble(TextField input1, TextField input2, TextField input3, TextField input4,
                                              TextField input5, TextField input6, TextField input7, TextField input8,
                                              TextField input9) {
        return NumberUtils.isDouble(input1, input1.getText()) && NumberUtils.isDouble(input2, input2.getText())
                && NumberUtils.isDouble(input3, input3.getText()) && NumberUtils.isDouble(input4, input4.getText())
                && NumberUtils.isDouble(input5, input5.getText()) && NumberUtils.isDouble(input6, input6.getText())
                && NumberUtils.isDouble(input7, input7.getText()) && NumberUtils.isDouble(input8, input8.getText())
                && NumberUtils.isDouble(input9, input9.getText());
    }

    public static double parseTextAsDouble (TextField input) {
        return Double.parseDouble(input.getText());
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
