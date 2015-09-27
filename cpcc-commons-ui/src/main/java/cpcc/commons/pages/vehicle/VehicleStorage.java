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

package cpcc.commons.pages.vehicle;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Property;

import cpcc.commons.services.StorageContentTagService;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleStorage;
import cpcc.vvrte.services.VvRteRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * VehicleStorage
 */
public class VehicleStorage
{
    @Inject
    protected VvRteRepository vvRteRepo;

    @Inject
    protected StorageContentTagService storageTagService;

    @SuppressFBWarnings(value = "URF_UNREAD_FIELD", justification = "VehicleStorage.tml uses this variable.")
    @Property
    private VirtualVehicle virtualVehicle;

    @SuppressFBWarnings(value = "URF_UNREAD_FIELD", justification = "VehicleStorage.tml uses this variable.")
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
     * @return the content link
     */
    public String getContentLink()
    {
        return storageTagService.getStorageContentTag(storageItem);
    }
}
