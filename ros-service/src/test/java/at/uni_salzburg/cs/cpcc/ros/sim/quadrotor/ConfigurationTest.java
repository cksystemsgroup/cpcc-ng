/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.ros.sim.quadrotor;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ros.node.NodeConfiguration;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.utilities.GeodeticSystem;

/**
 * ConfigurationTest
 */
public class ConfigurationTest
{
    @SuppressWarnings("serial")
    private static final Map<String, List<String>> CONFIG = new HashMap<String, List<String>>()
    {
        {
            put("topicRoot", Arrays.asList("/mav93"));
            put("origin", Arrays.asList("37.80806", "-122.42661", "0.3"));
            put("maxVelocity", Arrays.asList("10"));
            put("maxAcceleration", Arrays.asList("2"));
            put("precision", Arrays.asList("3"));
            put("updateCycle", Arrays.asList("100"));
            put("idlePower", Arrays.asList("17"));
            put("hoverPower", Arrays.asList("179"));
            put("mass", Arrays.asList("2.2"));
            put("batteryCapacity", Arrays.asList("41.3"));
            put("rechargingTime", Arrays.asList("40"));
            put("takeOffHeight", Arrays.asList("10"));
            put("takeOffVelocity", Arrays.asList("2.7"));
            put("takeOffAcceleration", Arrays.asList("1.3"));
            put("landingVelocity", Arrays.asList("1"));
            put("landingAcceleration", Arrays.asList("0.5"));
        }
    };

    @Test
    public void shouldParseConfiguration()
    {
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPrivate();
        Configuration cfg = new Configuration(nodeConfiguration, CONFIG);

        assertEquals(cfg.getBatteryCapacity(), 41.3, 1E-4);
        assertTrue(cfg.getGeodeticSystem() instanceof GeodeticSystem);
        assertEquals(cfg.getHoverPower(), 179.0, 1E-3);
        assertEquals(cfg.getIdlePower(), 17.0, 1E-3);
        assertEquals(cfg.getLandingAcceleration(), 0.5, 1E-3);
        assertEquals(cfg.getLandingVelocity(), 1.0, 1E-3);
        assertEquals(cfg.getMass(), 2.2, 1E-3);
        assertEquals(cfg.getMaxAcceleration(), 2.0, 1E-3);
        assertEquals(cfg.getMaxVelocity(), 10.0, 1E-3);
        assertEquals(cfg.getOrigin().getLatitude(), 37.80806, 1E-8);
        assertEquals(cfg.getOrigin().getLongitude(), -122.42661, 1E-8);
        assertEquals(cfg.getOrigin().getAltitude(), 0.3, 1E-8);
        assertEquals(cfg.getPrecision(), 3.0, 1E-3);
        assertEquals(cfg.getRechargingTime(), 40.0, 1E-3);
        assertEquals(cfg.getTakeOffAcceleration(), 1.3, 1E-3);
        assertEquals(cfg.getTakeOffHeight(), 10.0, 1E-3);
        assertEquals(cfg.getTakeOffVelocity(), 2.7, 1E-3);
        assertEquals(cfg.getTopicRoot(), "/mav93");
        assertEquals(cfg.getUpdateCycle(), 100);
    }
}
