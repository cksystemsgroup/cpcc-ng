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

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeometryCollection;

import cpcc.core.entities.PolarCoordinate;
import cpcc.vvrte.entities.Task;
import cpcc.vvrte.entities.VirtualVehicle;
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

        GeometryCollection vvFeatures = new GeometryCollection();
        vvFeatures.setGeometries(vjc.toGeometryObjectsList(vvList));

        Feature feature = new Feature();
        feature.setProperty("type", "vvs");
        feature.setGeometry(vvFeatures);

        featureCollection.add(feature);
    }
}
