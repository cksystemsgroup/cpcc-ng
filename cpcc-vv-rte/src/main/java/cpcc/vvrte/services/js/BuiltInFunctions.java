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

package cpcc.vvrte.services.js;

import java.util.List;

import org.mozilla.javascript.ScriptableObject;

/**
 * BuiltInFunctions
 */
public interface BuiltInFunctions
{
    /**
     * @return the list of sensors available in the CPCC system.
     */
    List<ScriptableObject> listSensors();

    /**
     * @return the list of active sensors of the current real vehicle.
     */
    List<ScriptableObject> listActiveSensors();

    /**
     * @param name the sensor name.
     * @return the sensor instance.
     */
    ScriptableObject getSensor(String name);

    //    /**
    //     * @param sensor the sensor instance.
    //     * @return the sensor value.
    //     */
    //    ScriptableObject getSensorValue(ScriptableObject sensor);

    /**
     * @param managementParameters the management parameters.
     * @param taskParameters the task parameters.
     */
    void executeTask(ScriptableObject managementParameters, ScriptableObject taskParameters);

    /**
     * @param name the object's name
     * @return the requested object or null, if not found.
     */
    ScriptableObject loadObject(String name);

    /**
     * @param name the object's name.
     * @param obj the object itself.
     */
    void storeObject(String name, ScriptableObject obj);

    /**
     * @param pattern the pattern to match the object names.
     * @return the list of found objects.
     */
    List<String> listObjects(String pattern);

    /**
     * @param name the object's name.
     */
    void removeObject(String name);
}
