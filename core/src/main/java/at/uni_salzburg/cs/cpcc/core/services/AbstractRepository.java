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
package at.uni_salzburg.cs.cpcc.core.services;

import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbstractRepository
 */
public abstract class AbstractRepository implements Repository
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRepository.class);

    private Session session;

    /**
     * @param session the session object {@link Session}
     */
    public AbstractRepository(Session session)
    {
        this.session = session;
    }

    /**
     * @param list the database objects to be saved.
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
     * @param o the database object to be saved.
     */
    protected void saveOrUpdate(Object o)
    {
        if (o != null)
        {
            session.saveOrUpdate(o);
        }
    }

    /**
     * @param o the database object to be deleted.
     */
    protected void delete(Object o)
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
     * @param list the database objects to be deleted.
     */
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

    /**
     * @return the session
     */
    protected Session getSession()
    {
        return session;
    }
}
