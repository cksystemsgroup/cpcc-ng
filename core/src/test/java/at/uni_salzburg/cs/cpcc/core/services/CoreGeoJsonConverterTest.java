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

package at.uni_salzburg.cs.cpcc.core.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.geojson.Polygon;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicleType;
import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CoreGeoJsonConverterTest
{
    private RealVehicle rv1;
    private RealVehicle rv2;
    private RealVehicle rv3;
    private CoreGeoJsonConverterImpl conv;

    @BeforeMethod
    public void setUp()
    {
        rv1 = mock(RealVehicle.class);
        when(rv1.getName()).thenReturn("rv1");
        when(rv1.getType()).thenReturn(RealVehicleType.QUADROCOPTER);
        when(rv1.getId()).thenReturn(1);

        rv2 = mock(RealVehicle.class);
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

        rv3 = mock(RealVehicle.class);
        when(rv3.getName()).thenReturn("rv3");
        when(rv3.getAreaOfOperation()).thenReturn("["
            + "{lat:37.80800,lng:-122.42400},{lat:37.80800,lng:-122.42500},{lat:37.80900,lng:-122.42500},"
            + "{lat:37.80900,lng:-122.42400},{lat:37.80800,lng:-122.42400}]");
        when(rv3.getType()).thenReturn(RealVehicleType.GROUND_STATION);
        when(rv3.getId()).thenReturn(3);

        conv = new CoreGeoJsonConverterImpl();
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
        Point point = conv.toPoint(position);
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
        Feature feature = conv.toFeature(rv);

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

    @Test
    public void buggeritRV() throws JsonProcessingException
    {
        double[][] pos =
        {{37.80700, -122.42400}, {37.80700, -122.42500}, {37.80800, -122.42500}, {37.80800, -122.42400},
            {37.80700, -122.42400}};
        double[] depot = {37.80750, -122.42450};

        Point point = new Point(depot[1], depot[0]);

        Feature pointFeature = new Feature();
        pointFeature.setGeometry(point);
        pointFeature.setProperty("type", "depot");

        List<LngLatAlt> l = new ArrayList<LngLatAlt>();
        for (double[] p : pos)
        {
            l.add(new LngLatAlt(p[1], p[0]));
        }

        Polygon polygon = new Polygon(l);

        Feature polygonFeature = new Feature();
        polygonFeature.setGeometry(polygon);
        polygonFeature.setProperty("minAlt", 20);
        polygonFeature.setProperty("maxAlt", 50);

        FeatureCollection fc = new FeatureCollection();
        fc.add(pointFeature);
        fc.add(polygonFeature);

        String json = new ObjectMapper().writeValueAsString(fc);

        System.out.println(json);
    }

    @Test
    public void buggeritGS() throws JsonProcessingException
    {
        Point point = new Point(-122.42600, 37.80800);

        Feature pointFeature = new Feature();
        pointFeature.setGeometry(point);
        pointFeature.setProperty("type", "depot");

        FeatureCollection fc = new FeatureCollection();
        fc.add(pointFeature);

        String json = new ObjectMapper().writeValueAsString(fc);

        System.out.println(json);
    }
}
