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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import sensor_msgs.Image;

/**
 * RosImageConverterImpl
 */
public class RosImageConverterImpl implements RosImageConverter
{
    @SuppressWarnings("serial")
    private static final Map<String, ImageConverter> CONVERTER_MAP = new HashMap<String, ImageConverter>()
    {
        {
            put("rgb8", new Rgb8ImageConverter());
            put("rgba8", new Rgb8aImageConverter());
            put("png", new GenericImageConverter());
            put("gif", new GenericImageConverter());
            put("jpg", new GenericImageConverter());
            put("jpeg", new GenericImageConverter());
        }
    };

    /**
     * Conversion of an <code>sensor_msgs.Image</code> message to a <code>BufferedImage</code>.
     * 
     * @param message the ROS image message.
     * @return the <code>BufferedImage</code>
     */
    @Override
    public BufferedImage messageToBufferedImage(sensor_msgs.Image message)
    {
        String encoding = message.getEncoding().toLowerCase();
        
        if (CONVERTER_MAP.containsKey(encoding))
        {
            return CONVERTER_MAP.get(encoding).convert(message);
        }

        return new BufferedImage(message.getWidth(), message.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * ImageConverter
     */
    private interface ImageConverter
    {
        /**
         * @param message the ROS image message.
         * @return the <code>BufferedImage</code>
         */
        BufferedImage convert(sensor_msgs.Image message);
    }

    /**
     * Rgb8ImageConverter
     */
    private static class Rgb8ImageConverter implements ImageConverter
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public BufferedImage convert(Image message)
        {
            DataBufferByte dataBuffer = new DataBufferByte(message.getData().array(), message.getData().array().length);

            int width = message.getWidth();
            int height = message.getHeight();
            int step = message.getStep();
            int[] bandOffsets = new int[]{41, 42, 43};
            PixelInterleavedSampleModel sampleModel =
                new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, width, height, 3, step, bandOffsets);

            WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, new Point(0, 0));

            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
            ColorModel colourModel =
                new ComponentColorModel(cs, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);

            BufferedImage im = new BufferedImage(colourModel, raster, false, null);
            return im;
        }
    }

    /**
     * Rgb8aImageConverter
     */
    private static class Rgb8aImageConverter implements ImageConverter
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public BufferedImage convert(Image message)
        {
            DataBufferByte dataBuffer = new DataBufferByte(message.getData().array(), message.getData().array().length);

            int width = message.getWidth();
            int height = message.getHeight();
            int step = message.getStep();
            int[] bandOffsets = new int[]{67, 68, 69, 70};
            PixelInterleavedSampleModel sampleModel =
                new PixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, width, height, 4, step, bandOffsets);

            WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, new Point(0, 0));

            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
            ColorModel colourModel =
                new ComponentColorModel(cs, true, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);

            BufferedImage im = new BufferedImage(colourModel, raster, false, null);
            return im;
        }
    }

    /**
     * GenericImageConverter
     */
    private static class GenericImageConverter implements ImageConverter
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public BufferedImage convert(Image message)
        {
            try
            {
                ChannelBuffer cb = ChannelBuffers.copiedBuffer(message.getData().array());
                ByteArrayInputStream stream = new ByteArrayInputStream(cb.array(), 40, cb.capacity());
                return ImageIO.read(stream);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return new BufferedImage(message.getWidth(), message.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
    }

}
