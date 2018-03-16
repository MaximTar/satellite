package utils;

/**
 * Created by Maxim Tarasov on 14.10.2017.
 */
public class NativeNRLMSISE {

    static {
        System.loadLibrary("NRLMSISELib");
    }

    native public static double densityNow(double ECIx, double ECIy, double ECIz, int doy, int year, int sec);
}
