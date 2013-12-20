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
package at.uni_salzburg.cs.cpcc.ros.sim;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.ros.concurrent.CancellableLoop;
import org.ros.node.ConnectedNode;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sensor_msgs.NavSatFix;
import at.uni_salzburg.cs.cpcc.core.utils.ConfigUtils;

/**
 * SonarEmulator
 */
public class SonarEmulator extends AbstractRosNodeGroup
{
    private static final Logger LOG = LoggerFactory.getLogger(SonarEmulator.class);
    private AnonymousNodeMain<sensor_msgs.NavSatFix> publisherNode;
    private AnonymousNodeMain<sensor_msgs.NavSatFix> listenerNode;
    private double origin;
    private String listenTopic;
    private float value;
    private NavSatFix receivedMessage;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start()
    {
        LOG.info("start()");

        origin = ConfigUtils.parseDouble(getConfig(), "origin", 0, 0);
        listenTopic = getConfig().get("gps").get(0);

        getConfig().put("topicRoot", Arrays.asList(getTopicRoot()));

        publisherNode = new AnonymousNodeMain<sensor_msgs.NavSatFix>()
        {

            /**
             * {@inheritDoc}
             */
            @Override
            public void onStart(final ConnectedNode connectedNode)
            {
                LOG.info("onStart");

                final Publisher<std_msgs.Float32> publisher =
                    connectedNode.newPublisher(getTopicRoot(), std_msgs.Float32._TYPE);

                CancellableLoop loop = new CancellableLoop()
                {
                    @Override
                    protected void loop() throws InterruptedException
                    {
                        if (receivedMessage == null)
                        {
                            return;
                        }
                        std_msgs.Float32 message =
                            connectedNode.getTopicMessageFactory().newFromType(std_msgs.Float32._TYPE);
                        value = (float) (receivedMessage.getAltitude() - origin);
                        message.setData(value);
                        publisher.publish(message);
                        Thread.sleep(200);
                    }
                };

                connectedNode.executeCancellableLoop(loop);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void onNewMessage(NavSatFix message)
            {
                receivedMessage = message;
            }
        };

        listenerNode = new AnonymousNodeMain<sensor_msgs.NavSatFix>()
        {

            /**
             * {@inheritDoc}
             */
            @Override
            public void onStart(ConnectedNode connectedNode)
            {
                LOG.info("onStart()");

                Subscriber<sensor_msgs.NavSatFix> subscriber =
                    connectedNode.newSubscriber(listenTopic, sensor_msgs.NavSatFix._TYPE);

                subscriber.addMessageListener(publisherNode);
            }
        };

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
        DefaultNodeMainExecutor.newDefault().shutdownNodeMain(publisherNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<String>> getCurrentState()
    {
        Map<String, List<String>> map = super.getCurrentState();

        if (receivedMessage != null)
        {
            map.put("sensor.gps.altitude", Arrays.asList(Double.toString(receivedMessage.getAltitude())));
        }
        map.put("sensor.sonar.altitude", Arrays.asList(Float.toString(value)));

        return map;
    }
}
