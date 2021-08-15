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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.Properties;
import java.util.stream.Stream;

import org.hibernate.MappingException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class UniqueLongIdGeneratorTest
{
    private UniqueLongIdGenerator sut;
    private Type type;
    private Properties params;
    private ServiceRegistry serviceRegistry;
    private SessionImplementor session;
    private Object object;
    private EntityPersister persister;

    @BeforeEach
    public void setUp()
    {
        sut = new UniqueLongIdGenerator();

        type = mock(Type.class);
        params = mock(Properties.class);
        serviceRegistry = mock(ServiceRegistry.class);
        object = mock(Object.class);
        session = mock(SessionImplementor.class);

        persister = mock(EntityPersister.class);
        when(session.getEntityPersister("name", object)).thenReturn(persister);
    }

    @Test
    public void shouldRunConfiguration()
    {
        when(params.getProperty(IdentifierGenerator.ENTITY_NAME)).thenReturn("name");

        sut.configure(type, params, serviceRegistry);
    }

    public void shouldThrowExceptionOnEmptyEntityName()
    {
        try
        {
            sut.configure(type, params, serviceRegistry);
            failBecauseExceptionWasNotThrown(MappingException.class);
        }
        catch (MappingException e)
        {
            assertThat(e).hasMessage("no entity name");
        }
    }

    static Stream<Arguments> numberDataProvider()
    {
        return Stream.of(
            arguments(123L),
            arguments(0L),
            arguments(9876565L));
    }

    @ParameterizedTest
    @MethodSource("numberDataProvider")
    public void shouldGenerateRandomId(Long expected)
    {
        when(persister.getIdentifier(object, session)).thenReturn(expected);

        when(params.getProperty(IdentifierGenerator.ENTITY_NAME)).thenReturn("name");
        sut.configure(type, params, serviceRegistry);

        Serializable actual = sut.generate(session, object);

        assertThat(actual).isInstanceOf(Long.class);
        assertThat((Long) actual).isEqualTo(expected);
    }

    @Test
    public void shouldGenerateNewRandomId()
    {
        when(params.getProperty(IdentifierGenerator.ENTITY_NAME)).thenReturn("name");
        sut.configure(type, params, serviceRegistry);

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
