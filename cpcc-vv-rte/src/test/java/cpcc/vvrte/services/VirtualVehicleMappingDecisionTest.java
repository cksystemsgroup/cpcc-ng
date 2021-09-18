// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.vvrte.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import cpcc.core.entities.RealVehicle;
import cpcc.vvrte.base.VirtualVehicleMappingDecision;
import cpcc.vvrte.entities.Task;

/**
 * VirtualVehicleMappingDecisionTest
 */
class VirtualVehicleMappingDecisionTest
{
    private VirtualVehicleMappingDecision decision;

    @BeforeEach
    void setUp()
    {
        decision = new VirtualVehicleMappingDecision();
    }

    static Stream<Arguments> mappingDataProvider()
    {
        return Stream.of(
            arguments(false, mock(Task.class), new RealVehicle[]{mock(RealVehicle.class)}),
            arguments(false, mock(Task.class), new RealVehicle[]{mock(RealVehicle.class)}),
            arguments(true, mock(Task.class), new RealVehicle[]{mock(RealVehicle.class)}),
            arguments(true, mock(Task.class), new RealVehicle[]{mock(RealVehicle.class)}));
    }

    @ParameterizedTest
    @MethodSource("mappingDataProvider")
    void shouldStoreData(boolean migration, Task task, RealVehicle[] realVehicle)
    {
        decision.setMigration(migration);
        decision.setTask(task);
        decision.setRealVehicles(Arrays.asList(realVehicle));

        assertThat(decision.isMigration()).isEqualTo(migration);
        assertThat(decision.getTask()).isNotNull().isEqualTo(task);
        assertThat(decision.getRealVehicles()).isNotNull().containsExactly(realVehicle);
    }
}
