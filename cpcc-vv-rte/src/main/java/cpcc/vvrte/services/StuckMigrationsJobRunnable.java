// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.services.PerthreadManager;

import cpcc.core.services.jobs.JobRunnable;

/**
 * MigrationSendAckJobRunnable implementation.
 */
public class StuckMigrationsJobRunnable implements JobRunnable
{
    private ServiceResources serviceResources;

    /**
     * @param serviceResources the service resources.
     */
    public StuckMigrationsJobRunnable(ServiceResources serviceResources)
    {
        this.serviceResources = serviceResources;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        PerthreadManager perthreadManager = serviceResources.getService(PerthreadManager.class);
        HibernateSessionManager sessionManager = serviceResources.getService(HibernateSessionManager.class);
        VirtualVehicleLauncher launcher = serviceResources.getService(VirtualVehicleLauncher.class);

        String name = Thread.currentThread().getName();
        Thread.currentThread().setName("STUCK-MIGS");

        try
        {
            launcher.handleStuckMigrations();
            sessionManager.commit();
        }
        finally
        {
            sessionManager.abort();
            perthreadManager.cleanup();
            Thread.currentThread().setName(name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean executionSucceeded()
    {
        return true;
    }
}
