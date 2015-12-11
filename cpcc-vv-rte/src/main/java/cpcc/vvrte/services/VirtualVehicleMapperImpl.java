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

import com.fasterxml.jackson.databind.ObjectMapper;

import cpcc.core.base.PolygonZone;
import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleType;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.services.RealVehicleRepository;
import cpcc.vvrte.base.VirtualVehicleMappingDecision;
import cpcc.vvrte.entities.Task;

/**
 * VirtualVehicleMapperImpl
 */
public class VirtualVehicleMapperImpl implements VirtualVehicleMapper
{
    private Logger logger;
    private RealVehicleRepository rvRepo;

    /**
     * @param logger the application logger.
     * @param rvRepo the real vehicle repository.
     */
    public VirtualVehicleMapperImpl(Logger logger, RealVehicleRepository rvRepo)
    {
        this.logger = logger;
        this.rvRepo = rvRepo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicleMappingDecision findMappingDecision(Task task)
    {
        VirtualVehicleMappingDecision decision = new VirtualVehicleMappingDecision()
            .setTask(task)
            .setMigration(true);

        RealVehicle rv = rvRepo.findOwnRealVehicle();
        if (rv == null)
        {
            return migrateTask(decision);
        }

        boolean migration = !isInsideAreasOfOperation(rv.getAreaOfOperation(), task.getPosition());

        if (migration || !rv.getSensors().containsAll(task.getSensors()))
        {
            return migrateTask(decision);
        }

        return decision.setMigration(false);
    }

    /**
     * @param decision the migration decision.
     * @return the migration decision.
     */
    private VirtualVehicleMappingDecision migrateTask(VirtualVehicleMappingDecision decision)
    {
        Task task = decision.getTask();
        List<RealVehicle> groundStations = new ArrayList<RealVehicle>();
        List<RealVehicle> destinationRealVehicles = new ArrayList<RealVehicle>();

        for (RealVehicle rv : rvRepo.findAllRealVehiclesExceptOwn())
        {
            if (rv.getType() == RealVehicleType.GROUND_STATION)
            {
                groundStations.add(rv);
            }

            if (isInsideAreasOfOperation(rv.getAreaOfOperation(), task.getPosition()))
            {
                if (rv.getSensors().containsAll(task.getSensors()))
                {
                    logger.info("Found migration candidate " + rv.getName() + " for task at " + task.getPosition());
                    destinationRealVehicles.add(rv);
                }
                else
                {
                    logger.info("Migrate not to " + rv.getName() + " because of sensors "
                        + getSensorString(task.getSensors(), rv.getSensors()));
                }
            }
            else
            {
                logger.info("Migrate not to " + rv.getName() + " because of position " + task.getPosition());
            }
        }

        decision.setRealVehicles(destinationRealVehicles.isEmpty() ? groundStations : destinationRealVehicles);
        return decision;
    }

    /**
     * @param areaOfOperation the area of operation as a {@code String}.
     * @return the list of {@code PolygonZone} instances.
     * @throws IOException in case of errors.
     */
    private static List<PolygonZone> getPolygons(String areaOfOperation) throws IOException
    {
        List<PolygonZone> list = new ArrayList<PolygonZone>();
        FeatureCollection fc = new ObjectMapper()
            .readValue(areaOfOperation.replace("\\n", "\n"), FeatureCollection.class);

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

    /**
     * @param areaOfOperation the area of operation as a {@code String}.
     * @param position the position in question.
     * @return true if the position is inside the area of operation.
     */
    private static boolean isInsideAreasOfOperation(String areaOfOperation, PolarCoordinate position)
    {
        if (StringUtils.isBlank(areaOfOperation))
        {
            return false;
        }

        try
        {
            for (PolygonZone zone : getPolygons(areaOfOperation))
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
     * @param requiredSensors the list of required sensors.
     * @param availableSensors the list of available sensors.
     * @return the sensors as a {@code String} object.
     */
    private static String getSensorString(List<SensorDefinition> requiredSensors,
        List<SensorDefinition> availableSensors)
    {
        StringBuilder b = new StringBuilder("required: ");
        sd(b, requiredSensors);
        b.append(" available: ");
        sd(b, availableSensors);
        return b.toString();
    }

    /**
     * @param toAppendTo the {@code StringBuilder} instance.
     * @param sensors the list of sensors to be appended.
     */
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
