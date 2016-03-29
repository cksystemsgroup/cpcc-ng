package cpcc.vvrte.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.json.JSONException;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;
import cpcc.core.entities.SensorVisibility;
import cpcc.core.services.QueryManager;
import cpcc.core.services.opts.Option;
import cpcc.core.services.opts.OptionsParserService;
import cpcc.core.services.opts.OptionsParserServiceImpl;
import cpcc.core.services.opts.ParseException;
import cpcc.core.services.opts.Token;
import cpcc.ros.base.AbstractRosAdapter;
import cpcc.ros.services.RosNodeService;
import cpcc.vvrte.services.db.VvRteRepository;
import cpcc.vvrte.services.ros.MessageConverter;
import cpcc.vvrte.services.task.TaskAnalyzer;
import cpcc.vvrte.utils.JavaScriptUtils;

public class BuiltInFunctionsTest
{
    private static final String SENSOR_3_PARAMETERS = "bugger=lala looney=3.141592 caspar='xxx uu'";

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
    private SensorDefinition sensor4;
    private AbstractRosAdapter adapter1;
    private AbstractRosAdapter adapter2;
    private AbstractRosAdapter adapter3;
    private std_msgs.Float32 message1;
    private std_msgs.Float32 message2;
    private std_msgs.Float32 message3;

    @BeforeMethod
    public void setUp()
    {
        sensor1 = mock(SensorDefinition.class);
        when(sensor1.toString()).thenReturn("sensor1");
        when(sensor1.getId()).thenReturn(101);
        when(sensor1.getDescription()).thenReturn("d1");
        when(sensor1.getMessageType()).thenReturn("mt1");
        when(sensor1.getType()).thenReturn(SensorType.ALTIMETER);
        when(sensor1.getVisibility()).thenReturn(SensorVisibility.ALL_VV);

        sensor2 = mock(SensorDefinition.class);
        when(sensor2.toString()).thenReturn("sensor2");
        when(sensor2.getId()).thenReturn(202);
        when(sensor2.getDescription()).thenReturn("d2");
        when(sensor2.getMessageType()).thenReturn("mt2");
        when(sensor2.getType()).thenReturn(SensorType.AREA_OF_OPERATIONS);
        when(sensor2.getVisibility()).thenReturn(SensorVisibility.ALL_VV);

        sensor3 = mock(SensorDefinition.class);
        when(sensor3.toString()).thenReturn("sensor3");
        when(sensor3.getId()).thenReturn(303);
        when(sensor3.getDescription()).thenReturn("d3");
        when(sensor3.getMessageType()).thenReturn("mt3");
        when(sensor3.getType()).thenReturn(SensorType.CAMERA);
        when(sensor3.getVisibility()).thenReturn(SensorVisibility.PRIVILEGED_VV);
        when(sensor3.getParameters()).thenReturn(SENSOR_3_PARAMETERS);

        sensor4 = mock(SensorDefinition.class);
        when(sensor4.toString()).thenReturn("sensor4");
        when(sensor4.getId()).thenReturn(404);
        when(sensor4.getDescription()).thenReturn("d4");
        when(sensor4.getMessageType()).thenReturn("mt4");
        when(sensor4.getType()).thenReturn(SensorType.AREA_OF_OPERATIONS);
        when(sensor4.getVisibility()).thenReturn(SensorVisibility.NO_VV);

        visibleSensors = Arrays.asList(sensor1, sensor2);
        activeSensors = Arrays.asList(sensor1, sensor2, sensor3, sensor4);

        message1 = mock(std_msgs.Float32.class);
        when(message1.toString()).thenReturn("message1");
        when(message1.getData()).thenReturn(1.234f);

        message2 = mock(std_msgs.Float32.class);
        when(message2.toString()).thenReturn("message2");
        when(message2.getData()).thenReturn(2.345f);

        message3 = mock(std_msgs.Float32.class);
        when(message3.toString()).thenReturn("message3");
        when(message3.getData()).thenReturn(3.456f);

        adapter1 = mock(AbstractRosAdapter.class);
        when(adapter1.toString()).thenReturn("adapter1");
        when(adapter1.getValue()).thenReturn(message1);

        adapter2 = mock(AbstractRosAdapter.class);
        when(adapter2.toString()).thenReturn("adapter2");
        when(adapter2.getValue()).thenReturn(message2);

        adapter3 = mock(AbstractRosAdapter.class);
        when(adapter3.toString()).thenReturn("adapter3");
        when(adapter3.getValue()).thenReturn(message3);

        rns = mock(RosNodeService.class);
        when(rns.findAdapterNodeBySensorDefinitionId(sensor1.getId())).thenReturn(adapter1);
        when(rns.findAdapterNodeBySensorDefinitionId(sensor2.getId())).thenReturn(adapter2);
        when(rns.findAdapterNodeBySensorDefinitionId(sensor3.getId())).thenReturn(adapter3);

        opts = mock(OptionsParserService.class);

        conv = mock(MessageConverter.class);

        mapper = mock(VirtualVehicleMapper.class);

        // taskExecutor = mock(TaskExecutionService.class);

        taskAnalyzer = mock(TaskAnalyzer.class);

        vvRteRepo = mock(VvRteRepository.class);

        qm = mock(QueryManager.class);

        sessionManager = mock(HibernateSessionManager.class);
        logger = mock(Logger.class);

        sut = new BuiltInFunctionsImpl(rns, opts, conv, mapper, taskAnalyzer, vvRteRepo, qm
            , sessionManager, logger);
    }

    @Test
    public void shouldListSensors()
    {
        when(qm.findAllVisibleSensorDefinitions()).thenReturn(visibleSensors);
        when(qm.findAllActiveSensorDefinitions()).thenReturn(activeSensors);

        List<ScriptableObject> actual = sut.listSensors();

        assertThat(actual).hasSize(2);

        ScriptableObject first = actual.get(0);
        assertThat(ScriptableObject.getProperty(first, "id")).isEqualTo(sensor1.getId());
        assertThat(ScriptableObject.getProperty(first, "description")).isEqualTo(sensor1.getDescription());
        assertThat(ScriptableObject.getProperty(first, "messageType")).isEqualTo(sensor1.getMessageType());
        assertThat(ScriptableObject.getProperty(first, "type")).isEqualTo(sensor1.getType().name());
        assertThat(ScriptableObject.getProperty(first, "visibility")).isEqualTo(sensor1.getVisibility().name());

        ScriptableObject second = actual.get(1);
        assertThat(ScriptableObject.getProperty(second, "id")).isEqualTo(sensor2.getId());
        assertThat(ScriptableObject.getProperty(second, "description")).isEqualTo(sensor2.getDescription());
        assertThat(ScriptableObject.getProperty(second, "messageType")).isEqualTo(sensor2.getMessageType());
        assertThat(ScriptableObject.getProperty(second, "type")).isEqualTo(sensor2.getType().name());
        assertThat(ScriptableObject.getProperty(second, "visibility")).isEqualTo(sensor2.getVisibility().name());
    }

    @Test
    public void shouldReturnEmptyListOnNotAvailableSensors()
    {
        when(qm.findAllVisibleSensorDefinitions()).thenReturn(null);

        List<ScriptableObject> actual = sut.listSensors();

        assertThat(actual).hasSize(0);
    }

    @Test
    public void shouldListActiveSensors()
    {
        when(qm.findAllVisibleSensorDefinitions()).thenReturn(visibleSensors);
        when(qm.findAllActiveSensorDefinitions()).thenReturn(activeSensors);

        List<ScriptableObject> actual = sut.listActiveSensors();

        assertThat(actual).hasSize(4);

        ScriptableObject first = actual.get(0);
        assertThat(ScriptableObject.getProperty(first, "id")).isEqualTo(sensor1.getId());
        assertThat(ScriptableObject.getProperty(first, "description")).isEqualTo(sensor1.getDescription());
        assertThat(ScriptableObject.getProperty(first, "messageType")).isEqualTo(sensor1.getMessageType());
        assertThat(ScriptableObject.getProperty(first, "type")).isEqualTo(sensor1.getType().name());
        assertThat(ScriptableObject.getProperty(first, "visibility")).isEqualTo(sensor1.getVisibility().name());

        ScriptableObject second = actual.get(1);
        assertThat(ScriptableObject.getProperty(second, "id")).isEqualTo(sensor2.getId());
        assertThat(ScriptableObject.getProperty(second, "description")).isEqualTo(sensor2.getDescription());
        assertThat(ScriptableObject.getProperty(second, "messageType")).isEqualTo(sensor2.getMessageType());
        assertThat(ScriptableObject.getProperty(second, "type")).isEqualTo(sensor2.getType().name());
        assertThat(ScriptableObject.getProperty(second, "visibility")).isEqualTo(sensor2.getVisibility().name());

        ScriptableObject third = actual.get(2);
        assertThat(ScriptableObject.getProperty(third, "id")).isEqualTo(sensor3.getId());
        assertThat(ScriptableObject.getProperty(third, "description")).isEqualTo(sensor3.getDescription());
        assertThat(ScriptableObject.getProperty(third, "messageType")).isEqualTo(sensor3.getMessageType());
        assertThat(ScriptableObject.getProperty(third, "type")).isEqualTo(sensor3.getType().name());
        assertThat(ScriptableObject.getProperty(third, "visibility")).isEqualTo(sensor3.getVisibility().name());

        ScriptableObject fourth = actual.get(3);
        assertThat(ScriptableObject.getProperty(fourth, "id")).isEqualTo(sensor4.getId());
        assertThat(ScriptableObject.getProperty(fourth, "description")).isEqualTo(sensor4.getDescription());
        assertThat(ScriptableObject.getProperty(fourth, "messageType")).isEqualTo(sensor4.getMessageType());
        assertThat(ScriptableObject.getProperty(fourth, "type")).isEqualTo(sensor4.getType().name());
        assertThat(ScriptableObject.getProperty(fourth, "visibility")).isEqualTo(sensor4.getVisibility().name());
    }

    @Test
    public void shouldReturnEmptyListOnNotAvailableActiveSensors()
    {
        when(qm.findAllActiveSensorDefinitions()).thenReturn(null);

        List<ScriptableObject> actual = sut.listActiveSensors();

        assertThat(actual).hasSize(0);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldLogParseExceptionOnParsingErrors() throws IOException, ParseException
    {
        when(opts.parse(anyString())).thenThrow(IOException.class);
        when(qm.findAllVisibleSensorDefinitions()).thenReturn(visibleSensors);
        when(qm.findAllActiveSensorDefinitions()).thenReturn(activeSensors);

        sut.listActiveSensors();

        verify(logger).error(anyString(), any(IOException.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldLogIOExceptionOnParsingErrors() throws IOException, ParseException
    {
        when(opts.parse(anyString())).thenThrow(ParseException.class);
        when(qm.findAllVisibleSensorDefinitions()).thenReturn(visibleSensors);
        when(qm.findAllActiveSensorDefinitions()).thenReturn(activeSensors);

        sut.listActiveSensors();

        verify(logger).error(anyString(), any(ParseException.class));
    }

    @DataProvider
    public Object[][] sensorsDataProvider() throws IOException, ParseException
    {
        OptionsParserServiceImpl parser = new OptionsParserServiceImpl();

        SensorDefinition s1 = mock(SensorDefinition.class);
        when(s1.toString()).thenReturn("sensor1");
        when(s1.getId()).thenReturn(101);
        when(s1.getDescription()).thenReturn("d1");
        when(s1.getMessageType()).thenReturn("mt1");
        when(s1.getType()).thenReturn(SensorType.ALTIMETER);
        when(s1.getVisibility()).thenReturn(SensorVisibility.ALL_VV);

        SensorDefinition s2 = mock(SensorDefinition.class);
        when(s2.toString()).thenReturn("sensor2");
        when(s2.getId()).thenReturn(202);
        when(s2.getDescription()).thenReturn("d2");
        when(s2.getMessageType()).thenReturn("mt2");
        when(s2.getType()).thenReturn(SensorType.AREA_OF_OPERATIONS);
        when(s2.getVisibility()).thenReturn(SensorVisibility.ALL_VV);

        SensorDefinition s3 = mock(SensorDefinition.class);
        when(s3.toString()).thenReturn("sensor3");
        when(s3.getId()).thenReturn(303);
        when(s3.getDescription()).thenReturn("d3");
        when(s3.getMessageType()).thenReturn("mt3");
        when(s3.getType()).thenReturn(SensorType.CAMERA);
        when(s3.getVisibility()).thenReturn(SensorVisibility.PRIVILEGED_VV);
        when(s3.getParameters()).thenReturn("bugger=lala looney=3.141592 caspar='xxx uu'");

        Collection<Option> opts1 = parser.parse(s1.getParameters());
        Collection<Option> opts2 = parser.parse(s2.getParameters());
        Collection<Option> opts3 = parser.parse(s3.getParameters());

        return new Object[][]{
            new Object[]{"d1", s1, opts1
                , "{\"id\":101,\"description\":\"d1\",\"messageType\":\"mt1\""
                    + ",\"type\":\"ALTIMETER\",\"visibility\":\"ALL_VV\"}"},
            new Object[]{"d2", s2, opts2
                , "{\"id\":202,\"description\":\"d2\",\"messageType\":\"mt2\""
                    + ",\"type\":\"AREA_OF_OPERATIONS\",\"visibility\":\"ALL_VV\"}"},
            new Object[]{"d3", s3, opts3
                , "{\"id\":303,\"description\":\"d3\",\"messageType\":\"mt3\""
                    + ",\"type\":\"CAMERA\",\"visibility\":\"PRIVILEGED_VV\""
                    + ",\"params\":{\"bugger\":\"lala\",\"looney\":3.141592,\"caspar\":\"xxx uu\"}}"},
        };
    }

    @Test(dataProvider = "sensorsDataProvider")
    public void shouldGetSensors(String description, SensorDefinition sensor, List<Option> options, String expectedJson)
        throws JSONException, IOException, ParseException
    {
        when(qm.findSensorDefinitionByDescription(description)).thenReturn(sensor);
        when(opts.parse(sensor.getParameters())).thenReturn(options);

        ScriptableObject actual = sut.getSensor(description);

        if (actual == null && expectedJson == null)
        {
            return;
        }

        String actualJson = JavaScriptUtils.toJsonString(actual);
        System.out.println(actualJson.replace("\"", "\\\""));

        JSONAssert.assertEquals(expectedJson, actualJson, false);
        JSONAssert.assertEquals(actualJson, expectedJson, false);
    }

    @DataProvider
    public Object[][] invisibleSensorsDataProvider() throws IOException, ParseException
    {
        SensorDefinition s1 = mock(SensorDefinition.class);
        when(s1.toString()).thenReturn("sensor1");
        when(s1.getId()).thenReturn(101);
        when(s1.getDescription()).thenReturn("d1");
        when(s1.getMessageType()).thenReturn("mt1");
        when(s1.getType()).thenReturn(SensorType.ALTIMETER);
        when(s1.getVisibility()).thenReturn(SensorVisibility.NO_VV);

        SensorDefinition s2 = mock(SensorDefinition.class);
        when(s2.toString()).thenReturn("sensor2");
        when(s2.getId()).thenReturn(202);
        when(s2.getDescription()).thenReturn("d2");
        when(s2.getMessageType()).thenReturn("mt2");
        when(s2.getType()).thenReturn(SensorType.AREA_OF_OPERATIONS);
        when(s2.getVisibility()).thenReturn(SensorVisibility.NO_VV);

        SensorDefinition s3 = mock(SensorDefinition.class);
        when(s3.toString()).thenReturn("sensor3");
        when(s3.getId()).thenReturn(303);
        when(s3.getDescription()).thenReturn("d3");
        when(s3.getMessageType()).thenReturn("mt3");
        when(s3.getType()).thenReturn(SensorType.CAMERA);
        when(s3.getVisibility()).thenReturn(SensorVisibility.NO_VV);
        when(s3.getParameters()).thenReturn("bugger=lala looney=3.141592 caspar='xxx uu'");

        SensorDefinition s4 = mock(SensorDefinition.class);
        when(s4.toString()).thenReturn("sensor4");
        when(s4.getId()).thenReturn(404);
        when(s4.getDescription()).thenReturn("d4");
        when(s4.getMessageType()).thenReturn("mt4");
        when(s4.getType()).thenReturn(SensorType.THERMOMETER);
        when(s4.getVisibility()).thenReturn(SensorVisibility.NO_VV);

        return new Object[][]{
            new Object[]{"d1", s1},
            new Object[]{"d2", s2},
            new Object[]{"d3", s3},
            new Object[]{"d4", s4},
            new Object[]{"d5", null},
        };
    }

    @Test(dataProvider = "invisibleSensorsDataProvider")
    public void shouldNotGetInvisibleSensors(String description, SensorDefinition sensor)
        throws JSONException, IOException, ParseException
    {
        when(qm.findSensorDefinitionByDescription(description)).thenReturn(sensor);

        ScriptableObject actual = sut.getSensor(description);

        assertThat(actual).isNull();
    }

    @Test
    public void shouldReturnNullOnEmptyTokenList()
    {
        Object actual = BuiltInFunctionsImpl.convertTokenList(Collections.<Token> emptyList());

        assertThat(actual).isNull();
    }

    @DataProvider
    public Object[][] tokenDataProvider()
    {
        Token t1 = mock(Token.class);
        when(t1.toString()).thenReturn("tokenOne");
        when(t1.getValue()).thenReturn("stringOne");

        Token t2 = mock(Token.class);
        when(t2.toString()).thenReturn("tokenTwo");
        when(t2.getValue()).thenReturn(new BigDecimal(20202L));

        return new Object[][]{
            new Object[]{t1},
            new Object[]{t2},
        };
    }

    @Test(dataProvider = "tokenDataProvider")
    public void shouldReturnTokenOnSingleListItem(Token token)
    {
        Object actual = BuiltInFunctionsImpl.convertTokenList(Arrays.asList(token));

        assertThat(actual).isSameAs(token.getValue());
    }

    @DataProvider
    public Object[][] tokenListDataProvider()
    {
        Token t1 = mock(Token.class);
        when(t1.toString()).thenReturn("tokenOne");
        when(t1.getValue()).thenReturn("stringOne");

        Token t2 = mock(Token.class);
        when(t2.toString()).thenReturn("tokenTwo");
        when(t2.getValue()).thenReturn(new BigDecimal(20202L));

        return new Object[][]{
            new Object[]{Arrays.asList(t2, t1), "[20202,\"stringOne\"]"},
            new Object[]{Arrays.asList(t1, t2), "[\"stringOne\",20202]"},
        };
    }

    @Test(dataProvider = "tokenListDataProvider")
    public void shouldReturnTokenArrayOnMultipleListItems(List<Token> tokenList, String expectedJson)
        throws JSONException
    {
        NativeArray actual = (NativeArray) BuiltInFunctionsImpl.convertTokenList(tokenList);

        String actualJson = JavaScriptUtils.toJsonString(actual);
        // System.out.println(actualJson.replace("\"", "\\\""));

        JSONAssert.assertEquals(expectedJson, actualJson, false);
        JSONAssert.assertEquals(actualJson, expectedJson, false);
    }

    @Test
    public void shouldGetSensorValue()
    {
        when(qm.findSensorDefinitionByDescription(sensor1.getDescription())).thenReturn(sensor1);
        when(qm.findSensorDefinitionByDescription(sensor2.getDescription())).thenReturn(sensor2);
        when(qm.findSensorDefinitionByDescription(sensor3.getDescription())).thenReturn(sensor3);
        when(qm.findAllVisibleSensorDefinitions()).thenReturn(visibleSensors);

        List<ScriptableObject> actualSensors = sut.listSensors();

        assertThat(actualSensors).hasSize(2);

        sut.getSensorValue(actualSensors.get(0));
        sut.getSensorValue(actualSensors.get(1));

        verify(conv).convertMessageToJS(message1);
        verify(conv).convertMessageToJS(message2);
    }

    @Test
    public void shouldNotGetSensorValuesOfInvisibleSensors()
    {
        when(qm.findSensorDefinitionByDescription(sensor1.getDescription())).thenReturn(sensor1);
        when(qm.findSensorDefinitionByDescription(sensor2.getDescription())).thenReturn(sensor2);
        when(qm.findSensorDefinitionByDescription(sensor3.getDescription())).thenReturn(sensor3);
        when(qm.findSensorDefinitionByDescription(sensor4.getDescription())).thenReturn(sensor4);
        when(qm.findAllActiveSensorDefinitions()).thenReturn(activeSensors);

        List<ScriptableObject> actualSensors = sut.listActiveSensors();

        assertThat(actualSensors).hasSize(4);

        sut.getSensorValue(actualSensors.get(0));
        sut.getSensorValue(actualSensors.get(1));
        sut.getSensorValue(actualSensors.get(2));
        sut.getSensorValue(actualSensors.get(3));

        verify(conv).convertMessageToJS(message1);
        verify(conv).convertMessageToJS(message2);

        ScriptableObject actualValue = sut.getSensorValue(actualSensors.get(2));
        assertThat(actualValue).isNull();

        actualValue = sut.getSensorValue(actualSensors.get(3));
        assertThat(actualValue).isNull();

        NativeObject sensor = new NativeObject();
        actualValue = sut.getSensorValue(sensor);

        assertThat(actualValue).isNull();
    }

    @Test
    public void shouldExecuteTasks()
    {
        //        sut.executeTask(managementParameters, taskParameters);

    }

    @Test
    public void shouldListObjects()
    {
        //        sut.listObjects(pattern)

    }

    @Test
    public void shouldLoadObjects()
    {
        //        sut.loadObject(name)

    }

    @Test
    public void shouldStoreObjects()
    {
        //        sut.storeObject(name, obj);

    }

    @Test
    public void shouldRemoveObjects()
    {
        //        sut.removeObject(name);

    }

}
