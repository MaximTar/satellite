package main.java.calculation;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Cylinder;
import javafx.stage.Stage;
import main.java.utils.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
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
//        Calendar c2 = new GregorianCalendar(2001, 8, 11);
    }

    public static void main(String[] args) {
        launch(args);
    }
}