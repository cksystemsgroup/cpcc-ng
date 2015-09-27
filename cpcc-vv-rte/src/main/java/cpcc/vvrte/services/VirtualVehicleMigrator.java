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

package cpcc.vvrte.services;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveException;

import cpcc.vvrte.entities.VirtualVehicle;

/**
 * VirtualVehicleMigrator
 */
public interface VirtualVehicleMigrator
{
    /**
     * @param vehicle the virtual vehicle to be migrated.
     */
    void initiateMigration(VirtualVehicle vehicle);

    /**
     * @param virtualVehicle the virtual vehicle to be migrated.
     * @return the virtual vehicle chunk as byte array.
     * @throws IOException thrown in case of errors.
     * @throws ArchiveException thrown in case of errors.
     */
    byte[] findFirstChunk(VirtualVehicle virtualVehicle) throws IOException, ArchiveException;

    /**
     * @param virtualVehicle the virtual vehicle to be migrated.
     * @param lastStorageName the name of the last successfully migrated storage entry.
     * @param chunkNumber the chunk number.
     * @return the virtual vehicle chunk as byte array.
     * @throws IOException thrown in case of errors.
     * @throws ArchiveException thrown in case of errors.
     */
    byte[] findChunk(VirtualVehicle virtualVehicle, String lastStorageName, int chunkNumber)
        throws IOException, ArchiveException;

    /**
     * @param inStream the input stream containing the virtual vehicle chunk to be stored in the database.
     * @return the last stored chunk name.
     * @throws ArchiveException thrown in case of errors.
     * @throws IOException thrown in case of errors.
     */
    String storeChunk(InputStream inStream) throws ArchiveException, IOException;

//    /**
//     * @param listener the virtual vehicle listener.
//     */
//    void addListener(VirtualVehicleListener listener);
}
