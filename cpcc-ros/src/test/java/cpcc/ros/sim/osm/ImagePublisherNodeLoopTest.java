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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.ros.message.MessageFactory;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.utils.WGS84;
import sensor_msgs.NavSatFix;

@PrepareForTest({ImagePublisherNodeLoop.class, HttpClientBuilder.class, Camera.class})
public class ImagePublisherNodeLoopTest extends PowerMockTestCase
{
    private static final String TOPIC_ROOT = "/topicRoot";
    private static final String INFO_TOPIC = TOPIC_ROOT + "/camera_info";
    private static final String IMAGE_TOPIC = TOPIC_ROOT + "/image";
    private static final String RESPONSE_DATA = "this is just a test.";

    private Configuration config;
    private ConnectedNode connectedNode;
    private Publisher<sensor_msgs.Image> imagePublisher;
    private Publisher<sensor_msgs.CameraInfo> infoPublisher;
    private NavSatFix message;
    private File tileDir;
    private MessageFactory messageFactory;
    private sensor_msgs.Image imageMessage;
    private sensor_msgs.CameraInfo cameraInfoMessage;
    private HttpEntity entity;
    private CloseableHttpResponse response;
    private CloseableHttpClient client;
    private HttpClientBuilder httpClientBuilderMock;
    private Logger logger;
    private StatusLine statusLine200ok;

    @SuppressWarnings("unchecked")
    @BeforeMethod
    public void setUp() throws Exception
    {
        logger = mock(Logger.class);

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
        when(config.getTileCacheBaseDir()).thenReturn("/tmp/lala");
        when(config.getTileServerUrl()).thenReturn("http://localhost:59999/nowhere");

        imageMessage = mock(sensor_msgs.Image.class);
        cameraInfoMessage = mock(sensor_msgs.CameraInfo.class);

        messageFactory = mock(MessageFactory.class);
        when(messageFactory.newFromType(sensor_msgs.Image._TYPE)).thenReturn(imageMessage);
        when(messageFactory.newFromType(sensor_msgs.CameraInfo._TYPE)).thenReturn(cameraInfoMessage);

        imagePublisher = (Publisher<sensor_msgs.Image>) mock(Publisher.class);
        infoPublisher = (Publisher<sensor_msgs.CameraInfo>) mock(Publisher.class);

        connectedNode = mock(ConnectedNode.class);
        when(connectedNode.getTopicMessageFactory()).thenReturn(messageFactory);
        when(connectedNode.<sensor_msgs.Image>newPublisher(IMAGE_TOPIC, sensor_msgs.Image._TYPE))
            .thenReturn(imagePublisher);
        when(connectedNode.<sensor_msgs.CameraInfo>newPublisher(INFO_TOPIC, sensor_msgs.CameraInfo._TYPE))
            .thenReturn(infoPublisher);

        message = mock(NavSatFix.class);
        when(message.getAltitude()).thenReturn(8.76);

        statusLine200ok = mock(StatusLine.class);
        when(statusLine200ok.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(statusLine200ok.getReasonPhrase()).thenReturn("200 OK");
        when(statusLine200ok.getProtocolVersion()).thenReturn(new ProtocolVersion("HTTP", 1, 0));

        entity = Mockito.mock(HttpEntity.class);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(RESPONSE_DATA.getBytes("UTF-8")));

        response = PowerMockito.mock(CloseableHttpResponse.class);
        PowerMockito.when(response.getStatusLine()).thenReturn(statusLine200ok);
        PowerMockito.when(response.getEntity()).thenReturn(entity);

        client = PowerMockito.mock(CloseableHttpClient.class);
        PowerMockito.doReturn(response).when(client).execute(any(HttpUriRequest.class));

        httpClientBuilderMock = mock(HttpClientBuilder.class);
        when(httpClientBuilderMock.build()).thenReturn(client);

        PowerMockito.mockStatic(HttpClientBuilder.class);
        when(HttpClientBuilder.create()).thenReturn(httpClientBuilderMock);
    }

    @AfterMethod
    public void tearDown() throws IOException
    {
        FileUtils.deleteDirectory(tileDir);
    }

    @Test
    public void shouldReceiveMessage() throws Exception
    {
        Camera camera = PowerMockito.mock(Camera.class);
        PowerMockito.when(camera.getImage(any(PolarCoordinate.class))).thenReturn(ArrayUtils.EMPTY_BYTE_ARRAY);
        PowerMockito.whenNew(Camera.class).withArguments(config).thenReturn(camera);

        ImagePublisherNodeLoop sut = new ImagePublisherNodeLoop(logger, config, connectedNode);

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

    @Test
    public void shouldHandleIOExceptionInCamera() throws Exception
    {
        Camera camera = PowerMockito.mock(Camera.class);
        PowerMockito.when(camera.getImage(any(PolarCoordinate.class))).thenThrow(new IOException("Thrown on purpose!"));
        PowerMockito.whenNew(Camera.class).withArguments(config).thenReturn(camera);

        ImagePublisherNodeLoop sut = new ImagePublisherNodeLoop(logger, config, connectedNode);
        sut.setMessage(message);
        sut.loop();

        verify(connectedNode).newPublisher(IMAGE_TOPIC, sensor_msgs.Image._TYPE);
        verify(connectedNode).newPublisher(INFO_TOPIC, sensor_msgs.CameraInfo._TYPE);
        verify(logger).error(eq("Can not get camera image."), any(IOException.class));
    }

    @Test
    public void shouldHandleMissingPosition() throws InterruptedException
    {
        ImagePublisherNodeLoop sut = new ImagePublisherNodeLoop(logger, config, connectedNode);

        sut.setMessage(null);
        sut.loop();

        verifyZeroInteractions(imagePublisher);
        verifyZeroInteractions(infoPublisher);
    }

    @Test
    public void shouldPublishImage() throws Exception
    {
        Camera camera = PowerMockito.mock(Camera.class);
        PowerMockito.when(camera.getImage(any(PolarCoordinate.class))).thenReturn(ArrayUtils.EMPTY_BYTE_ARRAY);
        PowerMockito.whenNew(Camera.class).withArguments(config).thenReturn(camera);

        ImagePublisherNodeLoop sut = new ImagePublisherNodeLoop(logger, config, connectedNode);
        sut.setMessage(message);
        sut.loop();

        verify(imagePublisher).publish(imageMessage);
        verify(infoPublisher).publish(cameraInfoMessage);
    }
}
