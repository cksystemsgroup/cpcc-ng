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

package at.uni_salzburg.cs.cpcc.core.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;

import at.uni_salzburg.cs.cpcc.core.services.opts.OptionsParserService;
import at.uni_salzburg.cs.cpcc.core.services.opts.OptionsParserServiceImpl;

/**
 * PersistenceModule
 */
public final class CoreModule
{
    private CoreModule()
    {
        // Intentionally empty.
    }

    /**
     * @param binder the service binder
     */
    public static void bind(ServiceBinder binder)
    {
        binder.bind(QueryManager.class, QueryManagerImpl.class).eagerLoad();
        binder.bind(CoreJsonConverter.class, CoreJsonConverterImpl.class);
        binder.bind(CoreGeoJsonConverter.class, CoreGeoJsonConverterImpl.class);
        binder.bind(OptionsParserService.class, OptionsParserServiceImpl.class);
    }

    /**
     * @param configuration the IoC configuration.
     */
    public static void contributeHibernateEntityPackageManager(Configuration<String> configuration)
    {
        configuration.add("at.uni_salzburg.cs.cpcc.core.entities");
    }
}
