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

package at.uni_salzburg.cs.cpcc.commons.pages.ros;

import java.io.IOException;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.Messages;
import org.hibernate.Session;

import at.uni_salzburg.cs.cpcc.commons.pages.configuration.ConfigurationEdit;
import at.uni_salzburg.cs.cpcc.core.entities.Device;
import at.uni_salzburg.cs.cpcc.core.entities.DeviceType;
import at.uni_salzburg.cs.cpcc.core.services.DeviceTypeSelectHelpers;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.services.opts.OptionsParserService;
import at.uni_salzburg.cs.cpcc.core.services.opts.ParseException;
import at.uni_salzburg.cs.cpcc.ros.services.RosNodeService;

/**
 * AbstractRosModifyDevice
 */
public abstract class AbstractRosModifyDevice
{
    protected static final String ERROR_TOPIC_ALREADY_USED = "error.topic.already.used";
    protected static final String ERROR_TOPIC_MUST_START_WITH_SLASH = "error.topic.must.start.with.slash";
    protected static final String ERROR_PARSING = "error.parsing";
    protected static final String ERROR_PARSING_SYNTAX = "error.parsing.syntax";

    @Inject
    protected Session session;

    @Inject
    protected QueryManager qm;

    @Inject
    protected RosNodeService nodeService;

    @Inject
    protected OptionsParserService parserService;

    @Inject
    protected Messages messages;

    @Valid
    @Property
    protected Device device;

    @Component(id = "form")
    protected Form form;

    /**
     * @return the page to show next.
     */
    @CommitAfter
    protected Object onSuccessFromForm()
    {
        if (!device.getTopicRoot().startsWith("/"))
        {
            device.setTopicRoot("/" + device.getTopicRoot());
        }

        session.saveOrUpdate(device);
        qm.saveOrUpdateMappingAttributes(device);
        nodeService.updateDevice(device);
        nodeService.updateMappingAttributes(qm.findMappingAttributesByDevice(device));
        return ConfigurationEdit.class;
    }

    /**
     * @return the device type select model.
     */
    public SelectModel getDeviceTypeNameSelectModel()
    {
        return DeviceTypeSelectHelpers.selectModel(qm.findAllDeviceTypes());
    }

    /**
     * @return the device type name encoder.
     */
    public ValueEncoder<DeviceType> getDeviceTypeNameEncoder()
    {
        return new DeviceTypeSelectHelpers(qm).valueEncoder();
    }

    /**
     * Check the configuration of the device.
     */
    protected void checkConfig()
    {
        if (device.getConfiguration() != null)
        {
            try
            {
                parserService.parse(device.getConfiguration());
            }
            catch (ParseException e)
            {
                String msg = parserService.formatParserErrorMessage(device.getConfiguration(),
                    messages.get(ERROR_PARSING_SYNTAX), e);
                form.recordError(msg);
            }
            catch (IOException e)
            {
                String msg = String.format(messages.get(ERROR_PARSING), e.getMessage());
                form.recordError(msg);
            }
        }
    }
}
