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

package cpcc.vvrte.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Date;
import java.util.UUID;

import org.testng.annotations.Test;

import cpcc.core.entities.RealVehicle;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;

/**
 * VehicleTest
 */
public class VirtualVehicleTest
{

    @Test
    public void shouldSetAndGetValuesPartOne()
    {
        int id = 10;
        UUID uuid = UUID.randomUUID();
        String vehicleName = "veh01";
        String clob = "bugger that!";
        RealVehicle migrationDestination = mock(RealVehicle.class);
        Date migStart = new Date(12345678);
        byte[] blob = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int apiVersion = 33;
        VirtualVehicleState state = VirtualVehicleState.MIGRATING_SND;
        Date startTime = new Date(1385063517000L);
        Date endTime = new Date(1385069517000L);
        String stateInfo = "The State Info";
        Task task = mock(Task.class);
        VirtualVehicleState preMigrationState = mock(VirtualVehicleState.class);

        VirtualVehicle v = new VirtualVehicle();
        v.setId(id);
        v.setUuid(uuid.toString());
        v.setName(vehicleName);
        v.setCode(clob);
        v.setMigrationDestination(migrationDestination);
        v.setMigrationStartTime(migStart);
        v.setContinuation(blob);
        v.setApiVersion(apiVersion);
        v.setState(state);
        v.setStartTime(startTime);
        v.setEndTime(endTime);
        v.setStateInfo(stateInfo);
        v.setTask(task);
        v.setPreMigrationState(preMigrationState);
        v.setSelected(false);

        assertThat(v.getId()).isNotNull().isEqualTo(id);
        assertThat(v.getUuid()).isNotNull().isEqualTo(uuid.toString());
        assertThat(v.getName()).isNotNull().isEqualTo(vehicleName);
        assertThat(v.getCode()).isNotNull().isEqualTo(clob);
        assertThat(v.getMigrationDestination()).isNotNull().isEqualTo(migrationDestination);
        assertThat(v.getMigrationStartTime()).isNotNull().isEqualTo(migStart);
        assertThat(v.getContinuation()).isNotNull().isEqualTo(blob);
        assertThat(v.getApiVersion()).isNotNull().isEqualTo(apiVersion);
        assertThat(v.getState()).isNotNull().isSameAs(state);
        assertThat(v.getStartTime()).isNotNull().isSameAs(startTime);
        assertThat(v.getEndTime()).isNotNull().isSameAs(endTime);
        assertThat(v.getStateInfo()).isNotNull().isSameAs(stateInfo);
        assertThat(v.getTask()).isNotNull().isSameAs(task);
        assertThat(v.getPreMigrationState()).isNotNull().isSameAs(preMigrationState);
        assertThat(v.isSelected()).isFalse();
    }

    @Test
    public void shouldSetAndGetValuesPartTwo()
    {
        int id = 20;
        UUID uuid = UUID.randomUUID();
        String vehicleName = "veh02";
        String clob = "bugger that also!";
        RealVehicle migrationDestination = mock(RealVehicle.class);
        Date migStart = new Date(22345678);
        byte[] blob = new byte[]{2, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int apiVersion = 44;
        VirtualVehicleState state = VirtualVehicleState.FINISHED;
        Date startTime = new Date(1385063518000L);
        Date endTime = new Date(1385069518000L);
        String stateInfo = "The State Info again";
        Task task = mock(Task.class);
        VirtualVehicleState preMigrationState = mock(VirtualVehicleState.class);

        VirtualVehicle v = new VirtualVehicle();
        v.setId(id);
        v.setUuid(uuid.toString());
        v.setName(vehicleName);
        v.setCode(clob);
        v.setMigrationDestination(migrationDestination);
        v.setMigrationStartTime(migStart);
        v.setContinuation(blob);
        v.setApiVersion(apiVersion);
        v.setState(state);
        v.setStartTime(startTime);
        v.setEndTime(endTime);
        v.setStateInfo(stateInfo);
        v.setTask(task);
        v.setPreMigrationState(preMigrationState);
        v.setSelected(true);

        assertThat(v.getId()).isNotNull().isEqualTo(id);
        assertThat(v.getUuid()).isNotNull().isEqualTo(uuid.toString());
        assertThat(v.getName()).isNotNull().isEqualTo(vehicleName);
        assertThat(v.getCode()).isNotNull().isEqualTo(clob);
        assertThat(v.getMigrationDestination()).isNotNull().isEqualTo(migrationDestination);
        assertThat(v.getMigrationStartTime()).isNotNull().isEqualTo(migStart);
        assertThat(v.getContinuation()).isNotNull().isEqualTo(blob);
        assertThat(v.getApiVersion()).isNotNull().isEqualTo(apiVersion);
        assertThat(v.getState()).isNotNull().isSameAs(state);
        assertThat(v.getStartTime()).isNotNull().isSameAs(startTime);
        assertThat(v.getEndTime()).isNotNull().isSameAs(endTime);
        assertThat(v.getStateInfo()).isNotNull().isSameAs(stateInfo);
        assertThat(v.getTask()).isNotNull().isSameAs(task);
        assertThat(v.getPreMigrationState()).isNotNull().isSameAs(preMigrationState);
        assertThat(v.isSelected()).isTrue();
    }
}
