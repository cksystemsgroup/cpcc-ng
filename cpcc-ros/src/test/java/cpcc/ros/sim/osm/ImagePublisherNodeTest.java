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

package cpcc.ros.sim.osm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.utils.WGS84;
import sensor_msgs.NavSatFix;

class ImagePublisherNodeTest
{
    private static final String TOPIC_ROOT = "/topicRoot";
    private static final String INFO_TOPIC = TOPIC_ROOT + "/camera_info";
    private static final String IMAGE_TOPIC = TOPIC_ROOT + "/image";

    private Configuration config;
    private ConnectedNode connectedNode;
    private Publisher<sensor_msgs.Image> publisher1;
    private Publisher<sensor_msgs.CameraInfo> publisher2;
    private NavSatFix message;
    private ImagePublisherNode sut;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp()
    {
        config = mock(Configuration.class);
        when(config.getTopicRoot()).thenReturn(TOPIC_ROOT);
        when(config.getCameraWidth()).thenReturn(640);
        when(config.getCameraHeight()).thenReturn(480);
        when(config.getTileWidth()).thenReturn(256);
        when(config.getTileHeight()).thenReturn(256);
        when(config.getOriginPosition()).thenReturn(new PolarCoordinate());
        when(config.getGeodeticSystem()).thenReturn(new WGS84());
        when(config.getCameraApertureAngle()).thenReturn(2.0);

        publisher1 = (Publisher<sensor_msgs.Image>) mock(Publisher.class);
        publisher2 = (Publisher<sensor_msgs.CameraInfo>) mock(Publisher.class);

        connectedNode = mock(ConnectedNode.class);
        when(connectedNode.<sensor_msgs.Image> newPublisher(IMAGE_TOPIC, sensor_msgs.Image._TYPE))
            .thenReturn(publisher1);
        when(connectedNode.<sensor_msgs.CameraInfo> newPublisher(INFO_TOPIC, sensor_msgs.CameraInfo._TYPE))
            .thenReturn(publisher2);

        message = mock(NavSatFix.class);
        when(message.getAltitude()).thenReturn(8.76);

        sut = new ImagePublisherNode(config);
    }

    @Test
    void shouldStartNode() throws InterruptedException
    {
        sut.onStart(connectedNode);

        verify(connectedNode).newPublisher(IMAGE_TOPIC, sensor_msgs.Image._TYPE);
        verify(connectedNode).newPublisher(INFO_TOPIC, sensor_msgs.CameraInfo._TYPE);
        verify(connectedNode).executeCancellableLoop(sut.getLoop());
    }

    @Test
    void shouldReceiveMessage() throws InterruptedException
    {
        sut.onNewMessage(message);

        assertThat(sut.getReceivedMessage()).isSameAs(message);
        assertThat(sut.getLoop()).isNull();

        sut.onStart(connectedNode);
        sut.onNewMessage(message);

        assertThat(sut.getLoop().getMessage()).isSameAs(message);
    }
}
