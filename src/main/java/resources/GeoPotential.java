//package main.java.utils;
//
//import java.util.ArrayList;
//import javacalculus.core.CALC;
//import javacalculus.core.CalcParser;
//import javacalculus.evaluator.CalcSUB;
//import javacalculus.struct.CalcDouble;
//import javacalculus.struct.CalcObject;
//import javacalculus.struct.CalcSymbol;
//
//public class GeoPotential {
//
//    public static double U(double x, double y, double z, int n, int qy) {
//        double f = 6.67 * Math.pow(10, -11);
//        double m = 5.9726 * Math.pow(10, 24);
//        double r0 = 6378137;
//        double r = Math.sqrt(x * x + y * y + z * z);
//        double U0 = f * m / r;
//        double U1 =
//
//
//        double U = U0 * (1 + U1);
//        double dunkdx = derivR(n, f, m, r, r0) * (-x / Math.pow(r, 3)) * Z(n, qy, dZold, dZoldest, z) * Q(n, qy, x, y, r) +
//                R(n, f, m, r, r0) * derivZ(n, qy, dZold, dZoldest, z) * (-x * z / Math.pow(r, 3)) * Q(n, qy, x, y, r) +
//                R(n, f, m, r, r0) * Z(n, qy, dZold, dZoldest, z) * (derivQnkXr * (1 / r - Math.pow(x, 2) / Math.pow(r, 3)) + derivQnkYr * (-x * y / Math.pow(r, 3)));
//        double dunkdy = derivR(n, f, m, r, r0) * (-y / Math.pow(r, 3)) * Z(n, qy, dZold, dZoldest, z) * Q(n, qy, x, y, r) +
//                R(n, f, m, r, r0) * derivZ(n, qy, dZold, dZoldest, z) * (-y * z / Math.pow(r, 3)) * Q(n, qy, x, y, r) +
//                R(n, f, m, r, r0) * Z(n, qy, dZold, dZoldest, z) * (derivQnkXr * (-x * y / Math.pow(r, 3)) + derivQnkYr * (1 / r - Math.pow(y, 2) / Math.pow(r, 3)));
//        double dunkdz = derivR(n, f, m, r, r0) * (-z / Math.pow(r, 3)) * Z(n, qy, dZold, dZoldest, z) * Q(n, qy, x, y, r) +
//                R(n, f, m, r, r0) * derivZ(n, qy, dZold, dZoldest, z) * (1 / r - Math.pow(z, 2) / Math.pow(r, 3)) * Q(n, qy, x, y, r) +
//                R(n, f, m, r, r0) * Z(n, qy, dZold, dZoldest, z) * (derivQnkXr * (-x * z / Math.pow(r, 3)) + derivQnkYr * (-y * z / Math.pow(r, 3)));
//
//        return
//    }
//
//    public static double Q(int n, int qy, double x, double y, double r) {
//        return C(n, qy) * XY(qy, x, y, r).get(0) + S(n, qy) * XY(qy, x, y, r).get(1);
//    }
//
//    public static ArrayList<Double> XY(int qy, double x, double y, double r) {
//        ArrayList<Double> XY = new ArrayList<>();
//        double Xold = 1;
//        double Yold = 0;
//        for (int qw = 0; qw <= qy; qw++) {
//            double Xnew = Xold * x / r - Yold * y / r;
//            double Ynew = Yold * x / r - Xold * y / r;
//            Xold = Xnew;
//            Yold = Ynew;
//        }
//        XY.add(Xold);
//        XY.add(Yold);
//        return XY;
//    }
//
//    public static double Z(int n, int qy, double dZold, double dZoldest, double z) {
//        if (qy == 1) {
//            return dPn(n, z);
//        } else if (qy > 1) {
//            return dPkn(n, qy, dZold, dZoldest, z);
//        } else {
//            return 0;
//        }
//    }
//
//    public static double dPkn(int n, int qy, double dPold, double dPoldest, double z) {
//        if (qy == 1) {
//            return dPn(n, z);
//        } else {
//            for (int qw = 2; qw <= n; qw++) {
//                // Sdelat dPold/dPoldest ???
//                double dPnew = (2 * qw - 1) * dPold + dPoldest;
////                double d1P = dPn(qw - 1, z);
////                double dPnew = (2 * qw - 1) * d1P + dPoldest;
//                dPoldest = dPold;
//                dPold = dPnew;
//            }
//            return dPold;
//        }
//    }
//
//    public static double dPn(int n, double z) {
//        double dPold = 0;
//        for (int qw = 1; qw <= n; qw++) {
//            dPold = qw * P(qw - 1, z) + z * dPold;
//        }
//        return dPold;
//    }
//
//    public static double P(int n, double z) {
//        double Poldest = 1;
//        double Pold = z;
//        for (int qw = 2; qw <= n; qw++) {
//            double Pnew = ((2 * qw + 1) * z * Pold - qw * Poldest) / (qw + 1);
//            Poldest = Pold;
//            Pold = Pnew;
//        }
//        return Pold;
//    }
//
//    public static double derivZ(int n, int qy, double dZold, double dZoldest, double z) {
//        return Z(n, qy + 1, dZold, dZoldest, z);
//    }
//
//    public static double R(int n, double f, double m, double r, double r0) {
//        return f * (m / r) * Math.pow((r0 / r), n);
//    }
//
//    public static double derivR(int n, double f, double m, double r, double r0) {
//        return f * m * (n + 1) * Math.pow((r0 / r), n);
//    }
//
//    public static double derivQX () {
//        return C(n, qy) * () + S(n, qy) * ();
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
