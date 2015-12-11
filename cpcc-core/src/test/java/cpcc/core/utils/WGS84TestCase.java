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

package cpcc.core.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.utils.CartesianCoordinate;
import cpcc.core.utils.GeodeticSystem;
import cpcc.core.utils.WGS84;

/**
 * This class verifies the implementation of the WGS84 class.
 */
public class WGS84TestCase
{
    GeodeticSystem gs;

    @BeforeMethod
    public void setUp()
    {
        gs = new WGS84();
    }

    @DataProvider
    public Object[][] polarAndRectangularDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new PolarCoordinate(47.99043439493213, 12.93670580800686, 435.94417220),
                new CartesianCoordinate(4168246.0564496145, 957466.6063627704, 4716488.496489645)
            },
        };
    };

    /**
     * This test case verifies the implementation of the rectangularToPolarCoordinates() and
     * polarToRectangularCoordinates() methods of class WGS84.
     */
    @Test(dataProvider = "polarAndRectangularDataProvider")
    public void shouldConvertCoodinates(PolarCoordinate wgs, CartesianCoordinate rect)
    {
        PolarCoordinate pos = gs.rectangularToPolarCoordinates(rect);
        assertThat(wgs.getLatitude()).isEqualTo(pos.getLatitude(), offset(1E-4));
        assertThat(wgs.getLongitude()).isEqualTo(pos.getLongitude(), offset(1E-4));
        assertThat(wgs.getAltitude()).isEqualTo(pos.getAltitude(), offset(1E-4));

        CartesianCoordinate rec = gs.polarToRectangularCoordinates(wgs);
        assertThat(rec.getX()).isEqualTo(rect.getX(), offset(1E-4));
        assertThat(rec.getY()).isEqualTo(rect.getY(), offset(1E-4));
        assertThat(rec.getZ()).isEqualTo(rect.getZ(), offset(1E-4));
    }

    /**
     * This test calculates the elevation of a course.
     */
    @Test
    public void shouldCalculateElevation()
    {
        PolarCoordinate A = new PolarCoordinate(48, 13, 1010);
        PolarCoordinate B = new PolarCoordinate(48.001, 13.002, 1000);
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

        assertThat(elevation).isEqualTo(-3.1902590577710233, offset(1E-9));
    }

    @DataProvider
    public Object[][] walkAroundDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new PolarCoordinate(48, 13, 1010),
                new CartesianCoordinate(100, 0, 0),
                new PolarCoordinate(47.99910182694469, 13, 1010)
            },
            new Object[]{
                new PolarCoordinate(48, 13, 1010),
                new CartesianCoordinate(0, 100, 0),
                new PolarCoordinate(48, 13.001342298568892, 1010)},
            new Object[]{
                new PolarCoordinate(48, 13, 1010),
                new CartesianCoordinate(0, 0, 100),
                new PolarCoordinate(48, 13, 1110)
            },
        };
    };

    /**
     * This test verifies the walk() method.
     */
    @Test(dataProvider = "walkAroundDataProvider")
    public void shouldWalkAround(PolarCoordinate startPos, CartesianCoordinate way, PolarCoordinate destPos)
    {
        PolarCoordinate b = gs.walk(startPos, way.getX(), way.getY(), way.getZ());
        assertThat(b.getLatitude()).isEqualTo(destPos.getLatitude(), offset(1E-8));
        assertThat(b.getLongitude()).isEqualTo(destPos.getLongitude(), offset(1E-8));
        assertThat(b.getAltitude()).isEqualTo(destPos.getAltitude(), offset(1E-8));
    }

    @DataProvider
    public Object[][] polarCoordinatesDataProvider()
    {
        return new Object[][]{
            new Object[]{new PolarCoordinate(48.001, 13.002, 10)},
            new Object[]{new PolarCoordinate(48.001, -12.002, 10)},
            new Object[]{new PolarCoordinate(48.001, -80.002, 10)},
            new Object[]{new PolarCoordinate(48.001, -122.002, 10)},
            new Object[]{new PolarCoordinate(-89.001, 80.002, 10)},
            new Object[]{new PolarCoordinate(89.001, 80.002, 10)},
            new Object[]{new PolarCoordinate(-89.001, 179.992, 10)},
            new Object[]{new PolarCoordinate(-89.001, -179.992, 10)},
            new Object[]{new PolarCoordinate(89.001, -179.992, 10)},
            new Object[]{new PolarCoordinate(-89.001, 44.992, 10)},
            new Object[]{new PolarCoordinate(-89.001, -44.992, 10)},
        };
    };

    @Test(dataProvider = "polarCoordinatesDataProvider")
    public void shouldConvertCoordinatesToAndFro(PolarCoordinate pos)
    {
        CartesianCoordinate cartA = gs.polarToRectangularCoordinates(pos);
        PolarCoordinate posA = gs.rectangularToPolarCoordinates(cartA);

        assertThat(posA.getLatitude())
            .overridingErrorMessage("Latitude")
            .isEqualTo(pos.getLatitude(), offset(1E-8));
        assertThat(posA.getLongitude())
            .overridingErrorMessage("Longitude")
            .isEqualTo(pos.getLongitude(), offset(1E-8));
        assertThat(posA.getAltitude())
            .overridingErrorMessage("Altitude")
            .isEqualTo(pos.getAltitude(), offset(1E-4));
    }

    @DataProvider
    public Object[][] specialCasesDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new CartesianCoordinate(0, 0, 1),
                new PolarCoordinate(90.0, 0.0, 1.0 - 6356752.3142)
            },
            new Object[]{
                new CartesianCoordinate(0, 0, -1),
                new PolarCoordinate(-90.0, 0.0, -1.0 + 6356752.3142)
            },
            new Object[]{
                new CartesianCoordinate(0, 1, 1),
                new PolarCoordinate(-45.38862666932452, 90.0, -6388982.402021714)
            },
            new Object[]{
                new CartesianCoordinate(1, 0, 1),
                new PolarCoordinate(-45.38862666932452, 0.0, -6388982.402021714)
            },
        };
    };

    @Test(dataProvider = "specialCasesDataProvider")
    public void shouldConsiderConvertingOfSpecialCases(CartesianCoordinate cartA, PolarCoordinate posA)
    {
        PolarCoordinate result = gs.rectangularToPolarCoordinates(cartA);
        assertThat(result.getLatitude())
            .overridingErrorMessage("Latitude")
            .isEqualTo(posA.getLatitude(), offset(1E-3));
        assertThat(result.getLongitude())
            .overridingErrorMessage("Longitude")
            .isEqualTo(posA.getLongitude(), offset(1E-3));
        assertThat(result.getAltitude())
            .overridingErrorMessage("Altitude")
            .isEqualTo(posA.getAltitude(), offset(1E-3));
    }

    @DataProvider
    public Object[][] distancesDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new PolarCoordinate(48.0, 13.0, 1010.0),
                new PolarCoordinate(47.99910182694469, 13.0, 1010.0),
                100.0D, 2E-1
            },
            new Object[]{
                new PolarCoordinate(48.0, 13.0, 1010.0),
                new PolarCoordinate(48.0, 13.001342298568892, 1010.0),
                100.0D, 2E-1
            },
            new Object[]{
                new PolarCoordinate(48.0, 13.0, 1010.0),
                new PolarCoordinate(48.0, 13.0, 1110.0),
                100.0D, 1E-4
            },
        };
    };

    @Test(dataProvider = "distancesDataProvider")
    public void shouldCalculateDistances(PolarCoordinate a, PolarCoordinate b, double distance, double delta)
    {
        assertThat(gs.calculateDistance(a, b)).isEqualTo(distance, offset(delta));
    }

}
