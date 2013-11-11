/*
 * This code is part of the CPCC-NG project.
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

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import org.testng.annotations.Test;

/**
 * This class verifies the implementation of the Position class.
 */
public class CartesianCoordinateTestCase
{
    /**
     * Verify if the default constructor sets all variables zero.
     */
    @Test
    public void createdSutShouldHaveZeroCoordinates()
    {
        CartesianCoordinate p = new CartesianCoordinate();
        assertThat(p.getX()).isEqualTo(0.0, offset(1E-10));
        assertThat(p.getY()).isEqualTo(0.0, offset(1E-10));
        assertThat(p.getZ()).isEqualTo(0.0, offset(1E-10));
    }

    /**
     * Verify if the constructor using double values assigns the initialization values to the attributes correctly.
     */
    @Test
    public void createdSutShouldHaveGivenCoordinates()
    {
        CartesianCoordinate u = new CartesianCoordinate(1, 2, 3);
        assertThat(u.getX()).isEqualTo(1.0, offset(1E-10));
        assertThat(u.getY()).isEqualTo(2.0, offset(1E-10));
        assertThat(u.getZ()).isEqualTo(3.0, offset(1E-10));
    }

    /**
     * Verify if the copy constructor works correctly.
     */
    @Test
    public void constructorShouldCopyCoordinates()
    {
        CartesianCoordinate v = new CartesianCoordinate(1, 2, 3);
        CartesianCoordinate u = new CartesianCoordinate(v);
        assertThat(u.getX()).isEqualTo(1.0, offset(1E-10));
        assertThat(u.getY()).isEqualTo(2.0, offset(1E-10));
        assertThat(u.getZ()).isEqualTo(3.0, offset(1E-10));

    }

    /**
     * Verify the <code>add()</code> method.
     */
    @Test
    public void shouldAddCoodinatesCorrectly()
    {
        CartesianCoordinate u = new CartesianCoordinate(1, 2, 3);
        CartesianCoordinate v = new CartesianCoordinate(10, 20, 30);
        CartesianCoordinate w = v.add(u);
        assertThat(u.getX()).isEqualTo(1.0, offset(1E-10));
        assertThat(u.getY()).isEqualTo(2.0, offset(1E-10));
        assertThat(u.getZ()).isEqualTo(3.0, offset(1E-10));
        assertThat(v.getX()).isEqualTo(10.0, offset(1E-10));
        assertThat(v.getY()).isEqualTo(20.0, offset(1E-10));
        assertThat(v.getZ()).isEqualTo(30.0, offset(1E-10));
        assertThat(w.getX()).isEqualTo(11.0, offset(1E-10));
        assertThat(w.getY()).isEqualTo(22.0, offset(1E-10));
        assertThat(w.getZ()).isEqualTo(33.0, offset(1E-10));
    }

    /**
     * Verify the <code>subtract()</code> method.
     */
    @Test
    public void shouldSubtractCoodinatesCorrectly()
    {
        CartesianCoordinate u = new CartesianCoordinate(1, 2, 3);
        CartesianCoordinate v = new CartesianCoordinate(10, 20, 30);
        CartesianCoordinate w = v.subtract(u);
        assertThat(u.getX()).isEqualTo(1.0, offset(1E-10));
        assertThat(u.getY()).isEqualTo(2.0, offset(1E-10));
        assertThat(u.getZ()).isEqualTo(3.0, offset(1E-10));
        assertThat(v.getX()).isEqualTo(10.0, offset(1E-10));
        assertThat(v.getY()).isEqualTo(20.0, offset(1E-10));
        assertThat(v.getZ()).isEqualTo(30.0, offset(1E-10));
        assertThat(w.getX()).isEqualTo(9.0, offset(1E-10));
        assertThat(w.getY()).isEqualTo(18.0, offset(1E-10));
        assertThat(w.getZ()).isEqualTo(27.0, offset(1E-10));
    }

    /**
     * Verify the <code>norm()</code> method.
     */
    @Test
    public void shouldCalculateNorm()
    {
        CartesianCoordinate u = new CartesianCoordinate(10, 20, 30);
        double n = u.norm();
        assertThat(u.getX()).isEqualTo(10.0, offset(1E-10));
        assertThat(u.getY()).isEqualTo(20.0, offset(1E-10));
        assertThat(u.getZ()).isEqualTo(30.0, offset(1E-10));
        assertThat(n).isEqualTo(37.416573867739416, offset(1E-6));
    }

    /**
     * Verify the <code>set()</code> method.
     */
    @Test
    public void shouldCopyCoordinatesFromForeignObjectViaSet()
    {
        CartesianCoordinate p = new CartesianCoordinate();
        p.set(new CartesianCoordinate(1, 2, 3));
        assertThat(p.getX()).isEqualTo(1.0, offset(1E-10));
        assertThat(p.getY()).isEqualTo(2.0, offset(1E-10));
        assertThat(p.getZ()).isEqualTo(3.0, offset(1E-10));
    }

    /**
     * Verify the <code>setX()</code>, <code>setY()</code> and <code>setZ()</code> methods.
     */
    @Test
    public void shouldSetCoordinatesIndividually()
    {
        CartesianCoordinate p = new CartesianCoordinate();
        p.setX(1);
        p.setY(2);
        p.setZ(3);
        assertThat(p.getX()).isEqualTo(1.0, offset(1E-10));
        assertThat(p.getY()).isEqualTo(2.0, offset(1E-10));
        assertThat(p.getZ()).isEqualTo(3.0, offset(1E-10));
    }

    /**
     * Verify the <code>toString()</code> method.
     */
    @Test
    public void shouldConvertCoodrinatesToString()
    {
        CartesianCoordinate p = new CartesianCoordinate(1, 2, 3);
        String coordinateString = p.toString();
        assertThat(coordinateString).isNotNull().isEqualTo("(1.0m, 2.0m, 3.0m)");
    }

    /**
     * Verify the <code>normalize()</code> method.
     */
    @Test
    public void shouldCalculateNormalizedVector()
    {
        CartesianCoordinate p = new CartesianCoordinate(1, 2, 3);
        CartesianCoordinate n = p.normalize();
        assertThat(n.norm()).isEqualTo(1.0, offset(1E-9));
        assertThat(n.getX()).isEqualTo(1.0 / Math.sqrt(14.0), offset(1E-9));
        assertThat(n.getY()).isEqualTo(2.0 / Math.sqrt(14.0), offset(1E-9));
        assertThat(n.getZ()).isEqualTo(3.0 / Math.sqrt(14.0), offset(1E-9));
    }

    /**
     * Verify the <code>normalize()</code> method if the norm of the coordinate is zero.
     */
    @Test
    public void shouldCalculateNormalizedNullVector()
    {
        CartesianCoordinate p = new CartesianCoordinate(0, 0, 0);
        assertThat(p.norm()).isEqualTo(0.0, offset(1E-9));
        CartesianCoordinate n = p.normalize();
        assertThat(n.norm()).isEqualTo(1.0, offset(1E-9));
        assertThat(n.getX()).isEqualTo(1.0, offset(1E-9));
        assertThat(n.getY()).isEqualTo(0.0, offset(1E-9));
        assertThat(n.getZ()).isEqualTo(0.0, offset(1E-9));
    }

    /**
     * Verify the <code>crossProduct()</code> method.
     */
    @Test
    public void shouldCalculateCrossProductA()
    {
        CartesianCoordinate a = new CartesianCoordinate(1, 0, 0);
        CartesianCoordinate b = new CartesianCoordinate(0, 1, 0);

        CartesianCoordinate c = a.crossProduct(b);
        assertThat(c.getX()).isEqualTo(0.0, offset(1E-9));
        assertThat(c.getY()).isEqualTo(0.0, offset(1E-9));
        assertThat(c.getZ()).isEqualTo(1.0, offset(1E-9));

        c = b.crossProduct(a);
        assertThat(c.getX()).isEqualTo(0.0, offset(1E-9));
        assertThat(c.getY()).isEqualTo(0.0, offset(1E-9));
        assertThat(c.getZ()).isEqualTo(-1.0, offset(1E-9));
    }

    /**
     * Verify the <code>crossProduct()</code> method.
     */
    @Test
    public void shouldCalculateCrossProductB()
    {
        CartesianCoordinate a = new CartesianCoordinate(0, 1, 0);
        CartesianCoordinate b = new CartesianCoordinate(0, 0, 1);

        CartesianCoordinate c = a.crossProduct(b);
        assertThat(c.getX()).isEqualTo(1.0, offset(1E-9));
        assertThat(c.getY()).isEqualTo(0.0, offset(1E-9));
        assertThat(c.getZ()).isEqualTo(0.0, offset(1E-9));

        c = b.crossProduct(a);
        assertThat(c.getX()).isEqualTo(-1.0, offset(1E-9));
        assertThat(c.getY()).isEqualTo(0.0, offset(1E-9));
        assertThat(c.getZ()).isEqualTo(0.0, offset(1E-9));
    }

    /**
     * Verify the <code>crossProduct()</code> method.
     */
    @Test
    public void shouldCalculateCrossProductC()
    {
        CartesianCoordinate a = new CartesianCoordinate(1, 0, 0);
        CartesianCoordinate b = new CartesianCoordinate(0, 0, 1);

        CartesianCoordinate c = a.crossProduct(b);
        assertThat(c.getX()).isEqualTo(0.0, offset(1E-9));
        assertThat(c.getY()).isEqualTo(-1.0, offset(1E-9));
        assertThat(c.getZ()).isEqualTo(0.0, offset(1E-9));

        c = b.crossProduct(a);
        assertThat(c.getX()).isEqualTo(0.0, offset(1E-9));
        assertThat(c.getY()).isEqualTo(1.0, offset(1E-9));
        assertThat(c.getZ()).isEqualTo(0.0, offset(1E-9));
    }

    /**
     * Verify the <code>crossProduct()</code> method.
     */
    @Test
    public void shouldCalculateCrossProductD()
    {
        CartesianCoordinate a = new CartesianCoordinate(1, 2, 3);
        CartesianCoordinate b = new CartesianCoordinate(4, 5, 6);

        CartesianCoordinate c = a.crossProduct(b);
        assertThat(c.getX()).isEqualTo(-3.0, offset(1E-9));
        assertThat(c.getY()).isEqualTo(6.0, offset(1E-9));
        assertThat(c.getZ()).isEqualTo(-3.0, offset(1E-9));

        c = b.crossProduct(a);
        assertThat(c.getX()).isEqualTo(3.0, offset(1E-9));
        assertThat(c.getY()).isEqualTo(-6.0, offset(1E-9));
        assertThat(c.getZ()).isEqualTo(3.0, offset(1E-9));
    }

    //    @Test
    //    public void testCase09()
    //    {
    //        double a = 10;
    //        double c = 3;
    //
    //        CartesianCoordinate x = new CartesianCoordinate(5, 5, (c / a) * Math.sqrt(a * a - 5 * 5 - 5 * 5));
    //        CartesianCoordinate ref = new CartesianCoordinate(5, 6, (c / a) * Math.sqrt(a * a - 5 * 5 - 6 * 6));
    //        CartesianCoordinate y = new CartesianCoordinate(5, 6, 1.7);
    //
    //        PolarCoordinate X = r2p(x);
    //        PolarCoordinate Y = r2p(y);
    //        AssertJUnit.assertNotNull(X);
    //        AssertJUnit.assertNotNull(Y);
    //
    //        CartesianCoordinate xy = y.subtract(x);
    //
    //        double nz = (a / c) * Math.sqrt(a * a - x.getX() * x.getX() - x.getY() * x.getY());
    //        CartesianCoordinate n = new CartesianCoordinate(x.getX(), x.getY(), nz);
    //
    //        double phi =
    //            Math.asin(Math.abs(n.getX() * xy.getX() + n.getY() * xy.getY() + n.getZ() * xy.getZ())
    //                / (n.norm() * xy.norm()));
    //
    //        phi /= PI180TH;
    //        if (ref.getZ() > y.getZ())
    //        {
    //            phi = -phi;
    //        }
    //    }
    //
    // public static double PI180TH = Math.PI / 180;
    //
    //    /**
    //     * @param p a polar coordinate.
    //     * @return the equivalent rectangular coordinate.
    //     */
    //    private static CartesianCoordinate p2r(PolarCoordinate p)
    //    {
    //        double x = p.getAltitude() * Math.cos(p.getLatitude() * PI180TH) * Math.cos(p.getLongitude() * PI180TH);
    //        double y = p.getAltitude() * Math.cos(p.getLatitude() * PI180TH) * Math.sin(p.getLongitude() * PI180TH);
    //        double z = p.getAltitude() * Math.sin(p.getLatitude() * PI180TH);
    //
    //        return new CartesianCoordinate(x, y, z);
    //    }
    //
    //    /**
    //     * @param p a rectangular coordinate.
    //     * @return the equivalent polar coordinate.
    //     */
    //    private static PolarCoordinate r2p(CartesianCoordinate p)
    //    {
    //        double N = Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY() + p.getZ() * p.getZ());
    //        double altitude = N;
    //        double latitude = Math.asin(p.getZ() / N);
    //        double longitude = Math.asin(p.getY() / (N * Math.cos(latitude)));
    //
    //        return new PolarCoordinate(latitude / PI180TH, longitude / PI180TH, altitude);
    //    }

}
