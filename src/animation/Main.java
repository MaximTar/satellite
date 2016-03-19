package animation;

import calculation.Kepler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableFloatArray;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.Form;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;

import static java.lang.Double.*;

public class Main extends Application {

    private static final String titleTxt = "Animation";
    final Group root = new Group();
    final Group axisGroup = new Group();
    final Form world = new Form();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Form cameraForm = new Form();
    final Form cameraForm2 = new Form();
    final Form cameraForm3 = new Form();
    final double cameraDistance = 200000;
    final Form spaceGroup = new Form();
//    boolean timelinePlaying = false;
//    double ONE_FRAME = 1.0 / 24.0;
//    double DELTA_MULTIPLIER = 200.0;
//    double CONTROL_MULTIPLIER = 0.1;
//    double SHIFT_MULTIPLIER = 0.1;
//    double ALT_MULTIPLIER = 0.5;
    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;
    private Integer j = 0;
    Stage primaryStage;
    List<String> list;
    ArrayList<Double> x = new ArrayList<>();
    ArrayList<Double> y = new ArrayList<>();
    ArrayList<Double> z = new ArrayList<>();
//    ArrayList<Float> points = new ArrayList<>();

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {

        primaryStage = stage;
        primaryStage.setTitle(titleTxt);

        // Window label
        Label label = new Label("Choose file to animate");
        HBox labelHb = new HBox();
        labelHb.setAlignment(Pos.CENTER);
        labelHb.getChildren().add(label);

        // Buttons
        Button btn1 = new Button("Choose a file...");
        btn1.setOnAction(new SingleFcButtonListener());
        HBox buttonHb1 = new HBox(10);
        buttonHb1.setAlignment(Pos.CENTER);
        buttonHb1.getChildren().addAll(btn1);

        // Status message text
        Text actionStatus = new Text();
        actionStatus.setFont(Font.font("Calibri", FontWeight.NORMAL, 20));
        actionStatus.setFill(Color.FIREBRICK);

        // Vbox
        VBox vbox = new VBox(30);
        vbox.setPadding(new Insets(25, 25, 25, 25));
        vbox.getChildren().addAll(labelHb, buttonHb1, actionStatus);

        // Scene
        Scene scene = new Scene(vbox, 300, 180); // w x h
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private class SingleFcButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            showSingleFileChooser();
        }
    }

    private void showSingleFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File selectedFile = fileChooser.showOpenDialog(null);
        fileChooser.setTitle("Select text file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        if (selectedFile != null) {
            start_animation(primaryStage, Paths.get(selectedFile.getPath()));
        }
//        else {
//            actionStatus.setText("File selection cancelled.");
//        }
    }

    private void buildScene() {
        root.getChildren().add(world);
    }

    private void buildCamera() {
        root.getChildren().add(cameraForm);
        cameraForm.getChildren().add(cameraForm2);
        cameraForm2.getChildren().add(cameraForm3);
        cameraForm3.getChildren().add(camera);
        cameraForm3.setRotateZ(180.0);

        camera.setNearClip(1);
        camera.setFarClip(1E8);
        camera.setTranslateZ(-cameraDistance);
        cameraForm.ry.setAngle(320.0);
        cameraForm.rx.setAngle(40);
    }

    private void buildAxes() {
        final PhongMaterial whiteMaterial = new PhongMaterial();
        whiteMaterial.setDiffuseColor(Color.WHITE);
        whiteMaterial.setSpecularColor(Color.WHITE);

        final Box xAxis = new Box(37240.0, 100, 100);
        final Box yAxis = new Box(100, 37240.0, 100);
        final Box zAxis = new Box(100, 100, 37240.0);

        xAxis.setMaterial(whiteMaterial);
        yAxis.setMaterial(whiteMaterial);
        zAxis.setMaterial(whiteMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        world.getChildren().addAll(axisGroup);
    }

    private void buildSpace(Path path) {

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.GREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial whiteMaterial = new PhongMaterial();
        whiteMaterial.setDiffuseColor(Color.WHITE);
        whiteMaterial.setSpecularColor(Color.WHITE);

        final PhongMaterial greyMaterial = new PhongMaterial();
        greyMaterial.setDiffuseColor(Color.GREY);
        greyMaterial.setSpecularColor(Color.GREY);

        Form spaceForm = new Form();
        Form earthForm = new Form();

        Sphere earthSphere = new Sphere(6370.0);
        earthSphere.setMaterial(greenMaterial);

        Sphere satelliteSphere = new Sphere(500.0);
        satelliteSphere.setMaterial(whiteMaterial);

        spaceForm.getChildren().add(earthForm);
        earthForm.getChildren().add(earthSphere);
        spaceGroup.getChildren().add(spaceForm);

        final StackPane stack = new StackPane();
        stack.getChildren().addAll(satelliteSphere);
        spaceGroup.getChildren().add(stack);

        world.getChildren().addAll(spaceGroup);

        try {
            list = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < list.size() - 2; j++) {
            String[] parts_origin = list.get(j).split("\\t\\t\\t");
            Point3D origin = new Point3D(Double.parseDouble(parts_origin[0])/1000, Double.parseDouble(parts_origin[1])/1000,
                    Double.parseDouble(parts_origin[2])/1000);
            j++;
            String[] parts_target = list.get(j).split("\\t\\t\\t");
            Point3D target = new Point3D(Double.parseDouble(parts_target[0])/1000, Double.parseDouble(parts_target[1])/1000,
                    Double.parseDouble(parts_target[2])/1000);
            Cylinder line = createConnection(origin, target);
            line.setMaterial(greyMaterial);
            world.getChildren().add(line);
        }

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);


        //Path path = Paths.get("C:", "Users", "Labcomp-1", "IdeaProjects", "hello", "hello", "04-02-2016 04-26.txt");
        try {
            list = Files.readAllLines(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        for (int j = 0; j < list.size() - 1; j++) {
            String[] parts = list.get(j).split("\\t\\t\\t");
            x.add(j, parseDouble(parts[0]));
            y.add(j, parseDouble(parts[1]));
            z.add(j, parseDouble(parts[2]));
        }

        Duration duration = Duration.millis(1);
        EventHandler onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                i = 0;
                stack.setTranslateX(x.get(j) / 1000);
                stack.setTranslateY(y.get(j) / 1000);
                stack.setTranslateZ(z.get(j) / 1000);
                j++;
                if (j == x.size()) {
                    j = 0;
                }
            }
        };
        KeyFrame keyFrame = new KeyFrame(duration, onFinished);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private void handleMouse(Scene scene, final Node root) {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = 1.0;
                double modifierFactor = 0.1;

                if (me.isControlDown()) {
                    modifier = 0.1;
                }
                if (me.isShiftDown()) {
                    modifier = 500.0;
                }
                if (me.isPrimaryButtonDown()) {
                    cameraForm.ry.setAngle(cameraForm.ry.getAngle() - mouseDeltaX * modifierFactor * modifier * 2.0);  // +
                    cameraForm.rx.setAngle(cameraForm.rx.getAngle() + mouseDeltaY * modifierFactor * modifier * 2.0);  // -
                } else if (me.isSecondaryButtonDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z + mouseDeltaX * modifierFactor * modifier;
                    camera.setTranslateZ(newZ);
                } else if (me.isMiddleButtonDown()) {
                    cameraForm2.t.setX(cameraForm2.t.getX() + mouseDeltaX * modifierFactor * modifier * 3.0);  // -
                    cameraForm2.t.setY(cameraForm2.t.getY() + mouseDeltaY * modifierFactor * modifier * 3.0);  // -
                }
            }
        });
    }

    public void start_animation(Stage primaryStage, Path path) {
        buildScene();
        buildCamera();
        buildAxes();
        buildSpace(path);

        Scene scene = new Scene(root, 1024, 768, true);
        scene.setFill(Color.web("#000028"));
        handleMouse(scene, world);

        primaryStage.setTitle("Animation");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setCamera(camera);

    }

    public Cylinder createConnection(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude()*5;

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(200, height);
        line.setOpacity(0.7);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }
}