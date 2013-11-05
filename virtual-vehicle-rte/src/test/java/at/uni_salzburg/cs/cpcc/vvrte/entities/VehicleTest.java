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
package at.uni_salzburg.cs.cpcc.vvrte.entities;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.sql.Clob;
import java.util.UUID;

import org.testng.annotations.Test;

/**
 * VehicleTest
 */
public class VehicleTest
{

    @Test
    public void shouldSetAndGetValues()
    {
        int id = 10;
        UUID uuid = UUID.randomUUID();
        Clob clob = mock(Clob.class);

        Vehicle v = new Vehicle();
        v.setId(id);
        v.setUuid(uuid.toString());
        v.setCode(clob);

        assertThat(v.getId()).isNotNull().isEqualTo(10);
        assertThat(v.getUuid()).isNotNull().isEqualTo(uuid.toString());
        assertThat(v.getCode()).isNotNull().isEqualTo(clob);
    }
}
