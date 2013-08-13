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
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;

import at.uni_salzburg.cs.cpcc.ros.services.RosQueryService;
import at.uni_salzburg.cs.cpcc.ros.services.RosQueryServiceImpl;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
public final class AppModule
{
    private AppModule()
    {
        // TODO Auto-generated constructor stub
    }
    
    /**
     * @param binder the service binder
     */
    public static void bind(ServiceBinder binder)
    {
        // binder.bind(MyServiceInterface.class, MyServiceImpl.class);

        // Make bind() calls on the binder object to define most IoC services.
        // Use service builder methods (example below) when the implementation
        // is provided inline, or requires more initialization than simply
        // invoking the constructor.
        binder.bind(RosQueryService.class, RosQueryServiceImpl.class);
        
    }

    /**
     * @param configuration the application configuration.
     */
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration)
    {
        // The application version number is incorprated into URLs for some
        // assets. Web browsers will cache assets because of the far future expires
        // header. If existing assets are changed, the version number should also
        // change, to force the browser to download new versions. This overrides Tapesty's default
        // (a random hexadecimal number), but may be further overriden by DevelopmentModule or
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
        // locales to just "en" (English). As you add localised message catalogs and other assets,
        // you can extend this list of locales (it's a comma separated series of locale names;
        // the first locale name is the default when there's no reasonable match).
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");
    }


    /**
     * This is a service definition, the service will be named "TimingFilter". The interface,
     * RequestFilter, is used within the RequestHandler service pipeline, which is built from the
     * RequestHandler service configuration. Tapestry IoC is responsible for passing in an
     * appropriate Logger instance. Requests for static resources are handled at a higher level, so
     * this filter will only be invoked for Tapestry related requests.
     * <p/>
     * <p/>
     * Service builder methods are useful when the implementation is inline as an inner class
     * (as here) or require some other kind of special initialization. In most cases,
     * use the static bind() method instead.
     * <p/>
     * <p/>
     * If this method was named "build", then the service id would be taken from the
     * service interface and would be "RequestFilter".  Since Tapestry already defines
     * a service named "RequestFilter" we use an explicit service id that we can reference
     * inside the contribution method.
     *
     * @param log the current logger
     * @return the newly created request filter.
     */
//    public RequestFilter buildTimingFilter(final Logger log)
//    {
//        return new RequestFilter()
//        {
//            /**
//             * {@inheritDoc}
//             */
//            @Override
//            public boolean service(Request request, Response response, RequestHandler handler)
//                throws IOException
//            {
//                long startTime = System.currentTimeMillis();
//
//                try
//                {
//                    // The responsibility of a filter is to invoke the corresponding method
//                    // in the handler. When you chain multiple filters together, each filter
//                    // received a handler that is a bridge to the next filter.
//
//                    return handler.service(request, response);
//                }
//                finally
//                {
//                    long elapsed = System.currentTimeMillis() - startTime;
//
//                    log.info(String.format("Request time: %d ms", elapsed));
//                }
//            }
//        };
//    }

    /**
     * This is a contribution to the RequestHandler service configuration. This is how we extend
     * Tapestry using the timing filter. A common use for this kind of filter is transaction
     * management or security. The @Local annotation selects the desired service by type, but only
     * from the same module.  Without @Local, there would be an error due to the other service(s)
     * that implement RequestFilter (defined in other modules).
     */
    /**
     * @param configuration the application configuration.
     * @param filter the request filter.
     */
//    public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration,
//                                         @Local
//                                         RequestFilter filter)
//    {
//        // Each contribution to an ordered configuration has a name, When necessary, you may
//        // set constraints to precisely control the invocation order of the contributed filter
//        // within the pipeline.
//
//        configuration.add("Timing", filter);
//    }
}
