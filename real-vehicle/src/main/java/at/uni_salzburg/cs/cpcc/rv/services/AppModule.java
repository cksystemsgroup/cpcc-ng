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
package at.uni_salzburg.cs.cpcc.rv.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import at.uni_salzburg.cs.cpcc.persistence.db.QueryManager;
import at.uni_salzburg.cs.cpcc.persistence.db.QueryManagerImpl;
import at.uni_salzburg.cs.cpcc.ros.services.RosImageConverter;
import at.uni_salzburg.cs.cpcc.ros.services.RosImageConverterImpl;
import at.uni_salzburg.cs.cpcc.ros.services.RosNodeService;
import at.uni_salzburg.cs.cpcc.ros.services.RosNodeServiceImpl;
import at.uni_salzburg.cs.cpcc.rv.services.image.ImageTagService;
import at.uni_salzburg.cs.cpcc.rv.services.image.ImageTagServiceImpl;
import at.uni_salzburg.cs.cpcc.rv.services.ros.GraphNameTranslator;
import at.uni_salzburg.cs.cpcc.utilities.opts.OptionsParserService;
import at.uni_salzburg.cs.cpcc.utilities.opts.OptionsParserServiceImpl;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to configure and extend
 * Tapestry, or to place your own service definitions.
 */
public final class AppModule
{
    private AppModule()
    {
        // intentionally empty.
    }

    /**
     * @param binder the service binder
     */
    public static void bind(ServiceBinder binder)
    {
        binder.bind(ImageTagService.class, ImageTagServiceImpl.class);
        binder.bind(OptionsParserService.class, OptionsParserServiceImpl.class);
        binder.bind(QueryManager.class, QueryManagerImpl.class).eagerLoad();
        binder.bind(RosNodeService.class, RosNodeServiceImpl.class).eagerLoad();
        binder.bind(RosImageConverter.class, RosImageConverterImpl.class);
    }

    /**
     * @param configuration the application configuration.
     */
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration)
    {
        // The application version number is incorporated into URLs for some
        // assets. Web browsers will cache assets because of the far future expires
        // header. If existing assets are changed, the version number should also
        // change, to force the browser to download new versions. This overrides Tapesty's default
        // (a random hexadecimal number), but may be further overridden by DevelopmentModule or
        // QaModule.
        configuration.override(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
    }

    /**
     * @param configuration the application configuration.
     */
    public static void contributeApplicationDefaults(MappedConfiguration<String, Object> configuration)
    {
        // Contributions to ApplicationDefaults will override any contributions to
        // FactoryDefaults (with the same key). Here we're restricting the supported
        // locales to just "en" (English). As you add localized message catalogs and other assets,
        // you can extend this list of locales (it's a comma separated series of locale names;
        // the first locale name is the default when there's no reasonable match).
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,de");
        configuration.add(SymbolConstants.MINIFICATION_ENABLED, "false");
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
}
