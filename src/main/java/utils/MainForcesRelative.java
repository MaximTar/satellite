package utils;

import model.Satellite;
import model.StateVector;
//import org.hipparchus.util.FastMath; // todo check this
//import jat.spacecraft.Spacecraft; // todo // FIXME: 16.03.18
import org.apache.commons.math3.util.FastMath;

import java.util.*;
// TODO: 08.11.2017 NOTE FAST MATH

/**
 * Created by Maxim Tarasov on 08.10.2017.
 */
public class MainForcesRelative {

    // Tension block
    public static double[] tensionOne;
    public static double[] tensionTwo;
    public static boolean tensionFFlag = false;

    public static List<Double> sunPos;
    public static boolean sunSignFlag;
    static double[] w_earth = new double[]{0., 0., 7.2921158553E-5};
    static Calendar calendar = new GregorianCalendar();
    public static double density;

    static int n = 0; // TODO counter/shitcode
    public static ArrayList<ArrayList<Double>> precession, nutation, earthRotation, pole;

    public static StateVector force(StateVector stateVector, double t, BooleansForIntegration bool,
                                    double bbOne, double bbTwo, double bodyMass, double pointMass) {

        double x = stateVector.x;
        double y = stateVector.y;
        double z = stateVector.z;
        double vx = stateVector.vx;
        double vy = stateVector.vy;
        double vz = stateVector.vz;

        StateVector state = new StateVector();

        double mu = 398600.4415E9;

        state.x = vx;
        state.y = vy;
        state.z = vz;

//        Простейшее приближение force = fm/r
        state.vx = -mu * x / FastMath.pow(FastMath.sqrt(x * x + y * y + z * z), 3);
        state.vy = -mu * y / FastMath.pow(FastMath.sqrt(x * x + y * y + z * z), 3);
        state.vz = -mu * z / FastMath.pow(FastMath.sqrt(x * x + y * y + z * z), 3);

//        Приближение по Чазову
        if (bool.geopotential) {
            List<Double> GeoPotential = GeoPot.calc(x, y, z, 4);
            state.vx = GeoPotential.get(0);
            state.vy = GeoPotential.get(1);
            state.vz = GeoPotential.get(2);
        }

//        Sun's Gravity
        if (bool.sunGravity) {
            double muSun = 132712440018E9;
            TimeZone timeZone = TimeZone.getTimeZone("GMT");
            Calendar c = Calendar.getInstance(timeZone);
            sunPos = SunPosition.sunPosition(c);
            double[] sunGravity = Gravitation.force(muSun, x, y, z, sunPos.get(0), sunPos.get(1), sunPos.get(2));
            state.vx += sunGravity[0];
            state.vy += sunGravity[1];
            state.vz += sunGravity[2];
        }

//        Moon's Gravity
        if (bool.moonGravity) {
            double muMoon = 4902.779E9;
            TimeZone timeZone = TimeZone.getTimeZone("GMT");
            Calendar c = Calendar.getInstance(timeZone);
            List<Double> moonPos = MoonPosition.moonPosition(c);
            double[] moonGravity = Gravitation.force(muMoon, x, y, z, moonPos.get(0), moonPos.get(1), moonPos.get(2));
            state.vx += moonGravity[0];
            state.vy += moonGravity[1];
            state.vz += moonGravity[2];
        }

//        Sun's Radiation Pressure
        if (bool.sunPressure) {
            TimeZone timeZone = TimeZone.getTimeZone("GMT");
            Calendar c = Calendar.getInstance(timeZone);
            sunSignFlag = false;
            if (!SunRadiationPressure.sunShadowSign(c, stateVector, 6371000)) {
                sunSignFlag = true;
                double A = 1; // Площадь сечения
                double C = 1; // Передача импульса за счёт поглощения и отражения
                double[] sunPres = SunRadiationPressure.force(A, C, c);
                state.vx += sunPres[0];
                state.vy += sunPres[1];
                state.vz += sunPres[2];
            }
        }

        //FIXME
//        Spacecraft sc = new Spacecraft();
//        NRLMSISE_Drag drag = new NRLMSISE_Drag(sc);
//        Atmospheric Drag
        {
            if (bool.drag) {
////                double A = 1; // Площадь сечения
//                double drag_coefficient = 2.4; // "Эмпирический коэффициент примерно равный 2"
////                double drag_coefficient = 2; // "Эмпирический коэффициент примерно равный 2"
//                double earth_radius = 6371000;
//
                double[] r = new double[]{x, y, z};
                double[] v_earth = VectorAlgebra.multV(w_earth, r); // Поправка для нахождения относительной скорости
//                double radv = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)); // Радиус-вектор
//
//                // Exponential Model
//                double density = Drag.exponentialModelDensity(radv - earth_radius);
//
//                // NRLMSISE-00
//                // FIXME
////                Calendar helpCalendar = (Calendar) calendar.clone();
////                helpCalendar.add(Calendar.MILLISECOND, (int) (t * 1000));
////                if (n == 100) {
////                    calendar.add(Calendar.MILLISECOND, 1);
////                    n = 0;
////                } else {
////                    n++;
////                }
//
////                double density = drag.densityNow(x, y, z, calendar);
////                System.out.println("DENSITY = " + density + " at Z = " + z);
                double[] dragForce = Drag.force_div_m_center(density, stateVector, v_earth, bbOne, bbTwo, bodyMass, pointMass);

                state.vx -= dragForce[0];
                state.vy -= dragForce[1];
                state.vz -= dragForce[2];
            }
        }
        return state;
    }

    public static StateVector forceRelative(StateVector stateVectorCenter, Satellite satellite, double t,
                                            BooleansForIntegration bool, double bbOne, double bbTwo, double mStar) {
        double x = satellite.stateVector.x;
        double y = satellite.stateVector.y;
        double z = satellite.stateVector.z;
        double vx = satellite.stateVector.vx;
        double vy = satellite.stateVector.vy;
        double vz = satellite.stateVector.vz;

        double xC = stateVectorCenter.x;
        double yC = stateVectorCenter.y;
        double zC = stateVectorCenter.z;

        StateVector state = new StateVector();

        state.x = vx;
        state.y = vy;
        state.z = vz;

//        Простейшее приближение force = fm/r
        double mu = Constant.MU;
        double Ro3 = FastMath.pow(FastMath.sqrt(xC * xC + yC * yC + zC * zC), 3);
        double[] eo = VectorAlgebra.normalize(new double[]{xC, yC, zC});
        double reo = VectorAlgebra.multS(new double[]{x, y, z}, eo);
        state.vx = -(mu / Ro3) * (x - 3 * reo * eo[0]);
        state.vy = -(mu / Ro3) * (y - 3 * reo * eo[1]);
        state.vz = -(mu / Ro3) * (z - 3 * reo * eo[2]);

        //FIXME
//        NRLMSISE_Drag drag = new NRLMSISE_Drag();
//        Atmospheric Drag
        {
            if (bool.drag) {
////                double A = 1; // Площадь сечения
//                double drag_coefficient = 2.4; // "Эмпирический коэффициент примерно равный 2"
////                double drag_coefficient = 2; // "Эмпирический коэффициент примерно равный 2"
//                double earth_radius = 6371000.0;
//
                double[] r = new double[]{xC, yC, zC};
                double[] v_earth = VectorAlgebra.multV(w_earth, r); // Поправка для нахождения относительной скорости
//                double radv = FastMath.sqrt(FastMath.pow(xC, 2) + FastMath.pow(yC, 2) + FastMath.pow(zC, 2)); // Радиус-вектор
//
//                // Exponential Model
////                double density = Drag.exponentialModelDensity(radv - earth_radius);
//
//                // NRLMSISE-00
//                // FIXME
//                Calendar helpCalendar = (Calendar) calendar.clone();
//                helpCalendar.add(Calendar.MILLISECOND, (int) (t * 1000));
//                calendar.add(Calendar.MILLISECOND, 1);
//                if (n == 1000) {
//                    calendar.add(Calendar.MILLISECOND, 1);
//                    n = 0;
//                } else {
//                    n++;
//                }
//
////                double density = drag.densityNow(xC, yC, zC, calendar);
//
//                ArrayList<ArrayList<Double>> help = new ArrayList<>();
//                ArrayList<ArrayList<Double>> ECEFCoordinates;
//                double[] LLACoordinates;
//                ArrayList<Double> ECICoordinates = new ArrayList<>();
//                Collections.addAll(ECICoordinates, xC, yC, zC);
//                help.add(ECICoordinates);
//                if (precession == null) {
//                    precession = ECEF_ECI_CONVERSION.precessionMatrix(calendar);
//                    nutation = ECEF_ECI_CONVERSION.nutationMatrix(calendar);
//                    pole = ECEF_ECI_CONVERSION.poleMatrix(calendar);
//                }
//                earthRotation = ECEF_ECI_CONVERSION.rotationMatrix(calendar);
//                ArrayList<ArrayList<Double>> cosine = matrixMult(matrixMult(matrixMult(pole, earthRotation), nutation), precession);
//                ECEFCoordinates = matrixMult(cosine, transpose(help));
//                LLACoordinates = ECEF2LLA.conversion(ECEFCoordinates.get(0).get(0), ECEFCoordinates.get(1).get(0), ECEFCoordinates.get(2).get(0));
//                int doy = calendar.get(GregorianCalendar.DAY_OF_YEAR);
//                int year = calendar.get(GregorianCalendar.YEAR); /* without effect */
//                int sec = calendar.get(GregorianCalendar.HOUR_OF_DAY) * 3600 + calendar.get(GregorianCalendar.MINUTE) * 60 + calendar.get(GregorianCalendar.SECOND);
//
//                double density = drag.densityNow(LLACoordinates[0], LLACoordinates[1], LLACoordinates[2], calendar);

//                double density = 0;
//                try {
//                    density = Orekit.densityNow(LLACoordinates[0], LLACoordinates[1], LLACoordinates[2], doy, year, sec);
//                } catch (OrekitException e) {
//                    e.printStackTrace();
//                }

//                System.out.println("DENSITY = " + density);
                double[] dragForce = Drag.force_div_m_relative(density, satellite.stateVector, v_earth,
                        satellite.m, bbOne, bbTwo, mStar);
//                System.out.println("Sat = " + satellite.m);
//                System.out.println("State = " + state);
//                System.out.println("Drag = " + dragForce);
                state.vx -= dragForce[0];
                state.vy -= dragForce[1];
                state.vz -= dragForce[2];
//                System.out.println("VZ = " + state.vz + " " + dragForce.get(2));
            }
        }
        return state;
    }

    public static List<StateVector> F2(StateVector stateVectorCenterMass, Satellite satelliteOne, Satellite satelliteTwo, double t,
                                       BooleansForIntegration bool, double k, double l) {
        double mStar = satelliteOne.m * satelliteTwo.m / (satelliteOne.m + satelliteTwo.m);
//        StateVector stateOne = forceRelative(stateVectorCenterMass, satelliteOne, t, bool, satelliteTwo.bb, satelliteOne.bb, mStar);
//        StateVector stateTwo = forceRelative(stateVectorCenterMass, satelliteTwo, t, bool, satelliteTwo.bb, satelliteOne.bb, mStar);

        List<StateVector> result = new ArrayList<>();

        // FIXME MAKE IT WITH STEP VALUE
        if (bool.drag && n == 100) {
            calendar.add(Calendar.MILLISECOND, 10);
            n = 0;
            double x = stateVectorCenterMass.x;
            double y = stateVectorCenterMass.y;
            double z = stateVectorCenterMass.z;

            // NRLMSISE-00
            NRLMSISE_Drag drag = new NRLMSISE_Drag();
            density = drag.densityNow(x, y, z, calendar);
        } else {
            n++;
        }

        StateVector stateCenter = force(stateVectorCenterMass, t, bool, satelliteOne.bb, satelliteTwo.bb, satelliteOne.m, satelliteTwo.m);
        StateVector stateOne = forceRelative(stateVectorCenterMass, satelliteOne, t, bool, satelliteOne.bb, satelliteTwo.bb, mStar);
        StateVector stateTwo = forceRelative(stateVectorCenterMass, satelliteTwo, t, bool, satelliteOne.bb, satelliteTwo.bb, mStar); // TODO CHECK BB
//        System.out.println("BEFORE = " + stateOne.vx);

//        List<Double> r1 = new ArrayList<>(Arrays.asList(satelliteOne.stateVector.x, satelliteOne.stateVector.y, satelliteOne.stateVector.z));
//        List<Double> r2 = new ArrayList<>(Arrays.asList(satelliteTwo.stateVector.x, satelliteTwo.stateVector.y, satelliteTwo.stateVector.z));
//        List<Double> r = VectorAlgebra.difference(r2, r1);
//        double dx = VectorAlgebra.absoluteValue(r) - l;
//        tensionFFlag = dx > 0;
//
//        System.out.println("dx = " + dx);

        if (tensionFFlag) {
            double[] rOne = new double[]{satelliteOne.stateVector.x, satelliteOne.stateVector.y, satelliteOne.stateVector.z};
            double[] rTwo = new double[]{satelliteTwo.stateVector.x, satelliteTwo.stateVector.y, satelliteTwo.stateVector.z};
            double[] rTwoOne = VectorAlgebra.difference(rTwo, rOne);
            double[] rOneTwo = VectorAlgebra.difference(rOne, rTwo);
            double dx = VectorAlgebra.absoluteValue(rTwoOne) - l;
            rTwoOne = VectorAlgebra.normalize(rTwoOne);
            rOneTwo = VectorAlgebra.normalize(rOneTwo);

            tensionOne = VectorAlgebra.constMult(k * dx / satelliteOne.m, rTwoOne);
            tensionTwo = VectorAlgebra.constMult(k * dx / satelliteTwo.m, rOneTwo);
//            System.out.println("INSIDE = " + s);
//            System.out.println("TENSION");
//            System.out.println("r21 = " + Arrays.toString(rTwoOne));
//            System.out.println(satelliteOne.m + " = " + satelliteOne.stateVector.x);
//            System.out.println(satelliteTwo.m + " = " + satelliteTwo.stateVector.x);
//            System.out.println(satelliteOne.m + " = " + Arrays.toString(tensionOne));
//            System.out.println(satelliteTwo.m + " = " + Arrays.toString(tensionTwo));
            stateOne.vx += tensionOne[0] / 2;
            stateOne.vy += tensionOne[1] / 2;
            stateOne.vz += tensionOne[2] / 2;
            stateTwo.vx += tensionTwo[0] / 2;
            stateTwo.vy += tensionTwo[1] / 2;
            stateTwo.vz += tensionTwo[2] / 2;
        }

//        List<StateVector> result = new ArrayList<>();
//        StateVector stateCenter = force(stateVectorCenterMass, t, bool, satelliteOne.bb, satelliteTwo.bb, satelliteOne.m, satelliteTwo.m);
//        StateVector stateOne = forceRelative(stateVectorCenterMass, satelliteOne, t, bool, satelliteOne.bb, satelliteTwo.bb, mStar);
//        StateVector stateTwo = forceRelative(stateVectorCenterMass, satelliteTwo, t, bool, satelliteOne.bb, satelliteTwo.bb, mStar); // TODO CHECK BB

        Collections.addAll(result, stateCenter, stateOne, stateTwo);

        return result;
    }
}
