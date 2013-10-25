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
package at.uni_salzburg.cs.cpcc.persistence.entities;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.persistence.entities.Device;
import at.uni_salzburg.cs.cpcc.persistence.entities.DeviceType;
import at.uni_salzburg.cs.cpcc.persistence.entities.Topic;

/**
 * DeviceTest
 */
public class DeviceTest
{
    private Device device;

    @BeforeMethod
    public void setUp()
    {
        device = new Device();
    }

    @DataProvider
    public Object[][] integerDataProvider()
    {
        return new Object[][]{
            new Object[]{1},
            new Object[]{10},
            new Object[]{1000},
            new Object[]{100000},
            new Object[]{1000000},
            new Object[]{10000000},
            new Object[]{100000000},
            new Object[]{1000000000},
        };
    };

    @Test(dataProvider = "integerDataProvider")
    public void shouldStoreId(Integer id)
    {
        device.setId(id);
        assertThat(device.getId()).isEqualTo(id);
    }

    @DataProvider
    public Object[][] topicRootDataProvider()
    {
        return new Object[][]{
            new Object[]{"/"},
            new Object[]{"/a"},
            new Object[]{"/abcdefg"},
        };
    };

    @Test(dataProvider = "topicRootDataProvider")
    public void shouldStoreTopicRoot(String topicRoot)
    {
        device.setTopicRoot(topicRoot);
        assertThat(device.getTopicRoot()).isEqualTo(topicRoot);
    }

    @DataProvider
    private final Iterator<Object[]> deviceTypeDataProvider()
    {
        return new Iterator<Object[]>()
        {
            private int counter = -1;
            private List<Topic> topicList = new ArrayList<Topic>();

            @Override
            public boolean hasNext()
            {
                return counter < 10;
            }

            @Override
            public Object[] next()
            {
                if (counter++ < 0)
                {
                    return new Object[]{null};
                }

                Topic topic = new Topic();
                topic.setId(counter);
                topic.setAdapterClassName("testclass" + counter);
                topic.setSubpath("path" + counter);
                topicList.add(topic);

                DeviceType deviceType = new DeviceType();
                deviceType.setId(counter);
                deviceType.setMainTopic(topicList.get(counter - 1));
                deviceType.setName("name" + counter);
                deviceType.setClassName("className" + counter);
                return new Object[]{deviceType};
            }

            @Override
            public void remove()
            {
                // Intentionally empty.
            }
        };
    };

    @Test(dataProvider = "deviceTypeDataProvider")
    public void shouldStoreType(DeviceType deviceType)
    {
        device.setType(deviceType);
        assertThat(device.getType()).isEqualTo(deviceType);
    }

    @DataProvider
    public Object[][] configurationDataProvider()
    {
        return new Object[][]{
            new Object[]{""},
            new Object[]{"a=b"},
            new Object[]{"a=b c=d"},
            new Object[]{"a=(1,2,3) b=7 c='lala'"},
        };
    };

    @Test(dataProvider = "configurationDataProvider")
    public void shouldStoreConfiguration(String configuration)
    {
        device.setConfiguration(configuration);
        assertThat(device.getConfiguration()).isEqualTo(configuration);
    }

    @Test
    public void shouldBeALeaf()
    {
        assertThat(device.isLeaf()).isTrue();
    }

    @Test
    public void shouldHaveNoChildren()
    {
        assertThat(device.hasChildren()).isFalse();
        assertThat(device.getChildren()).isNull();
    }

    @Test
    public void shouldHaveNoLabel()
    {
        assertThat(device.getLabel()).isNull();
    }

    @Test
    public void shouldHaveNoParentLabel()
    {
        assertThat(device.getParentLabel()).isNull();
    }

    @Test(dataProvider = "integerDataProvider")
    public void shouldHaveUniqueId(Integer id)
    {
        device.setId(id);
        assertThat(device.getUniqueId()).isEqualTo("device:" + id);
    }

}
