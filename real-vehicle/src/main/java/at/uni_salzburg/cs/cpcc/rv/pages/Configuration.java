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
package at.uni_salzburg.cs.cpcc.rv.pages;

import static org.apache.tapestry5.EventConstants.PREPARE;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import at.uni_salzburg.cs.cpcc.rv.entities.Device;
import at.uni_salzburg.cs.cpcc.rv.entities.MappingAttributes;
import at.uni_salzburg.cs.cpcc.rv.entities.Parameter;
import at.uni_salzburg.cs.cpcc.rv.entities.Topic;
import at.uni_salzburg.cs.cpcc.rv.entities.TopicCategory;
import at.uni_salzburg.cs.cpcc.rv.services.db.QueryManager;
import at.uni_salzburg.cs.cpcc.rv.services.ros.RosNodeService;

/**
 * Configuration
 */
public class Configuration
{
    @Inject
    private QueryManager qm;

    @Inject
    private RosNodeService nodeService;

    @Property
    private Parameter internalRosCore;

    @Property
    private Parameter masterServerURI;

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
        masterServerURI = qm.findParameterByName(Parameter.MASTER_SERVER_URI);
        internalRosCore = qm.findParameterByName(Parameter.USE_INTERNAL_ROS_CORE);
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
        List<MappingAttributes> attributeList = qm.findAllMappingAttributes();

        Map<String, MappingAttributes> attributeMap = new HashMap<String, MappingAttributes>();

        TopicCategory category = null;
        
        for (MappingAttributes attribute : attributeList)
        {
            StringBuilder b = new StringBuilder(attribute.getPk().getDevice().getTopicRoot());
            String subPath = attribute.getPk().getTopic().getSubpath();
            if (subPath != null)
            {
                b.append("/").append(subPath);
            }
            attributeMap.put(b.toString(), attribute);
            String attributeTopic = b.toString();
            if (attributeTopic.equals(topic))
            {
                category = attribute.getPk().getTopic().getCategory();
            }
        }

        if (category == null)
        {
            return;
        }
        
        for (Entry<String, MappingAttributes> entry : attributeMap.entrySet())
        {
            MappingAttributes attribute = entry.getValue();
            Topic attributeTopic = attribute.getPk().getTopic();
            if (category != attributeTopic.getCategory())
            {
                continue;
            }
            boolean connectedToAutopilot = topic.equals(entry.getKey());
            attribute.setConnectedToAutopilot(connectedToAutopilot);
            qm.saveOrUpdate(attribute);
        }
    }
    
    @OnEvent("disconnectFromAutoPilot")
    @CommitAfter
    void disconnectFromAutoPilot(String topic)
    {
        List<MappingAttributes> attributeList = qm.findAllMappingAttributes();
        for (MappingAttributes attribute : attributeList)
        {
            StringBuilder b = new StringBuilder(attribute.getPk().getDevice().getTopicRoot());
            String subPath = attribute.getPk().getTopic().getSubpath();
            if (subPath != null)
            {
                b.append("/").append(subPath);
            }
            String attributeTopic = b.toString();
            if (attributeTopic.equals(topic))
            {
                attribute.setConnectedToAutopilot(Boolean.FALSE);
                qm.saveOrUpdate(attribute);
                break;
            }
        }
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
        nodeService.updateMappingAttributes(mappingList);
    }
}
