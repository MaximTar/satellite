package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;

import java.io.IOException;

/**
 * Created by maxtar on 19.03.18.
 */
public class CalculationFirstViewController {

    public void oneBodyCoordinateView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/calculationOneBody.fxml"));
        try {
            MainCalculation.getStage().setScene(new Scene(loader.load(), 1100, 600));
        } catch (IOException e) {
            // todo handle
            e.printStackTrace();
        }
    }

    public void twoBodiesCoordinateView(ActionEvent actionEvent) {

    }

    public void keplerView(ActionEvent actionEvent) {

    }
}
