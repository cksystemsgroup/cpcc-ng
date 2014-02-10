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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Singleton;

import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.ros.RosCore;
import org.ros.address.InetAddressFactory;
import org.ros.exception.RosRuntimeException;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.core.entities.Device;
import at.uni_salzburg.cs.cpcc.core.entities.MappingAttributes;
import at.uni_salzburg.cs.cpcc.core.entities.Parameter;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.entities.Topic;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.services.opts.OptionsParserService;
import at.uni_salzburg.cs.cpcc.core.services.opts.ParseException;
import at.uni_salzburg.cs.cpcc.ros.base.AbstractRosAdapter;
import at.uni_salzburg.cs.cpcc.ros.base.RosTopic;
import at.uni_salzburg.cs.cpcc.ros.sim.RosNodeGroup;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * RosNodeServiceImpl
 */
@EagerLoad
@Singleton
public class RosNodeServiceImpl implements RosNodeService
{
    private static final Logger LOG = LoggerFactory.getLogger(RosNodeServiceImpl.class);

    private QueryManager qm;

    private OptionsParserService optionsParser;

    private NodeConfiguration nodeConfiguration;

    private RosNodeSingleton rns;

    /**
     * @param qm the query manager.
     * @param optionsParser the options parser.
     */
    public RosNodeServiceImpl(QueryManager qm, OptionsParserService optionsParser)
    {
        this.qm = qm;
        this.optionsParser = optionsParser;
        rns = RosNodeSingleton.getInstance();
        init();
    }

    /**
     * initialization
     */
    private void init()
    {
        Parameter uri = qm.findParameterByName(Parameter.MASTER_SERVER_URI);

        if (uri != null && uri.getValue() != null)
        {
            try
            {
                URI msi = new URI(uri.getValue());
                rns.setMasterServerUri(msi);
                String hostName = InetAddressFactory.newNonLoopback().getHostName();
                nodeConfiguration = NodeConfiguration.newPublic(hostName, msi);
            }
            catch (URISyntaxException e)
            {
                LOG.error(String.format("Can not set master server URI to '%s'.", uri.getValue().toString()));
                return;
            }
        }

        Parameter internal = qm.findParameterByName(Parameter.USE_INTERNAL_ROS_CORE);
        if (internal != null && "true".equalsIgnoreCase(internal.getValue()))
        {
            startRosCore();
        }

        List<Device> allDevices = qm.findAllDevices();

        LOG.info("init()");

        for (Device device : allDevices)
        {
            startRosNodeGroup(device);
        }
    }

    /**
     * @param device the device.
     * @param topic the topic.
     * @throws ClassNotFoundExceptionthrown in case of errors.
     */
    private AbstractRosAdapter launchDeviceAdapter(Device device, Topic topic)
    {
        StringBuilder topicPath = new StringBuilder();
        topicPath.append(device.getTopicRoot());
        if (topic.getSubpath() != null)
        {
            topicPath.append("/").append(topic.getSubpath());
        }

        LOG.info("launchDeviceAdapter launchNode=" + device.getTopicRoot() + ", topicRoot=" + device.getTopicRoot()
            + ", topic=" + topicPath.toString() + ", adapterClass=" + topic.getAdapterClassName());

        if (topic.getAdapterClassName() != null)
        {
            try
            {
                Class<?> clazz = Class.forName(topic.getAdapterClassName());
                LOG.info("Adapter class " + clazz.getName() + " loaded.");

                RosTopic rosTopic = new RosTopic();
                rosTopic.setName(topicPath.toString());
                rosTopic.setType(topic.getMessageType());

                AbstractRosAdapter instance = (AbstractRosAdapter) clazz.newInstance();
                instance.setTopic(rosTopic);
                if (device.getConfiguration() != null)
                {
                    instance.setConfig(optionsParser.parseConfig(device.getConfiguration()));
                }

                DefaultNodeMainExecutor.newDefault().execute(instance, nodeConfiguration);
                LOG.info("Adapter class " + clazz.getName() + " started.");
                return instance;
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException
                | ParseException e)
            {
                LOG.error("launchDeviceAdapter can not load class " + topic.getAdapterClassName(), e);
            }
        }
        return null;
    }

    /**
     * @param device the device
     */
    private void launchAllDeviceAdapters(Device device)
    {
        List<Topic> topicList = new ArrayList<Topic>();
        topicList.add(device.getType().getMainTopic());
        if (device.getType().getSubTopics() != null)
        {
            topicList.addAll(device.getType().getSubTopics());
        }

        List<AbstractRosAdapter> adapterList = new ArrayList<AbstractRosAdapter>();
        for (Topic topic : topicList)
        {
            AbstractRosAdapter adapter = launchDeviceAdapter(device, topic);
            if (adapter == null)
            {
                continue;
            }
            MappingAttributes attribute = qm.findMappingAttribute(device, topic);
            adapter.setConnectedToAutopilot(attribute.getConnectedToAutopilot());
            SensorDefinition sd = attribute.getSensorDefinition();
            if (sd != null && sd.getId() != null)
            {
                rns.getSensorDefinitionMap().put(sd.getId(), adapter);
            }
            adapterList.add(adapter);
        }

        rns.getAdapterNodes().put(device.getTopicRoot(), adapterList);
    }

    /**
     * start all devices and all device adapters.
     */
    private void startAllNodes()
    {
        for (Device device : qm.findAllDevices())
        {
            startRosNodeGroup(device);
        }
    }

    /**
     * @param device the device
     * @param topic the topic
     */
    private void stopDeviceAdapter(AbstractRosAdapter adapter)
    {
        if (adapter == null)
        {
            return;
        }
        LOG.info("stopDeviceAdapter topic=" + adapter.getTopic() + ", name=" + adapter.getName());
        DefaultNodeMainExecutor.newDefault().shutdownNodeMain(adapter);
    }

    /**
     * @param topicRoot
     */
    private void stopAllDeviceAdapters(String topicRoot)
    {
        List<AbstractRosAdapter> adapterList = rns.getAdapterNodes().get(topicRoot);
        if (adapterList == null)
        {
            return;
        }
        for (AbstractRosAdapter adapter : adapterList)
        {
            stopDeviceAdapter(adapter);
        }
        rns.getAdapterNodes().remove(topicRoot);
    }

    /**
     * Shutdown all device nodes and all adapter nodes.
     */
    private void shutdownAllNodes()
    {
        for (RosNodeGroup group : rns.getDeviceNodes().values())
        {
            group.shutdown();
        }
        for (List<AbstractRosAdapter> adapterList : rns.getAdapterNodes().values())
        {
            for (AbstractRosAdapter adapter : adapterList)
            {
                DefaultNodeMainExecutor.newDefault().shutdownNodeMain(adapter);
            }
        }
        rns.getDeviceNodes().clear();
        rns.getAdapterNodes().clear();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressFBWarnings(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    @Override
    public void updateMasterServerURI(URI uri)
    {
        LOG.info("updateMasterServerURI " + uri.toASCIIString());

        if (rns.getMasterServerUri() != null && rns.getMasterServerUri().equals(uri))
        {
            return;
        }

        rns.setMasterServerUri(uri);

        String hostName = InetAddressFactory.newNonLoopback().getHostName();
        nodeConfiguration = NodeConfiguration.newPublic(hostName, uri);

        shutdownAllNodes();

        if (rns.getRosCore() != null)
        {
            shutdownRosCore();
            startRosCore();
        }

        startAllNodes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateRosCore(boolean internal)
    {
        boolean rcore = rns.getRosCore() != null;
        LOG.info("updateRosCore internal=" + internal + ", core-running=" + rcore);

        synchronized (LOG)
        {
            if (internal && rns.getRosCore() == null)
            {
                startRosCore();
            }

            if (!internal && rns.getRosCore() != null)
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
        if (rns.getMasterServerUri() == null)
        {
            return;
        }
        
        String host = rns.getMasterServerUri().getHost();
        int port = rns.getMasterServerUri().getPort();

        LOG.info(String.format("Starting ROS core host=%s, port=%d", host, port));

        RosCore rosCore = RosCore.newPublic(host, port);
        rns.setRosCore(rosCore);

        try
        {
            rosCore.start();
            awaitStartOfRosCore(rosCore);
        }
        catch (RosRuntimeException e)
        {
            LOG.error("Starting ROS core failed", e);
            rns.setRosCore(null);
        }
    }

    /**
     * @param rosCore the ROS core instance.
     */
    private void awaitStartOfRosCore(RosCore rosCore)
    {
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
        rns.getRosCore().shutdown();
        rns.setRosCore(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDevice(Device device)
    {
        // TODO Auto-generated method stub
        LOG.info("updateDevice " + device.getTopicRoot());

        shutdownDevice(device);
        startRosNodeGroup(device);
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
            LOG.error("Can not start ROS node group: " + device.getTopicRoot() + ", id=" + device.getId()
                + ", className=" + className);
            launchAllDeviceAdapters(device);
            return;
        }

        LOG.info("Starting ROS node group, topic=" + topicRoot);

        try
        {
            Class<?> clazz = Class.forName(className);
            LOG.info("startRosNode class " + clazz.getName() + " loaded.");

            RosNodeGroup group = (RosNodeGroup) clazz.newInstance();
            group.setTopicRoot(topicRoot);
            group.setNodeConfiguration(nodeConfiguration);
            group.setConfig(optionsParser.parseConfig(config));

            group.start();

            getDeviceNodes().put(topicRoot, group);
            LOG.info(String.format("ROS node group %s started.", topicRoot));
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException
            | IOException | ParseException e)
        {
            LOG.error("Can not instantiate the ROS node group for topic " + topicRoot, e);
        }

        launchAllDeviceAdapters(device);
    }

    /**
     * @param topicRoot topicRoot
     */
    private void stopRosNodeGroup(String topicRoot)
    {
        LOG.info("Stopping ROS node group, topic=" + topicRoot);

        Map<String, RosNodeGroup> deviceNodes = getDeviceNodes();
        if (deviceNodes.containsKey(topicRoot))
        {
            stopAllDeviceAdapters(topicRoot);
            RosNodeGroup group = deviceNodes.get(topicRoot);
            group.shutdown();
            deviceNodes.remove(topicRoot);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, RosNodeGroup> getDeviceNodes()
    {
        return rns.getDeviceNodes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<AbstractRosAdapter>> getAdapterNodes()
    {
        return rns.getAdapterNodes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractRosAdapter getAdapterNodeByTopic(String topic)
    {
        for (List<AbstractRosAdapter> nodeList : rns.getAdapterNodes().values())
        {
            for (AbstractRosAdapter node : nodeList)
            {
                if (topic.equals(node.getTopic().getName()))
                {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractRosAdapter findAdapterNodeBySensorDefinitionId(Integer sensorDefinitionId)
    {
        return rns.getSensorDefinitionMap().get(sensorDefinitionId);
    }

    /**
     * RosNodeSingleton
     */
    private static final class RosNodeSingleton
    {
        private static RosNodeSingleton instance;
        private RosCore rosCore;
        private URI masterServerUri;

        private Map<String, RosNodeGroup> deviceNodes = Collections
            .synchronizedMap(new TreeMap<String, RosNodeGroup>());

        private Map<String, List<AbstractRosAdapter>> adapterNodes = Collections
            .synchronizedMap(new TreeMap<String, List<AbstractRosAdapter>>());

        private Map<Integer, AbstractRosAdapter> sensorDefinitionMap = Collections
            .synchronizedMap(new TreeMap<Integer, AbstractRosAdapter>());

        /**
         * Private constructor
         */
        private RosNodeSingleton()
        {
            // intentionally empty.
        }

        /**
         * @return the instance
         */
        public static synchronized RosNodeSingleton getInstance()
        {
            if (instance == null)
            {
                instance = new RosNodeSingleton();
            }
            return instance;
        }

        /**
         * @return the rosCore
         */
        public RosCore getRosCore()
        {
            return rosCore;
        }

        /**
         * @param rosCore the rosCore to set
         */
        public void setRosCore(RosCore rosCore)
        {
            this.rosCore = rosCore;
        }

        /**
         * @return the masterServerUri
         */
        public URI getMasterServerUri()
        {
            return masterServerUri;
        }

        /**
         * @param masterServerUri the masterServerUri to set
         */
        public void setMasterServerUri(URI masterServerUri)
        {
            this.masterServerUri = masterServerUri;
        }

        /**
         * @return the deviceNodes
         */
        public Map<String, RosNodeGroup> getDeviceNodes()
        {
            return deviceNodes;
        }

        /**
         * @return the adapterNodes
         */
        public Map<String, List<AbstractRosAdapter>> getAdapterNodes()
        {
            return adapterNodes;
        }

        /**
         * @return the sensor definition map
         */
        public Map<Integer, AbstractRosAdapter> getSensorDefinitionMap()
        {
            return sensorDefinitionMap;
        }
    }
}
