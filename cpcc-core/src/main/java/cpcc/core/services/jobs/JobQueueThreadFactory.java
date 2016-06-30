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

package cpcc.core.services.jobs;

import java.util.concurrent.ThreadFactory;

/**
 * Job Queue Thread Factory implementation.
 */
public class JobQueueThreadFactory implements ThreadFactory
{
    private long counter = 0;
    private String namePrefix;

    /**
     * @param namePrefix the name prefix.
     */
    public JobQueueThreadFactory(String namePrefix)
    {
        this.namePrefix = namePrefix.replace(" ", "-");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Thread newThread(Runnable r)
    {
        return new Thread(r, namePrefix + "-" + (counter++));
    }
}
