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

package cpcc.vvrte.services.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.services.jobs.TimeService;
import cpcc.vvrte.entities.TaskState;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.entities.VirtualVehicleStorage;

/**
 * VvRteRepository implementation.
 */
public class VvRteRepositoryImpl implements VvRteRepository
{
    private static final Logger LOG = LoggerFactory.getLogger(VvRteRepositoryImpl.class);

    private static final String NOW = "now";
    private static final String OLD_STATE = "oldState";
    private static final String NEW_STATE = "newState";
    private static final String UUID = "uuid";
    private static final String NAME = "name";
    private static final String UPDATE_TIME = "updateTime";
    private static final String MIGRATION_START_TIME = "migrationStartTime";
    private static final String STATE = "state";
    private static final String ID = "id";

    private static final String UPDATE_VIRTUAL_VEHICLE_STATE =
        "UPDATE VirtualVehicle SET state = :newState WHERE state = :oldState";

    private HibernateSessionManager sessionManager;
    private TaskRepository taskRepository;
    private TimeService timeService;

    /**
     * @param sessionManager the Hibernate session manager.
     * @param taskRepository the task repository instance.
     * @param timeService the time service.
     */
    public VvRteRepositoryImpl(HibernateSessionManager sessionManager, TaskRepository taskRepository,
        TimeService timeService)
    {
        this.sessionManager = sessionManager;
        this.taskRepository = taskRepository;
        this.timeService = timeService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetVirtualVehicleStates()
    {
        sessionManager.getSession()
            .createQuery(UPDATE_VIRTUAL_VEHICLE_STATE)
            .setParameter(NEW_STATE, VirtualVehicleState.MIGRATION_INTERRUPTED_SND)
            .setParameter(OLD_STATE, VirtualVehicleState.MIGRATING_SND)
            .executeUpdate();

        sessionManager.getSession()
            .createQuery(UPDATE_VIRTUAL_VEHICLE_STATE)
            .setParameter(NEW_STATE, VirtualVehicleState.MIGRATION_INTERRUPTED_SND)
            .setParameter(OLD_STATE, VirtualVehicleState.MIGRATION_AWAITED_SND)
            .executeUpdate();

        sessionManager.getSession()
            .createQuery("UPDATE VirtualVehicle SET migrationStartTime = :now "
                + "WHERE state = :state AND migrationStartTime is NULL")
            .setParameter(STATE, VirtualVehicleState.MIGRATION_INTERRUPTED_SND)
            .setParameter(NOW, timeService.newDate())
            .executeUpdate();

        sessionManager.getSession()
            .createQuery(UPDATE_VIRTUAL_VEHICLE_STATE)
            .setParameter(NEW_STATE, VirtualVehicleState.INTERRUPTED)
            .setParameter(OLD_STATE, VirtualVehicleState.RUNNING)
            .executeUpdate();

        sessionManager.getSession()
            .createQuery("UPDATE VirtualVehicle SET task = null")
            .executeUpdate();

        sessionManager.getSession()
            .createQuery("DELETE Task")
            .executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<VirtualVehicle> findAllVehicles()
    {
        return sessionManager.getSession()
            .createQuery("FROM VirtualVehicle ORDER BY id", VirtualVehicle.class)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<VirtualVehicleState, Integer> getVvStatistics()
    {
        Map<VirtualVehicleState, Integer> statistics = new EnumMap<>(VirtualVehicleState.class);

        statistics.putAll(Stream.of(VirtualVehicleState.values())
            .map(x -> Pair.of(x, Integer.valueOf(0)))
            .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)));

        sessionManager.getSession()
            .createQuery("SELECT state FROM VirtualVehicle", VirtualVehicleState.class)
            .list()
            .forEach(x -> statistics.put(x, statistics.get(x) + 1));

        return statistics;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<VirtualVehicle> findAllActiveVehicles(int maxResults)
    {
        return sessionManager.getSession()
            .createQuery("FROM VirtualVehicle WHERE state = :state ORDER BY id", VirtualVehicle.class)
            .setParameter(STATE, VirtualVehicleState.RUNNING)
            .setMaxResults(maxResults)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<VirtualVehicle> findAllStuckVehicles(Set<VirtualVehicleState> allowedStates)
    {
        List<VirtualVehicle> vvs = new ArrayList<>();

        vvs.addAll(sessionManager.getSession()
            .createQuery("SELECT v FROM VirtualVehicle v LEFT OUTER JOIN Task t ON t.id = v.task.id "
                + "WHERE v.state IN (:state)"
                + "AND t.taskState = :completed", VirtualVehicle.class)
            .setParameterList(STATE, allowedStates)
            .setParameter("completed", TaskState.COMPLETED)
            .list());

        vvs.addAll(sessionManager.getSession()
            .createQuery("FROM VirtualVehicle v WHERE v.state = :state AND v.task IS NULL", VirtualVehicle.class)
            .setParameter(STATE, VirtualVehicleState.TASK_COMPLETION_AWAITED)
            .list());

        vvs.addAll(sessionManager.getSession()
            .createQuery("FROM VirtualVehicle v WHERE v.state = :state AND v.migrationStartTime <= :migrationStartTime",
                VirtualVehicle.class)
            .setParameter(STATE, VirtualVehicleState.MIGRATION_AWAITED_SND)
            .setParameter(MIGRATION_START_TIME, new Date(timeService.currentTimeMillis() - 10000))
            .list());

        vvs.addAll(sessionManager.getSession()
            .createQuery("FROM VirtualVehicle v WHERE v.state IN (:state) AND v.updateTime <= :updateTime",
                VirtualVehicle.class)
            .setParameterList(STATE, Arrays.asList(
                VirtualVehicleState.MIGRATING_SND,
                VirtualVehicleState.MIGRATION_INTERRUPTED_SND,
                VirtualVehicleState.MIGRATION_COMPLETED_SND))
            .setParameter(UPDATE_TIME, new Date(timeService.currentTimeMillis() - 55000))
            .list());

        vvs.addAll(sessionManager.getSession()
            .createQuery("FROM VirtualVehicle v WHERE v.state = :state AND v.updateTime <= :updateTime",
                VirtualVehicle.class)
            .setParameter(STATE, VirtualVehicleState.MIGRATING_RCV)
            .setParameter(UPDATE_TIME, new Date(timeService.currentTimeMillis() - 175000))
            .list());

        return vvs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicle findVirtualVehicleById(Integer id)
    {
        return sessionManager.getSession()
            .createQuery("FROM VirtualVehicle WHERE id = :id", VirtualVehicle.class)
            .setParameter(ID, id)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicle findVirtualVehicleByName(String name)
    {
        return sessionManager.getSession()
            .createQuery("FROM VirtualVehicle WHERE name = :name", VirtualVehicle.class)
            .setParameter(NAME, name)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicle findVirtualVehicleByUUID(String uuid)
    {
        List<VirtualVehicle> vvs = sessionManager.getSession()
            .createQuery("FROM VirtualVehicle WHERE uuid = :uuid ORDER BY id", VirtualVehicle.class)
            .setParameter(UUID, uuid)
            .list();

        return vvs.isEmpty() ? null : vvs.remove(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteVirtualVehicleById(VirtualVehicle vehicle)
    {
        if (vehicle == null)
        {
            return;
        }

        if (!VirtualVehicleState.VV_STATES_FOR_DELETE.contains(vehicle.getState()))
        {
            LOG.warn("Not deleting virtual vehicle {} ({}) because of state {}",
                vehicle.getName(), vehicle.getUuid(), vehicle.getState());
            return;
        }

        LOG.info("Deleting virtual vehicle {} ({}) {}",
            vehicle.getName(), vehicle.getUuid(), vehicle.getState());

        sessionManager.getSession()
            .createQuery("DELETE FROM VirtualVehicleStorage WHERE virtualVehicle.id = :id")
            .setParameter(ID, vehicle.getId())
            .executeUpdate();

        taskRepository.unlinkTasksFromVirtualVehicleById(vehicle.getId());

        vehicle.setTask(null);
        sessionManager.getSession().update(vehicle);
        sessionManager.getSession().delete(vehicle);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> findAllStorageItemNames()
    {
        return sessionManager.getSession()
            .createQuery("SELECT name FROM VirtualVehicleStorage")
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicleStorage findStorageItemByVirtualVehicleAndName(VirtualVehicle vehicle, String name)
    {
        return sessionManager.getSession()
            .createQuery("FROM VirtualVehicleStorage s WHERE s.virtualVehicle.id = :id AND s.name = :name",
                VirtualVehicleStorage.class)
            .setParameter(ID, vehicle.getId())
            .setParameter(NAME, name)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicleStorage findStorageItemById(Integer id)
    {
        return sessionManager.getSession()
            .createQuery("FROM VirtualVehicleStorage WHERE id = :id", VirtualVehicleStorage.class)
            .setParameter(ID, id)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<VirtualVehicleStorage> findStorageItemsByVirtualVehicle(Integer id)
    {
        return sessionManager.getSession()
            .createQuery("SELECT s FROM VirtualVehicleStorage s "
                + "INNER JOIN VirtualVehicle vv ON s.virtualVehicle.id = vv.id "
                + "WHERE vv.id = :id "
                + "ORDER BY s.modificationTime", VirtualVehicleStorage.class)
            .setParameter(ID, id)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<VirtualVehicleStorage> findStorageItemsByVirtualVehicle(Integer id, String startName, int maxEntries)
    {
        return sessionManager.getSession()
            .createQuery("FROM VirtualVehicleStorage s "
                + "WHERE s.virtualVehicle.id = :id AND s.name > :name "
                + "ORDER BY name")
            .setParameter(ID, id)
            .setParameter(NAME, startName)
            .setMaxResults(maxEntries > 0 ? maxEntries : 1)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<VirtualVehicleStorage> findAllStorageItemsByVirtualVehicle(Integer id)
    {
        return sessionManager.getSession()
            .createQuery("FROM VirtualVehicleStorage s WHERE s.virtualVehicle.id = :id")
            .setParameter(ID, id)
            .list();
    }
}
