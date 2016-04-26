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

package cpcc.rv.base.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeometryCollection;

import cpcc.core.entities.PolarCoordinate;
import cpcc.vvrte.entities.Task;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.services.db.VvRteRepository;
import cpcc.vvrte.services.json.VvGeoJsonConverter;

/**
 * Virtual Vehicle contributor implementation.
 */
public class VirtualVehicleContributor implements StateContributor
{
    private VvRteRepository vvRepo;
    private VvGeoJsonConverter vjc;

    /**
     * @param vvRepo the Virtual Vehicle repository.
     * @param vjc the Virtual Vehicle GeoJSON converter.
     */
    public VirtualVehicleContributor(VvRteRepository vvRepo, VvGeoJsonConverter vjc)
    {
        this.vvRepo = vvRepo;
        this.vjc = vjc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void contribute(FeatureCollection featureCollection, PolarCoordinate rvPosition, List<Task> taskList)
    {
        List<VirtualVehicle> vvList = vvRepo.findAllActiveVehicles(10);
        if (vvList == null)
        {
            vvList = Collections.emptyList();
        }

        Map<VirtualVehicleState, Integer> stats = vvRepo.getVvStatistics();

        int defective = stats.get(VirtualVehicleState.DEFECTIVE);

        int dormant = stats.get(VirtualVehicleState.FINISHED)
            + stats.get(VirtualVehicleState.INIT);

        int active = stats.get(VirtualVehicleState.RUNNING)
            + stats.get(VirtualVehicleState.TASK_COMPLETION_AWAITED);

        int interrupted = stats.get(VirtualVehicleState.INTERRUPTED)
            + stats.get(VirtualVehicleState.MIGRATION_INTERRUPTED_SND);

        int migrating = stats.get(VirtualVehicleState.MIGRATING_RCV)
            + stats.get(VirtualVehicleState.MIGRATING_SND)
            + stats.get(VirtualVehicleState.MIGRATION_AWAITED_SND)
            + stats.get(VirtualVehicleState.MIGRATION_COMPLETED_RCV)
            + stats.get(VirtualVehicleState.MIGRATION_COMPLETED_SND);

        int total = defective + dormant + active + interrupted + migrating;

        GeometryCollection vvFeatures = new GeometryCollection();
        vvFeatures.setGeometries(vjc.toGeometryObjectsList(vvList));

        Feature feature = new Feature();
        feature.setProperty("type", "vvs");
        feature.setProperty("vvsTotal", total);
        feature.setProperty("vvsDefective", defective);
        feature.setProperty("vvsDormant", dormant);
        feature.setProperty("vvsActive", active);
        feature.setProperty("vvsInterrupted", interrupted);
        feature.setProperty("vvsMigrating", migrating);
        feature.setGeometry(vvFeatures);

        featureCollection.add(feature);
    }
}
