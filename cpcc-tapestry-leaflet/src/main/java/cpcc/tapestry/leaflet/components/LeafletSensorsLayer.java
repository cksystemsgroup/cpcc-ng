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

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.MixinAfter;

/**
 * Leaflet sensors layer component.
 */
@MixinAfter
@Import(stylesheet = {"css/vehicle.css"})
public class LeafletSensorsLayer extends AbstractLeafletAssetLayer
{
    @Override
    void afterRender()
    {
        super.afterRender();

        javaScriptSupport
            .require("leaflet/sensorsLayer")
            .invoke("initialize")
            .with(componentResources.getId(), mapId, name, iconBaseUrl);
    }
}
