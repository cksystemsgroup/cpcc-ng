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

import org.testng.annotations.Test;
import org.testng.AssertJUnit;

/**
 * This class verifies the implementation of the Position class.
 */
public class CartesianCoordinateTestCase
{
    /**
     * Verify if the default constructor sets all variables zero.
     */
    @Test
    public void testCase01()
    {
        CartesianCoordinate p = new CartesianCoordinate();
        AssertJUnit.assertEquals(0.0, p.getX(), 1E-10);
        AssertJUnit.assertEquals(0.0, p.getY(), 1E-10);
        AssertJUnit.assertEquals(0.0, p.getZ(), 1E-10);
    }

    /**
     * Verify if the constructor using double values assigns the initialization values to the attributes correctly.
     */
    @Test
    public void testCase02()
    {
        CartesianCoordinate u = new CartesianCoordinate(1, 2, 3);
        AssertJUnit.assertEquals(1.0, u.getX(), 1E-10);
        AssertJUnit.assertEquals(2.0, u.getY(), 1E-10);
        AssertJUnit.assertEquals(3.0, u.getZ(), 1E-10);
    }

    /**
     * Verify if the copy constructor works correctly.
     */
    @Test
    public void testCase03()
    {
        CartesianCoordinate v = new CartesianCoordinate(1, 2, 3);
        CartesianCoordinate u = new CartesianCoordinate(v);
        AssertJUnit.assertEquals(1.0, u.getX(), 1E-10);
        AssertJUnit.assertEquals(2.0, u.getY(), 1E-10);
        AssertJUnit.assertEquals(3.0, u.getZ(), 1E-10);

    }

    /**
     * Verify the <code>add()</code> method.
     */
    @Test
    public void testCase04()
    {
        CartesianCoordinate u = new CartesianCoordinate(1, 2, 3);
        CartesianCoordinate v = new CartesianCoordinate(10, 20, 30);
        CartesianCoordinate w = v.add(u);
        AssertJUnit.assertEquals(1.0, u.getX(), 1E-10);
        AssertJUnit.assertEquals(2.0, u.getY(), 1E-10);
        AssertJUnit.assertEquals(3.0, u.getZ(), 1E-10);
        AssertJUnit.assertEquals(10.0, v.getX(), 1E-10);
        AssertJUnit.assertEquals(20.0, v.getY(), 1E-10);
        AssertJUnit.assertEquals(30.0, v.getZ(), 1E-10);
        AssertJUnit.assertEquals(11.0, w.getX(), 1E-10);
        AssertJUnit.assertEquals(22.0, w.getY(), 1E-10);
        AssertJUnit.assertEquals(33.0, w.getZ(), 1E-10);
    }

    /**
     * Verify the <code>subtract()</code> method.
     */
    @Test
    public void testCase05()
    {
        CartesianCoordinate u = new CartesianCoordinate(1, 2, 3);
        CartesianCoordinate v = new CartesianCoordinate(10, 20, 30);
        CartesianCoordinate w = v.subtract(u);
        AssertJUnit.assertEquals(1.0, u.getX(), 1E-10);
        AssertJUnit.assertEquals(2.0, u.getY(), 1E-10);
        AssertJUnit.assertEquals(3.0, u.getZ(), 1E-10);
        AssertJUnit.assertEquals(10.0, v.getX(), 1E-10);
        AssertJUnit.assertEquals(20.0, v.getY(), 1E-10);
        AssertJUnit.assertEquals(30.0, v.getZ(), 1E-10);
        AssertJUnit.assertEquals(9.0, w.getX(), 1E-10);
        AssertJUnit.assertEquals(18.0, w.getY(), 1E-10);
        AssertJUnit.assertEquals(27.0, w.getZ(), 1E-10);
    }

    /**
     * Verify the <code>norm()</code> method.
     */
    @Test
    public void testCase06()
    {
        CartesianCoordinate u = new CartesianCoordinate(10, 20, 30);
        double n = u.norm();
        AssertJUnit.assertEquals(10.0, u.getX(), 1E-10);
        AssertJUnit.assertEquals(20.0, u.getY(), 1E-10);
        AssertJUnit.assertEquals(30.0, u.getZ(), 1E-10);
        AssertJUnit.assertEquals(37.416573867739416, n, 1E-6);
    }

    /**
     * Verify the <code>set()</code> method.
     */
    @Test
    public void testCase07()
    {
        CartesianCoordinate p = new CartesianCoordinate();
        p.set(new CartesianCoordinate(1, 2, 3));
        AssertJUnit.assertEquals(1.0, p.getX(), 1E-10);
        AssertJUnit.assertEquals(2.0, p.getY(), 1E-10);
        AssertJUnit.assertEquals(3.0, p.getZ(), 1E-10);
    }

    /**
     * Verify the <code>setX()</code>, <code>setY()</code> and <code>setZ()</code> methods.
     */
    @Test
    public void testCase08()
    {
        CartesianCoordinate p = new CartesianCoordinate();
        p.setX(1);
        p.setY(2);
        p.setZ(3);
        AssertJUnit.assertEquals(1.0, p.getX(), 1E-10);
        AssertJUnit.assertEquals(2.0, p.getY(), 1E-10);
        AssertJUnit.assertEquals(3.0, p.getZ(), 1E-10);
    }

    @Test
    public void testCase09()
    {
        double a = 10;
        double c = 3;

        CartesianCoordinate x = new CartesianCoordinate(5, 5, (c / a) * Math.sqrt(a * a - 5 * 5 - 5 * 5));
        CartesianCoordinate ref = new CartesianCoordinate(5, 6, (c / a) * Math.sqrt(a * a - 5 * 5 - 6 * 6));
        CartesianCoordinate y = new CartesianCoordinate(5, 6, 1.7);

        PolarCoordinate X = r2p(x);
        PolarCoordinate Y = r2p(y);
        AssertJUnit.assertNotNull(X);
        AssertJUnit.assertNotNull(Y);

        CartesianCoordinate xy = y.subtract(x);

        double nz = (a / c) * Math.sqrt(a * a - x.getX() * x.getX() - x.getY() * x.getY());
        CartesianCoordinate n = new CartesianCoordinate(x.getX(), x.getY(), nz);

        double phi =
            Math.asin(Math.abs(n.getX() * xy.getX() + n.getY() * xy.getY() + n.getZ() * xy.getZ())
                / (n.norm() * xy.norm()));

        phi /= PI180TH;
        if (ref.getZ() > y.getZ())
        {
            phi = -phi;
        }
    }

    /**
     * Verify the <code>toString()</code> method.
     */
    @Test
    public void testCase10()
    {
        CartesianCoordinate p = new CartesianCoordinate(1, 2, 3);
        String coordinateString = p.toString();
        AssertJUnit.assertEquals("(1.0m, 2.0m, 3.0m)", coordinateString);
    }

    /**
     * Verify the <code>normalize()</code> method.
     */
    @Test
    public void testCase11()
    {
        CartesianCoordinate p = new CartesianCoordinate(1, 2, 3);
        CartesianCoordinate n = p.normalize();
        AssertJUnit.assertEquals(1, n.norm(), 1E-9);
        AssertJUnit.assertEquals(1 / Math.sqrt(14), n.getX(), 1E-9);
        AssertJUnit.assertEquals(2 / Math.sqrt(14), n.getY(), 1E-9);
        AssertJUnit.assertEquals(3 / Math.sqrt(14), n.getZ(), 1E-9);
    }

    /**
     * Verify the <code>normalize()</code> method if the norm of the coordinate is zero.
     */
    @Test
    public void testCase12()
    {
        CartesianCoordinate p = new CartesianCoordinate(0, 0, 0);
        AssertJUnit.assertEquals(0, p.norm(), 1E-9);
        CartesianCoordinate n = p.normalize();
        AssertJUnit.assertEquals(1, n.norm(), 1E-9);
        AssertJUnit.assertEquals(1, n.getX(), 1E-9);
        AssertJUnit.assertEquals(0, n.getY(), 1E-9);
        AssertJUnit.assertEquals(0, n.getZ(), 1E-9);
    }

    /**
     * Verify the <code>crossProduct()</code> method.
     */
    @Test
    public void testCase13()
    {
        CartesianCoordinate a = new CartesianCoordinate(1, 0, 0);
        CartesianCoordinate b = new CartesianCoordinate(0, 1, 0);

        CartesianCoordinate c = a.crossProduct(b);
        AssertJUnit.assertEquals(0, c.getX(), 1E-9);
        AssertJUnit.assertEquals(0, c.getY(), 1E-9);
        AssertJUnit.assertEquals(1, c.getZ(), 1E-9);

        c = b.crossProduct(a);
        AssertJUnit.assertEquals(0, c.getX(), 1E-9);
        AssertJUnit.assertEquals(0, c.getY(), 1E-9);
        AssertJUnit.assertEquals(-1, c.getZ(), 1E-9);
    }

    /**
     * Verify the <code>crossProduct()</code> method.
     */
    @Test
    public void testCase14()
    {
        CartesianCoordinate a = new CartesianCoordinate(0, 1, 0);
        CartesianCoordinate b = new CartesianCoordinate(0, 0, 1);

        CartesianCoordinate c = a.crossProduct(b);
        AssertJUnit.assertEquals(1, c.getX(), 1E-9);
        AssertJUnit.assertEquals(0, c.getY(), 1E-9);
        AssertJUnit.assertEquals(0, c.getZ(), 1E-9);

        c = b.crossProduct(a);
        AssertJUnit.assertEquals(-1, c.getX(), 1E-9);
        AssertJUnit.assertEquals(0, c.getY(), 1E-9);
        AssertJUnit.assertEquals(0, c.getZ(), 1E-9);
    }

    /**
     * Verify the <code>crossProduct()</code> method.
     */
    @Test
    public void testCase15()
    {
        CartesianCoordinate a = new CartesianCoordinate(1, 0, 0);
        CartesianCoordinate b = new CartesianCoordinate(0, 0, 1);

        CartesianCoordinate c = a.crossProduct(b);
        AssertJUnit.assertEquals(0, c.getX(), 1E-9);
        AssertJUnit.assertEquals(-1, c.getY(), 1E-9);
        AssertJUnit.assertEquals(0, c.getZ(), 1E-9);

        c = b.crossProduct(a);
        AssertJUnit.assertEquals(0, c.getX(), 1E-9);
        AssertJUnit.assertEquals(1, c.getY(), 1E-9);
        AssertJUnit.assertEquals(0, c.getZ(), 1E-9);
    }

    /**
     * Verify the <code>crossProduct()</code> method.
     */
    @Test
    public void testCase16()
    {
        CartesianCoordinate a = new CartesianCoordinate(1, 2, 3);
        CartesianCoordinate b = new CartesianCoordinate(4, 5, 6);

        CartesianCoordinate c = a.crossProduct(b);
        AssertJUnit.assertEquals(-3, c.getX(), 1E-9);
        AssertJUnit.assertEquals(6, c.getY(), 1E-9);
        AssertJUnit.assertEquals(-3, c.getZ(), 1E-9);

        c = b.crossProduct(a);
        AssertJUnit.assertEquals(3, c.getX(), 1E-9);
        AssertJUnit.assertEquals(-6, c.getY(), 1E-9);
        AssertJUnit.assertEquals(3, c.getZ(), 1E-9);
    }

    public static double PI180TH = Math.PI / 180;

    public static CartesianCoordinate p2r(PolarCoordinate p)
    {

        double x = p.getAltitude() * Math.cos(p.getLatitude() * PI180TH) * Math.cos(p.getLongitude() * PI180TH);
        double y = p.getAltitude() * Math.cos(p.getLatitude() * PI180TH) * Math.sin(p.getLongitude() * PI180TH);
        double z = p.getAltitude() * Math.sin(p.getLatitude() * PI180TH);

        return new CartesianCoordinate(x, y, z);
    }

    public static PolarCoordinate r2p(CartesianCoordinate p)
    {

        double N = Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY() + p.getZ() * p.getZ());
        double altitude = N;
        double latitude = Math.asin(p.getZ() / N);
        double longitude = Math.asin(p.getY() / (N * Math.cos(latitude)));

        return new PolarCoordinate(latitude / PI180TH, longitude / PI180TH, altitude);
    }

}
