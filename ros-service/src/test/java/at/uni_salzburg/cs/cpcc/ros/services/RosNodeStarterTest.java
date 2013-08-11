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
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.ros.address.InetAddressFactory;
import org.ros.internal.node.client.MasterClient;
import org.ros.internal.node.response.Response;
import org.ros.internal.node.response.StatusCode;
import org.ros.master.client.SystemState;
import org.ros.master.client.TopicSystemState;
import org.ros.master.client.TopicType;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.topic.Subscriber;
import org.testng.annotations.Test;

import sensor_msgs.CameraInfo;

public class RosNodeStarterTest
{
//    RosNodeStarter nodeStarter;
//    
//    @BeforeClass
//    public void beforeClass()
//    {
//        nodeStarter = new RosNodeStarterImpl();
//    }
//
//    @AfterClass
//    public void afterClass()
//    {
//        nodeStarter.shutdown();
//    }

//    @Test
//    public void f()
//    {
//        RosCore core = nodeStarter.getRosCore();
//        
//        MasterServer master = core.getMasterServer();
//        
//        List<Object> publishedTopics = master.getPublishedTopics(GraphName.root(), GraphName.root());
//        
//        // [[/mav09/camera1/camera_info, sensor_msgs/CameraInfo], [/mav02/gps, sensor_msgs/NavSatFix], [/mav02/camera1/camera_info, sensor_msgs/CameraInfo], [/mav04/gps, sensor_msgs/NavSatFix], [/mav07/camera1/image, sensor_msgs/Image], [/mav01/camera1/camera_info, sensor_msgs/CameraInfo], [/mav03/camera1/camera_info, sensor_msgs/CameraInfo], [/mav07/gps, sensor_msgs/NavSatFix], [/mav00/camera1/camera_info, sensor_msgs/CameraInfo], [/mav02/camera1/image, sensor_msgs/Image], [/mav07/camera1/camera_info, sensor_msgs/CameraInfo], [/mav08/camera1/image, sensor_msgs/Image], [/mav03/camera1/image, sensor_msgs/Image], [/mav09/gps, sensor_msgs/NavSatFix], [/mav01/camera1/image, sensor_msgs/Image], [/mav08/camera1/camera_info, sensor_msgs/CameraInfo], [/mav06/camera1/camera_info, sensor_msgs/CameraInfo], [/mav01/gps, sensor_msgs/NavSatFix], [/mav04/camera1/image, sensor_msgs/Image], [/mav00/gps, sensor_msgs/NavSatFix], [/mav09/camera1/image, sensor_msgs/Image], [/mav05/gps, sensor_msgs/NavSatFix], [/mav08/gps, sensor_msgs/NavSatFix], [/mav06/camera1/image, sensor_msgs/Image], [/mav05/camera1/camera_info, sensor_msgs/CameraInfo], [/mav05/camera1/image, sensor_msgs/Image], [/mav06/gps, sensor_msgs/NavSatFix], [/mav00/camera1/image, sensor_msgs/Image], [/mav03/gps, sensor_msgs/NavSatFix], [/mav04/camera1/camera_info, sensor_msgs/CameraInfo], [/rosout, rosgraph_msgs/Log]]
//        
//        List<Object> systemState = master.getSystemState();
//           
//        // [[[/mav09/camera1/camera_info, [/morse]], [/mav02/gps, [/morse]], [/mav02/camera1/camera_info, [/morse]], [/mav04/gps, [/morse]], [/mav07/camera1/image, [/morse]], [/mav01/camera1/camera_info, [/morse]], [/mav03/camera1/camera_info, [/morse]], [/mav07/gps, [/morse]], [/mav00/camera1/camera_info, [/morse]], [/mav02/camera1/image, [/morse]], [/mav07/camera1/camera_info, [/morse]], [/mav08/camera1/image, [/morse]], [/mav03/camera1/image, [/morse]], [/mav09/gps, [/morse]], [/mav01/camera1/image, [/morse]], [/mav08/camera1/camera_info, [/morse]], [/mav06/camera1/camera_info, [/morse]], [/mav01/gps, [/morse]], [/mav04/camera1/image, [/morse]], [/mav00/gps, [/morse]], [/mav09/camera1/image, [/morse]], [/mav05/gps, [/morse]], [/mav08/gps, [/morse]], [/mav06/camera1/image, [/morse]], [/mav05/camera1/camera_info, [/morse]], [/mav05/camera1/image, [/morse]], [/mav06/gps, [/morse]], [/mav00/camera1/image, [/morse]], [/mav03/gps, [/morse]], [/mav04/camera1/camera_info, [/morse]], [/rosout, [/morse]]], [[/mav09/waypoint, [/morse]], [/mav06/waypoint, [/morse]], [/mav01/waypoint, [/morse]], [/mav00/waypoint, [/morse]], [/mav07/waypoint, [/morse]], [/mav03/waypoint, [/morse]], [/mav08/waypoint, [/morse]], [/mav02/waypoint, [/morse]], [/mav05/waypoint, [/morse]], [/mav04/waypoint, [/morse]]], [[/mav00/gps/get_local_data, [/mav00/gps/get_local_data]], [/mav05/waypoint/setdest, [/mav05/waypoint/setdest]], [/mav02/waypoint/setdest, [/mav02/waypoint/setdest]], [/mav07/waypoint/setdest, [/mav07/waypoint/setdest]], [/mav04/waypoint/get_status, [/mav04/waypoint/get_status]], [/mav03/gps/get_local_data, [/mav03/gps/get_local_data]], [/mav05/waypoint/get_status, [/mav05/waypoint/get_status]], [/mav06/camera1/get_local_data, [/mav06/camera1/get_local_data]], [/mav05/camera1/get_local_data, [/mav05/camera1/get_local_data]], [/mav08/gps/get_local_data, [/mav08/gps/get_local_data]], [/mav05/gps/get_local_data, [/mav05/gps/get_local_data]], [/mav02/camera1/get_local_data, [/mav02/camera1/get_local_data]], [/mav04/camera1/get_local_data, [/mav04/camera1/get_local_data]], [/mav09/waypoint/setdest, [/mav09/waypoint/setdest]], [/mav00/waypoint/get_status, [/mav00/waypoint/get_status]], [/mav01/waypoint/get_status, [/mav01/waypoint/get_status]], [/mav03/waypoint/get_status, [/mav03/waypoint/get_status]], [/mav02/waypoint/get_status, [/mav02/waypoint/get_status]], [/mav08/camera1/get_local_data, [/mav08/camera1/get_local_data]], [/mav06/waypoint/get_status, [/mav06/waypoint/get_status]], [/mav08/waypoint/setdest, [/mav08/waypoint/setdest]], [/mav01/gps/get_local_data, [/mav01/gps/get_local_data]], [/mav06/waypoint/setdest, [/mav06/waypoint/setdest]], [/mav02/gps/get_local_data, [/mav02/gps/get_local_data]], [/mav07/waypoint/get_status, [/mav07/waypoint/get_status]], [/morse/set_logger_level, [/morse/set_logger_level]], [/mav03/camera1/get_local_data, [/mav03/camera1/get_local_data]], [/mav09/waypoint/get_status, [/mav09/waypoint/get_status]], [/mav00/camera1/get_local_data, [/mav00/camera1/get_local_data]], [/mav00/waypoint/setdest, [/mav00/waypoint/setdest]], [/mav03/waypoint/setdest, [/mav03/waypoint/setdest]], [/mav06/gps/get_local_data, [/mav06/gps/get_local_data]], [/mav08/waypoint/get_status, [/mav08/waypoint/get_status]], [/mav04/waypoint/setdest, [/mav04/waypoint/setdest]], [/morse/get_loggers, [/morse/get_loggers]], [/mav01/waypoint/setdest, [/mav01/waypoint/setdest]], [/mav09/gps/get_local_data, [/mav09/gps/get_local_data]], [/mav04/gps/get_local_data, [/mav04/gps/get_local_data]], [/mav07/camera1/get_local_data, [/mav07/camera1/get_local_data]], [/mav07/gps/get_local_data, [/mav07/gps/get_local_data]], [/mav01/camera1/get_local_data, [/mav01/camera1/get_local_data]], [/mav09/camera1/get_local_data, [/mav09/camera1/get_local_data]]]]
//        
//        List<List<String>> topicTypes = master.getTopicTypes(GraphName.of("/mav05/camera1/camera_info"));
//
//        // [[/mav09/camera1/camera_info, sensor_msgs/CameraInfo], [/mav02/gps, sensor_msgs/NavSatFix], [/mav02/camera1/camera_info, sensor_msgs/CameraInfo], [/mav04/gps, sensor_msgs/NavSatFix], [/mav09/waypoint, geometry_msgs/Pose], [/mav06/waypoint, geometry_msgs/Pose], [/mav01/waypoint, geometry_msgs/Pose], [/mav07/camera1/image, sensor_msgs/Image], [/mav01/camera1/camera_info, sensor_msgs/CameraInfo], [/mav03/camera1/camera_info, sensor_msgs/CameraInfo], [/mav00/waypoint, geometry_msgs/Pose], [/mav07/gps, sensor_msgs/NavSatFix], [/mav00/camera1/camera_info, sensor_msgs/CameraInfo], [/mav07/waypoint, geometry_msgs/Pose], [/mav03/waypoint, geometry_msgs/Pose], [/mav02/camera1/image, sensor_msgs/Image], [/mav08/waypoint, geometry_msgs/Pose], [/mav02/waypoint, geometry_msgs/Pose], [/mav05/waypoint, geometry_msgs/Pose], [/mav07/camera1/camera_info, sensor_msgs/CameraInfo], [/mav08/camera1/image, sensor_msgs/Image], [/mav04/waypoint, geometry_msgs/Pose], [/mav03/camera1/image, sensor_msgs/Image], [/mav09/gps, sensor_msgs/NavSatFix], [/mav01/camera1/image, sensor_msgs/Image], [/mav08/camera1/camera_info, sensor_msgs/CameraInfo], [/mav06/camera1/camera_info, sensor_msgs/CameraInfo], [/mav01/gps, sensor_msgs/NavSatFix], [/mav04/camera1/image, sensor_msgs/Image], [/mav00/gps, sensor_msgs/NavSatFix], [/mav09/camera1/image, sensor_msgs/Image], [/mav05/gps, sensor_msgs/NavSatFix], [/mav08/gps, sensor_msgs/NavSatFix], [/mav06/camera1/image, sensor_msgs/Image], [/mav05/camera1/camera_info, sensor_msgs/CameraInfo], [/mav05/camera1/image, sensor_msgs/Image], [/mav06/gps, sensor_msgs/NavSatFix], [/mav00/camera1/image, sensor_msgs/Image], [/mav03/gps, sensor_msgs/NavSatFix], [/mav04/camera1/camera_info, sensor_msgs/CameraInfo], [/rosout, rosgraph_msgs/Log]]
//        
//        System.out.println();
//    }
    
    
    @Test(enabled = false)
    public void f2() throws URISyntaxException
    {
        URI uri = new URI("http", null, "localhost", 11311, "/", null, null);
        
        MasterClient master = new MasterClient(uri);
        
//        Response<List<TopicDeclaration>> publishedTopics = master.getPublishedTopics(GraphName.newAnonymous(), "");
//        boolean success2 = publishedTopics.isSuccess();
//        StatusCode statusCode2 = publishedTopics.getStatusCode();
//        String statusMessage2 = publishedTopics.getStatusMessage();
//        List<TopicDeclaration> result2 = publishedTopics.getResult();
//        
//        for (TopicDeclaration t : result2) {
//            TopicIdentifier identifier = t.getIdentifier();
//            String messageType = t.getMessageType();
//            GraphName name = t.getName();
//            System.out.printf("TopicDeclaration: %s %s %s\n", name, messageType, identifier.getName());
//        }
//        
//        System.out.println();
        
        Response<SystemState> systemState = master.getSystemState(GraphName.newAnonymous());
        
        StatusCode statusCode = systemState.getStatusCode();
        String statusMessage = systemState.getStatusMessage();
        boolean success = systemState.isSuccess();
        SystemState result = systemState.getResult();
        Collection<TopicSystemState> topics = result.getTopics();
        Map<String,TopicSystemState> topicMap = new TreeMap<String,TopicSystemState>();
        for (TopicSystemState t : topics)
        {
            topicMap.put(t.getTopicName(), t);
        }
        
        Response<List<TopicType>> topicTypes = master.getTopicTypes(GraphName.newAnonymous());
        StatusCode statusCode3 = topicTypes.getStatusCode();
        String statusMessage3 = topicTypes.getStatusMessage();
        boolean success3 = topicTypes.isSuccess();
        List<TopicType> result3 = topicTypes.getResult();
        Map<String,String> typeMap = new HashMap<String,String>();
        for (TopicType tt : result3)
        {
            typeMap.put(tt.getName(), tt.getMessageType());
        }
        
        for (Entry<String, TopicSystemState> e : topicMap.entrySet())
        {
            Set<String> publishers = e.getValue().getPublishers();
            Set<String> subscribers = e.getValue().getSubscribers();
            String topicName = e.getKey();
            String typeName = typeMap.containsKey(topicName) ? typeMap.get(topicName) : "";
            System.out.printf("Topic: %-30s, %d publishers, %d subscribers, type is %s\n", topicName, publishers.size(),
                subscribers.size(), typeName);
        }
        
        
//        Response<List<TopicType>> topicTypes = master.getTopicTypes(GraphName.of("/mav05/camera1/camera_info"));

        NodeConfiguration nodeConfiguration =
            NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostName(), uri);
        
        CameraInfoNode ciNode = new CameraInfoNode();
        DefaultNodeMainExecutor.newDefault().execute(ciNode, nodeConfiguration);
        ciNode.awaitMessage();
        CameraInfo message = ciNode.getMessage();
        if (message != null)
        {
            String distortionModel = message.getDistortionModel();
            int height = message.getHeight();
            int width = message.getWidth();
            System.out.printf("\nBugger: dm=%s, h=%d, w=%d\n", distortionModel, height,  width);
        }
        
        DefaultNodeMainExecutor.newDefault().shutdownNodeMain(ciNode);
        
        
        
        System.out.println();
    }
    
    private static class CameraInfoNode extends AbstractNodeMain
    {
        private sensor_msgs.CameraInfo message;
        
        @Override
        public GraphName getDefaultNodeName()
        {
            return GraphName.newAnonymous();
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void onStart(ConnectedNode connectedNode)
        {
            Subscriber<sensor_msgs.CameraInfo> subscriber =
                connectedNode.newSubscriber("mav00/camera1/camera_info", sensor_msgs.CameraInfo._TYPE);

            subscriber.addMessageListener(new MessageListener<sensor_msgs.CameraInfo>()
            {
                @Override
                public void onNewMessage(sensor_msgs.CameraInfo newMessage)
                {
                    message = newMessage;
                }
            });
        }
        
        public void awaitMessage()
        {
            int counter = 5;
            while (message == null && counter-- > 0)
            {
                try
                {
                    Thread.sleep(200);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        
        public sensor_msgs.CameraInfo getMessage()
        {
            return message;
        }
        
    }
}
