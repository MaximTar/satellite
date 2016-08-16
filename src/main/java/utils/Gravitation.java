package main.java.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxim Tarasov on 14.08.2016.
 */
public class Gravitation {

    public static List<Double> force(double mu, List<Double> r, List<Double> r_sigma) {
        List<Double> r_diff = VectorsAlgebra.difference(r_sigma, r);
        return VectorsAlgebra.constMult(mu, VectorsAlgebra.difference(VectorsAlgebra.normalize(r_diff), VectorsAlgebra.normalize(r_sigma)));
    }

    public static List<Double> force(double mu, double r_x, double r_y, double r_z, double r_sigma_x, double r_sigma_y, double r_sigma_z) {
        ArrayList<Double> r = new ArrayList<Double>(){{add(r_x);add(r_y);add(r_z);}};
        ArrayList<Double> r_sigma = new ArrayList<Double>(){{add(r_sigma_x);add(r_sigma_y);add(r_sigma_z);}};
        List<Double> r_diff = VectorsAlgebra.difference(r_sigma, r);
        return VectorsAlgebra.constMult(mu, VectorsAlgebra.difference(VectorsAlgebra.normalize(r_diff), VectorsAlgebra.normalize(r_sigma)));
    }
}
