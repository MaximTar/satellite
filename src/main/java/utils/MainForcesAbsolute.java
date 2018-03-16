package utils;

import model.StateVector;
//import jat.spacecraft.Spacecraft; todo fix

import java.util.*;

public class MainForcesAbsolute {

    // Tension block
    public static double[] tensionF;
    public static boolean tensionFFlag = false;

    public static List<Double> sunPos;
    public static boolean sunSignFlag;
    static double[] w_earth = new double[]{0., 0., 7.2921158553E-5};
    static Calendar calendar = new GregorianCalendar();

    static int n = 0; // TODO counter/shitcode

    public static StateVector F(StateVector stateVector, double t, BooleansForIntegration bool, double mass, double area) {

        double x = stateVector.x;
        double y = stateVector.y;
        double z = stateVector.z;
        double vx = stateVector.vx;
        double vy = stateVector.vy;
        double vz = stateVector.vz;

        //FIXME
//        Spacecraft sc = new Spacecraft();
        NRLMSISE_Drag drag = new NRLMSISE_Drag();

        StateVector state = new StateVector();

        double mu = 398600.4415E9;

        state.x = vx;
        state.y = vy;
        state.z = vz;

//        Простейшее приближение force = fm/r
        state.vx = -mu * x / Math.pow(Math.pow(x * x + y * y + z * z, 0.5), 3);
        state.vy = -mu * y / Math.pow(Math.pow(x * x + y * y + z * z, 0.5), 3);
        state.vz = -mu * z / Math.pow(Math.pow(x * x + y * y + z * z, 0.5), 3);

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

//        Atmospheric Drag
        {
            if (bool.drag) {
//                double A = 1; // Площадь сечения
                double drag_coefficient = 2.4; // "Эмпирический коэффициент примерно равный 2"
//                double drag_coefficient = 2; // "Эмпирический коэффициент примерно равный 2"
                double earth_radius = 6371000;

                double[] r = new double[]{x, y, z};
                double[] v_earth = VectorAlgebra.multV(w_earth, r); // Поправка для нахождения относительной скорости
                double radv = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)); // Радиус-вектор

                // Exponential Model
//                double density = Drag.exponentialModelDensity(radv - earth_radius);

                // NRLMSISE-00
                // FIXME MAKE IT WITH STEP VALUE
//                Calendar helpCalendar = (Calendar) calendar.clone();
//                helpCalendar.add(Calendar.MILLISECOND, (int) (t * 1000));

                if (n == 100) {
                    calendar.add(Calendar.MILLISECOND, 1);
                    n = 0;
                } else {
                    n++;
                }

                double density = drag.densityNow(x, y, z, calendar);
//                System.out.println("DENSITY = " + density + " at Z = " + z);
                double[] dragForce = Drag.force_div_m(drag_coefficient, density, stateVector, v_earth, area, mass);

                state.vx -= dragForce[0];
                state.vy -= dragForce[1];
                state.vz -= dragForce[2];
            }
        }
        return state;
    }

    public static StateVector F2(StateVector stateVectorOne, StateVector stateVectorTwo, double t,
                                 BooleansForIntegration bool, double k, double l, double massOne, double areaOne) {
        StateVector resultState = F(stateVectorOne, t, bool, massOne, areaOne);

        if (tensionFFlag) {
            double[] r1 = new double[]{stateVectorOne.x, stateVectorOne.y, stateVectorOne.z};
            double[] r2 = new double[]{stateVectorTwo.x, stateVectorTwo.y, stateVectorTwo.z};
            double[] r = VectorAlgebra.difference(r2, r1);
            double dx = VectorAlgebra.absoluteValue(r) - l;
            r = VectorAlgebra.normalize(r);

            tensionF = VectorAlgebra.constMult(k * dx / massOne, r);
            resultState.vx += tensionF[0];
            resultState.vy += tensionF[1];
            resultState.vz += tensionF[2];
        }

        return resultState;
    }
}
