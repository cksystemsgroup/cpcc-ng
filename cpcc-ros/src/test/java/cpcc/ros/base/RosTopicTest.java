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

package cpcc.ros.base;

import static org.fest.assertions.api.Assertions.assertThat;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.ros.base.RosTopic;

/**
 * RosTopicTest
 */
public class RosTopicTest
{
    @DataProvider
    public static Object[][] topicDataProvider()
    {
        return new Object[][]{
            new Object[]{null, null},
            new Object[]{"name1", null},
            new Object[]{null, "topic1"},
            new Object[]{"name1", "topic1"},
        };
    }

    @Test(dataProvider = "topicDataProvider")
    public void shouldStoreNameAndType(String name, String type)
    {
        RosTopic topic = new RosTopic();
        topic.setName(name);
        topic.setType(type);

        assertThat(topic.getName()).isEqualTo(name);
        assertThat(topic.getType()).isEqualTo(type);
    }
}
