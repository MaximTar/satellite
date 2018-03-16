package utils;

import model.Satellite;
import model.StateVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Maxim Tarasov on 14.08.2016.
 * This class calculates the atmospheric drag force for body given its height, cross section and velocity.
 * Function exponentialModelDensity outputs atmospheric layer density using piecewise-exponential density model.
 * Function force outputs drag force value in N.
 */
public class Drag {

    public static double force(double c, double ro, double v, double s) {
        return (c * ro * v * v * s) / 2;
    }

    public static List<Double> force(double c, double ro, Satellite satellite, List<Double> v, double s) {
        List<Double> result = new ArrayList<>();
        StateVector state = satellite.stateVector;
        Collections.addAll(result, (c * ro * (state.vx - v.get(0)) * (state.vx - v.get(0)) * s) / 2,
                (c * ro * (state.vy - v.get(1)) * (state.vy - v.get(1)) * s) / 2,
                (c * ro * (state.vz - v.get(2)) * (state.vz - v.get(2)) * s) / 2);
        return result;
    }

//    public static double force(double c, double ro, List<Double> v, double s) {
//        return (c * ro * s * Math.pow(VectorAlgebra.absoluteValue(v), 2)) / 2;
//    }

//    public static List<Double> force_v(double c, double ro, Satellite satellite, List<Double> v, double s) {
//        List<Double> v_u = new ArrayList<>(Arrays.asList(satellite.stateVector.vx, satellite.stateVector.vy, satellite.stateVector.vz));
//        List<Double> v_ref = VectorAlgebra.difference(v_u, v);
//        double mid = (c * ro * s * VectorAlgebra.absoluteValue(v_ref) * VectorAlgebra.absoluteValue(v_ref)) / 2;
//        return VectorAlgebra.constMult(mid, VectorAlgebra.normalize(v_ref));
//    }
//
//    public static List<Double> force_div_m(double c, double ro, Satellite satellite, List<Double> v, double s) {
//        List<Double> v_u = new ArrayList<>(Arrays.asList(satellite.stateVector.vx, satellite.stateVector.vy, satellite.stateVector.vz));
//        List<Double> v_ref = VectorAlgebra.difference(v_u, v);
//        double mid = (c * ro * s * VectorAlgebra.absoluteValue(v_ref) * VectorAlgebra.absoluteValue(v_ref)) / (2 * satellite.m);
//        return VectorAlgebra.constMult(mid, VectorAlgebra.normalize(v_ref));
//    }

//    public static List<Double> force_div_m(double c, double ro, StateVector stateVector, List<Double> v, double s, double mass) {
//        List<Double> v_u = new ArrayList<>(Arrays.asList(stateVector.vx, stateVector.vy, stateVector.vz));
//        List<Double> v_ref = VectorAlgebra.difference(v_u, v);
//        double mid = (c * ro * s * VectorAlgebra.absoluteValue(v_ref) * VectorAlgebra.absoluteValue(v_ref)) / (2 * mass);
//        return VectorAlgebra.constMult(mid, VectorAlgebra.normalize(v_ref));
//    }

    public static double[] force_div_m(double c, double ro, StateVector stateVector, double[] v, double s, double mass) {
        double[] v_u = new double[]{stateVector.vx, stateVector.vy, stateVector.vz};
        double[] v_ref = VectorAlgebra.difference(v_u, v);
        double mid = (c * ro * s * VectorAlgebra.absoluteValue(v_ref) * VectorAlgebra.absoluteValue(v_ref)) / (2 * mass);
        return VectorAlgebra.constMult(mid, VectorAlgebra.normalize(v_ref));
    }

//      public static List<Double> force_div_m_center(double ro, StateVector stateVector, List<Double> v,
//                                                  double bbOne, double bbTwo, double massOne, double massTwo) {
//        List<Double> v_u = new ArrayList<>(Arrays.asList(stateVector.vx, stateVector.vy, stateVector.vz));
//        List<Double> v_ref = VectorAlgebra.difference(v_u, v);
//        double mid = 0.5 * (((massOne / bbOne) + (massTwo / bbTwo)) * ro * VectorAlgebra.absoluteValue(v_ref) * VectorAlgebra.absoluteValue(v_ref)) / (massOne + massTwo);
//        return VectorAlgebra.constMult(mid, VectorAlgebra.normalize(v_ref));
//    }
    public static double[] force_div_m_center(double ro, StateVector stateVector, double[] v,
                                                   double bbOne, double bbTwo, double massOne, double massTwo) {
        double[] v_u = new double[]{stateVector.vx, stateVector.vy, stateVector.vz};
        double[] v_ref = VectorAlgebra.difference(v_u, v);
        double mid = 0.5 * (((massOne / bbOne) + (massTwo / bbTwo)) * ro * VectorAlgebra.absoluteValue(v_ref) * VectorAlgebra.absoluteValue(v_ref)) / (massOne + massTwo);
        return VectorAlgebra.constMult(mid, VectorAlgebra.normalize(v_ref));
    }

//    public static List<Double> force_div_m_relative(double ro, StateVector stateVector, List<Double> v, double mass,
//                                                    double bbOne, double bbTwo, double mStar) {
//        List<Double> v_u = new ArrayList<>(Arrays.asList(stateVector.vx, stateVector.vy, stateVector.vz));
//        List<Double> v_ref = VectorAlgebra.difference(v_u, v);
////        System.out.println("V_U = " + v_u);
////        System.out.println("V_REF = " + v_ref);
////        System.out.println("M STAR = " + mStar);
////        System.out.println("BB = " + bbOne + " " + bbTwo);
////        System.out.println("MASS = " + mass);
////        System.out.println("DIFF = " + (1.0 / bbOne - 1.0 / bbTwo));
//
////        double mid = (mStar / 2.0) * (1.0 / bbOne - 1.0 / bbTwo) * ro * VectorAlgebra.absoluteValue(v_ref) * VectorAlgebra.absoluteValue(v_ref) / mass;
//        double mid = (mStar / 2.0) * (1.0 / bbTwo - 1.0 / bbOne) * ro * VectorAlgebra.absoluteValue(v_ref) * VectorAlgebra.absoluteValue(v_ref) / mass;
//
////        System.out.println("MID = " + mid);
//        return VectorAlgebra.constMult(mid, VectorAlgebra.normalize(v_ref));
//    }

    public static double[] force_div_m_relative(double ro, StateVector stateVector, double[] v, double mass,
                                                     double bbOne, double bbTwo, double mStar) {
        double[] v_u = new double[]{stateVector.vx, stateVector.vy, stateVector.vz};
        double[] v_ref = VectorAlgebra.difference(v_u, v);

//        double mid = (mStar / 2.0) * (1.0 / bbOne - 1.0 / bbTwo) * ro * VectorAlgebra.absoluteValue(v_ref) * VectorAlgebra.absoluteValue(v_ref) / mass;
        double mid = (mStar / 2.0) * (1.0 / bbTwo - 1.0 / bbOne) * ro * VectorAlgebra.absoluteValue(v_ref) * VectorAlgebra.absoluteValue(v_ref) / mass;

        return VectorAlgebra.constMult(mid, VectorAlgebra.normalize(v_ref));
    }

    public static double exponentialModelDensity(double h) {
        // todo move it to the constants
        // that's not constants, there is just initialization
        double initialDensity = 0, ha = 0, heightScale = 0;

        // TODO Тут нужно что-то получше SOUT'а
        AltitudeLevel resultSet = AltitudeLevel.inInterval(h);
        if (resultSet.getBottomHeight() == 0 && resultSet.getHeightScale() == 0 && resultSet.getInitialDensity() == 0) {
            System.out.println("ERROR!");
        } else {
            initialDensity = resultSet.getInitialDensity();
            heightScale = resultSet.getHeightScale();
            ha = resultSet.getBottomHeight();
        }

        return initialDensity * Math.exp(((ha / 1000) - (h / 1000)) / heightScale);
    }

    public static double earthsRotation(double phi, double h) {
        //todo move it to the constants class
        double Re = 6378100.0;
        double Rp = 6356800.0;
        double w = 7.2921158553E-5;

        double rp2pow = Math.pow(Rp, 2);
        double re2pow = Math.pow(Re, 2);

        return ((Re * Rp) / (Math.sqrt(rp2pow + re2pow * Math.tan(phi) * Math.tan(phi))) +
                (rp2pow * h) / (Math.sqrt(rp2pow + re2pow * Math.tan(phi) * Math.tan(phi)))) * w;
    }
}
