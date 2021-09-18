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

package cpcc.rv.base.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import cpcc.com.services.CommunicationResponse;
import cpcc.com.services.CommunicationService;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;
import cpcc.core.services.RealVehicleRepository;

/**
 * RealVehicleStateJobRunnableTest implementation.
 */
class RealVehicleStateJobRunnableTest
{
    private static final int RV_ID = 123;

    private static final String RESPONSE_STRING = "response string";

    private ServiceResources serviceResources;
    private Map<String, String> parameters;
    private RealVehicleStateJobRunnable sut;
    private HibernateSessionManager sessionManager;
    private CommunicationService com;
    private RealVehicleRepository rvRepo;
    private RealVehicle rv;
    private Session session;
    private CommunicationResponse response;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() throws Exception
    {
        parameters = mock(Map.class);
        when(parameters.get("rv")).thenReturn(Integer.toString(RV_ID));

        rv = mock(RealVehicle.class);
        when(rv.getId()).thenReturn(RV_ID);
        when(rv.getName()).thenReturn("RV01");

        session = mock(Session.class);

        sessionManager = mock(HibernateSessionManager.class);
        when(sessionManager.getSession()).thenReturn(session);

        response = mock(CommunicationResponse.class);
        when(response.getContent()).thenReturn(RESPONSE_STRING.getBytes("UTF-8"));

        com = mock(CommunicationService.class);

        rvRepo = mock(RealVehicleRepository.class);
        when(rvRepo.findRealVehicleById(RV_ID)).thenReturn(rv);

        serviceResources = mock(ServiceResources.class);
        when(serviceResources.getService(HibernateSessionManager.class)).thenReturn(sessionManager);
        when(serviceResources.getService(CommunicationService.class)).thenReturn(com);
        when(serviceResources.getService(RealVehicleRepository.class)).thenReturn(rvRepo);

        sut = new RealVehicleStateJobRunnable(serviceResources, parameters);
    }

    @Test
    void shouldHandleExistingRvState() throws ClientProtocolException, IOException
    {
        when(com.transfer(rv, RealVehicleBaseConstants.REAL_VEHICLE_STATUS_CONNECTOR, ArrayUtils.EMPTY_BYTE_ARRAY))
            .thenReturn(response);

        RealVehicleState rvState = mock(RealVehicleState.class);
        when(rvRepo.findRealVehicleStateById(RV_ID)).thenReturn(rvState);

        Date now = new Date();
        sut.run();

        assertThat(sut.executionSucceeded()).isTrue();

        ArgumentCaptor<Date> captor = ArgumentCaptor.forClass(Date.class);
        verify(rvState).setLastUpdate(captor.capture());
        Date value = captor.getValue();
        assertThat(value.getTime() - now.getTime()).isLessThan(1000L);

        verify(rvState).setRealVehicleName(rv.getName());
        verify(rvState).setState(RESPONSE_STRING);
        verifyNoMoreInteractions(rvState);

        verify(session).saveOrUpdate(rvState);
    }

    @Test
    void shouldHandleMissingRvState() throws ClientProtocolException, IOException
    {
        when(com.transfer(rv, RealVehicleBaseConstants.REAL_VEHICLE_STATUS_CONNECTOR, ArrayUtils.EMPTY_BYTE_ARRAY))
            .thenReturn(response);

        Date now = new Date();
        sut.run();

        assertThat(sut.executionSucceeded()).isTrue();

        ArgumentCaptor<RealVehicleState> captor = ArgumentCaptor.forClass(RealVehicleState.class);

        verify(session).saveOrUpdate(captor.capture());

        RealVehicleState rvState = captor.getValue();
        assertThat(rvState.getId()).isEqualTo(rv.getId());
        assertThat(rvState.getLastUpdate().getTime() - now.getTime()).isLessThan(1000);
        assertThat(rvState.getRealVehicleName()).isEqualTo(rv.getName());
        assertThat(rvState.getState()).isEqualTo(RESPONSE_STRING);
    }

    @Test
    void shouldLogFailingConnections() throws ClientProtocolException, IOException
    {
        when(com.transfer(rv, RealVehicleBaseConstants.REAL_VEHICLE_STATUS_CONNECTOR, ArrayUtils.EMPTY_BYTE_ARRAY))
            .thenThrow(IOException.class);

        sut.run();

        assertThat(sut.executionSucceeded()).isFalse();
    }
}
