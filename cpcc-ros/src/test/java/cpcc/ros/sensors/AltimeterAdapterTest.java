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

package cpcc.ros.sensors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.mockito.ArgumentCaptor;
import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cpcc.ros.base.RosNodeState;
import cpcc.ros.base.RosTopic;

/**
 * AltimeterAdapterTest implementation.
 */
public class AltimeterAdapterTest
{
    private AltimeterAdapter sut;
    private ConnectedNode connectedNode;
    private RosTopic topic;
    private Subscriber<Object> altimeterSubscriber;
    private std_msgs.Float32 message1;

    @SuppressWarnings("unchecked")
    @BeforeMethod
    public void setUp()
    {
        message1 = mock(std_msgs.Float32.class);
        when(message1.getData()).thenReturn(3.456f);

        topic = mock(RosTopic.class);
        when(topic.getName()).thenReturn("topicOne");

        altimeterSubscriber = mock(Subscriber.class);

        connectedNode = mock(ConnectedNode.class);
        when(connectedNode.newSubscriber(topic.getName(), std_msgs.Float32._TYPE)).thenReturn(altimeterSubscriber);

        sut = new AltimeterAdapter();
        sut.setTopic(topic);
    }

    @Test
    public void shouldHaveCorrectType()
    {
        assertThat(sut.getType()).isEqualTo(SensorType.ALTIMETER);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void shouldHaveCurrentState()
    {
        Map<String, List<String>> actual = sut.getCurrentState();

        assertThat(actual.keySet()).containsExactly("node.state");
        assertThat(actual.get("node.state")).containsExactly(RosNodeState.INITIAL.name());

        sut.onStart(connectedNode);

        verify(connectedNode).newSubscriber(topic.getName(), std_msgs.Float32._TYPE);

        ArgumentCaptor<MessageListener> captor = ArgumentCaptor.forClass(MessageListener.class);

        verify(altimeterSubscriber).addMessageListener(captor.capture());

        MessageListener<std_msgs.Float32> listener = captor.getValue();

        listener.onNewMessage(message1);

        actual = sut.getCurrentState();

        assertThat(actual.keySet()).hasSize(3).contains("node.state", "sensor.float.value", "sensor.altitude");
        assertThat(actual.get("node.state")).containsExactly(RosNodeState.RUNNING.name());
        assertThat(actual.get("sensor.float.value")).containsExactly("3.456");
        assertThat(actual.get("sensor.altitude")).containsExactly("3.456");
    }
}
