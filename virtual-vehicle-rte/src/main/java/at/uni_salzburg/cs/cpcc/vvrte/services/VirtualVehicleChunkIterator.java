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
package at.uni_salzburg.cs.cpcc.vvrte.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.persistence.entities.Parameter;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleStorage;

/**
 * VirtualVehicleChunkIterator
 */
public class VirtualVehicleChunkIterator implements Iterator<byte[]>
{
    private static final Logger LOG = LoggerFactory.getLogger(VirtualVehicleChunkIterator.class);

    private VirtualVehicle virtualVehicle;
    private VvRteRepository vvRepo;
    private int chunkCounter = 0;
    private int chunkSize;
    private boolean moreChunksAvailable = false;
    private String lastStorageName = null;
    private List<VirtualVehicleStorage> storageChunk;

    private Properties virtualVehicleProps;

    /**
     * @param vvRepo the virtual vehicle repository.
     * @param virtualVehicle the virtual vehicle to be migrated.
     */
    public VirtualVehicleChunkIterator(VvRteRepository vvRepo, VirtualVehicle virtualVehicle)
    {
        this.vvRepo = vvRepo;
        this.virtualVehicle = virtualVehicle;
        if (virtualVehicle == null)
        {
            return;
        }

        Parameter p =
            vvRepo.getQueryManager().findParameterByName(Parameter.VIRTUAL_VEHICLE_MIGRATION_CHUNK_SIZE, "10");
        chunkSize = Integer.parseInt(p.getValue());
        moreChunksAvailable = true;
        storageChunk = vvRepo.findStorageItemsByVirtualVehicle(virtualVehicle.getId(), lastStorageName, chunkSize);

        virtualVehicleProps = fillVirtualVehicleProps(virtualVehicle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext()
    {
        return moreChunksAvailable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] next()
    {
        if (!moreChunksAvailable)
        {
            return null;
        }

        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.setEntryEncoding("UTF-8");

            ArchiveOutputStream outStream = factory.createArchiveOutputStream("tar", baos);
            writeVirtualVehicleProperties(outStream);
            if (chunkCounter == 0)
            {
                writeVirtualVehicleSourceCode(outStream);
                writeVirtualVehicleContinuation(outStream);
            }
            writeVirtualVehicleStorageChunk(outStream);
            outStream.close();

            baos.close();

            storageChunk = vvRepo.findStorageItemsByVirtualVehicle(virtualVehicle.getId(), lastStorageName, chunkSize);
            moreChunksAvailable = storageChunk != null && storageChunk.size() > 0;
            ++chunkCounter;

            return baos.toByteArray();
        }
        catch (ArchiveException | IOException e)
        {
            String msg = "Can not create chunk " + chunkCounter + " " + " for VV-ID=" + virtualVehicle.getUuid();
            LOG.error(msg, e);
            throw new IllegalStateException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove()
    {
        throw new IllegalStateException("Invoking remove() is not allowed.");
    }

    /**
     * @param os the output stream to write to.
     * @throws IOException thrown in case of errors.
     */
    private void writeVirtualVehicleProperties(ArchiveOutputStream os) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        virtualVehicleProps.store(baos, "Virtual Vehicle Properties");
        baos.close();

        byte[] propBytes = baos.toByteArray();

        TarArchiveEntry entry = new TarArchiveEntry("vv/vv.properties");
        entry.setModTime(new Date());
        entry.setSize(propBytes.length);
        entry.setIds(0, chunkCounter);
        entry.setNames("vvrte", "cpcc");

        os.putArchiveEntry(entry);
        os.write(propBytes);
        os.closeArchiveEntry();
    }

    /**
     * @param os the output stream to write to.
     * @throws IOException thrown in case of errors.
     */
    private void writeVirtualVehicleSourceCode(ArchiveOutputStream os) throws IOException
    {
        byte[] source = virtualVehicle.getCode().getBytes("UTF-8");

        TarArchiveEntry entry = new TarArchiveEntry("vv/vv-source.js");
        entry.setModTime(new Date());
        entry.setSize(source.length);
        entry.setIds(0, chunkCounter);
        entry.setNames("vvrte", "cpcc");

        os.putArchiveEntry(entry);
        os.write(source);
        os.closeArchiveEntry();
    }

    /**
     * @param os the output stream to write to.
     * @throws IOException thrown in case of errors.
     */
    private void writeVirtualVehicleContinuation(ArchiveOutputStream os) throws IOException
    {
        byte[] continuation = virtualVehicle.getContinuation();

        if (continuation == null)
        {
            return;
        }

        TarArchiveEntry entry = new TarArchiveEntry("vv/vv-continuation.js");
        entry.setModTime(new Date());
        entry.setSize(continuation.length);
        entry.setIds(0, chunkCounter);
        entry.setNames("vvrte", "cpcc");

        os.putArchiveEntry(entry);
        os.write(continuation);
        os.closeArchiveEntry();
    }

    /**
     * @param os the output stream to write to.
     * @throws IOException thrown in case of errors.
     */
    private void writeVirtualVehicleStorageChunk(ArchiveOutputStream os) throws IOException
    {
        for (VirtualVehicleStorage se : storageChunk)
        {
            lastStorageName = se.getName();
            LOG.info("Writing storage entry '" + lastStorageName + "'");

            byte[] content = se.getContentAsByteArray();
            TarArchiveEntry entry = new TarArchiveEntry("storage/" + se.getName() + ".dat");
            entry.setModTime(se.getModificationTime());
            entry.setSize(content.length);
            entry.setIds(se.getId(), chunkCounter);
            entry.setNames("vvrte", "cpcc");

            os.putArchiveEntry(entry);
            os.write(content);
            os.closeArchiveEntry();
        }
    }

    /**
     * @param vv the
     * @return
     */
    private static Properties fillVirtualVehicleProps(VirtualVehicle virtualVehicle)
    {
        Properties props = new Properties();
        props.setProperty("name", virtualVehicle.getName());
        props.setProperty("uuid", virtualVehicle.getUuid());
        props.setProperty("state", virtualVehicle.getState().toString());
        props.setProperty("api.version", Integer.toString(virtualVehicle.getApiVersion()));
        props.setProperty("start.time", Long.toString(virtualVehicle.getStartTime().getTime()));
        props.setProperty("end.time", Long.toString(virtualVehicle.getEndTime().getTime()));
        return props;
    }

}