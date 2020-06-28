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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * OpenStreetMap tile cache
 */
public class TileCache
{
    private static final Logger LOG = LoggerFactory.getLogger(TileCache.class);

    private static final String FORMAT_TILE_CACHE_DIR = "%1$s/%2$d/%3$d";
    private static final String FORMAT_TILE_CACHE_FILE = "%1$s/%2$d/%3$d/%4$d.png";

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
    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    public File getTile(int zoom, int x, int y) throws IOException
    {
        String tileCacheFileName = String.format(Locale.US, FORMAT_TILE_CACHE_FILE, tileCacheBaseDir, zoom, x, y);

        File tileCacheFile = new File(tileCacheFileName);

        if (tileCacheFile.exists())
        {
            LOG.debug("Cached tile found for zoom={}, x={}, y={}", zoom, x, y);
            return tileCacheFile;
        }

        String tileCacheDirName = String.format(Locale.US, FORMAT_TILE_CACHE_DIR, tileCacheBaseDir, zoom, x);
        File tileCacheDir = new File(tileCacheDirName);
        if (!tileCacheDir.exists())
        {
            FileUtils.forceMkdir(tileCacheDir);
        }

        String tileDownloadUrl = String.format(Locale.US, tileServerUrl, zoom, x, y);

        LOG.debug("Downloading tile for zoom={}, x={}, y={}, url={}", zoom, x, y, tileDownloadUrl);

        downloadFile(tileDownloadUrl, tileCacheFile);

        return tileCacheFile;
    }

    /**
     * @param url the URL of the desired file.
     * @param file the path where to store the retrieved data.
     * @throws IOException thrown in case of errors.
     */
    public static void downloadFile(String url, File file) throws IOException
    {
        HttpResponse response = null;
        try
        {
            response = HttpClientBuilder.create()
                .setUserAgent("TileCache/1.0")
                .build().execute(new HttpGet(url));
        }
        catch (IOException e)
        {
            throw new IOException("Can not load URL '" + url + "'", e);
        }

        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
        {
            String msg = String.format("Can not load URL '%s' code=%d (%s)",
                url, response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
            throw new IOException(msg);
        }

        HttpEntity entity = response.getEntity();
        if (entity != null)
        {
            try (FileOutputStream outStream = new FileOutputStream(file))
            {
                IOUtils.copy(entity.getContent(), outStream);
            }
        }
    }

}
