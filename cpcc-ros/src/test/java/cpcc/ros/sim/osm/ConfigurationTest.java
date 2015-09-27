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

package cpcc.ros.sim.osm;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ros.node.NodeConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cpcc.core.utils.GeodeticSystem;
import cpcc.ros.sim.osm.Configuration;

/**
 * ConfigurationTest
 */
public class ConfigurationTest
{
    NodeConfiguration nodeConfiguration;
    Map<String, List<String>> config;
    
    @BeforeMethod
    public void setUp()
    {
        nodeConfiguration = mock(NodeConfiguration.class);
        
        config = new HashMap<String, List<String>>();
        
        config.put(Configuration.CFG_TOPIC_ROOT, Arrays.asList("/quad879"));
        config.put(Configuration.CFG_ORIGIN, Arrays.asList("37.80806","-122.42661","2.1"));
        config.put(Configuration.CFG_GPS_TOPIC, Arrays.asList("gps"));
        config.put(Configuration.CFG_CAMERA_APERTURE_ANGLE, Arrays.asList("2"));
        config.put(Configuration.CFG_CAMERA_WIDTH, Arrays.asList("300"));
        config.put(Configuration.CFG_CAMERA_HEIGTH, Arrays.asList("200"));
        config.put(Configuration.CFG_ZOOM_LEVEL, Arrays.asList("17"));
        config.put(Configuration.CFG_TILE_WIDTH, Arrays.asList("140"));
        config.put(Configuration.CFG_TILE_HEIGTH, Arrays.asList("128"));
        config.put(Configuration.CFG_TILE_CACHE_DIR, Arrays.asList("/tmp/test/tile/dir"));
        config.put(Configuration.CFG_TILE_DOWNLOAD_URL, Arrays.asList("http://my.tile.server.eu/%1$d/%2$d/%3$d.png"));
    }
    
    @Test
    public void shouldParseConfigurationWithoutOriginCorrectly()
    {
        config.remove(Configuration.CFG_ORIGIN);
        
        Configuration cfg = new Configuration(nodeConfiguration, config);
        assertThat(cfg.getOriginPosition()).isNull();
        checkParsedValues(cfg);
        verifyZeroInteractions(nodeConfiguration);
    }

    @Test
    public void shouldParseConfigurationWithOriginCorrectly()
    {
        assertThat(config).containsKey(Configuration.CFG_ORIGIN);
        
        Configuration cfg = new Configuration(nodeConfiguration, config);
        assertThat(cfg.getOriginPosition()).isNotNull();
        assertThat(cfg.getOriginPosition().getLatitude()).isEqualTo(37.80806, offset(1E-6));
        assertThat(cfg.getOriginPosition().getLongitude()).isEqualTo(-122.42661, offset(1E-6));
        assertThat(cfg.getOriginPosition().getAltitude()).isEqualTo(2.1, offset(1E-6));
        checkParsedValues(cfg);
        verifyZeroInteractions(nodeConfiguration);
    }
    
    private void checkParsedValues(Configuration cfg)
    {
        assertThat(cfg.getTopicRoot()).isNotNull().isEqualTo(config.get(Configuration.CFG_TOPIC_ROOT).get(0));
        assertThat(cfg.getGpsTopic()).isNotNull().isEqualTo(config.get(Configuration.CFG_GPS_TOPIC).get(0));
        assertThat(cfg.getCameraApertureAngle()).isNotNull().isEqualTo(2.0, offset(1E-5));
        assertThat(cfg.getCameraWidth()).isNotNull().isEqualTo(Integer.parseInt(config.get(Configuration.CFG_CAMERA_WIDTH).get(0)));
        assertThat(cfg.getCameraHeight()).isNotNull().isEqualTo(Integer.parseInt(config.get(Configuration.CFG_CAMERA_HEIGTH).get(0)));
        assertThat(cfg.getZoomLevel()).isNotNull().isEqualTo(Integer.parseInt(config.get(Configuration.CFG_ZOOM_LEVEL).get(0)));
        assertThat(cfg.getTileWidth()).isNotNull().isEqualTo(Integer.parseInt(config.get(Configuration.CFG_TILE_WIDTH).get(0)));
        assertThat(cfg.getTileHeight()).isNotNull().isEqualTo(Integer.parseInt(config.get(Configuration.CFG_TILE_HEIGTH).get(0)));
        assertThat(cfg.getTileCacheBaseDir()).isNotNull().isEqualTo(config.get(Configuration.CFG_TILE_CACHE_DIR).get(0));
        assertThat(cfg.getTileServerUrl()).isNotNull().isEqualTo(config.get(Configuration.CFG_TILE_DOWNLOAD_URL).get(0));
        assertThat(cfg.getGeodeticSystem()).isNotNull().isInstanceOf(GeodeticSystem.class);
    }
}
