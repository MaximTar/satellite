package side;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
// todo check if unused
public class Material extends PhongMaterial {

    public Material(Color diffuseColor) {
        setDiffuseColor(diffuseColor);
        setSpecularColor(diffuseColor);
    }
}
