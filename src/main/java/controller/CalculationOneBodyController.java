package controller;

import calculation.RungeKuttaMethod;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.Satellite;
import utils.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by maxtar on 19.03.18.
 */
public class CalculationOneBodyController implements Initializable {

    @FXML
    private CheckBox checkBoxGeopotential;
    @FXML
    private CheckBox checkBoxSunsGravity;
    @FXML
    private CheckBox checkBoxMoonsGravity;
    @FXML
    private CheckBox checkBoxSunsRadiationPressure;
    @FXML
    private CheckBox checkBoxAtmosphericDrag;
    @FXML
    private WebView qw;
    @FXML
    private WebView qx;
    @FXML
    private WebView qy;
    @FXML
    private WebView qz;
    @FXML
    private TextField qwInput;
    @FXML
    private TextField qxInput;
    @FXML
    private TextField qyInput;
    @FXML
    private TextField qzInput;
    @FXML
    private WebView wx;
    @FXML
    private WebView wy;
    @FXML
    private WebView wz;
    @FXML
    private TextField wxInput;
    @FXML
    private TextField wyInput;
    @FXML
    private TextField wzInput;
    @FXML
    private WebView jxx;
    @FXML
    private TextField jxxInput;
    @FXML
    private WebView jyy;
    @FXML
    private TextField jyyInput;
    @FXML
    private WebView jzz;
    @FXML
    private TextField jzzInput;
    @FXML
    private Button convertToEcefButton;
    @FXML
    private Spinner<Integer> hourSpinner;
    @FXML
    private Spinner<Integer> minuteSpinner;
    @FXML
    private DatePicker datePicker;
    @FXML
    private CheckBox checkBoxPrecession;
    @FXML
    private CheckBox checkBoxNutation;
    @FXML
    private CheckBox checkBoxEarthRotation;
    @FXML
    private CheckBox checkBoxPolarMotion;
    @FXML
    private TextField t0Input;
    @FXML
    private TextField dtInput;
    @FXML
    private TextField tMaxInput;
    @FXML
    private TextField Vx0Input;
    @FXML
    private TextField Vy0Input;
    @FXML
    private TextField Vz0Input;
    @FXML
    private TextField x0Input;
    @FXML
    private TextField y0Input;
    @FXML
    private TextField z0Input;
    @FXML
    private ToggleGroup groupInitOrbit;
    @FXML
    private RadioButton radioISS;
    @FXML
    private RadioButton radioSunSync;
    @FXML
    private RadioButton radioGPS;
    @FXML
    private RadioButton radioMolniya;
    @FXML
    private RadioButton radioGEO;
    @FXML
    private RadioButton radioExp;
    @FXML
    private RadioButton radioUserDef;
    @FXML
    private WebView t0;
    @FXML
    private WebView dt;
    @FXML
    private WebView tMax;
    @FXML
    private WebView Vx0;
    @FXML
    private WebView Vy0;
    @FXML
    private WebView Vz0;
    @FXML
    private WebView x0;
    @FXML
    private WebView y0;
    @FXML
    private WebView z0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebEngine t0Engine = t0.getEngine();
        WebEngine dtEngine = dt.getEngine();
        WebEngine tMaxEngine = tMax.getEngine();
        WebEngine Vx0Engine = Vx0.getEngine();
        WebEngine Vy0Engine = Vy0.getEngine();
        WebEngine Vz0Engine = Vz0.getEngine();
        WebEngine x0Engine = x0.getEngine();
        WebEngine y0Engine = y0.getEngine();
        WebEngine z0Engine = z0.getEngine();
        WebEngine jxxEngine = jxx.getEngine();
        WebEngine jyyEngine = jyy.getEngine();
        WebEngine jzzEngine = jzz.getEngine();
        WebEngine qwEngine = qw.getEngine();
        WebEngine qxEngine = qx.getEngine();
        WebEngine qyEngine = qy.getEngine();
        WebEngine qzEngine = qz.getEngine();
        WebEngine wxEngine = wx.getEngine();
        WebEngine wyEngine = wy.getEngine();
        WebEngine wzEngine = wz.getEngine();
        t0Engine.loadContent(setFontSize("t<sub>0</sub>:", 10));
        dtEngine.loadContent(setFontSize("d<sub>t</sub>:", 10));
        tMaxEngine.loadContent(setFontSize("t<sub>max</sub>:", 10));
        Vx0Engine.loadContent(setFontSize("V<sub>x</sub>:", 10));
        Vy0Engine.loadContent(setFontSize("V<sub>y</sub>:", 10));
        Vz0Engine.loadContent(setFontSize("V<sub>z</sub>:", 10));
        x0Engine.loadContent(setFontSize("x<sub>0</sub>:", 10));
        y0Engine.loadContent(setFontSize("y<sub>0</sub>:", 10));
        z0Engine.loadContent(setFontSize("z<sub>0</sub>:", 10));
        jxxEngine.loadContent(setFontSize("J<sub>xx</sub>:", 10));
        jyyEngine.loadContent(setFontSize("J<sub>yy</sub>:", 10));
        jzzEngine.loadContent(setFontSize("J<sub>zz</sub>:", 10));
        qwEngine.loadContent(setFontSize("q<sub>w</sub>:", 10));
        qxEngine.loadContent(setFontSize("q<sub>x</sub>:", 10));
        qyEngine.loadContent(setFontSize("q<sub>y</sub>:", 10));
        qzEngine.loadContent(setFontSize("q<sub>z</sub>:", 10));
        wxEngine.loadContent(setFontSize("&omega;<sub>x</sub>:", 10));
        wyEngine.loadContent(setFontSize("&omega;<sub>y</sub>:", 10));
        wzEngine.loadContent(setFontSize("&omega;<sub>z</sub>:", 10));

        groupInitOrbit.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (radioISS.isSelected()) {
                textFieldSet("-4453783.586", "-5038203.756", "-426384.456",
                        "3831.888", "-2887.221", "-6018.232");
            } else if (radioSunSync.isSelected()) {
                textFieldSet("-2290301.063", "-6379471.940", "0.0",
                        "-883.923", "317.338", "7610.832");
            } else if (radioGPS.isSelected()) {
                textFieldSet("5525336.68", "-15871184.94", "-20998992.446",
                        "2750.341", "2434.198", "-1068.884");
            } else if (radioMolniya.isSelected()) {
                textFieldSet("-1529894.287", "-2672877.357", "-6150115.340",
                        "8717.518", "-4989.709", "0.0");
            } else if (radioGEO.isSelected()) {
                textFieldSet("36607358.256", "-20921723.703", "0.0",
                        "1525.636", "2669.451", "0.0");
            } else if (radioExp.isSelected()) {
                textFieldSet("6700000.0", "0.0", "0.0",
                        "0.0", "7713.0", "0.0");
            }
        });

        x0Input.textProperty().addListener((observable, oldValue, newValue) -> checkUserDef(x0Input));
        y0Input.textProperty().addListener((observable, oldValue, newValue) -> checkUserDef(y0Input));
        z0Input.textProperty().addListener((observable, oldValue, newValue) -> checkUserDef(z0Input));
        Vx0Input.textProperty().addListener((observable, oldValue, newValue) -> checkUserDef(Vx0Input));
        Vy0Input.textProperty().addListener((observable, oldValue, newValue) -> checkUserDef(Vy0Input));
        Vz0Input.textProperty().addListener((observable, oldValue, newValue) -> checkUserDef(Vz0Input));

        datePicker.setValue(LocalDate.now());
    }

    private void textFieldSet(String x0, String y0, String z0,
                              String Vx0, String Vy0, String Vz0) {
        x0Input.setText(x0);
        y0Input.setText(y0);
        z0Input.setText(z0);
        Vx0Input.setText(Vx0);
        Vy0Input.setText(Vy0);
        Vz0Input.setText(Vz0);
    }

    private void checkUserDef(TextField textField) {
        if (textField.isFocused()) {
            radioUserDef.setSelected(true);
        }
    }

    private String setFontSize(String string, int size) {
        return "<p style=\"font-size: " + size + "pt;\">" + string + "</p>";
    }

    //todo check method
    public void handleStartButtonAction() {

        // todo check if this method understand numbers like 1e7
        if (NumberUtils.textFieldsAreDouble(t0Input, dtInput, tMaxInput, Vx0Input, Vy0Input, Vz0Input,
                x0Input, y0Input, z0Input)) {
            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("N");
            yAxis.setLabel("Energy");
            final LineChart<Number, Number> lineChart = new
                    LineChart<>(xAxis, yAxis);
            XYChart.Series<Number, Number> series = new XYChart.Series<>();

            // TODO MASS TO GUI!
            double mass = 1;

            double t = Double.parseDouble(t0Input.getText());
            double dt = Double.parseDouble(dtInput.getText());
            double tMax = Double.parseDouble(tMaxInput.getText());
            double x = Double.parseDouble(x0Input.getText());
            double y = Double.parseDouble(y0Input.getText());
            double z = Double.parseDouble(z0Input.getText());
            double vx = Double.parseDouble(Vx0Input.getText());
            double vy = Double.parseDouble(Vy0Input.getText());
            double vz = Double.parseDouble(Vz0Input.getText());
            double qw = Double.parseDouble(qwInput.getText());
            double qx = Double.parseDouble(qxInput.getText());
            double qy = Double.parseDouble(qyInput.getText());
            double qz = Double.parseDouble(qzInput.getText());
            double wx = Double.parseDouble(wxInput.getText());
            double wy = Double.parseDouble(wyInput.getText());
            double wz = Double.parseDouble(wzInput.getText());
            double ix = Double.parseDouble(jxxInput.getText());
            double iy = Double.parseDouble(jyyInput.getText());
            double iz = Double.parseDouble(jzzInput.getText());

            // FIXME ADD AREA to gui
            double area = 1;
            double c = 2.4;
            Satellite satellite = new Satellite(x, y, z, vx, vy, vz, ix, iy, iz, mass, area, c);
            Quaternion quaternion = new Quaternion(qw, qx, qy, qz, wx, wy, wz);
            BooleansForIntegration bool = new BooleansForIntegration(checkBoxGeopotential.isSelected(),
                    checkBoxSunsGravity.isSelected(), checkBoxMoonsGravity.isSelected(),
                    checkBoxSunsRadiationPressure.isSelected(), checkBoxAtmosphericDrag.isSelected());

            // todo fix chart
            RungeKuttaMethod.oneBody(t, dt, tMax, satellite, quaternion, bool);
            series.setName("Energy");
            lineChart.getData().add(series);
            lineChart.setCreateSymbols(false);
            MainCalculation.getStage().setScene(new Scene(lineChart, 1000, 600));
        } else {
            // TODO: 19.03.18  
        }
    }

    public void handlePrecessionCheckBoxClicked() {
        checkBoxPrecession.setSelected(!checkBoxPrecession.isSelected());
    }

    public void handleNutationCheckBoxClicked() {
        checkBoxNutation.setSelected(!checkBoxNutation.isSelected());
    }

    public void handleEarthRotationCheckBoxClicked() {
        checkBoxEarthRotation.setSelected(!checkBoxEarthRotation.isSelected());
    }

    public void handlePolarMotionCheckBoxClicked() {
        checkBoxPolarMotion.setSelected(!checkBoxPolarMotion.isSelected());
    }

    // todo check method
    public void handleConvertToEcefButtonAction() {
        Calendar c = new GregorianCalendar(datePicker.getValue().getYear(), datePicker.getValue().getMonth().getValue(),
                datePicker.getValue().getDayOfMonth() - 1, hourSpinner.getValue(), minuteSpinner.getValue());

        ArrayList<ArrayList<Double>> precession = Matrix.identityMatrix(3);
        ArrayList<ArrayList<Double>> nutation = Matrix.identityMatrix(3);
        ArrayList<ArrayList<Double>> earthRotation = Matrix.identityMatrix(3);
        ArrayList<ArrayList<Double>> pole = Matrix.identityMatrix(3);
        if (checkBoxPrecession.isSelected() && Objects.equals(convertToEcefButton.getText(), "Conversion To ECEF")) {
            precession = ECEF_ECI_CONVERSION.precessionMatrix(c);
        } else if (checkBoxPrecession.isSelected() && Objects.equals(convertToEcefButton.getText(), "Conversion To ECI")) {
            precession = Matrix.transpose(ECEF_ECI_CONVERSION.precessionMatrix(c));
        }
        if (checkBoxNutation.isSelected() && Objects.equals(convertToEcefButton.getText(), "Conversion To ECEF")) {
            nutation = ECEF_ECI_CONVERSION.nutationMatrix(c);
        } else if (checkBoxNutation.isSelected() && Objects.equals(convertToEcefButton.getText(), "Conversion To ECI")) {
            nutation = Matrix.transpose(ECEF_ECI_CONVERSION.nutationMatrix(c));
        }
        if (checkBoxEarthRotation.isSelected() && Objects.equals(convertToEcefButton.getText(), "Conversion To ECEF")) {
            earthRotation = ECEF_ECI_CONVERSION.rotationMatrix(c);
        } else if (checkBoxEarthRotation.isSelected() && Objects.equals(convertToEcefButton.getText(), "Conversion To ECI")) {
            earthRotation = Matrix.transpose(ECEF_ECI_CONVERSION.rotationMatrix(c));
        }

        if (checkBoxPolarMotion.isSelected() && Objects.equals(convertToEcefButton.getText(), "Conversion To ECEF")) {
            pole = ECEF_ECI_CONVERSION.poleMatrix(c);
        } else if (checkBoxPolarMotion.isSelected() && Objects.equals(convertToEcefButton.getText(), "Conversion To ECI")) {
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

        if (checkBoxPolarMotion.isSelected() && Objects.equals(convertToEcefButton.getText(), "Conversion To ECEF")) {
            ArrayList<ArrayList<Double>> help = new ArrayList<>();
            help.add(initialCoordinates);
            newCoordinates = Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(pole, earthRotation), nutation), precession), Matrix.transpose(help));
            help.clear();
            help.add(initialVelocities);
            newVelocities = Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(pole, earthRotation), nutation), precession), Matrix.transpose(help));
        } else if (checkBoxPolarMotion.isSelected() && Objects.equals(convertToEcefButton.getText(), "Conversion To ECI")) {
            ArrayList<ArrayList<Double>> help = new ArrayList<>();
            help.add(initialCoordinates);
            newCoordinates = Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(precession, nutation), earthRotation), pole), Matrix.transpose(help));
            help.clear();
            help.add(initialVelocities);
            newVelocities = Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(Matrix.matrixMult(precession, nutation), earthRotation), pole), Matrix.transpose(help));
        }

        Vx0Input.setText(String.valueOf(newVelocities.get(0).get(0)));
        Vy0Input.setText(String.valueOf(newVelocities.get(1).get(0)));
        Vz0Input.setText(String.valueOf(newVelocities.get(2).get(0)));
        x0Input.setText(String.valueOf(newCoordinates.get(0).get(0)));
        y0Input.setText(String.valueOf(newCoordinates.get(1).get(0)));
        z0Input.setText(String.valueOf(newCoordinates.get(2).get(0)));

        if (Objects.equals(convertToEcefButton.getText(), "Conversion To ECEF")) {
            convertToEcefButton.setText("Conversion To ECI");
        } else if (Objects.equals(convertToEcefButton.getText(), "Conversion To ECI")) {
            convertToEcefButton.setText("Conversion To ECEF");
        }
    }

    public void handleGeopotentialCheckBoxClicked() {
        checkBoxGeopotential.setSelected(!checkBoxGeopotential.isSelected());
    }

    public void handleSunsGravityCheckBoxClicked() {
        checkBoxSunsGravity.setSelected(!checkBoxSunsGravity.isSelected());
    }

    public void handleMoonsGravityCheckBoxClicked() {
        checkBoxMoonsGravity.setSelected(!checkBoxMoonsGravity.isSelected());
    }

    public void handleSunsRadiationPressureCheckBoxClicked() {
        checkBoxSunsRadiationPressure.setSelected(!checkBoxSunsRadiationPressure.isSelected());
    }

    public void handleAtmosphericDragCheckBoxClicked() {
        checkBoxAtmosphericDrag.setSelected(!checkBoxAtmosphericDrag.isSelected());
    }
}
