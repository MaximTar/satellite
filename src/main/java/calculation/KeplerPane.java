// TODO СДЕЛАТЬ ОТДЕЛЬНЫЙ ВИДЖЕТ ДЛЯ КОНВЕРТАЦИИ, А НЕ ДЛЯ СЧЕТА

//package com.github.qcha.calculation;
//
//import com.github.qcha.utils.MainForcesAbsolute;
//import javafx.geometry.HPos;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.chart.LineChart;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.XYChart;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.GridPane;
//import javafx.stage.Stage;
//import com.github.qcha.utils.NumberUtils;
//
//import java.util.List;
//
//public class KeplerPane extends GridPane {
//
//    private Scene resultScene;
//
//    private TextField Kt0Input = new TextField("0");
//    private TextField KdtInput = new TextField("1000");
//    private TextField KtMaxInput = new TextField("86500");
//    private TextField OmegaInput = new TextField("90.0");
//    private TextField iInput = new TextField("180.0");
//    private TextField wInput = new TextField("0.0");
//    private TextField pInput = new TextField("4.2E7");
//    private TextField eInput = new TextField("0.0");
//    private TextField tauInput = new TextField("0.0");
//
//    public KeplerPane(Stage mainWindow) {
//        setPadding(new Insets(15, 15, 15, 15));
//        setVgap(15);
//        setHgap(8);
//
//        Label kt0Label = new Label("t0:");
//        Label kdtLabel = new Label("dt:");
//        Label ktMaxLabel = new Label("tMax:");
//        Label omegaLabel = new Label("Omega:");
//        Label iLabel = new Label("qw:");
//        Label wLabel = new Label("w:");
//        Label pLabel = new Label("p:");
//        Label eLabel = new Label("e:");
//        Label tauLabel = new Label("tau:");
//
//        addRow(0, kt0Label, Kt0Input, omegaLabel, OmegaInput, pLabel, pInput);
//        addRow(1, kdtLabel, KdtInput, iLabel, iInput, eLabel, eInput);
//        addRow(2, ktMaxLabel, KtMaxInput, wLabel, wInput, tauLabel, tauInput);
//
//        Button startButton = new Button("Start");
//        add(startButton, 0, 3, 6, 1);
//        setHalignment(startButton, HPos.CENTER);
//
//        startButton.setOnAction(event -> {
//            if (NumberUtils.textFieldsAreDouble(Kt0Input, KdtInput, KtMaxInput, OmegaInput, iInput, wInput,
//                    pInput, eInput, tauInput)) {
//                //defining the axes
//                final NumberAxis xAxis = new NumberAxis();
//                final NumberAxis yAxis = new NumberAxis();
//                xAxis.setLabel("X");
//                yAxis.setLabel("Y");
//                //creating the chart
//                final LineChart<Number, Number> lineChart = new
//                        LineChart<>(xAxis, yAxis);
//                //defining area series
//                XYChart.Series<Number, Number> series = new XYChart.Series<>();
//                //populating the series with data
//                List<Double> subResult = Kepler.convertToCoordinate(NumberUtils.parseTextAsDouble(OmegaInput),
//                        NumberUtils.parseTextAsDouble(iInput), NumberUtils.parseTextAsDouble(wInput),
//                        NumberUtils.parseTextAsDouble(pInput), NumberUtils.parseTextAsDouble(eInput),
//                        NumberUtils.parseTextAsDouble(tauInput));
//                List<List<Double>> result = MainForcesAbsolute.calculateOneBody(
//                        NumberUtils.parseTextAsDouble(Kt0Input), NumberUtils.parseTextAsDouble(KdtInput),
//                        NumberUtils.parseTextAsDouble(KtMaxInput), subResult.get(0), subResult.get(1),
//                        subResult.get(2), subResult.get(3), subResult.get(4), subResult.get(5),
//                        1, 0, 0, 0, 0, 0, 0, 10, 100, 100, true, true, true, true, true
//                );
//
//                List<Double> x = result.get(0);
//                List<Double> y = result.get(1);
//                List<Double> z = result.get(2);
//                List<Double> vx = result.get(3);
//                List<Double> vy = result.get(4);
//                List<Double> vz = result.get(5);
//                double G = 6.67 * Math.pow(10, -11);
//                double M = 5.9726 * Math.pow(10, 24);
//                for (int i = 0; i < x.size(); i++) {
////                        series.getData().add(new XYChart.Data(x.get(qw), y.get(qw)));
//                    series.getData().add(new XYChart.Data<>(i, (Math.pow(vx.get(i), 2) + Math.pow(vy.get(i), 2) + Math.pow(vz.get(i), 2)) / 2 -
//                            (G * M / (x.get(i) * x.get(i) + y.get(i) * y.get(i) + z.get(i) * z.get(i))) * Math.sqrt(x.get(i) * x.get(i) + y.get(i) * y.get(i) + z.get(i) * z.get(i))));
//                }
//                series.setName("Energy");
//                lineChart.getData().add(series);
//                lineChart.setCreateSymbols(false);
//                resultScene = new Scene(lineChart, 800, 600);
//                mainWindow.setScene(resultScene);
//                mainWindow.show();
//            }
//        });
//
//        Button conversionToCoordinatesButton = new Button("Conversion To Descartes");
//        add(conversionToCoordinatesButton, 0, 4, 6, 1);
//        setHalignment(conversionToCoordinatesButton, HPos.CENTER);
//
//
//        conversionToCoordinatesButton.setOnAction(event -> {
//            List result = Kepler.convertToCoordinate(NumberUtils.parseTextAsDouble(OmegaInput),
//                    NumberUtils.parseTextAsDouble(iInput), NumberUtils.parseTextAsDouble(wInput),
//                    NumberUtils.parseTextAsDouble(pInput), NumberUtils.parseTextAsDouble(eInput),
//                    NumberUtils.parseTextAsDouble(tauInput));
//            CoordinatePaneOneBody gridCoordinate = new CoordinatePaneOneBody(mainWindow);
//            gridCoordinate.getX0Input().setText(String.valueOf(result.get(0)));
//            gridCoordinate.getY0Input().setText(String.valueOf(result.get(1)));
//            gridCoordinate.getZ0Input().setText(String.valueOf(result.get(2)));
//            gridCoordinate.getVx0Input().setText(String.valueOf(result.get(3)));
//            gridCoordinate.getVy0Input().setText(String.valueOf(result.get(4)));
//            gridCoordinate.getVz0Input().setText(String.valueOf(result.get(5)));
//            Scene startCoordinateScene = new Scene(gridCoordinate, 600, 300);
//            mainWindow.setScene(startCoordinateScene);
//            mainWindow.show();
//        });
//    }
//
//    public TextField getOmegaInput() {
//        return OmegaInput;
//    }
//
//    public TextField getiInput() {
//        return iInput;
//    }
//
//    public TextField getwInput() {
//        return wInput;
//    }
//
//    public TextField getpInput() {
//        return pInput;
//    }
//
//    public TextField geteInput() {
//        return eInput;
//    }
//
//    public TextField getTauInput() {
//        return tauInput;
//    }
//
//}
