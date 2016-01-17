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

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.geojson.FeatureCollection;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import cpcc.core.entities.MappingAttributes;
import cpcc.core.entities.Parameter;
import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;
import cpcc.core.entities.RealVehicleType;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;
import cpcc.core.services.QueryManager;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.jobs.TimeService;
import cpcc.ros.sensors.AbstractGpsSensorAdapter;
import cpcc.ros.services.RosNodeService;
import cpcc.vvrte.entities.Task;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.services.db.TaskRepository;
import cpcc.vvrte.services.db.VvRteRepository;
import cpcc.vvrte.services.json.VvGeoJsonConverter;
import cpcc.vvrte.services.json.VvGeoJsonConverterImpl;
import sensor_msgs.NavSatFix;

public class StateServiceTest
{
    private static final double POSITION_ALTITUDE_1 = 4.5;
    private static final double POSITION_LONGITUDE_1 = 6.7;
    private static final double POSITION_LATITUDE_1 = 8.9;

    private static final double POSITION_ALTITUDE_2 = 5.3;
    private static final double POSITION_LONGITUDE_2 = 7.4;
    private static final double POSITION_LATITUDE_2 = 9.5;

    private static final int SENSOR_DEFINITION_ONE_ID = 111;
    private static final int SENSOR_DEFINITION_TWO_ID = 222;
    private static final int SENSOR_DEFINITION_THREE_ID = 333;

    private static final int REAL_VEHICLE_ID = 1001;
    private static final String REAL_VEHICLE_NAME = "RV01";

    private static final long LAST_RV_UPDATE = 123456789L;
    private static final long NOW_TIMEOUT_CURRENT_MILLIS = 200000000L;
    private static final long NOW_NO_TIMEOUT_CURRENT_MILLIS = 123456790L;

    private StateService sut;

    private QueryManager qm;
    private RosNodeService rns;
    private VvRteRepository vvRepo;
    private VvGeoJsonConverter vjc;
    private AbstractGpsSensorAdapter adapter;
    private NavSatFix position;
    private RealVehicle realVehicle;
    private RealVehicleState rvState;
    private List<VirtualVehicle> vvList;
    private VirtualVehicle vv1;
    private VirtualVehicle vv2;
    private RealVehicleRepository rvRepo;
    private TaskRepository taskRepo;
    private TimeService timeService;

    @BeforeMethod
    public void setUp()
    {
        Logger logger = mock(Logger.class);

        SensorDefinition sd1 = mock(SensorDefinition.class);
        when(sd1.getId()).thenReturn(SENSOR_DEFINITION_ONE_ID);
        when(sd1.getType()).thenReturn(SensorType.GPS);

        SensorDefinition sd2 = mock(SensorDefinition.class);
        when(sd2.getId()).thenReturn(SENSOR_DEFINITION_TWO_ID);
        when(sd2.getType()).thenReturn(SensorType.ALTIMETER);

        SensorDefinition sd3 = mock(SensorDefinition.class);
        when(sd3.getId()).thenReturn(SENSOR_DEFINITION_THREE_ID);
        when(sd3.getType()).thenReturn(SensorType.BAROMETER);

        MappingAttributes ma1 = mock(MappingAttributes.class);
        when(ma1.getConnectedToAutopilot()).thenReturn(true);
        when(ma1.getSensorDefinition()).thenReturn(sd1);

        MappingAttributes ma2 = mock(MappingAttributes.class);
        when(ma2.getConnectedToAutopilot()).thenReturn(true);
        when(ma2.getSensorDefinition()).thenReturn(sd2);

        MappingAttributes ma3 = mock(MappingAttributes.class);
        when(ma3.getConnectedToAutopilot()).thenReturn(true);

        MappingAttributes ma4 = mock(MappingAttributes.class);

        List<MappingAttributes> allMappingAttributes = Arrays.asList(ma1, ma2, ma3, ma4);

        Parameter rvName = mock(Parameter.class);
        when(rvName.getValue()).thenReturn(REAL_VEHICLE_NAME);

        realVehicle = mock(RealVehicle.class);
        when(realVehicle.getId()).thenReturn(REAL_VEHICLE_ID);
        when(realVehicle.getName()).thenReturn(REAL_VEHICLE_NAME);
        when(realVehicle.getType()).thenReturn(RealVehicleType.QUADROCOPTER);

        rvState = mock(RealVehicleState.class);
        when(rvState.getLastUpdate()).thenReturn(new Date(LAST_RV_UPDATE));

        qm = mock(QueryManager.class);
        when(qm.findAllMappingAttributes()).thenReturn(allMappingAttributes);
        when(qm.findParameterByName(Parameter.REAL_VEHICLE_NAME, "")).thenReturn(rvName);

        rvRepo = mock(RealVehicleRepository.class);
        when(rvRepo.findRealVehicleByName(REAL_VEHICLE_NAME)).thenReturn(realVehicle);
        when(rvRepo.findRealVehicleStateById(REAL_VEHICLE_ID)).thenReturn(rvState);
        when(rvRepo.findOwnRealVehicle()).thenReturn(realVehicle);

        position = mock(NavSatFix.class);
        when(position.getLatitude()).thenReturn(POSITION_LATITUDE_1);
        when(position.getLongitude()).thenReturn(POSITION_LONGITUDE_1);
        when(position.getAltitude()).thenReturn(POSITION_ALTITUDE_1);

        adapter = mock(AbstractGpsSensorAdapter.class);
        when(adapter.getPosition()).thenReturn(position);

        rns = mock(RosNodeService.class);
        when(rns.findAdapterNodeBySensorDefinitionId(SENSOR_DEFINITION_ONE_ID)).thenReturn(adapter);

        vv1 = mock(VirtualVehicle.class);
        when(vv1.getName()).thenReturn("vv1");
        when(vv1.getUuid()).thenReturn("19a43d42-669a-11e3-a337-df369887df3e");
        when(vv1.getState()).thenReturn(VirtualVehicleState.RUNNING);

        vv2 = mock(VirtualVehicle.class);
        when(vv2.getName()).thenReturn("vv2");
        when(vv2.getUuid()).thenReturn("1d50b380-669a-11e3-9008-471c9c51252f");
        when(vv2.getState()).thenReturn(VirtualVehicleState.DEFECTIVE);

        vvList = Arrays.asList(vv1, vv2);

        vvRepo = mock(VvRteRepository.class);
        when(vvRepo.findAllActiveVehicles(anyInt())).thenReturn(vvList);

        vjc = new VvGeoJsonConverterImpl();

        PolarCoordinate pos1 = mock(PolarCoordinate.class);
        when(pos1.getLatitude()).thenReturn(POSITION_LATITUDE_1);
        when(pos1.getLongitude()).thenReturn(POSITION_LONGITUDE_1);
        when(pos1.getAltitude()).thenReturn(POSITION_ALTITUDE_1);

        Task task1 = mock(Task.class);
        when(task1.getPosition()).thenReturn(pos1);
        when(task1.getSensors()).thenReturn(Arrays.asList(sd1));

        PolarCoordinate pos2 = mock(PolarCoordinate.class);
        when(pos2.getLatitude()).thenReturn(POSITION_LATITUDE_2);
        when(pos2.getLongitude()).thenReturn(POSITION_LONGITUDE_2);
        when(pos2.getAltitude()).thenReturn(POSITION_ALTITUDE_2);

        Task task2 = mock(Task.class);
        when(task2.getPosition()).thenReturn(pos2);
        when(task2.getSensors()).thenReturn(Arrays.asList(sd2, sd3));

        taskRepo = mock(TaskRepository.class);
        when(taskRepo.getScheduledTasks()).thenReturn(Arrays.asList(task1, task2));

        timeService = mock(TimeService.class);

        sut = new StateServiceImpl(logger, qm, rns, vvRepo, vjc, rvRepo, taskRepo, timeService);
    }

    @DataProvider
    public Object[][] stateDataProvider()
    {
        return new Object[][]{
            new Object[]{
                null,
                NOW_NO_TIMEOUT_CURRENT_MILLIS,
                "{\"type\":\"FeatureCollection\",\"features\":["
                    + "{\"type\":\"Feature\""
                    + ",\"properties\":{\"rvPosition\":{\"coordinates\":[6.7,8.9,4.5]},\"rvType\":\"QUADROCOPTER\""
                    + ",\"rvName\":\"RV01\",\"rvState\":\"busy\",\"rvHeading\":0,\"rvId\":1001,\"type\":\"rvPosition\",\"rvTime\":123456790}"
                    + ",\"geometry\":{\"type\":\"Point\",\"coordinates\":[6.7,8.9,4.5]}},"
                    + ""
                    + "{\"type\":\"Feature\""
                    + ",\"properties\":{\"type\":\"vvs\"}"
                    + ",\"geometry\":{\"type\":\"GeometryCollection\""
                    + ",\"geometries\":["
                    + "{\"type\":\"Feature\""
                    + ",\"properties\":{\"name\":\"vv1\",\"state\":\"running\",\"type\":\"vv\"},\"id\":\"19a43d...\"},"
                    + "{\"type\":\"Feature\""
                    + ",\"properties\":{\"name\":\"vv2\",\"state\":\"defective\",\"type\":\"vv\"},\"id\":\"1d50b3...\"}"
                    + "]}},"
                    + ""
                    + "{\"type\":\"Feature\""
                    + ",\"properties\":{\"type\":\"rvPath\"}"
                    + ",\"geometry\":{\"type\":\"LineString\",\"coordinates\":"
                    + "[[6.7,8.9,4.5],[6.7,8.9,4.5],[7.4,9.5,5.3]]}},"
                    + ""
                    + "{\"type\":\"Feature\""
                    + ",\"properties\":{\"type\":\"sensors\"}"
                    + ",\"geometry\":{\"type\":\"GeometryCollection\",\"geometries\":["
                    + "{\"type\":\"Feature\",\"properties\":{\"sensorList\":[\"GPS\"]"
                    + ",\"type\":\"rvSensor\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[6.7,8.9,4.5]}},"
                    + "{\"type\":\"Feature\",\"properties\":{\"sensorList\":[\"ALTIMETER\",\"BAROMETER\"]"
                    + ",\"type\":\"rvSensor\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[7.4,9.5,5.3]}}"
                    + "]}}"
                    + "]}"
            },

            new Object[]{
                "pos-vvs",
                NOW_TIMEOUT_CURRENT_MILLIS,
                "{\"type\":\"FeatureCollection\",\"features\":["
                    + "{\"type\":\"Feature\""
                    + ",\"properties\":{\"rvPosition\":{\"coordinates\":[6.7,8.9,4.5]}"
                    + ",\"rvType\":\"QUADROCOPTER\",\"rvName\":\"RV01\",\"rvState\":\"none\""
                    + ",\"rvHeading\":0,\"rvId\":1001,\"type\":\"rvPosition\",\"rvTime\":200000000}"
                    + ",\"geometry\":{\"type\":\"Point\",\"coordinates\":[6.7,8.9,4.5]}},"
                    + ""
                    + "{\"type\":\"Feature\""
                    + ",\"properties\":{\"type\":\"vvs\"}"
                    + ",\"geometry\":{\"type\":\"GeometryCollection\",\"geometries\":["
                    + "{\"type\":\"Feature\",\"properties\":{\"name\":\"vv1\",\"state\":\"running\""
                    + ",\"type\":\"vv\"},\"id\":\"19a43d...\"},"
                    + "{\"type\":\"Feature\",\"properties\":{\"name\":\"vv2\",\"state\":\"defective\""
                    + ",\"type\":\"vv\"},\"id\":\"1d50b3...\"}"
                    + "]}}"
                    + "]}"
            },

            new Object[]{
                "pos",
                NOW_NO_TIMEOUT_CURRENT_MILLIS,
                "{\"type\":\"FeatureCollection\",\"features\":["
                    + "{\"type\":\"Feature\",\"properties\":{"
                    + "\"rvPosition\":{\"coordinates\":[6.7,8.9,4.5]},"
                    + "\"rvType\":\"QUADROCOPTER\",\"rvName\":\"RV01\",\"rvState\":\"busy\",\"rvHeading\":0,"
                    + "\"rvId\":1001,\"type\":\"rvPosition\",\"rvTime\":123456790},"
                    + "\"geometry\":{\"type\":\"Point\",\"coordinates\":[6.7,8.9,4.5]}}"
                    + "]}"
            },

            new Object[]{
                "vvs",
                NOW_TIMEOUT_CURRENT_MILLIS,
                "{\"type\":\"FeatureCollection\",\"features\":["
                    + "{\"type\":\"Feature\""
                    + ",\"properties\":{\"type\":\"vvs\"}"
                    + ",\"geometry\":{\"type\":\"GeometryCollection\",\"geometries\":["
                    + "{\"type\":\"Feature\",\"properties\":{\"name\":\"vv1\",\"state\":\"running\""
                    + ",\"type\":\"vv\"},\"id\":\"19a43d...\"},"
                    + "{\"type\":\"Feature\",\"properties\":{\"name\":\"vv2\",\"state\":\"defective\""
                    + ",\"type\":\"vv\"},\"id\":\"1d50b3...\"}"
                    + "]"
                    + "}}"
                    + "]}"
            },

            new Object[]{
                "tsk",
                NOW_NO_TIMEOUT_CURRENT_MILLIS,
                "{\"type\":\"FeatureCollection\",\"features\":["
                    + "{\"type\":\"Feature\",\"properties\":{\"type\":\"rvPath\"},\"geometry\":{\"type\":\"LineString\""
                    + ",\"coordinates\":[[6.7,8.9,4.5],[6.7,8.9,4.5],[7.4,9.5,5.3]]}}"
                    + "]}"
            },

            new Object[]{
                "sen",
                NOW_TIMEOUT_CURRENT_MILLIS,
                "{\"type\":\"FeatureCollection\",\"features\":["
                    + "{\"type\":\"Feature\""
                    + ",\"properties\":{\"type\":\"sensors\"}"
                    + ",\"geometry\":{\"type\":\"GeometryCollection\",\"geometries\":["
                    + "{\"type\":\"Feature\",\"properties\":{\"sensorList\":[\"GPS\"],\"type\":\"rvSensor\"}"
                    + ",\"geometry\":{\"type\":\"Point\",\"coordinates\":[6.7,8.9,4.5]}},"
                    + "{\"type\":\"Feature\",\"properties\":{\"sensorList\":[\"ALTIMETER\",\"BAROMETER\"]"
                    + ",\"type\":\"rvSensor\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[7.4,9.5,5.3]}}"
                    + "]}}"
                    + "]}"
            },

            new Object[]{
                "tsk-sen",
                NOW_NO_TIMEOUT_CURRENT_MILLIS,
                "{\"type\":\"FeatureCollection\",\"features\":["
                    + "{\"type\":\"Feature\""
                    + ",\"properties\":{\"type\":\"rvPath\"}"
                    + ",\"geometry\":{\"type\":\"LineString\""
                    + ",\"coordinates\":[[6.7,8.9,4.5],[6.7,8.9,4.5],[7.4,9.5,5.3]]}},"
                    + ""
                    + "{\"type\":\"Feature\""
                    + ",\"properties\":{\"type\":\"sensors\"}"
                    + ",\"geometry\":{\"type\":\"GeometryCollection\""
                    + ",\"geometries\":["
                    + "{\"type\":\"Feature\",\"properties\":{\"sensorList\":[\"GPS\"]"
                    + ",\"type\":\"rvSensor\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[6.7,8.9,4.5]}},"
                    + "{\"type\":\"Feature\",\"properties\":{\"sensorList\":[\"ALTIMETER\",\"BAROMETER\"]"
                    + ",\"type\":\"rvSensor\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[7.4,9.5,5.3]}}"
                    + "]}}"
                    + "]}"
            },
        };
    }

    @Test(dataProvider = "stateDataProvider")
    public void shouldGetStateAsFeatureCollection(String what, long now, String expected)
        throws IOException, JSONException
    {
        when(timeService.currentTimeMillis()).thenReturn(now);

        FeatureCollection fc = sut.getState(what);

        String actual = new ObjectMapper().writeValueAsString(fc);

        System.out.println("actual: \"" + actual.replaceAll("\"", "\\\\\"") + "\"");

        JSONAssert.assertEquals(expected, actual, false);
        JSONAssert.assertEquals(actual, expected, false);
    }
}
