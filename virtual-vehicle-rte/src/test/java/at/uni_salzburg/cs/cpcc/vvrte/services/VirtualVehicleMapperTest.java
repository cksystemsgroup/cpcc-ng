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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.persistence.entities.Parameter;
import at.uni_salzburg.cs.cpcc.persistence.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.persistence.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.persistence.entities.SensorType;
import at.uni_salzburg.cs.cpcc.persistence.entities.SensorVisibility;
import at.uni_salzburg.cs.cpcc.persistence.services.QueryManager;
import at.uni_salzburg.cs.cpcc.utilities.PolarCoordinate;
import at.uni_salzburg.cs.cpcc.vvrte.task.Task;

/**
 * VirtualVehicleMapperTest
 */
public class VirtualVehicleMapperTest
{
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

    private static final String AREA_OF_OPERATION = "["
        + "{lat: 47.0, lon: 13}, {lat: 47.0, lon: 14}, {lat: 48.0, lon: 14}, {lat: 48.0, lon: 13},{lat: 47.0, lon: 13}"
        + "]";

    private RealVehicle realVehicle;

    private QueryManager qm;

    private VirtualVehicleMapperImpl mapper;

    @DataProvider
    public Object[][] tasksThatCauseMigrationDataProvider()
    {
        return new Object[][]{
            new Object[]{47.9, -13.8, 10.0, Arrays.asList(altimeter, barometer, co2Sensor)},
        };
    }

    @BeforeMethod
    public void setUp()
    {
        realVehicle = mock(RealVehicle.class);
        when(realVehicle.getAreaOfOperation()).thenReturn(AREA_OF_OPERATION);
        when(realVehicle.getSensors()).thenReturn(Arrays.asList(altimeter, barometer, co2Sensor));

        Parameter rvName = new Parameter();
        rvName.setValue("rv001");

        qm = mock(QueryManager.class);
        when(qm.findRealVehicleByName(anyString())).thenReturn(realVehicle);
        when(qm.findParameterByName(Parameter.REAL_VEHICLE_NAME)).thenReturn(rvName);

        mapper = new VirtualVehicleMapperImpl(qm);
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
    }

}
