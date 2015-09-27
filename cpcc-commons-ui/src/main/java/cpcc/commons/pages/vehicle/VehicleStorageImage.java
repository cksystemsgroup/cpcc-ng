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

package cpcc.commons.pages.vehicle;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteOrder;

import javax.inject.Inject;

import org.apache.tapestry5.StreamResponse;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.mozilla.javascript.ScriptableObject;
import org.ros.node.NodeConfiguration;
import org.slf4j.Logger;

import cpcc.core.utils.PngImageStreamResponse;
import cpcc.ros.services.RosImageConverter;
import cpcc.vvrte.entities.VirtualVehicleStorage;
import cpcc.vvrte.services.VvRteRepository;

/**
 * VehicleStorageImage
 */
public class VehicleStorageImage
{
    @Inject
    private Logger logger;

    @Inject
    private VvRteRepository vvRteRepo;

    @Inject
    private RosImageConverter imageConverter;

    /**
     * @param time the time stamp of the stored item (ignored).
     * @param storageId the identification of the stored item.
     * @return the currently available camera snapshot as <code>StreamResponse</code> image.
     */
    public StreamResponse onActivate(Integer time, Integer storageId)
    {
        VirtualVehicleStorage item = vvRteRepo.findStorageItemById(storageId);

        ScriptableObject obj = item.getContent();
        String encoding = (String) obj.get("encoding", obj);
        int height = (int) obj.get("height", obj);
        int width = (int) obj.get("width", obj);
        int step = (int) obj.get("step", obj);
        byte[] data = (byte[]) obj.get("data", obj);
        ChannelBuffer cb = ChannelBuffers.copiedBuffer(ByteOrder.LITTLE_ENDIAN, data);

        sensor_msgs.Image image = (sensor_msgs.Image) NodeConfiguration.newPrivate().getTopicMessageFactory()
            .newFromType(sensor_msgs.Image._TYPE);
        image.setEncoding(encoding);
        image.setHeight(height);
        image.setWidth(width);
        image.setStep(step);
        image.setIsBigendian((byte) 0);
        image.setData(cb);

        BufferedImage bufferedImage = imageConverter.messageToBufferedImage(image);

        try
        {
            return PngImageStreamResponse.convertImageToStreamResponse(bufferedImage);
        }
        catch (IOException e)
        {
            logger.error("Can not convert image to StreamResponse.", e);
            return new PngImageStreamResponse();
        }
    }
}
