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

import java.io.UnsupportedEncodingException;
import java.sql.Date;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import cpcc.rv.base.services.RealVehicleState;

public class RealVehicleStateTest
{
    @DataProvider
    public Object[][] stateDataProvider()
    {
        RealVehicle rv1 = mock(RealVehicle.class);

        return new Object[][]{
            new Object[]{rv1, false, false, new Date(12345678L), null, null},
            new Object[]{rv1, false, false, new Date(12345678L), new byte[]{65, 66, 67, 68}, "ABCD"},
        };
    }

    @Test(dataProvider = "stateDataProvider")
    public void should(RealVehicle realVehicle, boolean connected, boolean statusUpdateRunning
        , Date lastUpdate, byte[] status, String statusString) throws UnsupportedEncodingException
    {
        RealVehicleState sut = new RealVehicleState(realVehicle);

        sut.setConnected(connected);
        sut.setLastUpdate(lastUpdate);
        sut.setStatus(status);
        sut.setStatusUpdateRunning(statusUpdateRunning);

        assertThat(sut.getRealVehicle() == realVehicle).describedAs("realVehicle").isTrue();
        assertThat(sut.isConnected()).describedAs("connected").isEqualTo(connected);
        assertThat(sut.isStatusUpdateRunning()).describedAs("statusUpdateRunning").isEqualTo(statusUpdateRunning);
        assertThat(sut.getLastUpdate()).describedAs("lastUpdate").isEqualTo(lastUpdate);
        assertThat(sut.getStatus()).describedAs("status").isEqualTo(status);
        assertThat(sut.getStatusString()).describedAs("statusString").isEqualTo(statusString);
    }
}
