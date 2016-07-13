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

    public static final String MIGRATION_ACK_CONNECTOR = "migration-ack";
    public static final String MIGRATION_ACK_PATH = "/commons/vv/migrationAck";

    public static final String PROP_SCHEDULER_CLASS_NAME = "vvrte.default.scheduler";
    // public static final String PROP_DEFAULT_SCHEDULER_CLASS_NAME =
    //  "cpcc.vvrte.services.task.FirstComeFirstServeAlgorithm";
    public static final String PROP_SCHEDULER_CLASS_NAME_DEFAULT =
        "cpcc.vvrte.services.task.GatedTspSchedulingAlgorithm";

    public static final String PROP_GTSP_MAX_TASKS = "cpcc.vv-rte.gtsp-max-tasks";
    public static final String PROP_GTSP_MAX_TASKS_DEFAULT = "30";

    public static final String PROP_MIN_TOLERANCE_DISTANCE = "cpcc.vvrte.task.minimumToleranceDistance";
    public static final String PROP_MIN_TOLERANCE_DISTANCE_DEFAULT = "3.0";

    public static final String MIGRATION_JOB_QUEUE_NAME = "VV Migrations";
    public static final String NUMBER_OF_MIGRATION_POOL_THREADS = "vvrte.mig.job.pool.threads";
    public static final String NUMBER_OF_MIGRATION_POOL_THREADS_DEFAULT = "5";

    public static final String MIGRATION_CHUNK_SIZE = "vvrte.migration.chunk.size";
    public static final String MIGRATION_CHUNK_SIZE_DEFAULT = "1000";

    public static final String MIGRATION_MODE_SEND = "migration-send";
    public static final String MIGRATION_FORMAT_SEND = "mode=" + MIGRATION_MODE_SEND + ",id=%d";

    public static final String MIGRATION_MODE_SEND_ACK = "migration-send-ack";
    public static final String MIGRATION_FORMAT_SEND_ACK = "mode=" + MIGRATION_MODE_SEND_ACK + ",id=%d";

    public static final String MIGRATION_MODE_RECEIVE = "migration-receive";
    public static final String MIGRATION_RECEIVE = "mode=" + MIGRATION_MODE_RECEIVE;

    public static final String MIGRATION_CONTINUE = "mode=" + MIGRATION_MODE_SEND + ",id=0";

    public static final String STUCK_MIGRATIONS_MODE = "stuck-migrations";
    public static final String STUCK_MIGRATIONS = "mode=" + STUCK_MIGRATIONS_MODE;

    private VvRteConstants()
    {
        // Intentionally empty.
    }
}
