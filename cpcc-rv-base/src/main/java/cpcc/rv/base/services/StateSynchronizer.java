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

package cpcc.rv.base.services;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import cpcc.core.services.jobs.JobCreationException;

/**
 * ConfigurationSynchronizer interface.
 */
public interface StateSynchronizer
{
    /**
     * Synchronize the configuration to all real vehicles.
     */
    @CommitAfter
    void pushConfiguration();

    /**
     * @param data the data to be imported.
     * @throws JobCreationException in case of errors.
     */
    @CommitAfter
    void importConfiguration(byte[] data) throws JobCreationException;

    /**
     * Update the state of the active real vehicles.
     */
    @CommitAfter
    void realVehicleStatusUpdate();
}
