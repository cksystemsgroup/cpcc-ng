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
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.json.JSONObject;

/**
 * Leaflet tile layer component.
 */
@MixinAfter
public class LeafletTileLayer extends AbstractLeafletLayer
{
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String urlTemplate;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "{}")
    private String options;

    void afterRender()
    {
        JSONObject jsonOptions = new JSONObject(options);

        javaScriptSupport
            .require("leaflet/tileLayer")
            .with(componentResources.getId(), mapId, name, urlTemplate, jsonOptions);
    }
}
