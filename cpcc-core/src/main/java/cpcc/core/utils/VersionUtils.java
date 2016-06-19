// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * Application Version Utilities
 */
public final class VersionUtils
{
    private static final String UNKNOWN_VERSION = "0.0.0-UNKNOWN";
    private static final String MODULE_VERSION = "module.version";
    private static final String PROP_APPLICATION_VERSION = "application.version";
    private static final String VERSION_NOT_SET = "Property " + MODULE_VERSION + " is not set in resource %s";
    private static final String RESOURCE_FILTERING_FAILED = "Property " + MODULE_VERSION
        + " is not filtered in resource %s";
    private static final String RESOURCE_NOT_FOUND = "Property file resource not found: %s";

    private VersionUtils()
    {
        // Intentionally empty.
    }

    /**
     * @param resourceName the name of the resource to load for the version property.
     * @return the estimated version.
     */
    public static String getVersion(String resourceName)
    {
        InputStream propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);

        if (propStream == null)
        {
            return UNKNOWN_VERSION;
        }

        try
        {
            Properties props = new Properties();
            props.load(propStream);
            return props.getProperty(PROP_APPLICATION_VERSION, UNKNOWN_VERSION);
        }
        catch (IOException e)
        {
            return UNKNOWN_VERSION;
        }
    }

    /**
     * @param moduleName the module name
     * @return the module name and module version.
     */
    public static String getModuleVersion(String moduleName, String propertyFile)
    {
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFile))
        {
            Properties props = new Properties();
            props.load(stream);
            String version = props.getProperty("module.version");

            if (StringUtils.isEmpty(version))
            {
                throw new IllegalArgumentException(String.format(VERSION_NOT_SET, propertyFile));
            }

            if (version.startsWith("${"))
            {
                throw new IllegalArgumentException(String.format(RESOURCE_FILTERING_FAILED, propertyFile));
            }

            if (version.endsWith("SNAPSHOT"))
            {
                version += '-' + System.currentTimeMillis();
            }

            return moduleName + '/' + version;
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(String.format(RESOURCE_NOT_FOUND, propertyFile));
        }
    }
}
