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
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.geojson.Polygon;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleType;

public class GeoJsonUtilsTest
{
    private RealVehicle rv1 = mock(RealVehicle.class);
    private RealVehicle rv2 = mock(RealVehicle.class);
    private RealVehicle rv3 = mock(RealVehicle.class);

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
    }

    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<GeoJsonUtils> cnt = GeoJsonUtils.class.getDeclaredConstructor();
        assertThat(cnt.isAccessible()).isFalse();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @DataProvider
    public Object[][] geoJsonObjectsDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new GeoJsonObject()
                {
                },
                new double[]{}
            },
            new Object[]{
                new Point(-122.8, 37.2),
                new double[]{-122.8, 37.2, -122.8, 37.2}
            },
            new Object[]{
                new Polygon(
                    new LngLatAlt(-122.426, 37.808),
                    new LngLatAlt(-122.427, 37.808),
                    new LngLatAlt(-122.427, 37.809),
                    new LngLatAlt(-122.426, 37.809),
                    new LngLatAlt(-122.426, 37.808)
                ),
                new double[]{-122.427, 37.808, -122.426, 37.809}
            },
        };
    }

    @Test(dataProvider = "geoJsonObjectsDataProvider")
    public void shouldFindBoundingBox(GeoJsonObject obj, double[] expected)
    {
        double[] actual = GeoJsonUtils.findBoundingBox(obj);

        assertThat(actual).isEqualTo(expected);
    }

    @DataProvider
    public Object[][] bboxPositionDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN},
                new LngLatAlt(-122.8, 37.2),
                new double[]{-122.8, 37.2, -122.8, 37.2}
            },
            new Object[]{
                new double[]{-122.8, 37.2, -122.8, 37.2},
                new LngLatAlt(-122.8, 37.2),
                new double[]{-122.8, 37.2, -122.8, 37.2}
            },
            new Object[]{
                new double[]{-122.8, 37.2, -122.8, 37.2},
                new LngLatAlt(-122.9, 37.3),
                new double[]{-122.9, 37.2, -122.8, 37.3}
            },
            new Object[]{
                new double[]{-122.8, 37.2, -122.8, 37.2},
                new LngLatAlt(-122.7, 37.1),
                new double[]{-122.8, 37.1, -122.7, 37.2}
            },
        };
    }

    @Test(dataProvider = "bboxPositionDataProvider")
    public void shouldMergeBoundingBoxWithPosition(double[] actual, LngLatAlt position, double[] expected)
    {
        GeoJsonUtils.mergeBoundingBox(actual, position);

        assertThat(actual).isEqualTo(expected);
    }

    @DataProvider
    public Object[][] bboxesDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN},
                new double[]{-122.8, 37.2, -122.8, 37.2},
                new double[]{-122.8, 37.2, -122.8, 37.2}
            },
            new Object[]{
                new double[]{-122.8, 37.2, -122.8, 37.2},
                new double[]{-122.8, 37.2, -122.8, 37.2},
                new double[]{-122.8, 37.2, -122.8, 37.2}
            },
            new Object[]{
                new double[]{-122.8, 37.2, -122.8, 37.2},
                new double[]{-122.9, 37.3, -122.8, 37.3},
                new double[]{-122.9, 37.2, -122.8, 37.3}
            },
            new Object[]{
                new double[]{-122.8, 37.2, -122.8, 37.2},
                new double[]{-122.7, 37.1, -122.7, 37.1},
                new double[]{-122.8, 37.1, -122.7, 37.2}
            },
        };
    }

    @Test(dataProvider = "bboxesDataProvider")
    public void shouldMergeBoundingBoxes(double[] actual, double[] other, double[] expected)
    {
        GeoJsonUtils.mergeBoundingBoxes(actual, other);

        assertThat(actual).isEqualTo(expected);
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
        Point point = GeoJsonUtils.toPoint(position);
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
        Map<String, Double> actual = GeoJsonUtils.toPosition(position);

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
                + "\"id\":\"2\"}"
            },
        };
    }

    @Test(dataProvider = "realVehicleDataProvider")
    public void shouldConvertRealVehicleToFeature(RealVehicle rv, String expected) throws IOException, JSONException
    {
        Feature feature = GeoJsonUtils.toFeature(rv);

        String actual = new ObjectMapper().disable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(feature);

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

}
