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

import static org.apache.tapestry5.EventConstants.PREPARE;
import static org.apache.tapestry5.EventConstants.SUCCESS;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import at.uni_salzburg.cs.cpcc.rv.entities.Device;
import at.uni_salzburg.cs.cpcc.rv.entities.DeviceType;
import at.uni_salzburg.cs.cpcc.rv.pages.Configuration;
import at.uni_salzburg.cs.cpcc.rv.services.DeviceTypeSelectHelpers;
import at.uni_salzburg.cs.cpcc.rv.services.QueryManager;

/**
 * RosEditDevice
 */
public class RosEditDevice
{
    @Inject
    private QueryManager qm;

    @Valid
    @Property
    private Device device;
    
    @PageActivationContext
    private String deviceTopic;

    @Component(id = "form")
    private Form form;

    @OnEvent(PREPARE)
    void loadDevice()
    {
        device = qm.findDeviceByTopicRoot(deviceTopic);
    }

    @OnEvent(SUCCESS)
    @CommitAfter
    Object newDevice()
    {
        if (!device.getTopicRoot().startsWith("/"))
        {
            device.setTopicRoot("/" + device.getTopicRoot());
        }

        qm.saveOrUpdate(device);
        qm.saveOrUpdateMappingAttributes(device);
        return Configuration.class;
    }

    public SelectModel getDeviceTypeNameSelectModel()
    {
        return DeviceTypeSelectHelpers.selectModel(qm.findAllDeviceTypes());
    }

    public ValueEncoder<DeviceType> getDeviceTypeNameEncoder()
    {
        return new DeviceTypeSelectHelpers(qm).valueEncoder();
    }

}
