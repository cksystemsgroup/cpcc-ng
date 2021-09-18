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

import java.nio.ByteOrder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.mozilla.javascript.ScriptableObject;
import org.ros.node.NodeConfiguration;

import cpcc.vvrte.entities.VirtualVehicleStorage;

/**
 * Virtual Vehicle Storage Utils implementation.
 */
public final class VirtualVehicleStorageUtils
{
    private VirtualVehicleStorageUtils()
    {
        // Intentionally empty.
    }

    /**
     * @param item the virtual vehicle storage item containing an image.
     * @return the image as ROS image message.
     */
    public static sensor_msgs.Image itemToRosImageMessage(VirtualVehicleStorage item)
    {
        ScriptableObject obj = item.getContent();
        String encoding = (String) obj.get("encoding", obj);
        int height = (int) obj.get("height", obj);
        int width = (int) obj.get("width", obj);
        int step = (int) obj.get("step", obj);
        byte[] data = (byte[]) obj.get("data", obj);
        ChannelBuffer cb = ChannelBuffers.copiedBuffer(ByteOrder.LITTLE_ENDIAN, data);

        sensor_msgs.Image image = (sensor_msgs.Image) NodeConfiguration.newPrivate().getTopicMessageFactory()
            .newFromType(sensor_msgs.Image._TYPE);
        image.setEncoding(encoding);
        image.setHeight(height);
        image.setWidth(width);
        image.setStep(step);
        image.setIsBigendian((byte) 0);
        image.setData(cb);
        return image;
    }

    /**
     * @param item the virtual vehicle storage item to check.
     * @return true if the item contains an image, false otherwise.
     */
    public static boolean isItemAnImage(VirtualVehicleStorage item)
    {
        if (item == null || item.getContent() == null)
        {
            return false;
        }
        Object messageType = item.getContent().get("messageType", item.getContent());
        return sensor_msgs.Image._TYPE.equals(messageType);
    }
}
