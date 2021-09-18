// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.core.base;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.geojson.LngLatAlt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import cpcc.core.base.PolygonZone.TwoTuple;
import cpcc.core.entities.PolarCoordinate;

/**
 * PolygonZoneTest
 */
class PolygonZoneTest
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

    private PolygonZone zoneEight;
    private PolarCoordinate[] verticesZoneEight;

    private PolygonZone zoneNine;
    private LngLatAlt[] verticesZoneNine;

    @BeforeEach
    void setUp()
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

        verticesZoneEight = new PolarCoordinate[9];
        verticesZoneEight[0] = new PolarCoordinate(0.0, 5.0, 0.00);
        verticesZoneEight[1] = new PolarCoordinate(2.0, 0.0, 0.00);
        verticesZoneEight[2] = new PolarCoordinate(4.0, 5.0, 0.00);
        verticesZoneEight[3] = new PolarCoordinate(6.0, 0.0, 0.00);
        verticesZoneEight[4] = new PolarCoordinate(8.0, 5.0, 0.00);
        verticesZoneEight[5] = new PolarCoordinate(10.0, 0.0, 0.00);
        verticesZoneEight[6] = new PolarCoordinate(8.0, 10.0, 0.00);
        verticesZoneEight[7] = new PolarCoordinate(6.0, 9.0, 0.00);
        verticesZoneEight[8] = new PolarCoordinate(0.0, 5.0, 0.00);

        zoneEight = new PolygonZone(verticesZoneEight);

        verticesZoneNine = new LngLatAlt[2];
        verticesZoneNine[0] = new LngLatAlt(2.0, 2.0);
        verticesZoneNine[1] = new LngLatAlt(3.0, 2.0);

        zoneNine = new PolygonZone(Arrays.asList(verticesZoneNine));
    }

    @Test
    void shouldConsiderPolygonVerticesInsideThePolygonOfSimpleZoneOne()
    {
        assertThat(simpleZoneOne.isInside(verticesSimpleZoneOne[0].getX(), verticesSimpleZoneOne[0].getY())).isTrue();
        assertThat(simpleZoneOne.isInside(verticesSimpleZoneOne[1].getX(), verticesSimpleZoneOne[1].getY())).isTrue();
        assertThat(simpleZoneOne.isInside(verticesSimpleZoneOne[2].getX(), verticesSimpleZoneOne[2].getY())).isTrue();
        assertThat(simpleZoneOne.isInside(verticesSimpleZoneOne[3].getX(), verticesSimpleZoneOne[3].getY())).isTrue();
    }

    @Test
    void shouldConsiderPolygonVerticesInsideThePolygonOfSimpleZoneTwo()
    {
        assertThat(simpleZoneTwo.isInside(verticesSimpleZoneTwo[0].getX(), verticesSimpleZoneTwo[0].getY())).isTrue();
        assertThat(simpleZoneTwo.isInside(verticesSimpleZoneTwo[1].getX(), verticesSimpleZoneTwo[1].getY())).isTrue();
        assertThat(simpleZoneTwo.isInside(verticesSimpleZoneTwo[2].getX(), verticesSimpleZoneTwo[2].getY())).isTrue();
        assertThat(simpleZoneTwo.isInside(verticesSimpleZoneTwo[3].getX(), verticesSimpleZoneTwo[3].getY())).isTrue();
    }

    @Test
    void shouldConsiderPolygonVerticesInsideThePolygonOfZoneThree()
    {
        assertThat(zoneThree.isInside(verticesZoneThree[0].getX(), verticesZoneThree[0].getY())).isTrue();
        assertThat(zoneThree.isInside(verticesZoneThree[1].getX(), verticesZoneThree[1].getY())).isTrue();
        assertThat(zoneThree.isInside(verticesZoneThree[2].getX(), verticesZoneThree[2].getY())).isTrue();
        assertThat(zoneThree.isInside(verticesZoneThree[3].getX(), verticesZoneThree[3].getY())).isTrue();
    }

    @Test
    void shouldConsiderPolygonVerticesInsideThePolygonOfZoneFour()
    {
        assertThat(zoneFour.isInside(verticesZoneFour[0].getX(), verticesZoneFour[0].getY())).isTrue();
        assertThat(zoneFour.isInside(verticesZoneFour[1].getX(), verticesZoneFour[1].getY())).isTrue();
        assertThat(zoneFour.isInside(verticesZoneFour[2].getX(), verticesZoneFour[2].getY())).isTrue();
        assertThat(zoneFour.isInside(verticesZoneFour[3].getX(), verticesZoneFour[3].getY())).isTrue();
    }

    @Test
    void shouldConsiderPolygonVerticesInsideThePolygonOfZoneFive()
    {
        assertThat(zoneFive.isInside(verticesZoneFive[0])).isTrue();
        assertThat(zoneFive.isInside(verticesZoneFive[1])).isTrue();
        assertThat(zoneFive.isInside(verticesZoneFive[2])).isTrue();
        assertThat(zoneFive.isInside(verticesZoneFive[3])).isTrue();
    }

    @Test
    void shouldConsiderPolygonVerticesInsideThePolygonOfZoneSix()
    {
        assertThat(zoneSix.isInside(verticesZoneSix[0])).isTrue();
        assertThat(zoneSix.isInside(verticesZoneSix[1])).isTrue();
        assertThat(zoneSix.isInside(verticesZoneSix[2])).isTrue();
        assertThat(zoneSix.isInside(verticesZoneSix[3])).isTrue();
    }

    @Test
    void shouldFindCogOfSimpleZoneOne()
    {
        PolarCoordinate cog = simpleZoneOne.getCenterOfGravity();
        assertThat(cog.getLatitude()).isEqualTo(5, offset(1E-9));
        assertThat(cog.getLongitude()).isEqualTo(5, offset(1E-9));
    }

    @Test
    void shouldFindCogOfSimpleZoneTwo()
    {
        PolarCoordinate cog = simpleZoneTwo.getCenterOfGravity();
        assertThat(cog.getLatitude()).isEqualTo(5, offset(1E-9));
        assertThat(cog.getLongitude()).isEqualTo(5, offset(1E-9));
    }

    @Test
    void shouldFindCogOfZoneThree()
    {
        PolarCoordinate cog = zoneThree.getCenterOfGravity();
        assertThat(cog.getLatitude()).isEqualTo(5, offset(1E-9));
        assertThat(cog.getLongitude()).isEqualTo(5, offset(1E-9));
    }

    @Test
    void shouldFindCogOfZoneFour()
    {
        PolarCoordinate cog = zoneFour.getCenterOfGravity();
        assertThat(cog.getLatitude()).isEqualTo(5, offset(1E-9));
        assertThat(cog.getLongitude()).isEqualTo(5, offset(1E-9));
    }

    @Test
    void shouldFindCogOfZoneFive()
    {
        PolarCoordinate cog = zoneFive.getCenterOfGravity();
        assertThat(cog.getLatitude()).isEqualTo(49.0, offset(1E-9));
        assertThat(cog.getLongitude()).isEqualTo(12.5, offset(1E-9));
    }

    @Test
    void shouldFindCogOfZoneSix()
    {
        PolarCoordinate cog = zoneSix.getCenterOfGravity();
        assertThat(cog.getLatitude()).isEqualTo(49.0, offset(1E-9));
        assertThat(cog.getLongitude()).isEqualTo(12.5, offset(1E-9));
    }

    @Test
    void shouldFindCogOfZoneSeven()
    {
        PolarCoordinate cog = zoneSeven.getCenterOfGravity();
        assertThat(cog.getLatitude()).isEqualTo(47.82193634361729, offset(1E-9));
        assertThat(cog.getLongitude()).isEqualTo(13.041786263143168, offset(1E-9));
    }

    static Stream<Arguments> pointsInsideOfSimpleZoneOneAndTwo()
    {
        return Stream.of(
            arguments(5.0, 5.0),
            arguments(2.5, 2.5),
            arguments(2.5, 7.5),
            arguments(7.5, 2.5),
            arguments(7.5, 7.5));
    }

    @ParameterizedTest
    @MethodSource("pointsInsideOfSimpleZoneOneAndTwo")
    void shouldFindPointsInsideOfSimpleZoneOne(double lat, double lon)
    {
        assertThat(simpleZoneOne.isInside(lat, lon)).isTrue();
    }

    @Test
    void shouldNotFindNullPositionInsideSimpleZoneOne()
    {
        assertThat(simpleZoneOne.isInside(null)).isFalse();
    }

    @Test
    void shouldConvertSimpleZoneOneToStringCorrectly()
    {
        assertThat(simpleZoneOne).hasToString(ZONE_ONE_AS_STRING);
    }

    @ParameterizedTest
    @MethodSource("pointsInsideOfSimpleZoneOneAndTwo")
    void shouldFindPointsInsideOfSimpleZoneTwo(double lat, double lon)
    {
        assertThat(simpleZoneTwo.isInside(lat, lon)).isTrue();
    }

    static Stream<Arguments> pointsOutsideOfSimpleZoneOneAndTwo()
    {
        return Stream.of(
            arguments(-1.0, 5.0),
            arguments(11.0, 5.0),
            arguments(5.0, -1.0),
            arguments(5.0, 110));
    }

    @ParameterizedTest
    @MethodSource("pointsOutsideOfSimpleZoneOneAndTwo")
    void shouldFindPointsOutsideOfSimpleZoneOne(double lat, double lon)
    {
        assertThat(simpleZoneOne.isInside(lat, lon)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("pointsOutsideOfSimpleZoneOneAndTwo")
    void shouldFindPointsOutsideOfSimpleZoneTwo(double lat, double lon)
    {
        assertThat(simpleZoneTwo.isInside(lat, lon)).isFalse();
    }

    static Stream<Arguments> pointsInsideOfZoneThreeAndFour()
    {
        return Stream.of(
            arguments(5.0, 5.0),
            arguments(2.50000001, 2.5),
            arguments(2.5, 2.50000001),
            arguments(7.5, 7.5));
    }

    @ParameterizedTest
    @MethodSource("pointsInsideOfZoneThreeAndFour")
    void shouldFindPointsInsideOfZoneThree(double lat, double lon)
    {
        assertThat(zoneThree.isInside(lat, lon)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("pointsInsideOfZoneThreeAndFour")
    void shouldFindPointsInsideOfZoneFour(double lat, double lon)
    {
        assertThat(zoneFour.isInside(lat, lon)).isTrue();
    }

    static Stream<Arguments> pointsOutsideOfZoneThreeAndFour()
    {
        return Stream.of(
            arguments(-1.0, 5.0),
            arguments(11.0, 5.0),
            arguments(5.0, -1.0),
            arguments(5.0, 110),
            arguments(2.5, 2.5));
    }

    @ParameterizedTest
    @MethodSource("pointsOutsideOfZoneThreeAndFour")
    void shouldFindPointsOutsideOfZoneThree(double lat, double lon)
    {
        assertThat(zoneThree.isInside(lat, lon)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("pointsOutsideOfZoneThreeAndFour")
    void shouldFindPointsOutsideOfZoneFour(double lat, double lon)
    {
        assertThat(zoneFour.isInside(lat, lon)).isFalse();
    }

    static Stream<Arguments> pointsInsideOfZoneFiveAndSix()
    {
        return Stream.of(
            arguments(new PolarCoordinate(48.5, 12.5, 0)));
    }

    @ParameterizedTest
    @MethodSource("pointsInsideOfZoneFiveAndSix")
    void shouldFindPointsInsideOfZoneFive(PolarCoordinate coordinate)
    {
        assertThat(zoneFive.isInside(coordinate)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("pointsInsideOfZoneFiveAndSix")
    void shouldFindPointsInsideOfZoneSix(PolarCoordinate coordinate)
    {
        assertThat(zoneSix.isInside(coordinate)).isTrue();
    }

    static Stream<Arguments> pointsOutsideOfZoneFiveAndSix()
    {
        return Stream.of(
            arguments(new PolarCoordinate(47.5, 12.5, 0)),
            arguments(new PolarCoordinate(50.5, 12.5, 0)),
            arguments(new PolarCoordinate(48.5, 11.5, 0)),
            arguments(new PolarCoordinate(48.5, 13.5, 0)));
    }

    @ParameterizedTest
    @MethodSource("pointsOutsideOfZoneFiveAndSix")
    void shouldFindPointsOutsideOfZoneFive(PolarCoordinate coordinate)
    {
        assertThat(zoneFive.isInside(coordinate)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("pointsOutsideOfZoneFiveAndSix")
    void shouldFindPointsOutsideOfZoneSix(PolarCoordinate coordinate)
    {
        assertThat(zoneSix.isInside(coordinate)).isFalse();
    }

    static Stream<Arguments> pointsInsideOfZoneSeven()
    {
        return Stream.of(
            arguments(new PolarCoordinate(47.82174572, 13.04135522, 0.00)),
            arguments(new PolarCoordinate(47.82161965, 13.04205796, 0.00)),
            arguments(new PolarCoordinate(47.82180335, 13.04218134, 0.00)),
            arguments(new PolarCoordinate(47.82205548, 13.04229400, 0.00)),
            arguments(new PolarCoordinate(47.82223917, 13.04199895, 0.00)),
            arguments(new PolarCoordinate(47.82209510, 13.04137766, 0.00)));
    }

    @ParameterizedTest
    @MethodSource("pointsInsideOfZoneSeven")
    void shouldFindPointsInsideOfZoneSeven(PolarCoordinate coordinate)
    {
        assertThat(zoneSeven.isInside(coordinate)).isTrue();
    }

    static Stream<Arguments> pointsOutsideOfZoneSeven()
    {
        return Stream.of(
            arguments(new PolarCoordinate(47.82201586, 13.04122648, 0.00)),
            arguments(new PolarCoordinate(47.82159804, 13.04160199, 0.00)),
            arguments(new PolarCoordinate(47.82154401, 13.04198286, 0.00)),
            arguments(new PolarCoordinate(47.82162685, 13.04216525, 0.00)),
            arguments(new PolarCoordinate(47.82198704, 13.04237983, 0.00)),
            arguments(new PolarCoordinate(47.82211671, 13.04232082, 0.00)),
            arguments(new PolarCoordinate(47.82235443, 13.04195067, 0.00)),
            arguments(new PolarCoordinate(47.82229320, 13.04157516, 0.00)));
    }

    @ParameterizedTest
    @MethodSource("pointsOutsideOfZoneSeven")
    void shouldFindPointsOutsideOfZoneSeven(PolarCoordinate coordinate)
    {
        assertThat(zoneSeven.isInside(coordinate)).isFalse();
    }

    static Stream<Arguments> pointsInsideOfZoneEight()
    {
        return Stream.of(
            arguments(new PolarCoordinate(2.0, 2.0, 0.00)),
            arguments(new PolarCoordinate(6.0, 2.0, 0.00)),
            arguments(new PolarCoordinate(6.0, 6.0, 0.00)),
            arguments(new PolarCoordinate(9.0, 4.0, 0.00)));
    }

    @ParameterizedTest
    @MethodSource("pointsInsideOfZoneEight")
    void shouldFindPointsInsideOfZoneEight(PolarCoordinate coordinate)
    {
        assertThat(zoneEight.isInside(coordinate)).isTrue();
    }

    static Stream<Arguments> pointsOutsideOfZoneEight()
    {
        return Stream.of(
            arguments(new PolarCoordinate(1.0, 1.0, 0.00)),
            arguments(new PolarCoordinate(4.0, 1.0, 0.00)),
            arguments(new PolarCoordinate(8.0, 1.0, 0.00)),
            arguments(new PolarCoordinate(9.0, 10.0, 0.00)),
            arguments(new PolarCoordinate(10.0, 1.0, 0.00)));
    }

    @ParameterizedTest
    @MethodSource("pointsOutsideOfZoneEight")
    void shouldFindPointsOutsideOfZoneEight(PolarCoordinate coordinate)
    {
        assertThat(zoneEight.isInside(coordinate)).isFalse();
    }

    static Stream<Arguments> pointsInsideOfZoneNine()
    {
        return Stream.of(
            arguments(new PolarCoordinate(2.0, 2.0, 0.00)),
            arguments(new PolarCoordinate(2.0, 3.0, 0.00)));
    }

    @ParameterizedTest
    @MethodSource("pointsInsideOfZoneNine")
    void shouldFindPointsInsideOfZoneNine(PolarCoordinate coordinate)
    {
        assertThat(zoneNine.isInside(coordinate)).isTrue();
    }

    static Stream<Arguments> pointsOutsideOfZoneNine()
    {
        return Stream.of(
            arguments(new PolarCoordinate(1.0, 1.0, 0.00)),
            arguments(new PolarCoordinate(4.0, 1.0, 0.00)),
            arguments(new PolarCoordinate(8.0, 1.0, 0.00)),
            arguments(new PolarCoordinate(9.0, 10.0, 0.00)),
            arguments(new PolarCoordinate(10.0, 1.0, 0.00)));
    }

    @ParameterizedTest
    @MethodSource("pointsOutsideOfZoneNine")
    void shouldFindPointsOutsideOfZoneNine(PolarCoordinate coordinate)
    {
        assertThat(zoneNine.isInside(coordinate)).isFalse();
    }

    @Test
    void shouldReturnVerticesAsGivenToTheConstructors()
    {
        List<LngLatAlt> actual = zoneNine.getVertices();

        assertThat(actual).hasSameElementsAs(Arrays.asList(verticesZoneNine));
    }
}
