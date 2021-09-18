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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.management.JMException;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SystemMonitorImpl implementation.
 */
public class SystemMonitorImpl implements SystemMonitor
{
    private static final Logger LOG = LoggerFactory.getLogger(SystemMonitorImpl.class);

    private static final String PROCESS_CPU_TIME = "java.lang:type=OperatingSystem/ProcessCpuTime";
    private static final String PROCESS_CPU_LOAD = "java.lang:type=OperatingSystem/ProcessCpuLoad";
    private static final String SYSTEM_CPU_LOAD = "java.lang:type=OperatingSystem/SystemCpuLoad";
    private static final String SYSTEM_LOAD_AVERAGE = "java.lang:type=OperatingSystem/SystemLoadAverage";
    private static final String OPEN_FILE_COUNT = "java.lang:type=OperatingSystem/OpenFileDescriptorCount";

    private Runtime runtime;
    private MemoryMXBean memBean;
    private List<GarbageCollectorMXBean> gcBean;
    private List<MemoryPoolMXBean> memPoolBEans;
    private OperatingSystemMXBean opsysBean;
    private RuntimeMXBean runtimeBean;
    private ThreadMXBean threadBean;
    private String header = "";
    private MxBeanUtils mxb = new MxBeanUtils();

    /**
     * @param configuration the service configuration.
     */
    public SystemMonitorImpl(Map<String, Object> configuration)
    {
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
        List<Pair<String, String>> entries = new ArrayList<>(Stream
            .of(Pair.of("osName", opsysBean.getName()),
                Pair.of("osVersion", opsysBean.getVersion()),
                Pair.of("processors", Integer.toString(opsysBean.getAvailableProcessors())),
                Pair.of("jvmName", runtimeBean.getName()),
                Pair.of("jvmArch", opsysBean.getArch()),
                Pair.of("startTime", Long.toString(runtimeBean.getStartTime())))
            .collect(Collectors.toList()));

        String keys = entries.stream().map(Pair::getLeft).collect(Collectors.joining(";"));
        String values = entries.stream().map(Pair::getRight).collect(Collectors.joining(";"));

        LOG.info(";SH;{}", keys);
        LOG.info(";SV;{}", values);
    }

    private double readDoubleAttribute(String mxBeanAttributeName)
    {
        try
        {
            return Double.parseDouble(mxb.readMxBeanAttribute(mxBeanAttributeName));
        }
        catch (NumberFormatException | JMException e)
        {
            return Double.NaN;
        }
    }

    private long readLongAttribute(String mxBeanAttributeName)
    {
        try
        {
            return Long.parseLong(mxb.readMxBeanAttribute(mxBeanAttributeName));
        }
        catch (NumberFormatException | JMException e)
        {
            return 0L;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeLogEntry()
    {
        List<Pair<String, String>> entries = new ArrayList<>();

        entries.addAll(Stream
            .of(Pair.of("time", Long.toString(System.currentTimeMillis())),
                Pair.of("uptime", Long.toString(runtimeBean.getUptime())),

                Pair.of("processCpuTime", Double.toString(readLongAttribute(PROCESS_CPU_TIME) / 1E6)),
                Pair.of("processCpuLoad", Double.toString(readDoubleAttribute(PROCESS_CPU_LOAD))),

                Pair.of("systemCpuLoad", Double.toString(readDoubleAttribute(SYSTEM_CPU_LOAD))),
                Pair.of("systemLoadAvg", Double.toString(readDoubleAttribute(SYSTEM_LOAD_AVERAGE))),

                Pair.of("openFileCount", Long.toString(readLongAttribute(OPEN_FILE_COUNT))),

                Pair.of("threadCount", Integer.toString(threadBean.getThreadCount())),
                Pair.of("peakThreadCount", Integer.toString(threadBean.getPeakThreadCount())),
                Pair.of("daemonThreadCount", Integer.toString(threadBean.getDaemonThreadCount())),
                Pair.of("totalStartedTheadCount", Long.toString(threadBean.getTotalStartedThreadCount())),

                Pair.of("freeMemory", Long.toString(runtime.freeMemory())),
                Pair.of("maxMemory", Long.toString(runtime.maxMemory())),
                Pair.of("totalMemory", Long.toString(runtime.totalMemory())),

                Pair.of("heap.init", Long.toString(memBean.getHeapMemoryUsage().getInit())),
                Pair.of("heap.used", Long.toString(memBean.getHeapMemoryUsage().getUsed())),
                Pair.of("heap.max", Long.toString(memBean.getHeapMemoryUsage().getMax())),
                Pair.of("heap.committed", Long.toString(memBean.getHeapMemoryUsage().getCommitted())),

                Pair.of("nonheap.init", Long.toString(memBean.getNonHeapMemoryUsage().getInit())),
                Pair.of("nonheap.used", Long.toString(memBean.getNonHeapMemoryUsage().getMax())),
                Pair.of("nonheap.max", Long.toString(memBean.getNonHeapMemoryUsage().getUsed())),
                Pair.of("nonheap.committed", Long.toString(memBean.getNonHeapMemoryUsage().getCommitted())),

                Pair.of("objPendingFinCount", Integer.toString(memBean.getObjectPendingFinalizationCount())),
                Pair.of("sysLoadAvg", Double.toString(opsysBean.getSystemLoadAverage())))
            .collect(Collectors.toList()));

        for (GarbageCollectorMXBean bean : gcBean)
        {
            if (bean.isValid())
            {
                String prefix = bean.getName().replace(' ', '_');

                entries.addAll(Stream
                    .of(Pair.of(prefix + ".gcTime", Long.toString(bean.getCollectionTime())),
                        Pair.of(prefix + ".gcCount", Long.toString(bean.getCollectionCount())))
                    .collect(Collectors.toList()));
            }
        }

        for (MemoryPoolMXBean bean : memPoolBEans)
        {
            if (bean.isValid())
            {
                String prefix = bean.getName().replace(' ', '_');

                entries.addAll(Stream
                    .of(Pair.of(prefix + ".init", Long.toString(bean.getUsage().getInit())),
                        Pair.of(prefix + ".max", Long.toString(bean.getUsage().getMax())),
                        Pair.of(prefix + ".used", Long.toString(bean.getUsage().getUsed())),
                        Pair.of(prefix + ".commmitted", Long.toString(bean.getUsage().getCommitted())))
                    .collect(Collectors.toList()));
            }
        }

        String newHeader = ";H;" + entries.stream().map(Pair::getLeft).collect(Collectors.joining(";"));

        if (!header.equals(newHeader))
        {
            LOG.info(newHeader);
            header = newHeader;
        }

        String values = entries.stream().map(Pair::getRight).collect(Collectors.joining(";"));
        LOG.info(";V;{}", values);
    }
}
