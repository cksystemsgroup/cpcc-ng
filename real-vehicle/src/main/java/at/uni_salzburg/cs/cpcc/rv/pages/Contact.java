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
package at.uni_salzburg.cs.cpcc.rv.pages;

import java.util.List;

import javax.inject.Inject;

import static org.apache.tapestry5.EventConstants.ACTIVATE;

import org.apache.tapestry5.annotations.OnEvent;

import at.uni_salzburg.cs.cpcc.persistence.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.persistence.services.QueryManager;

/**
 * Contact
 */
public class Contact
{
    @Inject
    private QueryManager qm;
    
    @OnEvent(ACTIVATE)
    void doSomeThing()
    {
        List<SensorDefinition> x = qm.findAllActiveSensorDefinitions();
        for (SensorDefinition sd : x)
        {
            System.out.println("found active sd: " +sd.getDescription());
        }
        
        x = qm.findAllSensorDefinitions();
        for (SensorDefinition sd : x)
        {
            System.out.println("found sd: " +sd.getDescription());
        }
    }
    
}
