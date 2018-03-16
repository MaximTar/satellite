package utils;

import calculation.RungeKuttaMethod;
import model.StateVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxim Tarasov on 31.08.2017.
 */
public class CollectorUtils {

    public static List<Double> x1List = new ArrayList<>();
    public static List<Double> y1List = new ArrayList<>();
    public static List<Double> z1List = new ArrayList<>();
    public static List<Double> v1xList = new ArrayList<>();
    public static List<Double> v1yList = new ArrayList<>();
    public static List<Double> v1zList = new ArrayList<>();
    public static List<Double> x2List = new ArrayList<>();
    public static List<Double> y2List = new ArrayList<>();
    public static List<Double> z2List = new ArrayList<>();
    public static List<Double> v2xList = new ArrayList<>();
    public static List<Double> v2yList = new ArrayList<>();
    public static List<Double> v2zList = new ArrayList<>();
    public static List<Double> x0List = new ArrayList<>();
    public static List<Double> y0List = new ArrayList<>();
    public static List<Double> z0List = new ArrayList<>();
    public static List<Double> v0xList = new ArrayList<>();
    public static List<Double> v0yList = new ArrayList<>();
    public static List<Double> v0zList = new ArrayList<>();
    public static List<Boolean> flagList = new ArrayList<>();

    public static List<Double> i1List = new ArrayList<>();
    public static List<Double> j1List = new ArrayList<>();
    public static List<Double> k1List = new ArrayList<>();
    public static List<Double> l1List = new ArrayList<>();
    public static List<Double> w1xList = new ArrayList<>();
    public static List<Double> w1yList = new ArrayList<>();
    public static List<Double> w1zList = new ArrayList<>();
    public static List<Double> i2List = new ArrayList<>();
    public static List<Double> j2List = new ArrayList<>();
    public static List<Double> k2List = new ArrayList<>();
    public static List<Double> l2List = new ArrayList<>();
    public static List<Double> w2xList = new ArrayList<>();
    public static List<Double> w2yList = new ArrayList<>();
    public static List<Double> w2zList = new ArrayList<>();

    public static void collectData(StateVector stateOne, StateVector stateTwo,
                                   Quaternion quaternionOne, Quaternion quaternionTwo) {
        x1List.add(stateOne.x);
        y1List.add(stateOne.y);
        z1List.add(stateOne.z);
        v1xList.add(stateOne.vx);
        v1yList.add(stateOne.vy);
        v1zList.add(stateOne.vz);
        x2List.add(stateTwo.x);
        y2List.add(stateTwo.y);
        z2List.add(stateTwo.z);
        v2xList.add(stateTwo.vx);
        v2yList.add(stateTwo.vy);
        v2zList.add(stateTwo.vz);
        flagList.add(MainForcesAbsolute.tensionFFlag);

        i1List.add(quaternionOne.qw);
        j1List.add(quaternionOne.qx);
        k1List.add(quaternionOne.qy);
        l1List.add(quaternionOne.qz);
        w1xList.add(quaternionOne.wx);
        w1yList.add(quaternionOne.wy);
        w1zList.add(quaternionOne.wz);
        i2List.add(quaternionTwo.qw);
        j2List.add(quaternionTwo.qx);
        k2List.add(quaternionTwo.qy);
        l2List.add(quaternionTwo.qz);
        w2xList.add(quaternionTwo.wx);
        w2yList.add(quaternionTwo.wy);
        w2zList.add(quaternionTwo.wz);
    }

    public static void collectDataRelative(StateVector stateCenter, StateVector stateOne, StateVector stateTwo,
                                           Quaternion quaternionOne, Quaternion quaternionTwo) {
        x0List.add(stateCenter.x);
        y0List.add(stateCenter.y);
        z0List.add(stateCenter.z);
        v0xList.add(stateCenter.vx);
        v0yList.add(stateCenter.vy);
        v0zList.add(stateCenter.vz);
        x1List.add(stateOne.x);
        y1List.add(stateOne.y);
        z1List.add(stateOne.z);
        v1xList.add(stateOne.vx);
        v1yList.add(stateOne.vy);
        v1zList.add(stateOne.vz);
        x2List.add(stateTwo.x);
        y2List.add(stateTwo.y);
        z2List.add(stateTwo.z);
        v2xList.add(stateTwo.vx);
        v2yList.add(stateTwo.vy);
        v2zList.add(stateTwo.vz);
        flagList.add(MainForcesRelative.tensionFFlag);

        i1List.add(quaternionOne.qw);
        j1List.add(quaternionOne.qx);
        k1List.add(quaternionOne.qy);
        l1List.add(quaternionOne.qz);
        w1xList.add(quaternionOne.wx);
        w1yList.add(quaternionOne.wy);
        w1zList.add(quaternionOne.wz);
        i2List.add(quaternionTwo.qw);
        j2List.add(quaternionTwo.qx);
        k2List.add(quaternionTwo.qy);
        l2List.add(quaternionTwo.qz);
        w2xList.add(quaternionTwo.wx);
        w2yList.add(quaternionTwo.wy);
        w2zList.add(quaternionTwo.wz);
    }

    public static void writeData() {
        for (int i = 0; i < x1List.size(); i++) {
            String text = String.valueOf(x1List.get(i)) + "\t\t\t" + String.valueOf(y1List.get(i)) + "\t\t\t" + String.valueOf(z1List.get(i))
                    + "\t\t\t" + String.valueOf(v1xList.get(i)) + "\t\t\t" + String.valueOf(v1yList.get(i)) + "\t\t\t" + String.valueOf(v1zList.get(i))
                    + "\t\t\t" + String.valueOf(x2List.get(i)) + "\t\t\t" + String.valueOf(y2List.get(i)) + "\t\t\t" + String.valueOf(z2List.get(i))
                    + "\t\t\t" + String.valueOf(v2xList.get(i)) + "\t\t\t" + String.valueOf(v2yList.get(i)) + "\t\t\t" + String.valueOf(v2zList.get(i))
//                    + "\t\t\t" + String.valueOf(Math.sqrt(U1.x * U1.x + U1.y * U1.y + U1.z * U1.z))
//                    + "\t\t\t" + String.valueOf(Math.sqrt(U2.x * U2.x + U2.y * U2.y + U2.z * U2.z))
                    + "\t\t\t" + String.valueOf(flagList.get(i))
                    + "\n";
            WriteUtils.write(RungeKuttaMethod.fileName.getName(), text);

            Quaternion nQ1 = new Quaternion(i1List.get(i), j1List.get(i), k1List.get(i), l1List.get(i), w1xList.get(i), w1yList.get(i), w1zList.get(i));
            Quaternion nQ2 = new Quaternion(i2List.get(i), j2List.get(i), k2List.get(i), l2List.get(i), w2xList.get(i), w2yList.get(i), w2zList.get(i));
            StateVector nU1 = new StateVector(x1List.get(i), y1List.get(i), z1List.get(i), v1xList.get(i), v1yList.get(i), v1zList.get(i));
            StateVector nU2 = new StateVector(x2List.get(i), y2List.get(i), z2List.get(i), v2xList.get(i), v2yList.get(i), v2zList.get(i));
            Quaternion N1 = Quaternion.normalize(nQ1);
            Quaternion N2 = Quaternion.normalize(nQ2);

            String qtext = String.valueOf(N1.qw) + "\t\t\t" + String.valueOf(N1.qx) + "\t\t\t" + String.valueOf(N1.qy) + "\t\t\t" + String.valueOf(N1.qz)
                    + "\t\t\t" + String.valueOf(nQ1.wx) + "\t\t\t" + String.valueOf(nQ1.wy) + "\t\t\t" + String.valueOf(nQ1.wz)
//                    + "\t\t\t" + String.valueOf(N1.qw * N1.qw + N1.qx * N1.qx + N1.qy * N1.qy + N1.qz * N1.qz)
                    + "\t\t\t" + String.valueOf(nU1.x) + "\t\t\t" + String.valueOf(nU1.y) + "\t\t\t" + String.valueOf(nU1.z)
                    + "\t\t\t" + String.valueOf(nU1.vx) + "\t\t\t" + String.valueOf(nU1.vy) + "\t\t\t" + String.valueOf(nU1.vz)
                    + "\t\t\t" + String.valueOf(N2.qw) + "\t\t\t" + String.valueOf(N2.qx) + "\t\t\t" + String.valueOf(N2.qy) + "\t\t\t" + String.valueOf(N2.qz)
                    + "\t\t\t" + String.valueOf(nQ2.wx) + "\t\t\t" + String.valueOf(nQ2.wy) + "\t\t\t" + String.valueOf(nQ2.wz)
//                    + "\t\t\t" + String.valueOf(N2.qw * N2.qw + N2.qx * N2.qx + N2.qy * N2.qy + N2.qz * N2.qz)
                    + "\t\t\t" + String.valueOf(nU2.x) + "\t\t\t" + String.valueOf(nU2.y) + "\t\t\t" + String.valueOf(nU2.z)
                    + "\t\t\t" + String.valueOf(nU2.vx) + "\t\t\t" + String.valueOf(nU2.vy) + "\t\t\t" + String.valueOf(nU2.vz)
                    + "\n";
            WriteUtils.write(RungeKuttaMethod.quaternionFileName.getName(), qtext);
        }
    }

    public static void writeDataRelative() {
        for (int i = 0; i < x1List.size(); i++) {
            String text = String.valueOf(x0List.get(i)) + "\t\t\t" + String.valueOf(y0List.get(i)) + "\t\t\t" + String.valueOf(z0List.get(i))
                    + "\t\t\t" + String.valueOf(v0xList.get(i)) + "\t\t\t" + String.valueOf(v0yList.get(i)) + "\t\t\t" + String.valueOf(v0zList.get(i))
                    + "\t\t\t" + String.valueOf(x1List.get(i)) + "\t\t\t" + String.valueOf(y1List.get(i)) + "\t\t\t" + String.valueOf(z1List.get(i))
                    + "\t\t\t" + String.valueOf(v1xList.get(i)) + "\t\t\t" + String.valueOf(v1yList.get(i)) + "\t\t\t" + String.valueOf(v1zList.get(i))
                    + "\t\t\t" + String.valueOf(x2List.get(i)) + "\t\t\t" + String.valueOf(y2List.get(i)) + "\t\t\t" + String.valueOf(z2List.get(i))
                    + "\t\t\t" + String.valueOf(v2xList.get(i)) + "\t\t\t" + String.valueOf(v2yList.get(i)) + "\t\t\t" + String.valueOf(v2zList.get(i))
                    + "\t\t\t" + String.valueOf(flagList.get(i))
                    + "\t\t\t" + String.valueOf(Math.sqrt((x1List.get(i) - x2List.get(i)) * (x1List.get(i) - x2List.get(i)) +
                    (y1List.get(i) - y2List.get(i)) * (y1List.get(i) - y2List.get(i))))
                    + "\n";
            WriteUtils.write(RungeKuttaMethod.fileName.getName(), text);

            Quaternion nQ1 = new Quaternion(i1List.get(i), j1List.get(i), k1List.get(i), l1List.get(i), w1xList.get(i), w1yList.get(i), w1zList.get(i));
            Quaternion nQ2 = new Quaternion(i2List.get(i), j2List.get(i), k2List.get(i), l2List.get(i), w2xList.get(i), w2yList.get(i), w2zList.get(i));
            StateVector nU1 = new StateVector(x1List.get(i), y1List.get(i), z1List.get(i), v1xList.get(i), v1yList.get(i), v1zList.get(i));
            StateVector nU2 = new StateVector(x2List.get(i), y2List.get(i), z2List.get(i), v2xList.get(i), v2yList.get(i), v2zList.get(i));
            Quaternion N1 = Quaternion.normalize(nQ1);
            Quaternion N2 = Quaternion.normalize(nQ2);

            String qText = String.valueOf(N1.qw) + "\t\t\t" + String.valueOf(N1.qx) + "\t\t\t" + String.valueOf(N1.qy) + "\t\t\t" + String.valueOf(N1.qz)
                    + "\t\t\t" + String.valueOf(nQ1.wx) + "\t\t\t" + String.valueOf(nQ1.wy) + "\t\t\t" + String.valueOf(nQ1.wz)
                    + "\t\t\t" + String.valueOf(nU1.x) + "\t\t\t" + String.valueOf(nU1.y) + "\t\t\t" + String.valueOf(nU1.z)
                    + "\t\t\t" + String.valueOf(nU1.vx) + "\t\t\t" + String.valueOf(nU1.vy) + "\t\t\t" + String.valueOf(nU1.vz)
                    + "\t\t\t" + String.valueOf(N2.qw) + "\t\t\t" + String.valueOf(N2.qx) + "\t\t\t" + String.valueOf(N2.qy) + "\t\t\t" + String.valueOf(N2.qz)
                    + "\t\t\t" + String.valueOf(nQ2.wx) + "\t\t\t" + String.valueOf(nQ2.wy) + "\t\t\t" + String.valueOf(nQ2.wz)
                    + "\t\t\t" + String.valueOf(nU2.x) + "\t\t\t" + String.valueOf(nU2.y) + "\t\t\t" + String.valueOf(nU2.z)
                    + "\t\t\t" + String.valueOf(nU2.vx) + "\t\t\t" + String.valueOf(nU2.vy) + "\t\t\t" + String.valueOf(nU2.vz)
                    + "\n";
            WriteUtils.write(RungeKuttaMethod.quaternionFileName.getName(), qText);
        }
    }
}
