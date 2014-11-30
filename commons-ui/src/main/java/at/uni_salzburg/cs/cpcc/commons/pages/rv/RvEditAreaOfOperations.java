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

import static org.apache.tapestry5.EventConstants.SUCCESS;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.Polygon;
import org.hibernate.Transaction;

import at.uni_salzburg.cs.cpcc.commons.pages.Viewer;
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
public class RvEditAreaOfOperations extends Viewer
{
    @PageActivationContext
    private String vehicleId;

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

    @Persist(PersistenceConstants.FLASH)
    @Property
    private Double minAltitude;

    @Persist(PersistenceConstants.FLASH)
    @Property
    private Double maxAltitude;

    @Property
    private String realVehicleZones;

    @Component(id = "jsForm")
    protected Form jsForm;

    @Component(id = "rvForm")
    protected Form rvForm;

    @Inject
    protected Messages messages;

    @Inject
    private JavaScriptSupport js;

    /**
     * @param id the real vehicle identification.
     */
    public void onActivate(Integer id)
    {
        realVehicle = qm.findRealVehicleById(id);
        realVehicleZones = realVehicle.getAreaOfOperation();
    }

    /**
     * @param folder the sub-directory where the image resides.
     * @param imageName the image name.
     * @return the image as a StreamResponse object.
     */
    public StreamResponse onActivate(String folder, String imageName)
    {
        String pngResourcePath = "at/uni_salzburg/cs/cpcc/commons/" + folder + "/" + imageName;
        return new PngResourceStreamResponse(pngResourcePath);
    }

    /**
     * import the Draw JavaScript stack.
     */
    @SetupRender
    public void importStack()
    {
        super.importStack();
        js.importStack("draw");
        js.addScript("drawInit();");
    }

    @OnEvent(SUCCESS)
    void storeZones()
    {
        Transaction t = qm.getSession().getTransaction();
        t.begin();
        realVehicle.setAreaOfOperation(realVehicleZones);
        realVehicle.setLastUpdate(new Date());
        t.commit();
        
        rvss.notifyConfigurationChange();
        confSync.notifyConfigurationChange();
    }

    /**
     * @return the zones of the other real vehicles.
     * @throws IOException thrown in case of errors.
     */
    public String getOtherRealVehicleZones() throws IOException
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
}
