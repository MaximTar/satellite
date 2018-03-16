package utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by Maxim Tarasov on 14.08.2016.
 * This class calculates the geocentric equatorial position vector for the sun given the julian date.
 * Outputs: ijk position vector of sun (au), right ascension (rad), declination (rad).
 */
public class SunPosition {

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

        int A = (int) (Y / 100);
        double B = 2 - A + (int) (A / 4);

//        double JY = 365.25 * Y;
        double JY = 365.25 * (Y + 4716);
        double JM = 30.6001 * (M + 1);

//        return (int) JY + (int) JM + D + H / 24 + 1720981.5;
        return (int) JY + (int) JM + D + H / 24 + B - 1524.5;
    }

    public static ArrayList<Double> sunPosition(Calendar c) {

        ArrayList<Double> result = new ArrayList<>();
        double T = timeInJC(c);
        double deg2rad = Math.PI / 180;
        double AU = 149597870700.;

        double meanLongitude = 280.460 + 36000.77 * T;
        meanLongitude = meanLongitude % 360.0;

        double meanAnomaly = 357.5277233 + 35999.05034 * T;
        meanAnomaly = (meanAnomaly * deg2rad) % (2 * Math.PI);
        if (meanAnomaly < 0.0) {
            meanAnomaly = 2 * Math.PI + meanAnomaly;
        }

        double eclipticLongitude = meanLongitude + 1.914666471 * Math.sin(meanAnomaly)
                + 0.019994643 * Math.sin(2.0 * meanAnomaly);
        eclipticLongitude = eclipticLongitude % 360.0;

        double obliquity = 23.439291 - 0.0130042 * T;

        eclipticLongitude = eclipticLongitude * deg2rad;
        obliquity = obliquity * deg2rad;

        // find magnitude of sun vector, calculate sun position vector components
        double vectorMagnitude = 1.000140612 - 0.016708617 * Math.cos(meanAnomaly)
                - 0.000139589 * Math.cos(2.0 * meanAnomaly);
        double sunVector1 = vectorMagnitude * Math.cos(eclipticLongitude) * AU;
        double sunVector2 = vectorMagnitude * Math.cos(obliquity) * Math.sin(eclipticLongitude) * AU;
        double sunVector3 = vectorMagnitude * Math.sin(obliquity) * Math.sin(eclipticLongitude) * AU;

        double rightAscension = Math.atan(Math.cos(obliquity) * Math.tan(eclipticLongitude));

        // check that rightAscension is in the same quadrant as eclipticLongitude
        if (eclipticLongitude < 0.0) {
            eclipticLongitude = eclipticLongitude + 2 * Math.PI; // make sure it's in 0 to 2pi range
        }
        if (Math.abs(eclipticLongitude - rightAscension) > (Math.PI * 0.5)) {
            rightAscension = rightAscension + 0.5 * Math.PI * Math.round((eclipticLongitude - rightAscension) / (0.5 * Math.PI));
        }
        double declination = Math.asin(Math.sin(obliquity) * Math.sin(eclipticLongitude));

        Collections.addAll(result, sunVector1, sunVector2, sunVector3, rightAscension, declination);
        return result;
    }

}
