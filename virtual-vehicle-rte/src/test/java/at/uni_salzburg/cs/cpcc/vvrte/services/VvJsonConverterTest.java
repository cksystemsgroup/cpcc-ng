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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.tapestry5.json.JSONArray;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleState;

public class VvJsonConverterTest
{
    private VvJsonConverter converter;
    private VirtualVehicle vv1 = mock(VirtualVehicle.class);
    private VirtualVehicle vv2 = mock(VirtualVehicle.class);
    private VirtualVehicle vv3 = mock(VirtualVehicle.class);
    private VirtualVehicle vv4 = mock(VirtualVehicle.class);

    @BeforeMethod
    public void setUp()
    {
        converter = new VvJsonConverterImpl();

        when(vv1.getName()).thenReturn("vv1");
        when(vv1.getUuid()).thenReturn("19a43d42-669a-11e3-a337-df369887df3e");
        when(vv1.getState()).thenReturn(VirtualVehicleState.RUNNING);

        when(vv2.getName()).thenReturn("vv2");
        when(vv2.getUuid()).thenReturn("1d50b380-669a-11e3-9008-471c9c51252f");
        when(vv2.getState()).thenReturn(VirtualVehicleState.DEFECTIVE);

        when(vv3.getName()).thenReturn("vv3");
        when(vv3.getUuid()).thenReturn("2088ea36-669a-11e3-b371-13460b0b688a");
        when(vv3.getState()).thenReturn(VirtualVehicleState.FINISHED);

        when(vv4.getName()).thenReturn("vv4");
        when(vv4.getUuid()).thenReturn("235a7d38-669a-11e3-8672-5be62c412e2e");
        when(vv4.getState()).thenReturn(VirtualVehicleState.MIGRATING);
    }

    @DataProvider
    public Object[][] vehiclesDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new VirtualVehicle[]{}, "[]"
            },
            new Object[]{
                new VirtualVehicle[]{vv1}, "["
                    + "{\"name\":\"vv1\",\"state\":\"RUNNING\",\"uuid\":\"19a43d42-669a-11e3-a337-df369887df3e\"}"
                    + "]"
            },
            new Object[]{
                new VirtualVehicle[]{vv1, vv2}, "["
                    + "{\"name\":\"vv1\",\"state\":\"RUNNING\",\"uuid\":\"19a43d42-669a-11e3-a337-df369887df3e\"},"
                    + "{\"name\":\"vv2\",\"state\":\"DEFECTIVE\",\"uuid\":\"1d50b380-669a-11e3-9008-471c9c51252f\"}"
                    + "]"
            },
            new Object[]{
                new VirtualVehicle[]{vv1, vv2, vv3}, "["
                    + "{\"name\":\"vv1\",\"state\":\"RUNNING\",\"uuid\":\"19a43d42-669a-11e3-a337-df369887df3e\"},"
                    + "{\"name\":\"vv2\",\"state\":\"DEFECTIVE\",\"uuid\":\"1d50b380-669a-11e3-9008-471c9c51252f\"},"
                    + "{\"name\":\"vv3\",\"state\":\"FINISHED\",\"uuid\":\"2088ea36-669a-11e3-b371-13460b0b688a\"}"
                    + "]"
            },
            new Object[]{
                new VirtualVehicle[]{vv1, vv2, vv3, vv4}, "["
                    + "{\"name\":\"vv1\",\"state\":\"RUNNING\",\"uuid\":\"19a43d42-669a-11e3-a337-df369887df3e\"},"
                    + "{\"name\":\"vv2\",\"state\":\"DEFECTIVE\",\"uuid\":\"1d50b380-669a-11e3-9008-471c9c51252f\"},"
                    + "{\"name\":\"vv3\",\"state\":\"FINISHED\",\"uuid\":\"2088ea36-669a-11e3-b371-13460b0b688a\"},"
                    + "{\"name\":\"vv4\",\"state\":\"MIGRATING\",\"uuid\":\"235a7d38-669a-11e3-8672-5be62c412e2e\"}"
                    + "]"
            },
        };
    }

    @Test(dataProvider = "vehiclesDataProvider")
    public void should(VirtualVehicle[] vehicles, String expectedJsonString)
    {
        JSONArray result = converter.toJsonArray(vehicles);

        assertThat(result.toString(true)).isNotNull().isEqualTo(expectedJsonString);
    }
}
