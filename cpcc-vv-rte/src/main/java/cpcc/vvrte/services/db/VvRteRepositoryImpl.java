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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            .createQuery("UPDATE VirtualVehicle SET state = :newState WHERE state = :oldState")
            .setParameter("newState", VirtualVehicleState.MIGRATION_INTERRUPTED_SND)
            .setParameter("oldState", VirtualVehicleState.MIGRATING_SND)
            .executeUpdate();

        session
            .createQuery("UPDATE VirtualVehicle SET state = :newState WHERE state = :oldState")
            .setParameter("newState", VirtualVehicleState.MIGRATION_INTERRUPTED_SND)
            .setParameter("oldState", VirtualVehicleState.MIGRATION_AWAITED_SND)
            .executeUpdate();

        session
            .createQuery("UPDATE VirtualVehicle SET migrationStartTime = :now "
                + "WHERE state = :state AND migrationStartTime is NULL")
            .setParameter("state", VirtualVehicleState.MIGRATION_INTERRUPTED_SND)
            .setParameter("now", timeService.newDate())
            .executeUpdate();

        session
            .createQuery("UPDATE VirtualVehicle SET state = :newState WHERE state = :oldState")
            .setParameter("newState", VirtualVehicleState.INTERRUPTED)
            .setParameter("oldState", VirtualVehicleState.RUNNING)
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
        return (List<VirtualVehicle>) session
            .createCriteria(VirtualVehicle.class)
            .addOrder(Property.forName("id").asc())
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<VirtualVehicleState, Integer> getVvStatistics()
    {
        Map<VirtualVehicleState, Integer> statistics = new HashMap<>();

        statistics.putAll(Stream.of(VirtualVehicleState.values())
            .map(x -> new SimpleEntry<>(x, Integer.valueOf(0)))
            .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue())));

        List<VirtualVehicleState> stateList = session
            .createCriteria(VirtualVehicle.class)
            .setProjection(Projections.property("state"))
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
        return (List<VirtualVehicle>) session
            .createCriteria(VirtualVehicle.class)
            .add(Restrictions.eq("state", VirtualVehicleState.RUNNING))
            .addOrder(Property.forName("id").asc())
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
        List<VirtualVehicle> vvs = new ArrayList<VirtualVehicle>();

        vvs.addAll((List<VirtualVehicle>) session
            .createCriteria(VirtualVehicle.class, "v")
            .add(Restrictions.in("state", allowedStates))
            .createCriteria("v.task", "t", JoinType.LEFT_OUTER_JOIN, Restrictions.eq("taskState", TaskState.COMPLETED))
            .list());

        vvs.addAll((List<VirtualVehicle>) session.createCriteria(VirtualVehicle.class)
            .add(Restrictions.eq("state", VirtualVehicleState.TASK_COMPLETION_AWAITED))
            .add(Restrictions.isNull("task"))
            .list());

        vvs.addAll((List<VirtualVehicle>) session.createCriteria(VirtualVehicle.class)
            .add(Restrictions.eq("state", VirtualVehicleState.MIGRATION_AWAITED_SND))
            .add(Restrictions.le("migrationStartTime", new Date(timeService.currentTimeMillis() - 10000)))
            .list());

        vvs.addAll((List<VirtualVehicle>) session.createCriteria(VirtualVehicle.class)
            .add(Restrictions.in("state", Arrays.asList(
                VirtualVehicleState.MIGRATING_SND,
                VirtualVehicleState.MIGRATION_INTERRUPTED_SND,
                VirtualVehicleState.MIGRATION_COMPLETED_SND)))
            .add(Restrictions.le("updateTime", new Date(timeService.currentTimeMillis() - 55000)))
            .list());

        vvs.addAll((List<VirtualVehicle>) session.createCriteria(VirtualVehicle.class)
            .add(Restrictions.eq("state", VirtualVehicleState.MIGRATING_RCV))
            .add(Restrictions.le("updateTime", new Date(timeService.currentTimeMillis() - 175000)))
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
            .add(Restrictions.eq("id", id))
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicle findVirtualVehicleByName(String name)
    {
        return (VirtualVehicle) session.createCriteria(VirtualVehicle.class)
            .add(Restrictions.eq("name", name))
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public VirtualVehicle findVirtualVehicleByUUID(String uuid)
    {
        List<VirtualVehicle> vvs = (List<VirtualVehicle>) session.createCriteria(VirtualVehicle.class)
            .add(Restrictions.eq("uuid", uuid))
            .addOrder(Order.asc("id"))
            .list();

        VirtualVehicle result = vvs.isEmpty() ? null : vvs.remove(0);
        return result;
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
            logger.warn("Not deleting virtual vehicle " + vehicle.getName()
                + " (" + vehicle.getUuid() + ") because of state " + vehicle.getState());
            return;
        }

        logger.info("Deleting virtual vehicle " + vehicle.getName() + " (" + vehicle.getUuid() + ") "
            + vehicle.getState());

        session
            .createQuery("DELETE FROM VirtualVehicleStorage WHERE virtualVehicle.id = :id")
            .setParameter("id", vehicle.getId())
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
        return (List<String>) session
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
                Restrictions.eq("name", name)))
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicleStorage findStorageItemById(Integer id)
    {
        return (VirtualVehicleStorage) session.createCriteria(VirtualVehicleStorage.class)
            .add(Restrictions.eq("id", id))
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<VirtualVehicleStorage> findStorageItemsByVirtualVehicle(Integer id)
    {
        return (List<VirtualVehicleStorage>) session
            .createCriteria(VirtualVehicleStorage.class)
            .addOrder(Order.asc("modificationTime"))
            .createCriteria("virtualVehicle", "vv", JoinType.INNER_JOIN)
            .add(Restrictions.eq("id", id))
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<VirtualVehicleStorage> findStorageItemsByVirtualVehicle(Integer id, String startName, int maxEntries)
    {
        return (List<VirtualVehicleStorage>) session
            .createQuery("FROM VirtualVehicleStorage WHERE virtualVehicle.id = :id AND name > :name ORDER BY name")
            .setInteger("id", id)
            .setString("name", startName)
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
        return (List<VirtualVehicleStorage>) session
            .createQuery("FROM VirtualVehicleStorage WHERE virtualVehicle.id = :id")
            .setInteger("id", id)
            .list();
    }
}
