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

import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 * RosTopicDumpToFile
 */
public class RosTopicDumpToFile
{
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        String inputFileName = "/home/clem/cpcc-workspace/BigActors/src/morse/nix-rgba8.txt";
        String outputFileNameRGBA8 = "/home/clem/work/cpcc-ng-workspace/cpcc-ng/ros-service/src/test/resources/at/uni_salzburg/cs/cpcc/ros/services/data/test-image-rgba8.rgba8";
        String outputFileNamePNG = "/home/clem/work/cpcc-ng-workspace/cpcc-ng/ros-service/src/test/resources/at/uni_salzburg/cs/cpcc/ros/services/data/test-image-rgba8.png";
        
        RosTopicDumpToFile converter = new RosTopicDumpToFile();
        converter.getStream(new FileInputStream(inputFileName), new FileOutputStream(outputFileNameRGBA8), new FileOutputStream(outputFileNamePNG));
    }

    /**
     * @param fileInputStream
     * @return
     * @throws IOException 
     */
    private void getStream(InputStream inputStream, OutputStream rgba8OutputStream, OutputStream pngOutputStream) throws IOException
    {
        BufferedInputStream bin = new BufferedInputStream(inputStream);
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(bin));

        int width = 0;
        int height = 0;
        int step = 0;
        
        String line = reader2.readLine();
        while (line != null)
        {
            if (line.startsWith("height: "))
            {
                height = Integer.parseInt(line.substring(8));
            }
            else if (line.startsWith("width: "))
            {
                width = Integer.parseInt(line.substring(7));
            }
            else if (line.startsWith("step: "))
            {
                step = Integer.parseInt(line.substring(6));
            }
            else if (line.startsWith("data: "))
            {
                break;
            }
            line = reader2.readLine();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String[] bytes = line.substring(7, line.length() - 1).split(",\\s*");

        for (String b : bytes)
        {
            int value = Integer.parseInt(b);
            bos.write(value);
            rgba8OutputStream.write(value);
        }
        
        rgba8OutputStream.close();
        
        byte[] data = bos.toByteArray();
        DataBufferByte dataBuffer = new DataBufferByte(data, data.length);

        System.out.printf("height=%d, width=%d, step=%d\n", height, width, step);

        int[] bandOffsets = new int[]{0, 1, 2, 3};
        PixelInterleavedSampleModel sampleModel =
            new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, width, height, 4, step, bandOffsets);

        WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, new Point(0, 0));

        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel colourModel =
            new ComponentColorModel(cs, true, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);

        BufferedImage im = new BufferedImage(colourModel, raster, false, null);
        
        ImageIO.write(im, "PNG", pngOutputStream);
        pngOutputStream.close();
    }
}
