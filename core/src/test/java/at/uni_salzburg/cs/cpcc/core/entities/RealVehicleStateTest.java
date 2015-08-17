package at.uni_salzburg.cs.cpcc.core.entities;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Date;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RealVehicleStateTest
{
    private RealVehicleState sut;

    @BeforeMethod
    public void setUp()
    {
        sut = new RealVehicleState();
    }

    @DataProvider
    public Object[][] rvDataProvider()
    {
        return new Object[][]{
            new Object[]{1, new Date(1439669289111L), "RV01", "state01"},
            new Object[]{2, new Date(1439669289222L), "RV02", "state02"},
            new Object[]{3, new Date(1439669289333L), "RV03", "state03"},
            new Object[]{4, new Date(1439669289444L), "RV04", "state04"},
        };
    }

    @Test(dataProvider = "rvDataProvider")
    void shouldHandleSettersAndGetters(Integer id, Date lastUpdate, String realVehicleName, String state)
    {
        sut.setId(id);
        sut.setLastUpdate(lastUpdate);
        sut.setRealVehicleName(realVehicleName);
        sut.setState(state);

        assertThat(sut.getId()).isEqualTo(id);
        assertThat(sut.getLastUpdate()).isEqualTo(lastUpdate);
        assertThat(sut.getRealVehicleName()).isEqualTo(realVehicleName);
        assertThat(sut.getState()).isEqualTo(state);
    }
}
