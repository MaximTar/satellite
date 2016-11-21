package utils;

import java.util.ArrayList;

public class GeoPot {

    public double x, y, z;
    public static double EarthGravity = 398600.4415E9;
    public static double EarthEquatorialRadius = 6378137;
    public static int k;
    public static double c, s;

    GeoPot(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static GeoPot sum(GeoPot a, GeoPot b) {
        GeoPot c = new GeoPot(0, 0, 0);
        c.x = a.x + b.x;
        c.y = a.y + b.y;
        c.z = a.z + b.z;
        return c;
    }

//    public static GeoPot sub(GeoPot a, GeoPot b) {
//        GeoPot c = new GeoPot(0, 0, 0);
//        c.x = a.x - b.x;
//        c.y = a.y - b.y;
//        c.z = a.z - b.z;
//        return c;
//    }

    public static GeoPot mult(double a, GeoPot b) {
        GeoPot c = new GeoPot(0, 0, 0);
        c.x = a * b.x;
        c.y = a * b.y;
        c.z = a * b.z;
        return c;
    }

    public static GeoPot mult(GeoPot b, double a) {
        GeoPot c = new GeoPot(0, 0, 0);
        c.x = a * b.x;
        c.y = a * b.y;
        c.z = a * b.z;
        return c;
    }

    public static double norm(GeoPot a) {
        return 1 / Math.sqrt((a.x * a.x) + (a.y * a.y) + (a.z * a.z));
    }

    public static GeoPot rev(GeoPot a) {
        return new GeoPot(-a.x, -a.y, -a.z);
    }

    public static ArrayList<Double> calc(double x, double y, double z, int degree) {
        GeoPot rECEF = new GeoPot(x, y, z);
        int nhcs = 1; // initial count for harmonic coefficients
        double rs = norm(rECEF);
        GeoPot XYZoverR = mult(rECEF, rs);
        double rr = rs * rs;

        double[] czon; // Jn coefficients
        czon = new double[13];
        double[] ctes; // Cnk coefficients
        ctes = new double[78];
        double[] stes; // Snk coefficients
        stes = new double[78];

        czon[1] = 0.0;
        czon[2] = -1.082636023e-03;   //  2   0 -1082.63602298
        czon[3] = +2.532435e-06;      //  3   0     2.53243535
        czon[4] = +1.619331e-06;      //  4   0     1.61933121
        czon[5] = +2.277161e-07;      //  5   0     0.22771610
        czon[6] = -5.396485e-07;      //  6   0    -0.53964849
        czon[7] = +3.513684e-07;      //  7   0     0.35136844
        czon[8] = +2.025187e-07;      //  8   0     0.20251872
        czon[9] = +1.193687e-07;      //  9   0     0.11936871
        czon[10] = +2.480569e-07;     // 10   0     0.24805686
        czon[11] = -2.405652e-07;     // 11   0    -0.24056521
        czon[12] = +1.819117e-07;     // 12   0     0.18191170
        ctes[1] = -2.41399954e-10;    //  2   1
        stes[1] = +1.54309997e-09;    //  2   1
        ctes[2] = +2.19279880e-06;    //  3   1
        stes[2] = +2.68011894e-07;    //  3   1
        ctes[3] = -5.08725304e-07;    //  4   1
        stes[3] = -4.49459935e-07;    //  4   1
        ctes[4] = -5.37165102e-08;    //  5   1
        stes[4] = -8.06634638e-08;    //  5   1
        ctes[5] = -5.98779769e-08;    //  6   1
        stes[5] = +2.11646643e-08;    //  6   1
        ctes[6] = +2.05148728e-07;    //  7   1
        stes[6] = +6.93698935e-08;    //  7   1
        ctes[7] = +1.60345872e-08;    //  8   1
        stes[7] = +4.01997816e-08;    //  8   1
        ctes[8] = +9.24192722e-08;    //  9   1
        stes[8] = +1.42365696e-08;    //  9   1
        ctes[9] = +5.17557870e-08;    // 10   1
        stes[9] = -8.12891488e-08;    // 10   1
        ctes[10] = +9.50842760e-09;   // 11   1
        stes[10] = -1.64654645e-08;   // 11   1
        ctes[11] = -3.06800094e-08;   // 12   1
        stes[11] = -2.37844845e-08;   // 12   1
        ctes[12] = +1.57453604e-06;   //  2   2
        stes[12] = -9.03868073e-07;   //  2   2
        ctes[13] = +3.09016045e-07;   //  3   2
        stes[13] = -2.11402398e-07;   //  3   2
        ctes[14] = +7.84122308e-08;   //  4   2
        stes[14] = +1.48155457e-07;   //  4   2
        ctes[15] = +1.05590535e-07;   //  5   2
        stes[15] = -5.23267240e-08;   //  5   2
        ctes[16] = +6.01209884e-09;   //  6   2
        stes[16] = -4.65039481e-08;   //  6   2
        ctes[17] = +3.28449048e-08;   //  7   2
        stes[17] = +9.28231439e-09;   //  7   2
        ctes[18] = +6.57654233e-09;   //  8   2
        stes[18] = +5.38131641e-09;   //  8   2
        ctes[19] = +1.56687424e-09;   //  9   2
        stes[19] = -2.22867920e-09;   //  9   2
        ctes[20] = -5.56284564e-09;   // 10   2
        stes[20] = -3.05712916e-09;   // 10   2
        ctes[21] = +9.54203028e-10;   // 11   2
        stes[21] = -5.09736032e-09;   // 11   2
        ctes[22] = +6.38039765e-10;   // 12   2
        stes[22] = +1.41642228e-09;   // 12   2
        ctes[23] = +1.00558857e-07;   //  3   3
        stes[23] = +1.97201324e-07;   //  3   3
        ctes[24] = +5.92157432e-08;   //  4   3
        stes[24] = -1.20112918e-08;   //  4   3
        ctes[25] = -1.49261539e-08;   //  5   3
        stes[25] = -7.10087714e-09;   //  5   3
        ctes[26] = +1.18226641e-09;   //  6   3
        stes[26] = +1.84313369e-10;   //  6   3
        ctes[27] = +3.52854052e-09;   //  7   3
        stes[27] = -3.06115024e-09;   //  7   3
        ctes[28] = -1.94635815e-10;   //  8   3
        stes[28] = -8.72351950e-10;   //  8   3
        ctes[29] = -1.21727527e-09;   //  9   3
        stes[29] = -5.63392145e-10;   //  9   3
        ctes[30] = -4.19599893e-11;   // 10   3
        stes[30] = -8.98933286e-10;   // 10   3
        ctes[31] = -1.40960772e-10;   // 11   3
        stes[31] = -6.86352078e-10;   // 11   3
        ctes[32] = +1.45191793e-10;   // 12   3
        stes[32] = +9.15457482e-11;   // 12   3
        ctes[33] = -3.98239574e-09;   //  4   4
        stes[33] = +6.52560581e-09;   //  4   4
        ctes[34] = -2.29791235e-09;   //  5   4
        stes[34] = +3.87300507e-10;   //  5   4
        ctes[35] = -3.26413891e-10;   //  6   4
        stes[35] = -1.78449133e-09;   //  6   4
        ctes[36] = -5.85119492e-10;   //  7   4
        stes[36] = -2.63618222e-10;   //  7   4
        ctes[37] = -3.18935802e-10;   //  8   4
        stes[37] = +9.11773560e-11;   //  8   4
        ctes[38] = -7.01856112e-12;   //  9   4
        stes[38] = +1.71730872e-11;   //  9   4
        ctes[39] = -4.96702541e-11;   // 10   4
        stes[39] = -4.62248271e-11;   // 10   4
        ctes[40] = -1.68525661e-11;   // 11   4
        stes[40] = -2.67779792e-11;   // 11   4
        ctes[41] = -2.12381469e-11;   // 12   4
        stes[41] = +9.17051709e-13;   // 12   4
        ctes[42] = +4.30476750e-10;   //  5   5
        stes[42] = -1.64820395e-09;   //  5   5
        ctes[43] = -2.15577115e-10;   //  6   5
        stes[43] = -4.32918170e-10;   //  6   5
        ctes[44] = +5.81848555e-13;   //  7   5
        stes[44] = +6.39725265e-12;   //  7   5
        ctes[45] = -4.61517343e-12;   //  8   5
        stes[45] = +1.61252083e-11;   //  8   5
        ctes[46] = -1.66973699e-12;   //  9   5
        stes[46] = -5.55091854e-12;   //  9   5
        ctes[47] = -3.07428287e-12;   // 10   5
        stes[47] = -3.12226930e-12;   // 10   5
        ctes[48] = +1.48944116e-12;   // 11   5
        stes[48] = +1.98250517e-12;   // 11   5
        ctes[49] = +8.27990199e-13;   // 12   5
        stes[49] = +2.03324862e-13;   // 12   5
        ctes[50] = +2.21369255e-12;   //  6   6
        stes[50] = -5.52771222e-11;   //  6   6
        ctes[51] = -2.49071768e-11;   //  7   6
        stes[51] = +1.05348786e-11;   //  7   6
        ctes[52] = -1.83936427e-12;   //  8   6
        stes[52] = +8.62774317e-12;   //  8   6
        ctes[53] = +8.29672520e-13;   //  9   6
        stes[53] = +2.94031315e-12;   //  9   6
        ctes[54] = -2.59723183e-13;   // 10   6
        stes[54] = -5.51559139e-13;   // 10   6
        ctes[55] = -5.75467116e-15;   // 11   6
        stes[55] = +1.34623363e-13;   // 11   6
        ctes[56] = +7.88309139e-15;   // 12   6
        stes[56] = +9.33540765e-14;   // 12   6
        ctes[57] = +2.55907810e-14;   //  7   7
        stes[57] = +4.47598342e-13;   //  7   7
        ctes[58] = +3.42976182e-13;   //  8   7
        stes[58] = +3.81476567e-13;   //  8   7
        ctes[59] = -2.25197343e-13;   //  9   7
        stes[59] = -1.84679217e-13;   //  9   7
        ctes[60] = +6.90915376e-15;   // 10   7
        stes[60] = -2.65068061e-15;   // 10   7
        ctes[61] = +1.95426202e-15;   // 11   7
        stes[61] = -3.72803733e-14;   // 11   7
        ctes[62] = -4.13155736e-15;   // 12   7
        stes[62] = +7.89991291e-15;   // 12   7
        ctes[63] = -1.58033229e-13;   //  8   8
        stes[63] = +1.53533814e-13;   //  8   8
        ctes[64] = +6.14439391e-14;   //  9   8
        stes[64] = -9.85618446e-16;   //  9   8
        ctes[65] = +4.63531420e-15;   // 10   8
        stes[65] = -1.05284266e-14;   // 10   8
        ctes[66] = -2.92494873e-16;   // 11   8
        stes[66] = +1.17044830e-15;   // 11   8
        ctes[67] = -5.70825414e-16;   // 12   8
        stes[67] = +3.70152251e-16;   // 12   8
        ctes[68] = -3.67676273e-15;   //  9   9
        stes[68] = +7.44103881e-15;   //  9   9
        ctes[69] = +2.33014817e-15;   // 10   9
        stes[69] = -7.01194816e-16;   // 10   9
        ctes[70] = -1.93432044e-16;   // 11   9
        stes[70] = +2.58524487e-16;   // 11   9
        ctes[71] = +1.01272849e-16;   // 12   9
        stes[71] = +6.13664388e-17;   // 12   9
        ctes[72] = +4.17080240e-16;   // 10  10
        stes[72] = -9.89260955e-17;   // 10  10
        ctes[73] = -4.94639649e-17;   // 11  10
        stes[73] = -1.73664923e-17;   // 11  10
        ctes[74] = -1.84017258e-18;   // 12  10
        stes[74] = +9.24242436e-18;   // 12  10
        ctes[75] = +9.35170551e-18;   // 11  11
        stes[75] = -1.40785570e-17;   // 11  11
        ctes[76] = +4.97869995e-19;   // 12  11
        stes[76] = -2.79007835e-19;   // 12  11
        ctes[77] = -2.10894892e-20;   // 12  12
        stes[77] = -9.83829860e-20;   // 12  12

        // derivatives for 1/r x/r y/r z/r with respect to x y z
        GeoPot DR = mult(rev(XYZoverR), rr);
        GeoPot DX = new GeoPot(rs + rECEF.x * DR.x, rECEF.x * DR.y, rECEF.x * DR.z);
        GeoPot DY = new GeoPot(rECEF.y * DR.x, rs + rECEF.y * DR.y, rECEF.y * DR.z);
        GeoPot DZ = new GeoPot(rECEF.z * DR.x, rECEF.z * DR.y, rs + rECEF.z * DR.z);

        // for m = 0 initial values for recurrence process
        // P_n(x)=((2n-1)/n)*x*P_(n-1)(x)-(n-1)*P_(n-2)(x)
        // P_n'(x)=n*P_(n-1)(x)+x*P_(n-1)'(x)
        // P_n'(x)=(2n-1)*P_(n-1)(x)+P_(n-2)'(x)

        double rGeoFM = rs * EarthGravity;
        double rGeoR0 = rs * EarthEquatorialRadius;

        double[] RL = new double[degree + 3];
        double[] PL = new double[degree + 3];
        double[] UL = new double[degree + 3];

        RL[1] = rGeoFM * rGeoR0;
        RL[2] = rGeoR0 * RL[1];
        PL[1] = XYZoverR.z;  // P_1(x)=1 Legendre polynomial m=0
        PL[2] = 1.5 * XYZoverR.z * XYZoverR.z - 0.5; // P_2(x) = 3 / 2 * x * x - 1 / 2 Legendre polynomial
        UL[1] = 1; // derivative P_1'(x)=1 m=0
        UL[2] = 3 * XYZoverR.z; // derivative P_2'(x)=3*x

        for (int n = 3; n <= degree + 1; n++) {

            double j = (double) n;
            RL[n] = rGeoR0 * RL[n - 1];
            PL[n] = ((2 * j - 1) / j) * XYZoverR.z * PL[n - 1] - ((j - 1) / j) * PL[n - 2]; // P_n(x)
            UL[n] = n * PL[n - 1] + XYZoverR.z * UL[n - 1]; // P_n'(x) the first derivative
        } // by using P_n'(x) = n * P_(n-1)(x) + x * P_(n-1)'(x)

        double xam = 1; // initial  cos^m(phi)*cos(m*lambda) but m=0
        double yam = 0; // initial  cos^m(phi)*sin(m*lambda) but m=0
        double xax = 0; // d(cos^m(phi)*cos(m*lambda))/d(x/r) but m=0
        double xay = 0; // d(cos^m(phi)*cos(m*lambda))/d(y/r) but m=0
        double yax = 0; // d(cos^m(phi)*sin(m*lambda))/d(x/r) but m=0
        double yay = 0; // d(cos^m(phi)*sin(m*lambda))/d(y/r) but m=0

        GeoPot egECEF = mult(EarthGravity, DR);

        for (int m = 0; m <= degree; m++) {
            if (m > 0) {
                if (m < 2) {
                    k = 2;
                } else {
                    k = m;
                }

                System.arraycopy(UL, 1, PL, 1, degree + 1);
                for (int n = 1; n <= k - 1; n++) {
                    UL[n] = 0;
                }
                for (int n = k; n <= degree + 1; n++) {

                    double j = (double) n;
                    UL[n] = (2 * j - 1) * PL[n - 1];

                    if (n > (k + 1)) { // in ul[n] are d(p_(nm)(z / r))/d(z / r)
                        UL[n] = UL[n] + UL[n - 2];
                    }
                }

                double xac = xam * XYZoverR.x - yam * XYZoverR.y; // xr is x / r, xac = cos ^ m(phi) * cos(m * lambda)
                double yac = yam * XYZoverR.x + xam * XYZoverR.y; // yr is y / r, yac = cos ^ m(phi) * sin(m * lambda)
                double dxx = xax * XYZoverR.x + xam - yax * XYZoverR.y; // partial derivative with respect to x/r
                double dxy = xay * XYZoverR.x - yay * XYZoverR.y - yam; // partial derivative with respect to y/r
                double dyx = yax * XYZoverR.x + yam + xax * XYZoverR.y; // partial derivative with respect to x/r
                double dyy = yay * XYZoverR.x + xay * XYZoverR.y + xam; // partial derivative with respect to y/r
                xam = xac; // cos ^ m(phi) * cos(m * lambda) for the current m
                yam = yac; // cos ^ m(phi) * sin(m * lambda) for the current m
                xax = dxx; // ...*cos(m *...)partial derivative with respect to x/r
                xay = dxy; // ...*cos(m *...)partial derivative with respect to y/r
                yax = dyx; // ...*sin(m *...)partial derivative with respect to x/r
                yay = dyy; // ...*sin(m *...)partial derivative with respect to y/r
            }

            // to add forces for current m and for m <= n <= N
            if (m < 2) {
                k = 2;
            } else {
                k = m;
            }

            for (int n = k; n <= degree; n++) {
                // for (fm / r)*(r_0 / r) ^ n
                GeoPot U = mult((n + 1) * RL[n - 1], mult(EarthEquatorialRadius, DR)); // d((fm / r) * (r_0 / r) ^ n) / d(1 / r) * d(1 / r) / dr
                // p_(nm)->d ^ m(P_n(z / r)) / d(z / r) ^ m
                GeoPot V = mult(UL[n], DZ); // d(p_(nm) (z / r))/d(z / r) * d(z / r) / (d x, y, z)
                // current values Cnm Snm
                nhcs = nhcs + 1;

                if (m == 0) {
                    c = czon[nhcs];
                    s = 0;
                } else {
                    c = ctes[nhcs];
                    s = stes[nhcs];
                }

                // [C + Snm]*(1 - z ^ 2 / r ^ 2) ^ (m / 2) * [cos + sin](m * lambda)
                GeoPot W = sum(mult((c * xax + s * yax), DX), mult((c * xay + s * yay), DY));

                egECEF = sum(sum(egECEF, mult(mult(U, PL[n]), (c * xam + s * yam))), sum(mult(mult(RL[n], V), (c * xam + s * yam)), mult(RL[n] * PL[n], W))); // current n, m force
            }

            if (m == 1) {
                nhcs = 0;
            }
        }

        ArrayList<Double> GeoPot = new ArrayList<>();
        GeoPot.add(egECEF.x);
        GeoPot.add(egECEF.y);
        GeoPot.add(egECEF.z);

        return GeoPot;
    }

    public String toString() {
        return this.x + " " + this.y + " " + this.z;
    }

//    public static void main(String[] args) {
//    }
}