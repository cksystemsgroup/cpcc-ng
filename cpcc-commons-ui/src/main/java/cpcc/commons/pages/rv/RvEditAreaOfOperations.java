// This code is part of the CPCC-NG project.
//
// Copyright (c) 2014 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.commons.pages.rv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.hibernate.Session;

import cpcc.core.entities.RealVehicle;
import cpcc.core.services.CoreGeoJsonConverter;
import cpcc.core.services.CoreJsonConverter;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.utils.MathUtils;
import cpcc.core.utils.ResourceStreamResponse;
import cpcc.rv.base.services.StateSynchronizer;

/**
 * RvEditAreaOfOperations
 */
public class RvEditAreaOfOperations
{
    @Inject
    protected Session session;

    @Inject
    protected RealVehicleRepository realVehicleRepository;

    @Inject
    private CoreJsonConverter jsonConverter;

    @Inject
    private CoreGeoJsonConverter geoJsonConverter;

    @Inject
    protected StateSynchronizer confSync;

    @Valid
    @Property
    protected RealVehicle realVehicle;

    private Integer realVehicelId;

    /**
     * @param id the real vehicle identification.
     */
    void onActivate(Integer id)
    {
        this.realVehicelId = id;
        realVehicle = realVehicleRepository.findRealVehicleById(id);
    }

    /**
     * @return the real vehicle regions.
     */
    public String getRealVehicleRegions()
    {
        String regions = realVehicle.getAreaOfOperation();
        regions = regions.replaceAll("\\\\n\\s*", "");
        return regions;
    }

    /**
     * @param areaOfOperation the real vehicle regions to set.
     */
    public void setRealVehicleRegions(String areaOfOperation)
    {
        String regions = areaOfOperation.replaceAll("\\\\n\\s*", "");
        realVehicle.setAreaOfOperation(regions);
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
        String pngResourcePath = "cpcc/commons/" + folder + "/" + imageName;
        return new ResourceStreamResponse("application/png", pngResourcePath);
    }

    @CommitAfter
    void onSuccess()
    {
        realVehicle.setLastUpdate(new Date());
        session.saveOrUpdate(realVehicle);
    }

    /**
     * @return the zones of the other real vehicles.
     * @throws IOException in case of errors.
     */
    public String getOtherRealVehicleRegions() throws IOException
    {
        List<RealVehicle> otherRvList = new ArrayList<RealVehicle>();

        for (RealVehicle rv : realVehicleRepository.findAllRealVehicles())
        {
            if (rv.getId().intValue() != realVehicle.getId().intValue())
            {
                otherRvList.add(rv);
            }
        }

        return jsonConverter.toRegionJson(otherRvList);
    }

    /**
     * @return the map center coordinates.
     */
    public String getMapCenter()
    {
        double[] bbox = geoJsonConverter.findBoundingBox(Arrays.asList(realVehicle));
        if (bbox.length == 4 && MathUtils.containsNoNaN(bbox))
        {
            return "[" + MathUtils.avg(bbox[1], bbox[3]) + "," + MathUtils.avg(bbox[0], bbox[2]) + "]";
        }

        //        List<Point> depotList = geoJsonConverter.findDepotPositions(realVehicle);
        //        if (!depotList.isEmpty())
        //        {
        //            LngLatAlt coordinates = depotList.get(0).getCoordinates();
        //            return "[" + coordinates.getLatitude() + "," + coordinates.getLongitude() + "]";
        //        }

        // TODO implement some more!
        return "[37.8085124939787,-122.42505311965941]";
    }

    /**
     * @return the zoom level.
     */
    public String getZoomLevel()
    {
        // TODO implement!
        return "17";
    }

}
