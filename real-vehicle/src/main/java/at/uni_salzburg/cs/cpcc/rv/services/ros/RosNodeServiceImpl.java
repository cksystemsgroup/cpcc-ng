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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
import at.uni_salzburg.cs.cpcc.rv.services.opts.Option;
import at.uni_salzburg.cs.cpcc.rv.services.opts.OptionsParserService;
import at.uni_salzburg.cs.cpcc.rv.services.opts.ParseException;
import at.uni_salzburg.cs.cpcc.rv.services.opts.Token;

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

    private OptionsParserService optionsParser;

    private NodeConfiguration nodeConfiguration;


    private static Map<String, RosNodeGroup> deviceNodes = Collections
        .synchronizedMap(new TreeMap<String, RosNodeGroup>());

//    private static Map<String, RosNodeGroup> adapterNodes = Collections
//        .synchronizedMap(new TreeMap<String, RosNodeGroup>());
    
    /**
     * Constructor
     */
    public RosNodeServiceImpl(QueryManager qm, OptionsParserService optionsParser)
    {
        this.qm = qm;
        this.optionsParser = optionsParser;
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

        LOG.info("init()");

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
        LOG.info("RosNodeServiceImpl launchNode=" + device.getTopicRoot() + ", topic=" + topic.getSubpath());

        
        if (device.getType().getClassName() != null)
        {
            try
            {
                Class<?> name = Class.forName(device.getType().getClassName());
                LOG.info("RosNodeServiceImpl class " + name.getName() + " loaded.");
            }
            catch (ClassNotFoundException e)
            {
                LOG.error("RosNodeServiceImpl can not load class " + device.getType().getClassName(), e);
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
        LOG.info("updateMasterServerURI " + uri.toASCIIString());

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
        boolean rcore = rosCore != null;
        LOG.info("updateRosCore internal=" + internal + ", core-running=" + rcore);

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
        LOG.info("updateDevice " + device.getTopicRoot());
        
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
        LOG.info("shutdownDevice " + device.getTopicRoot());
        stopRosNodeGroup(device.getTopicRoot());
        
        // TODO delete from RTE
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMappingAttributes(Collection<MappingAttributes> mappings)
    {
        // TODO Auto-generated method stub
        LOG.info("updateMappingAttributes");
        
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdownMappingAttributes(Collection<MappingAttributes> mappings)
    {
        // TODO Auto-generated method stub
        LOG.info("shutdownMappingAttributes");
        
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
            LOG.info("startRosNode class " + clazz.getName() + " loaded.");

            RosNodeGroup group = (RosNodeGroup) clazz.newInstance();
            group.setConfig(parseConfig(config));
            group.setTopicRoot(topicRoot);
            group.setNodeConfiguration(nodeConfiguration);

            group.start();

            deviceNodes.put(topicRoot, group);
            LOG.info(String.format("ROS node group %s started.", topicRoot));
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException | ParseException e)
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
    
    /**
     * @param config the configuration as a string.
     * @return the configuration as a map.
     * @throws ParseException thrown in case of errors.
     * @throws IOException thrown in case of errors.
     */
    private Map<String, List<String>> parseConfig(String config) throws IOException, ParseException
    {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (Option option : optionsParser.parse(config))
        {
            List<Token> tokenList = option.getValue();
            List<String> valueList = new ArrayList<String>();
            for (Token token : tokenList)
            {
                valueList.add(token.getItemString());
            }
            map.put(option.getKey(), valueList);
        }
        return map;
    }
}
