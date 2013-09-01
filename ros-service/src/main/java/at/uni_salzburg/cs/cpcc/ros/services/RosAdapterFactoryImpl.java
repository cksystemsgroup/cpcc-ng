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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ros.namespace.GraphName;

import at.uni_salzburg.cs.cpcc.ros.actuators.SimpleWayPointControllerAdapter;
import at.uni_salzburg.cs.cpcc.ros.base.AbstractRosAdapter;
import at.uni_salzburg.cs.cpcc.ros.sensors.CameraSensorAdapter;
import at.uni_salzburg.cs.cpcc.ros.sensors.GpsSensorAdapter;

public class RosAdapterFactoryImpl implements RosAdapterFactory
{
    @SuppressWarnings("serial")
    private final static Map<String, List<String>> typeMap = new HashMap<String, List<String>>()
    {
        {
            put("sensor_msgs/Image", Arrays.asList("sensor_msgs/Image", "sensor_msgs/CameraInfo"));
            put("sensor_msgs/CameraInfo", Arrays.asList("sensor_msgs/Image", "sensor_msgs/CameraInfo"));
        }
    };

    private final static String[] topicSetters = {"setTopic", "setTopic2"};

    @SuppressWarnings("serial")
    private final static Map<String, Class<? extends AbstractRosAdapter>> classMap =
        new HashMap<String, Class<? extends AbstractRosAdapter>>()
        {
            {
                put("sensor_msgs/Image", CameraSensorAdapter.class);
                put("sensor_msgs/CameraInfo", CameraSensorAdapter.class);
                put("sensor_msgs/NavSatFix", GpsSensorAdapter.class);
                put("geometry_msgs/Pose", SimpleWayPointControllerAdapter.class);
            }
        };

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractRosAdapter build(RosTopic topic, Collection<RosTopicState> registered)
        throws InstantiationException, IllegalAccessException
    {
        if (typeMap.containsKey(topic.getType()))
        {
            return buildMulti(topic, registered);
        }
        else
        {
            return buildSimple(topic);
        }
    }

    /**
     * @param topic the ROS topic.
     * @return the newly build RosAdapter.
     * @throws InstantiationException thrown in case of errors.
     * @throws IllegalAccessException thrown in case of errors.
     */
    private AbstractRosAdapter buildSimple(RosTopic topic)
        throws InstantiationException, IllegalAccessException
    {
        Class<? extends AbstractRosAdapter> clazz = classMap.get(topic.getType());

        if (clazz == null)
        {
            return null;
        }

        AbstractRosAdapter newInstance = clazz.newInstance();
        newInstance.setName(GraphName.of(topic.getName()));
        newInstance.setTopic(topic);
        return newInstance;
    }

    /**
     * @param topic topic the ROS topic.
     * @param registered all registered ROS topics.
     * @return the newly build RosAdapter.
     * @throws InstantiationException thrown in case of errors.
     * @throws IllegalAccessException thrown in case of errors.
     * @throws SecurityException thrown in case of errors.
     */
    private AbstractRosAdapter buildMulti(RosTopic topic, Collection<RosTopicState> registered)
        throws InstantiationException, IllegalAccessException, SecurityException
    {
        String dirname = topic.getName().replaceAll("/[^/]+", "/");

        Map<String, RosTopicState> states = filterAssociatedTopics(registered, dirname);

        AbstractRosAdapter newInstance = buildSimple(topic);

        try
        {
            List<String> typeList = typeMap.get(topic.getType());

            for (int k = 0, l = typeList.size(); k < l; ++k)
            {
                Method declaredMethod = newInstance.getClass().getDeclaredMethod(topicSetters[k], RosTopic.class);
                declaredMethod.invoke(newInstance, states.get(typeList.get(k)));
            }
        }
        catch (NoSuchMethodException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return newInstance;
    }

    /**
     * @param registered all associated topics.
     * @param dirname the common topic path.
     * @return all topics associated to the common path.
     */
    private Map<String, RosTopicState> filterAssociatedTopics(Collection<RosTopicState> registered, String dirname)
    {
        Map<String, RosTopicState> states = new HashMap<String, RosTopicState>();
        for (RosTopicState tpc : registered)
        {
            if (tpc.getName().startsWith(dirname))
            {
                states.put(tpc.getType(), tpc);
            }
        }
        return states;
    }

}
