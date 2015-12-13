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

import org.geojson.FeatureCollection;
import org.slf4j.Logger;

import cpcc.core.entities.MappingAttributes;
import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;
import cpcc.core.services.QueryManager;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.jobs.TimeService;
import cpcc.ros.base.AbstractRosAdapter;
import cpcc.ros.sensors.AbstractGpsSensorAdapter;
import cpcc.ros.services.RosNodeService;
import cpcc.vvrte.entities.Task;
import cpcc.vvrte.services.db.TaskRepository;
import cpcc.vvrte.services.db.VvRteRepository;
import cpcc.vvrte.services.json.VvGeoJsonConverter;
import sensor_msgs.NavSatFix;

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

    private final Map<String, StateContributor> contributorMap = new HashMap<String, StateContributor>();

    private QueryManager qm;
    private RosNodeService rns;
    private TaskRepository taskRepository;

    /**
     * @param logger the application logger.
     * @param qm the query manager.
     * @param rns the ROS node service.
     * @param vvRepo the virtual vehicle RTE repository.
     * @param vjc the virtual vehicle JSON converter.
     * @param rvRepo the real vehicle repository.
     * @param taskRepository the task repository.
     * @param timeService the time service.
     */
    public StateServiceImpl(Logger logger, QueryManager qm, RosNodeService rns, VvRteRepository vvRepo,
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

        for (String x : what != null ? what.split("-") : ALL_CONTRIBUTORS)
        {
            if (contributorMap.containsKey(x))
            {
                contributorMap.get(x).contribute(fc, position, tasks);
            }
        }

        return fc;
    }

    /**
     * @return the real vehicle's current position, or null if unknown.
     */
    private PolarCoordinate findRealVehiclePosition()
    {
        for (MappingAttributes attr : qm.findAllMappingAttributes())
        {
            if (!attr.getConnectedToAutopilot())
            {
                continue;
            }

            SensorDefinition sd = attr.getSensorDefinition();
            if (sd == null || sd.getType() != SensorType.GPS)
            {
                continue;
            }

            AbstractRosAdapter adapter = rns.findAdapterNodeBySensorDefinitionId(sd.getId());
            if (adapter != null && adapter instanceof AbstractGpsSensorAdapter)
            {
                NavSatFix pos = ((AbstractGpsSensorAdapter) adapter).getPosition();
                if (pos != null)
                {
                    return new PolarCoordinate(pos.getLatitude(), pos.getLongitude(), pos.getAltitude());
                }
            }
        }

        return null;
    }
}
