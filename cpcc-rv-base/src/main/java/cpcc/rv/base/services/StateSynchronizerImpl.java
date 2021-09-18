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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.entities.Parameter;
import cpcc.core.entities.RealVehicle;
import cpcc.core.services.QueryManager;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.jobs.JobCreationException;
import cpcc.core.services.jobs.JobService;

/**
 * This service synchronizes real vehicle and sensor configuration data to other real vehicles.
 */
public class StateSynchronizerImpl implements StateSynchronizer
{
    private static final Logger LOG = LoggerFactory.getLogger(StateSynchronizerImpl.class);

    private QueryManager qm;
    private JobService jobService;
    private RealVehicleRepository realVehicleRepository;

    /**
     * @param qm the query manager.
     * @param jobService the job service.
     * @param realVehicleRepository the real vehicle repository.
     */
    public StateSynchronizerImpl(QueryManager qm, JobService jobService, RealVehicleRepository realVehicleRepository)
    {
        this.qm = qm;
        this.jobService = jobService;
        this.realVehicleRepository = realVehicleRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pushConfiguration()
    {
        queueSateSyncJobs(RealVehicleBaseConstants.JOB_MODE_CONFIG);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void importConfiguration(byte[] data) throws JobCreationException
    {
        jobService.addJobIfNotExists(RealVehicleBaseConstants.JOB_QUEUE_NAME,
            "mode=" + RealVehicleBaseConstants.JOB_MODE_IMPORT, data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void realVehicleStatusUpdate()
    {
        queueSateSyncJobs(RealVehicleBaseConstants.JOB_MODE_STATUS);
    }

    /**
     * @param mode the synchronization mode.
     */
    private void queueSateSyncJobs(String mode)
    {
        Parameter param = qm.findParameterByName(Parameter.REAL_VEHICLE_NAME);
        if (param == null || StringUtils.isEmpty(param.getValue()))
        {
            LOG.error("Hosting real vehicle name is not configured. Config sync aborted!");
            return;
        }

        for (RealVehicle rv : realVehicleRepository.findAllActiveRealVehicles())
        {
            try
            {
                jobService.addJob(RealVehicleBaseConstants.JOB_QUEUE_NAME,
                    String.format("mode=%s,rv=%d", mode, rv.getId()));
            }
            catch (JobCreationException e)
            {
                LOG.debug("Can not create config sync job for real vehicle {} ({}), mode={}",
                    rv.getName(), rv.getId(), mode, e);
            }
        }
    }
}
