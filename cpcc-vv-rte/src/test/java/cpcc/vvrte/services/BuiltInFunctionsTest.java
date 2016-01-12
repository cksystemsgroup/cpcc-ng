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
import cpcc.ros.services.RosNodeService;
import cpcc.vvrte.services.db.VvRteRepository;
import cpcc.vvrte.services.ros.MessageConverter;
import cpcc.vvrte.services.task.TaskAnalyzer;
import cpcc.vvrte.utils.JavaScriptUtils;

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

        assertThat(actual).hasSize(3);

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

        String actualJson = JavaScriptUtils.toJsonString(actual);
        System.out.println(actualJson.replace("\"", "\\\""));

        JSONAssert.assertEquals(expectedJson, actualJson, false);
        JSONAssert.assertEquals(actualJson, expectedJson, false);
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
        //        sut.getSensorValue(sensor)

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
