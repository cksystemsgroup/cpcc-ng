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

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;

public class ConfigSyncDataTest
{
    @DataProvider
    public Object[][] syncDataProvider()
    {
        return new Object[][]{
            new Object[]{
                Collections.<SensorDefinition> emptyList(),
                Collections.<RealVehicle> emptyList()
            },
            new Object[]{
                Arrays.asList(mock(SensorDefinition.class)),
                Arrays.asList(mock(RealVehicle.class))
            },
            new Object[]{
                Arrays.asList(mock(SensorDefinition.class), mock(SensorDefinition.class)),
                Arrays.asList(mock(RealVehicle.class), mock(RealVehicle.class))
            },
        };
    }

    @Test(dataProvider = "syncDataProvider")
    public void shouldHandleDefaultConstructor(List<SensorDefinition> sensors, List<RealVehicle> realVehicles)
    {
        ConfigSyncData sut = new ConfigSyncData(null, null);
        sut.setSen(sensors);
        sut.setRvs(realVehicles);

        assertThat(sut.getSen()).containsAll(sensors);
        assertThat(sut.getRvs()).containsAll(realVehicles);
    }

    @Test(dataProvider = "syncDataProvider")
    public void shouldHandleAlternativeConstructor(List<SensorDefinition> sensors, List<RealVehicle> realVehicles)
    {
        ConfigSyncData sut = new ConfigSyncData(sensors, realVehicles);

        assertThat(sut.getSen()).containsAll(sensors);
        assertThat(sut.getRvs()).containsAll(realVehicles);
    }
}
