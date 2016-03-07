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

package cpcc.vvrte.entities;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * VehicleState
 */
public enum VirtualVehicleState
{
    INIT
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public VirtualVehicleState traverse(VirtualVehicleState newState)
        {
            switch (newState)
            {
                case RUNNING:
                    return RUNNING;
                default:
                    return null;
            }
        }
    },

    RUNNING
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public VirtualVehicleState traverse(VirtualVehicleState newState)
        {
            switch (newState)
            {
                case FINISHED:
                    return FINISHED;
                case WAITING:
                    return WAITING;
                default:
                    return null;
            }
        }
    },

    WAITING
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public VirtualVehicleState traverse(VirtualVehicleState newState)
        {
            switch (newState)
            {
                case RUNNING:
                    return RUNNING;
                case MIGRATING:
                    return MIGRATING;
                default:
                    return null;
            }
        }
    },

    MIGRATION_AWAITED
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public VirtualVehicleState traverse(VirtualVehicleState newState)
        {
            switch (newState)
            {
                case MIGRATING:
                    return MIGRATING;
                default:
                    return null;
            }
        }
    },

    MIGRATING
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public VirtualVehicleState traverse(VirtualVehicleState newState)
        {
            switch (newState)
            {
                case MIGRATION_INTERRUPTED:
                    return MIGRATION_INTERRUPTED;
                case WAITING:
                    return WAITING;
                default:
                    return null;
            }
        }
    },

    MIGRATION_INTERRUPTED
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public VirtualVehicleState traverse(VirtualVehicleState newState)
        {
            switch (newState)
            {
                case MIGRATION_AWAITED:
                    return MIGRATION_AWAITED;
                case WAITING:
                    return WAITING;
                default:
                    return null;
            }
        }
    },

    MIGRATION_COMPLETED
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public VirtualVehicleState traverse(VirtualVehicleState newState)
        {
            return null;
        }
    },

    FINISHED
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public VirtualVehicleState traverse(VirtualVehicleState newState)
        {
            return null;
        }
    },

    INTERRUPTED
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public VirtualVehicleState traverse(VirtualVehicleState newState)
        {
            return newState;
        }
    },

    TASK_COMPLETION_AWAITED
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public VirtualVehicleState traverse(VirtualVehicleState newState)
        {
            return newState;
        }
    },

    DEFECTIVE
    {
        @Override
        public VirtualVehicleState traverse(VirtualVehicleState newState)
        {
            return null;
        }
    };

    public static final Set<VirtualVehicleState> VV_STATES_FOR_DELETE = Stream.of(
        DEFECTIVE, FINISHED, INIT, INTERRUPTED, TASK_COMPLETION_AWAITED, MIGRATION_COMPLETED, MIGRATION_INTERRUPTED
        ).collect(Collectors.toSet());

    public static final Set<VirtualVehicleState> VV_STATES_FOR_EDIT = Stream.of(
        DEFECTIVE, FINISHED, INIT
        ).collect(Collectors.toSet());

    public static final Set<VirtualVehicleState> VV_STATES_FOR_START = Stream.of(
        INIT
        ).collect(Collectors.toSet());

    public static final Set<VirtualVehicleState> VV_STATES_FOR_STOP = Stream.of(
        DEFECTIVE, INTERRUPTED, TASK_COMPLETION_AWAITED, MIGRATION_INTERRUPTED
        ).collect(Collectors.toSet());

    public static final Set<VirtualVehicleState> VV_STATES_FOR_RESTART = Stream.of(
        INIT, DEFECTIVE, FINISHED, INTERRUPTED, TASK_COMPLETION_AWAITED, MIGRATION_COMPLETED, MIGRATION_INTERRUPTED
        ).collect(Collectors.toSet());

    public static final Set<VirtualVehicleState> VV_STATES_FOR_RESTART_MIGRATION_FROM_RV = Stream.of(
        DEFECTIVE, FINISHED, INTERRUPTED, MIGRATION_AWAITED, MIGRATION_INTERRUPTED
        ).collect(Collectors.toSet());

    public static final Set<VirtualVehicleState> VV_STATES_FOR_RESTART_STUCK_MIGRATION_FROM_RV = Stream.of(
        INIT, DEFECTIVE, FINISHED, INTERRUPTED, MIGRATION_INTERRUPTED
        ).collect(Collectors.toSet());

    public static final Set<VirtualVehicleState> VV_STATES_FOR_RESTART_STUCK_MIGRATION_FROM_GS = Stream.of(
        MIGRATION_INTERRUPTED
        ).collect(Collectors.toSet());

    public static final Set<VirtualVehicleState> VV_NO_CHANGE_AFTER_MIGRATION = Stream.of(
        DEFECTIVE, FINISHED
        ).collect(Collectors.toSet());

    public static final Set<VirtualVehicleState> VV_STATES_FOR_MIGRATION = Stream.of(
        MIGRATING, MIGRATION_INTERRUPTED
        ).collect(Collectors.toSet());

    /**
     * @param newState the new state to traverse to.
     * @return the new state, if traversal is possible and null otherwise.
     */
    public abstract VirtualVehicleState traverse(VirtualVehicleState newState);

    /**
     * @param newState the new state to traverse to.
     * @return true if traversal is possible and false otherwise.
     */
    public boolean canTraverseTo(VirtualVehicleState newState)
    {
        return traverse(newState) != null;
    }
}
