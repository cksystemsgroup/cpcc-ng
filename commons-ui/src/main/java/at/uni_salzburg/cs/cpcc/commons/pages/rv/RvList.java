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

package at.uni_salzburg.cs.cpcc.commons.pages.rv;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import cpcc.rv.base.services.StateSynchronizer;

/**
 * Real Vehicle List
 */
public class RvList
{
    @Inject
    private HibernateSessionManager sessionManager;

    @Inject
    private QueryManager qm;

    @Inject
    protected StateSynchronizer confSync;

    @Property
    private List<RealVehicle> realVehicleList;

    @Property
    private RealVehicle realVehicle;

    void onActivate()
    {
        realVehicleList = qm.findAllRealVehiclesOrderByName();
    }

    @CommitAfter
    void onActivateRealVehicle(Integer id)
    {
        RealVehicle rv = qm.findRealVehicleById(id);
        rv.setDeleted(Boolean.FALSE);
        rv.setLastUpdate(new Date());
        sessionManager.getSession().saveOrUpdate(rv);
        sessionManager.commit();
    }

    @CommitAfter
    void onDeactivateRealVehicle(Integer id)
    {
        RealVehicle rv = qm.findRealVehicleById(id);
        rv.setDeleted(Boolean.TRUE);
        rv.setLastUpdate(new Date());
        sessionManager.getSession().saveOrUpdate(rv);
        sessionManager.commit();
    }

    public boolean getConnected()
    {
        return realVehicle != null ? qm.isRealVehicleConnected(realVehicle.getId()) : false;
    }
}
