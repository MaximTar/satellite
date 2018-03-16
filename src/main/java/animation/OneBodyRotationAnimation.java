package animation;
/**
 * Created by Maxim Tarasov on 12.10.2016.
 */

import model.Material;
import com.interactivemesh.jfx.importer.col.ColModelImporter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.Double.parseDouble;

public class OneBodyRotationAnimation {

    final static Group root = new Group();
    final static Group axisGroup = new Group();
    final static StackPane axisLVLH = new StackPane();
    final static StackPane stack = new StackPane();
    final static Form world = new Form();
    final static PerspectiveCamera camera = new PerspectiveCamera(true);
    final static Form cameraForm = new Form();
    final static Form cameraForm2 = new Form();
    final static Form cameraForm3 = new Form();
    final static double cameraDistance = 1000;
    final static Form spaceGroup = new Form();
    static double mousePosX;
    static double mousePosY;
    static double mouseOldX;
    static double mouseOldY;
    static double mouseDeltaX;
    static double mouseDeltaY;
    private static Integer j = 0;
    static List<String> list;
    static List<Double> qi = new ArrayList<>();
    static List<Double> qj = new ArrayList<>();
    static List<Double> qk = new ArrayList<>();
    static List<Double> ql = new ArrayList<>();
    static List<Double> x = new ArrayList<>();
    static List<Double> y = new ArrayList<>();
    static List<Double> z = new ArrayList<>();
    static List<Double> vx = new ArrayList<>();
    static List<Double> vy = new ArrayList<>();
    static List<Double> vz = new ArrayList<>();


    private static void buildScene() {
        root.getChildren().add(world);
    }

    private static void buildCamera() {
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

    private static void buildAxes() {
        Material redMaterial = new Material(Color.RED);
        Material greenMaterial = new Material(Color.GREEN);
        Material blueMaterial = new Material(Color.BLUE);
        Material darkMaterial = new Material(Color.BLACK);

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

        xLabel1.setMaterial(darkMaterial);
        xLabel2.setMaterial(darkMaterial);
        yLabel1.setMaterial(darkMaterial);
        yLabel2.setMaterial(darkMaterial);
        zLabel1.setMaterial(darkMaterial);
        zLabel2.setMaterial(darkMaterial);
        zLabel3.setMaterial(darkMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis, xLabel1, xLabel2, yLabel1, yLabel2, zLabel1, zLabel2, zLabel3);
//        axisLVLH.getChildren().addAll(xLabel1, xLabel2, yLabel1, yLabel2, zLabel1, zLabel2, zLabel3);
        world.getChildren().add(axisGroup);
    }

    private static void buildSpace(Path path) {

        Material whiteMaterial = new Material(Color.WHITE);
        Material greyMaterial = new Material(Color.GREY);

//        Form spaceForm = new Form();

        // TODO CYLINDER ADDED CHECK
        ColModelImporter tds = new ColModelImporter();
        tds.read("C:/Users/Labcomp-1/Downloads/rocketModels/NormStageLadee.dae");
        Node[] cylinderImport = tds.getImport();
        tds.close();
        Group cylinder = new Group(cylinderImport);
        cylinder.setRotationAxis(Rotate.Z_AXIS);
        cylinder.setRotate(90);


//        Cylinder cylinder = new Cylinder(50.0, 200.0);
//        cylinder.setMaterial(whiteMaterial);
////        cylinder.setRotationAxis(Rotate.X_AXIS);
////        cylinder.setRotate(90);
////        cylinder.setRotationAxis(Rotate.Y_AXIS);
////        cylinder.setRotate(90);
//        cylinder.setRotationAxis(Rotate.Z_AXIS);
//        cylinder.setRotate(90);

        final Box pivotX = new Box(300, 1, 1);
        final Box pivotY = new Box(1, 300, 1);
        final Box pivotZ = new Box(1, 1, 300);
//        pivotY.setMaterial(greyMaterial);
        pivotZ.setMaterial(greyMaterial);

//        spaceGroup.getChildren().add(spaceForm);

        final StackPane stack = new StackPane();
        final StackPane pivotStack = new StackPane();
//        stack.getChildren().addAll(cylinder, pivotX);
//        stack.getChildren().addAll(cylinder, pivotY);
//        stack.getChildren().addAll(cylinder, pivotZ);
        stack.getChildren().addAll(cylinder);
        pivotStack.getChildren().addAll(pivotX);

        Material redMaterial = new Material(Color.RED);
        Material greenMaterial = new Material(Color.GREEN);
        Material blueMaterial = new Material(Color.BLUE);

        final Box LVLHX = new Box(360, 1, 1);
        final Box LVLHY = new Box(1, 360, 1);
        final Box LVLHZ = new Box(1, 1, 360);
        LVLHX.setMaterial(redMaterial);
        LVLHY.setMaterial(greenMaterial);
        LVLHZ.setMaterial(blueMaterial);
        axisLVLH.getChildren().addAll(LVLHX, LVLHY, LVLHZ);

        spaceGroup.getChildren().add(stack);
        spaceGroup.getChildren().add(pivotStack);

        axisLVLH.setTranslateX(-180);
        axisLVLH.setTranslateY(-180);
//        spaceGroup.getChildren().add(axisLVLH);

        world.getChildren().addAll(spaceGroup);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        // TRANSLATE FOR ROCKET STAGE
        stack.setTranslateX(-19);
        stack.setTranslateY(-32);
        stack.setScaleX(2.5);
        stack.setScaleY(2.5);
        stack.setScaleZ(2.5);
        // FOR PIVOT
        pivotStack.setTranslateX(-150);

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
        EventHandler<ActionEvent> onFinished = t -> {
//            qx = 1;
            double qw = qi.get(j);
            double qx = qj.get(j);
            double qy = qk.get(j);
            double qz = ql.get(j);

            Quaternion first = new Quaternion(qw, qx, qy, qz);
            first = Quaternion.normalize(first);
            double[] rECI = new double[]{x.get(j), y.get(j), z.get(j)};
            double[] vECI = new double[]{vx.get(j), vy.get(j), vz.get(j)};

            double[] zNew = VectorAlgebra.invert(VectorAlgebra.normalize(rECI));
            double[] yNew = VectorAlgebra.normalize(VectorAlgebra.multV(zNew, vECI));
            double[] xNew = VectorAlgebra.multV(yNew, zNew);

            Quaternion second = Quaternion.fromRotationMatrix(xNew[0], xNew[1], xNew[2], yNew[0],
                    yNew[1], yNew[2], zNew[0], zNew[1], zNew[2]);
            second = Quaternion.conjugate(second);
            second = Quaternion.normalize(second);

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
            pivotStack.setRotationAxis(p);
            pivotStack.setRotate(Math.toDegrees(angle));

            j++;

//            System.out.println(stack.getChildren());
//            if (qx == qj.size()) {
//                qx = 0;
//            }
        };

        KeyFrame keyFrame = new KeyFrame(duration, onFinished);
        timeline.getKeyFrames().add(keyFrame);
        //timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private static void handleMouse(Scene scene) {
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

    public static void startAnimation(Stage primaryStage, Path path) {
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
}
