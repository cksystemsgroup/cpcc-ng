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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.ros.internal.message.Message;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.utilities.CartesianCoordinate;
import at.uni_salzburg.cs.cpcc.utilities.ConfigUtils;
import at.uni_salzburg.cs.cpcc.utilities.GeodeticSystem;
import at.uni_salzburg.cs.cpcc.utilities.PolarCoordinate;
import at.uni_salzburg.cs.cpcc.utilities.WGS84;

/**
 * SimpleWayPointController
 */
public class MorseWayPointControllerAdapter extends AbstractActuatorAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(MorseWayPointControllerAdapter.class);

    private static final String CFG_ORIGIN = "origin";

    private Publisher<geometry_msgs.Pose> publisher;

    private geometry_msgs.Pose position;

    private big_actor_msgs.LatLngAlt gpsPosition;

    private PolarCoordinate origin;

    private CartesianCoordinate originCart;

    private static final GeodeticSystem GEODETIC_SYSTEM = new WGS84();

    /**
     * {@inheritDoc}
     */
    @Override
    public ActuatorType getType()
    {
        return ActuatorType.SIMPLE_WAYPOINT_CONTROLLER;
    }

    /**
     * @param pos the desired position as latitude, longitude, and altitude over ground.
     */
    public void setPosition(big_actor_msgs.LatLngAlt pos)
    {
        gpsPosition = pos;
        if (publisher != null)
        {
            PolarCoordinate p = new PolarCoordinate(pos.getLatitude(), pos.getLongitude(), pos.getAltitude());
            CartesianCoordinate coord = GEODETIC_SYSTEM.polarToRectangularCoordinates(p);
            CartesianCoordinate distance = coord.subtract(originCart);

            position.getPosition().setX(distance.getY());
            position.getPosition().setY(-distance.getX());
            position.getPosition().setZ(distance.getZ());
            publisher.publish(position);
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

        publisher = connectedNode.newPublisher(getTopic().getName(), geometry_msgs.Pose._TYPE);
        position = (geometry_msgs.Pose) connectedNode.getTopicMessageFactory().newFromType(geometry_msgs.Pose._TYPE);

        origin = ConfigUtils.parsePolarCoordinate(getConfig(), CFG_ORIGIN, 0);
        originCart = GEODETIC_SYSTEM.polarToRectangularCoordinates(origin);
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
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<String>> getCurrentState()
    {
        Map<String, List<String>> map = super.getCurrentState();
        if (gpsPosition != null)
        {
            map.put("controller.gps.position", Arrays.asList(
                String.format(Locale.US, "%.8f", gpsPosition.getLatitude()),
                String.format(Locale.US, "%.8f", gpsPosition.getLongitude()),
                String.format(Locale.US, "%.3f", gpsPosition.getAltitude())
                ));
        }
        if (position != null)
        {
            map.put("controller.morse.position", Arrays.asList(
                String.format(Locale.US, "%.8f", position.getPosition().getX()),
                String.format(Locale.US, "%.8f", position.getPosition().getY()),
                String.format(Locale.US, "%.3f", position.getPosition().getZ())
                ));
        }
        return map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message getValue()
    {
        throw new IllegalStateException();
    }

    /**
     * @param value the desired value.
     */
    @Override
    public void setValue(Object value)
    {
        setPosition((big_actor_msgs.LatLngAlt) value);
    }
}
