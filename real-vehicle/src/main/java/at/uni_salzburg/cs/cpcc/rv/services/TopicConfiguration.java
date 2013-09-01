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
package at.uni_salzburg.cs.cpcc.rv.services;

import org.ros.namespace.GraphName;

/**
 * TopicConfiguration
 */
public class TopicConfiguration
{
    private GraphName name;

    private boolean builtin;

    private String configString;

    /**
     * @return the ROS graph name.
     */
    public GraphName getName()
    {
        return name;
    }

    /**
     * @param name the ROS graph name.
     */
    public void setName(GraphName name)
    {
        this.name = name;
    }

    /**
     * @return topic is build-in.
     */
    public boolean isBuiltin()
    {
        return builtin;
    }

    /**
     * @param builtin set topic as built-in.
     */
    public void setBuiltin(boolean builtin)
    {
        this.builtin = builtin;
    }

    /**
     * @return the configuration string.
     */
    public String getConfigString()
    {
        return configString;
    }

    /**
     * @param configString the configuration string.
     */
    public void setConfigString(String configString)
    {
        this.configString = configString;
    }

}
