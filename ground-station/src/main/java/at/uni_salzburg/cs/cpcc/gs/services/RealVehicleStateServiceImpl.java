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
package at.uni_salzburg.cs.cpcc.gs.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationRequest.Connector;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationResponse;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationResponse.Status;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationService;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.services.TimerService;

/**
 * RealVehicleStateServiceImpl
 */
public class RealVehicleStateServiceImpl extends TimerTask implements RealVehicleStateService
{
    private QueryManager qm;
    private CommunicationService com;
    private List<RvStatusCallable> rvStatusCallablesList = new ArrayList<RvStatusCallable>();
    private int numberOfPoolThreads;

    /**
     * @param qm the query manager.
     * @param com the communication service.
     * @param timer the timer service.
     */
    public RealVehicleStateServiceImpl(QueryManager qm, CommunicationService com, TimerService timer)
    {
        this.qm = qm;
        this.com = com;

        // TODO move parameters to configuration.
        numberOfPoolThreads = 10;
        reload();
        timer.periodicSchedule(this, 1000, 1000);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reload()
    {
        List<RealVehicle> realVehicles = qm.findAllRealVehicles();
        List<RvStatusCallable> newCallables = new ArrayList<RvStatusCallable>();

        for (RealVehicle rv : realVehicles)
        {
            RealVehicleStatus status = new RealVehicleStatus(rv);
            RvStatusCallable newCallable = new RvStatusCallable(status, com);
            newCallables.add(newCallable);
        }

        rvStatusCallablesList = newCallables;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        ExecutorService executor = Executors.newFixedThreadPool(numberOfPoolThreads);

        for (RvStatusCallable c : rvStatusCallablesList)
        {
            if (!c.getStatus().isStatusUpdateRunning())
            {
                FutureTask<CommunicationResponse> futureTask = new FutureTask<CommunicationResponse>(c);
                executor.execute(futureTask);
            }
        }
    }

    /**
     * RvStatusCallable
     */
    public static class RvStatusCallable implements Callable<CommunicationResponse>
    {
        private RealVehicleStatus status;
        private CommunicationService com;

        /**
         * @param status the real vehicle status.
         * @param com the communication service.
         */
        public RvStatusCallable(RealVehicleStatus status, CommunicationService com)
        {
            this.status = status;
            this.com = com;
        }

        /**
         * @return the real vehicle status.
         */
        public RealVehicleStatus getStatus()
        {
            return status;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public CommunicationResponse call() throws Exception
        {
            status.setStatusUpdateRunning(true);
            try
            {
                CommunicationResponse resp =
                    com.transfer(status.getRealVehicle(), Connector.REAL_VEHICLE_STATUS, new byte[0]);

                status.setConnected(true);
                status.setLastUpdate(new Date());
                
                if (resp.getStatus() == Status.OK)
                {
                    status.setStatus(resp.getContent());
                }
                else
                {
                    status.setStatus(new byte[0]);
                }

                return resp;
            }
            catch(Exception e)
            {
                status.setStatus(new byte[0]);
                throw e;
            }
            finally
            {
                
                status.setConnected(false);
                status.setStatusUpdateRunning(false);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RealVehicleStatus> getRealVehicleStatus()
    {
        List<RealVehicleStatus> status = new ArrayList<RealVehicleStatus>();

        for (RvStatusCallable c : rvStatusCallablesList)
        {
            status.add(c.getStatus());
        }

        return status;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<RealVehicle> getRealVehicles()
    {
        List<RealVehicle> realVehicles = new ArrayList<RealVehicle>();
        
        for (RvStatusCallable c : rvStatusCallablesList)
        {
            realVehicles.add(c.getStatus().getRealVehicle());
        }
        
        return realVehicles;
    }

}