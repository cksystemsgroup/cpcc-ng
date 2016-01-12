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

package cpcc.vvrte.services.js;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.testng.annotations.Test;

import cpcc.vvrte.base.VirtualVehicleMappingDecision;
import cpcc.vvrte.entities.Task;

/**
 * ApplicationStateTest implementation.
 */
public class ApplicationStateTest
{
    @Test
    public void shouldHoldMappingDecision()
    {
        VirtualVehicleMappingDecision decision = mock(VirtualVehicleMappingDecision.class);
        when(decision.toString()).thenReturn("decision A");

        ApplicationState sut = new ApplicationState(decision);

        assertThat(sut.getDecision()).isSameAs(decision);
        assertThat(sut.getTask()).isNull();
        assertThat(sut.isMappingDecision()).isTrue();
        assertThat(sut.isTask()).isFalse();
        assertThat(sut.toString()).isEqualTo("Decision: decision A");
    }

    @Test
    public void shouldHoldTask()
    {
        Task task = mock(Task.class);
        when(task.toString()).thenReturn("task B");

        ApplicationState sut = new ApplicationState(task);

        assertThat(sut.getDecision()).isNull();
        assertThat(sut.getTask()).isSameAs(task);
        assertThat(sut.isMappingDecision()).isFalse();
        assertThat(sut.isTask()).isTrue();
        assertThat(sut.toString()).isEqualTo("Task: task B");
    }

    @Test
    public void shouldHoldNothing()
    {
        ApplicationState sut = new ApplicationState((Task) null);

        assertThat(sut.getDecision()).isNull();
        assertThat(sut.getTask()).isNull();
        assertThat(sut.isMappingDecision()).isFalse();
        assertThat(sut.isTask()).isFalse();
        assertThat(sut.toString()).isEqualTo("Application state unknown!");
    }
}
