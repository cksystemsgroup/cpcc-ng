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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.Parameter;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.services.jobs.JobCreationException;
import at.uni_salzburg.cs.cpcc.core.services.jobs.JobService;

public class StateSynchronizerTest
{
    private static final String EXPECTED_CONFIG_PARAMETERS = "mode=config,rv=2002";
    private static final String EXPECTED_RV_PARAMETERS = "mode=status,rv=2002";

    private Logger logger;
    private QueryManager qm;
    private JobService jobService;
    private StateSynchronizerImpl sut;
    private Parameter hostRvName;

    @BeforeMethod
    public void setUp()
    {
        String rv01Name = "RV01";
        String rv02Name = "RV02";

        hostRvName = mock(Parameter.class);
        when(hostRvName.getName()).thenReturn(Parameter.REAL_VEHICLE_NAME);
        when(hostRvName.getValue()).thenReturn(rv01Name);

        RealVehicle rv01 = mock(RealVehicle.class);
        when(rv01.getId()).thenReturn(1001);
        when(rv01.getName()).thenReturn(rv01Name);

        RealVehicle rv02 = mock(RealVehicle.class);
        when(rv02.getName()).thenReturn(rv02Name);
        when(rv02.getId()).thenReturn(2002);

        logger = mock(Logger.class);

        qm = mock(QueryManager.class);
        when(qm.findAllRealVehicles()).thenReturn(Arrays.asList(rv01, rv02));

        jobService = mock(JobService.class);

        sut = new StateSynchronizerImpl(logger, qm, jobService);
    }

    @Test
    public void shouldSynchronizeConfig() throws JobCreationException
    {
        when(qm.findParameterByName(Parameter.REAL_VEHICLE_NAME)).thenReturn(hostRvName);

        sut.pushConfiguration();

        verify(qm).findParameterByName(Parameter.REAL_VEHICLE_NAME);
        verify(qm).findAllRealVehicles();

        verify(jobService).addJob(RealVehicleBaseConstants.JOB_QUEUE_NAME, EXPECTED_CONFIG_PARAMETERS);
        verifyZeroInteractions(logger);
    }

    @Test
    public void shouldSynchronizeRealVehicleState() throws JobCreationException
    {
        when(qm.findParameterByName(Parameter.REAL_VEHICLE_NAME)).thenReturn(hostRvName);

        sut.realVehicleStatusUpdate();

        verify(qm).findParameterByName(Parameter.REAL_VEHICLE_NAME);
        verify(qm).findAllRealVehicles();

        verify(jobService).addJob(RealVehicleBaseConstants.JOB_QUEUE_NAME, EXPECTED_RV_PARAMETERS);
        verifyZeroInteractions(logger);
    }

    @Test
    public void shouldLogErrorMessageIfRvNameIsNull() throws JobCreationException
    {
        when(qm.findParameterByName(Parameter.REAL_VEHICLE_NAME)).thenReturn(null);

        sut.pushConfiguration();

        verify(qm).findParameterByName(Parameter.REAL_VEHICLE_NAME);

        verify(logger).error("Hosting real vehicle name is not configured. Config sync aborted!");
    }

    @Test
    public void shouldLogErrorMessageIfRvNameIsEmpty() throws JobCreationException
    {
        Parameter emptyRvName = mock(Parameter.class);
        when(emptyRvName.getName()).thenReturn(Parameter.REAL_VEHICLE_NAME);
        when(emptyRvName.getValue()).thenReturn("");

        when(qm.findParameterByName(Parameter.REAL_VEHICLE_NAME)).thenReturn(emptyRvName);

        sut.pushConfiguration();

        verify(qm).findParameterByName(Parameter.REAL_VEHICLE_NAME);

        verify(logger).error("Hosting real vehicle name is not configured. Config sync aborted!");
    }

    @Test
    public void shouldLogErrorMessageAddingJobsIsNotPossible() throws JobCreationException
    {
        when(qm.findParameterByName(Parameter.REAL_VEHICLE_NAME)).thenReturn(hostRvName);

        JobCreationException toBeThrown = mock(JobCreationException.class);
        when(toBeThrown.getMessage())
            .thenReturn("Thrown on purpose!");

        doThrow(toBeThrown)
            .when(jobService).addJob(RealVehicleBaseConstants.JOB_QUEUE_NAME, EXPECTED_CONFIG_PARAMETERS);

        sut.pushConfiguration();

        verify(qm).findParameterByName(Parameter.REAL_VEHICLE_NAME);

        verify(logger)
            .error("Can not create config sync job for real vehicle RV02 (2002), mode=config Thrown on purpose!");
    }
}
