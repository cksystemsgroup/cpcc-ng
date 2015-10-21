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

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
import org.easymock.EasyMock;
import org.mockito.Mockito;
import org.powermock.api.easymock.PowerMock;
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

import sensor_msgs.NavSatFix;
import cpcc.core.utils.PolarCoordinate;
import cpcc.core.utils.WGS84;

// @RunWith(PowerMockRunner.class)
@PrepareForTest({ImagePublisherNodeLoop.class, HttpClientBuilder.class, Camera.class})
public class ImagePublisherNodeLoopTest extends PowerMockTestCase
{
    private static final String TOPIC_ROOT = "/topicRoot";
    private static final String INFO_TOPIC = TOPIC_ROOT + "/camera_info";
    private static final String IMAGE_TOPIC = TOPIC_ROOT + "/image";

    private ImagePublisherNodeLoop sut;
    private Configuration config;
    private ConnectedNode connectedNode;
    private Publisher<sensor_msgs.Image> publisher1;
    private Publisher<sensor_msgs.CameraInfo> publisher2;
    private NavSatFix message;
    private File tileDir;
    private MessageFactory messageFactory;
    private sensor_msgs.Image imageMessage;
    private sensor_msgs.CameraInfo cameraInfoMessage;
    private HttpEntity entity;
    private CloseableHttpResponse response;
    private CloseableHttpClient client;
    private HttpClientBuilder httpClientBuilderMock;
    private String responseData = "this is just a test.";
    private Camera camera;
    private Logger logger;

    private StatusLine statusLine200ok = new StatusLine()
    {
        @Override
        public int getStatusCode()
        {
            return HttpStatus.SC_OK;
        }

        @Override
        public String getReasonPhrase()
        {
            return "200 OK";
        }

        @Override
        public ProtocolVersion getProtocolVersion()
        {
            return new ProtocolVersion("HTTP", 1, 0);
        }
    };

    @SuppressWarnings("unchecked")
    @BeforeMethod
    public void setUp() throws Exception
    {
        logger = mock(Logger.class);

        tileDir = File.createTempFile("tile", "dir");
        tileDir.delete();
        tileDir.mkdir();
        assertThat(tileDir).exists().isDirectory();

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

        publisher1 = (Publisher<sensor_msgs.Image>) mock(Publisher.class);
        publisher2 = (Publisher<sensor_msgs.CameraInfo>) mock(Publisher.class);

        connectedNode = mock(ConnectedNode.class);
        when(connectedNode.getTopicMessageFactory()).thenReturn(messageFactory);
        when(connectedNode.<sensor_msgs.Image>
            newPublisher(IMAGE_TOPIC, sensor_msgs.Image._TYPE)).thenReturn(publisher1);
        when(connectedNode.<sensor_msgs.CameraInfo>
            newPublisher(INFO_TOPIC, sensor_msgs.CameraInfo._TYPE)).thenReturn(publisher2);

        message = mock(NavSatFix.class);
        when(message.getAltitude()).thenReturn(8.76);

        entity = Mockito.mock(HttpEntity.class);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(responseData.getBytes("UTF-8")));

        response = PowerMockito.mock(CloseableHttpResponse.class);
        PowerMockito.when(response.getStatusLine()).thenReturn(statusLine200ok);
        PowerMockito.when(response.getEntity()).thenReturn(entity);

        client = PowerMockito.mock(CloseableHttpClient.class);
        PowerMockito.doReturn(response).when(client).execute((HttpUriRequest) anyObject());

        httpClientBuilderMock = PowerMock.createMock(HttpClientBuilder.class);

        PowerMock.mockStatic(HttpClientBuilder.class);
        EasyMock.expect(HttpClientBuilder.create()).andReturn(httpClientBuilderMock).anyTimes();
        PowerMock.replay(HttpClientBuilder.class);
        EasyMock.expect(httpClientBuilderMock.build()).andReturn(client).anyTimes();
        PowerMock.replay(HttpClientBuilder.class, httpClientBuilderMock);
    }

    @AfterMethod
    public void tearDown() throws IOException
    {
        FileUtils.deleteDirectory(tileDir);
    }

    @Test
    public void shouldReceiveMessage() throws Exception
    {
        camera = PowerMock.createMockAndExpectNew(Camera.class, config);
        EasyMock.expect(camera.getImage(EasyMock.isA(PolarCoordinate.class))).andReturn(ArrayUtils.EMPTY_BYTE_ARRAY);
        PowerMock.replay(Camera.class, camera);

        sut = new ImagePublisherNodeLoop(logger, config, connectedNode);

        sut.loop();
        assertThat(sut.getMessage()).isNull();

        sut.setMessage(message);
        assertThat(sut.getMessage()).isSameAs(message);

        sut.loop();

        verify(connectedNode).newPublisher(IMAGE_TOPIC, sensor_msgs.Image._TYPE);
        verify(connectedNode).newPublisher(INFO_TOPIC, sensor_msgs.CameraInfo._TYPE);
        assertThat(sut.getMessage()).isSameAs(message);
        verify(publisher1).publish(imageMessage);
        verify(publisher2).publish(cameraInfoMessage);
    }

    @Test
    public void shouldHandleIOExceptionInCamera() throws Exception
    {
        camera = PowerMock.createMockAndExpectNew(Camera.class, config);
        EasyMock.expect(camera.getImage(EasyMock.isA(PolarCoordinate.class))).andThrow(new IOException());
        PowerMock.replay(camera, Camera.class);

        sut = new ImagePublisherNodeLoop(logger, config, connectedNode);

        sut.setMessage(message);
        sut.loop();

        verify(connectedNode).newPublisher(IMAGE_TOPIC, sensor_msgs.Image._TYPE);
        verify(connectedNode).newPublisher(INFO_TOPIC, sensor_msgs.CameraInfo._TYPE);
        verify(logger).error(eq("Can not get camera image."), any(IOException.class));
    }
}
