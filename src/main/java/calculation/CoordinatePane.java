package main.java.calculation;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.java.utils.*;

import java.io.*;

import java.time.LocalDate;
import java.util.*;

public class CoordinatePane extends GridPane {

    private Scene resultScene;

//    private TextField t0Input = new TextField("0");
//    private TextField dtInput = new TextField("1000");
//    private TextField tMaxInput = new TextField("86500");
//    private TextField Vx0Input = new TextField("3100.0");
//    private TextField Vy0Input = new TextField("0.0");
//    private TextField Vz0Input = new TextField("0.0");
//    private TextField x0Input = new TextField("0.0");
//    private TextField y0Input = new TextField("4.2E7");
//    private TextField z0Input = new TextField("0.0");
    private TextField t0Input = new TextField("0");
    private TextField dtInput = new TextField("1000");
    private TextField tMaxInput = new TextField("86500");
    private TextField Vx0Input = new TextField("-883.923");
    private TextField Vy0Input = new TextField("317.338");
    private TextField Vz0Input = new TextField("7610.832");
    private TextField x0Input = new TextField("-2290301.063");
    private TextField y0Input = new TextField("-6379471.940");
    private TextField z0Input = new TextField("0.0");

    public CoordinatePane(Stage mainWindow) {
        setPadding(new Insets(15, 15, 15, 15));
        setVgap(15);
        setHgap(8);

        Label t0Label = new Label("t0:");
        Label dtLabel = new Label("dt:");
        Label tMaxLabel = new Label("tMax:");
        Label vx0Label = new Label("Vx0:");
        Label vy0Label = new Label("Vy0:");
        Label vz0Label = new Label("Vz0:");
        Label x0Label = new Label("x0:");
        Label y0Label = new Label("y0:");
        Label z0Label = new Label("z0:");

        addRow(0, t0Label, t0Input, vx0Label, Vx0Input, x0Label, x0Input);
        addRow(1, dtLabel, dtInput, vy0Label, Vy0Input, y0Label, y0Input);
        addRow(2, tMaxLabel, tMaxInput, vz0Label, Vz0Input, z0Label, z0Input);

        Button startButton = new Button("Start");
        add(startButton, 0, 3, 6, 1);
        setHalignment(startButton, HPos.CENTER);

        startButton.setOnAction(event -> {
            if (NumberUtils.textFieldsAreDouble(t0Input, dtInput, tMaxInput, Vx0Input, Vy0Input, Vz0Input,
                    x0Input, y0Input, z0Input)) {
                final NumberAxis xAxis = new NumberAxis();
                final NumberAxis yAxis = new NumberAxis();
//                final NumberAxis yAxis = new NumberAxis(-29475000, -29365000, 5000);
                xAxis.setLabel("N");
                yAxis.setLabel("Energy");
                final LineChart<Number, Number> lineChart = new
                        LineChart<>(xAxis, yAxis);
                XYChart.Series series = new XYChart.Series();
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
                double mu = 398600.4415E9;
                for (int i = 0; i < x.size(); i++) {
//                        series.getData().add(new XYChart.Data(x.get(i), y.get(i)));
                    series.getData().add(new XYChart.Data(i, (Math.pow(vx.get(i), 2) + Math.pow(vy.get(i), 2) + Math.pow(vz.get(i), 2)) / 2 -
                            (mu / (x.get(i) * x.get(i) + y.get(i) * y.get(i) + z.get(i) * z.get(i))) * Math.sqrt(x.get(i) * x.get(i) + y.get(i) * y.get(i) + z.get(i) * z.get(i))));
//                    series.getData().add(new XYChart.Data(i, Math.sqrt(vx.get(i) * vx.get(i) + vy.get(i) * vy.get(i) + vz.get(i) * vz.get(i))));
                }
                List resultAngles = Kepler.convertToKepler(NumberUtils.parseTextAsDouble(x0Input),
                        NumberUtils.parseTextAsDouble(y0Input), NumberUtils.parseTextAsDouble(z0Input),
                        NumberUtils.parseTextAsDouble(Vx0Input), NumberUtils.parseTextAsDouble(Vy0Input),
                        NumberUtils.parseTextAsDouble(Vz0Input));
                String spacing = "\t\t\t";
                String elements = String.join(spacing, resultAngles.toString());
                WritingUtils.write(CalculationUtils.fileName.getName(), elements);
                series.setName("Energy");
                lineChart.getData().add(series);
                lineChart.setCreateSymbols(false);
                resultScene = new Scene(lineChart, 1600, 600);
                mainWindow.setScene(resultScene);
                mainWindow.show();
            }
        });

        Button conversionToKeplerButton = new Button("Conversion To Kepler");
        add(conversionToKeplerButton, 0, 4, 6, 1);
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

        Label checkboxLabelPrecession = new Label("Precession:");
        CheckBox checkBoxPrecession = new CheckBox();
        checkboxLabelPrecession.setLabelFor(checkBoxPrecession);
        checkboxLabelPrecession.setOnMouseClicked(e -> checkBoxPrecession.setSelected(!checkBoxPrecession.isSelected()));
        Label checkboxLabelNutation = new Label("   Nutation:");
        CheckBox checkBoxNutation = new CheckBox();
        checkboxLabelNutation.setLabelFor(checkBoxNutation);
        checkboxLabelNutation.setOnMouseClicked(e -> checkBoxNutation.setSelected(!checkBoxNutation.isSelected()));
        Label checkboxLabelER = new Label("   Earth Rotation:");
        CheckBox checkBoxER = new CheckBox();
        checkboxLabelER.setLabelFor(checkBoxER);
        checkboxLabelER.setOnMouseClicked(e -> checkBoxER.setSelected(!checkBoxER.isSelected()));
        Label checkboxLabelPM = new Label("   Polar Motion:");
        CheckBox checkBoxPM = new CheckBox();
        checkboxLabelPM.setLabelFor(checkBoxPM);
        checkboxLabelPM.setOnMouseClicked(e -> checkBoxPM.setSelected(!checkBoxPM.isSelected()));

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setMaxWidth(120);

        Label hourLabel = new Label("Hours:");
        Spinner<Integer> hourSpinner = new Spinner<>(0, 23, 0);
        hourSpinner.setMaxWidth(50);
//        hourSpinner.setEditable(true);
        Label minuteLabel = new Label("Minutes:");
        Spinner<Integer> minuteSpinner = new Spinner<>(0, 59, 0);
        minuteSpinner.setMaxWidth(50);
//        minuteSpinner.setEditable(true);

        Button conversionToECIButton = new Button("Conversion To ECI");

        HBox hBox = new HBox(3);
        hBox.getChildren().addAll(checkboxLabelPrecession, checkBoxPrecession, checkboxLabelNutation, checkBoxNutation, checkboxLabelER,
                checkBoxER, checkboxLabelPM, checkBoxPM);
        setColumnSpan(hBox, 6);
        addRow(5, hBox);
        hBox.setAlignment(Pos.CENTER);

        HBox hBox6row = new HBox(10);
        hBox6row.getChildren().addAll(datePicker, hourLabel, hourSpinner, minuteLabel, minuteSpinner, conversionToECIButton);
        setColumnSpan(hBox6row, 6);
        addRow(6, hBox6row);
        hBox6row.setAlignment(Pos.CENTER);

        conversionToECIButton.setOnAction(event -> {

            Calendar c = new GregorianCalendar(datePicker.getValue().getYear(), datePicker.getValue().getMonth().getValue(),
                    datePicker.getValue().getDayOfMonth() - 1, hourSpinner.getValue(), minuteSpinner.getValue());

            ArrayList<ArrayList<Double>> precession = Matrix.identityMatrix(3);
            ArrayList<ArrayList<Double>> nutation = Matrix.identityMatrix(3);
            ArrayList<ArrayList<Double>> earthRotation = Matrix.identityMatrix(3);
            ArrayList<ArrayList<Double>> pole = Matrix.identityMatrix(3);
            if (checkBoxPrecession.isSelected() && Objects.equals(conversionToECIButton.getText(), "Conversion To ECI")){
                precession = Matrix.transpose(EcefEci.precessionMatrix(c));
            } else if (checkBoxPrecession.isSelected() && Objects.equals(conversionToECIButton.getText(), "Conversion To ECEF")) {
                precession = EcefEci.precessionMatrix(c);
            }
            if (checkBoxNutation.isSelected() && Objects.equals(conversionToECIButton.getText(), "Conversion To ECI")) {
                nutation = Matrix.transpose(EcefEci.nutationMatrix(c));
            } else if (checkBoxNutation.isSelected() && Objects.equals(conversionToECIButton.getText(), "Conversion To ECEF")) {
                nutation = EcefEci.nutationMatrix(c);
            }
            if (checkBoxER.isSelected() && Objects.equals(conversionToECIButton.getText(), "Conversion To ECI")) {
                earthRotation = Matrix.transpose(EcefEci.rotationMatrix(c));
            } else if (checkBoxER.isSelected() && Objects.equals(conversionToECIButton.getText(), "Conversion To ECEF")) {
                earthRotation = EcefEci.rotationMatrix(c);
            }

            System.out.println(earthRotation);

            if (checkBoxPM.isSelected() && Objects.equals(conversionToECIButton.getText(), "Conversion To ECI")) {
                pole = Matrix.transpose(EcefEci.poleMatrix(c));
            } else if (checkBoxPM.isSelected() && Objects.equals(conversionToECIButton.getText(), "Conversion To ECEF")) {
                pole = EcefEci.poleMatrix(c);
            }

            ArrayList<Double> initialCoordinates = new ArrayList<>();
            Collections.addAll(initialCoordinates, NumberUtils.parseTextAsDouble(x0Input),
                    NumberUtils.parseTextAsDouble(y0Input), NumberUtils.parseTextAsDouble(z0Input));
            ArrayList<Double> initialVelocities = new ArrayList<>();
            Collections.addAll(initialVelocities, NumberUtils.parseTextAsDouble(Vx0Input),
                    NumberUtils.parseTextAsDouble(Vy0Input), NumberUtils.parseTextAsDouble(Vz0Input));
            ArrayList<ArrayList<Double>> newCoordinates = new ArrayList<>();
            ArrayList<ArrayList<Double>> newVelocities = new ArrayList<>();

            ArrayList<ArrayList<Double>> help = new ArrayList<>();
            help.add(initialCoordinates);
            newCoordinates = Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(pole, earthRotation), nutation), precession), Matrix.transpose(help));
            help.clear();
            help.add(initialVelocities);
            newVelocities = Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(pole, earthRotation), nutation), precession), Matrix.transpose(help));

            getVx0Input().setText(String.valueOf(newVelocities.get(0).get(0)));
            getVy0Input().setText(String.valueOf(newVelocities.get(1).get(0)));
            getVz0Input().setText(String.valueOf(newVelocities.get(2).get(0)));
            getX0Input().setText(String.valueOf(newCoordinates.get(0).get(0)));
            getY0Input().setText(String.valueOf(newCoordinates.get(1).get(0)));
            getZ0Input().setText(String.valueOf(newCoordinates.get(2).get(0)));

            if (Objects.equals(conversionToECIButton.getText(), "Conversion To ECI")) {
                conversionToECIButton.setText("Conversion To ECEF");
            } else if (Objects.equals(conversionToECIButton.getText(), "Conversion To ECEF")) {
                conversionToECIButton.setText("Conversion To ECI");
            }

        });
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
