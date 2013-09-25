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
package at.uni_salzburg.cs.cpcc.ros.sensors;

import java.util.List;
import java.util.Map;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CameraInfoAdapter
 */
public class Float32SensorAdapter extends AbstractSensorAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(Float32SensorAdapter.class);
    
    private std_msgs.Float32 value;

    /**
     * {@inheritDoc}
     */
    @Override
    public SensorType getType()
    {
        return SensorType.FLOAT_SENSOR;
    }

    /**
     * @return the current value
     */
    public std_msgs.Float32 getValue()
    {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(ConnectedNode connectedNode)
    {
        LOG.debug("onStart()");

        Subscriber<std_msgs.Float32> cameraInfoSubscriber =
            connectedNode.newSubscriber(getTopic().getName(), std_msgs.Float32._TYPE);

        cameraInfoSubscriber.addMessageListener(new MessageListener<std_msgs.Float32>()
        {
            @Override
            public void onNewMessage(std_msgs.Float32 message)
            {
                value = message;
            }
        });
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<String>> getCurrentState()
    {
        Map<String, List<String>> map = super.getCurrentState();

        if (value != null)
        {
            map.put("sensor.float.value", floatAsString(value.getData()));
        }

        return map;
    }
}
