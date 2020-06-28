package cpcc.tapestry.ace.services;

import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.Resource;
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
 * AceModule
 */
public final class AceModule
{
    private static final String PROPERTY_PATH =
        AceModule.class.getPackage().getName().replace('.', '/') + "/module.properties";

    private static final String ROOT = "classpath:META-INF/assets/ace";

    private AceModule()
    {
        // intentional empty.
    }

    /**
     * @param configuration the library mapping configuration.
     */
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("ace", "cpcc.tapestry.ace"));
    }

    /**
     * @param configuration the mapped configuration.
     */
    public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration)
    {
        configuration.add(VersionUtils.getModuleVersion("ace", PROPERTY_PATH), "cpcc/tapestry/ace");
    }

    /**
     * @param configuration the ordered configuration.
     */
    @Contribute(JavaScriptStack.class)
    @Core
    public static void setupAce(OrderedConfiguration<StackExtension> configuration)
    {
        configuration.add("ace-library", StackExtension.library(ROOT + "/ace.js"));
        add(configuration, StackExtensionType.MODULE, "ace");
    }

    /**
     * @param configuration the mapped configuration.
     * @param aceShim the shim resource.
     */
    @Contribute(ModuleManager.class)
    public static void setupBaseModules(MappedConfiguration<String, Object> configuration,
        @Path(ROOT + "/ace-shim.js") Resource aceShim)
    {
        configuration.add("ace", new JavaScriptModuleConfiguration(aceShim));
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
