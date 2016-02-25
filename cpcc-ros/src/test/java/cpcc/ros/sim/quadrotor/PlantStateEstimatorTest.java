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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ros.node.NodeConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.entities.PolarCoordinate;
import cpcc.ros.sim.quadrotor.Configuration;
import cpcc.ros.sim.quadrotor.PlantState;
import cpcc.ros.sim.quadrotor.PlantStateEstimator;
import cpcc.ros.sim.quadrotor.State;
import big_actor_msgs.LatLngAlt;

/**
 * PlantStateEstimatorTest
 */
public class PlantStateEstimatorTest
{
    @SuppressWarnings("serial")
    private static final Map<String, List<String>> configMap = new HashMap<String, List<String>>()
    {
        {
            put("topicRoot", Arrays.asList("/mav01"));
            put("origin", Arrays.asList("47.82199", "13.04085", "0"));
            put("maxVelocity", Arrays.asList("20"));
            put("maxAcceleration", Arrays.asList("3"));
            put("updateCycle", Arrays.asList("20"));
            put("idlePower", Arrays.asList("0"));
            put("hoverPower", Arrays.asList("0"));
            put("mass", Arrays.asList("2.2"));
            put("batteryCapacity", Arrays.asList("40"));
            put("rechargingTime", Arrays.asList("0"));
            put("takeOffHeight", Arrays.asList("10"));
            put("takeOffVelocity", Arrays.asList("2"));
            put("takeOffAcceleration", Arrays.asList("0.5"));
            put("landingVelocity", Arrays.asList("2"));
            put("landingAcceleration", Arrays.asList("1"));
        }
    };

    private Configuration config;
    private PlantState initialPlantState;

    /**
     * Tasks to be done before running tests.
     */
    @BeforeClass
    public void beforeClass()
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
    public void shouldTakeOffPlant() throws IOException
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
    public void shouldLandPlant() throws IOException
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
    public void shouldFlyHorizontallyNorthSouth() throws IOException
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
    public void shouldFlyHorizontallyEastWest() throws IOException
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
    public void shouldBreakBeforeFlyingToNewPoint() throws IOException
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

    @DataProvider
    public Object[][] numberDataProvider()
    {
        return new Object[][]{
            new Object[]{-10D},
            new Object[]{0D},
            new Object[]{1D},
            new Object[]{100D},
        };
    }

    @Test(dataProvider = "numberDataProvider")
    public void shouldStoreRemainingBatteryCapacity(double capacity)
    {
        PlantState plantState = new PlantState(initialPlantState);
        plantState.setRemainingBatteryCapacity(capacity);
        assertThat(plantState.getRemainingBatteryCapacity())
            .isNotNull().isEqualTo(capacity, offset(1E-6));
    }

    @Test
    public void shouldHaveFullStateMap()
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
            Math.toDegrees(elevation), remCap
            );
    }

}
