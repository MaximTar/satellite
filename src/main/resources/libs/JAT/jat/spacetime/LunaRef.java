/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2006 The JAT Project. All rights reserved.
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
 * Emergent Space Technologies
 * File created by Richard C. Page III 
 **/
package jat.spacetime;

import jat.eph.DE405;
//import jat.math.MathUtils;
import jat.matvec.data.Matrix;
import jat.matvec.data.VectorN;

public class LunaRef extends BodyCenteredInertialRef implements BodyRef {
  
	/** Moon's rotation rate in rad/s.
     */
    public final static double omega = 2.6617e-6;  // converted from Vallado - rounded to 5 sigfigs
    
    /** Rotational Period of Moon in days from Vallado
     */
    public final static double RotPeriod = 27.32166; //[days] Vallado
    
    /** Mass of the Moon [kg]
     */
    public final static double M_Luna = 734.9e20; //[kg] HORIZONS
    
    /** Equatorial radius of moon in m 
     */
    public final static double R_Luna = 1737.53e3;      // Radius Moon [m]; HORIZONS +-0.03e3
    
    /** Flattening factor of Moon from WGS-84
     */
    //public final static double f_Moon = ; // STK HPOP - 2
    
    /** Moon gravity constant in m^3/s^2 from HORIZONS
     */
    public final static double GM_Luna    = 4902.798e9;    // [m^3/s^2]; HORIZONS +-0.005e9
    
    /** Moon J2 term for gravity 
     */
    public final static double J2_Luna = 0.0002027;  // from vallado p906
    
    /** Obliquity to orbit for the Moon 6.67 deg converted to radians
     */
    public final static double Obliquity_Luna = 0.11641346110802178278081017425819; //Rad HORIZONS

    /** Lunar orientation parameters
     * Phi, Theta, Phi - euler angles defining the lunar fixed reference frame
     * Phi = angle between ICRF-x and intersection of lunar equator with ICRF-xy
     * Theta = inclination of the lunar equator to the ICRF-xy plane
     * Psi - angle from the intersection between ICRF-lunar to the lunar meridian
     */
    public double phi,theta,psi;
    
    /** Two PI.
     */
    //private static final double pi2 = 2.0*MathUtils.PI;
    
    private Matrix LCI2LCF;
    
    protected DE405 jpl_ephem;
    private VectorN r_sun;
    private VectorN r_earth;
    
    public LunaRef(){
        super(DE405.MOON);
    	jpl_ephem = new DE405();
    }
    
	public double get_spin_rate(Time t) {
		return LunaRef.omega;
	}

	public double get_mean_radius() {
		return LunaRef.R_Luna;
	}

	public double get_grav_const() {
		return LunaRef.GM_Luna;
	}

	public Matrix inertial_to_body(Time t) {
		//*TODO update LCI2LCF
		return this.LCI2LCF;
	}

	public Matrix body_to_inertial(Time t) {
		//*TODO update LCI2LCF
		return this.LCI2LCF.transpose();
	}

    /**
     * Get the current JPL vector to the Sun [km]
     * @return Vector [km]
     */
    public VectorN get_JPL_Sun_Vector(Time t) {
      DE405 jpl_ephemeris = new DE405();
      VectorN moon = jpl_ephemeris.get_pos(DE405.MOON, t.jd_tdb());
      VectorN sun = jpl_ephemeris.get_pos(DE405.SUN, t.jd_tdb());
      return sun.minus(moon);
    }

}
