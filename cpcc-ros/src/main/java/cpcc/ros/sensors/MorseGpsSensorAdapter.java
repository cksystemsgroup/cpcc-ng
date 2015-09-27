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

package cpcc.ros.sensors;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.utils.ConfigUtils;
import cpcc.core.utils.GeodeticSystem;
import cpcc.core.utils.PolarCoordinate;
import cpcc.core.utils.WGS84;

/**
 * MorseGpsSensorAdapter
 */
public class MorseGpsSensorAdapter extends AbstractGpsSensorAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(MorseGpsSensorAdapter.class);
    
    private static final String CFG_ORIGIN = "origin";
    
    private sensor_msgs.NavSatFix position;
    
    private sensor_msgs.NavSatFix gpsPosition;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(ConnectedNode connectedNode)
    {
        super.onStart(connectedNode);
        LOG.debug("onStart()");
        
        gpsPosition = connectedNode.getTopicMessageFactory().newFromType(sensor_msgs.NavSatFix._TYPE);

        Subscriber<sensor_msgs.NavSatFix> positionSubscriber =
            connectedNode.newSubscriber(getTopic().getName(), sensor_msgs.NavSatFix._TYPE);

        final PolarCoordinate origin = ConfigUtils.parsePolarCoordinate(getConfig(), CFG_ORIGIN, 0);
        
        positionSubscriber.addMessageListener(new MessageListener<sensor_msgs.NavSatFix>()
        {
            private GeodeticSystem gs = new WGS84();
            
            @Override
            public void onNewMessage(sensor_msgs.NavSatFix message)
            {
                position = message;
                double x = message.getLongitude();
                double y = message.getLatitude();
                double z = message.getAltitude();
                PolarCoordinate p = gs.walk(origin, -x, y, z);
                
                gpsPosition.setLatitude(p.getLatitude());
                gpsPosition.setLongitude(p.getLongitude());
                gpsPosition.setAltitude(p.getAltitude());
                setPosition(gpsPosition);
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
        
        if (position != null)
        {
            map.put("sensor.morse.position", Arrays.asList(
                String.format(Locale.US, "%.8f", position.getLatitude()),
                String.format(Locale.US, "%.8f", position.getLongitude()),
                String.format(Locale.US, "%.3f", position.getAltitude())
                ));
        }
        
        return map;
    }
}
