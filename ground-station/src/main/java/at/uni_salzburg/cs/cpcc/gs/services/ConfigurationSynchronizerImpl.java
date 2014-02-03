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
package at.uni_salzburg.cs.cpcc.gs.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationRequest.Connector;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationResponse;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationResponse.Status;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationService;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.services.CoreJsonConverter;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.services.TimerService;
import at.uni_salzburg.cs.cpcc.core.utils.JSONUtils;

/**
 * ConfigurationSynchronizerImpl
 */
public class ConfigurationSynchronizerImpl extends TimerTask implements ConfigurationSynchronizer
{
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationSynchronizerImpl.class);

    private QueryManager qm;
    private CommunicationService com;
    private CoreJsonConverter jsonConv;

    /**
     * @param timerService the timer service.
     * @param qm the query service.
     * @param com the communication service.
     * @param jsonConv the core JSON converter service.
     */
    public ConfigurationSynchronizerImpl(TimerService timerService, QueryManager qm, CommunicationService com,
        CoreJsonConverter jsonConv)
    {
        this.qm = qm;
        this.com = com;
        this.jsonConv = jsonConv;
        timerService.periodicSchedule(this, 10000, 1000);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void syncConfig(List<RealVehicle> targetList) throws IOException
    {
        byte[] data = prepareSyncData();

        for (RealVehicle target : targetList)
        {
            try
            {
                CommunicationResponse res = com.transfer(target, Connector.CONFIGURATION_UPDATE, data);
                if (res.getStatus() == Status.OK)
                {
                    updateOwnConfig(res.getContent());
                }
            }
            catch (IOException e)
            {
                LOG.error("Can not synchronize RVs to " + target.getName() + " (" + target.getId() + ")");
            }
        }
    }

    /**
     * @return
     * @throws IOException
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

        // TODO delete this
        //{
        //    ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        //    Writer osw = new OutputStreamWriter(bos, "UTF-8");
        //    PrintWriter writer = new PrintWriter(osw);
        //    o.print(writer, true);
        //    writer.close();
        //    osw.close();
        //    bos.close();
        //    System.out.println("prepareSyncData: buggerit: " + new String(bos.toByteArray(), "UTF-8"));
        //}

        return JSONUtils.toByteArray(o);
    }

    /**
     * @param content the parameters to be updated in this vehicle's configuration.
     * @throws IOException
     */
    private byte[] updateOwnConfig(byte[] content) throws IOException
    {
        if (content == null)
        {
            return null;
        }

        String jsonString = new String(content, "UTF-8");
        if (StringUtils.isEmpty(jsonString))
        {
            return null;
        }

        JSONObject o = new JSONObject(jsonString);
        JSONObject back = new JSONObject();

        JSONArray sensors = (JSONArray) o.get("sen");
        JSONArray sensorsBack = syncSensorDefinitionConfig(sensors);
        back.put("sen", sensorsBack);

        JSONArray realVehicles = (JSONArray) o.get("rvs");
        JSONArray realVehiclesBack = syncRealVehicleConfig(realVehicles);
        back.put("rvs", realVehiclesBack);

        return sensorsBack.length() == 0 && realVehiclesBack.length() == 0 ? null : JSONUtils.toByteArray(back);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray syncSensorDefinitionConfig(JSONArray sensorDefs)
    {
        List<SensorDefinition> back = new ArrayList<SensorDefinition>();
        List<SensorDefinition> incoming = new ArrayList<SensorDefinition>();

        for (int k = 0, l = sensorDefs.length(); k < l; ++k)
        {
            JSONObject sd = sensorDefs.getJSONObject(k);

            SensorDefinition newSd = jsonConv.toSensorDefinition(sd);
            incoming.add(newSd);

            SensorDefinition dbSd = qm.findSensorDefinitionById(newSd.getId());
            if (dbSd == null)
            {
                qm.saveOrUpdate(newSd);
            }
            else
            {
                long lastUpdateNew = newSd.getLastUpdate().getTime();
                long lastUpdateDb = dbSd.getLastUpdate().getTime();

                if (lastUpdateNew > lastUpdateDb)
                {
                    dbSd.setDescription(newSd.getDescription());
                    dbSd.setLastUpdate(newSd.getLastUpdate());
                    dbSd.setMessageType(newSd.getMessageType());
                    dbSd.setParameters(newSd.getParameters());
                    dbSd.setType(newSd.getType());
                    dbSd.setVisibility(newSd.getVisibility());
                    qm.saveOrUpdate(dbSd);
                }
                else if (lastUpdateNew < lastUpdateDb)
                {
                    back.add(dbSd);
                }
            }
        }

        List<SensorDefinition> allSds = qm.findAllSensorDefinitions();
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
                qm.delete(dbItem);
            }
        }

        return jsonConv.toJsonArray(back.toArray(new SensorDefinition[0]));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray syncRealVehicleConfig(JSONArray realVehicles)
    {
        List<RealVehicle> back = new ArrayList<RealVehicle>();
        List<RealVehicle> incoming = new ArrayList<RealVehicle>();

        for (int k = 0, l = realVehicles.length(); k < l; ++k)
        {
            JSONObject rv = realVehicles.getJSONObject(k);

            RealVehicle newRv = jsonConv.toRealVehicle(rv);
            incoming.add(newRv);

            RealVehicle dbRv = qm.findRealVehicleById(newRv.getId());
            if (dbRv == null)
            {
                fillInSensorDefinitions(newRv);
                qm.saveOrUpdate(newRv);
            }
            else
            {
                long lastUpdateNew = newRv.getLastUpdate().getTime();
                long lastUpdateDb = dbRv.getLastUpdate().getTime();

                if (lastUpdateNew > lastUpdateDb)
                {
                    dbRv.setLastUpdate(newRv.getLastUpdate());
                    dbRv.setType(newRv.getType());
                    dbRv.setAreaOfOperation(newRv.getAreaOfOperation());
                    dbRv.setName(newRv.getName());
                    dbRv.setUrl(newRv.getUrl());
                    copySensorDefinitions(newRv, dbRv);
                    qm.saveOrUpdate(dbRv);
                }
                else if (lastUpdateNew < lastUpdateDb)
                {
                    back.add(dbRv);
                }
            }
        }

        List<RealVehicle> allRvs = qm.findAllRealVehicles();
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
                qm.delete(dbItem);
            }
        }

        return jsonConv.toJsonArray(true, back.toArray(new RealVehicle[0]));
    }

    /**
     * Copy the dummy sensor definitions from a source real vehicle and replace them by sensor definitions from the
     * database.
     * 
     * @param srcRv the source real vehicle.
     * @param dstRv the destination real vehicle.
     */
    private void copySensorDefinitions(RealVehicle srcRv, RealVehicle dstRv)
    {
        dstRv.setSensors(srcRv.getSensors());
        fillInSensorDefinitions(dstRv);
    }

    /**
     * Replace the dummy sensor definitions by sensor definitions from the database.
     * 
     * @param rv the real vehicle.
     */
    private void fillInSensorDefinitions(RealVehicle rv)
    {
        for (int m = 0, o = rv.getSensors().size(); m < o; ++m)
        {
            Integer id = rv.getSensors().get(m).getId();
            SensorDefinition sd = qm.findSensorDefinitionById(id);
            rv.getSensors().set(m, sd);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        //        if (lastRealVehicleUpdate == null)
        //        {
        //            syncRealVehicles();
        //            lastRealVehicleUpdate = new Date();
        //        }
        //
        //        if (lastSensorDefinitionUpdate == null)
        //        {
        //            syncSensorDefinitions();
        //            lastSensorDefinitionUpdate = new Date();
        //        }
    }

}
