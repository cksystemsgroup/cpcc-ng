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

package cpcc.commons.pages.vv;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.inject.Inject;

import org.apache.tapestry5.StreamResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.utils.PngImageStreamResponse;
import cpcc.ros.services.RosImageConverter;
import cpcc.vvrte.entities.VirtualVehicleStorage;
import cpcc.vvrte.services.db.VvRteRepository;
import cpcc.vvrte.utils.VirtualVehicleStorageUtils;

/**
 * Vehicle storage image page.
 */
public class VvStorageImage
{
    private static final Logger LOG = LoggerFactory.getLogger(VvStorageImage.class);

    @Inject
    private VvRteRepository vvRteRepo;

    @Inject
    private RosImageConverter imageConverter;

    /**
     * @param time the time stamp of the stored item (ignored).
     * @param storageId the identification of the stored item.
     * @return the currently available camera snapshot as <code>StreamResponse</code> image.
     */
    public StreamResponse onActivate(Long time, Integer storageId)
    {
        VirtualVehicleStorage item = vvRteRepo.findStorageItemById(storageId);
        sensor_msgs.Image image = VirtualVehicleStorageUtils.itemToRosImageMessage(item);
        BufferedImage bufferedImage = imageConverter.messageToBufferedImage(image);

        try
        {
            return PngImageStreamResponse.convertImageToStreamResponse(bufferedImage);
        }
        catch (IOException e)
        {
            LOG.error("Can not convert image to StreamResponse.", e);
            return new PngImageStreamResponse();
        }
    }

}
