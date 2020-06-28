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

package cpcc.ros.services;

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

import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.ros.RosCore;
import org.ros.address.InetAddressFactory;
import org.ros.exception.RosRuntimeException;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.slf4j.Logger;

import cpcc.core.entities.Device;
import cpcc.core.entities.MappingAttributes;
import cpcc.core.entities.Parameter;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.Topic;
import cpcc.core.services.QueryManager;
import cpcc.core.services.opts.OptionsParserService;
import cpcc.core.services.opts.ParseException;
import cpcc.ros.base.AbstractRosAdapter;
import cpcc.ros.base.RosTopic;
import cpcc.ros.sim.RosNodeGroup;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * RosNode Service Implementation.
 */
@EagerLoad
@Singleton
public class RosNodeServiceImpl implements RosNodeService
{
    private Logger logger;
    private QueryManager qm;
    private OptionsParserService optionsParser;
    private NodeConfiguration nodeConfiguration;
    private ObjectLocator objectLocator;
    private RosCore rosCore;
    private URI masterServerUri;

    private Map<String, RosNodeGroup> deviceNodes = Collections
        .synchronizedMap(new TreeMap<String, RosNodeGroup>());

    private Map<String, List<AbstractRosAdapter>> adapterNodes = Collections
        .synchronizedMap(new TreeMap<String, List<AbstractRosAdapter>>());

    private Map<Integer, AbstractRosAdapter> sensorDefinitionMap = Collections
        .synchronizedMap(new TreeMap<Integer, AbstractRosAdapter>());

    /**
     * @param logger the application logger.
     * @param qm the query manager.
     * @param optionsParser the options parser.
     * @param objectLocator the object locator.
     */
    public RosNodeServiceImpl(Logger logger, QueryManager qm, OptionsParserService optionsParser,
        ObjectLocator objectLocator)
    {
        this.logger = logger;
        this.qm = qm;
        this.optionsParser = optionsParser;
        this.objectLocator = objectLocator;

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
                masterServerUri = new URI(uri.getValue());
                String hostName = InetAddressFactory.newNonLoopback().getHostName();
                nodeConfiguration = NodeConfiguration.newPublic(hostName, masterServerUri);
            }
            catch (URISyntaxException e)
            {
                logger.error(String.format("Can not set master server URI to '%s'.", uri.getValue()));
                return;
            }
        }

        Parameter internal = qm.findParameterByName(Parameter.USE_INTERNAL_ROS_CORE);
        if (internal != null && "true".equalsIgnoreCase(internal.getValue()))
        {
            startRosCore();
        }

        List<Device> allDevices = qm.findAllDevices();

        logger.info("init()");

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

        logger.info("launchDeviceAdapter launchNode={}, topicRoot={}, topic={}, adapterClass={}",
            device.getTopicRoot(), device.getTopicRoot(), topicPath, topic.getAdapterClassName());

        if (topic.getAdapterClassName() != null)
        {
            try
            {
                Class<?> clazz = Class.forName(topic.getAdapterClassName());
                logger.info("Adapter class {} loaded.", clazz.getName());

                RosTopic rosTopic = new RosTopic();
                rosTopic.setName(topicPath.toString());
                rosTopic.setType(topic.getMessageType());

                AbstractRosAdapter instance = (AbstractRosAdapter) objectLocator.autobuild(clazz);
                instance.setTopic(rosTopic);
                if (device.getConfiguration() != null)
                {
                    instance.setConfig(optionsParser.parseConfig(device.getConfiguration()));
                }

                DefaultNodeMainExecutor.newDefault().execute(instance, nodeConfiguration);
                logger.info("Adapter class {} started.", clazz.getName());
                return instance;
            }
            catch (ClassNotFoundException | IOException | ParseException e)
            {
                logger.error("launchDeviceAdapter can not load class {}", topic.getAdapterClassName(), e);
            }
        }
        return null;
    }

    /**
     * @param device the device
     */
    private void launchAllDeviceAdapters(Device device)
    {
        List<Topic> topicList = new ArrayList<>();
        topicList.add(device.getType().getMainTopic());
        if (device.getType().getSubTopics() != null)
        {
            topicList.addAll(device.getType().getSubTopics());
        }

        List<AbstractRosAdapter> adapterList = new ArrayList<>();
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
            if (sd != null && sd.getId() != null && !sensorDefinitionMap.containsKey(sd.getId()))
            {
                sensorDefinitionMap.put(sd.getId(), adapter);
            }
            adapterList.add(adapter);
        }

        adapterNodes.put(device.getTopicRoot(), adapterList);
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
        logger.info("stopDeviceAdapter topic={}, name={}", adapter.getTopic(), adapter.getName());
        DefaultNodeMainExecutor.newDefault().shutdownNodeMain(adapter);
    }

    /**
     * @param topicRoot
     */
    private void stopAllDeviceAdapters(String topicRoot)
    {
        List<AbstractRosAdapter> adapterList = adapterNodes.get(topicRoot);
        if (adapterList == null)
        {
            return;
        }

        for (AbstractRosAdapter adapter : adapterList)
        {
            stopDeviceAdapter(adapter);
        }

        adapterNodes.remove(topicRoot);
    }

    /**
     * Shutdown all device nodes and all adapter nodes.
     */
    private void shutdownAllNodes()
    {
        for (RosNodeGroup group : deviceNodes.values())
        {
            group.shutdown();
        }

        for (List<AbstractRosAdapter> adapterList : adapterNodes.values())
        {
            for (AbstractRosAdapter adapter : adapterList)
            {
                DefaultNodeMainExecutor.newDefault().shutdownNodeMain(adapter);
            }
        }

        deviceNodes.clear();
        adapterNodes.clear();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressFBWarnings(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    @Override
    public void updateMasterServerURI(URI uri)
    {
        logger.info("updateMasterServerURI {}", uri);

        if (masterServerUri != null && masterServerUri.equals(uri))
        {
            return;
        }

        masterServerUri = uri;

        String hostName = InetAddressFactory.newNonLoopback().getHostName();
        nodeConfiguration = NodeConfiguration.newPublic(hostName, uri);

        shutdownAllNodes();

        if (rosCore != null)
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
        boolean rcore = rosCore != null;
        logger.info("updateRosCore internal={}, core-running={}", internal, rcore);

        synchronized (RosNodeServiceImpl.class)
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
        if (masterServerUri == null)
        {
            return;
        }

        String host = masterServerUri.getHost();
        int port = masterServerUri.getPort();

        logger.info("Starting ROS core host={}, port={}", host, port);

        rosCore = RosCore.newPublic(host, port);

        try
        {
            rosCore.start();
            awaitStartOfRosCore();
        }
        catch (RosRuntimeException e)
        {
            logger.error("Starting ROS core failed", e);
            rosCore = null;
        }
    }

    /**
     * Wait for start of the ROS core.
     */
    private void awaitStartOfRosCore()
    {
        try
        {
            rosCore.awaitStart();
            logger.info("ROS core is up and running.");
        }
        catch (InterruptedException e)
        {
            logger.error("Starting ROS core has been interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * shutdown ROS core.
     */
    private void shutdownRosCore()
    {
        logger.info("Shutting down internal ROS core");
        rosCore.shutdown();
        rosCore = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDevice(Device device)
    {
        logger.info("updateDevice {}", device.getTopicRoot());

        shutdownDevice(device);
        startRosNodeGroup(device);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdownDevice(Device device)
    {
        logger.info("shutdownDevice {}", device.getTopicRoot());
        stopRosNodeGroup(device.getTopicRoot());

        // TODO delete from RTE

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMappingAttributes(Collection<MappingAttributes> mappings)
    {
        logger.info("updateMappingAttributes");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdownMappingAttributes(Collection<MappingAttributes> mappings)
    {
        logger.info("shutdownMappingAttributes");
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
            logger.error("Can not start ROS node group: {}, id={}, className={}",
                device.getTopicRoot(), device.getId(), className);
            launchAllDeviceAdapters(device);
            return;
        }

        logger.info("Starting ROS node group, topic={}", topicRoot);

        try
        {
            Class<?> clazz = Class.forName(className);
            logger.info("startRosNode class {} loaded.", clazz.getName());

            RosNodeGroup group = (RosNodeGroup) objectLocator.autobuild(clazz);
            group.setTopicRoot(topicRoot);
            group.setNodeConfiguration(nodeConfiguration);
            group.setConfig(optionsParser.parseConfig(config));

            group.start();

            getDeviceNodes().put(topicRoot, group);
            logger.info("ROS node group {} started.", topicRoot);
        }
        catch (ClassNotFoundException | IOException | ParseException e)
        {
            logger.error("Can not instantiate the ROS node group for topic {}", topicRoot, e);
        }

        launchAllDeviceAdapters(device);
    }

    /**
     * @param topicRoot topicRoot
     */
    private void stopRosNodeGroup(String topicRoot)
    {
        logger.info("Stopping ROS node group, topic={}", topicRoot);

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
        return deviceNodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<AbstractRosAdapter>> getAdapterNodes()
    {
        return adapterNodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractRosAdapter getAdapterNodeByTopic(String topic)
    {
        for (List<AbstractRosAdapter> nodeList : adapterNodes.values())
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
        return sensorDefinitionMap.get(sensorDefinitionId);
    }
}
