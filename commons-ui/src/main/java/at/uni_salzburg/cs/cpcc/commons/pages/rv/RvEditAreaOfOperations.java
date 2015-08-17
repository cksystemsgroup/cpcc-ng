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

package at.uni_salzburg.cs.cpcc.commons.pages.rv;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.hibernate.Session;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.utils.ResourceStreamResponse;
import cpcc.rv.base.services.StateSynchronizer;

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
        realVehicle = qm.findRealVehicleById(id);
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
        String pngResourcePath = "at/uni_salzburg/cs/cpcc/commons/" + folder + "/" + imageName;
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
        StringBuilder buff = new StringBuilder();

        List<RealVehicle> rvList = qm.findAllRealVehicles();
        boolean first = true;

        buff.append("{");
        for (RealVehicle rv : rvList)
        {
            if (rv.getId().intValue() == realVehicle.getId().intValue())
            {
                continue;
            }

            if (first)
            {
                first = false;
            }
            else
            {
                buff.append(",");
            }

            buff.append("\"")
                .append(rv.getName())
                .append("\":")
                .append(rv.getAreaOfOperation().replaceAll("\\\\n\\s*", ""));
        }

        buff.append("}");

        return buff.toString();
    }

    /**
     * @return the map center coordinates.
     */
    public String getMapCenter()
    {
        // TODO implement
        return "[37.8085124939787,-122.42505311965941]";
    }

    /**
     * @return the zoom level.
     */
    public String getZoomLevel()
    {
        // TODO implement
        return "17";
    }

}
