package utils;

import model.StateVector;

import java.util.*;

/**
 * Created by Maxim Tarasov on 06.10.2016.
 */
public class Quaternion {

    public double qw, qx, qy, qz, wx, wy, wz, M;

    public Quaternion(double qw, double qx, double qy, double qz) {
        this.qw = qw;
        this.qx = qx;
        this.qy = qy;
        this.qz = qz;
    }

    public Quaternion(double qw, double qx, double qy, double qz, double wx, double wy, double wz) {
        this.qw = qw;
        this.qx = qx;
        this.qy = qy;
        this.qz = qz;
        this.wx = wx;
        this.wy = wy;
        this.wz = wz;
    }

    public Quaternion(double qw, double qx, double qy, double qz, double wx, double wy, double wz, double M) {
        this.qw = qw;
        this.qx = qx;
        this.qy = qy;
        this.qz = qz;
        this.wx = wx;
        this.wy = wy;
        this.wz = wz;
        this.M = M;
    }

    public Quaternion(double qw, List<Double> a) {
        this.qw = qw;
        this.qx = a.get(0);
        this.qy = a.get(1);
        this.qz = a.get(2);
    }

    public Quaternion(double qw, double[] a) {
        this.qw = qw;
        this.qx = a[0];
        this.qy = a[1];
        this.qz = a[2];
    }

    public static Quaternion sum(Quaternion a, Quaternion b) {
        Quaternion c = new Quaternion(0, 0, 0, 0, 0, 0, 0);
        c.qw = a.qw + b.qw;
        c.qx = a.qx + b.qx;
        c.qy = a.qy + b.qy;
        c.qz = a.qz + b.qz;
        c.wx = a.wx + b.wx;
        c.wy = a.wy + b.wy;
        c.wz = a.wz + b.wz;
        return c;
    }

    public static Quaternion mult(double a, Quaternion b) {
        Quaternion c = new Quaternion(0, 0, 0, 0, 0, 0, 0);
        c.qw = a * b.qw;
        c.qx = a * b.qx;
        c.qy = a * b.qy;
        c.qz = a * b.qz;
        c.wx = a * b.wx;
        c.wy = a * b.wy;
        c.wz = a * b.wz;
        return c;
    }

//    public static Quaternion quatMultQuat(Quaternion a, Quaternion b) {
//        Quaternion c = new Quaternion(0, 0, 0, 0);
//        ArrayList<Double> va = new ArrayList<>();
//        ArrayList<Double> vb = new ArrayList<>();
//        List<Double> vc;
//        Collections.addAll(va, a.qx, a.qy, a.qz);
//        Collections.addAll(vb, b.qx, b.qy, b.qz);
//        c.qw = a.qw * b.qw - VectorAlgebra.multS(va, vb);
//        vc = VectorAlgebra.sum(VectorAlgebra.sum(VectorAlgebra.constMult(a.qw, vb), VectorAlgebra.constMult(b.qw, va)),
//                VectorAlgebra.multV(va, vb));
//        c.qx = vc.get(0);
//        c.qy = vc.get(1);
//        c.qz = vc.get(2);
//        return c;
//    }

    public static Quaternion quatMultQuat(Quaternion a, Quaternion b) {
        Quaternion c = new Quaternion(0, 0, 0, 0);
        double[] va = new double[]{a.qx, a.qy, a.qz};
        double[] vb = new double[]{b.qx, b.qy, b.qz};

        c.qw = a.qw * b.qw - VectorAlgebra.multS(va, vb);
        double[] vc = VectorAlgebra.sum(VectorAlgebra.sum(VectorAlgebra.constMult(a.qw, vb), VectorAlgebra.constMult(b.qw, va)),
                VectorAlgebra.multV(va, vb));
        c.qx = vc[0];
        c.qy = vc[1];
        c.qz = vc[2];
        return c;
    }


    public static Quaternion normalize(Quaternion q) {
        return new Quaternion(q.qw / Math.sqrt(q.qw * q.qw + q.qx * q.qx + q.qy * q.qy + q.qz * q.qz),
                q.qx / Math.sqrt(q.qw * q.qw + q.qx * q.qx + q.qy * q.qy + q.qz * q.qz),
                q.qy / Math.sqrt(q.qw * q.qw + q.qx * q.qx + q.qy * q.qy + q.qz * q.qz),
                q.qz / Math.sqrt(q.qw * q.qw + q.qx * q.qx + q.qy * q.qy + q.qz * q.qz));
    }

    public static Quaternion conjugate(Quaternion q) {
        return new Quaternion(q.qw, -q.qx, -q.qy, -q.qz);
    }

    public static ArrayList<ArrayList<Double>> rotMatrix(Quaternion q) {
        Quaternion intermediate = normalize(q);
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        ArrayList<Double> line1 = new ArrayList<>();
        ArrayList<Double> line2 = new ArrayList<>();
        ArrayList<Double> line3 = new ArrayList<>();

        double a11 = 1 - 2 * intermediate.qy * intermediate.qy - 2 * intermediate.qz * intermediate.qz;
        double a12 = 2 * intermediate.qx * intermediate.qy - 2 * intermediate.qw * intermediate.qz;
        double a13 = 2 * intermediate.qx * intermediate.qz + 2 * intermediate.qw * intermediate.qy;
        double a21 = 2 * intermediate.qx * intermediate.qy + 2 * intermediate.qw * intermediate.qz;
        double a22 = 1 - 2 * intermediate.qx * intermediate.qx - 2 * intermediate.qz * intermediate.qz;
        double a23 = 2 * intermediate.qy * intermediate.qz - 2 * intermediate.qw * intermediate.qx;
        double a31 = 2 * intermediate.qx * intermediate.qz - 2 * intermediate.qw * intermediate.qy;
        double a32 = 2 * intermediate.qy * intermediate.qz + 2 * intermediate.qw * intermediate.qx;
        double a33 = 1 - 2 * intermediate.qx * intermediate.qx - 2 * intermediate.qy * intermediate.qy;

        Collections.addAll(line1, a11, a12, a13);
        Collections.addAll(line2, a21, a22, a23);
        Collections.addAll(line3, a31, a32, a33);
        Collections.addAll(matrix, line1, line2, line3);
        return matrix;
    }

    public static ArrayList<ArrayList<Double>> mavlink_quaternion_to_dcm(Quaternion q) {
        Quaternion intermediate = normalize(q);
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        ArrayList<Double> line1 = new ArrayList<>();
        ArrayList<Double> line2 = new ArrayList<>();
        ArrayList<Double> line3 = new ArrayList<>();

        double a = intermediate.qw;
        double b = intermediate.qx;
        double c = intermediate.qy;
        double d = intermediate.qz;
        double aSq = a * a;
        double bSq = b * b;
        double cSq = c * c;
        double dSq = d * d;
        double a11 = aSq + bSq - cSq - dSq;
        double a12 = 2 * (b * c - a * d);
        double a13 = 2 * (a * c + b * d);
        double a21 = 2 * (b * c + a * d);
        double a22 = aSq - bSq + cSq - dSq;
        double a23 = 2 * (c * d - a * b);
        double a31 = 2 * (b * d - a * c);
        double a32 = 2 * (a * b + c * d);
        double a33 = aSq - bSq - cSq + dSq;

        Collections.addAll(line1, a11, a12, a13);
        Collections.addAll(line2, a21, a22, a23);
        Collections.addAll(line3, a31, a32, a33);
        Collections.addAll(matrix, line1, line2, line3);
        return matrix;
    }

    public static ArrayList<Double> mavlink_dcm_to_euler(ArrayList<ArrayList<Double>> matrix) {
        double phi, theta, psi;
        theta = Math.asin(-matrix.get(2).get(0));

        if (Math.abs(theta - Math.PI / 2) < 1.0e-3f) {
            phi = 0.0;
            psi = (Math.atan2(matrix.get(1).get(2) - matrix.get(0).get(1),
                    matrix.get(0).get(2) + matrix.get(1).get(1)) + phi);

        } else if (Math.abs(theta + Math.PI / 2) < 1.0e-3f) {
            phi = 0.0f;
            psi = Math.atan2(matrix.get(1).get(2) - matrix.get(0).get(1),
                    matrix.get(0).get(2) + matrix.get(1).get(1) - phi);

        } else {
            phi = Math.atan2(matrix.get(2).get(1), matrix.get(2).get(2));
            psi = Math.atan2(matrix.get(1).get(0), matrix.get(0).get(0));
        }

        ArrayList<Double> res = new ArrayList<>();
        Collections.addAll(res, phi, psi, theta);

        return res;
    }

    public static ArrayList<Double> mavlink_quaternion_to_euler(Quaternion q) {
        return mavlink_dcm_to_euler(mavlink_quaternion_to_dcm(q));
    }


    public static ArrayList<ArrayList<Double>> calculation(ArrayList<ArrayList<Double>> stateVector, ArrayList<ArrayList<Double>> bodyPosition) {
        ArrayList<ArrayList<Double>> bodyPositionData = new ArrayList<>();
        for (ArrayList<Double> element : stateVector) {
            ArrayList<ArrayList<Double>> newBodyPosition = new ArrayList<>();

            Quaternion q = new Quaternion(element.get(0), element.get(1), element.get(2), element.get(3));
            ArrayList<ArrayList<Double>> rotateMatrix = rotMatrix(q);

            for (ArrayList<Double> point : bodyPosition) {
                ArrayList<ArrayList<Double>> intermediate = new ArrayList<>();
                intermediate.add(point);
                ArrayList<ArrayList<Double>> newPoint = Matrix.matrixMult(intermediate, rotateMatrix);
                newBodyPosition.add(newPoint.get(0));
            }

//            for (ArrayList<Double> point : newBodyPosition) {
//
//            }

            bodyPositionData.addAll(newBodyPosition);
        }
        return bodyPositionData;
    }

    public static Quaternion F(Quaternion U, double t, StateVector stateVector, Tensor I) {
        double mu = Constant.MU;
        double x = stateVector.x;
        double y = stateVector.y;
        double z = stateVector.z;
        double R = Math.sqrt(x * x + y * y + z * z);

//        Quaternion res = Quaternion.quatMultQuat(new Quaternion(0, U.wx, U.wy, U.wz), U);
        Quaternion res = Quaternion.quatMultQuat(new Quaternion(U.qw, U.qx, U.qy, U.qz), new Quaternion(0, U.wx, U.wy, U.wz));
        res.qw = 0.5 * res.qw;
        res.qx = 0.5 * res.qx;
        res.qy = 0.5 * res.qy;
        res.qz = 0.5 * res.qz;
        res.wx = (1. / I.ix) * (I.iy - I.iz) * U.wy * U.wz;
        res.wy = (1. / I.iy) * (I.iz - I.ix) * U.wx * U.wz;
        res.wz = (1. / I.iz) * (I.ix - I.iy) * U.wx * U.wy;

        // Гравитационный момент
        double koeff = 3 * mu / Math.pow(R, 3);
        // Er
        double[] er = new double[]{-x / R, -y / R, -z / R};
        er = VectorAlgebra.normalize(er);

        // L*Er*~L
        Quaternion erq = new Quaternion(0, er);
        Quaternion L = new Quaternion(U.qw, U.qx, U.qy, U.qz);
        L = Quaternion.conjugate(L);

        Quaternion erf = Quaternion.quatMultQuat(Quaternion.quatMultQuat(L, erq), Quaternion.conjugate(L));

        er[0] = erf.qx;
        er[1] = erf.qy;
        er[2] = erf.qz;
        er = VectorAlgebra.normalize(er);

        // J*Er
        double[] Jer = new double[]{er[0] * I.ix, er[1] * I.iy, er[2] * I.iz};

        // Er x J*Er
        double[] quaziM = VectorAlgebra.multV(er, Jer);
        double[] M = VectorAlgebra.constMult(koeff, quaziM);

        res.wx += (1. / I.ix) * M[0];
        res.wy += (1. / I.iy) * M[1];
        res.wz += (1. / I.iz) * M[2];
        return res;
    }

    public static Quaternion F2(Quaternion U, double t, StateVector stateVector, Tensor I) {
        Quaternion res = F(U, t, stateVector, I);

        //TODO FIXME FOR RELATIVE MOTION
//        List<Double> attachmentPoint = new ArrayList<>(Arrays.asList(10., 0., 0.));
//
//        if (MainForcesAbsolute.tensionFFlag) {
//            // Перевод force(tension) ECI -> Body
//            List<Double> tensionFBody = new ArrayList<>();
//            List<Double> tensionFList = new ArrayList<>(Arrays.asList(MainForcesRelative.tensionF[0], MainForcesRelative.tensionF[1], MainForcesRelative.tensionF[2]));
//            Quaternion tensionFECI = new Quaternion(0, tensionFList);
//            Quaternion L = new Quaternion(U.qw, U.qx, U.qy, U.qz);
//            L = Quaternion.conjugate(L);
//            Quaternion tensionFBodyQ = Quaternion.quatMultQuat(Quaternion.quatMultQuat(L, tensionFECI), Quaternion.conjugate(L));
////            tensionFBody.clear();
//            Collections.addAll(tensionFBody, tensionFBodyQ.qx, tensionFBodyQ.qy, tensionFBodyQ.qz);
//
//            List<Double> tensionM = VectorAlgebra.multV(attachmentPoint, tensionFBody);
//            res.wx += (1. / I.ix) * tensionM.get(0);
//            res.wy += (1. / I.iy) * tensionM.get(1);
//            res.wz += (1. / I.iz) * tensionM.get(2);
//        }

        return res;
    }

//    @Override
//    public String toString() {
//        return "[" + this.qw + ", " + this.qx + ", " + this.qy + ", " + this.qz + "]";
//    }

    public static Quaternion fromRotationMatrix(List<List<Double>> rm) {

        double qw;
        double qx;
        double qy;
        double qz;

        double m00 = rm.get(0).get(0);
        double m01 = rm.get(0).get(1);
        double m02 = rm.get(0).get(2);
        double m10 = rm.get(1).get(0);
        double m11 = rm.get(1).get(1);
        double m12 = rm.get(1).get(2);
        double m20 = rm.get(2).get(0);
        double m21 = rm.get(2).get(1);
        double m22 = rm.get(2).get(2);

        double tr = m00 + m11 + m22;

        if (tr > 0) {
            double S = Math.sqrt(tr + 1.0) * 2; // S=4*qw
            qw = 0.25 * S;
            qx = (m21 - m12) / S;
            qy = (m02 - m20) / S;
            qz = (m10 - m01) / S;
        } else if ((m00 > m11) & (m00 > m22)) {
            double S = Math.sqrt(1.0 + m00 - m11 - m22) * 2; // S=4*qx
            qw = (m21 - m12) / S;
            qx = 0.25 * S;
            qy = (m01 + m10) / S;
            qz = (m02 + m20) / S;
        } else if (m11 > m22) {
            double S = Math.sqrt(1.0 + m11 - m00 - m22) * 2; // S=4*qy
            qw = (m02 - m20) / S;
            qx = (m01 + m10) / S;
            qy = 0.25 * S;
            qz = (m12 + m21) / S;
        } else {
            double S = Math.sqrt(1.0 + m22 - m00 - m11) * 2; // S=4*qz
            qw = (m10 - m01) / S;
            qx = (m02 + m20) / S;
            qy = (m12 + m21) / S;
            qz = 0.25 * S;
        }

        return new Quaternion(qw, qx, qy, qz);
    }

    public static Quaternion fromRotationMatrix(double m00, double m01, double m02, double m10, double m11,
                                                double m12, double m20, double m21, double m22) {

        double qw;
        double qx;
        double qy;
        double qz;

        double tr = m00 + m11 + m22;

        if (tr > 0) {
            double S = Math.sqrt(tr + 1.0) * 2; // S=4*qw
            qw = 0.25 * S;
            qx = (m21 - m12) / S;
            qy = (m02 - m20) / S;
            qz = (m10 - m01) / S;
        } else if ((m00 > m11) & (m00 > m22)) {
            double S = Math.sqrt(1.0 + m00 - m11 - m22) * 2; // S=4*qx
            qw = (m21 - m12) / S;
            qx = 0.25 * S;
            qy = (m01 + m10) / S;
            qz = (m02 + m20) / S;
        } else if (m11 > m22) {
            double S = Math.sqrt(1.0 + m11 - m00 - m22) * 2; // S=4*qy
            qw = (m02 - m20) / S;
            qx = (m01 + m10) / S;
            qy = 0.25 * S;
            qz = (m12 + m21) / S;
        } else {
            double S = Math.sqrt(1.0 + m22 - m00 - m11) * 2; // S=4*qz
            qw = (m10 - m01) / S;
            qx = (m02 + m20) / S;
            qy = (m12 + m21) / S;
            qz = 0.25 * S;
        }

        return new Quaternion(qw, qx, qy, qz);
    }

    public static Quaternion rm2quaternion(double m00, double m01, double m02, double m10, double m11,
                                           double m12, double m20, double m21, double m22) {
        Quaternion quaternion = new Quaternion(0, 0, 0, 0);
        double tr = m00 + m11 + m22;

        if (tr > 0) {
            double sqtrp1 = Math.sqrt(tr + 1.0);
            quaternion.qw = 0.5 * sqtrp1;
            quaternion.qx = (m12 - m21) / (2.0 * sqtrp1);
            quaternion.qy = (m20 - m02) / (2.0 * sqtrp1);
            quaternion.qz = (m01 - m10) / (2.0 * sqtrp1);
            return quaternion;
        }
        if ((m11 > m00) && (m11 > m22)) {
            double sqdip1 = Math.sqrt(m11 - m00 - m22 + 1.0);
            quaternion.qy = 0.5 * sqdip1;

            if (sqdip1 != 0) {
                sqdip1 = 0.5 / sqdip1;
            }

            quaternion.qw = (m20 - m02) * sqdip1;
            quaternion.qx = (m01 + m10) * sqdip1;
            quaternion.qz = (m12 + m21) * sqdip1;
        } else if (m22 > m00) {
            double sqdip1 = Math.sqrt(m22 - m00 - m11 + 1.0);
            quaternion.qy = 0.5 * sqdip1;

            if (sqdip1 != 0) {
                sqdip1 = 0.5 / sqdip1;
            }


            quaternion.qw = (m01 - m10) * sqdip1;
            quaternion.qx = (m20 + m02) * sqdip1;
            quaternion.qy = (m12 + m21) * sqdip1;
        } else {
            double sqdip1 = Math.sqrt(m00 - m11 - m22 + 1.0);
            quaternion.qx = 0.5 * sqdip1;

            if (sqdip1 != 0) {
                sqdip1 = 0.5 / sqdip1;
            }

            quaternion.qw = (m12 - m21) * sqdip1;
            quaternion.qy = (m01 + m10) * sqdip1;
            quaternion.qy = (m20 + m02) * sqdip1;
        }
        return quaternion;
    }

    @Override
    public String toString() {
        return "Quaternion{" +
                "qw=" + qw +
                ", qx=" + qx +
                ", qy=" + qy +
                ", qz=" + qz +
                ", wx=" + wx +
                ", wy=" + wy +
                ", wz=" + wz +
                ", M=" + M +
                '}';
    }
}
