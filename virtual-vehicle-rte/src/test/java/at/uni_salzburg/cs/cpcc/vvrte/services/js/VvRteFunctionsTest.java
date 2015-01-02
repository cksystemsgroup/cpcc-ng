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

package at.uni_salzburg.cs.cpcc.vvrte.services.js;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertFalse;

import java.io.PrintStream;
import java.lang.reflect.Constructor;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * VvRteFunctionsTest
 */
@Test(singleThreaded = true)
public class VvRteFunctionsTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<VvRteFunctions> cnt = VvRteFunctions.class.getDeclaredConstructor();
        assertFalse(cnt.isAccessible());
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @DataProvider
    public Object[][] vvRteDataProvider()
    {
        return new Object[][]{
            new Object[]{mock(BuiltInFunctions.class)},
            new Object[]{mock(BuiltInFunctions.class)},
            new Object[]{mock(BuiltInFunctions.class)},
        };
    }

    @Test(dataProvider = "vvRteDataProvider")
    public void shouldStoreVvRte(BuiltInFunctions vvRte)
    {
        VvRteFunctions.setVvRte(vvRte);
        assertThat(VvRteFunctions.getVvRte()).isNotNull().isEqualTo(vvRte);
    }
    
    
    @DataProvider
    public Object[][] stdOutDataProvider()
    {
        return new Object[][]{
            new Object[]{mock(PrintStream.class)},
            new Object[]{mock(PrintStream.class)},
            new Object[]{mock(PrintStream.class)},
        };
    }

    @Test(dataProvider = "stdOutDataProvider")
    public void shouldStoreVvRte(PrintStream stdOut)
    {
        VvRteFunctions.setStdOut(stdOut);
        assertThat(VvRteFunctions.getStdOut()).isNotNull().isEqualTo(stdOut);
    }
    
}
