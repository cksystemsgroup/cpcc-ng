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

package cpcc.vvrte.services;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.services.QueryManager;
import cpcc.core.utils.PolarCoordinate;
import cpcc.vvrte.task.Task;

/**
 * VirtualVehicleMapperImpl
 */
public class VirtualVehicleMapperImpl implements VirtualVehicleMapper
{
    private static final Logger LOG = LoggerFactory.getLogger(VirtualVehicleMapperImpl.class);

    private QueryManager qm;

    /**
     * @param qm the query manager.
     * @throws JsonParseException thrown in case of errors.
     * @throws JsonMappingException thrown in case of errors.
     * @throws IOException thrown in case of errors.
     */
    public VirtualVehicleMapperImpl(QueryManager qm) throws JsonParseException, JsonMappingException, IOException
    {
        this.qm = qm;
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

        RealVehicle rv = qm.findOwnRealVehicle();
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

        decision.setMigration(false);

        return decision;
    }

    /**
     * @param task the task to be migrated.
     * @param decision the migration decision.
     */
    private void migrateTask(Task task, VirtualVehicleMappingDecision decision)
    {
        List<RealVehicle> destinationRealVehicles = new ArrayList<RealVehicle>();

        for (RealVehicle rv : qm.findAllRealVehicles())
        {
            if (isInsideAreasOfOperation(rv.getAreaOfOperation(), task.getPosition()))
            {
                if (rv.getSensors().containsAll(task.getSensors()))
                {
                    LOG.info("Found migration candidate " + rv.getName() + " for task at " + task.getPosition());
                    destinationRealVehicles.add(rv);
                }
                else
                {
                    LOG.info("Migrate not to " + rv.getName() + " because of sensors "
                        + getSensorString(task.getSensors(), rv.getSensors()));
                }
            }
            else
            {
                LOG.info("Migrate not to " + rv.getName() + " because of position " + task.getPosition());
            }
        }

        decision.setRealVehicles(destinationRealVehicles);
    }

    private static List<PolygonZone> getPolygons(String areaOfOperationString) throws IOException
    {
        List<PolygonZone> list = new ArrayList<PolygonZone>();
        FeatureCollection fc = new ObjectMapper()
            .readValue(areaOfOperationString.replace("\\n", "\n"), FeatureCollection.class);

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

    private static boolean isInsideAreasOfOperation(String areasOfOperation, PolarCoordinate position)
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

    private static String getSensorString(List<SensorDefinition> requiredSensors,
        List<SensorDefinition> availableSensors)
    {
        StringBuilder b = new StringBuilder("required: ");
        sd(b, requiredSensors);
        b.append(" available: ");
        sd(b, availableSensors);
        return b.toString();
    }

    private static void sd(StringBuilder toAppendTo, List<SensorDefinition> sensors)
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
