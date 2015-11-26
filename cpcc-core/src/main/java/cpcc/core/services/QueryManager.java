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

package cpcc.core.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cpcc.core.entities.Device;
import cpcc.core.entities.DeviceType;
import cpcc.core.entities.MappingAttributes;
import cpcc.core.entities.Parameter;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.Topic;

/**
 * QueryManager
 */
public interface QueryManager
{
    /**
     * @param topicRoot the device name.
     * @return the retrieved device or null if not found.
     */
    Device findDeviceByTopicRoot(String topicRoot);

    /**
     * @return the list of devices.
     */
    List<Device> findAllDevices();

    /**
     * @param name the name of the requested device type.
     * @return the corresponding device type.
     */
    DeviceType findDeviceTypeByName(String name);

    /**
     * @return the list of device types.
     */
    List<DeviceType> findAllDeviceTypes();

    /**
     * @param device the device
     * @param topic the topic
     * @return the mapping attributes associated with the given topic.
     */
    MappingAttributes findMappingAttribute(Device device, Topic topic);

    /**
     * @param device the device.
     * @return the mapping attributes.
     */
    Collection<MappingAttributes> findMappingAttributesByDevice(Device device);

    /**
     * @return the mapping attributes.
     */
    List<MappingAttributes> findAllMappingAttributes();

    /**
     * @return the mapping attributes visible in virtual vehicles.
     */
    List<MappingAttributes> findAllVvVisibleMappingAttributes();

    /**
     * @return the mapping attributes.
     */
    Map<String, MappingAttributes> findAllMappingAttributesAsMap();

    /**
     * @param topic the topic as a string
     * @return the mapping attributes associated with the given topic.
     */
    MappingAttributes findMappingAttributesByTopic(String topic);

    /**
     * @param device the device
     */
    void saveOrUpdateMappingAttributes(Device device);

    /**
     * @param name the parameter name.
     * @return the parameter or null if not found.
     */
    Parameter findParameterByName(String name);

    /**
     * @param name the parameter name.
     * @param defaultValue the default value, if the parameter does not exist (yet).
     * @return the parameter or null if not found.
     */
    Parameter findParameterByName(String name, String defaultValue);

    /**
     * @param id the sensor definition identification.
     * @return the sensor definition.
     */
    SensorDefinition findSensorDefinitionById(Integer id);

    /**
     * @param type the message type.
     * @return the list of sensor definitions.
     */
    List<SensorDefinition> findSensorDefinitionsByMessageType(String type);

    /**
     * @return the list of sensor definitions.
     */
    List<SensorDefinition> findAllSensorDefinitions();

    /**
     * @return the list of visible sensor definitions.
     */
    List<SensorDefinition> findAllVisibleSensorDefinitions();

    /**
     * @return the list of active sensor definitions.
     */
    List<SensorDefinition> findAllActiveSensorDefinitions();

    /**
     * @param description the name of the sensor definition.
     * @return the sensor definition or null if not found.
     */
    SensorDefinition findSensorDefinitionByDescription(String description);

    /**
     * @return the latest change time of a sensor definition or real vehicle.
     */
    Date findLatestSensorDefinitionOrRealVehicleChangeDate();

    /**
     * @param id the sensor definition identification.
     */
    void deleteSensorDefinitionById(Integer id);
}
