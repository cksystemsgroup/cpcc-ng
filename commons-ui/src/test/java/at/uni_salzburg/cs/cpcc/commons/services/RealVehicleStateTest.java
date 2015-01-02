package at.uni_salzburg.cs.cpcc.commons.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.UnsupportedEncodingException;
import java.sql.Date;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;

public class RealVehicleStateTest
{
    @DataProvider
    public Object[][] stateDataProvider()
    {
        RealVehicle rv1 = mock(RealVehicle.class);

        return new Object[][]{
            new Object[]{rv1, false, false, new Date(12345678L), null, null},
            new Object[]{rv1, false, false, new Date(12345678L), new byte[]{65, 66, 67, 68}, "ABCD"},
        };
    }

    @Test(dataProvider = "stateDataProvider")
    public void should(RealVehicle realVehicle, boolean connected, boolean statusUpdateRunning
        , Date lastUpdate, byte[] status, String statusString) throws UnsupportedEncodingException
    {
        RealVehicleState sut = new RealVehicleState(realVehicle);

        sut.setConnected(connected);
        sut.setLastUpdate(lastUpdate);
        sut.setStatus(status);
        sut.setStatusUpdateRunning(statusUpdateRunning);

        assertThat(sut.getRealVehicle() == realVehicle).describedAs("realVehicle").isTrue();
        assertThat(sut.isConnected()).describedAs("connected").isEqualTo(connected);
        assertThat(sut.isStatusUpdateRunning()).describedAs("statusUpdateRunning").isEqualTo(statusUpdateRunning);
        assertThat(sut.getLastUpdate()).describedAs("lastUpdate").isEqualTo(lastUpdate);
        assertThat(sut.getStatus()).describedAs("status").isEqualTo(status);
        assertThat(sut.getStatusString()).describedAs("statusString").isEqualTo(statusString);
    }
}
