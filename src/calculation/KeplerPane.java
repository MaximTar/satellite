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

import java.util.ArrayList;

public class KeplerPane extends GridPane {

    private Scene resultScene;

    private Button startButton = new Button("Start");
    private Button conversionToCoordinatesButton = new Button("Conversion");

    private Label Kt0Label = new Label("t0:");
    private TextField Kt0Input = new TextField("0");
    private Label KdtLabel = new Label("dt:");
    private TextField KdtInput = new TextField("1000");
    private Label KtMaxLabel = new Label("tMax:");
    private TextField KtMaxInput = new TextField("86500");
    private Label OmegaLabel = new Label("Omega:");
    private TextField OmegaInput = new TextField("90.0");
    private Label iLabel = new Label("i:");
    private TextField iInput = new TextField("180.0");
    private Label wLabel = new Label("w:");
    private TextField wInput = new TextField("0.0");
    private Label pLabel = new Label("p:");
    private TextField pInput = new TextField("4.2E7");
    private Label eLabel = new Label("e:");
    private TextField eInput = new TextField("0.0");
    private Label tauLabel = new Label("tau:");
    private TextField tauInput = new TextField("0.0");

    public KeplerPane(Stage mainWindow) {
        this.setPadding(new Insets(15, 15, 15, 15));
        this.setVgap(15);
        this.setHgap(8);

        this.add(Kt0Label, 0, 0);
        this.add(Kt0Input, 1, 0);
        this.add(KdtLabel, 0, 1);
        this.add(KdtInput, 1, 1);
        this.add(KtMaxLabel, 0, 2);
        this.add(KtMaxInput, 1, 2);
        this.add(OmegaLabel, 3, 0);
        this.add(OmegaInput, 4, 0);
        this.add(iLabel, 3, 1);
        this.add(iInput, 4, 1);
        this.add(wLabel, 3, 2);
        this.add(wInput, 4, 2);
        this.add(pLabel, 5, 0);
        this.add(pInput, 6, 0);
        this.add(eLabel, 5, 1);
        this.add(eInput, 6, 1);
        this.add(tauLabel, 5, 2);
        this.add(tauInput, 6, 2);

//        startButton = new Button("Start");
        this.add(startButton, 4, 4);
        setHalignment(startButton, HPos.CENTER);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (NumberUtils.isDouble(Kt0Input, Kt0Input.getText()) && NumberUtils.isDouble(KdtInput, KdtInput.getText())
                        && NumberUtils.isDouble(KtMaxInput, KtMaxInput.getText()) && NumberUtils.isDouble(OmegaInput, OmegaInput.getText())
                        && NumberUtils.isDouble(iInput, iInput.getText()) && NumberUtils.isDouble(wInput, wInput.getText())
                        && NumberUtils.isDouble(pInput, pInput.getText()) && NumberUtils.isDouble(eInput, eInput.getText())
                        && NumberUtils.isDouble(tauInput, tauInput.getText())) {
                    //defining the axes
                    final NumberAxis xAxis = new NumberAxis();
                    final NumberAxis yAxis = new NumberAxis();
                    xAxis.setLabel("X");
                    yAxis.setLabel("Y");
                    //creating the chart
                    final LineChart<Number, Number> lineChart  = new
                            LineChart<>(xAxis, yAxis);
                    //defining a series
                    XYChart.Series series = new XYChart.Series();
                    //populating the series with data
                    ArrayList<Double> subresult = Kepler.convertToCoordinate(Double.parseDouble(OmegaInput.getText()),
                            Double.parseDouble(iInput.getText()), Double.parseDouble(wInput.getText()),
                            Double.parseDouble(pInput.getText()), Double.parseDouble(eInput.getText()),
                            Double.parseDouble(tauInput.getText()));
                    ArrayList<ArrayList<Double>> result = CalculationUtils.calculate(
                            Double.parseDouble(Kt0Input.getText()), Double.parseDouble(KdtInput.getText()),
                            Double.parseDouble(KtMaxInput.getText()),subresult.get(0), subresult.get(1),
                            subresult.get(2), subresult.get(3), subresult.get(4), subresult.get(5)
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
                        series.getData().add(new XYChart.Data(i, (Math.pow(vx.get(i), 2) + Math.pow(vy.get(i), 2) + Math.pow(vz.get(i), 2))/2 -
                                (G*M/(x.get(i)*x.get(i) + y.get(i)*y.get(i) + z.get(i)*z.get(i)))*Math.sqrt(x.get(i)*x.get(i) + y.get(i)*y.get(i) + z.get(i)*z.get(i))));
                    }
                    series.setName("Energy");
                    lineChart .getData().add(series);
                    lineChart.setCreateSymbols(false);
                    resultScene = new Scene(lineChart , 800, 600);
                    mainWindow.setScene(resultScene);
                    mainWindow.show();
                }
            }
        });

        this.add(conversionToCoordinatesButton, 4, 5);
        setHalignment(conversionToCoordinatesButton, HPos.CENTER);


        conversionToCoordinatesButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                ArrayList<Double> result = Kepler.convertToCoordinate(Double.parseDouble(OmegaInput.getText()),
                        Double.parseDouble(iInput.getText()), Double.parseDouble(wInput.getText()),
                        Double.parseDouble(pInput.getText()), Double.parseDouble(eInput.getText()),
                        Double.parseDouble(tauInput.getText()));
                CoordinatePane gridCoordinate = new CoordinatePane(mainWindow);
                gridCoordinate.getX0Input().setText(String.valueOf(result.get(0)));
                gridCoordinate.getY0Input().setText(String.valueOf(result.get(1)));
                gridCoordinate.getZ0Input().setText(String.valueOf(result.get(2)));
                gridCoordinate.getVx0Input().setText(String.valueOf(result.get(3)));
                gridCoordinate.getVy0Input().setText(String.valueOf(result.get(4)));
                gridCoordinate.getVz0Input().setText(String.valueOf(result.get(5)));
                Scene startCoordinateScene = new Scene(gridCoordinate, 600, 250);
                mainWindow.setScene(startCoordinateScene);
                mainWindow.show();
            }
        });
    }

    public TextField getKt0Input() {
        return Kt0Input;
    }

    public TextField getKdtInput() {
        return KdtInput;
    }

    public TextField getKtMaxInput() {
        return KtMaxInput;
    }

    public TextField getOmegaInput() {
        return OmegaInput;
    }

    public TextField getiInput() {
        return iInput;
    }

    public TextField getwInput() {
        return wInput;
    }

    public TextField getpInput() {
        return pInput;
    }

    public TextField geteInput() {
        return eInput;
    }

    public TextField getTauInput() {
        return tauInput;
    }

}
