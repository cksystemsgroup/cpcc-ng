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

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Hidden;
import org.apache.tapestry5.corelib.components.Submit;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Leaflet draw control component.
 */
@MixinAfter
public class LeafletDrawControl
{
    @InjectComponent
    private Hidden drawData;

    @InjectComponent
    private Submit drawButton;

    @InjectComponent
    private Form popupTemplate;

    @Environmental
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private ComponentResources componentResources;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String mapId;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "topleft")
    private String position;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "true")
    private Boolean polygonEnabled;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "true")
    private Boolean rectangleEnabled;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "true")
    private Boolean polylineEnabled;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "true")
    private Boolean circleEnabled;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private JSONObject data;

    void afterRender()
    {
        JSONObject draw = new JSONObject();

        draw.put("polygon", polygonEnabled
            ? new JSONObject("{ allowIntersection: false, drawError: { color: '#b00b00', timeout: 1000 }, "
                + "shapeOptions: { color: '#03f' }, showArea: true }")
            : Boolean.FALSE);

        draw.put("rectangle", rectangleEnabled
            ? new JSONObject("{ allowIntersection: false, drawError: { color: '#b00b00', timeout: 1000 }, "
                + "shapeOptions: { color: '#03f' }, showArea: true }")
            : Boolean.FALSE);

        draw.put("polyline", polylineEnabled
            ? new JSONObject("{ allowIntersection: false, drawError: { color: '#b00b00', timeout: 1000 }, "
                + "shapeOptions: { color: '#03f' }, showArea: false} ")
            : Boolean.FALSE);

        draw.put("circle", circleEnabled
            ? new JSONObject("{ allowIntersection: false, drawError: { color: '#b00b00', timeout: 1000 }, "
                + "shapeOptions: { color: '#03f' }, showArea: true }")
            : Boolean.FALSE);

        JSONObject params = new JSONObject("position", position, "draw", draw);
        params.put("position", position);
        params.put("dataId", drawData.getClientId());
        params.put("submitId", drawButton.getClientId());
        params.put("popupTemplateId", popupTemplate.getClientId());

        javaScriptSupport
            .require("leaflet/drawControl")
            .with(componentResources.getId(), mapId, params);
    }

}
