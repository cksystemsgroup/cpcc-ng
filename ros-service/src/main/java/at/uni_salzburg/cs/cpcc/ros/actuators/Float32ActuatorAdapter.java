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
package at.uni_salzburg.cs.cpcc.ros.actuators;

import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Float32Adapter
 */
public class Float32ActuatorAdapter extends AbstractActuatorAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(Float32ActuatorAdapter.class);
    
    private Publisher<std_msgs.Float32> publisher;

    /**
     * {@inheritDoc}
     */
    @Override
    public ActuatorType getType()
    {
        return ActuatorType.FLOAT_ADAPTER;
    }

    /**
     * @param value the desired value.
     */
    public void setValue(std_msgs.Float32 value)
    {
        if (publisher != null)
        {
            publisher.publish(value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(ConnectedNode connectedNode)
    {
        super.onStart(connectedNode);
        LOG.debug("onStart()");
        publisher = connectedNode.newPublisher(getTopic().getName(), std_msgs.Float32._TYPE);
        setStartCompleted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onShutdown(Node node)
    {
        super.onShutdown(node);
        publisher = null;
    }
}
