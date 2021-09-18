// This code is part of the CPCC-NG project.
//
// Copyright (c) 2014 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.commons.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Dimension;
import java.util.stream.Stream;

import org.apache.tapestry5.commons.Messages;
import org.apache.tapestry5.http.services.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.ros.internal.message.Message;

import cpcc.ros.base.AbstractRosAdapter;
import cpcc.ros.base.RosTopic;
import cpcc.ros.sensors.GpsSensorAdapter;
import cpcc.ros.sensors.ImageProvider;
import sensor_msgs.Image;

class ImageTagServiceTest
{
    Request request = mock(Request.class);
    Messages messages = mock(Messages.class);
    ImageTagService imageTagService;

    @BeforeEach
    void setUp()
    {
        when(messages.get("camera.image.alt")).thenReturn("image-alt");
        when(messages.get("camera.image.title")).thenReturn("image-title");

        imageTagService = new ImageTagServiceImpl(messages, "/test-context");
    }

    static Stream<Arguments> imageAdapterDataProvider()
    {
        Image image1 = mock(Image.class);
        Image image2 = mock(Image.class);
        when(image1.getHeight()).thenReturn(480);
        when(image1.getWidth()).thenReturn(640);
        when(image2.getHeight()).thenReturn(240);
        when(image2.getWidth()).thenReturn(320);

        ImageProvider imageAdapter1 = mock(ImageProvider.class);
        ImageProvider imageAdapter2 = mock(ImageProvider.class);
        ImageProvider imageAdapter3 = mock(ImageProvider.class);
        when(imageAdapter1.getImage()).thenReturn(image1);
        when(imageAdapter2.getImage()).thenReturn(image2);
        when(imageAdapter3.getImage()).thenReturn(null);

        AbstractRosAdapterHelper adapter1 = new AbstractRosAdapterHelper(imageAdapter1);
        AbstractRosAdapterHelper adapter2 = new AbstractRosAdapterHelper(imageAdapter2);
        AbstractRosAdapterHelper adapter3 = new AbstractRosAdapterHelper(imageAdapter3);

        return Stream.of(
            arguments(adapter1, new Dimension(640, 480)),
            arguments(adapter2, new Dimension(320, 240)),
            arguments(adapter3, new Dimension(10, 10)));
    }

    @ParameterizedTest
    @MethodSource("imageAdapterDataProvider")
    void shouldGetImageDimension(AbstractRosAdapter adapter, Dimension expectedDimension)
    {
        Dimension dimension = imageTagService.getRosImageDimension(adapter);
        assertThat(dimension.getWidth()).describedAs("width").isEqualTo(expectedDimension.getWidth(), offset(1E-8));
        assertThat(dimension.getHeight()).describedAs("height").isEqualTo(expectedDimension.getHeight(), offset(1E-8));
    }

    static Stream<Arguments> unfittingAdaptersDataProvider()
    {
        AbstractRosAdapter abstracAdapter1 = mock(AbstractRosAdapter.class);
        AbstractRosAdapter abstracAdapter2 = mock(GpsSensorAdapter.class);

        RosTopic topic1 = mock(RosTopic.class);
        when(topic1.getName()).thenReturn("/topic/1");

        when(abstracAdapter1.getTopic()).thenReturn(topic1);

        return Stream.of(
            arguments((AbstractRosAdapter) null),
            arguments(abstracAdapter1),
            arguments(abstracAdapter2));
    }

    @ParameterizedTest
    @MethodSource("unfittingAdaptersDataProvider")
    void shouldIgnoreUnfittingAdapters(AbstractRosAdapter adapter)
    {
        Dimension dimension = imageTagService.getRosImageDimension(adapter);
        assertThat(dimension.getWidth()).describedAs("width").isEqualTo(10.0, offset(1E-8));
        assertThat(dimension.getHeight()).describedAs("height").isEqualTo(10.0, offset(1E-8));
    }

    static Stream<Arguments> imageTagDataProvider()
    {
        AbstractRosAdapter abstracAdapter1 = mock(AbstractRosAdapter.class);
        RosTopic topic1 = mock(RosTopic.class);
        when(topic1.getName()).thenReturn("/topic/1");
        when(abstracAdapter1.getTopic()).thenReturn(topic1);

        Image image1 = mock(Image.class);
        Image image2 = mock(Image.class);
        when(image1.getHeight()).thenReturn(480);
        when(image1.getWidth()).thenReturn(640);
        when(image2.getHeight()).thenReturn(240);
        when(image2.getWidth()).thenReturn(320);

        ImageProvider imageAdapter1 = mock(ImageProvider.class);
        ImageProvider imageAdapter2 = mock(ImageProvider.class);
        ImageProvider imageAdapter3 = mock(ImageProvider.class);
        when(imageAdapter1.getImage()).thenReturn(image1);
        when(imageAdapter2.getImage()).thenReturn(image2);
        when(imageAdapter3.getImage()).thenReturn(null);

        AbstractRosAdapterHelper adapter1 = new AbstractRosAdapterHelper(imageAdapter1);
        AbstractRosAdapterHelper adapter2 = new AbstractRosAdapterHelper(imageAdapter2);
        AbstractRosAdapterHelper adapter3 = new AbstractRosAdapterHelper(imageAdapter3);

        return Stream.of(
            arguments(abstracAdapter1, "<img src=\"/test-context/commons/ros/cameraimage/.topic.1/\\d+\" "
                + "width=\"10\" height=\"10\" alt=\"image-alt\" title=\"image-title\">"),
            arguments(adapter1, "<img src=\"/test-context/commons/ros/cameraimage/.lala.topic11/\\d+\" "
                + "width=\"640\" height=\"480\" alt=\"image-alt\" title=\"image-title\">"),
            arguments(adapter2, "<img src=\"/test-context/commons/ros/cameraimage/.lala.topic11/\\d+\" "
                + "width=\"320\" height=\"240\" alt=\"image-alt\" title=\"image-title\">"),
            arguments(adapter3, "<img src=\"/test-context/commons/ros/cameraimage/.lala.topic11/\\d+\" "
                + "width=\"10\" height=\"10\" alt=\"image-alt\" title=\"image-title\">"));
    }

    @ParameterizedTest
    @MethodSource("imageTagDataProvider")
    void shouldGetImageTag(AbstractRosAdapter adapter, String expectedPattern)
    {
        String tag = imageTagService.getRosImageTag(adapter);
        assertThat(tag).describedAs("pattern").matches(expectedPattern);
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
