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
package at.uni_salzburg.cs.cpcc.vvrte.services;

/**
 * VehicleLauncherException
 */
public class VehicleLaunchException extends Exception
{
    private static final long serialVersionUID = -7375122526778864222L;

    /**
     * Constructs a new exception with null as its detail message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@code initCause}.
     */
    public VehicleLaunchException()
    {
        super();
    }

    /**
     * @param message the detail message
     */
    public VehicleLaunchException(String message)
    {
        super(message);
    }

    /**
     * @param cause the cause. A null value is permitted, and indicates that the cause is nonexistent or unknown.
     */
    public VehicleLaunchException(Throwable cause)
    {
        super(cause);
    }

    /**
     * @param message the detail message
     * @param cause the cause. A null value is permitted, and indicates that the cause is nonexistent or unknown.
     */
    public VehicleLaunchException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
