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

package cpcc.vvrte.services;

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
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.slf4j.Logger;

import cpcc.core.entities.Parameter;
import cpcc.core.services.QueryManager;
import cpcc.core.utils.PropertyUtils;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.entities.VirtualVehicleStorage;

/**
 * VirtualVehicleMigratorImpl
 */
public class VirtualVehicleMigratorImpl implements VirtualVehicleMigrator
{
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private Logger logger;
    private HibernateSessionManager sessionManager;
    private VvRteRepository vvRepository;
    // private Set<VirtualVehicleListener> listenerSet = new HashSet<VirtualVehicleListener>();
    private QueryManager qm;
    private VirtualVehicleLauncher launcher;
    private ServiceResources serviceResources;

    /**
     * @param logger the application logger.
     * @param serviceResources the service resources.
     * @param sessionManager the Hibernate session manager.
     * @param vvRepository the virtual vehicle repository.
     * @param qm the query manager.
     * @param launcher the virtual vehicle launcher.
     */
    public VirtualVehicleMigratorImpl(Logger logger, ServiceResources serviceResources
        , HibernateSessionManager sessionManager, VvRteRepository vvRepository, QueryManager qm
        , VirtualVehicleLauncher launcher)
    {
        this.logger = logger;
        this.serviceResources = serviceResources;
        this.sessionManager = sessionManager;
        this.vvRepository = vvRepository;
        this.qm = qm;
        this.launcher = launcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initiateMigration(VirtualVehicle vehicle)
    {
        VvMigrationWorker worker = new VvMigrationWorker(logger, serviceResources, vehicle.getId());
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
            logger.info("Writing storage entry '" + se.getName() + "'");

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
        // props.setProperty("api.version", Integer.toString(virtualVehicle.getApiVersion()));
        PropertyUtils.setProperty(props, "api.version", virtualVehicle.getApiVersion());
        PropertyUtils.setProperty(props, "state", virtualVehicle.getPreMigrationState());

        if (virtualVehicle.getStartTime() != null)
        {
            PropertyUtils.setProperty(props, "start.time", virtualVehicle.getStartTime().getTime());
            // props.setProperty("start.time", Long.toString(virtualVehicle.getStartTime().getTime()));
        }

        if (virtualVehicle.getEndTime() != null)
        {
            PropertyUtils.setProperty(props, "end.time", virtualVehicle.getEndTime().getTime());
            // props.setProperty("end.time", Long.toString(virtualVehicle.getEndTime().getTime()));
        }

        if (lastChunk)
        {
            PropertyUtils.setProperty(props, "last.chunk", Boolean.TRUE);
            // props.setProperty("last.chunk", Boolean.TRUE.toString());
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
        String chunkName = "unknown-" + System.currentTimeMillis();
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
                logMigratedChunk(chunkName, virtualVehicleHolder.getVirtualVehicle());
            }
            else if (chunkName.startsWith("storage/"))
            {
                storeStorageEntry(ais, entry, virtualVehicleHolder.getVirtualVehicle());
                logMigratedChunk(chunkName, virtualVehicleHolder.getVirtualVehicle());
            }
            // TODO message queue
            else
            {
                throw new IOException("Can not store unknown type of entry " + chunkName);
            }
        }

        sessionManager.commit();

        if (lastChunk)
        {
            VirtualVehicle vv = virtualVehicleHolder.getVirtualVehicle();
            launcher.stateChange(vv.getId(), vv.getState());
        }

        return chunkName;
    }

    /**
     * @param chunkName the name of the migrated chunk.
     * @param virtualVehicleHolder the virtual vehicle.
     */
    private void logMigratedChunk(String chunkName, VirtualVehicle virtualVehicle)
    {
        String name = virtualVehicle != null ? " name=" + virtualVehicle.getName() : "";
        logger.info("Migration of " + chunkName + name);
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
            sessionManager.getSession().saveOrUpdate(vv);
        }
        else if ("vv/vv-continuation.js".equals(entry.getName()))
        {
            byte[] continuation = IOUtils.toByteArray(inStream);
            virtualVehicleHolder.getVirtualVehicle().setContinuation(continuation);
            sessionManager.getSession().saveOrUpdate(virtualVehicleHolder.getVirtualVehicle());
        }
        else if ("vv/vv-source.js".equals(entry.getName()))
        {
            byte[] source = IOUtils.toByteArray(inStream);
            virtualVehicleHolder.getVirtualVehicle().setCode(new String(source, "UTF-8"));
            sessionManager.getSession().saveOrUpdate(virtualVehicleHolder.getVirtualVehicle());
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
        if (!VirtualVehicleState.VV_STATES_FOR_MIGRATION.contains(vv.getState()))
        {
            throw new IOException("Virtual vehicle " + vv.getName() + " (" + vv.getUuid() + ") "
                + "has not state " + VirtualVehicleState.MIGRATING + " but " + vv.getState());
        }

        if (vv.getMigrationDestination() != null)
        {
            throw new IOException("Virtual vehicle " + vv.getName() + " (" + vv.getUuid() + ") "
                + "is being migrated and can not be a migration target.");
        }

        logger.info("pre-migration:  " + vv.getName() + " (" + vv.getUuid() + ") " + vv.getState() + " last="
            + lastChunk);

        if (lastChunk)
        {
            VirtualVehicleState newState =
                VirtualVehicleState.VV_NO_CHANGE_AFTER_MIGRATION.contains(vv.getPreMigrationState())
                    ? vv.getPreMigrationState()
                    : VirtualVehicleState.MIGRATION_COMPLETED;

            vv.setState(newState);
            vv.setPreMigrationState(null);
        }
        //        else
        //        {
        //            // TODO check if necessary.
        //            vv.setState(VirtualVehicleState.MIGRATING);
        //        }
        sessionManager.getSession().saveOrUpdate(vv);

        logger.info("post-migration: " + vv.getName() + " (" + vv.getUuid() + ") " + vv.getState() + " last="
            + lastChunk);
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

        if (lastChunk)
        {
            vv.setState(getVehicleState(props.getProperty("state")));
        }
        else
        {
            vv.setState(VirtualVehicleState.MIGRATING);
            vv.setPreMigrationState(getVehicleState(props.getProperty("state")));
        }

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
        sessionManager.getSession().saveOrUpdate(vv);
        return vv;
    }

    /**
     * @param stateString the state as a {@code String}.
     * @return the state as an enumeration.
     */
    private VirtualVehicleState getVehicleState(String stateString)
    {
        if (StringUtils.isBlank(stateString))
        {
            return null;
        }

        return VirtualVehicleState.valueOf(stateString);
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

        sessionManager.getSession().saveOrUpdate(item);
    }

    //    @Override
    //    public void addListener(VirtualVehicleListener listener)
    //    {
    //        listenerSet.add(listener);
    //    }
    //
    //    private void notifyListeners(VirtualVehicle vehicle)
    //    {
    //        for (VirtualVehicleListener listener : listenerSet)
    //        {
    //            logger.info("Notifying listener " + listener.getClass());
    //            listener.notify(vehicle);
    //        }
    //    }

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
