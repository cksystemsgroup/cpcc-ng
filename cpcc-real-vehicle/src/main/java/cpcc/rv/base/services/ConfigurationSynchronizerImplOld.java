// This code is part of the CPCC-NG project.
//
// Copyright (c) 2014 Clemens Krainer <clemens.krainer@gmail.com>
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationResponse;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationResponse.Status;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationService;
import at.uni_salzburg.cs.cpcc.core.entities.Parameter;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.services.CoreJsonConverter;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.utils.JSONUtils;

/**
 * ConfigurationSynchronizerImpl
 */
public class ConfigurationSynchronizerImplOld implements ConfigurationSynchronizerOld, RealVehicleStateListener
{
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    // private static final int DB_CHANGE_TIMEOUT = 10;

    private Logger logger;
    private HibernateSessionManager sessionManager;
    private QueryManager qm;
    private CommunicationService com;
    private CoreJsonConverter jsonConv;
    private boolean synchronizationRunning = false;
    private boolean configurationChanged = false;
    private long configurationChangeTime = 0;
    private Map<Integer, Long> lastUpdateMap = new HashMap<Integer, Long>();
    private int hostingRealVehicleId = 0;

    /**
     * @param logger the application logger instance.
     * @param sessionManager the Hibernate session manager.
     * @param qm the query manager.
     * @param com the communication maanger.
     * @param jsonConv the core JSON converter service.
     */
    public ConfigurationSynchronizerImplOld(Logger logger, HibernateSessionManager sessionManager
        , QueryManager qm, CommunicationService com, CoreJsonConverter jsonConv)
    {
        this.logger = logger;
        this.sessionManager = sessionManager;
        this.qm = qm;
        this.com = com;
        this.jsonConv = jsonConv;
        //        stateSrv.addRealVehicleStateListener(this);

        determineHostingRealVehicleId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void synchronizeConfiguration()
    {
        if (configurationChanged)
        {
            configurationChanged = false;
            syncConfigWithTransaction(qm.findAllRealVehicles());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyConfigurationChange()
    {
        configurationChanged = true;
        configurationChangeTime = System.currentTimeMillis();
    }

    /**
     * Determine the hosting real vehicle identification, if necessary.
     */
    private boolean determineHostingRealVehicleId()
    {
        if (hostingRealVehicleId != 0)
        {
            return true;
        }

        Parameter param = qm.findParameterByName(Parameter.REAL_VEHICLE_NAME);

        if (param == null || StringUtils.isEmpty(param.getValue()))
        {
            return false;
        }

        RealVehicle rv = qm.findRealVehicleByName(param.getValue());

        if (rv == null)
        {
            return false;
        }

        hostingRealVehicleId = rv.getId();

        return hostingRealVehicleId != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notify(RealVehicleState realVehicleState)
    {
        if (!determineHostingRealVehicleId())
        {
            return;
        }

        RealVehicle rv = realVehicleState.getRealVehicle();
        if (rv.getId().intValue() == hostingRealVehicleId)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Not syncronizing configuration with hosting vehicle."
                    + rv.getName() + " (" + rv.getId() + ")");
            }
            return;
        }

        if (realVehicleState.isConnected())
        {
            syncConfigWithTransaction(Arrays.asList(rv));
        }
    }

    /**
     * @param targetList the real vehicles to synchronize the configuration with.
     */
    private void syncConfigWithTransaction(List<RealVehicle> targetList)
    {
        try
        {
            syncConfig(targetList);
            sessionManager.commit();
        }
        catch (Throwable e)
        {
            sessionManager.abort();
            logger.error("Can not synchronize configuration to other real vehicles.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void syncConfig(List<RealVehicle> targetList) throws IOException
    {
        Parameter rvNameParam = qm.findParameterByName(Parameter.REAL_VEHICLE_NAME);
        if (StringUtils.isEmpty(rvNameParam.getValue().trim()))
        {
            logger.error("Own real vehicle name not found in the real vehicle configuration. Synchronization aborted.");
            return;
        }

        synchronized (ConfigurationSynchronizerOld.class)
        {
            if (synchronizationRunning)
            {
                return;
            }

            synchronizationRunning = true;
        }

        try
        {
            byte[] data = prepareSyncData();
            logger.info("Synchronizing " + data.length + " bytes to the RVs");

            for (RealVehicle target : targetList)
            {
                Long lastSync = lastUpdateMap.get(target.getId());
                if (lastSync != null && configurationChangeTime < lastSync.longValue())
                {
                    continue;
                }

                if (target.getName().equals(rvNameParam.getValue()))
                {
                    logger.info("Not synchronizing to myself. " + target.getName());
                    continue;
                }

                try
                {
                    long syncTime = new Date().getTime();

                    CommunicationResponse res =
                        com.transfer(target, RealVehicleBaseConstants.CONFIGURATION_UPDATE_CONNECTOR, data);
                    if (res.getStatus() == Status.OK)
                    {
                        updateOwnConfig(res.getContent());
                    }

                    lastUpdateMap.put(target.getId(), syncTime);
                }
                catch (IOException e)
                {
                    logger.error("Can not synchronize RVs to " + target.getName() + " (" + target.getId() + ")");
                }
            }
        }
        finally
        {
            synchronizationRunning = false;
        }
    }

    /**
     * @return the synchronization data.
     * @throws IOException in case of errors.
     */
    private byte[] prepareSyncData() throws IOException
    {
        List<SensorDefinition> sdList = qm.findAllSensorDefinitions();
        JSONArray sensors = jsonConv.toJsonArray(sdList.toArray(new SensorDefinition[0]));

        List<RealVehicle> rvList = qm.findAllRealVehicles();
        JSONArray rvs = jsonConv.toJsonArray(true, rvList.toArray(new RealVehicle[0]));

        JSONObject o = new JSONObject();
        o.put("sen", sensors);
        o.put("rvs", rvs);

        // TODO delete this?
        //{
        //    ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        //    Writer osw = new OutputStreamWriter(bos, "UTF-8");
        //    PrintWriter writer = new PrintWriter(osw);
        //    o.print(writer, true);
        //    writer.close();
        //    osw.close();
        //    bos.close();
        //    System.out.println("#### prepareSyncData: buggerit: " + new String(bos.toByteArray(), "UTF-8"));
        //}

        return JSONUtils.toByteArray(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] updateOwnConfig(byte[] content) throws IOException
    {
        if (content == null)
        {
            logger.info("updateOwnConfig: no data!");
            return EMPTY_BYTE_ARRAY;
        }

        logger.info("updateOwnConfig: data length=" + content.length);

        String jsonString = new String(content, "UTF-8");
        if (StringUtils.isEmpty(jsonString))
        {
            return EMPTY_BYTE_ARRAY;
        }

        JSONObject o = new JSONObject(jsonString);
        JSONObject back = new JSONObject();

        JSONArray sensors = (JSONArray) o.get("sen");
        JSONArray sensorsBack = syncSensorDefinitionConfig(sensors);
        back.put("sen", sensorsBack);

        JSONArray realVehicles = (JSONArray) o.get("rvs");
        JSONArray realVehiclesBack = syncRealVehicleConfig(realVehicles);
        back.put("rvs", realVehiclesBack);

        return sensorsBack.length() == 0 && realVehiclesBack.length() == 0
            ? EMPTY_BYTE_ARRAY
            : JSONUtils.toByteArray(back);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized JSONArray syncSensorDefinitionConfig(JSONArray sensorDefs)
    {
        List<SensorDefinition> allSds = qm.findAllSensorDefinitions();

        List<SensorDefinition> back = new ArrayList<SensorDefinition>();
        List<SensorDefinition> incoming = new ArrayList<SensorDefinition>();

        for (int k = 0, l = sensorDefs.length(); k < l; ++k)
        {
            JSONObject sd = sensorDefs.getJSONObject(k);

            // SensorDefinition dbSd = qm.findSensorDefinitionById(sd.getInt(CoreJsonConverter.SENSOR_DEFINITION_ID));
            int sdId = sd.getInt(CoreJsonConverter.SENSOR_DEFINITION_ID);
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
                updated = jsonConv.fillInNewerSensorDefinitionFromJsonObject(dbSd, sd);
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
                        sessionManager.getSession().save(dbSd);
                    }
                    else
                    {
                        logger.info("### update SensorDefinition id=" + sdId + " " + dbSd.getLastUpdate().toString());
                        sessionManager.getSession().saveOrUpdate(dbSd);
                    }
                    break;
                default:
                    logger.info("### do nothing SensorDefinition id=" + sdId + " " + dbSd.getLastUpdate().toString());
                    break;
            }
        }

        deleteObsoleteSensorDefinitions(allSds, incoming);

        return jsonConv.toJsonArray(back.toArray(new SensorDefinition[back.size()]));
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
                sessionManager.getSession().saveOrUpdate(dbItem);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized JSONArray syncRealVehicleConfig(JSONArray realVehicles)
    {
        List<RealVehicle> allRvs = new ArrayList<RealVehicle>(qm.findAllRealVehicles());

        List<RealVehicle> back = new ArrayList<RealVehicle>();
        List<RealVehicle> incoming = new ArrayList<RealVehicle>();

        for (int k = 0, l = realVehicles.length(); k < l; ++k)
        {
            JSONObject rv = realVehicles.getJSONObject(k);
            int rdId = rv.getInt(CoreJsonConverter.REAL_VEHICLE_ID);
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
                updated = jsonConv.fillInNewerRealVehicleFromJsonObject(dbRv, rv);
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
                        sessionManager.getSession().save(dbRv);
                    }
                    else
                    {
                        logger.info("### update RealVehicle id=" + rdId);
                        sessionManager.getSession().saveOrUpdate(dbRv);
                    }
                    break;
                default:
                    break;
            }
        }

        deleteObsoleteRealVehicles(allRvs, incoming);

        return jsonConv.toJsonArray(true, back.toArray(new RealVehicle[back.size()]));
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
                sessionManager.getSession().saveOrUpdate(dbItem);
            }
        }
    }

    /**
     * @param rv the new real vehicle as a JSON object.
     * @param dbRv the real vehicle currently in the database.
     */
    private void updateSensorDefinitions(RealVehicle dbRv, JSONObject rv)
    {
        JSONArray sensors = (JSONArray) rv.get(CoreJsonConverter.REAL_VEHICLE_SENSORS);

        Set<Integer> assignedSdIds = new HashSet<Integer>();
        for (SensorDefinition s : dbRv.getSensors())
        {
            assignedSdIds.add(s.getId());
        }

        Set<Integer> newSdIds = new HashSet<Integer>();
        for (int m = 0, i = sensors.length(); m < i; ++m)
        {
            int sdId = sensors.getInt(m);
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

}
