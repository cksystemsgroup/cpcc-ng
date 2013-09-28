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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
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

import at.uni_salzburg.cs.cpcc.ros.services.RosNodeType;
import at.uni_salzburg.cs.cpcc.ros.sim.RosNodeGroup;
import at.uni_salzburg.cs.cpcc.rv.entities.Device;
import at.uni_salzburg.cs.cpcc.rv.entities.DeviceType;
import at.uni_salzburg.cs.cpcc.rv.entities.Parameter;
import at.uni_salzburg.cs.cpcc.rv.entities.Topic;
import at.uni_salzburg.cs.cpcc.rv.entities.TopicCategory;
import at.uni_salzburg.cs.cpcc.rv.services.db.QueryManager;
import at.uni_salzburg.cs.cpcc.rv.services.opts.OptionsParserService;

public class RosNodeServiceTest
{
    private QueryManager qm;
    private URI masterServer;
    private URI masterServer2;
    private Parameter masterServerURI;
    private Parameter useInternalRosCore;
    private Device device21;
    private DeviceType type8;
    private Topic topic10;

    @BeforeMethod
    public void setupTest() throws URISyntaxException
    {
        final int port = 40000 + (int) (Math.random() * 10000.0);
        final String uriString = "http://localhost:" + port + "/";
        final String uriString2 = "http://localhost:" + Integer.toString(port+1) + "/";
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

        useInternalRosCore = new Parameter()
        {
            {
                setId(2);
                setName("useInternalRosCore");
                setSort(1);
                setValue("true");
            }
        };

        qm = mock(QueryManager.class);
        when(qm.findParameterByName(Parameter.MASTER_SERVER_URI)).thenReturn(masterServerURI);
        when(qm.findParameterByName(Parameter.USE_INTERNAL_ROS_CORE)).thenReturn(useInternalRosCore);
        
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
    }

    @Test
    public void shouldStartAndStopAndStartInternalRosCore()
    {
        OptionsParserService optionsParser = mock(OptionsParserService.class);

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
        OptionsParserService optionsParser = mock(OptionsParserService.class);

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
    public void shouldUpdateDevice()
    {
        OptionsParserService optionsParser = mock(OptionsParserService.class);

        RosNodeServiceImpl svc = new RosNodeServiceImpl(qm, optionsParser);
        Assert.assertNotNull(svc);
        
        Map<String, RosNodeGroup> deviceNodes = svc.getDeviceNodes();
        Assert.assertNotNull(deviceNodes);
        Assert.assertEquals(0, deviceNodes.size());
        
        svc.updateDevice(device21);
        
        deviceNodes = svc.getDeviceNodes();
        Assert.assertNotNull(deviceNodes);
        Assert.assertEquals(1, deviceNodes.size());
        
        svc.shutdownDevice(device21);
    }
}