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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicleType;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.entities.SensorType;
import at.uni_salzburg.cs.cpcc.core.entities.SensorVisibility;
import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;

public class CoreJsonConverterTest
{
    private CoreJsonConverter converter;
    private SensorDefinition s1 = mock(SensorDefinition.class);
    private SensorDefinition s2 = mock(SensorDefinition.class);
    private SensorDefinition s3 = mock(SensorDefinition.class);
    private SensorDefinition s4 = mock(SensorDefinition.class);
    private RealVehicle rv1 = mock(RealVehicle.class);
    private RealVehicle rv2 = mock(RealVehicle.class);
    private RealVehicle rv3 = mock(RealVehicle.class);
    private RealVehicle rv4 = mock(RealVehicle.class);

    @BeforeMethod
    public void setUp()
    {
        when(s1.getId()).thenReturn(1);
        when(s1.getDescription()).thenReturn("Altimeter");
        when(s1.getType()).thenReturn(SensorType.ALTIMETER);
        when(s1.getMessageType()).thenReturn("std_msgs/Float32");
        when(s1.getVisibility()).thenReturn(SensorVisibility.ALL_VV);
        when(s1.getParameters()).thenReturn("random=10:35");
        when(s1.getLastUpdate()).thenReturn(new Date(10001));

        when(s2.getId()).thenReturn(2);
        when(s2.getDescription()).thenReturn("Barometer");
        when(s2.getType()).thenReturn(SensorType.BAROMETER);
        when(s2.getMessageType()).thenReturn("std_msgs/Float32");
        when(s2.getVisibility()).thenReturn(SensorVisibility.NO_VV);
        when(s2.getParameters()).thenReturn("random=1050:1080");
        when(s2.getLastUpdate()).thenReturn(new Date(20002));

        when(s3.getId()).thenReturn(3);
        when(s3.getDescription()).thenReturn("Belly Mounted Camera 640x480");
        when(s3.getType()).thenReturn(SensorType.CAMERA);
        when(s3.getMessageType()).thenReturn("sensor_msgs/Image");
        when(s3.getVisibility()).thenReturn(SensorVisibility.ALL_VV);
        when(s3.getParameters()).thenReturn("width=640 height=480 yaw=0 down=1.571 alignment=north");
        when(s3.getLastUpdate()).thenReturn(new Date(30003));

        when(s4.getId()).thenReturn(4);
        when(s4.getDescription()).thenReturn("GPS");
        when(s4.getType()).thenReturn(SensorType.GPS);
        when(s4.getMessageType()).thenReturn("sensor_msgs/NavSatFix");
        when(s4.getVisibility()).thenReturn(SensorVisibility.PRIVILEGED_VV);
        when(s4.getParameters()).thenReturn(null);

        when(rv1.getName()).thenReturn("rv1");
        when(rv1.getUrl()).thenReturn("http://localhost/rv01");
        when(rv1.getSensors()).thenReturn(Arrays.asList(s1, s2, s3));
        when(rv1.getAreaOfOperation()).thenReturn("["
            + "{lat:37.80800,lng:-122.42600},{lat:37.80800,lng:-122.42700},{lat:37.80900,lng:-122.42700},"
            + "{lat:37.80900,lng:-122.42600},{lat:37.80800,lng:-122.42600}]");
        when(rv1.getType()).thenReturn(RealVehicleType.QUADROCOPTER);
        when(rv1.getId()).thenReturn(1);
        when(rv1.getLastUpdate()).thenReturn(new Date(1001));

        when(rv2.getName()).thenReturn("rv2");
        when(rv2.getUrl()).thenReturn("http://localhost/rv02");
        when(rv2.getSensors()).thenReturn(Arrays.asList(s2, s3, s4));
        when(rv2.getAreaOfOperation()).thenReturn("["
            + "{lat:37.80800,lng:-122.42500},{lat:37.80800,lng:-122.42600},{lat:37.80900,lng:-122.42600},"
            + "{lat:37.80900,lng:-122.42500},{lat:37.80800,lng:-122.42500}]");
        when(rv2.getType()).thenReturn(RealVehicleType.FIXED_WING_AIRCRAFT);
        when(rv2.getId()).thenReturn(2);
        when(rv2.getLastUpdate()).thenReturn(new Date(2002));

        when(rv3.getName()).thenReturn("rv3");
        when(rv3.getUrl()).thenReturn("http://localhost/rv03");
        when(rv3.getSensors()).thenReturn(Arrays.asList(s3, s4, s1));
        when(rv3.getAreaOfOperation()).thenReturn("["
            + "{lat:37.80800,lng:-122.42400},{lat:37.80800,lng:-122.42500},{lat:37.80900,lng:-122.42500},"
            + "{lat:37.80900,lng:-122.42400},{lat:37.80800,lng:-122.42400}]");
        when(rv3.getType()).thenReturn(RealVehicleType.GROUND_STATION);
        when(rv3.getId()).thenReturn(3);
        when(rv3.getLastUpdate()).thenReturn(new Date(3003));

        when(rv4.getName()).thenReturn("rv4");
        when(rv4.getUrl()).thenReturn("http://localhost/rv04");
        when(rv4.getSensors()).thenReturn(Arrays.asList(s4));
        when(rv4.getAreaOfOperation()).thenReturn("["
            + "{lat:34.80800,lng:-124.42400},{lat:34.80800,lng:-124.42500},{lat:34.80900,lng:-124.42500},"
            + "{lat:34.80900,lng:-124.42400},{lat:34.80800,lng:-124.42400}]");
        when(rv4.getType()).thenReturn(RealVehicleType.TABLET);
        when(rv4.getId()).thenReturn(4);

        converter = new CoreJsonConverterImpl();
    }

    @DataProvider
    public Object[][] singleVehicleDataProvider()
    {
        return new Object[][]{
            new Object[]{rv1,
                "{\"id\":\"1\",\"aoo\":\"["
                    + "{lat:37.80800,lng:-122.42600},{lat:37.80800,lng:-122.42700},{lat:37.80900,lng:-122.42700},"
                    + "{lat:37.80900,lng:-122.42600},{lat:37.80800,lng:-122.42600}"
                    + "]\","
                    + "\"name\":\"rv1\","
                    + "\"sen\":["
                    + "{\"id\":\"1\",\"visibility\":\"ALL_VV\",\"lastUpdate\":10001,"
                    + "\"description\":\"Altimeter\",\"parameters\":\"random=10:35\","
                    + "\"messageType\":\"std_msgs/Float32\",\"type\":\"ALTIMETER\",\"deleted\":false},"
                    + "{\"id\":\"2\",\"visibility\":\"NO_VV\",\"lastUpdate\":20002,"
                    + "\"description\":\"Barometer\",\"parameters\":\"random=1050:1080\","
                    + "\"messageType\":\"std_msgs/Float32\",\"type\":\"BAROMETER\",\"deleted\":false},"
                    + "{\"id\":\"3\",\"visibility\":\"ALL_VV\",\"lastUpdate\":30003,"
                    + "\"description\":\"Belly Mounted Camera 640x480\","
                    + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment=north\","
                    + "\"messageType\":\"sensor_msgs/Image\",\"type\":\"CAMERA\",\"deleted\":false}],"
                    + "\"upd\":1001,"
                    + "\"type\":\"QUADROCOPTER\",\"deleted\":false,"
                    + "\"url\":\"http://localhost/rv01\"}"
            },
            new Object[]{rv2,
                "{\"id\":\"2\",\"aoo\":\"["
                    + "{lat:37.80800,lng:-122.42500},{lat:37.80800,lng:-122.42600},{lat:37.80900,lng:-122.42600},"
                    + "{lat:37.80900,lng:-122.42500},{lat:37.80800,lng:-122.42500}]\","
                    + "\"name\":\"rv2\",\"sen\":["
                    + "{\"id\":\"2\",\"visibility\":\"NO_VV\",\"lastUpdate\":20002,"
                    + "\"description\":\"Barometer\",\"parameters\":\"random=1050:1080\","
                    + "\"messageType\":\"std_msgs/Float32\",\"type\":\"BAROMETER\",\"deleted\":false},"
                    + "{\"id\":\"3\",\"visibility\":\"ALL_VV\",\"lastUpdate\":30003,"
                    + "\"description\":\"Belly Mounted Camera 640x480\","
                    + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment=north\","
                    + "\"messageType\":\"sensor_msgs/Image\",\"type\":\"CAMERA\",\"deleted\":false},"
                    + "{\"id\":\"4\",\"visibility\":\"PRIVILEGED_VV\","
                    + "\"description\":\"GPS\",\"messageType\":\"sensor_msgs/NavSatFix\","
                    + "\"type\":\"GPS\",\"deleted\":false}],\"upd\":2002,\"type\":\"FIXED_WING_AIRCRAFT\","
                    + "\"deleted\":false,\"url\":\"http://localhost/rv02\"}"
            },
            new Object[]{rv3,
                "{\"id\":\"3\",\"aoo\":\"["
                    + "{lat:37.80800,lng:-122.42400},{lat:37.80800,lng:-122.42500},{lat:37.80900,lng:-122.42500},"
                    + "{lat:37.80900,lng:-122.42400},{lat:37.80800,lng:-122.42400}]\","
                    + "\"name\":\"rv3\",\"sen\":["
                    + "{\"id\":\"3\",\"visibility\":\"ALL_VV\",\"lastUpdate\":30003,"
                    + "\"description\":\"Belly Mounted Camera 640x480\","
                    + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment=north\","
                    + "\"messageType\":\"sensor_msgs/Image\",\"type\":\"CAMERA\",\"deleted\":false},"
                    + "{\"id\":\"4\",\"visibility\":\"PRIVILEGED_VV\","
                    + "\"description\":\"GPS\",\"messageType\":\"sensor_msgs/NavSatFix\",\"type\":\"GPS\","
                    + "\"deleted\":false},"
                    + "{\"id\":\"1\",\"visibility\":\"ALL_VV\",\"lastUpdate\":10001,"
                    + "\"description\":\"Altimeter\",\"parameters\":\"random=10:35\","
                    + "\"messageType\":\"std_msgs/Float32\",\"type\":\"ALTIMETER\",\"deleted\":false}],\"upd\":3003,"
                    + "\"type\":\"GROUND_STATION\",\"deleted\":false,\"url\":\"http://localhost/rv03\"}"
            },

        };
    }

    @Test(dataProvider = "singleVehicleDataProvider")
    public void shouldConvertRealVehicles(RealVehicle vehicle, String expectedJsonString) throws JSONException
    {
        JSONObject result = converter.toJson(false, vehicle);
        JSONAssert.assertEquals(expectedJsonString, result.toString(true), false);
        JSONAssert.assertEquals(result.toString(true), expectedJsonString, false);
    }

    @DataProvider
    public Object[][] singleVehicleWithSensorIdsOnlyDataProvider()
    {
        return new Object[][]{
            new Object[]{rv1,
                "{\"id\":\"1\",\"aoo\":\"["
                    + "{lat:37.80800,lng:-122.42600},{lat:37.80800,lng:-122.42700},{lat:37.80900,lng:-122.42700},"
                    + "{lat:37.80900,lng:-122.42600},{lat:37.80800,lng:-122.42600}"
                    + "]\","
                    + "\"name\":\"rv1\","
                    + "\"sen\":[1,2,3],"
                    + "\"upd\":1001,"
                    + "\"type\":\"QUADROCOPTER\",\"deleted\":false,"
                    + "\"url\":\"http://localhost/rv01\"}"
            },
            new Object[]{rv2,
                "{\"id\":\"2\",\"aoo\":\"["
                    + "{lat:37.80800,lng:-122.42500},{lat:37.80800,lng:-122.42600},{lat:37.80900,lng:-122.42600},"
                    + "{lat:37.80900,lng:-122.42500},{lat:37.80800,lng:-122.42500}]\","
                    + "\"name\":\"rv2\",\"sen\":[2,3,4],\"upd\":2002,\"type\":\"FIXED_WING_AIRCRAFT\","
                    + "\"deleted\":false,\"url\":\"http://localhost/rv02\"}"
            },
            new Object[]{rv3,
                "{\"id\":\"3\",\"aoo\":\"["
                    + "{lat:37.80800,lng:-122.42400},{lat:37.80800,lng:-122.42500},{lat:37.80900,lng:-122.42500},"
                    + "{lat:37.80900,lng:-122.42400},{lat:37.80800,lng:-122.42400}]\","
                    + "\"name\":\"rv3\",\"sen\":[3,4,1],\"upd\":3003,"
                    + "\"type\":\"GROUND_STATION\",\"deleted\":false,\"url\":\"http://localhost/rv03\"}"
            },
            new Object[]{rv4,
                "{\"id\":\"4\",\"aoo\":\"["
                    + "{lat:34.80800,lng:-124.42400},{lat:34.80800,lng:-124.42500},{lat:34.80900,lng:-124.42500},"
                    + "{lat:34.80900,lng:-124.42400},{lat:34.80800,lng:-124.42400}]\","
                    + "\"name\":\"rv4\",\"sen\":[4],"
                    + "\"type\":\"TABLET\",\"deleted\":false,\"url\":\"http://localhost/rv04\"}"
            },
        };
    }

    @Test(dataProvider = "singleVehicleWithSensorIdsOnlyDataProvider")
    public void shouldConvertRealVehiclesWithSensorIdsOnly(RealVehicle vehicle, String expectedJsonString)
        throws JSONException
    {
        JSONObject result = converter.toJson(true, vehicle);
        JSONAssert.assertEquals(expectedJsonString, result.toString(true), false);
        JSONAssert.assertEquals(result.toString(true), expectedJsonString, false);
    }

    @DataProvider
    public Object[][] multiVehicleDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new RealVehicle[]{}, "[]"
            },
            new Object[]{
                new RealVehicle[]{rv1},
                "["
                    + "{\"id\":\"1\",\"aoo\":\"["
                    + "{lat:37.80800,lng:-122.42600},{lat:37.80800,lng:-122.42700},{lat:37.80900,lng:-122.42700},"
                    + "{lat:37.80900,lng:-122.42600},{lat:37.80800,lng:-122.42600}]\","
                    + "\"name\":\"rv1\","
                    + "\"sen\":["
                    + "{\"id\":\"1\",\"visibility\":\"ALL_VV\",\"lastUpdate\":10001,"
                    + "\"description\":\"Altimeter\",\"parameters\":\"random=10:35\","
                    + "\"messageType\":\"std_msgs/Float32\",\"type\":\"ALTIMETER\",\"deleted\":false},"
                    + "{\"id\":\"2\",\"visibility\":\"NO_VV\",\"lastUpdate\":20002,"
                    + "\"description\":\"Barometer\",\"parameters\":\"random=1050:1080\","
                    + "\"messageType\":\"std_msgs/Float32\",\"type\":\"BAROMETER\",\"deleted\":false},"
                    + "{\"id\":\"3\",\"visibility\":\"ALL_VV\",\"lastUpdate\":30003,"
                    + "\"description\":\"Belly Mounted Camera 640x480\","
                    + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment=north\","
                    + "\"messageType\":\"sensor_msgs/Image\",\"type\":\"CAMERA\",\"deleted\":false}"
                    + "],"
                    + "\"upd\":1001,"
                    + "\"type\":\"QUADROCOPTER\",\"deleted\":false,"
                    + "\"url\":\"http://localhost/rv01\""
                    + "}]"
            },
            new Object[]{
                new RealVehicle[]{rv1, rv2},
                "["
                    + "{\"id\":\"1\",\"aoo\":\"["
                    + "{lat:37.80800,lng:-122.42600},{lat:37.80800,lng:-122.42700},{lat:37.80900,lng:-122.42700},"
                    + "{lat:37.80900,lng:-122.42600},{lat:37.80800,lng:-122.42600}]\","
                    + "\"name\":\"rv1\","
                    + "\"sen\":["
                    + "{\"id\":\"1\",\"visibility\":\"ALL_VV\",\"lastUpdate\":10001,"
                    + "\"description\":\"Altimeter\",\"parameters\":\"random=10:35\","
                    + "\"messageType\":\"std_msgs/Float32\",\"type\":\"ALTIMETER\",\"deleted\":false},"
                    + "{\"id\":\"2\",\"visibility\":\"NO_VV\",\"lastUpdate\":20002,"
                    + "\"description\":\"Barometer\",\"parameters\":\"random=1050:1080\","
                    + "\"messageType\":\"std_msgs/Float32\",\"type\":\"BAROMETER\",\"deleted\":false},"
                    + "{\"id\":\"3\",\"visibility\":\"ALL_VV\",\"lastUpdate\":30003,"
                    + "\"description\":\"Belly Mounted Camera 640x480\","
                    + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment=north\","
                    + "\"messageType\":\"sensor_msgs/Image\",\"type\":\"CAMERA\",\"deleted\":false}"
                    + "],"
                    + "\"upd\":1001,\"type\":\"QUADROCOPTER\",\"deleted\":false,\"url\":\"http://localhost/rv01\"},"
                    + "{\"id\":\"2\",\"aoo\":\"["
                    + "{lat:37.80800,lng:-122.42500},{lat:37.80800,lng:-122.42600},{lat:37.80900,lng:-122.42600},"
                    + "{lat:37.80900,lng:-122.42500},{lat:37.80800,lng:-122.42500}]\","
                    + "\"name\":\"rv2\","
                    + "\"sen\":["
                    + "{\"id\":\"2\",\"visibility\":\"NO_VV\",\"lastUpdate\":20002,"
                    + "\"description\":\"Barometer\",\"parameters\":\"random=1050:1080\","
                    + "\"messageType\":\"std_msgs/Float32\",\"type\":\"BAROMETER\",\"deleted\":false},"
                    + "{\"id\":\"3\",\"visibility\":\"ALL_VV\",\"lastUpdate\":30003,"
                    + "\"description\":\"Belly Mounted Camera 640x480\","
                    + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment=north\","
                    + "\"messageType\":\"sensor_msgs/Image\",\"type\":\"CAMERA\",\"deleted\":false},"
                    + "{\"id\":\"4\",\"visibility\":\"PRIVILEGED_VV\","
                    + "\"description\":\"GPS\",\"messageType\":\"sensor_msgs/NavSatFix\","
                    + "\"type\":\"GPS\",\"deleted\":false}"
                    + "],\"upd\":2002,\"type\":\"FIXED_WING_AIRCRAFT\",\"deleted\":false,\"url\":\"http://localhost/rv02\"}]"
            },
            new Object[]{
                new RealVehicle[]{rv1, rv2, rv3},
                "["
                    + "{\"id\":\"1\",\"aoo\":\"["
                    + "{lat:37.80800,lng:-122.42600},{lat:37.80800,lng:-122.42700},{lat:37.80900,lng:-122.42700},"
                    + "{lat:37.80900,lng:-122.42600},{lat:37.80800,lng:-122.42600}]\","
                    + "\"name\":\"rv1\",\"sen\":["
                    + "{\"id\":\"1\",\"visibility\":\"ALL_VV\",\"lastUpdate\":10001,"
                    + "\"description\":\"Altimeter\",\"parameters\":\"random=10:35\","
                    + "\"messageType\":\"std_msgs/Float32\",\"type\":\"ALTIMETER\",\"deleted\":false},"
                    + "{\"id\":\"2\",\"visibility\":\"NO_VV\",\"lastUpdate\":20002,"
                    + "\"description\":\"Barometer\",\"parameters\":\"random=1050:1080\","
                    + "\"messageType\":\"std_msgs/Float32\",\"type\":\"BAROMETER\",\"deleted\":false},"
                    + "{\"id\":\"3\",\"visibility\":\"ALL_VV\",\"lastUpdate\":30003,"
                    + "\"description\":\"Belly Mounted Camera 640x480\","
                    + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment=north\","
                    + "\"messageType\":\"sensor_msgs/Image\",\"type\":\"CAMERA\",\"deleted\":false}],"
                    + "\"upd\":1001,\"type\":\"QUADROCOPTER\",\"deleted\":false,\"url\":\"http://localhost/rv01\"},"
                    + "{\"id\":\"2\",\"aoo\":\"["
                    + "{lat:37.80800,lng:-122.42500},{lat:37.80800,lng:-122.42600},{lat:37.80900,lng:-122.42600},"
                    + "{lat:37.80900,lng:-122.42500},{lat:37.80800,lng:-122.42500}]\","
                    + "\"name\":\"rv2\",\"sen\":["
                    + "{\"id\":\"2\",\"visibility\":\"NO_VV\",\"lastUpdate\":20002,"
                    + "\"description\":\"Barometer\",\"parameters\":\"random=1050:1080\","
                    + "\"messageType\":\"std_msgs/Float32\",\"type\":\"BAROMETER\",\"deleted\":false},"
                    + "{\"id\":\"3\",\"visibility\":\"ALL_VV\",\"lastUpdate\":30003,"
                    + "\"description\":\"Belly Mounted Camera 640x480\","
                    + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment=north\","
                    + "\"messageType\":\"sensor_msgs/Image\",\"type\":\"CAMERA\",\"deleted\":false},"
                    + "{\"id\":\"4\",\"visibility\":\"PRIVILEGED_VV\","
                    + "\"description\":\"GPS\",\"messageType\":\"sensor_msgs/NavSatFix\",\"type\":\"GPS\",\"deleted\":false}],"
                    + "\"upd\":2002,\"type\":\"FIXED_WING_AIRCRAFT\",\"deleted\":false,\"url\":\"http://localhost/rv02\"},"
                    + "{\"id\":\"3\",\"aoo\":\"["
                    + "{lat:37.80800,lng:-122.42400},{lat:37.80800,lng:-122.42500},{lat:37.80900,lng:-122.42500},"
                    + "{lat:37.80900,lng:-122.42400},{lat:37.80800,lng:-122.42400}]\","
                    + "\"name\":\"rv3\",\"sen\":["
                    + "{\"id\":\"3\",\"visibility\":\"ALL_VV\",\"lastUpdate\":30003,"
                    + "\"description\":\"Belly Mounted Camera 640x480\","
                    + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment=north\","
                    + "\"messageType\":\"sensor_msgs/Image\",\"type\":\"CAMERA\",\"deleted\":false},"
                    + "{\"id\":\"4\",\"visibility\":\"PRIVILEGED_VV\","
                    + "\"description\":\"GPS\",\"messageType\":\"sensor_msgs/NavSatFix\",\"type\":\"GPS\",\"deleted\":false},"
                    + "{\"id\":\"1\",\"visibility\":\"ALL_VV\",\"lastUpdate\":10001,"
                    + "\"description\":\"Altimeter\",\"parameters\":\"random=10:35\","
                    + "\"messageType\":\"std_msgs/Float32\",\"type\":\"ALTIMETER\",\"deleted\":false}],"
                    + "\"upd\":3003,\"type\":\"GROUND_STATION\",\"deleted\":false,\"url\":\"http://localhost/rv03\"}]"
            },
        };
    }

    @Test(dataProvider = "multiVehicleDataProvider")
    public void shouldConvertRealVehicleArrays(RealVehicle[] vehicles, String expectedJsonString)
        throws JSONException
    {
        JSONArray result = converter.toJsonArray(false, vehicles);
        String actual = result.toString(true);

        JSONAssert.assertEquals(expectedJsonString, actual, false);
        JSONAssert.assertEquals(actual, expectedJsonString, false);
    }

    @DataProvider
    public Object[][] polarCoordinateDataProvicer()
    {
        return new Object[][]{
            new Object[]{new PolarCoordinate(1, 2, 3)
                , "{\"lat\":\"1.00000000\",\"lon\":\"2.00000000\",\"alt\":\"3.000\"}"
            },
            new Object[]{new PolarCoordinate(2, 3, 4)
                , "{\"lat\":\"2.00000000\",\"lon\":\"3.00000000\",\"alt\":\"4.000\"}"
            },
            new Object[]{new PolarCoordinate(2.002, 3.002, 4.002)
                , "{\"lat\":\"2.00200000\",\"lon\":\"3.00200000\",\"alt\":\"4.002\"}"
            },
            new Object[]{new PolarCoordinate(2.00200003, 3.00200002, 4.009)
                , "{\"lat\":\"2.00200003\",\"lon\":\"3.00200002\",\"alt\":\"4.009\"}"
            },
        };
    }

    @Test(dataProvider = "polarCoordinateDataProvicer")
    public void shouldConvertPolarCoordinates(PolarCoordinate data, String expected) throws JSONException
    {
        JSONObject actual = converter.toJson(data);

        JSONAssert.assertEquals(expected, actual.toCompactString(), false);
        JSONAssert.assertEquals(actual.toCompactString(), expected, false);
    }

    @DataProvider
    public Object[][] integerArrayDataProvicer()
    {
        return new Object[][]{
            new Object[]{Arrays.asList(new Integer[]{}), "[]"},
            new Object[]{Arrays.asList(new Integer[]{1}), "[1]"},
            new Object[]{Arrays.asList(new Integer[]{1, 2}), "[1,2]"},
            new Object[]{Arrays.asList(new Integer[]{1, 2, 3}), "[1,2,3]"},
            new Object[]{Arrays.asList(new Integer[]{1, 2, 3, 4}), "[1,2,3,4]"},
        };
    }

    @Test(dataProvider = "integerArrayDataProvicer")
    public void shouldConvertIntegerArrays(List<Integer> data, String expected) throws JSONException
    {
        JSONArray actual = converter.toJsonArray(data.toArray(new Integer[data.size()]));

        JSONAssert.assertEquals(expected, actual.toCompactString(), false);
        JSONAssert.assertEquals(actual.toCompactString(), expected, false);
    }

    @DataProvider
    public Object[][] doubleArrayDataProvicer()
    {
        return new Object[][]{
            new Object[]{Arrays.asList(new Double[]{}), "[]"},
            new Object[]{Arrays.asList(new Double[]{1.1}), "[1.1]"},
            new Object[]{Arrays.asList(new Double[]{1.2, 2.3}), "[1.2,2.3]"},
            new Object[]{Arrays.asList(new Double[]{1.4, 2.5, 3.6}), "[1.4,2.5,3.6]"},
            new Object[]{Arrays.asList(new Double[]{1.7, 2.8, 3.9, 4.01}), "[1.7,2.8,3.9,4.01]"},
        };
    }

    @Test(dataProvider = "doubleArrayDataProvicer")
    public void shouldConvertDoubleArrays(List<Double> data, String expected) throws JSONException
    {
        JSONArray actual = converter.toJsonArray(data.toArray(new Double[data.size()]));

        JSONAssert.assertEquals(expected, actual.toCompactString(), false);
        JSONAssert.assertEquals(actual.toCompactString(), expected, false);
    }

    @SuppressWarnings("unchecked")
    private RealVehicle setupRealVehicle(Object... data)
    {
        RealVehicle realVehicle = new RealVehicle();
        realVehicle.setAreaOfOperation((String) data[0]);
        realVehicle.setDeleted((Boolean) data[1]);
        realVehicle.setId((Integer) data[2]);
        realVehicle.setLastUpdate((Date) data[3]);
        realVehicle.setName((String) data[4]);
        realVehicle.setSensors((List<SensorDefinition>) data[5]);
        realVehicle.setType((RealVehicleType) data[6]);
        realVehicle.setUrl((String) data[7]);
        return realVehicle;
    }

    @DataProvider
    public Object[][] newRealVehicleDataProvicer()
    {
        CoreJsonConverter conv = new CoreJsonConverterImpl();

        SensorDefinition sensor1 = mock(SensorDefinition.class);
        when(sensor1.getId()).thenReturn(1);
        when(sensor1.getDescription()).thenReturn("Altimeter");
        when(sensor1.getType()).thenReturn(SensorType.ALTIMETER);
        when(sensor1.getMessageType()).thenReturn("std_msgs/Float32");
        when(sensor1.getVisibility()).thenReturn(SensorVisibility.ALL_VV);
        when(sensor1.getParameters()).thenReturn("random=10:35");
        when(sensor1.getLastUpdate()).thenReturn(new Date(10001));

        RealVehicle rvA = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01"
            , Arrays.asList(sensor1), RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");
        String rvAJsonString = conv.toJson(true, rvA).toCompactString();

        RealVehicle rvB = setupRealVehicle("abc", false, 10, new Date(123456788), "rv01"
            , Arrays.asList(new SensorDefinition[0]), RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");
        String rvBJsonString = conv.toJson(true, rvB).toCompactString();

        RealVehicle rvC = setupRealVehicle("abcd", true, 10, new Date(123456791), "rv01c"
            , Arrays.asList(sensor1), RealVehicleType.TABLET, "http://localhost:8081/rv01");
        String rvCJsonString = conv.toJson(true, rvC).toCompactString();

        String rvDJsonString = rvAJsonString.replace(",\"deleted\":false", "").replace("123456789", "123456792");
        String rvEJsonString = rvAJsonString.replace("123456789", "123456792");

        return new Object[][]{
            new Object[]{sensor1, 0, rvAJsonString, rvAJsonString},
            new Object[]{sensor1, -1, rvBJsonString, rvAJsonString},
            new Object[]{sensor1, 1, rvCJsonString, rvCJsonString},
            new Object[]{sensor1, 1, rvDJsonString, rvEJsonString},
        };
    }

    @Test(dataProvider = "newRealVehicleDataProvicer")
    public void shouldFillInNewerRealVehicleFromJsonObject(SensorDefinition sensor, int cmp, String data
        , String expected) throws JSONException
    {
        RealVehicle vehicle = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01"
            , Arrays.asList(sensor), RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        JSONObject obj = new JSONObject(data);

        int actualCmp = converter.fillInNewerRealVehicleFromJsonObject(vehicle, obj);
        assertThat(actualCmp).isEqualTo(cmp);

        String actual = converter.toJson(true, vehicle).toCompactString();

        JSONAssert.assertEquals(expected, actual, false);
        JSONAssert.assertEquals(actual, expected, false);
    }

    private SensorDefinition setupSensorDefinition(Boolean deleted, String description, Integer id, Date lastUpdate
        , String messageType, String parameters, SensorType sensorType, SensorVisibility sensorVisibility)
    {
        SensorDefinition sd = new SensorDefinition();
        sd.setDeleted(deleted);
        sd.setDescription(description);
        sd.setId(id);
        sd.setLastUpdate(lastUpdate);
        sd.setMessageType(messageType);
        sd.setParameters(parameters);
        sd.setType(sensorType);
        sd.setVisibility(sensorVisibility);
        return sd;
    }

    @DataProvider
    public Object[][] newSensorDefinitionDataProvicer()
    {
        CoreJsonConverter conv = new CoreJsonConverterImpl();

        SensorDefinition sdA = setupSensorDefinition(false, "sensor1", 10, new Date(123456789), "msgs_type/t01"
            , "abc", SensorType.ALTIMETER, SensorVisibility.NO_VV);
        String sdAJsonString = conv.toJson(sdA).toCompactString();

        SensorDefinition sdB = setupSensorDefinition(false, "sensor1", 10, new Date(123456788), "msgs_type/t01"
            , "abc", SensorType.ALTIMETER, SensorVisibility.NO_VV);
        String sdBJsonString = conv.toJson(sdB).toCompactString();

        SensorDefinition sdC = setupSensorDefinition(false, "sensor1", 10, new Date(123456790), "msgs_type/t01"
            , "abc", SensorType.ALTIMETER, SensorVisibility.NO_VV);
        String sdCJsonString = conv.toJson(sdC).toCompactString();

        String sdDJsonString = sdAJsonString.replace(",\"deleted\":false", "").replace("123456789", "123456792");
        String sdEJsonString = sdAJsonString.replace("123456789", "123456792");

        String sdFJsonString = sdAJsonString.replace(",\"parameters\":\"abc\"", "").replace("123456789", "123456792");
        //        String sdGJsonString = sdAJsonString.replace("123456789", "123456792");

        return new Object[][]{
            new Object[]{0, sdAJsonString, sdAJsonString},
            new Object[]{-1, sdBJsonString, sdAJsonString},
            new Object[]{1, sdCJsonString, sdCJsonString},
            new Object[]{1, sdDJsonString, sdEJsonString},
            new Object[]{1, sdFJsonString, sdFJsonString},
        };
    }

    @Test(dataProvider = "newSensorDefinitionDataProvicer")
    public void shouldFillInNewerSensorDefinitionFromJsonObject(int cmp, String data, String expected)
        throws JSONException
    {
        SensorDefinition sdTest = setupSensorDefinition(false, "sensor1", 10, new Date(123456789), "msgs_type/t01"
            , "abc", SensorType.ALTIMETER, SensorVisibility.NO_VV);

        JSONObject obj = new JSONObject(data);

        int actualCmp = converter.fillInNewerSensorDefinitionFromJsonObject(sdTest, obj);
        assertThat(actualCmp).isEqualTo(cmp);

        String actual = converter.toJson(sdTest).toCompactString();

        JSONAssert.assertEquals(expected, actual, false);
        JSONAssert.assertEquals(actual, expected, false);
    }
}
