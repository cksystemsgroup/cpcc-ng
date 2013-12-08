/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.vvrte.services;

import java.util.Iterator;

import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;

/**
 * VirtualVehicleChunkIterable
 */
public class VirtualVehicleChunkIterable implements Iterable<byte[]>
{
    private VirtualVehicle virtualVehicle;
    private VvRteRepository vvRepository;

    /**
     * @param vvRepository the virtual vehicle repository.
     * @param virtualVehicle the virtual vehicle to be serialized in byte array chunks.
     */
    public VirtualVehicleChunkIterable(VvRteRepository vvRepository, VirtualVehicle virtualVehicle)
    {
        this.vvRepository = vvRepository;
        this.virtualVehicle = virtualVehicle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<byte[]> iterator()
    {
        return new VirtualVehicleChunkIterator(vvRepository, virtualVehicle);
    }
}
