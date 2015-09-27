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

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.Type;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.services.UniqueLongIdGenerator;

public class UniqueLongIdGeneratorTest
{
    private UniqueLongIdGenerator sut;
    private Type type;
    private Properties params;
    private Dialect dialect;
    private SessionImplementor session;
    private Object object;
    private EntityPersister persister;

    @BeforeMethod
    public void setUp()
    {
        sut = new UniqueLongIdGenerator();

        type = mock(Type.class);
        params = mock(Properties.class);
        dialect = mock(Dialect.class);
        object = mock(Object.class);
        session = mock(SessionImplementor.class);

        persister = mock(EntityPersister.class);
        when(session.getEntityPersister("name", object)).thenReturn(persister);
    }

    @Test
    public void shouldRunConfiguration()
    {
        when(params.getProperty(IdentifierGenerator.ENTITY_NAME)).thenReturn("name");

        sut.configure(type, params, dialect);
    }

    @Test(expectedExceptions = MappingException.class)
    public void shouldThrowExceptionOnEmptyEntityName()
    {
        sut.configure(type, params, dialect);
    }

    @DataProvider
    public Object[][] numberDataProvider()
    {
        return new Object[][]{
            new Object[]{123L},
            new Object[]{0L},
            new Object[]{9876565L},
        };
    }

    @Test(dataProvider = "numberDataProvider")
    public void shouldGenerateRandomId(Long expected)
    {
        when(persister.getIdentifier(object, session)).thenReturn(expected);

        when(params.getProperty(IdentifierGenerator.ENTITY_NAME)).thenReturn("name");
        sut.configure(type, params, dialect);

        Serializable actual = sut.generate(session, object);

        assertThat(actual).isInstanceOf(Long.class);
        assertThat((Long) actual).isEqualTo(expected);
    }

    @Test
    public void shouldGenerateNewRandomId()
    {
        when(params.getProperty(IdentifierGenerator.ENTITY_NAME)).thenReturn("name");
        sut.configure(type, params, dialect);

        Serializable actual1 = sut.generate(session, object);
        Serializable actual2 = sut.generate(session, object);
        Serializable actual3 = sut.generate(session, object);

        assertThat(actual1).isInstanceOf(Long.class);
        assertThat(actual2).isInstanceOf(Long.class);
        assertThat(actual3).isInstanceOf(Long.class);

        assertThat((Long) actual1).isNotEqualTo((Long) actual2);
        assertThat((Long) actual2).isNotEqualTo((Long) actual1);
        assertThat((Long) actual3).isNotEqualTo((Long) actual2);
        assertThat((Long) actual3).isNotEqualTo((Long) actual1);
    }
}
