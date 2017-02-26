INSERT INTO SENSOR_DEFINITIONS (ID,DESCRIPTION,LAST_UPDATE,MESSAGE_TYPE,PARAMETERS,TYPE,VISIBILITY,DELETED)
VALUES (1,'Altimeter',{ts '2016-08-09 18:24:57.135000000'},'std_msgs/Float32',null,'ALTIMETER','ALL_VV',0);
INSERT INTO SENSOR_DEFINITIONS (ID,DESCRIPTION,LAST_UPDATE,MESSAGE_TYPE,PARAMETERS,TYPE,VISIBILITY,DELETED)
VALUES (2,'Area of Operations',{ts '2016-08-09 18:24:57.135000000'},'std_msgs/Float32',null,'AREA_OF_OPERATIONS','PRIVILEGED_VV',0);
INSERT INTO SENSOR_DEFINITIONS (ID,DESCRIPTION,LAST_UPDATE,MESSAGE_TYPE,PARAMETERS,TYPE,VISIBILITY,DELETED)
VALUES (3,'Barometer',{ts '2016-08-09 18:24:57.135000000'},'std_msgs/Float32',null,'BAROMETER','ALL_VV',0);
INSERT INTO SENSOR_DEFINITIONS (ID,DESCRIPTION,LAST_UPDATE,MESSAGE_TYPE,PARAMETERS,TYPE,VISIBILITY,DELETED)
VALUES (4,'Battery',{ts '2016-08-09 18:24:57.135000000'},'std_msgs/Float32',null,'BATTERY','PRIVILEGED_VV',0);
INSERT INTO SENSOR_DEFINITIONS (ID,DESCRIPTION,LAST_UPDATE,MESSAGE_TYPE,PARAMETERS,TYPE,VISIBILITY,DELETED)
VALUES (5,'Belly Mounted Camera 640x480',{ts '2016-08-09 18:24:57.135000000'},'sensor_msgs/Image','width=640 height=480 yaw=0 down=1.571 alignment=''north''','CAMERA','ALL_VV',0);
INSERT INTO SENSOR_DEFINITIONS (ID,DESCRIPTION,LAST_UPDATE,MESSAGE_TYPE,PARAMETERS,TYPE,VISIBILITY,DELETED)
VALUES (7,'CO2',{ts '2016-08-09 18:24:57.135000000'},'std_msgs/Float32',null,'CO2','ALL_VV',0);
INSERT INTO SENSOR_DEFINITIONS (ID,DESCRIPTION,LAST_UPDATE,MESSAGE_TYPE,PARAMETERS,TYPE,VISIBILITY,DELETED)
VALUES (9,'GPS',{ts '2016-08-09 18:24:57.135000000'},'sensor_msgs/NavSatFix',null,'GPS','ALL_VV',0);
INSERT INTO SENSOR_DEFINITIONS (ID,DESCRIPTION,LAST_UPDATE,MESSAGE_TYPE,PARAMETERS,TYPE,VISIBILITY,DELETED)
VALUES (10,'Hardware',{ts '2016-08-09 18:24:57.135000000'},'std_msgs/Float32',null,'HARDWARE','PRIVILEGED_VV',0);
INSERT INTO SENSOR_DEFINITIONS (ID,DESCRIPTION,LAST_UPDATE,MESSAGE_TYPE,PARAMETERS,TYPE,VISIBILITY,DELETED)
VALUES (11,'NOx',{ts '2016-08-09 18:24:57.135000000'},'std_msgs/Float32',null,'NOX','ALL_VV',0);
INSERT INTO SENSOR_DEFINITIONS (ID,DESCRIPTION,LAST_UPDATE,MESSAGE_TYPE,PARAMETERS,TYPE,VISIBILITY,DELETED)
VALUES (12,'Thermometer',{ts '2016-08-09 18:24:57.135000000'},'std_msgs/Float32',null,'THERMOMETER','ALL_VV',0);

INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 1,{ts '2016-08-09 18:24:57.407000000'},'GS01','http://localhost:8000','GROUND_STATION','{"type":"FeatureCollection","properties":{"center":[13.040811717510222,47.821922207617014],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040811717510222,47.821922207617014]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 2,{ts '2016-08-09 18:24:57.410000000'},'RV01','http://localhost:8010','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040131081239696,47.82167121566238],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039796609455882,47.82144663684135],[13.039796609455882,47.82189579448341],[13.04046555302351,47.82189579448341],[13.04046555302351,47.82144663684135],[13.039796609455882,47.82144663684135]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040131081239696,47.82167121566238]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 5,{ts '2016-08-09 18:24:57.414000000'},'RV04','http://localhost:8040','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040131081239696,47.82212037330443],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039796609455882,47.82189579448341],[13.039796609455882,47.82234495212547],[13.04046555302351,47.82234495212547],[13.04046555302351,47.82189579448341],[13.039796609455882,47.82189579448341]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040131081239696,47.82212037330443]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 8,{ts '2016-08-09 18:24:57.418000000'},'RV07','http://localhost:8070','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040131081239696,47.822569530946495],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039796609455882,47.82234495212547],[13.039796609455882,47.82279410976753],[13.04046555302351,47.82279410976753],[13.04046555302351,47.82234495212547],[13.039796609455882,47.82234495212547]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040131081239696,47.822569530946495]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 3,{ts '2016-08-09 18:24:57.422000000'},'RV02','http://localhost:8020','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040800024807321,47.82167121566238],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.04046555302351,47.82144663684135],[13.04046555302351,47.82189579448341],[13.041134496591134,47.82189579448341],[13.041134496591134,47.82144663684135],[13.04046555302351,47.82144663684135]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040800024807321,47.82167121566238]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 6,{ts '2016-08-09 18:24:57.427000000'},'RV05','http://localhost:8050','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040800024807321,47.82212037330443],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.04046555302351,47.82189579448341],[13.04046555302351,47.82234495212547],[13.041134496591134,47.82234495212547],[13.041134496591134,47.82189579448341],[13.04046555302351,47.82189579448341]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040800024807321,47.82212037330443]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 9,{ts '2016-08-09 18:24:57.430000000'},'RV08','http://localhost:8080','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040800024807321,47.822569530946495],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.04046555302351,47.82234495212547],[13.04046555302351,47.82279410976753],[13.041134496591134,47.82279410976753],[13.041134496591134,47.82234495212547],[13.04046555302351,47.82234495212547]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040800024807321,47.822569530946495]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 4,{ts '2016-08-09 18:24:57.434000000'},'RV03','http://localhost:8030','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041468968374948,47.82167121566238],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041134496591134,47.82144663684135],[13.041134496591134,47.82189579448341],[13.041803440158763,47.82189579448341],[13.041803440158763,47.82144663684135],[13.041134496591134,47.82144663684135]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041468968374948,47.82167121566238]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 7,{ts '2016-08-09 18:24:57.438000000'},'RV06','http://localhost:8060','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041468968374948,47.82212037330443],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041134496591134,47.82189579448341],[13.041134496591134,47.82234495212547],[13.041803440158763,47.82234495212547],[13.041803440158763,47.82189579448341],[13.041134496591134,47.82189579448341]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041468968374948,47.82212037330443]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (10,{ts '2016-08-09 18:24:57.442000000'},'RV09','http://localhost:8090','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041468968374948,47.822569530946495],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041134496591134,47.82234495212547],[13.041134496591134,47.82279410976753],[13.041803440158763,47.82279410976753],[13.041803440158763,47.82234495212547],[13.041134496591134,47.82234495212547]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041468968374948,47.822569530946495]}}]}',0);

INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 1, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 2, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 2, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 2, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 5, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 5, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 5, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 8, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 8, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 8, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 3, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 3, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 3, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 6, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 6, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 6, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 9, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 9, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 9, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 4, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 4, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 4, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 7, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 7, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 7, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (10, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (10, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (10, 9);
