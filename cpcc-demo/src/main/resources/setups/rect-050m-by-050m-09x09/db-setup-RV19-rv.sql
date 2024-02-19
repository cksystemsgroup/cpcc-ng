INSERT INTO PARAMETERS (ID,NAME,SORT,STRING_VALUE) VALUES (1,'realVehicleName',0,'RV19');
INSERT INTO PARAMETERS (ID,NAME,SORT,STRING_VALUE) VALUES (2,'masterServerURI',0,'http://localhost:8191');
INSERT INTO PARAMETERS (ID,NAME,SORT,STRING_VALUE) VALUES (3,'useInternalRosCore',0,'true');

INSERT INTO DEVICES (ID,CONFIGURATION,TOPIC_ROOT,TYPE_ID) VALUES (1,'origin=(47.821222;13.038124;0) maxVelocity=20 maxAcceleration=5 precision=10 updateCycle=100 idlePower=20 hoverPower=55 mass=2.2 takeOffHeight=10 takeOffVelocity=2 takeOffAcceleration=2 landingVelocity=1.5 landingAcceleration=2','/rv19',4);
INSERT INTO MAPPING_ATTRIBUTES (CONNECTED_TO_AUTOPILOT,VV_VISIBLE,TOPIC_ID,DEVICE_ID,SENSORDEFINITION_ID) VALUES (1,1,6,1,null);
INSERT INTO MAPPING_ATTRIBUTES (CONNECTED_TO_AUTOPILOT,VV_VISIBLE,TOPIC_ID,DEVICE_ID,SENSORDEFINITION_ID) VALUES (1,1,5,1,9);
INSERT INTO MAPPING_ATTRIBUTES (CONNECTED_TO_AUTOPILOT,VV_VISIBLE,TOPIC_ID,DEVICE_ID,SENSORDEFINITION_ID) VALUES (1,1,7,1,1);
