/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2014 Clemens Krainer <clemens.krainer@gmail.com>
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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.ros.node.ConnectedNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.core.utils.ConfigUtils;
import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;

/**
 * GroundStationGpsPositionAdapter
 */
public class GroundStationGpsPositionAdapter extends AbstractGpsSensorAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(GroundStationGpsPositionAdapter.class);

    private static final String CFG_POSITION = "position";

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(ConnectedNode connectedNode)
    {
        super.onStart(connectedNode);
        LOG.debug("onStart()");

        sensor_msgs.NavSatFix gpsPosition =
            connectedNode.getTopicMessageFactory().newFromType(sensor_msgs.NavSatFix._TYPE);

        final PolarCoordinate origin = ConfigUtils.parsePolarCoordinate(getConfig(), CFG_POSITION, 0);

        gpsPosition.setLatitude(origin.getLatitude());
        gpsPosition.setLongitude(origin.getLongitude());
        gpsPosition.setAltitude(origin.getAltitude());
        setPosition(gpsPosition);

        setStartCompleted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<String>> getCurrentState()
    {
        Map<String, List<String>> map = super.getCurrentState();

        if (getPosition() != null)
        {
            map.put("ground.station.position", Arrays.asList(
                String.format(Locale.US, "%.8f", getPosition().getLatitude()),
                String.format(Locale.US, "%.8f", getPosition().getLongitude()),
                String.format(Locale.US, "%.3f", getPosition().getAltitude())
                ));
        }

        return map;
    }
}
