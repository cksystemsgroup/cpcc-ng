// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

import java.util.List;
import java.util.Map;

import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.slf4j.Logger;

import sensor_msgs.NavSatFix;
import cpcc.ros.sim.AnonymousNodeMain;

/**
 * Sonar Emulator Publisher Node
 */
public class SonarEmulatorPublisherNode extends AnonymousNodeMain<sensor_msgs.NavSatFix>
{
    private Logger logger;
    private Map<String, List<String>> config;
    private SonarEmulatorPublisherLoop loop;

    /**
     * @param logger the application logger.
     * @param config the device configuration
     */
    public SonarEmulatorPublisherNode(Logger logger, Map<String, List<String>> config)
    {
        this.logger = logger;
        this.config = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(final ConnectedNode connectedNode)
    {
        logger.info("onStart");
        loop = new SonarEmulatorPublisherLoop(config, connectedNode);
        connectedNode.executeCancellableLoop(loop);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onShutdown(Node node)
    {
        loop.cancel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNewMessage(NavSatFix message)
    {
        loop.onNewMessage(message);
    }

    /**
     * @return the publisher loop.
     */
    public SonarEmulatorPublisherLoop getLoop()
    {
        return loop;
    }
}
