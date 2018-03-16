package animation;
/**
 * Created by Maxim Tarasov on 12.10.2016.
 */

import model.Material;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Double.parseDouble;

public class AniQuat extends Application {

    private static final String titleTxt = "Animation";
    final Group root = new Group();
    final Group axisGroup = new Group();
    final StackPane axisLVLH = new StackPane();
    final Form world = new Form();
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Form cameraForm = new Form();
    final Form cameraForm2 = new Form();
    final Form cameraForm3 = new Form();
    final double cameraDistance = 2000;
    final Form spaceGroup = new Form();
    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;
    private Integer j = 0;
    Stage primaryStage;
    List<String> list;
    List<Double> qi = new ArrayList<>();
    List<Double> qj = new ArrayList<>();
    List<Double> qk = new ArrayList<>();
    List<Double> ql = new ArrayList<>();
    List<Double> x = new ArrayList<>();
    List<Double> y = new ArrayList<>();
    List<Double> z = new ArrayList<>();
    List<Double> vx = new ArrayList<>();
    List<Double> vy = new ArrayList<>();
    List<Double> vz = new ArrayList<>();

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
        Button btn1 = new Button("Choose area file...");
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
            startAnimation(primaryStage, Paths.get(selectedFile.getPath()));
        }
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
        camera.setFarClip(30000);
        camera.setTranslateZ(-cameraDistance);
        cameraForm.ry.setAngle(320.0);
        cameraForm.rx.setAngle(40);
    }

    private void buildAxes() {
        Material redMaterial = new Material(Color.RED);
        Material greenMaterial = new Material(Color.GREEN);
        Material blueMaterial = new Material(Color.BLUE);

        final Box xAxis = new Box(370, 1, 1);
        final Box yAxis = new Box(1, 370, 1);
        final Box zAxis = new Box(1, 1, 370);

//        Box axisX = new Box(370, 1 , 1);
//        Box axisY = new Box(1, 370 , 1);
//        Box axisZ = new Box(1, 1 , 370);
//        final StackPane axisStack = new StackPane();
//        axisStack.getChildren().addAll(axisX, axisY, axisZ);

        final Box xLabel1 = new Box(10, 1, 1);
        xLabel1.setTranslateX(195);
        xLabel1.setRotate(45);
        final Box xLabel2 = new Box(10, 1, 1);
        xLabel2.setTranslateX(195);
        xLabel2.setRotate(-45);

        final Box yLabel1 = new Box(12, 1, 1);
        yLabel1.setTranslateY(194);
//        yLabel1.setTranslateX(-80);
        yLabel1.setRotate(-55);
        final Box yLabel2 = new Box(7, 1, 1);
        yLabel2.setTranslateX(2);
        yLabel2.setTranslateY(196);
//        yLabel2.setTranslateX(80);
        yLabel2.setRotate(55);

        final Box zLabel1 = new Box(9, 1, 1);
        zLabel1.setTranslateZ(195);
        final Box zLabel2 = new Box(9, 1, 1);
        zLabel2.setTranslateZ(195);
        zLabel2.setTranslateY(8);
        final Box zLabel3 = new Box(11, 1, 1);
        zLabel3.setTranslateZ(195);
        zLabel3.setTranslateY(4);
        zLabel3.setRotate(-45);


        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis, xLabel1, xLabel2, yLabel1, yLabel2, zLabel1, zLabel2, zLabel3);
//        axisLVLH.getChildren().addAll(xLabel1, xLabel2, yLabel1, yLabel2, zLabel1, zLabel2, zLabel3);
        world.getChildren().add(axisGroup);
    }

    private void buildSpace(Path path) {

        Material whiteMaterial = new Material(Color.WHITE);
        Material greyMaterial = new Material(Color.GREY);

        Form spaceForm = new Form();

        Cylinder cylinder = new Cylinder(5.0, 20.0);
        cylinder.setMaterial(whiteMaterial);
//        cylinder.setRotationAxis(Rotate.X_AXIS);
//        cylinder.setRotate(90);
//        cylinder.setRotationAxis(Rotate.Y_AXIS);
//        cylinder.setRotate(90);
        cylinder.setRotationAxis(Rotate.Z_AXIS);
        cylinder.setRotate(90);

//        final Box pivotY = new Box(1, 300, 1);
        final Box pivotZ = new Box(1, 1, 300);
//        pivotY.setMaterial(greyMaterial);
        pivotZ.setMaterial(greyMaterial);

        spaceGroup.getChildren().add(spaceForm);

        final StackPane stack = new StackPane();
//        stack.getChildren().addAll(cylinder, pivotY);
        stack.getChildren().addAll(cylinder, pivotZ);
//        stack.getChildren().addAll(cylinder);

        Material redMaterial = new Material(Color.RED);
        Material greenMaterial = new Material(Color.GREEN);
        Material blueMaterial = new Material(Color.BLUE);
//        final StackPane axisLVLH = new StackPane();
        final Box LVLHX = new Box(360, 1, 1);
        final Box LVLHY = new Box(1, 360, 1);
        final Box LVLHZ = new Box(1, 1, 360);
        LVLHX.setMaterial(redMaterial);
        LVLHY.setMaterial(greenMaterial);
        LVLHZ.setMaterial(blueMaterial);
        axisLVLH.getChildren().addAll(LVLHX, LVLHY, LVLHZ);

        spaceGroup.getChildren().add(stack);

        axisLVLH.setTranslateX(-180);
        axisLVLH.setTranslateY(-180);
        spaceGroup.getChildren().add(axisLVLH);
//        axisLVLH.setTranslateZ(-180);

        world.getChildren().addAll(spaceGroup);

//        try {
//            list = Files.readAllLines(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for (int qx = 0; qx < list.size() - 2; qx++) {
//            String[] partsOrigin = list.get(qx).split("\\t\\t\\t");
//            Point3D origin = new Point3D(Double.parseDouble(partsOrigin[0]) / 1000, Double.parseDouble(partsOrigin[1]) / 1000,
//                    Double.parseDouble(partsOrigin[2]) / 1000);
//            qx++;
//            String[] partsTarget = list.get(qx).split("\\t\\t\\t");
//            Point3D target = new Point3D(Double.parseDouble(partsTarget[0]) / 1000, Double.parseDouble(partsTarget[1]) / 1000,
//                    Double.parseDouble(partsTarget[2]) / 1000);
//            Cylinder line = createConnection(origin, target);
//            line.setMaterial(greyMaterial);
//            world.getChildren().add(line);
//        }

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        stack.setTranslateX(-5);
        stack.setTranslateY(-10);
        // For pivotY
//        stack.setTranslateY(-150);

        try {
            list = Files.readAllLines(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        for (int j = 0; j < list.size() - 1; j++) {
            String[] parts = list.get(j).split("\\t\\t\\t");
            qi.add(j, parseDouble(parts[0]));
            qj.add(j, parseDouble(parts[1]));
            qk.add(j, parseDouble(parts[2]));
            ql.add(j, parseDouble(parts[3]));
            x.add(j, parseDouble(parts[7]));
            y.add(j, parseDouble(parts[8]));
            z.add(j, parseDouble(parts[9]));
            vx.add(j, parseDouble(parts[10]));
            vy.add(j, parseDouble(parts[11]));
            vz.add(j, parseDouble(parts[12]));
        }

        j++;

        Duration duration = Duration.millis(5);
        EventHandler onFinished = t -> {
//            qx = 1;
            double qw = qi.get(j);
            double qx = qj.get(j);
            double qy = qk.get(j);
            double qz = ql.get(j);

            Quaternion first = new Quaternion(qw, qx, qy, qz);
            first = Quaternion.normalize(first);
//            first = Quaternion.conjugate(first);
//            System.out.println("!!!");
//            System.out.println(first);
            double[] rECI = new double[]{x.get(j), y.get(j), z.get(j)};
            double[] vECI = new double[]{vx.get(j), vy.get(j), vz.get(j)};

            double[] zNew = VectorAlgebra.invert(VectorAlgebra.normalize(rECI));
            double[] yNew = VectorAlgebra.normalize(VectorAlgebra.multV(zNew, vECI));
            double[] xNew = VectorAlgebra.multV(yNew, zNew);

            Quaternion second = Quaternion.fromRotationMatrix(xNew[0], xNew[1], xNew[2], yNew[0],
                    yNew[1], yNew[2], zNew[0], zNew[1], zNew[2]);
            second = Quaternion.conjugate(second);
//            System.out.println(second);
            second = Quaternion.normalize(second);

//            Quaternion fin = Quaternion.quatMultQuat(first, second);
//            Quaternion fin = Quaternion.quatMultQuat(second, first);
//            fin = Quaternion.normalize(fin);
//            System.out.println(fin);

//            qw = fin.qw;
//            qx = fin.qx;
//            qy = fin.qy;
//            qz = fin.qz;

            double qwa = second.qw;
            double qxa = second.qx;
            double qya = second.qy;
            double qza = second.qz;

            double mod = Math.sqrt(qxa * qxa + qya * qya + qza * qza);
            Point3D pAxis = new Point3D(qxa / mod, qya / mod, qza / mod);
            double angleAxis = 2 * Math.acos(qwa);
            axisLVLH.setRotationAxis(pAxis);
            axisLVLH.setRotate(Math.toDegrees(angleAxis));

            double abs = Math.sqrt(qx * qx + qy * qy + qz * qz);
            Point3D p = new Point3D(qx / abs, qy / abs, qz / abs);
            double angle = 2 * Math.acos(qw);
            stack.setRotationAxis(p);
            stack.setRotate(Math.toDegrees(angle));

            j++;
//            if (qx == qj.size()) {
//                qx = 0;
//            }
        };

        KeyFrame keyFrame = new KeyFrame(duration, onFinished);
        timeline.getKeyFrames().add(keyFrame);
        //timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void handleMouse(Scene scene) {
        scene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged(me -> {
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
                double mod = modifierFactor * modifier * 2.0;
                cameraForm.ry.setAngle(cameraForm.ry.getAngle() - mouseDeltaX * mod);  // +
                cameraForm.rx.setAngle(cameraForm.rx.getAngle() + mouseDeltaY * mod);  // -
            } else if (me.isSecondaryButtonDown()) {
                double z1 = camera.getTranslateZ();
                double newZ = z1 + mouseDeltaX * modifierFactor * modifier;
                camera.setTranslateZ(newZ);
            } else if (me.isMiddleButtonDown()) {
                double mod = modifierFactor * modifier * 3.0;
                cameraForm2.t.setX(cameraForm2.t.getX() + mouseDeltaX * mod);  // -
                cameraForm2.t.setY(cameraForm2.t.getY() + mouseDeltaY * mod);  // -
            }
        });
    }

    public void startAnimation(Stage primaryStage, Path path) {
        buildScene();
        buildCamera();
        buildAxes();
        buildSpace(path);

        Scene scene = new Scene(root, 1024, 768, true);
//        scene.setFill(Color.web("#000028"));
        handleMouse(scene);

        primaryStage.setTitle("Animation");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setCamera(camera);

    }

    public Cylinder createConnection(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude() * 5;

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
