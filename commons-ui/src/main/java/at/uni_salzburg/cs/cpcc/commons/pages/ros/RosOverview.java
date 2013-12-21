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
package at.uni_salzburg.cs.cpcc.commons.pages.ros;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Property;

import at.uni_salzburg.cs.cpcc.ros.base.AbstractRosAdapter;
import at.uni_salzburg.cs.cpcc.ros.services.RosNodeService;
import at.uni_salzburg.cs.cpcc.ros.sim.RosNodeGroup;


/**
 * RosOverview
 */
public class RosOverview
{
    @Property
    @Inject
    private RosNodeService nodeService;
    
    @Property
    private AdapterNodeEntry adapterConfig;
    
    @Property
    private RosNodeGroupEntry rosNodeGroupConfig;
    
    
    /**
     * @return the list of adapters.
     */
    public Collection<AdapterNodeEntry> getAdapterList()
    {
        Map<String, List<AbstractRosAdapter>> nodeMap = nodeService.getAdapterNodes();
        
        List<AdapterNodeEntry> list = new ArrayList<AdapterNodeEntry>();
        
        for (Entry<String, List<AbstractRosAdapter>> entry : nodeMap.entrySet())
        {
            String rootTopic = entry.getKey();
            for (AbstractRosAdapter adapter : entry.getValue())
            {
                list.add(new AdapterNodeEntry(rootTopic, adapter.getTopic().getName(), adapter));
            }
        }
        
        return list;
    }
    
    /**
     * @return the list of node groups.
     */
    public Collection<RosNodeGroupEntry> getRosNodeGroupList()
    {
        Map<String, RosNodeGroup> groupMap = nodeService.getDeviceNodes();
        
        List<RosNodeGroupEntry> list = new ArrayList<RosNodeGroupEntry>();
        
        for (Entry<String, RosNodeGroup> group : groupMap.entrySet())
        {
            list.add(new RosNodeGroupEntry(group.getKey(),group.getValue()));
        }
        
        return list;
    }
    
    /**
     * AdapterNodeEntry
     */
    public static class AdapterNodeEntry
    {
        private String rootTopic;
        private String topic;
        private AbstractRosAdapter adapter;

        /**
         * @param rootTopic the root topic.
         * @param topic the topic.
         * @param adapter the adapter.
         */
        public AdapterNodeEntry(String rootTopic, String topic, AbstractRosAdapter adapter)
        {
            this.rootTopic = rootTopic;
            this.topic = topic;
            this.adapter = adapter;
        }
        
        /**
         * @return the rootTopic
         */
        public String getRootTopic()
        {
            return rootTopic;
        }
        
        /**
         * @return the topic
         */
        public String getTopic()
        {
            return topic;
        }
        
        /**
         * @return the adapter
         */
        public AbstractRosAdapter getAdapter()
        {
            return adapter;
        }
    }
    
    /**
     * DeviceNodeEntry
     */
    public static class RosNodeGroupEntry
    {
        private String rootTopic;
        private RosNodeGroup rosNodeGroup;
        
        /**
         * @param rootTopic the root topic.
         * @param rosNodeGroup the node group.
         */
        public RosNodeGroupEntry(String rootTopic, RosNodeGroup rosNodeGroup)
        {
            this.rootTopic = rootTopic;
            this.rosNodeGroup = rosNodeGroup;
        }
        
        /**
         * @return the root topic
         */
        public String getRootTopic()
        {
            return rootTopic;
        }
        
        /**
         * @return the device
         */
        public RosNodeGroup getRosNodeGroup()
        {
            return rosNodeGroup;
        }
    }
}
