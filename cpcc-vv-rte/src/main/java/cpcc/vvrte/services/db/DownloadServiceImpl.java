// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.vvrte.services.db;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import cpcc.core.services.jobs.TimeService;
import cpcc.ros.services.RosImageConverter;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleStorage;
import cpcc.vvrte.utils.JavaScriptUtils;
import cpcc.vvrte.utils.VirtualVehicleStorageUtils;

/**
 * DownloadServiceImpl implementation.
 */
public class DownloadServiceImpl implements DownloadService
{
    private VvRteRepository vvRteRepo;
    private TimeService timeService;
    private RosImageConverter imageConverter;

    /**
     * @param vvRteRepo the virtual vehicle repository.
     * @param timeService the time service.
     * @param imageConverter the ROS image converter.
     */
    public DownloadServiceImpl(VvRteRepository vvRteRepo, TimeService timeService, RosImageConverter imageConverter)
    {
        this.vvRteRepo = vvRteRepo;
        this.timeService = timeService;
        this.imageConverter = imageConverter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getAllVirtualVehicles() throws IOException
    {
        long now = timeService.currentTimeMillis();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bos);

        for (VirtualVehicle vv : vvRteRepo.findAllVehicles())
        {
            String vvDirName = vv.getUuid() + "/";
            String vvStorageName = vv.getUuid() + "/storage/";

            writeDirectory(zos, now, vvDirName);

            writeProperties(zos, vvDirName + "vv.properties", now, vv);
            writeFile(zos, vvDirName + "code.js", now, vv.getCode());
            writeFile(zos, vvDirName + "continuation.dat", now, vv.getContinuation());
            writeFile(zos, vvDirName + "state-info.txt", now, vv.getStateInfo());

            writeDirectory(zos, now, vvStorageName);

            for (VirtualVehicleStorage item : vvRteRepo.findStorageItemsByVirtualVehicle(vv.getId()))
            {
                if (VirtualVehicleStorageUtils.isItemAnImage(item))
                {
                    sensor_msgs.Image image = VirtualVehicleStorageUtils.itemToRosImageMessage(item);
                    BufferedImage bufferedImage = imageConverter.messageToBufferedImage(image);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "png", baos);
                    baos.flush();
                    baos.close();

                    writeFile(zos, vvStorageName + item.getName() + ".png", item.getModificationTime().getTime(),
                        baos.toByteArray());

                }
                else
                {
                    writeFile(zos, vvStorageName + item.getName() + ".json", item.getModificationTime().getTime(),
                        JavaScriptUtils.toJsonString(item.getContent()));
                }

            }
        }

        zos.close();
        bos.close();
        return bos.toByteArray();
    }

    private void writeDirectory(ZipOutputStream zos, long time, String dirName) throws IOException
    {
        ZipEntry dir = new ZipEntry(dirName);
        dir.setTime(time);
        dir.setSize(0);
        zos.putNextEntry(dir);
    }

    private void writeFile(ZipOutputStream zos, String fileName, long time, byte[] content) throws IOException
    {
        if (content != null)
        {
            ZipEntry entry = new ZipEntry(fileName);
            entry.setTime(time);
            entry.setSize(content.length);
            // entry.setCompressedSize(content.length);
            zos.putNextEntry(entry);
            zos.write(content);
            zos.closeEntry();
        }
    }

    private void writeFile(ZipOutputStream zos, String fileName, long time, String content) throws IOException
    {
        if (content != null)
        {
            writeFile(zos, fileName, time, content.getBytes(StandardCharsets.UTF_8));
        }
    }

    private void writeProperties(ZipOutputStream zos, String fileName, long time, VirtualVehicle vv) throws IOException
    {
        Properties props = new Properties();
        setProperty(props, "api-version", Integer.toString(vv.getApiVersion()));
        setDateProperty(props, "end-time", vv.getEndTime());
        setProperty(props, "name", vv.getName());
        setDateProperty(props, "start-time", vv.getStartTime());
        setProperty(props, "state", vv.getState().name());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(bos, StandardCharsets.UTF_8);
        props.store(osw, "VV Properties");
        osw.close();
        bos.close();

        writeFile(zos, fileName, time, bos.toByteArray());
    }

    private static void setProperty(Properties props, String key, String value)
    {
        if (value != null)
        {
            props.setProperty(key, value);
        }
    }

    private static void setDateProperty(Properties props, String key, Date value)
    {
        if (value != null)
        {
            props.setProperty(key, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value));
        }
    }
}
