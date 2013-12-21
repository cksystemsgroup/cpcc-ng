/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.commons.services.image;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Dimension;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.ros.internal.message.Message;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import sensor_msgs.Image;
import at.uni_salzburg.cs.cpcc.commons.services.image.ImageTagService;
import at.uni_salzburg.cs.cpcc.commons.services.image.ImageTagServiceImpl;
import at.uni_salzburg.cs.cpcc.ros.base.AbstractRosAdapter;
import at.uni_salzburg.cs.cpcc.ros.base.RosTopic;
import at.uni_salzburg.cs.cpcc.ros.sensors.GpsSensorAdapter;
import at.uni_salzburg.cs.cpcc.ros.sensors.ImageProvider;

public class ImageTagServiceTest
{
    Request request = mock(Request.class);
    RequestGlobals requestGlobals = mock(RequestGlobals.class);
    Messages messages = mock(Messages.class);
    ImageTagService imageTagService;
    private AbstractRosAdapter abstracAdapter1 = mock(AbstractRosAdapter.class);
    private AbstractRosAdapter abstracAdapter2 = mock(GpsSensorAdapter.class);
    private Image image1 = mock(Image.class);
    private Image image2 = mock(Image.class);
    private ImageProvider imageAdapter1 = mock(ImageProvider.class);
    private ImageProvider imageAdapter2 = mock(ImageProvider.class);
    private ImageProvider imageAdapter3 = mock(ImageProvider.class);
    private AbstractRosAdapterHelper adapter1 = new AbstractRosAdapterHelper(imageAdapter1);
    private AbstractRosAdapterHelper adapter2 = new AbstractRosAdapterHelper(imageAdapter2);
    private AbstractRosAdapterHelper adapter3 = new AbstractRosAdapterHelper(imageAdapter3);

    @BeforeMethod
    public void setUp()
    {
        RosTopic topic1 = mock(RosTopic.class);
        when(topic1.getName()).thenReturn("/topic/1");

        when(abstracAdapter1.getTopic()).thenReturn(topic1);

        when(request.getContextPath()).thenReturn("/test-context");

        when(requestGlobals.getRequest()).thenReturn(request);

        when(image1.getHeight()).thenReturn(480);
        when(image1.getWidth()).thenReturn(640);

        when(image2.getHeight()).thenReturn(240);
        when(image2.getWidth()).thenReturn(320);

        when(imageAdapter1.getImage()).thenReturn(image1);
        when(imageAdapter2.getImage()).thenReturn(image2);
        when(imageAdapter3.getImage()).thenReturn(null);

        when(messages.get("camera.image.alt")).thenReturn("image-alt");
        when(messages.get("camera.image.title")).thenReturn("image-title");

        imageTagService = new ImageTagServiceImpl(requestGlobals, messages);
    }

    @DataProvider
    public Object[][] imageAdapterDataProvider()
    {
        return new Object[][]{
            new Object[]{adapter1, new Dimension(640, 480)},
            new Object[]{adapter2, new Dimension(320, 240)},
            new Object[]{adapter3, new Dimension(10, 10)},
        };
    }

    @Test(dataProvider = "imageAdapterDataProvider")
    public void shouldGetImageDimension(AbstractRosAdapter adapter, Dimension expectedDimension)
    {
        Dimension dimension = imageTagService.getRosImageDimension(adapter);
        assertThat(dimension.getWidth()).isEqualTo(expectedDimension.getWidth(), offset(1E-8));
        assertThat(dimension.getHeight()).isEqualTo(expectedDimension.getHeight(), offset(1E-8));
    }

    @DataProvider
    public Object[][] unfittingAdaptersDataProvider()
    {
        return new Object[][]{
            new Object[]{null},
            new Object[]{abstracAdapter1},
            new Object[]{abstracAdapter2},
        };
    }

    @Test(dataProvider = "unfittingAdaptersDataProvider")
    public void shouldIgnoreUnfittingAdapters(AbstractRosAdapter adapter)
    {
        Dimension dimension = imageTagService.getRosImageDimension(adapter);
        assertThat(dimension.getWidth()).isEqualTo(10.0, offset(1E-8));
        assertThat(dimension.getHeight()).isEqualTo(10.0, offset(1E-8));
    }

    @DataProvider
    public Object[][] imageTagDataProvider()
    {
        return new Object[][]{
            new Object[]{abstracAdapter1, "<img src=\"/test-context/commons/ros/cameraimage/_topic_1/\\d+\" "
                + "width=\"10\" height=\"10\" alt=\"image-alt\" title=\"image-title\">"},
            new Object[]{adapter1, "<img src=\"/test-context/commons/ros/cameraimage/_lala_topic11/\\d+\" "
                + "width=\"640\" height=\"480\" alt=\"image-alt\" title=\"image-title\">"},
            new Object[]{adapter2, "<img src=\"/test-context/commons/ros/cameraimage/_lala_topic11/\\d+\" "
                + "width=\"320\" height=\"240\" alt=\"image-alt\" title=\"image-title\">"},
            new Object[]{adapter3, "<img src=\"/test-context/commons/ros/cameraimage/_lala_topic11/\\d+\" "
                + "width=\"10\" height=\"10\" alt=\"image-alt\" title=\"image-title\">"},
        };
    }

    @Test(dataProvider = "imageTagDataProvider")
    public void shouldGetImageTag(AbstractRosAdapter adapter, String expectedPattern)
    {
        String tag = imageTagService.getRosImageTag(adapter);
        assertThat(tag.matches(expectedPattern)).isTrue();
    }

    private static class AbstractRosAdapterHelper extends AbstractRosAdapter implements ImageProvider
    {
        private ImageProvider delegate;

        public AbstractRosAdapterHelper(ImageProvider delegate)
        {
            this.delegate = delegate;
        }

        @Override
        public Image getImage()
        {
            return delegate.getImage();
        }

        @Override
        public RosTopic getTopic()
        {
            RosTopic topic = new RosTopic();
            topic.setName("/lala/topic11");
            return topic;
        }

        @Override
        public Message getValue()
        {
            return null;
        }

        @Override
        public void setValue(Object object)
        {

        }
    }
}
