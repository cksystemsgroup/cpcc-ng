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

package at.uni_salzburg.cs.cpcc.core.entities;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Iterator;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.Device;
import at.uni_salzburg.cs.cpcc.core.entities.MappingAttributes;
import at.uni_salzburg.cs.cpcc.core.entities.MappingAttributesPK;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.entities.Topic;

/**
 * MappingAttributesTest
 */
public class MappingAttributesTest
{
    MappingAttributes attributes;

    @BeforeMethod
    public void setUp()
    {
        attributes = new MappingAttributes();
    }

    @DataProvider
    private final Iterator<Object[]> primaryKeyDataProvider()
    {
        return new Iterator<Object[]>()
        {
            private int counter = -1;

            @Override
            public boolean hasNext()
            {
                return counter < 5;
            }

            @Override
            public Object[] next()
            {
                if (counter++ < 0)
                {
                    return new Object[]{null};
                }

                MappingAttributesPK pk = mock(MappingAttributesPK.class);
                when(pk.getDevice()).thenReturn(new Device());
                when(pk.getTopic()).thenReturn(new Topic());
                return new Object[]{pk};
            }

            @Override
            public void remove()
            {
                // Intentionally empty.
            }
        };
    };

    @Test(dataProvider = "primaryKeyDataProvider")
    public void shouldStorePrimaryKey(MappingAttributesPK pk)
    {
        attributes.setPk(pk);
        assertThat(attributes.getPk()).isEqualTo(pk);
    }

    @DataProvider
    public Object[][] booleanDataProvider()
    {
        return new Object[][]{
            new Object[]{null},
            new Object[]{Boolean.FALSE},
            new Object[]{Boolean.TRUE},
        };
    };

    @Test(dataProvider = "booleanDataProvider")
    public void shouldStoreVvVisible(Boolean value)
    {
        attributes.setVvVisible(value);
        assertThat(attributes.getVvVisible()).isEqualTo(value);
    }

    @Test(dataProvider = "booleanDataProvider")
    public void shouldConnectedToAutopilot(Boolean value)
    {
        attributes.setConnectedToAutopilot(value);
        assertThat(attributes.getConnectedToAutopilot()).isEqualTo(value);
    }

    @Test
    public void shouldStoreSensorDefinition()
    {
        SensorDefinition def = mock(SensorDefinition.class);
        attributes.setSensorDefinition(def);
        assertThat(attributes.getSensorDefinition()).isNotNull().isEqualTo(def);
    }

}
