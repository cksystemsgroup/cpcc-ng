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
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Leaflet region layer component.
 */
@MixinAfter
public class LeafletRegionLayer extends AbstractLeafletLayer
{
    private static final String REGION_UPDATE = "regionUpdate";
    private static final String UPDATE_REGION_DATA = "updateRegionData";
    private static final String INITIALIZE = "initialize";
    private static final String LEAFLET_REGION_LAYER = "leaflet/regionLayer";

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @Inject
    private Request request;

    @InjectComponent
    private Zone regionZone;

    @Property
    @Parameter(required = true, principal = true, autoconnect = true)
    private String value;

    @Parameter
    private Object[] context;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true)
    private int frequencySecs;

    void afterRender()
    {
        String eventURL = componentResources.createEventLink(REGION_UPDATE, context).toAbsoluteURI();

        javaScriptSupport
            .require(LEAFLET_REGION_LAYER)
            .invoke(INITIALIZE)
            .with(componentResources.getId(), mapId, name, regionZone.getClientId(), eventURL, frequencySecs);

        javaScriptSupport
            .require(LEAFLET_REGION_LAYER)
            .invoke(UPDATE_REGION_DATA)
            .with(componentResources.getId(), mapId);
    }

    void onRegionUpdate()
    {
        if (request.isXHR())
        {
            ajaxResponseRenderer.addRender(regionZone);
            addRegionDataUpdateCallback();
        }
    }

    void addRegionDataUpdateCallback()
    {
        ajaxResponseRenderer.addCallback((JavaScriptSupport jsSupport) -> jsSupport
            .require(LEAFLET_REGION_LAYER)
            .invoke(UPDATE_REGION_DATA)
            .with(componentResources.getId(), mapId));
    }

    public String getRegions()
    {
        return value;
    }
}
