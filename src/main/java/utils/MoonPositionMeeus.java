package utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Maxim Tarasov on 18.01.2017.
 */

public class MoonPositionMeeus {
    // Pi, degrees, radians conversions
    public static double pi = Math.PI;
    public static double d2r = Math.PI / 180.0;

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

    // Gateway function ---------------------------------------------------------------

    public static List<Double> mexFunction(Calendar c) {
        double a = 1.0;  // default angle conversion factor
        double d = 1000.0;  // default distance conversion factor
        double ra, dec, delta, x, y, z;

        // Calculate the moon positions
        double T = timeInJC(c);  // Eqn. (21.1) pg 131, Julian Centuries from J2000

        List<Double> moonPosTod = moonpostod(T);
        ra = moonPosTod.get(0);
        dec = moonPosTod.get(1);
        delta = moonPosTod.get(2);

        delta *= d;  // Scale the distance output
        x = Math.cos(dec) * Math.cos(ra) * (delta);
        y = Math.cos(dec) * Math.sin(ra) * (delta);
        z = Math.sin(dec) * (delta);

        // user wants decimal output
        ra *= a;  // Scale the right ascension angle output

        // user wants decimal output
        dec *= a;  // Scale the declination angle output

        List<Double> result = new ArrayList<>();
        Collections.addAll(result, x, y, z, ra, dec, delta);
        return result;
    }

//--------------------------------------------------------------------------------------

    public static List<Double> moonpostod(double T)  // Moon Position TOD
    {
        double suml, sumr, sumb, lp, d, m, mp, f, a1, a2, a3, e, e2, arg, dpsi, deps,
                eps, lambda, beta, ra, dec, delta;

        // Argument Multiple of D column, page 309-310, Table 45.A
        double argd[] =
                {0, 2, 2, 0, 0, 0, 2, 2, 2, 2,
                        0, 1, 0, 2, 0, 0, 4, 0, 4, 2,
                        2, 1, 1, 2, 2, 4, 2, 0, 2, 2,
                        1, 2, 0, 0, 2, 2, 2, 4, 0, 3,
                        2, 4, 0, 2, 2, 2, 4, 0, 4, 1,
                        2, 0, 1, 3, 4, 2, 0, 1, 2, 2};

        // Argument Multiple of M column, page 309-310, Table 45.A
        double argm[] =
                {0, 0, 0, 0, 1, 0, 0, -1, 0, -1,
                        1, 0, 1, 0, 0, 0, 0, 0, 0, 1,
                        1, 0, 1, -1, 0, 0, 0, 1, 0, -1,
                        0, -2, 1, 2, -2, 0, 0, -1, 0, 0,
                        1, -1, 2, 2, 1, -1, 0, 0, -1, 0,
                        1, 0, 1, 0, 0, -1, 2, 1, 0, 0};

        // All the 1's and -1's of argm, used for M correction
        int argm1[] =
                {0, 0, 0, 0, 1, 0, 0, -1, 0, -1,
                        1, 0, 1, 0, 0, 0, 0, 0, 0, 1,
                        1, 0, 1, -1, 0, 0, 0, 1, 0, -1,
                        0, 0, 1, 0, 0, 0, 0, -1, 0, 0,
                        1, -1, 0, 0, 1, -1, 0, 0, -1, 0,
                        1, 0, 1, 0, 0, -1, 0, 1, 0, 0};

        // All the 2's and -2's of argm, used for 2M correction
        int argm2[] =
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, -2, 0, 2, -2, 0, 0, 0, 0, 0,
                        0, 0, 2, 2, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 2, 0, 0, 0};

        // Argument Multiple of M' column, page 309-310, Table 45.A
        double argmp[] =
                {1, -1, 0, 2, 0, 0, -2, -1, 1, 0,
                        -1, 0, 1, 0, 1, 1, -1, 3, -2, -1,
                        0, -1, 0, 1, 2, 0, -3, -2, -1, -2,
                        1, 0, 2, 0, -1, 1, 0, -1, 2, -1,
                        1, -2, -1, -1, -2, 0, 1, 4, 0, -2,
                        0, 2, 1, -2, -3, 2, 1, -1, 3, -1};

        // Argument Multiple of force column, page 309-310, Table 45.A
        double argf[] =
                {0, 0, 0, 0, 0, 2, 0, 0, 0, 0,
                        0, 0, 0, -2, 2, -2, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 2, 0,
                        0, 0, 0, 0, 0, -2, 2, 0, 2, 0,
                        0, 0, 0, 0, 0, -2, 0, 0, 0, 0,
                        -2, -2, 0, 0, 0, 0, 0, 0, 0, -2};

        // Coefficient of the sine of the argument column, page 309-310, Table 45.A
        double args[] =
                {6288774, 1274027, 658314, 213618, -185116,
                        -114332, 58793, 57066, 53322, 45758,
                        -40923, -34720, -30383, 15327, -12528,
                        10980, 10675, 10034, 8548, -7888,
                        -6766, -5163, 4987, 4036, 3994,
                        3861, 3665, -2689, -2602, 2390,
                        -2348, 2236, -2120, -2069, 2048,
                        -1773, -1595, 1215, -1110, -892,
                        -810, 759, -713, -700, 691,
                        596, 549, 537, 520, -487,
                        -399, -381, 351, -340, 330,
                        327, -323, 299, 294, 0};

        // Coefficient of the cosine of the argument column, page 309-310, Table 45.A
        double argc[] =
                {-20905355, -3699111, -2955968, -569925, 48888,
                        -3149, 246158, -152138, -170733, -204586,
                        -129620, 108743, 104755, 10321, 0,
                        79661, -34782, -23210, -21636, 24208,
                        30824, -8379, -16675, -12831, -10445,
                        -11650, 14403, -7003, 0, 10056,
                        6322, -9884, 5751, 0, -4950,
                        4130, 0, -3958, 0, 3258,
                        2616, -1897, -2117, 2354, 0,
                        0, -1423, -1117, -1571, -1739,
                        0, -4421, 0, 0, 0,
                        0, 1165, 0, 0, 8752};

        // Coefficient of the sine of the argument, page 311, Table 45.B
        double argb[] =
                {5128122, 280602, 277693, 173237, 55413,
                        46271, 32573, 17198, 9266, 8822,
                        8216, 4324, 4200, -3359, 2463,
                        2211, 2065, -1870, 1828, -1794,
                        -1749, -1565, -1491, -1475, -1410,
                        -1344, -1335, 1107, 1021, 833,
                        777, 671, 607, 596, 491,
                        -451, 439, 422, 421, -366,
                        -351, 331, 315, 302, -283,
                        -229, 223, 223, -220, -220,
                        -185, 181, -177, 176, 166,
                        -164, 132, -119, 115, 107};

        // Argument Multiple of D column, page 311, Table 45.B
        double bargd[] =
                {0, 0, 0, 2, 2, 2, 2, 0, 2, 0,
                        2, 2, 2, 2, 2, 2, 2, 0, 4, 0,
                        0, 0, 1, 0, 0, 0, 1, 0, 4, 4,
                        0, 4, 2, 2, 2, 2, 0, 2, 2, 2,
                        2, 4, 2, 2, 0, 2, 1, 1, 0, 2,
                        1, 2, 0, 4, 4, 1, 4, 1, 4, 2};

        // Argument Multiple of M column, page 311, Table 45.B
        double bargm[] =
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        -1, 0, 0, 1, -1, -1, -1, 1, 0, 1,
                        0, 1, 0, 1, 1, 1, 0, 0, 0, 0,
                        0, 0, 0, 0, -1, 0, 0, 0, 0, 1,
                        1, 0, -1, -2, 0, 1, 1, 1, 1, 1,
                        0, -1, 1, 0, -1, 0, 0, 0, -1, -2};

        // All the 1's and -1's of bargm, used for M correction
        double bargm1[] =
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        -1, 0, 0, 1, -1, -1, -1, 1, 0, 1,
                        0, 1, 0, 1, 1, 1, 0, 0, 0, 0,
                        0, 0, 0, 0, -1, 0, 0, 0, 0, 1,
                        1, 0, -1, 0, 0, 1, 1, 1, 1, 1,
                        0, -1, 1, 0, -1, 0, 0, 0, -1, 0};

        // All the 2's and -2's of bargm, used for 2M correction
        double bargm2[] =
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, -2, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, -2};

        // Argument Multiple of M' column, page 311, Table 45.B
        double bargmp[] =
                {0, 1, 1, 0, -1, -1, 0, 2, 1, 2,
                        0, -2, 1, 0, -1, 0, -1, -1, -1, 0,
                        0, -1, 0, 1, 1, 0, 0, 3, 0, -1,
                        1, -2, 0, 2, 1, -2, 3, 2, -3, -1,
                        0, 0, 1, 0, 1, 1, 0, 0, -2, -1,
                        1, -2, 2, -2, -1, 1, 1, -1, 0, 0};

        // Argument Multiple of force column, page 311, Table 45.B
        double bargf[] =
                {1, 1, -1, -1, 1, -1, 1, 1, -1, -1,
                        -1, -1, 1, -1, 1, 1, -1, -1, -1, 1,
                        3, 1, 1, 1, -1, -1, -1, 1, -1, 1,
                        -3, 1, -3, -1, -1, 1, -1, 1, -1, 1,
                        1, 1, 1, -1, 3, -1, -1, 1, -1, -1,
                        1, -1, 1, -1, -1, -1, -1, -1, -1, 1};

        // Find position in mean of date (MOD)
        lp = 218.3164591                 // Eqn. (45.1) pg 308
                + T * (481267.88134236
                + T * (-0.0013268
                + T * (1.0 / 538841.0
                + T * (-1.0 / 65194000.0))));
        lp = lp % 360.0;
        if (lp < 0.0) lp += 360.0;

        d = 297.8502042                  // Eqn. (45.2) pg 308
                + T * (445267.1115168
                + T * (-0.0016300
                + T * (1.0 / 545868.0
                + T * (-1.0 / 113065000))));
        d = d % 360.0;
        if (d < 0.0) d += 360.0;

        m = 357.5291092                  // Eqn. (45.3) pg 308
                + T * (35999.0502909
                + T * (-0.0001536
                + T * (1.0 / 24490000.0)));
        m = m % 360.0;
        if (m < 0.0) m += 360.0;

        mp = 134.9634114                 // Eqn. (45.4) pg 308
                + T * (477198.8676313
                + T * (0.0089970
                + T * (1.0 / 69699.0
                + T * (-1.0 / 14712000.0))));
        mp = (mp % 360.0);
        if (mp < 0.0) mp += 360.0;

        f = 93.2720993                   // Eqn. (45.5) pg 308
                + T * (483202.0175273
                + T * (-0.0034029
                + T * (-1.0 / 3526000.0
                + T * (1.0 / 863310000.0))));
        f = (f % 360.0);
        if (f < 0.0) f += 360.0;

        a1 = 119.75 + T * 131.849;       // Eqns. middle pg 308
        a2 = 53.09 + T * 479264.290;
        a3 = 313.45 + T * 481266.484;
        a1 = (a1 % 360.0);
        a2 = (a2 % 360.0);
        a3 = (a3 % 360.0);
        if (a1 < 0.0) a1 += 360.0;
        if (a2 < 0.0) a2 += 360.0;
        if (a3 < 0.0) a3 += 360.0;

        e = 1.0 + T * (-0.002516 - 0.0000074 * T);  // Eqn. (45.6) pg 308
        e2 = e * e;

        suml = 0.0;
        sumr = 0.0;
        sumb = 0.0;

        for (int i = 0; i < 60; i++) {                       // Sum of terms on pages 309 - 311
            arg = argd[i] * d + argm[i] * m + argmp[i] * mp + argf[i] * f;
            if (argm1[i] != 0) {                          // Correction for M
                suml += args[i] * sind(arg) * e;
                sumr += argc[i] * cosd(arg) * e;
            } else if (argm2[i] != 0) {                  // Correction for 2M
                suml += args[i] * sind(arg) * e2;
                sumr += argc[i] * cosd(arg) * e2;
            } else {
                suml += args[i] * sind(arg);
                sumr += argc[i] * cosd(arg);
            }
            arg = bargd[i] * d + bargm[i] * m + bargmp[i] * mp + bargf[i] * f;
            if (bargm1[i] != 0) {                          // Correction for M
                sumb += argb[i] * sind(arg) * e;
            } else if (bargm2[i] != 0) {                  // Correction for 2M
                sumb += argb[i] * sind(arg) * e2;
            } else {
                sumb += argb[i] * sind(arg);
            }
        }
        suml = suml + 3958.0 * sind(a1)               // Eqn. top of pg 312
                + 1962.0 * sind(lp - f)
                + 318.0 * sind(a2);

        sumb = sumb - 2235.0 * sind(lp)               // Eqn. top of pg 312
                + 382.0 * sind(a3)
                + 175.0 * sind(a1 - f)
                + 175.0 * sind(a1 + f)
                + 127.0 * sind(lp - mp)
                - 115.0 * sind(lp + mp);

        lambda = lp + suml / 1e6;      // Coordinate calculations middle page 312
        beta = sumb / 1e6;
        delta = 385000.56 + sumr / 1e3;

        eps = (23.0 + 26.0 / 60.0 + 21.448 / 3600.0)  // Eqn. (21.1) pg 135
                + T * (-46.8150 / 3600.0
                + T * (-0.00059 / 3600.0
                + T * (0.001813 / 3600.0)));

        List<Double> nutation = moonnutation(T);      // Use the complete Table 21.A pgs 133 - 134
//        List<Double> nutation = moonnutationlow(T);
        dpsi = nutation.get(0);
        deps = nutation.get(1);

        lambda += dpsi;                // Correction bottom of page 312
        eps += deps;                   // Correction top of page 313

        ra = Math.atan2(sind(lambda) * cosd(eps) - tand(beta) * sind(eps), cosd(lambda));  // Eqn. (12.3)
        if (ra < 0.0) {
            ra += pi + pi;
        }

        dec = Math.asin(sind(beta) * cosd(eps) + cosd(beta) * sind(eps) * sind(lambda));  // Eqn. (12.4)

        List<Double> result = new ArrayList<>();
        Collections.addAll(result, ra, dec, delta);

        return result;
    }

//-----------------------------------------------------------------------------------------------

    public static double sind(double arg)  // argument in degrees
    {
        return Math.sin(arg * d2r);
    }

//-----------------------------------------------------------------------------------------------

    public static double cosd(double arg)  // argument in degrees
    {
        return Math.cos(arg * d2r);
    }

//-----------------------------------------------------------------------------------------------

    public static double tand(double arg)  // argument in degrees
    {
        return Math.sin(arg * d2r) / Math.cos(arg * d2r);
    }

//-----------------------------------------------------------------------------------------------

    public static List<Double> moonnutationlow(double T) {
        double omega, L, LP, dpsi, deps;

        omega = 125.04452 + T * (-1934.136261 + T * (0.0020708 + T / 45000.0));  // Eqn. middle page 132

        L = 280.4665 + 36000.7698 * T;  // Eqn. bottom page 132

        LP = 218.3165 + 481267.8813 * T;  // Eqn. bottom page 132

        dpsi = (-17.20 / 3600.0) * sind(omega)  // Eqn. near bottom page 132
                + (-1.32 / 3600.0) * sind(L + L)
                + (-0.23 / 3600.0) * sind(LP + LP)
                + (0.21 / 3600.0) * sind(omega + omega);

        deps = (9.20 / 3600.0) * cosd(omega)  // Eqn. near bottom page 132
                + (0.57 / 3600.0) * cosd(L + L)
                + (0.10 / 3600.0) * cosd(LP + LP)
                + (-0.09 / 3600.0) * cosd(omega + omega);

        List<Double> result = new ArrayList<>();
        Collections.addAll(result, dpsi, deps);
        return result;
    }

//-----------------------------------------------------------------------------------------------

    public static List<Double> moonnutation(double T) {
        double d, m, mp, f, omega, arg, dpsi, deps;
        int i;

// Argument Multiple of D column, pages 133-134, Table 21.A

        double nargd[] =
                {0, -2, 0, 0, 0, 0, -2, 0, 0, -2,
                        -2, -2, 0, 2, 0, 2, 0, 0, -2, 0,
                        2, 0, 0, -2, 0, -2, 0, 0, 2, -2,
                        0, -2, 0, 0, 2, 2, 0, -2, 0, 2,
                        2, -2, -2, 2, 2, 0, -2, -2, 0, -2,
                        -2, 0, -1, -2, 1, 0, 0, -1, 0, 0,
                        2, 0, 2};

// Argument Multiple of M column, pages 133-134, Table 21.A

        double nargm[] =
                {0, 0, 0, 0, 1, 0, 1, 0, 0, -1,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 2, 0, 2,
                        1, 0, -1, 0, 0, 0, 1, 1, -1, 0,
                        0, 0, 0, 0, 0, -1, -1, 0, 0, 0,
                        1, 0, 0, 1, 0, 0, 0, -1, 1, -1,
                        -1, 0, -1};

// Argument Multiple of M' column, pages 133-134, Table 21.A

        double nargmp[] =
                {0, 0, 0, 0, 0, 1, 0, 0, 1, 0,
                        1, 0, -1, 0, 1, -1, -1, 1, 2, -2,
                        0, 2, 2, 1, 0, 0, -1, 0, -1, 0,
                        0, 1, 0, 2, -1, 1, 0, 1, 0, 0,
                        1, 2, 1, -2, 0, 1, 0, 0, 2, 2,
                        0, 1, 1, 0, 0, 1, -2, 1, 1, 1,
                        -1, 3, 0};

// Argument Multiple of force column, pages 133-134, Table 21.A

        double nargf[] =
                {0, 2, 2, 0, 0, 0, 2, 2, 2, 2,
                        0, 2, 2, 0, 0, 2, 0, 2, 0, 2,
                        2, 2, 0, 2, 2, 2, 2, 0, 0, 2,
                        0, 0, 0, -2, 2, 2, 2, 0, 2, 2,
                        0, 2, 2, 0, 0, 0, 2, 0, 2, 0,
                        2, -2, 0, 0, 0, 2, 2, 0, 0, 2,
                        2, 2, 2};

// Argument Multiple of Omega column, pages 133-134, Table 21.A

        double nargo[] =
                {1, 2, 2, 2, 0, 0, 2, 1, 2, 2,
                        0, 1, 2, 0, 1, 2, 1, 1, 0, 1,
                        2, 2, 0, 2, 0, 0, 1, 0, 1, 2,
                        1, 1, 1, 0, 1, 2, 2, 0, 2, 1,
                        0, 2, 1, 1, 1, 0, 1, 1, 1, 1,
                        1, 0, 0, 0, 0, 0, 2, 0, 0, 2,
                        2, 2, 2};

// Coefficient of the sine of the argument, pages 133-134, Table 21.A

        double nargs[] =
                {-171996, -13187, -2274, 2062, 1426, 712, -517, -386, -301, 217,
                        -158, 129, 123, 63, 63, -59, -58, -51, 48, 46,
                        -38, -31, 29, 29, 26, -22, 21, 17, 16, -16,
                        -15, -13, -12, 11, -10, -8, 7, -7, -7, -7,
                        6, 6, 6, -6, -6, 5, -5, -5, -5, 4,
                        4, 4, -4, -4, -4, 3, -3, -3, -3, -3,
                        -3, -3, -3};

// Coefficient of the sine of the argument, T column, pages 133-134, Table 21.A

        double nargst[] =
                {-174.2, -1.6, -0.2, 0.2, -3.4, 0.1, 1.2, -0.4, 0.0, -0.5,
                        0.0, 0.1, 0.0, 0.0, 0.1, 0.0, -0.1, 0.0, 0.0, 0.0,
                        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.1, 0.0, 0.1,
                        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                        0.0, 0.0, 0.0};

// Coefficient of the cosine of the argument, pages 133-134, Table 21.A

        double nargc[] =
                {92025, 5736, 977, -895, 54, -7, 224, 200, 129, -95,
                        0, -70, -53, 0, -33, 26, 32, 27, 0, -24,
                        16, 13, 0, -12, 0, 0, -10, 0, -8, 7,
                        9, 7, 6, 0, 5, 3, -3, 0, 3, 3,
                        0, -3, -3, 3, 3, 0, 3, 3, 3, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0};

// Coefficient of the cosine of the argument, T column, pages 133-134, Table 21.A

        double nargct[] =
                {8.9, -3.1, -0.5, 0.5, -0.1, 0.0, -0.6, 0.0, -0.1, 0.3,
                        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                        0.0, 0.0, 0.0};

        d = 297.85036                  // Eqn. top of page 132
                + T * (445267.111480
                + T * (-0.0019142
                + T * (1.0 / 189474.0)));
        d = (d % 360.0);
        if (d < 0.0) d += 360.0;

        m = 357.52772                  // Eqn. top of page 132
                + T * (35999.050340
                + T * (-0.0001603
                + T * (1.0 / 300000.0)));
        m = (m % 360.0);
        if (m < 0.0) m += 360.0;

        mp = 134.96298                 // Eqn. top of page 132
                + T * (477198.867398
                + T * (0.0086972
                + T * (1.0 / 56250.0)));
        mp = (mp % 360.0);
        if (mp < 0.0) mp += 360.0;

        f = 93.27191                   // Eqn. top of page 132
                + T * (483202.017538
                + T * (-0.0036825
                + T * (-1.0 / 327270.0)));
        f = (f % 360.0);
        if (f < 0.0) f += 360.0;

        omega = 125.04452 + T * (-1934.136261 + T * (0.0020708 + T / 45000.0));  // Eqn. middle page 132

        dpsi = 0.0;
        deps = 0.0;

        for (i = 0; i < 63; i++) {             // Sum of terms on pages 133 - 134
            arg = nargd[i] * d + nargm[i] * m + nargmp[i] * mp + nargf[i] * f + nargo[i] * omega;
            dpsi += (nargs[i] + nargst[i] * T) * sind(arg);
            deps += (nargc[i] + nargct[i] * T) * cosd(arg);
        }
        dpsi /= 36000000.0;  // Conversion from 0".0001 to degrees
        deps /= 36000000.0;

        List<Double> result = new ArrayList<>();
        Collections.addAll(result, dpsi, deps);

        return result;
    }
}
