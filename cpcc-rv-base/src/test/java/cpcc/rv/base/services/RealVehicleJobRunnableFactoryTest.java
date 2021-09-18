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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.tapestry5.ioc.ServiceResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cpcc.core.entities.Job;
import cpcc.core.services.jobs.JobRunnable;

class RealVehicleJobRunnableFactoryTest
{
    private RealVehicleJobRunnableFactory sut;
    private ServiceResources serviceResources;

    @BeforeEach
    void setUp()
    {
        serviceResources = mock(ServiceResources.class);

        sut = new RealVehicleJobRunnableFactory();
    }

    @Test
    void shouldCreateRealVehicleStateJobRunnable()
    {
        Job job = mock(Job.class);
        when(job.getParameters()).thenReturn("mode=status,rv=2002");

        JobRunnable actual = sut.createRunnable(serviceResources, job);

        assertThat(actual).isNotNull().isInstanceOf(RealVehicleStateJobRunnable.class);
    }

    @Test
    void shouldCreateConfigSyncJobRunnable()
    {
        Job job = mock(Job.class);
        when(job.getParameters()).thenReturn("mode=config,rv=2002");

        JobRunnable actual = sut.createRunnable(serviceResources, job);

        assertThat(actual).isNotNull().isInstanceOf(ConfigPushJobRunnable.class);
    }

    @Test
    void shouldCreateRealVehicleInitJobRunnable()
    {
        Job job = mock(Job.class);
        when(job.getParameters()).thenReturn("mode=init");

        JobRunnable actual = sut.createRunnable(serviceResources, job);

        assertThat(actual).isNotNull().isInstanceOf(RealVehicleInitJobRunnable.class);
    }

    @Test
    void shouldCreateConfigImportJobRunnable()
    {
        Job job = mock(Job.class);
        when(job.getParameters()).thenReturn("mode=import");

        JobRunnable actual = sut.createRunnable(serviceResources, job);

        assertThat(actual).isNotNull().isInstanceOf(ConfigImportJobRunnable.class);
    }

    @Test
    void shouldReturnNullOnUnknownMode()
    {
        Job job = mock(Job.class);
        when(job.getParameters()).thenReturn("mode=unknown,rv=2002");

        JobRunnable actual = sut.createRunnable(serviceResources, job);

        assertThat(actual).isNull();
    }

}
