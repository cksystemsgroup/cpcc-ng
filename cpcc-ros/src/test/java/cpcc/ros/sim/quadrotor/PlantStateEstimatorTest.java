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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.ros.node.NodeConfiguration;

import big_actor_msgs.LatLngAlt;
import cpcc.core.entities.PolarCoordinate;

/**
 * PlantStateEstimatorTest
 */
class PlantStateEstimatorTest
{
    private static final Map<String, List<String>> configMap = Collections.unmodifiableMap(Stream
        .of(
            Pair.of("topicRoot", Arrays.asList("/mav01")),
            Pair.of("origin", Arrays.asList("47.82199", "13.04085", "0")),
            Pair.of("maxVelocity", Arrays.asList("20")),
            Pair.of("maxAcceleration", Arrays.asList("3")),
            Pair.of("updateCycle", Arrays.asList("20")),
            Pair.of("idlePower", Arrays.asList("0")),
            Pair.of("hoverPower", Arrays.asList("0")),
            Pair.of("mass", Arrays.asList("2.2")),
            Pair.of("batteryCapacity", Arrays.asList("40")),
            Pair.of("rechargingTime", Arrays.asList("0")),
            Pair.of("takeOffHeight", Arrays.asList("10")),
            Pair.of("takeOffVelocity", Arrays.asList("2")),
            Pair.of("takeOffAcceleration", Arrays.asList("0.5")),
            Pair.of("landingVelocity", Arrays.asList("2")),
            Pair.of("landingAcceleration", Arrays.asList("1")))
        .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)));

    private Configuration config;
    private PlantState initialPlantState;

    /**
     * Tasks to be done before running tests.
     */
    @BeforeEach
    void beforeClass()
    {
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPrivate();
        config = new Configuration(nodeConfiguration, configMap);
        initialPlantState = new PlantState();
        LatLngAlt org = config.getOrigin();
        initialPlantState.setPosition(new PolarCoordinate(org.getLatitude(), org.getLongitude(), org.getAltitude()));
    }

    /**
     * Should take-off plant.
     * 
     * @throws IOException thrown in case of errors.
     */
    @Test
    void shouldTakeOffPlant() throws IOException
    {
        PlantState plantState = new PlantState(initialPlantState);
        PlantStateEstimator e = new PlantStateEstimatorImpl(config, plantState, State.TAKE_OFF);

        PrintWriter writer = new PrintWriter("target/takeoff.csv");
        writer.println("posLat;posLon;posAlt;flyTime;heading;dstLat;dstLon;dstAlt;v;a;elevation;remCap");

        int iterationCounter = 0;
        do
        {
            ++iterationCounter;
            printStateToFile(plantState, writer);
            assertThat(plantState.getPosition().getLatitude())
                .isEqualTo(plantState.getTarget().getLatitude(), offset(1E-5));
            assertThat(plantState.getPosition().getLongitude())
                .isEqualTo(plantState.getTarget().getLongitude(), offset(1E-5));
            assertThat(plantState.getTarget().getAltitude() - plantState.getPosition().getAltitude())
                .isLessThanOrEqualTo(10.0);
            assertThat(plantState.getTarget().getAltitude() - plantState.getPosition().getAltitude())
                .isGreaterThanOrEqualTo(0.0);
        } while (!e.calculateState());

        writer.close();

        assertThat(plantState.getElevation()).isEqualTo(0.5 * Math.PI, offset(1E-3));
        assertThat(plantState.getVelocity()).isEqualTo(0.0, offset(1E-3));
        assertThat(plantState.getPosition().getAltitude()).isEqualTo(10.0, offset(1E-3));
        assertThat(iterationCounter).isEqualTo(549);
    }

    /**
     * Should land plant.
     * 
     * @throws IOException thrown in case of errors.
     */
    @Test
    void shouldLandPlant() throws IOException
    {
        PlantState plantState = new PlantState(initialPlantState);
        plantState.getPosition().setAltitude(20.0);
        PlantStateEstimator e = new PlantStateEstimatorImpl(config, plantState, State.LAND);

        PrintWriter writer = new PrintWriter("target/land.csv");
        writer.println("posLat;posLon;posAlt;flyTime;heading;dstLat;dstLon;dstAlt;v;a;elevation;remCap");

        int iterationCounter = 0;
        do
        {
            ++iterationCounter;
            printStateToFile(plantState, writer);
            assertThat(plantState.getPosition().getLatitude())
                .isEqualTo(plantState.getTarget().getLatitude(), offset(1E-5));
            assertThat(plantState.getPosition().getLongitude())
                .isEqualTo(plantState.getTarget().getLongitude(), offset(1E-5));
            assertThat(plantState.getPosition().getAltitude() - plantState.getTarget().getAltitude())
                .isLessThanOrEqualTo(20.0);
            assertThat(plantState.getPosition().getAltitude() - plantState.getTarget().getAltitude())
                .isGreaterThanOrEqualTo(-1E-3);
        } while (!e.calculateState());

        writer.close();

        assertThat(plantState.getElevation()).isEqualTo(-0.5 * Math.PI, offset(1E-3));
        assertThat(plantState.getVelocity()).isEqualTo(0.0, offset(1E-3));
        assertThat(plantState.getPosition().getAltitude()).isEqualTo(0.0, offset(1E-3));
        assertThat(iterationCounter).isEqualTo(635);
    }

    /**
     * Should fly horizontally.
     * 
     * @throws IOException thrown in case of errors.
     */
    @Test
    void shouldFlyHorizontallyNorthSouth() throws IOException
    {
        PlantState plantState = new PlantState(initialPlantState);
        plantState.getPosition().setAltitude(20.0);
        plantState.setTarget(new PolarCoordinate(47.82146, 13.04085, 20.0));
        PlantStateEstimator e = new PlantStateEstimatorImpl(config, plantState, State.FLIGHT);

        PrintWriter writer = new PrintWriter("target/flyHorizontallyNorthSouth.csv");
        writer.println("posLat;posLon;posAlt;flyTime;heading;dstLat;dstLon;dstAlt;v;a;elevation;remCap");

        int iterationCounter = 0;
        do
        {
            ++iterationCounter;
            printStateToFile(plantState, writer);
            assertThat(Math.abs(plantState.getPosition().getAltitude() - plantState.getTarget().getAltitude()))
                .isLessThanOrEqualTo(1.02E-3);
        } while (!e.calculateState());

        writer.close();

        assertThat(plantState.getElevation())
            .isEqualTo(0.0, offset(1E-3));
        assertThat(plantState.getVelocity())
            .isEqualTo(0.0, offset(1E-3));
        assertThat(plantState.getPosition().getAltitude())
            .isEqualTo(20.0, offset(1E-3));
        assertThat(iterationCounter)
            .isEqualTo(544);
    }

    /**
     * Should fly horizontally.
     * 
     * @throws IOException thrown in case of errors.
     */
    @Test
    void shouldFlyHorizontallyEastWest() throws IOException
    {
        PlantState plantState = new PlantState(initialPlantState);
        plantState.getPosition().setAltitude(20.0);
        plantState.setTarget(new PolarCoordinate(47.82199, 13.04000, 20.0));
        PlantStateEstimator e = new PlantStateEstimatorImpl(config, plantState, State.FLIGHT);

        PrintWriter writer = new PrintWriter("target/flyHorizontallyEastWest.csv");
        writer.println("posLat;posLon;posAlt;flyTime;heading;dstLat;dstLon;dstAlt;v;a;elevation;remCap");

        int iterationCounter = 0;
        do
        {
            ++iterationCounter;
            printStateToFile(plantState, writer);
            writer.flush();
            //if (Math.abs(plantState.getPosition().getAltitude() - plantState.getTarget().getAltitude()) > 1E-3)
            //{
            //    System.out.println ("bugger");
            //}
            assertThat(Math.abs(plantState.getPosition().getAltitude() - plantState.getTarget().getAltitude()))
                .isLessThanOrEqualTo(1.04E-3);
        } while (!e.calculateState());

        writer.close();

        assertThat(plantState.getElevation())
            .isEqualTo(0.0, offset(1E-3));
        assertThat(plantState.getVelocity())
            .isEqualTo(0.0, offset(1E-3));
        assertThat(plantState.getPosition().getAltitude())
            .isEqualTo(20.0, offset(1E-3));
        assertThat(iterationCounter)
            .isEqualTo(566);
    }

    /**
     * Should land plant.
     * 
     * @throws IOException thrown in case of errors.
     */
    @Test
    void shouldBreakBeforeFlyingToNewPoint() throws IOException
    {
        PlantState plantState = new PlantState(initialPlantState);
        plantState.getPosition().setAltitude(20.0);
        plantState.setHeading(-0.75 * Math.PI);
        plantState.setVelocity(15.0);
        plantState.setTarget(new PolarCoordinate(47.82146, 13.04321, 20.0));
        PlantStateEstimator e = new PlantStateEstimatorImpl(config, plantState, State.FLIGHT);

        PrintWriter writer = new PrintWriter("target/breakBeforeNewPoint.csv");
        writer.println("posLat;posLon;posAlt;flyTime;heading;dstLat;dstLon;dstAlt;v;a;elevation;remCap");

        int iterationCounter = 0;
        do
        {
            ++iterationCounter;
            printStateToFile(plantState, writer);
            assertThat(Math.abs(plantState.getPosition().getAltitude() - plantState.getTarget().getAltitude()))
                .isLessThanOrEqualTo(2E-3);
        } while (!e.calculateState());

        writer.close();

        assertThat(plantState.getElevation())
            .isEqualTo(0.0, offset(1E-3));
        assertThat(plantState.getVelocity())
            .isEqualTo(0.0, offset(1E-3));
        assertThat(plantState.getPosition().getAltitude())
            .isEqualTo(20.0, offset(1E-3));
        assertThat(iterationCounter)
            .isEqualTo(1217);
    }

    static Stream<Arguments> numberDataProvider()
    {
        return Stream.of(
            arguments(-10D),
            arguments(0D),
            arguments(1D),
            arguments(100D));
    }

    @ParameterizedTest
    @MethodSource("numberDataProvider")
    void shouldStoreRemainingBatteryCapacity(double capacity)
    {
        PlantState plantState = new PlantState(initialPlantState);
        plantState.setRemainingBatteryCapacity(capacity);
        assertThat(plantState.getRemainingBatteryCapacity()).isEqualTo(capacity, offset(1E-6));
    }

    @Test
    void shouldHaveFullStateMap()
    {
        PlantState plantState = new PlantState(initialPlantState);
        Map<String, List<String>> map = plantState.getStateMap("test");

        assertThat(map.keySet())
            .isNotNull()
            .isNotEmpty()
            .contains("test.acceleration", "test.elevation", "test.flyingTime", "test.heading",
                "test.position", "test.batteryCapacity", "test.target", "test.velocity");

        assertThat(map.get("test.acceleration"))
            .isNotNull().isNotEmpty().containsExactly("0.00");
        assertThat(map.get("test.elevation"))
            .isNotNull().isNotEmpty().containsExactly("0.000");
        assertThat(map.get("test.flyingTime"))
            .isNotNull().isNotEmpty().containsExactly("0.00");
        assertThat(map.get("test.heading"))
            .isNotNull().isNotEmpty().containsExactly("0");
        assertThat(map.get("test.position"))
            .isNotNull().isNotEmpty().containsExactly("47.82199000", "13.04085000", "0.000");
        assertThat(map.get("test.batteryCapacity"))
            .isNotNull().isNotEmpty().containsExactly("0.0");
        assertThat(map.get("test.target"))
            .isNotNull().isNotEmpty().containsExactly("0.00000000", "0.00000000", "0.000");
        assertThat(map.get("test.velocity"))
            .isNotNull().isNotEmpty().containsExactly("0.00");
    }

    /**
     * @param plantState the plant state.
     * @param writer the writer.
     */
    private void printStateToFile(PlantState plantState, PrintWriter writer)
    {
        double posLat = plantState.getPosition().getLatitude();
        double posLon = plantState.getPosition().getLongitude();
        double posAlt = plantState.getPosition().getAltitude();
        double flyTime = plantState.getFlyingTime();
        double heading = plantState.getHeading();
        double dstLat = plantState.getTarget().getLatitude();
        double dstLon = plantState.getTarget().getLongitude();
        double dstAlt = plantState.getTarget().getAltitude();
        double v = plantState.getVelocity();
        double a = plantState.getAcceleration();
        double elevation = plantState.getElevation();
        double remCap = plantState.getRemainingBatteryCapacity();

        writer.printf("%.8f;%.8f;%.8f;%.3f;%.1f;%.8f;%.8f;%.8f;%.4f;%.4f;%.3f;%.1f\n",
            posLat, posLon, posAlt, flyTime, Math.toDegrees(heading), dstLat, dstLon, dstAlt, v, a,
            Math.toDegrees(elevation), remCap);
    }

}
