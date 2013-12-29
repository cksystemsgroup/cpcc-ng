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
package at.uni_salzburg.cs.cpcc.commons.pages.sensor;

import static org.apache.tapestry5.EventConstants.SUCCESS;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.Messages;

import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.services.opts.OptionsParserService;
import at.uni_salzburg.cs.cpcc.core.services.opts.ParseException;

public class AbstractModifySensor
{
    protected static final String ERROR_PARSING = "error.parsing";
    protected static final String ERROR_PARSING_SYNTAX = "error.parsing.syntax";

    @Inject
    protected QueryManager qm;

    @Inject
    protected OptionsParserService parserService;

    @Inject
    protected Messages messages;

    @Valid
    @Property
    protected SensorDefinition sensor;

    @Component(id = "form")
    protected Form form;

    /**
     * @return the page to show next.
     */
    @OnEvent(SUCCESS)
    @CommitAfter
    protected Object storeSensorDefinition()
    {
        sensor.setLastUpdate(new Date());
        qm.saveOrUpdate(sensor);
        return SensorList.class;
    }

    /**
     * Check the parameters of the sensor definition.
     */
    protected void checkParameters()
    {
        if (sensor.getParameters() != null)
        {
            try
            {
                parserService.parse(sensor.getParameters());
            }
            catch (ParseException e)
            {
                String msg = parserService.formatParserErrorMessage(sensor.getParameters(),
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
