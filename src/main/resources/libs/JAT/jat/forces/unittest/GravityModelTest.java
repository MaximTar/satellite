package jat.forces.unittest;

import java.io.IOException;

import jat.forces.GravityModel;
import jat.forces.GravityModelType;
import jat.spacecraft.Spacecraft;

public class GravityModelTest extends ForceModelTest {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(GravityModelTest.class);
  }

  /*
   * Test method for 'jat.forces.SolarRadiationPressure.acceleration(Time, BodyRef, Spacecraft)'
   */
  public void testAccelerationTimeBodyRefSpacecraft() throws IOException {
    Spacecraft sc = new Spacecraft();
    sc.set_area(20);
    sc.set_mass(1000);
    GravityModel force = new GravityModel(2, 2, GravityModelType.JGM3);
    
    testForceModelAcceleration(sc, force, "earth_gravity.txt", 
        "Gravity Model using JGM3 Earth Gravity");
  }
}
