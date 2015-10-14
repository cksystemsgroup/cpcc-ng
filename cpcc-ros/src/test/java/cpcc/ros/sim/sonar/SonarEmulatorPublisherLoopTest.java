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

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ros.message.MessageFactory;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import sensor_msgs.NavSatFix;

public class SonarEmulatorPublisherLoopTest
{
    private static final String TOPIC_ROOT = "/topicRoot";

    private SonarEmulatorPublisherLoop sut;
    private Map<String, List<String>> config;
    private ConnectedNode connectedNode;
    private NavSatFix message;
    private std_msgs.Float32 newFloatMessage;
    private MessageFactory messageFactory;
    private Publisher<std_msgs.Float32> publisher;

    @SuppressWarnings("unchecked")
    @BeforeMethod
    public void setUp()
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

        sut = new SonarEmulatorPublisherLoop(config, connectedNode);

        verify(connectedNode).newPublisher(TOPIC_ROOT, std_msgs.Float32._TYPE);
    }

    @Test
    public void shouldReceiveMessage() throws InterruptedException
    {
        sut.loop();
        assertThat(sut.getMessage()).isNull();

        sut.onNewMessage(message);
        sut.loop();

        assertThat(sut.getMessage()).isSameAs(message);
        assertThat(sut.getValue()).isEqualTo(8.76f - 3.27f, offset(1E-4f));
        verify(publisher).publish(newFloatMessage);
    }
}
