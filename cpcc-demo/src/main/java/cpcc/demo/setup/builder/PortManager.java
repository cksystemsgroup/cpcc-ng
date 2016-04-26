// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.demo.setup.builder;

/**
 * PortManager implementation.
 */
public class PortManager
{
    private int basePort;

    /**
     * @param basePort the TCP base port number.
     */
    public PortManager(int basePort)
    {
        this.basePort = basePort;
    }

    /**
     * @return the TCP base port number.
     */
    public int getBasePort()
    {
        return basePort;
    }

    /**
     * @param rvId the real vehicle identification.
     * @return the HTTP port number of the real vehicle..
     */
    public int getRvPort(int rvId)
    {
        return basePort + 10 * (rvId - 1);
    }

    /**
     * @param rvId the real vehicle identification.
     * @return the ROS master server port number of the real vehicle..
     */
    public int getRosPort(int rvId)
    {
        return getRvPort(rvId) + 1;
    }
}
