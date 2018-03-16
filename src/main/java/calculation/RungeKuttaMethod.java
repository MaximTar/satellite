package calculation;

import model.Satellite;
import model.StateVector;
import utils.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Maxim Tarasov on 31.08.2017.
 */
public class RungeKuttaMethod {

    public static File fileName;
    public static File quaternionFileName;

    public static void oneBody(double tM, double dtM, double tMaxM, Satellite satellite, Quaternion quaternion,
                               BooleansForIntegration bool) {

        Tensor tensor = satellite.tensor;
        StateVector state = satellite.stateVector;
        double mass = satellite.m;
        double area = satellite.area;

        StateVector k1, k2, k3, k4;
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh-mm");
        fileName = new File("OneBody_" + dateFormat.format(d) + ".txt");

        // Quaternions part
        Quaternion q1, q2, q3, q4;
        quaternionFileName = new File("OneBodyQuaternion_" + dateFormat.format(d) + ".txt");

        while (tM <= tMaxM) {
            k1 = StateVector.mult(MainForcesAbsolute.F(state, tM, bool, mass, area), dtM);
            k2 = StateVector.mult(MainForcesAbsolute.F(StateVector.sum(state, StateVector.mult(0.5, k1)), tM + 0.5 * dtM, bool, mass, area), dtM);
            k3 = StateVector.mult(MainForcesAbsolute.F(StateVector.sum(state, StateVector.mult(0.5, k2)), tM + 0.5 * dtM, bool, mass, area), dtM);
            k4 = StateVector.mult(MainForcesAbsolute.F(StateVector.sum(state, k3), tM + dtM, bool, mass, area), dtM);
            state = StateVector.sum(state, StateVector.mult(1.0 / 6.0, StateVector.sum(StateVector.sum(k1, StateVector.mult(2, k2)), StateVector.sum(StateVector.mult(2, k3), k4))));


            // Quaternions part
            q1 = Quaternion.mult(dtM, Quaternion.F(quaternion, tM, state, tensor));
            q2 = Quaternion.mult(dtM, Quaternion.F(Quaternion.sum(quaternion, Quaternion.mult(0.5, q1)), tM + 0.5 * dtM, state, tensor));
            q3 = Quaternion.mult(dtM, Quaternion.F(Quaternion.sum(quaternion, Quaternion.mult(0.5, q2)), tM + 0.5 * dtM, state, tensor));
            q4 = Quaternion.mult(dtM, Quaternion.F(Quaternion.sum(quaternion, q3), tM + dtM, state, tensor));
            quaternion = Quaternion.sum(quaternion, Quaternion.mult(1.0 / 6.0, Quaternion.sum(Quaternion.sum(q1, Quaternion.mult(2, q2)), Quaternion.sum(Quaternion.mult(2, q3), q4))));
            tM += dtM;

            Quaternion N = Quaternion.normalize(quaternion);

            String qtext = String.valueOf(N.qw) + "\t\t\t" + String.valueOf(N.qx) + "\t\t\t" + String.valueOf(N.qy) + "\t\t\t" + String.valueOf(N.qz)
//                    + "\n";
                    + "\t\t\t" + String.valueOf(quaternion.wx) + "\t\t\t" + String.valueOf(quaternion.wy) + "\t\t\t" + String.valueOf(quaternion.wz)
//                    + "\n";
//                    + "\t\t\t" + String.valueOf(N.qw * N.qw + N.qx * N.qx + N.qy * N.qy + N.qz * N.qz)
//                    + "\n";
                    + "\t\t\t" + String.valueOf(state.x) + "\t\t\t" + String.valueOf(state.y) + "\t\t\t" + String.valueOf(state.z)
                    + "\t\t\t" + String.valueOf(state.vx) + "\t\t\t" + String.valueOf(state.vy) + "\t\t\t" + String.valueOf(state.vz)
                    + "\n";
            WriteUtils.write(quaternionFileName.getName(), qtext);

            String text = String.valueOf(state.x) + "\t\t\t" + String.valueOf(state.y) + "\t\t\t" + String.valueOf(state.z) + "\t\t\t" + String.valueOf(state.vx)
                    + "\t\t\t" + String.valueOf(state.vy) + "\t\t\t" + String.valueOf(state.vz)
//                    + "\t\t\t" + String.valueOf(sunPos.get(0)) + "\t\t\t" + String.valueOf(sunPos.get(1)) + "\t\t\t" + String.valueOf(sunPos.get(2))
//                    + "\t\t\t" + sunSignFlag
                    + "\t\t\t" + String.valueOf(Math.sqrt(state.x * state.x + state.y * state.y + state.z * state.z)) + "\n";
            WriteUtils.write(fileName.getName(), text);
        }

        WriteUtils.write(fileName.getName(), new SimpleDateFormat("dd-MM-yyyy hh-mm").format(new Date()));
    }

    public static void twoBody(double tM, double dtM, double tMaxM, Satellite satelliteOne, Satellite satelliteTwo,
                               Quaternion quaternionOne, Quaternion quaternionTwo,
                               BooleansForIntegration bool, double k, double l) {

        Tensor tensorOne = satelliteOne.tensor;
        Tensor tensorTwo = satelliteTwo.tensor;
        StateVector stateOne = satelliteOne.stateVector;
        StateVector stateTwo = satelliteTwo.stateVector;

        double massOne = satelliteOne.m;
        double massTwo = satelliteTwo.m;
        double areaOne = satelliteOne.area;
        double areaTwo = satelliteTwo.area;

        StateVector stateOneSaver;
        StateVector k11, k12, k21, k22, k31, k32, k41, k42;
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh-mm");
        fileName = new File("TwoBody_" + dateFormat.format(d) + ".txt");

        // Quaternions part
        Quaternion q11, q12, q21, q22, q31, q32, q41, q42;
        quaternionFileName = new File("TwoBodyQuaternion_" + dateFormat.format(d) + ".txt");
        int n = 0;
        while (tM <= tMaxM) {
            double[] r1 = new double[]{stateOne.x, stateOne.y, stateOne.z};
            double[] r2 = new double[]{stateTwo.x, stateTwo.y, stateTwo.z};
            double[] r = VectorAlgebra.difference(r2, r1);
            double dx = VectorAlgebra.absoluteValue(r) - l;
            MainForcesAbsolute.tensionFFlag = dx > 0;

            stateOneSaver = stateOne;

            k11 = StateVector.mult(MainForcesAbsolute.F2(stateOne, stateTwo, tM, bool, k, l, massOne, areaOne), dtM);
            k21 = StateVector.mult(MainForcesAbsolute.F2(StateVector.sum(stateOne, StateVector.mult(0.5, k11)), stateTwo, tM + 0.5 * dtM, bool, k, l, massOne, areaOne), dtM);
            k31 = StateVector.mult(MainForcesAbsolute.F2(StateVector.sum(stateOne, StateVector.mult(0.5, k21)), stateTwo, tM + 0.5 * dtM, bool, k, l, massOne, areaOne), dtM);
            k41 = StateVector.mult(MainForcesAbsolute.F2(StateVector.sum(stateOne, k31), stateTwo, tM + dtM, bool, k, l, massOne, areaOne), dtM);
            stateOne = StateVector.sum(stateOne, StateVector.mult(1.0 / 6.0, StateVector.sum(StateVector.sum(k11, StateVector.mult(2, k21)), StateVector.sum(StateVector.mult(2, k31), k41))));
            k12 = StateVector.mult(MainForcesAbsolute.F2(stateTwo, stateOneSaver, tM, bool, k, l, massTwo, areaTwo), dtM);
            k22 = StateVector.mult(MainForcesAbsolute.F2(StateVector.sum(stateTwo, StateVector.mult(0.5, k12)), stateOneSaver, tM + 0.5 * dtM, bool, k, l, massTwo, areaTwo), dtM);
            k32 = StateVector.mult(MainForcesAbsolute.F2(StateVector.sum(stateTwo, StateVector.mult(0.5, k22)), stateOneSaver, tM + 0.5 * dtM, bool, k, l, massTwo, areaTwo), dtM);
            k42 = StateVector.mult(MainForcesAbsolute.F2(StateVector.sum(stateTwo, k32), stateOneSaver, tM + dtM, bool, k, l, massTwo, areaTwo), dtM);
            stateTwo = StateVector.sum(stateTwo, StateVector.mult(1.0 / 6.0, StateVector.sum(StateVector.sum(k12, StateVector.mult(2, k22)), StateVector.sum(StateVector.mult(2, k32), k42))));

//            // Quaternions part
            q11 = Quaternion.mult(dtM, Quaternion.F2(quaternionOne, tM, stateOne, tensorOne));
            q21 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(quaternionOne, Quaternion.mult(0.5, q11)), tM + 0.5 * dtM, stateOne, tensorOne));
            q31 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(quaternionOne, Quaternion.mult(0.5, q21)), tM + 0.5 * dtM, stateOne, tensorOne));
            q41 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(quaternionOne, q31), tM + dtM, stateOne, tensorOne));
            quaternionOne = Quaternion.sum(quaternionOne, Quaternion.mult(1.0 / 6.0, Quaternion.sum(Quaternion.sum(q11, Quaternion.mult(2, q21)), Quaternion.sum(Quaternion.mult(2, q31), q41))));

            q12 = Quaternion.mult(dtM, Quaternion.F2(quaternionTwo, tM, stateTwo, tensorTwo));
            q22 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(quaternionTwo, Quaternion.mult(0.5, q12)), tM + 0.5 * dtM, stateTwo, tensorTwo));
            q32 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(quaternionTwo, Quaternion.mult(0.5, q22)), tM + 0.5 * dtM, stateTwo, tensorTwo));
            q42 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(quaternionTwo, q32), tM + dtM, stateTwo, tensorTwo));
            quaternionTwo = Quaternion.sum(quaternionTwo, Quaternion.mult(1.0 / 6.0, Quaternion.sum(Quaternion.sum(q12, Quaternion.mult(2, q22)), Quaternion.sum(Quaternion.mult(2, q32), q42))));

//            System.out.println(tM);
            tM += dtM;


            //TODO Вынести переменную и добавить в GUI
            if (n == 10000) {
                CollectorUtils.collectData(stateOne, stateTwo, quaternionOne, quaternionTwo);
                n = 0;
            } else {
                n += 1;
            }

//            synchronized (stateOne) {
//                CollectorUtils.collectData(stateOne, stateTwo, quaternionOne, quaternionTwo);
//            }
        }

        CollectorUtils.writeData();
    }

    public static void twoBodyRelative(double tM, double dtM, double tMaxM, Satellite satelliteCenter, Satellite satelliteOne, Satellite satelliteTwo,
                                       Quaternion quaternionOne, Quaternion quaternionTwo,
                                       BooleansForIntegration bool, double k, double l) {

        Tensor tensorOne = satelliteOne.tensor;
        Tensor tensorTwo = satelliteTwo.tensor;
        StateVector stateOne = satelliteOne.stateVector;
        StateVector stateTwo = satelliteTwo.stateVector;
        StateVector stateCenter = satelliteCenter.stateVector;

        Satellite satelliteOneSaver = new Satellite(satelliteOne), satelliteTwoSaver = new Satellite(satelliteTwo);
        StateVector stateCenterSaver;

        List<StateVector> firstList, secondList, thirdList, fourthList;
        StateVector k1Center, k2Center, k3Center, k4Center, k1One, k2One, k3One, k4One, k1Two, k2Two, k3Two, k4Two;
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh-mm");
        fileName = new File("TwoBody_" + dateFormat.format(d) + ".txt");

        // Quaternions part
        Quaternion q11, q12, q21, q22, q31, q32, q41, q42;
        quaternionFileName = new File("TwoBodyQuaternion_" + dateFormat.format(d) + ".txt");
        int n = 0;
        while (tM <= tMaxM) {
            double[] r1 = new double[]{stateOne.x, stateOne.y, stateOne.z};
            double[] r2 = new double[]{stateTwo.x, stateTwo.y, stateTwo.z};
            double[] r = VectorAlgebra.difference(r2, r1);
            double dx = VectorAlgebra.absoluteValue(r) - l;
            MainForcesRelative.tensionFFlag = dx > 0;

//            System.out.println("FIRST");
            firstList = MainForcesRelative.F2(stateCenter, satelliteOne, satelliteTwo, tM, bool, k, l);
            k1Center = StateVector.mult(firstList.get(0), dtM);
            k1One = StateVector.mult(firstList.get(1), dtM);
            k1Two = StateVector.mult(firstList.get(2), dtM);

            stateCenterSaver = StateVector.sum(stateCenter, StateVector.mult(0.5, k1Center));
            satelliteOneSaver.stateVector = StateVector.sum(stateOne, StateVector.mult(0.5, k1One));
            satelliteTwoSaver.stateVector = StateVector.sum(stateTwo, StateVector.mult(0.5, k1Two));

//            System.out.println("SECOND");
            secondList = MainForcesRelative.F2(stateCenterSaver, satelliteOneSaver, satelliteTwoSaver, tM + 0.5 * dtM, bool, k, l);
            k2Center = StateVector.mult(secondList.get(0), dtM);
            k2One = StateVector.mult(secondList.get(1), dtM);
            k2Two = StateVector.mult(secondList.get(2), dtM);

            stateCenterSaver = StateVector.sum(stateCenter, StateVector.mult(0.5, k2Center));
            satelliteOneSaver.stateVector = StateVector.sum(stateOne, StateVector.mult(0.5, k2One));
            satelliteTwoSaver.stateVector = StateVector.sum(stateTwo, StateVector.mult(0.5, k2Two));

//            System.out.println("THIRD");
            thirdList = MainForcesRelative.F2(stateCenterSaver, satelliteOneSaver, satelliteTwoSaver, tM + 0.5 * dtM, bool, k, l);
            k3Center = StateVector.mult(thirdList.get(0), dtM);
            k3One = StateVector.mult(thirdList.get(1), dtM);
            k3Two = StateVector.mult(thirdList.get(2), dtM);

            stateCenterSaver = StateVector.sum(stateCenter, k3Center);
            satelliteOneSaver.stateVector = StateVector.sum(stateOne, k3One);
            satelliteTwoSaver.stateVector = StateVector.sum(stateTwo, k3Two);

//            System.out.println("FOURTH");
            fourthList = MainForcesRelative.F2(stateCenterSaver, satelliteOneSaver, satelliteTwoSaver, tM + dtM, bool, k, l);
            k4Center = StateVector.mult(fourthList.get(0), dtM);
            k4One = StateVector.mult(fourthList.get(1), dtM);
            k4Two = StateVector.mult(fourthList.get(2), dtM);

            stateCenter = StateVector.sum(stateCenter, StateVector.mult(1.0 / 6.0, StateVector.sum(StateVector.sum(k1Center, StateVector.mult(2., k2Center)), StateVector.sum(StateVector.mult(2., k3Center), k4Center))));
            stateOne = StateVector.sum(stateOne, StateVector.mult(1.0 / 6.0, StateVector.sum(StateVector.sum(k1One, StateVector.mult(2., k2One)), StateVector.sum(StateVector.mult(2., k3One), k4One))));
            stateTwo = StateVector.sum(stateTwo, StateVector.mult(1.0 / 6.0, StateVector.sum(StateVector.sum(k1Two, StateVector.mult(2., k2Two)), StateVector.sum(StateVector.mult(2., k3Two), k4Two))));

//            // Quaternions part
            q11 = Quaternion.mult(dtM, Quaternion.F2(quaternionOne, tM, stateOne, tensorOne));
            q21 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(quaternionOne, Quaternion.mult(0.5, q11)), tM + 0.5 * dtM, stateOne, tensorOne));
            q31 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(quaternionOne, Quaternion.mult(0.5, q21)), tM + 0.5 * dtM, stateOne, tensorOne));
            q41 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(quaternionOne, q31), tM + dtM, stateOne, tensorOne));
            quaternionOne = Quaternion.sum(quaternionOne, Quaternion.mult(1.0 / 6.0, Quaternion.sum(Quaternion.sum(q11, Quaternion.mult(2, q21)), Quaternion.sum(Quaternion.mult(2, q31), q41))));

            q12 = Quaternion.mult(dtM, Quaternion.F2(quaternionTwo, tM, stateTwo, tensorTwo));
            q22 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(quaternionTwo, Quaternion.mult(0.5, q12)), tM + 0.5 * dtM, stateTwo, tensorTwo));
            q32 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(quaternionTwo, Quaternion.mult(0.5, q22)), tM + 0.5 * dtM, stateTwo, tensorTwo));
            q42 = Quaternion.mult(dtM, Quaternion.F2(Quaternion.sum(quaternionTwo, q32), tM + dtM, stateTwo, tensorTwo));
            quaternionTwo = Quaternion.sum(quaternionTwo, Quaternion.mult(1.0 / 6.0, Quaternion.sum(Quaternion.sum(q12, Quaternion.mult(2, q22)), Quaternion.sum(Quaternion.mult(2, q32), q42))));

//            System.out.println(tM);
            tM += dtM;


            //TODO Вынести переменную и добавить в GUI
            if (n == 10000) {
                CollectorUtils.collectDataRelative(stateCenter, stateOne, stateTwo, quaternionOne, quaternionTwo);
                n = 0;
            } else {
                n += 1;
            }

//            synchronized (stateOne) {
//                CollectorUtils.collectData(stateOne, stateTwo, quaternionOne, quaternionTwo);
//            }
        }

        CollectorUtils.writeDataRelative();
    }
}
