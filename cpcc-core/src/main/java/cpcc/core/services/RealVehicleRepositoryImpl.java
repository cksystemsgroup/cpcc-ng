// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.core.services;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.hibernate.HibernateSessionManager;

import cpcc.core.entities.Parameter;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;
import cpcc.core.entities.RealVehicleType;
import cpcc.core.services.jobs.TimeService;

/**
 * Real Vehicle Repository Implementation.
 */
public class RealVehicleRepositoryImpl implements RealVehicleRepository
{
    private static final int TOO_OLD_TO_REMEMBER = 86400000;

    private static final String LAST_UPDATE = "lastUpdate";
    private static final String TYPE = "type";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String URL = "url";

    private HibernateSessionManager sessionManager;
    private QueryManager qm;
    private TimeService timeService;
    private long connectionTimeout;

    /**
     * @param sessionManager the Hibernate session manager {@link HibernateSessionManager}
     * @param qm the query manager instance.
     * @param timeService the time service instance.
     */
    public RealVehicleRepositoryImpl(HibernateSessionManager sessionManager, QueryManager qm, TimeService timeService)
    {
        this.sessionManager = sessionManager;
        this.qm = qm;
        this.timeService = timeService;
        this.connectionTimeout = 10000L;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RealVehicle> findAllRealVehicles()
    {
        return sessionManager.getSession()
            .createQuery("FROM RealVehicle ORDER BY id", RealVehicle.class)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RealVehicle> findAllActiveRealVehicles()
    {
        return sessionManager.getSession()
            .createQuery("FROM RealVehicle WHERE deleted = FALSE ORDER BY id", RealVehicle.class)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RealVehicle> findAllGroundStations()
    {
        return sessionManager.getSession()
            .createQuery("FROM RealVehicle WHERE type = :type", RealVehicle.class)
            .setParameter(TYPE, RealVehicleType.GROUND_STATION)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RealVehicle> findAllActiveRealVehiclesExceptOwn()
    {
        Parameter rvNameParam = qm.findParameterByName(Parameter.REAL_VEHICLE_NAME);
        if (rvNameParam == null)
        {
            return findAllActiveRealVehicles();
        }

        return sessionManager.getSession()
            .createQuery("FROM RealVehicle WHERE deleted = FALSE AND name != :name", RealVehicle.class)
            .setParameter(NAME, rvNameParam.getValue())
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RealVehicle> findAllRealVehiclesOrderByName()
    {
        return sessionManager.getSession()
            .createQuery("FROM RealVehicle ORDER BY name", RealVehicle.class)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealVehicle findRealVehicleByName(String name)
    {
        List<RealVehicle> rvList = sessionManager.getSession()
            .createQuery("FROM RealVehicle WHERE name = :name AND deleted = FALSE", RealVehicle.class)
            .setParameter(NAME, name)
            .list();

        return !rvList.isEmpty() ? rvList.get(0) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealVehicle findRealVehicleByUrl(String url)
    {
        return sessionManager.getSession()
            .createQuery("FROM RealVehicle WHERE url = :url", RealVehicle.class)
            .setParameter(URL, url)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealVehicle findRealVehicleById(Integer id)
    {
        return sessionManager.getSession()
            .createQuery("FROM RealVehicle WHERE id = :id", RealVehicle.class)
            .setParameter(ID, id)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealVehicle findOwnRealVehicle()
    {
        Parameter rvNameParam = qm.findParameterByName(Parameter.REAL_VEHICLE_NAME);
        return rvNameParam != null ? findRealVehicleByName(rvNameParam.getValue()) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RealVehicleState> findAllRealVehicleStates()
    {
        return sessionManager.getSession()
            .createQuery("FROM RealVehicleState", RealVehicleState.class)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealVehicleState findRealVehicleStateById(int id)
    {
        return sessionManager.getSession()
            .createQuery("FROM RealVehicleState WHERE id = :id", RealVehicleState.class)
            .setParameter(ID, id)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanupOldVehicleStates()
    {
        List<RealVehicleState> oldRvStates = sessionManager.getSession()
            .createQuery("FROM RealVehicleState WHERE lastUpdate <= :lastUpdate", RealVehicleState.class)
            .setParameter(LAST_UPDATE, new Date(timeService.currentTimeMillis() - TOO_OLD_TO_REMEMBER))
            .list();

        oldRvStates.forEach(x -> sessionManager.getSession().delete(x));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRealVehicleConnected(int id)
    {
        RealVehicleState state = findRealVehicleStateById(id);
        return state != null && timeService.currentTimeMillis() - state.getLastUpdate().getTime() < connectionTimeout;
    }
}
