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

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

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
    private static final String REAL_VEHICLE_NAME = "name";
    private static final String REAL_VEHICLE_URL = "url";

    private Session session;
    private QueryManager qm;
    private TimeService timeService;
    private long connectionTimeout;

    /**
     * @param session the Hibernate {@link Session}
     * @param qm the query manager instance.
     * @param timeService the time service instance.
     */
    public RealVehicleRepositoryImpl(Session session, QueryManager qm, TimeService timeService)
    {
        this.session = session;
        this.qm = qm;
        this.timeService = timeService;
        this.connectionTimeout = 10000L;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RealVehicle> findAllRealVehicles()
    {
        return (List<RealVehicle>) session
            .createCriteria(RealVehicle.class)
            .addOrder(Property.forName("id").asc())
            .list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RealVehicle> findAllActiveRealVehicles()
    {
        return (List<RealVehicle>) session
            .createCriteria(RealVehicle.class)
            .add(Restrictions.eq("deleted", Boolean.FALSE))
            .addOrder(Property.forName("id").asc())
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RealVehicle> findAllGroundStations()
    {
        return (List<RealVehicle>) session
            .createCriteria(RealVehicle.class, "rv")
            .add(Restrictions.eq("type", RealVehicleType.GROUND_STATION))
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RealVehicle> findAllRealVehiclesExceptOwn()
    {
        Parameter rvNameParam = qm.findParameterByName(Parameter.REAL_VEHICLE_NAME);
        if (rvNameParam == null)
        {
            return findAllRealVehicles();
        }

        return (List<RealVehicle>) session
            .createCriteria(RealVehicle.class)
            .add(Restrictions.not(Restrictions.eq(REAL_VEHICLE_NAME, rvNameParam.getValue())))
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<RealVehicle> findAllRealVehiclesOrderByName()
    {
        return (List<RealVehicle>) session
            .createCriteria(RealVehicle.class)
            .addOrder(Property.forName(REAL_VEHICLE_NAME).asc())
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealVehicle findRealVehicleByName(String name)
    {
        return (RealVehicle) session
            .createCriteria(RealVehicle.class)
            .add(Restrictions.eq(REAL_VEHICLE_NAME, name))
            .uniqueResult();
    }

    @Override
    public RealVehicle findRealVehicleByUrl(String url)
    {
        return (RealVehicle) session
            .createCriteria(RealVehicle.class)
            .add(Restrictions.eq(REAL_VEHICLE_URL, url))
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealVehicle findRealVehicleById(Integer id)
    {
        return (RealVehicle) session
            .createCriteria(RealVehicle.class)
            .add(Restrictions.eq("id", id))
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
    @SuppressWarnings("unchecked")
    @Override
    public List<RealVehicleState> findAllRealVehicleStates()
    {
        return (List<RealVehicleState>) session
            .createCriteria(RealVehicleState.class)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealVehicleState findRealVehicleStateById(int id)
    {
        return (RealVehicleState) session
            .createCriteria(RealVehicleState.class)
            .add(Restrictions.eq("id", id))
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRealVehicleConnected(int id)
    {
        RealVehicleState state = findRealVehicleStateById(id);
        return state != null
            ? timeService.currentTimeMillis() - state.getLastUpdate().getTime() < connectionTimeout
            : false;
    }
}
