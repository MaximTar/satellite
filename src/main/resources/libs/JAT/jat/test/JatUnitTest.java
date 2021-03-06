package jat.test;

import junit.framework.Test;
import junit.framework.TestSuite;


public final class JatUnitTest {

  ///////////////////////////////////////////////////////////////////////
  // Main operation

  /**
     * This is the method called to start the test application.
     * @param args
     */
  public static void main(final String[] args) {
        junit.swingui.TestRunner.run(JatUnitTest.class);
    }

  ///////////////////////////////////////////////////////////////////////
  // Test Operations

  /**
   * Launch all the tests of the suite.
   * @return Test
   */
    public static Test suite() {
      TestSuite suite = new TestSuite("JAT Unit Test Suite");
      suite.addTestSuite(jat.forces.unittest.CIRAExponentialDragTest.class);
      suite.addTestSuite(jat.forces.unittest.GravityModelTest.class);
      suite.addTestSuite(jat.forces.unittest.HarrisPriesterTest.class);
      suite.addTestSuite(jat.forces.unittest.NRLMSISEDragTest.class);
      suite.addTestSuite(jat.forces.unittest.SolarRadiationPressureTest.class);
      suite.addTestSuite(jat.spacetime.unittest.BodyCenteredInertialRefTest.class);
      suite.addTestSuite(jat.spacetime.unittest.LunaFixedRefTest.class);
      return suite;
    }

}
