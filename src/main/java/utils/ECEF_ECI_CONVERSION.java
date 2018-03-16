package utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

//        TimeZone timeZone = TimeZone.getTimeZone("GMT");
//        Calendar c = Calendar.getInstance(timeZone);

public class ECEF_ECI_CONVERSION {
    private static final String XYPOLE = "XYPole";
    private static final String Akoeff1 = "Akoeff1";
    private static final String Akoeff2 = "Akoeff2";
    private static final String Bkoeff1 = "Bkoeff1";
    private static final String Bkoeff2 = "Bkoeff2";
    static List<String> listXYPole, listA1String, listA2String, listB1String, listB2String;
    static List<double[]> listA1Double = new ArrayList<>(), listA2Double = new ArrayList<>(),
            listB1Double = new ArrayList<>(), listB2Double = new ArrayList<>();

    static double pi2 = Math.PI * 2;
    static double piHelp = Math.PI / (3600 * 180);
    static double dpsy = 0;

    static {
        try {
            Path pathA1 = Paths.get(ECEF_ECI_CONVERSION.class.getResource(String.format("/%s", Akoeff1)).toURI());
            Path pathA2 = Paths.get(ECEF_ECI_CONVERSION.class.getResource(String.format("/%s", Akoeff2)).toURI());
            Path pathB1 = Paths.get(ECEF_ECI_CONVERSION.class.getResource(String.format("/%s", Bkoeff1)).toURI());
            Path pathB2 = Paths.get(ECEF_ECI_CONVERSION.class.getResource(String.format("/%s", Bkoeff2)).toURI());
            Path pathXYPole = Paths.get(ECEF_ECI_CONVERSION.class.getResource(String.format("/%s", XYPOLE)).toURI());
            listA1String = Files.readAllLines(pathA1);
            listA2String = Files.readAllLines(pathA2);
            listB1String = Files.readAllLines(pathB1);
            listB2String = Files.readAllLines(pathB2);
            listXYPole = Files.readAllLines(pathXYPole);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

//        assert listA1String != null;
        for (int j = 0; j < listA1String.size() - 1; j++) {
            String[] str = listA1String.get(j).split("\\t");
            double[] parts = new double[str.length];
            for (int k = 0; k < str.length; k++) {
                parts[k] = microArcSec2Rad(Double.parseDouble(str[k]));
            }
            listA1Double.add(parts);
        }

//        assert listA2String != null;
        for (int j = 0; j < listA2String.size() - 1; j++) {
            String[] str = listA2String.get(j).split("\\t");
            double[] parts = new double[str.length];
            for (int k = 0; k < str.length; k++) {
                parts[k] = microArcSec2Rad(Double.parseDouble(str[k]));
            }
            listA2Double.add(parts);
        }

//        assert listB1String != null;
        for (int j = 0; j < listB1String.size() - 1; j++) {
            String[] str = listB1String.get(j).split("\\t");
            double[] parts = new double[str.length];
            for (int k = 0; k < str.length; k++) {
                parts[k] = microArcSec2Rad(Double.parseDouble(str[k]));
            }
            listB1Double.add(parts);
        }

//        assert listB2String != null;
        for (int j = 0; j < listB2String.size() - 1; j++) {
            String[] str = listB2String.get(j).split("\\t");
            double[] parts = new double[str.length];
            for (int k = 0; k < str.length; k++) {
                parts[k] = microArcSec2Rad(Double.parseDouble(str[k]));
            }
            listB2Double.add(parts);
        }
    }

    public static ArrayList<ArrayList<Double>> precessionMatrix(Calendar c) {
        double T = timeInJC(c);
//        System.out.println("T = " + T);
        double z = 0.011180860865024 * T + 5.3071584043699E-6 * T * T + 8.8250634372369E-8 * T * T * T;
        double theta = 0.0097171734551697 * T - 2.0684575704538E-6 * T * T - 2.0281210721855E-7 * T * T * T;
        double dzeta = 0.011180860865024 * T + 1.4635555405335E-6 * T * T + 8.7256766326094E-8 * T * T * T;


        ArrayList<Double> line1 = new ArrayList<>();
        ArrayList<Double> line2 = new ArrayList<>();
        ArrayList<Double> line3 = new ArrayList<>();

        // TODO Узнать про знаки
        line1.add(Math.cos(z) * Math.cos(theta) * Math.cos(dzeta) - Math.sin(z) * Math.sin(dzeta));
        line1.add(-Math.cos(z) * Math.cos(theta) * Math.sin(dzeta) - Math.sin(z) * Math.cos(dzeta));
        line1.add(-Math.cos(z) * Math.sin(theta));

        line2.add(Math.sin(z) * Math.cos(theta) * Math.cos(dzeta) + Math.cos(z) * Math.sin(dzeta));
        line2.add(-Math.sin(z) * Math.cos(theta) * Math.sin(dzeta) + Math.cos(z) * Math.cos(dzeta));
        line2.add(-Math.sin(z) * Math.sin(theta));

        line3.add(Math.sin(theta) * Math.cos(dzeta));
        line3.add(-Math.sin(theta) * Math.sin(dzeta));
        line3.add(Math.cos(theta));

        ArrayList<ArrayList<Double>> precessionMatrix = new ArrayList<>();
        precessionMatrix.add(line1);
        precessionMatrix.add(line2);
        precessionMatrix.add(line3);

        return precessionMatrix;
    }

    public static ArrayList<ArrayList<Double>> nutationMatrix(Calendar c) {
        double T = timeInJC(c);
        double dpsy = deltaPsi(c);
//        double dpsy = 0;
        double de = deltaEps(c);
//        double de = 0;

        double e = 0.40909280422233 - 0.00022696552481143 * T - 2.8604007185463E-9 * T * T + 8.7896720385159E-9 * T * T * T;
//        double e = 0;
        double et = e + de;
//        double et = 0;

        double sinDpsy = Math.sin(dpsy);
        double cosDpsy = Math.cos(dpsy);
        double sinE = Math.sin(e);
        double cosE = Math.cos(e);
        double sinEt = Math.sin(et);
        double cosEt = Math.cos(et);

        ArrayList<Double> line1 = new ArrayList<>();
        ArrayList<Double> line2 = new ArrayList<>();
        ArrayList<Double> line3 = new ArrayList<>();

        line1.add(cosDpsy);
        line1.add(-sinDpsy * cosE);
        line1.add(-sinDpsy * sinE);

        line2.add(sinDpsy * cosEt);
        line2.add(cosDpsy * cosEt * cosE + sinEt * sinE);
        line2.add(cosDpsy * cosEt * sinE - sinEt * cosE);

        line3.add(sinDpsy * sinEt);
        line3.add(cosDpsy * sinEt * cosE - cosEt * sinE);
        line3.add(cosDpsy * sinEt * sinE + cosEt * cosE);

        return new ArrayList<>(Arrays.asList(line1, line2, line3));
    }

    /////////////////////// FOR NUTATION MATRIX ////////////////////////

//        ArrayList<Double> line12 = new ArrayList<>();
//        ArrayList<Double> line22 = new ArrayList<>();
//        ArrayList<Double> line32 = new ArrayList<>();
//
//        line12.add(1.0);
//        line12.add(-dpsy * Math.cos(e));
//        line12.add(-dpsy * Math.sin(e));
//
//        line22.add(dpsy * Math.cos(et));
//        line22.add(1.0);
//        line22.add(-de);
//
//        line32.add(dpsy * Math.sin(et));
//        line32.add(de);
//        line32.add(1.0);
//
//        ArrayList<ArrayList<Double>> nutationMatrix2 = new ArrayList<>();
//        nutationMatrix2.add(line12);
//        nutationMatrix2.add(line22);
//        nutationMatrix2.add(line32);

    ////////////////////////////////////////////////////////////////////

    public static ArrayList<ArrayList<Double>> rotationMatrix(Calendar c) {
        double gast = gast(c);

        ArrayList<Double> line1 = new ArrayList<>();
        ArrayList<Double> line2 = new ArrayList<>();
        ArrayList<Double> line3 = new ArrayList<>();

        line1.add(Math.cos(gast));
        line1.add(Math.sin(gast));
        line1.add(0.0);

        line2.add(-Math.sin(gast));
        line2.add(Math.cos(gast));
        line2.add(0.0);

        line3.add(0.0);
        line3.add(0.0);
        line3.add(1.0);

        ArrayList<ArrayList<Double>> rotationMatrix = new ArrayList<>();
        rotationMatrix.add(line1);
        rotationMatrix.add(line2);
        rotationMatrix.add(line3);

        return rotationMatrix;
    }

    public static double timeInJC(Calendar c) {
        double JD = julianDate(c) - 2451545.0;
        return JD / 36525.0;
    }

    public static double julianDate(Calendar c) {
        double Y = c.get(Calendar.YEAR);
        double M = c.get(Calendar.MONTH) + 1;
        double D = c.get(Calendar.DAY_OF_MONTH);
        double H = c.get(Calendar.HOUR_OF_DAY);
        if (M <= 2) {
            Y = Y - 1;
            M = M + 12;
        }
        double JY = 365.25 * Y;
        double JM = 30.6001 * (M + 1);

        return (int) JY + (int) JM + D + H / 24 + 1720981.5;
    }

    // Вместо UT1 тут UTC
    public static double gast(Calendar c) {
        double deg2rad = Math.PI / 180;
        double T = timeInJC(c);
        double TT = T * T;
        double TTT = T * TT;
        double TTTT = TT * TT;
        double Om = 2.1824391966157 - 33.757044612636 * T + 3.6226247879867E-5 * TT + 3.7340349719056E-8 * TTT - 2.8793084521095E-10 * TTTT;
        double e = 0.40909280422233 - 0.00022696552481143 * T - 2.8604007185463E-9 * TT + 8.7896720385159E-9 * TTT;

        double gmst = ((-6.2E-6 * TTT + 0.093104 * TT + (876600.0 * 3600.0 + 8640184.812866) * T + 67310.54841)
                * deg2rad / 240) % (pi2);
        if (gmst < 0.0) {
            gmst = gmst + pi2;
        }

        if (dpsy == 0) {
            dpsy = deltaPsi(c);
        }

        return (gmst + dpsy * Math.cos(e) + 0.00264 * piHelp * Math.sin(Om) + 0.000063 * piHelp * Math.sin(2.0 * Om)) % (pi2);
    }

    public static ArrayList<ArrayList<Double>> poleMatrix(Calendar c) {
        double Y = c.get(Calendar.YEAR);
        double M = c.get(Calendar.MONTH) + 1;
        double D = c.get(Calendar.DAY_OF_MONTH);
        double xp = 0;
        double yp = 0;

        assert listXYPole != null;
        if (Double.parseDouble(listXYPole.get(listXYPole.size() - 1).split(" ")[0]) < Y) {
            // TODO THINK 'BOUT IT
        } else {
            for (int j = 0; j < listXYPole.size() - 1; j++) {
                String[] parts = listXYPole.get(j).split(" ");
                if (Double.parseDouble(parts[0]) == Y && Double.parseDouble(parts[1]) == M && Double.parseDouble(parts[2]) == D) {
                    xp = Double.parseDouble(parts[4]) * pi2 / 648000;
                    yp = Double.parseDouble(parts[5]) * pi2 / 648000;
                }
            }
        }

        ArrayList<Double> line1 = new ArrayList<>();
        ArrayList<Double> line2 = new ArrayList<>();
        ArrayList<Double> line3 = new ArrayList<>();

        line1.add(Math.cos(xp));
        line1.add(Math.sin(xp) * Math.sin(yp));
        line1.add(Math.sin(xp) * Math.cos(yp));

        line2.add(0.0);
        line2.add(Math.cos(yp));
        line2.add(-Math.sin(yp));

        line3.add(-Math.sin(xp));
        line3.add(Math.cos(xp) * Math.sin(yp));
        line3.add(Math.cos(xp) * Math.cos(yp));

        ArrayList<ArrayList<Double>> poleMatrix = new ArrayList<>();
        poleMatrix.add(line1);
        poleMatrix.add(line2);
        poleMatrix.add(line3);

        return poleMatrix;
    }

    public static double microArcSec2Rad(double arcsec) {
        return arcsec * pi2 * 1E-6 / 648000;
    }

    public static double deltaPsi(Calendar c) {
        double T = timeInJC(c);
        double dpsy = 0;
        double arg;
        double l = 2.3555557434939 + 8328.6914257191 * T + 0.00015455472302827 * T * T + 2.5033354424091E-7 * T * T * T - 1.186339077675E-9 * T * T * T * T;
        double lhatch = 6.2400601269133 + 628.3019551714 * T - 2.681989283898E-6 * T * T + 6.5934660630897E-10 * T * T * T - 5.5705091959486E-11 * T * T * T * T;
        double F = 1.6279050815375 + 8433.4661569164 * T - 6.1819562105639E-5 * T * T - 5.0275178731059E-9 * T * T * T + 2.0216730502268E-11 * T * T * T * T;
        double D = 5.1984665886602 + 7771.3771455937 * T - 3.0885540368764E-5 * T * T + 3.1963765995552E-8 * T * T * T - 1.5363745554361E-10 * T * T * T * T;
        double Om = 2.1824391966157 - 33.757044612636 * T + 3.6226247879867E-5 * T * T + 3.7340349719056E-8 * T * T * T - 2.8793084521095E-10 * T * T * T * T;

//        assert listA1String != null;
//        for (int j = 0; j < listA1String.size() - 1; j++) {
//            String[] parts = listA1String.get(j).split("\\t");
//            double a1 = microArcSec2Rad(Double.parseDouble(parts[1]));
//            double a2 = microArcSec2Rad(Double.parseDouble(parts[2]));
//            double n1 = microArcSec2Rad(Double.parseDouble(parts[3]));
//            double n2 = microArcSec2Rad(Double.parseDouble(parts[4]));
//            double n3 = microArcSec2Rad(Double.parseDouble(parts[5]));
//            double n4 = microArcSec2Rad(Double.parseDouble(parts[6]));
//            double n5 = microArcSec2Rad(Double.parseDouble(parts[7]));
//            arg = n1 * l + n2 * lhatch + n3 * F + n4 * D + n5 * Om;
//            dpsy = dpsy + (a1 * Math.sin(arg) + a2 * Math.cos(arg));
//        }
        for (int j = 0; j < listA1Double.size() - 1; j++) {
            double[] parts = listA1Double.get(j);
            double a1 = parts[1];
            double a2 = parts[2];
            double n1 = parts[3];
            double n2 = parts[4];
            double n3 = parts[5];
            double n4 = parts[6];
            double n5 = parts[7];
            arg = n1 * l + n2 * lhatch + n3 * F + n4 * D + n5 * Om;
            dpsy = dpsy + (a1 * Math.sin(arg) + a2 * Math.cos(arg));
        }

//        assert listA2String != null;
//        for (int j = 0; j < listA2String.size() - 1; j++) {
//            String[] parts = listA2String.get(j).split("\\t");
//            double a3 = microArcSec2Rad(Double.parseDouble(parts[1]));
//            double a4 = microArcSec2Rad(Double.parseDouble(parts[2]));
//            double n1 = microArcSec2Rad(Double.parseDouble(parts[3]));
//            double n2 = microArcSec2Rad(Double.parseDouble(parts[4]));
//            double n3 = microArcSec2Rad(Double.parseDouble(parts[5]));
//            double n4 = microArcSec2Rad(Double.parseDouble(parts[6]));
//            double n5 = microArcSec2Rad(Double.parseDouble(parts[7]));
//            arg = n1 * l + n2 * lhatch + n3 * F + n4 * D + n5 * Om;
//            dpsy = dpsy + (a3 * Math.sin(arg) + a4 * Math.cos(arg)) * T;
//        }
        for (int j = 0; j < listA2Double.size() - 1; j++) {
            double[] parts = listA2Double.get(j);
            double a3 = parts[1];
            double a4 = parts[2];
            double n1 = parts[3];
            double n2 = parts[4];
            double n3 = parts[5];
            double n4 = parts[6];
            double n5 = parts[7];
            arg = n1 * l + n2 * lhatch + n3 * F + n4 * D + n5 * Om;
            dpsy = dpsy + (a3 * Math.sin(arg) + a4 * Math.cos(arg)) * T;
        }

        return dpsy;
    }

    public static double deltaEps(Calendar c) {
        double T = timeInJC(c);
        double de = 0;
        double arg;
        double l = 2.3555557434939 + 8328.6914257191 * T + 0.00015455472302827 * T * T + 2.5033354424091E-7 * T * T * T - 1.186339077675E-9 * T * T * T * T;
        double lhatch = 6.2400601269133 + 628.3019551714 * T - 2.681989283898E-6 * T * T + 6.5934660630897E-10 * T * T * T - 5.5705091959486E-11 * T * T * T * T;
        double F = 1.6279050815375 + 8433.4661569164 * T - 6.1819562105639E-5 * T * T - 5.0275178731059E-9 * T * T * T + 2.0216730502268E-11 * T * T * T * T;
        double D = 5.1984665886602 + 7771.3771455937 * T - 3.0885540368764E-5 * T * T + 3.1963765995552E-8 * T * T * T - 1.5363745554361E-10 * T * T * T * T;
        double Om = 2.1824391966157 - 33.757044612636 * T + 3.6226247879867E-5 * T * T + 3.7340349719056E-8 * T * T * T - 2.8793084521095E-10 * T * T * T * T;

//        assert listB1String != null;
//        for (int j = 0; j < listB1String.size() - 1; j++) {
//            String[] parts = listB1String.get(j).split("\\t");
//            double b1 = microArcSec2Rad(Double.parseDouble(parts[1]));
//            double b2 = microArcSec2Rad(Double.parseDouble(parts[2]));
//            double n1 = microArcSec2Rad(Double.parseDouble(parts[3]));
//            double n2 = microArcSec2Rad(Double.parseDouble(parts[4]));
//            double n3 = microArcSec2Rad(Double.parseDouble(parts[5]));
//            double n4 = microArcSec2Rad(Double.parseDouble(parts[6]));
//            double n5 = microArcSec2Rad(Double.parseDouble(parts[7]));
//            arg = n1 * l + n2 * lhatch + n3 * F + n4 * D + n5 * Om;
//            de = de + (b1 * Math.cos(arg) + b2 * Math.sin(arg));
//        }
        for (int j = 0; j < listB1Double.size() - 1; j++) {
            double[] parts = listB1Double.get(j);
            double b1 = parts[1];
            double b2 = parts[2];
            double n1 = parts[3];
            double n2 = parts[4];
            double n3 = parts[5];
            double n4 = parts[6];
            double n5 = parts[7];
            arg = n1 * l + n2 * lhatch + n3 * F + n4 * D + n5 * Om;
            de = de + (b1 * Math.cos(arg) + b2 * Math.sin(arg));
        }

//        assert listB2String != null;
//        for (int j = 0; j < listB2String.size() - 1; j++) {
//            String[] parts = listB2String.get(j).split("\\t");
//            double b3 = microArcSec2Rad(Double.parseDouble(parts[1]));
//            double b4 = microArcSec2Rad(Double.parseDouble(parts[2]));
//            double n1 = microArcSec2Rad(Double.parseDouble(parts[3]));
//            double n2 = microArcSec2Rad(Double.parseDouble(parts[4]));
//            double n3 = microArcSec2Rad(Double.parseDouble(parts[5]));
//            double n4 = microArcSec2Rad(Double.parseDouble(parts[6]));
//            double n5 = microArcSec2Rad(Double.parseDouble(parts[7]));
//            arg = n1 * l + n2 * lhatch + n3 * F + n4 * D + n5 * Om;
//            de = de + (b3 * Math.cos(arg) + b4 * Math.sin(arg)) * T;
//        }
        for (int j = 0; j < listB2Double.size() - 1; j++) {
            double[] parts = listB2Double.get(j);
            double b3 = parts[1];
            double b4 = parts[2];
            double n1 = parts[3];
            double n2 = parts[4];
            double n3 = parts[5];
            double n4 = parts[6];
            double n5 = parts[7];
            arg = n1 * l + n2 * lhatch + n3 * F + n4 * D + n5 * Om;
            de = de + (b3 * Math.cos(arg) + b4 * Math.sin(arg)) * T;
        }

        return de;
    }
}
