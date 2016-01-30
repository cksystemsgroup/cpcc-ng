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

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Leaflet coordinates component.
 * 
 * @see https://github.com/MrMufflon/Leaflet.Coordinates
 */
@MixinAfter
@Import(stylesheet = "leaflet.coordinates.css")
public class LeafletCoordinates
{
    /**
     * The Leaflet map identification.
     */
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String mapId;

    /**
     * The position of the coordinate view. Default is 'bottomright'.
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "bottomright")
    private String position;

    /**
     * The number of decimals to show in the coordinate view. Default is 6.
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "6")
    private int decimals;

    /**
     * The decimal separator to use in the coordinate view. Default is '.'.
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = ".")
    private String decimalSeparator;

    /**
     * The label template for latitude values. Default is {@code 'Lat: y}'},
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "Lat: {y}")
    private String labelTemplateLat;

    /**
     * The label template for longitude values. Default is {@code 'Lng: x}'}.
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "Lng: {x}")
    private String labelTemplateLng;

    /**
     * Allow users to enter coordinates. Default is {@code true}.
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "true")
    private boolean enableUserInput;

    /**
     * When set to {@code true} show degrees, minutes, and seconds in the coordinate view. Default is {@code false}.
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "false")
    private boolean useDMS;

    /**
     * When set to {@code true} the order of values in the coordinate view is latitude and then longitude. Default is
     * {@code false}.
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "true")
    private boolean useLatLngOrder;

    @Environmental
    private JavaScriptSupport javaScriptSupport;

    void afterRender()
    {
        JSONObject params = new JSONObject("position", position, "decimals", decimals,
            "decimalSeparator", decimalSeparator, "labelTemplateLat", labelTemplateLat,
            "labelTemplateLng", labelTemplateLng, "enableUserInput", enableUserInput,
            "useDMS", useDMS, "useLatLngOrder", useLatLngOrder);

        javaScriptSupport.require("leaflet/coordinates").with(mapId, params);
    }

}
