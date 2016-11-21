package utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Maxim Tarasov on 06.10.2016.
 */
public class Quaternion {

    public double i, j, k, l, wx, wy, wz, M;
    public static File fileName;
    public static double ix = 1;
    public static double iy = 10;
    public static double iz = 10;

    public Quaternion(double i, double j, double k, double l) {
        this.i = i;
        this.j = j;
        this.k = k;
        this.l = l;
    }

    Quaternion(double i, double j, double k, double l, double wx, double wy, double wz) {
        this.i = i;
        this.j = j;
        this.k = k;
        this.l = l;
        this.wx = wx;
        this.wy = wy;
        this.wz = wz;
    }

    Quaternion(double i, double j, double k, double l, double wx, double wy, double wz, double M) {
        this.i = i;
        this.j = j;
        this.k = k;
        this.l = l;
        this.wx = wx;
        this.wy = wy;
        this.wz = wz;
        this.M = M;
    }

    public Quaternion(double i, List<Double> a) {
        this.i = i;
        this.j = a.get(0);
        this.k = a.get(1);
        this.l = a.get(2);
    }

    public static Quaternion sum(Quaternion a, Quaternion b) {
        Quaternion c = new Quaternion(0, 0, 0, 0, 0, 0, 0);
        c.i = a.i + b.i;
        c.j = a.j + b.j;
        c.k = a.k + b.k;
        c.l = a.l + b.l;
        c.wx = a.wx + b.wx;
        c.wy = a.wy + b.wy;
        c.wz = a.wz + b.wz;
        return c;
    }

    public static Quaternion mult(double a, Quaternion b) {
        Quaternion c = new Quaternion(0, 0, 0, 0, 0, 0, 0);
        c.i = a * b.i;
        c.j = a * b.j;
        c.k = a * b.k;
        c.l = a * b.l;
        c.wx = a * b.wx;
        c.wy = a * b.wy;
        c.wz = a * b.wz;
        return c;
    }

    public static Quaternion quatMultQuat(Quaternion a, Quaternion b) {
        Quaternion c = new Quaternion(0, 0, 0, 0);
        ArrayList va = new ArrayList();
        ArrayList vb = new ArrayList();
        List<Double> vc;
        Collections.addAll(va, a.j, a.k, a.l);
        Collections.addAll(vb, b.j, b.k, b.l);
        c.i = a.i * b.i - VectorsAlgebra.multS(va, vb);
        vc = VectorsAlgebra.sum(VectorsAlgebra.sum(VectorsAlgebra.constMult(a.i, vb), VectorsAlgebra.constMult(b.i, va)),
                VectorsAlgebra.multV(va, vb));
        c.j = vc.get(0);
        c.k = vc.get(1);
        c.l = vc.get(2);
        return c;
    }


    public static Quaternion normalize(Quaternion q) {
        return new Quaternion(q.i / Math.sqrt(q.i * q.i + q.j * q.j + q.k * q.k + q.l * q.l),
                q.j / Math.sqrt(q.i * q.i + q.j * q.j + q.k * q.k + q.l * q.l),
                q.k / Math.sqrt(q.i * q.i + q.j * q.j + q.k * q.k + q.l * q.l),
                q.l / Math.sqrt(q.i * q.i + q.j * q.j + q.k * q.k + q.l * q.l));
    }

    public static Quaternion conjugate(Quaternion q) {
        return new Quaternion(q.i, -q.j, -q.k, -q.l);
    }

    public static ArrayList<ArrayList<Double>> rotMatrix(Quaternion q) {
        Quaternion intermediate = normalize(q);
        ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
        ArrayList<Double> line1 = new ArrayList<>();
        ArrayList<Double> line2 = new ArrayList<>();
        ArrayList<Double> line3 = new ArrayList<>();

        double a11 = 1 - 2 * intermediate.k * intermediate.k - 2 * intermediate.l * intermediate.l;
        double a12 = 2 * intermediate.j * intermediate.k - 2 * intermediate.i * intermediate.l;
        double a13 = 2 * intermediate.j * intermediate.l + 2 * intermediate.i * intermediate.k;
        double a21 = 2 * intermediate.j * intermediate.k + 2 * intermediate.i * intermediate.l;
        double a22 = 1 - 2 * intermediate.j * intermediate.j - 2 * intermediate.l * intermediate.l;
        double a23 = 2 * intermediate.k * intermediate.l - 2 * intermediate.i * intermediate.j;
        double a31 = 2 * intermediate.j * intermediate.l - 2 * intermediate.i * intermediate.k;
        double a32 = 2 * intermediate.k * intermediate.l + 2 * intermediate.i * intermediate.j;
        double a33 = 1 - 2 * intermediate.j * intermediate.j - 2 * intermediate.k * intermediate.k;

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

        double a = intermediate.i;
        double b = intermediate.j;
        double c = intermediate.k;
        double d = intermediate.l;
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

    public static Quaternion F(Quaternion U, double t, CalculationUtils C) {
        Quaternion res = new Quaternion(0, 0, 0, 0, 0, 0, 0);
//        U = Quaternion.normalize(U);
        double mu = 398600.4415E9;
        double x = C.x;
        double y = C.y;
        double z = C.z;
        double R = Math.sqrt(x * x + y * y + z * z);

//        res.i = (-1. / 2) * (U.j * U.wx + U.k * U.wy + U.l * U.wz);
//        res.j = (1. / 2) * (U.i * U.wx + U.l * U.wy - U.k * U.wz);
//        res.k = (-1. / 2) * (U.l * U.wx - U.i * U.wy - U.j * U.wz);
//        res.l = (1. / 2) * (U.k * U.wx - U.j * U.wy + U.i * U.wz);
        res.i = (1. / 2) * (-U.j * U.wx - U.k * U.wy - U.l * U.wz);
        res.j = (1. / 2) * (U.i * U.wx - U.l * U.wy + U.k * U.wz);
        res.k = (1. / 2) * (U.l * U.wx + U.i * U.wy - U.j * U.wz);
        res.l = (1. / 2) * (-U.k * U.wx + U.j * U.wy + U.i * U.wz);
        res.wx = (1. / ix) * (iy - iz) * U.wy * U.wz;
        res.wy = (1. / iy) * (iz - ix) * U.wx * U.wz;
        res.wz = (1. / iz) * (ix - iy) * U.wx * U.wy;

        // Гравитационный момент
        double o = 0.0;
        double e = 1.0;
        double ex = x / R;
        double ey = y / R;
        double ez = z / R;
        double l = -3 * mu / Math.pow(R, 3);

        List<Double> er = new ArrayList<>();
        Collections.addAll(er, ex, ey, ez);

        ArrayList J = new ArrayList<>();
        ArrayList E = new ArrayList<>();
        ArrayList<Double> line1 = new ArrayList<>();
        ArrayList<Double> line2 = new ArrayList<>();
        ArrayList<Double> line3 = new ArrayList<>();
        Collections.addAll(line1, ix, 0., 0.);
        Collections.addAll(line2, 0., iy, 0.);
        Collections.addAll(line3, 0., 0., iz);
        Collections.addAll(J, line1, line2, line3);
        ArrayList<Double> line4 = new ArrayList<>();
        ArrayList<Double> line5 = new ArrayList<>();
        ArrayList<Double> line6 = new ArrayList<>();
        Collections.addAll(line4, er.get(0));
        Collections.addAll(line5, er.get(1));
        Collections.addAll(line6, er.get(2));
        Collections.addAll(E, line4, line5, line6);
        ArrayList er2 = new ArrayList();
        Collections.addAll(er2, er.get(0) * ix, er.get(1) * iy, er.get(2) * iz);
        List<Double> result = VectorsAlgebra.multV(er, er2);
        System.out.println("NEW");
//        System.out.println(E);
//        System.out.println(Matrix.matrixMult(J, E));
        System.out.println(l * result.get(0) / ix);
        System.out.println(l * result.get(1) / iy);
        System.out.println(l * result.get(2) / iz);

        res.wx += l * result.get(0) / ix;
        res.wy += l * result.get(1) / iy;
        res.wz += l * result.get(2) / iz;

//        System.out.println("er");
//        System.out.println(er);
        Quaternion erq = new Quaternion(0, er);
        Quaternion u = new Quaternion(U.i, U.j, U.k, U.l);
//        System.out.println("u");
//        System.out.println(u);
//        u = Quaternion.normalize(u);
        Quaternion erf = Quaternion.quatMultQuat(Quaternion.quatMultQuat(u, erq), Quaternion.conjugate(u));
//        System.out.println(erf);
        er.clear();
        Collections.addAll(er, erf.j, erf.k, erf.l);
//        System.out.println(er);
        er = VectorsAlgebra.normalize(er);
//        System.out.println(er);


        double eri = er.get(0);
        double erj = er.get(1);
        double erk = er.get(2);

//        System.out.println("OLD");
//        System.out.println(l * (-iy * erj * erk + iz * erj * erk) * (1. / ix));
//        System.out.println(l * (ix * eri * erk - iz * eri * erk) * (1. / iy));
//        System.out.println(l * (-ix * eri * erj + iy * eri * erj) * (1. / iz));


//        res.wx += l * (-iy * erj * erk + iz * erj * erk) * (1. / ix);
//        res.wy += l * (ix * eri * erk - iz * eri * erk) * (1. / iy);
//        res.wz += l * (-ix * eri * erj + iy * eri * erj) * (1. / iz);


//        ArrayList<Double> i = new ArrayList<>();
//        ArrayList<Double> j = new ArrayList<>();
//        ArrayList<Double> k = new ArrayList<>();
//        List<Double> er = new ArrayList<>();
//        Collections.addAll(i, e, o, o);
//        Collections.addAll(j, o, e, o);
//        Collections.addAll(k, o, o, e);
//        Collections.addAll(er, ex, ey, ez);
//        Quaternion erq = new Quaternion(0, er);
//        Quaternion u = new Quaternion(U.i, U.j, U.k, U.l);
//        u = Quaternion.normalize(u);
//        Quaternion erf = Quaternion.quatMultQuat(Quaternion.quatMultQuat(u, erq), Quaternion.conjugate(u));
//        er.clear();
//        Collections.addAll(er, erf.j, erf.k, erf.l);
//        er = VectorsAlgebra.normalize(er);
////        System.out.println("er");
////        System.out.println(er);
//        double l = -3 * mu / Math.pow(R, 3);
//        List<Double> addent1 = VectorsAlgebra.constMult(ix * VectorsAlgebra.multS(er, i), i);
//        List<Double> addent2 = VectorsAlgebra.constMult(-iy * VectorsAlgebra.multS(er, j), j);
//        List<Double> addent3 = VectorsAlgebra.constMult(-iz * VectorsAlgebra.multS(er, k), k);
//        List<Double> sum = VectorsAlgebra.difference(VectorsAlgebra.difference(addent1, addent2), addent3);
////        System.out.println('!');
////        System.out.println(sum);
////        System.out.println(er);
////        System.out.println(VectorsAlgebra.multV(er, sum));
//        List<Double> M = VectorsAlgebra.constMult(l / 10, VectorsAlgebra.multV(er, sum));
////        System.out.println("M");
////        System.out.println(M);
////        System.out.println(M);
//        double mx = M.get(0);
//        double my = M.get(1);
//        double mz = M.get(2);
//        System.out.println(M + "   " + t);

//        res.wx += (1. / ix) * mx;
//        res.wy += (1. / iy) * my;
//        res.wz += (1. / iz) * mz;

        return res;
    }

//    public static ArrayList<ArrayList<Double>> calculateAll(double tM, double dtM, double tMaxM, double l0, double l1, double l2,
//                                                            double l3, double wx, double wy, double wz) {
//        ArrayList<Double> iList = new ArrayList<>();
//        ArrayList<Double> jList = new ArrayList<>();
//        ArrayList<Double> kList = new ArrayList<>();
//        ArrayList<Double> lList = new ArrayList<>();
//        ArrayList<Double> wxList = new ArrayList<>();
//        ArrayList<Double> wyList = new ArrayList<>();
//        ArrayList<Double> wzList = new ArrayList<>();
//        ArrayList<ArrayList<Double>> resList = new ArrayList<>();
//        ArrayList<ArrayList<Double>> res2List = new ArrayList<>();
//        ArrayList<ArrayList<Double>> res4List = new ArrayList<>();
//        Quaternion U = new Quaternion(l0, l1, l2, l3, wx, wy, wz);
//        Quaternion k1, k2, k3, k4;
//        Date d = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh-mm");
//        fileName = new File("Quaternion_" + dateFormat.format(d) + ".txt");
//
//        while (tM <= tMaxM) {
//            k1 = mult(dtM, F(U, tM));
//            k2 = mult(dtM, F(sum(U, mult(0.5, k1)), tM + 0.5 * dtM));
//            k3 = mult(dtM, F(sum(U, mult(0.5, k2)), tM + 0.5 * dtM));
//            k4 = mult(dtM, F(sum(U, k3), tM + dtM));
//            U = sum(U, mult(1.0 / 6.0, sum(sum(k1, mult(2, k2)), sum(mult(2, k3), k4))));
//            tM += dtM;
//            iList.add(U.i);
//            jList.add(U.j);
//            kList.add(U.k);
//            lList.add(U.l);
//            wxList.add(U.wx);
//            wyList.add(U.wy);
//            wzList.add(U.wz);
//
//            Quaternion Q = normalize(U);
//
//            String text = String.valueOf(Q.i) + "\t\t\t" + String.valueOf(Q.j) + "\t\t\t" + String.valueOf(Q.k) +
//                    "\t\t\t" + String.valueOf(Q.l)
//                    + "\n";
////            + "\t\t\t" + String.valueOf(U.wx) + "\t\t\t" + String.valueOf(U.wy) +
////            "\t\t\t" + String.valueOf(U.wz) + "\n";
//            CalculationUtils.write(fileName.getName(), text);
////            ArrayList<Double> res3List = new ArrayList<>();
////            Collections.addAll(res3List, U.i, U.j, U.k, U.l, U.wx, U.wy, U.wz);
////            res2List.add(res3List);
//        }

//        ArrayList<ArrayList<Double>> bodyPosition = new ArrayList<>();
//        ArrayList<Double> line1 = new ArrayList<>();
//        ArrayList<Double> line2 = new ArrayList<>();
//        ArrayList<Double> line3 = new ArrayList<>();
//        ArrayList<Double> line4 = new ArrayList<>();
//        Collections.addAll(line1, -2., 0., 0.);
//        Collections.addAll(line2, 1., 0., 0.);
//        Collections.addAll(line3, 1., -4., 0.);
//        Collections.addAll(line4, 1., 4., 0.);
//        Collections.addAll(bodyPosition, line1, line2, line3, line4);
//
//        res4List = calculation(res2List, bodyPosition);
//
//        Collections.addAll(resList, iList, jList, kList, lList, wxList, wyList, wzList);
//        System.out.println(wxList);
//        return (res4List);
//    }

    @Override
    public String toString() {
        return "[" + this.i + ", " + this.j + ", " + this.k + ", " + this.l + "]";
    }
}
