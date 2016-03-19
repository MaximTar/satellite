package calculation;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import utils.GeoPot;


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
        startCoordinateButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                startCoordinateScene = new Scene(gridCoordinate, 600, 250);
                mainWindow.setScene(startCoordinateScene);
                mainWindow.show();
            }
        });

        startKeplerButton = new Button("Kepler View");
        GridPane.setConstraints(startKeplerButton, 4, 2);
        GridPane.setHalignment(startKeplerButton, HPos.CENTER);

        KeplerPane gridKepler = new KeplerPane(mainWindow);
        startKeplerButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                startKeplerScene = new Scene(gridKepler, 620, 250);
                mainWindow.setScene(startKeplerScene);
                mainWindow.show();
            }
        });

        gridStart.getChildren().addAll(startCoordinateButton, startKeplerButton);

        startScene = new Scene(gridStart, 200, 130);
        mainWindow.setResizable(false);
        mainWindow.setScene(startScene);
        mainWindow.show();
        System.out.println("yz");
        System.out.println(GeoPot.calc(0, 10000000, 10000000, 1));
        System.out.println(GeoPot.calc(0, 10000000, 10000000, 2));
        System.out.println(GeoPot.calc(0, 10000000, 10000000, 3));
        System.out.println(GeoPot.calc(0, 10000000, 10000000, 4));
        System.out.println(GeoPot.calc(0, 10000000, 10000000, 5));
        System.out.println(GeoPot.calc(0, 10000000, 10000000, 6));
        System.out.println(GeoPot.calc(0, 10000000, 10000000, 7));
        System.out.println(GeoPot.calc(0, 10000000, 10000000, 8));
        System.out.println(GeoPot.calc(0, 10000000, 10000000, 9));
        System.out.println(GeoPot.calc(0, 10000000, 10000000, 10));
        System.out.println(GeoPot.calc(0, 10000000, 10000000, 11));
        System.out.println(GeoPot.calc(0, 10000000, 10000000, 12));
        System.out.println("xz");
        System.out.println(GeoPot.calc(10000000, 0, 10000000, 1));
        System.out.println(GeoPot.calc(10000000, 0, 10000000, 2));
        System.out.println(GeoPot.calc(10000000, 0, 10000000, 3));
        System.out.println(GeoPot.calc(10000000, 0, 10000000, 4));
        System.out.println(GeoPot.calc(10000000, 0, 10000000, 5));
        System.out.println(GeoPot.calc(10000000, 0, 10000000, 6));
        System.out.println(GeoPot.calc(10000000, 0, 10000000, 7));
        System.out.println(GeoPot.calc(10000000, 0, 10000000, 8));
        System.out.println(GeoPot.calc(10000000, 0, 10000000, 9));
        System.out.println(GeoPot.calc(10000000, 0, 10000000, 10));
        System.out.println(GeoPot.calc(10000000, 0, 10000000, 11));
        System.out.println(GeoPot.calc(10000000, 0, 10000000, 12));
        System.out.println("xy");
        System.out.println(GeoPot.calc(10000000, 10000000, 0, 1));
        System.out.println(GeoPot.calc(10000000, 10000000, 0, 2));
        System.out.println(GeoPot.calc(10000000, 10000000, 0, 3));
        System.out.println(GeoPot.calc(10000000, 10000000, 0, 4));
        System.out.println(GeoPot.calc(10000000, 10000000, 0, 5));
        System.out.println(GeoPot.calc(10000000, 10000000, 0, 6));
        System.out.println(GeoPot.calc(10000000, 10000000, 0, 7));
        System.out.println(GeoPot.calc(10000000, 10000000, 0, 8));
        System.out.println(GeoPot.calc(10000000, 10000000, 0, 9));
        System.out.println(GeoPot.calc(10000000, 10000000, 0, 10));
        System.out.println(GeoPot.calc(10000000, 10000000, 0, 11));
        System.out.println(GeoPot.calc(10000000, 10000000, 0, 12));
        System.out.println("z");
        System.out.println(GeoPot.calc(0, 0, 10000000, 1));
        System.out.println(GeoPot.calc(0, 0, 10000000, 2));
        System.out.println(GeoPot.calc(0, 0, 10000000, 3));
        System.out.println(GeoPot.calc(0, 0, 10000000, 4));
        System.out.println(GeoPot.calc(0, 0, 10000000, 5));
        System.out.println(GeoPot.calc(0, 0, 10000000, 6));
        System.out.println(GeoPot.calc(0, 0, 10000000, 7));
        System.out.println(GeoPot.calc(0, 0, 10000000, 8));
        System.out.println(GeoPot.calc(0, 0, 10000000, 9));
        System.out.println(GeoPot.calc(0, 0, 10000000, 10));
        System.out.println(GeoPot.calc(0, 0, 10000000, 11));
        System.out.println(GeoPot.calc(0, 0, 10000000, 12));
        System.out.println("y");
        System.out.println(GeoPot.calc(0, 10000000, 0, 1));
        System.out.println(GeoPot.calc(0, 10000000, 0, 2));
        System.out.println(GeoPot.calc(0, 10000000, 0, 3));
        System.out.println(GeoPot.calc(0, 10000000, 0, 4));
        System.out.println(GeoPot.calc(0, 10000000, 0, 5));
        System.out.println(GeoPot.calc(0, 10000000, 0, 6));
        System.out.println(GeoPot.calc(0, 10000000, 0, 7));
        System.out.println(GeoPot.calc(0, 10000000, 0, 8));
        System.out.println(GeoPot.calc(0, 10000000, 0, 9));
        System.out.println(GeoPot.calc(0, 10000000, 0, 10));
        System.out.println(GeoPot.calc(0, 10000000, 0, 11));
        System.out.println(GeoPot.calc(0, 10000000, 0, 12));
        System.out.println("x");
        System.out.println(GeoPot.calc(10000000, 0, 0, 1));
        System.out.println(GeoPot.calc(10000000, 0, 0, 2));
        System.out.println(GeoPot.calc(10000000, 0, 0, 3));
        System.out.println(GeoPot.calc(10000000, 0, 0, 4));
        System.out.println(GeoPot.calc(10000000, 0, 0, 5));
        System.out.println(GeoPot.calc(10000000, 0, 0, 6));
        System.out.println(GeoPot.calc(10000000, 0, 0, 7));
        System.out.println(GeoPot.calc(10000000, 0, 0, 8));
        System.out.println(GeoPot.calc(10000000, 0, 0, 9));
        System.out.println(GeoPot.calc(10000000, 0, 0, 10));
        System.out.println(GeoPot.calc(10000000, 0, 0, 11));
        System.out.println(GeoPot.calc(10000000, 0, 0, 12));
    }


    public static void main(String[] args) {
        launch(args);
    }
}