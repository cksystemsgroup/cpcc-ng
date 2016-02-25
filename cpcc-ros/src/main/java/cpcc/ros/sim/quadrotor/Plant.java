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

package cpcc.ros.sim.quadrotor;

import org.ros.concurrent.CancellableLoop;
import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sensor_msgs.NavSatFix;
import std_msgs.Float32;
import big_actor_msgs.LatLngAlt;
import cpcc.core.entities.PolarCoordinate;

/**
 * Plant
 */
public class Plant extends CancellableLoop implements MessageListener<big_actor_msgs.LatLngAlt>
{
    private static final Logger LOG = LoggerFactory.getLogger(Plant.class);
    
    private Configuration config;
    private ConnectedNode connectedNode;
    private Publisher<NavSatFix> gpsPublisher;
    private Publisher<Float32> sonarPublisher;
    private LatLngAlt position;
    private boolean batteryLow = false;
    private Automaton automaton = new Automaton();
    private PlantState plantState = new PlantState();
    private PlantStateEstimator estimator;
    
    /**
     * @param config the configuration.
     * @param connectedNode the connected node.
     */
    public Plant(Configuration config, ConnectedNode connectedNode)
    {
        this.config = config;
        this.connectedNode = connectedNode;
        String gpsTopic = config.getTopicRoot() + "/gps";
        String sonarTopic = config.getTopicRoot() + "/sonar";
        gpsPublisher = connectedNode.newPublisher(gpsTopic, sensor_msgs.NavSatFix._TYPE);
        sonarPublisher = connectedNode.newPublisher(sonarTopic, std_msgs.Float32._TYPE);
        
        automaton.transition(Event.UNLOCK);
        automaton.transition(Event.START);

        LatLngAlt o = config.getOrigin();
        plantState.setPosition(new PolarCoordinate(o.getLatitude(), o.getLongitude(), o.getAltitude()));
        plantState.setTarget(new PolarCoordinate(plantState.getPosition()));
        plantState.getTarget().setAltitude(config.getTakeOffHeight());
        estimator = new PlantStateEstimatorImpl(config, plantState, State.TAKE_OFF);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void setup()
    {
        super.setup();
        LOG.info("setup()");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loop() throws InterruptedException
    {
        calculateNewPosition();
        
        PolarCoordinate currentPosition = plantState.getPosition();
       
        NavSatFix gpsPosition = connectedNode.getTopicMessageFactory().newFromType(NavSatFix._TYPE);
        gpsPosition.setLatitude(currentPosition.getLatitude());
        gpsPosition.setLongitude(currentPosition.getLongitude());
        gpsPosition.setAltitude(currentPosition.getAltitude());
        gpsPublisher.publish(gpsPosition);

        Float32 altitude = connectedNode.getTopicMessageFactory().newFromType(Float32._TYPE);
        altitude.setData((float) (currentPosition.getAltitude() - config.getOrigin().getAltitude()));
        sonarPublisher.publish(altitude);

        Thread.sleep(config.getUpdateCycle());
    }

    /**
     * calculate new position.
     */
    private void calculateNewPosition()
    {
        if (batteryLow && automaton.transition(Event.BATTERY_LOW) == State.DEPOT_FLIGHT)
        {
            initiateDepotFlight();
        }
        else if (automaton.getCurrentState() == State.FLIGHT || automaton.getCurrentState() == State.DEPOT_FLIGHT)
        {
            calculateFlightPosition();
        }
        else if (automaton.getCurrentState() == State.LAND || automaton.getCurrentState() == State.DEPOT_LAND)
        {
            calculateLandingPosition();
        }
        else if (automaton.getCurrentState() == State.TAKE_OFF)
        {
            calculateTakeoffPosition();
        }
    }

    /**
     * initiate flight
     */
    private void initiateFlight()
    {
        PolarCoordinate target =
            new PolarCoordinate(position.getLatitude(), position.getLongitude(), position.getAltitude());
        plantState.setTarget(target);
        estimator = new PlantStateEstimatorImpl(config, plantState, State.FLIGHT);
    }

    /**
     * initiate depot flight
     */
    private void initiateDepotFlight()
    {
        LatLngAlt o = config.getOrigin();
        PolarCoordinate target = new PolarCoordinate(o.getLatitude(), o.getLongitude(), o.getAltitude());
        plantState.setTarget(target);
        estimator = new PlantStateEstimatorImpl(config, plantState, State.DEPOT_FLIGHT);
    }

    /**
     * calculate take-off position
     */
    private void calculateTakeoffPosition()
    {
        if (estimator.calculateState())
        {
            automaton.transition(Event.REACHED);
            plantState.setAcceleration(0);
            plantState.setElevation(0);
        }
    }
    
    /**
     * calculate flight position
     */
    private void calculateFlightPosition()
    {
        if (estimator.calculateState())
        {
            automaton.transition(Event.REACHED);
            plantState.setAcceleration(0);
            plantState.setElevation(0);
        }
    }

    /**
     * calculate landing position
     */
    private void calculateLandingPosition()
    {
        if (estimator.calculateState())
        {
            automaton.transition(Event.LANDED);
            plantState.setAcceleration(0);
            plantState.setElevation(0);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void onNewMessage(LatLngAlt newPosition)
    {
        if (automaton.transition(Event.FLY_TO) != null)
        {
            position = newPosition;
            initiateFlight();
        }
    }

    /**
     * @return true if the destination has been reached.
     */
    public boolean isDestinationReached()
    {
        return automaton.getCurrentState() == State.HOVER;
    }
    
    /**
     * @return the plant state
     */
    public PlantState getPlantState()
    {
        return plantState;
    }
    
    /**
     * @return the current state.
     */
    public State getCurrentState()
    {
        return automaton.getCurrentState();
    }
}
