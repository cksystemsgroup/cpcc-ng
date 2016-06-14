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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleType;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.jobs.JobService;
import cpcc.core.services.jobs.TimeService;
import cpcc.vvrte.base.VirtualVehicleMappingDecision;
import cpcc.vvrte.base.VvRteConstants;
import cpcc.vvrte.entities.Task;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.services.db.TaskRepository;
import cpcc.vvrte.services.db.VvRteRepository;
import cpcc.vvrte.services.js.ApplicationState;
import cpcc.vvrte.services.js.JavascriptService;
import cpcc.vvrte.services.js.JavascriptWorker;

/**
 * VehicleLauncherImpl
 */
public class VirtualVehicleLauncherImpl implements VirtualVehicleLauncher
{
    private static final String MSG_MAPPING_DECISION = "virtual.vehicle.launcher.mapping.decision";

    private Logger logger;
    private HibernateSessionManager sessionManager;
    private JavascriptService jss;
    private VirtualVehicleMigrator migrator;
    private VvRteRepository vvRteRepository;
    private RealVehicleRepository rvRepository;
    private TimeService timeService;
    private Map<JavascriptWorker, Integer> vehicleMap =
        Collections.synchronizedMap(new HashMap<JavascriptWorker, Integer>());
    private Messages messages;
    private TaskRepository taskRepository;
    private JobService jobService;

    /**
     * @param logger the application logger.
     * @param sessionManager the Hibernate session manager.
     * @param jss the JavaScript service.
     * @param migrator the migration service.
     * @param vvRteRepository the virtual vehicle RTE repository.
     * @param rvRepository the real vehicle repository.
     * @param timeService the time service.
     * @param messages the application message catalog.
     * @param taskRepository the task repository instance.
     * @param jobService the job service.
     */
    public VirtualVehicleLauncherImpl(Logger logger, HibernateSessionManager sessionManager, JavascriptService jss
        , VirtualVehicleMigrator migrator, VvRteRepository vvRteRepository, RealVehicleRepository rvRepository
        , TimeService timeService, Messages messages, TaskRepository taskRepository, JobService jobService)
    {
        this.logger = logger;
        this.sessionManager = sessionManager;
        this.jss = jss;
        this.migrator = migrator;
        this.vvRteRepository = vvRteRepository;
        this.rvRepository = rvRepository;
        this.timeService = timeService;
        this.messages = messages;
        this.taskRepository = taskRepository;
        this.jobService = jobService;

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
        vehicle.setMigrationSource(null);
        vehicle.setMigrationStartTime(null);
        vehicle.setStartTime(null);
        vehicle.setEndTime(null);
        vehicle.setTask(null);
        vehicle.setUpdateTime(timeService.newDate());

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

        if (!VirtualVehicleState.VV_STATES_FOR_RESTART_MIGRATION_FROM_RV.contains(state))
        {
            throw new VirtualVehicleLaunchException("Got vehicle in state " + state
                + ", but expected " + VirtualVehicleState.VV_STATES_FOR_RESTART_MIGRATION_FROM_RV);
        }

        startVehicle(vehicle, true);
    }

    /**
     * @param vehicle the virtual vehicle to start
     * @param useContinuation use the continuation data if true
     */
    private void startVehicle(VirtualVehicle vehicle, boolean useContinuation)
    {
        try
        {
            JavascriptWorker worker = jss.createWorker(vehicle, useContinuation);
            worker.addStateListener(this);
            worker.setName("VV-" + vehicle.getId() + "-" + vehicle.getName());
            vehicleMap.put(worker, vehicle.getId());
            worker.start();
        }
        catch (IOException e)
        {
            logger.error("Can not (re)start virtual vehicle " + vehicle.getUuid()
                + ", id=" + vehicle.getId() + ", name=" + vehicle.getName(), e);

            vehicle.setState(VirtualVehicleState.DEFECTIVE);
            sessionManager.getSession().update(vehicle);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stateChange(int vehicleId, VirtualVehicleState newState)
    {
        logger.debug("New vehicle state for vehicle " + vehicleId + " : " + newState);
        if (newState == VirtualVehicleState.MIGRATION_COMPLETED_RCV)
        {
            VirtualVehicle vehicle = vvRteRepository.findVirtualVehicleById(vehicleId);
            startVehicle(vehicle, true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notify(Task tsk)
    {
        logger.debug("notify(): task=" + tsk.getId() + " " + tsk.getTaskState().name());

        Task task = taskRepository.findTaskById(tsk.getId());
        VirtualVehicle vehicle = task.getVehicle();
        task.setVehicle(null);
        sessionManager.getSession().saveOrUpdate(task);
        sessionManager.commit();

        startVehicle(vehicle, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notify(JavascriptWorker worker, VirtualVehicleState vehicleState)
    {
        logger.debug("notify(): worker=" + worker.getName() + ", state=" + vehicleState);
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
            case TASK_COMPLETION_AWAITED:
                decision = handleInterruptedVehicle(worker, vehicle);
                break;
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

        sessionManager.getSession().update(vehicle);
        sessionManager.commit();

        if (decision != null && vehicle.getMigrationDestination() != null)
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

        RealVehicle me = rvRepository.findOwnRealVehicle();

        if (me.getType() == RealVehicleType.GROUND_STATION)
        {
            return new VirtualVehicleMappingDecision().setMigration(false);
        }

        VirtualVehicleMappingDecision decision = new VirtualVehicleMappingDecision();

        List<RealVehicle> groundStations = rvRepository.findAllGroundStations();
        if (!groundStations.isEmpty())
        {
            decision.setMigration(true);
            decision.setRealVehicles(groundStations);
            vehicle.setMigrationDestination(groundStations.get(0));
            vehicle.setMigrationSource(me);
            vehicle.setUpdateTime(timeService.newDate());
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
        ApplicationState state = (ApplicationState) worker.getApplicationState();

        if (!state.isMappingDecision())
        {
            return null;
        }

        VirtualVehicleMappingDecision decision = state.getDecision();

        if (decision.isMigration() && decision.getRealVehicles().size() > 0)
        {
            // TODO select the best suitable RV instead of taking just the first.
            RealVehicle migrationDestination = decision.getRealVehicles().get(0);
            vehicle.setMigrationDestination(migrationDestination);
            vehicle.setMigrationSource(rvRepository.findOwnRealVehicle());
            vehicle.setState(VirtualVehicleState.MIGRATION_AWAITED_SND);
            vehicle.setMigrationStartTime(timeService.newDate());
            vehicle.setTask(null);
        }
        else
        {
            vehicle.setState(VirtualVehicleState.MIGRATION_INTERRUPTED_SND);
            vehicle.setMigrationDestination(null);
            vehicle.setStateInfo(convertToStateInfoString(decision));
            decision = null;
        }

        vehicle.setUpdateTime(timeService.newDate());

        return decision;
    }

    /**
     * @param decision the migration decision.
     * @return the decision as a string.
     */
    private String convertToStateInfoString(VirtualVehicleMappingDecision decision)
    {
        PolarCoordinate position = decision.getTask().getPosition();

        return messages.format(MSG_MAPPING_DECISION, decision.isMigration()
            , position.getLatitude(), position.getLongitude(), position.getAltitude());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleStuckMigrations()
    {
        logger.debug("Handling stuck VVs.");

        List<RealVehicle> groundStations = rvRepository.findAllGroundStations();

        RealVehicle me = rvRepository.findOwnRealVehicle();
        Set<VirtualVehicleState> requiredStates = me.getType() == RealVehicleType.GROUND_STATION
            ? VirtualVehicleState.VV_STATES_FOR_RESTART_STUCK_MIGRATION_FROM_GS
            : VirtualVehicleState.VV_STATES_FOR_RESTART_STUCK_MIGRATION_FROM_RV;

        for (VirtualVehicle vehicle : vvRteRepository.findAllStuckVehicles(requiredStates))
        {
            logger.info("Found stuck VV: " + vehicle.getName() + "  " + vehicle.getState().name());
            handleOneStuckMigration(groundStations, requiredStates, vehicle);
            sessionManager.commit();
        }
    }

    /**
     * @param groundStations the available ground stations.
     * @param requiredStates the required states for migration.
     * @param vehicle the virtual vehicle.
     */
    private void handleOneStuckMigration(List<RealVehicle> groundStations, Set<VirtualVehicleState> requiredStates,
        VirtualVehicle vehicle)
    {
        if (vehicle.getState() == VirtualVehicleState.MIGRATING_RCV)
        {
            vehicle.setState(VirtualVehicleState.DEFECTIVE);
            vvRteRepository.deleteVirtualVehicleById(vehicle);
            return;
        }

        if (vehicle.getState() == VirtualVehicleState.MIGRATING_SND
            || vehicle.getState() == VirtualVehicleState.MIGRATION_INTERRUPTED_SND
            || vehicle.getState() == VirtualVehicleState.MIGRATION_AWAITED_SND)
        {
            migrator.initiateMigration(vehicle);
            return;
        }

        if (vehicle.getState() == VirtualVehicleState.MIGRATION_COMPLETED_SND)
        {
            String result =
                new JSONObject("uuid", vehicle.getUuid(), "chunk", vehicle.getChunkNumber()).toCompactString();

            logger.debug("Queuing ACK job for VV: " + vehicle.getName() + "  " + vehicle.getState().name()
                + " result is " + result);

            jobService.addJobIfNotExists(VvRteConstants.MIGRATION_JOB_QUEUE_NAME
                , String.format(VvRteConstants.MIGRATION_FORMAT_SEND_ACK, vehicle.getId())
                , org.apache.commons.codec.binary.StringUtils.getBytesUtf8(result));
            return;
        }

        if (vehicle.getState() == VirtualVehicleState.TASK_COMPLETION_AWAITED)
        {
            startVehicle(vehicle, true);
            return;
        }

        if (!requiredStates.contains(vehicle.getState()))
        {
            return;
        }

        boolean migrate = vehicle.getMigrationDestination() == null;

        if (migrate && setDestinationAndSaveVehicle(groundStations, vehicle))
        {
            migrator.initiateMigration(vehicle);
        }
    }

    /**
     * @param groundStations the list of connected ground stations.
     * @param vehicle the vehicle to be migrated.
     */
    private boolean setDestinationAndSaveVehicle(List<RealVehicle> groundStations, VirtualVehicle vehicle)
    {
        if (groundStations.isEmpty())
        {
            return false;
        }

        RealVehicle destination = groundStations.get(0);
        RealVehicle source = rvRepository.findOwnRealVehicle();

        if (destination.getId().equals(source.getId()))
        {
            return false;
        }

        vehicle.setMigrationDestination(destination);
        vehicle.setMigrationSource(source);
        vehicle.setUpdateTime(timeService.newDate());
        sessionManager.getSession().update(vehicle);
        sessionManager.commit();
        return true;
    }
}
