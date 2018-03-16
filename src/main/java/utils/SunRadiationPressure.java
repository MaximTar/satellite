package utils;

import model.Satellite;
import model.StateVector;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Maxim Tarasov on 14.08.2016.
 */
public class SunRadiationPressure {

    // TODO Check P
    public static double[] force(double A, double C, double P, double as, Calendar c) {

        List<Double> sunVector = SunPosition.sunPosition(c);
        double[] r = new double[]{sunVector.get(0), sunVector.get(1), sunVector.get(2)};

        return VectorAlgebra.constMult(-C * P * A * Math.pow(as / VectorAlgebra.absoluteValue(r), 2), VectorAlgebra.normalize(r));
    }

    public static double[] force(double A, double C, Calendar c) {

        double as = 149597870660.0; //in meters
        double P = 4.56E-6; //in Pa (N/m^2)

        List<Double> sunVector = SunPosition.sunPosition(c);
        double[] r = new double[]{sunVector.get(0), sunVector.get(1), sunVector.get(2)};

        return VectorAlgebra.constMult(-C * P * A * Math.pow(as / VectorAlgebra.absoluteValue(r), 2), VectorAlgebra.normalize(r));
    }

    public static boolean sunShadowSign(Calendar c, Satellite satellite, double earthR) {
        return earthBetween(c, satellite) && satelliteBehind(c, satellite, earthR);
    }

    public static boolean sunShadowSign(Calendar c, StateVector stateVector, double earthR) {
        return earthBetween(c, stateVector) && satelliteBehind(c, stateVector, earthR);
    }

    public static boolean earthBetween(Calendar c, Satellite satellite) {

        List<Double> sunVector = SunPosition.sunPosition(c);
        double[] rSun = new double[]{sunVector.get(0), sunVector.get(1), sunVector.get(2)};
        double[] rSat = new double[]{satellite.stateVector.x, satellite.stateVector.y, satellite.stateVector.z};

        double cos = VectorAlgebra.multS(rSun, rSat) / (VectorAlgebra.absoluteValue(rSun) * VectorAlgebra.absoluteValue(rSat));

        return cos < 0;
    }

    public static boolean earthBetween(Calendar c, StateVector stateVector) {

        List<Double> sunVector = SunPosition.sunPosition(c);
        double[] rSun = new double[]{sunVector.get(0), sunVector.get(1), sunVector.get(2)};
        double[] rSat = new double[]{stateVector.x, stateVector.y, stateVector.z};

        double cos = VectorAlgebra.multS(rSun, rSat) / (VectorAlgebra.absoluteValue(rSun) * VectorAlgebra.absoluteValue(rSat));

        return cos < 0;
    }

    public static boolean satelliteBehind(Calendar c, Satellite satellite, double earthR) {

        List<Double> sunVector = SunPosition.sunPosition(c);
        double[] rSun = new double[]{sunVector.get(0), sunVector.get(1), sunVector.get(2)};
        double[] rSat = new double[]{satellite.stateVector.x, satellite.stateVector.y, satellite.stateVector.z};

        double distance = VectorAlgebra.absoluteValue(VectorAlgebra.multV(rSat, rSun)) / VectorAlgebra.absoluteValue(rSun);

        return distance < earthR;
    }

    public static boolean satelliteBehind(Calendar c, StateVector stateVector, double earthR) {

        List<Double> sunVector = SunPosition.sunPosition(c);
        double[] rSun = new double[]{sunVector.get(0), sunVector.get(1), sunVector.get(2)};
        double[] rSat = new double[]{stateVector.x, stateVector.y, stateVector.z};

        double distance = VectorAlgebra.absoluteValue(VectorAlgebra.multV(rSat, rSun)) / VectorAlgebra.absoluteValue(rSun);

        return distance < earthR;
    }
}
