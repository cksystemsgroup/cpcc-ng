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

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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

import org.apache.tapestry5.commons.ObjectLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ros.internal.node.client.MasterClient;
import org.ros.internal.node.response.Response;
import org.ros.internal.node.response.StatusCode;
import org.ros.master.client.SystemState;
import org.ros.master.client.TopicSystemState;
import org.ros.namespace.GraphName;
import org.ros.node.NodeConfiguration;
import org.slf4j.Logger;

import cpcc.core.entities.Device;
import cpcc.core.entities.DeviceType;
import cpcc.core.entities.MappingAttributes;
import cpcc.core.entities.Parameter;
import cpcc.core.entities.RosNodeType;
import cpcc.core.entities.Topic;
import cpcc.core.entities.TopicCategory;
import cpcc.core.services.QueryManager;
import cpcc.core.services.opts.OptionsParserService;
import cpcc.core.services.opts.ParseException;
import cpcc.ros.sensors.Float32SensorAdapter;
import cpcc.ros.sim.RosNodeGroup;
import cpcc.ros.sim.sonar.SonarEmulator;

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
    private Logger logger;
    private ObjectLocator objectLocator;
    private SonarEmulator sonarEmulator;
    private Float32SensorAdapter float32SensorAdapter;
    private NodeConfiguration sonarEmulatorNodeConfiguration;

    @SuppressWarnings("serial")
    @BeforeEach
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
        topic10.setAdapterClassName(Float32SensorAdapter.class.getName());
        topic10.setCategory(TopicCategory.ALTITUDE_OVER_GROUND);

        type8 = new DeviceType();
        type8.setId(8);
        type8.setClassName(SonarEmulator.class.getName());
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

        logger = mock(Logger.class);

        sonarEmulatorNodeConfiguration = mock(NodeConfiguration.class);
        when(sonarEmulatorNodeConfiguration.getNodeName()).thenReturn(GraphName.of("/sonar"));

        sonarEmulator = mock(SonarEmulator.class);
        when(sonarEmulator.getNodeConfiguration()).thenReturn(sonarEmulatorNodeConfiguration);

        float32SensorAdapter = spy(Float32SensorAdapter.class);

        objectLocator = mock(ObjectLocator.class);
        when(objectLocator.autobuild(SonarEmulator.class)).thenReturn(sonarEmulator);
        when(objectLocator.autobuild(Float32SensorAdapter.class)).thenReturn(float32SensorAdapter);
    }

    @Test
    public void shouldStartAndStopAndStartInternalRosCore()
    {
        RosNodeServiceImpl svc = new RosNodeServiceImpl(logger, qm, optionsParser, objectLocator);
        assertThat(svc).isNotNull();

        MasterClient master = new MasterClient(masterServer);

        Response<SystemState> systemState = master.getSystemState(GraphName.newAnonymous());
        assertThat(systemState).isNotNull();

        StatusCode statusCode = systemState.getStatusCode();
        assertThat(statusCode).isNotNull().isEqualTo(StatusCode.SUCCESS);

        String statusMessage = systemState.getStatusMessage();
        assertThat(statusMessage).isNotNull();

        Collection<TopicSystemState> topics = systemState.getResult().getTopics();
        assertThat(topics).isNotNull().isEmpty();
        //if (!topics.isEmpty())
        //{
        //    for (TopicSystemState x : topics)
        //    {
        //        System.out.println("### Topic: " + x.getTopicName());
        //    }
        //}

        svc.updateRosCore(false);

        catchException(() -> master.getSystemState(GraphName.newAnonymous()));

        assertThat((Throwable) caughtException()).isInstanceOf(RuntimeException.class);
        assertThat(caughtException().getCause()).isInstanceOf(SocketException.class);

        svc.updateRosCore(true);

        systemState = master.getSystemState(GraphName.newAnonymous());
        assertThat(systemState).isNotNull();

        statusCode = systemState.getStatusCode();
        assertThat(statusCode).isNotNull().isEqualTo(StatusCode.SUCCESS);
    }

    @Test
    public void shouldRestartInternalRosCoreOnMasterUriChange()
    {
        RosNodeServiceImpl svc = new RosNodeServiceImpl(logger, qm, optionsParser, objectLocator);
        assertThat(svc).isNotNull();

        MasterClient master = new MasterClient(masterServer);

        Response<SystemState> systemState = master.getSystemState(GraphName.newAnonymous());
        assertThat(systemState).isNotNull();

        StatusCode statusCode = systemState.getStatusCode();
        assertThat(statusCode).isNotNull().isEqualTo(StatusCode.SUCCESS);

        svc.updateMasterServerURI(masterServer2);

        master = new MasterClient(masterServer2);
        systemState = master.getSystemState(GraphName.newAnonymous());
        assertThat(systemState).isNotNull();

        statusCode = systemState.getStatusCode();
        assertThat(statusCode).isNotNull().isEqualTo(StatusCode.SUCCESS);
    }

    @Test
    public void shouldUpdateDevice() throws IOException, ParseException
    {
        RosNodeServiceImpl svc = new RosNodeServiceImpl(logger, qm, optionsParser, objectLocator);
        assertThat(svc).isNotNull();

        Map<String, RosNodeGroup> deviceNodes = svc.getDeviceNodes();
        assertThat(deviceNodes).hasSize(1);

        svc.updateDevice(device21);

        deviceNodes = svc.getDeviceNodes();
        assertThat(deviceNodes).hasSize(1);

        svc.shutdownDevice(device21);
    }

    public void shouldThrowIAEOnWreckedMasterURI()
    {
        QueryManager qmi = mock(QueryManager.class);
        when(qmi.findParameterByName(Parameter.MASTER_SERVER_URI)).thenReturn(wrongMasterServerURI);
        when(qmi.findParameterByName(Parameter.USE_INTERNAL_ROS_CORE)).thenReturn(useInternalRosCore);

        try
        {
            new RosNodeServiceImpl(logger, qmi, optionsParser, objectLocator);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        }
        catch (IllegalArgumentException e)
        {
            assertThat(e).hasMessage("");
        }
    }
}
