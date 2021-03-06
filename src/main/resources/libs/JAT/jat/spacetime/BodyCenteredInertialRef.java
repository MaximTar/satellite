/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2005 Emergent Space Technologies Inc. All rights reserved.
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
package jat.spacetime;

import jat.eph.DE405;
import jat.matvec.data.VectorN;

/**
 * Used to represent the inertial reference frame centered at any one
 * of the bodies tracked by the JPL ephemeris files.
 * 
 * @author Rob Antonucci
 *
 */
public class BodyCenteredInertialRef implements ReferenceFrame {
  
    /** Constant to indicate the reference is centered at
     * the barycenter of the solar system.
     */
    public static final int SOLAR_SYSTEM = 0;
    
    /** A code indentifying a body in the JPL ephemeris files.
     * (e.g. Mercury=1, Earth=3, Sun=10, Moon=11).
     * SOLAR_SYSTEM is a special case indicating the origin of
     * all the JPL ephemeris readings. 
     */
    private final int body;
    
    /**
     * Creates a reference frame centered on a central body and
     * using the inertial orientation
     * @param bodyNum a code indentifying a body in the JPL ephemeris
     * files (e.g. Mercury=1, Earth=3, Sun=10, Moon=11).
     * A special case, 0, indicates to use the Barycenter of the solar
     * system (the point from which all JPL ephemeris vectors 
     * originate) but it still uses the orientation used by ECI
     * and LCI.
     */
    public BodyCenteredInertialRef(int bodyNum)
    {
      body = bodyNum;
    }
    
    public int getBody() {
      return body;
    }
    
    /**
     * Creates a translate that can translate between two reference
     * frames at a given time
     * @param other another reference frame
     * @param t the time at which translation will be done
     * @return a translating object or null if the reference frame
     * does not know how to translate to the other reference frame.
     */
    public ReferenceFrameTranslater getTranslater(ReferenceFrame other, Time t)
    {
      ReferenceFrameTranslater xlater = null;
      if (other instanceof BodyCenteredInertialRef) {
        xlater = getTranslater((BodyCenteredInertialRef)other, t);
      }
      else if (other instanceof EarthRef) {
        // EarthRef is just a BodyCenteredInertialRef centered on Earth
        xlater = getTranslater(new BodyCenteredInertialRef(DE405.EARTH), t);
      }
      return xlater;
    }
    
    
    
    /**
     * Creates a translate that can translate between two inertial reference
     * frames at a given time
     * @param other another inertial reference frame
     * @param t the time at which translation will be done
     * @return a translating object
     */
    private ReferenceFrameTranslater getTranslater(BodyCenteredInertialRef other,
        Time t)
    {
      ReferenceFrameTranslater xlater = null;

      if (this.body == other.body)
      {
        xlater = new ReferenceFrameTranslater();
      }
      else
      {
        // Inertial reference frames have the same orientation, so
        // no transformation matrix is needed no matter where the
        // reference frame is centered.
        
        // To determine the distance between origins, we check the JPL
        // ephemeris of the two bodies and difference their positions.
        DE405 jpl_ephemeris = new DE405();
        VectorN state1 = new VectorN(this.body == SOLAR_SYSTEM ?
            new double[6] : jpl_ephemeris.get_planet_posvel(this.body, t.jd_tdb()));
        VectorN state2 = new VectorN(other.body == SOLAR_SYSTEM ?
            new double[6] : jpl_ephemeris.get_planet_posvel(other.body, t.jd_tdb()));
        // We difference and convert to meters (JPL reports kilometers)
        VectorN originDiff = state2.get(0, 3).minus(state1.get(0,3)).times(1000);
        VectorN originVel = state2.get(3, 3).minus(state1.get(3, 3)).times(1000);
        xlater = new ReferenceFrameTranslater(null, originDiff, originVel, null);
      }
      return xlater;
    }
    
    /**
     * Common method to determine difference and velocity between two bodies.
     * @param body1 source body code or SOLAR_SYSTEM for the solar system barycenter
     * @param body2 target body code or SOLAR_SYSTEM for the solar system barycenter
     * @param t the time
     * @return a 6 slot vector.  Slots 1-3 is position vector from body1 to body2.
     * Slots 4-6 is the difference in velocity from body1 to body2
     */
    public static VectorN getPosVelDiff(int body1, int body2, Time t) {
      DE405 jpl_ephemeris = new DE405();
      VectorN posVelDiff = null;
      
      // We can always just difference the solar system barycenter 
      // positions/velocities, but if we are doing Earth/Sun or Earth/Moon,
      // we use specific DE405 Geocentric calls.
      
      if ((body1 == DE405.EARTH) && (body2 == DE405.MOON)) {
        posVelDiff = jpl_ephemeris.get_Geocentric_Moon_posVel(t.jd_tdb());
      }
      else if ((body1 == DE405.MOON) && (body2 == DE405.EARTH)) {
        posVelDiff = jpl_ephemeris.get_Geocentric_Moon_posVel(t.jd_tdb()).times(-1);
      }
      else {
        VectorN state1 = new VectorN(body1 == SOLAR_SYSTEM ?
            new double[6] : jpl_ephemeris.get_planet_posvel(body1, t.jd_tdb()));
        VectorN state2 = new VectorN(body2 == SOLAR_SYSTEM ?
            new double[6] : jpl_ephemeris.get_planet_posvel(body2, t.jd_tdb()));
        // We difference and convert to meters (JPL reports kilometers)
        posVelDiff = state2.minus(state1).times(1000);
      }
      return posVelDiff;
    }
}
