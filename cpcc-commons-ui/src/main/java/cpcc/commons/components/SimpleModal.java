// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.ajax.JavaScriptCallback;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Based on http://readyareyou.blogspot.com.au/2012/11/tapestry5-bootstrap-modal-dialog.html
 */
public class SimpleModal implements ClientElement
{
    @Parameter(name = "componentClientId", value = "prop:componentResources.id",
        defaultPrefix = BindingConstants.LITERAL)
    private String componentClientId;

    @Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL)
    private boolean large;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClientId()
    {
        return componentClientId;
    }

    public String getModalClass()
    {
        return large ? "modal-lg" : "";
    }

    void afterRender()
    {
        javaScriptSupport.require("activate-modal").with(componentClientId, new JSONObject());
    }

    /**
     * Hide the modal window.
     */
    public void hide()
    {
        ajaxResponseRenderer.addCallback(makeScriptToHideModal());
    }

    private JavaScriptCallback makeScriptToHideModal()
    {
        return javascriptSupport -> javascriptSupport.require("hide-modal").with(componentClientId);
    }
}
