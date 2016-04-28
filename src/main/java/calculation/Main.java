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
import main.java.utils.CalculationUtils;
import main.java.utils.GeoPot;
import main.java.utils.NumberUtils;
import main.java.utils.WritingUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


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
            startCoordinateScene = new Scene(gridCoordinate, 600, 250);
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
    }

    public static void main(String[] args) {
        launch(args);
    }
}