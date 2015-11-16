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

package cpcc.core.base;

/**
 * Core constant definitions.
 */
public final class CoreConstants
{
    public static final String PROP_MAX_JOB_AGE = "core.max.job.age";
    public static final String DEFAULT_MAX_JOB_AGE = "3600000";

    public static final String PROP_LIQUIBASE_CHANGE_LOG_FILE = "liquibase.changeLogFile";
    public static final String DEFAULT_LIQUIBASE_CHANGE_LOG_FILE = "dbchange/update.xml";

    public static final String PROP_LIQUIBASE_DATABASE_URL = "liquibase.database.url";
    public static final String DEFAULT_LIQUIBASE_DATABASE_URL = "java:/comp/env/jdbc/LIQUIBASE";
    
    private CoreConstants()
    {
        // Intentionally left empty.
    }
}
