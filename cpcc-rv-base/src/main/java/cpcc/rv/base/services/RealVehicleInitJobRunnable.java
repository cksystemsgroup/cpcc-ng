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

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.entities.RealVehicle;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.jobs.JobRunnable;

/**
 * Real vehicle init job runnable..
 */
public class RealVehicleInitJobRunnable implements JobRunnable
{
    private static final Logger LOG = LoggerFactory.getLogger(RealVehicleInitJobRunnable.class);

    private ServiceResources serviceResources;
    private boolean succeeded = false;

    /**
     * @param serviceResources the service resources.
     */
    public RealVehicleInitJobRunnable(ServiceResources serviceResources)
    {
        this.serviceResources = serviceResources;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        RealVehicleRepository rvRepo = serviceResources.getService(RealVehicleRepository.class);
        HibernateSessionManager sessionManager = serviceResources.getService(HibernateSessionManager.class);

        LOG.info("Cleaning up old real vehicle states");
        rvRepo.cleanupOldVehicleStates();
        sessionManager.commit();

        RealVehicle myself = rvRepo.findOwnRealVehicle();
        if (myself != null)
        {
            LOG.info("Found own vehicle name: {}, id={}. Initialization already complete.",
                myself.getName(), myself.getId());
            succeeded = true;
            return;
        }

        LOG.error("Own vehicle name not configured!");
        SetupService setup = serviceResources.getService(SetupService.class);
        setup.setupRealVehicle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean executionSucceeded()
    {
        return succeeded;
    }
}
