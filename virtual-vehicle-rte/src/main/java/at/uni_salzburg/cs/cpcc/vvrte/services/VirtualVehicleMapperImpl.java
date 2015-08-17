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
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;
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

    /**
     * @param qm the query manager.
     * @throws JsonParseException thrown in case of errors.
     * @throws JsonMappingException thrown in case of errors.
     * @throws IOException thrown in case of errors.
     */
    public VirtualVehicleMapperImpl(QueryManager qm) throws JsonParseException, JsonMappingException, IOException
    {
        this.qm = qm;

        Parameter rvNameParam = qm.findParameterByName(Parameter.REAL_VEHICLE_NAME);
        rvName = rvNameParam != null ? rvNameParam.getValue() : null;

    }

    private List<PolygonZone> getPolygons(String areaOfOperationString) throws IOException
    {
        List<PolygonZone> list = new ArrayList<PolygonZone>();
        FeatureCollection fc = new ObjectMapper().readValue(areaOfOperationString, FeatureCollection.class);

        for (Feature feature : fc.getFeatures())
        {
            GeoJsonObject geom = feature.getGeometry();
            if (geom instanceof Polygon)
            {
                List<LngLatAlt> coordinates = ((Polygon) geom).getCoordinates().get(0);
                list.add(new PolygonZone(coordinates));
            }
        }

        return list;
    }

    private boolean isInsideAreasOfOperation(String areasOfOperation, PolarCoordinate position)
    {
        if (StringUtils.isBlank(areasOfOperation))
        {
            return false;
        }

        try
        {
            for (PolygonZone zone : getPolygons(areasOfOperation))
            {
                if (zone.isInside(position))
                {
                    return true;
                }
            }

            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicleMappingDecision findMappingDecision(Task task)
    {
        VirtualVehicleMappingDecision decision = new VirtualVehicleMappingDecision();
        decision.setTask(task);
        decision.setMigration(true);

        //        boolean migration = true;

        RealVehicle rv = qm.findRealVehicleByName(rvName);

        if (rv == null)
        {
            migrateTask(task, decision);
            return decision;
        }

        boolean migration = !isInsideAreasOfOperation(rv.getAreaOfOperation(), task.getPosition());

        if (migration || !rv.getSensors().containsAll(task.getSensors()))
        {
            migrateTask(task, decision);
            return decision;
        }

        //        if (areaOfOperationMap.containsKey(rvName))
        //        {
        //            migration = !areaOfOperationMap.get(rvName).isInside(task.getPosition());
        //        }
        //
        //        if (!migration && realVehicleMap.containsKey(rvName))
        //        {
        //            RealVehicle realVehicle = realVehicleMap.get(rvName);  // TODO
        //            migration = !realVehicle.getSensors().containsAll(task.getSensors());
        //        }

        decision.setMigration(false);

        //        if (migration)
        //        {
        //            migrateTask(task, decision);
        //        }

        return decision;
    }

    /**
     * @param task the task to be migrated.
     * @param decision the migration decision.
     */
    private void migrateTask(Task task, VirtualVehicleMappingDecision decision)
    {
        List<RealVehicle> destinationRealVehicles = new ArrayList<RealVehicle>();
        //        for (Entry<String, RealVehicle> entry : realVehicleMap.entrySet())
        //        {
        //            PolygonZone areaOfOperation = areaOfOperationMap.get(entry.getKey());
        //            if (areaOfOperation == null || !areaOfOperation.isInside(task.getPosition()))
        //            {
        //                LOG.info("Migrate not to " + entry.getValue().getName()
        //                    + " because of position " + task.getPosition());
        //                continue;
        //            }
        //
        //            if (!entry.getValue().getSensors().containsAll(task.getSensors()))
        //            {
        //
        //                StringBuilder b = new StringBuilder("required: ");
        //                for (SensorDefinition s : task.getSensors())
        //                {
        //                    b.append(s.getDescription()).append(", ");
        //                }
        //                b.append("available: ");
        //                for (SensorDefinition s : entry.getValue().getSensors())
        //                {
        //                    b.append(s.getDescription()).append(", ");
        //                }
        //                LOG.info("Migrate not to " + entry.getValue().getName()
        //                    + " because of sensors " + b.toString());
        //                continue;
        //            }
        //
        //            destinationRealVehicles.add(entry.getValue());
        //        }

        for (RealVehicle rv : qm.findAllRealVehicles())
        {
            if (isInsideAreasOfOperation(rv.getAreaOfOperation(), task.getPosition()))
            {
                if (rv.getSensors().containsAll(task.getSensors()))
                {
                    LOG.info("Migrate not to " + rv.getName() + " because of sensors "
                        + getSensorString(task.getSensors(), rv.getSensors()));
                    destinationRealVehicles.add(rv);
                }
            }
            else
            {
                LOG.info("Migrate not to " + rv.getName() + " because of position " + task.getPosition());
            }
        }

        decision.setRealVehicles(destinationRealVehicles);
    }

    private String getSensorString(List<SensorDefinition> requiredSensors, List<SensorDefinition> availableSensors)
    {
        StringBuilder b = new StringBuilder("required: ");
        sd(b, requiredSensors);
        b.append("available: ");
        sd(b, availableSensors);
        return b.toString();
    }

    private void sd(StringBuilder toAppendTo, List<SensorDefinition> sensors)
    {
        boolean first = true;

        for (SensorDefinition s : sensors)
        {
            if (first)
            {
                first = false;
            }
            else
            {
                toAppendTo.append(", ");
            }

            toAppendTo.append(s.getDescription());
        }
    }
}
