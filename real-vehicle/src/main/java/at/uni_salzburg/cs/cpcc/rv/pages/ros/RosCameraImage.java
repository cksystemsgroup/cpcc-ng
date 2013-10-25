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
package at.uni_salzburg.cs.cpcc.rv.pages.ros;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.ros.base.AbstractRosAdapter;
import at.uni_salzburg.cs.cpcc.ros.sensors.ImageProvider;
import at.uni_salzburg.cs.cpcc.ros.services.RosImageConverter;
import at.uni_salzburg.cs.cpcc.ros.services.RosNodeService;
import at.uni_salzburg.cs.cpcc.rv.services.image.PngImageStreamResponse;

/**
 * RosCameraImage
 */
public class RosCameraImage
{
    private static final Logger LOG = LoggerFactory.getLogger(RosCameraImage.class);

    @Inject
    private RosNodeService rns;

    @Inject
    private RosImageConverter imageConverter;

    @PageActivationContext
    private String ctx;

    /**
     * @param rootTopicParam the root topic.
     * @return the currently available camera snapshot as <code>StreamResponse</code> image.
     */
    public StreamResponse onActivate(String rootTopicParam)
    {
        String rootTopic = rootTopicParam.replaceAll("_", "/").replaceAll("image/raw", "image_raw");
        
        if (LOG.isDebugEnabled())
        {
            LOG.debug("ctx=" + ctx + ", rootTopic=" + rootTopic);
            LOG.debug("Preparing image for root topic " + rootTopicParam);
        }
        
        AbstractRosAdapter adapter = rns.getAdapterNodeByTopic(rootTopic);
        
        sensor_msgs.Image image = null;
        if (adapter instanceof ImageProvider)
        {
            image = ((ImageProvider) adapter).getImage();
        }

        if (image == null)
        {
            LOG.error("No image adapter found for root topic " + rootTopicParam);
            return new PngImageStreamResponse();
        }

        BufferedImage bufferedImage = imageConverter.messageToBufferedImage(image);

        return convertImageToStreamResponse(rootTopicParam, bufferedImage);
    }

    /**
     * @param rootTopic the root topic.
     * @param image the buffered image object.
     * @return the image as a <code>StreamResponse</code> object.
     */
    private StreamResponse convertImageToStreamResponse(String rootTopic, BufferedImage image)
    {
        if (image == null)
        {
            LOG.error("Image is NULL " + rootTopic);
            return new PngImageStreamResponse();
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return new PngImageStreamResponse(imageInByte);
        }
        catch (IOException e)
        {
            LOG.error("Can not convert ROS image to PNG " + rootTopic, e);
        }

        return new PngImageStreamResponse();
    }

}
