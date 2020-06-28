// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.geojson.FeatureCollection;

import cpcc.core.entities.MappingAttributes;
import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.SensorType;
import cpcc.core.services.QueryManager;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.jobs.TimeService;
import cpcc.ros.sensors.AbstractGpsSensorAdapter;
import cpcc.ros.sensors.AbstractSensorAdapter;
import cpcc.ros.services.RosNodeService;
import cpcc.vvrte.entities.Task;
import cpcc.vvrte.services.db.TaskRepository;
import cpcc.vvrte.services.db.VvRteRepository;
import cpcc.vvrte.services.json.VvGeoJsonConverter;

/**
 * State service implementation.
 */
public class StateServiceImpl implements StateService
{
    private static final String POSITION = "pos";
    private static final String VIRTUAL_VEHICLES = "vvs";
    private static final String TASKS = "tsk";
    private static final String SENSORS = "sen";

    private static final String[] ALL_CONTRIBUTORS = {POSITION, VIRTUAL_VEHICLES, TASKS, SENSORS};

    private final Map<String, StateContributor> contributorMap = new HashMap<>();

    private QueryManager qm;
    private RosNodeService rns;
    private TaskRepository taskRepository;

    /**
     * @param qm the query manager.
     * @param rns the ROS node service.
     * @param vvRepo the virtual vehicle RTE repository.
     * @param vjc the virtual vehicle JSON converter.
     * @param rvRepo the real vehicle repository.
     * @param taskRepository the task repository.
     * @param timeService the time service.
     */
    public StateServiceImpl(QueryManager qm, RosNodeService rns, VvRteRepository vvRepo,
        VvGeoJsonConverter vjc, RealVehicleRepository rvRepo, TaskRepository taskRepository, TimeService timeService)
    {
        this.qm = qm;
        this.rns = rns;
        this.taskRepository = taskRepository;

        contributorMap.put(POSITION, new PositionContributor(timeService, rvRepo));
        contributorMap.put(VIRTUAL_VEHICLES, new VirtualVehicleContributor(vvRepo, vjc));
        contributorMap.put(TASKS, new TasksContributor());
        contributorMap.put(SENSORS, new SensorsContributor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureCollection getState(String what) throws IOException
    {
        List<Task> tasks = new ArrayList<>();
        Task task = taskRepository.getCurrentRunningTask();
        if (task != null)
        {
            tasks.add(task);
        }
        tasks.addAll(taskRepository.getScheduledTasks());

        PolarCoordinate position = findRealVehiclePosition();

        FeatureCollection fc = new FeatureCollection();

        Stream.of(what != null ? what.split("-") : ALL_CONTRIBUTORS)
            .map(contributorMap::get)
            .filter(Objects::nonNull)
            .forEach(c -> c.contribute(fc, position, tasks));

        return fc;
    }

    /**
     * @return the real vehicle's current position, or null if unknown.
     */
    private PolarCoordinate findRealVehiclePosition()
    {
        Optional<PolarCoordinate> coordinate = qm.findAllMappingAttributes().stream()
            .filter(MappingAttributes::getConnectedToAutopilot)
            .map(MappingAttributes::getSensorDefinition)
            .filter(Objects::nonNull)
            .filter(sd -> sd.getType() == SensorType.GPS)
            .map(sd -> rns.findAdapterNodeBySensorDefinitionId(sd.getId()))
            .filter(Objects::nonNull)
            .filter(adapter -> adapter instanceof AbstractSensorAdapter)
            .map(adapter -> ((AbstractGpsSensorAdapter) adapter).getPosition())
            .filter(Objects::nonNull)
            .map(pos -> new PolarCoordinate(pos.getLatitude(), pos.getLongitude(), pos.getAltitude()))
            .findFirst();

        return coordinate.isPresent()
            ? coordinate.get()
            : null;
    }
}
