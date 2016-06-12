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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.entities.Parameter;
import cpcc.core.entities.RealVehicle;
import cpcc.core.services.QueryManager;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.jobs.JobCreationException;
import cpcc.core.services.jobs.JobService;

public class StateSynchronizerTest
{
    private static final String EXPECTED_CONFIG_PARAMETERS_1 = "mode=config,rv=1001";
    private static final String EXPECTED_CONFIG_PARAMETERS_2 = "mode=config,rv=2002";
    private static final String EXPECTED_RV_PARAMETERS = "mode=status,rv=1001";

    private Logger logger;
    private QueryManager qm;
    private JobService jobService;
    private StateSynchronizerImpl sut;
    private Parameter hostRvName;
    private RealVehicle rv01;
    private RealVehicle rv02;
    private RealVehicleRepository realVehicleRepository;

    @BeforeMethod
    public void setUp()
    {
        String rv01Name = "RV01";
        String rv02Name = "RV02";

        hostRvName = mock(Parameter.class);
        when(hostRvName.getName()).thenReturn(Parameter.REAL_VEHICLE_NAME);
        when(hostRvName.getValue()).thenReturn(rv01Name);

        rv01 = mock(RealVehicle.class);
        when(rv01.getId()).thenReturn(1001);
        when(rv01.getName()).thenReturn(rv01Name);

        rv02 = mock(RealVehicle.class);
        when(rv02.getName()).thenReturn(rv02Name);
        when(rv02.getId()).thenReturn(2002);

        logger = mock(Logger.class);

        qm = mock(QueryManager.class);

        realVehicleRepository = mock(RealVehicleRepository.class);
        when(realVehicleRepository.findAllActiveRealVehicles()).thenReturn(Arrays.asList(rv01, rv02));
        
        jobService = mock(JobService.class);

        sut = new StateSynchronizerImpl(logger, qm, jobService, realVehicleRepository);
    }

    @Test
    public void shouldSynchronizeConfig() throws JobCreationException
    {
        when(qm.findParameterByName(Parameter.REAL_VEHICLE_NAME)).thenReturn(hostRvName);

        sut.pushConfiguration();

        verify(qm).findParameterByName(Parameter.REAL_VEHICLE_NAME);
        verify(realVehicleRepository).findAllActiveRealVehicles();

        verify(jobService).addJob(RealVehicleBaseConstants.JOB_QUEUE_NAME, EXPECTED_CONFIG_PARAMETERS_1);
        verify(jobService).addJob(RealVehicleBaseConstants.JOB_QUEUE_NAME, EXPECTED_CONFIG_PARAMETERS_2);
        verifyZeroInteractions(logger);
    }

    @DataProvider
    public Object[][] importConfigDataProvider()
    {
        return new Object[][]{
            new Object[]{new byte[]{11, 12, 13, 14}},
            new Object[]{new byte[]{21, 22, 23, 24}},
        };
    }

    @Test(dataProvider = "importConfigDataProvider")
    public void shouldImportConfiguratin(byte[] data) throws JobCreationException
    {
        sut.importConfiguration(data);

        verify(jobService).addJobIfNotExists(RealVehicleBaseConstants.JOB_QUEUE_NAME, "mode=import", data);
    }

    @Test
    public void shouldSynchronizeRealVehicleState() throws JobCreationException
    {
        when(rv02.getDeleted()).thenReturn(true);
        when(qm.findParameterByName(Parameter.REAL_VEHICLE_NAME)).thenReturn(hostRvName);

        sut.realVehicleStatusUpdate();

        verify(qm).findParameterByName(Parameter.REAL_VEHICLE_NAME);
        verify(realVehicleRepository).findAllActiveRealVehicles();

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
            .when(jobService).addJob(RealVehicleBaseConstants.JOB_QUEUE_NAME, EXPECTED_CONFIG_PARAMETERS_2);

        sut.pushConfiguration();

        verify(qm).findParameterByName(Parameter.REAL_VEHICLE_NAME);

        verify(logger)
            .debug("Can not create config sync job for real vehicle RV02 (2002), mode=config Thrown on purpose!");
    }
}
