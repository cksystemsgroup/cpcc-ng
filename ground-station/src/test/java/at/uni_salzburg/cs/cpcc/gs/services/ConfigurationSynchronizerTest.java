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

package at.uni_salzburg.cs.cpcc.gs.services;


public class ConfigurationSynchronizerTest
{
//    private Logger logger;
//    private ConfigurationSynchronizerImpl sync;
//    private QueryManager qm;
//    private CommunicationService com;
//    private Session session;
//    private HibernateSessionManager sessionManager;
//
//    //    private RealVehicle syncRealVehicle;
//    //    private Connector syncConnector;
//    //    private byte[] syncData;
//    private CommunicationResponse response = new CommunicationResponse();
//
//    private String result1 =
//        "{\"rvs\":[{\"id\":\"10\",\"aoo\":\"[{lat: 43.85,lng: 13.45}]\",\"name\":\"GS01\",\"sen\":[1],\"upd\":10010,"
//            + "\"type\":\"GROUND_STATION\",\"url\":\"http://localhost:12345/gs01\"},"
//            + "{\"id\":\"1\",\"aoo\":\"[{lat: 43.8,lng: 13.4},{lat: 43.8,lng: 13.5},{lat: 43.9,lng: 13.5},"
//            + "{lat: 43.9,lng: 13.5},{lat: 43.8,lng: 13.4}]\",\"name\":\"RV01\",\"sen\":[2],\"upd\":1,"
//            + "\"type\":\"QUADROCOPTER\",\"deleted\":false,\"url\":\"http://localhost:12345/rv01\"},"
//            + "{\"id\":\"2\",\"aoo\":\"[{lat: 43.9,lng: 13.4},{lat: 43.9,lng: 13.5},{lat: 44.0,lng: 13.5},"
//            + "{lat: 44.0,lng: 13.5},{lat: 43.9,lng: 13.4}]\",\"name\":\"RV02\",\"sen\":[1,3],\"upd\":2,"
//            + "\"type\":\"FIXED_WING_AIRCRAFT\",\"deleted\":false,\"url\":\"http://localhost:12345/rv02\"},"
//            + "{\"id\":\"3\",\"aoo\":\"[{lat: 43.8,lng: 13.5},{lat: 43.8,lng: 13.6},{lat: 43.9,lng: 13.6},"
//            + "{lat: 43.9,lng: 13.6},{lat: 43.8,lng: 13.5}]\",\"name\":\"RV03\",\"sen\":[1,2,3],\"upd\":3,"
//            + "\"type\":\"MOBILE_PHONE\",\"deleted\":false,\"url\":\"http://localhost:12345/rv03\"},"
//            + "{\"id\":\"4\",\"aoo\":\"[{lat: 43.9,lng: 13.5},{lat: 43.9,lng: 13.6},{lat: 44.0,lng: 13.6},"
//            + "{lat: 44.0,lng: 13.6},{lat: 43.9,lng: 13.5}]\",\"name\":\"RV04\",\"sen\":[1,2,3,4],\"upd\":4,"
//            + "\"type\":\"TABLET\",\"deleted\":false,\"url\":\"http://localhost:12345/rv04\"}],\"sen\":["
//            + "{\"id\":\"1\",\"visibility\":\"ALL_VV\",\"lastUpdate\":1000,\"description\":\"Altimeter\","
//            + "\"messageType\":\"std_msgs/Float32\",\"type\":\"ALTIMETER\",\"deleted\":false},{\"id\":\"2\","
//            + "\"visibility\":\"PRIVILEGED_VV\",\"lastUpdate\":2000,\"description\":\"Belly Mounted Camera 640x480\","
//            + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment='north'\","
//            + "\"messageType\":\"sensor_msgs/Image\",\"type\":\"CAMERA\",\"deleted\":false},"
//            + "{\"id\":\"3\",\"visibility\":\"NO_VV\",\"lastUpdate\":3000,\"description\":\"CO2\","
//            + "\"messageType\":\"std_msgs/Float32\",\"type\":\"CO2\",\"deleted\":false},{\"id\":\"4\","
//            + "\"visibility\":\"ALL_VV\",\"lastUpdate\":4000,\"description\":\"GPS\","
//            + "\"messageType\":\"sensor_msgs/NavSatFix\",\"type\":\"GPS\",\"deleted\":false}]}";
//
//    //    private String response1 = "";
//
//    private SensorDefinition sd1 = new SensorDefinition();
//    private SensorDefinition sd2 = new SensorDefinition();
//    private SensorDefinition sd3 = new SensorDefinition();
//    private SensorDefinition sd4 = new SensorDefinition();
//
//    private SensorDefinition sd1new = new SensorDefinition();
//
//    private RealVehicle gs1 = new RealVehicle();
//    private RealVehicle rv1 = new RealVehicle();
//    private RealVehicle rv2 = new RealVehicle();
//    private RealVehicle rv3 = new RealVehicle();
//    private RealVehicle rv4 = new RealVehicle();
//    private RealVehicle rv5 = new RealVehicle();
//
//    private RealVehicle rv1new = new RealVehicle();
//
//    private List<SensorDefinition> gs1SensorList = Arrays.asList(sd1);
//    private List<SensorDefinition> rv1SensorList = Arrays.asList(sd2);
//    private List<SensorDefinition> rv2SensorList = Arrays.asList(sd1, sd3);
//    private List<SensorDefinition> rv3SensorList = Arrays.asList(sd1, sd2, sd3);
//    private List<SensorDefinition> rv4SensorList = Arrays.asList(sd1, sd2, sd3, sd4);
//    private List<SensorDefinition> rv5SensorList = new ArrayList<SensorDefinition>();
//
//    private CoreJsonConverterImpl jsonConv;
//    private ArrayList<RealVehicle> addedRVs;
//    private ArrayList<RealVehicle> removedRVs;
//    private ArrayList<RealVehicle> changedRVs;
//    private ArrayList<SensorDefinition> changedSDs;
//    private ArrayList<SensorDefinition> removedSDs;
//    private ArrayList<SensorDefinition> addedSDs;
//
//    private int transferCount;
//    private byte[] lastTransferData;
//
//    @BeforeMethod
//    public void setUp() throws ClientProtocolException, IOException
//    {
//        logger = mock(Logger.class);
//
//        sd1.setId(1);
//        sd1.setDescription("Altimeter");
//        sd1.setLastUpdate(new Date(1000));
//        sd1.setMessageType("std_msgs/Float32");
//        sd1.setParameters(null);
//        sd1.setType(SensorType.ALTIMETER);
//        sd1.setVisibility(SensorVisibility.ALL_VV);
//        sd1.setDeleted(false);
//
//        sd2.setId(2);
//        sd2.setDescription("Belly Mounted Camera 640x480");
//        sd2.setLastUpdate(new Date(2000));
//        sd2.setMessageType("sensor_msgs/Image");
//        sd2.setParameters("width=640 height=480 yaw=0 down=1.571 alignment='north'");
//        sd2.setType(SensorType.CAMERA);
//        sd2.setVisibility(SensorVisibility.PRIVILEGED_VV);
//        sd2.setDeleted(false);
//
//        sd3.setId(3);
//        sd3.setDescription("CO2");
//        sd3.setLastUpdate(new Date(3000));
//        sd3.setMessageType("std_msgs/Float32");
//        sd3.setParameters(null);
//        sd3.setType(SensorType.CO2);
//        sd3.setVisibility(SensorVisibility.NO_VV);
//        sd3.setDeleted(false);
//
//        sd4.setId(4);
//        sd4.setDescription("GPS");
//        sd4.setLastUpdate(new Date(4000));
//        sd4.setMessageType("sensor_msgs/NavSatFix");
//        sd4.setParameters(null);
//        sd4.setType(SensorType.GPS);
//        sd4.setVisibility(SensorVisibility.ALL_VV);
//        sd4.setDeleted(false);
//
//        sd1new.setId(1);
//        sd1new.setDescription("Barometer");
//        sd1new.setLastUpdate(new Date(10001000));
//        sd1new.setMessageType("std_msgs/Float64");
//        sd1new.setParameters("random=1080:1100");
//        sd1new.setType(SensorType.BAROMETER);
//        sd1new.setVisibility(SensorVisibility.NO_VV);
//        sd1new.setDeleted(false);
//
//        gs1.setId(10);
//        gs1.setAreaOfOperation("[{lat: 43.85,lng: 13.45}]");
//        gs1.setLastUpdate(new Date(10010));
//        gs1.setName("GS01");
//        gs1.setSensors(gs1SensorList);
//        gs1.setType(RealVehicleType.GROUND_STATION);
//        gs1.setUrl("http://localhost:12345/gs01");
//
//        rv1.setId(1);
//        rv1.setAreaOfOperation("[{lat: 43.8,lng: 13.4},{lat: 43.8,lng: 13.5},{lat: 43.9,lng: 13.5},"
//            + "{lat: 43.9,lng: 13.5},{lat: 43.8,lng: 13.4}]");
//        rv1.setLastUpdate(new Date(1));
//        rv1.setName("RV01");
//        rv1.setSensors(rv1SensorList);
//        rv1.setType(RealVehicleType.QUADROCOPTER);
//        rv1.setUrl("http://localhost:12345/rv01");
//        rv1.setDeleted(false);
//
//        rv2.setId(2);
//        rv2.setAreaOfOperation("[{lat: 43.9,lng: 13.4},{lat: 43.9,lng: 13.5},{lat: 44.0,lng: 13.5},"
//            + "{lat: 44.0,lng: 13.5},{lat: 43.9,lng: 13.4}]");
//        rv2.setLastUpdate(new Date(2));
//        rv2.setName("RV02");
//        rv2.setSensors(rv2SensorList);
//        rv2.setType(RealVehicleType.FIXED_WING_AIRCRAFT);
//        rv2.setUrl("http://localhost:12345/rv02");
//        rv2.setDeleted(false);
//
//        rv3.setId(3);
//        rv3.setAreaOfOperation("[{lat: 43.8,lng: 13.5},{lat: 43.8,lng: 13.6},{lat: 43.9,lng: 13.6},"
//            + "{lat: 43.9,lng: 13.6},{lat: 43.8,lng: 13.5}]");
//        rv3.setLastUpdate(new Date(3));
//        rv3.setName("RV03");
//        rv3.setSensors(rv3SensorList);
//        rv3.setType(RealVehicleType.MOBILE_PHONE);
//        rv3.setUrl("http://localhost:12345/rv03");
//        rv3.setDeleted(false);
//
//        rv4.setId(4);
//        rv4.setAreaOfOperation("[{lat: 43.9,lng: 13.5},{lat: 43.9,lng: 13.6},{lat: 44.0,lng: 13.6},"
//            + "{lat: 44.0,lng: 13.6},{lat: 43.9,lng: 13.5}]");
//        rv4.setLastUpdate(new Date(4));
//        rv4.setName("RV04");
//        rv4.setSensors(rv4SensorList);
//        rv4.setType(RealVehicleType.TABLET);
//        rv4.setUrl("http://localhost:12345/rv04");
//        rv4.setDeleted(false);
//
//        rv5.setId(5);
//        rv5.setAreaOfOperation("[{lat: 44.0,lng: 13.5},{lat: 44.0,lng: 13.6},{lat: 44.1,lng: 13.6},"
//            + "{lat: 44.1,lng: 13.6},{lat: 44.0,lng: 13.5}]");
//        rv5.setLastUpdate(new Date(5));
//        rv5.setName("RV05");
//        rv5.setSensors(rv5SensorList);
//        rv5.setType(RealVehicleType.UNMANNED_UNDERWATER_VEHICLE);
//        rv5.setUrl("http://localhost:12345/rv05");
//        rv5.setDeleted(false);
//
//        rv1new.setId(1);
//        rv1new.setAreaOfOperation("[{lat: 43.8,lng: 13.4},{lat: 43.8,lng: 13.5},{lat: 43.9,lng: 13.5},"
//            + "{lat: 43.9,lng: 13.5}]");
//        rv1new.setLastUpdate(new Date(10001));
//        rv1new.setName("RV01new");
//        rv1new.setSensors(rv1SensorList);
//        rv1new.setType(RealVehicleType.FIXED_WING_AIRCRAFT);
//        rv1new.setUrl("http://localhost:12345/rv01new");
//        rv1new.setDeleted(false);
//
//        Parameter rvNameParam = mock(Parameter.class);
//        when(rvNameParam.getValue()).thenReturn("GS01");
//
//        qm = mock(QueryManager.class);
//        // when(qm.findAllRealVehicles()).thenReturn(Arrays.asList(gs1, rv1, rv2, rv3, rv4));
//        // when(qm.findAllSensorDefinitions()).thenReturn(Arrays.asList(sd1, sd2, sd3, sd4));
//        when(qm.findParameterByName(Parameter.REAL_VEHICLE_NAME)).thenReturn(rvNameParam);
//        when(qm.findRealVehicleByName(rvNameParam.getValue())).thenReturn(gs1);
//        when(qm.findAllRealVehicles()).thenReturn(new ArrayList<RealVehicle>(Arrays.asList(gs1, rv1, rv2, rv3, rv4)));
//
//        session = mock(Session.class);
//        // when(qm.getSession()).thenReturn(session);
//        sessionManager = mock(HibernateSessionManager.class);
//        when(sessionManager.getSession()).thenReturn(session);
//
//        response.setStatus(Status.OK);
//        response.setContent(null);
//
//        com = mock(CommunicationService.class);
//        doAnswer(new Answer<CommunicationResponse>()
//        {
//            @Override
//            public CommunicationResponse answer(InvocationOnMock invocation) throws Throwable
//            {
//                ++transferCount;
//                //Object[] args = invocation.getArguments();
//                //RealVehicle syncRealVehicle = (RealVehicle) args[0];
//                //Connector syncConnector = (Connector) args[1];
//                //byte[] syncData = (byte[]) args[2];
//
//                lastTransferData = invocation.getArgumentAt(2, byte[].class);
//                return response;
//            }
//        }).when(com).transfer(any(RealVehicle.class), anyString(), any(byte[].class));
//
//        jsonConv = new CoreJsonConverterImpl();
//
//        RealVehicleStateService stateSrv = mock(RealVehicleStateService.class);
//
//        // sync = new ConfigurationSynchronizerImpl(logger, sessionManager, qm, com, jsonConv, stateSrv);
//        sync = new ConfigurationSynchronizerImpl(logger, qm, com);
//    }
//
//    @DataProvider
//    public Object[][] sensorDefJSONDataProvider()
//    {
//        return new Object[][]{
//            new Object[]{
//                Arrays.asList(sd1new, sd2, sd3, sd4),
//                Arrays.asList(sd1, sd2, sd3, sd4),
//                Arrays.asList(sd1new)
//            },
//        //            new Object[]{
//        //                Arrays.asList(sd1, sd2, sd3, sd4),
//        //                Arrays.asList(sd1new, sd2, sd3, sd4),
//        //                new ArrayList<SensorDefinition>()
//        //            },
//        //            new Object[]{
//        //                new ArrayList<SensorDefinition>(),
//        //                Arrays.asList(sd1, sd2, sd3, sd4),
//        //                new ArrayList<SensorDefinition>()
//        //            },
//        //            new Object[]{
//        //                Arrays.asList(sd1, sd2, sd3, sd4),
//        //                Arrays.asList(sd1, sd2, sd4),
//        //                new ArrayList<SensorDefinition>()
//        //            },
//        //            new Object[]{
//        //                Arrays.asList(sd1, sd2, sd4),
//        //                Arrays.asList(sd1, sd2, sd3, sd4),
//        //                new ArrayList<SensorDefinition>()
//        //            },
//        };
//    }
//
//    @Test(dataProvider = "sensorDefJSONDataProvider")
//    public void shouldSynchronizeSensorDefinitionsFromJSON(
//        List<SensorDefinition> db,
//        List<SensorDefinition> in,
//        List<SensorDefinition> out)
//    {
//        setUpQmAllSensorDefinitions(db, in);
//
//        JSONArray sensorDefs = jsonConv.toJsonArray(in.toArray(new SensorDefinition[0]));
//        JSONArray back = sync.syncSensorDefinitionConfig(sensorDefs);
//
//        List<SensorDefinition> result = ConvUtils.toSensorDefinitionList(back);
//
//        assertThat(result).isNotNull().containsAll(out);
//        assertThat(out).containsAll(result);
//        // TODO database deletes
//
//        verifySensorDefinitionsAccessInDb(in);
//    }
//
//    @DataProvider
//    public Object[][] realVehicleJSONDataProvider()
//    {
//        return new Object[][]{
//            new Object[]{
//                Arrays.asList(rv1new, rv2, rv3, rv4),
//                Arrays.asList(rv1, rv2, rv3, rv4),
//                Arrays.asList(rv1new)
//            },
//            new Object[]{
//                Arrays.asList(rv1, rv2, rv3, rv4),
//                Arrays.asList(rv1new, rv2, rv3, rv4),
//                new ArrayList<RealVehicle>()
//            },
//            new Object[]{
//                new ArrayList<RealVehicle>(),
//                Arrays.asList(rv1, rv2, rv3, rv4),
//                new ArrayList<RealVehicle>()
//            },
//            new Object[]{
//                Arrays.asList(rv1, rv2, rv4),
//                Arrays.asList(rv1, rv2, rv3, rv4),
//                new ArrayList<RealVehicle>()
//            },
//            new Object[]{
//                Arrays.asList(rv1, rv2, rv3, rv4),
//                Arrays.asList(rv1, rv2, rv4),
//                new ArrayList<RealVehicle>()
//            },
//        };
//    }
//
//    @Test(dataProvider = "realVehicleJSONDataProvider")
//    public void shouldSynchronizeRealVehicleConfigurationsFromJSON(
//        List<RealVehicle> db,
//        List<RealVehicle> in,
//        List<RealVehicle> out)
//    {
//        setUpQmAllSensorDefinitions(Arrays.asList(sd1, sd2, sd3, sd4));
//        setUpQmAllRealVehicles(db, in);
//
//        JSONArray realVehicles = jsonConv.toJsonArray(true, in.toArray(new RealVehicle[0]));
//        JSONArray back = sync.syncRealVehicleConfig(realVehicles);
//
//        List<RealVehicle> result = ConvUtils.toRealVehicleList(back);
//
//        reconstructRealVehicleListFromDb(result);
//
//        assertThat(result).isNotNull().containsAll(out);
//        assertThat(out).containsAll(result);
//
//        verifyRealVehicleAccessInDb(in);
//    }
//
//    @SuppressWarnings("unchecked")
//    @Test
//    public void shouldThrowNoExceptionOnTransmissionError() throws IOException
//    {
//        when(com.transfer(any(RealVehicle.class), anyString(), any(byte[].class)))
//            .thenThrow(IOException.class);
//
//        sync.syncConfig(Arrays.asList(rv1));
//    }
//
//    @Test
//    public void shouldThrowNoExceptionOnNegativeResponseFromPeer() throws IOException
//    {
//        response.setStatus(Status.NOT_OK);
//
//        sync.syncConfig(Arrays.asList(rv1));
//    }
//
//    @Test
//    public void shouldSynchronizeRealVehicleConfigurations() throws ClientProtocolException, IOException, JSONException
//    {
//        transferCount = 0;
//        lastTransferData = null;
//
//        when(qm.findAllRealVehicles()).thenReturn(Arrays.asList(gs1, rv1, rv2, rv3, rv4));
//        when(qm.findAllSensorDefinitions()).thenReturn(Arrays.asList(sd1, sd2, sd3, sd4));
//
//        sync.syncConfig(Arrays.asList(rv1));
//
//        // System.out.println("### buggerit: " + new String(result1));
//
//        // verify(com).transfer(rv1, Connector.CONFIGURATION_UPDATE, result1.getBytes());
//
//        verify(com).transfer(eq(rv1), eq(RealVehicleBaseConstants.CONFIGURATION_UPDATE_CONNECTOR), any(byte[].class));
//
//        assertThat(transferCount).isEqualTo(1);
//        assertThat(lastTransferData).isNotNull();
//
//        JSONAssert.assertEquals(result1, new String(lastTransferData), false);
//    }
//
//    @DataProvider
//    public Object[][] sensorDefinition1DataProvider()
//    {
//        return new Object[][]{
//            new Object[]{
//                new ArrayList<RealVehicle>(),
//                new ArrayList<SensorDefinition>(),
//                rv1,
//                "",
//                "{\"rvs\":[],\"sen\":[]}",
//                new Object[]{},
//                new Object[]{},
//                new Object[]{},
//            },
//            new Object[]{
//                new ArrayList<RealVehicle>(),
//                Arrays.asList(sd1),
//                rv1,
//                "",
//                "{\"rvs\":[],\"sen\":[{\"id\":\"1\",\"visibility\":\"ALL_VV\",\"lastUpdate\":1000,"
//                    + "\"description\":\"Altimeter\",\"messageType\":\"std_msgs/Float32\",\"type\":\"ALTIMETER\",\"deleted\":false}]}",
//                new Object[]{},
//                new Object[]{},
//                new Object[]{},
//            },
//            new Object[]{
//                Arrays.asList(rv1),
//                new ArrayList<SensorDefinition>(),
//                rv2,
//                "",
//                "{\"rvs\":[{\"id\":\"1\",\"aoo\":\"[{lat: 43.8,lng: 13.4},{lat: 43.8,lng: 13.5},{lat: 43.9,lng: 13.5},"
//                    + "{lat: 43.9,lng: 13.5},{lat: 43.8,lng: 13.4}]\",\"name\":\"RV01\",\"sen\":[2],"
//                    + "\"upd\":1,\"type\":\"QUADROCOPTER\",\"deleted\":false,\"url\":\"http://localhost:12345/rv01\"}],\"sen\":[]}",
//                new Object[]{},
//                new Object[]{},
//                new Object[]{},
//            },
//            new Object[]{
//                Arrays.asList(rv1),
//                Arrays.asList(sd1),
//                rv3,
//                "",
//                "{\"rvs\":[{\"id\":\"1\",\"aoo\":\"[{lat: 43.8,lng: 13.4},{lat: 43.8,lng: 13.5},{lat: 43.9,lng: 13.5},"
//                    + "{lat: 43.9,lng: 13.5},{lat: 43.8,lng: 13.4}]\",\"name\":\"RV01\",\"sen\":[2],"
//                    + "\"upd\":1,\"type\":\"QUADROCOPTER\",\"deleted\":false,\"url\":\"http://localhost:12345/rv01\"}],"
//                    + "\"sen\":[{\"id\":\"1\",\"visibility\":\"ALL_VV\",\"lastUpdate\":1000,"
//                    + "\"description\":\"Altimeter\",\"messageType\":\"std_msgs/Float32\",\"type\":\"ALTIMETER\",\"deleted\":false}]}",
//                new Object[]{},
//                new Object[]{},
//                new Object[]{},
//            },
//            new Object[]{
//                Arrays.asList(),
//                Arrays.asList(),
//                rv4,
//                "{\"rvs\":[],\"sen\":[]}",
//                "{\"rvs\":[],\"sen\":[]}",
//                new Object[]{},
//                new Object[]{},
//                new Object[]{},
//            },
//            new Object[]{
//                new ArrayList<RealVehicle>(),
//                new ArrayList<SensorDefinition>(),
//                rv1,
//                "{\"rvs\":[],\"sen\":[{\"id\":\"1\",\"visibility\":\"ALL_VV\",\"lastUpdate\":1000,"
//                    + "\"description\":\"Altimeter\",\"messageType\":\"std_msgs/Float32\",\"type\":\"ALTIMETER\",\"deleted\":false}]}",
//                "{\"rvs\":[],\"sen\":[]}",
//                new Object[]{},
//                new Object[]{},
//                new Object[]{sd1},
//            },
//            new Object[]{
//                new ArrayList<RealVehicle>(),
//                new ArrayList<SensorDefinition>(),
//                rv2,
//                "{\"rvs\":[{\"id\":\"5\",\"aoo\":\"[{lat: 44.0,lng: 13.5},{lat: 44.0,lng: 13.6},{lat: 44.1,lng: 13.6},"
//                    + "{lat: 44.1,lng: 13.6},{lat: 44.0,lng: 13.5}]\",\"name\":\"RV05\",\"sen\":[],"
//                    + "\"type\":\"UNMANNED_UNDERWATER_VEHICLE\",\"upd\":\"5\","
//                    + "\"url\":\"http://localhost:12345/rv05\"}],\"sen\":[]}",
//                "{\"rvs\":[],\"sen\":[]}",
//                new Object[]{},
//                new Object[]{},
//                new Object[]{rv5},
//            },
//            new Object[]{
//                Arrays.asList(rv1),
//                Arrays.asList(sd2),
//                rv3,
//                "{\"rvs\":[{\"id\":\"1\",\"aoo\":\"[{lat: 43.8,lng: 13.4},{lat: 43.8,lng: 13.5},{lat: 43.9,lng: 13.5},"
//                    + "{lat: 43.9,lng: 13.5}]\",\"name\":\"RV01new\",\"sen\":[2],\"type\":\"FIXED_WING_AIRCRAFT\","
//                    + "\"upd\":\"10001\",\"url\":\"http://localhost:12345/rv01new\"}],\"sen\":[]}",
//                "{\"rvs\":[{\"id\":\"1\",\"aoo\":\"[{lat: 43.8,lng: 13.4},{lat: 43.8,lng: 13.5},{lat: 43.9,lng: 13.5},"
//                    + "{lat: 43.9,lng: 13.5},{lat: 43.8,lng: 13.4}]\",\"name\":\"RV01\",\"sen\":[2],"
//                    + "\"upd\":1,\"type\":\"QUADROCOPTER\",\"deleted\":false,\"url\":\"http://localhost:12345/rv01\"}],"
//                    + "\"sen\":["
//                    + "{\"id\":\"2\",\"visibility\":\"PRIVILEGED_VV\",\"lastUpdate\":2000,"
//                    + "\"description\":\"Belly Mounted Camera 640x480\","
//                    + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment='north'\","
//                    + "\"messageType\":\"sensor_msgs/Image\",\"type\":\"CAMERA\",\"deleted\":false}]}",
//                new Object[]{rv1new},
//                new Object[]{},
//                new Object[]{},
//            },
//            new Object[]{
//                Arrays.asList(rv1new),
//                Arrays.asList(sd2),
//                rv4,
//                "{\"rvs\":[],\"sen\":[]}",
//                "{\"rvs\":[{\"id\":\"1\",\"aoo\":\"[{lat: 43.8,lng: 13.4},{lat: 43.8,lng: 13.5},{lat: 43.9,lng: 13.5},"
//                    + "{lat: 43.9,lng: 13.5}]\",\"name\":\"RV01new\",\"sen\":[2],\"upd\":10001,\"type\":\"FIXED_WING_AIRCRAFT\","
//                    + "\"deleted\":false,\"url\":\"http://localhost:12345/rv01new\"}],"
//                    + "\"sen\":[{\"id\":\"2\",\"visibility\":\"PRIVILEGED_VV\",\"lastUpdate\":2000,"
//                    + "\"description\":\"Belly Mounted Camera 640x480\","
//                    + "\"parameters\":\"width=640 height=480 yaw=0 down=1.571 alignment='north'\","
//                    + "\"messageType\":\"sensor_msgs/Image\",\"type\":\"CAMERA\",\"deleted\":false}]}",
//                new Object[]{},
//                new Object[]{},
//                new Object[]{},
//            },
//        };
//    }
//
//    @Test(dataProvider = "sensorDefinition1DataProvider")
//    public void shouldUpdateOwnSensorDefinitionConfig(
//        List<RealVehicle> rvsDb,
//        List<SensorDefinition> sdsDb,
//        RealVehicle targetRV,
//        String responseContent,
//        String expectedResult,
//        Object[] dbUpdates,
//        Object[] dbDeletes,
//        Object[] dbSaves
//        ) throws IOException, JSONException
//    {
//        transferCount = 0;
//        lastTransferData = null;
//
//        setUpQmAllSensorDefinitions(sdsDb, new ArrayList<SensorDefinition>());
//        setUpQmAllRealVehicles(rvsDb, new ArrayList<RealVehicle>());
//
//        response.setContent(responseContent.getBytes());
//        response.setStatus(Status.OK);
//
//        sync.syncConfig(Arrays.asList(targetRV));
//
//        System.out.println("### " + expectedResult);
//        // verify(com).transfer(targetRV, Connector.CONFIGURATION_UPDATE, expectedResult.getBytes());
//
//        verify(com).transfer(eq(targetRV), eq(RealVehicleBaseConstants.CONFIGURATION_UPDATE_CONNECTOR)
//            , any(byte[].class));
//
//        assertThat(transferCount).isEqualTo(1);
//        assertThat(lastTransferData).isNotNull();
//        JSONAssert.assertEquals(expectedResult, new String(lastTransferData), false);
//
//        verify(qm, times(responseContent.length() == 0 ? 1 : 2)).findAllSensorDefinitions();
//        verify(qm, times(responseContent.length() == 0 ? 1 : 2)).findAllRealVehicles();
//
//        for (Object obj : dbUpdates)
//        {
//            verify(session).saveOrUpdate(eq(obj));
//        }
//
//        for (Object obj : dbDeletes)
//        {
//            verify(session).delete(eq(obj));
//        }
//
//        for (Object obj : dbSaves)
//        {
//            verify(session).save(eq(obj));
//        }
//    }
//
//    @DataProvider
//    public Object[][] sensorDefinition2DataProvider()
//    {
//        return new Object[][]{
//            new Object[]{
//                new ArrayList<RealVehicle>(),
//                new ArrayList<SensorDefinition>(),
//                gs1,
//                "",
//                "{\"rvs\":[],\"sen\":[]}",
//                new Object[]{},
//                new Object[]{},
//            },
//            new Object[]{
//                new ArrayList<RealVehicle>(),
//                Arrays.asList(sd1),
//                gs1,
//                "",
//                "{\"rvs\":[],\"sen\":[{\"id\":\"1\",\"visibility\":\"ALL_VV\",\"lastUpdate\":\"1000\","
//                    + "\"description\":\"Altimeter\",\"messageType\":\"std_msgs/Float32\",\"type\":\"ALTIMETER\"}]}",
//                new Object[]{},
//                new Object[]{},
//            },
//
//        };
//    }
//
//    @Test(dataProvider = "sensorDefinition2DataProvider")
//    public void shouldNotUpdateOwnSensorDefinitionConfigToHostingRv(
//        List<RealVehicle> rvsDb,
//        List<SensorDefinition> sdsDb,
//        RealVehicle targetRV,
//        String responseContent,
//        String expectedResult,
//        Object[] dbUpdates,
//        Object[] dbDeletes
//        ) throws IOException
//    {
//        setUpQmAllSensorDefinitions(sdsDb, new ArrayList<SensorDefinition>());
//        setUpQmAllRealVehicles(rvsDb, new ArrayList<RealVehicle>());
//
//        response.setContent(responseContent.getBytes());
//        response.setStatus(Status.OK);
//
//        sync.syncConfig(Arrays.asList(targetRV));
//
//        verifyZeroInteractions(com);
//    }
//
//    private void verifyRealVehicleAccessInDb(List<RealVehicle> in)
//    {
//        //        for (RealVehicle sd : in)
//        //        {
//        //            verify(qm).findRealVehicleById(eq(sd.getId()));
//        //        }
//
//        for (RealVehicle sd : addedRVs)
//        {
//            verify(session).save(eq(sd));
//        }
//
//        for (RealVehicle sd : removedRVs)
//        {
//            verify(session).saveOrUpdate(eq(sd));
//        }
//
//        for (RealVehicle sd : changedRVs)
//        {
//            verify(session).saveOrUpdate(eq(sd));
//        }
//    }
//
//    private void reconstructRealVehicleListFromDb(List<RealVehicle> result)
//    {
//        for (RealVehicle rv : result)
//        {
//            for (int k = 0, l = rv.getSensors().size(); k < l; ++k)
//            {
//                int id = rv.getSensors().get(k).getId();
//                for (SensorDefinition sdg : Arrays.asList(sd1, sd2, sd3, sd4))
//                {
//                    if (id == sdg.getId())
//                    {
//                        rv.getSensors().set(k, sdg);
//                        break;
//                    }
//                }
//            }
//        }
//    }
//
//    private void setUpQmAllRealVehicles(List<RealVehicle> db, List<RealVehicle> in)
//    {
//        when(qm.findAllRealVehicles()).thenReturn(db);
//        for (RealVehicle rv : db)
//        {
//            when(qm.findRealVehicleById(rv.getId())).thenReturn(rv);
//        }
//
//        addedRVs = new ArrayList<RealVehicle>();
//        for (RealVehicle rvIn : in)
//        {
//            boolean found = false;
//            for (RealVehicle rv : db)
//            {
//                if (rvIn.getId().intValue() == rv.getId().intValue())
//                {
//                    found = true;
//                    break;
//                }
//            }
//            if (!found)
//            {
//                addedRVs.add(rvIn);
//            }
//        }
//
//        removedRVs = new ArrayList<RealVehicle>();
//        for (RealVehicle rvDb : db)
//        {
//            boolean found = false;
//            for (RealVehicle rv : in)
//            {
//                if (rvDb.getId().intValue() == rv.getId().intValue())
//                {
//                    found = true;
//                    break;
//                }
//            }
//            if (!found)
//            {
//                removedRVs.add(rvDb);
//            }
//        }
//
//        changedRVs = new ArrayList<RealVehicle>();
//        for (RealVehicle rvDb : db)
//        {
//            for (RealVehicle rvIn : in)
//            {
//                if (rvDb.getId() == rvIn.getId() && rvDb.getLastUpdate().getTime() < rvIn.getLastUpdate().getTime())
//                {
//                    changedRVs.add(rvDb);
//                }
//            }
//        }
//    }
//
//    private void verifySensorDefinitionsAccessInDb(List<SensorDefinition> in)
//    {
//        verify(qm).findAllSensorDefinitions();
//
//        for (SensorDefinition sd : addedSDs)
//        {
//            verify(session).save(eq(sd));
//        }
//
//        for (SensorDefinition sd : removedSDs)
//        {
//            verify(session).saveOrUpdate(eq(sd));
//        }
//
//        for (SensorDefinition sd : changedSDs)
//        {
//            verify(session).saveOrUpdate(eq(sd));
//        }
//    }
//
//    private void setUpQmAllSensorDefinitions(List<SensorDefinition> allSds)
//    {
//        when(qm.findAllSensorDefinitions()).thenReturn(allSds);
//        for (SensorDefinition sd : allSds)
//        {
//            when(qm.findSensorDefinitionById(sd.getId())).thenReturn(sd);
//        }
//    }
//
//    private void setUpQmAllSensorDefinitions(List<SensorDefinition> db, List<SensorDefinition> in)
//    {
//        setUpQmAllSensorDefinitions(db);
//
//        addedSDs = new ArrayList<SensorDefinition>();
//        for (SensorDefinition sdIn : in)
//        {
//            boolean found = false;
//            for (SensorDefinition sd : db)
//            {
//                if (sdIn.getId().intValue() == sd.getId().intValue())
//                {
//                    found = true;
//                    break;
//                }
//            }
//            if (!found)
//            {
//                addedSDs.add(sdIn);
//            }
//        }
//
//        removedSDs = new ArrayList<SensorDefinition>();
//        for (SensorDefinition sdDb : db)
//        {
//            boolean found = false;
//            for (SensorDefinition sd : in)
//            {
//                if (sdDb.getId().intValue() == sd.getId().intValue())
//                {
//                    found = true;
//                    break;
//                }
//            }
//            if (!found)
//            {
//                removedSDs.add(sdDb);
//            }
//        }
//
//        changedSDs = new ArrayList<SensorDefinition>();
//        for (SensorDefinition sdDb : db)
//        {
//            for (SensorDefinition sdIn : in)
//            {
//                if (sdDb.getId() == sdIn.getId()
//                    && sdDb.getLastUpdate().getTime() < sdIn.getLastUpdate().getTime())
//                {
//                    changedSDs.add(sdDb);
//                }
//            }
//        }
//    }

}
