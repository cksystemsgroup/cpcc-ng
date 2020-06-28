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
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;

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
    private static final String NOW = "now";
    private static final String OLD_STATE = "oldState";
    private static final String NEW_STATE = "newState";
    private static final String MODIFICATION_TIME = "modificationTime";
    private static final String UUID = "uuid";
    private static final String NAME = "name";
    private static final String UPDATE_TIME = "updateTime";
    private static final String MIGRATION_START_TIME = "migrationStartTime";
    private static final String TASK = "task";
    private static final String STATE = "state";
    private static final String ID = "id";

    private static final String UPDATE_VIRTUAL_VEHICLE_STATE =
        "UPDATE VirtualVehicle SET state = :newState WHERE state = :oldState";

    private Logger logger;
    private Session session;
    private TaskRepository taskRepository;
    private TimeService timeService;

    /**
     * @param logger the application logger.
     * @param session the Hibernate session.
     * @param taskRepository the task repository instance.
     * @param timeService the time service.
     */
    public VvRteRepositoryImpl(Logger logger, Session session, TaskRepository taskRepository, TimeService timeService)
    {
        this.logger = logger;
        this.session = session;
        this.taskRepository = taskRepository;
        this.timeService = timeService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetVirtualVehicleStates()
    {
        session
            .createQuery(UPDATE_VIRTUAL_VEHICLE_STATE)
            .setParameter(NEW_STATE, VirtualVehicleState.MIGRATION_INTERRUPTED_SND)
            .setParameter(OLD_STATE, VirtualVehicleState.MIGRATING_SND)
            .executeUpdate();

        session
            .createQuery(UPDATE_VIRTUAL_VEHICLE_STATE)
            .setParameter(NEW_STATE, VirtualVehicleState.MIGRATION_INTERRUPTED_SND)
            .setParameter(OLD_STATE, VirtualVehicleState.MIGRATION_AWAITED_SND)
            .executeUpdate();

        session
            .createQuery("UPDATE VirtualVehicle SET migrationStartTime = :now "
                + "WHERE state = :state AND migrationStartTime is NULL")
            .setParameter(STATE, VirtualVehicleState.MIGRATION_INTERRUPTED_SND)
            .setParameter(NOW, timeService.newDate())
            .executeUpdate();

        session
            .createQuery(UPDATE_VIRTUAL_VEHICLE_STATE)
            .setParameter(NEW_STATE, VirtualVehicleState.INTERRUPTED)
            .setParameter(OLD_STATE, VirtualVehicleState.RUNNING)
            .executeUpdate();

        session
            .createQuery("UPDATE VirtualVehicle SET task = null")
            .executeUpdate();

        session
            .createQuery("DELETE Task")
            .executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<VirtualVehicle> findAllVehicles()
    {
        return session
            .createCriteria(VirtualVehicle.class)
            .addOrder(Property.forName(ID).asc())
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<VirtualVehicleState, Integer> getVvStatistics()
    {
        Map<VirtualVehicleState, Integer> statistics = new EnumMap<>(VirtualVehicleState.class);

        statistics.putAll(Stream.of(VirtualVehicleState.values())
            .map(x -> Pair.of(x, Integer.valueOf(0)))
            .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)));

        List<VirtualVehicleState> stateList = session
            .createCriteria(VirtualVehicle.class)
            .setProjection(Projections.property(STATE))
            .list();

        stateList.stream().forEach(x -> statistics.put(x, statistics.get(x) + 1));

        return statistics;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<VirtualVehicle> findAllActiveVehicles(int maxResults)
    {
        return session
            .createCriteria(VirtualVehicle.class)
            .add(Restrictions.eq(STATE, VirtualVehicleState.RUNNING))
            .addOrder(Property.forName(ID).asc())
            .setMaxResults(maxResults)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<VirtualVehicle> findAllStuckVehicles(Set<VirtualVehicleState> allowedStates)
    {
        List<VirtualVehicle> vvs = new ArrayList<>();

        vvs.addAll((List<VirtualVehicle>) session
            .createCriteria(VirtualVehicle.class, "v")
            .add(Restrictions.in(STATE, allowedStates))
            .createCriteria("v.task", "t", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("taskState", TaskState.COMPLETED))
            .list());

        vvs.addAll((List<VirtualVehicle>) session.createCriteria(VirtualVehicle.class)
            .add(Restrictions.eq(STATE, VirtualVehicleState.TASK_COMPLETION_AWAITED))
            .add(Restrictions.isNull(TASK))
            .list());

        vvs.addAll((List<VirtualVehicle>) session.createCriteria(VirtualVehicle.class)
            .add(Restrictions.eq(STATE, VirtualVehicleState.MIGRATION_AWAITED_SND))
            .add(Restrictions.le(MIGRATION_START_TIME, new Date(timeService.currentTimeMillis() - 10000)))
            .list());

        vvs.addAll((List<VirtualVehicle>) session.createCriteria(VirtualVehicle.class)
            .add(Restrictions.in(STATE, Arrays.asList(
                VirtualVehicleState.MIGRATING_SND,
                VirtualVehicleState.MIGRATION_INTERRUPTED_SND,
                VirtualVehicleState.MIGRATION_COMPLETED_SND)))
            .add(Restrictions.le(UPDATE_TIME, new Date(timeService.currentTimeMillis() - 55000)))
            .list());

        vvs.addAll((List<VirtualVehicle>) session.createCriteria(VirtualVehicle.class)
            .add(Restrictions.eq(STATE, VirtualVehicleState.MIGRATING_RCV))
            .add(Restrictions.le(UPDATE_TIME, new Date(timeService.currentTimeMillis() - 175000)))
            .list());

        return vvs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicle findVirtualVehicleById(Integer id)
    {
        return (VirtualVehicle) session.createCriteria(VirtualVehicle.class)
            .add(Restrictions.eq(ID, id))
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicle findVirtualVehicleByName(String name)
    {
        return (VirtualVehicle) session.createCriteria(VirtualVehicle.class)
            .add(Restrictions.eq(NAME, name))
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public VirtualVehicle findVirtualVehicleByUUID(String uuid)
    {
        List<VirtualVehicle> vvs = session.createCriteria(VirtualVehicle.class)
            .add(Restrictions.eq(UUID, uuid))
            .addOrder(Order.asc(ID))
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
            logger.warn("Not deleting virtual vehicle {} ({}) because of state {}",
                vehicle.getName(), vehicle.getUuid(), vehicle.getState());
            return;
        }

        logger.info("Deleting virtual vehicle {} ({}) {}",
            vehicle.getName(), vehicle.getUuid(), vehicle.getState());

        session
            .createQuery("DELETE FROM VirtualVehicleStorage WHERE virtualVehicle.id = :id")
            .setParameter(ID, vehicle.getId())
            .executeUpdate();

        taskRepository.unlinkTasksFromVirtualVehicleById(vehicle.getId());

        vehicle.setTask(null);
        session.update(vehicle);
        session.delete(vehicle);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> findAllStorageItemNames()
    {
        return session
            .createQuery("select name from VirtualVehicleStorage")
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicleStorage findStorageItemByVirtualVehicleAndName(VirtualVehicle vehicle, String name)
    {
        //        return (VirtualVehicleStorage) session
        //            .createQuery("from VirtualVehicleStorage where virtualVehicle.id = :id AND name = :name")
        //            .setInteger("id", vehicle.getId())
        //            .setString("name", name)
        //            .uniqueResult();

        return (VirtualVehicleStorage) session
            .createCriteria(VirtualVehicleStorage.class)
            .add(Restrictions.and(
                Restrictions.eq("virtualVehicle.id", vehicle.getId()),
                Restrictions.eq(NAME, name)))
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicleStorage findStorageItemById(Integer id)
    {
        return (VirtualVehicleStorage) session.createCriteria(VirtualVehicleStorage.class)
            .add(Restrictions.eq(ID, id))
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<VirtualVehicleStorage> findStorageItemsByVirtualVehicle(Integer id)
    {
        return session
            .createCriteria(VirtualVehicleStorage.class)
            .addOrder(Order.asc(MODIFICATION_TIME))
            .createCriteria("virtualVehicle", "vv", JoinType.INNER_JOIN)
            .add(Restrictions.eq(ID, id))
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<VirtualVehicleStorage> findStorageItemsByVirtualVehicle(Integer id, String startName, int maxEntries)
    {
        return session
            .createQuery("FROM VirtualVehicleStorage WHERE virtualVehicle.id = :id AND name > :name ORDER BY name")
            .setInteger(ID, id)
            .setString(NAME, startName)
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
        return session
            .createQuery("FROM VirtualVehicleStorage WHERE virtualVehicle.id = :id")
            .setInteger(ID, id)
            .list();
    }
}
