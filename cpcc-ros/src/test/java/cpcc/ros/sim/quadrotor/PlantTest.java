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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ros.message.MessageFactory;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

import big_actor_msgs.LatLngAlt;
import cpcc.core.utils.WGS84;
import sensor_msgs.NavSatFix;
import std_msgs.Float32;

/**
 * PlantTest implementation.
 */
public class PlantTest
{
    private static final int MAX_LOOP_CYCLES = 200;

    private static final String TOPIC_ROOT = "/quad";
    private static final String TOPIC_GPS = TOPIC_ROOT + "/gps";
    private static final String TOPIC_SONAR = TOPIC_ROOT + "/sonar";

    private Configuration config;
    private ConnectedNode connectedNode;
    private Plant sut;
    private LatLngAlt origin;
    private MessageFactory factory;
    private NavSatFix gpsPosition;
    private Float32 float32;
    private Publisher<Object> gpsPublisher;
    private Publisher<Object> sonarPublisher;
    private LatLngAlt targetOne;
    private LatLngAlt targetTwo;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp()
    {
        targetOne = mock(LatLngAlt.class);
        when(targetOne.getLatitude()).thenReturn(47.822898);
        when(targetOne.getLongitude()).thenReturn(13.038800);
        when(targetOne.getAltitude()).thenReturn(25.0);

        targetTwo = mock(LatLngAlt.class);
        when(targetTwo.getLatitude()).thenReturn(47.822905);
        when(targetTwo.getLongitude()).thenReturn(13.038793);
        when(targetTwo.getAltitude()).thenReturn(19.0);

        origin = mock(LatLngAlt.class);
        when(origin.getLatitude()).thenReturn(47.822898);
        when(origin.getLongitude()).thenReturn(13.038793);
        when(origin.getAltitude()).thenReturn(25.0);

        // origin=(47.822898;13.038793;0) maxVelocity=20 maxAcceleration=5 precision=10 updateCycle=100 idlePower=20
        // takeOffHeight=10 takeOffVelocity=2 takeOffAcceleration=2 landingVelocity=1.5 landingAcceleration=2
        // hoverPower=55 mass=2.2

        config = mock(Configuration.class);
        when(config.getGeodeticSystem()).thenReturn(new WGS84());
        when(config.getLandingAcceleration()).thenReturn(2.0);
        when(config.getLandingVelocity()).thenReturn(1.5);
        when(config.getMaxAcceleration()).thenReturn(5.0);
        when(config.getMaxVelocity()).thenReturn(20.0);
        when(config.getOrigin()).thenReturn(origin);
        when(config.getTakeOffAcceleration()).thenReturn(2.0);
        when(config.getTakeOffHeight()).thenReturn(10.0);
        when(config.getTakeOffVelocity()).thenReturn(2.0);
        when(config.getTopicRoot()).thenReturn(TOPIC_ROOT);
        when(config.getUpdateCycle()).thenReturn(100L);

        gpsPosition = mock(NavSatFix.class);
        float32 = mock(Float32.class);

        factory = mock(MessageFactory.class);
        when(factory.newFromType(NavSatFix._TYPE)).thenReturn(gpsPosition);
        when(factory.newFromType(Float32._TYPE)).thenReturn(float32);

        gpsPublisher = mock(Publisher.class);
        sonarPublisher = mock(Publisher.class);

        connectedNode = mock(ConnectedNode.class);
        when(connectedNode.getTopicMessageFactory()).thenReturn(factory);

        when(connectedNode.newPublisher(TOPIC_GPS, sensor_msgs.NavSatFix._TYPE)).thenReturn(gpsPublisher);
        when(connectedNode.newPublisher(TOPIC_SONAR, std_msgs.Float32._TYPE)).thenReturn(sonarPublisher);

        sut = new Plant(config, connectedNode);

        sut.setup();
    }

    @Test
    public void shouldTakeOffAndFly() throws InterruptedException
    {
        int count = loopUntilStateChanges(sut, State.TAKE_OFF);

        assertThat(count)
            .overridingErrorMessage("Number of loops to take off schould be less than %d, but were %d.",
                MAX_LOOP_CYCLES, count)
            .isLessThanOrEqualTo(MAX_LOOP_CYCLES);

        assertThat(sut.getCurrentState())
            .describedAs("Current State after Take Off")
            .isEqualTo(State.HOVER);

        assertThat(sut.isDestinationReached())
            .describedAs("Destination reached after Take Off")
            .isTrue();

        PlantState actual = sut.getPlantState();

        assertThat(actual.getVelocity()).describedAs("Vehicle velocity").isLessThanOrEqualTo(0.1);
        assertThat(actual.getAcceleration()).describedAs("Vehicle acceleration").isLessThanOrEqualTo(0.1);
        assertThat(actual.getElevation()).describedAs("Vehicle elevation").isLessThanOrEqualTo(0.1);
        assertThat(actual.getHeading()).describedAs("Vehicle heading").isLessThanOrEqualTo(0.1);

        assertThat(actual.getPosition().getLatitude()).isEqualTo(origin.getLatitude(), offset(1E-8));
        assertThat(actual.getPosition().getLongitude()).isEqualTo(origin.getLongitude(), offset(1E-8));
        assertThat(actual.getPosition().getAltitude()).isEqualTo(config.getTakeOffHeight(), offset(1E-4));

        assertThat(actual.getTarget().getLatitude()).isEqualTo(origin.getLatitude(), offset(1E-8));
        assertThat(actual.getTarget().getLongitude()).isEqualTo(origin.getLongitude(), offset(1E-8));
        assertThat(actual.getTarget().getAltitude()).isEqualTo(config.getTakeOffHeight(), offset(1E-4));

        sut.onNewMessage(targetOne);
        sut.loop();

        assertThat(sut.getCurrentState())
            .describedAs("Current State at Flight")
            .isEqualTo(State.FLIGHT);

        assertThat(sut.isDestinationReached())
            .describedAs("Destination reached at Flight")
            .isFalse();

        loopUntilStateChanges(sut, State.FLIGHT);

        assertThat(sut.getCurrentState())
            .describedAs("Current State after Flight")
            .isEqualTo(State.HOVER);

        assertThat(sut.isDestinationReached())
            .describedAs("Destination reached after Flight")
            .isTrue();

        actual = sut.getPlantState();

        assertThat(actual.getVelocity()).describedAs("Vehicle velocity").isLessThanOrEqualTo(0.1);
        assertThat(actual.getAcceleration()).describedAs("Vehicle acceleration").isLessThanOrEqualTo(0.1);
        assertThat(actual.getElevation()).describedAs("Vehicle elevation").isLessThanOrEqualTo(0.1);
        assertThat(actual.getHeading()).describedAs("Vehicle heading").isLessThanOrEqualTo(5.0);

        assertThat(actual.getPosition().getLatitude()).isEqualTo(targetOne.getLatitude(), offset(1E-8));
        assertThat(actual.getPosition().getLongitude()).isEqualTo(targetOne.getLongitude(), offset(1E-8));
        assertThat(actual.getPosition().getAltitude()).isEqualTo(targetOne.getAltitude(), offset(1E-4));

        assertThat(actual.getTarget().getLatitude()).isEqualTo(targetOne.getLatitude(), offset(1E-8));
        assertThat(actual.getTarget().getLongitude()).isEqualTo(targetOne.getLongitude(), offset(1E-8));
        assertThat(actual.getTarget().getAltitude()).isEqualTo(targetOne.getAltitude(), offset(1E-4));
    }

    //    @Test
    // TODO not working yet!
    //    public void shouldInterruptOngoingFlightForNewTargets() throws InterruptedException
    //    {
    //        loopUntilStateChanges(sut, State.TAKE_OFF);
    //
    //        assertThat(sut.getCurrentState())
    //            .describedAs("Current State after Take Off")
    //            .isEqualTo(State.HOVER);
    //
    //        sut.onNewMessage(targetOne);
    //        for (int k = 0; k < 10; ++k)
    //        {
    //            sut.loop();
    //        }
    //
    //        assertThat(sut.getCurrentState())
    //            .describedAs("Current State when Flying")
    //            .isEqualTo(State.FLIGHT);
    //
    //        sut.onNewMessage(targetTwo);
    //
    //        loopUntilStateChanges(sut, State.FLIGHT);
    //
    //        assertThat(sut.getCurrentState())
    //            .describedAs("Current State after Flight")
    //            .isEqualTo(State.HOVER);
    //
    //        assertThat(sut.isDestinationReached())
    //            .describedAs("Destination reached after Flight")
    //            .isTrue();
    //
    //        PlantState actual = sut.getPlantState();
    //
    //        assertThat(actual.getVelocity()).describedAs("Vehicle velocity").isLessThanOrEqualTo(0.1);
    //        assertThat(actual.getAcceleration()).describedAs("Vehicle acceleration").isLessThanOrEqualTo(0.1);
    //        assertThat(actual.getElevation()).describedAs("Vehicle elevation").isLessThanOrEqualTo(0.1);
    //        assertThat(actual.getHeading()).describedAs("Vehicle heading").isLessThanOrEqualTo(5.0);
    //
    //        assertThat(actual.getPosition().getLatitude()).isEqualTo(targetTwo.getLatitude(), offset(1E-8));
    //        assertThat(actual.getPosition().getLongitude()).isEqualTo(targetTwo.getLongitude(), offset(1E-8));
    //        assertThat(actual.getPosition().getAltitude()).isEqualTo(targetTwo.getAltitude(), offset(1E-4));
    //
    //        assertThat(actual.getTarget().getLatitude()).isEqualTo(targetTwo.getLatitude(), offset(1E-8));
    //        assertThat(actual.getTarget().getLongitude()).isEqualTo(targetTwo.getLongitude(), offset(1E-8));
    //        assertThat(actual.getTarget().getAltitude()).isEqualTo(targetTwo.getAltitude(), offset(1E-4));
    //    }

    @Test
    public void shouldInitiateDepotFlightOnLowBattery() throws InterruptedException
    {

        loopUntilStateChanges(sut, State.TAKE_OFF);

        assertThat(sut.getCurrentState())
            .describedAs("Current State after Take Off")
            .isEqualTo(State.HOVER);

        sut.setBatteryLow(true);
        sut.loop();

        assertThat(sut.getCurrentState())
            .describedAs("Current State after Battery low")
            .isEqualTo(State.DEPOT_FLIGHT);

        loopUntilStateChanges(sut, State.DEPOT_FLIGHT);

        assertThat(sut.getCurrentState())
            .describedAs("Current State after Depot Flight")
            .isEqualTo(State.DEPOT_LAND);

        loopUntilStateChanges(sut, State.DEPOT_LAND);

        assertThat(sut.getCurrentState())
            .describedAs("Current State after Depot Land I")
            .isEqualTo(State.DENY);

        sut.loop();

        assertThat(sut.getCurrentState())
            .describedAs("Current State after Depot Land II")
            .isEqualTo(State.DENY);

        sut.onNewMessage(targetOne);
        sut.loop();

        assertThat(sut.getCurrentState())
            .describedAs("Current State after new target in mode DENY.")
            .isEqualTo(State.DENY);
    }

    private static int loopUntilStateChanges(Plant plant, State state) throws InterruptedException
    {
        int count = 0;

        while (count++ < MAX_LOOP_CYCLES && plant.getCurrentState() == state)
        {
            plant.loop();
        }

        return count;
    }
}
