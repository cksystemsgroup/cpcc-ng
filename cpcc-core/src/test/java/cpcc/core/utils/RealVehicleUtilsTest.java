// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.geojson.LngLatAlt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import cpcc.core.base.PolygonZone;
import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleType;

/**
 * RealVehicleUtilsTest implementation.
 */
class RealVehicleUtilsTest
{
    private static final String AOO_001 =
        "{\"type\":\"FeatureCollection\",\"features\":["
            + "{\"type\":\"Feature\""
            + ",\"properties\":{\"type\":\"depot\"}"
            + ",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-122.4245,37.8085]}},"
            + "{\"type\":\"Feature\""
            + ",\"properties\":{\"minAlt\":20,\"maxAlt\":50}"
            + ",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-122.424,37.808],[-122.425,37.808]"
            + ",[-122.425,37.809],[-122.424,37.809],[-122.424,37.808]]]}}"
            + "]}";

    private static final List<LngLatAlt> AOO_001_DEPOT = Arrays.asList(new LngLatAlt(-122.4245, 37.8085));
    private static final List<List<LngLatAlt>> AOO_001_POLYGONS = Arrays.asList(
        Arrays.asList(new LngLatAlt(-122.424, 37.808), new LngLatAlt(-122.425, 37.808),
            new LngLatAlt(-122.425, 37.809), new LngLatAlt(-122.424, 37.809), new LngLatAlt(-122.424, 37.808)));

    private static final String AOO_002 =
        "{\"type\":\"FeatureCollection\",\"features\":["
            + "{\"type\":\"Feature\""
            + ",\"properties\":{\"type\":\"depot\"}"
            + ",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-122.4265,37.8095]}},"
            + "{\"type\":\"Feature\""
            + ",\"properties\":{\"type\":\"nodepot\"}"
            + ",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-122.4255,37.8095]}},"
            + "{\"type\":\"Feature\""
            + ",\"properties\":{\"minAlt\":20,\"maxAlt\":50}"
            + ",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-122.426,37.809],[-122.427,37.809]"
            + ",[-122.427,37.81],[-122.426,37.81],[-122.426,37.809]]]}}]}";

    private static final List<LngLatAlt> AOO_002_DEPOT = Arrays.asList(new LngLatAlt(-122.4265, 37.8095));
    private static final List<List<LngLatAlt>> AOO_002_POLYGONS = Arrays.asList(
        Arrays.asList(new LngLatAlt(-122.426, 37.809), new LngLatAlt(-122.427, 37.809),
            new LngLatAlt(-122.427, 37.81), new LngLatAlt(-122.426, 37.81), new LngLatAlt(-122.426, 37.809)));

    private static final String AOO_003 =
        "{\"type\":\"FeatureCollection\",\"features\":["
            + "{\"type\":\"Feature\""
            + ",\"properties\":{\"minAlt\":20,\"maxAlt\":50}"
            + ",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-122.425,37.809],[-122.426,37.809]"
            + ",[-122.426,37.81],[-122.425,37.81],[-122.425,37.809]]]}}]}";

    private static final List<LngLatAlt> AOO_003_DEPOT = Collections.<LngLatAlt> emptyList();
    private static final List<List<LngLatAlt>> AOO_003_POLYGONS = Arrays.asList(
        Arrays.asList(new LngLatAlt(-122.425, 37.809), new LngLatAlt(-122.426, 37.809),
            new LngLatAlt(-122.426, 37.81), new LngLatAlt(-122.425, 37.81), new LngLatAlt(-122.425, 37.809)));

    private static final String AOO_004 =
        "{\"type\":\"FeatureCollection\",\"features\":["
            + "{\"type\":\"Feature\""
            + ",\"properties\":{\"type\":\"depot\"}"
            + ",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-122.4244737625122,37.807457166355256]}},"
            + "{\"type\":\"Feature\""
            + ",\"properties\":{\"minAlt\":0,\"maxAlt\":20}"
            + ",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":["
            + "[[-122.42499947547913,37.807999665973156],[-122.42500014603137,37.80699942920263],"
            + "[-122.42399498820306,37.806999958992634],[-122.42399901151656,37.807999136190325],"
            + "[-122.42499947547913,37.807999665973156]]"
            + "]}},"
            + "{\"type\":\"Feature\""
            + ",\"properties\":{\"type\":\"depot\"}"
            + ",\"geometry\":{\"type\":\"Point\",\"coordinates\":[-122.42357254028322,37.80736392383205]}},"
            + "{\"type\":\"Feature\""
            + ",\"properties\":{\"minAlt\":0,\"maxAlt\":20}"
            + ",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":["
            + "[[-122.42366373538971,37.80775384632759],[-122.42399632930756,37.807456106781785],"
            + "[-122.42399632930756,37.807193332092844],[-122.42373347282411,37.806940092697346],"
            + "[-122.42318630218506,37.80728339610364],[-122.42314875125885,37.807766561156924],"
            + "[-122.42366373538971,37.80775384632759]]"
            + "]}}]"
            + ",\"properties\":{\"zoom\":19"
            + ",\"center\":{\"lat\":37.80749954928144,\"lng\":-122.42540985345839},\"layer\":null}}";

    private static final List<LngLatAlt> AOO_004_DEPOT =
        Arrays.asList(
            new LngLatAlt(-122.4244737625122, 37.807457166355256),
            new LngLatAlt(-122.42357254028322, 37.80736392383205));
    private static final List<List<LngLatAlt>> AOO_004_POLYGONS = Arrays.asList(
        Arrays.asList(
            new LngLatAlt(-122.42499947547913, 37.807999665973156),
            new LngLatAlt(-122.42500014603137, 37.80699942920263),
            new LngLatAlt(-122.42399498820306, 37.806999958992634),
            new LngLatAlt(-122.42399901151656, 37.807999136190325),
            new LngLatAlt(-122.42499947547913, 37.807999665973156)),
        Arrays.asList(
            new LngLatAlt(-122.42366373538971, 37.80775384632759),
            new LngLatAlt(-122.42399632930756, 37.807456106781785),
            new LngLatAlt(-122.42399632930756, 37.807193332092844),
            new LngLatAlt(-122.42373347282411, 37.806940092697346),
            new LngLatAlt(-122.42318630218506, 37.80728339610364),
            new LngLatAlt(-122.42314875125885, 37.807766561156924),
            new LngLatAlt(-122.42366373538971, 37.80775384632759)));

    @Test
    void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<RealVehicleUtils> cnt = RealVehicleUtils.class.getDeclaredConstructor();
        assertThat(cnt.isAccessible()).isFalse();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    static Stream<Arguments> depotDataProvider()
    {
        return Stream.of(
            arguments(AOO_001, AOO_001_DEPOT),
            arguments(AOO_002, AOO_002_DEPOT),
            arguments(AOO_003, AOO_003_DEPOT),
            arguments(AOO_004, AOO_004_DEPOT));
    }

    @ParameterizedTest
    @MethodSource("depotDataProvider")
    void shouldGetDepotPosition(String areaOfOperation, List<LngLatAlt> depotPosition) throws IOException
    {
        List<PolarCoordinate> expected = depotPosition.stream()
            .map(x -> new PolarCoordinate(x.getLatitude(), x.getLongitude(), x.getAltitude()))
            .collect(Collectors.toList());

        List<PolarCoordinate> actual = RealVehicleUtils.getDepotPositions(areaOfOperation);

        assertThat(actual).has(new PolarCoordinateListCondition(expected, 1E-8));
    }

    static Stream<Arguments> polygonDataProvider()
    {
        return Stream.of(
            arguments(AOO_001, AOO_001_POLYGONS),
            arguments(AOO_002, AOO_002_POLYGONS),
            arguments(AOO_003, AOO_003_POLYGONS),
            arguments(AOO_004, AOO_004_POLYGONS));
    }

    @ParameterizedTest
    @MethodSource("polygonDataProvider")
    void shouldGetPolygons(String areaOfOperation, List<List<LngLatAlt>> polygons) throws IOException
    {
        List<PolygonZone> expected = polygons.stream().map(x -> new PolygonZone(x)).collect(Collectors.toList());

        List<PolygonZone> actual = RealVehicleUtils.getPolygons(areaOfOperation);

        assertThat(actual).has(new PolygonListCondition(expected, 1E-8));
    }

    static Stream<Arguments> positionDataProvider()
    {
        return Stream.of(
            arguments(AOO_001, new PolarCoordinate(37.8085, -122.4245, 0.0), true),
            arguments(AOO_002, new PolarCoordinate(37.8085, -122.4245, 0.0), false),
            arguments(AOO_003, new PolarCoordinate(37.8085, -122.4245, 0.0), false),
            arguments(AOO_004, new PolarCoordinate(37.8085, -122.4245, 0.0), false),
            arguments(AOO_002, new PolarCoordinate(37.8095, -122.4265, 0.0), true),
            arguments(AOO_003, new PolarCoordinate(37.8095, -122.4255, 0.0), true),
            arguments(AOO_004, new PolarCoordinate(37.8075, -122.4245, 0.0), true),
            arguments(AOO_004, new PolarCoordinate(37.8074, -122.4236, 0.0), true));
    }

    @ParameterizedTest
    @MethodSource("positionDataProvider")
    void shouldCheckIfPositionsAreInsideTheAreaOfOperation(String areaOfOperation, PolarCoordinate position,
        boolean expectedResult) throws IOException
    {
        boolean actual = RealVehicleUtils.isInsideAreaOfOperation(areaOfOperation, position);

        assertThat(actual).isEqualTo(expectedResult);
    }

    static Stream<Arguments> failingInsideCheckDataProvider()
    {
        return Stream.of(
            arguments("", mock(PolarCoordinate.class)),
            arguments((String) null, mock(PolarCoordinate.class)),
            arguments("{", mock(PolarCoordinate.class)));
    }

    @ParameterizedTest
    @MethodSource("failingInsideCheckDataProvider")
    void shouldReturnFalseOnFailingInsideChecks(String areaOfOperation, PolarCoordinate position)
    {
        boolean actual = RealVehicleUtils.isInsideAreaOfOperation(areaOfOperation, position);

        assertThat(actual).isFalse();
        verifyNoInteractions(position);
    }

    @Test
    void shouldReturnEmptyDepotListIfAreaOfOperationIsEmpty() throws IOException
    {
        List<PolarCoordinate> actual = RealVehicleUtils.getDepotPositions("");

        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnEmptyPolygonListIfAreaOfOperationIsEmpty() throws IOException
    {
        List<PolygonZone> actual = RealVehicleUtils.getPolygons(null);

        assertThat(actual).isEmpty();
    }

    static Stream<Arguments> bboxPositionDataProvider()
    {
        RealVehicle rv1 = mock(RealVehicle.class);
        RealVehicle rv2 = mock(RealVehicle.class);
        RealVehicle rv3 = mock(RealVehicle.class);

        when(rv1.getName()).thenReturn("rv1");
        when(rv1.getType()).thenReturn(RealVehicleType.QUADROCOPTER);
        when(rv1.getId()).thenReturn(1);

        when(rv2.getName()).thenReturn("rv2");
        when(rv2.getAreaOfOperation()).thenReturn("{"
            + "\"type\":\"FeatureCollection\","
            + "\"features\":["
            + "{\"type\":\"Feature\",\"properties\":{\"type\":\"depot\"},\"geometry\":"
            + "{\"type\":\"Point\",\"coordinates\":[-122.4255,37.8085]}},"
            + "{\"type\":\"Feature\",\"properties\":{\"minAlt\":20,\"maxAlt\":50},\"geometry\":"
            + "{\"type\":\"Polygon\",\"coordinates\":[[[-122.425,37.808],[-122.426,37.808],[-122.426,37.809],"
            + "[-122.425,37.809],[-122.425,37.808]]]}}]}");
        when(rv2.getType()).thenReturn(RealVehicleType.FIXED_WING_AIRCRAFT);
        when(rv2.getId()).thenReturn(2);

        when(rv3.getName()).thenReturn("rv3");
        when(rv3.getAreaOfOperation()).thenReturn("["
            + "{lat:37.80800,lng:-122.42400},{lat:37.80800,lng:-122.42500},{lat:37.80900,lng:-122.42500},"
            + "{lat:37.80900,lng:-122.42400},{lat:37.80800,lng:-122.42400}]");
        when(rv3.getType()).thenReturn(RealVehicleType.GROUND_STATION);
        when(rv3.getId()).thenReturn(3);

        return Stream.of(
            arguments(
                new BBTestParameters(
                    Arrays.asList(rv1),
                    new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN})),
            arguments(
                new BBTestParameters(
                    Arrays.asList(rv2),
                    new double[]{-122.426, 37.808, -122.425, 37.809})),
            arguments(
                new BBTestParameters(
                    Arrays.asList(rv3),
                    new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN})));
    }

    @ParameterizedTest
    @MethodSource("bboxPositionDataProvider")
    void shouldFindBoundingBox(BBTestParameters params)
    {
        double[] actual = RealVehicleUtils.findBoundingBox(params.getRealVehicle());

        assertThat(actual).isEqualTo(params.getExpected());
    }

    static class BBTestParameters
    {
        private List<RealVehicle> realVehicleList;
        private double[] expected;

        public BBTestParameters(List<RealVehicle> realVehicle, double[] expected)
        {
            this.realVehicleList = realVehicle;
            this.expected = expected;
        }

        public List<RealVehicle> getRealVehicle()
        {
            return realVehicleList;
        }

        public double[] getExpected()
        {
            return expected;
        }

        @Override
        public String toString()
        {
            StringBuilder b = new StringBuilder();

            String delimiter = "";
            b.append("RV=[");
            for (RealVehicle rv : realVehicleList)
            {
                b.append(rv.getName());
                delimiter = ",";
            }
            b.append("] ");

            delimiter = "";
            for (double d : expected)
            {
                b.append(delimiter).append(d);
                delimiter = ",";
            }
            b.append("]");

            return b.toString();
        }
    }

}
