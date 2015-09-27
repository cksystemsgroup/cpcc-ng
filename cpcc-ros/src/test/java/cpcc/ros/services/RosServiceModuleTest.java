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

package cpcc.ros.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;

import java.lang.reflect.Constructor;

import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.ServiceBindingOptions;
import org.testng.annotations.Test;

import cpcc.ros.services.RosImageConverter;
import cpcc.ros.services.RosImageConverterImpl;
import cpcc.ros.services.RosNodeService;
import cpcc.ros.services.RosNodeServiceImpl;
import cpcc.ros.services.RosServiceModule;

/**
 * RosServiceModuleTest
 */
public class RosServiceModuleTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<RosServiceModule> cnt = RosServiceModule.class.getDeclaredConstructor();
        assertFalse(cnt.isAccessible());
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @Test
    public void shouldBindServices()
    {
        ServiceBindingOptions options1 = mock(ServiceBindingOptions.class);
        ServiceBinder binder = mock(ServiceBinder.class);
        when(binder.bind(RosNodeService.class, RosNodeServiceImpl.class)).thenReturn(options1);
        when(binder.bind(RosImageConverter.class, RosImageConverterImpl.class)).thenReturn(null);

        RosServiceModule.bind(binder);

        verify(binder).bind(RosNodeService.class, RosNodeServiceImpl.class);
        verify(options1).eagerLoad();
        verify(binder).bind(RosImageConverter.class, RosImageConverterImpl.class);
    }

}
