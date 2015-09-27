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

package cpcc.core.entities;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.entities.DeviceType;
import cpcc.core.entities.Topic;

/**
 * DeviceTypeTest
 */
public class DeviceTypeTest
{
    DeviceType deviceType;

    @BeforeMethod
    public void setUp()
    {
        deviceType = new DeviceType();
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
        deviceType.setId(id);
        assertThat(deviceType.getId()).isEqualTo(id);
    }

    @DataProvider
    public Object[][] nameDataProvider()
    {
        return new Object[][]{
            new Object[]{null},
            new Object[]{""},
            new Object[]{"Generic GPS Receiver"},
            new Object[]{"Camera Sensor"},
            new Object[]{"Sonar"},
            new Object[]{"Thermometer"},
        };
    };

    @Test(dataProvider = "nameDataProvider")
    public void shouldStoreName(String name)
    {
        deviceType.setName(name);
        assertThat(deviceType.getName()).isEqualTo(name);
    }

    @DataProvider
    private final Iterator<Object[]> topicDataProvider()
    {
        return new Iterator<Object[]>()
        {
            private int counter = -1;

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
                return new Object[]{topic};
            }

            @Override
            public void remove()
            {
                // Intentionally empty.
            }
        };
    };

    @Test(dataProvider = "topicDataProvider")
    public void shouldStoreMainTopic(Topic topic)
    {
        deviceType.setMainTopic(topic);
        assertThat(deviceType.getMainTopic()).isEqualTo(topic);
    }

    @DataProvider
    public Object[][] classNameDataProvider()
    {
        return new Object[][]{
            new Object[]{null},
            new Object[]{""},
            new Object[]{"cpcc.ros.actuators.MorseWayPointControllerAdapter"},
            new Object[]{"cpcc.ros.sensors.CameraSensorAdapter"},
        };
    };

    @Test(dataProvider = "classNameDataProvider")
    public void shouldStoreClassName(String className)
    {
        deviceType.setClassName(className);
        assertThat(deviceType.getClassName()).isEqualTo(className);
    }

    @DataProvider
    private final Iterator<Object[]> subTopicsDataProvider()
    {
        return new Iterator<Object[]>()
        {
            private int counter = -2;
            private List<Topic> topicList = new ArrayList<Topic>();

            @Override
            public boolean hasNext()
            {
                return counter < 10;
            }

            @Override
            public Object[] next()
            {
                if (counter++ < -1)
                {
                    return new Object[]{null};
                }
                if (counter > 0)
                {
                    Topic topic = new Topic();
                    topic.setId(counter);
                    topic.setAdapterClassName("testclass" + counter);
                    topic.setSubpath("path" + counter);
                    topicList.add(topic);
                }
                return new Object[]{topicList};
            }

            @Override
            public void remove()
            {
                // Intentionally empty.
            }
        };
    };

    @Test(dataProvider = "subTopicsDataProvider")
    public void shouldStoreSubTopics(List<Topic> subTopics)
    {
        deviceType.setSubTopics(subTopics);
        assertThat(deviceType.getSubTopics()).isEqualTo(subTopics);
    }

}
