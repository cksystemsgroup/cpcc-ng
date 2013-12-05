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

import java.util.List;

import org.hibernate.criterion.Order;

import at.uni_salzburg.cs.cpcc.persistence.services.AbstractRepository;
import at.uni_salzburg.cs.cpcc.persistence.services.QueryManager;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleStorage;

/**
 * VvRteRepositoryImpl
 */
public class VvRteRepositoryImpl extends AbstractRepository implements VvRteRepository
{
    private QueryManager qm;

    /**
     * @param qm the query manager
     */
    public VvRteRepositoryImpl(QueryManager qm)
    {
        super(qm.getSession());
        this.qm = qm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QueryManager getQueryManager()
    {
        return qm;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<VirtualVehicle> findAllVehicles()
    {
        return (List<VirtualVehicle>) getSession()
            .createCriteria(VirtualVehicle.class)
            .addOrder(Order.asc("id"))
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicle findVirtualVehicleById(Integer id)
    {
        return (VirtualVehicle) getSession()
            .createQuery("from VirtualVehicle where id = :id")
            .setInteger("id", id)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicle findVirtualVehicleByName(String name)
    {
        return (VirtualVehicle) getSession()
            .createQuery("from VirtualVehicle where name = :name")
            .setString("name", name)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> findAllStorageItemNames()
    {
        return (List<String>) getSession()
            .createQuery("select name from VirtualVehicleStorage")
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicleStorage findStorageItemByName(String name)
    {
        return (VirtualVehicleStorage) getSession()
            .createQuery("from VirtualVehicleStorage where name = :name")
            .setString("name", name)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicleStorage findStorageItemById(Integer id)
    {
        return (VirtualVehicleStorage) getSession()
            .createQuery("from VirtualVehicleStorage where id = :id")
            .setInteger("id", id)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<VirtualVehicleStorage> findStorageItemsByVirtualVehicle(Integer id)
    {
        return (List<VirtualVehicleStorage>) getSession()
            .createQuery("from VirtualVehicleStorage where virtualVehicle.id = :id")
            .setInteger("id", id)
            .list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<VirtualVehicleStorage> findStorageItemsByVirtualVehicle(Integer id, String startName,
        int maxEntries)
    {
        return (List<VirtualVehicleStorage>) getSession()
            .createQuery("FROM VirtualVehicleStorage WHERE virtualVehicle.id = :id AND name >= :name ORDER BY name")
            .setInteger("id", id)
            .setString("name", startName)
            .setMaxResults(maxEntries)
            .list();
    }
}
