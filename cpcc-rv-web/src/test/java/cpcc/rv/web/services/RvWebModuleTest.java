// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.rv.web.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Constructor;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.commons.MappedConfiguration;
import org.junit.jupiter.api.Test;

/**
 * AppModuleTest
 */
public class RvWebModuleTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<RvWebModule> cnt = RvWebModule.class.getDeclaredConstructor();
        assertThat(cnt.isAccessible()).isFalse();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @Test
    public void shouldContributreFactoryDefaults()
    {
        @SuppressWarnings("unchecked")
        MappedConfiguration<String, Object> configuration = mock(MappedConfiguration.class);

        RvWebModule.contributeFactoryDefaults(configuration);

        verify(configuration).override(eq(SymbolConstants.APPLICATION_VERSION), any());
    }

    @Test
    public void shouldContributreApplicationDefaults()
    {
        @SuppressWarnings("unchecked")
        MappedConfiguration<String, Object> configuration = mock(MappedConfiguration.class);

        RvWebModule.contributeApplicationDefaults(configuration);

        verify(configuration).add(eq(SymbolConstants.SUPPORTED_LOCALES), any());
        verify(configuration).add(eq(SymbolConstants.MINIFICATION_ENABLED), any());
        verify(configuration).add(eq(SymbolConstants.HMAC_PASSPHRASE), any());
    }

}
