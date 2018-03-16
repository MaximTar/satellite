package calculation;

import utils.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.orekit.errors.OrekitException;

import java.util.GregorianCalendar;


public class Main extends Application {

    Stage mainWindow, convertWindow;
    Scene startScene, startCoordinateOneScene, startCoordinateTwoScene, startKeplerScene;
    Button startCoordinateButtonOneBody, startKeplerButton, startCoordinateButtonTwoBody;

    @Override
    public void start(Stage stage) {
        mainWindow = stage;
        convertWindow = stage;
        mainWindow.setTitle("Program");
        convertWindow.setTitle("Calculation/Conversion");

        GridPane gridStart = new GridPane();
        gridStart.setPadding(new Insets(15, 15, 15, 15));
        gridStart.setVgap(15);
        gridStart.setHgap(8);

        startCoordinateButtonOneBody = new Button("One Body Coordinate View");
        GridPane.setConstraints(startCoordinateButtonOneBody, 4, 1);
        GridPane.setHalignment(startCoordinateButtonOneBody, HPos.CENTER);

        CoordinatePaneOneBody gridCoordinateOne = new CoordinatePaneOneBody(mainWindow);
        startCoordinateButtonOneBody.setOnAction(event -> {
            startCoordinateOneScene = new Scene(gridCoordinateOne, 780, 490);
            mainWindow.setScene(startCoordinateOneScene);
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            mainWindow.setX((primScreenBounds.getWidth() - mainWindow.getWidth()) / 2);
            mainWindow.setY((primScreenBounds.getHeight() - mainWindow.getHeight()) / 2);
            mainWindow.show();
        });

        startCoordinateButtonTwoBody = new Button("Two Bodies Coordinate View");
        GridPane.setConstraints(startCoordinateButtonTwoBody, 4, 2);
        GridPane.setHalignment(startCoordinateButtonTwoBody, HPos.CENTER);

        CoordinatePaneTwoBody gridCoordinateTwo = new CoordinatePaneTwoBody(mainWindow);
        startCoordinateButtonTwoBody.setOnAction(event -> {
            startCoordinateTwoScene = new Scene(gridCoordinateTwo, 1145, 445);
            mainWindow.setScene(startCoordinateTwoScene);
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            mainWindow.setX((primScreenBounds.getWidth() - mainWindow.getWidth()) / 2);
            mainWindow.setY((primScreenBounds.getHeight() - mainWindow.getHeight()) / 2);
            mainWindow.show();
        });

        startKeplerButton = new Button("Kepler View");
        GridPane.setConstraints(startKeplerButton, 4, 3);
        GridPane.setHalignment(startKeplerButton, HPos.CENTER);

        // TODO KEPLER
//        KeplerPane gridKepler = new KeplerPane(mainWindow);
//        startKeplerButton.setOnAction(event -> {
//            startKeplerScene = new Scene(gridKepler, 620, 250);
//            mainWindow.setScene(startKeplerScene);
//            mainWindow.show();
//        });

        gridStart.getChildren().addAll(startCoordinateButtonOneBody, startCoordinateButtonTwoBody, startKeplerButton);

        startScene = new Scene(gridStart, 250, 170);
        mainWindow.setResizable(false);
        mainWindow.setScene(startScene);
        mainWindow.show();

        // CHECK ECEF-ECI
//        ArrayList<ArrayList<Double>> area = new ArrayList<>();
//        ArrayList<ArrayList<Double>> b = new ArrayList<>();
//        ArrayList<Double> l1 = new ArrayList<>();
//        ArrayList<Double> l2 = new ArrayList<>();
//        ArrayList<Double> l3 = new ArrayList<>();
//        ArrayList<Double> l4 = new ArrayList<>();
//
//        l1.add(1.0);
//        l1.add(2.0);
//        l2.add(3.0);
//        l2.add(4.0);
//        l3.add(5.0);
//        l3.add(6.0);
//        l4.add(7.0);
//        l4.add(8.0);
//        area.add(l1);
//        area.add(l2);
//        b.add(l3);
//        b.add(l4);
//
//        System.out.println(area);
//        System.out.println(b);
//        System.out.println(Matrix.matrixMult(area,b));

//        TimeZone timeZone = TimeZone.getTimeZone("GMT");
//        Calendar c = Calendar.getInstance(timeZone);
//
//        List<Double> moonPos = MoonPositionMeeus.mexFunction(c);
//        System.out.println(moonPos);

//        Quaternion L = new Quaternion(1, 2, 3, 4);
//        Quaternion E = new Quaternion(0, 9, 8, 7);
//        Quaternion E2 = new Quaternion(0, -9, -9, -7);
//        Quaternion L2 = Quaternion.conjugate(L);
//        System.out.println(Quaternion.quatMultQuat(Quaternion.quatMultQuat(L2, E), L));
//        System.out.println(Quaternion.quatMultQuat(Quaternion.quatMultQuat(L2, E2), L));
//        System.out.println(Quaternion.quatMultQuat(Quaternion.quatMultQuat(L, E), L2));
//        System.out.println(Quaternion.quatMultQuat(Quaternion.quatMultQuat(L, E2), L2));
//        System.out.println(Quaternion.quatMultQuat(Quaternion.quatMultQuat()));

//        System.out.println(2 * Math.PI * Math.sqrt(Math.pow(6700000, 3) / 398600.4415E9));
//        Quaternion q1 = new Quaternion(1, 2, 3, 4);
//        Quaternion q2 = new Quaternion(0, 5, 6, 7);
//        System.out.println(Quaternion.quatMultQuat(q1, q2));
//        System.out.println(Quaternion.quatMultQuat(q2, q1));


        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        double m11 = 100;
        double m12 = 1000;
        double ll = 50;
        double rr = 10;
        double RR = 7100000;
        double cc = 2.4;
        double mz = m11 * m12 / (m11 + m12);
        double A = Math.PI * rr * rr;
        System.out.println(A);
        double roo = 4 * mz * ll / (A * RR * RR * cc);
        System.out.println(roo);
        double haa = 500000 - 63.822 * Math.log(roo / 6.967E-13);
        double haa2 = 600000 - 71.835 * Math.log(roo / 1.454E-13);
        System.out.println(Math.sqrt(398600.4415E9 / RR));


        // Данные для относительного равновесия (гравитационная ориентация)
        double m1 = 10000; // масса первого тела
        double r11 = 2.5;
        double m2 = 100; // масса второго тела
        double r22 = 10;

        System.out.println("Inertial1 = " + 0.4 * m1 * r11 * r11);
        System.out.println("Inertial2 = " + 0.4 * m2 * r22 * r22);

//        double x = -2290301.063;
        double x = 7000000;
//        double y = -6379471.940;
        double y = 0.0;
//        double z = 0.0;
        double z = 0.0;

//        double rO = 7100000; // координаты центра масс: (6500000; 0; 0)
        double rO = Math.sqrt(x * x + y * y); // координаты центра масс: (6500000; 0; 0)
        System.out.println("rO = " + rO);
//        double c1 = 1000;
//        double a1 = 1;
        double mu = 398600.4415E9; // гравитационный параметр
        double l = 100; // длина троса
        double k = 10000; // жесткость троса
        double vO = Math.sqrt(mu / rO); // скорость центра масс
        double w = vO / rO; // угловая скорость (кол-во движения)
        double mpr = (m1 * m2) / (m1 + m2); // приведенная масса
        double ldl = (k * l) / (k - 3 * w * w * mpr); // длина растянутого троса
        double r10 = (m2 * ldl) / (m1 + m2);
        double r20 = (m1 * ldl) / (m1 + m2);
        double r1 = rO - (m2 * ldl) / (m1 + m2); // координата х первого тела
        double r2 = rO + (m1 * ldl) / (m1 + m2); // координата х второго тела
        double v1 = w * r1; // скорость первого тела
        double v2 = w * r2; // скорость второго тела
        double T = (2 * Math.PI) / w;
        System.out.println("T = " + T);
        System.out.println("FIRST");
        System.out.println(w);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(v1);
        System.out.println(v2);

        System.out.println("SECOND");

        // Косое относительное равновесие с учетом атмосферы
        double earthR = 6371000; // средний радиус Земли
        double ha = r1 - earthR; // высота первого тела над уровнем моря
//        double ro = Drag.exponentialModelDensity(ha); // плотность атмосферы на высоте ha
//        Spacecraft sc = new Spacecraft();
        NRLMSISE_Drag drag = new NRLMSISE_Drag();
        double ro = drag.densityNow(x, y, z, new GregorianCalendar());

        double rho = -1;
//        try {
//            rho = Orekit.densityNow();
//        } catch (OrekitException e) {
//            e.printStackTrace();
//        }
        System.out.println("!!!");
        System.out.println("ro = " + ro);
        System.out.println("rho = " + rho);
        System.out.println("!!!");
//        System.out.println("FUCK!!!!");
//        System.out.println(NativeNRLMSISE.densityNow(6900000, 0, 0, 270, 2017, 20000));
//        String property = System.getProperty("java.library.path");
//        System.out.println(property);
//        double bb = m1 / (c1 * a1); // m1
        double bb = 212.2; // m1
//        double bb = 2.4; // m1
        double dpb = k * l / (k - 3 * w * w * mpr);
        double w_earth = 7.2921158553E-5;
        double v_earth = w_earth * rO;
        double smth = ro * (vO - v_earth) * (vO - v_earth) / (6 * w * w * dpb * bb);
//        if (smth > Math.PI / 2) smth = Math.PI / 2;
        double acos2 = Math.acos(smth);
        System.out.println("acos2 = " + acos2);
        double alpha2 = Math.PI - acos2;
        double beta2 = alpha2 - Math.PI / 2;
        double r1x = rO - r10 * Math.cos(beta2);
        double r1y = 0 - r10 * Math.sin(beta2);
        double r2x = rO + r20 * Math.cos(beta2);
        double r2y = 0 + r20 * Math.sin(beta2);
        double v1y2 = vO - w * r10 * Math.cos(beta2);
        double v1x2 = w * r10 * Math.sin(beta2);
        double v2y2 = vO + w * r20 * Math.cos(beta2);
        double v2x2 = -w * r20 * Math.sin(beta2);
//        double checkR1 = Math.sqrt(r1x * r1x + r1y * r1y);
//        double checkR2 = Math.sqrt(r2x * r2x + r2y * r2y);
//        double checkV1 = Math.sqrt(v1x * v1x + v1y * v1y);
//        double checkV2 = Math.sqrt(v2x * v2x + v2y * v2y);
        System.out.println("w = " + w);
        System.out.println("mpr = " + mpr);
        System.out.println("ldl = " + ldl);
        System.out.println("r10 = " + r10);
        System.out.println("r20 = " + r20);
        System.out.println("r1 = " + r1);
        System.out.println("r2 = " + r2);
        System.out.println("r2check = " + (ldl + r1));
        System.out.println("v1 = " + v1);
        System.out.println("v2 = " + v2);
        System.out.println("ha = " + ha);
        System.out.println("ro = " + ro);
//        System.out.println("smth: " + smth);
//        System.out.println("beta2: " + beta2);
//        System.out.println("beta2degrees: " + Math.toDegrees(beta2));

        System.out.println("r1x: " + r1x);
        System.out.println("r1y: " + r1y);
        System.out.println("r2x: " + r2x);
        System.out.println("r2y: " + r2y);
        System.out.println("v1x2 = " + v1x2);
        System.out.println("v1y2 = " + v1y2);
        System.out.println("v2x2 = " + v2x2);
        System.out.println("v2y2 = " + v2y2);
//        System.out.println((c1 * ro * VectorAlgebra.absoluteValue(new double[]{v1x2, v1y2, 0}) * VectorAlgebra.absoluteValue(new double[]{v1x2, v1y2, 0}) * a1) / 2);
//        System.out.println("r1sin = " + (r10 * Math.sin(beta2)));
//        System.out.println("r1cos = " + (r10 * Math.cos(beta2)));
//        System.out.println("r2sin = " + (r20 * Math.sin(beta2)));
//        System.out.println("r2cos = " + (r20 * Math.cos(beta2)));

        System.out.println();

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////


//        double earth = 6371000;
//        double x = 0;
//        double y = 0;
//        double c = 2;
//        double s = 1;
//        double m = 1;
//        double h = 0;
//        double z = earth + h;
//
//        System.out.println("DRAG");
//        NRLMSISE_Drag drag = new NRLMSISE_Drag(c, s, m);
//        Time time = new Time(2017, 9, 26, 23, 27, 0);
////        Time time1 = new Time();
//        BodyRef ref = new EarthRef(time);
//        VectorN vectorN = new VectorN(x, y, z);
//        System.out.println(drag.computeDensity(time, ref, vectorN));
//        System.out.println(Drag.exponentialModelDensity(h));


//        Atmosphere atm = new NRLMSISE00();
//        DragForce dragForce = new DragForce(atm, );
//        NRLMSISE_Drag.main();

        ////////////////////////////////////////////

        //CHECK
//        double re = 6371000.0;
//        ArrayList<Double> w_earth = new ArrayList<>();
//        Collections.addAll(w_earth, 0., 0., 7.2921158553E-5);
//        ArrayList<Double> r = new ArrayList<>();
//        Collections.addAll(r, re * Math.sin(Math.toRadians(30)), 0., re * Math.cos(Math.toRadians(30)));
//        List<Double> v_earth = VectorAlgebra.multV(w_earth, r);
//        System.out.println(String.valueOf(VectorAlgebra.absoluteValue(v_earth)));

//        System.out.println(Math.sqrt((r10 * Math.sin(beta2)) * (r10 * Math.sin(beta2)) +
//                (r10 * Math.cos(beta2)) * (r10 * Math.cos(beta2))) +
//                Math.sqrt((r20 * Math.sin(beta2)) * (r20 * Math.sin(beta2)) +
//                        (r20 * Math.cos(beta2)) * (r20 * Math.cos(beta2))));

//        double x1 = rO + ldl * mpr / m2;
//        double x2 = rO - ldl * mpr / m1;
//        System.out.println(x1);
//        System.out.println(x2);
//        double kdl1 = m1 * (w * w * x1 - 398600.4415E9 / (x1 * x1));
//        double kdl2 = m2 * (w * w * a2 - 398600.4415E9 / (a2 * a2));
//        System.out.println(kdl1);
//        System.out.println(kdl2);
//
//        double area = m2 * w * w;
//        double b = -mu * m1 / (x1 * x1) + m1 * w * w * x1;
////        double b = mu * m1 / (a1 * a1) - m1 * w * w * a1;
//        double c = 0;
//        double d = -mu * m2;
//        double q = (2 * b * b * b - 9 * area * b * c + 27 * area * area * d) / (27 * area * area * area);
//        double p = (3 * area * c - b * b) / (3 * area * area);
//        double D = q * q + (4 * p * p * p) / 27;
////        System.out.println("Q");
////        System.out.println(q);
////        System.out.println(D);
//        double ans01 = Math.pow(-q + Math.sqrt(D) / 2, 1. / 3);
//        double ans02 = Math.pow(-q - Math.sqrt(D) / 2, 1. / 3);
////        System.out.println("ans");
////        System.out.println(ans01);
////        System.out.println(ans02);
//        if (ans01 >= 0) {
//            double ans11 = Math.sqrt(ans01);
//            double ans12 = -Math.sqrt(ans01);
////            System.out.println("ans01");
////            System.out.println(ans11);
////            System.out.println(ans12);
//            double t1 = ans11 - p / (3 * ans11);
//            double t2 = ans12 - p / (3 * ans12);
//            double t3 = ans02 - p / (3 * ans02);
////            System.out.println("t");
////            System.out.println(t1);
////            System.out.println(t2);
////            System.out.println(t3);
////            System.out.println(a1 + qz - t1);
////            System.out.println(a1 + qz - t2);
////            System.out.println(a1 + qz - t3);
//        }
//        if (ans02 >= 0) {
//            double ans21 = Math.sqrt(ans02);
//            double ans22 = Math.sqrt(ans02);
////            System.out.println("ans02");
////            System.out.println(ans21);
////            System.out.println(ans22);
//            double t1 = ans21 - p / (3 * ans21);
//            double t2 = ans22 - p / (3 * ans22);
//            double t3 = ans01 - p / (3 * ans01);
////            System.out.println("t");
////            System.out.println(t1);
////            System.out.println(t2);
////            System.out.println(t3);
////            System.out.println(a1 + qz - t1);
////            System.out.println(a1 + qz - t2);
////            System.out.println(a1 + qz - t3);
//        }


//        double misha = (mu * m1 * a2 * a2 * a2 - mu * m2 * x1 * x1 * x1) / (x1 * x1 * a2 * a2 * (x1 + a2));
//        System.out.println("M");
//        System.out.println(misha);
//
//        System.out.println(kdl2 / 2 - kdl1 / 2);

//        double cn = 200;
//        double ap = 0.5;
//        double mt = 1;
//        double ad = ap * cn / (3 * mt * w * w);
//        System.out.println("NEW");
//        double lo = ad / (1 - ad / qz);
//        System.out.println(ad);
//        System.out.println(lo);
//
//        System.out.println("wksi");
//        double wksi = Math.sqrt((2 + (1. / (w * w))) + Math.sqrt(13 - (2. / (w * w)) + (1. / (2 * w * w * w * w))));
//        System.out.println(wksi);
//        System.out.println('!');
//        System.out.println(c);
//        System.out.println(v);
//        System.out.println(w);
//        double sina = b / c;
//        double cosa = area / c;
//        double vx = v * sina;
//        double vy = v * cosa;
//        System.out.println('!');
//        System.out.println(vx);
//        System.out.println(vy);
//        System.out.println(Math.sqrt(vx * vx + vy * vy));


//        System.out.println(Math.sqrt(398600.4415E9 / 6700000));
//        System.out.println(7713.144832618873 / 6700000);
//        System.out.println(Math.sqrt(398600.4415E9 / 6700200));
//        System.out.println(7713.029713631492 / 6700200);
//        System.out.println("R");
//        System.out.println(Math.pow(398600.4415E9 / (0.0011512156466595334 * 0.0011512156466595334), 1. / 3));
//        System.out.println(Math.pow(398600.4415E9 / (0.001151086791256568 * 0.001151086791256568), 1. / 3));
//        System.out.println("T");
//        System.out.println(2 * Math.PI / 0.0011512156466595334);
//        System.out.println(2 * Math.PI / 0.001151086791256568);
//        System.out.println(2 * Math.PI * 6700000 / 7713.144832618873);
//        System.out.println(2 * Math.PI * 6700500 / 7712.857044814634);

//        System.out.println("Density");
//        double h = 10000;
//        for (int qw = 0; qw < 101; qw ++) {
////            System.out.println(h + "   " + Drag.exponentialModelDensity(h));
//            System.out.println(Drag.exponentialModelDensity(h));
//            h += 10000;
//        }

//        System.out.println(Math.sin(Math.toRadians(15)));
//        System.out.println(Math.cos(Math.toRadians(15)));
//        System.out.println(Math.sin(Math.toRadians(5)) * Math.sin(Math.toRadians(5)) + Math.cos(Math.toRadians(5)) * Math.cos(Math.toRadians(5)));
//
//        Quaternion q1 = new Quaternion(0.7071067811865475, 0, 0.7071067811865475, 0);
//        Quaternion q2 = new Quaternion(0.9659258262890683, 0, 0, 0.25881904510252074);
//        Quaternion q3 = Quaternion.quatMultQuat(q2, q1);
//        System.out.println(q3.qw);
//        System.out.println(q3.qx);
//        System.out.println(q3.qy);
//        System.out.println(q3.qz);


//        System.out.println(Math.sin(Math.toRadians(2 / 2)));
//        System.out.println(Math.cos(Math.toRadians(1 / 2)));
//        System.out.println(Math.cos(Math.toRadians(89 / 2)));
//        System.out.println(Math.cos(Math.toRadians(91 / 2)));
//        System.out.println(Math.cos(Math.toRadians(179 / 2)));
//        System.out.println(Math.cos(Math.toRadians(181 / 2)));
//        System.out.println(Math.cos(Math.toRadians(269 / 2)));
//        System.out.println(Math.cos(Math.toRadians(271 / 2)));
//        System.out.println(Math.cos(Math.toRadians(359 / 2)));

//        double qw = 0;
//        double qx = 1;
//        double qy = 0;
//        double qz = 0;
//        double sin = Math.sqrt(1.0 - qw * qw);
//        if (Math.abs(sin) < 0.0005) {
//            sin = 1;
//        }
//
//        System.out.println("OLDOLD");
//        System.out.println((2 * Math.asin(qx) * 180 / Math.PI));
//        System.out.println((2 * Math.asin(qy) * 180 / Math.PI));
//        System.out.println((2 * Math.asin(qz) * 180 / Math.PI));
//
//        System.out.println("OLD");
//        System.out.println((qx / sin));
//        System.out.println((qy / sin));
//        System.out.println((qz / sin));
//
//        System.out.println("NEW");
//        System.out.println(NumberUtils.r2d(Math.atan2(2 * (qw * qx + qy * qz), 1 - 2 * (qx * qx + qy * qy))));
//        System.out.println(NumberUtils.r2d(Math.asin(2 * (qw * qy - qx * qz))));
//        System.out.println(NumberUtils.r2d(Math.atan2(2 * (qw * qz + qx * qy), 1 - 2 * (qy * qy + qz * qz))));

//        System.out.println("!!!");
//        System.out.println(Math.acos(1));
//        System.out.println(Math.acos(0.7));
//        System.out.println(Math.acos(-0.7));
//        System.out.println(Math.acos(-1));
        //System.out.println(Quaternion.calculateAll(0, 0.1, 100, 1, 0, 0, 0, 1, 1E-5, 1E-2));
        //System.out.println((2 * Math.asin(0.4791490184678442)) * 180 / Math.PI);
        //System.out.println((2 * Math.acos(0.8777335689725565)) * 180 / Math.PI);

//        Calendar c2 = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
//        Calendar c6 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c2.set(-1000, Calendar.JULY, 12, 12, 0, 0);
//        System.out.println(MoonPosition.julianDate(c2));
//        System.out.println(SunPosition.julianDate(c2));

//        Calendar c3 = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
//        Calendar c3 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c3.set(2012, Calendar.AUGUST, 3, 12, 2, 3);
//
////        Calendar c4 = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
//        Calendar c4 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c4.set(2003, Calendar.MAY, 11, 11, 23, 23);
//
////        Calendar c5 = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
//        Calendar c5 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c5.set(2020, Calendar.OCTOBER, 1, 1, 1, 1);
//
//        System.out.println(MoonPositionMeeus.mexFunction(c3));
//        System.out.println(MoonPosition.moonPosition(c3));
//        Calendar c0 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c0.set(2003, Calendar.OCTOBER, 17, 19, 30, 30);
//        List<Double> c = SunPosition.sunPosition(c0);
//        System.out.println(Math.sqrt(c.get(0) * c.get(0) + c.get(1) * c.get(1) + c.get(2) * c.get(2)));
//        Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c1.set(2016, Calendar.FEBRUARY, 1, 0, 0, 0);
//        Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c1.set(1992, Calendar.OCTOBER, 13, 0, 0, 0);
//        List<Double> c = MoonPositionMeeus.mexFunction(c1);
//        List<Double> c = SunPosition.sunPosition(c1);
//        System.out.println(c);
//        System.out.println(Math.sqrt(c.get(0)*c.get(0)+c.get(1)*c.get(1)+c.get(2)*c.get(2)));
//        Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c2.set(2016, Calendar.MARCH, 2, 0, 0, 0);
//        Calendar c3 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c3.set(2016, Calendar.APRIL, 1, 0, 0, 0);
//        Calendar c4 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c4.set(2016, Calendar.MAY, 1, 0, 0, 0);
//        Calendar c5 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c5.set(2016, Calendar.JUNE, 2, 0, 0, 0);
//        Calendar c6 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c6.set(2016, Calendar.JULY, 2, 0, 0, 0);
//        Calendar c7 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c7.set(2016, Calendar.AUGUST, 1, 0, 0, 0);
//        Calendar c8 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c8.set(2016, Calendar.SEPTEMBER, 2, 0, 0, 0);
//        Calendar c9 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c9.set(2016, Calendar.OCTOBER, 2, 0, 0, 0);
//        Calendar c10 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c10.set(2016, Calendar.NOVEMBER, 1, 0, 0, 0);
//        Calendar c11 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c11.set(2016, Calendar.DECEMBER, 1, 0, 0, 0);
//        List<Double> c = SunPosition.sunPosition(c0);
//        System.out.println(c);
//        System.out.println(Math.sqrt(c.get(0)*c.get(0)+c.get(1)*c.get(1)+c.get(2)*c.get(2)));
//        c = SunPosition.sunPosition(c1);
//        System.out.println(Math.sqrt(c.get(0)*c.get(0)+c.get(1)*c.get(1)+c.get(2)*c.get(2)));
//        c = SunPosition.sunPosition(c2);
//        System.out.println(Math.sqrt(c.get(0)*c.get(0)+c.get(1)*c.get(1)+c.get(2)*c.get(2)));
//        c = SunPosition.sunPosition(c3);
//        System.out.println(Math.sqrt(c.get(0)*c.get(0)+c.get(1)*c.get(1)+c.get(2)*c.get(2)));
//        c = SunPosition.sunPosition(c4);
//        System.out.println(Math.sqrt(c.get(0)*c.get(0)+c.get(1)*c.get(1)+c.get(2)*c.get(2)));
//        c = SunPosition.sunPosition(c5);
//        System.out.println(Math.sqrt(c.get(0)*c.get(0)+c.get(1)*c.get(1)+c.get(2)*c.get(2)));
//        c = SunPosition.sunPosition(c6);
//        System.out.println(Math.sqrt(c.get(0)*c.get(0)+c.get(1)*c.get(1)+c.get(2)*c.get(2)));
//        c = SunPosition.sunPosition(c7);
//        System.out.println(Math.sqrt(c.get(0)*c.get(0)+c.get(1)*c.get(1)+c.get(2)*c.get(2)));
//        c = SunPosition.sunPosition(c8);
//        System.out.println(Math.sqrt(c.get(0)*c.get(0)+c.get(1)*c.get(1)+c.get(2)*c.get(2)));
//        c = SunPosition.sunPosition(c9);
//        System.out.println(Math.sqrt(c.get(0)*c.get(0)+c.get(1)*c.get(1)+c.get(2)*c.get(2)));
//        c = SunPosition.sunPosition(c10);
//        System.out.println(Math.sqrt(c.get(0)*c.get(0)+c.get(1)*c.get(1)+c.get(2)*c.get(2)));
//        c = SunPosition.sunPosition(c11);
//        System.out.println(Math.sqrt(c.get(0)*c.get(0)+c.get(1)*c.get(1)+c.get(2)*c.get(2)));
//
//
//        System.out.println(MoonPositionMeeus.mexFunction(c1));
//        System.out.println(MoonPosition.moonPosition(c1));
//        System.out.println(MoonPositionMeeus.mexFunction(c2));
//        System.out.println(MoonPosition.moonPosition(c2));
//        System.out.println(MoonPositionMeeus.mexFunction(c3));
//        System.out.println(MoonPosition.moonPosition(c3));
//        System.out.println(MoonPositionMeeus.mexFunction(c4));
//        System.out.println(MoonPosition.moonPosition(c4));
//        System.out.println(MoonPositionMeeus.mexFunction(c5));
//        System.out.println(MoonPosition.moonPosition(c5));
//        System.out.println(MoonPositionMeeus.mexFunction(c6));
//        System.out.println(MoonPosition.moonPosition(c6));
//        System.out.println(MoonPositionMeeus.mexFunction(c7));
//        System.out.println(MoonPosition.moonPosition(c7));
//        System.out.println(MoonPositionMeeus.mexFunction(c8));
//        System.out.println(MoonPosition.moonPosition(c8));
//        System.out.println(MoonPositionMeeus.mexFunction(c9));
//        System.out.println(MoonPosition.moonPosition(c9));
//        System.out.println(MoonPositionMeeus.mexFunction(c10));
//        System.out.println(MoonPosition.moonPosition(c10));
//        System.out.println(MoonPositionMeeus.mexFunction(c11));
//        System.out.println(MoonPosition.moonPosition(c11));

//        System.out.println(MoonPositionMeeus.mexFunction(c4));
//        System.out.println(MoonPosition.moonPosition(c4));
//        System.out.println(MoonPositionMeeus.mexFunction(c5));
//        System.out.println(MoonPosition.moonPosition(c5));
//
//        System.out.println(c5.getTime());
//        List<Double> moonForce2 = MoonPosition.moonPosition(c5);
//        System.out.println(MoonPosition.julianDate(c5));
//        System.out.println(moonForce2);
//        System.out.println(Math.sqrt(Math.pow(moonForce2.get(0) ,2) + Math.pow(moonForce2.get(1) ,2) + Math.pow(moonForce2.get(2) ,2)));
//        double f = 4.0230E8;
//        double s = 0.063455E8;
//        double t = -0.35475E8;
//        System.out.println(Math.sqrt(Math.pow(Math.abs(moonForce2.get(0)) - Math.abs(f),2) + Math.pow(Math.abs(moonForce2.get(1))  - Math.abs(s),2) + Math.pow(Math.abs(moonForce2.get(2)) - Math.abs(t),2)));
//        System.out.println((Math.sqrt(Math.pow(Math.abs(moonForce2.get(0)) - Math.abs(f),2) + Math.pow(Math.abs(moonForce2.get(1))  - Math.abs(s),2) + Math.pow(Math.abs(moonForce2.get(2)) - Math.abs(t),2)))/(Math.sqrt(Math.pow(moonForce2.get(0) ,2) + Math.pow(moonForce2.get(1) ,2) + Math.pow(moonForce2.get(2) ,2))));
//        System.out.println(Math.abs(Math.abs(moonForce2.get(1))-Math.abs(s))/Math.abs(moonForce2.get(1)));

        // CHECK MOON POSITION
//        System.out.println('!');
//        System.out.println(MoonCoordinates.julianDate(c));
//        System.out.println(MoonCoordinates.timeInJC(c));
//        double T = MoonCoordinates.timeInJC(c);
//        System.out.println(MoonCoordinates.moonsMeanLongitude(T));
//        System.out.println(MoonCoordinates.moonsMeanElongation(T));
//        System.out.println(MoonCoordinates.sunsMeanAnomaly(T));
//        System.out.println(MoonCoordinates.moonsMeanAnomaly(T));
//        System.out.println(MoonCoordinates.moonsLatitudeArgument(T));
//        System.out.println(MoonCoordinates.firstArgument(T));
//        System.out.println(MoonCoordinates.secondArgument(T));
//        System.out.println(MoonCoordinates.thirdArgument(T));
//        System.out.println(MoonCoordinates.earthsOrbitEccentricity(T));

        // CHECK SOLAR COORDINATES
//        System.out.println('!');
//        System.out.println(SolarCoordinates.julianDate(c));
//        System.out.println(SolarCoordinates.timeInJC(c));
//        double T = SolarCoordinates.timeInJC(c);
//        System.out.println(SolarCoordinates.sunsMeanLongitude(T));
//        System.out.println(SolarCoordinates.sunsMeanAnomaly(T));
//        System.out.println(SolarCoordinates.earthsOrbitEccentrity(T));
//        System.out.println(SolarCoordinates.sunsCenterEquation(T));
//        System.out.println(SolarCoordinates.sunsTrueLongitude(T));
//        System.out.println(SolarCoordinates.sunsRadiusVector(T));
//        System.out.println(SolarCoordinates.longitude(T));
//        System.out.println(SolarCoordinates.sunsApparentLongitude(T));
//        System.out.println(SolarCoordinates.eclipticsMeanObliquity(T));
//        System.out.println(SolarCoordinates.eclipticsMeanObliquityApparent(T));
//        System.out.println(SolarCoordinates.sunsRightAscensionApparent(T));
//        System.out.println(SolarCoordinates.sunsRightDeclinationApparent(T));
//

        // CHECK ECEF-ECI
//        System.out.println('!');
//        System.out.println(ECEF_ECI_CONVERSION.rotationMatrix(c));
//        Calendar c2 = new GregorianCalendar(2001, 8, 11);
    }

    public static void main(String[] args) {
        launch(args);
    }
}