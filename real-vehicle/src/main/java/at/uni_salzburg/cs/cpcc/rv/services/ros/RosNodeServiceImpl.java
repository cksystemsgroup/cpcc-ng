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
package at.uni_salzburg.cs.cpcc.rv.services.ros;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Singleton;

import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.ros.RosCore;
import org.ros.address.InetAddressFactory;
import org.ros.node.NodeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.ros.sim.RosNodeGroup;
import at.uni_salzburg.cs.cpcc.rv.entities.Device;
import at.uni_salzburg.cs.cpcc.rv.entities.MappingAttributes;
import at.uni_salzburg.cs.cpcc.rv.entities.Parameter;
import at.uni_salzburg.cs.cpcc.rv.entities.Topic;
import at.uni_salzburg.cs.cpcc.rv.services.QueryManager;

/**
 * RosNodeServiceImpl
 */
@EagerLoad
@Singleton
public class RosNodeServiceImpl implements RosNodeService
{
    private final static Logger LOG = LoggerFactory.getLogger(RosNodeServiceImpl.class);

//    private static final String MASTER_SERVER_DEFAULT_URI = null;
    
    private static RosCore rosCore = null;
    private static URI masterServerUri = null;

    private QueryManager qm;

    private NodeConfiguration nodeConfiguration;

    private static Map<String, RosNodeGroup> deviceNodes = Collections
        .synchronizedMap(new TreeMap<String, RosNodeGroup>());

//    private static Map<String, RosNodeGroup> adapterNodes = Collections
//        .synchronizedMap(new TreeMap<String, RosNodeGroup>());
    
    /**
     * Constructor
     */
    public RosNodeServiceImpl(QueryManager qm)
    {
        this.qm = qm;
        init();
    }

    /**
     * initialization
     */
    private void init()
    {
        Parameter uri = qm.findParameterByName(Parameter.MASTER_SERVER_URI);
        Parameter internal = qm.findParameterByName(Parameter.USE_INTERNAL_ROS_CORE);
        
        if (uri != null && uri.getValue() != null)
        {
            try
            {
                updateMasterServerURI(new URI(uri.getValue()));
            }
            catch (URISyntaxException e)
            {
                LOG.error(String.format("Can not set master server URI to '%s'.", uri.getValue().toString()));
            }
        }
        
        updateRosCore("true".equalsIgnoreCase(internal.getValue()));
        
        List<Device> allDevices = qm.findAllDevices();

        System.out.println("RosNodeServiceImpl.init()");

        for (Device device : allDevices)
        {
            startRosNodeGroup(device);
            launchDeviceAdapter(device, device.getType().getMainTopic());
            for (Topic topic : device.getType().getSubTopics())
            {
                launchDeviceAdapter(device, topic);
            }
        }
    }

    /**
     * @param device the device
     * @param topic the topic
     * @throws ClassNotFoundException
     */
    private void launchDeviceAdapter(Device device, Topic topic)
    {
        
        // TODO Auto-generated method stub
        System.out.println("RosNodeServiceImpl launchNode=" + device.getTopicRoot() + ", topic=" + topic.getSubpath());

        
        if (device.getType().getClassName() != null)
        {
            try
            {
                Class<?> name = Class.forName(device.getType().getClassName());
                System.out.println("RosNodeServiceImpl class " + name.getName() + " loaded.");
            }
            catch (ClassNotFoundException e)
            {
                System.out.println("RosNodeServiceImpl can not load class " + device.getType().getClassName());
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMasterServerURI(URI uri)
    {
        // TODO Auto-generated method stub
        System.out.println("updateMasterServerURI " + uri.toASCIIString());

        if (masterServerUri != null && masterServerUri.equals(uri))
        {
            return;
        }

        masterServerUri = uri;
        
        String hostName = InetAddressFactory.newNonLoopback().getHostName();
        nodeConfiguration = NodeConfiguration.newPublic(hostName, masterServerUri);
        
        if (rosCore != null)
        {
            // TODO
//            shutdownNodes();
            shutdownRosCore();
            startRosCore();
//            startNodes();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateRosCore(boolean internal)
    {
        // TODO Auto-generated method stub
        boolean rcore = rosCore != null;
        System.out.println("updateRosCore internal=" + internal + ", core-running=" + rcore);

        synchronized (LOG)
        {
            if (internal && rosCore == null)
            {
                startRosCore();
            }
            
            if (!internal && rosCore != null)
            {
                shutdownRosCore();
            }
        }
    }

    /**
     * start ROS core.
     */
    private void startRosCore()
    {
        LOG.info(String.format("Starting ROS core host=%s, port=%d", masterServerUri.getHost(), masterServerUri.getPort()));
        String host = masterServerUri.getHost();
        int port = masterServerUri.getPort();
        rosCore = RosCore.newPublic(host, port);
        rosCore.start();
        try
        {
            rosCore.awaitStart();
            LOG.info("ROS core is up and running.");
        }
        catch (InterruptedException e)
        {
            LOG.error("Starting ROS core has been interrupted", e);
        }
    }
    
    /**
     * shutdown ROS core.
     */
    private void shutdownRosCore()
    {
        LOG.info("Shutting down internal ROS core");
        rosCore.shutdown();
        rosCore = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDevice(Device device)
    {
        // TODO Auto-generated method stub
        System.out.println("updateDevice " + device.getTopicRoot());
        
        stopRosNodeGroup(device.getTopicRoot());
        
        if (device.getType().getClassName() != null)
        {
            startRosNodeGroup(device);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdownDevice(Device device)
    {
        // TODO Auto-generated method stub
        System.out.println("shutdownDevice " + device.getTopicRoot());
        stopRosNodeGroup(device.getTopicRoot());
        
        // TODO delete from RTE
    }
    
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void updateMappingAttributes(MappingAttributes attributes)
//    {
//        // TODO Auto-generated method stub
//        System.out.println("updateMappingAttributes device=" + attributes.getPk().getDevice().getTopicRoot());
//        
//        
//    }
//    
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void shutdownMappingAttributes(MappingAttributes attributes)
//    {
//        // TODO Auto-generated method stub
//        System.out.println("shutdownMappingAttributes device=" + attributes.getPk().getDevice().getTopicRoot());
//    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMappingAttributes(Collection<MappingAttributes> mappings)
    {
        // TODO Auto-generated method stub
        System.out.println("updateMappingAttributes");
        
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdownMappingAttributes(Collection<MappingAttributes> mappings)
    {
        // TODO Auto-generated method stub
        System.out.println("shutdownMappingAttributes");
        
    }
    
    /**
     * @param topicRoot topic root
     * @param className class name
     * @param config device configuration
     */
    private void startRosNodeGroup(Device device)
    {
        String topicRoot = device.getTopicRoot();
        String className = device.getType().getClassName();
        String config = device.getConfiguration();
        
        if (topicRoot == null || className == null)
        {
            LOG.error("Can not start ROS node group.");
            return;
        }

        LOG.info("Starting ROS node group, topic=" + topicRoot);
        
        try
        {
            Class<?> clazz = Class.forName(className);
            System.out.println("startRosNode class " + clazz.getName() + " loaded.");

            RosNodeGroup group = (RosNodeGroup) clazz.newInstance();
            group.setConfig(config);
            group.setTopicRoot(topicRoot);
            group.setNodeConfiguration(nodeConfiguration);
            
            group.start();
            
            deviceNodes.put(topicRoot, group);
            LOG.info(String.format("ROS node group %s started.", topicRoot));
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            LOG.error("Can not instantiate the ROS node group for topic " + topicRoot, e);
        }

    }
    
    /**
     * @param topicRoot topicRoot
     */
    private void stopRosNodeGroup(String topicRoot)
    {
        LOG.info("Stopping ROS node group, topic=" + topicRoot);
        if (deviceNodes.containsKey(topicRoot))
        {
            deviceNodes.get(topicRoot).shutdown();
            deviceNodes.remove(topicRoot);
        }
    }
}
