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

package cpcc.core.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * RealVehicleTest
 */
public class RealVehicleTest
{

    private RealVehicle sut;

    @BeforeMethod
    public void setUp()
    {
        sut = new RealVehicle();
    }

    @DataProvider
    public Object[][] integerDataProvider()
    {
        return new Object[][]{
            new Object[]{0},
            new Object[]{1},
            new Object[]{2},
            new Object[]{3},
            new Object[]{5},
            new Object[]{7},
            new Object[]{11},
            new Object[]{1009},
        };
    }

    @Test(dataProvider = "integerDataProvider")
    public void shouldStoreId(int id)
    {
        sut.setId(id);
        assertThat(sut.getId()).isNotNull().isEqualTo(id);
    }

    @DataProvider
    public Object[][] stringDataProvider()
    {
        return new Object[][]{
            new Object[]{""},
            new Object[]{"a"},
            new Object[]{"a,b"},
            new Object[]{"a, b,c"},
            new Object[]{"a,b , c"},
        };
    }

    @Test(dataProvider = "stringDataProvider")
    public void shouldStoreAreaOfOperations(String areaOfOperation)
    {
        sut.setAreaOfOperation(areaOfOperation);
        assertThat(sut.getAreaOfOperation()).isNotNull().isEqualTo(areaOfOperation);
    }

    @DataProvider
    public Object[][] booleanDataProvider()
    {
        return new Object[][]{
            new Object[]{Boolean.TRUE},
            new Object[]{Boolean.FALSE},
        };
    }

    @Test(dataProvider = "booleanDataProvider")
    public void shouldStoreDeletedMarker(boolean deleted)
    {
        sut.setDeleted(deleted);
        assertThat(sut.getDeleted()).isNotNull().isEqualTo(deleted);
    }

    @DataProvider
    public Object[][] dateDataProvider()
    {
        return new Object[][]{
            new Object[]{new Date(System.currentTimeMillis() + 10)},
            new Object[]{new Date(System.currentTimeMillis() + 100)},
            new Object[]{new Date(System.currentTimeMillis() + 1000)},
        };
    }

    @Test(dataProvider = "dateDataProvider")
    public void shouldStoreLastUpdate(Date lastUpdate)
    {
        sut.setLastUpdate(lastUpdate);
        assertThat(sut.getLastUpdate()).isNotNull().isEqualTo(lastUpdate);
    }

    @Test(dataProvider = "stringDataProvider")
    public void shouldStoreName(String name)
    {
        sut.setName(name);
        assertThat(sut.getName()).isNotNull().isEqualTo(name);
    }

    @DataProvider
    public Object[][] typeDataProvider()
    {
        RealVehicleType[] types = RealVehicleType.values();
        Object[][] data = new Object[types.length][];
        for (int k = 0; k < types.length; ++k)
        {
            data[k] = new Object[]{types[k]};
        }
        return data;
    }

    @Test(dataProvider = "typeDataProvider")
    public void shouldStoreType(RealVehicleType type)
    {
        sut.setType(type);
        assertThat(sut.getType()).isNotNull().isEqualTo(type);
    }

    @Test(dataProvider = "stringDataProvider")
    public void shouldStoreUrl(String url)
    {
        sut.setUrl(url);
        assertThat(sut.getUrl()).isNotNull().isEqualTo(url);
    }

    @DataProvider
    public Object[][] sensorDataProvider()
    {
        return new Object[][]{
            new Object[]{new SensorDefinition[]{
                mock(SensorDefinition.class)}
            },
            new Object[]{new SensorDefinition[]{
                mock(SensorDefinition.class), mock(SensorDefinition.class)}
            },
            new Object[]{new SensorDefinition[]{
                mock(SensorDefinition.class), mock(SensorDefinition.class), mock(SensorDefinition.class)}
            },
        };
    }

    @Test(dataProvider = "sensorDataProvider")
    public void should(SensorDefinition[] sensors)
    {
        sut.setSensors(Arrays.asList(sensors));
        assertThat(sut.getSensors()).isNotNull().containsExactly(sensors);
    }

    @SuppressWarnings("unchecked")
    private static RealVehicle setupRealVehicle(Object... data)
    {
        RealVehicle realVehicle = new RealVehicle();
        realVehicle.setAreaOfOperation((String) data[0]);
        realVehicle.setDeleted((Boolean) data[1]);
        realVehicle.setId((Integer) data[2]);
        realVehicle.setLastUpdate((Date) data[3]);
        realVehicle.setName((String) data[4]);
        realVehicle.setSensors((List<SensorDefinition>) data[5]);
        realVehicle.setType((RealVehicleType) data[6]);
        realVehicle.setUrl((String) data[7]);
        return realVehicle;
    }

    @DataProvider
    public Object[][] equalRealVehicleDataProvider()
    {
        SensorDefinition sen1 = mock(SensorDefinition.class);

        RealVehicle rvA = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1),
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        RealVehicle rvB = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1),
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        RealVehicle rvC = setupRealVehicle("abc", true, 10, new Date(123456789), "rv01", Arrays.asList(sen1),
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        return new Object[][]{
            new Object[]{rvA, rvA},
            new Object[]{rvA, rvB},
            new Object[]{rvA, rvC},
        };
    }

    @Test(dataProvider = "equalRealVehicleDataProvider")
    public void shouldFindEqualRealVehicles(RealVehicle a, RealVehicle b)
    {
        assertThat(a.equals(b)).isTrue();
        assertThat(a.hashCode()).describedAs("hash code").isEqualTo(b.hashCode());
    }

    @DataProvider
    public Object[][] notEqualRealVehicleDataProvider()
    {
        SensorDefinition sen1 = mock(SensorDefinition.class);
        Mockito.when(sen1.getId()).thenReturn(1);
        SensorDefinition sen2 = mock(SensorDefinition.class);
        Mockito.when(sen1.getId()).thenReturn(2);

        RealVehicle rvA = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1),
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        RealVehicle rvB = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01",
            Arrays.asList(new SensorDefinition[0]), RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        RealVehicle rvC = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sen2),
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        RealVehicle rvD = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", null,
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        RealVehicle rvE = setupRealVehicle("abcd", false, 10, new Date(123456789), "rv01", null,
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        return new Object[][]{
            new Object[]{rvA,
                setupRealVehicle("abcd", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01")},
            new Object[]{rvA,
                setupRealVehicle("abc", false, 20, new Date(123456789), "rv01", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01")},
            new Object[]{rvA,
                setupRealVehicle("abc", false, 10, new Date(987654321), "rv01", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01")},
            new Object[]{rvA,
                setupRealVehicle("abc", false, 10, new Date(123456789), "rv02", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01")},
            new Object[]{rvA,
                setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1, sen2),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01")},
            new Object[]{rvA,
                setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1),
                    RealVehicleType.GROUND_STATION, "http://localhost:8080/rv01")},
            new Object[]{rvA,
                setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv02")},
            new Object[]{setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1, sen2),
                RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01"), rvA},
            new Object[]{rvA, rvB},
            new Object[]{rvB, rvA},
            new Object[]{rvA, rvC},
            new Object[]{rvC, rvA},
            new Object[]{rvA, rvD},
            new Object[]{rvD, rvA},
            new Object[]{rvD, rvE},
            new Object[]{rvE, rvD},
        };
    }

    @Test(dataProvider = "notEqualRealVehicleDataProvider")
    public void shouldFindNotEqualRealVehicles(RealVehicle a, RealVehicle b)
    {
        assertThat(a.equals(b)).isFalse();
        assertThat(a.hashCode()).describedAs("hash code").isNotEqualTo(b.hashCode());
    }

    @Test
    public void shouldReturnFalseOnComparisonWithNull()
    {
        RealVehicle a = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(),
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        assertThat(a.equals(null)).isFalse();
    }

    @Test
    public void shouldReturnFalseOnComparisonWithOtherTypes()
    {
        RealVehicle a = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(),
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        assertThat(a.equals(new Object[0])).isFalse();
    }

    @DataProvider
    public Object[][] realVehicleDataProvider()
    {
        SensorDefinition sen1 = new SensorDefinition();
        sen1.setId(9174);

        return new Object[][]{
            new Object[]{Arrays.asList(
                setupRealVehicle("abcd", false, 10, new Date(123456789L), "rv01", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01"),
                setupRealVehicle(null, false, 10, new Date(123456789L), "rv01", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01"),
                setupRealVehicle("abcd", false, null, new Date(123456789), "rv01", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01"),
                setupRealVehicle("abcd", false, 10, null, "rv01", Arrays.asList(sen1), RealVehicleType.QUADROCOPTER,
                    "http://localhost:8080/rv01"),
                setupRealVehicle("abcd", false, 10, new Date(123456789), null, Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01"),
                setupRealVehicle("abcd", false, 10, new Date(123456789), "rv01", null, RealVehicleType.QUADROCOPTER,
                    "http://localhost:8080/rv01"),
                setupRealVehicle("abcd", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1), null,
                    "http://localhost:8080/rv01"),
                setupRealVehicle("abcd", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, null))
            },
        };
    }

    @Test(dataProvider = "realVehicleDataProvider")
    public void shouldCalculateHashCode(List<RealVehicle> rvList)
    {
        Set<Integer> codeSet = new HashSet<Integer>();

        for (RealVehicle rv : rvList)
        {
            int hash = rv.hashCode();
            assertThat(codeSet).doesNotContain(hash);
            codeSet.add(hash);
        }
    }

    @Test
    public void shouldStoreState()
    {
        RealVehicleState state = mock(RealVehicleState.class);

        sut.setState(state);

        assertThat(sut.getState()).isSameAs(state);
    }
}
