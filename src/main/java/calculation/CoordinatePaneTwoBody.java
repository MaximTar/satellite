package calculation;

/**
 * Created by Maxim Tarasov on 28.11.2016.
 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import utils.*;

import java.time.LocalDate;
import java.util.*;

public class CoordinatePaneTwoBody extends GridPane {

    private Scene resultScene;
    Group root = new Group();
    private TextField t0Input = new TextField("0");
    private TextField dtInput = new TextField("1");
    private TextField tMaxInput = new TextField("10000");
    private TextField V1x0Input = new TextField("0.0");
    private TextField V2x0Input = new TextField("0.0");
    private TextField V1y0Input = new TextField("7713.0");
    private TextField V2y0Input = new TextField("7713.0");
    private TextField V1z0Input = new TextField("0.0");
    private TextField V2z0Input = new TextField("0.0");
    private TextField x10Input = new TextField("6700000.0");
    private TextField x20Input = new TextField("6700000.0");
    private TextField y10Input = new TextField("0.0");
    private TextField y20Input = new TextField("0.0");
    private TextField z10Input = new TextField("0.0");
    private TextField z20Input = new TextField("0.0");
    private TextField J1xxInput = new TextField("10.0");
    private TextField J2xxInput = new TextField("10.0");
    private TextField J1yyInput = new TextField("100.0");
    private TextField J2yyInput = new TextField("100.0");
    private TextField J1zzInput = new TextField("100.0");
    private TextField J2zzInput = new TextField("100.0");
    private TextField q1xInput = new TextField("0.0");
    private TextField q2xInput = new TextField("0.0");
    private TextField q1yInput = new TextField("0.0");
    private TextField q2yInput = new TextField("0.0");
    private TextField q1zInput = new TextField("0.0");
    private TextField q2zInput = new TextField("0.0");
    private TextField q1wInput = new TextField("1.0");
    private TextField q2wInput = new TextField("1.0");
    private TextField w1xInput = new TextField("0.0");
    private TextField w2xInput = new TextField("0.0");
    private TextField w1yInput = new TextField("0.0");
    private TextField w2yInput = new TextField("0.0");
    private TextField w1zInput = new TextField("0.00115");
    private TextField w2zInput = new TextField("0.00115");
    private TextField tetherLengthInput = new TextField("1000");
    private TextField tetherRestitutionInput = new TextField("10000");
    CheckBox checkBoxGeoPot = new CheckBox();
    CheckBox checkBoxSunGravity = new CheckBox();
    CheckBox checkBoxMoonGravity = new CheckBox();
    CheckBox checkBoxSunPres = new CheckBox();
    CheckBox checkBoxDrag = new CheckBox();

    public CoordinatePaneTwoBody(Stage mainWindow) {
        setPadding(new Insets(15, 15, 15, 15));
        setVgap(15);
        setHgap(8);

        Label t0Label = new Label("t0:");
        Label dtLabel = new Label("   dt:");
        Label tMaxLabel = new Label("   tMax:");
        Label v1x0Label = new Label("V1x0:");
        Label v2x0Label = new Label("V2x0:");
        Label v1y0Label = new Label("V1y0:");
        Label v2y0Label = new Label("V2y0:");
        Label v1z0Label = new Label("V1z0:");
        Label v2z0Label = new Label("V2z0:");
        Label x10Label = new Label("   x10:");
        Label x20Label = new Label("   x20:");
        Label y10Label = new Label("   y10:");
        Label y20Label = new Label("   y20:");
        Label z10Label = new Label("   z10:");
        Label z20Label = new Label("   z20:");

        t0Input.setMaxWidth(100);
        dtInput.setMaxWidth(100);
        tMaxInput.setMaxWidth(100);
        V1x0Input.setMinWidth(230);
        V2x0Input.setMinWidth(230);
        V1y0Input.setMinWidth(230);
        V2y0Input.setMinWidth(230);
        V1z0Input.setMinWidth(230);
        V2z0Input.setMinWidth(230);
        x10Input.setMinWidth(230);
        y10Input.setMinWidth(230);
        z10Input.setMinWidth(230);
        x20Input.setMinWidth(230);
        y20Input.setMinWidth(230);
        z20Input.setMinWidth(230);

        HBox row0 = new HBox(5);
        row0.getChildren().addAll(t0Label, t0Input, dtLabel, dtInput, tMaxLabel, tMaxInput);
        row0.setAlignment(Pos.CENTER);
        add(row0, 0, 0, 9, 1);

        final Separator sepVert1 = new Separator();
        final Separator sepVert2 = new Separator();
        final Separator sepVert3 = new Separator();
        sepVert1.setOrientation(Orientation.VERTICAL);
        sepVert2.setOrientation(Orientation.VERTICAL);
        sepVert3.setOrientation(Orientation.VERTICAL);
        addRow(1, v1x0Label, V1x0Input, x10Label, x10Input, sepVert1, v2x0Label, V2x0Input, x20Label, x20Input);
        addRow(2, v1y0Label, V1y0Input, y10Label, y10Input, sepVert2, v2y0Label, V2y0Input, y20Label, y20Input);
        addRow(3, v1z0Label, V1z0Input, z10Label, z10Input, sepVert3, v2z0Label, V2z0Input, z20Label, z20Input);

        ToggleGroup toggleGroupInitialOrbit = new ToggleGroup();
        RadioButton buttonISS = new RadioButton("ISS");
        buttonISS.setToggleGroup(toggleGroupInitialOrbit);
        RadioButton buttonSunSync = new RadioButton("Sun-Sync");
        buttonSunSync.setToggleGroup(toggleGroupInitialOrbit);
        RadioButton buttonGPS = new RadioButton("GPS");
        buttonGPS.setToggleGroup(toggleGroupInitialOrbit);
        RadioButton buttonMolniya = new RadioButton("Molniya");
        buttonMolniya.setToggleGroup(toggleGroupInitialOrbit);
        RadioButton buttonGEO = new RadioButton("GEO");
        buttonGEO.setToggleGroup(toggleGroupInitialOrbit);
        RadioButton buttonExperiment = new RadioButton("Experimental");
        buttonExperiment.setToggleGroup(toggleGroupInitialOrbit);
        buttonExperiment.setSelected(true);
        RadioButton buttonUserDefined = new RadioButton("User Defined");
        buttonUserDefined.setToggleGroup(toggleGroupInitialOrbit);

        x10Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (x10Input.isFocused()) {
                    buttonUserDefined.setSelected(true);
                }
            }
        });
        y10Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (y10Input.isFocused()) {
                    buttonUserDefined.setSelected(true);
                }
            }
        });
        z10Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (z10Input.isFocused()) {
                    buttonUserDefined.setSelected(true);
                }
            }
        });
        V1x0Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (V1x0Input.isFocused()) {
                    buttonUserDefined.setSelected(true);
                }
            }
        });
        V1y0Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (V1y0Input.isFocused()) {
                    buttonUserDefined.setSelected(true);
                }
            }
        });
        V1z0Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (V1z0Input.isFocused()) {
                    buttonUserDefined.setSelected(true);
                }
            }
        });

        toggleGroupInitialOrbit.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (buttonISS.isSelected()) {
                    getX10Input().setText(String.valueOf(-4453783.586));
                    getY10Input().setText(String.valueOf(-5038203.756));
                    getZ10Input().setText(String.valueOf(-426384.456));
                    getV1x0Input().setText(String.valueOf(3831.888));
                    getV1y0Input().setText(String.valueOf(-2887.221));
                    getV1z0Input().setText(String.valueOf(-6018.232));
                } else if (buttonSunSync.isSelected()) {
                    getX10Input().setText(String.valueOf(-2290301.063));
                    getY10Input().setText(String.valueOf(-6379471.940));
                    getZ10Input().setText(String.valueOf(0.0));
                    getV1x0Input().setText(String.valueOf(-0883.923));
                    getV1y0Input().setText(String.valueOf(0317.338));
                    getV1z0Input().setText(String.valueOf(7610.832));
                } else if (buttonGPS.isSelected()) {
                    getX10Input().setText(String.valueOf(5525336.68));
                    getY10Input().setText(String.valueOf(-15871184.94));
                    getZ10Input().setText(String.valueOf(-20998992.446));
                    getV1x0Input().setText(String.valueOf(2750.341));
                    getV1y0Input().setText(String.valueOf(2434.198));
                    getV1z0Input().setText(String.valueOf(-1068.884));
                } else if (buttonMolniya.isSelected()) {
                    getX10Input().setText(String.valueOf(-1529894.287));
                    getY10Input().setText(String.valueOf(-2672877.357));
                    getZ10Input().setText(String.valueOf(-6150115.340));
                    getV1x0Input().setText(String.valueOf(8717.518));
                    getV1y0Input().setText(String.valueOf(-4989.709));
                    getV1z0Input().setText(String.valueOf(0.0));
                } else if (buttonGEO.isSelected()) {
                    getX10Input().setText(String.valueOf(36607358.256));
                    getY10Input().setText(String.valueOf(-20921723.703));
                    getZ10Input().setText(String.valueOf(0.0));
                    getV1x0Input().setText(String.valueOf(1525.636));
                    getV1y0Input().setText(String.valueOf(2669.451));
                    getV1z0Input().setText(String.valueOf(0.0));
                } else if (buttonExperiment.isSelected()) {
                    getX10Input().setText(String.valueOf(6700000.0));
                    getY10Input().setText(String.valueOf(0.0));
                    getZ10Input().setText(String.valueOf(0.0));
                    getV1x0Input().setText(String.valueOf(0.0));
                    getV1y0Input().setText(String.valueOf(7713.0));
                    getV1z0Input().setText(String.valueOf(0.0));
                }
            }
        });

        HBox hBoxOrbitToggle = new HBox(10);
        hBoxOrbitToggle.getChildren().addAll(buttonISS, buttonSunSync, buttonGPS, buttonMolniya, buttonGEO, buttonExperiment, buttonUserDefined);
        setColumnSpan(hBoxOrbitToggle, 4);
        add(hBoxOrbitToggle, 0, 4, 4, 1);
        hBoxOrbitToggle.setAlignment(Pos.CENTER);

        ToggleGroup toggleGroup2InitialOrbit = new ToggleGroup();
        RadioButton button2ISS = new RadioButton("ISS");
        button2ISS.setToggleGroup(toggleGroup2InitialOrbit);
        RadioButton button2SunSync = new RadioButton("Sun-Sync");
        button2SunSync.setToggleGroup(toggleGroup2InitialOrbit);
        RadioButton button2GPS = new RadioButton("GPS");
        button2GPS.setToggleGroup(toggleGroup2InitialOrbit);
        RadioButton button2Molniya = new RadioButton("Molniya");
        button2Molniya.setToggleGroup(toggleGroup2InitialOrbit);
        RadioButton button2GEO = new RadioButton("GEO");
        button2GEO.setToggleGroup(toggleGroup2InitialOrbit);
        RadioButton button2Experiment = new RadioButton("Experimental");
        button2Experiment.setToggleGroup(toggleGroup2InitialOrbit);
        button2Experiment.setSelected(true);
        RadioButton button2UserDefined = new RadioButton("User Defined");
        button2UserDefined.setToggleGroup(toggleGroup2InitialOrbit);

        x20Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (x20Input.isFocused()) {
                    button2UserDefined.setSelected(true);
                }
            }
        });
        y20Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (y20Input.isFocused()) {
                    button2UserDefined.setSelected(true);
                }
            }
        });
        z20Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (z20Input.isFocused()) {
                    button2UserDefined.setSelected(true);
                }
            }
        });
        V2x0Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (V2x0Input.isFocused()) {
                    button2UserDefined.setSelected(true);
                }
            }
        });
        V2y0Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (V2y0Input.isFocused()) {
                    button2UserDefined.setSelected(true);
                }
            }
        });
        V2z0Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (V2z0Input.isFocused()) {
                    button2UserDefined.setSelected(true);
                }
            }
        });

        toggleGroup2InitialOrbit.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (button2ISS.isSelected()) {
                    getX20Input().setText(String.valueOf(-4453783.586));
                    getY20Input().setText(String.valueOf(-5038203.756));
                    getZ20Input().setText(String.valueOf(-426384.456));
                    getV2x0Input().setText(String.valueOf(3831.888));
                    getV2y0Input().setText(String.valueOf(-2887.221));
                    getV2z0Input().setText(String.valueOf(-6018.232));
                } else if (button2SunSync.isSelected()) {
                    getX20Input().setText(String.valueOf(-2290301.063));
                    getY20Input().setText(String.valueOf(-6379471.940));
                    getZ20Input().setText(String.valueOf(0.0));
                    getV2x0Input().setText(String.valueOf(-0883.923));
                    getV2y0Input().setText(String.valueOf(0317.338));
                    getV2z0Input().setText(String.valueOf(7610.832));
                } else if (button2GPS.isSelected()) {
                    getX20Input().setText(String.valueOf(5525336.68));
                    getY20Input().setText(String.valueOf(-15871184.94));
                    getZ20Input().setText(String.valueOf(-20998992.446));
                    getV2x0Input().setText(String.valueOf(2750.341));
                    getV2y0Input().setText(String.valueOf(2434.198));
                    getV2z0Input().setText(String.valueOf(-1068.884));
                } else if (button2Molniya.isSelected()) {
                    getX20Input().setText(String.valueOf(-1529894.287));
                    getY20Input().setText(String.valueOf(-2672877.357));
                    getZ20Input().setText(String.valueOf(-6150115.340));
                    getV2x0Input().setText(String.valueOf(8717.518));
                    getV2y0Input().setText(String.valueOf(-4989.709));
                    getV2z0Input().setText(String.valueOf(0.0));
                } else if (button2GEO.isSelected()) {
                    getX20Input().setText(String.valueOf(36607358.256));
                    getY20Input().setText(String.valueOf(-20921723.703));
                    getZ20Input().setText(String.valueOf(0.0));
                    getV2x0Input().setText(String.valueOf(1525.636));
                    getV2y0Input().setText(String.valueOf(2669.451));
                    getV2z0Input().setText(String.valueOf(0.0));
                } else if (button2Experiment.isSelected()) {
                    getX20Input().setText(String.valueOf(6700000.0));
                    getY20Input().setText(String.valueOf(0.0));
                    getZ20Input().setText(String.valueOf(0.0));
                    getV2x0Input().setText(String.valueOf(0.0));
                    getV2y0Input().setText(String.valueOf(7713.0));
                    getV2z0Input().setText(String.valueOf(0.0));
                }
            }
        });

        HBox hBox2OrbitToggle = new HBox(10);
        hBox2OrbitToggle.getChildren().addAll(button2ISS, button2SunSync, button2GPS, button2Molniya, button2GEO, button2Experiment, button2UserDefined);
        setColumnSpan(hBox2OrbitToggle, 4);
        final Separator sepVert4 = new Separator();
        sepVert4.setOrientation(Orientation.VERTICAL);
        add(sepVert4, 4, 4);
        add(hBox2OrbitToggle, 5, 4, 4, 1);
        hBox2OrbitToggle.setAlignment(Pos.CENTER);

        HBox tetherBox = new HBox(10);
        Label tetherLengthLabel = new Label("Tether L:");
        Label tetherRestitutionLabel = new Label("   Tether K:");
        tetherBox.getChildren().addAll(tetherLengthLabel, tetherLengthInput, tetherRestitutionLabel, tetherRestitutionInput);
        add(tetherBox, 0, 5, 9, 1);
        tetherBox.setAlignment(Pos.CENTER);

        Button startButton = new Button("Start");
        add(startButton, 0, 6, 9, 1);
        setHalignment(startButton, HPos.CENTER);

        startButton.setOnAction(event -> {
            if (NumberUtils.textFieldsAreDouble(t0Input, dtInput, tMaxInput, V1x0Input, V1y0Input, V1z0Input,
                    x10Input, y10Input, z10Input)) {
                final NumberAxis xAxis = new NumberAxis();
                final NumberAxis yAxis = new NumberAxis();
                xAxis.setLabel("N");
                yAxis.setLabel("Energy");
                final LineChart<Number, Number> lineChart = new
                        LineChart<>(xAxis, yAxis);
                XYChart.Series series = new XYChart.Series();
                List<List<Double>> result = CalculationUtils.calculateTwoBody(
                        NumberUtils.parseTextAsDouble(t0Input), NumberUtils.parseTextAsDouble(dtInput),
                        NumberUtils.parseTextAsDouble(tMaxInput), NumberUtils.parseTextAsDouble(x10Input),
                        NumberUtils.parseTextAsDouble(y10Input), NumberUtils.parseTextAsDouble(z10Input),
                        NumberUtils.parseTextAsDouble(x20Input), NumberUtils.parseTextAsDouble(y20Input),
                        NumberUtils.parseTextAsDouble(z20Input), NumberUtils.parseTextAsDouble(V1x0Input),
                        NumberUtils.parseTextAsDouble(V1y0Input), NumberUtils.parseTextAsDouble(V1z0Input),
                        NumberUtils.parseTextAsDouble(V2x0Input), NumberUtils.parseTextAsDouble(V2y0Input),
                        NumberUtils.parseTextAsDouble(V2z0Input), NumberUtils.parseTextAsDouble(q1wInput),
                        NumberUtils.parseTextAsDouble(q1xInput), NumberUtils.parseTextAsDouble(q1yInput),
                        NumberUtils.parseTextAsDouble(q1zInput), NumberUtils.parseTextAsDouble(q2wInput),
                        NumberUtils.parseTextAsDouble(q2xInput), NumberUtils.parseTextAsDouble(q2yInput),
                        NumberUtils.parseTextAsDouble(q2zInput), NumberUtils.parseTextAsDouble(w1xInput),
                        NumberUtils.parseTextAsDouble(w1yInput), NumberUtils.parseTextAsDouble(w1zInput),
                        NumberUtils.parseTextAsDouble(w2xInput), NumberUtils.parseTextAsDouble(w2yInput),
                        NumberUtils.parseTextAsDouble(w2zInput), NumberUtils.parseTextAsDouble(J1xxInput),
                        NumberUtils.parseTextAsDouble(J1yyInput), NumberUtils.parseTextAsDouble(J1zzInput),
                        NumberUtils.parseTextAsDouble(J2xxInput), NumberUtils.parseTextAsDouble(J2yyInput),
                        NumberUtils.parseTextAsDouble(J2zzInput),
                        checkBoxGeoPot.isSelected(), checkBoxSunGravity.isSelected(), checkBoxMoonGravity.isSelected(),
                        checkBoxSunPres.isSelected(), checkBoxDrag.isSelected(),
                        NumberUtils.parseTextAsDouble(tetherRestitutionInput), NumberUtils.parseTextAsDouble(tetherLengthInput)
                );

                List<Double> x1 = result.get(0);
                List<Double> y1 = result.get(1);
                List<Double> z1 = result.get(2);
                List<Double> v1x = result.get(3);
                List<Double> v1y = result.get(4);
                List<Double> v1z = result.get(5);
                List<Double> x2 = result.get(6);
                List<Double> y2 = result.get(7);
                List<Double> z2 = result.get(8);
                List<Double> v2x = result.get(9);
                List<Double> v2y = result.get(10);
                List<Double> v2z = result.get(11);
//                double G = 6.67 * Math.pow(10, -11);
//                double M = 5.9726 * Math.pow(10, 24);
                double mu = 398600.4415E9;
                for (int i = 0; i < x1.size(); i++) {
                    double xyz1 = Math.sqrt(x1.get(i) * x1.get(i) + y1.get(i) * y1.get(i) + z1.get(i) * z1.get(i));
                    double xyz2 = Math.sqrt(x2.get(i) * x2.get(i) + y2.get(i) * y2.get(i) + z2.get(i) * z2.get(i));
                    double dx = Math.abs(xyz1 - xyz2);
                    if (dx > NumberUtils.parseTextAsDouble(tetherLengthInput)) {
                        dx = dx - NumberUtils.parseTextAsDouble(tetherLengthInput);
                    } else {
                        dx = 0;
                    }
                    series.getData().add(new XYChart.Data(i, (Math.pow(v1x.get(i), 2) + Math.pow(v1y.get(i), 2) + Math.pow(v1z.get(i), 2)) / 2 +
                            (Math.pow(v2x.get(i), 2) + Math.pow(v2y.get(i), 2) + Math.pow(v2z.get(i), 2)) / 2 -
                            (mu / xyz1) - (mu / xyz2) - (NumberUtils.parseTextAsDouble(tetherRestitutionInput) * dx * dx) / 2));
                }
                List resultAngles = Kepler.convertToKepler(NumberUtils.parseTextAsDouble(x10Input),
                        NumberUtils.parseTextAsDouble(y10Input), NumberUtils.parseTextAsDouble(z10Input),
                        NumberUtils.parseTextAsDouble(V1x0Input), NumberUtils.parseTextAsDouble(V1y0Input),
                        NumberUtils.parseTextAsDouble(V1z0Input));
                String spacing = "\t\t\t";
                String elements = String.join(spacing, resultAngles.toString());
                WritingUtils.write(CalculationUtils.fileName.getName(), elements);
                series.setName("Energy");
                lineChart.getData().add(series);
                lineChart.setCreateSymbols(false);
                resultScene = new Scene(lineChart, 1000, 600);
                mainWindow.setScene(resultScene);
                mainWindow.show();
            }
        });

//        Button conversionToKeplerButton = new Button("Conversion To Kepler");
//        add(conversionToKeplerButton, 0, 6, 6, 1);
//        setHalignment(conversionToKeplerButton, HPos.CENTER);
//
//        conversionToKeplerButton.setOnAction(event -> {
//            List result = Kepler.convertToKepler(NumberUtils.parseTextAsDouble(x10Input),
//                    NumberUtils.parseTextAsDouble(y10Input), NumberUtils.parseTextAsDouble(z10Input),
//                    NumberUtils.parseTextAsDouble(V1x0Input), NumberUtils.parseTextAsDouble(V1y0Input),
//                    NumberUtils.parseTextAsDouble(V1z0Input));
//            KeplerPane gridKepler = new KeplerPane(mainWindow);
//            gridKepler.getOmegaInput().setText(String.valueOf(result.get(0)));
//            gridKepler.getiInput().setText(String.valueOf(result.get(1)));
//            gridKepler.getwInput().setText(String.valueOf(result.get(2)));
//            gridKepler.getpInput().setText(String.valueOf(result.get(3)));
//            gridKepler.geteInput().setText(String.valueOf(result.get(4)));
//            gridKepler.getTauInput().setText(String.valueOf(result.get(5)));
//            Scene startKeplerScene = new Scene(gridKepler, 620, 250);
//            mainWindow.setScene(startKeplerScene);
//            mainWindow.show();
//        });
//
//        Label checkboxLabelPrecession = new Label("Precession:");
//        CheckBox checkBoxPrecession = new CheckBox();
//        checkboxLabelPrecession.setLabelFor(checkBoxPrecession);
//        checkboxLabelPrecession.setOnMouseClicked(e -> checkBoxPrecession.setSelected(!checkBoxPrecession.isSelected()));
//        Label checkboxLabelNutation = new Label("   Nutation:");
//        CheckBox checkBoxNutation = new CheckBox();
//        checkboxLabelNutation.setLabelFor(checkBoxNutation);
//        checkboxLabelNutation.setOnMouseClicked(e -> checkBoxNutation.setSelected(!checkBoxNutation.isSelected()));
//        Label checkboxLabelER = new Label("   Earth Rotation:");
//        CheckBox checkBoxER = new CheckBox();
//        checkboxLabelER.setLabelFor(checkBoxER);
//        checkboxLabelER.setOnMouseClicked(e -> checkBoxER.setSelected(!checkBoxER.isSelected()));
//        Label checkboxLabelPM = new Label("   Polar Motion:");
//        CheckBox checkBoxPM = new CheckBox();
//        checkboxLabelPM.setLabelFor(checkBoxPM);
//        checkboxLabelPM.setOnMouseClicked(e -> checkBoxPM.setSelected(!checkBoxPM.isSelected()));
//
//        DatePicker datePicker = new DatePicker(LocalDate.now());
//        datePicker.setMaxWidth(120);
//
//        Label hourLabel = new Label("Hours:");
//        Spinner<Integer> hourSpinner = new Spinner<>(0, 23, 0);
//        hourSpinner.setMaxWidth(50);
//        Label minuteLabel = new Label("Minutes:");
//        Spinner<Integer> minuteSpinner = new Spinner<>(0, 59, 0);
//        minuteSpinner.setMaxWidth(50);
//
//        Button conversionToECEFButton = new Button("Conversion To ECEF");
//
//        HBox hBoxECEFECI = new HBox(3);
//        hBoxECEFECI.getChildren().addAll(checkboxLabelPrecession, checkBoxPrecession, checkboxLabelNutation, checkBoxNutation, checkboxLabelER,
//                checkBoxER, checkboxLabelPM, checkBoxPM);
//        setColumnSpan(hBoxECEFECI, 6);
//        addRow(7, hBoxECEFECI);
//        hBoxECEFECI.setAlignment(Pos.CENTER);
//
//        HBox hBoxECEFECI2 = new HBox(10);
//        hBoxECEFECI2.getChildren().addAll(datePicker, hourLabel, hourSpinner, minuteLabel, minuteSpinner, conversionToECEFButton);
//        setColumnSpan(hBoxECEFECI2, 6);
//        addRow(8, hBoxECEFECI2);
//        hBoxECEFECI2.setAlignment(Pos.CENTER);
//
//        conversionToECEFButton.setOnAction(event -> {
//
//            Calendar c = new GregorianCalendar(datePicker.getValue().getYear(), datePicker.getValue().getMonth().getValue(),
//                    datePicker.getValue().getDayOfMonth() - 1, hourSpinner.getValue(), minuteSpinner.getValue());
//
//            ArrayList<ArrayList<Double>> precession = Matrix.identityMatrix(3);
//            ArrayList<ArrayList<Double>> nutation = Matrix.identityMatrix(3);
//            ArrayList<ArrayList<Double>> earthRotation = Matrix.identityMatrix(3);
//            ArrayList<ArrayList<Double>> pole = Matrix.identityMatrix(3);
//            if (checkBoxPrecession.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECEF")) {
//                precession = EcefEci.precessionMatrix(c);
//            } else if (checkBoxPrecession.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECI")) {
//                precession = Matrix.transpose(EcefEci.precessionMatrix(c));
//            }
//            if (checkBoxNutation.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECEF")) {
//                nutation = EcefEci.nutationMatrix(c);
//            } else if (checkBoxNutation.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECI")) {
//                nutation = Matrix.transpose(EcefEci.nutationMatrix(c));
//            }
//            if (checkBoxER.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECEF")) {
//                earthRotation = EcefEci.rotationMatrix(c);
//            } else if (checkBoxER.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECI")) {
//                earthRotation = Matrix.transpose(EcefEci.rotationMatrix(c));
//            }
//
//            if (checkBoxPM.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECEF")) {
//                pole = EcefEci.poleMatrix(c);
//            } else if (checkBoxPM.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECI")) {
//                pole = Matrix.transpose(EcefEci.poleMatrix(c));
//            }
//
//            ArrayList<Double> initialCoordinates = new ArrayList<>();
//            Collections.addAll(initialCoordinates, NumberUtils.parseTextAsDouble(x10Input),
//                    NumberUtils.parseTextAsDouble(y10Input), NumberUtils.parseTextAsDouble(z10Input));
//            ArrayList<Double> initialVelocities = new ArrayList<>();
//            Collections.addAll(initialVelocities, NumberUtils.parseTextAsDouble(V1x0Input),
//                    NumberUtils.parseTextAsDouble(V1y0Input), NumberUtils.parseTextAsDouble(V1z0Input));
//            ArrayList<ArrayList<Double>> newCoordinates = new ArrayList<>();
//            ArrayList<ArrayList<Double>> newVelocities = new ArrayList<>();
//
//            if (checkBoxPM.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECEF")) {
//                ArrayList<ArrayList<Double>> help = new ArrayList<>();
//                help.add(initialCoordinates);
//                newCoordinates = Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(pole, earthRotation), nutation), precession), Matrix.transpose(help));
//                help.clear();
//                help.add(initialVelocities);
//                newVelocities = Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(pole, earthRotation), nutation), precession), Matrix.transpose(help));
//            } else if (checkBoxPM.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECI")) {
//                ArrayList<ArrayList<Double>> help = new ArrayList<>();
//                help.add(initialCoordinates);
//                newCoordinates = Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(precession, nutation), earthRotation), pole), Matrix.transpose(help));
//                help.clear();
//                help.add(initialVelocities);
//                newVelocities = Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(precession, nutation), earthRotation), pole), Matrix.transpose(help));
//            }
//
//            getV1x0Input().setText(String.valueOf(newVelocities.get(0).get(0)));
//            getV1y0Input().setText(String.valueOf(newVelocities.get(1).get(0)));
//            getV1z0Input().setText(String.valueOf(newVelocities.get(2).get(0)));
//            getX10Input().setText(String.valueOf(newCoordinates.get(0).get(0)));
//            getY10Input().setText(String.valueOf(newCoordinates.get(1).get(0)));
//            getZ10Input().setText(String.valueOf(newCoordinates.get(2).get(0)));
//
//            if (Objects.equals(conversionToECEFButton.getText(), "Conversion To ECEF")) {
//                conversionToECEFButton.setText("Conversion To ECI");
//            } else if (Objects.equals(conversionToECEFButton.getText(), "Conversion To ECI")) {
//                conversionToECEFButton.setText("Conversion To ECEF");
//            }
//
//        });

        HBox hBoxFirstBody1 = new HBox(5);
        HBox hBoxSecondBody1 = new HBox(5);
        Label j1xxLabel = new Label("J1xx:");
        Label j2xxLabel = new Label("J2xx:");
        Label j1yyLabel = new Label("   J1yy:");
        Label j2yyLabel = new Label("   J2yy:");
        Label j1zzLabel = new Label("   J1zz:");
        Label j2zzLabel = new Label("   J2zz:");
        J1xxInput.setMaxWidth(100);
        J2xxInput.setMaxWidth(100);
        J1yyInput.setMaxWidth(100);
        J2yyInput.setMaxWidth(100);
        J1zzInput.setMaxWidth(100);
        J2zzInput.setMaxWidth(100);
        hBoxFirstBody1.getChildren().addAll(j1xxLabel, J1xxInput, j1yyLabel, J1yyInput, j1zzLabel, J1zzInput);
        hBoxSecondBody1.getChildren().addAll(j2xxLabel, J2xxInput, j2yyLabel, J2yyInput, j2zzLabel, J2zzInput);
        add(hBoxFirstBody1, 0, 7, 4, 1);
        final Separator sepVert5 = new Separator();
        sepVert5.setOrientation(Orientation.VERTICAL);
        add(sepVert5, 4, 7);
        add(hBoxSecondBody1, 5, 7, 4, 1);
        hBoxFirstBody1.setAlignment(Pos.CENTER);
        hBoxSecondBody1.setAlignment(Pos.CENTER);

        HBox hBoxFirstBody2 = new HBox(5);
        HBox hBoxSecondBody2 = new HBox(5);
        Label q1wLabel = new Label("q1w:");
        Label q2wLabel = new Label("q2w:");
        Label q1xLabel = new Label("   q1x:");
        Label q2xLabel = new Label("   q2x:");
        Label q1yLabel = new Label("   q1y:");
        Label q2yLabel = new Label("   q2y:");
        Label q1zLabel = new Label("   q1z:");
        Label q2zLabel = new Label("   q2z:");
        q1wInput.setMaxWidth(90);
        q1xInput.setMaxWidth(90);
        q1yInput.setMaxWidth(90);
        q1zInput.setMaxWidth(90);
        q2wInput.setMaxWidth(90);
        q2xInput.setMaxWidth(90);
        q2yInput.setMaxWidth(90);
        q2zInput.setMaxWidth(90);
        hBoxFirstBody2.getChildren().addAll(q1wLabel, q1wInput, q1xLabel, q1xInput, q1yLabel, q1yInput, q1zLabel, q1zInput);
        hBoxSecondBody2.getChildren().addAll(q2wLabel, q2wInput, q2xLabel, q2xInput, q2yLabel, q2yInput, q2zLabel, q2zInput);
        add(hBoxFirstBody2, 0, 8, 4, 1);
        final Separator sepVert6 = new Separator();
        sepVert6.setOrientation(Orientation.VERTICAL);
        add(sepVert6, 4, 8);
        add(hBoxSecondBody2, 5, 8, 4, 1);
        hBoxFirstBody2.setAlignment(Pos.CENTER);
        hBoxSecondBody2.setAlignment(Pos.CENTER);

        HBox hBoxFirstBody3 = new HBox(5);
        HBox hBoxSecondBody3 = new HBox(5);
        Label w1xLabel = new Label("w1x:");
        Label w2xLabel = new Label("w2x:");
        Label w1yLabel = new Label("   w1y:");
        Label w2yLabel = new Label("   w2y:");
        Label w1zLabel = new Label("   w1z:");
        Label w2zLabel = new Label("   w2z:");
        w1xInput.setMaxWidth(130);
        w1yInput.setMaxWidth(130);
        w1zInput.setMaxWidth(130);
        w2xInput.setMaxWidth(130);
        w2yInput.setMaxWidth(130);
        w2zInput.setMaxWidth(130);
        hBoxFirstBody3.getChildren().addAll(w1xLabel, w1xInput, w1yLabel, w1yInput, w1zLabel, w1zInput);
        hBoxSecondBody3.getChildren().addAll(w2xLabel, w2xInput, w2yLabel, w2yInput, w2zLabel, w2zInput);
        add(hBoxFirstBody3, 0, 9, 4, 1);
        final Separator sepVert7 = new Separator();
        sepVert7.setOrientation(Orientation.VERTICAL);
        add(sepVert7, 4, 9);
        add(hBoxSecondBody3, 5, 9, 4, 1);
        hBoxFirstBody3.setAlignment(Pos.CENTER);
        hBoxSecondBody3.setAlignment(Pos.CENTER);

        Label checkboxLabelGeoPot = new Label("GeoPotential:");
        checkboxLabelGeoPot.setLabelFor(checkBoxGeoPot);
        checkboxLabelGeoPot.setOnMouseClicked(e -> checkBoxGeoPot.setSelected(!checkBoxGeoPot.isSelected()));
        Label checkboxLabelSunGravity = new Label("   Sun's gravity:");
        checkboxLabelSunGravity.setLabelFor(checkBoxSunGravity);
        checkboxLabelSunGravity.setOnMouseClicked(e -> checkBoxSunGravity.setSelected(!checkBoxSunGravity.isSelected()));
        Label checkboxLabelMoonGravity = new Label("   Moon's Gravity:");
        checkboxLabelMoonGravity.setLabelFor(checkBoxMoonGravity);
        checkboxLabelMoonGravity.setOnMouseClicked(e -> checkBoxMoonGravity.setSelected(!checkBoxMoonGravity.isSelected()));
        Label checkboxLabelSunPres = new Label("   Sun's radiation pressure:");
        checkboxLabelSunPres.setLabelFor(checkBoxSunPres);
        checkboxLabelSunPres.setOnMouseClicked(e -> checkBoxSunPres.setSelected(!checkBoxSunPres.isSelected()));
        Label checkboxLabelDrag = new Label("   Atmospheric Drag:");
        checkboxLabelDrag.setLabelFor(checkBoxDrag);
        checkboxLabelDrag.setOnMouseClicked(e -> checkBoxDrag.setSelected(!checkBoxDrag.isSelected()));
        HBox hBoxDisturbances = new HBox(10);
        hBoxDisturbances.getChildren().addAll(checkboxLabelGeoPot, checkBoxGeoPot,
                checkboxLabelSunGravity, checkBoxSunGravity, checkboxLabelMoonGravity, checkBoxMoonGravity,
                checkboxLabelSunPres, checkBoxSunPres, checkboxLabelDrag, checkBoxDrag);
        setColumnSpan(hBoxDisturbances, 9);
        addRow(10, hBoxDisturbances);
        hBoxDisturbances.setAlignment(Pos.CENTER);
    }

    public TextField getV1x0Input() {
        return V1x0Input;
    }

    public TextField getV1y0Input() {
        return V1y0Input;
    }

    public TextField getV1z0Input() {
        return V1z0Input;
    }

    public TextField getX10Input() {
        return x10Input;
    }

    public TextField getY10Input() {
        return y10Input;
    }

    public TextField getZ10Input() {
        return z10Input;
    }

    public TextField getV2x0Input() {
        return V2x0Input;
    }

    public TextField getV2y0Input() {
        return V2y0Input;
    }

    public TextField getV2z0Input() {
        return V2z0Input;
    }

    public TextField getX20Input() {
        return x20Input;
    }

    public TextField getY20Input() {
        return y20Input;
    }

    public TextField getZ20Input() {
        return z20Input;
    }
}
