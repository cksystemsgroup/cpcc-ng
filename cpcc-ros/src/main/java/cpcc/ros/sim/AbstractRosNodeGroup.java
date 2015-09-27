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

package cpcc.ros.sim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ros.node.NodeConfiguration;

/**
 * AbstractRosNodeGroup
 */
public abstract class AbstractRosNodeGroup implements RosNodeGroup
{
    private String topicRoot;
    private Map<String, List<String>> config;
    private NodeConfiguration nodeConfiguration;



    /**
     * {@inheritDoc}
     */
    public String getTopicRoot()
    {
        return topicRoot;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setTopicRoot(String topicRoot)
    {
        this.topicRoot = topicRoot;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, List<String>> getConfig()
    {
        return config;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setConfig(Map<String, List<String>> config)
    {
        this.config = config;
    }

    /**
     * {@inheritDoc}
     */
    public NodeConfiguration getNodeConfiguration()
    {
        return nodeConfiguration;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setNodeConfiguration(NodeConfiguration nodeConfiguration)
    {
        this.nodeConfiguration = nodeConfiguration;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<String>> getCurrentState()
    {
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        for (Entry<String, List<String>> entry : getConfig().entrySet())
        {
            map.put("config." + entry.getKey(), entry.getValue());
        }
        
        return map;
    }
}
