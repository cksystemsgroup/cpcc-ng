package cpcc.vvrte.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.mozilla.javascript.ScriptableObject;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;
import cpcc.core.entities.SensorVisibility;
import cpcc.core.services.QueryManager;
import cpcc.core.services.opts.OptionsParserService;
import cpcc.ros.services.RosNodeService;
import cpcc.vvrte.services.db.VvRteRepository;
import cpcc.vvrte.services.ros.MessageConverter;
import cpcc.vvrte.services.task.TaskAnalyzer;

public class BuiltInFunctionsTest
{
    private BuiltInFunctionsImpl sut;
    private RosNodeService rns;
    private OptionsParserService opts;
    private MessageConverter conv;
    private VirtualVehicleMapper mapper;
    // private TaskExecutionService taskExecutor;
    private TaskAnalyzer taskAnalyzer;
    private VvRteRepository vvRteRepo;
    private QueryManager qm;
    private HibernateSessionManager sessionManager;
    private Logger logger;
    private List<SensorDefinition> activeSensors;
    private List<SensorDefinition> visibleSensors;
    private SensorDefinition sensor1;
    private SensorDefinition sensor2;
    private SensorDefinition sensor3;
    private String sensor3Parameters = "bugger=lala looney=3.141592 caspar='xxx uu'";

    @BeforeMethod
    public void setUp()
    {
        sensor1 = mock(SensorDefinition.class);
        when(sensor1.getId()).thenReturn(101);
        when(sensor1.getDescription()).thenReturn("d1");
        when(sensor1.getMessageType()).thenReturn("mt1");
        when(sensor1.getType()).thenReturn(SensorType.ALTIMETER);
        when(sensor1.getVisibility()).thenReturn(SensorVisibility.ALL_VV);

        sensor2 = mock(SensorDefinition.class);
        when(sensor2.getId()).thenReturn(202);
        when(sensor2.getDescription()).thenReturn("d2");
        when(sensor2.getMessageType()).thenReturn("mt2");
        when(sensor2.getType()).thenReturn(SensorType.AREA_OF_OPERATIONS);
        when(sensor2.getVisibility()).thenReturn(SensorVisibility.ALL_VV);

        sensor3 = mock(SensorDefinition.class);
        when(sensor3.getId()).thenReturn(303);
        when(sensor3.getDescription()).thenReturn("d3");
        when(sensor3.getMessageType()).thenReturn("mt3");
        when(sensor3.getType()).thenReturn(SensorType.CAMERA);
        when(sensor3.getVisibility()).thenReturn(SensorVisibility.PRIVILEGED_VV);
        when(sensor3.getParameters()).thenReturn(sensor3Parameters);

        visibleSensors = Arrays.asList(sensor1, sensor2);
        activeSensors = Arrays.asList(sensor1, sensor2, sensor3);

        rns = mock(RosNodeService.class);
        opts = mock(OptionsParserService.class);
        conv = mock(MessageConverter.class);
        mapper = mock(VirtualVehicleMapper.class);
        // taskExecutor = mock(TaskExecutionService.class);
        taskAnalyzer = mock(TaskAnalyzer.class);
        vvRteRepo = mock(VvRteRepository.class);
        qm = mock(QueryManager.class);
        when(qm.findAllVisibleSensorDefinitions()).thenReturn(visibleSensors);
        when(qm.findAllActiveSensorDefinitions()).thenReturn(activeSensors);

        sessionManager = mock(HibernateSessionManager.class);
        logger = mock(Logger.class);

        sut = new BuiltInFunctionsImpl(rns, opts, conv, mapper, taskAnalyzer, vvRteRepo, qm
            , sessionManager, logger);
    }

    @Test
    public void shouldListSensors()
    {
        List<ScriptableObject> actual = sut.listSensors();

        assertThat(actual).hasSize(2);

        ScriptableObject first = actual.get(0);
        assertThat(ScriptableObject.getProperty(first, "id")).isEqualTo(sensor1.getId());
        assertThat(ScriptableObject.getProperty(first, "description")).isEqualTo(sensor1.getDescription());
        assertThat(ScriptableObject.getProperty(first, "messageType")).isEqualTo(sensor1.getMessageType());
        assertThat(ScriptableObject.getProperty(first, "type")).isEqualTo(sensor1.getType());
        assertThat(ScriptableObject.getProperty(first, "visibility")).isEqualTo(sensor1.getVisibility());

        ScriptableObject second = actual.get(1);
        assertThat(ScriptableObject.getProperty(second, "id")).isEqualTo(sensor2.getId());
        assertThat(ScriptableObject.getProperty(second, "description")).isEqualTo(sensor2.getDescription());
        assertThat(ScriptableObject.getProperty(second, "messageType")).isEqualTo(sensor2.getMessageType());
        assertThat(ScriptableObject.getProperty(second, "type")).isEqualTo(sensor2.getType());
        assertThat(ScriptableObject.getProperty(second, "visibility")).isEqualTo(sensor2.getVisibility());
    }

    @Test
    public void shouldListActiveSensors()
    {
        List<ScriptableObject> actual = sut.listActiveSensors();

        assertThat(actual).hasSize(3);

        ScriptableObject first = actual.get(0);
        assertThat(ScriptableObject.getProperty(first, "id")).isEqualTo(sensor1.getId());
        assertThat(ScriptableObject.getProperty(first, "description")).isEqualTo(sensor1.getDescription());
        assertThat(ScriptableObject.getProperty(first, "messageType")).isEqualTo(sensor1.getMessageType());
        assertThat(ScriptableObject.getProperty(first, "type")).isEqualTo(sensor1.getType());
        assertThat(ScriptableObject.getProperty(first, "visibility")).isEqualTo(sensor1.getVisibility());

        ScriptableObject second = actual.get(1);
        assertThat(ScriptableObject.getProperty(second, "id")).isEqualTo(sensor2.getId());
        assertThat(ScriptableObject.getProperty(second, "description")).isEqualTo(sensor2.getDescription());
        assertThat(ScriptableObject.getProperty(second, "messageType")).isEqualTo(sensor2.getMessageType());
        assertThat(ScriptableObject.getProperty(second, "type")).isEqualTo(sensor2.getType());
        assertThat(ScriptableObject.getProperty(second, "visibility")).isEqualTo(sensor2.getVisibility());

        ScriptableObject third = actual.get(2);
        assertThat(ScriptableObject.getProperty(third, "id")).isEqualTo(sensor3.getId());
        assertThat(ScriptableObject.getProperty(third, "description")).isEqualTo(sensor3.getDescription());
        assertThat(ScriptableObject.getProperty(third, "messageType")).isEqualTo(sensor3.getMessageType());
        assertThat(ScriptableObject.getProperty(third, "type")).isEqualTo(sensor3.getType());
        assertThat(ScriptableObject.getProperty(third, "visibility")).isEqualTo(sensor3.getVisibility());
    }
}
