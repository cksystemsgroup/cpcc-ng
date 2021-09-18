// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, offset(or
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.ros.node.NodeConfiguration;

import cpcc.core.utils.GeodeticSystem;

/**
 * ConfigurationTest
 */
class ConfigurationTest
{
    private static final Map<String, List<String>> CONFIG = Collections.unmodifiableMap(Stream
        .of(Pair.of("topicRoot", Arrays.asList("/mav93")),
            Pair.of("origin", Arrays.asList("37.80806", "-122.42661", "0.3")),
            Pair.of("maxVelocity", Arrays.asList("10")),
            Pair.of("maxAcceleration", Arrays.asList("2")),
            Pair.of("precision", Arrays.asList("3")),
            Pair.of("updateCycle", Arrays.asList("100")),
            Pair.of("idlePower", Arrays.asList("17")),
            Pair.of("hoverPower", Arrays.asList("179")),
            Pair.of("mass", Arrays.asList("2.2")),
            Pair.of("batteryCapacity", Arrays.asList("41.3")),
            Pair.of("rechargingTime", Arrays.asList("40")),
            Pair.of("takeOffHeight", Arrays.asList("10")),
            Pair.of("takeOffVelocity", Arrays.asList("2.7")),
            Pair.of("takeOffAcceleration", Arrays.asList("1.3")),
            Pair.of("landingVelocity", Arrays.asList("1")),
            Pair.of("landingAcceleration", Arrays.asList("0.5")))
        .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)));

    @Test
    void shouldParseConfiguration()
    {
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPrivate();
        Configuration cfg = new Configuration(nodeConfiguration, CONFIG);

        assertThat(cfg.getBatteryCapacity()).isEqualTo(41.3, offset(1E-4));
        assertThat(cfg.getGeodeticSystem()).isInstanceOf(GeodeticSystem.class);
        assertThat(cfg.getHoverPower()).isEqualTo(179.0, offset(1E-3));
        assertThat(cfg.getIdlePower()).isEqualTo(17.0, offset(1E-3));
        assertThat(cfg.getLandingAcceleration()).isEqualTo(0.5, offset(1E-3));
        assertThat(cfg.getLandingVelocity()).isEqualTo(1.0, offset(1E-3));
        assertThat(cfg.getMass()).isEqualTo(2.2, offset(1E-3));
        assertThat(cfg.getMaxAcceleration()).isEqualTo(2.0, offset(1E-3));
        assertThat(cfg.getMaxVelocity()).isEqualTo(10.0, offset(1E-3));
        assertThat(cfg.getOrigin().getLatitude()).isEqualTo(37.80806, offset(1E-8));
        assertThat(cfg.getOrigin().getLongitude()).isEqualTo(-122.42661, offset(1E-8));
        assertThat(cfg.getOrigin().getAltitude()).isEqualTo(0.3, offset(1E-8));
        assertThat(cfg.getPrecision()).isEqualTo(3.0, offset(1E-3));
        assertThat(cfg.getRechargingTime()).isEqualTo(40.0, offset(1E-3));
        assertThat(cfg.getTakeOffAcceleration()).isEqualTo(1.3, offset(1E-3));
        assertThat(cfg.getTakeOffHeight()).isEqualTo(10.0, offset(1E-3));
        assertThat(cfg.getTakeOffVelocity()).isEqualTo(2.7, offset(1E-3));
        assertThat(cfg.getTopicRoot()).isEqualTo("/mav93");
        assertThat(cfg.getUpdateCycle()).isEqualTo(100);
    }
}
