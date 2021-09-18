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

package cpcc.ros.sim.sonar;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.ros.node.DefaultNodeMainExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.ros.sim.AbstractRosNodeGroup;
import sensor_msgs.NavSatFix;

/**
 * SonarEmulator
 */
public class SonarEmulator extends AbstractRosNodeGroup
{
    private static final Logger LOG = LoggerFactory.getLogger(SonarEmulator.class);

    private SonarEmulatorPublisherNode publisherNode;
    private SonarEmulatorListenerNode listenerNode;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start()
    {
        LOG.info("start()");
        getConfig().put("topicRoot", Arrays.asList(getTopicRoot()));

        publisherNode = new SonarEmulatorPublisherNode(getConfig());
        listenerNode = new SonarEmulatorListenerNode(getConfig(), publisherNode);

        DefaultNodeMainExecutor.newDefault().execute(publisherNode, getNodeConfiguration());
        DefaultNodeMainExecutor.newDefault().execute(listenerNode, getNodeConfiguration());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown()
    {
        LOG.info("shutdown()");
        DefaultNodeMainExecutor.newDefault().shutdownNodeMain(listenerNode);
        listenerNode.onShutdown(null);
        DefaultNodeMainExecutor.newDefault().shutdownNodeMain(publisherNode);
        publisherNode.onShutdown(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<String>> getCurrentState()
    {
        Map<String, List<String>> map = super.getCurrentState();

        NavSatFix receivedMessage = publisherNode.getLoop().getMessage();

        if (receivedMessage != null)
        {
            map.put("sensor.gps.altitude", Arrays.asList(Double.toString(receivedMessage.getAltitude())));
        }

        map.put("sensor.sonar.altitude", Arrays.asList(Float.toString(publisherNode.getLoop().getValue())));

        return map;
    }
}
