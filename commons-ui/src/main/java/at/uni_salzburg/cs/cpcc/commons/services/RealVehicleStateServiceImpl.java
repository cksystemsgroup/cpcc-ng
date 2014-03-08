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
package at.uni_salzburg.cs.cpcc.commons.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.TreeMap;
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
    private boolean configurationChange = true;
    private Map<Integer, RvStatusCallable> rvStatusCallablesMap = new HashMap<Integer, RvStatusCallable>();
    private int numberOfPoolThreads;
    private Set<RealVehicleStateListener> stateListenerSet =
        Collections.synchronizedSet(new HashSet<RealVehicleStateListener>());

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
        timer.periodicSchedule(this, 1000, 100000);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reload()
    {
        List<RealVehicle> realVehicles = qm.findAllRealVehicles();
        Map<Integer, RvStatusCallable> newCallables = new HashMap<Integer, RvStatusCallable>();

        for (RealVehicle rv : realVehicles)
        {
            RealVehicleState status = new RealVehicleState(rv);

            RvStatusCallable oldRvCallable = rvStatusCallablesMap.get(rv.getId());
            status.setConnected(oldRvCallable != null ? oldRvCallable.getStatus().isConnected() : false);

            RvStatusCallable newCallable = new RvStatusCallable(status, com, stateListenerSet);
            newCallables.put(rv.getId(), newCallable);
        }

        rvStatusCallablesMap = newCallables;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyConfigurationChange()
    {
        configurationChange = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        if (configurationChange)
        {
            configurationChange = false;
            reload();
        }

        ExecutorService executor = Executors.newFixedThreadPool(numberOfPoolThreads);

        for (RvStatusCallable c : rvStatusCallablesMap.values())
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
        private RealVehicleState status;
        private CommunicationService com;
        private Set<RealVehicleStateListener> listeners;

        /**
         * @param status the real vehicle status.
         * @param com the communication service.
         * @param listeners the state listeners.
         */
        public RvStatusCallable(RealVehicleState status, CommunicationService com,
            Set<RealVehicleStateListener> listeners)
        {
            this.status = status;
            this.com = com;
            this.listeners = listeners;
        }

        /**
         * @return the real vehicle status.
         */
        public RealVehicleState getStatus()
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

                if (status.getRealVehicle().getName().equals("RV01"))
                {
                    System.out.println("buggerit! RV01");
                }
                status.setLastUpdate(new Date());
                status.setStatus(resp.getStatus() == Status.OK ? resp.getContent() : new byte[0]);
                if (!status.isConnected() && status.getStatus().length > 0)
                {
                    status.setConnected(true);
                    notifyListeners();
                }
                return resp;
            }
            catch (Exception e)
            {
                status.setStatus(new byte[0]);
                if (status.isConnected())
                {
                    status.setConnected(false);
                    notifyListeners();
                }
                throw e;
            }
            finally
            {
                status.setStatusUpdateRunning(false);
            }
        }

        /**
         * Notify listeners on connection state change.
         */
        private void notifyListeners()
        {
            for (RealVehicleStateListener l : listeners)
            {
                l.notify(status);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<RealVehicleState> getRealVehicleStatus()
    {
        Map<String, RealVehicleState> statusMap = new TreeMap<String, RealVehicleState>();

        for (RvStatusCallable c : rvStatusCallablesMap.values())
        {
            statusMap.put(c.getStatus().getRealVehicle().getName(), c.getStatus());
        }

        return statusMap.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<RealVehicle> getRealVehicles()
    {
        Map<String, RealVehicle> realVehicleMap = new TreeMap<String, RealVehicle>();

        for (RvStatusCallable c : rvStatusCallablesMap.values())
        {
            RealVehicle rv = c.getStatus().getRealVehicle();
            realVehicleMap.put(rv.getName(), rv);
        }

        return realVehicleMap.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRealVehicleStateListener(RealVehicleStateListener listener)
    {
        stateListenerSet.add(listener);
    }

}