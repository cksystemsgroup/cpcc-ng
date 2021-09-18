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

package cpcc.core.services.jobs;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class JobCreationExceptionTest
{
    @Test
    void shouldHaveDefaultConstructor() throws JobCreationException
    {
        ExceptionThrower sut = new ExceptionThrower();

        catchException(() -> sut.throwException());

        assertThat(caughtException().getMessage()).isNull();
    }

    static Stream<Arguments> messagesDataProvider()
    {
        return Stream.of(
            arguments("msg1"),
            arguments("msg2"));
    }

    @ParameterizedTest
    @MethodSource("messagesDataProvider")
    void shouldHaveMessageConstructor(String expected) throws JobCreationException
    {
        ExceptionThrower sut = new ExceptionThrower();

        catchException(() -> sut.throwException(expected));

        assertThat(caughtException().getMessage()).isEqualTo(expected);
    }

    static Stream<Arguments> messageAndCauseDataProvider()
    {
        return Stream.of(
            arguments("msg1", new IOException("xx")),
            arguments("msg2", new IllegalStateException()));
    }

    @ParameterizedTest
    @MethodSource("messageAndCauseDataProvider")
    void shouldHaveMessageAndCauseConstructor(String expectedMessage, Throwable expectedCause)
        throws JobCreationException
    {
        ExceptionThrower sut = new ExceptionThrower();

        catchException(() -> sut.throwException(expectedMessage, expectedCause));

        assertThat(caughtException().getMessage()).isEqualTo(expectedMessage);
        assertThat(caughtException().getCause()).isEqualTo(expectedCause);
    }

    static class ExceptionThrower
    {
        public void throwException() throws JobCreationException
        {
            throw new JobCreationException();
        }

        public void throwException(String message) throws JobCreationException
        {
            throw new JobCreationException(message);
        }

        public void throwException(String message, Throwable cause) throws JobCreationException
        {
            throw new JobCreationException(message, cause);
        }
    }
}
