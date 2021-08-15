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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;
import cpcc.core.entities.RealVehicleType;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;
import cpcc.core.entities.SensorVisibility;

public class CoreJsonConverterTest
{
    private static final String RV1_AREA_OF_OPERATION = "["
        + "{lat:37.80800,lng:-122.42600},{lat:37.80800,lng:-122.42700},{lat:37.80900,lng:-122.42700},"
        + "{lat:37.80900,lng:-122.42600},{lat:37.80800,lng:-122.42600}]";
    private static final String RV2_AREA_OF_OPERATION = "["
        + "{lat:37.80800,lng:-122.42500},{lat:37.80800,lng:-122.42600},{lat:37.80900,lng:-122.42600},"
        + "{lat:37.80900,lng:-122.42500},{lat:37.80800,lng:-122.42500}]";
    private static final String RV3_AREA_OF_OPERATION = "["
        + "{lat:37.80800,lng:-122.42400},{lat:37.80800,lng:-122.42500},{lat:37.80900,lng:-122.42500},"
        + "{lat:37.80900,lng:-122.42400},{lat:37.80800,lng:-122.42400}]";
    private static final String RV4_AREA_OF_OPERATION = "["
        + "{lat:34.80800,lng:-124.42400},{lat:34.80800,lng:-124.42500},{lat:34.80900,lng:-124.42500},"
        + "{lat:34.80900,lng:-124.42400},{lat:34.80800,lng:-124.42400}]";

    private CoreJsonConverter sut;
    private static SensorDefinition s1 = mock(SensorDefinition.class);
    private static SensorDefinition s2 = mock(SensorDefinition.class);
    private static SensorDefinition s3 = mock(SensorDefinition.class);
    private static SensorDefinition s4 = mock(SensorDefinition.class);

    @BeforeEach
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

        sut = new CoreJsonConverterImpl();
    }

    static Stream<Arguments> singleVehicleDataProvider()
    {
        RealVehicle rv1 = mock(RealVehicle.class);
        RealVehicle rv2 = mock(RealVehicle.class);
        RealVehicle rv3 = mock(RealVehicle.class);

        when(rv1.getName()).thenReturn("rv1");
        when(rv1.getUrl()).thenReturn("http://localhost/rv01");
        when(rv1.getSensors()).thenReturn(Arrays.asList(s1, s2, s3));
        when(rv1.getAreaOfOperation()).thenReturn(RV1_AREA_OF_OPERATION);
        when(rv1.getType()).thenReturn(RealVehicleType.QUADROCOPTER);
        when(rv1.getId()).thenReturn(1);
        when(rv1.getLastUpdate()).thenReturn(new Date(1001));

        when(rv2.getName()).thenReturn("rv2");
        when(rv2.getUrl()).thenReturn("http://localhost/rv02");
        when(rv2.getSensors()).thenReturn(Arrays.asList(s2, s3, s4));
        when(rv2.getAreaOfOperation()).thenReturn(RV2_AREA_OF_OPERATION);
        when(rv2.getType()).thenReturn(RealVehicleType.FIXED_WING_AIRCRAFT);
        when(rv2.getId()).thenReturn(2);
        when(rv2.getLastUpdate()).thenReturn(new Date(2002));

        when(rv3.getName()).thenReturn("rv3");
        when(rv3.getUrl()).thenReturn("http://localhost/rv03");
        when(rv3.getSensors()).thenReturn(Arrays.asList(s3, s4, s1));
        when(rv3.getAreaOfOperation()).thenReturn(RV3_AREA_OF_OPERATION);
        when(rv3.getType()).thenReturn(RealVehicleType.GROUND_STATION);
        when(rv3.getId()).thenReturn(3);
        when(rv3.getLastUpdate()).thenReturn(new Date(3003));

        return Stream.of(
            arguments(rv1,
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
                    + "\"url\":\"http://localhost/rv01\"}"),
            arguments(rv2,
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
                    + "\"deleted\":false,\"url\":\"http://localhost/rv02\"}"),
            arguments(rv3,
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
                    + "\"type\":\"GROUND_STATION\",\"deleted\":false,\"url\":\"http://localhost/rv03\"}"));
    }

    @ParameterizedTest
    @MethodSource("singleVehicleDataProvider")
    public void shouldConvertRealVehicles(RealVehicle vehicle, String expectedJsonString) throws JSONException
    {
        JSONObject result = sut.toJson(false, vehicle);
        JSONAssert.assertEquals(expectedJsonString, result.toString(true), false);
        JSONAssert.assertEquals(result.toString(true), expectedJsonString, false);
    }

    static Stream<Arguments> singleVehicleWithSensorIdsOnlyDataProvider()
    {
        RealVehicle rv1 = mock(RealVehicle.class);
        RealVehicle rv2 = mock(RealVehicle.class);
        RealVehicle rv3 = mock(RealVehicle.class);
        RealVehicle rv4 = mock(RealVehicle.class);

        when(rv1.getName()).thenReturn("rv1");
        when(rv1.getUrl()).thenReturn("http://localhost/rv01");
        when(rv1.getSensors()).thenReturn(Arrays.asList(s1, s2, s3));
        when(rv1.getAreaOfOperation()).thenReturn(RV1_AREA_OF_OPERATION);
        when(rv1.getType()).thenReturn(RealVehicleType.QUADROCOPTER);
        when(rv1.getId()).thenReturn(1);
        when(rv1.getLastUpdate()).thenReturn(new Date(1001));

        when(rv2.getName()).thenReturn("rv2");
        when(rv2.getUrl()).thenReturn("http://localhost/rv02");
        when(rv2.getSensors()).thenReturn(Arrays.asList(s2, s3, s4));
        when(rv2.getAreaOfOperation()).thenReturn(RV2_AREA_OF_OPERATION);
        when(rv2.getType()).thenReturn(RealVehicleType.FIXED_WING_AIRCRAFT);
        when(rv2.getId()).thenReturn(2);
        when(rv2.getLastUpdate()).thenReturn(new Date(2002));

        when(rv3.getName()).thenReturn("rv3");
        when(rv3.getUrl()).thenReturn("http://localhost/rv03");
        when(rv3.getSensors()).thenReturn(Arrays.asList(s3, s4, s1));
        when(rv3.getAreaOfOperation()).thenReturn(RV3_AREA_OF_OPERATION);
        when(rv3.getType()).thenReturn(RealVehicleType.GROUND_STATION);
        when(rv3.getId()).thenReturn(3);
        when(rv3.getLastUpdate()).thenReturn(new Date(3003));

        when(rv4.getName()).thenReturn("rv4");
        when(rv4.getUrl()).thenReturn("http://localhost/rv04");
        when(rv4.getSensors()).thenReturn(Arrays.asList(s4));
        when(rv4.getAreaOfOperation()).thenReturn(RV4_AREA_OF_OPERATION);
        when(rv4.getType()).thenReturn(RealVehicleType.TABLET);
        when(rv4.getId()).thenReturn(4);

        return Stream.of(
            arguments(rv1,
                "{\"id\":\"1\",\"aoo\":\"["
                    + "{lat:37.80800,lng:-122.42600},{lat:37.80800,lng:-122.42700},{lat:37.80900,lng:-122.42700},"
                    + "{lat:37.80900,lng:-122.42600},{lat:37.80800,lng:-122.42600}"
                    + "]\","
                    + "\"name\":\"rv1\","
                    + "\"sen\":[1,2,3],"
                    + "\"upd\":1001,"
                    + "\"type\":\"QUADROCOPTER\",\"deleted\":false,"
                    + "\"url\":\"http://localhost/rv01\"}"),
            arguments(rv2,
                "{\"id\":\"2\",\"aoo\":\"["
                    + "{lat:37.80800,lng:-122.42500},{lat:37.80800,lng:-122.42600},{lat:37.80900,lng:-122.42600},"
                    + "{lat:37.80900,lng:-122.42500},{lat:37.80800,lng:-122.42500}]\","
                    + "\"name\":\"rv2\",\"sen\":[2,3,4],\"upd\":2002,\"type\":\"FIXED_WING_AIRCRAFT\","
                    + "\"deleted\":false,\"url\":\"http://localhost/rv02\"}"),
            arguments(rv3,
                "{\"id\":\"3\",\"aoo\":\"["
                    + "{lat:37.80800,lng:-122.42400},{lat:37.80800,lng:-122.42500},{lat:37.80900,lng:-122.42500},"
                    + "{lat:37.80900,lng:-122.42400},{lat:37.80800,lng:-122.42400}]\","
                    + "\"name\":\"rv3\",\"sen\":[3,4,1],\"upd\":3003,"
                    + "\"type\":\"GROUND_STATION\",\"deleted\":false,\"url\":\"http://localhost/rv03\"}"),
            arguments(rv4,
                "{\"id\":\"4\",\"aoo\":\"["
                    + "{lat:34.80800,lng:-124.42400},{lat:34.80800,lng:-124.42500},{lat:34.80900,lng:-124.42500},"
                    + "{lat:34.80900,lng:-124.42400},{lat:34.80800,lng:-124.42400}]\","
                    + "\"name\":\"rv4\",\"sen\":[4],"
                    + "\"type\":\"TABLET\",\"deleted\":false,\"url\":\"http://localhost/rv04\"}"));
    }

    @ParameterizedTest
    @MethodSource("singleVehicleWithSensorIdsOnlyDataProvider")
    public void shouldConvertRealVehiclesWithSensorIdsOnly(RealVehicle vehicle, String expectedJsonString)
        throws JSONException
    {
        JSONObject result = sut.toJson(true, vehicle);
        JSONAssert.assertEquals(expectedJsonString, result.toString(true), false);
        JSONAssert.assertEquals(result.toString(true), expectedJsonString, false);
    }

    static Stream<Arguments> multiVehicleDataProvider()
    {
        RealVehicle rv1 = mock(RealVehicle.class);
        RealVehicle rv2 = mock(RealVehicle.class);
        RealVehicle rv3 = mock(RealVehicle.class);

        when(rv1.getName()).thenReturn("rv1");
        when(rv1.getUrl()).thenReturn("http://localhost/rv01");
        when(rv1.getSensors()).thenReturn(Arrays.asList(s1, s2, s3));
        when(rv1.getAreaOfOperation()).thenReturn(RV1_AREA_OF_OPERATION);
        when(rv1.getType()).thenReturn(RealVehicleType.QUADROCOPTER);
        when(rv1.getId()).thenReturn(1);
        when(rv1.getLastUpdate()).thenReturn(new Date(1001));

        when(rv2.getName()).thenReturn("rv2");
        when(rv2.getUrl()).thenReturn("http://localhost/rv02");
        when(rv2.getSensors()).thenReturn(Arrays.asList(s2, s3, s4));
        when(rv2.getAreaOfOperation()).thenReturn(RV2_AREA_OF_OPERATION);
        when(rv2.getType()).thenReturn(RealVehicleType.FIXED_WING_AIRCRAFT);
        when(rv2.getId()).thenReturn(2);
        when(rv2.getLastUpdate()).thenReturn(new Date(2002));

        when(rv3.getName()).thenReturn("rv3");
        when(rv3.getUrl()).thenReturn("http://localhost/rv03");
        when(rv3.getSensors()).thenReturn(Arrays.asList(s3, s4, s1));
        when(rv3.getAreaOfOperation()).thenReturn(RV3_AREA_OF_OPERATION);
        when(rv3.getType()).thenReturn(RealVehicleType.GROUND_STATION);
        when(rv3.getId()).thenReturn(3);
        when(rv3.getLastUpdate()).thenReturn(new Date(3003));

        return Stream.of(
            arguments(
                new RealVehicle[]{}, "[]"),
            arguments(
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
                    + "}]"),
            arguments(
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
                    + "],\"upd\":2002,\"type\":\"FIXED_WING_AIRCRAFT\",\"deleted\":false,\"url\":\"http://localhost/rv02\"}]"),
            arguments(
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
                    + "\"upd\":3003,\"type\":\"GROUND_STATION\",\"deleted\":false,\"url\":\"http://localhost/rv03\"}]"));
    }

    @ParameterizedTest
    @MethodSource("multiVehicleDataProvider")
    public void shouldConvertRealVehicleArrays(RealVehicle[] vehicles, String expectedJsonString)
        throws JSONException
    {
        JSONArray result = sut.toJsonArray(false, vehicles);
        String actual = result.toString(true);

        JSONAssert.assertEquals(expectedJsonString, actual, false);
        JSONAssert.assertEquals(actual, expectedJsonString, false);
    }

    static Stream<Arguments> polarCoordinateDataProvicer()
    {
        return Stream.of(
            arguments(new PolarCoordinate(1, 2, 3),
                "{\"lat\":\"1.00000000\",\"lon\":\"2.00000000\",\"alt\":\"3.000\"}"),
            arguments(new PolarCoordinate(2, 3, 4),
                "{\"lat\":\"2.00000000\",\"lon\":\"3.00000000\",\"alt\":\"4.000\"}"),
            arguments(new PolarCoordinate(2.002, 3.002, 4.002),
                "{\"lat\":\"2.00200000\",\"lon\":\"3.00200000\",\"alt\":\"4.002\"}"),
            arguments(new PolarCoordinate(2.00200003, 3.00200002, 4.009),
                "{\"lat\":\"2.00200003\",\"lon\":\"3.00200002\",\"alt\":\"4.009\"}"));
    }

    @ParameterizedTest
    @MethodSource("polarCoordinateDataProvicer")
    public void shouldConvertPolarCoordinates(PolarCoordinate data, String expected) throws JSONException
    {
        JSONObject actual = sut.toJson(data);

        JSONAssert.assertEquals(expected, actual.toCompactString(), false);
        JSONAssert.assertEquals(actual.toCompactString(), expected, false);
    }

    static Stream<Arguments> integerArrayDataProvicer()
    {
        return Stream.of(
            arguments(Arrays.asList(new Integer[]{}), "[]"),
            arguments(Arrays.asList(new Integer[]{1}), "[1]"),
            arguments(Arrays.asList(new Integer[]{1, 2}), "[1,2]"),
            arguments(Arrays.asList(new Integer[]{1, 2, 3}), "[1,2,3]"),
            arguments(Arrays.asList(new Integer[]{1, 2, 3, 4}), "[1,2,3,4]"));
    }

    @ParameterizedTest
    @MethodSource("integerArrayDataProvicer")
    public void shouldConvertIntegerArrays(List<Integer> data, String expected) throws JSONException
    {
        JSONArray actual = sut.toJsonArray(data.toArray(new Integer[data.size()]));

        JSONAssert.assertEquals(expected, actual.toCompactString(), false);
        JSONAssert.assertEquals(actual.toCompactString(), expected, false);
    }

    static Stream<Arguments> doubleArrayDataProvicer()
    {
        return Stream.of(
            arguments(Arrays.asList(new Double[]{}), "[]"),
            arguments(Arrays.asList(new Double[]{1.1}), "[1.1]"),
            arguments(Arrays.asList(new Double[]{1.2, 2.3}), "[1.2,2.3]"),
            arguments(Arrays.asList(new Double[]{1.4, 2.5, 3.6}), "[1.4,2.5,3.6]"),
            arguments(Arrays.asList(new Double[]{1.7, 2.8, 3.9, 4.01}), "[1.7,2.8,3.9,4.01]"));
    }

    @ParameterizedTest
    @MethodSource("doubleArrayDataProvicer")
    public void shouldConvertDoubleArrays(List<Double> data, String expected) throws JSONException
    {
        JSONArray actual = sut.toJsonArray(data.toArray(new Double[data.size()]));

        JSONAssert.assertEquals(expected, actual.toCompactString(), false);
        JSONAssert.assertEquals(actual.toCompactString(), expected, false);
    }

    @SuppressWarnings("unchecked")
    static RealVehicle setupRealVehicle(Object... data)
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

    static Stream<Arguments> newRealVehicleDataProvicer()
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

        RealVehicle rvA = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sensor1),
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");
        String rvAJsonString = conv.toJson(true, rvA).toCompactString();

        RealVehicle rvB = setupRealVehicle("abc", false, 10, new Date(123456788), "rv01",
            Arrays.asList(new SensorDefinition[0]), RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");
        String rvBJsonString = conv.toJson(true, rvB).toCompactString();

        RealVehicle rvC = setupRealVehicle("abcd", true, 10, new Date(123456791), "rv01c", Arrays.asList(sensor1),
            RealVehicleType.TABLET, "http://localhost:8081/rv01");
        String rvCJsonString = conv.toJson(true, rvC).toCompactString();

        String rvDJsonString = rvAJsonString.replace(",\"deleted\":false", "").replace("123456789", "123456792");
        String rvEJsonString = rvAJsonString.replace("123456789", "123456792");

        return Stream.of(
            arguments(sensor1, 0, rvAJsonString, rvAJsonString),
            arguments(sensor1, -1, rvBJsonString, rvAJsonString),
            arguments(sensor1, 1, rvCJsonString, rvCJsonString),
            arguments(sensor1, 1, rvDJsonString, rvEJsonString));
    }

    @ParameterizedTest
    @MethodSource("newRealVehicleDataProvicer")
    public void shouldFillInNewerRealVehicleFromJsonObject(SensorDefinition sensor, int cmp, String data,
        String expected) throws JSONException
    {
        RealVehicle vehicle = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sensor),
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        JSONObject obj = new JSONObject(data);

        int actualCmp = sut.fillInNewerRealVehicleFromJsonObject(vehicle, obj);
        assertThat(actualCmp).isEqualTo(cmp);

        String actual = sut.toJson(true, vehicle).toCompactString();

        JSONAssert.assertEquals(expected, actual, false);
        JSONAssert.assertEquals(actual, expected, false);
    }

    static SensorDefinition setupSensorDefinition(Boolean deleted, String description, Integer id, Date lastUpdate,
        String messageType, String parameters, SensorType sensorType, SensorVisibility sensorVisibility)
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

    static Stream<Arguments> newSensorDefinitionDataProvicer()
    {
        CoreJsonConverter conv = new CoreJsonConverterImpl();

        SensorDefinition sdA = setupSensorDefinition(false, "sensor1", 10, new Date(123456789), "msgs_type/t01", "abc",
            SensorType.ALTIMETER, SensorVisibility.NO_VV);
        String sdAJsonString = conv.toJson(sdA).toCompactString();

        SensorDefinition sdB = setupSensorDefinition(false, "sensor1", 10, new Date(123456788), "msgs_type/t01", "abc",
            SensorType.ALTIMETER, SensorVisibility.NO_VV);
        String sdBJsonString = conv.toJson(sdB).toCompactString();

        SensorDefinition sdC = setupSensorDefinition(false, "sensor1", 10, new Date(123456790), "msgs_type/t01", "abc",
            SensorType.ALTIMETER, SensorVisibility.NO_VV);
        String sdCJsonString = conv.toJson(sdC).toCompactString();

        String sdDJsonString = sdAJsonString.replace(",\"deleted\":false", "").replace("123456789", "123456792");
        String sdEJsonString = sdAJsonString.replace("123456789", "123456792");

        String sdFJsonString = sdAJsonString.replace(",\"parameters\":\"abc\"", "").replace("123456789", "123456792");
        //        String sdGJsonString = sdAJsonString.replace("123456789", "123456792");

        return Stream.of(
            arguments(0, sdAJsonString, sdAJsonString),
            arguments(-1, sdBJsonString, sdAJsonString),
            arguments(1, sdCJsonString, sdCJsonString),
            arguments(1, sdDJsonString, sdEJsonString),
            arguments(1, sdFJsonString, sdFJsonString));
    }

    @ParameterizedTest
    @MethodSource("newSensorDefinitionDataProvicer")
    public void shouldFillInNewerSensorDefinitionFromJsonObject(int cmp, String data, String expected)
        throws JSONException
    {
        SensorDefinition sdTest = setupSensorDefinition(false, "sensor1", 10, new Date(123456789), "msgs_type/t01",
            "abc", SensorType.ALTIMETER, SensorVisibility.NO_VV);

        JSONObject obj = new JSONObject(data);

        int actualCmp = sut.fillInNewerSensorDefinitionFromJsonObject(sdTest, obj);
        assertThat(actualCmp).isEqualTo(cmp);

        String actual = sut.toJson(sdTest).toCompactString();

        JSONAssert.assertEquals(expected, actual, false);
        JSONAssert.assertEquals(actual, expected, false);
    }

    static Stream<Arguments> regionsDataProvicer()
    {
        RealVehicle rv1 = mock(RealVehicle.class);
        RealVehicle rv2 = mock(RealVehicle.class);

        when(rv1.getName()).thenReturn("rv1");
        when(rv1.getUrl()).thenReturn("http://localhost/rv01");
        when(rv1.getSensors()).thenReturn(Arrays.asList(s1, s2, s3));
        when(rv1.getAreaOfOperation()).thenReturn(RV1_AREA_OF_OPERATION);
        when(rv1.getType()).thenReturn(RealVehicleType.QUADROCOPTER);
        when(rv1.getId()).thenReturn(1);
        when(rv1.getLastUpdate()).thenReturn(new Date(1001));

        when(rv2.getName()).thenReturn("rv2");
        when(rv2.getUrl()).thenReturn("http://localhost/rv02");
        when(rv2.getSensors()).thenReturn(Arrays.asList(s2, s3, s4));
        when(rv2.getAreaOfOperation()).thenReturn(RV2_AREA_OF_OPERATION);
        when(rv2.getType()).thenReturn(RealVehicleType.FIXED_WING_AIRCRAFT);
        when(rv2.getId()).thenReturn(2);
        when(rv2.getLastUpdate()).thenReturn(new Date(2002));

        return Stream.of(
            arguments(Arrays.asList(rv1), "{\"rv1\":" + RV1_AREA_OF_OPERATION + "}"),
            arguments(Arrays.asList(rv2), "{\"rv2\":" + RV2_AREA_OF_OPERATION + "}"),
            arguments(Arrays.asList(rv1, rv2),
                "{\"rv1\":" + RV1_AREA_OF_OPERATION + ",\"rv2\":" + RV2_AREA_OF_OPERATION + "}"));
    }

    @ParameterizedTest
    @MethodSource("regionsDataProvicer")
    public void shouldConvertToRegionJson(List<RealVehicle> rvList, String expected)
    {
        String actual = sut.toRegionJson(rvList);

        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> statesDataProvicer()
    {
        RealVehicleState rvs1 = mock(RealVehicleState.class);
        when(rvs1.getId()).thenReturn(10101);
        when(rvs1.getRealVehicleName()).thenReturn("RV01");
        when(rvs1.getLastUpdate()).thenReturn(new Date(1010101L));
        when(rvs1.toString()).thenReturn("RV01");
        when(rvs1.getState()).thenReturn(
            "{\"type\":\"FeatureCollection\",\"features\":["
                + "{\"type\":\"Feature\",\"properties\":{"
                + "\"rvPosition\":{\"coordinates\":[-122.42649999999999,37.808499999754275,9.999978839419782]},"
                + "\"rvType\":\"QUADROCOPTER\",\"rvName\":\"RV01\",\"rvState\":\"idle\",\"rvHeading\":0,\"rvId\":1,"
                + "\"type\":\"rvPosition\",\"rvTime\":1453047531086},\"geometry\":"
                + "{\"type\":\"Point\",\"coordinates\":[-122.42649999999999,37.808499999754275,9.999978839419782]}},"
                + "{\"type\":\"Feature\",\"properties\":{\"type\":\"vvs\"},\"geometry\":"
                + "{\"type\":\"GeometryCollection\",\"geometries\":[]}},"
                + "{\"type\":\"Feature\",\"properties\":"
                + "{\"type\":\"rvPath\"},\"geometry\":{\"type\":\"LineString\","
                + "\"coordinates\":[[-122.42649999999999,37.808499999754275,9.999978839419782]]}},"
                + "{\"type\":\"Feature\",\"properties\":"
                + "{\"type\":\"sensors\"},\"geometry\":{\"type\":\"GeometryCollection\",\"geometries\":[]}}]}");

        RealVehicleState rvs2 = mock(RealVehicleState.class);
        when(rvs2.getId()).thenReturn(20202);
        when(rvs2.getRealVehicleName()).thenReturn("RV02");
        when(rvs2.getLastUpdate()).thenReturn(new Date(2020202L));
        when(rvs2.toString()).thenReturn("RV02");
        when(rvs2.getState()).thenReturn("");

        return Stream.of(
            arguments(Collections.<RealVehicleState> emptyList(), "{}"),

            arguments(Arrays.asList(rvs1), "{\"10101\":{\"type\":\"FeatureCollection\",\"features\":["
                + "{\"type\":\"Feature\",\"properties\":{"
                + "\"rvPosition\":{\"coordinates\":[-122.42649999999999,37.808499999754275,9.999978839419782]},"
                + "\"rvType\":\"QUADROCOPTER\",\"rvName\":\"RV01\",\"rvState\":\"idle\",\"rvHeading\":0,\"rvId\":1,"
                + "\"type\":\"rvPosition\",\"rvTime\":1453047531086},\"geometry\":"
                + "{\"type\":\"Point\",\"coordinates\":[-122.42649999999999,37.808499999754275,9.999978839419782]}},"
                + "{\"type\":\"Feature\",\"properties\":{\"type\":\"vvs\"},\"geometry\":"
                + "{\"type\":\"GeometryCollection\",\"geometries\":[]}},{\"type\":\"Feature\",\"properties\":"
                + "{\"type\":\"rvPath\"},\"geometry\":{\"type\":\"LineString\","
                + "\"coordinates\":[[-122.42649999999999,37.808499999754275,9.999978839419782]]}},"
                + "{\"type\":\"Feature\",\"properties\":{\"type\":\"sensors\"},\"geometry\":"
                + "{\"type\":\"GeometryCollection\",\"geometries\":[]}}]}}"),

            arguments(Arrays.asList(rvs2), "{\"20202\":{\"features\":[]}}"));
    }

    @ParameterizedTest
    @MethodSource("statesDataProvicer")
    public void shouldConvertoToRvSTateJson(List<RealVehicleState> statesList, String expected)
    {
        String actual = sut.toRealVehicleStateJson(statesList);

        assertThat(actual).isEqualTo(expected);
    }

    //    @Test
    //    public void shouldReturnEmptyJsonObjectOnCorruptedState()
    //    {
    //        RealVehicleState rvs = mock(RealVehicleState.class);
    //        when(rvs.getId()).thenReturn(30303);
    //        when(rvs.getRealVehicleName()).thenReturn("RV03");
    //        when(rvs.getLastUpdate()).thenReturn(new Date(3030303L));
    //        when(rvs.toString()).thenReturn("RV03");
    //        when(rvs.getState()).thenReturn("{");
    //        
    //        String actual = sut.toRealVehicleStateJson(Arrays.asList(rvs));
    //
    //        assertThat(actual).isEqualTo("{\"30303\":{}}");
    //    }
}
