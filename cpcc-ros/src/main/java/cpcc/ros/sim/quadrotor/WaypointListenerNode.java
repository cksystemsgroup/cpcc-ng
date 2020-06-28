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

package cpcc.ros.sim.quadrotor;

import java.util.ArrayList;
import java.util.List;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WaypointListenerNode
 */
public class WaypointListenerNode extends AbstractNodeMain
{
    private static final Logger LOG = LoggerFactory.getLogger(WaypointListenerNode.class);

    private String topic;

    private List<MessageListener<big_actor_msgs.LatLngAlt>> messageListenerList = new ArrayList<>();

    /**
     * @param topic the topic root / prefix
     */
    public WaypointListenerNode(String topic)
    {
        this.topic = topic;
    }

    /**
     * @param listener a message listener to be added.
     */
    public void addMessageListener(MessageListener<big_actor_msgs.LatLngAlt> listener)
    {
        messageListenerList.add(listener);
    }

    /**
     * @param listener a message listener to be removed.
     */
    public void removeMessageListener(MessageListener<big_actor_msgs.LatLngAlt> listener)
    {
        while (messageListenerList.contains(listener))
        {
            messageListenerList.remove(listener);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphName getDefaultNodeName()
    {
        return GraphName.of(topic);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(ConnectedNode connectedNode)
    {
        LOG.info("onStart()");

        Subscriber<big_actor_msgs.LatLngAlt> subscriber =
            connectedNode.newSubscriber(topic, big_actor_msgs.LatLngAlt._TYPE);

        for (MessageListener<big_actor_msgs.LatLngAlt> listener : messageListenerList)
        {
            subscriber.addMessageListener(listener);
        }
    }

}
