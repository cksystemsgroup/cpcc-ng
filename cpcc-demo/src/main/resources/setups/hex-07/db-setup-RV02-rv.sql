
-- GS01 47.821922;13.040812
-- RV01 47.822898;13.038793
-- RV02 47.823676;13.040800
-- RV03 47.822898;13.042807
-- RV04 47.821342;13.042807
-- RV05 47.820565;13.040800
-- RV06 47.821342;13.038793
-- RV07 47.822120;13.040800

INSERT INTO DEVICES (ID,CONFIGURATION,TOPIC_ROOT,TYPE_ID)
VALUES (8,'origin=(47.823676;13.040800;0) maxVelocity=20 maxAcceleration=5 precision=10 updateCycle=100 idlePower=20 hoverPower=55 mass=2.2 takeOffHeight=10 takeOffVelocity=2 takeOffAcceleration=2 landingVelocity=1.5 landingAcceleration=2','/quad02',4);

INSERT INTO MAPPING_ATTRIBUTES (CONNECTED_TO_AUTOPILOT,VV_VISIBLE,TOPIC_ID,DEVICE_ID,SENSORDEFINITION_ID) VALUES (1,0,6,8,null);
INSERT INTO MAPPING_ATTRIBUTES (CONNECTED_TO_AUTOPILOT,VV_VISIBLE,TOPIC_ID,DEVICE_ID,SENSORDEFINITION_ID) VALUES (1,1,7,8,1);
INSERT INTO MAPPING_ATTRIBUTES (CONNECTED_TO_AUTOPILOT,VV_VISIBLE,TOPIC_ID,DEVICE_ID,SENSORDEFINITION_ID) VALUES (1,1,5,8,9);

INSERT INTO PARAMETERS (ID,NAME,SORT,STRING_VALUE) VALUES (1,'realVehicleName',0,'RV02');
INSERT INTO PARAMETERS (ID,NAME,SORT,STRING_VALUE) VALUES (2,'masterServerURI',0,'http://localhost:13002');
INSERT INTO PARAMETERS (ID,NAME,SORT,STRING_VALUE) VALUES (3,'useInternalRosCore',0,'true');

-- UPDATE real_vehicles SET last_update = SYSDATE WHERE name = 'RV02';
