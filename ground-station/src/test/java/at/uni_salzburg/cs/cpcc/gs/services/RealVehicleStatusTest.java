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

package at.uni_salzburg.cs.cpcc.gs.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import cpcc.rv.base.services.RealVehicleState;

/**
 * RealVehicleStatusTest
 */
public class RealVehicleStatusTest
{
    private RealVehicle rv1 = mock(RealVehicle.class);
    private RealVehicle rv2 = mock(RealVehicle.class);
    private RealVehicle rv3 = mock(RealVehicle.class);

    @BeforeMethod
    public void setUp()
    {

    }

    @DataProvider
    public Object[][] statusDataProvider()
    {
        return new Object[][]{
            new Object[]{rv1, null, false, false, new Date(12345)},
            new Object[]{rv2, "", true, true, new Date(234567)},
            new Object[]{rv3, "xxXxx", false, false, new Date(98765432)},
        };
    }

    @Test(dataProvider = "statusDataProvider")
    public void shouldStoreProperties(RealVehicle realVehicle, String statusString, boolean statusUpdateRunning, boolean connected, Date lastUpdate)
        throws UnsupportedEncodingException
    {
        RealVehicleState status = new RealVehicleState(realVehicle);
        status.setStatus(statusString == null ? null :statusString.getBytes());
        status.setStatusUpdateRunning(statusUpdateRunning);
        status.setConnected(connected);
        status.setLastUpdate(lastUpdate);

        assertThat(status.getRealVehicle()).isNotNull().isEqualTo(realVehicle);
        assertThat(status.getStatus()).isEqualTo(statusString == null ? null : statusString.getBytes());
        assertThat(status.getStatusString()).isEqualTo(statusString);
        assertThat(status.isStatusUpdateRunning()).isEqualTo(statusUpdateRunning);
        assertThat(status.isConnected()).isEqualTo(connected);
        assertThat(status.getLastUpdate()).isEqualTo(lastUpdate);
    }
}
