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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.GeometryCollection;
import org.geojson.Point;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.utils.GeoJsonUtils;
import cpcc.vvrte.entities.Task;

/**
 * Sensors contributor implementation.
 */
public class SensorsContributor implements StateContributor
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void contribute(FeatureCollection featureCollection, PolarCoordinate rvPosition, List<Task> taskList)
    {
        GeometryCollection sensorFeatures = new GeometryCollection();
        sensorFeatures.setGeometries(toSensorFeatures(taskList));

        Feature feature = new Feature();
        feature.setProperty("type", "sensors");
        feature.setGeometry(sensorFeatures);

        featureCollection.add(feature);
    }

    /**
     * @param taskList the tasks to be converted.
     * @return the tasks as a GeoJSON object.
     */
    private List<GeoJsonObject> toSensorFeatures(List<Task> taskList)
    {
        List<GeoJsonObject> featureList = new ArrayList<>();

        for (Task task : taskList)
        {
            Point point = GeoJsonUtils.toPoint(task.getPosition());

            Feature pointFeature = new Feature();
            pointFeature.setGeometry(point);
            pointFeature.setProperty("type", "rvSensor");
            pointFeature.setProperty("sensorList", toSensorList(task.getSensors()));

            featureList.add(pointFeature);
        }

        return featureList;
    }

    /**
     * @param sensorList the list of sensor definitions.
     * @return the sensor definitions as a {@code String}.
     */
    private String toSensorList(List<SensorDefinition> sensorList)
    {
        return sensorList.stream().map(x -> x.getType().name()).collect(Collectors.joining(","));
    }
}
