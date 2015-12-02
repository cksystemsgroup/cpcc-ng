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

package cpcc.tapestry.leaflet.components;

import javax.inject.Inject;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Leaflet map component.
 */
@Import(stylesheet = {"leaflet.css", "leaflet.draw.css"})
public class LeafletMap
{
    @Environmental
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private ComponentResources componentResources;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "")
    private String cssClass;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String height;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "false")
    private Boolean drawControl;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "[0,0]")
    private String center;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "13")
    private Integer zoom;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String iconBaseUrl;

    @Inject
    @Path("images/dummyAsset.txt")
    private Asset dummyAsset;

    void afterRender()
    {
        if (iconBaseUrl == null)
        {
            iconBaseUrl = dummyAsset.toClientURL().replace("dummyAsset.txt", "");
        }

        JSONObject params = new JSONObject();
        params.put("height", height);
        params.put("options", new JSONObject("drawControl", drawControl));
        params.put("center", new JSONArray(center));
        params.put("zoom", zoom);
        params.put("iconBaseUrl", iconBaseUrl);

        javaScriptSupport.require("leaflet/map").with(componentResources.getId(), params);
    }
}
