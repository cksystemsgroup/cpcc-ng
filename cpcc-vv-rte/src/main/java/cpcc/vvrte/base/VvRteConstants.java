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

package cpcc.vvrte.base;

/**
 * Virtual vehicle runtime environment constants.
 */
public final class VvRteConstants
{
    public static final String MIGRATION_CONNECTOR = "migration";
    public static final String MIGRATION_PATH = "/commons/vv/migration";

    public static final String PROP_DEFAULT_SCHEDULER = "vvrte.default.scheduler";
    // public static final String PROP_DEFAULT_SCHEDULER_CLASS_NAME =
    //  "cpcc.vvrte.services.task.FirstComeFirstServeAlgorithm";
    public static final String PROP_DEFAULT_SCHEDULER_CLASS_NAME =
        "cpcc.vvrte.services.task.GatedTspSchedulingAlgorithm";

    public static final String PROP_MIN_TOLERANCE_DISTANCE = "cpcc.vvrte.task.minimumToleranceDistance";
    public static final String PROP_MIN_TOLERANCE_DISTANCE_DEFAULT = "3.0";

    private VvRteConstants()
    {
        // Intentionally empty.
    }
}
