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

package cpcc.commons.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.LibraryMapping;

/**
 * CommonsModule
 */
public final class CommonsModule
{
    private CommonsModule()
    {
        // Intentionally empty.
    }

    /**
     * @param binder the service binder
     */
    public static void bind(ServiceBinder binder)
    {
        binder.bind(ImageTagService.class, ImageTagServiceImpl.class);
    }

    /**
     * @param configuration the IoC configuration.
     */
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("commons", "cpcc.commons"));
    }

    /**
     * @param configuration the configuration
     */
    @SuppressWarnings("rawtypes")
    public static void contributeTranslatorAlternatesSource(MappedConfiguration<String, Translator> configuration)
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
     * @param configuration the IoC configuration.
     */
    public static void contributeComponentMessagesSource(OrderedConfiguration<String> configuration)
    {
        configuration.add("cpccCommonsMessages", "cpcc/commons/CommonsMessages");
    }
}
