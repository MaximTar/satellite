package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Maxim Tarasov on 19.09.2016.
 * <p>
 * ECEF - Earth Centered Earth Fixed
 * <p>
 * LLA - Lat Lon Alt
 * <p>
 * ported from matlab code at
 * https://gist.github.com/1536054
 * and
 * https://gist.github.com/1536056
 */
public class ECEF2LLA {

    public static List<Double> conversion(double x, double y, double z) {

        // WGS84 ellipsoid constants
        final double a = 6378137; // radius
        final double e = 8.1819190842622e-2;  // eccentricity

        final double asq = Math.pow(a, 2);
        final double esq = Math.pow(e, 2);
        ArrayList<Double> result = new ArrayList<>();

        double b = Math.sqrt(asq * (1 - esq));
        double bsq = Math.pow(b, 2);
        double ep = Math.sqrt((asq - bsq) / bsq);
        double p = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
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
}
