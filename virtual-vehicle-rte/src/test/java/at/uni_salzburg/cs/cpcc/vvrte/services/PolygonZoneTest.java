/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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
package at.uni_salzburg.cs.cpcc.vvrte.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.utilities.PolarCoordinate;
import at.uni_salzburg.cs.cpcc.vvrte.services.PolygonZone.TwoTuple;

/**
 * PolygonZoneTest
 */
public class PolygonZoneTest
{
    private static final String ZONE_ONE_AS_STRING =
        "vertices: (0.00000000, 0.00000000), (10.00000000, 0.00000000), (10.00000000, 10.00000000), "
            + "(0.00000000, 10.00000000), (0.00000000, 0.00000000)";

    private PolygonZone simpleZoneOne;
    PolygonZone.TwoTuple[] verticesSimpleZoneOne;

    private PolygonZone simpleZoneTwo;
    PolygonZone.TwoTuple[] verticesSimpleZoneTwo;

    private PolygonZone zoneThree;
    PolygonZone.TwoTuple[] verticesZoneThree;

    private PolygonZone zoneFour;
    private TwoTuple[] verticesZoneFour;

    private PolygonZone zoneFive;
    private PolarCoordinate[] verticesZoneFive;

    private PolygonZone zoneSix;
    private PolarCoordinate[] verticesZoneSix;

    private PolygonZone zoneSeven;
    private PolarCoordinate[] verticesZoneSeven;

    @BeforeMethod
    public void setUp()
    {
        verticesSimpleZoneOne = new PolygonZone.TwoTuple[5];
        verticesSimpleZoneOne[0] = new PolygonZone.TwoTuple(0.0, 0.0);
        verticesSimpleZoneOne[1] = new PolygonZone.TwoTuple(10.0, 0.0);
        verticesSimpleZoneOne[2] = new PolygonZone.TwoTuple(10.0, 10.0);
        verticesSimpleZoneOne[3] = new PolygonZone.TwoTuple(0.0, 10.0);
        verticesSimpleZoneOne[4] = new PolygonZone.TwoTuple(0.0, 0.0);

        simpleZoneOne = new PolygonZone(verticesSimpleZoneOne);

        verticesSimpleZoneTwo = new PolygonZone.TwoTuple[5];
        verticesSimpleZoneTwo[0] = new PolygonZone.TwoTuple(0.0, 0.0);
        verticesSimpleZoneTwo[1] = new PolygonZone.TwoTuple(0.0, 10.0);
        verticesSimpleZoneTwo[2] = new PolygonZone.TwoTuple(10.0, 10.0);
        verticesSimpleZoneTwo[3] = new PolygonZone.TwoTuple(10.0, 0.0);
        verticesSimpleZoneTwo[4] = new PolygonZone.TwoTuple(0.0, 0.0);

        simpleZoneTwo = new PolygonZone(verticesSimpleZoneTwo);

        verticesZoneThree = new PolygonZone.TwoTuple[5];
        verticesZoneThree[0] = new PolygonZone.TwoTuple(5.0, 0.0);
        verticesZoneThree[1] = new PolygonZone.TwoTuple(10.0, 5.0);
        verticesZoneThree[2] = new PolygonZone.TwoTuple(5.0, 10.0);
        verticesZoneThree[3] = new PolygonZone.TwoTuple(0.0, 5.0);
        verticesZoneThree[4] = new PolygonZone.TwoTuple(5.0, 0.0);

        zoneThree = new PolygonZone(verticesZoneThree);

        verticesZoneFour = new PolygonZone.TwoTuple[5];
        verticesZoneFour[0] = new PolygonZone.TwoTuple(5.0, 0.0);
        verticesZoneFour[1] = new PolygonZone.TwoTuple(0.0, 5.0);
        verticesZoneFour[2] = new PolygonZone.TwoTuple(5.0, 10.0);
        verticesZoneFour[3] = new PolygonZone.TwoTuple(10.0, 5.0);
        verticesZoneFour[4] = new PolygonZone.TwoTuple(5.0, 0.0);

        zoneFour = new PolygonZone(verticesZoneFour);

        verticesZoneFive = new PolarCoordinate[5];
        verticesZoneFive[0] = new PolarCoordinate(48.0, 13.0, 0);
        verticesZoneFive[1] = new PolarCoordinate(50.0, 13.0, 0);
        verticesZoneFive[2] = new PolarCoordinate(50.0, 12.0, 0);
        verticesZoneFive[3] = new PolarCoordinate(48.0, 12.0, 0);
        verticesZoneFive[4] = new PolarCoordinate(48.0, 13.0, 0);

        zoneFive = new PolygonZone(verticesZoneFive);

        verticesZoneSix = new PolarCoordinate[5];
        verticesZoneSix[0] = new PolarCoordinate(48.0, 13.0, 0);
        verticesZoneSix[1] = new PolarCoordinate(48.0, 12.0, 0);
        verticesZoneSix[2] = new PolarCoordinate(50.0, 12.0, 0);
        verticesZoneSix[3] = new PolarCoordinate(50.0, 13.0, 0);
        verticesZoneSix[4] = new PolarCoordinate(48.0, 13.0, 0);

        zoneSix = new PolygonZone(verticesZoneSix);

        verticesZoneSeven = new PolarCoordinate[7];
        verticesZoneSeven[0] = new PolarCoordinate(47.82213832, 13.04132304, 0.00);
        verticesZoneSeven[1] = new PolarCoordinate(47.82166287, 13.04126403, 0.00);
        verticesZoneSeven[2] = new PolarCoordinate(47.82157283, 13.04209551, 0.00);
        verticesZoneSeven[3] = new PolarCoordinate(47.82206268, 13.04235300, 0.00);
        verticesZoneSeven[4] = new PolarCoordinate(47.82235083, 13.04188630, 0.00);
        verticesZoneSeven[5] = new PolarCoordinate(47.82215273, 13.04136971, 0.00);
        verticesZoneSeven[6] = new PolarCoordinate(47.82213832, 13.04132304, 0.00);

        zoneSeven = new PolygonZone(verticesZoneSeven);
    }

    @Test
    public void shouldConsiderPolygonVerticesInsideThePolygonOfSimpleZoneOne()
    {
        assertThat(simpleZoneOne.isInside(verticesSimpleZoneOne[0].getX(), verticesSimpleZoneOne[0].getY())).isTrue();
        assertThat(simpleZoneOne.isInside(verticesSimpleZoneOne[1].getX(), verticesSimpleZoneOne[1].getY())).isTrue();
        assertThat(simpleZoneOne.isInside(verticesSimpleZoneOne[2].getX(), verticesSimpleZoneOne[2].getY())).isTrue();
        assertThat(simpleZoneOne.isInside(verticesSimpleZoneOne[3].getX(), verticesSimpleZoneOne[3].getY())).isTrue();
    }

    @Test
    public void shouldConsiderPolygonVerticesInsideThePolygonOfSimpleZoneTwo()
    {
        assertThat(simpleZoneTwo.isInside(verticesSimpleZoneTwo[0].getX(), verticesSimpleZoneTwo[0].getY())).isTrue();
        assertThat(simpleZoneTwo.isInside(verticesSimpleZoneTwo[1].getX(), verticesSimpleZoneTwo[1].getY())).isTrue();
        assertThat(simpleZoneTwo.isInside(verticesSimpleZoneTwo[2].getX(), verticesSimpleZoneTwo[2].getY())).isTrue();
        assertThat(simpleZoneTwo.isInside(verticesSimpleZoneTwo[3].getX(), verticesSimpleZoneTwo[3].getY())).isTrue();
    }

    @Test
    public void shouldConsiderPolygonVerticesInsideThePolygonOfZoneThree()
    {
        assertThat(zoneThree.isInside(verticesZoneThree[0].getX(), verticesZoneThree[0].getY())).isTrue();
        assertThat(zoneThree.isInside(verticesZoneThree[1].getX(), verticesZoneThree[1].getY())).isTrue();
        assertThat(zoneThree.isInside(verticesZoneThree[2].getX(), verticesZoneThree[2].getY())).isTrue();
        assertThat(zoneThree.isInside(verticesZoneThree[3].getX(), verticesZoneThree[3].getY())).isTrue();
    }

    @Test
    public void shouldConsiderPolygonVerticesInsideThePolygonOfZoneFour()
    {
        assertThat(zoneFour.isInside(verticesZoneFour[0].getX(), verticesZoneFour[0].getY())).isTrue();
        assertThat(zoneFour.isInside(verticesZoneFour[1].getX(), verticesZoneFour[1].getY())).isTrue();
        assertThat(zoneFour.isInside(verticesZoneFour[2].getX(), verticesZoneFour[2].getY())).isTrue();
        assertThat(zoneFour.isInside(verticesZoneFour[3].getX(), verticesZoneFour[3].getY())).isTrue();
    }

    @Test
    public void shouldConsiderPolygonVerticesInsideThePolygonOfZoneFive()
    {
        assertThat(zoneFive.isInside(verticesZoneFive[0])).isTrue();
        assertThat(zoneFive.isInside(verticesZoneFive[1])).isTrue();
        assertThat(zoneFive.isInside(verticesZoneFive[2])).isTrue();
        assertThat(zoneFive.isInside(verticesZoneFive[3])).isTrue();
    }

    @Test
    public void shouldConsiderPolygonVerticesInsideThePolygonOfZoneSix()
    {
        assertThat(zoneSix.isInside(verticesZoneSix[0])).isTrue();
        assertThat(zoneSix.isInside(verticesZoneSix[1])).isTrue();
        assertThat(zoneSix.isInside(verticesZoneSix[2])).isTrue();
        assertThat(zoneSix.isInside(verticesZoneSix[3])).isTrue();
    }

    @Test
    public void shouldFindCogOfSimpleZoneOne()
    {
        PolarCoordinate cog = simpleZoneOne.getCenterOfGravity();
        assertThat(cog.getLatitude()).isEqualTo(5, offset(1E-9));
        assertThat(cog.getLongitude()).isEqualTo(5, offset(1E-9));
    }

    @Test
    public void shouldFindCogOfSimpleZoneTwo()
    {
        PolarCoordinate cog = simpleZoneTwo.getCenterOfGravity();
        assertThat(cog.getLatitude()).isEqualTo(5, offset(1E-9));
        assertThat(cog.getLongitude()).isEqualTo(5, offset(1E-9));
    }

    @Test
    public void shouldFindCogOfZoneThree()
    {
        PolarCoordinate cog = zoneThree.getCenterOfGravity();
        assertThat(cog.getLatitude()).isEqualTo(5, offset(1E-9));
        assertThat(cog.getLongitude()).isEqualTo(5, offset(1E-9));
    }

    @Test
    public void shouldFindCogOfZoneFour()
    {
        PolarCoordinate cog = zoneFour.getCenterOfGravity();
        assertThat(cog.getLatitude()).isEqualTo(5, offset(1E-9));
        assertThat(cog.getLongitude()).isEqualTo(5, offset(1E-9));
    }

    @Test
    public void shouldFindCogOfZoneFive()
    {
        PolarCoordinate cog = zoneFive.getCenterOfGravity();
        assertThat(cog.getLatitude()).isEqualTo(49.0, offset(1E-9));
        assertThat(cog.getLongitude()).isEqualTo(12.5, offset(1E-9));
    }

    @Test
    public void shouldFindCogOfZoneSix()
    {
        PolarCoordinate cog = zoneSix.getCenterOfGravity();
        assertThat(cog.getLatitude()).isEqualTo(49.0, offset(1E-9));
        assertThat(cog.getLongitude()).isEqualTo(12.5, offset(1E-9));
    }

    @Test
    public void shouldFindCogOfZoneSeven()
    {
        PolarCoordinate cog = zoneSeven.getCenterOfGravity();
        assertThat(cog.getLatitude()).isEqualTo(47.82193634361729, offset(1E-9));
        assertThat(cog.getLongitude()).isEqualTo(13.041786263143168, offset(1E-9));
    }

    @DataProvider
    public Object[][] pointsInsideOfSimpleZoneOneAndTwo()
    {
        return new Object[][]{
            new Object[]{5.0, 5.0},
            new Object[]{2.5, 2.5},
            new Object[]{2.5, 7.5},
            new Object[]{7.5, 2.5},
            new Object[]{7.5, 7.5},
        };
    }

    @Test(dataProvider = "pointsInsideOfSimpleZoneOneAndTwo")
    public void shouldFindPointsInsideOfSimpleZoneOne(double lat, double lon)
    {
        assertThat(simpleZoneOne.isInside(lat, lon)).isTrue();
    }

    @Test
    public void shouldNotFindNullPositionInsideSimpleZoneOne()
    {
        assertThat(simpleZoneOne.isInside(null)).isFalse();
    }

    @Test
    public void shouldConvertSimpleZoneOneToStringCorrectly()
    {
        assertThat(simpleZoneOne.toString()).isEqualTo(ZONE_ONE_AS_STRING);
    }

    @Test(dataProvider = "pointsInsideOfSimpleZoneOneAndTwo")
    public void shouldFindPointsInsideOfSimpleZoneTwo(double lat, double lon)
    {
        assertThat(simpleZoneTwo.isInside(lat, lon)).isTrue();
    }

    @DataProvider
    public Object[][] pointsOutsideOfSimpleZoneOneAndTwo()
    {
        return new Object[][]{
            new Object[]{-1.0, 5.0},
            new Object[]{11.0, 5.0},
            new Object[]{5.0, -1.0},
            new Object[]{5.0, 110},
        };
    }

    @Test(dataProvider = "pointsOutsideOfSimpleZoneOneAndTwo")
    public void shouldFindPointsOutsideOfSimpleZoneOne(double lat, double lon)
    {
        assertThat(simpleZoneOne.isInside(lat, lon)).isFalse();
    }

    @Test(dataProvider = "pointsOutsideOfSimpleZoneOneAndTwo")
    public void shouldFindPointsOutsideOfSimpleZoneTwo(double lat, double lon)
    {
        assertThat(simpleZoneTwo.isInside(lat, lon)).isFalse();
    }

    @DataProvider
    public Object[][] pointsInsideOfZoneThreeAndFour()
    {
        return new Object[][]{
            new Object[]{5.0, 5.0},
        };
    }

    @Test(dataProvider = "pointsInsideOfZoneThreeAndFour")
    public void shouldFindPointsInsideOfZoneThree(double lat, double lon)
    {
        assertThat(zoneThree.isInside(lat, lon)).isTrue();
    }

    @Test(dataProvider = "pointsInsideOfZoneThreeAndFour")
    public void shouldFindPointsInsideOfZoneFour(double lat, double lon)
    {
        assertThat(zoneFour.isInside(lat, lon)).isTrue();
    }

    @DataProvider
    public Object[][] pointsOutsideOfZoneThreeAndFour()
    {
        return new Object[][]{
            new Object[]{-1.0, 5.0},
            new Object[]{11.0, 5.0},
            new Object[]{5.0, -1.0},
            new Object[]{5.0, 110},
        };
    }

    @Test(dataProvider = "pointsOutsideOfZoneThreeAndFour")
    public void shouldFindPointsOutsideOfZoneThree(double lat, double lon)
    {
        assertThat(zoneThree.isInside(lat, lon)).isFalse();
    }

    @Test(dataProvider = "pointsOutsideOfZoneThreeAndFour")
    public void shouldFindPointsOutsideOfZoneFour(double lat, double lon)
    {
        assertThat(zoneFour.isInside(lat, lon)).isFalse();
    }

    @DataProvider
    public Object[][] pointsInsideOfZoneFiveAndSix()
    {
        return new Object[][]{
            new Object[]{new PolarCoordinate(48.5, 12.5, 0)},
        };
    }

    @Test(dataProvider = "pointsInsideOfZoneFiveAndSix")
    public void shouldFindPointsInsideOfZoneFive(PolarCoordinate coordinate)
    {
        assertThat(zoneFive.isInside(coordinate)).isTrue();
    }

    @Test(dataProvider = "pointsInsideOfZoneFiveAndSix")
    public void shouldFindPointsInsideOfZoneSix(PolarCoordinate coordinate)
    {
        assertThat(zoneSix.isInside(coordinate)).isTrue();
    }

    @DataProvider
    public Object[][] pointsOutsideOfZoneFiveAndSix()
    {
        return new Object[][]{
            new Object[]{new PolarCoordinate(47.5, 12.5, 0)},
            new Object[]{new PolarCoordinate(50.5, 12.5, 0)},
            new Object[]{new PolarCoordinate(48.5, 11.5, 0)},
            new Object[]{new PolarCoordinate(48.5, 13.5, 0)},
        };
    }

    @Test(dataProvider = "pointsOutsideOfZoneFiveAndSix")
    public void shouldFindPointsOutsideOfZoneFive(PolarCoordinate coordinate)
    {
        assertThat(zoneFive.isInside(coordinate)).isFalse();
    }

    @Test(dataProvider = "pointsOutsideOfZoneFiveAndSix")
    public void shouldFindPointsOutsideOfZoneSix(PolarCoordinate coordinate)
    {
        assertThat(zoneSix.isInside(coordinate)).isFalse();
    }

    @DataProvider
    public Object[][] pointsInsideOfZoneSeven()
    {
        return new Object[][]{
            new Object[]{new PolarCoordinate(47.82174572, 13.04135522, 0.00)},
            new Object[]{new PolarCoordinate(47.82161965, 13.04205796, 0.00)},
            new Object[]{new PolarCoordinate(47.82180335, 13.04218134, 0.00)},
            new Object[]{new PolarCoordinate(47.82205548, 13.04229400, 0.00)},
            new Object[]{new PolarCoordinate(47.82223917, 13.04199895, 0.00)},
            new Object[]{new PolarCoordinate(47.82209510, 13.04137766, 0.00)},
        };
    }

    @Test(dataProvider = "pointsInsideOfZoneSeven")
    public void shouldFindPointsInsideOfZoneSeven(PolarCoordinate coordinate)
    {
        assertThat(zoneSeven.isInside(coordinate)).isTrue();
    }

    @DataProvider
    public Object[][] pointsOutsideOfZoneSeven()
    {
        return new Object[][]{
            new Object[]{new PolarCoordinate(47.82201586, 13.04122648, 0.00)},
            new Object[]{new PolarCoordinate(47.82159804, 13.04160199, 0.00)},
            new Object[]{new PolarCoordinate(47.82154401, 13.04198286, 0.00)},
            new Object[]{new PolarCoordinate(47.82162685, 13.04216525, 0.00)},
            new Object[]{new PolarCoordinate(47.82198704, 13.04237983, 0.00)},
            new Object[]{new PolarCoordinate(47.82211671, 13.04232082, 0.00)},
            new Object[]{new PolarCoordinate(47.82235443, 13.04195067, 0.00)},
            new Object[]{new PolarCoordinate(47.82229320, 13.04157516, 0.00)},
        };
    }

    @Test(dataProvider = "pointsOutsideOfZoneSeven")
    public void shouldFindPointsOutsideOfZoneSeven(PolarCoordinate coordinate)
    {
        assertThat(zoneSeven.isInside(coordinate)).isFalse();
    }

}
