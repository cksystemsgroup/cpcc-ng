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

package cpcc.core.entities;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Date;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.entities.RealVehicleState;

public class RealVehicleStateTest
{
    private RealVehicleState sut;

    @BeforeMethod
    public void setUp()
    {
        sut = new RealVehicleState();
    }

    @DataProvider
    public Object[][] rvDataProvider()
    {
        return new Object[][]{
            new Object[]{1, new Date(1439669289111L), "RV01", "state01"},
            new Object[]{2, new Date(1439669289222L), "RV02", "state02"},
            new Object[]{3, new Date(1439669289333L), "RV03", "state03"},
            new Object[]{4, new Date(1439669289444L), "RV04", "state04"},
        };
    }

    @Test(dataProvider = "rvDataProvider")
    void shouldHandleSettersAndGetters(Integer id, Date lastUpdate, String realVehicleName, String state)
    {
        sut.setId(id);
        sut.setLastUpdate(lastUpdate);
        sut.setRealVehicleName(realVehicleName);
        sut.setState(state);

        assertThat(sut.getId()).isEqualTo(id);
        assertThat(sut.getLastUpdate()).isEqualTo(lastUpdate);
        assertThat(sut.getRealVehicleName()).isEqualTo(realVehicleName);
        assertThat(sut.getState()).isEqualTo(state);
    }
}
