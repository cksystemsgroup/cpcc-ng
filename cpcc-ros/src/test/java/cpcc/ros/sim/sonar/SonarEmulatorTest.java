// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomUtils;
import org.ros.RosCore;
import org.ros.address.InetAddressFactory;
import org.ros.concurrent.CancellableLoop;
import org.ros.node.ConnectedNode;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.topic.Publisher;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cpcc.ros.sim.AnonymousNodeMain;
import sensor_msgs.NavSatFix;

/**
 * SonarEmulatorTest implementation.
 */
public class SonarEmulatorTest
{
    private Logger logger;
    private SonarEmulator sut;
    private Map<String, List<String>> config;
    private NodeConfiguration nodeConfiguration;
    private RosCore rosCore;
    private AnonymousNodeMain<NavSatFix> senderNode;

    @BeforeMethod
    public void setUp() throws URISyntaxException, InterruptedException
    {
        int port = RandomUtils.nextInt(20000, 40000);
        String hostName = InetAddressFactory.newNonLoopback().getHostName();

        rosCore = RosCore.newPublic(hostName, port);
        rosCore.start();
        rosCore.awaitStart();

        nodeConfiguration = NodeConfiguration.newPublic(hostName, rosCore.getMasterServer().getUri());

        logger = mock(Logger.class);

        config = new HashMap<>();
        config.put("gps", Arrays.asList("/gpsrcv"));
        config.put("origin", Arrays.asList("0"));

        sut = new SonarEmulator(logger);
        sut.setTopicRoot("/topicRoot");
        sut.setConfig(config);
        sut.setNodeConfiguration(nodeConfiguration);
        sut.start();

        TimeUnit.SECONDS.sleep(1);
    }

    @AfterMethod
    public void tearDown() throws InterruptedException
    {
        if (senderNode != null)
        {
            System.out.println("Sender node: shutdown");
            DefaultNodeMainExecutor.newDefault().shutdownNodeMain(senderNode);
        }

        System.out.println("SUT: shutdown");
        sut.shutdown();
        TimeUnit.SECONDS.sleep(1);

        System.out.println("ROS core: shutdown");
        rosCore.shutdown();
        System.out.println("ROS core: shutdown done");
    }

    private void setupMessageSender() throws InterruptedException
    {
        senderNode = new AnonymousNodeMain<sensor_msgs.NavSatFix>()
        {
            private Publisher<sensor_msgs.NavSatFix> publisher;
            private CancellableLoop loop;

            @Override
            public void onStart(ConnectedNode connectedNode)
            {
                publisher = connectedNode.newPublisher("/gpsrcv", sensor_msgs.NavSatFix._TYPE);
                loop = new CancellableLoop()
                {
                    @Override
                    protected void loop() throws InterruptedException
                    {
                        publisher.publish(getReceivedMessage());
                        TimeUnit.MILLISECONDS.sleep(200);
                    }
                };

                NavSatFix message = connectedNode.getTopicMessageFactory().newFromType(sensor_msgs.NavSatFix._TYPE);
                message.setAltitude(123.66);
                onNewMessage(message);

                connectedNode.executeCancellableLoop(loop);
            }

            @Override
            public void onShutdown(Node node)
            {
                loop.cancel();
            }
        };

        DefaultNodeMainExecutor.newDefault().execute(senderNode, nodeConfiguration);
    }

    @Test
    public void shouldProduceStateWithMessagesReceived() throws InterruptedException
    {
        setupMessageSender();

        TimeUnit.SECONDS.sleep(1);

        Map<String, List<String>> actual = sut.getCurrentState();

        System.out.println("Actual: " + actual);

        assertThat(actual.keySet())
            .describedAs("values")
            .hasSize(5)
            .contains("config.topicRoot", "config.gps", "config.origin", "sensor.sonar.altitude",
                "sensor.gps.altitude");

        assertThat(actual.get("config.topicRoot"))
            .describedAs("config.topicRoot")
            .hasSize(1)
            .contains("/topicRoot");

        assertThat(actual.get("config.gps"))
            .describedAs("config.gps")
            .hasSize(1)
            .contains("/gpsrcv");

        assertThat(actual.get("config.origin"))
            .describedAs("config.origin")
            .hasSize(1)
            .contains("0");

        assertThat(actual.get("sensor.sonar.altitude"))
            .describedAs("sensor.sonar.altitude")
            .hasSize(1)
            .contains("123.66");

        assertThat(actual.get("sensor.gps.altitude"))
            .describedAs("sensor.gps.altitude")
            .hasSize(1)
            .contains("123.66");
    }

    @Test
    public void shouldProduceStateWithoutMessagesReceived()
    {
        Map<String, List<String>> actual = sut.getCurrentState();

        System.out.println("Actual: " + actual);

        assertThat(actual.keySet())
            .hasSize(4)
            .contains("config.topicRoot", "config.gps", "config.origin", "sensor.sonar.altitude");

        assertThat(actual.get("config.topicRoot"))
            .hasSize(1)
            .contains("/topicRoot");

        assertThat(actual.get("config.gps"))
            .hasSize(1)
            .contains("/gpsrcv");

        assertThat(actual.get("config.origin"))
            .hasSize(1)
            .contains("0");

        assertThat(actual.get("sensor.sonar.altitude"))
            .hasSize(1)
            .contains("0.0");
    }

}
