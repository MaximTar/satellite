package utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalculationUtils {

    public double x, y, z, vx, vy, vz;
    public static File fileName;
    public static File qfileName;
    public static File fileNameFull;
    public static double i1x, i1y, i1z, i2x, i2y, i2z;
    public static List<Double> tensionF;
    public static boolean tensionFFlag = false;

    CalculationUtils(double x, double y, double z, double vx, double vy, double vz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

//    public void out() {
//        System.out.println("x=%f\t" + this.x);
//        System.out.println("y=%f\t" + this.y);
//        System.out.println("vx=%f\t" + this.vx);
//        System.out.println("vy=%f\t" + this.vy);
//    }

    public static CalculationUtils sum(CalculationUtils a, CalculationUtils b) {
        CalculationUtils c = new CalculationUtils(0, 0, 0, 0, 0, 0);
        c.x = a.x + b.x;
        c.y = a.y + b.y;
        c.z = a.z + b.z;
        c.vx = a.vx + b.vx;
        c.vy = a.vy + b.vy;
        c.vz = a.vz + b.vz;
        return c;
    }

    public static CalculationUtils sub(CalculationUtils a, CalculationUtils b) {
        CalculationUtils c = new CalculationUtils(0, 0, 0, 0, 0, 0);
        c.x = a.x - b.x;
        c.y = a.y - b.y;
        c.z = a.z - b.z;
        c.vx = a.vx - b.vx;
        c.vy = a.vy - b.vy;
        c.vz = a.vz - b.vz;
        return c;
    }

    public static CalculationUtils mult(double a, CalculationUtils b) {
        CalculationUtils c = new CalculationUtils(0, 0, 0, 0, 0, 0);
        c.x = a * b.x;
        c.y = a * b.y;
        c.z = a * b.z;
        c.vx = a * b.vx;
        c.vy = a * b.vy;
        c.vz = a * b.vz;
        return c;
    }

    public static CalculationUtils mult(CalculationUtils b, double a) {
        CalculationUtils c = new CalculationUtils(0, 0, 0, 0, 0, 0);
        c.x = a * b.x;
        c.y = a * b.y;
        c.z = a * b.z;
        c.vx = a * b.vx;
        c.vy = a * b.vy;
        c.vz = a * b.vz;
        return c;
    }

    public static CalculationUtils F(CalculationUtils U, double t, boolean geopot, boolean sungrav, boolean moongrav, boolean sunpres, boolean drag) {
        CalculationUtils res = new CalculationUtils(0, 0, 0, 0, 0, 0);
//        double G = 6.67 * Math.pow(10, -11);
//        double M = 5.9726 * Math.pow(10, 24);
        double mu = 398600.4415E9;

        res.x = U.vx;
        res.y = U.vy;
        res.z = U.vz;

//        Простейшее приближение F = fm/r
//        res.vx = -G * M * U.x / Math.pow(Math.pow(U.x * U.x + U.y * U.y + U.z * U.z, 0.5), 3);
//        res.vy = -G * M * U.y / Math.pow(Math.pow(U.x * U.x + U.y * U.y + U.z * U.z, 0.5), 3);
//        res.vz = -G * M * U.z / Math.pow(Math.pow(U.x * U.x + U.y * U.y + U.z * U.z, 0.5), 3);
//        Это считает точнее
        res.vx = -mu * U.x / Math.pow(Math.pow(U.x * U.x + U.y * U.y + U.z * U.z, 0.5), 3);
        res.vy = -mu * U.y / Math.pow(Math.pow(U.x * U.x + U.y * U.y + U.z * U.z, 0.5), 3);
        res.vz = -mu * U.z / Math.pow(Math.pow(U.x * U.x + U.y * U.y + U.z * U.z, 0.5), 3);

//        Приближение по Чазову
        if (geopot) {
            List<Double> GeoPotential = GeoPot.calc(U.x, U.y, U.z, 4);
            res.vx = GeoPotential.get(0);
            res.vy = GeoPotential.get(1);
            res.vz = GeoPotential.get(2);
        }

//        Sun's Gravity
        if (sungrav) {
            double muSun = 132712440018E9;
            TimeZone timeZone = TimeZone.getTimeZone("GMT");
            Calendar c = Calendar.getInstance(timeZone);
            List<Double> sunPos = SunPosition.sunPosition(c);
            List<Double> sunGravity = Gravitation.force(muSun, U.x, U.y, U.z, sunPos.get(0), sunPos.get(1), sunPos.get(2));
            res.vx += sunGravity.get(0);
            res.vy += sunGravity.get(1);
            res.vz += sunGravity.get(2);
        }

//        Moon's Gravity
        if (moongrav) {
            double muMoon = 4902.779E9;
            TimeZone timeZone = TimeZone.getTimeZone("GMT");
            Calendar c = Calendar.getInstance(timeZone);
            List<Double> moonPos = MoonPosition.moonPosition(c);
            List<Double> moonGravity = Gravitation.force(muMoon, U.x, U.y, U.z, moonPos.get(0), moonPos.get(1), moonPos.get(2));
            res.vx += moonGravity.get(0);
            res.vy += moonGravity.get(1);
            res.vz += moonGravity.get(2);
        }

//        Sun's Radiation Pressure
        if (sunpres) {
            double A = 1; // Площадь сечения
            double C = 1; // Передача импульса за счёт поглощения и отражения
            TimeZone timeZone = TimeZone.getTimeZone("GMT");
            Calendar c = Calendar.getInstance(timeZone);
            List<Double> sunPres = SunRadiationPressure.force(A, C, c);
            res.vx += sunPres.get(0);
            res.vy += sunPres.get(1);
            res.vz += sunPres.get(2);
        }

//        Atmospheric Drag
        if (drag) {
            double A = 1; // Площадь сечения
            // List<Double> lla = ECEF2LLA.conversion(U.x, U.y, U.z);
            double drag_coefficient = 2; // "Эмпирический коэффициент примерно равный 2"
            ArrayList<Double> w_earth = new ArrayList<>();
            Collections.addAll(w_earth, 0., 0., 7.2921158553E-5);
            ArrayList<Double> r = new ArrayList<>();
            Collections.addAll(r, U.x, U.y, U.z);
            List<Double> v_earth = VectorsAlgebra.multV(w_earth, r);// Поправка для нахождения относительной скорости
            double radv = Math.sqrt(Math.pow(U.x, 2) + Math.pow(U.y, 2) + Math.pow(U.z, 2)); // Радиус-вектор

            res.vx += Drag.force(drag_coefficient, Drag.exponentialModelDensity(radv), U.vx - v_earth.get(0), A);
            res.vy += Drag.force(drag_coefficient, Drag.exponentialModelDensity(radv), U.vy - v_earth.get(1), A);
            res.vz += Drag.force(drag_coefficient, Drag.exponentialModelDensity(radv), U.vz - v_earth.get(2), A);
        }


        return (res);
    }

    public static CalculationUtils F2(CalculationUtils U1, CalculationUtils U2, double t, boolean geopot, boolean sungrav, boolean moongrav, boolean sunpres, boolean drag, double k, double l) {
        CalculationUtils res = F(U1, t, geopot, sungrav, moongrav, sunpres, drag);
        ArrayList<Double> r1 = new ArrayList<>(Arrays.asList(U1.x, U1.y, U1.z));
        ArrayList<Double> r2 = new ArrayList<>(Arrays.asList(U2.x, U2.y, U2.z));
        List<Double> r = VectorsAlgebra.difference(r2, r1);
        double dx = VectorsAlgebra.absoluteValue(r) - l;
        tensionFFlag = false;

        if (dx > 0) {
            r = VectorsAlgebra.normalize(r);
            tensionF = VectorsAlgebra.constMult(k * dx, r);
            tensionFFlag = true;
            res.vx += tensionF.get(0);
            res.vy += tensionF.get(1);
            res.vz += tensionF.get(2);
        }

        return res;
    }

    public static List<List<Double>> calculateOneBody(double tM, double dtM, double tMaxM, double xM, double yM, double zM,
                                                      double VxM, double VyM, double VzM,
                                                      double l0, double l1, double l2, double l3,
                                                      double wx, double wy, double wz,
                                                      double jxx, double jyy, double jzz,
                                                      boolean geoPot, boolean sunGravity, boolean moonGravity, boolean sunPres, boolean drag) {
        i1x = jxx;
        i1y = jyy;
        i1z = jzz;

        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();
        List<Double> zList = new ArrayList<>();
        List<Double> vxList = new ArrayList<>();
        List<Double> vyList = new ArrayList<>();
        List<Double> vzList = new ArrayList<>();
        List<List<Double>> resList = new ArrayList<>();
        CalculationUtils U = new CalculationUtils(xM, yM, zM, VxM, VyM, VzM);
        CalculationUtils k1, k2, k3, k4;
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh-mm");
        fileName = new File("OneBody_" + dateFormat.format(d) + ".txt");
        fileNameFull = new File(dateFormat.format(d) + "FULL.txt");

        // Quaternions part
        ArrayList<Double> iList = new ArrayList<>();
        ArrayList<Double> jList = new ArrayList<>();
        ArrayList<Double> kList = new ArrayList<>();
        ArrayList<Double> lList = new ArrayList<>();
        ArrayList<Double> wxList = new ArrayList<>();
        ArrayList<Double> wyList = new ArrayList<>();
        ArrayList<Double> wzList = new ArrayList<>();
        Quaternion Q = new Quaternion(l0, l1, l2, l3, wx, wy, wz);
        Quaternion q1, q2, q3, q4;
        qfileName = new File("OneBodyQuaternion_" + dateFormat.format(d) + ".txt");

        while (tM <= tMaxM) {
            k1 = mult(F(U, tM, geoPot, sunGravity, moonGravity, sunPres, drag), dtM);
            k2 = mult(F(sum(U, mult(0.5, k1)), tM + 0.5 * dtM, geoPot, sunGravity, moonGravity, sunPres, drag), dtM);
            k3 = mult(F(sum(U, mult(0.5, k2)), tM + 0.5 * dtM, geoPot, sunGravity, moonGravity, sunPres, drag), dtM);
            k4 = mult(F(sum(U, k3), tM + dtM, geoPot, sunGravity, moonGravity, sunPres, drag), dtM);
            U = sum(U, mult(1.0 / 6.0, sum(sum(k1, mult(2, k2)), sum(mult(2, k3), k4))));
            xList.add(U.x);
            yList.add(U.y);
            zList.add(U.z);
            vxList.add(U.vx);
            vyList.add(U.vy);
            vzList.add(U.vz);

            // Quaternions part
            q1 = Quaternion.mult(dtM, Quaternion.F(Q, tM, U));
            q2 = Quaternion.mult(dtM, Quaternion.F(Quaternion.sum(Q, Quaternion.mult(0.5, q1)), tM + 0.5 * dtM, U));
            q3 = Quaternion.mult(dtM, Quaternion.F(Quaternion.sum(Q, Quaternion.mult(0.5, q2)), tM + 0.5 * dtM, U));
            q4 = Quaternion.mult(dtM, Quaternion.F(Quaternion.sum(Q, q3), tM + dtM, U));
            Q = Quaternion.sum(Q, Quaternion.mult(1.0 / 6.0, Quaternion.sum(Quaternion.sum(q1, Quaternion.mult(2, q2)), Quaternion.sum(Quaternion.mult(2, q3), q4))));
            iList.add(Q.i);
            jList.add(Q.j);
            kList.add(Q.k);
            lList.add(Q.l);
            wxList.add(Q.wx);
            wyList.add(Q.wy);
            wzList.add(Q.wz);
            tM += dtM;

            Quaternion N = Quaternion.normalize(Q);

            String qtext = String.valueOf(N.i) + "\t\t\t" + String.valueOf(N.j) + "\t\t\t" + String.valueOf(N.k) + "\t\t\t" + String.valueOf(N.l)
//                    + "\n";
                    + "\t\t\t" + String.valueOf(Q.wx) + "\t\t\t" + String.valueOf(Q.wy) + "\t\t\t" + String.valueOf(Q.wz)
//                    + "\n";
//                    + "\t\t\t" + String.valueOf(N.i * N.i + N.j * N.j + N.k * N.k + N.l * N.l)
//                    + "\n";
                    + "\t\t\t" + String.valueOf(U.x) + "\t\t\t" + String.valueOf(U.y) + "\t\t\t" + String.valueOf(U.z)
                    + "\t\t\t" + String.valueOf(U.vx) + "\t\t\t" + String.valueOf(U.vy) + "\t\t\t" + String.valueOf(U.vz)
                    + "\n";
            write(qfileName.getName(), qtext);

            String text = String.valueOf(U.x) + "\t\t\t" + String.valueOf(U.y) + "\t\t\t" + String.valueOf(U.z) + "\t\t\t" + String.valueOf(U.vx)
                    + "\t\t\t" + String.valueOf(U.vy) + "\t\t\t" + String.valueOf(U.vz) + "\t\t\t" + String.valueOf(Math.sqrt(U.x * U.x + U.y * U.y + U.z * U.z)) + "\n";
            write(fileName.getName(), text);
        }

        Collections.addAll(resList, xList, yList, zList, vxList, vyList, vzList);

        return (resList);
    }

    public static List<List<Double>> calculateTwoBody(double tM, double dtM, double tMaxM,
                                                      double x1M, double y1M, double z1M,
                                                      double x2M, double y2M, double z2M,
                                                      double V1xM, double V1yM, double V1zM,
                                                      double V2xM, double V2yM, double V2zM,
                                                      double q1w, double q1x, double q1y, double q1z,
                                                      double q2w, double q2x, double q2y, double q2z,
                                                      double w1x, double w1y, double w1z,
                                                      double w2x, double w2y, double w2z,
                                                      double j1xx, double j1yy, double j1zz,
                                                      double j2xx, double j2yy, double j2zz,
                                                      boolean geoPot, boolean sunGravity, boolean moonGravity, boolean sunPres, boolean drag,
                                                      double k, double l) {
        i1x = j1xx;
        i1y = j1yy;
        i1z = j1zz;
        i2x = j2xx;
        i2y = j2yy;
        i2z = j2zz;

        List<Double> x1List = new ArrayList<>();
        List<Double> y1List = new ArrayList<>();
        List<Double> z1List = new ArrayList<>();
        List<Double> v1xList = new ArrayList<>();
        List<Double> v1yList = new ArrayList<>();
        List<Double> v1zList = new ArrayList<>();
        List<Double> x2List = new ArrayList<>();
        List<Double> y2List = new ArrayList<>();
        List<Double> z2List = new ArrayList<>();
        List<Double> v2xList = new ArrayList<>();
        List<Double> v2yList = new ArrayList<>();
        List<Double> v2zList = new ArrayList<>();
        List<List<Double>> resList = new ArrayList<>();
        CalculationUtils U1 = new CalculationUtils(x1M, y1M, z1M, V1xM, V1yM, V1zM);
        CalculationUtils U2 = new CalculationUtils(x2M, y2M, z2M, V2xM, V2yM, V2zM);
        CalculationUtils k11, k12, k21, k22, k31, k32, k41, k42;
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh-mm");
        fileName = new File("TwoBody_" + dateFormat.format(d) + ".txt");

        // Quaternions part
        ArrayList<Double> i1List = new ArrayList<>();
        ArrayList<Double> j1List = new ArrayList<>();
        ArrayList<Double> k1List = new ArrayList<>();
        ArrayList<Double> l1List = new ArrayList<>();
        ArrayList<Double> w1xList = new ArrayList<>();
        ArrayList<Double> w1yList = new ArrayList<>();
        ArrayList<Double> w1zList = new ArrayList<>();
        ArrayList<Double> i2List = new ArrayList<>();
        ArrayList<Double> j2List = new ArrayList<>();
        ArrayList<Double> k2List = new ArrayList<>();
        ArrayList<Double> l2List = new ArrayList<>();
        ArrayList<Double> w2xList = new ArrayList<>();
        ArrayList<Double> w2yList = new ArrayList<>();
        ArrayList<Double> w2zList = new ArrayList<>();
        Quaternion Q1 = new Quaternion(q1w, q1x, q1y, q1z, w1x, w1y, w1z);
        Quaternion Q2 = new Quaternion(q2w, q2x, q2y, q2z, w2x, w2y, w2z);
        Quaternion q11, q12, q21, q22, q31, q32, q41, q42;
        qfileName = new File("TwoBodyQuaternion_" + dateFormat.format(d) + ".txt");

        while (tM <= tMaxM) {
            k11 = mult(F2(U1, U2, tM, geoPot, sunGravity, moonGravity, sunPres, drag, k, l), dtM);
            k21 = mult(F2(sum(U1, mult(0.5, k11)), U2, tM + 0.5 * dtM, geoPot, sunGravity, moonGravity, sunPres, drag, k, l), dtM);
            k31 = mult(F2(sum(U1, mult(0.5, k21)), U2, tM + 0.5 * dtM, geoPot, sunGravity, moonGravity, sunPres, drag, k, l), dtM);
            k41 = mult(F2(sum(U1, k31), U2, tM + dtM, geoPot, sunGravity, moonGravity, sunPres, drag, k, l), dtM);
            U1 = sum(U1, mult(1.0 / 6.0, sum(sum(k11, mult(2, k21)), sum(mult(2, k31), k41))));
            x1List.add(U1.x);
            y1List.add(U1.y);
            z1List.add(U1.z);
            v1xList.add(U1.vx);
            v1yList.add(U1.vy);
            v1zList.add(U1.vz);

            k12 = mult(F2(U2, U1, tM, geoPot, sunGravity, moonGravity, sunPres, drag, k, l), dtM);
            k22 = mult(F2(sum(U2, mult(0.5, k12)), U1, tM + 0.5 * dtM, geoPot, sunGravity, moonGravity, sunPres, drag, k, l), dtM);
            k32 = mult(F2(sum(U2, mult(0.5, k22)), U1, tM + 0.5 * dtM, geoPot, sunGravity, moonGravity, sunPres, drag, k, l), dtM);
            k42 = mult(F2(sum(U2, k32), U1, tM + dtM, geoPot, sunGravity, moonGravity, sunPres, drag, k, l), dtM);
            U2 = sum(U2, mult(1.0 / 6.0, sum(sum(k12, mult(2, k22)), sum(mult(2, k32), k42))));
            x2List.add(U2.x);
            y2List.add(U2.y);
            z2List.add(U2.z);
            v2xList.add(U2.vx);
            v2yList.add(U2.vy);
            v2zList.add(U2.vz);

            // Quaternions part
            q11 = Quaternion.mult(dtM, Quaternion.F2(Q1, tM, U1));
            q21 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(Q1, Quaternion.mult(0.5, q11)), tM + 0.5 * dtM, U1));
            q31 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(Q1, Quaternion.mult(0.5, q21)), tM + 0.5 * dtM, U1));
            q41 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(Q1, q31), tM + dtM, U1));
            Q1 = Quaternion.sum(Q1, Quaternion.mult(1.0 / 6.0, Quaternion.sum(Quaternion.sum(q11, Quaternion.mult(2, q21)), Quaternion.sum(Quaternion.mult(2, q31), q41))));
            i1List.add(Q1.i);
            j1List.add(Q1.j);
            k1List.add(Q1.k);
            l1List.add(Q1.l);
            w1xList.add(Q1.wx);
            w1yList.add(Q1.wy);
            w1zList.add(Q1.wz);

            q12 = Quaternion.mult(dtM, Quaternion.F2(Q2, tM, U2));
            q22 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(Q2, Quaternion.mult(0.5, q12)), tM + 0.5 * dtM, U2));
            q32 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(Q2, Quaternion.mult(0.5, q22)), tM + 0.5 * dtM, U2));
            q42 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(Q2, q32), tM + dtM, U2));
            Q2 = Quaternion.sum(Q2, Quaternion.mult(1.0 / 6.0, Quaternion.sum(Quaternion.sum(q12, Quaternion.mult(2, q22)), Quaternion.sum(Quaternion.mult(2, q32), q42))));
            i1List.add(Q2.i);
            j1List.add(Q2.j);
            k1List.add(Q2.k);
            l1List.add(Q2.l);
            w1xList.add(Q2.wx);
            w1yList.add(Q2.wy);
            w1zList.add(Q2.wz);

            tM += dtM;

            Quaternion N1 = Quaternion.normalize(Q1);
            Quaternion N2 = Quaternion.normalize(Q2);

            String qtext = String.valueOf(N1.i) + "\t\t\t" + String.valueOf(N1.j) + "\t\t\t" + String.valueOf(N1.k) + "\t\t\t" + String.valueOf(N1.l)
                    + "\t\t\t" + String.valueOf(Q1.wx) + "\t\t\t" + String.valueOf(Q1.wy) + "\t\t\t" + String.valueOf(Q1.wz)
//                    + "\t\t\t" + String.valueOf(N1.i * N1.i + N1.j * N1.j + N1.k * N1.k + N1.l * N1.l)
                    + "\t\t\t" + String.valueOf(U1.x) + "\t\t\t" + String.valueOf(U1.y) + "\t\t\t" + String.valueOf(U1.z)
                    + "\t\t\t" + String.valueOf(U1.vx) + "\t\t\t" + String.valueOf(U1.vy) + "\t\t\t" + String.valueOf(U1.vz)
                    + "\t\t\t" + String.valueOf(N2.i) + "\t\t\t" + String.valueOf(N2.j) + "\t\t\t" + String.valueOf(N2.k) + "\t\t\t" + String.valueOf(N2.l)
                    + "\t\t\t" + String.valueOf(Q2.wx) + "\t\t\t" + String.valueOf(Q2.wy) + "\t\t\t" + String.valueOf(Q2.wz)
//                    + "\t\t\t" + String.valueOf(N2.i * N2.i + N2.j * N2.j + N2.k * N2.k + N2.l * N2.l)
                    + "\t\t\t" + String.valueOf(U2.x) + "\t\t\t" + String.valueOf(U2.y) + "\t\t\t" + String.valueOf(U2.z)
                    + "\t\t\t" + String.valueOf(U2.vx) + "\t\t\t" + String.valueOf(U2.vy) + "\t\t\t" + String.valueOf(U2.vz)
                    + "\n";
            write(qfileName.getName(), qtext);

            String text = String.valueOf(U1.x) + "\t\t\t" + String.valueOf(U1.y) + "\t\t\t" + String.valueOf(U1.z)
                    + "\t\t\t" + String.valueOf(U1.vx) + "\t\t\t" + String.valueOf(U1.vy) + "\t\t\t" + String.valueOf(U1.vz)
                    + "\t\t\t" + String.valueOf(U2.x) + "\t\t\t" + String.valueOf(U2.y) + "\t\t\t" + String.valueOf(U2.z)
                    + "\t\t\t" + String.valueOf(U2.vx) + "\t\t\t" + String.valueOf(U2.vy) + "\t\t\t" + String.valueOf(U2.vz)
//                    + "\t\t\t" + String.valueOf(Math.sqrt(U1.x * U1.x + U1.y * U1.y + U1.z * U1.z))
//                    + "\t\t\t" + String.valueOf(Math.sqrt(U2.x * U2.x + U2.y * U2.y + U2.z * U2.z))
                    + "\n";
            write(fileName.getName(), text);
        }

        Collections.addAll(resList, x1List, y1List, z1List, v1xList, v1yList, v1zList,
                x2List, y2List, z2List, v2xList, v2yList, v2zList);

        return (resList);
    }

    public static void write(String fileName, String text) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(text);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static double getJ1xx() {
        return i1x;
    }

    public static double getJ1yy() {
        return i1y;
    }

    public static double getJ1zz() {
        return i1z;
    }

    public static double getJ2xx() {
        return i2x;
    }

    public static double getJ2yy() {
        return i2y;
    }

    public static double getJ2zz() {
        return i2z;
    }
}
