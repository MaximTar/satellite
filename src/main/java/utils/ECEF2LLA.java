package utils;

import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Maxim Tarasov on 19.09.2016.
 * ECEF - Earth Centered Earth Fixed
 * LLA - Latitude Longitude Altitude
 * ported from matlab code at
 * https://gist.github.com/1536054
 * and
 * https://gist.github.com/1536056
 */
public class ECEF2LLA {

    // WGS84 ellipsoid constants
    static final double a = 6378137; // radius
    static final double e = 8.1819190842622E-2; // eccentricity

    // TO NOT INIT MANY TIMES
    static final double asq = Math.pow(a, 2);
    static final double esq = Math.pow(e, 2);
    static final double b = Math.sqrt(asq * (1 - esq));
    static final double bsq = Math.pow(b, 2);
    static final double ep = Math.sqrt((asq - bsq) / bsq);

    public static List<Double> conversionList(double x, double y, double z) {
        ArrayList<Double> result = new ArrayList<>();

        double p = Math.sqrt(x * x + y * y);
        double th = Math.atan2(a * z, b * p);

        double lon = Math.atan2(y, x);
        double lat = Math.atan2((z + Math.pow(ep, 2) * b * Math.pow(Math.sin(th), 3)), (p - esq * a * Math.pow(Math.cos(th), 3)));
        double N = a / (Math.sqrt(1 - esq * Math.pow(Math.sin(lat), 2)));
        double alt = p / Math.cos(lat) - N;

        // mod lat to 0-2pi
        lon = lon % (2 * Math.PI);

        // correction for altitude near poles left out.

        Collections.addAll(result, NumberUtils.r2d(lat), NumberUtils.r2d(lon), alt);

        return result;
    }

    public static double[] conversion(double x, double y, double z) {
        double[] result = new double[3];

        double p = FastMath.sqrt(x * x + y * y);
        double th = FastMath.atan2(a * z, b * p);

        double sinTh = FastMath.sin(th);
        double cosTh = FastMath.cos(th);

        double lon = FastMath.atan2(y, x);
        double lat = FastMath.atan2((z + ep * ep * b * sinTh * sinTh * sinTh), (p - esq * a * cosTh * cosTh * cosTh));
        double sinLat = FastMath.sin(lat);
        double N = a / (FastMath.sqrt(1 - esq * sinLat * sinLat));
        double alt = p / FastMath.cos(lat) - N;

        // mod lat to 0-2pi
        lon = lon % (2 * FastMath.PI);

        // correction for altitude near poles left out.

        result[0] = NumberUtils.r2d(lat);
        result[1] = NumberUtils.r2d(lon);
        result[2] = alt;

        return result;
    }
}
