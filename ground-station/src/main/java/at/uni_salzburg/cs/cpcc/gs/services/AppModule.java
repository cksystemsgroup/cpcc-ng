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
package at.uni_salzburg.cs.cpcc.gs.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.javascript.JavaScriptStack;

import com.trsvax.bootstrap.environment.TableEnvironment;
import com.trsvax.bootstrap.environment.TableValues;
import com.trsvax.bootstrap.services.EnvironmentSetup;

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
        // Intentional empty.
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
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,de");
        configuration.add(SymbolConstants.MINIFICATION_ENABLED, "false");
        configuration.add(SymbolConstants.HMAC_PASSPHRASE, "Eith6Du9reeSa7aiaiweaM7oaCh6quae");
    }
    
    /**
     * @param configuration the mapped configuration
     */
    public static void contributeJavaScriptStackSource(MappedConfiguration<String, JavaScriptStack> configuration)
    {
        configuration.addInstance("RealVehicle", RealVehicleStack.class);
    }
    
    /**
     * @param configuration the configuration
     * @param threadLocale the locale
     */
    @SuppressWarnings("rawtypes")
    public static void contributeTranslatorAlternatesSource(MappedConfiguration<String, Translator> configuration,
        ThreadLocale threadLocale)
    {
    }

    /**
     * @param configuration the application configuration.
     */
    @Contribute(EnvironmentSetup.class)
    public static void provideEnvironmentSetup(MappedConfiguration<Class<?>, Object> configuration)
    {
        configuration.override(TableEnvironment.class, new TableValues(null).withType("table"));
    }
}
