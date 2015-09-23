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

package at.uni_salzburg.cs.cpcc.core.services.jobs;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class JobExecutionExceptionTest
{

    @Test
    public void shouldHaveDefaultConstructor() throws JobExecutionException
    {
        ExceptionThrower sut = new ExceptionThrower();

        catchException(sut).throwException();

        assertThat(caughtException().getMessage()).isNull();
    }

    @DataProvider
    public Object[][] messagesDataProvider()
    {
        return new Object[][]{
            new Object[]{"msg1"},
            new Object[]{"msg2"},
        };
    }

    @Test(dataProvider = "messagesDataProvider")
    public void shouldHaveMessageConstructor(String expected) throws JobExecutionException
    {
        ExceptionThrower sut = new ExceptionThrower();

        catchException(sut).throwException(expected);

        assertThat(caughtException().getMessage()).isEqualTo(expected);
    }

    @DataProvider
    public Object[][] messageAndCauseDataProvider()
    {
        return new Object[][]{
            new Object[]{"msg1", new IOException("xx")},
            new Object[]{"msg2", new IllegalStateException()},
        };
    }

    @Test(dataProvider = "messageAndCauseDataProvider")
    public void shouldHaveMessageAndCauseConstructor(String expectedMessage, Throwable expectedCause)
        throws JobExecutionException
    {
        ExceptionThrower sut = new ExceptionThrower();

        catchException(sut).throwException(expectedMessage, expectedCause);

        assertThat(caughtException().getMessage()).isEqualTo(expectedMessage);
        assertThat(caughtException().getCause()).isEqualTo(expectedCause);
    }

    public static class ExceptionThrower
    {
        public void throwException() throws JobExecutionException
        {
            throw new JobExecutionException();
        }

        public void throwException(String message) throws JobExecutionException
        {
            throw new JobExecutionException(message);
        }

        public void throwException(String message, Throwable cause) throws JobExecutionException
        {
            throw new JobExecutionException(message, cause);
        }
    }
}
