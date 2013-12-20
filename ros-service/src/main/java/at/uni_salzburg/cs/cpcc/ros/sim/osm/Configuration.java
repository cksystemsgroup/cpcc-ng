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
package at.uni_salzburg.cs.cpcc.ros.sim.osm;

import java.util.List;
import java.util.Map;

import org.ros.node.NodeConfiguration;

import at.uni_salzburg.cs.cpcc.core.utils.ConfigUtils;
import at.uni_salzburg.cs.cpcc.core.utils.GeodeticSystem;
import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;
import at.uni_salzburg.cs.cpcc.core.utils.WGS84;

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

    private static final String TILE_DEFAULT_CACHE_DIR = "/tmp/tiles";
    private static final String TILE_DEFAULT_DOWNLOAD_URL =
        "http://otile1.mqcdn.com/tiles/1.0.0/sat/%1$d/%2$d/%3$d.png";

    // OK:
    // http://otile1.mqcdn.com/tiles/1.0.0/sat/%1$d/%2$d/%3$d.png
    // http://mtile03.mqcdn.com/tiles/1.0.0/vy/sat/%1$d/%2$d/%3$d.png
    //?? http://khm1.google.com/kh/v=128&src=app&x=%2$d&y=%3$d&z=%1$d&s=Galileo

    // http://khm1.google.com/kh/v=128&src=app&x={x}&y={y}&z={z}&s=Galileo
    // http://otile1.mqcdn.com/tiles/1.0.0/map/%1$d/%2$d/%3$d.png
    // http://c.tile.openstreetmap.org/18/140611/90996.png
    // http://c.tile.openstreetmap.org/%1$d/%2$d/%3$d.png
    // http://mtile03.mqcdn.com/tiles/1.0.0/vy/sat/18/54624/99490.png
    // http://otile1.mqcdn.com/tiles/1.0.0/sat/%1$d/%2$d/%3$d.jpg
    // http://khm1.google.com/kh/v=84&x=%2$d&y=%3$d&z=%1$d&s=Gal
    // https://khms1.google.at/kh/v=117&src=app&x=%2$d&y=%3$d&z=%1$d&s=Gali
    // http://a.tile.osm.org/%1$d/%2$d/%3$d.png
    // https://khms0.google.com/kh?v=81&src=app&x=%2$d&y=%3$d&z=%1$d&s=G&deg=0
    //
    // http://khm1.google.com/kh/v=128&src=app&x={x}&y={y}&z={z}&s=Galileo
    // http://{s}.tile.osm.org/{z}/{x}/{y}.png
    // http://tile.opencyclemap.org/cycle/%1$d/%2$d/%3$d.png
    // http://otile1.mqcdn.com/tiles/1.0.0/sat/15/5240/12661.jpg
    // http://mt1.google.com/vt/lyrs=y&x=[x]&y=[y]&z=[z]
    // http://khm1.google.com/kh/v=84&x=166&y=397&z=10&s=Gal
    // http://khm.google.com/maptilecompress/t=3&x=[X]&y=[Y]&z=[INVZ] 
    // http://khm.google.com/maptilecompress/t=3&x=1001&y=1030&z=11
    //
    // https://khms1.google.at/kh/v=137&src=app&x=277&y=181&z=9&s=Gali
    // https://khms1.google.at/kh/v=137&src=app&x=277&y=179&z=9&s=Ga
    // https://khms1.google.de/kh/v=117&src=app&x=17623&s=&y=10959&z=15&s=Gali

    // https://mts0.google.com/vt/lyrs=h@231053698&hl=de&src=app&opts=o&deg=0&x=70278&s=&y=51482&z=17&s=Gali&deg=0
    // https://mts0.google.com/vt/lyrs=h@231017399&hl=de&src=app&opts=o&deg=0&x=70282&s=&y=51482&z=17&s=&deg=0
    // https://khms0.google.com/kh?v=81&src=app&x=140560&y=102961&z=18&s=G&deg=0

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
        // gpsTopic = config.get(CFG_GPS_TOPIC).get(0);
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

        tileCacheBaseDir = ConfigUtils.parseString(config, CFG_TILE_CACHE_DIR, 0, TILE_DEFAULT_CACHE_DIR);
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
