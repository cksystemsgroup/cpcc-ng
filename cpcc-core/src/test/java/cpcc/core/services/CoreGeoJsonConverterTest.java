// This code is part of the CPCC-NG project.
//
// Copyright (c) 2014 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.core.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.geojson.Feature;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleType;
import cpcc.core.utils.PolarCoordinate;

public class CoreGeoJsonConverterTest
{
    private RealVehicle rv1 = mock(RealVehicle.class);
    private RealVehicle rv2 = mock(RealVehicle.class);
    private RealVehicle rv3 = mock(RealVehicle.class);
    private CoreGeoJsonConverterImpl sut;

    @BeforeMethod
    public void setUp()
    {
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
            + "[-122.425,37.809],[-122.425,37.808]]]}}]}"
            );
        when(rv2.getType()).thenReturn(RealVehicleType.FIXED_WING_AIRCRAFT);
        when(rv2.getId()).thenReturn(2);

        when(rv3.getName()).thenReturn("rv3");
        when(rv3.getAreaOfOperation()).thenReturn("["
            + "{lat:37.80800,lng:-122.42400},{lat:37.80800,lng:-122.42500},{lat:37.80900,lng:-122.42500},"
            + "{lat:37.80900,lng:-122.42400},{lat:37.80800,lng:-122.42400}]");
        when(rv3.getType()).thenReturn(RealVehicleType.GROUND_STATION);
        when(rv3.getId()).thenReturn(3);

        sut = new CoreGeoJsonConverterImpl();
    }

    @DataProvider
    public Object[][] polarCoordinatesDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new PolarCoordinate(0, 0, 0),
                new LngLatAlt(0, 0, 0)
            },
            new Object[]{
                new PolarCoordinate(47.12345678, 13.12345678, 50.1),
                new LngLatAlt(13.12345678, 47.12345678, 50.1)
            },
            new Object[]{
                new PolarCoordinate(-47.12345678, -13.12345678, -50.1),
                new LngLatAlt(-13.12345678, -47.12345678, -50.1)
            },
        };
    }

    @Test(dataProvider = "polarCoordinatesDataProvider")
    public void shouldConvertPolarCoordinateToPoint(PolarCoordinate position, LngLatAlt expected)
    {
        Point point = sut.toPoint(position);
        LngLatAlt coord = point.getCoordinates();

        assertThat(coord.getLongitude())
            .overridingErrorMessage("Longitude differs %.8f != %.8f", coord.getLongitude(), expected.getLongitude())
            .isEqualTo(expected.getLongitude(), offset(1E-8));

        assertThat(coord.getLatitude())
            .overridingErrorMessage("Latitude differs %.8f != %.8f", coord.getLatitude(), expected.getLatitude())
            .isEqualTo(expected.getLatitude(), offset(1E-8));

        assertThat(coord.getAltitude())
            .overridingErrorMessage("Altitude differs %.3f != %.3f", coord.getAltitude(), expected.getAltitude())
            .isEqualTo(expected.getAltitude(), offset(1E-3));
    }

    @DataProvider
    public Object[][] positionDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new PolarCoordinate(0, 0, 0),
            },
            new Object[]{
                new PolarCoordinate(47.12345678, 13.12345678, 50.1),
            },
            new Object[]{
                new PolarCoordinate(-47.12345678, -13.12345678, -50.1),
            },
        };
    }

    @Test(dataProvider = "positionDataProvider")
    public void shouldConvertPolarCoordinateToPositionMap(PolarCoordinate position)
    {
        Map<String, Double> actual = sut.toPosition(position);

        assertThat(actual.get("lat")).isEqualTo(position.getLatitude(), offset(1E-9));
        assertThat(actual.get("lon")).isEqualTo(position.getLongitude(), offset(1E-9));
        assertThat(actual.get("alt")).isEqualTo(position.getAltitude(), offset(1E-9));
    }

    @DataProvider
    public Object[][] realVehicleDataProvider()
    {
        return new Object[][]{
            new Object[]{rv1, "{"
                + "\"type\":\"Feature\","
                + "\"properties\":{\"name\":\"rv1\",\"rvtype\":\"QUADROCOPTER\",\"type\":\"rv\"},"
                + "\"id\":\"1\""
                + "}"
            },
            new Object[]{rv2, "{"
                + "\"type\":\"Feature\","
                + "\"properties\":{\"name\":\"rv2\",\"rvtype\":\"FIXED_WING_AIRCRAFT\",\"type\":\"rv\"},"
                // + "\"geometry\":{\"type\":\"FeatureCollection\",\"features\":["
                // + "{\"type\":\"Feature\",\"properties\":{\"type\":\"depot\"},"
                // + "\"geometry\":{\"type\":\"Point\",\"coordinates\":[-122.4255,37.8085]}},"
                // + "{\"type\":\"Feature\",\"properties\":{\"minAlt\":20,\"maxAlt\":50},"
                // + "\"geometry\":{\"type\":\"Polygon\",\"coordinates\":["
                // + "[[-122.425,37.808],[-122.426,37.808],[-122.426,37.809],[-122.425,37.809],[-122.425,37.808]]]}"
                // + "}]},"
                + "\"id\":\"2\"}"
            },
        };
    }

    @Test(dataProvider = "realVehicleDataProvider")
    public void shouldConvertRealVehicleToFeature(RealVehicle rv, String expected) throws IOException, JSONException
    {
        Feature feature = sut.toFeature(rv);

        String actual = new ObjectMapper().writeValueAsString(feature);

        assertThat(actual).isNotNull();
        JSONAssert.assertEquals(expected, actual, false);
    }

    @DataProvider
    public Object[][] realVehicleListDataProvider()
    {
        return new Object[][]{
            new Object[]{Arrays.asList(rv1),
                "{\"type\":\"FeatureCollection\",\"features\":["
                    + "{\"type\":\"Feature\","
                    + "\"properties\":{\"name\":\"rv1\",\"rvtype\":\"QUADROCOPTER\",\"type\":\"rv\"},\"id\":\"1\"}]}"
            },
            new Object[]{Arrays.asList(rv1, rv2),
                "{\"type\":\"FeatureCollection\",\"features\":["
                    + "{\"type\":\"Feature\","
                    + "\"properties\":{\"name\":\"rv1\",\"rvtype\":\"QUADROCOPTER\",\"type\":\"rv\"},\"id\":\"1\"},"
                    + "{\"type\":\"Feature\","
                    + "\"properties\":{\"name\":\"rv2\",\"rvtype\":\"FIXED_WING_AIRCRAFT\",\"type\":\"rv\"},"
                    + "\"geometry\":{\"type\":\"FeatureCollection\",\"features\":["
                    + "{\"type\":\"Feature\","
                    + "\"properties\":{\"type\":\"depot\"},"
                    + "\"geometry\":{\"type\":\"Point\",\"coordinates\":[-122.4255,37.8085]}},"
                    + "{\"type\":\"Feature\","
                    + "\"properties\":{\"minAlt\":20,\"maxAlt\":50},"
                    + "\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-122.425,37.808],[-122.426,37.808],"
                    + "[-122.426,37.809],[-122.425,37.809],[-122.425,37.808]]]}}]},\"id\":\"2\"}]}"
            },
        };
    }

    class DPTestParameters
    {
        private RealVehicle realVehicle;
        private List<Point> expected;

        public DPTestParameters(RealVehicle realVehicle, List<Point> expected)
        {
            this.realVehicle = realVehicle;
            this.expected = expected;
        }

        public RealVehicle getRealVehicle()
        {
            return realVehicle;
        }

        public List<Point> getExpected()
        {
            return expected;
        }

        @Override
        public String toString()
        {
            StringBuilder b = new StringBuilder();
            b.append("RV=").append(realVehicle.getName()).append(", Points=[");

            String delimiter = "";

            for (Point p : expected)
            {
                double lng = p.getCoordinates().getLongitude();
                double lat = p.getCoordinates().getLatitude();

                b.append(delimiter).append("[").append(lng).append(",").append(lat).append("]");
                delimiter = ",";
            }

            b.append("]");

            return b.toString();
        }
    }

    @DataProvider
    public Object[][] depotPositionDataProvider()
    {
        return new Object[][]{
            new Object[]{new DPTestParameters(rv1, Collections.<Point> emptyList())},
            new Object[]{new DPTestParameters(rv2, Arrays.asList(new Point(-122.4255, 37.8085)))},
            new Object[]{new DPTestParameters(rv3, Collections.<Point> emptyList())},
        };
    }

    @Test(dataProvider = "depotPositionDataProvider")
    public void shouldFindDepotPositions(DPTestParameters params)
    {
        List<Point> actual = sut.findDepotPositions(params.getRealVehicle());

        if (params.getExpected().isEmpty())
        {
            assertThat(actual).isEmpty();
        }
        else
        {
            assertThat(actual)
                .usingElementComparator(new PointComparator())
                .containsExactly(params.getExpected().toArray(new Point[0]));
        }
    }

    public class PointComparator implements Comparator<Point>
    {
        @Override
        public int compare(Point o1, Point o2)
        {
            LngLatAlt c1 = o1.getCoordinates();
            LngLatAlt c2 = o2.getCoordinates();
            boolean eq = Math.abs(c1.getLongitude() - c2.getLongitude()) < 1E-6
                && Math.abs(c1.getLatitude() - c2.getLatitude()) < 1E-6;
            // System.out.println(posToString(c1) + (eq ? " == " : " != ") + posToString(c2));
            return eq ? 0 : 1;
        }
    }

    // private static String posToString(LngLatAlt p)
    // {
    //     return "[" + p.getLongitude() + "," + p.getLatitude() + "]";
    // }

    class BBTestParameters
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

    @DataProvider
    public Object[][] bboxPositionDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new BBTestParameters(Arrays.asList(rv1), new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN})},
            new Object[]{
                new BBTestParameters(Arrays.asList(rv2), new double[]{-122.426, 37.808, -122.425, 37.809})},
            new Object[]{
                new BBTestParameters(Arrays.asList(rv3), new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN})},
        };
    }

    @Test(dataProvider = "bboxPositionDataProvider")
    public void shouldFindBoundingBox(BBTestParameters params)
    {
        double[] actual = sut.findBoundingBox(params.getRealVehicle());

        assertThat(actual).isEqualTo(params.getExpected());
    }

//    @Test
//    public void buggeritRV() throws JsonProcessingException
//    {
//        double[][] pos =
//        {{37.80700, -122.42400}, {37.80700, -122.42500}, {37.80800, -122.42500}, {37.80800, -122.42400},
//            {37.80700, -122.42400}};
//        double[] depot = {37.80750, -122.42450};
//
//        Point point = new Point(depot[1], depot[0]);
//
//        Feature pointFeature = new Feature();
//        pointFeature.setGeometry(point);
//        pointFeature.setProperty("type", "depot");
//
//        List<LngLatAlt> l = new ArrayList<LngLatAlt>();
//        for (double[] p : pos)
//        {
//            l.add(new LngLatAlt(p[1], p[0]));
//        }
//
//        Polygon polygon = new Polygon(l);
//
//        Feature polygonFeature = new Feature();
//        polygonFeature.setGeometry(polygon);
//        polygonFeature.setProperty("minAlt", 20);
//        polygonFeature.setProperty("maxAlt", 50);
//
//        FeatureCollection fc = new FeatureCollection();
//        fc.add(pointFeature);
//        fc.add(polygonFeature);
//
//        String json = new ObjectMapper().writeValueAsString(fc);
//
//        // TODO fixme
//        // System.out.println(json);
//    }
//
//    @Test
//    public void buggeritGS() throws JsonProcessingException
//    {
//        Point point = new Point(-122.42600, 37.80800);
//
//        Feature pointFeature = new Feature();
//        pointFeature.setGeometry(point);
//        pointFeature.setProperty("type", "depot");
//
//        FeatureCollection fc = new FeatureCollection();
//        fc.add(pointFeature);
//
//        String json = new ObjectMapper().writeValueAsString(fc);
//
//        // TODO fixme
//        // System.out.println(json);
//    }
}
