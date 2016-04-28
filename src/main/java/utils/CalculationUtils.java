package main.java.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalculationUtils {

    public double x, y, z, vx, vy, vz;
    public static File fileName;
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

////        Это считает точнее
//        res.vx = -mu * U.x / Math.pow(Math.pow(U.x * U.x + U.y * U.y + U.z * U.z, 0.5), 3);
//        res.vy = -mu * U.y / Math.pow(Math.pow(U.x * U.x + U.y * U.y + U.z * U.z, 0.5), 3);
//        res.vz = -mu * U.z / Math.pow(Math.pow(U.x * U.x + U.y * U.y + U.z * U.z, 0.5), 3);

//        Приближение по Чазову
        List<Double> GeoPotential = GeoPot.calc(U.x, U.y, U.z, 4);
        res.vx = GeoPotential.get(0);
        res.vy = GeoPotential.get(1);
        res.vz = GeoPotential.get(2);

        return (res);
    }

    public static List<List<Double>> calculate(double tM, double dtM, double tMaxM, double xM, double yM, double zM, double VxM, double VyM, double VzM) {
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

        while (tM <= tMaxM) {
            k1 = mult(F(U, tM), dtM);
            k2 = mult(F(sum(U, mult(0.5, k1)), tM + 0.5 * dtM), dtM);
            k3 = mult(F(sum(U, mult(0.5, k2)), tM + 0.5 * dtM), dtM);
            k4 = mult(F(sum(U, k3), tM + dtM), dtM);
            U = sum(U, mult(1.0 / 6.0, sum(sum(k1, mult(2, k2)), sum(mult(2, k3), k4))));
            tM += dtM;
            xList.add(U.x);
            yList.add(U.y);
            zList.add(U.z);
            vxList.add(U.vx);
            vyList.add(U.vy);
            vzList.add(U.vz);

            String text = String.valueOf(U.x) + "\t\t\t" + String.valueOf(U.y) + "\t\t\t" + String.valueOf(U.z) + "\t\t\t" + String.valueOf(U.vx) + "\t\t\t" + String.valueOf(U.vy) + "\t\t\t" + String.valueOf(U.vz) + "\n";
            write(fileName.getName(), text);
        }

        resList.add(xList);
        resList.add(yList);
        resList.add(zList);
        resList.add(vxList);
        resList.add(vyList);
        resList.add(vzList);
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
