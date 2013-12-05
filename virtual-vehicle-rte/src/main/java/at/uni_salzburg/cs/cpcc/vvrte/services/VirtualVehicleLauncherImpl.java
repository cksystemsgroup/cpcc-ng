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

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.persistence.services.QueryManager;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleState;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptService;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptWorker;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptWorker.WorkerState;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptWorkerStateListener;

/**
 * VehicleLauncherImpl
 */
public class VirtualVehicleLauncherImpl implements VirtualVehicleLauncher, JavascriptWorkerStateListener
{
    private static final Logger LOG = LoggerFactory.getLogger(VirtualVehicleLauncherImpl.class);
    
    private QueryManager qm;
    private JavascriptService jss;

    private Map<Integer, JavascriptWorker> workerMap = new HashMap<Integer, JavascriptWorker>();
    private Map<JavascriptWorker, VirtualVehicle> vehicleMap = new HashMap<JavascriptWorker, VirtualVehicle>();
    
    /**
     * @param qm the query manager.
     * @param jss the JavaScript service.
     */
    public VirtualVehicleLauncherImpl(QueryManager qm, JavascriptService jss)
    {
        this.qm = qm;
        this.jss = jss;
        
        jss.addAllowedClassRegex("\\$BuiltInFunctions_.*");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(VirtualVehicle vehicle) throws VirtualVehicleLaunchException, IOException
    {
        if (vehicle == null)
        {
            throw new VirtualVehicleLaunchException("Invalid virtual vehicle 'null'");
        }

        VirtualVehicleState state = vehicle.getState();

        if (state != VirtualVehicleState.INIT)
        {
            throw new VirtualVehicleLaunchException("Expected vehicle in state " + VirtualVehicleState.INIT
                + ", but got " + state);
        }

        if (!state.canTraverseTo(VirtualVehicleState.RUNNING))
        {
            throw new VirtualVehicleLaunchException("Can not switch vehicle to state " + VirtualVehicleState.RUNNING);
        }

        vehicle.setStartTime(new Date());
        qm.saveOrUpdate(vehicle);

        JavascriptWorker worker = jss.createWorker(vehicle.getCode(), vehicle.getApiVersion());
        worker.addStateListener(this);
        worker.setName("VV-"+vehicle.getName());
        workerMap.put(vehicle.getId(), worker);
        vehicleMap.put(worker, vehicle);
        worker.start();
    }
    
    @SuppressWarnings("serial")
    private static final Map<WorkerState, VirtualVehicleState> STATE_MAP =
        new HashMap<WorkerState, VirtualVehicleState>()
        {
            {
                put(WorkerState.DEFECTIVE, VirtualVehicleState.DEFECTIVE);
                put(WorkerState.FINISHED, VirtualVehicleState.FINISHED);
                put(WorkerState.INTERRUPTED, VirtualVehicleState.INTERRUPTED);
                put(WorkerState.RUNNING, VirtualVehicleState.RUNNING);
            }
        };

    /**
     * {@inheritDoc}
     */
    @Override
    public void notify(JavascriptWorker worker, WorkerState state)
    {
        LOG.info("notify(): " + worker.getName() + ", state=" + state);
        VirtualVehicle vehicle = vehicleMap.get(worker);
        VirtualVehicleState vehicleState = STATE_MAP.get(state);
        if (vehicle != null && vehicleState != null)
        {
            Transaction t = qm.getSession().beginTransaction();
            vehicle.setState(vehicleState);
            switch (state)
            {
                case INTERRUPTED:
                    vehicle.setContinuation(worker.getSnapshot());
                    break;
                case FINISHED:
                    vehicle.setEndTime(new Date());
                    break;
                case DEFECTIVE:
                    LOG.error("buggerit: " + worker.getResult());
                    break;
                default:
                    break;
            }
            qm.getSession().saveOrUpdate(vehicle);
            t.commit();
            qm.getSession().flush();
        }

        Object applicationState = worker.getApplicationState();
        if (applicationState instanceof VirtualVehicleMappingDecision)
        {
            initiateMigration(vehicle, (VirtualVehicleMappingDecision) applicationState);
        }
    }

    /**
     * @param vehicle the virtual vehicle.
     * @param applicationState the application state.
     */
    private void initiateMigration(VirtualVehicle vehicle, VirtualVehicleMappingDecision applicationState)
    {
        LOG.error("initiateMigration not implemented!");
    }

}
