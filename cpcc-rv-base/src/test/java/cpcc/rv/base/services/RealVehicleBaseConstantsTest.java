// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.rv.base.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

/**
 * RealVehicleBaseConstantsTest implementation.
 */
public class RealVehicleBaseConstantsTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<RealVehicleBaseConstants> cnt = RealVehicleBaseConstants.class.getDeclaredConstructor();
        assertThat(cnt.isAccessible()).isFalse();
        cnt.setAccessible(true);
        cnt.newInstance();
    }
}
