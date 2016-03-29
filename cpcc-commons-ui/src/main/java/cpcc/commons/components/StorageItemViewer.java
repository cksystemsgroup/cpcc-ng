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
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Symbol;

import cpcc.vvrte.entities.VirtualVehicleStorage;
import cpcc.vvrte.utils.JavaScriptUtils;
import cpcc.vvrte.utils.VirtualVehicleStorageUtils;

/**
 * Storage Item Viewer implementation.
 */
@Import(module = {"bootstrap/modal"}, stylesheet = "vv-storage.css")
public class StorageItemViewer
{
    @Inject
    @Symbol(SymbolConstants.CONTEXT_PATH)
    @Property
    private String contextPath;

    @Parameter(required = true, defaultPrefix = BindingConstants.PROP)
    @Property
    private VirtualVehicleStorage item;

    @Parameter(required = false, defaultPrefix = BindingConstants.PROP, value = "")
    @Property
    private String altText;

    @Parameter(required = false, defaultPrefix = BindingConstants.PROP, value = "")
    @Property
    private String titleText;

    @Inject
    private ComponentResources res;

    public String getClientId()
    {
        return res.getId() + "_" + item.getId();
    }

    /**
     * @return true if this item is an image, false otherwise.
     */
    public boolean isImage()
    {
        return VirtualVehicleStorageUtils.isItemAnImage(item);
    }

    /**
     * @return the item as an JSON string.
     */
    public String getJsonString()
    {
        return JavaScriptUtils.toJsonString(item.getContent());
    }

    /**
     * @return the image source path.
     */
    public String getImgSource()
    {
        return contextPath + "/commons/vv/storageImage/" + System.currentTimeMillis() + "/" + item.getId();
    }
}
