/*
 * This code is part of the CPCC-NG and the JNavigator project.
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OpenStreetMap tile cache
 */
public class TileCache
{
    private final static Logger LOG = LoggerFactory.getLogger(TileCache.class);

    private final static String FORMAT_TILE_CACHE_DIR = "%1$s/%2$d/%3$d";
    private final static String FORMAT_TILE_CACHE_FILE = "%1$s/%2$d/%3$d/%4$d.png";

    private String tileCacheBaseDir;

    private String tileServerUrl;

    /**
     * @param config the configuration.
     */
    public TileCache(Configuration config)
    {
        tileCacheBaseDir = config.getTileCacheBaseDir();
        tileServerUrl = config.getTileServerUrl();
    }

    /**
     * @param zoom the zoom level
     * @param x the X-coordinate of the tile.
     * @param y the Y-coordinate of the tile.
     * @return the tile as a <code>File</code> object.
     * @throws IOException thrown in case of errors.
     */
    public File getTile(int zoom, int x, int y) throws IOException
    {
        String tileCacheFileName = String.format(Locale.US, FORMAT_TILE_CACHE_FILE, tileCacheBaseDir, zoom, x, y);

        File tileCacheFile = new File(tileCacheFileName);

        if (tileCacheFile.exists())
        {
            LOG.debug(String.format("Cached tile found for zoom=%d, x=%d, y=%d", zoom, x, y));
            return tileCacheFile;
        }

        String tileCacheDirName = String.format(Locale.US, FORMAT_TILE_CACHE_DIR, tileCacheBaseDir, zoom, x, y);
        File tileCacheDir = new File(tileCacheDirName);
        if (!tileCacheDir.exists())
        {
            tileCacheDir.mkdirs();
        }

        String tileDownloadUrl = String.format(Locale.US, tileServerUrl, zoom, x, y);

        LOG.info(String.format("Downloading tile for zoom=%d, x=%d, y=%d, url=%s", zoom, x, y, tileDownloadUrl));

        downloadFile(tileDownloadUrl, tileCacheFile);

        return tileCacheFile;
    }

    public static void downloadFile(String url, File file) throws IOException
    {
        // TODO extract in a service.
        HttpResponse response = null;
        try
        {
            response = HttpClientBuilder.create().build().execute(new HttpGet(url));
        }
        catch (IOException e)
        {
            LOG.error("Can not load URL '" + url.toString() + "'", e);
            throw e;
        }

        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
        {
            String msg = String.format("Can not load URL '%s' code=%d (%s)", url.toString(), response.getStatusLine()
                .getStatusCode(), response.getStatusLine().getReasonPhrase());
            LOG.error(msg);
            throw new IOException(msg);
        }

        HttpEntity entity = response.getEntity();
        if (entity != null)
        {
            FileOutputStream outStream = new FileOutputStream(file);
            try
            {
                IOUtils.copy(entity.getContent(), outStream);
            }
            finally
            {
                outStream.close();
            }
        }
    }

}
