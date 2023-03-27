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

package cpcc.commons.pages.sensor;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.commons.Messages;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import cpcc.core.entities.SensorDefinition;
import cpcc.core.services.QueryManager;
import cpcc.core.services.opts.OptionsParserService;
import cpcc.core.services.opts.ParseException;

/**
 * AbstractModifySensor
 */
public class AbstractModifySensor
{
    protected static final String ERROR_PARSING = "error.parsing";
    protected static final String ERROR_PARSING_SYNTAX = "error.parsing.syntax";

    @Inject
    protected HibernateSessionManager sessionManager;

    @Inject
    protected QueryManager qm;

    @Inject
    protected OptionsParserService parserService;

    @Inject
    protected Messages messages;

    @Property
    protected SensorDefinition sensor;

    @Component(id = "form")
    protected Form form;

    /**
     * @return the page to show next.
     */
    @CommitAfter
    protected Object onSuccessFromForm()
    {
        sensor.setLastUpdate(new Date());
        sessionManager.getSession().saveOrUpdate(sensor);
        sessionManager.commit();

        // TODO
        //        rvss.notifyConfigurationChange();
        //        confSync.notifyConfigurationChange();
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
