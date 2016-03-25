package main.java.calculation;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.java.utils.CalculationUtils;
import main.java.utils.NumberUtils;

import java.io.*;

import java.util.ArrayList;
import java.util.List;

public class CoordinatePane extends GridPane {

    private Scene resultScene;

    private TextField t0Input = new TextField("0");
    private TextField dtInput = new TextField("1000");
    private TextField tMaxInput = new TextField("86500");
    private TextField Vx0Input = new TextField("3100.0");
    private TextField Vy0Input = new TextField("0.0");
    private TextField Vz0Input = new TextField("0.0");
    private TextField x0Input = new TextField("0.0");
    private TextField y0Input = new TextField("4.2E7");
    private TextField z0Input = new TextField("0.0");

    public CoordinatePane(Stage mainWindow) {
        this.setPadding(new Insets(15, 15, 15, 15));
        this.setVgap(15);
        this.setHgap(8);

        Label t0Label = new Label("t0:");
        Label dtLabel = new Label("dt:");
        Label tMaxLabel = new Label("tMax:");
        Label vx0Label = new Label("Vx0:");
        Label vy0Label = new Label("Vy0:");
        Label vz0Label = new Label("Vz0:");
        Label x0Label = new Label("x0:");
        Label y0Label = new Label("y0:");
        Label z0Label = new Label("z0:");

        this.addRow(0, t0Label, t0Input, vx0Label, Vx0Input, x0Label, x0Input);
        this.addRow(1, dtLabel, dtInput, vy0Label, Vy0Input, y0Label, y0Input);
        this.addRow(2, tMaxLabel, tMaxInput, vz0Label, Vz0Input, z0Label, z0Input);

        Button startButton = new Button("Start");
        this.add(startButton, 0, 3, 6, 1);
        setHalignment(startButton, HPos.CENTER);

        startButton.setOnAction(event -> {
            if (NumberUtils.textFieldsAreDouble(t0Input, dtInput, tMaxInput, Vx0Input, Vy0Input, Vz0Input,
                    x0Input, y0Input, z0Input)) {
                //defining the axes
                final NumberAxis xAxis = new NumberAxis();
                final NumberAxis yAxis = new NumberAxis();
                xAxis.setLabel("X");
                yAxis.setLabel("Y");
                //creating the chart
                final LineChart<Number, Number> lineChart = new
                        LineChart<>(xAxis, yAxis);
                //defining a series
                XYChart.Series series = new XYChart.Series();
                //populating the series with data
                List<List<Double>> result = CalculationUtils.calculate(
                        NumberUtils.parseTextAsDouble(t0Input),
                        NumberUtils.parseTextAsDouble(dtInput), NumberUtils.parseTextAsDouble(tMaxInput),
                        NumberUtils.parseTextAsDouble(x0Input), NumberUtils.parseTextAsDouble(y0Input),
                        NumberUtils.parseTextAsDouble(z0Input), NumberUtils.parseTextAsDouble(Vx0Input),
                        NumberUtils.parseTextAsDouble(Vy0Input), NumberUtils.parseTextAsDouble(Vz0Input)
                );

                List<Double> x = result.get(0);
                List<Double> y = result.get(1);
                List<Double> z = result.get(2);
                List<Double> vx = result.get(3);
                List<Double> vy = result.get(4);
                List<Double> vz = result.get(5);
                double G = 6.67 * Math.pow(10, -11);
                double M = 5.9726 * Math.pow(10, 24);
                for (int i = 0; i < x.size(); i++) {
//                        series.getData().add(new XYChart.Data(x.get(i), y.get(i)));
                    series.getData().add(new XYChart.Data(i, (Math.pow(vx.get(i), 2) + Math.pow(vy.get(i), 2) + Math.pow(vz.get(i), 2)) / 2 -
                            (G * M / (x.get(i) * x.get(i) + y.get(i) * y.get(i) + z.get(i) * z.get(i))) * Math.sqrt(x.get(i) * x.get(i) + y.get(i) * y.get(i) + z.get(i) * z.get(i))));
                }
                List resultAngles = Kepler.convertToKepler(NumberUtils.parseTextAsDouble(x0Input),
                        NumberUtils.parseTextAsDouble(y0Input), NumberUtils.parseTextAsDouble(z0Input),
                        NumberUtils.parseTextAsDouble(Vx0Input), NumberUtils.parseTextAsDouble(Vy0Input),
                        NumberUtils.parseTextAsDouble(Vz0Input));
                String spacing = "\t\t\t";
                String elements = String.join(spacing, resultAngles.toString());
                write(CalculationUtils.fileName.getName(), elements);
                series.setName("Energy");
                lineChart.getData().add(series);
                lineChart.setCreateSymbols(false);
                resultScene = new Scene(lineChart, 800, 600);
                mainWindow.setScene(resultScene);
                mainWindow.show();
            }
        });

        Button conversionToKeplerButton = new Button("Conversion");
        this.add(conversionToKeplerButton, 0, 4, 6, 1);
        setHalignment(conversionToKeplerButton, HPos.CENTER);


        conversionToKeplerButton.setOnAction(event -> {
            List result = Kepler.convertToKepler(NumberUtils.parseTextAsDouble(x0Input),
                    NumberUtils.parseTextAsDouble(y0Input), NumberUtils.parseTextAsDouble(z0Input),
                    NumberUtils.parseTextAsDouble(Vx0Input), NumberUtils.parseTextAsDouble(Vy0Input),
                    NumberUtils.parseTextAsDouble(Vz0Input));
            KeplerPane gridKepler = new KeplerPane(mainWindow);
            gridKepler.getOmegaInput().setText(String.valueOf(result.get(0)));
            gridKepler.getiInput().setText(String.valueOf(result.get(1)));
            gridKepler.getwInput().setText(String.valueOf(result.get(2)));
            gridKepler.getpInput().setText(String.valueOf(result.get(3)));
            gridKepler.geteInput().setText(String.valueOf(result.get(4)));
            gridKepler.getTauInput().setText(String.valueOf(result.get(5)));
            Scene startKeplerScene = new Scene(gridKepler, 620, 250);
            mainWindow.setScene(startKeplerScene);
            mainWindow.show();
        });
    }

    public static void write(String fileName, String text) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(text);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public TextField getVx0Input() {
        return Vx0Input;
    }

    public TextField getVy0Input() {
        return Vy0Input;
    }

    public TextField getVz0Input() {
        return Vz0Input;
    }

    public TextField getX0Input() {
        return x0Input;
    }

    public TextField getY0Input() {
        return y0Input;
    }

    public TextField getZ0Input() {
        return z0Input;
    }
}
