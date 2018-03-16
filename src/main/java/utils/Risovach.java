package utils;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

public class Risovach extends Application {

    private static final String titleTxt = "Animation";
    Stage primaryStage;
    static List<String> list;
    static List<Double> x1 = new ArrayList<>();
    static List<Double> x2 = new ArrayList<>();
    static List<Double> y1 = new ArrayList<>();
    static List<Double> y2 = new ArrayList<>();
    static List<Double> z1 = new ArrayList<>();
    static List<Double> z2 = new ArrayList<>();
    private Scene resultScene;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {

        primaryStage = stage;
        primaryStage.setTitle(titleTxt);

        // Window label
        Label label = new Label("Choose file to draw");
        HBox labelHb = new HBox();
        labelHb.setAlignment(Pos.CENTER);
        labelHb.getChildren().add(label);

        // Buttons
        Button btnOneBodyOrbit = new Button("Risovalka");
        btnOneBodyOrbit.setOnAction(new ButtonOneBodyOrbitListener());
        VBox buttonVb = new VBox(10);
        buttonVb.setAlignment(Pos.CENTER);
        buttonVb.getChildren().addAll(btnOneBodyOrbit);

        // Status message text
        Text actionStatus = new Text();
        actionStatus.setFont(Font.font("Calibri", FontWeight.NORMAL, 20));
        actionStatus.setFill(Color.FIREBRICK);

        // Vbox
        VBox vbox = new VBox(30);
        vbox.setPadding(new Insets(25, 25, 25, 25));
        vbox.getChildren().addAll(labelHb, buttonVb, actionStatus);

        // Scene
        Scene scene = new Scene(vbox, 300, 280); // w x h
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private class ButtonOneBodyOrbitListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            oneBodyOrbitFileChooser();
        }
    }

    private void oneBodyOrbitFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File selectedFile = fileChooser.showOpenDialog(null);
        fileChooser.setTitle("Select text file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        if (selectedFile != null) {
            XYChart.Series series = new XYChart.Series();
            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();
            final LineChart<Number, Number> lineChart = new
                    LineChart<>(xAxis, yAxis);
            Path path = Paths.get(selectedFile.getPath());
            try {
                list = Files.readAllLines(path);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            System.out.println(list.size());
            int n = 0;
            for (int j = 0; j < list.size() - 1; j += 1) {
                String[] parts = list.get(j).split("\\t\\t\\t");
                x1.add(n, parseDouble(parts[0]));
                y1.add(n, parseDouble(parts[1]));
                z1.add(n, parseDouble(parts[2]));
                x2.add(n, parseDouble(parts[6]));
                y2.add(n, parseDouble(parts[7]));
                z2.add(n, parseDouble(parts[8]));
                n += 1;
            }

            for (int i = 0; i < x1.size(); i += 1000) {
                double xyz1 = Math.sqrt(x1.get(i) * x1.get(i) + y1.get(i) * y1.get(i) + z1.get(i) * z1.get(i));
                double xyz2 = Math.sqrt(x2.get(i) * x2.get(i) + y2.get(i) * y2.get(i) + z2.get(i) * z2.get(i));
                double dx = Math.abs(xyz1 - xyz2) - 100;
                series.getData().add(new XYChart.Data(i, dx));

            }
            lineChart.getData().add(series);
            lineChart.setCreateSymbols(false);
            resultScene = new Scene(lineChart, 1000, 600);
            primaryStage.setScene(resultScene);
            primaryStage.show();
        }
    }
}