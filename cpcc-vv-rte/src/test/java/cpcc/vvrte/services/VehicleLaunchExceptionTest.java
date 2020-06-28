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
import static org.mockito.Mockito.mock;

import org.testng.annotations.Test;

/**
 * VehicleLauncherExceptionTest
 */
public class VehicleLaunchExceptionTest
{
    @Test
    public void shouldConstructWithoutParameters()
    {
        VirtualVehicleLaunchException ex = new VirtualVehicleLaunchException();
        assertThat(ex).isNotNull();
    }

    @Test
    public void shouldConstructWithMessageOnly()
    {
        String message = "buggerit!";
        VirtualVehicleLaunchException ex = new VirtualVehicleLaunchException(message);
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).isNotNull().isEqualTo(message);
    }

    @Test
    public void shouldConstructWithCauseOnly()
    {
        Throwable cause = mock(Throwable.class);

        VirtualVehicleLaunchException ex = new VirtualVehicleLaunchException(cause);
        assertThat(ex).isNotNull();
        assertThat(ex.getCause()).isNotNull().isEqualTo(cause);
    }

    @Test
    public void shouldConstructWithMessageAndCause()
    {
        String message = "buggerit II!";
        Throwable cause = mock(Throwable.class);

        VirtualVehicleLaunchException ex = new VirtualVehicleLaunchException(message, cause);
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).isNotNull().isEqualTo(message);
        assertThat(ex.getCause()).isNotNull().isEqualTo(cause);
    }
}
