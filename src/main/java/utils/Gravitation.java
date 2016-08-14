package main.java.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Maxim Tarasov on 14.08.2016.
 */
public class Gravitation {

    public static List<Double> force(double mu, List<Double> r, List<Double> r_sigma) {
        List<Double> r_diff = difference(r_sigma, r);
        return constMult(mu, difference(normalize(r_diff),normalize(r_sigma)));
    }

    private static double absoluteValue(List<Double> r) {
        double abs = 0;
        for (Double i : r) {
            abs = i * i;
        }
        return abs;
    }

    private static List<Double> normalize (List<Double> r) {
        List<Double> norm = new ArrayList<>();
        double abs = absoluteValue(r);
        norm.addAll(r.stream().map(i -> i / abs).collect(Collectors.toList()));
        return norm;
    }

    private static List<Double> difference (List<Double> r1, List<Double> r2) {
        List<Double> r_diff = new ArrayList<>();
        assert r1.size() == r2.size();
        for (int i = 0; i < r1.size(); i++) {
            r_diff.add(r1.get(i) - r2.get(i));
        }
        return r_diff;
    }

    private static List<Double> constMult (double c, List<Double> r) {
        return r.stream().map(i -> i * c).collect(Collectors.toList());
    }
}
