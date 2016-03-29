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

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cpcc.ros.sim.AnonymousNodeMain;
import sensor_msgs.NavSatFix;

public class GpsListenerNodeTest
{
    private static final String LISTEN_TOPIC = "/topicRoot";

    private Logger logger;
    private GpsListenerNode sut;
    private Configuration config;
    private ConnectedNode connectedNode;
    private AnonymousNodeMain<sensor_msgs.NavSatFix> publisherNode;
    private Subscriber<sensor_msgs.NavSatFix> subscriber;

    @SuppressWarnings("unchecked")
    @BeforeMethod
    public void setUp()
    {
        logger = mock(Logger.class);

        config = mock(Configuration.class);
        when(config.getGpsTopic()).thenReturn(LISTEN_TOPIC);

        subscriber = (Subscriber<sensor_msgs.NavSatFix>) mock(Subscriber.class);

        connectedNode = mock(ConnectedNode.class);
        when(connectedNode.<sensor_msgs.NavSatFix> newSubscriber(LISTEN_TOPIC, sensor_msgs.NavSatFix._TYPE))
            .thenReturn(subscriber);

        publisherNode = mock(ImagePublisherNode.class);

        sut = new GpsListenerNode(logger, config, publisherNode);
    }

    @Test
    public void shouldStartNode() throws InterruptedException
    {
        sut.onStart(connectedNode);

        verify(connectedNode).newSubscriber(LISTEN_TOPIC, sensor_msgs.NavSatFix._TYPE);
        verify(subscriber).addMessageListener(publisherNode);
    }

    @Test
    public void shouldPublishMessage()
    {
        NavSatFix message = mock(NavSatFix.class);

        sut.onNewMessage(message);

        verify(publisherNode).onNewMessage(message);

        assertThat(sut.getReceivedMessage()).isSameAs(message);
    }

    @Test
    public void shouldHaveDefaultNodeName()
    {
        assertThat(sut.getDefaultNodeName().toString()).matches("anonymous_\\d+");
    }
}
