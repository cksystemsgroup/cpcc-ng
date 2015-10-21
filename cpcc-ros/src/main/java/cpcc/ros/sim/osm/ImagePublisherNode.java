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

package cpcc.ros.sim.osm;

import org.ros.node.ConnectedNode;
import org.slf4j.Logger;

import sensor_msgs.NavSatFix;
import cpcc.ros.sim.AnonymousNodeMain;

/**
 * ImagePublisherNode
 */
public class ImagePublisherNode extends AnonymousNodeMain<sensor_msgs.NavSatFix>
{
    private Logger logger;
    private Configuration config;
    private ImagePublisherNodeLoop loop;

    /**
     * @param logger the application logger.
     * @param config the configuration.
     */
    public ImagePublisherNode(Logger logger, Configuration config)
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

        loop = new ImagePublisherNodeLoop(logger, config, connectedNode);
        connectedNode.executeCancellableLoop(loop);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNewMessage(NavSatFix message)
    {
        super.onNewMessage(message);
        loop.setMessage(message);
    }

    /**
     * @return the ROS loop instance.
     */
    public ImagePublisherNodeLoop getLoop()
    {
        return loop;
    }
}
