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
package at.uni_salzburg.cs.cpcc.commons.pages.rv;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Property;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.Polygon;
import org.hibernate.Session;
import org.hibernate.Transaction;

import at.uni_salzburg.cs.cpcc.commons.services.ConfigurationSynchronizer;
import at.uni_salzburg.cs.cpcc.commons.services.RealVehicleStateService;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.services.CoreGeoJsonConverter;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.utils.PngResourceStreamResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * RvEditAreaOfOperations
 */
public class RvEditAreaOfOperations
{
    @Inject
    protected Session session;

    @Inject
    protected QueryManager qm;

    @Inject
    private CoreGeoJsonConverter geoConv;

    @Inject
    protected RealVehicleStateService rvss;

    @Inject
    protected ConfigurationSynchronizer confSync;

    @Valid
    @Property
    protected RealVehicle realVehicle;

    @Property
    private String realVehicleRegions;

    private Integer realVehicelId;

    /**
     * @param id the real vehicle identification.
     */
    void onActivate(Integer id)
    {
        this.realVehicelId = id;
        realVehicle = qm.findRealVehicleById(id);
        realVehicleRegions = realVehicle.getAreaOfOperation();
    }

    /**
     * @return the current real vehicle identification.
     */
    Integer onPassivate()
    {
        return realVehicelId;
    }

    /**
     * @param folder the sub-directory where the image resides.
     * @param imageName the image name.
     * @return the image as a StreamResponse object.
     */
    StreamResponse onActivate(String folder, String imageName)
    {
        System.out.println("### ALERT! onActivate " + folder + " " + imageName);
        String pngResourcePath = "at/uni_salzburg/cs/cpcc/commons/" + folder + "/" + imageName;
        return new PngResourceStreamResponse(pngResourcePath);
    }

    void onSuccess()
    {
        Session newSession = session.getSessionFactory().openSession();
        try
        {
            Transaction t = newSession.getTransaction();
            t.begin();
            realVehicle.setAreaOfOperation(realVehicleRegions);
            realVehicle.setLastUpdate(new Date());
            newSession.saveOrUpdate(realVehicle);
            t.commit();
        }
        finally
        {
            newSession.close();
        }

        rvss.notifyConfigurationChange();
        confSync.notifyConfigurationChange();
    }

    /**
     * @return the zones of the other real vehicles.
     * @throws IOException in case of errors.
     */
    public String getOtherRealVehicleRegions() throws IOException
    {
        FeatureCollection fc = new FeatureCollection();

        for (RealVehicle rv : qm.findAllRealVehicles())
        {
            if (rv.getId().intValue() == realVehicle.getId().intValue())
            {
                continue;
            }

            Feature feature = geoConv.toFeature(rv);

            GeoJsonObject geometry = feature.getGeometry();
            if (geometry == null || !(geometry instanceof FeatureCollection))
            {
                continue;
            }

            FeatureCollection fc2 = (FeatureCollection) geometry;
            for (Feature f : fc2.getFeatures())
            {
                if (f.getGeometry() instanceof Polygon)
                {
                    fc.add(f);
                }
            }
        }

        return new ObjectMapper().writeValueAsString(fc);
    }

    /**
     * @return the map center coordinates.
     */
    public String getMapCenter()
    {
        return "[37.8085,-122.4265]";
    }

    /**
     * @return the zoom level.
     */
    public String getZoomLevel()
    {
        return "11";
    }

}
