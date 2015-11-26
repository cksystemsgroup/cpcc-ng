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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.geojson.FeatureCollection;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import sensor_msgs.NavSatFix;

import com.fasterxml.jackson.databind.ObjectMapper;

import cpcc.core.entities.MappingAttributes;
import cpcc.core.entities.Parameter;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;
import cpcc.core.entities.RealVehicleType;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;
import cpcc.core.services.CoreGeoJsonConverter;
import cpcc.core.services.CoreGeoJsonConverterImpl;
import cpcc.core.services.QueryManager;
import cpcc.core.services.RealVehicleRepository;
import cpcc.ros.sensors.AbstractGpsSensorAdapter;
import cpcc.ros.services.RosNodeService;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.services.VvGeoJsonConverter;
import cpcc.vvrte.services.VvGeoJsonConverterImpl;
import cpcc.vvrte.services.VvRteRepository;

public class StateServiceTest
{
    private static final double POSITION_ALTITUDE = 4.5;
    private static final double POSITION_LONGITUDE = 6.7;
    private static final double POSITION_LATITUDE = 8.9;
    private static final int SENSOR_DEFINITION_ONE_ID = 111;
    private static final int SENSOR_DEFINITION_TWO_ID = 222;
    private static final int REAL_VEHICLE_ID = 1001;
    private static final String REAL_VEHICLE_NAME = "RV01";

    private StateService sut;

    private QueryManager qm;
    private RosNodeService rns;
    private VvRteRepository vvRepo;
    private CoreGeoJsonConverter pjc;
    private VvGeoJsonConverter vjc;
    private AbstractGpsSensorAdapter adapter;
    private NavSatFix position;
    private RealVehicle realVehicle;
    private RealVehicleState rvState;
    private List<VirtualVehicle> vvList;
    private VirtualVehicle vv1;
    private VirtualVehicle vv2;
    private RealVehicleRepository rvRepo;

    @BeforeMethod
    public void setUp()
    {
        SensorDefinition sd1 = mock(SensorDefinition.class);
        when(sd1.getId()).thenReturn(SENSOR_DEFINITION_ONE_ID);
        when(sd1.getType()).thenReturn(SensorType.GPS);

        SensorDefinition sd2 = mock(SensorDefinition.class);
        when(sd2.getId()).thenReturn(SENSOR_DEFINITION_TWO_ID);
        when(sd2.getType()).thenReturn(SensorType.ALTIMETER);

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

        qm = mock(QueryManager.class);
        when(qm.findAllMappingAttributes()).thenReturn(allMappingAttributes);
        when(qm.findParameterByName(Parameter.REAL_VEHICLE_NAME, "")).thenReturn(rvName);

        rvRepo = mock(RealVehicleRepository.class);
        when(rvRepo.findRealVehicleByName(REAL_VEHICLE_NAME)).thenReturn(realVehicle);
        when(rvRepo.findRealVehicleStateById(REAL_VEHICLE_ID)).thenReturn(rvState);
        when(rvRepo.findOwnRealVehicle()).thenReturn(realVehicle);

        position = mock(NavSatFix.class);
        when(position.getLatitude()).thenReturn(POSITION_LATITUDE);
        when(position.getLongitude()).thenReturn(POSITION_LONGITUDE);
        when(position.getAltitude()).thenReturn(POSITION_ALTITUDE);

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
        when(vvRepo.findAllVehicles()).thenReturn(vvList);

        pjc = new CoreGeoJsonConverterImpl();
        vjc = new VvGeoJsonConverterImpl();

        sut = new StateServiceImpl(qm, rns, vvRepo, pjc, vjc, rvRepo);
    }

    @DataProvider
    public Object[][] stateDataProvider()
    {
        return new Object[][]{
            new Object[]{
                null,
                "{\"type\":\"FeatureCollection\",\"features\":["
                    + "{\"type\":\"Feature\""
                    + ",\"properties\":{\"rvType\":\"QUADROCOPTER\",\"rvState\":\"none\",\"rvHeading\":0,"
                    + "\"rvPosition\":{\"coordinates\":[6.7,8.9,4.5]},"
                    + "\"rvName\":\"RV01\",\"type\":\"rvPosition\",\"rvId\":1001},"
                    + "\"geometry\":{\"type\":\"Point\",\"coordinates\":[6.7,8.9,4.5]}},"
                    + "{\"type\":\"Feature\",\"properties\":{\"name\":\"vv1\",\"state\":\"running\",\"type\":\"vv\"},"
                    + "\"id\":\"19a43d...\"},"
                    + "{\"type\":\"Feature\",\"properties\":{\"name\":\"vv2\",\"state\":\"defective\",\"type\":\"vv\"},"
                    + "\"id\":\"1d50b3...\"}]}"
            },

            new Object[]{
                "pos-vvs",
                "{\"type\":\"FeatureCollection\",\"features\":["
                    + "{\"type\":\"Feature\""
                    + ",\"properties\":{\"rvType\":\"QUADROCOPTER\",\"rvState\":\"none\",\"rvHeading\":0,"
                    + "\"rvPosition\":{\"coordinates\":[6.7,8.9,4.5]},"
                    + "\"rvName\":\"RV01\",\"type\":\"rvPosition\",\"rvId\":1001},"
                    + "\"geometry\":{\"type\":\"Point\",\"coordinates\":[6.7,8.9,4.5]}},"
                    + "{\"type\":\"Feature\",\"properties\":{\"name\":\"vv1\",\"state\":\"running\",\"type\":\"vv\"},"
                    + "\"id\":\"19a43d...\"},"
                    + "{\"type\":\"Feature\",\"properties\":{\"name\":\"vv2\",\"state\":\"defective\",\"type\":\"vv\"},"
                    + "\"id\":\"1d50b3...\"}]}"
            },

            new Object[]{
                "pos",
                "{\"type\":\"FeatureCollection\",\"features\":["
                    + "{\"type\":\"Feature\""
                    + ",\"properties\":{\"rvType\":\"QUADROCOPTER\",\"rvState\":\"none\",\"rvHeading\":0,"
                    + "\"rvPosition\":{\"coordinates\":[6.7,8.9,4.5]},"
                    + "\"rvName\":\"RV01\",\"type\":\"rvPosition\",\"rvId\":1001},"
                    + "\"geometry\":{\"type\":\"Point\",\"coordinates\":[6.7,8.9,4.5]}}]}"
            },

            new Object[]{
                "vvs",
                "{\"type\":\"FeatureCollection\",\"features\":["
                    + "{\"type\":\"Feature\",\"properties\":{\"name\":\"vv1\",\"state\":\"running\",\"type\":\"vv\"},"
                    + "\"id\":\"19a43d...\"},"
                    + "{\"type\":\"Feature\",\"properties\":{\"name\":\"vv2\",\"state\":\"defective\",\"type\":\"vv\"},"
                    + "\"id\":\"1d50b3...\"}]}"
            },
        };
    }

    @Test(dataProvider = "stateDataProvider")
    public void should(String what, String expected) throws IOException, JSONException
    {
        FeatureCollection fc = sut.getState(what);

        String actual = new ObjectMapper().writeValueAsString(fc);
        // System.out.println("actual: \"" + actual.replaceAll("\"", "\\\\\"") + "\"");

        JSONAssert.assertEquals(expected, actual, false);
        JSONAssert.assertEquals(actual, expected, false);
    }
}
