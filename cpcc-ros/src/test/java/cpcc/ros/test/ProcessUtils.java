// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2019 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.ros.test;

import static org.awaitility.Awaitility.await;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Utils for Process handling.
 */
public final class ProcessUtils
{
    private ProcessUtils()
    {
        // Intentionally empty.
    }

    public static Map<String, Integer> readOpenPortsFromTcpSocketMap() throws IOException
    {
        Map<String, Integer> result = new HashMap<>();
        File tcpMap = new File("/proc/net/tcp");
        File tcp6Map = new File("/proc/net/tcp6");

        for (File f : Arrays.asList(tcpMap, tcp6Map))
        {
            if (!f.exists())
            {
                continue;
            }

            String fx = FileUtils.readFileToString(f, "UTF-8");
            if (StringUtils.isBlank(fx))
            {
                continue;
            }

            String[] lines = fx.trim().split("(\r|\n)+");
            for (int k = 1; k < lines.length; ++k)
            {
                String[] l = lines[k].split("\\s+");
                String inode = l[10];
                String[] localAddress = l[2].split(":");
                if (localAddress.length == 2)
                {
                    result.put(inode, Integer.valueOf(localAddress[1], 16));
                }
            }
        }

        return result;
    }

    public static int getRandomPortNumber(int startInclusive, int endExclusive) throws IOException
    {
        PortGenerator generator = new PortGenerator(startInclusive, endExclusive);

        await()
            .forever()
            .pollInterval(100L, TimeUnit.MILLISECONDS)
            .until(generator);

        return generator.port;
    }

    private static class PortGenerator implements Callable<Boolean>
    {
        int port = 0;
        int startInclusive;
        int endExclusive;

        public PortGenerator(int startInclusive, int endExclusive)
        {
            this.startInclusive = startInclusive;
            this.endExclusive = endExclusive;
        }

        @Override
        public Boolean call() throws Exception
        {
            port = RandomUtils.nextInt(startInclusive, endExclusive);
            return !readOpenPortsFromTcpSocketMap().values().contains(port);
        }
    }

}
