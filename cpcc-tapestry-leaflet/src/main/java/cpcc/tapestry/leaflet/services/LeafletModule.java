package cpcc.tapestry.leaflet.services;

import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.commons.Configuration;
import org.apache.tapestry5.commons.MappedConfiguration;
import org.apache.tapestry5.commons.OrderedConfiguration;
import org.apache.tapestry5.commons.Resource;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.services.Core;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.javascript.JavaScriptModuleConfiguration;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.ModuleManager;
import org.apache.tapestry5.services.javascript.StackExtension;
import org.apache.tapestry5.services.javascript.StackExtensionType;

import cpcc.core.utils.VersionUtils;

/**
 * LeafletModule
 */
public final class LeafletModule
{
    private static final String LEAFLET = "leaflet";

    private static final String PROPERTY_PATH =
        LeafletModule.class.getPackage().getName().replace('.', '/') + "/module.properties";

    private static final String ROOT = "classpath:META-INF/assets/leaflet";

    private LeafletModule()
    {
        // Intentionally empty.
    }

    /**
     * @param configuration the library mapping configuration.
     */
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping(LEAFLET, "cpcc.tapestry.leaflet"));
    }

    /**
     * @param configuration the mapped configuration.
     */
    public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration)
    {
        configuration.add(VersionUtils.getModuleVersion(LEAFLET, PROPERTY_PATH), "cpcc/tapestry/leaflet");
    }

    /**
     * @param configuration the ordered configuration.
     */
    @Contribute(JavaScriptStack.class)
    @Core
    public static void setupLeaflet(OrderedConfiguration<StackExtension> configuration)
    {
        configuration.add("leaflet-library", StackExtension.library(ROOT + "/leaflet.js"));
        add(configuration, StackExtensionType.MODULE, LEAFLET);
    }

    /**
     * @param configuration the mapped configuration.
     * @param leafletShim the shim resource.
     */
    @Contribute(ModuleManager.class)
    public static void setupBaseModules(MappedConfiguration<String, Object> configuration,
        @Path(ROOT + "/leaflet-shim.js") Resource leafletShim)
    {
        configuration.add(LEAFLET, new JavaScriptModuleConfiguration(leafletShim).dependsOn("t5/core/console"));
    }

    /**
     * @param configuration the ordered configuration.
     * @param type the stack extension type.
     * @param paths the extension paths.
     */
    private static void add(OrderedConfiguration<StackExtension> configuration, StackExtensionType type,
        String... paths)
    {
        for (String path : paths)
        {
            int slashx = path.lastIndexOf('/');
            String id = path.substring(slashx + 1);

            configuration.add(id, new StackExtension(type, path));
        }
    }
}
