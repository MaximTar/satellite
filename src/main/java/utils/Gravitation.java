package utils;

/**
 * Created by Maxim Tarasov on 14.08.2016.
 */
public class Gravitation {

//    public static List<Double> force(double mu, List<Double> r, List<Double> r_sigma) {
//        List<Double> r_diff = VectorAlgebra.difference(r_sigma, r);
//        return VectorAlgebra.constMult(mu, VectorAlgebra.difference(
//                VectorAlgebra.constMult(Math.pow(VectorAlgebra.absoluteValue(r_diff), -2), VectorAlgebra.normalize(r_diff)),
//                VectorAlgebra.constMult(Math.pow(VectorAlgebra.absoluteValue(r_sigma), -2), VectorAlgebra.normalize(r_sigma))));
//    }

    public static double[] force(double mu, double[] r, double[] r_sigma) {
        double[] r_diff = VectorAlgebra.difference(r_sigma, r);
        return VectorAlgebra.constMult(mu, VectorAlgebra.difference(
                VectorAlgebra.constMult(Math.pow(VectorAlgebra.absoluteValue(r_diff), -2), VectorAlgebra.normalize(r_diff)),
                VectorAlgebra.constMult(Math.pow(VectorAlgebra.absoluteValue(r_sigma), -2), VectorAlgebra.normalize(r_sigma))));
    }

    public static double[] force(double mu, double r_x, double r_y, double r_z, double r_sigma_x, double r_sigma_y, double r_sigma_z) {
        double[] r = new double[]{r_x, r_y, r_z};
        double[] rSigma = new double[]{r_sigma_x, r_sigma_y, r_sigma_z};
//        ArrayList<Double> r = new ArrayList<Double>() {{
//            add(r_x);
//            add(r_y);
//            add(r_z);
//        }};
//        ArrayList<Double> r_sigma = new ArrayList<Double>() {{
//            add(r_sigma_x);
//            add(r_sigma_y);
//            add(r_sigma_z);
//        }};
//        List<Double> r_diff = VectorAlgebra.difference(r_sigma, r);
//        return VectorAlgebra.constMult(mu, VectorAlgebra.difference(VectorAlgebra.normalize(r_diff), VectorAlgebra.normalize(r_sigma)));
        return force(mu, r, rSigma);
    }
}
