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
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import cpcc.ros.base.RosTopic;
import std_msgs.Float32;

/**
 * Float32SensorAdapterTest implementation.
 */
class Float32SensorAdapterTest
{
    private Float32SensorAdapter sut;
    private ConnectedNode connectedNode;
    private RosTopic topic;
    private Subscriber<Object> float32Subscriber;
    private Float32 message1;
    private Float32 message2;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp()
    {
        message1 = mock(std_msgs.Float32.class);
        when(message1.getData()).thenReturn(3.456f);

        message2 = mock(std_msgs.Float32.class);
        when(message2.getData()).thenReturn(2.789f);

        topic = mock(RosTopic.class);
        when(topic.getName()).thenReturn("topicOne");

        sut = new Float32SensorAdapter();

        sut.setTopic(topic);

        connectedNode = mock(ConnectedNode.class);
        float32Subscriber = mock(Subscriber.class);
        when(connectedNode.newSubscriber(topic.getName(), std_msgs.Float32._TYPE)).thenReturn(float32Subscriber);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    void shouldRegisterMessageListenerOnStartup()
    {
        sut.onStart(connectedNode);

        verify(connectedNode).newSubscriber(topic.getName(), std_msgs.Float32._TYPE);

        ArgumentCaptor<MessageListener> captor = ArgumentCaptor.forClass(MessageListener.class);

        verify(float32Subscriber).addMessageListener(captor.capture());

        MessageListener<std_msgs.Float32> listener = captor.getValue();

        listener.onNewMessage(message1);
        assertThat(sut.getValue().getData()).isEqualTo(message1.getData(), offset(1E-5f));

        listener.onNewMessage(message2);
        assertThat(sut.getValue().getData()).isEqualTo(message2.getData(), offset(1E-5f));
    }

    @Test
    void shouldGetCurrentStateOnUninitializedNode()
    {
        Map<String, List<String>> actual = sut.getCurrentState();

        assertThat(actual.keySet()).containsExactly("node.state");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    void shouldGetCurrentStateOnInitializedNode()
    {
        sut.onStart(connectedNode);
        ArgumentCaptor<MessageListener> captor = ArgumentCaptor.forClass(MessageListener.class);
        verify(float32Subscriber).addMessageListener(captor.capture());
        MessageListener<std_msgs.Float32> listener = captor.getValue();

        listener.onNewMessage(message1);

        Map<String, List<String>> actual = sut.getCurrentState();

        assertThat(actual.keySet()).hasSize(2).contains("node.state", "sensor.float.value");

        assertThat(actual.get("node.state")).containsExactly("RUNNING");
        assertThat(actual.get("sensor.float.value")).containsExactly("3.456");
    }

    @Test
    void shouldReturnCorrectSensorType()
    {
        SensorType actual = sut.getType();

        assertThat(actual).isEqualTo(SensorType.FLOAT_SENSOR);
    }

    @Test
    void shouldThrowExceptionOnSetValue()
    {
        try
        {
            sut.setValue(message1);
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        }
        catch (IllegalStateException e)
        {
            assertThat(e.getMessage()).isNull();
        }
    }
}
