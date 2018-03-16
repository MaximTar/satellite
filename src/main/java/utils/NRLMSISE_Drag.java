package utils;
/**
 * Created by Maxim Tarasov on 24.09.2017.
 * TODO FIXME
 * The NRLMSISE-00 model was developed by Mike Picone, Alan Hedin, and
 * Doug Drob. They also wrote area NRLMSISE-00 distribution package in
 * FORTRAN which is available at
 * http://uap-www.nrl.navy.mil/models_web/msis/msis_home.htm
 * <p>
 * Dominik Brodowski implemented and maintains this C version. You can
 * reach him at mail@brodo.de. See the file "DOCUMENTATION" for details,
 * and check http://www.brodo.de/english/pub/nrlmsise/index.html for
 * updated releases of this package.
 */

import java.util.*;

import static utils.Matrix.matrixMult;
import static utils.Matrix.transpose;

/**
 * NRLMSISE drag model.  Translated from area code written in c, originally
 * developed by Mike Picone, Alan Hedin, and Doug Drob and implemented by
 * Dominik Brodowski.
 * <p>
 * * The NRLMSISE-00 model was developed by Mike Picone, Alan Hedin, and
 * Doug Drob. They also wrote area NRLMSISE-00 distribution package in
 * FORTRAN which is available at
 * http://uap-www.nrl.navy.mil/models_web/msis/msis_home.htm
 * <p>
 * Dominik Brodowski implemented and maintains this C version. You can
 * reach him at devel@brodo.de. See the file "DOCUMENTATION" for details,
 * and check http://www.brodo.de/english/pub/nrlmsise/index.html for
 * updated releases of this package.
 * <p>
 * Translated to Java by Richard C. Page III
 *
 * @author Richard C. Page III
 */
public class NRLMSISE_Drag { //extends AtmosphericDrag {

    public double ap_opt = 15;
    public double f107_opt = 150;

    public static ArrayList<ArrayList<Double>> precession, nutation, earthRotation, pole;

    //    private final double dgtr = Constants.deg2rad;
    private final double dgtr = Math.PI / 180;
    /**
     * Drag coefficient
     */
    protected double cd;
    /**
     * Cross sectional area [m^2]
     */
    protected double area;
    /**
     * Spacecraft mass [kg]
     */
    protected double mass;

    /* ------------------------------------------------------------------- */
    /* ------------------------------- INPUT ----------------------------- */
    /* ------------------------------------------------------------------- */

    /**
     * Switches: to turn on and off particular variations use these switches.
     * 0 is off, 1 is on, and 2 is main effects off but cross terms on.
     * <p>
     * Standard values are 0 for switch 0 and 1 for switches 1 to 23. The
     * array "switches" needs to be set accordingly by the calling program.
     * The arrays sw and swc are set internally.
     * <p>
     * switches[i]:
     * i - explanation
     * -----------------
     * 0 - output in centimeters instead of meters
     * 1 - F10.7 effect on mean
     * 2 - time independent
     * 3 - symmetrical annual
     * 4 - symmetrical semiannual
     * 5 - asymmetrical annual
     * 6 - asymmetrical semiannual
     * 7 - diurnal
     * 8 - semidiurnal
     * 9 - daily ap [when this is set to -1 (!) the pointer
     * ap_a in struct nrlmsise_input must
     * point to area struct ap_array]
     * 10 - all UT/long effects
     * 11 - longitudinal
     * 12 - UT and mixed UT/long
     * 13 - mixed AP/UT/LONG
     * 14 - terdiurnal
     * 15 - departures from diffusive equilibrium
     * 16 - all TINF var
     * 17 - all TLB var
     * 18 - all TN1 var
     * 19 - all S var
     * 20 - all TN2 var
     * 21 - all NLB var
     * 22 - all TN3 var
     * 23 - turbo scale height var
     */
    protected class struct_nrlmsise_flags {
        int switches[] = new int[24];
        double sw[] = new double[24];
        double swc[] = new double[24];

        public struct_nrlmsise_flags() {
            for (int i = 0; i < 24; i++) {
                switches[i] = 0;
                sw[i] = 0.0;
                swc[i] = 0.0;
            }
        }
    }

    /**
     * Array containing the following magnetic values:
     * 0 : daily AP
     * 1 : 3 hr AP index for current time
     * 2 : 3 hr AP index for 3 hrs before current time
     * 3 : 3 hr AP index for 6 hrs before current time
     * 4 : 3 hr AP index for 9 hrs before current time
     * 5 : Average of eight 3 hr AP indicies from 12 to 33 hrs
     * prior to current time
     * 6 : Average of eight 3 hr AP indicies from 36 to 57 hrs
     * prior to current time
     */
    protected class struct_ap_array {
        double a[] = new double[7];

        public struct_ap_array() {
            for (int i = 0; i < 7; i++) a[i] = 0.0;
        }
    }

    /**
     * NOTES ON INPUT VARIABLES:
     * UT, Local Time, and Longitude are used independently in the
     * model and are not of equal importance for every situation.
     * For the most physically realistic calculation these three
     * variables should be consistent (lst=sec/3600 + g_long/15).
     * The Equation of Time departures from the above formula
     * for apparent local time can be included if available but
     * are of minor importance.
     * <p>
     * f107 and f107A values used to generate the model correspond
     * to the 10.7 cm radio flux at the actual distance of the Earth
     * from the Sun rather than the radio flux at 1 AU. The following
     * site provides both classes of values:
     * ftp://ftp.ngdc.noaa.gov/STP/SOLAR_DATA/SOLAR_RADIO/FLUX/
     * <p>
     * f107, f107A, and ap effects are neither large nor well
     * established below 80 km and these parameters should be set to
     * 150., 150., and 4. respectively.
     */
    protected class struct_nrlmsise_input {
        int year;      /* year, currently ignored */
        int doy;       /* day of year */
        double sec;    /* seconds in day (UT) */
        double alt;    /* altitude in kilometers */
        double g_lat;  /* geodetic latitude */
        double g_long; /* geodetic longitude */
        double lst;    /* local apparent solar time (hours), see note above */
        double f107A;  /* 81 day average of F10.7 flux (centered on doy) */
        double f107;   /* daily F10.7 flux for previous day */
        double ap;     /* magnetic index(daily) */
        struct_ap_array ap_a; /* see above */

        public struct_nrlmsise_input() {
            year = 0;
            doy = 0;
            sec = 0.0;
            alt = 0.0;
            g_lat = 0.0;
            g_long = 0.0;
            lst = 0.0;
            f107A = 0.0;
            f107 = 0.0;
            ap = 0.0;
            ap_a = new struct_ap_array();
        }
    }
    /* ------------------------------------------------------------------- */
    /* ------------------------------ OUTPUT ----------------------------- */
    /* ------------------------------------------------------------------- */

    /**
     * OUTPUT VARIABLES:
     * d[0] - HE NUMBER DENSITY(CM-3)
     * d[1] - O NUMBER DENSITY(CM-3)
     * d[2] - N2 NUMBER DENSITY(CM-3)
     * d[3] - O2 NUMBER DENSITY(CM-3)
     * d[4] - AR NUMBER DENSITY(CM-3)
     * d[5] - TOTAL MASS DENSITY(GM/CM3) [includes d[8] in td7d]
     * d[6] - H NUMBER DENSITY(CM-3)
     * d[7] - N NUMBER DENSITY(CM-3)
     * d[8] - Anomalous oxygen NUMBER DENSITY(CM-3)
     * t[0] - EXOSPHERIC TEMPERATURE
     * t[1] - TEMPERATURE AT ALT
     * <p>
     * <p>
     * O, H, and N are set to zero below 72.5 km
     * <p>
     * t[0], Exospheric temperature, is set to global average for
     * altitudes below 120 km. The 120 km gradient is left at global
     * average value for altitudes below 72 km.
     * <p>
     * d[5], TOTAL MASS DENSITY, is NOT the same for subroutines GTD7
     * and GTD7D
     * <p>
     * SUBROUTINE GTD7 -- d[5] is the sum of the mass densities of the
     * species labeled by indices 0-4 and 6-7 in output variable d.
     * This includes He, O, N2, O2, Ar, H, and N but does NOT include
     * anomalous oxygen (species index 8).
     * <p>
     * SUBROUTINE GTD7D -- d[5] is the "effective total mass density
     * for drag" and is the sum of the mass densities of all species
     * in this model, INCLUDING anomalous oxygen.
     */
    protected class struct_nrlmsise_output {
        double d[] = new double[9];   /* densities */
        doublestar t[] = new doublestar[2];   /* temperatures */

        public struct_nrlmsise_output() {
            for (int i = 0; i < 9; i++) d[i] = 0;
            for (int i = 0; i < 2; i++) t[i] = new doublestar();
        }

        @Override
        public String toString() {
            return "struct_nrlmsise_output{" +
                    "d=" + Arrays.toString(d) +
                    ", t=" + Arrays.toString(t) +
                    '}';
        }
    }

    /* ------------------------------------------------------------------- */
    /* ------------------------- SHARED VARIABLES ------------------------ */
    /* ------------------------------------------------------------------- */

    /* PARMB */
    protected doublestar gsurf = new doublestar();
    protected doublestar re = new doublestar();

    /* GTS3C */
    protected double dd;

    /* DMIX */
    protected double dm04, dm16, dm28, dm32, dm40, dm01, dm14;

    /* MESO7 */
    protected double meso_tn1[] = new double[5];
    protected double meso_tn2[] = new double[4];
    protected double meso_tn3[] = new double[6];//[3];
    protected double meso_tgn1[] = new double[2];
    protected double meso_tgn2[] = new double[2];
    protected double meso_tgn3[] = new double[2];

    /* POWER7 */
    public double pt[] = NRLMSISE_data.pt;
    public double pd[][] = NRLMSISE_data.pd;
    public double ps[] = NRLMSISE_data.ps;
    public double pdl[][] = NRLMSISE_data.pdl;
    public double ptl[][] = NRLMSISE_data.ptl;
    public double pma[][] = NRLMSISE_data.pma;
    public double sam[] = NRLMSISE_data.sam;

    /* LOWER7 */
    public double ptm[] = NRLMSISE_data.ptm;
    public double pdm[][] = NRLMSISE_data.pdm;
    public double pavgm[] = NRLMSISE_data.pavgm;

    /* LPOLY */
    protected double dfa;
    protected double plg[][] = new double[4][9];
    protected double ctloc, stloc;
    protected double c2tloc, s2tloc;
    protected double s3tloc, c3tloc;
    protected double apdf;
    protected double apt[] = new double[4];

    /**
     * Inner class doublestar simulates pass by reference in c for area
     * single double value
     */
    protected static class doublestar {
        public double val = 0;

        public doublestar() {
            val = 0;
        }

        public doublestar(double x) {
            val = x;
        }

        @Override
        public String toString() {
            return "doublestar{" +
                    "val=" + val +
                    '}';
        }
    }

    /**
     * Simulates the casting of area double type in area c if statement
     *
     * @param x The double expression
     * @return The boolean value of the double (if(x==0) 'true' else 'false')
     */
    boolean bool(double x) {
        return x != 0;
    }

//    /**
//     * Constructor
//     *
//     * @param sc Spacecraft containing drag_coefficient, area, and mass
//     */
//    public NRLMSISE_Drag(Spacecraft sc) {
//        this.cd = sc.cd();
//        this.area = sc.area();
//        this.mass = sc.mass();
//    }


//    /**
//     * Constructor
//     *
//     * @param cd   Drag coefficient
//     * @param area Cross-section [m^2]
//     * @param mass Spacecraft mass [kg]
//     */
//    public NRLMSISE_Drag(double cd, double area, double mass) {
//        this.cd = cd;
//        this.area = area;
//        this.mass = mass;
//    }

    public NRLMSISE_Drag() {
    }

    //    /**
//     * Abstract function from AtmosphericDrag - computes atmospheric density
//     *
//     * @param ref EarthRef object.
//     * @param r   Position vector.
//     * @return Atmospheric density in kg/m^3
//     */
//    public double computeDensity(Time t, BodyRef ref, VectorN r) {
//        struct_nrlmsise_output output = new struct_nrlmsise_output();
//        struct_nrlmsise_input input = new struct_nrlmsise_input();
//        struct_nrlmsise_flags flags = new struct_nrlmsise_flags();
//        struct_ap_array aph = new struct_ap_array();
//
//        // Translate from J2000 to TOD
//        ReferenceFrameTranslater xlater =
//                new ReferenceFrameTranslater(ref, new EarthTrueOfDateRef(), t);
//        VectorN r_tod = xlater.translatePoint(r);
//        //* Satellite true altitude
//        jat.matvec.data.Matrix eci2ecef = ref.inertial_to_body(t);     //*debug
//        VectorN r_ecef = eci2ecef.times(r);   //*debug
//        System.out.println(r_ecef);
//        Geodetic geod = new Geodetic(r_ecef); //*debug
//        //Geodetic geod = new Geodetic(r_tod);
//        double alt = geod.getHAE() / 1000.0;     //* [km]
//        if (alt > 1000) {
//            return 0;             //* Valid from 0 to 1000 km
//        }
//        double dist2sun = ref.get_JPL_Sun_Vector(t).mag() * 1000;
//        //double f107_in = this.f107_opt*Math.Math.pow(dist2sun/Constants.AU,2);
//        //double f107_in = this.f107_opt*Math.Math.pow(Constants.AU/dist2sun,2);
//        double f107_in = this.f107_opt;
//        int i;
//        int j;
//        /* input values */
//        //for (i=0;i<7;i++)
//        //	aph.area[i]=13.853964381;//100;
//
//        flags.switches[0] = 0;
//        for (i = 1; i < 24; i++)
//            flags.switches[i] = 1;
//        input.doy = t.dayOfYear();
//        input.year = 2004; /* without effect */
//        input.sec = t.secOfDay();
//        input.alt = alt;
//        System.out.println(alt);
//        input.g_lat = geod.getLatitude() * MathUtils.RAD2DEG;
//        input.g_long = geod.getLongitude() * MathUtils.RAD2DEG;
//        input.lst = input.sec / 3600 + input.g_long / 15;
//        input.f107A = f107_in;
//        input.f107 = f107_in;
//        input.ap = this.ap_opt;//14.924291;//13.853964381; //???
//        //input.ap_a = aph;
//        /* evaluate */
//        if (alt > 500) {
//            System.out.println("alt > 500");
//            gtd7d(input, flags, output);
//        } else {
//            System.out.println("alt < 500");
//            gtd7(input, flags, output);
//        }
//        return output.d[5] * 1000; //[kg/m^3]
//    }

    /* ------------------------------------------------------------------- */
    /* ------------------------------ TSELEC ----------------------------- */
    /* ------------------------------------------------------------------- */

    protected void tselec(struct_nrlmsise_flags flags) {
        int i;
        for (i = 0; i < 24; i++) {
            if (i != 9) {
                if (flags.switches[i] == 1)
                    flags.sw[i] = 1;
                else
                    flags.sw[i] = 0;
                if (flags.switches[i] > 0)
                    flags.swc[i] = 1;
                else
                    flags.swc[i] = 0;
            } else {
                flags.sw[i] = flags.switches[i];
                flags.swc[i] = flags.switches[i];
            }
        }
    }



    /* ------------------------------------------------------------------- */
    /* ------------------------------ GLATF ------------------------------ */
    /* ------------------------------------------------------------------- */

    protected void glatf(double lat, doublestar gv, doublestar reff) {
        double c2;
        c2 = Math.cos(2.0 * dgtr * lat);
        gv.val = 980.616 * (1.0 - 0.0026373 * c2);
        reff.val = 2.0 * (gv.val) / (3.085462E-6 + 2.27E-9 * c2) * 1.0E-5;
    }



    /* ------------------------------------------------------------------- */
    /* ------------------------------ CCOR ------------------------------- */
    /* ------------------------------------------------------------------- */

    protected double ccor(double alt, double r, double h1, double zh) {
    /*        CHEMISTRY/DISSOCIATION CORRECTION FOR MSIS MODELS
     *         ALT - altitude
     *         R - target ratio
     *         H1 - transition scale length
     *         ZH - altitude of 1/2 R
     */
        double e;
        double ex;
        e = (alt - zh) / h1;
        if (e > 70)
            return Math.exp(0);
        if (e < -70)
            return Math.exp(r);
        ex = Math.exp(e);
        e = r / (1.0 + ex);
        return Math.exp(e);
    }



    /* ------------------------------------------------------------------- */
    /* ------------------------------ CCOR ------------------------------- */
    /* ------------------------------------------------------------------- */

    protected double ccor2(double alt, double r, double h1, double zh, double h2) {
    /*        CHEMISTRY/DISSOCIATION CORRECTION FOR MSIS MODELS
     *         ALT - altitude
     *         R - target ratio
     *         H1 - transition scale length
     *         ZH - altitude of 1/2 R
     *         H2 - transition scale length #2 ?
     */
        double e1, e2;
        double ex1, ex2;
        double ccor2v;
        e1 = (alt - zh) / h1;
        e2 = (alt - zh) / h2;
        if ((e1 > 70) || (e2 > 70))
            return Math.exp(0);
        if ((e1 < -70) && (e2 < -70))
            return Math.exp(r);
        ex1 = Math.exp(e1);
        ex2 = Math.exp(e2);
        ccor2v = r / (1.0 + 0.5 * (ex1 + ex2));
        return Math.exp(ccor2v);
    }



    /* ------------------------------------------------------------------- */
    /* ------------------------------- SCALH ----------------------------- */
    /* ------------------------------------------------------------------- */

    protected double scalh(double alt, double xm, double temp) {
        double g;
        double rgas = 831.4;
        g = gsurf.val / (Math.pow((1.0 + alt / re.val), 2.0));
        g = rgas * temp / (g * xm);
        return g;
    }



    /* ------------------------------------------------------------------- */
    /* -------------------------------- DNET ----------------------------- */
    /* ------------------------------------------------------------------- */

    protected double dnet(double dd, double dm, double zhm, double xmm, double xm) {
    /*       TURBOPAUSE CORRECTION FOR MSIS MODELS
     *        Root mean density
     *         DD - diffusive density
     *         DM - full mixed density
     *         ZHM - transition scale length
     *         XMM - full mixed molecular weight
     *         XM  - species molecular weight
     *         DNET - combined density
     */
        double a;
        double ylog;
        a = zhm / (xmm - xm);
        if (!((dm > 0) && (dd > 0))) {
            System.out.println("dnet log error " + dm + " " + dd + " " + xm);
            if ((dd == 0) && (dm == 0))
                dd = 1;
            if (dm == 0)
                return dd;
            if (dd == 0)
                return dm;
        }
        ylog = a * Math.log(dm / dd);
        if (ylog < -10)
            return dd;
        if (ylog > 10)
            return dm;
        a = dd * Math.pow((1.0 + Math.exp(ylog)), (1.0 / a));
        return a;
    }



    /* ------------------------------------------------------------------- */
    /* ------------------------------- SPLINI ---------------------------- */
    /* ------------------------------------------------------------------- */

    protected void splini(double[] xa, double[] ya, double[] y2a, int n, double x, doublestar y) {
    /*      INTEGRATE CUBIC SPLINE FUNCTION FROM XA(1) TO X
     *       XA,YA: ARRAYS OF TABULATED FUNCTION IN ASCENDING ORDER BY X
     *       Y2A: ARRAY OF SECOND DERIVATIVES
     *       N: SIZE OF ARRAYS XA,YA,Y2A
     *       X: ABSCISSA ENDPOINT FOR INTEGRATION
     *       Y: OUTPUT VALUE
     */
        double yi = 0;
        int klo = 0;
        int khi = 1;
        double xx, h, a, b, a2, b2;
        while ((x > xa[klo]) && (khi < n)) {
            xx = x;
            if (khi < (n - 1)) {
                if (x < xa[khi])
                    xx = x;
                else
                    xx = xa[khi];
            }
            h = xa[khi] - xa[klo];
            a = (xa[khi] - xx) / h;
            b = (xx - xa[klo]) / h;
            a2 = a * a;
            b2 = b * b;
            yi += ((1.0 - a2) * ya[klo] / 2.0 + b2 * ya[khi] / 2.0 + ((-(1.0 + a2 * a2) / 4.0 + a2 / 2.0) * y2a[klo] + (b2 * b2 / 4.0 - b2 / 2.0) * y2a[khi]) * h * h / 6.0) * h;
            klo++;
            khi++;
        }
        y.val = yi;
    }



    /* ------------------------------------------------------------------- */
    /* ------------------------------- SPLINT ---------------------------- */
    /* ------------------------------------------------------------------- */

    protected void splint(double[] xa, double[] ya, double[] y2a, int n, double x, doublestar y) {
    /*      CALCULATE CUBIC SPLINE INTERP VALUE
     *       ADAPTED FROM NUMERICAL RECIPES BY PRESS ET AL.
     *       XA,YA: ARRAYS OF TABULATED FUNCTION IN ASCENDING ORDER BY X
     *       Y2A: ARRAY OF SECOND DERIVATIVES
     *       N: SIZE OF ARRAYS XA,YA,Y2A
     *       X: ABSCISSA FOR INTERPOLATION
     *       Y: OUTPUT VALUE
     */
        int klo = 0;
        int khi = n - 1;
        int k;
        double h;
        double a, b, yi;
        while ((khi - klo) > 1) {
            k = (khi + klo) / 2;
            if (xa[k] > x)
                khi = k;
            else
                klo = k;
        }
        h = xa[khi] - xa[klo];
        if (h == 0.0)
            System.out.println("bad XA input to splint");
        a = (xa[khi] - x) / h;
        b = (x - xa[klo]) / h;
        yi = a * ya[klo] + b * ya[khi] + ((a * a * a - a) * y2a[klo] + (b * b * b - b) * y2a[khi]) * h * h / 6.0;
        y.val = yi;
    }



    /* ------------------------------------------------------------------- */
    /* ------------------------------- SPLINE ---------------------------- */
    /* ------------------------------------------------------------------- */

    protected void spline(double[] x, double[] y, int n, double yp1, double ypn, double[] y2) {
    /*       CALCULATE 2ND DERIVATIVES OF CUBIC SPLINE INTERP FUNCTION
     *       ADAPTED FROM NUMERICAL RECIPES BY PRESS ET AL
     *       X,Y: ARRAYS OF TABULATED FUNCTION IN ASCENDING ORDER BY X
     *       N: SIZE OF ARRAYS X,Y
     *       YP1,YPN: SPECIFIED DERIVATIVES AT X[0] AND X[N-1]; VALUES
     *                >= 1E30 SIGNAL SIGNAL SECOND DERIVATIVE ZERO
     *       Y2: OUTPUT ARRAY OF SECOND DERIVATIVES
     */
        double[] u;
        double sig, p, qn, un;
        int i, k;
        u = new double[n];
        if (u == null) {
            System.out.println("Out Of Memory in spline - ERROR");
            return;
        }
        if (yp1 > 0.99E30) {
            y2[0] = 0;
            u[0] = 0;
        } else {
            y2[0] = -0.5;
            u[0] = (3.0 / (x[1] - x[0])) * ((y[1] - y[0]) / (x[1] - x[0]) - yp1);
        }
        for (i = 1; i < (n - 1); i++) {
            sig = (x[i] - x[i - 1]) / (x[i + 1] - x[i - 1]);
            p = sig * y2[i - 1] + 2.0;
            y2[i] = (sig - 1.0) / p;
            u[i] = (6.0 * ((y[i + 1] - y[i]) / (x[i + 1] - x[i]) - (y[i] - y[i - 1]) / (x[i] - x[i - 1])) / (x[i + 1] - x[i - 1]) - sig * u[i - 1]) / p;
        }
        if (ypn > 0.99E30) {
            qn = 0;
            un = 0;
        } else {
            qn = 0.5;
            un = (3.0 / (x[n - 1] - x[n - 2])) * (ypn - (y[n - 1] - y[n - 2]) / (x[n - 1] - x[n - 2]));
        }
        y2[n - 1] = (un - qn * u[n - 2]) / (qn * y2[n - 2] + 1.0);
        for (k = n - 2; k >= 0; k--)
            y2[k] = y2[k] * y2[k + 1] + u[k];
    }



    /* ------------------------------------------------------------------- */
    /* ------------------------------- DENSM ----------------------------- */
    /* ------------------------------------------------------------------- */

    protected double zeta(double zz, double zl) {
        return ((zz - zl) * (re.val + zl) / (re.val + zz));
    }

    protected double densm(double alt, double d0, double xm, doublestar tz, int mn3, double[] zn3, double[] tn3, double[] tgn3, int mn2, double[] zn2, double[] tn2, double[] tgn2) {
    /*      Calculate Temperature and Density Profiles for lower atmos.  */
        double[] xs = new double[10];
        double[] ys = new double[10];
        double[] y2out = new double[10];
        double rgas = 831.4;
        double z, z1, z2, t1, t2, zg, zgdif;
        double yd1, yd2;
        double x;
        doublestar y = new doublestar();
        doublestar yi = new doublestar();
        double expl, gamm, glb;
        double densm_tmp;
        int mn;
        int k;
        densm_tmp = d0;
        if (alt > zn2[0]) {
            if (xm == 0.0)
                return tz.val;
            else
                return d0;
        }

    	/* STRATOSPHERE/MESOSPHERE TEMPERATURE */
        if (alt > zn2[mn2 - 1])
            z = alt;
        else
            z = zn2[mn2 - 1];
        mn = mn2;
        z1 = zn2[0];
        z2 = zn2[mn - 1];
        t1 = tn2[0];
        t2 = tn2[mn - 1];
        zg = zeta(z, z1);
        zgdif = zeta(z2, z1);

    	/* set up spline nodes */
        for (k = 0; k < mn; k++) {
            xs[k] = zeta(zn2[k], z1) / zgdif;
            ys[k] = 1.0 / tn2[k];
        }
        yd1 = -tgn2[0] / (t1 * t1) * zgdif;
        yd2 = -tgn2[1] / (t2 * t2) * zgdif * (Math.pow(((re.val + z2) / (re.val + z1)), 2.0));

    	/* calculate spline coefficients */
        spline(xs, ys, mn, yd1, yd2, y2out);
        x = zg / zgdif;
        splint(xs, ys, y2out, mn, x, y);

    	/* temperature at altitude */
        tz.val = 1.0 / y.val;
        if (xm != 0.0) {
            /* calaculate stratosphere / mesospehere density */
            glb = gsurf.val / (Math.pow((1.0 + z1 / re.val), 2.0));
            gamm = xm * glb * zgdif / rgas;

    		/* Integrate temperature profile */
            splini(xs, ys, y2out, mn, x, yi);
            expl = gamm * yi.val;
            if (expl > 50.0)
                expl = 50.0;

    		/* Density at altitude */
            densm_tmp = densm_tmp * (t1 / tz.val) * Math.exp(-expl);
        }

        if (alt > zn3[0]) {
            if (xm == 0.0)
                return tz.val;
            else
                return densm_tmp;
        }

    	/* troposhere / stratosphere temperature */
        z = alt;
        mn = mn3;
        z1 = zn3[0];
        z2 = zn3[mn - 1];
        t1 = tn3[0];
        t2 = tn3[mn - 1];
        zg = zeta(z, z1);
        zgdif = zeta(z2, z1);

    	/* set up spline nodes */
        for (k = 0; k < mn; k++) {
            xs[k] = zeta(zn3[k], z1) / zgdif;
            ys[k] = 1.0 / tn3[k];
        }
        yd1 = -tgn3[0] / (t1 * t1) * zgdif;
        yd2 = -tgn3[1] / (t2 * t2) * zgdif * (Math.pow(((re.val + z2) / (re.val + z1)), 2.0));

    	/* calculate spline coefficients */
        spline(xs, ys, mn, yd1, yd2, y2out);
        x = zg / zgdif;
        splint(xs, ys, y2out, mn, x, y);

    	/* temperature at altitude */
        tz.val = 1.0 / y.val;
        if (xm != 0.0) {
            /* calculate tropospheric / stratosphere density */
            glb = gsurf.val / (Math.pow((1.0 + z1 / re.val), 2.0));
            gamm = xm * glb * zgdif / rgas;

    		/* Integrate temperature profile */
            splini(xs, ys, y2out, mn, x, yi);
            expl = gamm * yi.val;
            if (expl > 50.0)
                expl = 50.0;

    		/* Density at altitude */
            densm_tmp = densm_tmp * (t1 / tz.val) * Math.exp(-expl);
        }
        if (xm == 0.0)
            return tz.val;
        else
            return densm_tmp;
    }



    /* ------------------------------------------------------------------- */
    /* ------------------------------- DENSU ----------------------------- */
    /* ------------------------------------------------------------------- */

    protected double densu(double alt, double dlb, double tinf, double tlb, double xm, double alpha, doublestar tz, double zlb, double s2, int mn1, double[] zn1, double[] tn1, double[] tgn1) {
    /*      Calculate Temperature and Density Profiles for MSIS models
     *      New lower thermo polynomial
     */
        double yd2, yd1, x = 0.0;
        doublestar y = new doublestar();
        double rgas = 831.4;
        double densu_temp = 1.0;
        double za, z, zg2, tt, ta;
        double dta, z1 = 0.0, z2, t1 = 0.0, t2, zg, zgdif = 0.0;
        int mn = 0;
        int k;
        double glb;
        double expl;
        doublestar yi = new doublestar();
        double densa;
        double gamma, gamm;
        double[] xs = new double[5];
        double[] ys = new double[5];
        double[] y2out = new double[5];
        /* joining altitudes of Bates and spline */
        za = zn1[0];
        if (alt > za)
            z = alt;
        else
            z = za;

    	/* geopotential altitude difference from ZLB */
        zg2 = zeta(z, zlb);

    	/* Bates temperature */
        tt = tinf - (tinf - tlb) * Math.exp(-s2 * zg2);
        ta = tt;
        tz.val = tt;
        densu_temp = tz.val;

        if (alt < za) {
            /* calculate temperature below ZA
             * temperature gradient at ZA from Bates profile */
            dta = (tinf - ta) * s2 * Math.pow(((re.val + zlb) / (re.val + za)), 2.0);
            tgn1[0] = dta;
            tn1[0] = ta;
            if (alt > zn1[mn1 - 1])
                z = alt;
            else
                z = zn1[mn1 - 1];
            mn = mn1;
            z1 = zn1[0];
            z2 = zn1[mn - 1];
            t1 = tn1[0];
            t2 = tn1[mn - 1];
            /* geopotental difference from z1 */
            zg = zeta(z, z1);
            zgdif = zeta(z2, z1);
            /* set up spline nodes */
            for (k = 0; k < mn; k++) {
                xs[k] = zeta(zn1[k], z1) / zgdif;
                ys[k] = 1.0 / tn1[k];
            }
            /* end node derivatives */
            yd1 = -tgn1[0] / (t1 * t1) * zgdif;
            yd2 = -tgn1[1] / (t2 * t2) * zgdif * Math.pow(((re.val + z2) / (re.val + z1)), 2.0);
            /* calculate spline coefficients */
            spline(xs, ys, mn, yd1, yd2, y2out);
            x = zg / zgdif;
            splint(xs, ys, y2out, mn, x, y);
            /* temperature at altitude */
            tz.val = 1.0 / y.val;
            densu_temp = tz.val;
        }
        if (xm == 0)
            return densu_temp;

    	/* calculate density above za */
        glb = gsurf.val / Math.pow((1.0 + zlb / re.val), 2.0);
        gamma = xm * glb / (s2 * rgas * tinf);
        expl = Math.exp(-s2 * gamma * zg2);
        if (expl > 50.0)
            expl = 50.0;
        if (tt <= 0)
            expl = 50.0;

    	/* density at altitude */
        densa = dlb * Math.pow((tlb / tt), ((1.0 + alpha + gamma))) * expl;
        densu_temp = densa;
        if (alt >= za)
            return densu_temp;

    	/* calculate density below za */
        glb = gsurf.val / Math.pow((1.0 + z1 / re.val), 2.0);
        gamm = xm * glb * zgdif / rgas;

    	/* integrate spline temperatures */
        splini(xs, ys, y2out, mn, x, yi);
        expl = gamm * yi.val;
        if (expl > 50.0)
            expl = 50.0;
        if (tz.val <= 0)
            expl = 50.0;

    	/* density at altitude */
        densu_temp = densu_temp * Math.pow((t1 / tz.val), (1.0 + alpha)) * Math.exp(-expl);
        return densu_temp;
    }



    /* ------------------------------------------------------------------- */
    /* ------------------------------- GLOBE7 ---------------------------- */
    /* ------------------------------------------------------------------- */

    /*    3hr Magnetic activity functions */
    /*    Eq. A24d */
    protected double g0(double a, double[] p) {
        return (a - 4.0 + (p[25] - 1.0) * (a - 4.0 + (Math.exp(-Math.sqrt(p[24] * p[24]) * (a - 4.0)) - 1.0) / Math.sqrt(p[24] * p[24])));
    }

    /*    Eq. A24c */
    protected double sumex(double ex) {
        return (1.0 + (1.0 - Math.pow(ex, 19.0)) / (1.0 - ex) * Math.pow(ex, 0.5));
    }

    /*    Eq. A24a */
    protected double sg0(double ex, double[] p, double[] ap) {
        return (g0(ap[1], p) + (g0(ap[2], p) * ex + g0(ap[3], p) * ex * ex +
                g0(ap[4], p) * Math.pow(ex, 3.0) + (g0(ap[5], p) * Math.pow(ex, 4.0) +
                g0(ap[6], p) * Math.pow(ex, 12.0)) * (1.0 - Math.pow(ex, 8.0)) / (1.0 - ex))) / sumex(ex);
    }

    protected double globe7(double[] p, struct_nrlmsise_input input, struct_nrlmsise_flags flags) {
    /*       CALCULATE G(L) FUNCTION
     *       Upper Thermosphere Parameters */
        double t[] = new double[15];
        int i, j;
        int sw9 = 1;
        double apd;
        double xlong;
        double tloc;
        double c, s, c2, c4, s2;
        double sr = 7.2722E-5;
        //double dgtr = 1.74533E-2;
        double dr = 1.72142E-2;
        double hr = 0.2618;
        double cd32, cd18, cd14, cd39;
        double p32, p18, p14, p39;
        double df;
        // TODO CHECK DFA
//        double dfa;
        double f1, f2;
        double tinf = 0;
        struct_ap_array ap;

        tloc = input.lst;
        for (j = 0; j < 14; j++)
            t[j] = 0;
        if (flags.sw[9] > 0)
            sw9 = 1;
        else if (flags.sw[9] < 0)
            sw9 = -1;
        xlong = input.g_long;

    	/* calculate legendre polynomials */
        c = Math.sin(input.g_lat * dgtr);
        s = Math.cos(input.g_lat * dgtr);
        c2 = c * c;
        c4 = c2 * c2;
        s2 = s * s;

        plg[0][1] = c;
        plg[0][2] = 0.5 * (3.0 * c2 - 1.0);
        plg[0][3] = 0.5 * (5.0 * c * c2 - 3.0 * c);
        plg[0][4] = (35.0 * c4 - 30.0 * c2 + 3.0) / 8.0;
        plg[0][5] = (63.0 * c2 * c2 * c - 70.0 * c2 * c + 15.0 * c) / 8.0;
        plg[0][6] = (11.0 * c * plg[0][5] - 5.0 * plg[0][4]) / 6.0;
    /*      plg[0][7] = (13.0*c*plg[0][6] - 6.0*plg[0][5])/7.0; */
        plg[1][1] = s;
        plg[1][2] = 3.0 * c * s;
        plg[1][3] = 1.5 * (5.0 * c2 - 1.0) * s;
        plg[1][4] = 2.5 * (7.0 * c2 * c - 3.0 * c) * s;
        plg[1][5] = 1.875 * (21.0 * c4 - 14.0 * c2 + 1.0) * s;
        plg[1][6] = (11.0 * c * plg[1][5] - 6.0 * plg[1][4]) / 5.0;
    /*      plg[1][7] = (13.0*c*plg[1][6]-7.0*plg[1][5])/6.0; */
    /*      plg[1][8] = (15.0*c*plg[1][7]-8.0*plg[1][6])/7.0; */
        plg[2][2] = 3.0 * s2;
        plg[2][3] = 15.0 * s2 * c;
        plg[2][4] = 7.5 * (7.0 * c2 - 1.0) * s2;
        plg[2][5] = 3.0 * c * plg[2][4] - 2.0 * plg[2][3];
        plg[2][6] = (11.0 * c * plg[2][5] - 7.0 * plg[2][4]) / 4.0;
        plg[2][7] = (13.0 * c * plg[2][6] - 8.0 * plg[2][5]) / 5.0;
        plg[3][3] = 15.0 * s2 * s;
        plg[3][4] = 105.0 * s2 * s * c;
        plg[3][5] = (9.0 * c * plg[3][4] - 7. * plg[3][3]) / 2.0;
        plg[3][6] = (11.0 * c * plg[3][5] - 8. * plg[3][4]) / 3.0;

        if (!(((flags.sw[7] == 0) && (flags.sw[8] == 0)) && (flags.sw[14] == 0))) {
            stloc = Math.sin(hr * tloc);
            ctloc = Math.cos(hr * tloc);
            s2tloc = Math.sin(2.0 * hr * tloc);
            c2tloc = Math.cos(2.0 * hr * tloc);
            s3tloc = Math.sin(3.0 * hr * tloc);
            c3tloc = Math.cos(3.0 * hr * tloc);
        }

        cd32 = Math.cos(dr * (input.doy - p[31]));
        cd18 = Math.cos(2.0 * dr * (input.doy - p[17]));
        cd14 = Math.cos(dr * (input.doy - p[13]));
        cd39 = Math.cos(2.0 * dr * (input.doy - p[38]));
        p32 = p[31];
        p18 = p[17];
        p14 = p[13];
        p39 = p[38];

    	/* F10.7 EFFECT */
        df = input.f107 - input.f107A;
        dfa = input.f107A - 150.0;
        t[0] = p[19] * df * (1.0 + p[59] * dfa) + p[20] * df * df + p[21] * dfa + p[29] * Math.pow(dfa, 2.0);
        f1 = 1.0 + (p[47] * dfa + p[19] * df + p[20] * df * df) * flags.swc[1];
        f2 = 1.0 + (p[49] * dfa + p[19] * df + p[20] * df * df) * flags.swc[1];

    	/*  TIME INDEPENDENT */
        t[1] = (p[1] * plg[0][2] + p[2] * plg[0][4] + p[22] * plg[0][6]) +
                (p[14] * plg[0][2]) * dfa * flags.swc[1] + p[26] * plg[0][1];

    	/*  SYMMETRICAL ANNUAL */
        t[2] = p[18] * cd32;

    	/*  SYMMETRICAL SEMIANNUAL */
        t[3] = (p[15] + p[16] * plg[0][2]) * cd18;

    	/*  ASYMMETRICAL ANNUAL */
        t[4] = f1 * (p[9] * plg[0][1] + p[10] * plg[0][3]) * cd14;

    	/*  ASYMMETRICAL SEMIANNUAL */
        t[5] = p[37] * plg[0][1] * cd39;

        /* DIURNAL */
        if (bool(flags.sw[7])) {
            double t71, t72;
            t71 = (p[11] * plg[1][2]) * cd14 * flags.swc[5];
            t72 = (p[12] * plg[1][2]) * cd14 * flags.swc[5];
            t[6] = f2 * ((p[3] * plg[1][1] + p[4] * plg[1][3] + p[27] * plg[1][5] + t71) *
                    ctloc + (p[6] * plg[1][1] + p[7] * plg[1][3] + p[28] * plg[1][5]
                    + t72) * stloc);
        }

    	/* SEMIDIURNAL */
        if (bool(flags.sw[8])) {
            double t81, t82;
            t81 = (p[23] * plg[2][3] + p[35] * plg[2][5]) * cd14 * flags.swc[5];
            t82 = (p[33] * plg[2][3] + p[36] * plg[2][5]) * cd14 * flags.swc[5];
            t[7] = f2 * ((p[5] * plg[2][2] + p[41] * plg[2][4] + t81) * c2tloc + (p[8] * plg[2][2] + p[42] * plg[2][4] + t82) * s2tloc);
        }

    	/* TERDIURNAL */
        if (bool(flags.sw[14])) {
            t[13] = f2 * ((p[39] * plg[3][3] + (p[93] * plg[3][4] + p[46] * plg[3][6]) * cd14 * flags.swc[5]) * s3tloc + (p[40] * plg[3][3] + (p[94] * plg[3][4] + p[48] * plg[3][6]) * cd14 * flags.swc[5]) * c3tloc);
        }

    	/* magnetic activity based on daily ap */
        if (flags.sw[9] == -1) {
            ap = input.ap_a;
            if (p[51] != 0) {
                double exp1;
                exp1 = Math.exp(-10800.0 * Math.sqrt(p[51] * p[51]) / (1.0 + p[138] * (45.0 - Math.sqrt(input.g_lat * input.g_lat))));
                if (exp1 > 0.99999)
                    exp1 = 0.99999;
                if (p[24] < 1.0E-4)
                    p[24] = 1.0E-4;
                apt[0] = sg0(exp1, p, ap.a);
                /* apt[1]=sg2(exp1,p,ap.area);
    			   apt[2]=sg0(exp2,p,ap.area);
    			   apt[3]=sg2(exp2,p,ap.area);
    			*/
                if (bool(flags.sw[9])) {
                    t[8] = apt[0] * (p[50] + p[96] * plg[0][2] + p[54] * plg[0][4] +
                            (p[125] * plg[0][1] + p[126] * plg[0][3] + p[127] * plg[0][5]) * cd14 * flags.swc[5] +
                            (p[128] * plg[1][1] + p[129] * plg[1][3] + p[130] * plg[1][5]) * flags.swc[7] *
                                    Math.cos(hr * (tloc - p[131])));
                }
            }
        } else {
            double p44, p45;
            apd = input.ap - 4.0;
            p44 = p[43];
            p45 = p[44];
            if (p44 < 0)
                p44 = 1.0E-5;
            apdf = apd + (p45 - 1.0) * (apd + (Math.exp(-p44 * apd) - 1.0) / p44);
            if (bool(flags.sw[9])) {
                t[8] = apdf * (p[32] + p[45] * plg[0][2] + p[34] * plg[0][4] +
                        (p[100] * plg[0][1] + p[101] * plg[0][3] + p[102] * plg[0][5]) * cd14 * flags.swc[5] +
                        (p[121] * plg[1][1] + p[122] * plg[1][3] + p[123] * plg[1][5]) * flags.swc[7] *
                                Math.cos(hr * (tloc - p[124])));
            }
        }

        if (bool(flags.sw[10]) && (input.g_long > -1000.0)) {

    		/* longitudinal */
            if (bool(flags.sw[11])) {
                t[10] = (1.0 + p[80] * dfa * flags.swc[1]) *
                        ((p[64] * plg[1][2] + p[65] * plg[1][4] + p[66] * plg[1][6]
                                + p[103] * plg[1][1] + p[104] * plg[1][3] + p[105] * plg[1][5]
                                + flags.swc[5] * (p[109] * plg[1][1] + p[110] * plg[1][3] + p[111] * plg[1][5]) * cd14) *
                                Math.cos(dgtr * input.g_long)
                                + (p[90] * plg[1][2] + p[91] * plg[1][4] + p[92] * plg[1][6]
                                + p[106] * plg[1][1] + p[107] * plg[1][3] + p[108] * plg[1][5]
                                + flags.swc[5] * (p[112] * plg[1][1] + p[113] * plg[1][3] + p[114] * plg[1][5]) * cd14) *
                                Math.sin(dgtr * input.g_long));
            }

    		/* ut and mixed ut, longitude */
            if (bool(flags.sw[12])) {
                t[11] = (1.0 + p[95] * plg[0][1]) * (1.0 + p[81] * dfa * flags.swc[1]) *
                        (1.0 + p[119] * plg[0][1] * flags.swc[5] * cd14) *
                        ((p[68] * plg[0][1] + p[69] * plg[0][3] + p[70] * plg[0][5]) *
                                Math.cos(sr * (input.sec - p[71])));
                t[11] += flags.swc[11] *
                        (p[76] * plg[2][3] + p[77] * plg[2][5] + p[78] * plg[2][7]) *
                        Math.cos(sr * (input.sec - p[79]) + 2.0 * dgtr * input.g_long) * (1.0 + p[137] * dfa * flags.swc[1]);
            }

    		/* ut, longitude magnetic activity */
            if (bool(flags.sw[13])) {
                if (flags.sw[9] == -1) {
                    if (bool(p[51])) {
                        t[12] = apt[0] * flags.swc[11] * (1. + p[132] * plg[0][1]) *
                                ((p[52] * plg[1][2] + p[98] * plg[1][4] + p[67] * plg[1][6]) *
                                        Math.cos(dgtr * (input.g_long - p[97])))
                                + apt[0] * flags.swc[11] * flags.swc[5] *
                                (p[133] * plg[1][1] + p[134] * plg[1][3] + p[135] * plg[1][5]) *
                                cd14 * Math.cos(dgtr * (input.g_long - p[136]))
                                + apt[0] * flags.swc[12] *
                                (p[55] * plg[0][1] + p[56] * plg[0][3] + p[57] * plg[0][5]) *
                                Math.cos(sr * (input.sec - p[58]));
                    }
                } else {
                    t[12] = apdf * flags.swc[11] * (1.0 + p[120] * plg[0][1]) *
                            ((p[60] * plg[1][2] + p[61] * plg[1][4] + p[62] * plg[1][6]) *
                                    Math.cos(dgtr * (input.g_long - p[63])))
                            + apdf * flags.swc[11] * flags.swc[5] *
                            (p[115] * plg[1][1] + p[116] * plg[1][3] + p[117] * plg[1][5]) *
                            cd14 * Math.cos(dgtr * (input.g_long - p[118]))
                            + apdf * flags.swc[12] *
                            (p[83] * plg[0][1] + p[84] * plg[0][3] + p[85] * plg[0][5]) *
                            Math.cos(sr * (input.sec - p[75]));
                }
            }
        }

    	/* parms not used: 82, 89, 99, 139-149 */
        tinf = p[30];
        for (i = 0; i < 14; i++)
            tinf = tinf + Math.abs(flags.sw[i + 1]) * t[i];
        return tinf;
    }



    /* ------------------------------------------------------------------- */
    /* ------------------------------- GLOB7S ---------------------------- */
    /* ------------------------------------------------------------------- */

    protected double glob7s(double[] p, struct_nrlmsise_input input, struct_nrlmsise_flags flags) {
    /*    VERSION OF GLOBE FOR LOWER ATMOSPHERE 10/26/99
     */
        double pset = 2.0;
        double t[] = new double[14];
        double tt;
        double cd32, cd18, cd14, cd39;
        double p32, p18, p14, p39;
        int i, j;
        double dr = 1.72142E-2;
    	/* confirm parameter set */
        if (p[99] == 0)
            p[99] = pset;
        if (p[99] != pset) {
            System.out.println("Wrong parameter set for glob7s");
            return -1;
        }
        for (j = 0; j < 14; j++)
            t[j] = 0.0;
        cd32 = Math.cos(dr * (input.doy - p[31]));
        cd18 = Math.cos(2.0 * dr * (input.doy - p[17]));
        cd14 = Math.cos(dr * (input.doy - p[13]));
        cd39 = Math.cos(2.0 * dr * (input.doy - p[38]));
        p32 = p[31];
        p18 = p[17];
        p14 = p[13];
        p39 = p[38];

    	/* F10.7 */
        t[0] = p[21] * dfa;

    	/* time independent */
        t[1] = p[1] * plg[0][2] + p[2] * plg[0][4] + p[22] * plg[0][6] + p[26] * plg[0][1] + p[14] * plg[0][3] + p[59] * plg[0][5];

            /* SYMMETRICAL ANNUAL */
        t[2] = (p[18] + p[47] * plg[0][2] + p[29] * plg[0][4]) * cd32;

            /* SYMMETRICAL SEMIANNUAL */
        t[3] = (p[15] + p[16] * plg[0][2] + p[30] * plg[0][4]) * cd18;

            /* ASYMMETRICAL ANNUAL */
        t[4] = (p[9] * plg[0][1] + p[10] * plg[0][3] + p[20] * plg[0][5]) * cd14;

    	/* ASYMMETRICAL SEMIANNUAL */
        t[5] = (p[37] * plg[0][1]) * cd39;

            /* DIURNAL */
        if (bool(flags.sw[7])) {
            double t71, t72;
            t71 = p[11] * plg[1][2] * cd14 * flags.swc[5];
            t72 = p[12] * plg[1][2] * cd14 * flags.swc[5];
            t[6] = ((p[3] * plg[1][1] + p[4] * plg[1][3] + t71) * ctloc + (p[6] * plg[1][1] + p[7] * plg[1][3] + t72) * stloc);
        }

    	/* SEMIDIURNAL */
        if (bool(flags.sw[8])) {
            double t81, t82;
            t81 = (p[23] * plg[2][3] + p[35] * plg[2][5]) * cd14 * flags.swc[5];
            t82 = (p[33] * plg[2][3] + p[36] * plg[2][5]) * cd14 * flags.swc[5];
            t[7] = ((p[5] * plg[2][2] + p[41] * plg[2][4] + t81) * c2tloc + (p[8] * plg[2][2] + p[42] * plg[2][4] + t82) * s2tloc);
        }

    	/* TERDIURNAL */
        if (bool(flags.sw[14])) {
            t[13] = p[39] * plg[3][3] * s3tloc + p[40] * plg[3][3] * c3tloc;
        }

    	/* MAGNETIC ACTIVITY */
        if (bool(flags.sw[9])) {
            if (flags.sw[9] == 1)
                t[8] = apdf * (p[32] + p[45] * plg[0][2] * flags.swc[2]);
            if (flags.sw[9] == -1)
                t[8] = (p[50] * apt[0] + p[96] * plg[0][2] * apt[0] * flags.swc[2]);
        }

    	/* LONGITUDINAL */
        if (!((flags.sw[10] == 0) || (flags.sw[11] == 0) || (input.g_long <= -1000.0))) {
            t[10] = (1.0 + plg[0][1] * (p[80] * flags.swc[5] * Math.cos(dr * (input.doy - p[81]))
                    + p[85] * flags.swc[6] * Math.cos(2.0 * dr * (input.doy - p[86])))
                    + p[83] * flags.swc[3] * Math.cos(dr * (input.doy - p[84]))
                    + p[87] * flags.swc[4] * Math.cos(2.0 * dr * (input.doy - p[88])))
                    * ((p[64] * plg[1][2] + p[65] * plg[1][4] + p[66] * plg[1][6]
                    + p[74] * plg[1][1] + p[75] * plg[1][3] + p[76] * plg[1][5]
            ) * Math.cos(dgtr * input.g_long)
                    + (p[90] * plg[1][2] + p[91] * plg[1][4] + p[92] * plg[1][6]
                    + p[77] * plg[1][1] + p[78] * plg[1][3] + p[79] * plg[1][5]
            ) * Math.sin(dgtr * input.g_long));
        }
        tt = 0;
        for (i = 0; i < 14; i++)
            tt += Math.abs(flags.sw[i + 1]) * t[i];
        return tt;
    }



    /* ------------------------------------------------------------------- */
    /* ------------------------------- GTD7 ------------------------------ */
    /* ------------------------------------------------------------------- */

    /**
     * Neutral Atmosphere Empircial Model from the surface to lower
     * exosphere.
     */
    protected void gtd7(struct_nrlmsise_input input, struct_nrlmsise_flags flags, struct_nrlmsise_output output) {
        double xlat;
        double xmm;
        int mn3 = 5;
        double zn3[] = {32.5, 20.0, 15.0, 10.0, 0.0};
        int mn2 = 4;
        double zn2[] = {72.5, 55.0, 45.0, 32.5};
        double altt;
        double zmix = 62.5;
        double tmp;
        double dm28m;
        doublestar tz = new doublestar();
        double dmc;
        double dmr;
        double dz28;
        struct_nrlmsise_output soutput = new struct_nrlmsise_output();
        int i;

        tselec(flags);

    	/* Latitude variation of gravity (none for sw[2]=0) */
        xlat = input.g_lat;
        if (flags.sw[2] == 0)
            xlat = 45.0;
        glatf(xlat, gsurf, re);

        xmm = pdm[2][4];

    	/* THERMOSPHERE / MESOSPHERE (above zn2[0]) */
        if (input.alt > zn2[0])
            altt = input.alt;
        else
            altt = zn2[0];

        tmp = input.alt;
        input.alt = altt;
        gts7(input, flags, soutput);
        altt = input.alt;
        input.alt = tmp;
        if (bool(flags.sw[0]))   /* metric adjustment */
            dm28m = dm28 * 1.0E6;
        else
            dm28m = dm28;
        output.t[0] = soutput.t[0];
        output.t[1] = soutput.t[1];
        if (input.alt >= zn2[0]) {
            for (i = 0; i < 9; i++)
                output.d[i] = soutput.d[i];
            return;
        }

    /*       LOWER MESOSPHERE/UPPER STRATOSPHERE (between zn3[0] and zn2[0])
     *         Temperature at nodes and gradients at end nodes
     *         Inverse temperature area linear function of spherical harmonics
     */
        meso_tgn2[0] = meso_tgn1[1];
        meso_tn2[0] = meso_tn1[4];
        meso_tn2[1] = pma[0][0] * pavgm[0] / (1.0 - flags.sw[20] * glob7s(pma[0], input, flags));
        meso_tn2[2] = pma[1][0] * pavgm[1] / (1.0 - flags.sw[20] * glob7s(pma[1], input, flags));
        meso_tn2[3] = pma[2][0] * pavgm[2] / (1.0 - flags.sw[20] * flags.sw[22] * glob7s(pma[2], input, flags));
        meso_tgn2[1] = pavgm[8] * pma[9][0] * (1.0 + flags.sw[20] * flags.sw[22] * glob7s(pma[9], input, flags)) * meso_tn2[3] * meso_tn2[3] / (Math.pow((pma[2][0] * pavgm[2]), 2.0));
        meso_tn3[0] = meso_tn2[3];

        if (input.alt < zn3[0]) {
    /*       LOWER STRATOSPHERE AND TROPOSPHERE (below zn3[0])
     *         Temperature at nodes and gradients at end nodes
     *         Inverse temperature area linear function of spherical harmonics
     */
            meso_tgn3[0] = meso_tgn2[1];
            meso_tn3[1] = pma[3][0] * pavgm[3] / (1.0 - flags.sw[22] * glob7s(pma[3], input, flags));
            meso_tn3[2] = pma[4][0] * pavgm[4] / (1.0 - flags.sw[22] * glob7s(pma[4], input, flags));
            meso_tn3[3] = pma[5][0] * pavgm[5] / (1.0 - flags.sw[22] * glob7s(pma[5], input, flags));
            meso_tn3[4] = pma[6][0] * pavgm[6] / (1.0 - flags.sw[22] * glob7s(pma[6], input, flags));
            meso_tgn3[1] = pma[7][0] * pavgm[7] * (1.0 + flags.sw[22] * glob7s(pma[7], input, flags)) * meso_tn3[4] * meso_tn3[4] / (Math.pow((pma[6][0] * pavgm[6]), 2.0));
        }

            /* LINEAR TRANSITION TO FULL MIXING BELOW zn2[0] */

        dmc = 0;
        if (input.alt > zmix)
            dmc = 1.0 - (zn2[0] - input.alt) / (zn2[0] - zmix);
        dz28 = soutput.d[2];

        /**** N2 density ****/
        dmr = soutput.d[2] / dm28m - 1.0;
        output.d[2] = densm(input.alt, dm28m, xmm, tz, mn3, zn3, meso_tn3, meso_tgn3, mn2, zn2, meso_tn2, meso_tgn2);
        output.d[2] = output.d[2] * (1.0 + dmr * dmc);

        /**** HE density ****/
        dmr = soutput.d[0] / (dz28 * pdm[0][1]) - 1.0;
        output.d[0] = output.d[2] * pdm[0][1] * (1.0 + dmr * dmc);

        /**** O density ****/
        output.d[1] = 0;
        output.d[8] = 0;

        /**** O2 density ****/
        dmr = soutput.d[3] / (dz28 * pdm[3][1]) - 1.0;
        output.d[3] = output.d[2] * pdm[3][1] * (1.0 + dmr * dmc);

        /**** AR density ***/
        dmr = soutput.d[4] / (dz28 * pdm[4][1]) - 1.0;
        output.d[4] = output.d[2] * pdm[4][1] * (1.0 + dmr * dmc);

        /**** Hydrogen density ****/
        output.d[6] = 0;

        /**** Atomic nitrogen density ****/
        output.d[7] = 0;

        /**** Total mass density */
        output.d[5] = 1.66E-24 * (4.0 * output.d[0] + 16.0 * output.d[1] + 28.0 * output.d[2] + 32.0 * output.d[3] + 40.0 * output.d[4] + output.d[6] + 14.0 * output.d[7]);

        if (bool(flags.sw[0]))
            output.d[5] = output.d[5] / 1000;

        /**** temperature at altitude ****/
        dd = densm(input.alt, 1.0, 0, tz, mn3, zn3, meso_tn3, meso_tgn3, mn2, zn2, meso_tn2, meso_tgn2);
        output.t[1].val = tz.val;

    }



    /* ------------------------------------------------------------------- */
    /* ------------------------------- GTD7D ----------------------------- */
    /* ------------------------------------------------------------------- */

    /**
     * This subroutine provides Effective Total Mass Density for output
     * d[5] which includes contributions from "anomalous oxygen" which can
     * affect satellite drag above 500 km. See the section "output" for
     * additional details.
     */
    protected void gtd7d(struct_nrlmsise_input input, struct_nrlmsise_flags flags, struct_nrlmsise_output output) {
        gtd7(input, flags, output);
        output.d[5] = 1.66E-24 * (4.0 * output.d[0] + 16.0 * output.d[1] + 28.0 * output.d[2] + 32.0 * output.d[3] + 40.0 * output.d[4] + output.d[6] + 14.0 * output.d[7] + 16.0 * output.d[8]);
        // CHECK
        if (bool(flags.sw[0])) {
            output.d[5] = output.d[5] / 1000;
        }
    }



    /* ------------------------------------------------------------------- */
    /* -------------------------------- GHP7 ----------------------------- */
    /* ------------------------------------------------------------------- */

    /**
     * To specify outputs at area pressure level (press) rather than at
     * an altitude.
     */
    protected void ghp7(struct_nrlmsise_input input, struct_nrlmsise_flags flags, struct_nrlmsise_output output, double press) {
        double bm = 1.3806E-19;
        double rgas = 831.4;
        double test = 0.00043;
        double ltest = 12;
        double pl, p;
        double zi;
        double z;
        double cl, cl2;
        double ca, cd;
        double xn, xm, diff;
        double g, sh;
        int l;
        pl = Math.log10(press);
        if (pl >= -5.0) {
            if (pl > 2.5)
                zi = 18.06 * (3.00 - pl);
            else if ((pl > 0.075) && (pl <= 2.5))
                zi = 14.98 * (3.08 - pl);
            else if ((pl > -1) && (pl <= 0.075))
                zi = 17.80 * (2.72 - pl);
            else if ((pl > -2) && (pl <= -1))
                zi = 14.28 * (3.64 - pl);
            else if ((pl > -4) && (pl <= -2))
                zi = 12.72 * (4.32 - pl);
            else // CHECK if (pl <= -4)
                zi = 25.3 * (0.11 - pl);
            cl = input.g_lat / 90.0;
            cl2 = cl * cl;
            if (input.doy < 182)
                cd = (1.0 - (double) input.doy) / 91.25;
            else
                cd = ((double) input.doy) / 91.25 - 3.0;
            ca = 0;
            if ((pl > -1.11) && (pl <= -0.23))
                ca = 1.0;
            if (pl > -0.23)
                ca = (2.79 - pl) / (2.79 + 0.23);
            if ((pl <= -1.11) && (pl > -3))
                ca = (-2.93 - pl) / (-2.93 + 1.11);
            z = zi - 4.87 * cl * cd * ca - 1.64 * cl2 * ca + 0.31 * ca * cl;
        } else
            z = 22.0 * Math.pow((pl + 4.0), 2.0) + 110.0;

    	/* iteration  loop */
        l = 0;
        do {
            l++;
            input.alt = z;
            gtd7(input, flags, output);
            z = input.alt;
            xn = output.d[0] + output.d[1] + output.d[2] + output.d[3] + output.d[4] + output.d[6] + output.d[7];
            p = bm * xn * output.t[1].val;
            if (bool(flags.sw[0]))
                p = p * 1.0E-6;
            diff = pl - Math.log10(p);
            if (Math.sqrt(diff * diff) < test)
                return;
            if (l == ltest) {
                System.out.println("ERROR: ghp7 not converging for press " + press + ", diff " + diff);
                return;
            }
            xm = output.d[5] / xn / 1.66E-24;
            if (bool(flags.sw[0]))
                xm = xm * 1.0E3;
            g = gsurf.val / (Math.pow((1.0 + z / re.val), 2.0));
            sh = rgas * output.t[1].val / (xm * g);

    		/* new altitude estimate using scale height */
            if (l < 6)
                z = z - sh * diff * 2.302;
            else
                z = z - sh * diff;
        } while (1 == 1);
    }



    /* ------------------------------------------------------------------- */
    /* ------------------------------- GTS7 ------------------------------ */
    /* ------------------------------------------------------------------- */

    /**
     * Thermospheric portion of NRLMSISE-00
     */
    protected void gts7(struct_nrlmsise_input input, struct_nrlmsise_flags flags, struct_nrlmsise_output output) {
    /*     Thermospheric portion of NRLMSISE-00
     *     See GTD7 for more extensive comments
     *     alt > 72.5 km!
     */
        double za;
        int i, j;
        double ddum, z;
        double zn1[] = {120.0, 110.0, 100.0, 90.0, 72.5};
        double tinf;
        int mn1 = 5;
        double g0;
        double tlb;
        double s;
        // CHECK z0, t0, tr12;
        double db01, db04, db14, db16, db28, db32, db40;
        //CHECK db48;
        double zh28, zh04, zh16, zh32, zh40, zh01, zh14;
        double zhm28, zhm04, zhm16, zhm32, zhm40, zhm01, zhm14;
        double xmd;
        double b28, b04, b16, b32, b40, b01, b14;
        doublestar tz = new doublestar();
        double g28, g4, g16, g32, g40, g1, g14;
        double zhf, xmm;
        double zc04, zc16, zc32, zc40, zc01, zc14;
        double hc04, hc16, hc32, hc40, hc01, hc14;
        double hcc16, hcc32, hcc01, hcc14;
        double zcc16, zcc32, zcc01, zcc14;
        double rc16, rc32, rc01, rc14;
        double rl;
        double g16h, db16h, tho, zsht, zmho, zsho;
        double dr = 1.72142E-2;
        double alpha[] = {-0.38, 0.0, 0.0, 0.0, 0.17, 0.0, -0.38, 0.0, 0.0};
        double altl[] = {200.0, 300.0, 160.0, 250.0, 240.0, 450.0, 320.0, 450.0};
        double dd;
        double hc216, hcc232;

        za = pdl[1][15];
        zn1[0] = za;
        for (j = 0; j < 9; j++)
            output.d[j] = 0;

    	/* TINF VARIATIONS NOT IMPORTANT BELOW ZA OR ZN1(1) */
        if (input.alt > zn1[0])
            tinf = ptm[0] * pt[0] * (1.0 + flags.sw[16] * globe7(pt, input, flags));
        else
            tinf = ptm[0] * pt[0];
        output.t[0].val = tinf;

    	/*  GRADIENT VARIATIONS NOT IMPORTANT BELOW ZN1(5) */
        if (input.alt > zn1[4])
            g0 = ptm[3] * ps[0] * (1.0 + flags.sw[19] * globe7(ps, input, flags));
        else
            g0 = ptm[3] * ps[0];
        tlb = ptm[1] * (1.0 + flags.sw[17] * globe7(pd[3], input, flags)) * pd[3][0];
        s = g0 / (tinf - tlb);

    /*      Lower thermosphere temp variations not significant for
     *       density above 300 km */
        if (input.alt < 300.0) {
            meso_tn1[1] = ptm[6] * ptl[0][0] / (1.0 - flags.sw[18] * glob7s(ptl[0], input, flags));
            meso_tn1[2] = ptm[2] * ptl[1][0] / (1.0 - flags.sw[18] * glob7s(ptl[1], input, flags));
            meso_tn1[3] = ptm[7] * ptl[2][0] / (1.0 - flags.sw[18] * glob7s(ptl[2], input, flags));
            meso_tn1[4] = ptm[4] * ptl[3][0] / (1.0 - flags.sw[18] * flags.sw[20] * glob7s(ptl[3], input, flags));
            meso_tgn1[1] = ptm[8] * pma[8][0] * (1.0 + flags.sw[18] * flags.sw[20] * glob7s(pma[8], input, flags)) * meso_tn1[4] * meso_tn1[4] / (Math.pow((ptm[4] * ptl[3][0]), 2.0));
        } else {
            meso_tn1[1] = ptm[6] * ptl[0][0];
            meso_tn1[2] = ptm[2] * ptl[1][0];
            meso_tn1[3] = ptm[7] * ptl[2][0];
            meso_tn1[4] = ptm[4] * ptl[3][0];
            meso_tgn1[1] = ptm[8] * pma[8][0] * meso_tn1[4] * meso_tn1[4] / (Math.pow((ptm[4] * ptl[3][0]), 2.0));
        }

        // CHECK
//        z0 = zn1[3];
//        t0 = meso_tn1[3];
//        tr12 = 1.0;

    	/* N2 variation factor at Zlb */
        g28 = flags.sw[21] * globe7(pd[2], input, flags);

    	/* VARIATION OF TURBOPAUSE HEIGHT */
        zhf = pdl[1][24] * (1.0 + flags.sw[5] * pdl[0][24] * Math.sin(dgtr * input.g_lat) * Math.cos(dr * (input.doy - pt[13])));
        output.t[0].val = tinf;
        xmm = pdm[2][4];
        z = input.alt;


        /**** N2 DENSITY ****/

    	/* Diffusive density at Zlb */
        db28 = pdm[2][0] * Math.exp(g28) * pd[2][0];
    	/* Diffusive density at Alt */
        output.d[2] = densu(z, db28, tinf, tlb, 28.0, alpha[2], output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
        dd = output.d[2];
    	/* Turbopause */
        zh28 = pdm[2][2] * zhf;
        zhm28 = pdm[2][3] * pdl[1][5];
        xmd = 28.0 - xmm;
    	/* Mixed density at Zlb */
        b28 = densu(zh28, db28, tinf, tlb, xmd, (alpha[2] - 1.0), tz, ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
        if (bool(flags.sw[15]) && (z <= altl[2])) {
    		/*  Mixed density at Alt */
            dm28 = densu(z, b28, tinf, tlb, xmm, alpha[2], tz, ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
    		/*  Net density at Alt */
            output.d[2] = dnet(output.d[2], dm28, zhm28, xmm, 28.0);
        }


        /**** HE DENSITY ****/

    	/*   Density variation factor at Zlb */
        g4 = flags.sw[21] * globe7(pd[0], input, flags);
    	/*  Diffusive density at Zlb */
        db04 = pdm[0][0] * Math.exp(g4) * pd[0][0];
        /*  Diffusive density at Alt */
        output.d[0] = densu(z, db04, tinf, tlb, 4., alpha[0], output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
        dd = output.d[0];
        if (bool(flags.sw[15]) && (z < altl[0])) {
    		/*  Turbopause */
            zh04 = pdm[0][2];
    		/*  Mixed density at Zlb */
            b04 = densu(zh04, db04, tinf, tlb, 4. - xmm, alpha[0] - 1., output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
    		/*  Mixed density at Alt */
            dm04 = densu(z, b04, tinf, tlb, xmm, 0., output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
            zhm04 = zhm28;
    		/*  Net density at Alt */
            output.d[0] = dnet(output.d[0], dm04, zhm04, xmm, 4.);
    		/*  Correction to specified mixing ratio at ground */
            rl = Math.log(b28 * pdm[0][1] / b04);
            zc04 = pdm[0][4] * pdl[1][0];
            hc04 = pdm[0][5] * pdl[1][1];
    		/*  Net density corrected at Alt */
            output.d[0] = output.d[0] * ccor(z, rl, hc04, zc04);
        }


        /**** O DENSITY ****/

    	/*  Density variation factor at Zlb */
        g16 = flags.sw[21] * globe7(pd[1], input, flags);
    	/*  Diffusive density at Zlb */
        db16 = pdm[1][0] * Math.exp(g16) * pd[1][0];
        /*  Diffusive density at Alt */
        output.d[1] = densu(z, db16, tinf, tlb, 16., alpha[1], output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
        dd = output.d[1];
        if (bool(flags.sw[15]) && (z <= altl[1])) {
    		/*  Turbopause */
            zh16 = pdm[1][2];
    		/*  Mixed density at Zlb */
            b16 = densu(zh16, db16, tinf, tlb, 16.0 - xmm, (alpha[1] - 1.0), output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
    		/*  Mixed density at Alt */
            dm16 = densu(z, b16, tinf, tlb, xmm, 0., output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
            zhm16 = zhm28;
    		/*  Net density at Alt */
            output.d[1] = dnet(output.d[1], dm16, zhm16, xmm, 16.);
            rl = pdm[1][1] * pdl[1][16] * (1.0 + flags.sw[1] * pdl[0][23] * (input.f107A - 150.0));
            hc16 = pdm[1][5] * pdl[1][3];
            zc16 = pdm[1][4] * pdl[1][2];
            hc216 = pdm[1][5] * pdl[1][4];
            output.d[1] = output.d[1] * ccor2(z, rl, hc16, zc16, hc216);
    		/*  Chemistry correction */
            hcc16 = pdm[1][7] * pdl[1][13];
            zcc16 = pdm[1][6] * pdl[1][12];
            rc16 = pdm[1][3] * pdl[1][14];
    		/*  Net density corrected at Alt */
            output.d[1] = output.d[1] * ccor(z, rc16, hcc16, zcc16);
        }


        /**** O2 DENSITY ****/

        /*  Density variation factor at Zlb */
        g32 = flags.sw[21] * globe7(pd[4], input, flags);
        /*  Diffusive density at Zlb */
        db32 = pdm[3][0] * Math.exp(g32) * pd[4][0];
        /*  Diffusive density at Alt */
        output.d[3] = densu(z, db32, tinf, tlb, 32., alpha[3], output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
        dd = output.d[3];
        if (bool(flags.sw[15])) {
            if (z <= altl[3]) {
    			/*  Turbopause */
                zh32 = pdm[3][2];
    			/*  Mixed density at Zlb */
                b32 = densu(zh32, db32, tinf, tlb, 32. - xmm, alpha[3] - 1., output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
    			/*  Mixed density at Alt */
                dm32 = densu(z, b32, tinf, tlb, xmm, 0., output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
                zhm32 = zhm28;
    			/*  Net density at Alt */
                output.d[3] = dnet(output.d[3], dm32, zhm32, xmm, 32.);
    			/*  Correction to specified mixing ratio at ground */
                rl = Math.log(b28 * pdm[3][1] / b32);
                hc32 = pdm[3][5] * pdl[1][7];
                zc32 = pdm[3][4] * pdl[1][6];
                output.d[3] = output.d[3] * ccor(z, rl, hc32, zc32);
            }
    		/*  Correction for general departure from diffusive equilibrium above Zlb */
            hcc32 = pdm[3][7] * pdl[1][22];
            hcc232 = pdm[3][7] * pdl[0][22];
            zcc32 = pdm[3][6] * pdl[1][21];
            rc32 = pdm[3][3] * pdl[1][23] * (1. + flags.sw[1] * pdl[0][23] * (input.f107A - 150.));
    		/*  Net density corrected at Alt */
            output.d[3] = output.d[3] * ccor2(z, rc32, hcc32, zcc32, hcc232);
        }


        /**** AR DENSITY ****/

        /*  Density variation factor at Zlb */
        // CHECK g40 = flags.sw[20] * globe7(pd[5], input, flags);
        g40 = flags.sw[21] * globe7(pd[5], input, flags);
        /*  Diffusive density at Zlb */
        db40 = pdm[4][0] * Math.exp(g40) * pd[5][0];
    	/*  Diffusive density at Alt */
        output.d[4] = densu(z, db40, tinf, tlb, 40., alpha[4], output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
        dd = output.d[4];
        if (bool(flags.sw[15]) && (z <= altl[4])) {
    		/*  Turbopause */
            zh40 = pdm[4][2];
    		/*  Mixed density at Zlb */
            b40 = densu(zh40, db40, tinf, tlb, 40. - xmm, alpha[4] - 1., output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
    		/*  Mixed density at Alt */
            dm40 = densu(z, b40, tinf, tlb, xmm, 0., output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
            zhm40 = zhm28;
    		/*  Net density at Alt */
            output.d[4] = dnet(output.d[4], dm40, zhm40, xmm, 40.);
    		/*  Correction to specified mixing ratio at ground */
            rl = Math.log(b28 * pdm[4][1] / b40);
            hc40 = pdm[4][5] * pdl[1][9];
            zc40 = pdm[4][4] * pdl[1][8];
    		/*  Net density corrected at Alt */
            output.d[4] = output.d[4] * ccor(z, rl, hc40, zc40);
        }


        /**** HYDROGEN DENSITY ****/

        /*  Density variation factor at Zlb */
        g1 = flags.sw[21] * globe7(pd[6], input, flags);
        /*  Diffusive density at Zlb */
        db01 = pdm[5][0] * Math.exp(g1) * pd[6][0];
        /*  Diffusive density at Alt */
        output.d[6] = densu(z, db01, tinf, tlb, 1., alpha[6], output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
        dd = output.d[6];
        if (bool(flags.sw[15]) && (z <= altl[6])) {
    		/*  Turbopause */
            zh01 = pdm[5][2];
    		/*  Mixed density at Zlb */
            b01 = densu(zh01, db01, tinf, tlb, 1. - xmm, alpha[6] - 1., output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
    		/*  Mixed density at Alt */
            dm01 = densu(z, b01, tinf, tlb, xmm, 0., output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
            zhm01 = zhm28;
    		/*  Net density at Alt */
            output.d[6] = dnet(output.d[6], dm01, zhm01, xmm, 1.);
    		/*  Correction to specified mixing ratio at ground */
            rl = Math.log(b28 * pdm[5][1] * Math.sqrt(pdl[1][17] * pdl[1][17]) / b01);
            hc01 = pdm[5][5] * pdl[1][11];
            zc01 = pdm[5][4] * pdl[1][10];
            output.d[6] = output.d[6] * ccor(z, rl, hc01, zc01);
    		/*  Chemistry correction */
            hcc01 = pdm[5][7] * pdl[1][19];
            zcc01 = pdm[5][6] * pdl[1][18];
            rc01 = pdm[5][3] * pdl[1][20];
    		/*  Net density corrected at Alt */
            output.d[6] = output.d[6] * ccor(z, rc01, hcc01, zcc01);
        }


        /**** ATOMIC NITROGEN DENSITY ****/

    	/*  Density variation factor at Zlb */
        g14 = flags.sw[21] * globe7(pd[7], input, flags);
        /*  Diffusive density at Zlb */
        db14 = pdm[6][0] * Math.exp(g14) * pd[7][0];
        /*  Diffusive density at Alt */
        output.d[7] = densu(z, db14, tinf, tlb, 14., alpha[7], output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
        dd = output.d[7];
        if (bool(flags.sw[15]) && (z <= altl[7])) {
    		/*  Turbopause */
            zh14 = pdm[6][2];
    		/*  Mixed density at Zlb */
            b14 = densu(zh14, db14, tinf, tlb, 14. - xmm, alpha[7] - 1., output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
    		/*  Mixed density at Alt */
            dm14 = densu(z, b14, tinf, tlb, xmm, 0., output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
            zhm14 = zhm28;
    		/*  Net density at Alt */
            output.d[7] = dnet(output.d[7], dm14, zhm14, xmm, 14.);
    		/*  Correction to specified mixing ratio at ground */
            rl = Math.log(b28 * pdm[6][1] * Math.sqrt(pdl[0][2] * pdl[0][2]) / b14);
            hc14 = pdm[6][5] * pdl[0][1];
            zc14 = pdm[6][4] * pdl[0][0];
            output.d[7] = output.d[7] * ccor(z, rl, hc14, zc14);
    		/*  Chemistry correction */
            hcc14 = pdm[6][7] * pdl[0][4];
            zcc14 = pdm[6][6] * pdl[0][3];
            rc14 = pdm[6][3] * pdl[0][5];
    		/*  Net density corrected at Alt */
            output.d[7] = output.d[7] * ccor(z, rc14, hcc14, zcc14);
        }


        /**** Anomalous OXYGEN DENSITY ****/

        g16h = flags.sw[21] * globe7(pd[8], input, flags);
        db16h = pdm[7][0] * Math.exp(g16h) * pd[8][0];
        tho = pdm[7][9] * pdl[0][6];
        dd = densu(z, db16h, tho, tho, 16., alpha[8], output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
        zsht = pdm[7][5];
        zmho = pdm[7][4];
        zsho = scalh(zmho, 16.0, tho);
        output.d[8] = dd * Math.exp(-zsht / zsho * (Math.exp(-(z - zmho) / zsht) - 1.));


    	/* total mass density */
        output.d[5] = 1.66E-24 * (4.0 * output.d[0] + 16.0 * output.d[1] + 28.0 * output.d[2] + 32.0 * output.d[3] + 40.0 * output.d[4] + output.d[6] + 14.0 * output.d[7]);
        // CHECK db48 = 1.66E-24 * (4.0 * db04 + 16.0 * db16 + 28.0 * db28 + 32.0 * db32 + 40.0 * db40 + db01 + 14.0 * db14);



    	/* temperature */
        z = Math.sqrt(input.alt * input.alt);
        densu(z, 1.0, tinf, tlb, 0.0, 0.0, output.t[1], ptm[5], s, mn1, zn1, meso_tn1, meso_tgn1);
        if (bool(flags.sw[0])) {
            for (i = 0; i < 9; i++)
                output.d[i] = output.d[i] * 1.0E6;
            output.d[5] = output.d[5] / 1000;
        }
    }

    public double densityNow(double ECIx, double ECIy, double ECIz, Calendar c) {
        struct_nrlmsise_output output = new struct_nrlmsise_output();
        struct_nrlmsise_input input = new struct_nrlmsise_input();
        struct_nrlmsise_flags flags = new struct_nrlmsise_flags();
        struct_ap_array aph = new struct_ap_array();
    	/* input values */
        for (int i = 0; i < 7; i++)
            aph.a[i] = 0;
        aph.a[0] = 4;
        flags.switches[0] = 0;
        for (int i = 1; i < 24; i++)
            flags.switches[i] = 1;

        ArrayList<ArrayList<Double>> help = new ArrayList<>();
        ArrayList<ArrayList<Double>> ECEFCoordinates;
        double[] LLACoordinates;
        ArrayList<Double> ECICoordinates = new ArrayList<>();
        Collections.addAll(ECICoordinates, ECIx, ECIy, ECIz);
        help.add(ECICoordinates);

        if (precession == null) {
            precession = ECEF_ECI_CONVERSION.precessionMatrix(c);
            nutation = ECEF_ECI_CONVERSION.nutationMatrix(c);
            pole = ECEF_ECI_CONVERSION.poleMatrix(c);
        }
        earthRotation = ECEF_ECI_CONVERSION.rotationMatrix(c);

        ArrayList<ArrayList<Double>> cosine = matrixMult(matrixMult(matrixMult(pole, earthRotation), nutation), precession);
        ECEFCoordinates = matrixMult(cosine, transpose(help));
        LLACoordinates = ECEF2LLA.conversion(ECEFCoordinates.get(0).get(0), ECEFCoordinates.get(1).get(0), ECEFCoordinates.get(2).get(0));

//        System.out.println("LLA = " + Arrays.toString(LLACoordinates));

        input.doy = c.get(GregorianCalendar.DAY_OF_YEAR);
        input.year = c.get(GregorianCalendar.YEAR); /* without effect */
        input.sec = c.get(GregorianCalendar.HOUR_OF_DAY) * 3600 + c.get(GregorianCalendar.MINUTE) * 60 + c.get(GregorianCalendar.SECOND);
        input.alt = LLACoordinates[2] / 1000;
        input.g_lat = LLACoordinates[0];
        input.g_long = LLACoordinates[1];
        input.lst = input.sec / 3600 + input.g_long / 15;
        input.f107A = 150;
        input.f107 = 150;
        input.ap = 4;
        input.ap_a = aph;

        gtd7(input, flags, output);

        return output.d[5] * 1000; // g/cm^3 -> kg/m^3
    }
}

