package main.java.calculation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Kepler {
    public static ArrayList convertToCoordinate(double omega, double i, double w, double p, double e, double tau) {

//        final double MU = 4.0362e14;
        final double MU = 398600.4415E9;

        double Om = Math.toRadians(omega);
        double I = Math.toRadians(i);
        double W = Math.toRadians(w);
        double C = Math.sqrt(p * MU);
        double a = p / (1 - e * e);
//        System.out.println('a');
//        System.out.println(a);
        double T = 2 * Math.PI * Math.sqrt(a * a * a / MU);
//        System.out.println('T');
//        System.out.println(T);
//        double b = p / Math.sqrt(1 - e * e);
        double n = Math.sqrt(MU / (a * a * a));
        double M = n * (0 - tau);
        double E = kpSolvekepler(M, e, 1E-10);
        double v = 2 * Math.atan((Math.tan(E / 2)) / (Math.sqrt((1 - e) / (1 + e))));
//        double f = e * MU;
        double u = v + W;
        double r = p / (1 + e * Math.cos(v));

//        double x = r * (NumberUtils.round(Math.cos(u), 3) * NumberUtils.round(Math.cos(Om), 3) -
//                NumberUtils.round(Math.sin(u), 3) * NumberUtils.round(Math.sin(Om), 3) * NumberUtils.round(Math.cos(I), 3));
//        double y = r * (NumberUtils.round(Math.cos(u), 3) * NumberUtils.round(Math.sin(Om), 3) +
//                NumberUtils.round(Math.sin(u), 3) * NumberUtils.round(Math.cos(Om), 3) * NumberUtils.round(Math.cos(I), 3));
//        double z = r * NumberUtils.round(Math.sin(u), 3) * NumberUtils.round(Math.sin(I), 3);

        double x = r * (Math.cos(u) * Math.cos(Om) - Math.sin(u) * Math.sin(Om) * Math.cos(I));
        double y = r * (Math.cos(u) * Math.sin(Om) + Math.sin(u) * Math.cos(Om) * Math.cos(I));
        double z = r * Math.sin(u) * Math.sin(I);


//        double Vx = (C / p) * (e * NumberUtils.round(Math.sin(v), 3) * (NumberUtils.round(Math.cos(u), 3) * NumberUtils.round(Math.cos(Om), 3) -
//                NumberUtils.round(Math.sin(u), 3) * NumberUtils.round(Math.sin(Om), 3) * NumberUtils.round(Math.cos(I), 3)) +
//                (1 + e * NumberUtils.round(Math.cos(v), 3)) * (-NumberUtils.round(Math.sin(u), 3) * NumberUtils.round(Math.cos(Om), 3) -
//                        NumberUtils.round(Math.cos(u), 3) * NumberUtils.round(Math.sin(Om), 3) * NumberUtils.round(Math.cos(I), 3)));
//        double Vy = (C / p) * (e * NumberUtils.round(Math.sin(v), 3) * ((NumberUtils.round(Math.cos(u), 3) * NumberUtils.round(Math.sin(Om), 3) +
//                NumberUtils.round(Math.sin(u), 3) * NumberUtils.round(Math.cos(Om), 3) * NumberUtils.round(Math.cos(I), 3)) +
//                (1 + e * NumberUtils.round(Math.cos(v), 3)) * (-NumberUtils.round(Math.sin(u), 3) * NumberUtils.round(Math.sin(Om), 3) +
//                        NumberUtils.round(Math.cos(u), 3) * NumberUtils.round(Math.cos(Om), 3) * NumberUtils.round(Math.cos(I), 3))));
//        double Vz = (C / p) * (e * NumberUtils.round(Math.sin(v), 3) * NumberUtils.round(Math.sin(u), 3) * NumberUtils.round(Math.sin(I), 3) +
//                (1 + e * NumberUtils.round(Math.cos(v), 3)) * NumberUtils.round(Math.cos(u), 3) * NumberUtils.round(Math.sin(I), 3));

        double Vx = (C / p) * (e * Math.sin(v) * (Math.cos(u) * Math.cos(Om) - Math.sin(u) * Math.sin(Om) * Math.cos(I)) +
                (1 + e * Math.cos(v)) * (-Math.sin(u) * Math.cos(Om) - Math.cos(u) * Math.sin(Om) * Math.cos(I)));
        double Vy = (C / p) * (e * Math.sin(v) * (Math.cos(u) * Math.sin(Om) + Math.sin(u) * Math.cos(Om) * Math.cos(I)) +
                (1 + e * Math.cos(v)) * (-Math.sin(u) * Math.sin(Om) + Math.cos(u) * Math.cos(Om) * Math.cos(I)));
        double Vz = (C / p) * (e * Math.sin(v) * Math.sin(u) * Math.sin(I) + (1 + e * Math.cos(v)) * Math.cos(u) * Math.sin(I));

        ArrayList<Double> resList = new ArrayList<>();
        Collections.addAll(resList, x, y, z, Vx, Vy, Vz);
        return resList;
    }

    public static ArrayList convertToKepler(double x, double y, double z, double vx, double vy, double vz) {

//        final double MU = 4.0362e14;
        final double MU = 398600.4415E9;
//        double r = NumberUtils.round(Math.sqrt(x * x + y * y + z * z), 6);
//        double c1 = NumberUtils.round(y * vz - vy * z, 6);
//        double c2 = NumberUtils.round(z * vx - vz * x, 6);
//        double c3 = NumberUtils.round(x * vy - vx * y, 6);
//        double c = NumberUtils.round(Math.sqrt(c1 * c1 + c2 * c2 + c3 * c3), 6);
//        double f1 = NumberUtils.round((-MU * x / r - vz * c2 + vy * c3), 6);
//        double f2 = NumberUtils.round((-MU * y / r - vx * c3 + vz * c1), 6);
//        double f3 = NumberUtils.round((-MU * z / r - vy * c1 + vx * c2), 6);
//        double f = NumberUtils.round(Math.sqrt(f1 * f1 + f2 * f2 + f3 * f3), 6);
//        double p = NumberUtils.round(c * c / MU, 6);
//        double e = NumberUtils.round(f / MU, 6);
        double r = Math.sqrt(x * x + y * y + z * z);
//        System.out.println('r');
//        System.out.println(r);
//        System.out.println(Math.hypot(Math.hypot(x, y), z));
        double c1 = y * vz - vy * z;
        double c2 = z * vx - vz * x;
        double c3 = x * vy - vx * y;
        double c = Math.sqrt(c1 * c1 + c2 * c2 + c3 * c3);
//        System.out.println('c');
//        System.out.println(c);
//        System.out.println(Math.hypot(Math.hypot(c1, c2), c3));
        double f1 = (-MU * x / r - vz * c2 + vy * c3);
//        System.out.println(f1);
        double f2 = (-MU * y / r - vx * c3 + vz * c1);
//        System.out.println(f2);
        double f3 = (-MU * z / r - vy * c1 + vx * c2);
//        System.out.println(f3);
        double f = Math.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
//        System.out.println('f');
//        System.out.println(f);
//        System.out.println(Math.hypot(Math.hypot(f1, f2), f3));
        double p = c * c / MU;
        double e = f / MU;
        if (Double.isNaN(e)) {
            e = 0;
        }

        double a = p / (1 - e * e);
        double T = 2 * Math.PI * Math.sqrt(a * a * a / MU);
//        System.out.println('T');
//        System.out.println(T);
//        System.out.println('a');
//        System.out.println(a);

//        double v = Math.acos((p / r - 1.) / e);
//        System.out.println('v');
//        System.out.println(Math.toDegrees(v));
//      Sdelat
        double ortrx = x / r;
        double ortry = y / r;
        double ortrz = z / r;
        double vr = vx * ortrx + vy * ortry + vz * ortrz;
//        double v1 = Math.asin(vr * p / (c * e));
        double sinv = vr * p / (c * e);
        double cosv = (p / r - 1.) / e;
        double v = 0;

        if (Double.compare(sinv, 0) > 0 && Double.compare(cosv, 0) > 0) {
            v = Math.asin(sinv);
        } else if (Double.compare(sinv, 0) > 0 && Double.compare(cosv, 0) < 0) {
            v = Math.PI - Math.asin(sinv);
        } else if (Double.compare(sinv, 0) < 0 && Double.compare(cosv, 0) < 0) {
            v = Math.PI - Math.asin(sinv);
        } else if (Double.compare(sinv, 0) < 0 && Double.compare(cosv, 0) > 0) {
            v = Math.PI * 2 + Math.asin(sinv);
        } else if (Double.compare(sinv, 0) == 0 && Double.compare(cosv, 1) == 0) {
            v = 0;
        } else if (Double.compare(sinv, 1) == 0 && Double.compare(cosv, 0) == 0) {
            v = Math.PI / 2;
        } else if (Double.compare(sinv, 0) == 0 && Double.compare(cosv, -1) == 0) {
            v = Math.PI;
        } else if (Double.compare(sinv, -1) == 0 && Double.compare(cosv, 0) == 0) {
            v = 3 * Math.PI / 2;
        }

//        System.out.println('v');
//        System.out.println(Math.toDegrees(v));
//        BalkEnd

//        double tau = NumberUtils.round((-p * p / c) * NumberUtils.round(integral(100000, 0, v, e), 4), 4);
//        double E1 = 2 * Math.atan(Math.sqrt((1 - e) / (1 + e)) * Math.tan(v / 2));
//        double a = p / (1 - e * e);
//        double n = Math.sqrt(MU) / Math.pow(a, 3. / 2);
//        double diff = (E1 - e * Math.sin(E1)) / n;
        double tau = (-p * p / c) * integral(100000, 0, v, e);
        if (Double.isNaN(tau)) {
            tau = 0;
        }

//        double rvx = -z * vy + y * vz;
//        double rvy = -x * vz + z * vx;
//        double rvz = -y * vx + x * vy;
//        double rvabs = Math.sqrt(rvx * rvx + rvy * rvy + rvz * rvz);
//        double kx = rvx / rvabs;
//        double ky = rvy / rvabs;
//        double kz = rvz / rvabs;
        double kx = c1 / c;
        double ky = c2 / c;
        double kz = c3 / c;

//        double i = Math.acos(c3 / c);
        double i = Math.acos(kz);
        if (Double.isNaN(i)) {
            i = 0;
        }

        double omegasin = kx / Math.sin(i);
        double omegacos = -ky / Math.sin(i);
        double omega = 0;
        if (omegasin > 0 && omegacos > 0) {
            omega = Math.asin(omegasin);
        } else if (omegasin > 0 && omegacos < 0) {
            omega = Math.PI - Math.asin(omegasin);
        } else if (omegasin < 0 && omegacos < 0) {
            omega = Math.PI - Math.asin(omegasin);
        } else if (omegasin < 0 && omegacos > 0) {
            omega = Math.PI * 2 + Math.asin(omegasin);
        } else if (omegasin == 0 && omegacos == 1) {
            omega = 0;
        } else if (omegasin == 1 && omegacos == 0) {
            omega = Math.PI / 2;
        } else if (omegasin == 0 && omegacos == -1) {
            omega = Math.PI;
        } else if (omegasin == -1 && omegacos == 0) {
            omega = 3 * Math.PI / 2;
        }
        if (Double.isNaN(omega)) {
            omega = 0;
        }

        double w = 0;
        double wsinv = -Math.cos(omega) * ortrz / ky;
        double wcosv = Math.cos(omega) * ortrx + Math.sin(omega) * ortry;
        if (wsinv == 0 || Double.isNaN(wsinv)) {
            wsinv = Math.sin(omega) * ortrz / kx;
        }
        if (wsinv == 0 || Double.isNaN(wsinv)) {
            wsinv = (-Math.sin(omega) * ortrx + Math.cos(omega) * ortry) / kz;
        }
        if (wsinv > 0 && wcosv > 0) {
            w = Math.asin(wsinv) - v;
        } else if (wsinv > 0 && wcosv < 0) {
            w = Math.PI + Math.asin(wsinv) - v;
        } else if (wsinv < 0 && wcosv < 0) {
            w = Math.PI - Math.asin(wsinv) - v;
        } else if (wsinv < 0 && wcosv > 0) {
            w = Math.PI * 2 + Math.asin(wsinv) - v;
        } else if (wsinv == 0 && wcosv == 1) {
            w = 0 - v;
        } else if (wsinv == 1 && wcosv == 0) {
            w = Math.PI / 2 - v;
        } else if (wsinv == 0 && wcosv == -1) {
            w = Math.PI - v;
        } else if (wsinv == -1 && wcosv == 0) {
            w = 3 * Math.PI / 2 - v;
        }
        if (w < 0) {
            w = 2 * Math.PI + w;
        }
        if (Double.isNaN(w)) {
            w = 0;
        }

        ArrayList<Double> resList = new ArrayList<>();
        Collections.addAll(resList, Math.toDegrees(omega), Math.toDegrees(i), Math.toDegrees(w), p, e, tau);
        return resList;
    }

//    public static double newtonMethod(double e, double mu, double p, double acc) {
//        double x1 = -1;
//        double x2 = 2;
//        double diff;
//        diff = Math.abs(x1 - x2);
//        while (diff > acc) {
//            x2 = x1 - (Math.atan(x1) - e * Math.abs(x1 / (1 + x1 * x1)) - ((1 - e * e) / (2 * p)) * Math.sqrt((mu * (1 - e * e)) / p)) /
//                    ((e * x1 * (x1 * x1 - 1) + (x1 * x1 + 1) * Math.abs(x1)) / ((x1 * x1 + 1) * (x1 * x1 + 1) * Math.abs(x1)));
//            diff = Math.abs(x1 - x2);
//            x1 = x2;
//            System.out.println(diff);
//        }
//        System.out.println(x1);
//        return 2 * Math.atan(x1 / (Math.sqrt((1 - e) / (1 + e))));
//    }
//
//    public static double dichotomy(double e, double mu, double p, double C, double tau, double acc) {
//        double a = -1000;
//        double b = 1000;
//        double c = (a + b) / 2.;
//
//        while ((b - a) > acc) {
//            if (Double.isNaN(anomaly_function(e, mu, p, C, tau, a)) || Double.isNaN(anomaly_function(e, mu, p, C, tau, b)) ||
//                    Double.isNaN(anomaly_function(e, mu, p, C, tau, c))) {
//                return 0;
//            }
//            if (anomaly_function(e, mu, p, C, tau, a) * anomaly_function(e, mu, p, C, tau, c) < 0) {
//                b = c;
//                c = (a + b) / 2.;
//            } else if (anomaly_function(e, mu, p, C, tau, c) * anomaly_function(e, mu, p, C, tau, b) < 0) {
//                a = c;
//                c = (a + b) / 2.;
//            } else if (anomaly_function(e, mu, p, C, tau, a) == 0) {
//                return a;
//            } else if (anomaly_function(e, mu, p, C, tau, b) == 0) {
//                return b;
//            } else if (anomaly_function(e, mu, p, C, tau, c) == 0) {
//                return c;
//            }
//        }
//        return a;
//    }


    public static double f(double x, double e) {
        return 1. / Math.pow(1 + e * Math.cos(x), 2);
    }

    public static double integral(int n, double a, double b, double e) {
        double sum = 0, sum2 = 0;
        double[] x = new double[n];
        double h = (b - a) / n;
        for (int i = 0; i < n; i++) {
            x[i] = a + i * h;
        }
        for (int i = 1; i < n; i++) {
            sum += f(x[i], e);
            sum2 += f((x[i - 1] + x[i]) / 2, e);
        }
        return h / 6 * (f(a, e) + f(b, e) + 2 * sum + 4 * (sum2 + b));
    }

    public static double kpSolvekepler(double M, double e, double acc) {
        double curE, nextE;
        nextE = M;
        do {
            curE = nextE;
            nextE = M + e * Math.sin(curE);
        } while (Math.abs(curE - nextE) > acc);
        return nextE;
    }
}
