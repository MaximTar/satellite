// todo check if unused

//package utils;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * Created by Maxim Tarasov on 14.08.2016.
// */
//public class VectorsAlgebra {
//
//    public static double absoluteValue(List<Double> r) {
//        double abs = 0;
//        for (Double i : r) {
//            abs += i * i;
//        }
//        return Math.sqrt(abs);
//    }
//
//    public static double absoluteValue(double[] r) {
//        double abs = 0;
//        for (Double i : r) {
//            abs += i * i;
//        }
//        return Math.sqrt(abs);
//    }
//
//    public static List<Double> normalize(List<Double> r) {
//        List<Double> norm = new ArrayList<>();
//        double abs = absoluteValue(r);
//        norm.addAll(r.stream().map(i -> i / abs).collect(Collectors.toList()));
//        return norm;
//    }
//
//    public static double[] normalize(double[] r) {
//        double[] norm = new double[3];
//        double abs = absoluteValue(r);
//        for (int i = 0; i < r.length; i++) {
//            norm[i] = r[i] / abs;
//        }
//        return norm;
//    }
//
//    public static List<Double> difference(List<Double> r1, List<Double> r2) {
//        List<Double> r_diff = new ArrayList<>();
//        assert r1.size() == r2.size();
//        for (int i = 0; i < r1.size(); i++) {
//            r_diff.add(r1.get(i) - r2.get(i));
//        }
//        return r_diff;
//    }
//
//    public static double[] difference(double[] r1, double[] r2) {
//        double[] r_diff = new double[3];
//        assert r1.length == r2.length;
//        for (int i = 0; i < r1.length; i++) {
//            r_diff[i] = r1[i] - r2[i];
//        }
//        return r_diff;
//    }
//
//    public static List<Double> constMult(double c, List<Double> r) {
//        return r.stream().map(i -> i * c).collect(Collectors.toList());
//    }
//
//    public static double[] constMult(double c, double[] r) {
//        double[] cm = new double[3];
//        for (int i = 0; i < r.length; i++) {
//            cm[i] = r[i] * c;
//        }
//        return cm;
//    }
//
//    public static List<Double> multV(List<Double> a, List<Double> b) {
//        ArrayList<Double> r = new ArrayList<>();
//        double r_x = a.get(1) * b.get(2) - a.get(2) * b.get(1);
//        double r_y = a.get(2) * b.get(0) - a.get(0) * b.get(2);
//        double r_z = a.get(0) * b.get(1) - a.get(1) * b.get(0);
//        Collections.addAll(r, r_x, r_y, r_z);
//
//        return r;
//    }
//
//    public static double multS(List<Double> a, List<Double> b) {
//        return a.get(0) * b.get(0) + a.get(1) * b.get(1) + a.get(2) * b.get(2);
//    }
//
//    public static List<Double> sum(List<Double> a, List<Double> b) {
//        List<Double> c = new ArrayList<>();
//        Collections.addAll(c, a.get(0) + b.get(0), a.get(1) + b.get(1), a.get(2) + b.get(2));
//        return c;
//    }
//
//    public static List<Double> invert(List<Double> a) {
//        return a.stream().map(anA -> -anA).collect(Collectors.toList());
//    }
//}
