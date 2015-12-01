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

package cpcc.vvrte.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.Messages;
import org.slf4j.Logger;

import cpcc.core.entities.RealVehicle;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.jobs.TimeService;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.services.js.JavascriptService;
import cpcc.vvrte.services.js.JavascriptWorker;
import cpcc.vvrte.services.js.JavascriptWorkerStateListener;
import cpcc.vvrte.task.Task;

/**
 * VehicleLauncherImpl
 */
public class VirtualVehicleLauncherImpl implements VirtualVehicleLauncher, JavascriptWorkerStateListener
{
    private static final String MSG_MAPPING_DECISION = "virtual.vehicle.launcher.mapping.decision";

    private Logger logger;
    private HibernateSessionManager sessionManager;
    private JavascriptService jss;
    private VirtualVehicleMigrator migrator;
    private VvRteRepository vvRteRepository;
    private RealVehicleRepository rvRepository;
    private TimeService timeService;
    private Map<JavascriptWorker, Integer> vehicleMap = new HashMap<JavascriptWorker, Integer>();
    private Messages messages;

    /**
     * @param logger the application logger.
     * @param sessionManager the Hibernate session manager.
     * @param jss the JavaScript service.
     * @param migrator the migration service.
     * @param vvRteRepository the virtual vehicle RTE repository.
     * @param rvRepository the real vehicle repository.
     * @param timeService the time service.
     * @param messages the application message catalog.
     */
    public VirtualVehicleLauncherImpl(Logger logger, HibernateSessionManager sessionManager, JavascriptService jss
        , VirtualVehicleMigrator migrator, VvRteRepository vvRteRepository, RealVehicleRepository rvRepository
        , TimeService timeService, Messages messages)
    {
        this.logger = logger;
        this.sessionManager = sessionManager;
        this.jss = jss;
        this.migrator = migrator;
        this.vvRteRepository = vvRteRepository;
        this.rvRepository = rvRepository;
        this.timeService = timeService;
        this.messages = messages;

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

        vehicle.setStartTime(timeService.newDate());
        vehicle.setContinuation(null);
        vehicle.setPreMigrationState(null);
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

        vehicle.setEndTime(timeService.newDate());
        vehicle.setState(VirtualVehicleState.INIT);
        vehicle.setStateInfo("Vehicle has been stopped manually.");
        vehicle.setContinuation(null);
        vehicle.setPreMigrationState(null);
        vehicle.setMigrationDestination(null);
        vehicle.setMigrationStartTime(null);
        vehicle.setStartTime(null);
        vehicle.setEndTime(null);

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
            throw new VirtualVehicleLaunchException("Invalid virtual vehicle 'null' for id " + vehicleId);
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
            : jss.createWorker(vehicle.getCode().replace("\\n", "\n"), vehicle.getApiVersion());

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
                decision = handleInterruptedVehicle(worker, vehicle);
                break;
            case FINISHED:
                decision = handleFinishedVehicle(vehicle);
                break;
            case DEFECTIVE:
                decision = handleDefectiveVehicle(worker, vehicle);
                break;
            default:
                break;
        }

        sessionManager.getSession().saveOrUpdate(vehicle);
        sessionManager.commit();

        if (decision != null)
        {
            logger.info("initiate Migration of VV " + vehicle.getName() + "(" + vehicle.getUuid() + ")");
            migrator.initiateMigration(vehicle);
        }

    }

    /**
     * @param worker the worker instance.
     * @param vehicle the defective virtual vehicle.
     * @return the mapping decision.
     */
    private VirtualVehicleMappingDecision handleDefectiveVehicle(JavascriptWorker worker, VirtualVehicle vehicle)
    {
        logger.error("Virtual Vehicle crashed! Message is: " + worker.getResult());
        vehicle.setStateInfo(worker.getResult());
        return handleFinishedVehicle(vehicle);
    }

    /**
     * @param vehicle the defective virtual vehicle.
     * @return the mapping decision.
     */
    private VirtualVehicleMappingDecision handleFinishedVehicle(VirtualVehicle vehicle)
    {
        vehicle.setEndTime(timeService.newDate());

        List<RealVehicle> groundStations = rvRepository.findAllConnectedGroundStations();

        VirtualVehicleMappingDecision decision = new VirtualVehicleMappingDecision();
        if (!groundStations.isEmpty())
        {
            decision.setMigration(true);
            decision.setRealVehicles(groundStations);
            vehicle.setMigrationDestination(groundStations.get(0));
        }

        return decision;
    }

    /**
     * @param worker the worker instance.
     * @param vehicle the interrupted virtual vehicle.
     * @return the mapping decision.
     */
    private VirtualVehicleMappingDecision handleInterruptedVehicle(JavascriptWorker worker, VirtualVehicle vehicle)
    {
        vehicle.setContinuation(worker.getSnapshot());
        VirtualVehicleMappingDecision decision = (VirtualVehicleMappingDecision) worker.getApplicationState();

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
            vehicle.setStateInfo(convertToStateInfoString(decision));
            decision = null;
        }

        return decision;
    }

    /**
     * @param decision the migration decision.
     * @return the decision as a string.
     */
    private String convertToStateInfoString(VirtualVehicleMappingDecision decision)
    {
        Task task = decision.getTask();

        return messages.format(MSG_MAPPING_DECISION, decision.isMigration()
            , task.getLatitude(), task.getLongitude(), task.getAltitude());

        //        StringBuilder bldr = new StringBuilder("No suitable real vehicle found to migrate to!\n\n");
        //
        //        bldr.append("Migration: ").append(decision.isMigration()).append("\n")
        //            .append("Task Position: {lat: ").append(decision.getTask().getPosition().getLatitude())
        //            .append(", lng: ").append(decision.getTask().getPosition().getLongitude())
        //            .append(", alt: ").append(decision.getTask().getPosition().getAltitude())
        //            .append("}\n");
        //
        //        return bldr.toString();
    }

}
