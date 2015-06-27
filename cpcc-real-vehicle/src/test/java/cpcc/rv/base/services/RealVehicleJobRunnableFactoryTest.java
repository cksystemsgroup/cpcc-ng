// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.rv.base.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationService;
import at.uni_salzburg.cs.cpcc.core.entities.Job;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.services.jobs.JobRunnable;
import at.uni_salzburg.cs.cpcc.core.services.jobs.TimeService;

public class RealVehicleJobRunnableFactoryTest
{
    private Session session;
    private QueryManager qm;
    private CommunicationService com;
    private TimeService timeService;
    private RealVehicleJobRunnableFactory sut;
    private Logger logger;

    @BeforeMethod
    public void setUp()
    {
        session = mock(Session.class);
        qm = mock(QueryManager.class);
        com = mock(CommunicationService.class);
        timeService = mock(TimeService.class);
        logger = mock(Logger.class);

        sut = new RealVehicleJobRunnableFactory(session, qm, com, timeService, logger);
    }

    @Test
    public void shouldCreateRealVehicleStateJobRunnable()
    {
        Job job = mock(Job.class);
        when(job.getParameters()).thenReturn("mode=status,rv=2002");

        JobRunnable actual = sut.createRunnable(job);

        assertThat(actual).isNotNull().isInstanceOf(RealVehicleStateJobRunnable.class);
    }

    @Test
    public void shouldCreateConfigSyncJobRunnable()
    {
        Job job = mock(Job.class);
        when(job.getParameters()).thenReturn("mode=config,rv=2002");

        JobRunnable actual = sut.createRunnable(job);

        assertThat(actual).isNotNull().isInstanceOf(ConfigSyncJobRunnable.class);
    }

    @Test
    public void shouldReturnNullOnUnknownMode()
    {
        Job job = mock(Job.class);
        when(job.getParameters()).thenReturn("mode=unknown,rv=2002");

        JobRunnable actual = sut.createRunnable(job);

        assertThat(actual).isNull();
    }

}
