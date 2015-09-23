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

package at.uni_salzburg.cs.cpcc.commons.pages.sensor;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.hibernate.Session;

import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;

/**
 * SensorList
 */
public class SensorList
{
    @Inject
    private Session session;

    @Inject
    private QueryManager qm;

    @Property
    private SensorDefinition sensor;

    /**
     * @return the current list of sensors.
     */
    public List<SensorDefinition> getSensorList()
    {
        return qm.findAllSensorDefinitions();
    }

    @CommitAfter
    void onActivateSensor(Integer id)
    {
        updateSensorDefinition(id, Boolean.FALSE);
    }

    @CommitAfter
    void onDeactivateSensor(Integer id)
    {
        updateSensorDefinition(id, Boolean.TRUE);
    }

    @CommitAfter
    private void updateSensorDefinition(Integer id, Boolean deleted)
    {
        SensorDefinition sd = qm.findSensorDefinitionById(id);
        sd.setLastUpdate(new Date());
        sd.setDeleted(deleted);
        session.saveOrUpdate(sd);
    }
}
