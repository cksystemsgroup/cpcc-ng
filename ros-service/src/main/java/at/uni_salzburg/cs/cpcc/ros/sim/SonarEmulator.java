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
package at.uni_salzburg.cs.cpcc.ros.sim;

import java.util.List;
import java.util.Map;

import org.ros.node.NodeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SonarEmulator
 */
public class SonarEmulator implements RosNodeGroup
{
    private final static Logger LOG = LoggerFactory.getLogger(SonarEmulator.class);
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setTopicRoot(String topicRoot)
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConfig(Map<String,List<String>> config)
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNodeConfiguration(NodeConfiguration nodeConfiguration)
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start()
    {
        // TODO Auto-generated method stub
        LOG.info("start()");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown()
    {
        // TODO Auto-generated method stub
        LOG.info("shutdown()");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<String>> getCurrentState()
    {
//        LOG.info("getCurrentState()");
        
        // TODO Auto-generated method stub
        return null;
    }
}
