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

package cpcc.rv.base.services;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.json.JSONException;
import org.mockito.ArgumentMatcher;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationService;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicleType;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.entities.SensorType;
import at.uni_salzburg.cs.cpcc.core.entities.SensorVisibility;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;

/**
 * Configuration synchronization job runnable test.
 */
public class ConfigSyncJobRunnableTest
{

    private final static String EXPECTED_RESULT = "{\"rvs\":["
        + "{\"areaOfOperation\":\"{\\\"a\\\":17,\\\"b\\\":\\\"s\\\"}\",\"deleted\":false,"
        + "\"id\":1001,\"lastUpdate\":1001001001,\"name\":\"RV01\",\"sensors\":["
        + "{\"deleted\":false,\"description\":\"Altimeter\",\"id\":1111,\"lastUpdate\":1439642630111,"
        + "\"messageType\":\"std_msgs/Float32\",\"parameters\":null,\"type\":\"ALTIMETER\",\"visibility\":\"ALL_VV\"},"
        + "{\"deleted\":true,\"description\":\"CO2\",\"id\":2222,\"lastUpdate\":1439642630222,"
        + "\"messageType\":\"std_msgs/Float32\",\"parameters\":null,\"type\":\"CO2\",\"visibility\":\"NO_VV\"}],"
        + "\"type\":\"QUADROCOPTER\",\"url\":\"http://rv01.site/app\"}],"
        + "\"sen\":[{\"deleted\":false,\"description\":\"Altimeter\",\"id\":1111,\"lastUpdate\":1439642630111,"
        + "\"messageType\":\"std_msgs/Float32\",\"parameters\":null,\"type\":\"ALTIMETER\",\"visibility\":\"ALL_VV\"},"
        + "{\"deleted\":true,\"description\":\"CO2\",\"id\":2222,\"lastUpdate\":1439642630222,"
        + "\"messageType\":\"std_msgs/Float32\",\"parameters\":null,\"type\":\"CO2\",\"visibility\":\"NO_VV\"},"
        + "{\"deleted\":false,\"description\":\"FPV Camera 640x480\",\"id\":3333,\"lastUpdate\":1439642630333,"
        + "\"messageType\":\"sensor_msgs/Image\","
        + "\"parameters\":\"width=640 height=480 yaw=0 down=0 alignment='heading'\","
        + "\"type\":\"CAMERA\",\"visibility\":\"PRIVILEGED_VV\"}]}";

    private Map<String, String> parameters;

    private ConfigPushJobRunnable sut;

    private SensorDefinition sd01;
    private SensorDefinition sd02;
    private SensorDefinition sd03;

    private RealVehicle rv01old;
    private RealVehicle rv01new;

    private HibernateSessionManager sessionManager;
    private QueryManager queryManager;
    private CommunicationService com;

    private RealVehicle target;

    @SuppressWarnings("unchecked")
    @BeforeMethod
    public void setUp()
    {
        sd01 = mock(SensorDefinition.class);
        when(sd01.getId()).thenReturn(1111);
        when(sd01.getDescription()).thenReturn("Altimeter");
        when(sd01.getDeleted()).thenReturn(false);
        when(sd01.getLastUpdate()).thenReturn(new Date(1439642630111L));
        when(sd01.getMessageType()).thenReturn("std_msgs/Float32");
        when(sd01.getParameters()).thenReturn(null);
        when(sd01.getType()).thenReturn(SensorType.ALTIMETER);
        when(sd01.getVisibility()).thenReturn(SensorVisibility.ALL_VV);

        sd02 = mock(SensorDefinition.class);
        when(sd02.getId()).thenReturn(2222);
        when(sd02.getDescription()).thenReturn("CO2");
        when(sd02.getDeleted()).thenReturn(true);
        when(sd02.getLastUpdate()).thenReturn(new Date(1439642630222L));
        when(sd02.getMessageType()).thenReturn("std_msgs/Float32");
        when(sd02.getParameters()).thenReturn(null);
        when(sd02.getType()).thenReturn(SensorType.CO2);
        when(sd02.getVisibility()).thenReturn(SensorVisibility.NO_VV);

        sd03 = mock(SensorDefinition.class);
        when(sd03.getId()).thenReturn(3333);
        when(sd03.getDescription()).thenReturn("FPV Camera 640x480");
        when(sd03.getDeleted()).thenReturn(false);
        when(sd03.getLastUpdate()).thenReturn(new Date(1439642630333L));
        when(sd03.getMessageType()).thenReturn("sensor_msgs/Image");
        when(sd03.getParameters()).thenReturn("width=640 height=480 yaw=0 down=0 alignment='heading'");
        when(sd03.getType()).thenReturn(SensorType.CAMERA);
        when(sd03.getVisibility()).thenReturn(SensorVisibility.PRIVILEGED_VV);

        List<SensorDefinition> sensorDefinitions = Arrays.asList(sd01, sd02, sd03);

        rv01old = mock(RealVehicle.class);
        when(rv01old.getAreaOfOperation()).thenReturn("{\"a\":17,\"b\":\"s\"}");
        when(rv01old.getDeleted()).thenReturn(false);
        when(rv01old.getId()).thenReturn(1001);
        when(rv01old.getLastUpdate()).thenReturn(new Date(1001001001L));
        when(rv01old.getName()).thenReturn("RV01");
        when(rv01old.getSensors()).thenReturn(Arrays.asList(sd01, sd02));
        when(rv01old.getType()).thenReturn(RealVehicleType.QUADROCOPTER);
        when(rv01old.getUrl()).thenReturn("http://rv01.site/app");

        rv01new = mock(RealVehicle.class);
        when(rv01new.getAreaOfOperation()).thenReturn("{\"a\":18,\"b\":\"t\"}");
        when(rv01new.getDeleted()).thenReturn(true);
        when(rv01new.getId()).thenReturn(1002);
        when(rv01new.getLastUpdate()).thenReturn(new Date(1001001002L));
        when(rv01new.getName()).thenReturn("rv01");
        when(rv01new.getSensors()).thenReturn(Arrays.asList(sd01, sd02, sd03));
        when(rv01new.getType()).thenReturn(RealVehicleType.GROUND_STATION);
        when(rv01new.getUrl()).thenReturn("http://rv01.site:8001/app");

        List<RealVehicle> realVehicles = Arrays.asList(rv01old);

        target = mock(RealVehicle.class);

        parameters = mock(Map.class);
        when(parameters.get("rv")).thenReturn("10");

        sessionManager = mock(HibernateSessionManager.class);

        queryManager = mock(QueryManager.class);
        when(queryManager.findAllSensorDefinitions()).thenReturn(sensorDefinitions);
        when(queryManager.findAllRealVehicles()).thenReturn(realVehicles);
        when(queryManager.findRealVehicleById(10)).thenReturn(target);

        com = mock(CommunicationService.class);

        ServiceResources serviceResources = mock(ServiceResources.class);
        when(serviceResources.getService(HibernateSessionManager.class)).thenReturn(sessionManager);
        when(serviceResources.getService(QueryManager.class)).thenReturn(queryManager);
        when(serviceResources.getService(CommunicationService.class)).thenReturn(com);

        sut = new ConfigPushJobRunnable(serviceResources, parameters);
    }

    @Test
    public void shouldPrepareSyncData() throws Exception
    {
        sut.run();

        verify(com).transfer(eq(target), eq(RealVehicleBaseConstants.CONFIGURATION_UPDATE_CONNECTOR)
            , argThat(new DataArgumentMatcher()));
    }

    private class DataArgumentMatcher extends ArgumentMatcher<byte[]>
    {
        @Override
        public boolean matches(Object argument)
        {
            byte[] data = (byte[]) argument;

            String actual = new String(data);

            try
            {
                JSONAssert.assertEquals(EXPECTED_RESULT, actual, false);
                JSONAssert.assertEquals(actual, EXPECTED_RESULT, false);
                return true;
            }
            catch (JSONException e)
            {
                // e.printStackTrace();
            }

            return false;
        }
    }

}
