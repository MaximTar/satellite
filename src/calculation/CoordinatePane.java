package calculation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import utils.CalculationUtils;
import utils.NumberUtils;

import java.io.*;

import java.util.ArrayList;

public class CoordinatePane extends GridPane {

    private Scene resultScene;

    private Button startButton = new Button("Start");
    private Button conversionToKeplerButton = new Button("Conversion");

    private Label t0Label = new Label("t0:");
    private TextField t0Input = new TextField("0");
    private Label dtLabel = new Label("dt:");
    private TextField dtInput = new TextField("1000");
    private Label tMaxLabel = new Label("tMax:");
    private TextField tMaxInput = new TextField("86500");
    private Label Vx0Label = new Label("Vx0:");
    private TextField Vx0Input = new TextField("3100.0");
    private Label Vy0Label = new Label("Vy0:");
    private TextField Vy0Input = new TextField("0.0");
    private Label Vz0Label = new Label("Vz0:");
    private TextField Vz0Input = new TextField("0.0");
    private Label x0Label = new Label("x0:");
    private TextField x0Input = new TextField("0.0");
    private Label y0Label = new Label("y0:");
    private TextField y0Input = new TextField("4.2E7");
    private Label z0Label = new Label("z0:");
    private TextField z0Input = new TextField("0.0");

    public CoordinatePane(Stage mainWindow) {
        this.setPadding(new Insets(15, 15, 15, 15));
        this.setVgap(15);
        this.setHgap(8);

        this.add(t0Label, 0, 0);
        this.add(t0Input, 1, 0);
        this.add(dtLabel, 0, 1);
        this.add(dtInput, 1, 1);
        this.add(tMaxLabel, 0, 2);
        this.add(tMaxInput, 1, 2);
        this.add(Vx0Label, 3, 0);
        this.add(Vx0Input, 4, 0);
        this.add(Vy0Label, 3, 1);
        this.add(Vy0Input, 4, 1);
        this.add(Vz0Label, 3, 2);
        this.add(Vz0Input, 4, 2);
        this.add(x0Label, 5, 0);
        this.add(x0Input, 6, 0);
        this.add(y0Label, 5, 1);
        this.add(y0Input, 6, 1);
        this.add(z0Label, 5, 2);
        this.add(z0Input, 6, 2);


        this.add(startButton, 4, 4);
        setHalignment(startButton, HPos.CENTER);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (NumberUtils.isDouble(t0Input, t0Input.getText()) && NumberUtils.isDouble(dtInput, dtInput.getText())
                        && NumberUtils.isDouble(tMaxInput, tMaxInput.getText()) && NumberUtils.isDouble(Vx0Input, Vx0Input.getText())
                        && NumberUtils.isDouble(Vy0Input, Vy0Input.getText()) && NumberUtils.isDouble(Vz0Input, Vz0Input.getText())
                        && NumberUtils.isDouble(x0Input, x0Input.getText()) && NumberUtils.isDouble(y0Input, y0Input.getText())
                        && NumberUtils.isDouble(z0Input, z0Input.getText())) {
                    //defining the axes
                    final NumberAxis xAxis = new NumberAxis();
                    final NumberAxis yAxis = new NumberAxis();
                    xAxis.setLabel("X");
                    yAxis.setLabel("Y");
                    //creating the chart
//                  final LineChart<Number,Number> lineChart =
//                      new LineChart<Number,Number>(xAxis,yAxis);
                    final LineChart<Number, Number> lineChart = new
                            LineChart<>(xAxis, yAxis);
                    //defining a series
                    XYChart.Series series = new XYChart.Series();
                    //populating the series with data
                    ArrayList<ArrayList<Double>> result = CalculationUtils.calculate(
                            Double.parseDouble(t0Input.getText()),
                            Double.parseDouble(dtInput.getText()), Double.parseDouble(tMaxInput.getText()),
                            Double.parseDouble(x0Input.getText()), Double.parseDouble(y0Input.getText()),
                            Double.parseDouble(z0Input.getText()), Double.parseDouble(Vx0Input.getText()), Double.parseDouble(Vy0Input.getText()),
                            Double.parseDouble(Vz0Input.getText())
                    );

                    ArrayList<Double> x = result.get(0);
                    ArrayList<Double> y = result.get(1);
                    ArrayList<Double> z = result.get(2);
                    ArrayList<Double> vx = result.get(3);
                    ArrayList<Double> vy = result.get(4);
                    ArrayList<Double> vz = result.get(5);
                    double G = 6.67 * Math.pow(10, -11);
                    double M = 5.9726 * Math.pow(10, 24);
                    for (int i = 0; i < x.size(); i++) {
//                        series.getData().add(new XYChart.Data(x.get(i), y.get(i)));
                        series.getData().add(new XYChart.Data(i, (Math.pow(vx.get(i), 2) + Math.pow(vy.get(i), 2) + Math.pow(vz.get(i), 2)) / 2 -
                                (G * M / (x.get(i) * x.get(i) + y.get(i) * y.get(i) + z.get(i) * z.get(i))) * Math.sqrt(x.get(i) * x.get(i) + y.get(i) * y.get(i) + z.get(i) * z.get(i))));
                    }
                    ArrayList<Double> result_angles = Kepler.convertToKepler(Double.parseDouble(x0Input.getText()),
                            Double.parseDouble(y0Input.getText()), Double.parseDouble(z0Input.getText()),
                            Double.parseDouble(Vx0Input.getText()), Double.parseDouble(Vy0Input.getText()),
                            Double.parseDouble(Vz0Input.getText()));
                    String elements = String.valueOf(result_angles.get(0)) + "\t\t\t" + String.valueOf(result_angles.get(1))
                            + "\t\t\t" + String.valueOf(result_angles.get(2)) + "\t\t\t" + String.valueOf(result_angles.get(3))
                            + "\t\t\t" + String.valueOf(result_angles.get(4)) + "\t\t\t" + String.valueOf(result_angles.get(5));
                    write(CalculationUtils.fileName.getName(), elements);
                    series.setName("Energy");
                    lineChart.getData().add(series);
                    lineChart.setCreateSymbols(false);
                    resultScene = new Scene(lineChart, 800, 600);
                    mainWindow.setScene(resultScene);
                    mainWindow.show();
                }
            }
        });

        this.add(conversionToKeplerButton, 4, 5);
        setHalignment(conversionToKeplerButton, HPos.CENTER);


        conversionToKeplerButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                ArrayList<Double> result = Kepler.convertToKepler(Double.parseDouble(x0Input.getText()),
                        Double.parseDouble(y0Input.getText()), Double.parseDouble(z0Input.getText()),
                        Double.parseDouble(Vx0Input.getText()), Double.parseDouble(Vy0Input.getText()),
                        Double.parseDouble(Vz0Input.getText()));
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
            }
        });
    }

    public static void write(String fileName, String text) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(text);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public TextField getT0Input() {
        return t0Input;
    }

    public TextField getDtInput() {
        return dtInput;
    }

    public TextField gettMaxInput() {
        return tMaxInput;
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

    public Scene getResultScene() {
        return resultScene;
    }
}
