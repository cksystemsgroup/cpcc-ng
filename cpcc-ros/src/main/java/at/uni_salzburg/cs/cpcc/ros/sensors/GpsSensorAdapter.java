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

package at.uni_salzburg.cs.cpcc.ros.sensors;

import java.util.List;
import java.util.Map;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.core.utils.ConvertUtils;
import sensor_msgs.NavSatFix;
import sensor_msgs.NavSatStatus;

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
        super.onStart(connectedNode);
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
            NavSatFix p = getPosition();
            NavSatStatus s = p.getStatus();
            map.put("sensor.gps.position.covariance", ConvertUtils.doubleListAsString(p.getPositionCovariance()));
            map.put("sensor.gps.position.covariance.type", ConvertUtils.byteAsString(p.getPositionCovarianceType()));
            map.put("sensor.gps.status.status", ConvertUtils.byteAsString(s.getStatus()));
            map.put("sensor.gps.status.service", ConvertUtils.shortAsString(s.getService()));
        }

        return map;
    }
}
