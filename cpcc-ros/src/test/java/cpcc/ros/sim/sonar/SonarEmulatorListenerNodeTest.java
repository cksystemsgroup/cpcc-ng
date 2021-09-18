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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import sensor_msgs.NavSatFix;

class SonarEmulatorListenerNodeTest
{
    private static final String LISTEN_TOPIC = "/topicRoot";

    private SonarEmulatorListenerNode sut;
    private Map<String, List<String>> config;
    private ConnectedNode connectedNode;
    private MessageListener<NavSatFix> listenerNode;
    private Subscriber<sensor_msgs.NavSatFix> subscriber;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp()
    {
        config = new HashMap<String, List<String>>();
        config.put("gps", Arrays.asList(LISTEN_TOPIC));

        subscriber = (Subscriber<sensor_msgs.NavSatFix>) mock(Subscriber.class);

        connectedNode = mock(ConnectedNode.class);
        when(connectedNode.<sensor_msgs.NavSatFix> newSubscriber(LISTEN_TOPIC, sensor_msgs.NavSatFix._TYPE))
            .thenReturn(subscriber);

        listenerNode = (MessageListener<NavSatFix>) mock(MessageListener.class);

        sut = new SonarEmulatorListenerNode(config, listenerNode);
    }

    @Test
    void shouldStartNode() throws InterruptedException
    {
        sut.onStart(connectedNode);

        verify(connectedNode).newSubscriber(LISTEN_TOPIC, sensor_msgs.NavSatFix._TYPE);
        verify(subscriber).addMessageListener(listenerNode);
    }
}
