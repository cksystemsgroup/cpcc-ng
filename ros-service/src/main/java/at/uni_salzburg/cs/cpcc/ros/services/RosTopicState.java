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

import java.util.Set;

/**
 * RosTopicState
 */
public class RosTopicState extends RosTopic
{
    private Set<String> publishers;
    private Set<String> subscribers;
    
    /**
     * @param publishers the registered publishers.
     */
    public void setPublishers(Set<String> publishers)
    {
        this.publishers = publishers;
    }
    
    /**
     * @return the registered publishers.
     */
    public Set<String> getPublishers()
    {
        return publishers;
    }
    
    /**
     * @param subscribers the registered subscribers.
     */
    public void setSubscribers(Set<String> subscribers)
    {
        this.subscribers = subscribers;
    }
    
    /**
     * @return the registered subscribers.
     */
    public Set<String> getSubscribers()
    {
        return subscribers;
    }

}
