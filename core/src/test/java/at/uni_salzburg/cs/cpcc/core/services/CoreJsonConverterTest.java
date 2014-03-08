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
package at.uni_salzburg.cs.cpcc.core.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicleType;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.entities.SensorType;
import at.uni_salzburg.cs.cpcc.core.entities.SensorVisibility;

public class CoreJsonConverterTest
{
    RealVehicle rv1 = mock(RealVehicle.class);
    RealVehicle rv2 = mock(RealVehicle.class);
    RealVehicle rv3 = mock(RealVehicle.class);
    SensorDefinition s1 = mock(SensorDefinition.class);
    SensorDefinition s2 = mock(SensorDefinition.class);
    SensorDefinition s3 = mock(SensorDefinition.class);
    SensorDefinition s4 = mock(SensorDefinition.class);

    CoreJsonConverter converter;

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
        when(s4.getLastUpdate()).thenReturn(new Date(40004));

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
                    + "{\"id\":\"4\",\"visibility\":\"PRIVILEGED_VV\",\"lastUpdate\":40004,"
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
                    + "{\"id\":\"4\",\"visibility\":\"PRIVILEGED_VV\",\"lastUpdate\":40004,"
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
    public void shouldConvertRealVehicles(RealVehicle vehicle, String expectedJsonString)
    {
        JSONObject result = converter.toJson(false, vehicle);
        assertThat(result.toString(true)).isNotNull().isEqualTo(expectedJsonString);
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
                    + "{\"id\":\"4\",\"visibility\":\"PRIVILEGED_VV\",\"lastUpdate\":40004,"
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
                    + "{\"id\":\"4\",\"visibility\":\"PRIVILEGED_VV\",\"lastUpdate\":40004,"
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
                    + "{\"id\":\"4\",\"visibility\":\"PRIVILEGED_VV\",\"lastUpdate\":40004,"
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
    {
        JSONArray result = converter.toJsonArray(false, vehicles);
        assertThat(result.toString(true)).isNotNull().isEqualTo(expectedJsonString);
    }
}
