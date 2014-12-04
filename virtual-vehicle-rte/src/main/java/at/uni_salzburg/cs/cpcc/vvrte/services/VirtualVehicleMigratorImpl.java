/*
 * This code is part of the CPCC-NG project. Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com> This program
 * is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.vvrte.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationService;
import at.uni_salzburg.cs.cpcc.core.entities.Parameter;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleState;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleStorage;

/**
 * VirtualVehicleMigratorImpl
 */
public class VirtualVehicleMigratorImpl implements VirtualVehicleMigrator
{
    private static final Logger LOG = LoggerFactory.getLogger(VirtualVehicleMigratorImpl.class);

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    
    private VvRteRepository vvRepository;
    private CommunicationService com;
    private Set<VirtualVehicleListener> listenerSet = new HashSet<VirtualVehicleListener>();
    private QueryManager qm;
    private Session session;

    /**
     * @param vvRepository the virtual vehicle repository.
     * @param com the communication service.
     */
    public VirtualVehicleMigratorImpl(VvRteRepository vvRepository, CommunicationService com, QueryManager qm
        , Session session)
    {
        this.vvRepository = vvRepository;
        this.com = com;
        this.qm = qm;
        this.session = session;
    }

    @Override
    public void initiateMigration(VirtualVehicle vehicle)
    {
        VvMigrationWorker worker = new VvMigrationWorker(vehicle, vvRepository, com, this, session);
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

        writeVirtualVehicleProperties(virtualVehicle, outStream, 0, false);

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
        Parameter paramChunkSize = qm.findParameterByName(Parameter.VIRTUAL_VEHICLE_MIGRATION_CHUNK_SIZE, "10");

        int chunkSize = Integer.parseInt(paramChunkSize.getValue());

        String name = lastStorageName != null && lastStorageName.startsWith("storage/")
            ? lastStorageName.substring(8) : "";

        List<VirtualVehicleStorage> storageChunk =
            vvRepository.findStorageItemsByVirtualVehicle(virtualVehicle.getId(), name, chunkSize);

        if (storageChunk.size() == 0 && chunkNumber > 1)
        {
            virtualVehicle.setState(VirtualVehicleState.MIGRATION_COMPLETED);
            return EMPTY_BYTE_ARRAY;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.setEntryEncoding("UTF-8");

        boolean lastChunk = storageChunk.size() == 0 || storageChunk.size() < chunkSize;

        ArchiveOutputStream outStream = factory.createArchiveOutputStream("tar", baos);

        writeVirtualVehicleProperties(virtualVehicle, outStream, chunkNumber, lastChunk);
        if (chunkNumber == 1)
        {
            writeVirtualVehicleSourceCode(virtualVehicle, outStream, chunkNumber);
            writeVirtualVehicleContinuation(virtualVehicle, outStream, chunkNumber);
        }

        writeVirtualVehicleStorageChunk(virtualVehicle, outStream, chunkNumber, storageChunk);

        outStream.close();
        baos.close();

        if (lastChunk)
        {
            virtualVehicle.setState(VirtualVehicleState.MIGRATION_COMPLETED);
        }

        return baos.toByteArray();
    }

    /**
     * @param virtualVehicle the virtual vehicle.
     * @param os the output stream to write to.
     * @param chunkNumber the chunk number.
     * @throws IOException thrown in case of errors.
     */
    private void writeVirtualVehicleProperties(VirtualVehicle virtualVehicle, ArchiveOutputStream os, int chunkNumber,
        boolean lastChunk) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Properties virtualVehicleProps = fillVirtualVehicleProps(virtualVehicle, lastChunk);
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
     * @param lastChunk true if this property object is going to be transferred in the last data chunk.
     * @return the virtual vehicle properties.
     */
    private static Properties fillVirtualVehicleProps(VirtualVehicle virtualVehicle, boolean lastChunk)
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
        if (lastChunk)
        {
            props.setProperty("last.chunk", Boolean.TRUE.toString());
        }
        return props;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String storeChunk(InputStream inStream) throws ArchiveException, IOException
    {
        boolean lastChunk = false;
        String chunkName = null;
        ArchiveStreamFactory f = new ArchiveStreamFactory();
        ArchiveInputStream ais = f.createArchiveInputStream("tar", inStream);
        VirtualVehicleHolder virtualVehicleHolder = new VirtualVehicleHolder();

        for (TarArchiveEntry entry = (TarArchiveEntry) ais.getNextEntry(); entry != null; entry =
            (TarArchiveEntry) ais.getNextEntry())
        {
            chunkName = entry.getName();

            if (chunkName.startsWith("vv/"))
            {
                lastChunk |= storeVirtualVehicleEntry(ais, entry, virtualVehicleHolder);
                String name = virtualVehicleHolder.getVirtualVehicle() != null
                    ? " name=" + virtualVehicleHolder.getVirtualVehicle().getName() : "";
                LOG.info("Migration of " + chunkName + name);
            }
            else if (chunkName.startsWith("storage/"))
            {
                storeStorageEntry(ais, entry, virtualVehicleHolder.getVirtualVehicle());
                String name = virtualVehicleHolder.getVirtualVehicle() != null
                    ? " name=" + virtualVehicleHolder.getVirtualVehicle().getName() : "";
                LOG.info("Migration of " + chunkName + name);
            }
            // TODO message queue
            else
            {
                throw new IOException("Can not store unknown type of entry " + chunkName);
            }
        }

        if (lastChunk)
        {
            notifyListeners(virtualVehicleHolder.getVirtualVehicle());
        }

        return chunkName;
    }

    /**
     * @param inStream the input stream containing a virtual vehicle chunk.
     * @param entry the previously read archive entry.
     * @param virtualVehicleHolder the holder object for the virtual vehicle read.
     * @throws IOException thrown in case of errors.
     */
    private boolean storeVirtualVehicleEntry(InputStream inStream, TarArchiveEntry entry,
        VirtualVehicleHolder virtualVehicleHolder) throws IOException
    {
        boolean lastChunk = false;

        if ("vv/vv.properties".equals(entry.getName()))
        {
            Properties props = new Properties();
            props.load(inStream);

            lastChunk |= Boolean.parseBoolean(props.getProperty("last.chunk", "false"));

            VirtualVehicle vv = vvRepository.findVirtualVehicleByUUID(props.getProperty("uuid"));
            if (vv == null)
            {
                vv = createVirtualVehicle(lastChunk, props);
            }
            else
            {
                updateVirtualVehicle(lastChunk, vv);
            }

            virtualVehicleHolder.setVirtualVehicle(vv);
        }
        else if ("vv/vv-continuation.js".equals(entry.getName()))
        {
            byte[] continuation = IOUtils.toByteArray(inStream);
            virtualVehicleHolder.getVirtualVehicle().setContinuation(continuation);
            session.saveOrUpdate(virtualVehicleHolder.getVirtualVehicle());
        }
        else if ("vv/vv-source.js".equals(entry.getName()))
        {
            byte[] source = IOUtils.toByteArray(inStream);
            virtualVehicleHolder.getVirtualVehicle().setCode(new String(source, "UTF-8"));
            session.saveOrUpdate(virtualVehicleHolder.getVirtualVehicle());
        }
        else
        {
            throw new IOException("Can not store unknown virtual vehicle entry " + entry.getName());
        }

        return lastChunk;
    }

    /**
     * @param lastChunk true if this is the last migration chunk.
     * @param vv the virtual vehicle
     * @throws IOException thrown in case of errors.
     */
    private void updateVirtualVehicle(boolean lastChunk, VirtualVehicle vv) throws IOException
    {
        if (vv.getState() != VirtualVehicleState.MIGRATING
            && vv.getState() != VirtualVehicleState.MIGRATION_INTERRUPTED)
        {
            throw new IOException("Virtual vehicle " + vv.getName() + " (" + vv.getUuid() + ") "
                + "has not state " + VirtualVehicleState.MIGRATING + " but " + vv.getState());
        }

        if (vv.getMigrationDestination() != null)
        {
            throw new IOException("Virtual vehicle " + vv.getName() + " (" + vv.getUuid() + ") "
                + "is being migrated and can not be a migration target.");
        }

        vv.setState(lastChunk ? VirtualVehicleState.MIGRATION_COMPLETED : VirtualVehicleState.MIGRATING);
        session.saveOrUpdate(vv);

        LOG.info("migration: " + vv.getName() + " (" + vv.getUuid() + ") " + vv.getState() + " last=" + lastChunk);
    }

    /**
     * @param lastChunk true if this is the last migration chunk.
     * @param props the virtual vehicle properties.
     * @return the created virtual vehicle
     */
    private VirtualVehicle createVirtualVehicle(boolean lastChunk, Properties props)
    {
        VirtualVehicle vv;
        vv = new VirtualVehicle();
        vv.setName(props.getProperty("name"));
        vv.setUuid(props.getProperty("uuid"));
        vv.setApiVersion(Integer.parseInt(props.getProperty("api.version")));
        vv.setState(lastChunk ? VirtualVehicleState.MIGRATION_COMPLETED : VirtualVehicleState.MIGRATING);

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
        session.saveOrUpdate(vv);
        return vv;
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

        session.saveOrUpdate(item);
    }

    @Override
    public void addListener(VirtualVehicleListener listener)
    {
        listenerSet.add(listener);
    }

    private void notifyListeners(VirtualVehicle vehicle)
    {
        for (VirtualVehicleListener listener : listenerSet)
        {
            listener.notify(vehicle);
        }
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
