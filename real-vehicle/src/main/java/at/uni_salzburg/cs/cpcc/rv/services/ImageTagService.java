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
package at.uni_salzburg.cs.cpcc.rv.services;

import java.awt.Dimension;

import at.uni_salzburg.cs.cpcc.ros.base.AbstractRosAdapter;

/**
 * ImageTagService
 */
public interface ImageTagService
{
    /**
     * @param adapter the ROS adapter.
     * @return the image tag.
     */
    String getRosImageTag(AbstractRosAdapter adapter);

    /**
     * @param adapter the ROS adapter.
     * @return the image dimensions.
     */
    Dimension getRosImageDimension(AbstractRosAdapter adapter);
}
