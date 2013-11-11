/*
 * This code is part of the CPCC-NG project. Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com> This program
 * is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.rv.pages.vehicle;

import static org.apache.tapestry5.EventConstants.SUCCESS;

import java.io.IOException;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.Messages;

import at.uni_salzburg.cs.cpcc.rv.pages.Vehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.services.VvRteRepository;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptService;

/**
 * AbstractModifyVehicle
 */
public class AbstractModifyVehicle
{
    public static final String ERROR_AT_COMPILATION = "error.at.compilation";
    private static final String ERROR_NAME_REQUIRED = "error.name.required";
    private static final String ERROR_CODE_REQUIRED = "error.code.required";

    @Inject
    protected VvRteRepository repository;

    @Inject
    protected JavascriptService javaScriptService;

    @Inject
    protected Messages messages;

    @Valid
    @Property
    protected VirtualVehicle vehicle;

    @Component(id = "form")
    protected Form form;

    /**
     * @return the page to show next.
     */
    @OnEvent(SUCCESS)
    @CommitAfter
    protected Object storeVehicle()
    {
        vehicle.setCode(vehicle.getCode().trim());
        repository.saveOrUpdate(vehicle);
        return Vehicle.class;
    }

    /**
     * Callback function for validating form data.
     */
    void onValidateFromForm()
    {
        if (StringUtils.isEmpty(vehicle.getName()))
        {
            form.recordError(messages.format(ERROR_NAME_REQUIRED));
        }
        
        if (StringUtils.isEmpty(vehicle.getCode()))
        {
            form.recordError(messages.format(ERROR_CODE_REQUIRED));
        }

        try
        {
            Object[] result = javaScriptService.codeVerification(vehicle.getCode(), 1);
            if (result != null)
            {
                form.recordError(messages.format(ERROR_AT_COMPILATION, result));
            }
        }
        catch (IOException e)
        {
            form.recordError(e.getMessage());
        }
    }
}
