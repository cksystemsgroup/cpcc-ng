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

import java.util.List;
import java.util.Map;

import org.ros.node.NodeConfiguration;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.utils.ConfigUtils;
import cpcc.core.utils.GeodeticSystem;
import cpcc.core.utils.WGS84;

/**
 * Configuration
 */
public class Configuration
{
    static final String CFG_TOPIC_ROOT = "topicRoot";
    static final String CFG_ORIGIN = "origin";
    static final String CFG_GPS_TOPIC = "gps";
    static final String CFG_CAMERA_APERTURE_ANGLE = "cameraApertureAngle";
    static final String CFG_CAMERA_WIDTH = "cameraWidth";
    static final String CFG_CAMERA_HEIGTH = "cameraHeight";
    static final String CFG_ZOOM_LEVEL = "osmZoomLevel";
    static final String CFG_TILE_WIDTH = "osmTileWidth";
    static final String CFG_TILE_HEIGTH = "osmTileHeight";
    static final String CFG_TILE_CACHE_DIR = "osmTileCache.dir";
    static final String CFG_TILE_DOWNLOAD_URL = "osmTileServerUrl";

    private static final String TILE_DEFAULT_DOWNLOAD_URL =
        "http://otile1.mqcdn.com/tiles/1.0.0/sat/%1$d/%2$d/%3$d.png";

    // OK:
    // http://a.tile.osm.org/%1$d/%2$d/%3$d.png
    // http://{s}.tile.osm.org/{z}/{x}/{y}.png
    // http://otile1.mqcdn.com/tiles/1.0.0/sat/%1$d/%2$d/%3$d.png
    // http://c.tile.openstreetmap.org/%1$d/%2$d/%3$d.png
    // http://tile.opencyclemap.org/cycle/%1$d/%2$d/%3$d.png

    private String topicRoot;
    private PolarCoordinate originPosition = null;
    private String gpsTopic;
    private GeodeticSystem geodeticSystem;
    private double cameraApertureAngle;
    private int cameraWidth;
    private int cameraHeight;
    private int tileWidth;
    private int tileHeight;
    private int zoomLevel;
    private String tileCacheBaseDir;
    private String tileServerUrl;

    /**
     * @param nodeConfiguration the node configuration.
     * @param config the parsed configuration parameters.
     */
    public Configuration(NodeConfiguration nodeConfiguration, Map<String, List<String>> config)
    {
        topicRoot = config.get(CFG_TOPIC_ROOT).get(0);
        gpsTopic = ConfigUtils.parseString(config, CFG_GPS_TOPIC, 0, "/unknown");

        geodeticSystem = new WGS84();

        if (config.containsKey(CFG_ORIGIN))
        {
            originPosition = ConfigUtils.parsePolarCoordinate(config, CFG_ORIGIN, 0);
        }

        cameraApertureAngle = ConfigUtils.parseDouble(config, CFG_CAMERA_APERTURE_ANGLE, 0, 1.0);
        cameraWidth = ConfigUtils.parseInteger(config, CFG_CAMERA_WIDTH, 0, 320);
        cameraHeight = ConfigUtils.parseInteger(config, CFG_CAMERA_HEIGTH, 0, 240);
        tileWidth = ConfigUtils.parseInteger(config, CFG_TILE_WIDTH, 0, 256);
        tileHeight = ConfigUtils.parseInteger(config, CFG_TILE_HEIGTH, 0, 256);
        zoomLevel = ConfigUtils.parseInteger(config, CFG_ZOOM_LEVEL, 0, 18);

        String defaultCacheDir = System.getProperty("user.home") + "/.cpcc/tiles";

        tileCacheBaseDir = ConfigUtils.parseString(config, CFG_TILE_CACHE_DIR, 0, defaultCacheDir);
        tileServerUrl = ConfigUtils.parseString(config, CFG_TILE_DOWNLOAD_URL, 0, TILE_DEFAULT_DOWNLOAD_URL);
    }

    /**
     * @return the topic root
     */
    public String getTopicRoot()
    {
        return topicRoot;
    }

    /**
     * @return the origin position
     */
    public PolarCoordinate getOriginPosition()
    {
        return originPosition;
    }

    /**
     * @return the GPS topic
     */
    public String getGpsTopic()
    {
        return gpsTopic;
    }

    /**
     * @return the geodeticSystem
     */
    public GeodeticSystem getGeodeticSystem()
    {
        return geodeticSystem;
    }

    /**
     * @return the camera aperture angle
     */
    public double getCameraApertureAngle()
    {
        return cameraApertureAngle;
    }

    /**
     * @return the camera width
     */
    public int getCameraWidth()
    {
        return cameraWidth;
    }

    /**
     * @return the camera height
     */
    public int getCameraHeight()
    {
        return cameraHeight;
    }

    /**
     * @return the tile width
     */
    public int getTileWidth()
    {
        return tileWidth;
    }

    /**
     * @return the tile height
     */
    public int getTileHeight()
    {
        return tileHeight;
    }

    /**
     * @return the zoom level
     */
    public int getZoomLevel()
    {
        return zoomLevel;
    }

    /**
     * @return the tile cache base-directory
     */
    public String getTileCacheBaseDir()
    {
        return tileCacheBaseDir;
    }

    /**
     * @return the tile server URL
     */
    public String getTileServerUrl()
    {
        return tileServerUrl;
    }
}
