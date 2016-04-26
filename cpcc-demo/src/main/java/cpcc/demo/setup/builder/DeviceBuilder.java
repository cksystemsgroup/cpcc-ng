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

import cpcc.core.entities.Device;
import cpcc.core.entities.DeviceType;

/**
 * DeviceBuilder implementation.
 */
public class DeviceBuilder
{
    private Device device;

    /**
     * Public default constructor.
     */
    public DeviceBuilder()
    {
        this.device = new Device();
    }

    /**
     * @param id the device ID.
     * @return this instance.
     */
    public DeviceBuilder setId(Integer id)
    {
        device.setId(id);
        return this;
    }

    /**
     * @param topicRoot the topic root path to set
     * @return this instance.
     */
    public DeviceBuilder setTopicRoot(String topicRoot)
    {
        device.setTopicRoot(topicRoot);
        return this;
    }

    /**
     * @param type the device type.
     * @return this instance.
     */
    public DeviceBuilder setType(DeviceType type)
    {
        device.setType(type);
        return this;
    }

    /**
     * @param configuration the device configuration string.
     * @return this instance.
     */
    public DeviceBuilder setConfiguration(String configuration)
    {
        device.setConfiguration(configuration);
        return this;
    }

    /**
     * @return the newly build device instance.
     */
    public Device build()
    {
        return device;
    }

}
