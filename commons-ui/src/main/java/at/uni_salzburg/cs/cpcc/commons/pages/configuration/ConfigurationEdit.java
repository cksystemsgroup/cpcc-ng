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
package at.uni_salzburg.cs.cpcc.commons.pages.configuration;

import static org.apache.tapestry5.EventConstants.PREPARE;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import at.uni_salzburg.cs.cpcc.core.entities.Device;
import at.uni_salzburg.cs.cpcc.core.entities.MappingAttributes;
import at.uni_salzburg.cs.cpcc.core.entities.Parameter;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.entities.Topic;
import at.uni_salzburg.cs.cpcc.core.entities.TopicCategory;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.services.SensorDefinitionSelectHelpers;
import at.uni_salzburg.cs.cpcc.ros.services.RosNodeService;
import at.uni_salzburg.cs.cpcc.vvrte.services.VirtualVehicleMapper;

/**
 * Configuration
 */
public class ConfigurationEdit
{
    @Inject
    private QueryManager qm;

    @Inject
    private RosNodeService nodeService;

    @Inject
    private VirtualVehicleMapper mapper;

    @Property
    private Parameter internalRosCore;

    @Property
    private Parameter masterServerURI;

    @Property
    private Parameter realVehicleName;

    @Property
    private RealVehicle realVehicle;

    @Property
    private Collection<Device> deviceList;

    @Property
    private Device deviceConfig;

    @Property
    private Collection<MappingAttributes> mappingList;

    @Property
    private MappingAttributes mappingConfig;

    @OnEvent(PREPARE)
    void loadParameters()
    {
        masterServerURI = qm.findParameterByName(Parameter.MASTER_SERVER_URI, "");
        internalRosCore = qm.findParameterByName(Parameter.USE_INTERNAL_ROS_CORE, "");
        realVehicleName = qm.findParameterByName(Parameter.REAL_VEHICLE_NAME, "");
        realVehicle = qm.findRealVehicleByName(realVehicleName.getValue());
        deviceList = qm.findAllDevices();
        mappingList = orderByTopic(qm.findAllMappingAttributes());
    }

    /**
     * @param attributeList the mapping attributes
     * @return the mapping attributes ordered by topic
     */
    private Collection<MappingAttributes> orderByTopic(Collection<MappingAttributes> attributeList)
    {
        Map<String, MappingAttributes> tree = new TreeMap<String, MappingAttributes>();
        for (MappingAttributes attribute : attributeList)
        {
            StringBuilder b = new StringBuilder(attribute.getPk().getDevice().getTopicRoot());

            String subPath = attribute.getPk().getTopic().getSubpath();

            if (subPath != null)
            {
                b.append("/").append(subPath);
            }
            tree.put(b.toString(), attribute);
        }
        return tree.values();
    }

    @OnEvent("deleteDevice")
    @CommitAfter
    void deleteDevice(String topic)
    {
        Device device = qm.findDeviceByTopicRoot(topic);
        Collection<MappingAttributes> mappings = qm.findMappingAttributesByDevice(device);
        nodeService.shutdownMappingAttributes(mappings);
        nodeService.shutdownDevice(device);
        qm.deleteAll(mappings);
        qm.delete(device);
    }

    @OnEvent("connectToAutoPilot")
    @CommitAfter
    void connectToAutoPilot(String topic)
    {
        Map<String, MappingAttributes> attributeMap = qm.findAllMappingAttributesAsMap();
        if (!attributeMap.containsKey(topic))
        {
            return;
        }

        TopicCategory category = attributeMap.get(topic).getPk().getTopic().getCategory();

        for (Entry<String, MappingAttributes> entry : attributeMap.entrySet())
        {
            MappingAttributes attributes = entry.getValue();
            Topic attributeTopic = attributes.getPk().getTopic();
            if (category != attributeTopic.getCategory())
            {
                continue;
            }
            boolean connectedToAutopilot = topic.equals(entry.getKey());
            attributes.setConnectedToAutopilot(connectedToAutopilot);
            qm.saveOrUpdate(attributes);
        }
    }

    @OnEvent("disconnectFromAutoPilot")
    @CommitAfter
    void disconnectFromAutoPilot(String topic)
    {
        MappingAttributes attributes = qm.findMappingAttributesByTopic(topic);
        if (attributes != null)
        {
            attributes.setConnectedToAutopilot(Boolean.FALSE);
            qm.saveOrUpdate(attributes);
        }
    }

    @OnEvent("setInvisibleInVirtualVehicle")
    @CommitAfter
    void setInvisibleInVirtualVehicle(String topic)
    {
        MappingAttributes attributes = qm.findMappingAttributesByTopic(topic);
        if (attributes != null)
        {
            attributes.setVvVisible(Boolean.FALSE);
            qm.saveOrUpdate(attributes);
        }
    }

    @OnEvent("setVisibleInVirtualVehicle")
    @CommitAfter
    void setVisibleInVirtualVehicle(String topic)
    {
        MappingAttributes attributes = qm.findMappingAttributesByTopic(topic);
        if (attributes != null)
        {
            attributes.setVvVisible(Boolean.TRUE);
            qm.saveOrUpdate(attributes);
        }
    }

    @CommitAfter
    void onSuccessFromRealVehicleNameForm() throws JsonParseException, JsonMappingException, IOException
    {
        if (realVehicleName.getValue() == null)
        {
            return;
        }
        qm.saveOrUpdate(realVehicleName);

        if (realVehicle == null)
        {
            realVehicle = qm.findRealVehicleByName(realVehicleName.getValue());
            return;
        }
        realVehicle.setName(realVehicleName.getValue());
        qm.saveOrUpdate(realVehicle);
        mapper.refresh();
    }

    @CommitAfter
    void onSuccessFromUriForm() throws URISyntaxException
    {
        qm.saveOrUpdate(masterServerURI);
        nodeService.updateMasterServerURI(new URI(masterServerURI.getValue()));
    }

    @CommitAfter
    void onSuccessFromCoreForm()
    {
        qm.saveOrUpdate(internalRosCore);
        nodeService.updateRosCore(Boolean.parseBoolean(internalRosCore.getValue()));
    }

    @CommitAfter
    void onSuccessFromMappingForm()
    {
        qm.saveOrUpdateAll(mappingList);

        if (realVehicle != null)
        {
            List<SensorDefinition> sdList = new ArrayList<>();
            for (MappingAttributes x : mappingList)
            {
                SensorDefinition sd = x.getSensorDefinition();
                if (sd != null)
                {
                    sdList.add(sd);
                }
            }
            realVehicle.setSensors(sdList);
            realVehicle.setLastUpdate(new Date());
            qm.saveOrUpdate(realVehicle);
        }

        nodeService.updateMappingAttributes(mappingList);
    }

    /**
     * @return the sensor definition select model.
     */
    public SelectModel getSensorDefinitionNameSelectModel()
    {
        return SensorDefinitionSelectHelpers.selectModel(
            qm.findSensorDefinitionsByMessageType(mappingConfig.getPk().getTopic().getMessageType())
            );
    }

    /**
     * @return the sensor definition name encoder.
     */
    public ValueEncoder<SensorDefinition> getSensorDefinitionNameEncoder()
    {
        return new SensorDefinitionSelectHelpers(qm).valueEncoder();
    }

    /**
     * @return true if sensor definitions are available, false otherwise.
     */
    public Boolean getSensorDefinitionsAvailable()
    {
        return qm.findSensorDefinitionsByMessageType(mappingConfig.getPk().getTopic().getMessageType()).size() > 0;
    }
}
