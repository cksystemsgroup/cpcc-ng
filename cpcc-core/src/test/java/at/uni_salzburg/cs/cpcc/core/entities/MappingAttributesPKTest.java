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

package at.uni_salzburg.cs.cpcc.core.entities;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Iterator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.Device;
import at.uni_salzburg.cs.cpcc.core.entities.MappingAttributesPK;
import at.uni_salzburg.cs.cpcc.core.entities.Topic;

/**
 * MappingAttributesPKTest
 */
public class MappingAttributesPKTest
{
    @Test
    public void shouldHaveNothingInitializedWithDefaultConstructor()
    {
        MappingAttributesPK pk = new MappingAttributesPK();
        assertThat(pk.getDevice()).isNull();
        assertThat(pk.getTopic()).isNull();
    }

    @DataProvider
    private final Iterator<Object[]> primaryKeyDataProvider()
    {
        return new Iterator<Object[]>()
        {
            private int counter = -1;

            @Override
            public boolean hasNext()
            {
                return counter < 5;
            }

            @Override
            public Object[] next()
            {
                if (counter++ < 0)
                {
                    return new Object[]{null, null};
                }

                return new Object[]{new Device(), new Topic()};
            }

            @Override
            public void remove()
            {
                // Intentionally empty.
            }
        };
    };

    @Test(dataProvider = "primaryKeyDataProvider")
    public void shouldInitializeDeviceAndTopicCorrectlyByConstructor(Device device, Topic topic)
    {
        MappingAttributesPK pk = new MappingAttributesPK(device, topic);
        assertThat(pk.getDevice()).isEqualTo(device);
        assertThat(pk.getTopic()).isEqualTo(topic);
    }

    @Test(dataProvider = "primaryKeyDataProvider")
    public void shouldStoreDeviceAndTopicCorrectlyByConstructor(Device device, Topic topic)
    {
        MappingAttributesPK pk = new MappingAttributesPK();
        pk.setDevice(device);
        pk.setTopic(topic);
        assertThat(pk.getDevice()).isEqualTo(device);
        assertThat(pk.getTopic()).isEqualTo(topic);
    }
}
