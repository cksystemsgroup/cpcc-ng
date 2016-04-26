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

package cpcc.demo.setup.builder;

import java.util.List;

import cpcc.core.entities.DeviceType;
import cpcc.core.entities.Topic;

/**
 * DeviceTypeBuilder implementation.
 */
public class DeviceTypeBuilder
{
    private DeviceType deviceType;

    /**
     * Public default constructor.
     */
    public DeviceTypeBuilder()
    {
        this.deviceType = new DeviceType();
    }

    /**
     * @param id the device ID.
     * @return this instance.
     */
    public DeviceTypeBuilder setId(int id)
    {
        deviceType.setId(id);
        return this;
    }

    /**
     * @param name the device name.
     * @return this instance.
     */
    public DeviceTypeBuilder setName(String name)
    {
        deviceType.setName(name);
        return this;
    }

    /**
     * @param mainTopic the main topic to set.
     * @return this instance.
     */
    public DeviceTypeBuilder setMainTopic(Topic mainTopic)
    {
        deviceType.setMainTopic(mainTopic);
        return this;
    }

    /**
     * @param className the className to set.
     * @return this instance.
     */
    public DeviceTypeBuilder setClassName(String className)
    {
        deviceType.setClassName(className);
        return this;
    }

    /**
     * @param subTopics the sub topics.
     * @return this instance.
     */
    public DeviceTypeBuilder setSubTopics(List<Topic> subTopics)
    {
        deviceType.setSubTopics(subTopics);
        return this;
    }

    /**
     * @return the newly build device type instance.
     */
    public DeviceType build()
    {
        return deviceType;
    }
}
