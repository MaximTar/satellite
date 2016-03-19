package utils;

import calculation.CoordinatePane;
import calculation.Kepler;
import calculation.KeplerPane;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        double G = 6.67 * Math.pow(10, -11);
        double M = 5.9726 * Math.pow(10, 24);
        ArrayList<Double> GeoPotential = GeoPot.calc(U.x, U.y, U.z, 4);
        res.x = U.vx;
        res.y = U.vy;
        res.z = U.vz;
//        Простейшее приближение F = fm/r
//        res.vx = -G * M * U.x / Math.pow(Math.pow(U.x * U.x + U.y * U.y + U.z * U.z, 0.5), 3);
//        res.vy = -G * M * U.y / Math.pow(Math.pow(U.x * U.x + U.y * U.y + U.z * U.z, 0.5), 3);
//        res.vz = -G * M * U.z / Math.pow(Math.pow(U.x * U.x + U.y * U.y + U.z * U.z, 0.5), 3);

//        Приближение по Чазову
        res.vx = GeoPotential.get(0);
        res.vy = GeoPotential.get(1);
        res.vz = GeoPotential.get(2);

        return (res);
    }

    public static ArrayList<ArrayList<Double>> calculate(double tM, double dtM, double tMaxM, double xM, double yM, double zM, double VxM, double VyM, double VzM) {
        ArrayList<Double> X_list = new ArrayList<>();
        ArrayList<Double> Y_list = new ArrayList<>();
        ArrayList<Double> Z_list = new ArrayList<>();
        ArrayList<Double> VX_list = new ArrayList<>();
        ArrayList<Double> VY_list = new ArrayList<>();
        ArrayList<Double> VZ_list = new ArrayList<>();
        ArrayList<ArrayList<Double>> Res_list = new ArrayList<>();
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
            X_list.add(U.x);
            Y_list.add(U.y);
            Z_list.add(U.z);
            VX_list.add(U.vx);
            VY_list.add(U.vy);
            VZ_list.add(U.vz);

            String text = String.valueOf(U.x) + "\t\t\t" + String.valueOf(U.y) + "\t\t\t" + String.valueOf(U.z) + "\t\t\t" + String.valueOf(U.vx) + "\t\t\t" + String.valueOf(U.vy) + "\t\t\t" + String.valueOf(U.vz) + "\n";
            write(fileName.getName(), text);
        }

//        double tFull = 2 * 3.15 * Math.sqrt(xM * xM + yM * yM + zM * zM) / Math.sqrt(VxM * VxM + VyM * VyM + VzM * VzM);
//        System.out.println(tMaxM);
//        System.out.println(tFull);
//        while (tM <= tFull) {
//            k1 = mult(F(U, tM), dtM);
//            k2 = mult(F(sum(U, mult(0.5, k1)), tM + 0.5 * dtM), dtM);
//            k3 = mult(F(sum(U, mult(0.5, k2)), tM + 0.5 * dtM), dtM);
//            k4 = mult(F(sum(U, k3), tM + dtM), dtM);
//            U = sum(U, mult(1.0 / 6.0, sum(sum(k1, mult(2, k2)), sum(mult(2, k3), k4))));
//            tM += dtM;
//            X_list.add(U.x);
//            Y_list.add(U.y);
//            Z_list.add(U.z);
//            VX_list.add(U.vx);
//            VY_list.add(U.vy);
//            VZ_list.add(U.vz);
//
//            String text = String.valueOf(U.x) + "\t\t\t" + String.valueOf(U.y) + "\t\t\t" + String.valueOf(U.z) + "\n";
//            write(fileNameFull.getName(), text);
//        }

        Res_list.add(X_list);
        Res_list.add(Y_list);
        Res_list.add(Z_list);
        Res_list.add(VX_list);
        Res_list.add(VY_list);
        Res_list.add(VZ_list);
        return (Res_list);
    }

    public static void write(String fileName, String text) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(text);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}