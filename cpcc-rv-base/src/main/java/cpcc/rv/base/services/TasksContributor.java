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

package cpcc.rv.base.services;

import java.util.List;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;
import org.geojson.LngLatAlt;

import cpcc.core.utils.PolarCoordinate;
import cpcc.vvrte.task.Task;

/**
 * Tasks contributor implementation.
 */
public class TasksContributor implements StateContributor
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void contribute(FeatureCollection featureCollection, PolarCoordinate rvPosition, List<Task> taskList)
    {
        featureCollection.add(toTaskFeature(rvPosition, taskList));
    }

    /**
     * @param position the current real vehicle position.
     * @param taskList the tasks to be converted.
     * @return the tasks as a GeoJSON Feature collection.
     */
    public Feature toTaskFeature(PolarCoordinate position, List<Task> taskList)
    {
        LngLatAlt pos = new LngLatAlt(position.getLongitude(), position.getLatitude(), position.getAltitude());
        LineString lineString = new LineString(pos);

        for (Task task : taskList)
        {
            lineString.add(new LngLatAlt(task.getLongitude(), task.getLatitude(), task.getAltitude()));
        }

        Feature lineStringFeature = new Feature();
        lineStringFeature.setGeometry(lineString);
        lineStringFeature.setProperty("type", "rvPath");

        return lineStringFeature;
    }
}
