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

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GpsSensor
 */
public class GpsSensorAdapter extends AbstractGpsSensorAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(GpsSensorAdapter.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(ConnectedNode connectedNode)
    {
        LOG.debug("onStart()");

        Subscriber<sensor_msgs.NavSatFix> positionSubscriber =
            connectedNode.newSubscriber(getTopic().getName(), sensor_msgs.NavSatFix._TYPE);

        positionSubscriber.addMessageListener(new MessageListener<sensor_msgs.NavSatFix>()
        {
            @Override
            public void onNewMessage(sensor_msgs.NavSatFix message)
            {
                setPosition(message);
            }
        });
    }
}
