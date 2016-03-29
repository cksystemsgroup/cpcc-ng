// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.commons.components;

import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.hibernate.Session;

import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.services.js.JavascriptService;

/**
 * VvCodeUploadModal implementation.
 */
@Import(stylesheet = "upload-button.css")
@Events({EventConstants.SUBMIT})
public class VvCodeUploadModal implements ClientElement
{
    private static final String ERROR_AT_COMPILATION = "error.at.compilation";
    private static final String ERROR_NAME_REQUIRED = "error.name.required";
    private static final String ERROR_CODE_REQUIRED = "error.code.required";
    private static final String ERROR_API_VERSION_REQUIRED = "error.api.version.required";

    @Parameter(name = "componentClientId", value = "prop:componentResources.id",
        defaultPrefix = BindingConstants.LITERAL)
    private String componentClientId;

    @Inject
    private JavascriptService javaScriptService;

    @Inject
    private Messages messages;

    @Inject
    private Session session;

    @Inject
    protected Request request;

    @Inject
    protected AjaxResponseRenderer ajaxResponseRenderer;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @InjectComponent
    private Form form;

    @Property
    private VirtualVehicle vehicle;

    @Property
    private int instances;

    @Persist(PersistenceConstants.FLASH)
    @Property
    private UploadedFile uploadedFile;

    /**
     * @return the client identification.
     */
    @Override
    public String getClientId()
    {
        return componentClientId;
    }

    void afterRender()
    {
        javaScriptSupport.require("upload-button").with(componentClientId, vehicle.getUuid() != null);
    }

    void setupRender()
    {
        if (vehicle == null)
        {
            vehicle = new VirtualVehicle();
        }
    }

    void onPrepareForSubmit()
    {
        vehicle = new VirtualVehicle();
        vehicle.setApiVersion(1);
        vehicle.setUuid(UUID.randomUUID().toString());
        vehicle.setState(VirtualVehicleState.INIT);
        instances = 1;
    }

    /**
     * Callback function for validating form data.
     */
    void onValidateFromForm()
    {
        if (StringUtils.isBlank(vehicle.getName()))
        {
            form.recordError(messages.format(ERROR_NAME_REQUIRED));
        }

        if (uploadedFile == null)
        {
            form.recordError(messages.format(ERROR_CODE_REQUIRED));
        }
        else
        {
            try
            {
                vehicle.setCode(IOUtils.toString(uploadedFile.getStream()).trim().replace("\\n", "\n"));
                if (StringUtils.isBlank(vehicle.getCode()))
                {
                    form.recordError(messages.format(ERROR_CODE_REQUIRED));
                }

                Object[] result = javaScriptService.codeVerification(vehicle.getCode(), 1);
                if (result != null && result.length > 0)
                {
                    form.recordError(messages.format(ERROR_AT_COMPILATION, result));
                }
            }
            catch (IOException e)
            {
                form.recordError(e.getMessage());
            }
        }

        if (vehicle.getApiVersion() == null)
        {
            form.recordError(messages.format(ERROR_API_VERSION_REQUIRED));
        }
    }

    /**
     * Save the newly created virtual vehicle.
     */
    @CommitAfter
    void onSuccessFromForm()
    {
        if (instances == 1)
        {
            session.save(vehicle);
        }
        else
        {
            for (int k = 0; k < instances; ++k)
            {
                VirtualVehicle vv = new VirtualVehicle();
                vv.setApiVersion(vehicle.getApiVersion());
                vv.setCode(vehicle.getCode());
                vv.setName(String.format("%s-%04d", vehicle.getName(), k));
                vv.setUuid(UUID.randomUUID().toString());
                vv.setState(VirtualVehicleState.INIT);
                session.save(vv);
            }
        }
    }
}
