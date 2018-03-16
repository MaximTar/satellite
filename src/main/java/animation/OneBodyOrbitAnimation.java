package animation;

import com.interactivemesh.jfx.importer.col.ColModelImporter;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.chart.Axis;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.Form;
import model.Material;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

/**
 * Created by Maxim Tarasov on 06.12.2016.
 */
public class OneBodyOrbitAnimation {

    final static Group root = new Group();
    final static Group axisGroup = new Group();
    final static Form world = new Form();
    final static PerspectiveCamera camera = new PerspectiveCamera(true);
    final static Form cameraForm = new Form();
    final static Form cameraForm2 = new Form();
    final static Form cameraForm3 = new Form();
    final static double cameraDistance = 100000;
    final static Form spaceGroup = new Form();
    static double mousePosX;
    static double mousePosY;
    static double mouseOldX;
    static double mouseOldY;
    static double mouseDeltaX;
    static double mouseDeltaY;
    static private int j = 0;
    static List<String> list;
    static List<Double> x = new ArrayList<>();
    static List<Double> y = new ArrayList<>();
    static List<Double> z = new ArrayList<>();

    private static final double EARTH_RADIUS = 6370;
    private static final double VIEWPORT_SIZE = 800;
    private static final double ROTATE_SECS = 30;

    //    private static final double MAP_WIDTH  = 8192 / 2d;
    private static final double MAP_WIDTH = 1024 / 2d;
    //    private static final double MAP_HEIGHT = 4092 / 2d;
    private static final double MAP_HEIGHT = 512 / 2d;

    private static final String DIFFUSE_MAP =
//            "http://planetmaker.wthr.us/img/earth_gebco8_texture_8192x4096.jpg";
//            "http://planetmaker.wthr.us/img/earth_gebco8_texture_1024x512.jpg";
            "file:C:/Users/Labcomp-1/Desktop/texture_earth_clouds.jpg";
    private static final String NORMAL_MAP =
//            "http://planetmaker.wthr.us/img/earth_normalmap_flat_8192x4096.jpg";
            "http://planetmaker.wthr.us/img/earth_normalmap_flat_1024x512.jpg";
    private static final String SPECULAR_MAP =
//            "http://planetmaker.wthr.us/img/earth_specularmap_flat_8192x4096.jpg";
            "http://planetmaker.wthr.us/img/earth_specularmap_flat_1024x512.jpg";

    // SHITCODE FOR SSO PRECESSION
    static int counter = 0; // counter + 56

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
        camera.setFarClip(1E8);
        camera.setTranslateZ(-cameraDistance);
        cameraForm.ry.setAngle(320.0);
        cameraForm.rx.setAngle(40);
    }

    private static void buildAxes() {
        Material redMaterial = new Material(Color.RED);
        Material greenMaterial = new Material(Color.GREEN);
        Material blueMaterial = new Material(Color.BLUE);

        Material darkMaterial = new Material(Color.BLACK);

        final Box xAxis = new Box(37240.0, 100, 100);
        final Box yAxis = new Box(100, 37240.0, 100);
        final Box zAxis = new Box(100, 100, 37240.0);

        final Box xLabel1 = new Box(1000, 100, 100);
        xLabel1.setTranslateX(19500);
        xLabel1.setRotate(45);
        final Box xLabel2 = new Box(1000, 100, 100);
        xLabel2.setTranslateX(19500);
        xLabel2.setRotate(-45);

        final Box yLabel1 = new Box(1200, 100, 100);
        yLabel1.setTranslateY(19400);
        yLabel1.setTranslateX(-80);
        yLabel1.setRotate(-55);
        final Box yLabel2 = new Box(600, 100, 100);
        yLabel2.setTranslateY(19600);
        yLabel2.setTranslateX(80);
        yLabel2.setRotate(55);

        final Box zLabel1 = new Box(900, 100, 100);
        zLabel1.setTranslateZ(19500);
        final Box zLabel2 = new Box(900, 100, 100);
        zLabel2.setTranslateZ(19500);
        zLabel2.setTranslateY(800);
        final Box zLabel3 = new Box(1100, 100, 100);
        zLabel3.setTranslateZ(19500);
        zLabel3.setTranslateY(400);
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
        world.getChildren().add(axisGroup);
    }

    private static void buildSpace(Path path) {
        // TODO FIX EARTH AND DELETE IT
//        Material greenMaterial = new Material(Color.GREEN);

        Material whiteMaterial = new Material(Color.WHITE);
        Material greyMaterial = new Material(Color.GREY);

        Form spaceForm = new Form();
        Form earthForm = new Form();

        Sphere earthSphere = new Sphere(EARTH_RADIUS);
//        earthSphere.setTranslateX(VIEWPORT_SIZE / 2d);
//        earthSphere.setTranslateY(VIEWPORT_SIZE / 2d);

        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(
                new Image(
                        DIFFUSE_MAP,
                        MAP_WIDTH,
                        MAP_HEIGHT,
                        true,
                        true
                )
        );
        earthMaterial.setBumpMap(
                new Image(
                        NORMAL_MAP,
                        MAP_WIDTH,
                        MAP_HEIGHT,
                        true,
                        true
                )
        );
        earthMaterial.setSpecularMap(
                new Image(
                        SPECULAR_MAP,
                        MAP_WIDTH,
                        MAP_HEIGHT,
                        true,
                        true
                )
        );

        earthSphere.setMaterial(
                earthMaterial
        );

        // TODO WTF WITH TEXTURES
        earthSphere.getTransforms().add(new Rotate(270, Rotate.X_AXIS));
        earthSphere.getTransforms().add(new Rotate(270, Rotate.Y_AXIS));

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
            String[] partsOrigin = list.get(j).split("\\t\\t\\t");
            Point3D origin = new Point3D(Double.parseDouble(partsOrigin[0]) / 1000, Double.parseDouble(partsOrigin[1]) / 1000,
                    Double.parseDouble(partsOrigin[2]) / 1000);
            j++;
            String[] partsTarget = list.get(j).split("\\t\\t\\t");
            Point3D target = new Point3D(Double.parseDouble(partsTarget[0]) / 1000, Double.parseDouble(partsTarget[1]) / 1000,
                    Double.parseDouble(partsTarget[2]) / 1000);
            Cylinder line = createConnection(origin, target);
            line.setMaterial(greyMaterial);
            // SHITCODE FOR 5IN1 TEXTFILE
//            if (j == 17300 || j == 17301 || j == 17302 || j == 17303 || j == 34601 || j == 34602 || j == 34603 ||
//                    j == 34604 || j == 52603 || j == 52604 || j == 52605 || j == 52606 || j == 52607 || j == 52608 ||
//                    j == 104604 || j == 104605 || j == 104606 || j == 104607 || j == 104608 || j == 104609) {
//
//            }
//            else {
            world.getChildren().add(line);
        }

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);


        //Path path = Paths.get("C:", "Users", "Labcomp-1", "IdeaProjects", "hello", "hello", "04-02-2016 04-26.txt");
//        try {
//            list = Files.readAllLines(path);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }

        for (int j = 0; j < list.size() - 1; j++) {
            String[] parts = list.get(j).split("\\t\\t\\t");
            x.add(j, parseDouble(parts[0]));
            y.add(j, parseDouble(parts[1]));
            z.add(j, parseDouble(parts[2]));
        }

        Duration duration = Duration.millis(2);
        EventHandler<ActionEvent> onFinished = t -> {
//                qw = 0;
            stack.setTranslateX(x.get(j) / 1000);
            stack.setTranslateY(y.get(j) / 1000);
            stack.setTranslateZ(z.get(j) / 1000);
            j++;
            if (j == x.size()) {
                j = 0;
            }

            // SHITCODE FOR SSO PRECESSION
//            String[] partsOrigin = list.get(j).split("\\t\\t\\t");
//            Point3D origin = new Point3D(Double.parseDouble(partsOrigin[0]) / 1000, Double.parseDouble(partsOrigin[1]) / 1000,
//                    Double.parseDouble(partsOrigin[2]) / 1000);
//            String[] partsTarget = list.get(j).split("\\t\\t\\t");
//            Point3D target = new Point3D(Double.parseDouble(partsTarget[0]) / 1000, Double.parseDouble(partsTarget[1]) / 1000,
//                    Double.parseDouble(partsTarget[2]) / 1000);
//            Cylinder line = createConnection(origin, target);
//            line.setMaterial(greyMaterial);
//            world.getChildren().add(line);
//            j++;
//            }
        };
        KeyFrame keyFrame = new KeyFrame(duration, onFinished);
        timeline.getKeyFrames().add(keyFrame);
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

    public static Cylinder createConnection(Point3D origin, Point3D target) {
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
