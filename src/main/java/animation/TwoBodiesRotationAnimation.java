package animation;
/**
 * Created by Maxim Tarasov on 07.12.2016.
 */

import model.Material;
import com.interactivemesh.jfx.importer.col.ColModelImporter;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.Double.parseDouble;

public class TwoBodiesRotationAnimation {

    final static Group root = new Group();
    final static Group axisGroup = new Group();
    final static StackPane axisLVLH = new StackPane();
    final static StackPane stackLine = new StackPane();
    final static Form world = new Form();
    final static PerspectiveCamera camera = new PerspectiveCamera(true);
    final static Form cameraForm = new Form();
    final static Form cameraForm2 = new Form();
    final static Form cameraForm3 = new Form();
    final static double cameraDistance = 2000;
    final static Form spaceGroup = new Form();
    static double mousePosX;
    static double mousePosY;
    static double mouseOldX;
    static double mouseOldY;
    static double mouseDeltaX;
    static double mouseDeltaY;
    private static Integer j = 0;
    static List<String> list;
    static List<Double> q1i = new ArrayList<>();
    static List<Double> q1j = new ArrayList<>();
    static List<Double> q1k = new ArrayList<>();
    static List<Double> q1l = new ArrayList<>();
    static List<Double> w1x = new ArrayList<>();
    static List<Double> w1y = new ArrayList<>();
    static List<Double> w1z = new ArrayList<>();
    static List<Double> x1 = new ArrayList<>();
    static List<Double> y1 = new ArrayList<>();
    static List<Double> z1 = new ArrayList<>();
    static List<Double> v1x = new ArrayList<>();
    static List<Double> v1y = new ArrayList<>();
    static List<Double> v1z = new ArrayList<>();
    static List<Double> q2i = new ArrayList<>();
    static List<Double> q2j = new ArrayList<>();
    static List<Double> q2k = new ArrayList<>();
    static List<Double> q2l = new ArrayList<>();
    static List<Double> w2x = new ArrayList<>();
    static List<Double> w2y = new ArrayList<>();
    static List<Double> w2z = new ArrayList<>();
    static List<Double> x2 = new ArrayList<>();
    static List<Double> y2 = new ArrayList<>();
    static List<Double> z2 = new ArrayList<>();
    static List<Double> v2x = new ArrayList<>();
    static List<Double> v2y = new ArrayList<>();
    static List<Double> v2z = new ArrayList<>();
    static double initStateSecondX = 5;
//    static double initStateSecondX = -25;
    static double initStateSecondY = 0;
//    static double initStateSecondY = -50;



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
        // !!!
//        world.getChildren().add(axisGroup);
    }

    private static void buildSpace(Path path) {

        Material whiteMaterial = new Material(Color.WHITE);
        Material greyMaterial = new Material(Color.GREY);

        Form spaceForm = new Form();

//        Cylinder cylinder = new Cylinder(25.0, 100.0);
        TdsModelImporter tds = new TdsModelImporter();
        tds.read("C:/Users/Labcomp-1/Downloads/rocketModels/badWingsButWorks/Rocket.3ds");
        Node[] cylinder2 = tds.getImport();
        tds.close();
        Group cylinder = new Group(cylinder2);

//        cylinder.setMaterial(whiteMaterial);
//        cylinder.setRotationAxis(Rotate.X_AXIS);
//        cylinder.setRotate(90);
//        cylinder.setRotationAxis(Rotate.Y_AXIS);
//        cylinder.setRotate(90);
        cylinder.setRotationAxis(Rotate.Z_AXIS);
        cylinder.setRotate(-90);
        cylinder.setTranslateY(-0.4);
        cylinder.setTranslateZ(-0.5);

//        Cylinder cylinderSecondary = new Cylinder(25.0, 100.0);
//        TdsModelImporter tds2 = new TdsModelImporter();
//        tds2.read("C:/Users/Labcomp-1/Downloads/rocketModels/agenaNormStage/agena_carbajal.3ds");
        ColModelImporter tds2 = new ColModelImporter();
        tds2.read("C:/Users/Labcomp-1/Downloads/rocketModels/NormStageLadee.dae");
        Node[] cylinder3 = tds2.getImport();
        tds2.close();
        Group cylinderSecondary = new Group(cylinder3);
//        cylinderSecondary.setMaterial(whiteMaterial);
        cylinderSecondary.setRotationAxis(Rotate.Z_AXIS);
        cylinderSecondary.setRotate(50);
        cylinderSecondary.setRotationAxis(Rotate.X_AXIS);
        cylinderSecondary.setRotate(30);
        cylinderSecondary.setTranslateY(-4);

//        StlMeshImporter tds3 = new StlMeshImporter();
//        tds3.read("C:/Users/Labcomp-1/Downloads/rocketModels/rope/rope.stl");
//        Mesh cylinder4 = tds3.getImport();
//        tds3.close();
//        Group cylinderThird = new Group(new MeshView(cylinder4));
//        cylinderSecondary.setMaterial(whiteMaterial);
//        cylinderSecondary.setRotationAxis(Rotate.Y_AXIS);
//        cylinderSecondary.setRotate(90);
//        cylinderSecondary.setTranslateY(-4);

        // TODO Костыль для конфы
//        final Box line = new Box(2, 2, 200);
        Cylinder line = new Cylinder(1, 200);
        line.setRotationAxis(Rotate.Z_AXIS);
        line.setRotate(90);
        line.setTranslateX(100);
        line.setTranslateY(-100);
        line.setMaterial(new Material(Color.YELLOW));
        stackLine.getChildren().addAll(line);
        spaceGroup.getChildren().add(stackLine);

//        final Box pivotY = new Box(1, 300, 1);
        final Box pivotZ = new Box(1, 1, 300);
//        pivotY.setMaterial(greyMaterial);
        pivotZ.setMaterial(greyMaterial);

        spaceGroup.getChildren().add(spaceForm);

        final StackPane stack = new StackPane();
        final StackPane stackSecondary = new StackPane();
        final StackPane stackThird = new StackPane();
//        stack.getChildren().addAll(cylinder, pivotY);
//        stack.getChildren().addAll(cylinder, pivotZ);
        stack.getChildren().addAll(cylinder);
        stackSecondary.getChildren().addAll(cylinderSecondary);
//        stackThird.getChildren().addAll(cylinderThird);

        Material redMaterial = new Material(Color.RED);
        Material greenMaterial = new Material(Color.GREEN);
        Material blueMaterial = new Material(Color.BLUE);
        final Box LVLHX = new Box(3600, 1, 1);
        final Box LVLHY = new Box(1, 3600, 1);
        final Box LVLHZ = new Box(1, 1, 3600);
//        final Box LVLHline = new Box(4, 4, 210);
        Cylinder LVLHline = new Cylinder(2, 210);
        LVLHline.setTranslateZ(-100);
        LVLHline.setRotationAxis(Rotate.X_AXIS);
        LVLHline.setRotate(90);
        LVLHline.setMaterial(new Material(Color.YELLOW));
        LVLHX.setMaterial(redMaterial);
        LVLHY.setMaterial(greenMaterial);
        LVLHZ.setMaterial(blueMaterial);
        axisLVLH.getChildren().addAll(LVLHX, LVLHY, LVLHZ, LVLHline);

        spaceGroup.getChildren().add(stack);
        spaceGroup.getChildren().add(stackSecondary);
        spaceGroup.getChildren().add(stackThird);

        axisLVLH.setTranslateX(-1800);
        axisLVLH.setTranslateY(-1800);
        // !!!
//        spaceGroup.getChildren().add(axisLVLH);

        world.getChildren().addAll(spaceGroup);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

//        stack.setTranslateX(-25);
//        stack.setTranslateY(-50);
        stack.setScaleX(10);
        stack.setScaleY(10);
        stack.setScaleZ(10);
        stackSecondary.setScaleX(2);
        stackSecondary.setScaleY(3);
        stackSecondary.setScaleZ(2);
        stackThird.setScaleX(10);
        stackThird.setScaleY(10);
        stackThird.setScaleZ(10);
        stackThird.setTranslateX(108);
        stackThird.setTranslateY(-21);
        stackThird.setTranslateZ(-4);
        stackThird.setRotationAxis(Rotate.X_AXIS);
        stackThird.setRotate(45);
        stackSecondary.setTranslateX(initStateSecondX);
        stackSecondary.setTranslateY(initStateSecondY);
        // For pivotY
//        stack.setTranslateY(-150);

        try {
            list = Files.readAllLines(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        for (int j = 0; j < list.size() - 1; j++) {
            String[] parts = list.get(j).split("\\t\\t\\t");
            q1i.add(j, parseDouble(parts[0]));
            q1j.add(j, parseDouble(parts[1]));
            q1k.add(j, parseDouble(parts[2]));
            q1l.add(j, parseDouble(parts[3]));
            x1.add(j, parseDouble(parts[7]));
            y1.add(j, parseDouble(parts[8]));
            z1.add(j, parseDouble(parts[9]));
            v1x.add(j, parseDouble(parts[10]));
            v1y.add(j, parseDouble(parts[11]));
            v1z.add(j, parseDouble(parts[12]));
            q2i.add(j, parseDouble(parts[13]));
            q2j.add(j, parseDouble(parts[14]));
            q2k.add(j, parseDouble(parts[15]));
            q2l.add(j, parseDouble(parts[16]));
            x2.add(j, parseDouble(parts[20]));
            y2.add(j, parseDouble(parts[21]));
            z2.add(j, parseDouble(parts[22]));
            v2x.add(j, parseDouble(parts[23]));
            v2y.add(j, parseDouble(parts[24]));
            v2z.add(j, parseDouble(parts[25]));
        }

        j++;

        Duration duration = Duration.millis(5);
        EventHandler onFinished = t -> {
            j = 1;
            double q1w = q1i.get(j);
            double q1x = q1j.get(j);
            double q1y = q1k.get(j);
            double q1z = q1l.get(j);

            double q2w = q2i.get(j);
            double q2x = q2j.get(j);
            double q2y = q2k.get(j);
            double q2z = q2l.get(j);

            Quaternion first1 = new Quaternion(q1w, q1x, q1y, q1z);
            first1 = Quaternion.normalize(first1);
            double[] r1ECI = new double[]{x1.get(j), y1.get(j), z1.get(j)};
            double[] v1ECI = new double[]{v1x.get(j), v1y.get(j), v1z.get(j)};

            double[] z1New = VectorAlgebra.invert(VectorAlgebra.normalize(r1ECI));
            double[] y1New = VectorAlgebra.normalize(VectorAlgebra.multV(z1New, v1ECI));
            double[] x1New = VectorAlgebra.multV(y1New, z1New);

            Quaternion second1 = Quaternion.fromRotationMatrix(x1New[0], x1New[1], x1New[2], y1New[0],
                    y1New[1], y1New[2], z1New[0], z1New[1], z1New[2]);
            second1 = Quaternion.conjugate(second1);
            second1 = Quaternion.normalize(second1);

            double qwa = second1.qw;
            double qxa = second1.qx;
            double qya = second1.qy;
            double qza = second1.qz;

            double mod = Math.sqrt(qxa * qxa + qya * qya + qza * qza);
            Point3D pAxis = new Point3D(qxa / mod, qya / mod, qza / mod);
            double angleAxis = 2 * Math.acos(qwa);
//            System.out.println("!");
//            System.out.println(pAxis);
//            System.out.println(angleAxis);
            axisLVLH.setRotationAxis(pAxis);
//            stackLine.setTranslateX(100);
//            stackLine.setRotationAxis(Rotate.Y_AXIS);
//            stackLine.setRotate(90);
//            stackLine.setRotationAxis(pAxis);
//            stackLine.setRotate(Math.toDegrees(angleAxis));
            axisLVLH.setRotate(Math.toDegrees(angleAxis));

            double abs1 = Math.sqrt(q1x * q1x + q1y * q1y + q1z * q1z);
            Point3D p1 = new Point3D(q1x / abs1, q1y / abs1, q1z / abs1);
            double angle1 = 2 * Math.acos(q1w);
            stack.setRotationAxis(p1);
            stack.setRotate(Math.toDegrees(angle1));

            double dx = x2.get(j) - x1.get(j);
            double dy = y2.get(j) - y1.get(j);
            double dz = z2.get(j) - z1.get(j);

//            System.out.println("!!!");
//            System.out.println(y1.get(qx));
//            System.out.println(y2.get(qx));
//            System.out.println(dy);
//            System.out.println(z1.get(qx));
//            System.out.println(z2.get(qx));
//            System.out.println(dz);
//            System.out.println('!');
//            System.out.println(dx);
//            System.out.println(dy);
//            System.out.println(dz);

            double abs2 = Math.sqrt(q2x * q2x + q2y * q2y + q2z * q2z);
//            Point3D p2 = new Point3D(q2x / abs2, q2y / abs2, q2z / abs2);
            double angle2 = 2 * Math.acos(q2w);
            stackSecondary.setTranslateX(dx + initStateSecondX);
            stackSecondary.setTranslateY(dy + initStateSecondY);
            stackSecondary.setTranslateZ(dz);
//            stackSecondary.setRotationAxis(p2);
            stackSecondary.setRotationAxis(p1);
            stackSecondary.setRotate(Math.toDegrees(angle2));

            j++;
            if (j == q1i.size()) {
                j = 0;
            }
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
