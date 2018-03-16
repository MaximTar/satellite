package animation;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Controller for fxml animation application.
 **/
public class ChooserController {
    private static Stage stage;
    private File fileToAnimate;
    private FileChooser fileChooser;

    public ChooserController() {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setTitle("Select text file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
    }

    @FXML
    private void oneBodyOrbitFileChooser() {
        if (isChosen()) {
            OneBodyOrbitAnimation.startAnimation(stage, Paths.get(fileToAnimate.getPath()));
        }
    }

    @FXML
    private void twoBodiesOrbitFileChooser() {
        if (isChosen()) {
            TwoBodiesOrbitAnimation.startAnimation(stage, Paths.get(fileToAnimate.getPath()));
        }
    }

    @FXML
    private void oneBodyRotationFileChooser() {
        if (isChosen()) {
            OneBodyRotationAnimation.startAnimation(stage, Paths.get(fileToAnimate.getPath()));
        }
    }

    @FXML
    private void twoBodiesRotationFileChooser() {
        if (isChosen()) {
            TwoBodiesRotationAnimation.startAnimation(stage, Paths.get(fileToAnimate.getPath()));
        }
    }

    private boolean isChosen() {
        fileToAnimate = fileChooser.showOpenDialog(null);

        return Objects.nonNull(fileToAnimate);
    }

    public static void setStage(Stage stage) {
        ChooserController.stage = stage;
    }
}
