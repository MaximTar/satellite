package main.java.utils;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * Created by Labcomp-1 on 24.03.2016.
 */
public class Material extends PhongMaterial {

    public Material(Color diffuseColor) {
        setDiffuseColor(diffuseColor);
        setSpecularColor(diffuseColor);
    }
}
