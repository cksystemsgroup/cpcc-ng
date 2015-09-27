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

package cpcc.ros.sensors;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * AbstractGpsSensorAdapter
 */
public abstract class AbstractGpsSensorAdapter extends AbstractSensorAdapter
{
    private sensor_msgs.NavSatFix position;

    /**
     * {@inheritDoc}
     */
    @Override
    public SensorType getType()
    {
        return SensorType.GPS_RECEIVER;
    }

    /**
     * @return the current GPS position.
     */
    public sensor_msgs.NavSatFix getPosition()
    {
        return position;
    }

    /**
     * @param position the position to set
     */
    protected void setPosition(sensor_msgs.NavSatFix position)
    {
        this.position = position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<String>> getCurrentState()
    {
        Map<String, List<String>> map = super.getCurrentState();

        if (position != null)
        {
            map.put("sensor.gps.position", Arrays.asList(
                String.format(Locale.US, "%.8f", position.getLatitude()),
                String.format(Locale.US, "%.8f", position.getLongitude()),
                String.format(Locale.US, "%.3f", position.getAltitude())
                ));
        }

        return map;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public sensor_msgs.NavSatFix getValue()
    {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(Object object)
    {
        throw new IllegalStateException();
    }
}
