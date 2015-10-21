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

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import org.slf4j.Logger;

import sensor_msgs.NavSatFix;
import cpcc.ros.sim.AnonymousNodeMain;

/**
 * Sonar Emulator Listener Node
 */
public class SonarEmulatorListenerNode extends AnonymousNodeMain<sensor_msgs.NavSatFix>
{
    private Logger logger;
    private MessageListener<NavSatFix> listenerNode;
    private String listenTopic;

    /**
     * @param logger the application logger.
     * @param config the device configuration.
     * @param listenerNode the listener node.
     */
    public SonarEmulatorListenerNode(Logger logger, Map<String, List<String>> config,
        MessageListener<NavSatFix> listenerNode)
    {
        this.logger = logger;
        this.listenerNode = listenerNode;

        listenTopic = config.get("gps").get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(ConnectedNode connectedNode)
    {
        logger.info("onStart()");

        Subscriber<sensor_msgs.NavSatFix> subscriber =
            connectedNode.newSubscriber(listenTopic, sensor_msgs.NavSatFix._TYPE);

        subscriber.addMessageListener(listenerNode);
    }
}
