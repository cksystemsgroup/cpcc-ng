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
public enum VehicleState
{
    INIT
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public VehicleState traverse(VehicleState newState)
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
        public VehicleState traverse(VehicleState newState)
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
        public VehicleState traverse(VehicleState newState)
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

    MIGRATING
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public VehicleState traverse(VehicleState newState)
        {
            switch (newState)
            {
                case WAITING:
                    return WAITING;
                default:
                    return null;
            }
        }
    },

    FINISHED
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public VehicleState traverse(VehicleState newState)
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
        public VehicleState traverse(VehicleState newState)
        {
            return newState;
        }
    },
    
    DEFECTIVE
    {
        @Override
        public VehicleState traverse(VehicleState newState)
        {
            return null;
        }
    };

    /**
     * @param newState the new state to traverse to.
     * @return the new state, if traversal is possible and null otherwise.
     */
    public abstract VehicleState traverse(VehicleState newState);

    /**
     * @param newState the new state to traverse to.
     * @return true if traversal is possible and false otherwise.
     */
    public boolean canTraverseTo(VehicleState newState)
    {
        return traverse(newState) != null;
    }
}
