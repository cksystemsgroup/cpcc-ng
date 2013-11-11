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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleState;

/**
 * VehicleLauncherTest
 */
public class VehicleLauncherTest
{

    @Test
    public void shouldLaunchSimpleVirtualVehicle()
        throws SerialException, SQLException, IOException, VirtualVehicleLaunchException
    {
        String vvProgramFileName = "simple-vv.js";
        InputStream scriptStream = VehicleLauncherTest.class.getResourceAsStream(vvProgramFileName);
        String program = IOUtils.toString(scriptStream, "UTF-8");

        VirtualVehicle v = spy(new VirtualVehicle());
        v.setCode(program);
        v.setState(VirtualVehicleState.INIT);

        VirtualVehicleLauncher launcher = new VirtualVehicleLauncherImpl();
        launcher.start(v);

        verify(v).setState(VirtualVehicleState.RUNNING);
        assertThat(v.getState()).isNotNull().isEqualTo(VirtualVehicleState.RUNNING);
    }
}
