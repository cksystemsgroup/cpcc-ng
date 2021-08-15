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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import cpcc.core.entities.Parameter;
import cpcc.core.services.QueryManager;

/**
 * Setup Service Test implementation.
 */
public class SetupServiceTest
{
    private static final String RV_ONE_NAME = "RV-ONE";

    private Logger logger;
    private HibernateSessionManager sessionManager;
    private QueryManager qm;
    private SetupServiceImpl sut;
    private Session session;
    private Parameter rvName;

    @BeforeEach
    public void setUp()
    {
        logger = mock(Logger.class);

        session = mock(Session.class);

        sessionManager = mock(HibernateSessionManager.class);
        when(sessionManager.getSession()).thenReturn(session);

        rvName = mock(Parameter.class);
        when(rvName.getName()).thenReturn(Parameter.REAL_VEHICLE_NAME);
        when(rvName.getValue()).thenReturn(RV_ONE_NAME);

        qm = mock(QueryManager.class);

        sut = new SetupServiceImpl(logger, sessionManager, qm);
    }

    @Test
    public void shouldSetupAnEmptySystem()
    {
        sut.setupRealVehicle();

        verify(qm).findParameterByName(Parameter.REAL_VEHICLE_NAME);
        verify(qm).findParameterByName(Parameter.MASTER_SERVER_URI);
        verify(qm).findParameterByName(Parameter.USE_INTERNAL_ROS_CORE);
        verify(sessionManager).commit();
        verify(session, times(3)).saveOrUpdate(any());

        verify(logger).info(eq("Automatic configuration: {}={}"),
            eq(Parameter.REAL_VEHICLE_NAME),
            matches("RV-\\S+"));
        verify(logger).info(eq("Automatic configuration: {}={}"),
            eq(Parameter.MASTER_SERVER_URI),
            matches("http://localhost:\\d+"));
        verify(logger).info(eq("Automatic configuration: {}={}"),
            eq(Parameter.USE_INTERNAL_ROS_CORE),
            eq("true"));
    }

    @Test
    public void shouldSetupAnPartlySetupSystem()
    {
        when(qm.findParameterByName(Parameter.REAL_VEHICLE_NAME)).thenReturn(rvName);

        sut.setupRealVehicle();

        verify(qm).findParameterByName(Parameter.REAL_VEHICLE_NAME);
        verify(qm).findParameterByName(Parameter.MASTER_SERVER_URI);
        verify(qm).findParameterByName(Parameter.USE_INTERNAL_ROS_CORE);
        verify(sessionManager).commit();
        verify(session, times(2)).saveOrUpdate(any());

        verify(logger).info(eq("Automatic configuration: {}={}"),
            eq(Parameter.MASTER_SERVER_URI),
            matches("http://localhost:\\d+"));
        verify(logger).info(eq("Automatic configuration: {}={}"),
            eq(Parameter.USE_INTERNAL_ROS_CORE),
            eq("true"));
        verifyNoMoreInteractions(logger);
    }
}
