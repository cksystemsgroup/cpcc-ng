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

package at.uni_salzburg.cs.cpcc.ros.sim.osm;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;

/**
 * An OpenStreetMap camera.
 */
public class Camera
{
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    
    private TileCache tileCache;
    private BufferedImage map;
    private MercatorProjection topLeftTile;
    private MercatorProjection bottomRightTile;
    private Graphics[][] tiles;
    private Configuration cfg;
    private int mapWidth;
    private int mapHeight;

    /**
     * @param config the configuration.
     */
    public Camera(Configuration config)
    {
        tileCache = new TileCache(config);
        cfg = config;
        mapWidth = cfg.getCameraWidth() / cfg.getTileWidth() + 2;
        mapHeight = cfg.getCameraHeight() / cfg.getTileHeight() + 2;
        initMap();
    }

    /**
     * Initialize the tile map.
     */
    private void initMap()
    {
        map = new BufferedImage(mapWidth * cfg.getTileWidth(), mapHeight * cfg.getTileHeight()
            , BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = map.createGraphics();
        tiles = new Graphics[mapWidth][mapHeight];

        for (int w = 0; w < mapWidth; ++w)
        {
            for (int h = 0; h < mapHeight; ++h)
            {
                tiles[w][h] = g2d.create(w * cfg.getTileWidth(), h * cfg.getTileHeight()
                    , cfg.getTileWidth(), cfg.getTileHeight());
            }
        }
    }

    /**
     * @param position the position to capture an image.
     * @return the captured image.
     * @throws IOException thrown in case of errors.
     */
    public byte[] getImage(PolarCoordinate position) throws IOException
    {
        if (position == null)
        {
            return EMPTY_BYTE_ARRAY;
        }

        PolarCoordinate pos = new PolarCoordinate(position);

        if (pos.getAltitude() > 200)
        {
            pos.setAltitude(200);
        }

        if (cfg.getOriginPosition() != null)
        {
            pos = cfg.getGeodeticSystem().walk(cfg.getOriginPosition()
                , -pos.getLatitude(), pos.getLongitude(), pos.getAltitude());
        }

        double dx = pos.getAltitude() * Math.tan(cfg.getCameraApertureAngle() / 2.0);
        double dy = dx * cfg.getCameraHeight() / cfg.getCameraWidth();

        PolarCoordinate topLeftPosition = cfg.getGeodeticSystem().walk(pos, -dy, -dx, 0);
        PolarCoordinate bottomRightPosition = cfg.getGeodeticSystem().walk(pos, dy, dx, 0);

        MercatorProjection newTopLeftTile = new MercatorProjection(cfg.getZoomLevel()
            , topLeftPosition.getLatitude(), topLeftPosition.getLongitude());

        MercatorProjection newBottomRightTile = new MercatorProjection(cfg.getZoomLevel()
            , bottomRightPosition.getLatitude(), bottomRightPosition.getLongitude());

        boolean reloadTiles = topLeftTile == null || !topLeftTile.equalsTile(newTopLeftTile);
        topLeftTile = newTopLeftTile;
        bottomRightTile = newBottomRightTile;

        int newMapWidth = bottomRightTile.getxTile() - topLeftTile.getxTile() + 1;
        int newMapHeight = bottomRightTile.getyTile() - topLeftTile.getyTile() + 1;

        if (newMapWidth > mapWidth || newMapHeight > mapHeight)
        {
            mapWidth = newMapWidth;
            mapHeight = newMapHeight;
            initMap();
            reloadTiles = true;
        }

        if (reloadTiles)
        {
            loadTiles();
        }

        return extractImage();
    }

    /**
     * @throws IOException thrown in case of errors.
     */
    private void loadTiles()
    {
        int xt = topLeftTile.getxTile();
        int yt = topLeftTile.getyTile();

        for (int w = 0; w < mapWidth; ++w)
        {
            for (int h = 0; h < mapHeight; ++h)
            {
                BufferedImage image;
                try
                {
                    File f = tileCache.getTile(cfg.getZoomLevel(), xt + w, yt + h);
                    image = ImageIO.read(f);
                }
                catch (IOException e)
                {
                    image = new BufferedImage(cfg.getTileWidth(), cfg.getTileHeight(), BufferedImage.TYPE_INT_ARGB);
                }
                tiles[w][h].drawImage(image, 0, 0, null);
            }
        }
    }

    /**
     * @return the extracted image.
     * @throws IOException thrown in case of errors.
     */
    private byte[] extractImage() throws IOException
    {
        double dTilesX = bottomRightTile.getxTile() - topLeftTile.getxTile();
        double dTilesY = bottomRightTile.getyTile() - topLeftTile.getyTile();

        int height = (int) (dTilesY * cfg.getTileHeight()) + bottomRightTile.getyPixel() - topLeftTile.getyPixel();
        int width = (int) (dTilesX * cfg.getTileWidth()) + bottomRightTile.getxPixel() - topLeftTile.getxPixel();

        if (height < 1)
        {
            height = 1;
        }

        if (width < 1)
        {
            width = 1;
        }

        BufferedImage image = map.getSubimage(topLeftTile.getxPixel(), topLeftTile.getyPixel(), width, height);
        Image scaledImage = image.getScaledInstance(cfg.getCameraWidth(), cfg.getCameraHeight(), 0);

        BufferedImage cameraImage =
            new BufferedImage(cfg.getCameraWidth(), cfg.getCameraHeight(), BufferedImage.TYPE_INT_RGB);
        cameraImage.createGraphics().drawImage(scaledImage, 0, 0, null);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(cameraImage, "PNG", bos);

        return bos.toByteArray();
    }
}
