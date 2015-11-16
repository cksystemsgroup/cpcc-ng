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

/**
 * Application Version Utilities
 */
public class VersionUtils
{
    private static final String UNKNOWN_VERSION = "0.0.0-UNKNOWN";
    private static final String PROP_APPLICATION_VERSION = "application.version";

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
}
