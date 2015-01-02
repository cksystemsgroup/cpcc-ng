// This code is part of the JNavigator project.
//
// Copyright (c) 2009-2013 Clemens Krainer <clemens.krainer@gmail.com>
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software Foundation,
// Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

package at.uni_salzburg.cs.cpcc.core.utils;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;

public class PolarCoordinateTestCase
{

    /**
     * Verify if the default constructor sets all variables zero.
     */
    @Test
    public void testCase01()
    {
        PolarCoordinate p = new PolarCoordinate();
        AssertJUnit.assertEquals(0.0, p.getLatitude(), 1E-10);
        AssertJUnit.assertEquals(0.0, p.getLongitude(), 1E-10);
        AssertJUnit.assertEquals(0.0, p.getAltitude(), 1E-10);
    }

    /**
     * Verify if the constructor using double values assigns the initialization values to the attributes correctly.
     */
    @Test
    public void testCase02()
    {
        PolarCoordinate u = new PolarCoordinate(1, 2, 3);
        AssertJUnit.assertEquals(1.0, u.getLatitude(), 1E-10);
        AssertJUnit.assertEquals(2.0, u.getLongitude(), 1E-10);
        AssertJUnit.assertEquals(3.0, u.getAltitude(), 1E-10);
    }

    /**
     * Verify if the constructor using another <code>PolarCoordinate</code> as input absorbs the coordinate values
     * correctly.
     */
    @Test
    public void testCase03()
    {
        PolarCoordinate v = new PolarCoordinate(1, 2, 3);
        PolarCoordinate u = new PolarCoordinate(v);
        AssertJUnit.assertEquals(1.0, u.getLatitude(), 1E-10);
        AssertJUnit.assertEquals(2.0, u.getLongitude(), 1E-10);
        AssertJUnit.assertEquals(3.0, u.getAltitude(), 1E-10);
    }

    /**
     * Verify if the setter methods work correctly.
     */
    @Test
    public void testCase04()
    {
        PolarCoordinate u = new PolarCoordinate();
        u.setLatitude(1);
        u.setLongitude(2);
        u.setAltitude(3);
        AssertJUnit.assertEquals(1.0, u.getLatitude(), 1E-10);
        AssertJUnit.assertEquals(2.0, u.getLongitude(), 1E-10);
        AssertJUnit.assertEquals(3.0, u.getAltitude(), 1E-10);
    }

    /**
     * Verify if the setter method for setting all coordinate values works correctly.
     */
    @Test
    public void testCase05()
    {
        PolarCoordinate v = new PolarCoordinate(1, 2, 3);
        PolarCoordinate u = new PolarCoordinate();
        u.set(v);
        AssertJUnit.assertEquals(1.0, u.getLatitude(), 1E-10);
        AssertJUnit.assertEquals(2.0, u.getLongitude(), 1E-10);
        AssertJUnit.assertEquals(3.0, u.getAltitude(), 1E-10);
    }

    /**
     * Verify the <code>toString()</code> method.
     */
    @Test
    public void testCase06()
    {
        PolarCoordinate v = new PolarCoordinate(1, 2, 3);
        String cooardinateString = v.toString();
        AssertJUnit.assertEquals("(1.0\u00B0, 2.0\u00B0, 3.0m)", cooardinateString);
    }
}
