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
package at.uni_salzburg.cs.cpcc.vvrte.entities;

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

    DEFECTIVE
    {
        @Override
        public VirtualVehicleState traverse(VirtualVehicleState newState)
        {
            return null;
        }
    };

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
