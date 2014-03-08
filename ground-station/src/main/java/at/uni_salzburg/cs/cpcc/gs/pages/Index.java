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
package at.uni_salzburg.cs.cpcc.gs.pages;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;

import at.uni_salzburg.cs.cpcc.commons.components.DeviceTree;
import at.uni_salzburg.cs.cpcc.commons.services.RealVehicleState;
import at.uni_salzburg.cs.cpcc.commons.services.RealVehicleStateService;
import at.uni_salzburg.cs.cpcc.core.entities.Device;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;

/**
 * Index
 */
public class Index
{
    @Inject
    private QueryManager qm;

    @Component(parameters = {"devices=deviceList"})
    private DeviceTree deviceTree;

    /**
     * @return the list of devices.
     */
    public List<Device> getDeviceList()
    {
        return qm.findAllDevices();
    }
    
    @Property
    @Inject
    private RealVehicleStateService state;

    @Property
    private RealVehicleState stateString;
    
    /**
     * @return the state list.
     */
    public Collection<RealVehicleState> getStateList()
    {
//        Map<RealVehicle, byte[]> x = state.getRealVehicleStatus();
//
//        Map<String, BuggerIt> y = new TreeMap<String, BuggerIt>();
//
//        for (Entry<RealVehicle, byte[]> e : x.entrySet())
//        {
//            String rv = e.getKey().getName();
//            byte[] state = e.getValue();
//            
//            try
//            {
//                BuggerIt b = new BuggerIt(rv, state != null ? new String(state, "UTF-8") : "", true);
//                y.put(rv, b);
//            }
//            catch (UnsupportedEncodingException e1)
//            {
//                // TODO please translate me!
//                y.put(rv, new BuggerIt(rv, "unknown (please translate me!)", false));
//            }
//        }
//
//        List<BuggerIt> result = new ArrayList<BuggerIt>();
//        for (Entry<String, BuggerIt> e : y.entrySet())
//        {
//            result.add(e.getValue());
//        }
//        
        return state.getRealVehicleStatus();
    }
    
//    public static class BuggerIt
//    {
//        private String name;
//        private String state;
//        private boolean connected;
//        
//        public BuggerIt(String name, String state, boolean connected)
//        {
//            this.name = name;
//            this.state = state;
//            this.connected = connected;
//        }
//        
//        public String getName()
//        {
//            return name;
//        }
//        
//        public String getState()
//        {
//            return state;
//        }
//        
//        public boolean isConnected()
//        {
//            return connected;
//        }
//    }
}
