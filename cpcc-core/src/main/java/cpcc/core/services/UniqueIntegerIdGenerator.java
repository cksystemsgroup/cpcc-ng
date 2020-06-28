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

package cpcc.core.services;

import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.lang3.RandomUtils;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;

/**
 * UniqueIntegerIdGenerator
 */
public class UniqueIntegerIdGenerator implements IdentifierGenerator, Configurable
{
    private String entityName;

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(Type type, Properties params, Dialect d)
    {
        entityName = params.getProperty(ENTITY_NAME);
        if (entityName == null)
        {
            throw new MappingException("no entity name");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Serializable generate(SessionImplementor session, Object obj)
    {
        final Serializable id = session.getEntityPersister(entityName, obj).getIdentifier(obj, session);
        return id != null ? id : RandomUtils.nextInt(0, Integer.MAX_VALUE);
    }
}
