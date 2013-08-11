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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.ros.internal.node.client.MasterClient;
import org.ros.internal.node.response.Response;
import org.ros.master.client.SystemState;
import org.ros.master.client.TopicSystemState;
import org.ros.master.client.TopicType;
import org.ros.namespace.GraphName;

/**
 * RosQueryServiceImpl
 */
public class RosQueryServiceImpl implements RosQueryService
{
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<RosTopicState> findRegisteredTopics(URI masterSrvURI)
    {
        MasterClient master = new MasterClient(masterSrvURI);
        
        Response<SystemState> systemState = master.getSystemState(GraphName.newAnonymous());
        Response<List<TopicType>> topicTypes = master.getTopicTypes(GraphName.newAnonymous());

        List<TopicType> result3 = topicTypes.getResult();
        Map<String, String> typeMap = new HashMap<String, String>();
        for (TopicType tt : result3)
        {
            typeMap.put(tt.getName(), tt.getMessageType());
        }
        
        Map<String,RosTopicState> topicMap = new TreeMap<String,RosTopicState>();
        
        SystemState result = systemState.getResult();
        Collection<TopicSystemState> topics = result.getTopics();
        for (TopicSystemState t : topics)
        {
            String topicName = t.getTopicName();
            RosTopicState state = new RosTopicState();
            state.setName(topicName);
            state.setPublishers(t.getPublishers());
            state.setSubscribers(t.getSubscribers());
            state.setType(typeMap.get(topicName));
            topicMap.put(topicName, state);
        }
        
        return topicMap.values();
    }

}
