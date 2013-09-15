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
package at.uni_salzburg.cs.cpcc.rv.pages.ros;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;

import at.uni_salzburg.cs.cpcc.ros.sim.RosNodeGroup;
import at.uni_salzburg.cs.cpcc.rv.components.DeviceTree;
import at.uni_salzburg.cs.cpcc.rv.entities.Device;
import at.uni_salzburg.cs.cpcc.rv.services.db.QueryManager;
import at.uni_salzburg.cs.cpcc.rv.services.ros.RosNodeService;

/**
 * RosDeviceDetail
 */
public class RosDeviceDetail
{
    @Inject
    private QueryManager qm;
    
    @Inject
    private RosNodeService nodeService;
    
    @Property
    @PageActivationContext
    private String deviceDetailLinkContext;
    
    @Component(parameters = { "devices=deviceList" })
    private DeviceTree deviceTree;
    
    @Property
    private String deviceParameter;
    
    /**
     * @return the list of devices.
     */
    public List<Device> getDeviceList()
    {
        return qm.findAllDevices();
    }
    
    /**
     * @return the device parameter list.
     */
    public Collection<String> getDeviceParameterList()
    {
        Device device = qm.findDeviceByTopicRoot(deviceDetailLinkContext);
        RosNodeGroup group = nodeService.getDeviceNodes().get(device.getTopicRoot());
        if (group == null)
        {
            return null;
        }
        
        Map<String, List<String>> state = group.getCurrentState();
        if (state == null)
        {
            return null;
        }
        
        Map<String,String> parameterMap = new TreeMap<String, String>();
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
            parameterMap.put(entry.getKey() ,b.toString());
        }

        return parameterMap.values();
    }
}
