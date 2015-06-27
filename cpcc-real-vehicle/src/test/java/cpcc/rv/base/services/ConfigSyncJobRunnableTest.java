package cpcc.rv.base.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationService;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicleType;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.services.jobs.TimeService;

/**
 * Configuration synchronization job runnable test.
 */
public class ConfigSyncJobRunnableTest
{
    private Map<String, String> parameters;
    private Session session;
    private QueryManager qm;
    private CommunicationService com;
    private TimeService timeService;

    private ConfigSyncJobRunnable sut;
    private Logger logger;

    private SensorDefinition sd01;
    private SensorDefinition sd02;
    private SensorDefinition sd03;

    private RealVehicle rv01old;
    private RealVehicle rv01new;

    @SuppressWarnings("unchecked")
    @BeforeMethod
    public void setUp()
    {
        sd01 = new SensorDefinition();
        sd02 = new SensorDefinition();
        sd03 = new SensorDefinition();

        rv01old = mock(RealVehicle.class);
        when(rv01old.getAreaOfOperation()).thenReturn("{\"a\":17,\"b\":\"s\"}");
        when(rv01old.getDeleted()).thenReturn(false);
        when(rv01old.getId()).thenReturn(1001);
        when(rv01old.getLastUpdate()).thenReturn(new Date(1001001001L));
        when(rv01old.getName()).thenReturn("RV01");
        when(rv01old.getSensors()).thenReturn(Arrays.asList(sd01, sd02));
        when(rv01old.getType()).thenReturn(RealVehicleType.QUADROCOPTER);
        when(rv01old.getUrl()).thenReturn("http://rv01.site/app");

        rv01new = mock(RealVehicle.class);
        when(rv01new.getAreaOfOperation()).thenReturn("{\"a\":18,\"b\":\"t\"}");
        when(rv01new.getDeleted()).thenReturn(true);
        when(rv01new.getId()).thenReturn(1002);
        when(rv01new.getLastUpdate()).thenReturn(new Date(1001001002L));
        when(rv01new.getName()).thenReturn("rv01");
        when(rv01new.getSensors()).thenReturn(Arrays.asList(sd01, sd02, sd03));
        when(rv01new.getType()).thenReturn(RealVehicleType.GROUND_STATION);
        when(rv01new.getUrl()).thenReturn("http://rv01.site:8001/app");

        parameters = mock(Map.class);
        when(parameters.get("rv")).thenReturn("10");

        session = mock(Session.class);
        qm = mock(QueryManager.class);
        com = mock(CommunicationService.class);
        timeService = mock(TimeService.class);
        logger = mock(Logger.class);

        sut = new ConfigSyncJobRunnable(parameters, session, qm, com, timeService, logger);
    }

    @Test
    public void shouldConvert() throws IOException
    {
        byte[] result = sut.prepareSyncData();
        System.out.write("result='".getBytes());
        System.out.write(result);
        System.out.write("'\n".getBytes());

        sut.updateOwnConfig(result);

        // TODO complete test.
    }
}
