// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.rv.base.services;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;

/**
 * SystemMonitorImpl implementation.
 */
public class SystemMonitorImpl implements SystemMonitor
{
    private Runtime runtime;
    private MemoryMXBean memBean;
    private Logger logger;
    private List<GarbageCollectorMXBean> gcBean;
    private List<MemoryPoolMXBean> memPoolBEans;
    private OperatingSystemMXBean opsysBean;
    private RuntimeMXBean runtimeBean;
    private ThreadMXBean threadBean;
    private String header = "";

    /**
     * @param configuration the service configuration.
     */
    public SystemMonitorImpl(Map<String, Object> configuration)
    {
        this.logger = (Logger) configuration.get("logger");

        runtime = Runtime.getRuntime();
        memBean = ManagementFactory.getMemoryMXBean();
        gcBean = ManagementFactory.getGarbageCollectorMXBeans();
        memPoolBEans = ManagementFactory.getMemoryPoolMXBeans();
        opsysBean = ManagementFactory.getOperatingSystemMXBean();
        runtimeBean = ManagementFactory.getRuntimeMXBean();
        threadBean = ManagementFactory.getThreadMXBean();

        writeStaticValues();
    }

    /**
     * Write the static values.
     */
    private void writeStaticValues()
    {
        List<SimpleEntry<String, Object>> entries = new ArrayList<>(Stream.of(
            new SimpleEntry<String, Object>("osName", opsysBean.getName()),
            new SimpleEntry<String, Object>("osVersion", opsysBean.getVersion()),
            new SimpleEntry<String, Object>("processors", opsysBean.getAvailableProcessors()),
            new SimpleEntry<String, Object>("jvmName", runtimeBean.getName()),
            new SimpleEntry<String, Object>("jvmArch", opsysBean.getArch()),
            new SimpleEntry<String, Object>("startTime", runtimeBean.getStartTime())
            ).collect(Collectors.toList()));

        logger.info(";SH;" + entries.stream().map(x -> x.getKey().toString()).collect(Collectors.joining(";")));
        logger.info(";SV;" + entries.stream().map(x -> x.getValue().toString()).collect(Collectors.joining(";")));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeLogEntry()
    {
        List<SimpleEntry<String, Number>> entries = new ArrayList<>(Stream.of(
            new SimpleEntry<String, Number>("time", System.currentTimeMillis()),
            new SimpleEntry<String, Number>("freeMemory", runtime.freeMemory()),
            new SimpleEntry<String, Number>("maxMemory", runtime.maxMemory()),
            new SimpleEntry<String, Number>("totalMemory", runtime.totalMemory()),
            new SimpleEntry<String, Number>("heap.init", memBean.getHeapMemoryUsage().getInit()),
            new SimpleEntry<String, Number>("heap.used", memBean.getHeapMemoryUsage().getUsed()),
            new SimpleEntry<String, Number>("heap.max", memBean.getHeapMemoryUsage().getMax()),
            new SimpleEntry<String, Number>("heap.committed", memBean.getHeapMemoryUsage().getCommitted()),

            new SimpleEntry<String, Number>("nonheap.init", memBean.getNonHeapMemoryUsage().getInit()),
            new SimpleEntry<String, Number>("nonheap.used", memBean.getNonHeapMemoryUsage().getMax()),
            new SimpleEntry<String, Number>("nonheap.max", memBean.getNonHeapMemoryUsage().getUsed()),
            new SimpleEntry<String, Number>("nonheap.committed", memBean.getNonHeapMemoryUsage().getCommitted()),

            new SimpleEntry<String, Number>("objPendingFinCount", memBean.getObjectPendingFinalizationCount()),
            new SimpleEntry<String, Number>("sysLoadAvg", opsysBean.getSystemLoadAverage()),
            new SimpleEntry<String, Number>("uptime", runtimeBean.getUptime()),
            new SimpleEntry<String, Number>("threadCount", threadBean.getThreadCount()),
            new SimpleEntry<String, Number>("peakThreadCount", threadBean.getPeakThreadCount()),
            new SimpleEntry<String, Number>("daemonThreadCount", threadBean.getDaemonThreadCount()),
            new SimpleEntry<String, Number>("totalStartedTheadCount", threadBean.getTotalStartedThreadCount())
            ).collect(Collectors.toList()));

        for (GarbageCollectorMXBean bean : gcBean)
        {
            if (bean.isValid())
            {
                String prefix = bean.getName().replace(' ', '_');

                entries.addAll(Stream.of(
                    new SimpleEntry<String, Number>(prefix + ".gcTime", bean.getCollectionTime()),
                    new SimpleEntry<String, Number>(prefix + ".gcCount", bean.getCollectionCount())
                    ).collect(Collectors.toList()));
            }
        }

        for (MemoryPoolMXBean bean : memPoolBEans)
        {
            if (bean.isValid())
            {
                String prefix = bean.getName().replace(' ', '_');

                entries.addAll(Stream.of(
                    new SimpleEntry<String, Number>(prefix + ".init", bean.getUsage().getInit()),
                    new SimpleEntry<String, Number>(prefix + ".max", bean.getUsage().getMax()),
                    new SimpleEntry<String, Number>(prefix + ".used", bean.getUsage().getUsed()),
                    new SimpleEntry<String, Number>(prefix + ".commmitted", bean.getUsage().getCommitted())
                    ).collect(Collectors.toList()));
            }
        }

        String newHeader = ";H;" + entries.stream().map(x -> x.getKey()).collect(Collectors.joining(";"));

        if (!header.equals(newHeader))
        {
            logger.info(newHeader);
            header = newHeader;
        }

        logger.info(";V;" + entries.stream().map(x -> x.getValue().toString()).collect(Collectors.joining(";")));
    }

}
