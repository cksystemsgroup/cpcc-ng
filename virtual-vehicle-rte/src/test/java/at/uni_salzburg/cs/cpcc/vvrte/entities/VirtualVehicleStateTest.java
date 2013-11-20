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
package at.uni_salzburg.cs.cpcc.vvrte.entities;

import static org.fest.assertions.api.Assertions.assertThat;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * VirtualVehicleStateTest
 */
public class VirtualVehicleStateTest
{
    @DataProvider
    public Object[][] validTransitionsFromInitDataprovider()
    {
        return new Object[][]{
            new Object[]{VirtualVehicleState.RUNNING},
        };
    }

    @Test(dataProvider = "validTransitionsFromInitDataprovider")
    public void shouldDetectValidTransitionsFromInit(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.INIT.canTraverseTo(state);
        assertThat(result).isTrue();
    }

    @DataProvider
    public Object[][] invalidTransitionsFromInitDataprovider()
    {
        return new Object[][]{
            new Object[]{VirtualVehicleState.INIT},
            new Object[]{VirtualVehicleState.WAITING},
            new Object[]{VirtualVehicleState.MIGRATING},
            new Object[]{VirtualVehicleState.FINISHED},
            new Object[]{VirtualVehicleState.INTERRUPTED},
            new Object[]{VirtualVehicleState.DEFECTIVE},
        };
    }

    @Test(dataProvider = "invalidTransitionsFromInitDataprovider")
    public void shouldDetectInvalidTransitionsFromInit(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.INIT.canTraverseTo(state);
        assertThat(result).isFalse();
    }

    @DataProvider
    public Object[][] validTransitionsFromRunningDataprovider()
    {
        return new Object[][]{
            new Object[]{VirtualVehicleState.WAITING},
            new Object[]{VirtualVehicleState.FINISHED},
        };
    }

    @Test(dataProvider = "validTransitionsFromRunningDataprovider")
    public void shouldDetectValidTransitionsFromRunning(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.RUNNING.canTraverseTo(state);
        assertThat(result).isTrue();
    }

    @DataProvider
    public Object[][] invalidTransitionsFromRunningDataprovider()
    {
        return new Object[][]{
            new Object[]{VirtualVehicleState.INIT},
            new Object[]{VirtualVehicleState.RUNNING},
            new Object[]{VirtualVehicleState.MIGRATING},
            new Object[]{VirtualVehicleState.INTERRUPTED},
            new Object[]{VirtualVehicleState.DEFECTIVE},
        };
    }

    @Test(dataProvider = "invalidTransitionsFromRunningDataprovider")
    public void shouldDetectInvalidTransitionsFromRunning(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.RUNNING.canTraverseTo(state);
        assertThat(result).isFalse();
    }

    @DataProvider
    public Object[][] validTransitionsFromWaitingDataprovider()
    {
        return new Object[][]{
            new Object[]{VirtualVehicleState.RUNNING},
            new Object[]{VirtualVehicleState.MIGRATING},
        };
    }

    @Test(dataProvider = "validTransitionsFromWaitingDataprovider")
    public void shouldDetectValidTransitionsFromWaiting(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.WAITING.canTraverseTo(state);
        assertThat(result).isTrue();
    }

    @DataProvider
    public Object[][] invalidTransitionsFromWaitingDataprovider()
    {
        return new Object[][]{
            new Object[]{VirtualVehicleState.INIT},
            new Object[]{VirtualVehicleState.WAITING},
            new Object[]{VirtualVehicleState.FINISHED},
            new Object[]{VirtualVehicleState.INTERRUPTED},
            new Object[]{VirtualVehicleState.DEFECTIVE},
        };
    }

    @Test(dataProvider = "invalidTransitionsFromWaitingDataprovider")
    public void shouldDetectInvalidTransitionsFromWaiting(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.WAITING.canTraverseTo(state);
        assertThat(result).isFalse();
    }

    @DataProvider
    public Object[][] validTransitionsFromMigratingDataprovider()
    {
        return new Object[][]{
            new Object[]{VirtualVehicleState.WAITING},
        };
    }

    @Test(dataProvider = "validTransitionsFromMigratingDataprovider")
    public void shouldDetectValidTransitionsFromMigrating(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.MIGRATING.canTraverseTo(state);
        assertThat(result).isTrue();
    }

    @DataProvider
    public Object[][] invalidTransitionsFromMigratingDataprovider()
    {
        return new Object[][]{
            new Object[]{VirtualVehicleState.INIT},
            new Object[]{VirtualVehicleState.RUNNING},
            new Object[]{VirtualVehicleState.MIGRATING},
            new Object[]{VirtualVehicleState.FINISHED},
            new Object[]{VirtualVehicleState.INTERRUPTED},
            new Object[]{VirtualVehicleState.DEFECTIVE},
        };
    }

    @Test(dataProvider = "invalidTransitionsFromMigratingDataprovider")
    public void shouldDetectInvalidTransitionsFromMigrating(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.MIGRATING.canTraverseTo(state);
        assertThat(result).isFalse();
    }

    @DataProvider
    public Object[][] allStatesDataprovider()
    {
        return new Object[][]{
            new Object[]{VirtualVehicleState.INIT},
            new Object[]{VirtualVehicleState.RUNNING},
            new Object[]{VirtualVehicleState.WAITING},
            new Object[]{VirtualVehicleState.MIGRATING},
            new Object[]{VirtualVehicleState.FINISHED},
            new Object[]{VirtualVehicleState.INTERRUPTED},
            new Object[]{VirtualVehicleState.DEFECTIVE},
        };
    }

    @Test(dataProvider = "allStatesDataprovider")
    public void shouldDetectInvalidTransitionsFromFinished(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.FINISHED.canTraverseTo(state);
        assertThat(result).isFalse();
    }

    @Test(dataProvider = "allStatesDataprovider")
    public void shouldDetectValidTransitionsFromInterrupted(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.INTERRUPTED.canTraverseTo(state);
        assertThat(result).isTrue();
    }

    @Test(dataProvider = "allStatesDataprovider")
    public void shouldDetectInvalidTransitionsFromDefective(VirtualVehicleState state)
    {
        boolean result = VirtualVehicleState.FINISHED.canTraverseTo(state);
        assertThat(result).isFalse();
    }
}
