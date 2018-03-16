package utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Maxim Tarasov on 14.08.2016.
 */
public class VectorAlgebra {

    public static double absoluteValue(double[] vector3d) {
        double abs = 0;
        abs += vector3d[0] * vector3d[0] + vector3d[1] * vector3d[1] + vector3d[2] * vector3d[2];
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

    public static double[] normalize(double[] vector3d) {
        double[] norm = new double[3];
        double abs = absoluteValue(vector3d);
        for (int i = 0; i < vector3d.length; i++) {
            norm[i] = vector3d[i] / abs;
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

    public static double[] difference(double[] vector3da, double[] vector3db) {
        double[] r_diff = new double[3];
        assert vector3da.length == vector3db.length;
        for (int i = 0; i < vector3da.length; i++) {
            r_diff[i] = vector3da[i] - vector3db[i];
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

    public static double[] constMult(double constant, double[] vector3d) {
        double[] cm = new double[3];
        for (int i = 0; i < vector3d.length; i++) {
            cm[i] = vector3d[i] * constant;
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

    public static double[] multV(double[] vector3da, double[] vector3db) {
        double r_x = vector3da[1] * vector3db[2] - vector3da[2] * vector3db[1];
        double r_y = vector3da[2] * vector3db[0] - vector3da[0] * vector3db[2];
        double r_z = vector3da[0] * vector3db[1] - vector3da[1] * vector3db[0];

        return new double[]{r_x, r_y, r_z};
    }

//    public static double multS(List<Double> a, List<Double> b) {
//        return a.get(0) * b.get(0) + a.get(1) * b.get(1) + a.get(2) * b.get(2);
//    }

    public static double multS(double[] a, double[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

//    public static List<Double> sum(List<Double> a, List<Double> b) {
//        List<Double> c = new ArrayList<>();
//        Collections.addAll(c, a.get(0) + b.get(0), a.get(1) + b.get(1), a.get(2) + b.get(2));
//        return c;
//    }

    public static double[] sum(double[] a, double[] b) {
        return new double[]{a[0] + b[0], a[1] + b[1], a[2] + b[2]};
    }

//    public static List<Double> invert(List<Double> a) {
//        return a.stream().map(anA -> -anA).collect(Collectors.toList());
//    }

    public static double[] invert(double[] a) {
        return new double[]{-a[0], -a[1], -a[2]};
    }
}
