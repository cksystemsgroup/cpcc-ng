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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.commons.services.image.PngImageStreamResponse;

/**
 * PngImageStreamResponseTest
 */
public class PngImageStreamResponseTest
{
    @Test
    public void shouldCreateDefaultPngStreamResponse() throws IOException
    {
        PngImageStreamResponse response = new PngImageStreamResponse();
        InputStream stream = response.getStream();
        assertThat(stream).isNotNull();
        byte[] expected = new byte[]{(byte) 0x89, 0x50, 0x4e, 0x47};
        byte[] buffer = new byte[4];
        stream.read(buffer);
        assertThat(buffer).isEqualTo(expected);
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
        byte[] imageData = new byte[]{1,2,3,4,5,6,7,8,9,10};
        PngImageStreamResponse response = new PngImageStreamResponse(imageData);
        byte[] buffer = new byte[imageData.length];
        response.getStream().read(buffer);
        assertThat(buffer).isEqualTo(imageData);
        assertThat(response.getStream().read()).isEqualTo(-1);
        response.getStream().close();
    }
    
    @Test
    public void shouldStreamGivenFile() throws IOException
    {
        byte[] imageData = new byte[]{11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
        File file = File.createTempFile("random", "png");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(imageData);
        fos.close();
        
        PngImageStreamResponse response = new PngImageStreamResponse(file);
        byte[] buffer = new byte[imageData.length];
        response.getStream().read(buffer);
        assertThat(buffer).isEqualTo(imageData);
        assertThat(response.getStream().read()).isEqualTo(-1);
        response.getStream().close();
        
        file.delete();
    }
    
    @Test
    public void shouldDeliverDefaultImageWhenMissingFile() throws IOException
    {
        File file = File.createTempFile("random", "png");
        file.delete();
        
        PngImageStreamResponse response = new PngImageStreamResponse(file);
        InputStream stream = response.getStream();
        assertThat(stream).isNotNull();
        byte[] expected = new byte[]{(byte) 0x89, 0x50, 0x4e, 0x47};
        byte[] buffer = new byte[4];
        stream.read(buffer);
        assertThat(buffer).isEqualTo(expected);
    }
    
}
