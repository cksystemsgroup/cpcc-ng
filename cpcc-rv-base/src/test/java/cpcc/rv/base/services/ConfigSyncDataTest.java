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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.SensorDefinition;

public class ConfigSyncDataTest
{
    static Stream<Arguments> syncDataProvider()
    {
        return Stream.of(
            arguments(
                Collections.<SensorDefinition> emptyList(),
                Collections.<RealVehicle> emptyList()),
            arguments(
                Arrays.asList(mock(SensorDefinition.class)),
                Arrays.asList(mock(RealVehicle.class))),
            arguments(
                Arrays.asList(mock(SensorDefinition.class), mock(SensorDefinition.class)),
                Arrays.asList(mock(RealVehicle.class), mock(RealVehicle.class))));
    }

    @ParameterizedTest
    @MethodSource("syncDataProvider")
    public void shouldHandleDefaultConstructor(List<SensorDefinition> sensors, List<RealVehicle> realVehicles)
    {
        ConfigSyncData sut = new ConfigSyncData(null, null);
        sut.setSen(sensors);
        sut.setRvs(realVehicles);

        assertThat(sut.getSen()).containsAll(sensors);
        assertThat(sut.getRvs()).containsAll(realVehicles);
    }

    @ParameterizedTest
    @MethodSource("syncDataProvider")
    public void shouldHandleAlternativeConstructor(List<SensorDefinition> sensors, List<RealVehicle> realVehicles)
    {
        ConfigSyncData sut = new ConfigSyncData(sensors, realVehicles);

        assertThat(sut.getSen()).containsAll(sensors);
        assertThat(sut.getRvs()).containsAll(realVehicles);
    }
}
