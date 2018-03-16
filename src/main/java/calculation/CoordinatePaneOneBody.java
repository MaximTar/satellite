package calculation;
/**
 * Created by Maxim Tarasov on 28.11.2016.
 *
 */

import model.Satellite;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import utils.*;
import java.time.LocalDate;
import java.util.*;

public class CoordinatePaneOneBody extends GridPane {

    private Scene resultScene;
//    Group root = new Group();

    private TextField t0Input = new TextField("0");
    //    private TextField dtInput = new TextField("1000");
    private TextField dtInput = new TextField("1");
    //    private TextField tMaxInput = new TextField("86500");
    private TextField tMaxInput = new TextField("10000");
    //    private TextField Vx0Input = new TextField("-883.923");
    private TextField Vx0Input = new TextField("0.0");
    //    private TextField Vy0Input = new TextField("317.338");
    private TextField Vy0Input = new TextField("7713.0");
    //    private TextField Vz0Input = new TextField("7610.832");
    private TextField Vz0Input = new TextField("0.0");
    //    private TextField x0Input = new TextField("-2290301.063");
    private TextField x0Input = new TextField("6700000.0");
    //    private TextField y0Input = new TextField("-6379471.940");
    private TextField y0Input = new TextField("0.0");
    private TextField z0Input = new TextField("0.0");
    private TextField JxxInput = new TextField("10.0");
    private TextField JyyInput = new TextField("100.0");
    private TextField JzzInput = new TextField("100.0");
    private TextField qxInput = new TextField("0.0");
    private TextField qyInput = new TextField("0.0");
    private TextField qzInput = new TextField("0.0");
    private TextField qwInput = new TextField("1.0");
    private TextField wxInput = new TextField("0.0");
    private TextField wyInput = new TextField("0.0");
    private TextField wzInput = new TextField("0.00115");
    CheckBox checkBoxGeoPot = new CheckBox();
    CheckBox checkBoxSunGravity = new CheckBox();
    CheckBox checkBoxMoonGravity = new CheckBox();
    CheckBox checkBoxSunPres = new CheckBox();
    CheckBox checkBoxDrag = new CheckBox();

    public CoordinatePaneOneBody(Stage mainWindow) {
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

        t0Input.setMinWidth(200);
        dtInput.setMinWidth(200);
        tMaxInput.setMinWidth(200);
        Vx0Input.setMinWidth(200);
        Vy0Input.setMinWidth(200);
        Vz0Input.setMinWidth(200);

        addRow(0, t0Label, t0Input, vx0Label, Vx0Input, x0Label, x0Input);
        addRow(1, dtLabel, dtInput, vy0Label, Vy0Input, y0Label, y0Input);
        addRow(2, tMaxLabel, tMaxInput, vz0Label, Vz0Input, z0Label, z0Input);

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

        x0Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (x0Input.isFocused()) {
                    buttonUserDefined.setSelected(true);
                }
            }
        });
        y0Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (y0Input.isFocused()) {
                    buttonUserDefined.setSelected(true);
                }
            }
        });
        z0Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (z0Input.isFocused()) {
                    buttonUserDefined.setSelected(true);
                }
            }
        });
        Vx0Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (Vx0Input.isFocused()) {
                    buttonUserDefined.setSelected(true);
                }
            }
        });
        Vy0Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (Vy0Input.isFocused()) {
                    buttonUserDefined.setSelected(true);
                }
            }
        });
        Vz0Input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (Vz0Input.isFocused()) {
                    buttonUserDefined.setSelected(true);
                }
            }
        });

        toggleGroupInitialOrbit.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (buttonISS.isSelected()) {
                    getX0Input().setText(String.valueOf(-4453783.586));
                    getY0Input().setText(String.valueOf(-5038203.756));
                    getZ0Input().setText(String.valueOf(-426384.456));
                    getVx0Input().setText(String.valueOf(3831.888));
                    getVy0Input().setText(String.valueOf(-2887.221));
                    getVz0Input().setText(String.valueOf(-6018.232));
                } else if (buttonSunSync.isSelected()) {
                    getX0Input().setText(String.valueOf(-2290301.063));
                    getY0Input().setText(String.valueOf(-6379471.940));
                    getZ0Input().setText(String.valueOf(0.0));
                    getVx0Input().setText(String.valueOf(-0883.923));
                    getVy0Input().setText(String.valueOf(0317.338));
                    getVz0Input().setText(String.valueOf(7610.832));
                } else if (buttonGPS.isSelected()) {
                    getX0Input().setText(String.valueOf(5525336.68));
                    getY0Input().setText(String.valueOf(-15871184.94));
                    getZ0Input().setText(String.valueOf(-20998992.446));
                    getVx0Input().setText(String.valueOf(2750.341));
                    getVy0Input().setText(String.valueOf(2434.198));
                    getVz0Input().setText(String.valueOf(-1068.884));
                } else if (buttonMolniya.isSelected()) {
                    getX0Input().setText(String.valueOf(-1529894.287));
                    getY0Input().setText(String.valueOf(-2672877.357));
                    getZ0Input().setText(String.valueOf(-6150115.340));
                    getVx0Input().setText(String.valueOf(8717.518));
                    getVy0Input().setText(String.valueOf(-4989.709));
                    getVz0Input().setText(String.valueOf(0.0));
                } else if (buttonGEO.isSelected()) {
                    getX0Input().setText(String.valueOf(36607358.256));
                    getY0Input().setText(String.valueOf(-20921723.703));
                    getZ0Input().setText(String.valueOf(0.0));
                    getVx0Input().setText(String.valueOf(1525.636));
                    getVy0Input().setText(String.valueOf(2669.451));
                    getVz0Input().setText(String.valueOf(0.0));
                } else if (buttonExperiment.isSelected()) {
                    getX0Input().setText(String.valueOf(6700000.0));
                    getY0Input().setText(String.valueOf(0.0));
                    getZ0Input().setText(String.valueOf(0.0));
                    getVx0Input().setText(String.valueOf(0.0));
                    getVy0Input().setText(String.valueOf(7713.0));
                    getVz0Input().setText(String.valueOf(0.0));
                }
            }
        });

        HBox hBoxOrbitToggle = new HBox(20);
        hBoxOrbitToggle.getChildren().addAll(buttonISS, buttonSunSync, buttonGPS, buttonMolniya, buttonGEO, buttonExperiment, buttonUserDefined);
        setColumnSpan(hBoxOrbitToggle, 6);
        addRow(3, hBoxOrbitToggle);
        hBoxOrbitToggle.setAlignment(Pos.CENTER);

        Button startButton = new Button("Start");
        add(startButton, 0, 4, 6, 1);
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
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
//                XYChart.Series series2 = new XYChart.Series();
//                XYChart.Series series3 = new XYChart.Series();

                // TODO MASS TO GUI!
                double mass = 1;

                double t = NumberUtils.parseTextAsDouble(t0Input);
                double dt = NumberUtils.parseTextAsDouble(dtInput);
                double tMax = NumberUtils.parseTextAsDouble(tMaxInput);
                double x = NumberUtils.parseTextAsDouble(x0Input);
                double y = NumberUtils.parseTextAsDouble(y0Input);
                double z = NumberUtils.parseTextAsDouble(z0Input);
                double vx = NumberUtils.parseTextAsDouble(Vx0Input);
                double vy = NumberUtils.parseTextAsDouble(Vy0Input);
                double vz = NumberUtils.parseTextAsDouble(Vz0Input);
                double qw = NumberUtils.parseTextAsDouble(qwInput);
                double qx = NumberUtils.parseTextAsDouble(qxInput);
                double qy = NumberUtils.parseTextAsDouble(qyInput);
                double qz = NumberUtils.parseTextAsDouble(qzInput);
                double wx = NumberUtils.parseTextAsDouble(wxInput);
                double wy = NumberUtils.parseTextAsDouble(wyInput);
                double wz = NumberUtils.parseTextAsDouble(wzInput);
                double ix = NumberUtils.parseTextAsDouble(JxxInput);
                double iy = NumberUtils.parseTextAsDouble(JyyInput);
                double iz = NumberUtils.parseTextAsDouble(JzzInput);

                // FIXME ADD AREA
                double area = 1;
                double c = 2.4;
                Satellite satellite = new Satellite(x, y, z, vx, vy, vz, ix, iy, iz, mass, area, c);
                Quaternion quaternion = new Quaternion(qw, qx, qy, qz, wx, wy, wz);
                BooleansForIntegration bool = new BooleansForIntegration(checkBoxGeoPot.isSelected(),
                        checkBoxSunGravity.isSelected(), checkBoxMoonGravity.isSelected(),
                        checkBoxSunPres.isSelected(), checkBoxDrag.isSelected());

                RungeKuttaMethod.oneBody(t, dt, tMax, satellite, quaternion, bool);

//                List<Double> x = result.get(0);
//                List<Double> y = result.get(1);
//                List<Double> z = result.get(2);
//                List<Double> vx = result.get(3);
//                List<Double> vy = result.get(4);
//                List<Double> vz = result.get(5);
//                double G = 6.67 * Math.pow(10, -11);
//                double M = 5.9726 * Math.pow(10, 24);
//                double mu = 398600.4415E9;
//                for (int qw = 0; qw < x.size(); qw++) {
////                        series.getData().add(new XYChart.Data(x.get(qw), y.get(qw)));
//                    series.getData().add(new XYChart.Data(qw, (Math.pow(vx.get(qw), 2) + Math.pow(vy.get(qw), 2) + Math.pow(vz.get(qw), 2)) / 2 -
//                            (mu / (x.get(qw) * x.get(qw) + y.get(qw) * y.get(qw) + z.get(qw) * z.get(qw))) * Math.sqrt(x.get(qw) * x.get(qw) + y.get(qw) * y.get(qw) + z.get(qw) * z.get(qw))));
//                    // KinMoment Components
////                    series.getData().add(new XYChart.Data(qw, y.get(qw) * vz.get(qw) - z.get(qw) * vy.get(qw)));
////                    series2.getData().add(new XYChart.Data(qw, z.get(qw) * vx.get(qw) - x.get(qw) * vz.get(qw)));
////                    series3.getData().add(new XYChart.Data(qw, x.get(qw) * vy.get(qw) - y.get(qw) * vx.get(qw)));
//                }
//                List resultAngles = Kepler.convertToKepler(NumberUtils.parseTextAsDouble(x0Input),
//                        NumberUtils.parseTextAsDouble(y0Input), NumberUtils.parseTextAsDouble(z0Input),
//                        NumberUtils.parseTextAsDouble(Vx0Input), NumberUtils.parseTextAsDouble(Vy0Input),
//                        NumberUtils.parseTextAsDouble(Vz0Input));
//                String spacing = "\t\t\t";
//                String elements = String.join(spacing, resultAngles.toString());
                //fixme
//                FileTextWriter.write(MainForcesAbsolute.fileName.getName(), elements);
                series.setName("Energy");
                // KinMoment Components
//                series.setName("c1");
//                series2.setName("c2");
//                series3.setName("c3");
                lineChart.getData().add(series);
                // KinMoment Components
//                lineChart.getData().addAll(series, series2, series3);
                lineChart.setCreateSymbols(false);
                resultScene = new Scene(lineChart, 1000, 600);
                mainWindow.setScene(resultScene);
                mainWindow.show();
            }
        });

        Button conversionToKeplerButton = new Button("Conversion To Kepler");
        add(conversionToKeplerButton, 0, 5, 6, 1);
        setHalignment(conversionToKeplerButton, HPos.CENTER);

        // TODO KEPLER
//        conversionToKeplerButton.setOnAction(event -> {
//            List result = Kepler.convertToKepler(NumberUtils.parseTextAsDouble(x0Input),
//                    NumberUtils.parseTextAsDouble(y0Input), NumberUtils.parseTextAsDouble(z0Input),
//                    NumberUtils.parseTextAsDouble(Vx0Input), NumberUtils.parseTextAsDouble(Vy0Input),
//                    NumberUtils.parseTextAsDouble(Vz0Input));
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

        Button conversionToECEFButton = new Button("Conversion To ECEF");

        HBox hBoxECEFECI = new HBox(3);
        hBoxECEFECI.getChildren().addAll(checkboxLabelPrecession, checkBoxPrecession, checkboxLabelNutation, checkBoxNutation, checkboxLabelER,
                checkBoxER, checkboxLabelPM, checkBoxPM);
        setColumnSpan(hBoxECEFECI, 6);
        addRow(6, hBoxECEFECI);
        hBoxECEFECI.setAlignment(Pos.CENTER);

        HBox hBoxECEFECI2 = new HBox(10);
        hBoxECEFECI2.getChildren().addAll(datePicker, hourLabel, hourSpinner, minuteLabel, minuteSpinner, conversionToECEFButton);
        setColumnSpan(hBoxECEFECI2, 6);
        addRow(7, hBoxECEFECI2);
        hBoxECEFECI2.setAlignment(Pos.CENTER);

        conversionToECEFButton.setOnAction(event -> {

            Calendar c = new GregorianCalendar(datePicker.getValue().getYear(), datePicker.getValue().getMonth().getValue(),
                    datePicker.getValue().getDayOfMonth() - 1, hourSpinner.getValue(), minuteSpinner.getValue());

            ArrayList<ArrayList<Double>> precession = Matrix.identityMatrix(3);
            ArrayList<ArrayList<Double>> nutation = Matrix.identityMatrix(3);
            ArrayList<ArrayList<Double>> earthRotation = Matrix.identityMatrix(3);
            ArrayList<ArrayList<Double>> pole = Matrix.identityMatrix(3);
            if (checkBoxPrecession.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECEF")) {
                precession = ECEF_ECI_CONVERSION.precessionMatrix(c);
            } else if (checkBoxPrecession.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECI")) {
                precession = Matrix.transpose(ECEF_ECI_CONVERSION.precessionMatrix(c));
            }
            if (checkBoxNutation.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECEF")) {
                nutation = ECEF_ECI_CONVERSION.nutationMatrix(c);
            } else if (checkBoxNutation.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECI")) {
                nutation = Matrix.transpose(ECEF_ECI_CONVERSION.nutationMatrix(c));
            }
            if (checkBoxER.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECEF")) {
                earthRotation = ECEF_ECI_CONVERSION.rotationMatrix(c);
            } else if (checkBoxER.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECI")) {
                earthRotation = Matrix.transpose(ECEF_ECI_CONVERSION.rotationMatrix(c));
            }

//            System.out.println(earthRotation);

            if (checkBoxPM.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECEF")) {
                pole = ECEF_ECI_CONVERSION.poleMatrix(c);
            } else if (checkBoxPM.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECI")) {
                pole = Matrix.transpose(ECEF_ECI_CONVERSION.poleMatrix(c));
            }

            ArrayList<Double> initialCoordinates = new ArrayList<>();
            Collections.addAll(initialCoordinates, NumberUtils.parseTextAsDouble(x0Input),
                    NumberUtils.parseTextAsDouble(y0Input), NumberUtils.parseTextAsDouble(z0Input));
            ArrayList<Double> initialVelocities = new ArrayList<>();
            Collections.addAll(initialVelocities, NumberUtils.parseTextAsDouble(Vx0Input),
                    NumberUtils.parseTextAsDouble(Vy0Input), NumberUtils.parseTextAsDouble(Vz0Input));
            ArrayList<ArrayList<Double>> newCoordinates = new ArrayList<>();
            ArrayList<ArrayList<Double>> newVelocities = new ArrayList<>();

            if (checkBoxPM.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECEF")) {
                ArrayList<ArrayList<Double>> help = new ArrayList<>();
                help.add(initialCoordinates);
                newCoordinates = Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(pole, earthRotation), nutation), precession), Matrix.transpose(help));
                help.clear();
                help.add(initialVelocities);
                newVelocities = Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(pole, earthRotation), nutation), precession), Matrix.transpose(help));
            } else if (checkBoxPM.isSelected() && Objects.equals(conversionToECEFButton.getText(), "Conversion To ECI")) {
                ArrayList<ArrayList<Double>> help = new ArrayList<>();
                help.add(initialCoordinates);
                newCoordinates = Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(precession, nutation), earthRotation), pole), Matrix.transpose(help));
                help.clear();
                help.add(initialVelocities);
                newVelocities = Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(precession, nutation), earthRotation), pole), Matrix.transpose(help));
            }

            getVx0Input().setText(String.valueOf(newVelocities.get(0).get(0)));
            getVy0Input().setText(String.valueOf(newVelocities.get(1).get(0)));
            getVz0Input().setText(String.valueOf(newVelocities.get(2).get(0)));
            getX0Input().setText(String.valueOf(newCoordinates.get(0).get(0)));
            getY0Input().setText(String.valueOf(newCoordinates.get(1).get(0)));
            getZ0Input().setText(String.valueOf(newCoordinates.get(2).get(0)));

            if (Objects.equals(conversionToECEFButton.getText(), "Conversion To ECEF")) {
                conversionToECEFButton.setText("Conversion To ECI");
            } else if (Objects.equals(conversionToECEFButton.getText(), "Conversion To ECI")) {
                conversionToECEFButton.setText("Conversion To ECEF");
            }

        });

        HBox hBoxFirstBody1 = new HBox(5);
//        TextFieldTableCell J = new TextFieldTableCell(); // Try to fill like matrix
//        hBoxFirstBody.getChildren().addAll(J);
        Label jxxLabel = new Label("Jxx:");
        Label jyyLabel = new Label("   Jyy:");
        Label jzzLabel = new Label("   Jzz:");
        JxxInput.setMaxWidth(100);
        JyyInput.setMaxWidth(100);
        JzzInput.setMaxWidth(100);
        hBoxFirstBody1.getChildren().addAll(jxxLabel, JxxInput, jyyLabel, JyyInput, jzzLabel, JzzInput);
        setColumnSpan(hBoxFirstBody1, 6);
        addRow(9, hBoxFirstBody1);
        hBoxFirstBody1.setAlignment(Pos.CENTER);

        HBox hBoxFirstBody2 = new HBox(5);
        Label qwLabel = new Label("qw:");
        Label qxLabel = new Label("   qx:");
        Label qyLabel = new Label("   qy:");
        Label qzLabel = new Label("   qz:");
        hBoxFirstBody2.getChildren().addAll(qwLabel, qwInput, qxLabel, qxInput, qyLabel, qyInput, qzLabel, qzInput);
        setColumnSpan(hBoxFirstBody2, 6);
        addRow(10, hBoxFirstBody2);
        hBoxFirstBody2.setAlignment(Pos.CENTER);

        HBox hBoxFirstBody3 = new HBox(5);
        Label wxLabel = new Label("wx:");
        Label wyLabel = new Label("   wy:");
        Label wzLabel = new Label("   wz:");
        hBoxFirstBody3.getChildren().addAll(wxLabel, wxInput, wyLabel, wyInput, wzLabel, wzInput);
        setColumnSpan(hBoxFirstBody3, 6);
        addRow(11, hBoxFirstBody3);
        hBoxFirstBody3.setAlignment(Pos.CENTER);

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
        HBox hBoxDisturbances = new HBox(5);
        hBoxDisturbances.getChildren().addAll(checkboxLabelGeoPot, checkBoxGeoPot,
                checkboxLabelSunGravity, checkBoxSunGravity, checkboxLabelMoonGravity, checkBoxMoonGravity,
                checkboxLabelSunPres, checkBoxSunPres, checkboxLabelDrag, checkBoxDrag);
        setColumnSpan(hBoxDisturbances, 6);
        addRow(12, hBoxDisturbances);
        hBoxDisturbances.setAlignment(Pos.CENTER);
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
