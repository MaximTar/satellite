package main.java.calculation;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.java.utils.*;

import java.util.*;


public class Main extends Application {

    Stage mainWindow, convertWindow;
    Scene startScene, startCoordinateScene, startKeplerScene;
    Button startCoordinateButton, startKeplerButton;

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

        startCoordinateButton = new Button("Coordinate View");
        GridPane.setConstraints(startCoordinateButton, 4, 1);
        GridPane.setHalignment(startCoordinateButton, HPos.CENTER);

        CoordinatePane gridCoordinate = new CoordinatePane(mainWindow);
        startCoordinateButton.setOnAction(event -> {
            startCoordinateScene = new Scene(gridCoordinate, 600, 300);
            mainWindow.setScene(startCoordinateScene);
            mainWindow.show();
        });

        startKeplerButton = new Button("Kepler View");
        GridPane.setConstraints(startKeplerButton, 4, 2);
        GridPane.setHalignment(startKeplerButton, HPos.CENTER);

        KeplerPane gridKepler = new KeplerPane(mainWindow);
        startKeplerButton.setOnAction(event -> {
            startKeplerScene = new Scene(gridKepler, 620, 250);
            mainWindow.setScene(startKeplerScene);
            mainWindow.show();
        });

        gridStart.getChildren().addAll(startCoordinateButton, startKeplerButton);

        startScene = new Scene(gridStart, 200, 130);
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

//        TimeZone timeZone = TimeZone.getTimeZone("GMT");
//        Calendar c = Calendar.getInstance(timeZone);

        Calendar c = GregorianCalendar.getInstance();
        c.set(1992, Calendar.APRIL, 12, 0, 0, 0);

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