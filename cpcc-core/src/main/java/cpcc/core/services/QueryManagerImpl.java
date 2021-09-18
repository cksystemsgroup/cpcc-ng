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

package cpcc.core.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.hibernate.Session;

import cpcc.core.entities.Device;
import cpcc.core.entities.DeviceType;
import cpcc.core.entities.MappingAttributes;
import cpcc.core.entities.MappingAttributesPK;
import cpcc.core.entities.Parameter;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorVisibility;
import cpcc.core.entities.Topic;

/**
 * QueryManager implementation.
 */
public class QueryManagerImpl implements QueryManager
{
    private static final String DEVICE_ID = "deviceId";
    private static final String SENSOR_DESCRIPTION = "description";
    private static final String SENSOR_MESSAGETYPE = "messageType";
    private static final String NAME = "name";
    private static final String TOPIC_ROOT = "topicRoot";

    private HibernateSessionManager sessionManager;

    /**
     * @param session the Hibernate {@link Session}
     */
    public QueryManagerImpl(Session session, HibernateSessionManager sessionManager)
    {
        this.sessionManager = sessionManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Device findDeviceByTopicRoot(String topicRoot)
    {
        return sessionManager.getSession()
            .createQuery("FROM Device WHERE topicRoot = :topicRoot", Device.class)
            .setParameter(TOPIC_ROOT, topicRoot)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Device> findAllDevices()
    {
        return sessionManager.getSession()
            .createQuery("FROM Device ORDER BY topicRoot", Device.class)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeviceType findDeviceTypeByName(String name)
    {
        return sessionManager.getSession()
            .createQuery("FROM DeviceType WHERE name = :name", DeviceType.class)
            .setParameter(NAME, name)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DeviceType> findAllDeviceTypes()
    {
        return sessionManager.getSession()
            .createQuery("FROM DeviceType ORDER BY name", DeviceType.class)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Parameter findParameterByName(String name)
    {
        return sessionManager.getSession()
            .createQuery("FROM Parameter WHERE name = :name", Parameter.class)
            .setParameter(NAME, name)
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
        return sessionManager.getSession()
            .createQuery("FROM Parameter WHERE id = :id", SensorDefinition.class)
            .setParameter("id", id)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SensorDefinition> findSensorDefinitionsByMessageType(String messagetype)
    {
        return sessionManager.getSession()
            .createQuery("FROM SensorDefinition WHERE messageType = :messageType", SensorDefinition.class)
            .setParameter(SENSOR_MESSAGETYPE, messagetype)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SensorDefinition findSensorDefinitionByDescription(String description)
    {
        return sessionManager.getSession()
            .createQuery("FROM SensorDefinition WHERE description = :description", SensorDefinition.class)
            .setParameter(SENSOR_DESCRIPTION, description)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date findLatestSensorDefinitionOrRealVehicleChangeDate()
    {
        Date sdd = sessionManager.getSession()
            .createQuery("SELECT MAX(lastUpdate) FROM SensorDefinition", Date.class)
            .uniqueResult();

        Date rvd = sessionManager.getSession()
            .createQuery("SELECT MAX(lastUpdate) FROM RealVehicle", Date.class)
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
        sessionManager.getSession()
            .createQuery("DELETE FROM SensorDefinition WHERE id = :id")
            .setParameter("id", id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SensorDefinition> findAllSensorDefinitions()
    {
        return sessionManager.getSession()
            .createQuery("FROM SensorDefinition ORDER BY id", SensorDefinition.class)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SensorDefinition> findAllVisibleSensorDefinitions()
    {
        return sessionManager.getSession()
            .createQuery("FROM SensorDefinition WHERE visibility != :visibility", SensorDefinition.class)
            .setParameter("visibility", SensorVisibility.NO_VV)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SensorDefinition> findAllActiveSensorDefinitions()
    {
        return sessionManager.getSession()
            .createQuery("SELECT d "
                + "FROM SensorDefinition d, MappingAttributes m "
                + "WHERE m.sensorDefinition = d.id "
                + "AND m.vvVisible = true "
                + "AND d.visibility != :visibility", SensorDefinition.class)
            .setParameter("visibility", SensorVisibility.NO_VV.toString())
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MappingAttributes findMappingAttribute(Device device, Topic topic)
    {
        return sessionManager.getSession()
            .createQuery("FROM MappingAttributes "
                + "WHERE pk.device.id = :deviceId and pk.topic.id = :topicId", MappingAttributes.class)
            .setParameter(DEVICE_ID, device.getId())
            .setParameter("topicId", topic.getId())
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<MappingAttributes> findMappingAttributesByDevice(Device device)
    {
        return sessionManager.getSession()
            .createQuery("FROM MappingAttributes WHERE pk.device.id = :deviceId", MappingAttributes.class)
            .setParameter(DEVICE_ID, device.getId())
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MappingAttributes> findAllMappingAttributes()
    {
        return sessionManager.getSession()
            .createQuery("FROM MappingAttributes", MappingAttributes.class)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MappingAttributes> findAllVvVisibleMappingAttributes()
    {
        return sessionManager.getSession()
            .createQuery("FROM MappingAttributes WHERE vvVisible = TRUE)", MappingAttributes.class)
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
        Map<String, MappingAttributes> attributeMap = new HashMap<>();

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

        Set<Topic> topicSet = new HashSet<>();
        topicSet.add(device.getType().getMainTopic());
        topicSet.addAll(device.getType().getSubTopics());

        attributeList = findMappingAttributesByDevice(device);

        if (attributeList == null)
        {
            attributeList = new ArrayList<>();
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
                sessionManager.getSession().delete(attribute);
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
                sessionManager.getSession().saveOrUpdate(newAttributes);
            }
        }
    }

}
