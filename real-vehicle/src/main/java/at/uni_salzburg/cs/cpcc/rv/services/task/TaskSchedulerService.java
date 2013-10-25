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
package at.uni_salzburg.cs.cpcc.rv.services.task;

import java.lang.reflect.InvocationTargetException;

/**
 * TaskSchedulerService
 */
public interface TaskSchedulerService extends TaskSchedulingAlgorithm
{
    /**
     * @param className the class name of the scheduling algorithm.
     * @throws ClassNotFoundException in case of errors.
     * @throws NoSuchMethodException in case of errors.
     * @throws SecurityException in case of errors.
     * @throws InstantiationException in case of errors.
     * @throws IllegalAccessException in case of errors.
     * @throws IllegalArgumentException in case of errors.
     * @throws InvocationTargetException in case of errors.
     */
    void setAlgorithm(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
        InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}
