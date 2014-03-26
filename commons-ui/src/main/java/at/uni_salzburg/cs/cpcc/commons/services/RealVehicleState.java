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
package at.uni_salzburg.cs.cpcc.commons.services;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;

/**
 * RealVehicleState
 */
public class RealVehicleState
{
    private RealVehicle realVehicle;

    private boolean statusUpdateRunning = false;

    private byte[] status = null;

    private boolean connected = false;

    private Date lastUpdate = null;

    /**
     * @param realVehicle the associated real vehicle.
     */
    public RealVehicleState(RealVehicle realVehicle)
    {
        this.realVehicle = realVehicle;
    }

    /**
     * @return the associated real vehicle.
     */
    public RealVehicle getRealVehicle()
    {
        return realVehicle;
    }

    /**
     * @return true if the status update is running, false otherwise.
     */
    public boolean isStatusUpdateRunning()
    {
        return statusUpdateRunning;
    }

    /**
     * @param statusUpdateRunning the update status to set.
     */
    public void setStatusUpdateRunning(boolean statusUpdateRunning)
    {
        this.statusUpdateRunning = statusUpdateRunning;
    }

    /**
     * @return the current real vehicle status.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Exposed on purpose!")
    public byte[] getStatus()
    {
        return status;
    }

    /**
     * @param status the current real vehicle status to set.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Exposed on purpose!")
    public void setStatus(byte[] status)
    {
        this.status = status;
    }

    /**
     * @return the current status as string.
     * @throws UnsupportedEncodingException thrown in case of errors.
     */
    public String getStatusString() throws UnsupportedEncodingException
    {
        return status != null ? new String(status, "UTF-8") : null;
    }

    /**
     * @return true if the real vehicle is reachable via network, false otherwise.
     */
    public boolean isConnected()
    {
        return connected;
    }

    /**
     * @param connected set to true if the real vehicle is reachable via network, set to false otherwise.
     */
    public void setConnected(boolean connected)
    {
        this.connected = connected;
    }

    /**
     * @return the time the last update happened.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Exposed on purpose!")
    public Date getLastUpdate()
    {
        return lastUpdate;
    }

    /**
     * @param lastUpdate set the time the last update happened.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Exposed on purpose!")
    public void setLastUpdate(Date lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }
}
