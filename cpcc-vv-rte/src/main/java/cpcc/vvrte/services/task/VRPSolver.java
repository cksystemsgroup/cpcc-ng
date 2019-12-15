// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2019 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.vvrte.services.task;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.util.Solutions;

import cpcc.core.entities.PolarCoordinate;
import cpcc.vvrte.entities.Task;

/**
 * VRPSolver implementation.
 */
public class VRPSolver implements TspSolver
{
    private static final int WEIGHT_INDEX = 0;

    private static double scale(double actualValue, double minValue, double maxValue)
    {
        return 1000.0 * (actualValue - minValue) / (maxValue - minValue);
    }

    @Override
    public List<Task> calculateBestPath(PolarCoordinate current, List<Task> path) throws TimeoutException
    {
        if (path.size() < 2)
        {
            return path;
        }

        System.out.println("Buggerit start.");
        path.stream().forEach(x -> System.out.println("Buggerit: " + x.toString()));
        System.out.println("Buggerit end.");

        Map<String, Task> taskMap = path.stream().collect(Collectors.toMap(x -> x.getId().toString(), y -> y));

        double minLat = path.stream().mapToDouble(x -> x.getPosition().getLatitude()).min().getAsDouble();
        double minLng = path.stream().mapToDouble(x -> x.getPosition().getLongitude()).min().getAsDouble();
        double maxLat = path.stream().mapToDouble(x -> x.getPosition().getLatitude()).max().getAsDouble();
        double maxLng = path.stream().mapToDouble(x -> x.getPosition().getLongitude()).max().getAsDouble();

        VehicleType vehicleType = VehicleTypeImpl.Builder
            .newInstance("vehicleType")
            .addCapacityDimension(WEIGHT_INDEX, 100000)
            .build();

        VehicleImpl.Builder vehicleBuilder = VehicleImpl.Builder.newInstance("vehicle");
        vehicleBuilder.setStartLocation(Location.newInstance(
            scale(current.getLatitude(), minLat, maxLat),
            scale(current.getLongitude(), minLng, maxLng)));
        vehicleBuilder.setType(vehicleType);

        VehicleImpl vehicle = vehicleBuilder.build();

        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        vrpBuilder.addVehicle(vehicle);

        path.stream().map(x -> Service.Builder
            .newInstance(x.getId().toString())
            .addSizeDimension(WEIGHT_INDEX, 1)
            .setLocation(Location.newInstance(
                scale(x.getPosition().getLatitude(), minLat, maxLat),
                scale(x.getPosition().getLongitude(), minLng, maxLng)))
            .build())
            .forEach(x -> vrpBuilder.addJob(x));

        VehicleRoutingProblem problem = vrpBuilder.build();
        VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(problem);
        Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();
        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);

        List<Task> res = bestSolution.getRoutes().stream()
            .map(x -> (TourActivity.JobActivity) x.getTourActivities().getActivities().get(0))
            .map(x -> (Service) x.getJob())
            .map(x -> taskMap.get(x.getId()))
            .collect(Collectors.toList());

        return res;
    }
}
