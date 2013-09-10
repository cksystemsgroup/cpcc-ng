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
package at.uni_salzburg.cs.cpcc.ros.sim.quadrotor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.ros.sim.RosNodeGroup;

/**
 * NodeGroup
 */
public class NodeGroup implements RosNodeGroup
{
    private final static Logger LOG = LoggerFactory.getLogger(NodeGroup.class);

    private String topicRoot;
    private Map<String, List<String>> config;
    private NodeConfiguration nodeConfiguration;

    private PlantNode plantNode;

    private WaypointListenerNode wayPointListenerNode;

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
    @Override
    public void setConfig(Map<String, List<String>> config)
    {
        this.config = config;
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
    public void start()
    {
        LOG.info("start()");
        
        config.put("topicRoot", Arrays.asList(topicRoot));
        Configuration cfg = new Configuration(nodeConfiguration, config);
        
        plantNode = new PlantNode(cfg);
        DefaultNodeMainExecutor.newDefault().execute(plantNode, nodeConfiguration);
        try
        {
            plantNode.awaitStartup();
        }
        catch (InterruptedException e)
        {
            LOG.error("node.awaitStartup() interrupted", e);
        }

        wayPointListenerNode = new WaypointListenerNode(topicRoot + "/waypoint");
        wayPointListenerNode.addMessageListener(plantNode.getPlant());
        DefaultNodeMainExecutor.newDefault().execute(wayPointListenerNode, nodeConfiguration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown()
    {
        LOG.info("shutdown()");
        wayPointListenerNode.RemoveMessageListener(plantNode.getPlant());
        DefaultNodeMainExecutor.newDefault().shutdownNodeMain(wayPointListenerNode);
        DefaultNodeMainExecutor.newDefault().shutdownNodeMain(plantNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<String>> getCurrentState()
    {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        
        for (Entry<String, List<String>> entry : config.entrySet())
        {
            map.put(entry.getKey(), entry.getValue());
        }
        
        Plant plant = plantNode.getPlant();
        
        map.put("plant.destinationReached", Arrays.asList(Boolean.toString(plant.isDestinationReached())));
        map.put("plant.running", Arrays.asList(Boolean.toString(plant.isRunning())));
        map.put("plant.state", Arrays.asList(plant.getCurrentState().toString()));
        
        for (Entry<String, List<String>> entry : plant.getPlantState().getStateMap().entrySet())
        {
            map.put(entry.getKey(), entry.getValue());
        }
        
        return map;
    }
}
