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

package at.uni_salzburg.cs.cpcc.vvrte.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.LngLatAlt;
import org.geojson.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.core.entities.Parameter;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.vvrte.task.Task;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * VirtualVehicleMapperImpl
 */
public class VirtualVehicleMapperImpl implements VirtualVehicleMapper
{
    private static final Logger LOG = LoggerFactory.getLogger(VirtualVehicleMapperImpl.class);

    private QueryManager qm;
    private String rvName;
    private Map<String, RealVehicle> realVehicleMap;
    private Map<String, PolygonZone> areaOfOperationMap;

    /**
     * @param qm the query manager.
     * @throws JsonParseException thrown in case of errors.
     * @throws JsonMappingException thrown in case of errors.
     * @throws IOException thrown in case of errors.
     */
    public VirtualVehicleMapperImpl(QueryManager qm) throws JsonParseException, JsonMappingException, IOException
    {
        this.qm = qm;
        refresh();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refresh()
    {
        Parameter rvNameParam = qm.findParameterByName(Parameter.REAL_VEHICLE_NAME);
        rvName = rvNameParam != null ? rvNameParam.getValue() : null;

        Map<String, RealVehicle> rvMap = new HashMap<String, RealVehicle>();
        Map<String, PolygonZone> zoneMap = new HashMap<String, PolygonZone>();

        List<RealVehicle> realVehicleList = qm.findAllRealVehicles();

        for (RealVehicle realVehicle : realVehicleList)
        {
            String areaOfOperationString = realVehicle.getAreaOfOperation();

            // TODO use GeoJSON !
            //            JSONArray polygon = new JSONArray(areaOfOperationString);
            //
            //            PolarCoordinate[] coordinates = new PolarCoordinate[polygon.length()];
            //            for (int k = 0, l = polygon.length(); k < l; ++k)
            //            {
            //                JSONObject point = (JSONObject) polygon.get(k);
            //                double lat = point.getDouble("lat");
            //                double lng = point.getDouble("lng");
            //                coordinates[k] = new PolarCoordinate(lat, lng, 0.0);
            //            }

            try
            {
                FeatureCollection fc = new ObjectMapper().readValue(areaOfOperationString, FeatureCollection.class);

                for (Feature feature : fc.getFeatures())
                {
                    // String fType = feature.getProperty("type");  // TODO check for aoo ?

                    GeoJsonObject geom = feature.getGeometry();
                    if (geom instanceof Polygon)
                    {
                        Polygon poly = (Polygon) geom;
                        List<LngLatAlt> coordinates = poly.getCoordinates().get(0);
                        PolygonZone areaOfOperation = new PolygonZone(coordinates);
                        zoneMap.put(realVehicle.getName(), areaOfOperation);
                        break;
                    }
                }
            }
            catch (IOException e)
            {
                LOG.error("Can not parse Area Of Operations of RV " + realVehicle.getName()
                    + "(" + realVehicle.getId() + ")", e);
            }

            rvMap.put(realVehicle.getName(), realVehicle);
        }

        realVehicleMap = rvMap;
        areaOfOperationMap = zoneMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicleMappingDecision findMappingDecision(Task task)
    {
        VirtualVehicleMappingDecision decision = new VirtualVehicleMappingDecision();
        decision.setTask(task);

        boolean migration = true;

        if (areaOfOperationMap.containsKey(rvName))
        {
            migration = !areaOfOperationMap.get(rvName).isInside(task.getPosition());
        }

        if (!migration && realVehicleMap.containsKey(rvName))
        {
            RealVehicle realVehicle = realVehicleMap.get(rvName);
            migration = !realVehicle.getSensors().containsAll(task.getSensors());
        }

        decision.setMigration(migration);

        if (migration)
        {
            List<RealVehicle> destinationRealVehicles = new ArrayList<RealVehicle>();
            for (Entry<String, RealVehicle> entry : realVehicleMap.entrySet())
            {
                PolygonZone areaOfOperation = areaOfOperationMap.get(entry.getKey());
                if (areaOfOperation == null || !areaOfOperation.isInside(task.getPosition()))
                {
                    LOG.info("Migrate not to " + entry.getValue().getName()
                        + " because of position " + task.getPosition());
                    continue;
                }

                if (!entry.getValue().getSensors().containsAll(task.getSensors()))
                {

                    StringBuilder b = new StringBuilder("required: ");
                    for (SensorDefinition s : task.getSensors())
                    {
                        b.append(s.getDescription()).append(", ");
                    }
                    b.append("available: ");
                    for (SensorDefinition s : entry.getValue().getSensors())
                    {
                        b.append(s.getDescription()).append(", ");
                    }
                    LOG.info("Migrate not to " + entry.getValue().getName() + " because of sensors " + b.toString());
                    continue;
                }

                destinationRealVehicles.add(entry.getValue());
            }

            decision.setRealVehicles(destinationRealVehicles);
        }

        return decision;
    }

}
