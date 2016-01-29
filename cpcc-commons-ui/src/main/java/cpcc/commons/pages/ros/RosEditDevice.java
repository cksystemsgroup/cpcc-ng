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

package cpcc.commons.pages.ros;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.PageActivationContext;

import cpcc.core.entities.Device;

/**
 * RosEditDevice
 */
@Import(module = "bootstrap/modal")
public class RosEditDevice extends AbstractRosModifyDevice
{
    @PageActivationContext
    private String deviceTopic;

    void onPrepare()
    {
        device = qm.findDeviceByTopicRoot(deviceTopic);
    }

    /**
     * Callback function for validating form data.
     */
    void onValidateFromForm()
    {
        if (!device.getTopicRoot().startsWith("/"))
        {
            String msg = messages.get(ERROR_TOPIC_MUST_START_WITH_SLASH);
            form.recordError(msg);
        }

        if (!deviceTopic.equals(device.getTopicRoot()))
        {
            Device dev = qm.findDeviceByTopicRoot(device.getTopicRoot());
            // TODO: ask KOC
            // TODO: use HQL: qm.isTopicAlreadyPresent() ...
            if (dev != null && dev != device)
            {
                String msg = messages.get(ERROR_TOPIC_ALREADY_USED);
                form.recordError(String.format(msg, device.getTopicRoot()));
            }
        }

        checkConfig();
    }
}
