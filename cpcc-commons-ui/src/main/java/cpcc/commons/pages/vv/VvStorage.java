// This code is part of the CPCC-NG project.
//
// Copyright (c) 2014 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.commons.pages.vv;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;

import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleStorage;
import cpcc.vvrte.services.db.VvRteRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Vehicle storage page.
 */
public class VvStorage
{
    private static final String MSG_STORAGE_CONTENT_TITLE = "storage.content.title";
    private static final String MSG_STORAGE_CONTENT_ALT = "storage.content.alt";

    @Inject
    protected Messages messages;

    @Inject
    protected VvRteRepository vvRteRepo;

    @SuppressFBWarnings(value = "URF_UNREAD_FIELD", justification = "The template uses this variable.")
    @Property
    private VirtualVehicle virtualVehicle;

    @SuppressFBWarnings(value = "URF_UNREAD_FIELD", justification = "The template uses this variable.")
    @Property
    private List<VirtualVehicleStorage> storageList;

    @Property
    private VirtualVehicleStorage storageItem;

    private Integer vvId;

    void onActivate(Integer id)
    {
        this.vvId = id;
        virtualVehicle = vvRteRepo.findVirtualVehicleById(id);
        storageList = vvRteRepo.findStorageItemsByVirtualVehicle(id);
    }

    Integer onPassivate()
    {
        return vvId;
    }

    /**
     * @return the content alt text.
     */
    public String getContentAlt()
    {
        return messages.format(MSG_STORAGE_CONTENT_ALT, storageItem.getId(), storageItem.getName());
    }

    /**
     * @return the content title text.
     */
    public String getContentTitle()
    {
        return messages.format(MSG_STORAGE_CONTENT_TITLE, storageItem.getId(), storageItem.getName());
    }

}
