package main.java.utils;

import java.util.List;

/**
 * Created by Maxim Tarasov on 14.08.2016.
 */
public class Gravitation {

    public static List<Double> force(double mu, List<Double> r, List<Double> r_sigma) {
        List<Double> r_diff = VectorsAlgebra.difference(r_sigma, r);
        return VectorsAlgebra.constMult(mu, VectorsAlgebra.difference(VectorsAlgebra.normalize(r_diff), VectorsAlgebra.normalize(r_sigma)));
    }
}
