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

package cpcc.gs.web.components;

import java.util.Arrays;
import java.util.Collection;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

import cpcc.core.services.QueryManager;

/**
 * Layout component for pages of application real vehicle web application.
 */
@Import(module = {"jquery", "bootstrap/collapse", "jquery"}, stylesheet = "layout.css")
public class Layout
{
    /**
     * The page title, for the <title> element and the <h1>element.
     */
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;

    @Property
    private String pageName;

    @Inject
    private Messages messages;

    @Inject
    private ComponentResources resources;

    @Inject
    @Property
    @Symbol(SymbolConstants.APPLICATION_VERSION)
    private String appVersion;

    @Inject
    private QueryManager qm;

    String defaultTitle()
    {
        return messages.get("pagetitle." + resources.getPageName());
    }

    /**
     * @return the class associated to a page name.
     */
    public String getClassForPageName()
    {
        return resources.getPageName().equalsIgnoreCase(pageName) ? "active" : null;
    }

    /**
     * @return the page names in the main menu.
     */
    public Collection<String> getPageNames()
    {
        return Arrays.asList(
            "commons/configuration/edit",
            "commons/contact",
            "commons/ros/overview",
            "commons/sensor/list",
            "commons/rv/list",
            "commons/vv/list",
            "commons/task/list",
            "commons/jobs/list",
            "gsviewer");
    }

    /**
     * @return the label for a given page name.
     */
    public String getPageLabel()
    {
        return messages.get("pagetitle." + pageName);
    }

    /**
     * @return the name of the real vehicle
     */
    public String getRealVehicleName()
    {
        cpcc.core.entities.Parameter rvn =
            qm.findParameterByName(cpcc.core.entities.Parameter.REAL_VEHICLE_NAME, "");
        return rvn != null && rvn.getValue() != null ? rvn.getValue() : "";
    }
}
