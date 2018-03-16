package utils;

import javafx.scene.control.TextField;

import java.util.Objects;

public class NumberUtils {

    public static boolean textFieldsAreDouble(TextField... inputs) {
        for (TextField input : inputs) {
            if (Objects.isNull(input) || !org.apache.commons.lang3.math.NumberUtils.isParsable(input.getText())) {
                return false;
            }
        }

        return true;
    }

    public static double parseTextAsDouble(TextField input) {
        return Double.parseDouble(input.getText());
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static double checkResultAngle(double angle) {
        if (angle <= 0) {
            while (angle <= 0) {
                angle += 360;
            }
        } else if (angle >= 360) {
            while (angle >= 360) {
                angle -= 360;
            }
        }
        return angle;
    }

    public static double d2r(double deg) {
        return Math.toRadians(deg);
    }

    public static double r2d(double rad) {
        return Math.toDegrees(rad);
    }

//    public static double pointLineDistance ()
}
