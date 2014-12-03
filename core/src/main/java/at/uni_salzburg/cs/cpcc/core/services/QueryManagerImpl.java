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
package at.uni_salzburg.cs.cpcc.core.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.criterion.Property;

import at.uni_salzburg.cs.cpcc.core.entities.Device;
import at.uni_salzburg.cs.cpcc.core.entities.DeviceType;
import at.uni_salzburg.cs.cpcc.core.entities.MappingAttributes;
import at.uni_salzburg.cs.cpcc.core.entities.MappingAttributesPK;
import at.uni_salzburg.cs.cpcc.core.entities.Parameter;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.entities.SensorVisibility;
import at.uni_salzburg.cs.cpcc.core.entities.Topic;

/**
 * QueryManagerImpl
 */
public class QueryManagerImpl extends AbstractRepository implements QueryManager
{
    private static final String DEVICE_ID = "deviceId";
    private static final String SENSOR_DESCRIPTION = "description";
    private static final String SENSOR_MESSAGETYPE = "messagetype";
    private static final String DEVICE_NAME = "name";
    private static final String TOPIC_ROOT = "topicRoot";
    private static final String REAL_VEHICLE_NAME = "name";
    private static final String REAL_VEHICLE_URL = "url";

    /**
     * @param session {@link Session}
     */
    public QueryManagerImpl(Session session)
    {
        super(session);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Device findDeviceByTopicRoot(String topicRoot)
    {
        return (Device) getSession()
            .createQuery("from Device where topicRoot = :topicRoot")
            .setString(TOPIC_ROOT, topicRoot)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Device> findAllDevices()
    {
        return (List<Device>) getSession()
            .createCriteria(Device.class)
            .addOrder(Property.forName(TOPIC_ROOT).asc())
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeviceType findDeviceTypeByName(String name)
    {
        return (DeviceType) getSession()
            .createQuery("from DeviceType where name = :name")
            .setString(DEVICE_NAME, name)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<DeviceType> findAllDeviceTypes()
    {
        return (List<DeviceType>) getSession().createCriteria(DeviceType.class)
            .addOrder(Property.forName(DEVICE_NAME).asc())
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parameter findParameterByName(String name)
    {
        return (Parameter) getSession()
            .createQuery("from Parameter where name = :name")
            .setString(DEVICE_NAME, name)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parameter findParameterByName(String name, String defaultValue)
    {
        Parameter parameter = findParameterByName(name);

        if (parameter == null)
        {
            parameter = new Parameter();
            parameter.setName(name);
            parameter.setValue(defaultValue);
            parameter.setSort(0);
        }

        return parameter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SensorDefinition findSensorDefinitionById(Integer id)
    {
        SensorDefinition x = (SensorDefinition) getSession()
            .createQuery("FROM SensorDefinition WHERE id = :id")
            .setInteger("id", id)
            .uniqueResult();
        return x;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<SensorDefinition> findSensorDefinitionsByMessageType(String messagetype)
    {
        List<SensorDefinition> x = (List<SensorDefinition>) getSession()
            .createQuery("from SensorDefinition where messagetype = :messagetype")
            .setString(SENSOR_MESSAGETYPE, messagetype)
            .list();
        return x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SensorDefinition findSensorDefinitionByDescription(String description)
    {
        SensorDefinition x = (SensorDefinition) getSession()
            .createQuery("from SensorDefinition where description = :description")
            .setString(SENSOR_DESCRIPTION, description)
            .uniqueResult();
        return x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date findLatestSensorDefinitionOrRealVehicleChangeDate()
    {
        Date sdd = (Date) getSession()
            .createQuery("select max(lastUpdate) from SensorDefinition")
            .uniqueResult();
        
        Date rvd = (Date) getSession()
            .createQuery("select max(lastUpdate) from RealVehicle")
            .uniqueResult();

        if (sdd != null && rvd != null)
        {
            return sdd.compareTo(rvd) > 1 ? sdd : rvd;
        }

        return sdd != null ? sdd : rvd != null ? rvd : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteSensorDefinitionById(Integer id)
    {
        getSession()
            .createQuery("DELETE FROM SensorDefinition WHERE id = :id")
            .setInteger("id", id);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RealVehicle> findAllRealVehicles()
    {
        return (List<RealVehicle>) getSession()
            .createQuery("FROM RealVehicle")
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RealVehicle> findAllRealVehiclesOrderByName()
    {
        return (List<RealVehicle>) getSession()
            .createQuery("FROM RealVehicle ORDER BY name")
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealVehicle findRealVehicleByName(String name)
    {
        return (RealVehicle) getSession()
            .createQuery("FROM RealVehicle WHERE name = :name")
            .setString(REAL_VEHICLE_NAME, name)
            .uniqueResult();
    }

    @Override
    public RealVehicle findRealVehicleByUrl(String url)
    {
        return (RealVehicle) getSession()
            .createQuery("FROM RealVehicle WHERE url = :url")
            .setString(REAL_VEHICLE_URL, url)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealVehicle findRealVehicleById(Integer id)
    {
        RealVehicle x = (RealVehicle) getSession()
            .createQuery("FROM RealVehicle WHERE id = :id")
            .setInteger("id", id)
            .uniqueResult();
        return x;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<SensorDefinition> findAllSensorDefinitions()
    {
        return (List<SensorDefinition>) getSession()
            .createCriteria(SensorDefinition.class)
            .addOrder(Property.forName("id").asc())
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<SensorDefinition> findAllVisibleSensorDefinitions()
    {
        return (List<SensorDefinition>) getSession()
            .createQuery("SELECT d "
                + "FROM SensorDefinition d "
                + "WHERE d.visibility != :visibility")
            .setString("visibility", SensorVisibility.NO_VV.toString())
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<SensorDefinition> findAllActiveSensorDefinitions()
    {
        return (List<SensorDefinition>) getSession()
            .createQuery("SELECT d "
                + "FROM SensorDefinition d, MappingAttributes m "
                + "WHERE m.sensorDefinition = d.id AND m.vvVisible = true AND d.visibility != :visibility")
            .setString("visibility", SensorVisibility.NO_VV.toString())
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MappingAttributes findMappingAttribute(Device device, Topic topic)
    {
        return (MappingAttributes) getSession()
            .createQuery("from MappingAttributes where pk.device.id = :deviceId and pk.topic.id = :topicId")
            .setInteger(DEVICE_ID, device.getId())
            .setInteger("topicId", topic.getId())
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<MappingAttributes> findMappingAttributesByDevice(Device device)
    {
        return (List<MappingAttributes>) getSession()
            .createQuery("from MappingAttributes where pk.device.id = :deviceId)")
            .setParameter(DEVICE_ID, device.getId())
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<MappingAttributes> findAllMappingAttributes()
    {
        return (List<MappingAttributes>) getSession()
            .createCriteria(MappingAttributes.class)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<MappingAttributes> findAllVvVisibleMappingAttributes()
    {
        return (List<MappingAttributes>) getSession()
            .createQuery("from MappingAttributes where vvVisible = true")
            .list();
    }

    /**
     * @param attributes the mapping attributes
     * @return
     */
    private static String getAttributesTopicPath(MappingAttributes attributes)
    {
        StringBuilder b = new StringBuilder(attributes.getPk().getDevice().getTopicRoot());
        String subPath = attributes.getPk().getTopic().getSubpath();
        if (subPath != null)
        {
            b.append("/").append(subPath);
        }
        return b.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, MappingAttributes> findAllMappingAttributesAsMap()
    {
        Map<String, MappingAttributes> attributeMap = new HashMap<String, MappingAttributes>();

        for (MappingAttributes attribute : findAllMappingAttributes())
        {
            attributeMap.put(getAttributesTopicPath(attribute), attribute);
        }
        return attributeMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MappingAttributes findMappingAttributesByTopic(String topic)
    {
        for (MappingAttributes attribute : findAllMappingAttributes())
        {
            if (getAttributesTopicPath(attribute).equals(topic))
            {
                return attribute;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveOrUpdateMappingAttributes(Device device)
    {
        Collection<MappingAttributes> attributeList;

        Set<Topic> topicSet = new HashSet<Topic>();
        topicSet.add(device.getType().getMainTopic());
        topicSet.addAll(device.getType().getSubTopics());

        attributeList = findMappingAttributesByDevice(device);

        if (attributeList == null)
        {
            attributeList = new ArrayList<MappingAttributes>();
        }

        for (MappingAttributes attribute : attributeList)
        {
            boolean found = false;
            for (Topic topic : topicSet)
            {
                if (topic.getId().intValue() == attribute.getPk().getTopic().getId().intValue())
                {
                    found = true;
                    break;
                }
            }

            if (!found)
            {
                delete(attribute);
            }
        }

        for (Topic topic : topicSet)
        {
            boolean found = false;

            for (MappingAttributes a : attributeList)
            {
                if (topic.getId().intValue() == a.getPk().getTopic().getId().intValue())
                {
                    found = true;
                    break;
                }
            }

            if (!found)
            {
                MappingAttributesPK pk = new MappingAttributesPK(device, topic);
                MappingAttributes newAttributes = new MappingAttributes();
                newAttributes.setPk(pk);
                newAttributes.setVvVisible(Boolean.FALSE);
                newAttributes.setConnectedToAutopilot(Boolean.FALSE);
                saveOrUpdate(newAttributes);
            }
        }
    }

}
