package calculation;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.*;


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

        KeplerPane gridKepler = new KeplerPane(mainWindow);
        startKeplerButton.setOnAction(event -> {
            startKeplerScene = new Scene(gridKepler, 620, 250);
            mainWindow.setScene(startKeplerScene);
            mainWindow.show();
        });

        gridStart.getChildren().addAll(startCoordinateButtonOneBody, startCoordinateButtonTwoBody, startKeplerButton);

        startScene = new Scene(gridStart, 250, 170);
        mainWindow.setResizable(false);
        mainWindow.setScene(startScene);
        mainWindow.show();

        // CHECK ECEF-ECI
//        ArrayList<ArrayList<Double>> a = new ArrayList<>();
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
//        a.add(l1);
//        a.add(l2);
//        b.add(l3);
//        b.add(l4);
//
//        System.out.println(a);
//        System.out.println(b);
//        System.out.println(Matrix.matrixMult(a,b));

        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Calendar c = Calendar.getInstance(timeZone);

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

        System.out.println(Math.sqrt(398600.4415E9/Math.pow(6700000, 1)));
        System.out.println(7713.144832618873/6700000);
        System.out.println(Math.sqrt(398600.4415E9/Math.pow(6700500, 1)));
        System.out.println(7712.857044814634/6700500);
//        System.out.println(Math.sin(Math.toRadians(15)));
//        System.out.println(Math.cos(Math.toRadians(15)));
//        System.out.println(Math.sin(Math.toRadians(5)) * Math.sin(Math.toRadians(5)) + Math.cos(Math.toRadians(5)) * Math.cos(Math.toRadians(5)));
//
//        Quaternion q1 = new Quaternion(0.7071067811865475, 0, 0.7071067811865475, 0);
//        Quaternion q2 = new Quaternion(0.9659258262890683, 0, 0, 0.25881904510252074);
//        Quaternion q3 = Quaternion.quatMultQuat(q2, q1);
//        System.out.println(q3.i);
//        System.out.println(q3.j);
//        System.out.println(q3.k);
//        System.out.println(q3.l);


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
//        c2.set(1992, Calendar.APRIL, 12, 0, 0, 0);
//
//        Calendar c3 = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c3.set(2012, Calendar.AUGUST, 3, 12, 2, 3);
//
//        Calendar c4 = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c4.set(2003, Calendar.MAY, 11, 11, 23, 23);
//
//        Calendar c5 = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
//        c5.set(2020, Calendar.OCTOBER, 1, 1, 1, 1);
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
//        System.out.println(EcefEci.rotationMatrix(c));
//        Calendar c2 = new GregorianCalendar(2001, 8, 11);
    }

    public static void main(String[] args) {
        launch(args);
    }
}