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

package at.uni_salzburg.cs.cpcc.commons.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.ioc.services.cron.CronSchedule;
import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor;
import org.apache.tapestry5.services.LibraryMapping;

import at.uni_salzburg.cs.cpcc.commons.services.image.ImageTagService;
import at.uni_salzburg.cs.cpcc.commons.services.image.ImageTagServiceImpl;
import at.uni_salzburg.cs.cpcc.commons.services.ros.GraphNameTranslator;

/**
 * CommonsModule
 */
public final class CommonsModule
{
    private CommonsModule()
    {
        // intentionally empty.
    }

    /**
     * @param binder the service binder
     */
    public static void bind(ServiceBinder binder)
    {
        binder.bind(ImageTagService.class, ImageTagServiceImpl.class);
        binder.bind(StorageContentTagService.class, StorageContentTagServiceImpl.class);
        binder.bind(RealVehicleStateService.class, RealVehicleStateServiceImpl.class).eagerLoad();
        binder.bind(ConfigurationSynchronizer.class, ConfigurationSynchronizerImpl.class).eagerLoad();
    }

    /**
     * @param configuration the IoC configuration.
     */
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("commons", "at.uni_salzburg.cs.cpcc.commons"));
    }

    /**
     * @param configuration the configuration
     * @param threadLocale the locale
     */
    @SuppressWarnings("rawtypes")
    public static void contributeTranslatorAlternatesSource(MappedConfiguration<String, Translator> configuration,
        ThreadLocale threadLocale)
    {
        configuration.add("graphName", new GraphNameTranslator("graphName"));
        configuration.add("uri", new UriTranslator("uri"));
    }

    /**
     * @param configuration the configuration.
     */
    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");
    }

    /**
     * @param executor the periodic executor service.
     * @param importService the data import service.
     */
    @Startup
    public static void scheduleJobs(PeriodicExecutor executor, final RealVehicleStateService rvStateService
        , final ConfigurationSynchronizer configSyncService)
    {
        executor.addJob(new CronSchedule("0 0/2 * * * ?"), "Real Vehicle status update", new Runnable()
        {
            @Override
            public void run()
            {
                rvStateService.realVehicleStatusUpdate();
            }
        });

        // TODO check cycle time!
        executor.addJob(new CronSchedule("2 * * * * ?"), "Synchronize Configuration", new Runnable()
        {
            @Override
            public void run()
            {
                configSyncService.synchronizeConfiguration();
            }
        });
    }
}
