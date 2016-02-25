// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.ros.sim.quadrotor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.utils.CartesianCoordinate;
import cpcc.core.utils.GeodeticSystem;

/**
 * Plant State Estimator implementation.
 */
public class PlantStateEstimatorImpl implements PlantStateEstimator
{
    private static final Logger LOG = LoggerFactory.getLogger(PlantStateEstimatorImpl.class);

    private Configuration config;
    private PlantState plantState;
    private State state;
    private double maxA;
    private double totalTime;
    private int remainingCycles;
    private boolean deAcceleration;
    private CartesianCoordinate initPosition;
    private CartesianCoordinate normalizedDist;
    private GeodeticSystem geodeticSystem;
    private PlantMotionAlgorithm algorithm;

    private double distNorm;

    /**
     * @param config the configuration.
     * @param plantState the current plant state.
     * @param state the quadrotor state.
     */
    public PlantStateEstimatorImpl(Configuration config, PlantState plantState, State state)
    {
        this.config = config;
        this.plantState = plantState;
        this.state = state;

        geodeticSystem = config.getGeodeticSystem();

        if (plantState.getVelocity() > 1.0)
        {
            initDeAcceleration();
        }
        else
        {
            initFlight();
        }
    }

    /**
     * Initialization of a de-acceleration.
     */
    private void initDeAcceleration()
    {
        deAcceleration = true;
        maxA = config.getMaxAcceleration();

        double x = -Math.cos(plantState.getHeading());
        double y = Math.sin(plantState.getHeading());
        PolarCoordinate newPosition = geodeticSystem.walk(plantState.getPosition(), x, y, 0);

        initPosition = geodeticSystem.polarToRectangularCoordinates(plantState.getPosition());
        CartesianCoordinate targetPosition = geodeticSystem.polarToRectangularCoordinates(newPosition);
        CartesianCoordinate distance = targetPosition.subtract(initPosition);
        normalizedDist = distance.normalize();

        totalTime = plantState.getVelocity() / maxA;
        remainingCycles = (int) Math.round(1000.0 * totalTime / config.getUpdateCycle() + 0.5);
        totalTime = remainingCycles * config.getUpdateCycle() / 1000.0;
        maxA = -plantState.getVelocity() / totalTime;

        plantState.setAcceleration(maxA);
        plantState.setElevation(0);
        plantState.setFlyingTime(totalTime);
    }

    /**
     * Initialization of a new flight.
     */
    private void initFlight()
    {
        deAcceleration = false;

        double maxV;
        switch (state)
        {
            case TAKE_OFF:
                maxA = config.getTakeOffAcceleration();
                maxV = config.getTakeOffVelocity();
                plantState.setTarget(new PolarCoordinate(plantState.getPosition()));
                plantState.getTarget().setAltitude(config.getTakeOffHeight());
                break;
            case LAND:
                maxA = config.getLandingAcceleration();
                maxV = config.getLandingVelocity();
                plantState.setTarget(new PolarCoordinate(plantState.getPosition()));
                plantState.getTarget().setAltitude(0);
                break;
            default:
                maxA = config.getMaxAcceleration();
                maxV = config.getMaxVelocity();
                break;
        }

        initPosition = geodeticSystem.polarToRectangularCoordinates(plantState.getPosition());

        CartesianCoordinate targetPosition = geodeticSystem.polarToRectangularCoordinates(plantState.getTarget());
        CartesianCoordinate distance = targetPosition.subtract(initPosition);
        normalizedDist = distance.normalize();

        distNorm = distance.norm();

        double minDistOne = 8.0 * maxV * maxV / 3.0 / maxA;

        algorithm = distNorm >= minDistOne
            ? new PlantMotionAlgorithmOne(LOG, distNorm, maxV, maxA)
            : new PlantMotionAlgorithmTwo(LOG, distNorm, maxA);

        // algorithm = new PlantMotionAlgorithmZero(state, distNorm, maxV, maxV);

        totalTime = algorithm.getTotalTime();

        remainingCycles = (int) Math.round(1000.0 * totalTime / config.getUpdateCycle() + 0.5);
        totalTime = remainingCycles * config.getUpdateCycle() / 1000.0;
        plantState.setFlyingTime(totalTime);

        double latCur = plantState.getPosition().getLatitude();
        double lonCur = plantState.getPosition().getLongitude();
        double latDst = plantState.getTarget().getLatitude();
        double lonDst = plantState.getTarget().getLongitude();

        double heading = Math.atan2((lonDst - lonCur) * Math.cos(latDst), latDst - latCur);
        while (heading < 0)
        {
            heading += 2.0 * Math.PI;
        }
        plantState.setHeading(heading);

        PolarCoordinate targetFlat = new PolarCoordinate(plantState.getTarget());
        targetFlat.setAltitude(plantState.getPosition().getAltitude());
        CartesianCoordinate targetFlatCart = geodeticSystem.polarToRectangularCoordinates(targetFlat);
        CartesianCoordinate distOverGround = targetFlatCart.subtract(initPosition);

        double altCur = plantState.getPosition().getAltitude();
        double altDst = plantState.getTarget().getAltitude();
        double elevation = distOverGround.norm() >= 1E-3
            ? Math.atan((altDst - altCur) / distOverGround.norm())
            : Math.signum(altDst - altCur) * Math.PI / 2.0;

        plantState.setElevation(elevation);
    }

    /**
     * @return true if the target has been reached, false otherwise.
     */
    @Override
    public boolean calculateState()
    {
        if (deAcceleration)
        {
            if (remainingCycles > 0)
            {
                --remainingCycles;

                double newFlyingTime = plantState.getFlyingTime() - config.getUpdateCycle() / 1000.0;
                plantState.setFlyingTime(newFlyingTime);

                double v = -maxA * newFlyingTime;
                plantState.setVelocity(v);

                double t = totalTime - newFlyingTime;
                CartesianCoordinate newPosition = initPosition.add(normalizedDist.multiply(t * (v + maxA / (2.0 * t))));
                plantState.setPosition(geodeticSystem.rectangularToPolarCoordinates(newPosition));
                return false;
            }

            deAcceleration = false;
            initFlight();
            return false;
        }

        if (remainingCycles <= 0)
        {
            return true;
        }

        --remainingCycles;
        double newFlyingTime = plantState.getFlyingTime() - config.getUpdateCycle() / 1000.0;
        plantState.setFlyingTime(newFlyingTime);

        double t = totalTime - newFlyingTime;
        double s = algorithm.distance(t);
        double v = algorithm.velocity(t);
        double a = algorithm.acceleration(t);

        CartesianCoordinate newPosition = initPosition.add(normalizedDist.multiply(s));
        plantState.setPosition(geodeticSystem.rectangularToPolarCoordinates(newPosition));
        plantState.setVelocity(v);
        plantState.setAcceleration(a);
        return false;
    }
}
