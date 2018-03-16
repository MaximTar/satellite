package utils;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.errors.OrekitException;
import org.orekit.forces.drag.atmosphere.NRLMSISE00;
import org.orekit.forces.drag.atmosphere.NRLMSISE00InputParameters;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.DateComponents;
import org.orekit.time.TimeComponents;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinatesProvider;

import java.io.File;

/**
 * Created by Maxim Tarasov on 13.10.2017.
 */
public class Orekit {

    static {
        File orekitData = new File("C:/Users/Labcomp-1/orekit-data");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        try {
            manager.addProvider(new DirectoryCrawler(orekitData));
        } catch (OrekitException e) {
            e.printStackTrace();
        }
    }

    public static double densityNow(double lat, double lon, double alt_in_m, int doy, int year, int sec) throws OrekitException {

        NRLMSISE00InputParameters ip = new NRLMSISE00InputParameters() {
            @Override
            public AbsoluteDate getMinDate() throws OrekitException {
                return new AbsoluteDate(2017, 10, 1, TimeScalesFactory.getUTC());
            }

            @Override
            public AbsoluteDate getMaxDate() throws OrekitException {
                return new AbsoluteDate(2017, 11, 1, TimeScalesFactory.getUTC());
            }

            @Override
            public double getDailyFlux(AbsoluteDate date) throws OrekitException {
                return 150.;
            }

            @Override
            public double getAverageFlux(AbsoluteDate date) throws OrekitException {
                return 150.;
            }

            @Override
            public double[] getAp(AbsoluteDate date) throws OrekitException {
                return new double[]{4., 0., 0., 0., 0., 0., 0.};
//                return new double[]{4., 100., 100., 100., 100., 100., 100.};
            }
        };

        PVCoordinatesProvider sun = CelestialBodyFactory.getSun();
        Frame itrf = FramesFactory.getITRF(IERSConventions.IERS_2010, true);
        
        OneAxisEllipsoid earth = new OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
                Constants.WGS84_EARTH_FLATTENING, itrf);
        final AbsoluteDate date = new AbsoluteDate(new DateComponents(year, doy),
                new TimeComponents(sec),
                TimeScalesFactory.getUT1(IERSConventions.IERS_2010, true));
        // Build the position
        final GeodeticPoint point = new GeodeticPoint(FastMath.toRadians(lat),
                FastMath.toRadians(lon),
                alt_in_m);
        final Vector3D pos = earth.transform(point);

        NRLMSISE00 atm = new NRLMSISE00(ip, sun, earth);

        return atm.getDensity(date, pos, itrf);

//    	/* input values */
//        for (int i = 0; i < 7; i++)
//            aph.a[i] = 0;
//        aph.a[0] = 4;
//        flags.switches[0] = 0;
//        for (int i = 1; i < 24; i++)
//            flags.switches[i] = 1;
//
////        Calendar c = new GregorianCalendar();
//
//        ArrayList<ArrayList<Double>> help = new ArrayList<>();
//        ArrayList<ArrayList<Double>> ECEFCoordinates;
//        double[] LLACoordinates;
//        ArrayList<Double> ECICoordinates = new ArrayList<>();
//        Collections.addAll(ECICoordinates, ECIx, ECIy, ECIz);
//        help.add(ECICoordinates);
//
//        if (precession == null) {
//            precession = ECEF_ECI_CONVERSION.precessionMatrix(c);
//            nutation = ECEF_ECI_CONVERSION.nutationMatrix(c);
//            pole = ECEF_ECI_CONVERSION.poleMatrix(c);
//        }
//        earthRotation = ECEF_ECI_CONVERSION.rotationMatrix(c);
//
//        ArrayList<ArrayList<Double>> cosine = matrixMult(matrixMult(matrixMult(pole, earthRotation), nutation), precession);
//        ECEFCoordinates = matrixMult(cosine, transpose(help));
//        LLACoordinates = ECEF2LLA.conversion(ECEFCoordinates.get(0).get(0), ECEFCoordinates.get(1).get(0), ECEFCoordinates.get(2).get(0));
//
//        input.doy = c.get(GregorianCalendar.DAY_OF_YEAR);
//        input.year = c.get(GregorianCalendar.YEAR); /* without effect */
//        input.sec = c.get(GregorianCalendar.HOUR_OF_DAY) * 3600 + c.get(GregorianCalendar.MINUTE) * 60 + c.get(GregorianCalendar.SECOND);
//        input.alt = LLACoordinates[2] / 1000;
////        System.out.println("ALT = " + input.alt);
//        input.g_lat = LLACoordinates[0];
//        input.g_long = LLACoordinates[1];
//        input.lst = input.sec / 3600 + input.g_long / 15;
//        input.f107A = 150;
//        input.f107 = 150;
//        input.ap = 4;
////        input.ap = this.ap_opt;
//        input.ap_a = aph;
//
//        gtd7(input, flags, output);
//
//        return output.d[5] * 1000; // g/cm^3 -> kg/m^3
    }

}
