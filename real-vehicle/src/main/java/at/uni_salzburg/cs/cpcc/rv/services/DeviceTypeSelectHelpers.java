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

import java.util.Collection;
import java.util.Locale;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;

import at.uni_salzburg.cs.cpcc.rv.entities.DeviceType;

/**
 * DeviceTypeSelectHelpers
 */
public final class DeviceTypeSelectHelpers
{
    private QueryManager qm;

    public DeviceTypeSelectHelpers(QueryManager queryManager)
    {
        this.qm = queryManager;
    }
    
    public ValueEncoder<DeviceType> valueEncoder()
    {
            return new ValueEncoder<DeviceType>()
            {
                public DeviceType toValue(String clientValue)
                {
                    return clientValue == null ? null : qm.findDeviceTypeByName(clientValue);
                }
        
                public String toClient(DeviceType value)
                {
                    return value == null ? "" : value.getName();
                }
            };
    } 
    
    public static SelectModel selectModel(Collection<DeviceType> deviceTypes)
    {           
        OptionModel[] optionModels = new OptionModel[deviceTypes.size()];
        int i = 0;
        for (DeviceType DeviceType : deviceTypes)
        {
            optionModels[i++] = new OptionModelImpl(DeviceType.getName(), DeviceType);
        }

        return new SelectModelImpl(optionModels);
    }

}
