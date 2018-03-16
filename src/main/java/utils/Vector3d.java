package utils;

import java.util.*;

/**
 * Created by Maxim Tarasov on 14.08.2016.
 */
public class Vector3d {

    private double x;
    private double y;
    private double z;
    private double[] vector = new double[3];

    public Vector3d() {
    }

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.vector = new double[]{x, y, z};
    }

    public Vector3d(double[] doubleArray) {
        if (doubleArray.length != 3) {
            System.out.println("Dimension Mismatch");
        } else {
            this.x = doubleArray[0];
            this.y = doubleArray[1];
            this.z = doubleArray[2];
            this.vector = new double[]{x, y, z};
        }
    }

    public Vector3d(List<Double> doubleList) {
        if (doubleList.size() != 3) {
            System.out.println("Dimension Mismatch");
        } else {
            this.x = doubleList.get(0);
            this.y = doubleList.get(1);
            this.z = doubleList.get(2);
            this.vector = new double[]{x, y, z};
        }
    }

    //    public static double absoluteValue(List<Double> r) {
//        double abs = 0;
//        for (Double i : r) {
//            abs += i * i;
//        }
//        return Math.sqrt(abs);
//    }

    public static double absoluteValue(Vector3d vector3d) {
        double abs = 0;
        for (int i = 0; i < vector3d.vector.length; i++) {
            abs += vector3d.vector[i] * vector3d.vector[i];
        }
        return Math.sqrt(abs);
    }

//    public static List<Double> normalizeStream(List<Double> r) {
//        List<Double> norm = new ArrayList<>();
//        double abs = absoluteValue(r);
//        norm.addAll(r.stream().map(i -> i / abs).collect(Collectors.toList()));
//        return norm;
//    }
//
//    public static List<Double> normalizeForEach(List<Double> r) {
//        List<Double> norm = new ArrayList<>();
//        double abs = absoluteValue(r);
//        r.forEach(i -> norm.add(i / abs));
//        return norm;
//    }

//    public static List<Double> normalize(List<Double> r) {
//        List<Double> norm = new ArrayList<>();
//        Double abs = absoluteValue(r);
//        for (Double i : r) {
//            norm.add(i / abs);
//        }
//        return norm;
//    }

    public static Vector3d normalize(Vector3d vector3d) {
        Vector3d norm = new Vector3d();
        double abs = absoluteValue(vector3d);
        for (int i = 0; i < vector3d.vector.length; i++) {
            norm.vector[i] = vector3d.vector[i] / abs;
        }
        return norm;
    }

//    public static List<Double> difference(List<Double> r1, List<Double> r2) {
//        List<Double> r_diff = new ArrayList<>();
//        assert r1.size() == r2.size();
//        for (int i = 0; i < r1.size(); i++) {
//            r_diff.add(r1.get(i) - r2.get(i));
//        }
//        return r_diff;
//    }

    public static Vector3d difference(Vector3d vector3da, Vector3d vector3db) {
        Vector3d r_diff = new Vector3d();
        assert vector3da.vector.length == vector3db.vector.length;
        for (int i = 0; i < vector3da.vector.length; i++) {
            r_diff.vector[i] = vector3da.vector[i] - vector3db.vector[i];
        }
        return r_diff;
    }

//    public static List<Double> constMult(Double c, List<Double> r) {
//        List<Double> res = new ArrayList<>();
//        for (Double i : r) {
//            res.add(i * c);
//        }
//        return res;
//    }

//    public static List<Double> constMultForEach(double c, List<Double> r) {
//        List<Double> res = new ArrayList<>();
//        r.forEach(i -> res.add(i * c));
//        return res;
//    }
//
//    public static List<Double> constMultStream(double c, List<Double> r) {
//        return r.stream().map(i -> i * c).collect(Collectors.toList());
//    }

    public static Vector3d constMult(double constant, Vector3d vector3d) {
        Vector3d cm = new Vector3d();
        for (int i = 0; i < vector3d.vector.length; i++) {
            cm.vector[i] = vector3d.vector[i] * constant;
        }
        return cm;
    }

//    public static List<Double> multV(List<Double> a, List<Double> b) {
//        ArrayList<Double> r = new ArrayList<>();
//        double r_x = a.get(1) * b.get(2) - a.get(2) * b.get(1);
//        double r_y = a.get(2) * b.get(0) - a.get(0) * b.get(2);
//        double r_z = a.get(0) * b.get(1) - a.get(1) * b.get(0);
//        Collections.addAll(r, r_x, r_y, r_z);
//
//        return r;
//    }

    public static Vector3d multV(Vector3d vector3da, Vector3d vector3db) {
        double r_x = vector3da.y * vector3db.z - vector3da.z * vector3db.y;
        double r_y = vector3da.z * vector3db.x - vector3da.x * vector3db.z;
        double r_z = vector3da.x * vector3db.y - vector3da.y * vector3db.x;

        return new Vector3d(r_x, r_y, r_z);
    }

//    public static double multS(List<Double> a, List<Double> b) {
//        return a.get(0) * b.get(0) + a.get(1) * b.get(1) + a.get(2) * b.get(2);
//    }

    public static double multS(Vector3d vector3da, Vector3d vector3db) {
        return vector3da.x * vector3db.x + vector3da.y * vector3db.y + vector3da.z * vector3db.z;
    }

//    public static List<Double> sum(List<Double> a, List<Double> b) {
//        List<Double> c = new ArrayList<>();
//        Collections.addAll(c, a.get(0) + b.get(0), a.get(1) + b.get(1), a.get(2) + b.get(2));
//        return c;
//    }

    public static Vector3d sum(Vector3d vector3da, Vector3d vector3db) {
        return new Vector3d(vector3da.x + vector3db.x, vector3da.y + vector3db.y, vector3da.z + vector3db.z);
    }

//    public static List<Double> invert(List<Double> a) {
//        return a.stream().map(anA -> -anA).collect(Collectors.toList());
//    }

    public static Vector3d invert(Vector3d vector3d) {
        return new Vector3d(-vector3d.x, -vector3d.y, -vector3d.z);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "Vector3d{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", vector=" + Arrays.toString(vector) +
                '}';
    }
}
