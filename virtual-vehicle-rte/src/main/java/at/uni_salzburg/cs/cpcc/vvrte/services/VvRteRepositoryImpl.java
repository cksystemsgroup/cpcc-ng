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

import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;

/**
 * VvRteRepositoryImpl
 */
public class VvRteRepositoryImpl implements VvRteRepository
{
    private static final Logger LOG = LoggerFactory.getLogger(VvRteRepositoryImpl.class);

    private Session session;

    /**
     * @param session {@link Session}
     */
    public VvRteRepositoryImpl(Session session)
    {
        this.session = session;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<VirtualVehicle> findAllVehicles()
    {
        return (List<VirtualVehicle>) session.createCriteria(VirtualVehicle.class).addOrder(Order.asc("id")).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicle findVirtualVehicleById(Integer id)
    {
        return (VirtualVehicle) session.createQuery("from VirtualVehicle where id = :id").setInteger("id", id)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    public void saveOrUpdateAll(Collection<?> list)
    {
        if (list == null)
        {
            return;
        }
        for (Object o : list)
        {
            session.saveOrUpdate(o);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveOrUpdate(Object o)
    {
        if (o != null)
        {
            session.saveOrUpdate(o);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Object o)
    {
        try
        {
            session.delete(o);
        }
        catch (HibernateException e)
        {
            LOG.error(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll(Collection<?> list)
    {
        if (list == null)
        {
            return;
        }
        for (Object o : list)
        {
            delete(o);
        }
    }
}
