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

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.slf4j.Logger;

import at.uni_salzburg.cs.cpcc.commons.services.UuidFormatter;
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
    /**
     * View function
     */
    public enum ViewFunction
    {
        VIEW_LIST,
        VIEW_MODAL
    }

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
    protected Request request;

    @Inject
    protected AjaxResponseRenderer ajaxResponseRenderer;

    @Inject
    private VvRteRepository repository;

    @Inject
    private VirtualVehicleLauncher launcher;

    @InjectComponent
    protected Zone paneZone;

    @InjectComponent
    protected Zone modalZone;

    @Property
    private Collection<VirtualVehicle> virtualVehicleList;

    @Property
    private VirtualVehicle virtualVehicle;

    @Property
    protected ViewFunction viewFunction;

    @Property
    protected UuidFormatter uuidFormat;

    void onActivate()
    {
        virtualVehicleList = repository.findAllVehicles();
        viewFunction = ViewFunction.VIEW_LIST;
        uuidFormat = new UuidFormatter();
    }

    /**
     * @param viewFunc the function to check.
     * @return true if the provided function equals the internal state.
     */
    public boolean isFunction(ViewFunction viewFunc)
    {
        return viewFunc == viewFunction;
    }

    /**
     * Handle XHR requests.
     *
     * @param zones the zones to handle.
     */
    protected void handleXhrRequest(Zone... zones)
    {
        if (request.isXHR())
        {
            for (Zone zone : zones)
            {
                ajaxResponseRenderer.addRender(zone);
            }
        }
    }

    @OnEvent("deleteVehicle")
    @CommitAfter
    void deleteVehicle(Integer id)
    {
        VirtualVehicle vehicle = repository.findVirtualVehicleById(id);
        repository.deleteVirtualVehicleById(vehicle);

        handleXhrRequest(paneZone);
    }

    @OnEvent("startVehicle")
    @CommitAfter
    void startVehicle(Integer id)
    {
        VirtualVehicle vehicle = repository.findVirtualVehicleById(id);
        if (!VV_STATES_FOR_START.contains(vehicle.getState()))
        {
            return;
        }

        try
        {
            launcher.start(vehicle.getId());
        }
        catch (VirtualVehicleLaunchException | IOException e)
        {
            logger.error("Can not start virtual vehicle " + id, e);
        }

        handleXhrRequest(paneZone);
    }

    @OnEvent("restartMigration")
    @CommitAfter
    void restartMigration(Integer id)
    {
        VirtualVehicle vehicle = repository.findVirtualVehicleById(id);
        if (!VV_STATES_FOR_RESTART_MIGRATION.contains(vehicle.getState()))
        {
            return;
        }

        try
        {
            launcher.resume(vehicle.getId());
        }
        catch (VirtualVehicleLaunchException | IOException e)
        {
            logger.error("Can not restart migration of virtual vehicle " + id, e);
        }

        handleXhrRequest(paneZone);
    }

    @OnEvent("pauseVehicle")
    @CommitAfter
    void pauseVehicle(Integer id)
    {
        logger.error("pauseVehicle " + id + " not implemented.");
        handleXhrRequest(paneZone);
    }

    @OnEvent("stopVehicle")
    @CommitAfter
    void stopVehicle(Integer id)
    {
        handleXhrRequest(paneZone);
    }

    @OnEvent("restartVehicle")
    @CommitAfter
    void restartVehicle(Integer id)
    {
        VirtualVehicle vehicle = repository.findVirtualVehicleById(id);
        if (!VV_STATES_FOR_RESTART.contains(vehicle.getState()))
        {
            return;
        }

        try
        {
            vehicle.setState(VirtualVehicleState.INIT);
            launcher.start(vehicle.getId());
        }
        catch (VirtualVehicleLaunchException | IOException e)
        {
            logger.error("Can not restart virtual vehicle " + id, e);
        }

        handleXhrRequest(paneZone);
    }

    @OnEvent("showStateInfo")
    @CommitAfter
    void showStateInfo(Integer id)
    {
        viewFunction = ViewFunction.VIEW_MODAL;
        virtualVehicle = repository.findVirtualVehicleById(id);

        handleXhrRequest(paneZone, modalZone);
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
