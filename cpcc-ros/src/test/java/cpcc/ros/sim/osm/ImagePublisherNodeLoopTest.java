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
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.model.MediaType;
import org.ros.message.MessageFactory;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.utils.WGS84;
import sensor_msgs.NavSatFix;

@ExtendWith(MockServerExtension.class)
class ImagePublisherNodeLoopTest
{
    private static final String TOPIC_ROOT = "/topicRoot";
    private static final String INFO_TOPIC = TOPIC_ROOT + "/camera_info";
    private static final String IMAGE_TOPIC = TOPIC_ROOT + "/image";
    private static final String RESPONSE_DATA_01 = "this is just a test 01.";

    private Configuration config;
    private ConnectedNode connectedNode;
    private Publisher<sensor_msgs.Image> imagePublisher;
    private Publisher<sensor_msgs.CameraInfo> infoPublisher;
    private NavSatFix message;
    private File tileDir;
    private MessageFactory messageFactory;
    private sensor_msgs.Image imageMessage;
    private sensor_msgs.CameraInfo cameraInfoMessage;
    private File tempDirectory;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp(MockServerClient client) throws Exception
    {
        File path = new File("target/");
        tempDirectory = Files.createTempDirectory(path.toPath(), "tmp-test-ipnlt").toFile();
        assertThat(tempDirectory).exists();

        tileDir = File.createTempFile("tile", "dir");
        tileDir.delete();
        tileDir.mkdir();
        assertThat(tileDir).exists().isDirectory().canWrite();

        config = mock(Configuration.class);
        when(config.getTopicRoot()).thenReturn(TOPIC_ROOT);
        when(config.getCameraWidth()).thenReturn(640);
        when(config.getCameraHeight()).thenReturn(480);
        when(config.getTileWidth()).thenReturn(256);
        when(config.getTileHeight()).thenReturn(256);
        when(config.getOriginPosition()).thenReturn(new PolarCoordinate());
        when(config.getGeodeticSystem()).thenReturn(new WGS84());
        when(config.getCameraApertureAngle()).thenReturn(2.0);
        when(config.getTileCacheBaseDir()).thenReturn(tempDirectory.getAbsolutePath());
        when(config.getTileServerUrl())
            .thenReturn("http://localhost:" + client.getPort() + "/%1$d/%2$d/%3$d.png");

        imageMessage = mock(sensor_msgs.Image.class);
        cameraInfoMessage = mock(sensor_msgs.CameraInfo.class);

        messageFactory = mock(MessageFactory.class);
        when(messageFactory.newFromType(sensor_msgs.Image._TYPE)).thenReturn(imageMessage);
        when(messageFactory.newFromType(sensor_msgs.CameraInfo._TYPE)).thenReturn(cameraInfoMessage);

        imagePublisher = (Publisher<sensor_msgs.Image>) mock(Publisher.class);
        infoPublisher = (Publisher<sensor_msgs.CameraInfo>) mock(Publisher.class);

        connectedNode = mock(ConnectedNode.class);
        when(connectedNode.getTopicMessageFactory()).thenReturn(messageFactory);
        when(connectedNode.<sensor_msgs.Image> newPublisher(IMAGE_TOPIC, sensor_msgs.Image._TYPE))
            .thenReturn(imagePublisher);
        when(connectedNode.<sensor_msgs.CameraInfo> newPublisher(INFO_TOPIC, sensor_msgs.CameraInfo._TYPE))
            .thenReturn(infoPublisher);

        message = mock(NavSatFix.class);
        when(message.getAltitude()).thenReturn(8.76);

        client
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/0/0/0.png"))
            .respond(
                response()
                    .withStatusCode(200)
                    .withContentType(MediaType.PNG)
                    .withBody(RESPONSE_DATA_01));
    }

    @AfterEach
    void tearDown() throws IOException
    {
        FileUtils.deleteDirectory(tileDir);
    }

    @Test
    void shouldReceiveMessage() throws Exception
    {
        ImagePublisherNodeLoop sut = new ImagePublisherNodeLoop(config, connectedNode);

        sut.loop();
        assertThat(sut.getMessage()).isNull();

        sut.setMessage(message);
        assertThat(sut.getMessage()).isSameAs(message);

        sut.loop();

        verify(connectedNode).newPublisher(INFO_TOPIC, sensor_msgs.CameraInfo._TYPE);
        assertThat(sut.getMessage()).isSameAs(message);
        verify(imagePublisher).publish(imageMessage);
        verify(infoPublisher).publish(cameraInfoMessage);
    }
}
