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
package at.uni_salzburg.cs.cpcc.ros.services;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.ros.address.InetAddressFactory;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import at.uni_salzburg.cs.cpcc.ros.base.AbstractRosAdapter;


/**
 * RosNodeWardenImpl
 */
public class RosNodeWardenImpl implements RosNodeWarden
{
    private static final Logger LOG = LoggerFactory.getLogger(RosNodeWardenImpl.class);

    private static Map<String, AbstractRosAdapter> adapterMap = 
        Collections.synchronizedMap(new HashMap<String, AbstractRosAdapter>());

    private static URI masterURI;
    
    private static NodeConfiguration nodeConfiguration;
    
    /**
     * @return the current master URI.
     */
    @Override
    public URI getMasterURI()
    {
        return masterURI;
    }
    
    /**
     * @param masterURI the new master URI
     */
    @SuppressFBWarnings(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    @Override
    public void setMasterURI(URI masterURI)
    {
        if (masterURI != null)
        {
            RosNodeWardenImpl.masterURI = masterURI;
            String me = InetAddressFactory.newNonLoopback().getHostName();
            nodeConfiguration = NodeConfiguration.newPublic(me, masterURI);
        }
        else
        {
            LOG.error("Can not change master URI. Request ignored.");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addRosNode(AbstractRosAdapter node)
    {
        String topic = node.getTopic().getName();

        if (adapterMap.containsKey(topic))
        {
            LOG.error("Topic %s is already registered, shutting it down.", topic);
            DefaultNodeMainExecutor.newDefault().shutdownNodeMain(node);
            adapterMap.remove(topic);
        }
        DefaultNodeMainExecutor.newDefault().execute(node, nodeConfiguration);
        adapterMap.put(topic, node);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeRosNode(AbstractRosAdapter node)
    {
        String topic = node.getTopic().getName();

        if (adapterMap.containsKey(topic))
        {
            AbstractRosAdapter adapter = adapterMap.get(topic);
            DefaultNodeMainExecutor.newDefault().shutdownNodeMain(adapter);
            adapterMap.remove(topic);
        }
        else
        {
            LOG.error("Topic %s is not registered. Request ignored.", topic);
        }
    }

    /**
     * @return the adapter map.
     */
    @Override
    public Map<String, AbstractRosAdapter> getAdapterMap()
    {
        return adapterMap;
    }
}
