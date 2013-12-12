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
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationService;
import at.uni_salzburg.cs.cpcc.persistence.entities.Parameter;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleState;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleStorage;

/**
 * VirtualVehicleMigratorImpl
 */
public class VirtualVehicleMigratorImpl implements VirtualVehicleMigrator
{
    private static final Logger LOG = LoggerFactory.getLogger(VirtualVehicleMigratorImpl.class);

    private VvRteRepository vvRepository;
    private CommunicationService com;

    /**
     * @param vvRepository the virtual vehicle repository.
     * @param com the communication service.
     */
    public VirtualVehicleMigratorImpl(VvRteRepository vvRepository, CommunicationService com)
    {
        this.vvRepository = vvRepository;
        this.com = com;
    }

    @Override
    public void initiateMigration(VirtualVehicle vehicle, VirtualVehicleMappingDecision decision)
    {
        VvMigrationWorker worker = new VvMigrationWorker(vehicle, vvRepository, com, this);
        worker.start();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] findFirstChunk(VirtualVehicle virtualVehicle) throws IOException, ArchiveException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.setEntryEncoding("UTF-8");

        ArchiveOutputStream outStream = factory.createArchiveOutputStream("tar", baos);
        writeVirtualVehicleProperties(virtualVehicle, outStream, 0);
        outStream.close();

        baos.close();

        return baos.toByteArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] findChunk(VirtualVehicle virtualVehicle, String lastStorageName, int chunkNumber)
        throws IOException, ArchiveException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.setEntryEncoding("UTF-8");

        ArchiveOutputStream outStream = factory.createArchiveOutputStream("tar", baos);
        writeVirtualVehicleProperties(virtualVehicle, outStream, chunkNumber);
        if (chunkNumber == 1)
        {
            writeVirtualVehicleSourceCode(virtualVehicle, outStream, chunkNumber);
            writeVirtualVehicleContinuation(virtualVehicle, outStream, chunkNumber);
        }

        Parameter paramChunkSize = vvRepository.getQueryManager()
            .findParameterByName(Parameter.VIRTUAL_VEHICLE_MIGRATION_CHUNK_SIZE, "10");

        int chunkSize = Integer.parseInt(paramChunkSize.getValue());

        List<VirtualVehicleStorage> storageChunk =
            vvRepository.findStorageItemsByVirtualVehicle(virtualVehicle.getId(), lastStorageName, chunkSize);
        writeVirtualVehicleStorageChunk(virtualVehicle, outStream, chunkNumber, storageChunk);

        outStream.close();

        baos.close();

        return baos.toByteArray();
    }

    /**
     * @param virtualVehicle the virtual vehicle.
     * @param os the output stream to write to.
     * @param chunkNumber the chunk number.
     * @throws IOException thrown in case of errors.
     */
    private void writeVirtualVehicleProperties(VirtualVehicle virtualVehicle, ArchiveOutputStream os, int chunkNumber)
        throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Properties virtualVehicleProps = fillVirtualVehicleProps(virtualVehicle);
        virtualVehicleProps.store(baos, "Virtual Vehicle Properties");
        baos.close();

        byte[] propBytes = baos.toByteArray();

        TarArchiveEntry entry = new TarArchiveEntry("vv/vv.properties");
        entry.setModTime(new Date());
        entry.setSize(propBytes.length);
        entry.setIds(0, chunkNumber);
        entry.setNames("vvrte", "cpcc");

        os.putArchiveEntry(entry);
        os.write(propBytes);
        os.closeArchiveEntry();
    }

    /**
     * @param virtualVehicle the virtual vehicle.
     * @param os the output stream to write to.
     * @param chunkNumber the chunk number.
     * @throws IOException thrown in case of errors.
     */
    private void writeVirtualVehicleContinuation(VirtualVehicle virtualVehicle, ArchiveOutputStream os, int chunkNumber)
        throws IOException
    {
        byte[] continuation = virtualVehicle.getContinuation();

        if (continuation == null)
        {
            return;
        }

        TarArchiveEntry entry = new TarArchiveEntry("vv/vv-continuation.js");
        entry.setModTime(new Date());
        entry.setSize(continuation.length);
        entry.setIds(0, chunkNumber);
        entry.setNames("vvrte", "cpcc");

        os.putArchiveEntry(entry);
        os.write(continuation);
        os.closeArchiveEntry();
    }

    /**
     * @param virtualVehicle the virtual vehicle.
     * @param os the output stream to write to.
     * @param chunkNumber the chunk number.
     * @throws IOException thrown in case of errors.
     */
    private void writeVirtualVehicleSourceCode(VirtualVehicle virtualVehicle, ArchiveOutputStream os, int chunkNumber)
        throws IOException
    {
        byte[] source = virtualVehicle.getCode().getBytes("UTF-8");

        TarArchiveEntry entry = new TarArchiveEntry("vv/vv-source.js");
        entry.setModTime(new Date());
        entry.setSize(source.length);
        entry.setIds(0, chunkNumber);
        entry.setNames("vvrte", "cpcc");

        os.putArchiveEntry(entry);
        os.write(source);
        os.closeArchiveEntry();
    }

    /**
     * @param os the output stream to write to.
     * @throws IOException thrown in case of errors.
     */
    private void writeVirtualVehicleStorageChunk(VirtualVehicle virtualVehicle, ArchiveOutputStream os,
        int chunkNumber, List<VirtualVehicleStorage> storageChunk)
        throws IOException
    {
        for (VirtualVehicleStorage se : storageChunk)
        {
            LOG.info("Writing storage entry '" + se.getName() + "'");

            byte[] content = se.getContentAsByteArray();
            TarArchiveEntry entry = new TarArchiveEntry("storage/" + se.getName());
            entry.setModTime(se.getModificationTime());
            entry.setSize(content.length);
            entry.setIds(se.getId(), chunkNumber);
            entry.setNames("vvrte", "cpcc");

            os.putArchiveEntry(entry);
            os.write(content);
            os.closeArchiveEntry();
        }
    }

    /**
     * @param virtualVehicle the virtual vehicle.
     * @return the virtual vehicle properties.
     */
    private static Properties fillVirtualVehicleProps(VirtualVehicle virtualVehicle)
    {
        Properties props = new Properties();
        props.setProperty("name", virtualVehicle.getName());
        props.setProperty("uuid", virtualVehicle.getUuid());
        props.setProperty("api.version", Integer.toString(virtualVehicle.getApiVersion()));
        if (virtualVehicle.getStartTime() != null)
        {
            props.setProperty("start.time", Long.toString(virtualVehicle.getStartTime().getTime()));
        }
        if (virtualVehicle.getEndTime() != null)
        {
            props.setProperty("end.time", Long.toString(virtualVehicle.getEndTime().getTime()));
        }
        return props;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeChunk(InputStream inStream) throws ArchiveException, IOException
    {
        ArchiveStreamFactory f = new ArchiveStreamFactory();
        ArchiveInputStream ais = f.createArchiveInputStream("tar", inStream);
        VirtualVehicleHolder virtualVehicleHolder = new VirtualVehicleHolder();

        for (TarArchiveEntry entry = (TarArchiveEntry) ais.getNextEntry(); entry != null; entry =
            (TarArchiveEntry) ais.getNextEntry())
        {
            if (entry.getName().startsWith("vv/"))
            {
                storeVirtualVehicleEntry(ais, entry, virtualVehicleHolder);
            }
            else if (entry.getName().startsWith("storage/"))
            {
                storeStorageEntry(ais, entry, virtualVehicleHolder.getVirtualVehicle());
            }
            else
            {
                throw new IOException("Can not store unknown type of entry " + entry.getName());
            }
        }
    }

    /**
     * @param inStream the input stream containing a virtual vehicle chunk.
     * @param entry the previously read archive entry.
     * @param virtualVehicleHolder the holder object for the virtual vehicle read.
     * @throws IOException thrown in case of errors.
     */
    private void storeVirtualVehicleEntry(InputStream inStream, TarArchiveEntry entry,
        VirtualVehicleHolder virtualVehicleHolder) throws IOException
    {
        if ("vv/vv.properties".equals(entry.getName()))
        {
            Properties props = new Properties();
            props.load(inStream);

            VirtualVehicle vv = vvRepository.findVirtualVehicleByUUID(props.getProperty("uuid"));
            if (vv == null)
            {
                vv = new VirtualVehicle();
                vv.setName(props.getProperty("name"));
                vv.setUuid(props.getProperty("uuid"));
                vv.setApiVersion(Integer.parseInt(props.getProperty("api.version")));
                vv.setState(VirtualVehicleState.MIGRATING);
                String startTime = props.getProperty("start.time");
                if (startTime != null)
                {
                    vv.setStartTime(new Date(Long.parseLong(startTime)));
                }
                String endTime = props.getProperty("end.time");
                if (endTime != null)
                {
                    vv.setEndTime(new Date(Long.parseLong(endTime)));
                }
                vv.setMigrationDestination(null);
                vvRepository.saveOrUpdate(vv);
            }
            else
            {
                if (vv.getState() != VirtualVehicleState.MIGRATING)
                {
                    throw new IOException("Virtual vehicle " + vv.getName() + " (" + vv.getUuid() + ") has not state "
                        + VirtualVehicleState.MIGRATING);
                }
                if (vv.getMigrationDestination() != null)
                {
                    throw new IOException("Virtual vehicle " + vv.getName() + " (" + vv.getUuid()
                        + ") is being migrated and can not be a migration target.");
                }
            }
            virtualVehicleHolder.setVirtualVehicle(vv);
        }
        else if ("vv/vv-continuation.js".equals(entry.getName()))
        {
            byte[] continuation = IOUtils.toByteArray(inStream);
            virtualVehicleHolder.getVirtualVehicle().setContinuation(continuation);
        }
        else if ("vv/vv-source.js".equals(entry.getName()))
        {
            byte[] source = IOUtils.toByteArray(inStream);
            virtualVehicleHolder.getVirtualVehicle().setCode(new String(source, "UTF-8"));
        }
        else
        {
            throw new IOException("Can not store unknown virtual vehicle entry " + entry.getName());
        }
    }

    /**
     * @param inStream the input stream containing a virtual vehicle chunk.
     * @param entry the previously read archive entry.
     * @param virtualVehicleHolder the holder object for the virtual vehicle read.
     * @throws IOException thrown in case of errors.
     */
    private void storeStorageEntry(InputStream inStream, TarArchiveEntry entry, VirtualVehicle virtualVehicle)
        throws IOException
    {
        String name = entry.getName().substring(8, entry.getName().length());
        VirtualVehicleStorage item = vvRepository.findStorageItemByVirtualVehicleAndName(virtualVehicle, name);

        if (item == null)
        {
            item = new VirtualVehicleStorage();
            item.setName(name);
            item.setVirtualVehicle(virtualVehicle);
        }

        item.setModificationTime(entry.getModTime());
        item.setContentAsByteArray(IOUtils.toByteArray(inStream));

        vvRepository.saveOrUpdate(item);
    }

    /**
     * VirtualVehicleHolder
     */
    private static class VirtualVehicleHolder
    {
        private VirtualVehicle virtualVehicle;

        /**
         * @return the virtual vehicle.
         */
        public VirtualVehicle getVirtualVehicle()
        {
            return virtualVehicle;
        }

        /**
         * @param virtualVehicle the virtual vehicle to set.
         */
        public void setVirtualVehicle(VirtualVehicle virtualVehicle)
        {
            this.virtualVehicle = virtualVehicle;
        }
    }
}
