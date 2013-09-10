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

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PlantNode
 */
public class PlantNode extends AbstractNodeMain
{
    private static final Logger LOG = LoggerFactory.getLogger(PlantNode.class);

    private Configuration config;
    private String nodeTopic;
    private Plant plant;

    /**
     * @param config the configuration.
     */
    public PlantNode(Configuration config)
    {
        this.config = config;
        nodeTopic = config.getTopicRoot();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphName getDefaultNodeName()
    {
        return GraphName.of(nodeTopic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(final ConnectedNode connectedNode)
    {
        LOG.info("onStart");
        plant = new Plant(config, connectedNode);
        connectedNode.executeCancellableLoop(plant);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onShutdown(Node node)
    {
        super.onShutdown(node);
        LOG.info("onShutdown()");
    }

    /**
     * @return the plant.
     */
    public Plant getPlant()
    {
        return plant;
    }

    /**
     * @throws InterruptedException thrown in case of an interruption.
     */
    public void awaitStartup() throws InterruptedException
    {
        while (plant == null)
        {
            Thread.sleep(100);
        }
    }
}
