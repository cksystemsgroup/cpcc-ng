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
package at.uni_salzburg.cs.cpcc.commons.pages.ros;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;

import at.uni_salzburg.cs.cpcc.commons.components.DeviceTree;
import at.uni_salzburg.cs.cpcc.commons.services.image.ImageTagService;
import at.uni_salzburg.cs.cpcc.core.entities.Device;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.ros.base.AbstractRosAdapter;
import at.uni_salzburg.cs.cpcc.ros.services.RosNodeService;
import at.uni_salzburg.cs.cpcc.ros.sim.RosNodeGroup;

/**
 * RosDeviceDetail
 */
public class RosDeviceDetail
{
    @Inject
    private QueryManager qm;

    @Inject
    private ImageTagService imageTagService;

    @Property
    @Inject
    private RosNodeService nodeService;

    @PageActivationContext
    @Property
    private String deviceDetailLinkContext;

    @Component(parameters = {"devices=deviceList"})
    private DeviceTree deviceTree;

    @Property
    private String deviceParameter;

    @Property
    private AbstractRosAdapter adapter;

    @Property
    private String adapterParameter;

    @InjectComponent
    private Zone zone;

    /**
     * @return the list of devices.
     */
    public List<Device> getDeviceList()
    {
        return qm.findAllDevices();
    }

    /**
     * @return the device type name.
     */
    public String getDeviceTypeName()
    {
        Device device = qm.findDeviceByTopicRoot(deviceDetailLinkContext);
        if (device == null)
        {
            return null;
        }
        return device.getType().getName();
    }

    /**
     * @return the device parameter list.
     */
    public Collection<String> getDeviceParameterList()
    {
        Device device = qm.findDeviceByTopicRoot(deviceDetailLinkContext);
        if (device == null)
        {
            return null;
        }
        RosNodeGroup group = nodeService.getDeviceNodes().get(device.getTopicRoot());
        if (group == null)
        {
            return null;
        }
        return renderParameterList(group.getCurrentState());
    }

    /**
     * @return the adapter list.
     */
    public Collection<AbstractRosAdapter> getAdapterList()
    {
        if (deviceDetailLinkContext == null)
        {
            return null;
        }
        Device device = qm.findDeviceByTopicRoot(deviceDetailLinkContext);
        if (device == null)
        {
            return null;
        }
        List<AbstractRosAdapter> l = nodeService.getAdapterNodes().get(device.getTopicRoot());
        return l;
    }

    /**
     * @return the adapter parameter list.
     */
    public Collection<String> getAdapterParameterList()
    {
        if (adapter == null)
        {
            return null;
        }
        return renderParameterList(adapter.getCurrentState());
    }

    /**
     * @return true if the current adapter has image data.
     */
    public Boolean getAdapterHasImage()
    {
        if (adapter == null)
        {
            return Boolean.FALSE;
        }
        return "sensor_msgs/Image".equals(adapter.getTopic().getType());
    }

    /**
     * @return the tag for the adapter's image data.
     */
    public String getAdapterImageTag()
    {
        return imageTagService.getRosImageTag(adapter);
    }

    /**
     * @return the image dimensions.
     */
    public Dimension getDimension()
    {
        return imageTagService.getRosImageDimension(adapter);
    }

    /**
     * @param state the state map.
     * @return the parameter list.
     */
    private static Collection<String> renderParameterList(Map<String, List<String>> state)
    {
        if (state == null)
        {
            return Arrays.asList(new String[0]);
        }

        Map<String, String> parameterMap = new TreeMap<String, String>();
        for (Entry<String, List<String>> entry : state.entrySet())
        {
            StringBuilder b = new StringBuilder();
            b.append(entry.getKey()).append(" = ");

            List<String> valueList = entry.getValue();
            boolean first = true;
            if (valueList.size() > 1)
            {
                b.append("(");
            }
            for (String value : valueList)
            {
                if (first)
                {
                    first = false;
                }
                else
                {
                    b.append(",");
                }
                b.append(value);
            }
            if (valueList.size() > 1)
            {
                b.append(")");
            }
            parameterMap.put(entry.getKey(), b.toString());
        }

        return parameterMap.values();
    }

}
