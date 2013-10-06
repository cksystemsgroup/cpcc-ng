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
package at.uni_salzburg.cs.cpcc.ros.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import sensor_msgs.Image;

/**
 * RosImageConverterTest
 */
public class RosImageConverterTest
{
    private RosImageConverterImpl conv;
    private ChannelBuffer buffer;
    private Image message;

    //    private static final byte[] SIXTY_BYTES = new byte[]{
    //        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
    //        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
    //        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
    //        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
    //        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
    //        0, 1, 2, 3, 4, 5, 6, 7, 8, 9
    //    };

    @BeforeMethod
    public void setUp()
    {
        conv = new RosImageConverterImpl();
        buffer = mock(ChannelBuffer.class);
        message = mock(Image.class);
        when(message.getData()).thenReturn(buffer);

    }

    @DataProvider
    public Object[][] emptyImageDataprovider()
    {
        return new Object[][]{
            new Object[]{40, 30, "gif"},
            new Object[]{40, 30, "jpeg"},
            new Object[]{40, 30, "jpg"},
            new Object[]{40, 30, "png"},
        };
    }

    @Test(dataProvider = "emptyImageDataprovider")
    public void shouldReturnNullOnEmptyImage(int height, int width, String encoding)
    {
        when(buffer.array()).thenReturn(new byte[0]);
        when(buffer.hasArray()).thenReturn(false);

        when(message.getEncoding()).thenReturn(encoding);
        when(message.getHeight()).thenReturn(height);
        when(message.getWidth()).thenReturn(width);

        BufferedImage result = conv.messageToBufferedImage(message);
        assertThat(result).isNull();
    }

    @DataProvider
    public Object[][] imageDataprovider()
    {
        return new Object[][]{
            new Object[]{90, 120, "GIF", "data/test-image.gif"},
            new Object[]{90, 120, "JPEG", "data/test-image.jpeg"},
            new Object[]{90, 120, "JPG", "data/test-image.jpg"},
            new Object[]{90, 120, "PNG", "data/test-image.png"},
        };
    }

    @Test(dataProvider = "imageDataprovider")
    public void shouldConvertGenericImages(int height, int width, String encoding, String imageName) throws IOException
    {
        InputStream stream = RosImageConverterTest.class.getResourceAsStream(imageName);
        byte[] imageData = IOUtils.toByteArray(stream);

        when(buffer.array()).thenReturn(imageData);
//        when(buffer.hasArray()).thenReturn(true);

        when(message.getEncoding()).thenReturn(encoding);
        when(message.getHeight()).thenReturn(height);
        when(message.getWidth()).thenReturn(width);
        when(message.getData()).thenReturn(
            ChannelBuffers.copiedBuffer(ByteOrder.nativeOrder(), new byte[40], imageData));

        BufferedImage result = conv.messageToBufferedImage(message);
        assertThat(result).isNotNull();

        assertThat(result.getHeight()).isEqualTo(height);
        assertThat(result.getWidth()).isEqualTo(width);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(result, encoding, bos);

        byte[] resultImageData = bos.toByteArray();

        assertThat(resultImageData.length).isEqualTo(imageData.length);
        // assertThat(resultImageData).isEqualTo(imageData);
    }
    
    @Test
    public void shouldReturnAnEmptyImageForACorruptedPNG() throws IOException
    {
        int height=90;
        int width=120;
        String encoding="PNG";
        String imageName="data/test-image.png";
        
        InputStream stream = RosImageConverterTest.class.getResourceAsStream(imageName);
        byte[] saneImageData = IOUtils.toByteArray(stream);
        byte[] imageData = Arrays.copyOfRange(saneImageData, 0, saneImageData.length-100);
        
        when(buffer.array()).thenReturn(imageData);

        when(message.getEncoding()).thenReturn(encoding);
        when(message.getHeight()).thenReturn(height);
        when(message.getWidth()).thenReturn(width);
        when(message.getData()).thenReturn(
            ChannelBuffers.copiedBuffer(ByteOrder.nativeOrder(), new byte[40], imageData));
        
        BufferedImage result = conv.messageToBufferedImage(message);
        assertThat(result).isNotNull();
        
        assertThat(result.getHeight()).isEqualTo(height);
        assertThat(result.getWidth()).isEqualTo(width);
        
        assertThatImageIsEmpty(result);
    }
    
    @Test
    public void shouldReturnAnEmptyImageForUnknownImageFormats()
    {
        int height=91;
        int width=121;
        String encoding="unknownImageFormat";
        
        when(message.getEncoding()).thenReturn(encoding);
        when(message.getHeight()).thenReturn(height);
        when(message.getWidth()).thenReturn(width);
        
        BufferedImage result = conv.messageToBufferedImage(message);
        assertThat(result).isNotNull();
        
        assertThat(result.getHeight()).isEqualTo(height);
        assertThat(result.getWidth()).isEqualTo(width);
        
        assertThatImageIsEmpty(result);
    }

    /**
     * @param image the image to be checked.
     */
    private void assertThatImageIsEmpty(BufferedImage image)
    {
        for (int y = 0; y < image.getHeight(); ++y)
        {
            for (int x = 0; x < image.getWidth(); ++x)
            {
                assertThat(image.getRGB(x, y)).overridingErrorMessage("Problem at x=%d, y=%d", x, y).isEqualTo(0);
            }
        }
    }

    @Test
    public void shouldConvertRGB8Images() throws IOException
    {
        int height = 240;
        int width = 320;
        int step = 960;
        String encoding = "rgb8";
        String imageName = "data/test-image-rgb8.rgb8";
        String convertedImageName = "data/test-image-rgb8.png";

        InputStream stream = RosImageConverterTest.class.getResourceAsStream(imageName);
        byte[] imageData = IOUtils.toByteArray(stream);
        
        stream = RosImageConverterTest.class.getResourceAsStream(convertedImageName);
        byte[] convertedImageData = IOUtils.toByteArray(stream);
        
        when(buffer.array()).thenReturn(imageData);

        when(message.getEncoding()).thenReturn(encoding);
        when(message.getHeight()).thenReturn(height);
        when(message.getWidth()).thenReturn(width);
        when(message.getStep()).thenReturn(step);
        when(message.getData()).thenReturn(
            ChannelBuffers.copiedBuffer(ByteOrder.nativeOrder(), new byte[41], imageData));

        BufferedImage result = conv.messageToBufferedImage(message);
        assertThat(result).isNotNull();
        assertThat(result.getHeight()).isEqualTo(height);
        assertThat(result.getWidth()).isEqualTo(width);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(result, "PNG", bos);
        
        assertThat(bos.toByteArray()).isEqualTo(convertedImageData);
    }

    @Test
    public void shouldConvertRGBA8Images() throws IOException
    {
        int height = 240;
        int width = 320;
        int step = 1280;
        String encoding = "rgba8";
        String imageName = "data/test-image-rgba8.rgba8";
        String convertedImageName = "data/test-image-rgba8.png";

        InputStream stream = RosImageConverterTest.class.getResourceAsStream(imageName);
        byte[] imageData = IOUtils.toByteArray(stream);
        
        stream = RosImageConverterTest.class.getResourceAsStream(convertedImageName);
        byte[] convertedImageData = IOUtils.toByteArray(stream);
        
        when(buffer.array()).thenReturn(imageData);

        when(message.getEncoding()).thenReturn(encoding);
        when(message.getHeight()).thenReturn(height);
        when(message.getWidth()).thenReturn(width);
        when(message.getStep()).thenReturn(step);
        when(message.getData()).thenReturn(
            ChannelBuffers.copiedBuffer(ByteOrder.nativeOrder(), new byte[67], imageData));

        BufferedImage result = conv.messageToBufferedImage(message);
        assertThat(result).isNotNull();
        assertThat(result.getHeight()).isEqualTo(height);
        assertThat(result.getWidth()).isEqualTo(width);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(result, "PNG", bos);
        
        assertThat(bos.toByteArray()).isEqualTo(convertedImageData);
    }
}
