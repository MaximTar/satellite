/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2002 The JAT Project. All rights reserved.
 *
 * This file is part of JAT. JAT is free software; you can
 * redistribute it and/or modify it under the terms of the
 * NASA Open Source Agreement, version 1.3 or later.
 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * NASA Open Source Agreement for more details.
 *
 * You should have received a copy of the NASA Open Source Agreement
 * along with this program; if not, write to the NASA Goddard
 * Space Flight Center at opensource@gsfc.nasa.gov.
 *
 */

/*

Original comments from Jim Baer:

This class contains the methods necessary to parse the JPL DE405 ephemeris files
(text versions), and compute the position and velocity of the planets, Moon, and Sun.

IMPORTANT: In order to use these methods, the user should:
- save this class in a directory of his/her choosing;
- save to the same directory the text versions of the DE405 ephemeris files,
which must be named  "ASCPxxxx.txt", where xxxx represents the start-year of the
20-year block;
- have at least Java 1.1.8 installed.

The input is the julian date (jultime) for which the ephemeris is needed.
Note that only julian dates from 2414992.5 to 2524624.5 are supported.
This input must be specified in the "main" method, which contains the call to
"planetary_ephemeris".

GENERAL IDEA:  The "get_ephemeris_coefficients" method reads the ephemeris file
corresponding to the input julian day, and stores the ephemeris coefficients needed
to calculate planetary positions and velocities in the array "ephemeris_coefficients".
The "get_planet_posvel" method calls "get_ephemeris_coefficients" if needed, then
calculates the position and velocity of the specified planet.
The "planetary_ephemeris" method calls "get_planet_posvel" for each planet, and
resolves the position and velocity of the Earth/Moon barycenter and geocentric
Moon into the position and velocity of the Earth and Moon.

Since the "ephemeris_coefficients" array is declared as an instance variable, its
contents will remain intact, should this code be modified to call "planetary_ephemeris"
more than once.  As a result, assuming the julian date of the subsequent call fell
within the same 20-year file as the initial call, there would be no need to reread
the ephemeris file; this would save on i/o time.

The outputs are the arrays "planet_r" and "planet_rprime", also declared as instance
variables.

Several key constants and variables follow.  As noted, they are configured for DE405;
however, they could be adjusted to use the DE200 ephemeris, whose format is quite
similar.
*/

package jat.eph;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jat.matvec.data.VectorN;
import jat.util.FileUtil;

/** compute planet positions and velocities from JPL DE405 Ephemerides
 */
public class DE405
{
	/*  DECLARE CLASS CONSTANTS  */

	/*	  Length of an A.U., in km	*/
	static final double au = 149597870.691;

	/*  Declare Class Variables  */

	/*	  Ratio of mass of Earth to mass of Moon	*/
	static double emrat = 81.3005600000000044;

	/*	  Chebyshev coefficients for the DE405 ephemeris are contained in the files
	"ASCPxxxx.txt".  These files are broken into intervals of length "interval_duration",
	in days.	*/
	static int interval_duration = 32;

	/*	  Each interval contains an interval number, length, start and end 
     * jultimes, and Chebyshev coefficients for the planets, nutation and
     * libration.  We keep only the coefficients for planets and libration. */
    static int numbers_per_file_interval = 1018;
	static int numbers_per_kept_interval = 936;
    
    /*  Number planets we track the ephemeris of (including the Sun and Moon).
     * Moon's libration gets its own slot and is treated like a planet, too.
     */
    static int num_planets = 12;

	/*	  For each planet (and the Moon makes 10, and the Sun makes 11, and the
     * Moon's libration makes 12), each interval contains several complete 
     * sets of coefficients, each covering a fraction of the interval 
     * duration	*/
	static int number_of_coef_sets_1 = 4;
	static int number_of_coef_sets_2 = 2;
	static int number_of_coef_sets_3 = 2;
	static int number_of_coef_sets_4 = 1;
	static int number_of_coef_sets_5 = 1;
	static int number_of_coef_sets_6 = 1;
	static int number_of_coef_sets_7 = 1;
	static int number_of_coef_sets_8 = 1;
	static int number_of_coef_sets_9 = 1;
	static int number_of_coef_sets_10 = 8;
	static int number_of_coef_sets_11 = 2;
	static int number_of_coef_sets_12 = 4; // For Moon libration
    
	/*	  Each planet (and the Moon makes 10, and the Sun makes 11, and the
     * Moon's libration makes 12) has a different number of Chebyshev 
     * coefficients used to calculate each component of position
	 * and velocity.	*/
	static int number_of_coefs_1 = 14;
	static int number_of_coefs_2 = 10;
	static int number_of_coefs_3 = 13;
	static int number_of_coefs_4 = 11;
	static int number_of_coefs_5 = 8;
	static int number_of_coefs_6 = 7;
	static int number_of_coefs_7 = 6;
	static int number_of_coefs_8 = 6;
	static int number_of_coefs_9 = 6;
	static int number_of_coefs_10 = 13;
	static int number_of_coefs_11 = 11;
	static int number_of_coefs_12 = 10; // For Moon libration

    /** We statically cache the ephemeris files we read in so
     * that lots of instances can use the same data without
     * rereading it.  We keep a count of how many instances
     * are using an ephemeris so we know when it can be
     * garbage collected.
     */
    private static class CachedEphemeris {
      public final String name;
      public final double[] data;
      public int numUsers = 0;
      public CachedEphemeris(String inName, double[] inData) {
        name = inName;
        data = inData;
      }
    }
    /** The cache.  Maps the file name to the cached data. */
//    private static Map<String, CachedEphemeris> cachedEphemeris =
//      Collections.synchronizedMap(new HashMap<String, CachedEphemeris>());
    private static Map<String, CachedEphemeris> cachedEphemeris =
        (new HashMap<String, CachedEphemeris>());
    
	/*  DEFINE INSTANCE VARIABLES  */

	/*  Define ephemeris dates and coefficients as instance variables  */
    CachedEphemeris cached = null;
	double[] ephemeris_coefficients = null;
	double[] ephemeris_dates = new double[3];

	/* Define the positions and velocities of the major planets as instance variables.
	Note that the first subscript is the planet number, while the second subscript
	specifies x, y, or z component.  	*/
	public double[][] planet_r = new double[num_planets+1][4];
	public double[][] planet_rprime = new double[num_planets+1][4];

	String path;

	public final static int MERCURY = 1;
	public final static int VENUS = 2;
	public final static int EARTH = 3;
	public final static int MARS = 4;
	public final static int JUPITER = 5;
	public final static int SATURN = 6;
	public final static int PLUTO = 9;
	public final static int MOON = 10;
	public final static int SUN = 11;
    private final static int LIBRATION = 12;

	protected VectorN r_moon_geo;
	protected VectorN r_sun_geo;
	
	public DE405(){
		String filesep = FileUtil.file_separator();
        String directory;
        try{
            directory = FileUtil.getClassFilePath("jat.eph","DE405");
        }catch(Exception e){
        	System.err.println("Error: Could not read default DE405 path.");
            directory = "C:/Code/Jat/jat/eph/";
        }
        this.path = directory+filesep+"DE405data"+filesep;
	}
	
	public DE405(String DE405_path)
	{
		this.path = DE405_path;
	}

	public void planetary_ephemeris(double jultime)
	{
		/* Procedure to calculate the position and velocity at jultime of the major
		planets. Note that the planets are enumerated as follows:  Mercury = 1,
		Venus = 2, Earth-Moon barycenter = 3, Mars = 4, ... , Pluto = 9,
		Geocentric Moon = 10, Sun = 11.  		*/

		int i = 0, j = 0;

		double[] ephemeris_r = new double[4];
		double[] ephemeris_rprime = new double[4];

		/*  Get the ephemeris positions and velocities of each major planet  */
		for (i = 1; i <= num_planets; i++)
		{
			get_planet_posvel(jultime, i, ephemeris_r, ephemeris_rprime);
			for (j = 1; j <= 3; j++)
			{
				planet_r[i][j] = ephemeris_r[j];
				planet_rprime[i][j] = ephemeris_rprime[j];
			}
		}

		r_moon_geo = new VectorN(planet_r[10][1],planet_r[10][2],planet_r[10][3]);
		
		/*  The positions and velocities of the Earth and Moon are found indirectly.
		We already have the pos/vel of the Earth-Moon barycenter (i = 3).  We have
		also calculated planet_r(10,j), a geocentric vector from the Earth to the Moon.
		Using the ratio of masses, we get vectors from the Earth-Moon barycenter to the
		Moon and to the Earth.  */
		for (j = 1; j <= 3; j++)
		{
			planet_r[3][j] = planet_r[3][j] - planet_r[10][j] / (1 + emrat);
			planet_r[10][j] = planet_r[3][j] + planet_r[10][j];
			planet_rprime[3][j] = planet_rprime[3][j] - planet_rprime[10][j] / (1 + emrat);
			planet_rprime[10][j] = planet_rprime[3][j] + planet_rprime[10][j];
		}
		
		r_sun_geo = new VectorN(3);
		r_sun_geo.x[0] = planet_r[SUN][1]-planet_r[EARTH][1];
		r_sun_geo.x[1] = planet_r[SUN][2]-planet_r[EARTH][2];
		r_sun_geo.x[2] = planet_r[SUN][3]-planet_r[EARTH][3];
	}

	void get_planet_posvel(double jultime, int i, double ephemeris_r[], double ephemeris_rprime[])
	{
		/*
		  Procedure to calculate the position and velocity of planet i, subject to the
		  JPL DE405 ephemeris.  The positions and velocities are calculated using Chebyshev
		  polynomials, the coefficients of which are stored in the files "ASCPxxxx.txt".
		  The general idea is as follows:  First, check to be sure the proper ephemeris
		  coefficients (corresponding to jultime) are available.  Then read the coefficients
		  corresponding to jultime, and calculate the positions and velocities of the planet.
		*/

		int interval = 0, numbers_to_skip = 0, pointer = 0, j = 0, k = 0, subinterval = 0;

		double interval_start_time = 0, subinterval_duration = 0, chebyshev_time = 0;

		double[] position_poly = new double[20];
		double[][] coef = new double[4][20];
		double[] velocity_poly = new double[20];

		int[] number_of_coef_sets = new int[num_planets+1];
		int[] number_of_coefs = new int[num_planets+1];

		/*		  Initialize arrays		*/
		number_of_coefs[1] = number_of_coefs_1;
		number_of_coefs[2] = number_of_coefs_2;
		number_of_coefs[3] = number_of_coefs_3;
		number_of_coefs[4] = number_of_coefs_4;
		number_of_coefs[5] = number_of_coefs_5;
		number_of_coefs[6] = number_of_coefs_6;
		number_of_coefs[7] = number_of_coefs_7;
		number_of_coefs[8] = number_of_coefs_8;
		number_of_coefs[9] = number_of_coefs_9;
		number_of_coefs[10] = number_of_coefs_10;
		number_of_coefs[11] = number_of_coefs_11;
        number_of_coefs[12] = number_of_coefs_12;
		number_of_coef_sets[1] = number_of_coef_sets_1;
		number_of_coef_sets[2] = number_of_coef_sets_2;
		number_of_coef_sets[3] = number_of_coef_sets_3;
		number_of_coef_sets[4] = number_of_coef_sets_4;
		number_of_coef_sets[5] = number_of_coef_sets_5;
		number_of_coef_sets[6] = number_of_coef_sets_6;
		number_of_coef_sets[7] = number_of_coef_sets_7;
		number_of_coef_sets[8] = number_of_coef_sets_8;
		number_of_coef_sets[9] = number_of_coef_sets_9;
		number_of_coef_sets[10] = number_of_coef_sets_10;
		number_of_coef_sets[11] = number_of_coef_sets_11;
        number_of_coef_sets[12] = number_of_coef_sets_12;

		/* Begin by determining whether the current ephemeris coefficients are
		   appropriate for jultime, or if we need to load a new set. */
		if ((jultime < ephemeris_dates[1]) || (jultime > ephemeris_dates[2]))
			get_ephemeris_coefficients(jultime);

		interval = (int) (Math.floor((jultime - ephemeris_dates[1]) / interval_duration) + 1);
		interval_start_time = (interval - 1) * interval_duration + ephemeris_dates[1];
		subinterval_duration = interval_duration / number_of_coef_sets[i];
		subinterval = (int) (Math.floor((jultime - interval_start_time) / subinterval_duration) + 1);
		numbers_to_skip = (interval - 1) * numbers_per_kept_interval;

		/* Starting at the beginning of the coefficient array, skip the first
		"numbers_to_skip" coefficients.  This puts the pointer on the first piece
		of data in the correct interval. */
		pointer = numbers_to_skip + 1;

		/*  Skip the coefficients for the first (i-1) planets  */
		for (j = 1; j <= (i - 1); j++)
			pointer = pointer + 3 * number_of_coef_sets[j] * number_of_coefs[j];

		/*  Skip the next (subinterval - 1)*3*number_of_coefs(i) coefficients  */
		pointer = pointer + (subinterval - 1) * 3 * number_of_coefs[i];

		for (j = 1; j <= 3; j++)
		{
			for (k = 1; k <= number_of_coefs[i]; k++)
			{
				/*  Read the pointer'th coefficient as the array entry coef[j][k]  */
				coef[j][k] = ephemeris_coefficients[pointer];
				pointer = pointer + 1;
			}
		}

		/*  Calculate the chebyshev time within the subinterval, between -1 and +1  */
		chebyshev_time =
			2 * (jultime - ((subinterval - 1) * subinterval_duration + interval_start_time)) / subinterval_duration - 1;

		/*  Calculate the Chebyshev position polynomials   */
		position_poly[1] = 1;
		position_poly[2] = chebyshev_time;
		for (j = 3; j <= number_of_coefs[i]; j++)
			position_poly[j] = 2 * chebyshev_time * position_poly[j - 1] - position_poly[j - 2];

		/*  Calculate the position of the i'th planet at jultime  */
		for (j = 1; j <= 3; j++)
		{
			ephemeris_r[j] = 0;
			for (k = 1; k <= number_of_coefs[i]; k++)
				ephemeris_r[j] = ephemeris_r[j] + coef[j][k] * position_poly[k];

			/*  Convert from km to A.U.  */
			//ephemeris_r[j] = ephemeris_r[j]/au;
		}

		/*  Calculate the Chebyshev velocity polynomials  */
		velocity_poly[1] = 0;
		velocity_poly[2] = 1;
		velocity_poly[3] = 4 * chebyshev_time;
		for (j = 4; j <= number_of_coefs[i]; j++)
			velocity_poly[j] =
				2 * chebyshev_time * velocity_poly[j - 1] + 2 * position_poly[j - 1] - velocity_poly[j - 2];

		/*  Calculate the velocity of the i'th planet  */
		for (j = 1; j <= 3; j++)
		{
			ephemeris_rprime[j] = 0;
			for (k = 1; k <= number_of_coefs[i]; k++)
				ephemeris_rprime[j] = ephemeris_rprime[j] + coef[j][k] * velocity_poly[k];
			/*  The next line accounts for differentiation of the iterative formula with
			respect to chebyshev time.  Essentially, if dx/dt = (dx/dct) times (dct/dt),
			the next line includes the factor (dct/dt) so that the units are km/day  */
			ephemeris_rprime[j] = ephemeris_rprime[j] * (2.0 * number_of_coef_sets[i] / interval_duration);

			/*  Convert from km to A.U.  */
			//ephemeris_rprime[j] = ephemeris_rprime[j]/au;

		}
	}

	void get_ephemeris_coefficients(double jultime)
	{
	  /*
	   Procedure to read the DE405 ephemeris file corresponding to jultime.
	   The start and end dates of the ephemeris file are returned, as are the
	   Chebyshev coefficients for Mercury, Venus, Earth-Moon, Mars, Jupiter, Saturn,
	   Uranus, Neptune, Pluto, Geocentric Moon, and Sun.
	   
	   Note that the DE405 ephemeris files should be in the same folder as this class.
	   
	   Tested and verified 7-16-99.
	   */
	  
	  int records = 0;
	  String filename = " ";
	  
	  /*  Select the proper ephemeris file  */
	  if ((jultime >= 2414992.5) && (jultime < 2422320.5))
	  {
	    ephemeris_dates[1] = 2414992.5;
	    ephemeris_dates[2] = 2422320.5;
	    filename = path+"ASCP1900.405";
	    records = 230;
	  } else if ((jultime >= 2422320.5) && (jultime < 2429616.5))
	  {
	    ephemeris_dates[1] = 2422320.5;
	    ephemeris_dates[2] = 2429616.5;
	    filename = path+"ASCP1920.405";
	    records = 229;
	  } else if ((jultime >= 2429616.5) && (jultime < 2436912.5))
	  {
	    ephemeris_dates[1] = 2429616.5;
	    ephemeris_dates[2] = 2436912.5;
	    filename = path+"ASCP1940.405";
	    records = 229;
	  } else if ((jultime >= 2436912.5) && (jultime < 2444208.5))
	  {
	    ephemeris_dates[1] = 2436912.5;
	    ephemeris_dates[2] = 2444208.5;
	    filename = path+"ASCP1960.405";
	    records = 229;
	  } else if ((jultime >= 2444208.5) && (jultime < 2451536.5))
	  {
	    ephemeris_dates[1] = 2444208.5;
	    ephemeris_dates[2] = 2451536.5;
	    filename = path + "ASCP1980.405";
	    records = 230;
	  } else if ((jultime >= 2451536.5) && (jultime < 2458832.5))
	  {
	    ephemeris_dates[1] = 2451536.5;
	    ephemeris_dates[2] = 2458832.5;
	    filename = path + "ASCP2000.405";
	    records = 229;
	  } else if ((jultime >= 2458832.5) && (jultime < 2466128.5))
	  {
	    ephemeris_dates[1] = 2458832.5;
	    ephemeris_dates[2] = 2466128.5;
	    filename = path + "ASCP2020.405";
	    records = 229;
	  } else if ((jultime >= 2466128.5) && (jultime < 2473456.5))
	  {
	    ephemeris_dates[1] = 2466128.5;
	    ephemeris_dates[2] = 2473456.5;
	    filename = path+"ASCP2040.405";
	    records = 230;
	  } else if ((jultime >= 2473456.5) && (jultime < 2480752.5))
	  {
	    ephemeris_dates[1] = 2473456.5;
	    ephemeris_dates[2] = 2480752.5;
	    filename = path+"ASCP2060.405";
	    records = 229;
	  } else if ((jultime >= 2480752.5) && (jultime < 2488048.5))
	  {
	    ephemeris_dates[1] = 2480752.5;
	    ephemeris_dates[2] = 2488048.5;
	    filename = path+"ASCP2080.405";
	    records = 229;
	  } else if ((jultime >= 2488048.5) && (jultime < 2495344.5))
	  {
	    ephemeris_dates[1] = 2488048.5;
	    ephemeris_dates[2] = 2495344.5;
	    filename = path+"ASCP2100.405";
	    records = 229;
	  } else if ((jultime >= 2495344.5) && (jultime < 2502672.5))
	  {
	    ephemeris_dates[1] = 2495344.5;
	    ephemeris_dates[2] = 2502672.5;
	    filename = path+"ASCP2120.405";
	    records = 230;
	  } else if ((jultime >= 2502672.5) && (jultime < 2509968.5))
	  {
	    ephemeris_dates[1] = 2502672.5;
	    ephemeris_dates[2] = 2509968.5;
	    filename = path+"ASCP2140.405";
	    records = 229;
	  } else if ((jultime >= 2509968.5) && (jultime < 2517264.5))
	  {
	    ephemeris_dates[1] = 2509968.5;
	    ephemeris_dates[2] = 2517264.5;
	    filename = path+"ASCP2160.405";
	    records = 229;
	  } else if ((jultime >= 2517264.5)
	      && (jultime < 2524624.5))
	  {
	    ephemeris_dates[1] = 2517264.5;
	    ephemeris_dates[2] = 2524624.5;
	    filename = path+"ASCP2180.405";
	    records = 230;
	  }
      
      useEphemeris(filename, records);
	}
    
    private static double[] readEphemerisFile(String filename, int records) 
    {
      int i = 0, j = 0;
      
      double[] newCoefficients = new double[records * numbers_per_kept_interval + 1];;
      try
      {
			FileReader file = new FileReader(filename);
			BufferedReader buff = new BufferedReader(file);
			
			// Read each record in the file 
            // We want to skip the first two numbers, they are times
            // that can be ignored.  Read the next 816 numbers (the
            // coefficients for computing planet position and velocity),
            // skip the next 80 (coefficients for computing nutation), and
            // read the next 120 (coefficients for computing moon libration)
            
            // Because we are skipping around, we keep track of two
            // numbers.  Where we are in the file, and where we are
            // in the array of coefficients we are saving.
            // The file index resets with each new interval.
            int arrayIndex = 1;
			for (j = 1; j <= records; j++)
			{
                int fileIndex = 1;

                /* The first line contains the record counter and
                 * the number of coefficients (which is already known).
                 * Read the line and ignore.  */
				String line = buff.readLine();
                
                // Now continue reading a line at a time until all lines
                // have been read in.  Each line will have
                // three numbers on it.  Each number takes up
                // exactly 26 characters.
                final int chars_per_number = 26;
				while (fileIndex <= numbers_per_file_interval)
                {
				  line = buff.readLine();
                  for(int columnCtr=0; columnCtr<3; ++columnCtr)
                  {
                    if (((fileIndex >=3) && (fileIndex <=818)) ||
                        ((fileIndex >=899) && (fileIndex <=1018)))
                    {
                      String stringRep = 
                        line.substring(columnCtr*chars_per_number,
                            (columnCtr+1)*chars_per_number);
                      newCoefficients[arrayIndex] =
                        parseFloat(stringRep);
                      ++arrayIndex;
                    }
                    else
                    {
                      // It's one of the leading time values, or the nutation,
                      // or just numbers used to pad the last row.  Ignore.
                    }
                    ++fileIndex;
                  }
                }
			}

			buff.close();

		} catch (IOException e)
		{
			System.out.println("Error = " + e.toString());
		} catch (StringIndexOutOfBoundsException e)
		{
			System.out.println("String index out of bounds at i = " + i);
		}
        
        return newCoefficients;
	}

    /**
     * Parse the strings found in the ephemeris files as double
     * precision floating point numbers.
     * This method is very exacting about character position.
     * @param stringRep the string found in the file
     * @return the number read in
     */
    private static double parseFloat(String stringRep)
    {
        double result = 0;
        int mantissa1 = Integer.parseInt(stringRep.substring(4, 13));
        int mantissa2 = Integer.parseInt(stringRep.substring(13, 22));
        int exponent = Integer.parseInt(stringRep.substring(24, 26));
        if (stringRep.substring(23, 24).equals("+"))
        {
            result = mantissa1 * Math.pow(10, (exponent - 9)) + 
              mantissa2 * Math.pow(10, (exponent - 18));
        }
        else
        {
            result = mantissa1 * Math.pow(10, - (exponent + 9)) +
              mantissa2 * Math.pow(10, - (exponent + 18));
        }
        if (stringRep.substring(1, 2).equals("-"))
        {
            result = -result;
        }
        return result;
    }
    
	/** the position and velocity of the planet at the given Julian date
	 * @param testBody
	 * @param planet Planet number
	 * @param jultime Julian date
	 * @return position and velocity of the planet
	 */
	public double[] get_planet_posvel(DE405 testBody, int planet, double jultime)
	{
		double[] posvel = new double[6];
		double daysec = 3600. * 24.;

		testBody.planetary_ephemeris(jultime);
		posvel[0] = testBody.planet_r[planet][1];
		posvel[1] = testBody.planet_r[planet][2];
		posvel[2] = testBody.planet_r[planet][3];
		posvel[3] = testBody.planet_rprime[planet][1] / daysec;
		posvel[4] = testBody.planet_rprime[planet][2] / daysec;
		posvel[5] = testBody.planet_rprime[planet][3] / daysec;

		return posvel;
	}

	/** the position and velocity of the planet at the given Julian date
	 * @param planet Planet number
	 * @param jultime Julian date
	 * @return position and velocity of the planet
	 */
	public double[] get_planet_posvel(int planet, double jultime)
	{
		double[] posvel = new double[6];
		double daysec = 3600. * 24.;

		planetary_ephemeris(jultime);
		posvel[0] = planet_r[planet][1];
		posvel[1] = planet_r[planet][2];
		posvel[2] = planet_r[planet][3];
		posvel[3] = planet_rprime[planet][1] / daysec;
		posvel[4] = planet_rprime[planet][2] / daysec;
		posvel[5] = planet_rprime[planet][3] / daysec;

		return posvel;
	}

	/** the position of the planet at the given Julian date
	 * @param planet Planet number
	 * @param jultime Julian date
	 * @return position of the planet
	 */
	public double[] get_planet_pos(int planet, double jultime)
	{
		double[] pos = new double[3];

		planetary_ephemeris(jultime);
		pos[0] = planet_r[planet][1];
		pos[1] = planet_r[planet][2];
		pos[2] = planet_r[planet][3];

		return pos;
	}

	/** the position and velocity of the planet at the given Julian date
	 * @param planet Planet number
	 * @param jultime Julian date
	 * @return position and velocity of the planet
	 */
	public VectorN get_pos_vel(int planet, double jultime)
	{
		return new VectorN(get_planet_posvel(planet, jultime));
	}

	/** the position of the planet at the given Julian date
	 * @param planet Planet number
	 * @param jultime Julian date
	 * Julian date
	 * @return position of the planet
	 */
	public VectorN get_pos(int planet, double jultime)
	{
		return new VectorN(get_planet_pos(planet, jultime));
	}

	public VectorN get_Geocentric_Moon_pos(){
	    return this.r_moon_geo;
	}
	
	/** the geocentric position of the moon at the given Julian date
	 * @param jultime Julian Date (TDB)
	 * @return position of the moon [km]
	 */
	public VectorN get_Geocentric_Moon_pos(double jultime){
      return get_Geocentric_Moon_posVel(jultime).get(0, 3);
	}
	
    /** the geocentric velocity of the moon at the given Julian date
     * @param jultime Julian Date (TDB)
     * @return velocity of the moon [km]
     */
    public VectorN get_Geocentric_Moon_vel(double jultime){
        return get_Geocentric_Moon_posVel(jultime).get(3, 3);
    }
    
    /** the geocentric velocity of the moon at the given Julian date
     * @param jultime Julian Date (TDB)
     * @return position and velocity of the moon in one 6 coordinate vector [km]
     */
    public VectorN get_Geocentric_Moon_posVel(double jultime){
        double[] ephemeris_r = new double[4];
        double[] ephemeris_rprime = new double[4];
        get_planet_posvel(jultime, 10, ephemeris_r, ephemeris_rprime);
        VectorN posvel = new VectorN(6);
        for (int j = 1; j <= 3; j++)
        {
            planet_r[10][j] = ephemeris_r[j];
            posvel.set(j-1, ephemeris_r[j]);
        }
        for (int j = 1; j <= 3; j++)
        {
            planet_rprime[10][j] = ephemeris_rprime[j];
            posvel.set(j+2, ephemeris_rprime[j]);
        }
        return posvel;
    }
    
	/** the geocentric position of the sun at the given Julian date
	 * @param jultime Julian Date (TDB)
	 * @return position of the sun [km]
	 */
	public VectorN get_Geocentric_Sun_pos(double jultime){
		/* Procedure to calculate the position and velocity at jultime of the major
		planets. Note that the planets are enumerated as follows:  Mercury = 1,
		Venus = 2, Earth-Moon barycenter = 3, Mars = 4, ... , Pluto = 9,
		Geocentric Moon = 10, Sun = 11.  		*/

		int i = 0, j = 0;

		double[] ephemeris_r = new double[4];
		double[] ephemeris_rprime = new double[4];

		/*  Get the ephemeris positions and velocities of each major planet  */
		i = 10;
		get_planet_posvel(jultime, i, ephemeris_r, ephemeris_rprime);
		for (j = 1; j <= 3; j++)
		{
			planet_r[i][j] = ephemeris_r[j];
			planet_rprime[i][j] = ephemeris_rprime[j];
		}
		i = 11;
			get_planet_posvel(jultime, i, ephemeris_r, ephemeris_rprime);
			for (j = 1; j <= 3; j++)
			{
				planet_r[i][j] = ephemeris_r[j];
				planet_rprime[i][j] = ephemeris_rprime[j];
			}
		i = 3;
			get_planet_posvel(jultime, i, ephemeris_r, ephemeris_rprime);
			for (j = 1; j <= 3; j++)
			{
				planet_r[i][j] = ephemeris_r[j];
				planet_rprime[i][j] = ephemeris_rprime[j];
			}
		
			/*  The positions and velocities of the Earth and Moon are found indirectly.
			We already have the pos/vel of the Earth-Moon barycenter (i = 3).  We have
			also calculated planet_r(10,j), a geocentric vector from the Earth to the Moon.
			Using the ratio of masses, we get vectors from the Earth-Moon barycenter to the
			Moon and to the Earth.  */
			for (j = 1; j <= 3; j++)
			{
				planet_r[3][j] = planet_r[3][j] - planet_r[10][j] / (1 + emrat);
				planet_r[10][j] = planet_r[3][j] + planet_r[10][j];
				planet_rprime[3][j] = planet_rprime[3][j] - planet_rprime[10][j] / (1 + emrat);
				planet_rprime[10][j] = planet_rprime[3][j] + planet_rprime[10][j];
			}
			
			r_sun_geo = new VectorN(3);
			r_sun_geo.x[0] = planet_r[SUN][1]-planet_r[EARTH][1];
			r_sun_geo.x[1] = planet_r[SUN][2]-planet_r[EARTH][2];
			r_sun_geo.x[2] = planet_r[SUN][3]-planet_r[EARTH][3];
			return r_sun_geo;
	}
	
    /**
     * The libration angles of the moon at a given time
     * @param jultime the time
     * @return an array of three angles (phi, theta, psi) all in radians
     */
    public double[] get_Moon_Libration(double jultime)
    {
      return get_planet_pos(LIBRATION, jultime);
    }
    
    /**
     * The libration angles and angular velocity at a given time
     * @param jultime the time
     * @return an array of six doubles.  The first three are the
     * libration angles (phi, theta, psi) in radians.  The second
     * three are angular velocity (phi dot, theta dot, psi dot) in
     * radians/second
     */
    public double[] get_Moon_LibrationVelocity(double jultime)
    {
      return get_planet_posvel(LIBRATION, jultime);
    }
    
    private void useEphemeris(String filename, int records) {
      
      CachedEphemeris oldCached = cached;
      cached = cachedEphemeris.get(filename);
      if (cached == null) {
        double[] newCoefficients = readEphemerisFile(filename, records);
        // We synchronize because several instances may be hitting the
        // cache at once, but we didn't want to lock it while we read the
        // whole file in.
        synchronized(cachedEphemeris) {
          // We check again in case it has been created while we
          // were reading.
          cached = cachedEphemeris.get(filename);
          if (cached == null) {
            // We don't clean out entries as soon as they are released.
            // Instead, we clean them out before we add new ones.
            // This prevents cleaning out and recreating the same cached data.
            cleanCache();
            cached = new CachedEphemeris(filename, newCoefficients);
            cachedEphemeris.put(cached.name, cached);
          }
        }
      }
      // If we were using a previous set of ephemeris, mark that we are
      // not using it anymore.
      if (oldCached != null) {
        --oldCached.numUsers;
      }
      ++cached.numUsers;
      ephemeris_coefficients = cached.data;
    }
    
    /**
     * We need to release our use of the cached ephemeris before
     * we are garbage collected.
     */
    protected void finalize() throws Throwable
    {
      try
      {
        if (cached != null) {
          --cached.numUsers;
        }
      }
      catch (Exception e) {}
      super.finalize();
    }
    
    /**
     *  Simply go through and remove any entries that are not being used.
     */
    private static void cleanCache() {
      Iterator<Map.Entry<String, CachedEphemeris>> iter =
        cachedEphemeris.entrySet().iterator();
      while (iter.hasNext()) {
        Map.Entry<String, CachedEphemeris> next = iter.next();
        if (next.getValue().numUsers == 0) {
          iter.remove();
        }
      }
    }

    /**
     * Test program.  Outputs the computed position and velocity of
     * a planet or the moon's libration.
     * @param args first element should indicate a planet (1-12).
     * Second element should indicate a Julian date.  If neither is
     * supplied it will prompt the user for it.
     */
    public static void main(String[] args) throws IOException
    {
      boolean done = false;
      DE405 ephemeris = new DE405();
      while (!done) 
      {
        BufferedReader input = (args.length >= 2 ? null :
          new BufferedReader(new InputStreamReader(System.in)));
        String planetStr = null;;
        if (args.length == 0)
        {
          System.out.print("Enter a planet (1-9, 10=Moon, 11=Sun, " +
          "12=Moon Libration): ");
          planetStr = input.readLine();
        }
        else 
        {
          planetStr = args[0];
        }
        int planet = Integer.parseInt(planetStr);
        
        String dateStr = null;
        if (args.length < 2) 
        {
          System.out.print("Enter date of interest (Julian format): ");
          dateStr = input.readLine();
        }
        else
        {
          dateStr = args[1];
        }
        int juldate = Integer.parseInt(dateStr);
        
        double[] result = ephemeris.get_planet_posvel(planet, juldate);
        System.out.println("Position: x=" + result[0] + " y=" + result[1] +
            " z=" + result[2]);
        System.out.println("Velocity: vx=" + result[3] + " vy=" + result[4] +
            " vz=" + result[5]);
        
        done = (args.length >= 2);
      }
    }
    
}
