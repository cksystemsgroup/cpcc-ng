/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2014 Clemens Krainer <clemens.krainer@gmail.com>
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
package at.uni_salzburg.cs.cpcc.commons.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.core.services.QueryManager;

/**
 * ConfigurationChangeWaiter
 */
public class ConfigurationChangeWaiter
{
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationChangeWaiter.class);

    private QueryManager qm;
    private long lastConfigurationChangeTime = 0;
    private long sleepTime = 1000;

    /**
     * @param qm the query manager.
     */
    public ConfigurationChangeWaiter(QueryManager qm)
    {
        this.qm = qm;
    }

    /**
     * Wait for other transactions to finish to have the right data.
     * 
     * @param timeOut the number of seconds to wait for a database change.
     * @return true if the database has changed and time-out has not happened, false otherwise.
     */
    public boolean waitForDatabaseChange(int timeOut)
    {
        long latestChange = 0;
        int counter = timeOut;

        do
        {
            Date d = qm.findLatestSensorDefinitionOrRealVehicleChangeDate();
            if (d == null)
            {
                // No configuration available, yet.
                return false;
            }

            latestChange = d.getTime();
            LOG.info("Waiting for other transactions to finish.");
            sleep(sleepTime);
        } while (latestChange < lastConfigurationChangeTime && counter-- > 0);

        if (counter <= 0)
        {
            LOG.info("No database updates happened within " + timeOut + "s.");
            return false;
        }

        LOG.info("Database has changed, counter=" + counter);

        lastConfigurationChangeTime = latestChange;
        return true;
    }

    /**
     * @param time the time to sleep.
     */
    private static void sleep(long time)
    {
        try
        {
            Thread.sleep(time);
        }
        catch (InterruptedException e)
        {
            LOG.debug("Interrupted while sleeping.", e);
        }
    }
}
