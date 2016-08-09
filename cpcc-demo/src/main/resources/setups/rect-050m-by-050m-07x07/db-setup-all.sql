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
VALUES ( 1,{ts '2016-08-09 18:24:58.069000000'},'GS01','http://localhost:8000','GROUND_STATION','{"type":"FeatureCollection","properties":{"center":[13.040811717510222,47.821922207617014],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040811717510222,47.821922207617014]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 2,{ts '2016-08-09 18:24:58.070000000'},'RV01','http://localhost:8010','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038793170953396,47.82077290037826],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.03845870495716,47.82054832155723],[13.03845870495716,47.82099747919929],[13.039127636949631,47.82099747919929],[13.039127636949631,47.82054832155723],[13.03845870495716,47.82054832155723]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038793170953396,47.82077290037826]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 9,{ts '2016-08-09 18:24:58.072000000'},'RV08','http://localhost:8080','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038793170953396,47.82122205802031],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.03845870495716,47.82099747919929],[13.03845870495716,47.82144663684135],[13.039127636949631,47.82144663684135],[13.039127636949631,47.82099747919929],[13.03845870495716,47.82099747919929]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038793170953396,47.82122205802031]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (16,{ts '2016-08-09 18:24:58.074000000'},'RV15','http://localhost:8150','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038793170953396,47.82167121566238],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.03845870495716,47.82144663684135],[13.03845870495716,47.82189579448341],[13.039127636949631,47.82189579448341],[13.039127636949631,47.82144663684135],[13.03845870495716,47.82144663684135]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038793170953396,47.82167121566238]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (23,{ts '2016-08-09 18:24:58.075000000'},'RV22','http://localhost:8220','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038793170953396,47.822120373304436],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.03845870495716,47.82189579448341],[13.03845870495716,47.82234495212547],[13.039127636949631,47.82234495212547],[13.039127636949631,47.82189579448341],[13.03845870495716,47.82189579448341]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038793170953396,47.822120373304436]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (30,{ts '2016-08-09 18:24:58.077000000'},'RV29','http://localhost:8290','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038793170953396,47.822569530946495],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.03845870495716,47.82234495212547],[13.03845870495716,47.82279410976753],[13.039127636949631,47.82279410976753],[13.039127636949631,47.82234495212547],[13.03845870495716,47.82234495212547]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038793170953396,47.822569530946495]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (37,{ts '2016-08-09 18:24:58.078000000'},'RV36','http://localhost:8360','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038793170953396,47.823018688588554],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.03845870495716,47.82279410976753],[13.03845870495716,47.82324326740958],[13.039127636949631,47.82324326740958],[13.039127636949631,47.82279410976753],[13.03845870495716,47.82279410976753]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038793170953396,47.823018688588554]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (44,{ts '2016-08-09 18:24:58.080000000'},'RV43','http://localhost:8430','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038793170953396,47.82346784623061],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.03845870495716,47.82324326740958],[13.03845870495716,47.82369242505165],[13.039127636949631,47.82369242505165],[13.039127636949631,47.82324326740958],[13.03845870495716,47.82324326740958]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038793170953396,47.82346784623061]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 3,{ts '2016-08-09 18:24:58.082000000'},'RV02','http://localhost:8020','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039462102945867,47.82077290037826],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039127636949631,47.82054832155723],[13.039127636949631,47.82099747919929],[13.039796568942107,47.82099747919929],[13.039796568942107,47.82054832155723],[13.039127636949631,47.82054832155723]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039462102945867,47.82077290037826]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (10,{ts '2016-08-09 18:24:58.083000000'},'RV09','http://localhost:8090','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039462102945867,47.82122205802031],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039127636949631,47.82099747919929],[13.039127636949631,47.82144663684135],[13.039796568942107,47.82144663684135],[13.039796568942107,47.82099747919929],[13.039127636949631,47.82099747919929]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039462102945867,47.82122205802031]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (17,{ts '2016-08-09 18:24:58.085000000'},'RV16','http://localhost:8160','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039462102945867,47.82167121566238],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039127636949631,47.82144663684135],[13.039127636949631,47.82189579448341],[13.039796568942107,47.82189579448341],[13.039796568942107,47.82144663684135],[13.039127636949631,47.82144663684135]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039462102945867,47.82167121566238]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (24,{ts '2016-08-09 18:24:58.086000000'},'RV23','http://localhost:8230','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039462102945867,47.822120373304436],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039127636949631,47.82189579448341],[13.039127636949631,47.82234495212547],[13.039796568942107,47.82234495212547],[13.039796568942107,47.82189579448341],[13.039127636949631,47.82189579448341]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039462102945867,47.822120373304436]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (31,{ts '2016-08-09 18:24:58.088000000'},'RV30','http://localhost:8300','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039462102945867,47.822569530946495],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039127636949631,47.82234495212547],[13.039127636949631,47.82279410976753],[13.039796568942107,47.82279410976753],[13.039796568942107,47.82234495212547],[13.039127636949631,47.82234495212547]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039462102945867,47.822569530946495]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (38,{ts '2016-08-09 18:24:58.089000000'},'RV37','http://localhost:8370','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039462102945867,47.823018688588554],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039127636949631,47.82279410976753],[13.039127636949631,47.82324326740958],[13.039796568942107,47.82324326740958],[13.039796568942107,47.82279410976753],[13.039127636949631,47.82279410976753]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039462102945867,47.823018688588554]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (45,{ts '2016-08-09 18:24:58.091000000'},'RV44','http://localhost:8440','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039462102945867,47.82346784623061],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039127636949631,47.82324326740958],[13.039127636949631,47.82369242505165],[13.039796568942107,47.82369242505165],[13.039796568942107,47.82324326740958],[13.039127636949631,47.82324326740958]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039462102945867,47.82346784623061]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 4,{ts '2016-08-09 18:24:58.092000000'},'RV03','http://localhost:8030','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040131034938343,47.82077290037826],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039796568942107,47.82054832155723],[13.039796568942107,47.82099747919929],[13.04046550093458,47.82099747919929],[13.04046550093458,47.82054832155723],[13.039796568942107,47.82054832155723]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040131034938343,47.82077290037826]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (11,{ts '2016-08-09 18:24:58.094000000'},'RV10','http://localhost:8100','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040131034938343,47.82122205802031],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039796568942107,47.82099747919929],[13.039796568942107,47.82144663684135],[13.04046550093458,47.82144663684135],[13.04046550093458,47.82099747919929],[13.039796568942107,47.82099747919929]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040131034938343,47.82122205802031]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (18,{ts '2016-08-09 18:24:58.095000000'},'RV17','http://localhost:8170','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040131034938343,47.82167121566238],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039796568942107,47.82144663684135],[13.039796568942107,47.82189579448341],[13.04046550093458,47.82189579448341],[13.04046550093458,47.82144663684135],[13.039796568942107,47.82144663684135]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040131034938343,47.82167121566238]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (25,{ts '2016-08-09 18:24:58.097000000'},'RV24','http://localhost:8240','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040131034938343,47.822120373304436],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039796568942107,47.82189579448341],[13.039796568942107,47.82234495212547],[13.04046550093458,47.82234495212547],[13.04046550093458,47.82189579448341],[13.039796568942107,47.82189579448341]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040131034938343,47.822120373304436]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (32,{ts '2016-08-09 18:24:58.098000000'},'RV31','http://localhost:8310','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040131034938343,47.822569530946495],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039796568942107,47.82234495212547],[13.039796568942107,47.82279410976753],[13.04046550093458,47.82279410976753],[13.04046550093458,47.82234495212547],[13.039796568942107,47.82234495212547]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040131034938343,47.822569530946495]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (39,{ts '2016-08-09 18:24:58.100000000'},'RV38','http://localhost:8380','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040131034938343,47.823018688588554],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039796568942107,47.82279410976753],[13.039796568942107,47.82324326740958],[13.04046550093458,47.82324326740958],[13.04046550093458,47.82279410976753],[13.039796568942107,47.82279410976753]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040131034938343,47.823018688588554]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (46,{ts '2016-08-09 18:24:58.101000000'},'RV45','http://localhost:8450','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040131034938343,47.82346784623061],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039796568942107,47.82324326740958],[13.039796568942107,47.82369242505165],[13.04046550093458,47.82369242505165],[13.04046550093458,47.82324326740958],[13.039796568942107,47.82324326740958]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040131034938343,47.82346784623061]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 5,{ts '2016-08-09 18:24:58.103000000'},'RV04','http://localhost:8040','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040799966930816,47.82077290037826],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.04046550093458,47.82054832155723],[13.04046550093458,47.82099747919929],[13.041134432927054,47.82099747919929],[13.041134432927054,47.82054832155723],[13.04046550093458,47.82054832155723]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040799966930816,47.82077290037826]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (12,{ts '2016-08-09 18:24:58.104000000'},'RV11','http://localhost:8110','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040799966930816,47.82122205802031],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.04046550093458,47.82099747919929],[13.04046550093458,47.82144663684135],[13.041134432927054,47.82144663684135],[13.041134432927054,47.82099747919929],[13.04046550093458,47.82099747919929]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040799966930816,47.82122205802031]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (19,{ts '2016-08-09 18:24:58.105000000'},'RV18','http://localhost:8180','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040799966930816,47.82167121566238],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.04046550093458,47.82144663684135],[13.04046550093458,47.82189579448341],[13.041134432927054,47.82189579448341],[13.041134432927054,47.82144663684135],[13.04046550093458,47.82144663684135]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040799966930816,47.82167121566238]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (26,{ts '2016-08-09 18:24:58.107000000'},'RV25','http://localhost:8250','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040799966930816,47.822120373304436],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.04046550093458,47.82189579448341],[13.04046550093458,47.82234495212547],[13.041134432927054,47.82234495212547],[13.041134432927054,47.82189579448341],[13.04046550093458,47.82189579448341]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040799966930816,47.822120373304436]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (33,{ts '2016-08-09 18:24:58.108000000'},'RV32','http://localhost:8320','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040799966930816,47.822569530946495],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.04046550093458,47.82234495212547],[13.04046550093458,47.82279410976753],[13.041134432927054,47.82279410976753],[13.041134432927054,47.82234495212547],[13.04046550093458,47.82234495212547]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040799966930816,47.822569530946495]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (40,{ts '2016-08-09 18:24:58.109000000'},'RV39','http://localhost:8390','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040799966930816,47.823018688588554],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.04046550093458,47.82279410976753],[13.04046550093458,47.82324326740958],[13.041134432927054,47.82324326740958],[13.041134432927054,47.82279410976753],[13.04046550093458,47.82279410976753]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040799966930816,47.823018688588554]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (47,{ts '2016-08-09 18:24:58.111000000'},'RV46','http://localhost:8460','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040799966930816,47.82346784623061],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.04046550093458,47.82324326740958],[13.04046550093458,47.82369242505165],[13.041134432927054,47.82369242505165],[13.041134432927054,47.82324326740958],[13.04046550093458,47.82324326740958]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040799966930816,47.82346784623061]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 6,{ts '2016-08-09 18:24:58.112000000'},'RV05','http://localhost:8050','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.04146889892329,47.82077290037826],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041134432927054,47.82054832155723],[13.041134432927054,47.82099747919929],[13.041803364919526,47.82099747919929],[13.041803364919526,47.82054832155723],[13.041134432927054,47.82054832155723]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.04146889892329,47.82077290037826]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (13,{ts '2016-08-09 18:24:58.113000000'},'RV12','http://localhost:8120','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.04146889892329,47.82122205802031],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041134432927054,47.82099747919929],[13.041134432927054,47.82144663684135],[13.041803364919526,47.82144663684135],[13.041803364919526,47.82099747919929],[13.041134432927054,47.82099747919929]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.04146889892329,47.82122205802031]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (20,{ts '2016-08-09 18:24:58.115000000'},'RV19','http://localhost:8190','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.04146889892329,47.82167121566238],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041134432927054,47.82144663684135],[13.041134432927054,47.82189579448341],[13.041803364919526,47.82189579448341],[13.041803364919526,47.82144663684135],[13.041134432927054,47.82144663684135]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.04146889892329,47.82167121566238]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (27,{ts '2016-08-09 18:24:58.116000000'},'RV26','http://localhost:8260','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.04146889892329,47.822120373304436],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041134432927054,47.82189579448341],[13.041134432927054,47.82234495212547],[13.041803364919526,47.82234495212547],[13.041803364919526,47.82189579448341],[13.041134432927054,47.82189579448341]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.04146889892329,47.822120373304436]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (34,{ts '2016-08-09 18:24:58.118000000'},'RV33','http://localhost:8330','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.04146889892329,47.822569530946495],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041134432927054,47.82234495212547],[13.041134432927054,47.82279410976753],[13.041803364919526,47.82279410976753],[13.041803364919526,47.82234495212547],[13.041134432927054,47.82234495212547]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.04146889892329,47.822569530946495]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (41,{ts '2016-08-09 18:24:58.120000000'},'RV40','http://localhost:8400','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.04146889892329,47.823018688588554],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041134432927054,47.82279410976753],[13.041134432927054,47.82324326740958],[13.041803364919526,47.82324326740958],[13.041803364919526,47.82279410976753],[13.041134432927054,47.82279410976753]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.04146889892329,47.823018688588554]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (48,{ts '2016-08-09 18:24:58.121000000'},'RV47','http://localhost:8470','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.04146889892329,47.82346784623061],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041134432927054,47.82324326740958],[13.041134432927054,47.82369242505165],[13.041803364919526,47.82369242505165],[13.041803364919526,47.82324326740958],[13.041134432927054,47.82324326740958]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.04146889892329,47.82346784623061]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 7,{ts '2016-08-09 18:24:58.123000000'},'RV06','http://localhost:8060','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042137830915763,47.82077290037826],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041803364919526,47.82054832155723],[13.041803364919526,47.82099747919929],[13.042472296912,47.82099747919929],[13.042472296912,47.82054832155723],[13.041803364919526,47.82054832155723]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042137830915763,47.82077290037826]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (14,{ts '2016-08-09 18:24:58.124000000'},'RV13','http://localhost:8130','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042137830915763,47.82122205802031],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041803364919526,47.82099747919929],[13.041803364919526,47.82144663684135],[13.042472296912,47.82144663684135],[13.042472296912,47.82099747919929],[13.041803364919526,47.82099747919929]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042137830915763,47.82122205802031]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (21,{ts '2016-08-09 18:24:58.126000000'},'RV20','http://localhost:8200','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042137830915763,47.82167121566238],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041803364919526,47.82144663684135],[13.041803364919526,47.82189579448341],[13.042472296912,47.82189579448341],[13.042472296912,47.82144663684135],[13.041803364919526,47.82144663684135]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042137830915763,47.82167121566238]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (28,{ts '2016-08-09 18:24:58.127000000'},'RV27','http://localhost:8270','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042137830915763,47.822120373304436],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041803364919526,47.82189579448341],[13.041803364919526,47.82234495212547],[13.042472296912,47.82234495212547],[13.042472296912,47.82189579448341],[13.041803364919526,47.82189579448341]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042137830915763,47.822120373304436]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (35,{ts '2016-08-09 18:24:58.129000000'},'RV34','http://localhost:8340','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042137830915763,47.822569530946495],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041803364919526,47.82234495212547],[13.041803364919526,47.82279410976753],[13.042472296912,47.82279410976753],[13.042472296912,47.82234495212547],[13.041803364919526,47.82234495212547]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042137830915763,47.822569530946495]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (42,{ts '2016-08-09 18:24:58.130000000'},'RV41','http://localhost:8410','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042137830915763,47.823018688588554],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041803364919526,47.82279410976753],[13.041803364919526,47.82324326740958],[13.042472296912,47.82324326740958],[13.042472296912,47.82279410976753],[13.041803364919526,47.82279410976753]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042137830915763,47.823018688588554]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (49,{ts '2016-08-09 18:24:58.131000000'},'RV48','http://localhost:8480','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042137830915763,47.82346784623061],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041803364919526,47.82324326740958],[13.041803364919526,47.82369242505165],[13.042472296912,47.82369242505165],[13.042472296912,47.82324326740958],[13.041803364919526,47.82324326740958]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042137830915763,47.82346784623061]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 8,{ts '2016-08-09 18:24:58.133000000'},'RV07','http://localhost:8070','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042806762908238,47.82077290037826],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042472296912,47.82054832155723],[13.042472296912,47.82099747919929],[13.043141228904474,47.82099747919929],[13.043141228904474,47.82054832155723],[13.042472296912,47.82054832155723]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042806762908238,47.82077290037826]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (15,{ts '2016-08-09 18:24:58.134000000'},'RV14','http://localhost:8140','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042806762908238,47.82122205802031],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042472296912,47.82099747919929],[13.042472296912,47.82144663684135],[13.043141228904474,47.82144663684135],[13.043141228904474,47.82099747919929],[13.042472296912,47.82099747919929]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042806762908238,47.82122205802031]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (22,{ts '2016-08-09 18:24:58.136000000'},'RV21','http://localhost:8210','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042806762908238,47.82167121566238],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042472296912,47.82144663684135],[13.042472296912,47.82189579448341],[13.043141228904474,47.82189579448341],[13.043141228904474,47.82144663684135],[13.042472296912,47.82144663684135]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042806762908238,47.82167121566238]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (29,{ts '2016-08-09 18:24:58.137000000'},'RV28','http://localhost:8280','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042806762908238,47.822120373304436],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042472296912,47.82189579448341],[13.042472296912,47.82234495212547],[13.043141228904474,47.82234495212547],[13.043141228904474,47.82189579448341],[13.042472296912,47.82189579448341]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042806762908238,47.822120373304436]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (36,{ts '2016-08-09 18:24:58.139000000'},'RV35','http://localhost:8350','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042806762908238,47.822569530946495],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042472296912,47.82234495212547],[13.042472296912,47.82279410976753],[13.043141228904474,47.82279410976753],[13.043141228904474,47.82234495212547],[13.042472296912,47.82234495212547]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042806762908238,47.822569530946495]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (43,{ts '2016-08-09 18:24:58.140000000'},'RV42','http://localhost:8420','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042806762908238,47.823018688588554],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042472296912,47.82279410976753],[13.042472296912,47.82324326740958],[13.043141228904474,47.82324326740958],[13.043141228904474,47.82279410976753],[13.042472296912,47.82279410976753]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042806762908238,47.823018688588554]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (50,{ts '2016-08-09 18:24:58.141000000'},'RV49','http://localhost:8490','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042806762908238,47.82346784623061],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042472296912,47.82324326740958],[13.042472296912,47.82369242505165],[13.043141228904474,47.82369242505165],[13.043141228904474,47.82324326740958],[13.042472296912,47.82324326740958]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042806762908238,47.82346784623061]}}]}',0);

INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 1, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 2, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 2, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 2, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 9, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 9, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 9, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (16, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (16, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (16, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (23, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (23, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (23, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (30, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (30, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (30, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (37, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (37, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (37, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (44, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (44, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (44, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 3, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 3, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 3, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (10, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (10, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (10, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (17, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (17, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (17, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (24, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (24, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (24, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (31, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (31, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (31, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (38, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (38, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (38, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (45, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (45, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (45, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 4, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 4, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 4, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (11, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (11, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (11, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (18, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (18, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (18, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (25, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (25, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (25, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (32, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (32, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (32, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (39, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (39, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (39, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (46, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (46, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (46, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 5, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 5, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 5, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (12, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (12, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (12, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (19, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (19, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (19, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (26, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (26, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (26, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (33, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (33, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (33, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (40, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (40, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (40, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (47, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (47, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (47, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 6, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 6, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 6, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (13, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (13, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (13, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (20, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (20, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (20, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (27, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (27, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (27, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (34, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (34, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (34, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (41, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (41, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (41, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (48, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (48, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (48, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 7, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 7, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 7, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (14, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (14, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (14, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (21, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (21, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (21, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (28, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (28, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (28, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (35, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (35, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (35, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (42, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (42, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (42, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (49, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (49, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (49, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 8, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 8, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 8, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (15, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (15, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (15, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (22, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (22, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (22, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (29, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (29, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (29, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (36, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (36, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (36, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (43, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (43, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (43, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (50, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (50, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (50, 9);

