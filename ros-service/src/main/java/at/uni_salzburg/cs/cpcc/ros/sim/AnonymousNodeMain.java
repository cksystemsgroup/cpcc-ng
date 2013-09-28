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
package at.uni_salzburg.cs.cpcc.ros.sim;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;

/**
 * AnonymousNodeMain
 * 
 * @param <T> the message type concerned by this node.
 */
public abstract class AnonymousNodeMain<T> extends AbstractNodeMain implements MessageListener<T>
{
    private GraphName myNodeName = GraphName.newAnonymous();
    private T receivedMessage;

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphName getDefaultNodeName()
    {
        return myNodeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNewMessage(T message)
    {
        receivedMessage = message;
    }

    /**
     * @return the received message
     */
    public T getReceivedMessage()
    {
        return receivedMessage;
    }
}
