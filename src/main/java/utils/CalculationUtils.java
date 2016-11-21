package utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalculationUtils {

    public double x, y, z, vx, vy, vz;
    public static File fileName;
    public static File qfileName;
    public static File fileNameFull;

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

    public static CalculationUtils F(CalculationUtils U, double t) {
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
//        List<Double> GeoPotential = GeoPot.calc(U.x, U.y, U.z, 4);
//        res.vx = GeoPotential.get(0);
//        res.vy = GeoPotential.get(1);
//        res.vz = GeoPotential.get(2);

//        Добавки (гравитация Солнца и Луны, солнечное давление и атмосфера)
//        double muSun = 132712440018E9;
//        double muMoon = 4902.779E9;
//        TimeZone timeZone = TimeZone.getTimeZone("GMT");
//        Calendar c = Calendar.getInstance(timeZone);
//        List<Double> sunPos = SunPosition.sunPosition(c);
//        List<Double> moonPos = MoonPosition.moonPosition(c);
//        List<Double> sunGravity = Gravitation.force(muSun, U.x, U.y, U.z, sunPos.get(0), sunPos.get(1), sunPos.get(2));
//        List<Double> moonGravity = Gravitation.force(muMoon, U.x, U.y, U.z, moonPos.get(0), moonPos.get(1), moonPos.get(2));
//        res.vx += sunGravity.get(0);
//        res.vy += sunGravity.get(1);
//        res.vz += sunGravity.get(2);
//        res.vx += moonGravity.get(0);
//        res.vy += moonGravity.get(1);
//        res.vz += moonGravity.get(2);
//
//        double A = 1; // Площадь сечения
//        double C = 1; // Передача импульса за счёт поглощения и отражения
//        List<Double> sunPres = SunRadiationPressure.force(A, C, c);
//        res.vx += sunPres.get(0);
//        res.vy += sunPres.get(1);
//        res.vz += sunPres.get(2);
//
//        // List<Double> lla = ECEF2LLA.conversion(U.x, U.y, U.z);
//        double drag_coefficient = 2; // "Эмпирический коэффициент примерно равный 2"
//        ArrayList<Double> w_earth = new ArrayList<>();
//        Collections.addAll(w_earth, 0., 0., 7.2921158553E-5);
//        ArrayList<Double> r = new ArrayList<>();
//        Collections.addAll(r, U.x, U.y, U.z);
//        List<Double> v_earth = VectorsAlgebra.multV(w_earth, r);// Поправка для нахождения относительной скорости
//        double radv = Math.sqrt(Math.pow(U.x, 2) + Math.pow(U.y, 2) + Math.pow(U.z, 2)); // Радиус-вектор
//
//        res.vx += Drag.force(drag_coefficient, Drag.exponentialModelDensity(radv), U.vx - v_earth.get(0), A);
//        res.vy += Drag.force(drag_coefficient, Drag.exponentialModelDensity(radv), U.vy - v_earth.get(1), A);
//        res.vz += Drag.force(drag_coefficient, Drag.exponentialModelDensity(radv), U.vz - v_earth.get(2), A);

        return (res);
    }

    public static List<List<Double>> calculate(double tM, double dtM, double tMaxM, double xM, double yM, double zM, double VxM, double VyM, double VzM,
                                               double l0, double l1, double l2, double l3, double wx, double wy, double wz) {
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
        fileName = new File(dateFormat.format(d) + ".txt");
        fileNameFull = new File(dateFormat.format(d) + "FULL.txt");

        // Quaternions part
        ArrayList<Double> iList = new ArrayList<>();
        ArrayList<Double> jList = new ArrayList<>();
        ArrayList<Double> kList = new ArrayList<>();
        ArrayList<Double> lList = new ArrayList<>();
        ArrayList<Double> wxList = new ArrayList<>();
        ArrayList<Double> wyList = new ArrayList<>();
        ArrayList<Double> wzList = new ArrayList<>();
        Quaternion Q = new Quaternion(l0, l1, l2, l3, wx, wy, wz, 0);
        Quaternion q1, q2, q3, q4;
        qfileName = new File("Quaternion_" + dateFormat.format(d) + ".txt");

        while (tM <= tMaxM) {
//            System.out.println(tM);
            k1 = mult(F(U, tM), dtM);
            k2 = mult(F(sum(U, mult(0.5, k1)), tM + 0.5 * dtM), dtM);
            k3 = mult(F(sum(U, mult(0.5, k2)), tM + 0.5 * dtM), dtM);
            k4 = mult(F(sum(U, k3), tM + dtM), dtM);
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
            //Quaternion N = Q;

            String qtext = String.valueOf(N.i) + "\t\t\t" + String.valueOf(N.j) + "\t\t\t" + String.valueOf(N.k) + "\t\t\t" + String.valueOf(N.l)
//                    + "\n";
            + "\t\t\t" + String.valueOf(Q.wx) + "\t\t\t" + String.valueOf(Q.wy) + "\t\t\t" + String.valueOf(Q.wz)
//                    + "\n";
                    + "\t\t\t" + String.valueOf(N.i * N.i + N.j * N.j + N.k * N.k + N.l * N.l) + "\n";
            write(qfileName.getName(), qtext);

            String text = String.valueOf(U.x) + "\t\t\t" + String.valueOf(U.y) + "\t\t\t" + String.valueOf(U.z) + "\t\t\t" + String.valueOf(U.vx) + "\t\t\t" + String.valueOf(U.vy) + "\t\t\t" + String.valueOf(U.vz) + "\n";
            write(fileName.getName(), text);
        }

        Collections.addAll(resList, xList, yList, zList, vxList, vyList, vzList);
//        resList.add(xList);
//        resList.add(yList);
//        resList.add(zList);
//        resList.add(vxList);
//        resList.add(vyList);
//        resList.add(vzList);
        return (resList);
    }

    public static void write(String fileName, String text) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(text);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
