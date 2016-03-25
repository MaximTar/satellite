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
//    public static double U(double x, double y, double z, int n, int k) {
//        double f = 6.67 * Math.pow(10, -11);
//        double m = 5.9726 * Math.pow(10, 24);
//        double r0 = 6378137;
//        double r = Math.sqrt(x * x + y * y + z * z);
//        double U0 = f * m / r;
//        double U1 =
//
//
//        double U = U0 * (1 + U1);
//        double dunkdx = derivR(n, f, m, r, r0) * (-x / Math.pow(r, 3)) * Z(n, k, dZold, dZoldest, z) * Q(n, k, x, y, r) +
//                R(n, f, m, r, r0) * derivZ(n, k, dZold, dZoldest, z) * (-x * z / Math.pow(r, 3)) * Q(n, k, x, y, r) +
//                R(n, f, m, r, r0) * Z(n, k, dZold, dZoldest, z) * (derivQnkXr * (1 / r - Math.pow(x, 2) / Math.pow(r, 3)) + derivQnkYr * (-x * y / Math.pow(r, 3)));
//        double dunkdy = derivR(n, f, m, r, r0) * (-y / Math.pow(r, 3)) * Z(n, k, dZold, dZoldest, z) * Q(n, k, x, y, r) +
//                R(n, f, m, r, r0) * derivZ(n, k, dZold, dZoldest, z) * (-y * z / Math.pow(r, 3)) * Q(n, k, x, y, r) +
//                R(n, f, m, r, r0) * Z(n, k, dZold, dZoldest, z) * (derivQnkXr * (-x * y / Math.pow(r, 3)) + derivQnkYr * (1 / r - Math.pow(y, 2) / Math.pow(r, 3)));
//        double dunkdz = derivR(n, f, m, r, r0) * (-z / Math.pow(r, 3)) * Z(n, k, dZold, dZoldest, z) * Q(n, k, x, y, r) +
//                R(n, f, m, r, r0) * derivZ(n, k, dZold, dZoldest, z) * (1 / r - Math.pow(z, 2) / Math.pow(r, 3)) * Q(n, k, x, y, r) +
//                R(n, f, m, r, r0) * Z(n, k, dZold, dZoldest, z) * (derivQnkXr * (-x * z / Math.pow(r, 3)) + derivQnkYr * (-y * z / Math.pow(r, 3)));
//
//        return
//    }
//
//    public static double Q(int n, int k, double x, double y, double r) {
//        return C(n, k) * XY(k, x, y, r).get(0) + S(n, k) * XY(k, x, y, r).get(1);
//    }
//
//    public static ArrayList<Double> XY(int k, double x, double y, double r) {
//        ArrayList<Double> XY = new ArrayList<>();
//        double Xold = 1;
//        double Yold = 0;
//        for (int i = 0; i <= k; i++) {
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
//    public static double Z(int n, int k, double dZold, double dZoldest, double z) {
//        if (k == 1) {
//            return dPn(n, z);
//        } else if (k > 1) {
//            return dPkn(n, k, dZold, dZoldest, z);
//        } else {
//            return 0;
//        }
//    }
//
//    public static double dPkn(int n, int k, double dPold, double dPoldest, double z) {
//        if (k == 1) {
//            return dPn(n, z);
//        } else {
//            for (int i = 2; i <= n; i++) {
//                // TODO dPold/dPoldest ???
//                double dPnew = (2 * i - 1) * dPold + dPoldest;
////                double d1P = dPn(i - 1, z);
////                double dPnew = (2 * i - 1) * d1P + dPoldest;
//                dPoldest = dPold;
//                dPold = dPnew;
//            }
//            return dPold;
//        }
//    }
//
//    public static double dPn(int n, double z) {
//        double dPold = 0;
//        for (int i = 1; i <= n; i++) {
//            dPold = i * P(i - 1, z) + z * dPold;
//        }
//        return dPold;
//    }
//
//    public static double P(int n, double z) {
//        double Poldest = 1;
//        double Pold = z;
//        for (int i = 2; i <= n; i++) {
//            double Pnew = ((2 * i + 1) * z * Pold - i * Poldest) / (i + 1);
//            Poldest = Pold;
//            Pold = Pnew;
//        }
//        return Pold;
//    }
//
//    public static double derivZ(int n, int k, double dZold, double dZoldest, double z) {
//        return Z(n, k + 1, dZold, dZoldest, z);
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
//        return C(n, k) * () + S(n, k) * ();
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
