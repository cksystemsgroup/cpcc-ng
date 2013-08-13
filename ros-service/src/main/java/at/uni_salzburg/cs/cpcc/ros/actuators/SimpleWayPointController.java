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

import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SimpleWayPointController
 */
public class SimpleWayPointController extends AbstractActuator
{
    private static final Logger LOG = LoggerFactory.getLogger(SimpleWayPointController.class);
    
    private Publisher<geometry_msgs.Pose> publisher;

    /**
     * {@inheritDoc}
     */
    @Override
    public ActuatorType getType()
    {
        return ActuatorType.SIMPLE_WAYPOINT_CONTROLLER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphName getDefaultNodeName()
    {
        return GraphName.newAnonymous();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(ConnectedNode connectedNode)
    {
        LOG.debug("onStart()");
        
        publisher = connectedNode.newPublisher(getTopic().getName(), geometry_msgs.Pose._TYPE);
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

    /**
     * @param position the desired position.
     */
    public void setPosition(geometry_msgs.Pose position)
    {
        if (publisher != null)
        {
            publisher.publish(position);
        }
    }
}
