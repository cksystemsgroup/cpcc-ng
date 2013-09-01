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

import java.util.Collection;

import at.uni_salzburg.cs.cpcc.ros.base.AbstractRosAdapter;

public interface RosAdapterFactory
{
    /**
     * @param topic the ROS topic.
     * @param registered all registered topics.
     * @return the newly built ROS adapter.
     * @throws IllegalAccessException thrown in case of errors.
     * @throws InstantiationException thrown in case of errors.
     */
    AbstractRosAdapter build(RosTopic topic, Collection<RosTopicState> registered) throws InstantiationException,
        IllegalAccessException;
}
