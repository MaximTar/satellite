package main.java.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Maxim Tarasov on 14.08.2016.
 */
public class SunsRadiationPressure {

    // TODO Check P
    public static List<Double> force(double A, double C, double P, Calendar c) {

        double as = 149597870660.0; //in meters

        List<Double> sunVector = SunPosition.sunPosition(c);
        List<Double> r = new ArrayList<>();
        Collections.addAll(r, sunVector.get(0), sunVector.get(1), sunVector.get(2));

        return VectorsAlgebra.constMult(-C * P * A * Math.pow(as / VectorsAlgebra.absoluteValue(r), 2), VectorsAlgebra.normalize(r));
    }
}
