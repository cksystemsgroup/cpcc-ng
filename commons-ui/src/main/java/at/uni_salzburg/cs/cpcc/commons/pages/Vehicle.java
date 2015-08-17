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

package at.uni_salzburg.cs.cpcc.commons.pages;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.slf4j.Logger;

import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleState;
import at.uni_salzburg.cs.cpcc.vvrte.services.VirtualVehicleLaunchException;
import at.uni_salzburg.cs.cpcc.vvrte.services.VirtualVehicleLauncher;
import at.uni_salzburg.cs.cpcc.vvrte.services.VvRteRepository;

/**
 * Vehicle
 */
public class Vehicle
{
    @SuppressWarnings("serial")
    private static final Set<VirtualVehicleState> VV_STATES_FOR_DELETE = new HashSet<VirtualVehicleState>()
    {
        {
            add(VirtualVehicleState.DEFECTIVE);
            add(VirtualVehicleState.FINISHED);
            add(VirtualVehicleState.INIT);
            add(VirtualVehicleState.INTERRUPTED);
            add(VirtualVehicleState.MIGRATION_COMPLETED);
            add(VirtualVehicleState.MIGRATION_INTERRUPTED);
        }
    };

    @SuppressWarnings("serial")
    private static final Set<VirtualVehicleState> VV_STATES_FOR_EDIT = new HashSet<VirtualVehicleState>()
    {
        {
            add(VirtualVehicleState.DEFECTIVE);
            add(VirtualVehicleState.FINISHED);
            add(VirtualVehicleState.INIT);
        }
    };

    @SuppressWarnings("serial")
    private static final Set<VirtualVehicleState> VV_STATES_FOR_START = new HashSet<VirtualVehicleState>()
    {
        {
            add(VirtualVehicleState.INIT);
        }
    };

    @SuppressWarnings("serial")
    private static final Set<VirtualVehicleState> VV_STATES_FOR_RESTART = new HashSet<VirtualVehicleState>()
    {
        {
            add(VirtualVehicleState.DEFECTIVE);
            add(VirtualVehicleState.FINISHED);
            add(VirtualVehicleState.INTERRUPTED);
            add(VirtualVehicleState.MIGRATION_COMPLETED);
            add(VirtualVehicleState.MIGRATION_INTERRUPTED);
        }
    };

    @SuppressWarnings("serial")
    private static final Set<VirtualVehicleState> VV_STATES_FOR_RESTART_MIGRATION = new HashSet<VirtualVehicleState>()
    {
        {
            add(VirtualVehicleState.MIGRATION_AWAITED);
            add(VirtualVehicleState.MIGRATION_INTERRUPTED);
        }
    };

    @Inject
    private Logger logger;

    @Inject
    private VvRteRepository repository;

    @Inject
    private VirtualVehicleLauncher launcher;

    @Property
    private Collection<VirtualVehicle> virtualVehicleList;

    @Property
    private VirtualVehicle virtualVehicle;

    void onActivate()
    {
        virtualVehicleList = repository.findAllVehicles();
    }

    @OnEvent("deleteVehicle")
    @CommitAfter
    void deleteVehicle(Integer id)
    {
        System.out.println("deleteVehicle " + id);
        VirtualVehicle vehicle = repository.findVirtualVehicleById(id);
        repository.deleteVirtualVehicleById(vehicle);
    }

    @OnEvent("startVehicle")
    @CommitAfter
    void startVehicle(Integer id)
    {
        System.out.println("startVehicle " + id);
        VirtualVehicle vehicle = repository.findVirtualVehicleById(id);
        if (!VV_STATES_FOR_START.contains(vehicle.getState()))
        {
            return;
        }

        try
        {
            launcher.start(vehicle);
        }
        catch (VirtualVehicleLaunchException | IOException e)
        {
            logger.error("Can not start virtual vehicle " + id, e);
        }
    }

    @OnEvent("restartMigration")
    @CommitAfter
    void restartMigration(Integer id)
    {
        System.out.println("restartMigration " + id);
        VirtualVehicle vehicle = repository.findVirtualVehicleById(id);
        if (!VV_STATES_FOR_RESTART_MIGRATION.contains(vehicle.getState()))
        {
            return;
        }

        try
        {
            launcher.resume(vehicle);
        }
        catch (VirtualVehicleLaunchException | IOException e)
        {
            logger.error("Can not restart migration of virtual vehicle " + id, e);
        }
    }

    @OnEvent("pauseVehicle")
    @CommitAfter
    void pauseVehicle(Integer id)
    {
        System.out.println("pauseVehicle " + id + " not implemented.");
    }

    @OnEvent("stopVehicle")
    @CommitAfter
    void stopVehicle(Integer id)
    {
        System.out.println("stopVehicle " + id);
    }

    @OnEvent("restartVehicle")
    @CommitAfter
    void restartVehicle(Integer id)
    {
        System.out.println("restartVehicle " + id);
        VirtualVehicle vehicle = repository.findVirtualVehicleById(id);
        if (!VV_STATES_FOR_RESTART.contains(vehicle.getState()))
        {
            return;
        }

        try
        {
            vehicle.setState(VirtualVehicleState.INIT);
            launcher.start(vehicle);
        }
        catch (VirtualVehicleLaunchException | IOException e)
        {
            logger.error("Can not restart virtual vehicle " + id, e);
        }
    }

    /**
     * @return true if starting is allowed.
     */
    public boolean isStart()
    {
        return VV_STATES_FOR_START.contains(virtualVehicle.getState());
    }

    /**
     * @return true if a migration restart is allowed.
     */
    public boolean isRestartMigration()
    {
        return VV_STATES_FOR_RESTART_MIGRATION.contains(virtualVehicle.getState());
    }

    /**
     * @return true if editing is allowed.
     */
    public boolean isEdit()
    {
        return VV_STATES_FOR_EDIT.contains(virtualVehicle.getState());
    }

    /**
     * @return true if deletion is allowed.
     */
    public boolean isDelete()
    {
        return VV_STATES_FOR_DELETE.contains(virtualVehicle.getState());
    }

    /**
     * @return true if pausing is allowed.
     */
    public boolean isPause()
    {
        return false;
    }

    /**
     * @return true if stopping is allowed.
     */
    public boolean isStop()
    {
        return false;
    }

    /**
     * @return true if restarting is allowed.
     */
    public boolean isRestart()
    {
        return VV_STATES_FOR_RESTART.contains(virtualVehicle.getState());
    }
}
