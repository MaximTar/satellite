//package main.java.utils;
//
//
//import java.util.Calendar;
//
//public class SolarCoordinates {
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
//    public static double sunsMeanLongitude(double T) {
//        return r2d(d2r(280.46645) + d2r(36000.76983) * T + d2r(0.0003032) * T * T);
//    }
//
//    public static double sunsMeanAnomaly(double T) {
//        return r2d(d2r(357.52910) + d2r(35999.05030) * T - d2r(0.0001559) * T * T - d2r(0.00000048) * T * T * T);
//    }
//
//    public static double earthsOrbitEccentrity(double T) {
//        return r2d(d2r(0.016708617) - d2r(0.000042037) * T - d2r(0.0000001236) * T * T);
//    }
//
//    public static double sunsCenterEquation(double T) {
//        double M = sunsMeanAnomaly(T);
//        return r2d((d2r(1.9146) - d2r(0.004817) * T - d2r(0.000014) * T * T) * Math.sin(d2r(M)) +
//                (d2r(0.019993) - d2r(0.000101) * T) * Math.sin(2 * d2r(M)) +
//                d2r(0.000290) * Math.sin(3 * d2r(M)));
//    }
//
//    public static double sunsTrueLongitude(double T) {
//        double L = sunsMeanLongitude(T);
//        double C = sunsCenterEquation(T);
//        return L + C;
//    }
//
//    public static double sunsTrueAnomaly(double T) {
//        double M = sunsMeanAnomaly(T);
//        double C = sunsCenterEquation(T);
//        return M + C;
//    }
//
//    public static double sunsRadiusVector(double T) {
//        double v = sunsTrueAnomaly(T);
//        double e = earthsOrbitEccentrity(T);
//        return 1.000001018 * (1 - e * e) / (1 + e * Math.cos(d2r(v)));
//    }
//
//    public static double longitude(double T) {
//        return r2d(d2r(125.04) - d2r(1934.136) * T);
//    }
//
//    public static double sunsApparentLongitude(double T) {
//        double theta = sunsTrueLongitude(T);
//        double omega = longitude(T);
//        return r2d(d2r(theta) - d2r(0.00569) - d2r(0.00478) * Math.sin(d2r(omega)));
//    }
//
//    public static double eclipticsMeanObliquity(double T) {
//        return r2d(d2r(23 + 0.433333 + 0.00583333 + 0.00012444444) - d2r(0.0130041667) * T - d2r(1.63888889E-7) * T * T
//                + d2r(3.021666667E-5) * T * T * T);
//    }
//
//    public static double sunsRightAscension(double T) {
//        double theta = d2r(sunsTrueLongitude(T));
//        double e = d2r(eclipticsMeanObliquity(T));
//        return r2d(Math.atan(Math.cos(e) * Math.sin(theta) / Math.cos(theta)));
//    }
//
//    public static double sunsRightDeclination(double T) {
//        double theta = d2r(sunsTrueLongitude(T));
//        double e = d2r(eclipticsMeanObliquity(T));
//        return r2d(Math.asin(Math.sin(e) * Math.sin(theta)));
//    }
//
//    public static double eclipticsMeanObliquityApparent(double T) {
//        double omega = d2r(longitude(T));
//        return r2d(d2r(23 + 0.433333 + 0.00583333 + 0.00012444444) - d2r(0.0130041667) * T - d2r(1.63888889E-7) * T * T
//                + d2r(3.021666667E-5) * T * T * T + d2r(0.00256) * Math.cos(omega));
//    }
//
//    public static double sunsRightAscensionApparent(double T) {
//        double lambda = d2r(sunsApparentLongitude(T));
//        double e = d2r(eclipticsMeanObliquityApparent(T));
////        return r2d(Math.atan(Math.cos(e) * Math.sin(lambda) / Math.cos(lambda)));
//        return r2d(Math.atan2(Math.cos(e) * Math.sin(lambda), Math.cos(lambda)));
//    }
//
//    public static double sunsRightDeclinationApparent(double T) {
//        double lambda = d2r(sunsApparentLongitude(T));
//        double e = d2r(eclipticsMeanObliquityApparent(T));
//        return r2d(Math.asin(Math.sin(e) * Math.sin(lambda)));
//    }
//
//
//    public static double d2r(double deg) {
//        return Math.toRadians(deg);
//    }
//
//    public static double r2d(double rad) {
//        return Math.toDegrees(rad);
//    }
//
//}
