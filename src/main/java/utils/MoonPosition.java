package main.java.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by Maxim Tarasov on 14.08.2016.
 * This class calculates the geocentric equatorial position vector for the moon given the julian date.
 * Outputs: ijk position vector of moon (er), right ascension (rad), declination (rad).
 */
public class MoonPosition {

    public static double timeInJC(Calendar c) {
        double JD = julianDate(c) - 2451545.0;
        return JD / 36525.0;
    }

    public static double julianDate(Calendar c) {
        double Y = c.get(Calendar.YEAR);
        double M = c.get(Calendar.MONTH) + 1;
        double D = c.get(Calendar.DAY_OF_MONTH);
        double H = c.get(Calendar.HOUR_OF_DAY);
        if (M <= 2) {
            Y = Y - 1;
            M = M + 12;
        }
        double JY = 365.25 * Y;
        double JM = 30.6001 * (M + 1);

        return (int) JY + (int) JM + D + H / 24 + 1720981.5;
    }

    public static ArrayList<Double> moonPosition(Calendar c) {

        ArrayList<Double> result = new ArrayList<>();
        double T = timeInJC(c);
        double deg2rad = Math.PI / 180;
        double ER = 6371000.;

        double eclipticLongitude = 218.32 + 481267.8813 * T
                + 6.29 * Math.sin((134.9 + 477198.85 * T) * deg2rad)
                - 1.27 * Math.sin((259.2 - 413335.38 * T) * deg2rad)
                + 0.66 * Math.sin((235.7 + 890534.23 * T) * deg2rad)
                + 0.21 * Math.sin((269.9 + 954397.70 * T) * deg2rad)
                - 0.19 * Math.sin((357.5 + 35999.050 * T) * deg2rad)
                - 0.11 * Math.sin((186.6 + 966404.05 * T) * deg2rad);

        double eclipticLatitude = 5.13 * Math.sin((93.3 + 483202.030 * T) * deg2rad)
                + 0.28 * Math.sin((228.2 + 960400.87 * T) * deg2rad)
                - 0.28 * Math.sin((318.3 + 6003.1800 * T) * deg2rad)
                - 0.17 * Math.sin((217.6 - 407332.20 * T) * deg2rad);

        double horizontalParallax = 0.9508 + 0.0518 * Math.cos((134.9 + 477198.85 * T) * deg2rad)
                + 0.0095 * Math.cos((259.2 - 413335.38 * T) * deg2rad)
                + 0.0078 * Math.cos((235.7 + 890534.23 * T) * deg2rad)
                + 0.0028 * Math.cos((269.9 + 954397.70 * T) * deg2rad);

        eclipticLongitude = (eclipticLongitude * deg2rad) % (2 * Math.PI);
        eclipticLatitude = (eclipticLatitude * deg2rad) % (2 * Math.PI);
        horizontalParallax = (horizontalParallax * deg2rad) % (2 * Math.PI);


        double obliquity = (23.439291 - 0.0130042 * T) * deg2rad;

        // find the geocentric direction cosines
        double l = Math.cos(eclipticLatitude) * Math.cos(eclipticLongitude);
        double m = Math.cos(obliquity) * Math.cos(eclipticLatitude) * Math.sin(eclipticLongitude)
                - Math.sin(obliquity) * Math.sin(eclipticLatitude);
        double n = Math.sin(obliquity) * Math.cos(eclipticLatitude) * Math.sin(eclipticLongitude)
                + Math.cos(obliquity) * Math.sin(eclipticLatitude);

        // find magnitude of moon vector, calculate moon position vector components
        double vectorMagnitude = 1.0 / Math.sin(horizontalParallax);
        double moonVector1 = vectorMagnitude * l * ER;
        double moonVector2 = vectorMagnitude * m * ER;
        double moonVector3 = vectorMagnitude * n * ER;

        // find rt ascension and declination
        double rightAscension = Math.atan2(m, l);
        double declination = Math.asin(n);

        Collections.addAll(result, moonVector1, moonVector2, moonVector3, rightAscension, declination);
        return result;
    }

}
