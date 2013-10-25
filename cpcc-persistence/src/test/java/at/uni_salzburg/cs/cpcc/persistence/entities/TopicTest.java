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

import java.util.Iterator;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * TopicTest
 */
public class TopicTest
{
    private Topic topic;

    @BeforeMethod
    public void setUp()
    {
        topic = new Topic();
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
        topic.setId(id);
        assertThat(topic.getId()).isEqualTo(id);
    }

    @DataProvider
    private final Iterator<Object[]> nodeTypeDataProvider()
    {
        return new Iterator<Object[]>()
        {
            private int counter = -1;

            @Override
            public boolean hasNext()
            {
                return counter < RosNodeType.values().length;
            }

            @Override
            public Object[] next()
            {
                if (counter++ < 0)
                {
                    return new Object[]{null};
                }
                return new Object[]{RosNodeType.values()[counter - 1]};
            }

            @Override
            public void remove()
            {
                // Intentionally empty.
            }
        };
    };

    @Test(dataProvider = "nodeTypeDataProvider")
    public void shouldStoreNodeType(RosNodeType nodeType)
    {
        topic.setNodeType(nodeType);
        assertThat(topic.getNodeType()).isEqualTo(nodeType);
    }

    @DataProvider
    public Object[][] pathDataProvider()
    {
        return new Object[][]{
            new Object[]{""},
            new Object[]{"/"},
            new Object[]{"/a"},
            new Object[]{"/abcdefg"},
        };
    };

    @Test(dataProvider = "pathDataProvider")
    public void shouldStoreSubPath(String path)
    {
        topic.setSubpath(path);
        assertThat(topic.getSubpath()).isEqualTo(path);
    }

    @DataProvider
    public Object[][] messageTypeDataProvider()
    {
        return new Object[][]{
            new Object[]{null},
            new Object[]{""},
            new Object[]{"std_msgs/String"},
            new Object[]{"sensor_msgs/Image"},
            new Object[]{"sensor_msgs/Camera_Info"},
            new Object[]{"std_msgs/Float32"},
        };
    };

    @Test(dataProvider = "messageTypeDataProvider")
    public void shouldStoreMessagetype(String messageType)
    {
        topic.setMessageType(messageType);
        assertThat(topic.getMessageType()).isEqualTo(messageType);
    }

    @DataProvider
    public Object[][] classNameDataProvider()
    {
        return new Object[][]{
            new Object[]{null},
            new Object[]{""},
            new Object[]{"at.uni_salzburg.cs.cpcc.ros.actuators.MorseWayPointControllerAdapter"},
            new Object[]{"at.uni_salzburg.cs.cpcc.ros.sensors.CameraSensorAdapter"},
        };
    };

    @Test(dataProvider = "classNameDataProvider")
    public void shouldStoreAdapterClassName(String className)
    {
        topic.setAdapterClassName(className);
        assertThat(topic.getAdapterClassName()).isEqualTo(className);
    }

    @DataProvider
    private final Iterator<Object[]> topicCategoryDataProvider()
    {
        return new Iterator<Object[]>()
        {
            private int counter = -1;

            @Override
            public boolean hasNext()
            {
                return counter < TopicCategory.values().length;
            }

            @Override
            public Object[] next()
            {
                if (counter++ < 0)
                {
                    return new Object[]{null};
                }
                return new Object[]{TopicCategory.values()[counter - 1]};
            }

            @Override
            public void remove()
            {
                // Intentionally empty.
            }
        };
    };

    @Test(dataProvider = "topicCategoryDataProvider")
    public void shouldStoreCategory(TopicCategory category)
    {
        topic.setCategory(category);
        assertThat(topic.getCategory()).isEqualTo(category);
    }
}
