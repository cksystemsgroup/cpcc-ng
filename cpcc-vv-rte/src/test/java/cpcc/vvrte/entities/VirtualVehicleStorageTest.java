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

import org.hibernate.internal.util.SerializationHelper;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleStorage;

public class VirtualVehicleStorageTest
{
    private VirtualVehicleStorage storage;

    ScriptableObject object0 = new NativeObject();
    ScriptableObject object1 = new NativeObject();
    ScriptableObject object2 = new NativeObject();
    ScriptableObject object3 = new NativeObject();
    ScriptableObject object4 = new NativeObject();
    ScriptableObject object5 = new NativeObject();

    @BeforeMethod
    public void setUp()
    {
        storage = new VirtualVehicleStorage();

        object0.put("id", object0, 0);
        object0.put("value", object0, null);

        object1.put("id", object1, 1);
        object1.put("value", object1, object0);

        object2.put("id", object2, 2);
        object2.put("value", object2, object1);

        object3.put("id", object3, 3);
        object3.put("value", object3, object2);

        object4.put("id", object4, 4);
        object4.put("value", object4, object3);

        object5.put("id", object5, 5);
        object5.put("value", object5, object4);
    }

    @DataProvider
    public Object[][] valuesDataProvider()
    {
        return new Object[][]{
            new Object[]{0, mock(VirtualVehicle.class), "file#0", object0},
            new Object[]{1, mock(VirtualVehicle.class), "file/1", object1},
            new Object[]{2, mock(VirtualVehicle.class), "file-2", object2},
            new Object[]{3, mock(VirtualVehicle.class), "file'3", object3},
            new Object[]{4, mock(VirtualVehicle.class), "file~4", object4},
            new Object[]{5, mock(VirtualVehicle.class), "file|5", object5},
        };
    }

    @Test(dataProvider = "valuesDataProvider")
    public void shouldSetAndGetValues(Integer id, VirtualVehicle virtualVehicle, String name, ScriptableObject content)
    {
        Date modificationTime = new Date();

        storage.setId(id);
        storage.setVirtualVehicle(virtualVehicle);
        storage.setName(name);
        storage.setContent(content);
        storage.setModificationTime(modificationTime);

        assertThat(storage.getId()).isNotNull().isEqualTo(id);
        assertThat(storage.getVirtualVehicle()).isNotNull().isEqualTo(virtualVehicle);
        assertThat(storage.getName()).isNotNull().isEqualTo(name);

        ScriptableObject actual = storage.getContent();
        ScriptableObject actualSub = (ScriptableObject) actual.get("value");
        ScriptableObject contentSub = (ScriptableObject) content.get("value");

        assertThat((Integer) actual.get("id")).isNotNull().isEqualTo((Integer) content.get("id"));

        assertThat((actualSub == null && contentSub == null) || (actualSub != null && contentSub != null)).isTrue();

        if (contentSub != null)
        {
            assertThat((Integer) actualSub.get("id")).isNotNull().isEqualTo((Integer) contentSub.get("id"));
        }

        assertThat(storage.getModificationTime()).isNotNull();
        assertThat(storage.getModificationTime().getTime()).isNotNull().isEqualTo(modificationTime.getTime());
    }

    @Test(dataProvider = "valuesDataProvider")
    public void shouldReturnContentAsByteArray(Integer id, VirtualVehicle virtualVehicle, String name,
        ScriptableObject content)
    {
        byte[] required = SerializationHelper.serialize(content);

        storage.setContent(content);

        byte[] actual = storage.getContentAsByteArray();

        assertThat(actual).isNotNull().isEqualTo(required);
    }
}
