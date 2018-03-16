package utils;

/**
 * Created by Maxim Tarasov on 31.08.2017.
 *
 */
public class BooleansForIntegration {

    boolean geopotential, sunGravity, moonGravity, sunPressure, drag;

    public BooleansForIntegration(boolean geopotential, boolean sunGravity, boolean moonGravity, boolean sunPressure, boolean drag) {
        this.geopotential = geopotential;
        this.sunGravity = sunGravity;
        this.moonGravity = moonGravity;
        this.sunPressure = sunPressure;
        this.drag = drag;
    }
}
