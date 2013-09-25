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

/**
 * RosNodeStarterTest
 */
public class RosNodeStarterTest
{
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
        
//        StatusCode statusCode = systemState.getStatusCode();
//        String statusMessage = systemState.getStatusMessage();
//        boolean success = systemState.isSuccess();
        SystemState result = systemState.getResult();
        Collection<TopicSystemState> topics = result.getTopics();
        Map<String,TopicSystemState> topicMap = new TreeMap<String,TopicSystemState>();
        for (TopicSystemState t : topics)
        {
            topicMap.put(t.getTopicName(), t);
        }
        
        Response<List<TopicType>> topicTypes = master.getTopicTypes(GraphName.newAnonymous());
//        StatusCode statusCode3 = topicTypes.getStatusCode();
//        String statusMessage3 = topicTypes.getStatusMessage();
//        boolean success3 = topicTypes.isSuccess();
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
