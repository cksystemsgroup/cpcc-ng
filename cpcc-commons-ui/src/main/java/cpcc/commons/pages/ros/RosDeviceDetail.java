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

package cpcc.commons.pages.ros;

import java.awt.Dimension;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;

import cpcc.commons.services.ImageTagService;
import cpcc.core.entities.Device;
import cpcc.core.services.QueryManager;
import cpcc.ros.base.AbstractRosAdapter;
import cpcc.ros.services.RosNodeService;
import cpcc.ros.sim.RosNodeGroup;

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

        return device != null
            ? device.getType().getName()
            : null;
    }

    /**
     * @return the device parameter list.
     */
    public Collection<String> getDeviceParameterList()
    {
        Device device = qm.findDeviceByTopicRoot(deviceDetailLinkContext);
        if (device == null)
        {
            return Collections.emptySet();
        }

        RosNodeGroup group = nodeService.getDeviceNodes().get(device.getTopicRoot());
        if (group == null)
        {
            return Collections.emptySet();
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
            return Collections.emptySet();
        }

        Device device = qm.findDeviceByTopicRoot(deviceDetailLinkContext);
        if (device == null)
        {
            return Collections.emptySet();
        }

        return nodeService.getAdapterNodes().get(device.getTopicRoot());
    }

    /**
     * @return the adapter parameter list.
     */
    public Collection<String> getAdapterParameterList()
    {
        return adapter != null
            ? renderParameterList(adapter.getCurrentState())
            : Collections.<String> emptySet();
    }

    /**
     * @return true if the current adapter has image data.
     */
    public boolean getAdapterHasImage()
    {
        return adapter != null && "sensor_msgs/Image".equals(adapter.getTopic().getType());
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
            return Collections.emptySet();
        }

        Map<String, String> parameterMap = new TreeMap<>();
        for (Entry<String, List<String>> entry : state.entrySet())
        {
            parameterMap.put(entry.getKey(), getEntryString(entry.getKey(), entry.getValue()));
        }

        return parameterMap.values();
    }

    /**
     * @param key the key.
     * @param valueList the list of values.
     * @return the key and values as one {@code String}.
     */
    private static String getEntryString(String key, List<String> valueList)
    {
        StringBuilder b = new StringBuilder(key).append(" = ");

        if (valueList.size() > 1)
        {
            b.append("(");
        }

        b.append(StringUtils.join(valueList, ","));

        if (valueList.size() > 1)
        {
            b.append(")");
        }
        return b.toString();
    }

}
