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

package at.uni_salzburg.cs.cpcc.ros.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.ros.internal.node.client.MasterClient;
import org.ros.internal.node.response.Response;
import org.ros.internal.node.response.StatusCode;
import org.ros.master.client.SystemState;
import org.ros.master.client.TopicSystemState;
import org.ros.namespace.GraphName;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.Device;
import at.uni_salzburg.cs.cpcc.core.entities.DeviceType;
import at.uni_salzburg.cs.cpcc.core.entities.MappingAttributes;
import at.uni_salzburg.cs.cpcc.core.entities.Parameter;
import at.uni_salzburg.cs.cpcc.core.entities.RosNodeType;
import at.uni_salzburg.cs.cpcc.core.entities.Topic;
import at.uni_salzburg.cs.cpcc.core.entities.TopicCategory;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.services.opts.OptionsParserService;
import at.uni_salzburg.cs.cpcc.core.services.opts.ParseException;
import at.uni_salzburg.cs.cpcc.ros.sim.RosNodeGroup;

public class RosNodeServiceTest
{
    private QueryManager qm;
    private OptionsParserService optionsParser;
    private URI masterServer;
    private URI masterServer2;
    private Parameter masterServerURI;
    private Parameter wrongMasterServerURI;
    private Parameter useInternalRosCore;
    private Device device21;
    private DeviceType type8;
    private Topic topic10;
    private MappingAttributes mappingAttribute;

    @SuppressWarnings("serial")
    @BeforeMethod
    public void setupTest() throws URISyntaxException, IOException, ParseException
    {
        final int port = 40000 + (int) (Math.random() * 10000.0);
        final String uriString = "http://localhost:" + port + "/";
        final String uriString2 = "http://localhost:" + Integer.toString(port + 1) + "/";
        masterServer = new URI(uriString);
        masterServer2 = new URI(uriString2);

        masterServerURI = new Parameter()
        {
            {
                setId(1);
                setName("masterServerURI");
                setSort(2);
                setValue(uriString);
            }
        };

        wrongMasterServerURI = new Parameter()
        {
            {
                setId(2);
                setName("masterServerURI");
                setSort(3);
                setValue("this://is/a/wrong/master/Uri");
            }
        };

        useInternalRosCore = new Parameter()
        {
            {
                setId(2);
                setName("useInternalRosCore");
                setSort(1);
                setValue("true");
            }
        };

        topic10 = new Topic();
        topic10.setId(10);
        topic10.setMessageType("std_msgs/Float32");
        topic10.setNodeType(RosNodeType.PUBLISHER);
        topic10.setSubpath(null);
        topic10.setAdapterClassName("at.uni_salzburg.cs.cpcc.ros.sensors.Float32SensorAdapter");
        topic10.setCategory(TopicCategory.ALTITUDE_OVER_GROUND);

        type8 = new DeviceType();
        type8.setId(8);
        type8.setClassName("at.uni_salzburg.cs.cpcc.ros.sim.SonarEmulator");
        type8.setMainTopic(topic10);
        type8.setName("Sonar Emulator");
        type8.setSubTopics(null);

        device21 = new Device();
        device21.setId(21);
        device21.setTopicRoot("mav01/sonar");
        device21.setType(type8);
        device21.setConfiguration("origin=471 gps='/mav01/gps'");
        
        mappingAttribute = mock(MappingAttributes.class);
        when(mappingAttribute.getConnectedToAutopilot()).thenReturn(true);
        
        qm = mock(QueryManager.class);
        when(qm.findParameterByName(Parameter.MASTER_SERVER_URI)).thenReturn(masterServerURI);
        when(qm.findParameterByName(Parameter.USE_INTERNAL_ROS_CORE)).thenReturn(useInternalRosCore);
        when(qm.findAllDevices()).thenReturn(Arrays.asList(device21));
        when(qm.findMappingAttribute(device21, topic10)).thenReturn(mappingAttribute);

        Map<String, List<String>> options = new HashMap<String, List<String>>()
        {
            {
                put("origin", Arrays.asList("471"));
                put("gps", Arrays.asList("/mav01/gps"));
            }
        };
        optionsParser = mock(OptionsParserService.class);
        when(optionsParser.parseConfig("origin=471 gps='/mav01/gps'")).thenReturn(options);
    }

    @Test
    public void shouldStartAndStopAndStartInternalRosCore()
    {
        RosNodeServiceImpl svc = new RosNodeServiceImpl(qm, optionsParser);
        Assert.assertNotNull(svc);

        MasterClient master = new MasterClient(masterServer);

        Response<SystemState> systemState = master.getSystemState(GraphName.newAnonymous());
        Assert.assertNotNull(systemState);

        StatusCode statusCode = systemState.getStatusCode();
        Assert.assertNotNull(statusCode);
        Assert.assertEquals(StatusCode.SUCCESS, statusCode);

        String statusMessage = systemState.getStatusMessage();
        Assert.assertNotNull(statusMessage);

        Collection<TopicSystemState> topics = systemState.getResult().getTopics();
        Assert.assertNotNull(topics);
        if (!topics.isEmpty())
        {
            for (TopicSystemState x : topics)
            {
                System.out.println("### Topic: " + x.getTopicName());
            }
        }
        
        Assert.assertTrue(topics.isEmpty());

        svc.updateRosCore(false);

        try
        {
            systemState = master.getSystemState(GraphName.newAnonymous());
        }
        catch (RuntimeException e)
        {
            Assert.assertTrue(e.getCause() instanceof SocketException);
        }

        svc.updateRosCore(true);

        systemState = master.getSystemState(GraphName.newAnonymous());
        Assert.assertNotNull(systemState);

        statusCode = systemState.getStatusCode();
        Assert.assertNotNull(statusCode);
        Assert.assertEquals(StatusCode.SUCCESS, statusCode);
    }

    @Test
    public void shouldRestartInternalRosCoreOnMasterUriChange()
    {
        RosNodeServiceImpl svc = new RosNodeServiceImpl(qm, optionsParser);
        Assert.assertNotNull(svc);

        MasterClient master = new MasterClient(masterServer);

        Response<SystemState> systemState = master.getSystemState(GraphName.newAnonymous());
        Assert.assertNotNull(systemState);

        StatusCode statusCode = systemState.getStatusCode();
        Assert.assertNotNull(statusCode);
        Assert.assertEquals(StatusCode.SUCCESS, statusCode);

        svc.updateMasterServerURI(masterServer2);

        master = new MasterClient(masterServer2);
        systemState = master.getSystemState(GraphName.newAnonymous());
        Assert.assertNotNull(systemState);

        statusCode = systemState.getStatusCode();
        Assert.assertNotNull(statusCode);
        Assert.assertEquals(StatusCode.SUCCESS, statusCode);
    }

    @Test
    public void shouldUpdateDevice() throws IOException, ParseException
    {
        RosNodeServiceImpl svc = new RosNodeServiceImpl(qm, optionsParser);
        Assert.assertNotNull(svc);

        Map<String, RosNodeGroup> deviceNodes = svc.getDeviceNodes();
        Assert.assertNotNull(deviceNodes);
        Assert.assertEquals(1, deviceNodes.size());

        svc.updateDevice(device21);

        deviceNodes = svc.getDeviceNodes();
        Assert.assertNotNull(deviceNodes);
        Assert.assertEquals(1, deviceNodes.size());

        svc.shutdownDevice(device21);
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowIAEOnWreckedMasterURI()
    {
        QueryManager qmi = mock(QueryManager.class);
        when(qmi.findParameterByName(Parameter.MASTER_SERVER_URI)).thenReturn(wrongMasterServerURI);
        when(qmi.findParameterByName(Parameter.USE_INTERNAL_ROS_CORE)).thenReturn(useInternalRosCore);

        RosNodeServiceImpl svc = new RosNodeServiceImpl(qmi, optionsParser);
        Assert.assertNull(svc);
    }
}
