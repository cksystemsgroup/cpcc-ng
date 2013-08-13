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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import org.ros.RosCore;
import org.ros.address.AdvertiseAddress;
import org.ros.address.BindAddress;
import org.ros.internal.node.server.master.MasterServer;
import org.ros.namespace.GraphName;
import org.testng.annotations.Test;

/**
 * RosQueryServiceTest
 */
public class RosQueryServiceTest
{
    /**
     * @throws URISyntaxException thrown in case of errors.
     * @throws InterruptedException thrown in case of errors.
     */
    @Test
    public void shouldSucceedWithProperRosCore() throws URISyntaxException, InterruptedException
    {
        RosCore core = RosCore.newPrivate();
        core.start();
        core.awaitStart();
        
        URI pubURI = new URI("http://localhost:12345/");
        URI slvURI = new URI("http://localhost:22345/");
        URI srvURI = new URI("http://localhost:32345/");
        URI subURI = new URI("http://localhost:42345/");
        
        core.getMasterServer().registerPublisher(GraphName.of("pub"), pubURI, GraphName.of("publ"), "sensor_msgs/CameraInfo");
        core.getMasterServer().registerService(GraphName.of("srv"), slvURI, GraphName.of("srvc"), srvURI);
        core.getMasterServer().registerSubscriber(GraphName.of("sub"), subURI, GraphName.of("subs"), "sensor_msgs/Image");
        
        URI uri = core.getUri();
        
        RosQueryService q = new RosQueryServiceImpl();
        Collection<RosTopicState> registeredTopics = q.findRegisteredTopics(uri);
        
        for (RosTopicState topic : registeredTopics)
        {
            System.out.printf("%s %s %s %s\n",
            topic.getName(),
            topic.getPublishers(),
            topic.getSubscribers(),
            topic.getType());
        }
        
        core.shutdown();
        
        RosTopicState[] topics = registeredTopics.toArray(new RosTopicState[0]);
        String[] empty = new String[0];
        
        assertEquals(topics[0].getName(), "publ", "Publisher: topic name");
        assertEquals(topics[0].getPublishers().size(), 1, "Publisher: number of publishers");
        assertEquals(topics[0].getPublishers().toArray(empty)[0], "pub", "Publisher: publisher name");
        assertEquals(topics[0].getSubscribers().size(), 0, "Publisher: number of subscribers");
        assertEquals(topics[0].getType(), "sensor_msgs/CameraInfo", "Publisher: type name");
        
        assertEquals(topics[1].getName(), "subs", "Subscriber: topic name");
        assertEquals(topics[1].getPublishers().size(), 0, "Subscriber: number of publishers");
        assertEquals(topics[1].getSubscribers().size(), 1, "Subscriber: number of subscribers");
        assertEquals(topics[1].getSubscribers().toArray(empty)[0], "sub", "Subscriber: publisher name");
        assertEquals(topics[1].getType(), "sensor_msgs/Image", "Subscriber: type name");        
    }
    
    /**
     * @throws URISyntaxException thrown in case of errors.
     * @throws InterruptedException thrown in case of errors.
     */
    @Test
    public void shouldFailWithoutRosCore() throws URISyntaxException
    {
        URI uri = new URI("http://localhost:42345/");

        RosQueryService q = new RosQueryServiceImpl();
        try
        {
            Collection<RosTopicState> registeredTopics = q.findRegisteredTopics(uri);
            assertNull(registeredTopics);
        }
        catch (RuntimeException e)
        {
            assertTrue(e.getCause() instanceof ConnectException);
        }
    }
    
    
    /**
     * @throws URISyntaxException thrown in case of errors.
     * @throws InterruptedException thrown in case of errors.
     */
    @Test
    public void shouldFailWithFaultyRosCore() throws URISyntaxException, InterruptedException
    {
        MasterServer master = new FailingMasterServer(true, false);
        master.start();
        master.awaitStart();

        RosQueryService q = new RosQueryServiceImpl();
        try
        {
            Collection<RosTopicState> registeredTopics = q.findRegisteredTopics(master.getUri());
            assertNull(registeredTopics);
        }
        catch (RuntimeException e)
        {
            // intentionally empty.
        }
        finally
        {
            master.shutdown();
        }
    }
    
    
    /**
     * FailingMasterServer
     */
    private class FailingMasterServer extends MasterServer
    {
        private boolean systemStateFails;
        private boolean topicTypesFails;

        /**
         * @param bindAddress the bind address
         * @param advertiseAddress the advertise address
         */
        public FailingMasterServer(boolean systemStateFails, boolean topicTypesFails)
        {
            super(BindAddress.newPrivate(), AdvertiseAddress.newPrivate());
            this.systemStateFails = systemStateFails;
            this.topicTypesFails = topicTypesFails;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<Object> getSystemState()
        {
            if (systemStateFails)
            {
                throw new RuntimeException();
            }
            return super.getSystemState();
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public List<List<String>> getTopicTypes(GraphName calledId)
        {
            if (topicTypesFails)
            {
                throw new RuntimeException();
            }
            return super.getTopicTypes(calledId);
        }
    }
}
