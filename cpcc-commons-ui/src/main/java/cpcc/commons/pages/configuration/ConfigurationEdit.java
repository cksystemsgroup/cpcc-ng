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

package cpcc.commons.pages.configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.Format;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.hibernate.Session;

import cpcc.commons.services.EnumFormatter;
import cpcc.core.entities.Device;
import cpcc.core.entities.MappingAttributes;
import cpcc.core.entities.Parameter;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.services.QueryManager;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.SensorDefinitionSelectHelpers;
import cpcc.ros.services.RosNodeService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Configuration edit page.
 */
public class ConfigurationEdit
{
    private static final String MSG_DELETE_DEVICE_CONFIRM = "deleteDeviceConfirm";

    @Inject
    private Messages messages;

    @Inject
    private Request request;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @InjectComponent
    private Zone realVehicleNameFormZone;

    @InjectComponent
    private Zone uriFormZone;

    @InjectComponent
    private Zone coreFormZone;

    @InjectComponent
    private Zone deviceFormZone;

    @InjectComponent
    private Zone mappingFormZone;

    @Inject
    private Session session;

    @Inject
    private QueryManager qm;

    @Inject
    private RosNodeService nodeService;

    @Inject
    private RealVehicleRepository rvRepo;

    @Property
    private Parameter internalRosCore;

    @Property
    private Parameter masterServerURI;

    @Property
    private Parameter realVehicleName;

    @Property
    private RealVehicle realVehicle;

    @Property
    private Collection<Device> deviceList;

    @Property
    private Device deviceConfig;

    @Property
    private Collection<MappingAttributes> mappingList;

    @Property
    private MappingAttributes mappingConfig;

    @SuppressFBWarnings(value = "URF_UNREAD_FIELD", justification = "The Tapestry template uses this formatter.")
    @Property
    private Format enumFormat;

    public String getDeleteDeviceConfirmationMessage()
    {
        return messages.format(MSG_DELETE_DEVICE_CONFIRM, deviceConfig.getTopicRoot());
    }

    void onPrepare()
    {
        masterServerURI = qm.findParameterByName(Parameter.MASTER_SERVER_URI, "");
        internalRosCore = qm.findParameterByName(Parameter.USE_INTERNAL_ROS_CORE, "");
        realVehicleName = qm.findParameterByName(Parameter.REAL_VEHICLE_NAME, "");
        realVehicle = rvRepo.findRealVehicleByName(realVehicleName.getValue());
        deviceList = qm.findAllDevices();
        mappingList = orderByTopic(qm.findAllMappingAttributes());
        enumFormat = new EnumFormatter(messages);
    }

    /**
     * @param attributeList the mapping attributes
     * @return the mapping attributes ordered by topic
     */
    private Collection<MappingAttributes> orderByTopic(Collection<MappingAttributes> attributeList)
    {
        Map<String, MappingAttributes> tree = new TreeMap<>();

        for (MappingAttributes attribute : attributeList)
        {
            StringBuilder b = new StringBuilder(attribute.getPk().getDevice().getTopicRoot());

            String subPath = attribute.getPk().getTopic().getSubpath();

            if (subPath != null)
            {
                b.append("/").append(subPath);
            }
            tree.put(b.toString(), attribute);
        }

        return tree.values();
    }

    @CommitAfter
    void onDeleteDevice(String topic)
    {
        Device device = qm.findDeviceByTopicRoot(topic);
        Collection<MappingAttributes> mappingCollection = qm.findMappingAttributesByDevice(device);

        nodeService.shutdownMappingAttributes(mappingCollection);
        nodeService.shutdownDevice(device);

        for (MappingAttributes mapping : mappingCollection)
        {
            session.delete(mapping);
        }
        session.delete(device);

        handleXhrRequest(deviceFormZone);
    }

    @CommitAfter
    void onSuccessFromRealVehicleNameForm()
    {
        if (realVehicleName.getValue() == null)
        {
            handleXhrRequest(realVehicleNameFormZone);
            return;
        }
        session.saveOrUpdate(realVehicleName);

        if (realVehicle == null)
        {
            realVehicle = rvRepo.findRealVehicleByName(realVehicleName.getValue());
            handleXhrRequest(realVehicleNameFormZone);
            return;
        }

        realVehicle.setName(realVehicleName.getValue());
        session.saveOrUpdate(realVehicle);
        handleXhrRequest(realVehicleNameFormZone);
    }

    @CommitAfter
    void onSuccessFromUriForm() throws URISyntaxException
    {
        session.saveOrUpdate(masterServerURI);
        nodeService.updateMasterServerURI(new URI(masterServerURI.getValue()));
        handleXhrRequest(uriFormZone);
    }

    @CommitAfter
    void onSuccessFromCoreForm()
    {
        session.saveOrUpdate(internalRosCore);
        nodeService.updateRosCore(Boolean.parseBoolean(internalRosCore.getValue()));
        handleXhrRequest(coreFormZone);
    }

    @CommitAfter
    void onSuccessFromMappingForm()
    {
        session.saveOrUpdate(mappingConfig);

        if (realVehicle != null)
        {
            List<SensorDefinition> sdList = new ArrayList<>();
            for (MappingAttributes x : mappingList)
            {
                SensorDefinition sd = x.getSensorDefinition();
                if (sd != null)
                {
                    sdList.add(sd);
                }
            }
            realVehicle.setSensors(sdList);
            realVehicle.setLastUpdate(new Date());
            session.saveOrUpdate(realVehicle);
        }

        nodeService.updateMappingAttributes(mappingList);

        handleXhrRequest(mappingFormZone);
    }

    /**
     * @return the sensor definition select model.
     */
    public SelectModel getSensorDefinitionNameSelectModel()
    {
        return SensorDefinitionSelectHelpers.selectModel(
            qm.findSensorDefinitionsByMessageType(mappingConfig.getPk().getTopic().getMessageType()));
    }

    /**
     * @return the sensor definition name encoder.
     */
    public ValueEncoder<SensorDefinition> getSensorDefinitionNameEncoder()
    {
        return new SensorDefinitionSelectHelpers(qm).valueEncoder();
    }

    /**
     * @return true if sensor definitions are available, false otherwise.
     */
    public Boolean getSensorDefinitionsAvailable()
    {
        return !qm.findSensorDefinitionsByMessageType(mappingConfig.getPk().getTopic().getMessageType()).isEmpty();
    }

    /**
     * @param zone the zone.
     */
    private void handleXhrRequest(Zone zone)
    {
        if (request.isXHR())
        {
            ajaxResponseRenderer.addRender(zone);
        }
    }
}
