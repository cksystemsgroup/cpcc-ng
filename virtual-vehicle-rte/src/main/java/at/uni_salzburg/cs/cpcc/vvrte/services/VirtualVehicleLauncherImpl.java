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

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleState;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptService;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptWorker;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptWorker.WorkerState;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptWorkerStateListener;

/**
 * VehicleLauncherImpl
 */
public class VirtualVehicleLauncherImpl implements VirtualVehicleLauncher, JavascriptWorkerStateListener,
    VirtualVehicleListener
{
    private static final Logger LOG = LoggerFactory.getLogger(VirtualVehicleLauncherImpl.class);

    // private QueryManager qm;
    private Session session;
    private JavascriptService jss;
    private VirtualVehicleMigrator migrator;
    private Map<JavascriptWorker, VirtualVehicle> vehicleMap = new HashMap<JavascriptWorker, VirtualVehicle>();

    /**
     * @param qm the query manager.
     * @param jss the JavaScript service.
     * @param migrator the migration service.
     */
    public VirtualVehicleLauncherImpl(Session session, JavascriptService jss, VirtualVehicleMigrator migrator)
    {
        this.session = session;
        this.jss = jss;
        this.migrator = migrator;

        migrator.addListener(this);
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

        vehicle.setStartTime(new Date());
        session.saveOrUpdate(vehicle);

        startVehicle(vehicle, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resume(VirtualVehicle vehicle) throws VirtualVehicleLaunchException, IOException
    {
        if (vehicle == null)
        {
            throw new VirtualVehicleLaunchException("Invalid virtual vehicle 'null'");
        }

        VirtualVehicleState state = vehicle.getState();

        if (state != VirtualVehicleState.MIGRATION_AWAITED
            && state != VirtualVehicleState.MIGRATION_INTERRUPTED
            && state != VirtualVehicleState.MIGRATION_COMPLETED)
        {
            throw new VirtualVehicleLaunchException("Expected vehicle in state "
                + VirtualVehicleState.MIGRATION_AWAITED + ", "
                + VirtualVehicleState.MIGRATION_INTERRUPTED + ", or "
                + VirtualVehicleState.MIGRATION_COMPLETED + ", but got " + state);
        }

        startVehicle(vehicle, true);
    }

    /**
     * @param vehicle the virtual vehicle to start
     * @param useContinuation use the continuation data if true
     * @throws VirtualVehicleLaunchException thrown in case of errors.
     * @throws IOException thrown in case of errors.
     */
    private void startVehicle(VirtualVehicle vehicle, boolean useContinuation)
        throws VirtualVehicleLaunchException, IOException
    {
        JavascriptWorker worker = useContinuation
            ? jss.createWorker(vehicle.getContinuation())
            : jss.createWorker(vehicle.getCode(), vehicle.getApiVersion());

        worker.addStateListener(this);
        worker.setName("VV-" + vehicle.getId() + "-" + vehicle.getName());
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
            VirtualVehicleMappingDecision decision = null;

            Session newSession = session.getSessionFactory().openSession();
            try
            {
                Transaction t = newSession.beginTransaction();
                vehicle.setState(vehicleState);

                switch (state)
                {
                    case INTERRUPTED:
                        vehicle.setContinuation(worker.getSnapshot());
                        decision = (VirtualVehicleMappingDecision) worker.getApplicationState();

                        if (decision.isMigration() && decision.getRealVehicles().size() > 0)
                        {
                            // TODO select the best suitable RV instead of taking just the first.
                            RealVehicle migrationDestination = decision.getRealVehicles().get(0);
                            vehicle.setMigrationDestination(migrationDestination);
                            vehicle.setState(VirtualVehicleState.MIGRATION_AWAITED);
                        }
                        else
                        {
                            decision = null;
                            vehicle.setState(VirtualVehicleState.MIGRATION_INTERRUPTED);
                            vehicle.setMigrationDestination(null);
                        }
                        break;
                    case FINISHED:
                        vehicle.setEndTime(new Date());
                        break;
                    case DEFECTIVE:
                        LOG.error("Virtual Vehicle crashed! Message is: " + worker.getResult());
                        break;
                    default:
                        break;
                }

                newSession.saveOrUpdate(vehicle);
                t.commit();
            }
            finally
            {
                newSession.close();
            }

            if (decision != null)
            {
                LOG.info("initiateMigration of VV " + vehicle.getName() + "(" + vehicle.getUuid() + ")");
                migrator.initiateMigration(vehicle);
            }
        }
    }

    @Override
    public void notify(VirtualVehicle vehicle)
    {
        if (vehicle.getState() == VirtualVehicleState.MIGRATION_COMPLETED)
        {
            try
            {
                startVehicle(vehicle, true);
            }
            catch (VirtualVehicleLaunchException | IOException e)
            {
                LOG.error("Can not start virtual vehicle " + vehicle.getName() + " (" + vehicle.getUuid() + ")", e);
            }
        }
    }
}
