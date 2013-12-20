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
package at.uni_salzburg.cs.cpcc.vvrte.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.Parameter;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.entities.SensorType;
import at.uni_salzburg.cs.cpcc.core.entities.SensorVisibility;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;
import at.uni_salzburg.cs.cpcc.vvrte.task.Task;

/**
 * VirtualVehicleMapperTest
 */
public class VirtualVehicleMapperTest
{
    private static final String REAL_VEHICLE_ONE_NAME = "rv001";
    private static final String REAL_VEHICLE_TWO_NAME = "rv002";
    
    private static final SensorDefinition altimeter = new SensorDefinition()
    {
        {
            setId(1);
            setDescription("Altimeter");
            setLastUpdate(0);
            setParameters(null);
            setType(SensorType.ALTIMETER);
            setVisibility(SensorVisibility.ALL_VV);
            setMessageType("std_msgs/Float32");
        }
    };

    private static final SensorDefinition barometer = new SensorDefinition()
    {
        {
            setId(1);
            setDescription("Barometer");
            setLastUpdate(0);
            setParameters(null);
            setType(SensorType.BAROMETER);
            setVisibility(SensorVisibility.ALL_VV);
            setMessageType("std_msgs/Float32");
        }
    };

    private static final SensorDefinition co2Sensor = new SensorDefinition()
    {
        {
            setId(1);
            setDescription("CO2");
            setLastUpdate(0);
            setParameters(null);
            setType(SensorType.CO2);
            setVisibility(SensorVisibility.ALL_VV);
            setMessageType("std_msgs/Float32");
        }
    };

    private static final SensorDefinition gpsReceiver = new SensorDefinition()
    {
        {
            setId(1);
            setDescription("GPS");
            setLastUpdate(0);
            setParameters(null);
            setType(SensorType.GPS);
            setVisibility(SensorVisibility.ALL_VV);
            setMessageType("sensor_msgs/NavSatFix");
        }
    };

    private static final String AREA_OF_OPERATION_RV1 = "["
        + "{lat: 47.0, lng: 13}, {lat: 47.0, lng: 14}, {lat: 48.0, lng: 14}, {lat: 48.0, lng: 13},{lat: 47.0, lng: 13}"
        + "]";

    private static final String AREA_OF_OPERATION_RV2 = "["
        + "{lat: 47.0, lng: 14}, {lat: 47.0, lng: 15}, {lat: 48.0, lng: 15}, {lat: 48.0, lng: 14},{lat: 47.0, lng: 14}"
        + "]";

    private Parameter rvName1;
    private RealVehicle realVehicle1;
    private Parameter rvName2;
    private RealVehicle realVehicle2;
    private QueryManager qm;
    private VirtualVehicleMapperImpl mapper;

    @BeforeMethod
    public void setUp()
    {
        realVehicle1 = mock(RealVehicle.class);
        when(realVehicle1.getAreaOfOperation()).thenReturn(AREA_OF_OPERATION_RV1);
        when(realVehicle1.getSensors()).thenReturn(Arrays.asList(altimeter, barometer, co2Sensor));
        when(realVehicle1.getName()).thenReturn(REAL_VEHICLE_ONE_NAME);
        
        realVehicle2 = mock(RealVehicle.class);
        when(realVehicle2.getAreaOfOperation()).thenReturn(AREA_OF_OPERATION_RV2);
        when(realVehicle2.getSensors()).thenReturn(Arrays.asList(altimeter, barometer, co2Sensor));
        when(realVehicle2.getName()).thenReturn(REAL_VEHICLE_TWO_NAME);

        rvName1 = new Parameter();
        rvName1.setValue(REAL_VEHICLE_ONE_NAME);

        rvName2 = new Parameter();
        rvName2.setValue(REAL_VEHICLE_TWO_NAME);

        qm = mock(QueryManager.class);

        when(qm.findRealVehicleByName(REAL_VEHICLE_ONE_NAME)).thenReturn(realVehicle1);
        when(qm.findRealVehicleByName(REAL_VEHICLE_TWO_NAME)).thenReturn(realVehicle2);
        when(qm.findParameterByName(Parameter.REAL_VEHICLE_NAME)).thenReturn(rvName1);
        when(qm.findAllRealVehicles()).thenReturn(Arrays.asList(realVehicle1, realVehicle2));

        mapper = new VirtualVehicleMapperImpl(qm);
    }

    @DataProvider
    public Object[][] tasksThatCauseMigrationDataProvider()
    {
        return new Object[][]{
            new Object[]{47.9, -13.8, 10.0, Arrays.asList(altimeter, barometer, co2Sensor)},
        };
    }
    
    @Test(dataProvider = "tasksThatCauseMigrationDataProvider")
    public void shouldDecideForMigratingOfVirtualVehicles(double latitude, double longitude, double altitude,
        List<SensorDefinition> sensors)
    {
        Task task = mock(Task.class);
        when(task.getPosition()).thenReturn(new PolarCoordinate(latitude, longitude, altitude));
        when(task.getSensors()).thenReturn(sensors);

        VirtualVehicleMappingDecision decision = mapper.findMappingDecision(task);

        assertThat(decision).isNotNull();
        assertThat(decision.isMigration()).isTrue();
        assertThat(decision.getRealVehicles()).isNotNull().isEmpty();
    }

    @DataProvider
    public Object[][] tasksThatNotCauseMigrationDataProvider()
    {
        return new Object[][]{
            new Object[]{47.9, 13.8, 10.0, Arrays.asList(altimeter, barometer, co2Sensor)},
        };
    }

    @Test(dataProvider = "tasksThatNotCauseMigrationDataProvider")
    public void shouldDecideForNotMigratingOfVirtualVehicles(double latitude, double longitude, double altitude,
        List<SensorDefinition> sensors)
    {
        Task task = mock(Task.class);
        when(task.getPosition()).thenReturn(new PolarCoordinate(latitude, longitude, altitude));
        when(task.getSensors()).thenReturn(sensors);

        VirtualVehicleMappingDecision decision = mapper.findMappingDecision(task);

        assertThat(decision).isNotNull();
        assertThat(decision.isMigration()).isFalse();
    }

    @DataProvider
    public Object[][] tasksThatCauseNoMigrationBecauseOfSensorsDataProvider()
    {
        return new Object[][]{
            new Object[]{47.9, 13.8, 10.0, Arrays.asList(altimeter, barometer, co2Sensor)},
            new Object[]{47.9, 13.8, 10.0, Arrays.asList(altimeter, co2Sensor, barometer)},
            new Object[]{47.9, 13.8, 10.0, Arrays.asList(barometer, co2Sensor, altimeter)},
            new Object[]{47.9, 13.8, 10.0, Arrays.asList(barometer, altimeter, co2Sensor)},
            new Object[]{47.9, 13.8, 10.0, Arrays.asList(co2Sensor, altimeter, barometer)},
            new Object[]{47.9, 13.8, 10.0, Arrays.asList(co2Sensor, barometer, altimeter)}
        };
    }

    @Test(dataProvider = "tasksThatCauseNoMigrationBecauseOfSensorsDataProvider")
    public void shouldDecideForNoMigrationBecauseOfSensors(double latitude, double longitude, double altitude,
        List<SensorDefinition> sensors)
    {
        Task task = mock(Task.class);
        when(task.getPosition()).thenReturn(new PolarCoordinate(latitude, longitude, altitude));
        when(task.getSensors()).thenReturn(sensors);

        VirtualVehicleMappingDecision decision = mapper.findMappingDecision(task);

        assertThat(decision).isNotNull();
        assertThat(decision.isMigration()).isFalse();
    }

    @DataProvider
    public Object[][] tasksThatCauseMigrationBecauseOfSensorsDataProvider()
    {
        return new Object[][]{
            new Object[]{47.9, 13.8, 10.0, Arrays.asList(altimeter, barometer, co2Sensor, gpsReceiver)},
        };
    }

    @Test(dataProvider = "tasksThatCauseMigrationBecauseOfSensorsDataProvider")
    public void shouldDecideForMigrationBecauseOfSensors(double latitude, double longitude, double altitude,
        List<SensorDefinition> sensors)
    {
        Task task = mock(Task.class);
        when(task.getPosition()).thenReturn(new PolarCoordinate(latitude, longitude, altitude));
        when(task.getSensors()).thenReturn(sensors);

        VirtualVehicleMappingDecision decision = mapper.findMappingDecision(task);

        assertThat(decision).isNotNull();
        assertThat(decision.isMigration()).isTrue();
        assertThat(decision.getRealVehicles()).isNotNull().isEmpty();
    }

    
    @DataProvider
    public Object[][] tasksThatCauseMigrationBecauseOfPositionDataProvider()
    {
        return new Object[][]{
            new Object[]{47.9, 14.1, 10.0, Arrays.asList(altimeter, barometer, co2Sensor)},
            new Object[]{47.8, 14.2, 10.0, Arrays.asList(altimeter, co2Sensor, barometer)},
            new Object[]{47.7, 14.3, 10.0, Arrays.asList(barometer, co2Sensor, altimeter)},
            new Object[]{47.6, 14.4, 10.0, Arrays.asList(barometer, altimeter, co2Sensor)},
            new Object[]{47.2, 14.5, 10.0, Arrays.asList(co2Sensor, altimeter, barometer)},
            new Object[]{47.1, 14.6, 10.0, Arrays.asList(co2Sensor, barometer, altimeter)}
        };
    }

    @Test(dataProvider = "tasksThatCauseMigrationBecauseOfPositionDataProvider")
    public void shouldDecideForMigrationBecauseOfPosition(double latitude, double longitude, double altitude,
        List<SensorDefinition> sensors)
    {
        Task task = mock(Task.class);
        when(task.getPosition()).thenReturn(new PolarCoordinate(latitude, longitude, altitude));
        when(task.getSensors()).thenReturn(sensors);

        VirtualVehicleMappingDecision decision = mapper.findMappingDecision(task);

        assertThat(decision).isNotNull();
        assertThat(decision.isMigration()).isTrue();
        assertThat(decision.getRealVehicles())
            .overridingErrorMessage("Expected migration to real vehicle %s", rvName2.getValue())
            .isNotNull()
            .containsExactly(realVehicle2);
        assertThat(decision.getRealVehicles().get(0).getName()).isNotNull().isEqualTo(rvName2.getValue());
    }
    
}
