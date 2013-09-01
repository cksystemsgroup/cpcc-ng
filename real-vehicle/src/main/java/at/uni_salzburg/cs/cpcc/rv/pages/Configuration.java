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
import static org.apache.tapestry5.EventConstants.SUCCESS;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import at.uni_salzburg.cs.cpcc.rv.entities.Device;
import at.uni_salzburg.cs.cpcc.rv.entities.MappingAttributes;
import at.uni_salzburg.cs.cpcc.rv.entities.Parameter;
import at.uni_salzburg.cs.cpcc.rv.services.QueryManager;

/**
 * Configuration
 */
public class Configuration
{
    @Inject
    private QueryManager qm;

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
     * @param attributes the mapping attributes
     * @return the mapping attributes ordered by topic
     */
    private Collection<MappingAttributes> orderByTopic(List<MappingAttributes> attributes)
    {
        Map<String,MappingAttributes> tree = new TreeMap<String,MappingAttributes>();
        for (MappingAttributes a : attributes)
        {
            StringBuilder b = new StringBuilder(a.getPk().getDevice().getTopicRoot());
            
            String subPath = a.getPk().getTopic().getSubpath();
            
            if (subPath != null)
            {
                b.append("/").append(subPath);
            }
            tree.put(b.toString(), a);
        }
        return tree.values();
    }

    @OnEvent(SUCCESS)
    Object updateDatabase()
    {
        qm.saveOrUpdate(internalRosCore);
        qm.saveOrUpdate(masterServerURI);
        qm.saveOrUpdateAll(deviceList);
        return Configuration.class;
    }
    
    @OnEvent("deleteDevice")
    @CommitAfter
    void deleteDevice(String topic)
    {
        Device device = qm.findDeviceByTopicRoot(topic);
        qm.deleteAll(qm.findMappingAttributesByDevice(device));
        qm.delete(device);
    }
    
    @OnEvent("deleteMapping")
    @CommitAfter
    void deleteMapping(String topic)
    {
        Device device = qm.findDeviceByTopicRoot(topic);

        // delete only external mappings.
        if (device.getType().getClassName() == null)
        {
            qm.deleteAll(qm.findMappingAttributesByDevice(device));
            qm.delete(device);
        }
    }
    
    @CommitAfter
    void onSuccessFromUriForm()
    {
        qm.saveOrUpdate(masterServerURI);
    }
    
    @CommitAfter
    void onSuccessFromCoreForm()
    {
        qm.saveOrUpdate(internalRosCore);
    }
    
    @CommitAfter
    void onSuccessFromMappingForm()
    {
        qm.saveOrUpdateAll(mappingList);
    }
}
