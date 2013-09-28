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

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;

import at.uni_salzburg.cs.cpcc.rv.entities.DeviceType;
import at.uni_salzburg.cs.cpcc.rv.services.db.QueryManager;

/**
 * DeviceTypeSelectHelpers
 */
public final class DeviceTypeSelectHelpers
{
    private QueryManager qm;

    /**
     * @param queryManager the query manager service.
     */
    public DeviceTypeSelectHelpers(QueryManager queryManager)
    {
        this.qm = queryManager;
    }

    /**
     * @return the new value encoder.
     */
    public ValueEncoder<DeviceType> valueEncoder()
    {
        return new ValueEncoder<DeviceType>()
        {
            /**
             * {@inheritDoc}
             */
            public DeviceType toValue(String clientValue)
            {
                return clientValue == null ? null : qm.findDeviceTypeByName(clientValue);
            }

            /**
             * @param value the device type.
             * @return the device type as a string.
             */
            public String toClient(DeviceType value)
            {
                return value == null ? "" : value.getName();
            }
        };
    }

    /**
     * @param deviceTypeList the list of device type objects.
     * @return the new selection model.
     */
    public static SelectModel selectModel(Collection<DeviceType> deviceTypeList)
    {
        OptionModel[] optionModels = new OptionModel[deviceTypeList.size()];
        int i = 0;
        for (DeviceType deviceType : deviceTypeList)
        {
            optionModels[i++] = new OptionModelImpl(deviceType.getName(), deviceType);
        }

        return new SelectModelImpl(optionModels);
    }

}
