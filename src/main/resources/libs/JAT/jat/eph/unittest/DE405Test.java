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
 * File created by Rob Antonucci 
 **/
package jat.eph.unittest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jat.eph.DE405;
import jat.spacetime.CalDate;
import jat.spacetime.Time;
import junit.framework.TestCase;

public class DE405Test extends TestCase {
  
  
  private static final int NUM_PLANETS = 11;

  public static void main(String[] args) {
    junit.textui.TestRunner.run(DE405Test.class);
  }

  public void testPosVel() {
    DE405 ephem = new DE405();
    CalDate date = new CalDate(2004,6,1,12,00, 00);
    Time time = new Time(date);
    double[][] targetPosVels = readTargetPosVels();
    boolean createNew = (targetPosVels == null);
    if (createNew) {
      targetPosVels = new double[NUM_PLANETS][6];
    }
    
    for (int planet = 0; planet < NUM_PLANETS; ++planet) {
      double[] posVel = ephem.get_planet_posvel(planet+1, time.jd_tdb());
      for(int index=0; index<6; ++index) {
        if (createNew) {
          targetPosVels[planet][index] = posVel[index];
        }
        else {
          assertEquals(label(index) + " failed for planet " + String.valueOf(planet+1),
              targetPosVels[planet][index], posVel[index]);
        }
      }
    }
    
    if (createNew) {
      writeTargetPosVels(targetPosVels);
      TestCase.fail("No target file to compare to.");
    }
  }
  
  private double[][] readTargetPosVels() {
    
    double[][] targetPosVels = null; 
    try {
      InputStream rstrm = getClass().getClassLoader().
        getResourceAsStream("jat/eph/unittest/ephemeris.txt");
      if (rstrm != null) {
        BufferedReader rrdr = new BufferedReader(new InputStreamReader(rstrm));
        String rLine = rrdr.readLine();
        // First lines may be comments and can be skipped.
        while (rLine.trim().startsWith("#")) {
          rLine = rrdr.readLine();
        }
        targetPosVels = new double[NUM_PLANETS][6];
        for(int planet=0; planet<NUM_PLANETS; ++planet) {
          if (rLine == null) {
            TestCase.fail("Target value file only had " + planet + " lines.  Should have " +
                NUM_PLANETS);
          }
          String[] numberStrs = rLine.trim().split("\\s+");
          for(int index=0; index<6; ++index) {
            targetPosVels[planet][index] = Double.parseDouble(numberStrs[index]);
          }        
          rLine = rrdr.readLine();
        }
      }
    }
    catch (IOException ioe) {
      System.out.println("Error reading ephemeris.txt file: " + ioe.getMessage());
      targetPosVels = null;
    }
  
    return targetPosVels;
  }
  
  private void writeTargetPosVels(double[][] posVels) {
    try
    {
      File outFile = new File(System.getProperty("java.io.tmpdir"),
      "ephemeris.txt");
      BufferedWriter wrtr = new BufferedWriter(new FileWriter(outFile));
      System.out.println("Creating ephemeris file " +
          outFile.getAbsolutePath());
      wrtr.write("#\t Ephemeris file");
      wrtr.newLine();
      wrtr.write("#\trx\t\t\try\t\t\trz\t\tvx\t\t\tvy\t\t\tvz");
      wrtr.newLine();
      
      for(int planet=0; planet<posVels.length; ++planet) {
        for(int index=0; index<6; ++index) {
          wrtr.write(posVels[planet][index] + "\t");
        }
        wrtr.newLine();
      }
      wrtr.close();
    }
    catch (IOException ioe) {
      TestCase.fail(ioe.getMessage());
    }
  }
  
  private String label(int index) {
    String label = (index<3 ? "position" : "velocity");
    switch (index) {
    case 0:
    case 3:
      label = "X " + label;
      break;
    case 1:
    case 4:
      label = "Y " + label;
      break;
    case 2:
    case 5:
      label = "Z " + label;
      break;
    }
    return label;
  }
}
