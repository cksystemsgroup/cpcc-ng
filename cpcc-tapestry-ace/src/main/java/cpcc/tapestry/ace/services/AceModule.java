package cpcc.tapestry.ace.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
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

/**
 * AceModule
 */
public final class AceModule
{
    private static final String MODULE_VERSION = "module.version";
    private static final String PROPERTY_PATH = AceModule.class.getPackage().getName() + "/module.properties";
    private static final String RESOURCE_NOT_FOUND = "Property file resource not found: " + PROPERTY_PATH;
    private static final String VERSION_NOT_SET = "Property " + MODULE_VERSION
        + " is not set in resource " + PROPERTY_PATH;
    private static final String RESOURCE_FILTERING_FAILED = "Property " + MODULE_VERSION
        + " is not filtered in resource " + PROPERTY_PATH;

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
        configuration.add(getModuleVersion("ace"), "cpcc/tapestry/ace");
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
    public static void setupBaseModules(MappedConfiguration<String, Object> configuration
        , @Path(ROOT + "/ace-shim.js") Resource aceShim)
    {
        configuration.add("ace", new JavaScriptModuleConfiguration(aceShim));
    }

    /**
     * @param configuration the ordered configuration.
     * @param type the stack extension type.
     * @param paths the extension paths.
     */
    private static void add(OrderedConfiguration<StackExtension> configuration, StackExtensionType type
        , String... paths)
    {
        for (String path : paths)
        {
            int slashx = path.lastIndexOf('/');
            String id = path.substring(slashx + 1);

            configuration.add(id, new StackExtension(type, path));
        }
    }

    /**
     * @param moduleName the module name
     * @return the module name and module version.
     */
    private static String getModuleVersion(String moduleName)
    {
        try (InputStream stream = AceModule.class.getResourceAsStream("module.properties"))
        {
            Properties props = new Properties();
            props.load(stream);
            String version = props.getProperty("module.version");

            if (StringUtils.isEmpty(version))
            {
                throw new IllegalArgumentException(VERSION_NOT_SET);
            }

            if (version.startsWith("${"))
            {
                throw new IllegalArgumentException(RESOURCE_FILTERING_FAILED);
            }

            if (version.endsWith("SNAPSHOT"))
            {
                version += '-' + System.currentTimeMillis();
            }

            return moduleName + '/' + version;
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(RESOURCE_NOT_FOUND);
        }
    }
}
