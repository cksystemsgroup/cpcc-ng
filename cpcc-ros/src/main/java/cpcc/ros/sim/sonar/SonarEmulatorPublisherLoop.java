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

import org.ros.concurrent.CancellableLoop;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import sensor_msgs.NavSatFix;
import cpcc.core.utils.ConfigUtils;

/**
 * Sonar Emulator Publisher Node
 */
public class SonarEmulatorPublisherLoop extends CancellableLoop
{
    private ConnectedNode connectedNode;
    private Publisher<std_msgs.Float32> publisher;
    private float value;
    private double origin;
    private NavSatFix message;

    /**
     * @param config the device configuration.
     * @param connectedNode the connected node.
     */
    public SonarEmulatorPublisherLoop(Map<String, List<String>> config, ConnectedNode connectedNode)
    {
        this.connectedNode = connectedNode;

        String topicRoot = config.get("topicRoot").get(0);
        origin = ConfigUtils.parseDouble(config, "origin", 0, 0);
        publisher = connectedNode.newPublisher(topicRoot, std_msgs.Float32._TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loop() throws InterruptedException
    {
        if (message != null)
        {
            std_msgs.Float32 sonarMessage =
                connectedNode.getTopicMessageFactory().newFromType(std_msgs.Float32._TYPE);

            value = (float) (message.getAltitude() - origin);
            sonarMessage.setData(value);
            publisher.publish(sonarMessage);
        }

        Thread.sleep(200);
    }

    /**
     * @return the current value.
     */
    public float getValue()
    {
        return value;
    }

    /**
     * @param newMessage new message from the GPS receiver.
     */
    public void onNewMessage(NavSatFix newMessage)
    {
        this.message = newMessage;
    }

    /**
     * @return the last message from the GPS receiver.
     */
    public NavSatFix getMessage()
    {
        return message;
    }
}
