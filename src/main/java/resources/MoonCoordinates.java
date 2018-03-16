//package main.java.utils;
//
//import java.util.Calendar;
//
//public class MoonCoordinates {
//
//    public static double timeInJC(Calendar c) {
//        double JD = julianDate(c) - 2451545.0;
//        return JD / 36525.0;
//    }
//
//    public static double julianDate(Calendar c) {
//        double Y = c.get(Calendar.YEAR);
//        double M = c.get(Calendar.MONTH) + 1;
//        double D = c.get(Calendar.DAY_OF_MONTH);
//        double H = c.get(Calendar.HOUR_OF_DAY);
//        if (M <= 2) {
//            Y = Y - 1;
//            M = M + 12;
//        }
//        double JY = 365.25 * Y;
//        double JM = 30.6001 * (M + 1);
//
//        return (int) JY + (int) JM + D + H / 24 + 1720981.5;
//    }
//
//    //TODO WTF?!
//    public static double moonsEquatorialHorizontalParallax(double delta) {
//        return Math.asin(6378.14 / delta);
//    }
//
//    public static double moonsMeanLongitude(double T) {
//        double result = 218.3164591 + 481267.88134236 * T - 0.0013268 * T * T + T * T * T / 538841 - T * T * T * T / 65194000;
//        result = NumberUtils.checkResultAngle(result);
//        return result;
//    }
//
//    public static double moonsMeanElongation(double T) {
//        double result = 297.8502042 + 445267.1115168 * T - 0.0016300 * T * T + T * T * T / 545868 - T * T * T * T / 113065000;
//        result = NumberUtils.checkResultAngle(result);
//        return result;
//    }
//
//    public static double sunsMeanAnomaly(double T) {
//        double result = 357.5291092 + 35999.0502909 * T - 0.0001536 * T * T + T * T * T / 24490000;
//        result = NumberUtils.checkResultAngle(result);
//        return result;
//    }
//
//    public static double moonsMeanAnomaly(double T) {
//        double result = 134.9634114 + 477198.8676313 * T + 0.0089970 * T * T + T * T * T / 69699 - T * T * T * T / 14712000;
//        result = NumberUtils.checkResultAngle(result);
//        return result;
//    }
//
//    // Mean distance of the Moon from its ascending node
//    public static double moonsLatitudeArgument(double T) {
//        double result = 93.2720993 + 483202.0175273 * T - 0.0034029 * T * T - T * T * T / 3526000 + T * T * T * T / 863310000;
//        result = NumberUtils.checkResultAngle(result);
//        return result;
//    }
//
//    public static double firstArgument(double T) {
//        double result = 119.75 + 131.849 * T;
//        result = NumberUtils.checkResultAngle(result);
//        return result;
//    }
//
//    public static double secondArgument(double T) {
//        double result = 53.09 + 479264.290 * T;
//        result = NumberUtils.checkResultAngle(result);
//        return result;
//    }
//
//    public static double thirdArgument(double T) {
//        double result = 313.45 + 481266.484 * T;
//        result = NumberUtils.checkResultAngle(result);
//        return result;
//    }
//
//    public static double earthsOrbitEccentricity(double T) {
//        return 1 - 0.002516 * T - 0.0000074 * T * T;
//    }
//
//    public static double d2r(double deg) {
//        return Math.toRadians(deg);
//    }
//
//    public static double r2d(double rad) {
//        return Math.toDegrees(rad);
//    }
//}
