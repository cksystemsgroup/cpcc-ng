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
        String vehicleName = "veh01";
        String clob = "bugger that!";
        byte[] blob = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        VirtualVehicle v = new VirtualVehicle();
        v.setId(id);
        v.setUuid(uuid.toString());
        v.setName(vehicleName);
        v.setCode(clob);
        v.setContinuation(blob);

        assertThat(v.getId()).isNotNull().isEqualTo(10);
        assertThat(v.getUuid()).isNotNull().isEqualTo(uuid.toString());
        assertThat(v.getName()).isNotNull().isEqualTo(vehicleName);
        assertThat(v.getCode()).isNotNull().isEqualTo(clob);
        assertThat(v.getContinuation()).isNotNull().isEqualTo(blob);
    }
}
