// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.vvrte.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;

import java.lang.reflect.Constructor;

import org.mozilla.javascript.ScriptableObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.vvrte.entities.VirtualVehicleStorage;
import sensor_msgs.Image;

/**
 * VirtualVehicleStorageUtilsTest implementation.
 */
public class VirtualVehicleStorageUtilsTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<VirtualVehicleStorageUtils> cnt = VirtualVehicleStorageUtils.class.getDeclaredConstructor();
        assertFalse(cnt.isAccessible());
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @Test
    public void shouldConvertItemToRosImage()
    {
        byte[] data = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

        ScriptableObject content = mock(ScriptableObject.class);
        when(content.get(eq("messageType"), any())).thenReturn(sensor_msgs.Image._TYPE);
        when(content.get(eq("encoding"), any())).thenReturn("png");
        when(content.get(eq("height"), any())).thenReturn(240);
        when(content.get(eq("width"), any())).thenReturn(320);
        when(content.get(eq("step"), any())).thenReturn(3);
        when(content.get(eq("data"), any())).thenReturn(data);

        VirtualVehicleStorage item = mock(VirtualVehicleStorage.class);
        when(item.getContent()).thenReturn(content);

        Image actual = VirtualVehicleStorageUtils.itemToRosImageMessage(item);
        assertThat(actual.getEncoding()).isEqualTo("png");
        assertThat(actual.getHeight()).isEqualTo(240);
        assertThat(actual.getWidth()).isEqualTo(320);
        assertThat(actual.getStep()).isEqualTo(3);
        assertThat(actual.getData().array()).isEqualTo(data);
    }

    @DataProvider
    public Object[][] fakeImageDataProvider()
    {
        ScriptableObject content1 = mock(ScriptableObject.class);
        when(content1.get(eq("messageType"), any())).thenReturn(sensor_msgs.Image._TYPE);

        ScriptableObject content2 = mock(ScriptableObject.class);
        when(content2.get(eq("messageType"), any())).thenReturn(sensor_msgs.Temperature._TYPE);

        VirtualVehicleStorage item1 = mock(VirtualVehicleStorage.class);
        when(item1.getContent()).thenReturn(content1);

        VirtualVehicleStorage item2 = mock(VirtualVehicleStorage.class);
        when(item2.getContent()).thenReturn(content2);

        VirtualVehicleStorage item3 = mock(VirtualVehicleStorage.class);

        return new Object[][]{
            new Object[]{null, false},
            new Object[]{item1, true},
            new Object[]{item2, false},
            new Object[]{item3, false},
        };
    }

    @Test(dataProvider = "fakeImageDataProvider")
    public void shouldFindOutIfItemIsAnImage(VirtualVehicleStorage item, boolean expected)
    {
        boolean actual = VirtualVehicleStorageUtils.isItemAnImage(item);

        assertThat(actual).isEqualTo(expected);
    }
}
