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
import sensor_msgs.NavSatStatus;

/**
 * GpsSensorAdapterTest implementation.
 */
public class GpsSensorAdapterTest
{
    private GpsSensorAdapter sut;
    private ConnectedNode connectedNode;
    private RosTopic topic;
    private Subscriber<Object> altimeterSubscriber;
    private sensor_msgs.NavSatFix message1;
    private NavSatStatus status;

    @SuppressWarnings("unchecked")
    @BeforeMethod
    public void setUp()
    {
        status = mock(NavSatStatus.class);
        when(status.getStatus()).thenReturn(sensor_msgs.NavSatStatus.STATUS_FIX);

        message1 = mock(sensor_msgs.NavSatFix.class);
        when(message1.getAltitude()).thenReturn(0.678);
        when(message1.getLatitude()).thenReturn(47.123);
        when(message1.getLongitude()).thenReturn(13.789);
        when(message1.getPositionCovariance()).thenReturn(new double[]{1, 2, 3});
        when(message1.getPositionCovarianceType()).thenReturn((byte) 3);

        when(message1.getStatus()).thenReturn(status);

        topic = mock(RosTopic.class);
        when(topic.getName()).thenReturn("topicOne");

        altimeterSubscriber = mock(Subscriber.class);

        connectedNode = mock(ConnectedNode.class);
        when(connectedNode.newSubscriber(topic.getName(), sensor_msgs.NavSatFix._TYPE)).thenReturn(altimeterSubscriber);

        sut = new GpsSensorAdapter();
        sut.setTopic(topic);
    }

    @Test
    public void shouldHaveCorrectType()
    {
        assertThat(sut.getType()).isEqualTo(SensorType.GPS_RECEIVER);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void shouldHaveCurrentState()
    {
        Map<String, List<String>> actual = sut.getCurrentState();

        assertThat(actual.keySet()).containsExactly("node.state");
        assertThat(actual.get("node.state")).containsExactly(RosNodeState.INITIAL.name());

        sut.onStart(connectedNode);

        verify(connectedNode).newSubscriber(topic.getName(), sensor_msgs.NavSatFix._TYPE);

        ArgumentCaptor<MessageListener> captor = ArgumentCaptor.forClass(MessageListener.class);

        verify(altimeterSubscriber).addMessageListener(captor.capture());

        MessageListener<sensor_msgs.NavSatFix> listener = captor.getValue();

        listener.onNewMessage(message1);

        actual = sut.getCurrentState();

        assertThat(actual.keySet())
            .hasSize(6)
            .contains("node.state", "sensor.gps.position.covariance.type", "sensor.gps.position"
                , "sensor.gps.position.covariance", "sensor.gps.status.service", "sensor.gps.status.status");

        assertThat(actual.get("node.state")).containsExactly(RosNodeState.RUNNING.name());
        assertThat(actual.get("sensor.gps.position.covariance.type")).containsExactly("3");
        assertThat(actual.get("sensor.gps.position")).containsExactly("47.12300000", "13.78900000", "0.678");
        assertThat(actual.get("sensor.gps.position.covariance")).containsExactly("[1.0, 2.0, 3.0]");
        assertThat(actual.get("sensor.gps.status.service")).containsExactly("0");
        assertThat(actual.get("sensor.gps.status.status")).containsExactly("0");
    }

    @Test
    public void shouldThrowExceptionOnSetValue()
    {
        try
        {
            sut.setValue(null);
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        }
        catch (IllegalStateException e)
        {
            assertThat(e).hasMessage(null);
        }
    }

    @Test
    public void shouldSetPosition()
    {
        sensor_msgs.NavSatFix position = mock(sensor_msgs.NavSatFix.class);

        sut.setPosition(position);
        assertThat(sut.getPosition()).isSameAs(position);
        assertThat(sut.getValue()).isSameAs(position);

        position = null;

        sut.setPosition(position);
        assertThat(sut.getPosition()).isNull();
        assertThat(sut.getValue()).isNull();
    }

}
