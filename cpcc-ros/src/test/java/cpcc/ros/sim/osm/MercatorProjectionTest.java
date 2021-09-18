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

package cpcc.ros.sim.osm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MercatorProjectionTest
{
    static Stream<Arguments> mercatorData()
    {
        return Stream.of(
            arguments(19, 37.80789, -122.42697, 83846, 202587, 205, 125),
            arguments(18, 37.80789, -122.42697, 41923, 101293, 102, 190));
    }

    @ParameterizedTest
    @MethodSource("mercatorData")
    void shouldConvertMercatorToTileCoordinates(int zoomLevel, double latitude, double longitude, int x, int y,
        int xPixel, int yPixel)
    {
        MercatorProjection mp = new MercatorProjection(zoomLevel, latitude, longitude);
        assertThat(mp.getxTile()).isEqualTo(x);
        assertThat(mp.getyTile()).isEqualTo(y);
        assertThat(mp.getxPixel()).isEqualTo(xPixel);
        assertThat(mp.getyPixel()).isEqualTo(yPixel);
        assertThat(mp.equalsTile(mp)).isTrue();
    }

    @Test
    void shouldRecognizeTheSameTileCoordinates()
    {
        MercatorProjection mp1 = new MercatorProjection(19, 37.80789, -122.42697);
        MercatorProjection mp2 = new MercatorProjection(19, 37.80788, -122.42696);

        assertThat(mp1.equalsTile(mp2)).isTrue();
        assertThat(mp1).isNotEqualTo(mp2);
    }

    static Stream<Arguments> nonEqualMercatorProjectionDataProvider()
    {
        return Stream.of(
            arguments(
                new MercatorProjection(19, 37.80789, -122.42697),
                new MercatorProjection(19, 37.80789, -102.42697)),
            arguments(
                new MercatorProjection(19, 37.80789, -122.42697),
                new MercatorProjection(19, 36.80789, -122.42697)),
            arguments(
                new MercatorProjection(19, 37.80788, -122.42696),
                new MercatorProjection(18, 37.80789, -122.42697)));
    };

    @ParameterizedTest
    @MethodSource("nonEqualMercatorProjectionDataProvider")
    void shouldRecognizeDifferentTileCoordinates(MercatorProjection a, MercatorProjection b)
    {
        assertThat(a.equalsTile(b)).isFalse();
    }
}
