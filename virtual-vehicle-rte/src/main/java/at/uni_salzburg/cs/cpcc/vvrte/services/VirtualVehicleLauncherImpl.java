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

package at.uni_salzburg.cs.cpcc.vvrte.services;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.slf4j.Logger;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleState;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptService;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptWorker;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptWorkerStateListener;

/**
 * VehicleLauncherImpl
 */
public class VirtualVehicleLauncherImpl implements VirtualVehicleLauncher, JavascriptWorkerStateListener
{
    private Logger logger;
    private HibernateSessionManager sessionManager;
    private JavascriptService jss;
    private VirtualVehicleMigrator migrator;
    private VvRteRepository vvRteRepository;
    private Map<JavascriptWorker, Integer> vehicleMap = new HashMap<JavascriptWorker, Integer>();

    /**
     * @param logger the application logger.
     * @param sessionManager the Hibernate session manager.
     * @param jss the JavaScript service.
     * @param migrator the migration service.
     * @param vvRteRepository the virtual vehicle RTE repository.
     */
    public VirtualVehicleLauncherImpl(Logger logger, HibernateSessionManager sessionManager, JavascriptService jss
        , VirtualVehicleMigrator migrator, VvRteRepository vvRteRepository)
    {
        this.logger = logger;
        this.sessionManager = sessionManager;
        this.jss = jss;
        this.migrator = migrator;
        this.vvRteRepository = vvRteRepository;

        jss.addAllowedClassRegex("\\$BuiltInFunctions_.*");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(int vehicleId) throws VirtualVehicleLaunchException, IOException
    {
        VirtualVehicle vehicle = vvRteRepository.findVirtualVehicleById(vehicleId);

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
        sessionManager.getSession().saveOrUpdate(vehicle);
        sessionManager.commit();

        startVehicle(vehicle, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop(int vehicleId) throws VirtualVehicleLaunchException, IOException
    {
        VirtualVehicle vehicle = vvRteRepository.findVirtualVehicleById(vehicleId);

        if (vehicle == null)
        {
            throw new VirtualVehicleLaunchException("Invalid virtual vehicle 'null'");
        }

        VirtualVehicleState state = vehicle.getState();

        if (!VirtualVehicleState.VV_STATES_FOR_STOP.contains(state))
        {
            throw new VirtualVehicleLaunchException("Got vehicle in state " + state
                + ", but expected " + VirtualVehicleState.VV_STATES_FOR_STOP);
        }

        vehicle.setEndTime(new Date());
        vehicle.setState(VirtualVehicleState.INIT);
        vehicle.setStateInfo("Vehicle has been stopped manually.");
        vehicle.setContinuation(null);
        sessionManager.getSession().saveOrUpdate(vehicle);
        sessionManager.commit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resume(int vehicleId) throws VirtualVehicleLaunchException, IOException
    {
        VirtualVehicle vehicle = vvRteRepository.findVirtualVehicleById(vehicleId);

        if (vehicle == null)
        {
            throw new VirtualVehicleLaunchException("Invalid virtual vehicle 'null'");
        }

        VirtualVehicleState state = vehicle.getState();

        if (!VirtualVehicleState.VV_STATES_FOR_RESTART.contains(state))
        {
            throw new VirtualVehicleLaunchException("Got vehicle in state " + state
                + ", but expected " + VirtualVehicleState.VV_STATES_FOR_RESTART);
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
        JavascriptWorker worker = useContinuation && vehicle.getContinuation() != null
            ? jss.createWorker(vehicle.getContinuation())
            : jss.createWorker(vehicle.getCode(), vehicle.getApiVersion());

        worker.addStateListener(this);
        worker.setName("VV-" + vehicle.getId() + "-" + vehicle.getName());
        vehicleMap.put(worker, vehicle.getId());
        worker.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stateChange(int vehicleId, VirtualVehicleState newState)
    {
        logger.info("New vehicle state for vehicle " + vehicleId + " : " + newState);
        if (newState == VirtualVehicleState.MIGRATION_COMPLETED)
        {
            VirtualVehicle vehicle = vvRteRepository.findVirtualVehicleById(vehicleId);

            try
            {
                startVehicle(vehicle, true);
            }
            catch (VirtualVehicleLaunchException | IOException e)
            {
                logger.error("Can not start virtual vehicle " + vehicle.getName() + " (" + vehicle.getUuid() + ")", e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notify(JavascriptWorker worker, VirtualVehicleState vehicleState)
    {
        logger.info("notify(): " + worker.getName() + ", state=" + vehicleState);
        VirtualVehicle vehicle = vvRteRepository.findVirtualVehicleById(vehicleMap.get(worker));

        if (vehicle == null || vehicleState == null)
        {
            return;
        }

        VirtualVehicleMappingDecision decision = null;

        vehicle.setState(vehicleState);
        vehicle.setStateInfo(worker.getResult());

        switch (vehicleState)
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
                    vehicle.setState(VirtualVehicleState.MIGRATION_INTERRUPTED);
                    vehicle.setMigrationDestination(null);
                    vehicle.setStateInfo(convertToString(decision));
                    decision = null;
                }
                break;
            case FINISHED:
                vehicle.setEndTime(new Date());
                break;
            case DEFECTIVE:
                logger.error("Virtual Vehicle crashed! Message is: " + worker.getResult());
                break;
            default:
                break;
        }

        sessionManager.getSession().saveOrUpdate(vehicle);
        sessionManager.commit();

        if (decision != null)
        {
            logger.info("initiateMigration of VV " + vehicle.getName() + "(" + vehicle.getUuid() + ")");
            migrator.initiateMigration(vehicle);
        }

    }

    /**
     * @param decision the migration decision.
     * @return the decision as a string.
     */
    private String convertToString(VirtualVehicleMappingDecision decision)
    {
        StringBuilder bldr = new StringBuilder("No suitable real vehicle found to migrate to!\n\n");

        bldr.append("Migration: ").append(decision.isMigration()).append("\n")
            .append("Task Position: {lat: ").append(decision.getTask().getPosition().getLatitude())
            .append(", lng: ").append(decision.getTask().getPosition().getLongitude())
            .append(", alt: ").append(decision.getTask().getPosition().getAltitude())
            .append("}\n");

        return bldr.toString();
    }

}
