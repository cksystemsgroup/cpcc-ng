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
package at.uni_salzburg.cs.cpcc.ros.base;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;

/**
 * AbstractRosAdapter
 */
public abstract class AbstractRosAdapter extends AbstractNodeMain
{
    private GraphName defaultNodeName = GraphName.newAnonymous();
    
    private GraphName name;
    
    private RosTopic topic;

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphName getDefaultNodeName()
    {
        return defaultNodeName;
    }

    /**
     * @return ROS node path
     */
    public GraphName getName()
    {
        return name;
    }

    /**
     * @param name ROS node path
     */
    public void setName(GraphName name)
    {
        this.name = name;
    }

    /**
     * @return ROS topic
     */
    public RosTopic getTopic()
    {
        return topic;
    }

    /**
     * @param topic ROS topic
     */
    public void setTopic(RosTopic topic)
    {
        this.topic = topic;
    }
}
