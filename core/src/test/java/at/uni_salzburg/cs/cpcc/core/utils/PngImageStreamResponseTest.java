// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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

package at.uni_salzburg.cs.cpcc.core.utils;

import static org.fest.assertions.api.Assertions.assertThat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.StreamResponse;
import org.testng.annotations.Test;

/**
 * PngImageStreamResponseTest
 */
public class PngImageStreamResponseTest
{
    @Test
    public void shouldCreateDefaultPngStreamResponse() throws IOException
    {
        byte[] expected = PngImageStreamResponse.ONE_PIXEL_EMPTY_PNG;

        PngImageStreamResponse response = new PngImageStreamResponse();
        assertThat(response.getStream()).isNotNull();
        byte[] actual = IOUtils.toByteArray(response.getStream());

        assertThat(actual).isEqualTo(expected);
        assertThat(response.getStream().read()).isEqualTo(-1);
    }

    @Test
    public void shouldHaveContentTypePng()
    {
        PngImageStreamResponse response = new PngImageStreamResponse();
        assertThat(response.getContentType()).isNotNull().isEqualTo("image/png");
    }

    @Test
    public void shouldIgnorePrepareResponse()
    {
        PngImageStreamResponse response = new PngImageStreamResponse();
        response.prepareResponse(null);
    }

    @Test
    public void shouldStreamGivenImageData() throws IOException
    {
        byte[] imageData = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        PngImageStreamResponse response = new PngImageStreamResponse(imageData);

        assertThat(response.getStream()).isNotNull();
        byte[] actual = IOUtils.toByteArray(response.getStream());

        assertThat(actual).isEqualTo(imageData);
        assertThat(response.getStream().read()).isEqualTo(-1);
        response.getStream().close();
    }

    @Test
    public void shouldStreamGivenFile() throws IOException
    {
        byte[] imageData = new byte[]{11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
        File file = File.createTempFile("random", "png");
        FileUtils.writeByteArrayToFile(file, imageData);

        PngImageStreamResponse response = new PngImageStreamResponse(file);

        assertThat(response.getStream()).isNotNull();
        byte[] actual = IOUtils.toByteArray(response.getStream());

        assertThat(actual).isEqualTo(imageData);
        assertThat(response.getStream().read()).isEqualTo(-1);
        response.getStream().close();

        file.delete();
    }

    @Test
    public void shouldDeliverDefaultImageWhenMissingFile() throws IOException
    {
        byte[] expected = PngImageStreamResponse.ONE_PIXEL_EMPTY_PNG;

        File file = File.createTempFile("random", "png");
        file.delete();

        PngImageStreamResponse response = new PngImageStreamResponse(file);

        assertThat(response.getStream()).isNotNull();
        byte[] actual = IOUtils.toByteArray(response.getStream());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConvertBufferedImage() throws IOException
    {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        Graphics gr = image.getGraphics();
        gr.setColor(Color.GREEN);
        gr.drawLine(0, 0, 1, 1);
        gr.setColor(Color.BLACK);
        gr.drawLine(1, 1, 0, 0);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        byte[] expected = baos.toByteArray();

        StreamResponse response = PngImageStreamResponse.convertImageToStreamResponse(image);

        assertThat(response.getStream()).isNotNull();
        byte[] actual = IOUtils.toByteArray(response.getStream());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConvertNullImageToDefaultImage() throws IOException
    {
        byte[] expected = PngImageStreamResponse.ONE_PIXEL_EMPTY_PNG;

        StreamResponse response = PngImageStreamResponse.convertImageToStreamResponse(null);

        assertThat(response.getStream()).isNotNull();
        byte[] actual = IOUtils.toByteArray(response.getStream());

        assertThat(actual).isEqualTo(expected);
    }

}
