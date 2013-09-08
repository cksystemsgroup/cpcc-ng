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
import java.util.List;
import java.util.Map;

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
    private Configuration config;
    private NodeConfiguration nodeConfiguration;

    private PlantNode node;

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
        config.put("topicRoot", Arrays.asList(topicRoot));
        this.config = new Configuration(nodeConfiguration, config);
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
        node = new PlantNode(config);
        DefaultNodeMainExecutor.newDefault().execute(node, nodeConfiguration);
        try
        {
            node.awaitStartup();
        }
        catch (InterruptedException e)
        {
            LOG.error("node.awaitStartup() interrupted", e);
        }

        wayPointListenerNode = new WaypointListenerNode(topicRoot + "/waypoint");
        wayPointListenerNode.addMessageListener(node.getPlant());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown()
    {
        LOG.info("shutdown()");
        wayPointListenerNode.RemoveMessageListener(node.getPlant());
        DefaultNodeMainExecutor.newDefault().shutdownNodeMain(wayPointListenerNode);
        DefaultNodeMainExecutor.newDefault().shutdownNodeMain(node);
    }

}
