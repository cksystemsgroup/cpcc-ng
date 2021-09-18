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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ros.message.MessageFactory;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import sensor_msgs.NavSatFix;

class SonarEmulatorPublisherNodeTest
{
    private static final String TOPIC_ROOT = "/topicRoot";

    private Map<String, List<String>> config;
    private ConnectedNode connectedNode;
    private std_msgs.Float32 newFloatMessage;
    private MessageFactory messageFactory;
    private Publisher<std_msgs.Float32> publisher;
    private NavSatFix message;
    private SonarEmulatorPublisherNode sut;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp()
    {
        config = new HashMap<String, List<String>>();
        config.put("topicRoot", Arrays.asList(TOPIC_ROOT));
        config.put("origin", Arrays.asList("3.27"));

        newFloatMessage = mock(std_msgs.Float32.class);

        messageFactory = mock(MessageFactory.class);
        when(messageFactory.newFromType(std_msgs.Float32._TYPE)).thenReturn(newFloatMessage);

        publisher = (Publisher<std_msgs.Float32>) mock(Publisher.class);

        connectedNode = mock(ConnectedNode.class);
        when(connectedNode.getTopicMessageFactory()).thenReturn(messageFactory);
        when(connectedNode.<std_msgs.Float32> newPublisher(TOPIC_ROOT, std_msgs.Float32._TYPE)).thenReturn(publisher);

        message = mock(NavSatFix.class);
        when(message.getAltitude()).thenReturn(8.76);

        sut = new SonarEmulatorPublisherNode(config);
    }

    @Test
    void shouldStartNode() throws InterruptedException
    {
        sut.onStart(connectedNode);

        verify(connectedNode).newPublisher(TOPIC_ROOT, std_msgs.Float32._TYPE);
        verify(connectedNode).executeCancellableLoop(sut.getLoop());
    }

    @Test
    void shouldReceiveMessage() throws InterruptedException
    {
        sut.onStart(connectedNode);
        sut.onNewMessage(message);

        assertThat(sut.getLoop().getMessage()).isSameAs(message);
    }
}
