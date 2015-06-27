// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.rv.base.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.slf4j.Logger;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationResponse;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationResponse.Status;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationService;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.services.jobs.JobRunnable;
import at.uni_salzburg.cs.cpcc.core.services.jobs.TimeService;

import com.owlike.genson.Genson;

/**
 * Configuration synchronization job runnable.
 */
public class ConfigSyncJobRunnable implements JobRunnable
{
    private Session session;
    private Genson genson;
    private QueryManager qm;
    private int id;
    private CommunicationService com;
    private TimeService timeService;
    private Logger logger;

    /**
     * @param parameters the parameters.
     * @param session the Hibernate session.
     * @param qm the query manager.
     * @param com the communication service.
     * @param timeService the time service.
     * @param logger the application logger.
     */
    public ConfigSyncJobRunnable(Map<String, String> parameters, Session session, QueryManager qm
        , CommunicationService com, TimeService timeService, Logger logger)
    {
        this.session = session;
        this.qm = qm;
        this.com = com;
        this.timeService = timeService;
        this.logger = logger;

        id = Integer.parseInt(parameters.get("rv"));

        genson = new Genson();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() throws Exception
    {
        Date syncTime = timeService.newDate();

        RealVehicle target = qm.findRealVehicleById(id);
        byte[] data = prepareSyncData();

        CommunicationResponse res =
            com.transfer(target, RealVehicleBaseConstants.CONFIGURATION_UPDATE_CONNECTOR, data);

        if (res.getStatus() == Status.OK)
        {
            updateOwnConfig(res.getContent());
        }

        target.setLastUpdate(syncTime);
    }

    /**
     * @return the synchronization data.
     * @throws IOException in case of errors.
     */
    public byte[] prepareSyncData() throws IOException
    {
        ConfigSyncData data = new ConfigSyncData(qm.findAllSensorDefinitions(), qm.findAllRealVehicles());
        return genson.serializeBytes(data);
    }

    /**
     * @param content the content to be updated.
     * @return our own synchronization data.
     */
    public byte[] updateOwnConfig(byte[] content)
    {
        ConfigSyncData data = genson.deserialize(content, ConfigSyncData.class);

        return genson.serializeBytes(
            new ConfigSyncData(
                syncSensorDefinitionConfig(data.getSen()),
                syncRealVehicleConfig(data.getRvs()))
            );
    }

    /**
     * @param realVehicles the list of real vehicles.
     * @return the list of sent back real vehicles.
     */
    private List<RealVehicle> syncRealVehicleConfig(List<RealVehicle> realVehicles)
    {
        List<RealVehicle> allRvs = new ArrayList<RealVehicle>(qm.findAllRealVehicles());

        List<RealVehicle> back = new ArrayList<RealVehicle>();
        List<RealVehicle> incoming = new ArrayList<RealVehicle>();

        for (RealVehicle rv : realVehicles)
        {
            int rdId = rv.getId();
            RealVehicle dbRv = null;
            for (RealVehicle r : allRvs)
            {
                if (r.getId().intValue() == rdId)
                {
                    dbRv = r;
                    // logger.info("### found RealVehicle id=" + rdId);
                    break;
                }
            }

            boolean saveOnly = false;
            if (dbRv == null)
            {
                dbRv = new RealVehicle();
                dbRv.setId(rdId);
                dbRv.setLastUpdate(new Date(0));
                allRvs.add(dbRv);
                saveOnly = true;
                logger.info("### new RealVehicle id=" + rdId);
            }

            int updated = 0;
            try
            {
                updated = fillInNewerRealVehicle(dbRv, rv);
            }
            catch (RuntimeException e)
            {
                logger.error("Can not synchronize JSON object " + rv.toString(), e);
                continue;
            }

            incoming.add(dbRv);

            switch (updated)
            {
                case -1:
                    logger.info("### send back RealVehicle id=" + rdId);
                    back.add(dbRv);
                    break;
                case 1:
                    updateSensorDefinitions(dbRv, rv);
                    if (saveOnly)
                    {
                        logger.info("### save RealVehicle id=" + rdId);
                        session.save(dbRv);
                    }
                    else
                    {
                        logger.info("### update RealVehicle id=" + rdId);
                        session.saveOrUpdate(dbRv);
                    }
                    break;
                default:
                    break;
            }
        }

        deleteObsoleteRealVehicles(allRvs, incoming);
        return back;
    }

    /**
     * @param rv the real vehicle.
     * @param rvObj the other real vehicle.
     * @return 1 if an update has taken place, <= 0 if nothing has changed.
     */
    private int fillInNewerRealVehicle(RealVehicle rv, RealVehicle rvObj)
    {
        long lastUpdateNew = rvObj.getLastUpdate().getTime();
        long lastUpdateDb = rv.getLastUpdate().getTime();

        if (lastUpdateNew < lastUpdateDb)
        {
            return -1;
        }

        if (lastUpdateNew == lastUpdateDb)
        {
            return 0;
        }

        rv.setId(rvObj.getId());
        rv.setLastUpdate(rvObj.getLastUpdate());
        rv.setAreaOfOperation(rvObj.getAreaOfOperation());
        rv.setUrl(rvObj.getUrl());
        rv.setName(rvObj.getName());
        rv.setType(rvObj.getType());
        rv.setDeleted(rvObj.getDeleted());

        // JSONArray sensors = (JSONArray) rvObj.get(REAL_VEHICLE_SENSORS);
        //
        // for (int k = 0, l = sensors.length(); k < l; ++k)
        // {
        //     SensorDefinition sd = new SensorDefinition();
        //     sd.setId(sensors.getInt(k));
        //     rv.getSensors().add(sd);
        // }

        return 1;
    }

    /**
     * @param rv the new real vehicle as a JSON object.
     * @param dbRv the real vehicle currently in the database.
     */
    private void updateSensorDefinitions(RealVehicle dbRv, RealVehicle rv)
    {
        List<SensorDefinition> sensors = rv.getSensors();

        Set<Integer> assignedSdIds = new HashSet<Integer>();
        for (SensorDefinition s : dbRv.getSensors())
        {
            assignedSdIds.add(s.getId());
        }

        Set<Integer> newSdIds = new HashSet<Integer>();
        for (SensorDefinition sdx : sensors)
        {
            int sdId = sdx.getId();
            newSdIds.add(sdId);

            if (!assignedSdIds.contains(sdId))
            {
                SensorDefinition sd = qm.findSensorDefinitionById(sdId);
                dbRv.getSensors().add(sd);
            }
        }

        Set<SensorDefinition> removedSds = new HashSet<SensorDefinition>();
        for (SensorDefinition s : dbRv.getSensors())
        {
            if (!newSdIds.contains(s.getId()))
            {
                removedSds.add(s);
            }
        }

        dbRv.getSensors().removeAll(removedSds);
    }

    /**
     * @param allRvs all real vehicles this vehicle knows.
     * @param incoming the real vehicles transferred from the remote real vehicle.
     */
    private void deleteObsoleteRealVehicles(List<RealVehicle> allRvs, List<RealVehicle> incoming)
    {
        for (int k = 0, l = allRvs.size(); k < l; ++k)
        {
            RealVehicle dbItem = allRvs.get(k);
            boolean found = false;

            for (int j = 0, m = incoming.size(); j < m; ++j)
            {
                if (dbItem.getId().intValue() == incoming.get(j).getId().intValue())
                {
                    found = true;
                    break;
                }
            }

            if (!found)
            {
                logger.info("### remove RealVehicle id=" + dbItem.getId());
                dbItem.setDeleted(Boolean.TRUE);
                session.saveOrUpdate(dbItem);
            }
        }
    }

    /**
     * @param sensorDefs the sensor definitions to be synchronized with the local database.
     * @return the sensor definitions that are newer in the local database.
     */
    private List<SensorDefinition> syncSensorDefinitionConfig(List<SensorDefinition> sensorDefs)
    {
        List<SensorDefinition> allSds = qm.findAllSensorDefinitions();

        List<SensorDefinition> back = new ArrayList<SensorDefinition>();
        List<SensorDefinition> incoming = new ArrayList<SensorDefinition>();

        for (SensorDefinition sd : sensorDefs)
        {
            // SensorDefinition dbSd = qm.findSensorDefinitionById(sd.getInt(CoreJsonConverter.SENSOR_DEFINITION_ID));
            int sdId = sd.getId();
            SensorDefinition dbSd = null;
            for (SensorDefinition r : allSds)
            {
                if (r.getId().intValue() == sdId)
                {
                    dbSd = r;
                    // logger.info("### found SensorDefinition id=" + sdId + "      " + r.getLastUpdate());
                    break;
                }
            }

            boolean saveOnly = false;
            if (dbSd == null)
            {
                dbSd = new SensorDefinition();
                dbSd.setId(sdId);
                dbSd.setLastUpdate(new Date(0));
                allSds.add(dbSd);
                saveOnly = true;
                logger.info("### new SensorDefinition id=" + sdId);
            }

            int updated = 0;
            try
            {
                updated = fillInNewerSensorDefinition(dbSd, sd);
            }
            catch (RuntimeException e)
            {
                logger.error("Can not synchronize JSON object " + sd.toString(), e);
                continue;
            }

            incoming.add(dbSd);

            switch (updated)
            {
                case -1:
                    logger.info("### send back SensorDefinition id=" + sdId + " " + dbSd.getLastUpdate().toString());
                    back.add(dbSd);
                    break;
                case 1:
                    if (saveOnly)
                    {
                        logger.info("### save SensorDefinition id=" + sdId + " " + dbSd.getLastUpdate().toString());
                        session.save(dbSd);
                    }
                    else
                    {
                        logger.info("### update SensorDefinition id=" + sdId + " " + dbSd.getLastUpdate().toString());
                        session.saveOrUpdate(dbSd);
                    }
                    break;
                default:
                    logger.info("### do nothing SensorDefinition id=" + sdId + " " + dbSd.getLastUpdate().toString());
                    break;
            }
        }

        deleteObsoleteSensorDefinitions(allSds, incoming);
        return back;
    }

    /**
     * @param sensorDatabase the sensor definition from the database.
     * @param sensorNew the new sensor definition.
     * @return
     */
    private int fillInNewerSensorDefinition(SensorDefinition sensorDatabase, SensorDefinition sensorNew)
    {
        long lastUpdateNew = sensorNew.getLastUpdate().getTime();
        long lastUpdateDb = sensorDatabase.getLastUpdate().getTime();

        if (lastUpdateNew < lastUpdateDb)
        {
            return -1;
        }

        if (lastUpdateNew == lastUpdateDb)
        {
            return 0;
        }

        sensorDatabase.setId(sensorNew.getId());
        sensorDatabase.setDescription(sensorNew.getDescription());
        sensorDatabase.setType(sensorNew.getType());
        sensorDatabase.setMessageType(sensorNew.getMessageType());
        sensorDatabase.setVisibility(sensorNew.getVisibility());
        sensorDatabase.setParameters(sensorNew.getParameters());
        sensorDatabase.setLastUpdate(sensorNew.getLastUpdate());
        sensorDatabase.setDeleted(sensorNew.getDeleted());
        return 1;
    }

    /**
     * @param allSds all sensor definitions this vehicle knows.
     * @param incoming the sensor definitions transferred from the remote real vehicle.
     */
    private void deleteObsoleteSensorDefinitions(List<SensorDefinition> allSds
        , List<SensorDefinition> incoming)
    {
        for (int k = 0, l = allSds.size(); k < l; ++k)
        {
            SensorDefinition dbItem = allSds.get(k);
            boolean found = false;

            for (int j = 0, m = incoming.size(); j < m; ++j)
            {
                if (dbItem.getId().intValue() == incoming.get(j).getId().intValue())
                {
                    found = true;
                    break;
                }
            }

            if (!found)
            {
                logger.info("### delete SensorDefinition id=" + dbItem.getId());
                dbItem.setDeleted(Boolean.TRUE);
                session.saveOrUpdate(dbItem);
            }
        }
    }
}
