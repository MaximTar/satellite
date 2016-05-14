package main.java.utils;

import java.util.ArrayList;

public class Matrix {
    ArrayList<ArrayList<Double>> arr;

    public Matrix(ArrayList<ArrayList<Double>> arr) {
        this.arr = arr;
    }

    public static ArrayList<ArrayList<Double>> matrixMult(ArrayList<ArrayList<Double>> a, ArrayList<ArrayList<Double>> b) {

        try {
            assert a.get(0).size() == b.size();
        } catch (Exception e) {
            System.out.println("Number of columns in first matrix is not equal to the number of rows in second matrix");
        }

        ArrayList<ArrayList<Double>> resultMatrix = new ArrayList<>();

        for (int i = 0; i < a.size(); i++) {
            ArrayList<Double> line = new ArrayList<>();
            for (int j = 0; j < b.get(0).size(); j++) {
                double c = elementSum(a, b, i, j);
                line.add(c);
            }
            resultMatrix.add(line);
        }

        return resultMatrix;
    }

    public static double elementSum(ArrayList<ArrayList<Double>> a, ArrayList<ArrayList<Double>> b, int i, int j) {

        double c = 0;

        for (int r = 0; r < a.get(0).size(); r++) {
            c = c + a.get(i).get(r) * b.get(r).get(j);
        }

        return c;
    }

    public static ArrayList<ArrayList<Double>> transpose(ArrayList<ArrayList<Double>> a) {

        ArrayList<ArrayList<Double>> b = new ArrayList<>();

        for (int j = 0; j < a.get(0).size(); j++) {
            ArrayList<Double> line = new ArrayList<>();
            for (int i = 0; i < a.size(); i++) {
                line.add(a.get(i).get(j));
            }
            b.add(line);
        }
        return b;
    }

    public static ArrayList<ArrayList<Double>> identityMatrix(double i) {

        ArrayList<ArrayList<Double>> a = new ArrayList<>();

        for (int j = 0; j < i; j++) {
            ArrayList<Double> line = new ArrayList<>();
            for (int k = 0; k < i; k++) {
                if (k == j) {
                    line.add(1.0);
                } else {
                    line.add(0.0);
                }
            }
            a.add(line);
        }

        return a;
    }
}
