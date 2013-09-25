/*
 * This code is part of the JNavigator project.
 *
 * Copyright (c) 2009-2013 Clemens Krainer <clemens.krainer@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.utilities;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class verifies the implementation of the WGS84 class.
 */
public class WGS84TestCase
{
    /**
     * This test case verifies the implementation of the rectangularToPolarCoordinates() and
     * polarToRectangularCoordinates() methods of class WGS84.
     */
//    @Test
//    public void testCase01()
//    {
//        CartesianCoordinate pos = new CartesianCoordinate(1000, 10, 100);
//        WGS84 gs = new WGS84();
//
//        PolarCoordinate wgs = gs.rectangularToPolarCoordinates(pos);
//        CartesianCoordinate rec = gs.polarToRectangularCoordinates(wgs);
//
//        System.out.println();
//        System.out.println("Rectangular X=" + pos.getX() + " Y=" + pos.getY() + " Z=" + pos.getZ());
//        System.out.println("WGS84       Latitude=" + wgs.getLatitude() + " Longitude=" + wgs.getLongitude()
//            + " Altitude=" + wgs.getAltitude());
//        System.out.println("Rectangular X=" + rec.getX() + " Y=" + rec.getY() + " Z=" + rec.getZ());
//
//        AssertJUnit.assertTrue(Math.abs(rec.getX() - pos.getX()) < 1E-4);
//        AssertJUnit.assertTrue(Math.abs(rec.getY() - pos.getY()) < 1E-4);
//        AssertJUnit.assertTrue(Math.abs(rec.getZ() - pos.getZ()) < 1E-4);
//
//        AssertJUnit.assertTrue(Math.abs(88.66552997271454 - wgs.getLatitude()) < 1E-4);
//        AssertJUnit.assertTrue(Math.abs(0.5729386976834859 - wgs.getLongitude()) < 1E-4);
//        AssertJUnit.assertTrue(Math.abs(-6356640.669229707 - wgs.getAltitude()) < 1E-4);
//    }

    /**
     * This test case verifies the implementation of the rectangularToPolarCoordinates() and
     * polarToRectangularCoordinates() methods of class WGS84.
     */
    @Test
    public void testCase02()
    {
        double latitude = 47.99043439493213;
        double longitude = 12.93670580800686;
        double altitude = 435.94417220;
        WGS84 gs = new WGS84();

        PolarCoordinate wgs = new PolarCoordinate(latitude, longitude, altitude);
        CartesianCoordinate rec = gs.polarToRectangularCoordinates(wgs);
        PolarCoordinate pos = gs.rectangularToPolarCoordinates(rec);

        System.out.println();
        System.out.println("WGS84       Latitude=" + wgs.getLatitude() + " Longitude=" + wgs.getLongitude()
            + " Altitude=" + wgs.getAltitude());
        System.out.println("Rectangular X=" + rec.getX() + " Y=" + rec.getY() + " Z=" + rec.getZ());
        System.out.println("WGS84       Latitude=" + pos.getLatitude() + " Longitude=" + pos.getLongitude()
            + " Altitude=" + pos.getAltitude());

        AssertJUnit.assertTrue(Math.abs(wgs.getLatitude() - pos.getLatitude()) < 1E-4);
        AssertJUnit.assertTrue(Math.abs(wgs.getLongitude() - pos.getLongitude()) < 1E-4);
        AssertJUnit.assertTrue(Math.abs(wgs.getAltitude() - pos.getAltitude()) < 1E-4);

        AssertJUnit.assertTrue(Math.abs(4168246.0564496145 - rec.getX()) < 1E-4);
        AssertJUnit.assertTrue(Math.abs(957466.6063627704 - rec.getY()) < 1E-4);
        AssertJUnit.assertTrue(Math.abs(4716488.496489645 - rec.getZ()) < 1E-4);
    }

    /**
     * This test calculates the elevation of a course.
     */
    @Test
    public void testCase13()
    {
        PolarCoordinate A = new PolarCoordinate(48, 13, 1010);
        PolarCoordinate B = new PolarCoordinate(48.001, 13.002, 1000);
        GeodeticSystem gs = new WGS84();
        CartesianCoordinate a = gs.polarToRectangularCoordinates(A);
        CartesianCoordinate b = gs.polarToRectangularCoordinates(B);

        CartesianCoordinate mv = b.subtract(a);
        double distance = mv.norm();

        double x = a.multiply(mv) / (a.norm() * distance);
        if (x > 1)
            x = 1;
        else if (x < -1)
            x = -1;
        double elevation = Math.toDegrees(Math.asin(x));

        AssertJUnit.assertEquals(-3.1902590577710233, elevation, 1E-9);
    }

    /**
     * This test verifies the walk() method.
     */
    @Test
    public void testCase14()
    {
        PolarCoordinate A = new PolarCoordinate(48, 13, 1010);
        GeodeticSystem gs = new WGS84();

        PolarCoordinate U = gs.walk(A, 100, 0, 0);
        AssertJUnit.assertEquals(47.99910182694469, U.getLatitude(), 1E-8);
        AssertJUnit.assertEquals(A.getLongitude(), U.getLongitude(), 1E-8);
        AssertJUnit.assertEquals(A.getAltitude(), U.getAltitude(), 1E-8);

        PolarCoordinate V = gs.walk(A, 0, 100, 0);
        AssertJUnit.assertEquals(A.getLatitude(), V.getLatitude(), 1E-8);
        AssertJUnit.assertEquals(13.001342298568892, V.getLongitude(), 1E-8);
        AssertJUnit.assertEquals(A.getAltitude(), V.getAltitude(), 1E-8);

        PolarCoordinate W = gs.walk(A, 0, 0, 100);
        AssertJUnit.assertEquals(A.getLatitude(), W.getLatitude(), 1E-8);
        AssertJUnit.assertEquals(A.getLongitude(), W.getLongitude(), 1E-8);
        AssertJUnit.assertEquals(A.getAltitude() + 100, W.getAltitude(), 1E-8);
    }

    @DataProvider
    public Object[][] polarCoordinatesDataProvider()
    {
        return new Object[][]{
            new Object[]{48.001, 13.002, 10},
            new Object[]{48.001, -12.002, 10},
            new Object[]{48.001, -80.002, 10},
            new Object[]{48.001, -122.002, 10}, 
            // X : -2265.872 km, Y : -3625.871 km, Z : 4716.958 km
            new Object[]{-89.001, 80.002, 10},
            new Object[]{89.001, 80.002, 10},
            new Object[]{-89.001, 179.992, 10},
            new Object[]{-89.001, -179.992, 10},
            new Object[]{89.001, -179.992, 10},
            new Object[]{-89.001, 44.992, 10},
            new Object[]{-89.001, -44.992, 10},
        };
    };

    @Test(dataProvider = "polarCoordinatesDataProvider")
    public void shouldConvertCoordinatesToAndFro(double lat, double lon, double alt)
    {
        PolarCoordinate A = new PolarCoordinate(lat, lon, alt);
        WGS84 gs = new WGS84();
        CartesianCoordinate cA = gs.polarToRectangularCoordinates(A);
        PolarCoordinate pA = gs.rectangularToPolarCoordinates(cA);
        
//        PolarCoordinate pA = gs.ecef2wgs(cA.getX(), cA.getY(), cA.getZ());
        Assert.assertEquals(pA.getLatitude(), lat, 1E-8, "Latitude");
        Assert.assertEquals(pA.getLongitude(), lon, 1E-8, "Longitude");
        Assert.assertEquals(pA.getAltitude(), alt, 1E-4, "Altitude");
    }

}
