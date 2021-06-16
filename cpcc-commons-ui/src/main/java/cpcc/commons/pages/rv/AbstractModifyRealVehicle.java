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

package cpcc.commons.pages.rv;

import java.util.Date;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.commons.Messages;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import cpcc.core.entities.RealVehicle;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.opts.OptionsParserService;

/**
 * AbstractModifyRealVehicle
 */
public class AbstractModifyRealVehicle
{
    protected static final String ERROR_PARSING = "error.parsing";
    protected static final String ERROR_PARSING_SYNTAX = "error.parsing.syntax";
    protected static final String ERROR_AREA_OF_OPERATIUONS_IS_NULL = "error.area.of.operations.is.null";
    protected static final String ERROR_REAL_VEHICLE_NAME_ALREADY_EXISTS = "error.real.vehicle.name.already.exists";
    protected static final String ERROR_REAL_VEHICLE_URL_ALREADY_EXISTS = "error.real.vehicle.url.already.exists";

    protected static final String MSG_DEFINE_AOO = "info.define.areaOfOperations";

    @Inject
    protected HibernateSessionManager sessionManager;

    @Inject
    protected RealVehicleRepository rvRepo;

    @Inject
    protected OptionsParserService parserService;

    @Inject
    protected Messages messages;

    @Inject
    protected AlertManager alertManager;

    @Valid
    @Property
    protected RealVehicle realVehicle;

    @Component(id = "form")
    protected Form form;

    /**
     * @return the page to show next.
     */
    @CommitAfter
    protected Object onSuccessFromForm()
    {
        realVehicle.setLastUpdate(new Date());
        sessionManager.getSession().saveOrUpdate(realVehicle);
        sessionManager.commit();

        if (realVehicle.getAreaOfOperation() == null)
        {
            alertManager.info(messages.get(MSG_DEFINE_AOO));
        }

        return RvList.class;
    }
}
