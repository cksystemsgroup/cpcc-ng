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

package cpcc.ros.actuators;

import org.ros.message.MessageFactory;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.topic.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.utils.PolarCoordinate;

/**
 * SimpleWayPointController
 */
public class SimpleWayPointControllerAdapter extends AbstractActuatorAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(SimpleWayPointControllerAdapter.class);

    private Publisher<big_actor_msgs.LatLngAlt> publisher;

    private MessageFactory factory;

    /**
     * Constructor
     */
    public SimpleWayPointControllerAdapter()
    {
        factory = NodeConfiguration.newPrivate().getTopicMessageFactory();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActuatorType getType()
    {
        return ActuatorType.SIMPLE_WAYPOINT_CONTROLLER;
    }

    /**
     * @param position the desired position.
     */
    public void setPosition(PolarCoordinate position)
    {
        if (publisher != null)
        {
            big_actor_msgs.LatLngAlt pos = factory.newFromType(big_actor_msgs.LatLngAlt._TYPE);

            pos.setLatitude(position.getLatitude());
            pos.setLongitude(position.getLongitude());
            pos.setAltitude(position.getAltitude());

            publisher.publish(pos);
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
        publisher = connectedNode.newPublisher(getTopic().getName(), big_actor_msgs.LatLngAlt._TYPE);
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

    /**
     * @param value the desired value.
     */
    @Override
    public void setValue(Object value)
    {
        setPosition((PolarCoordinate) value);
    }
}
