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
VALUES ( 1,{ts '2016-08-09 18:24:58.309000000'},'GS01','http://localhost:8000','GROUND_STATION','{"type":"FeatureCollection","properties":{"center":[13.040811717510222,47.821922207617014],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040811717510222,47.821922207617014]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 2,{ts '2016-08-09 18:24:58.310000000'},'RV01','http://localhost:8010','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458693381864,47.82054832155724],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124228832476,47.820323742736214],[13.038124228832476,47.820772900378266],[13.03879315793125,47.820772900378266],[13.03879315793125,47.820323742736214],[13.038124228832476,47.820323742736214]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458693381864,47.82054832155724]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (10,{ts '2016-08-09 18:24:58.312000000'},'RV09','http://localhost:8090','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458693381864,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124228832476,47.820772900378266],[13.038124228832476,47.82122205802033],[13.03879315793125,47.82122205802033],[13.03879315793125,47.820772900378266],[13.038124228832476,47.820772900378266]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458693381864,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (18,{ts '2016-08-09 18:24:58.313000000'},'RV17','http://localhost:8170','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458693381864,47.82144663684136],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124228832476,47.82122205802033],[13.038124228832476,47.82167121566239],[13.03879315793125,47.82167121566239],[13.03879315793125,47.82122205802033],[13.038124228832476,47.82122205802033]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458693381864,47.82144663684136]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (26,{ts '2016-08-09 18:24:58.314000000'},'RV25','http://localhost:8250','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458693381864,47.821895794483424],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124228832476,47.82167121566239],[13.038124228832476,47.82212037330445],[13.03879315793125,47.82212037330445],[13.03879315793125,47.82167121566239],[13.038124228832476,47.82167121566239]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458693381864,47.821895794483424]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (34,{ts '2016-08-09 18:24:58.316000000'},'RV33','http://localhost:8330','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458693381864,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124228832476,47.82212037330445],[13.038124228832476,47.82256953094651],[13.03879315793125,47.82256953094651],[13.03879315793125,47.82212037330445],[13.038124228832476,47.82212037330445]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458693381864,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (42,{ts '2016-08-09 18:24:58.317000000'},'RV41','http://localhost:8410','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458693381864,47.822794109767536],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124228832476,47.82256953094651],[13.038124228832476,47.82301868858857],[13.03879315793125,47.82301868858857],[13.03879315793125,47.82256953094651],[13.038124228832476,47.82256953094651]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458693381864,47.822794109767536]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (50,{ts '2016-08-09 18:24:58.318000000'},'RV49','http://localhost:8490','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458693381864,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124228832476,47.82301868858857],[13.038124228832476,47.82346784623063],[13.03879315793125,47.82346784623063],[13.03879315793125,47.82301868858857],[13.038124228832476,47.82301868858857]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458693381864,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (58,{ts '2016-08-09 18:24:58.320000000'},'RV57','http://localhost:8570','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458693381864,47.823692425051654],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124228832476,47.82346784623063],[13.038124228832476,47.823917003872694],[13.03879315793125,47.823917003872694],[13.03879315793125,47.82346784623063],[13.038124228832476,47.82346784623063]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458693381864,47.823692425051654]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 3,{ts '2016-08-09 18:24:58.321000000'},'RV02','http://localhost:8020','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127622480637,47.82054832155724],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.03879315793125,47.820323742736214],[13.03879315793125,47.820772900378266],[13.039462087030023,47.820772900378266],[13.039462087030023,47.820323742736214],[13.03879315793125,47.820323742736214]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127622480637,47.82054832155724]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (11,{ts '2016-08-09 18:24:58.323000000'},'RV10','http://localhost:8100','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127622480637,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.03879315793125,47.820772900378266],[13.03879315793125,47.82122205802033],[13.039462087030023,47.82122205802033],[13.039462087030023,47.820772900378266],[13.03879315793125,47.820772900378266]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127622480637,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (19,{ts '2016-08-09 18:24:58.324000000'},'RV18','http://localhost:8180','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127622480637,47.82144663684136],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.03879315793125,47.82122205802033],[13.03879315793125,47.82167121566239],[13.039462087030023,47.82167121566239],[13.039462087030023,47.82122205802033],[13.03879315793125,47.82122205802033]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127622480637,47.82144663684136]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (27,{ts '2016-08-09 18:24:58.326000000'},'RV26','http://localhost:8260','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127622480637,47.821895794483424],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.03879315793125,47.82167121566239],[13.03879315793125,47.82212037330445],[13.039462087030023,47.82212037330445],[13.039462087030023,47.82167121566239],[13.03879315793125,47.82167121566239]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127622480637,47.821895794483424]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (35,{ts '2016-08-09 18:24:58.327000000'},'RV34','http://localhost:8340','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127622480637,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.03879315793125,47.82212037330445],[13.03879315793125,47.82256953094651],[13.039462087030023,47.82256953094651],[13.039462087030023,47.82212037330445],[13.03879315793125,47.82212037330445]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127622480637,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (43,{ts '2016-08-09 18:24:58.328000000'},'RV42','http://localhost:8420','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127622480637,47.822794109767536],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.03879315793125,47.82256953094651],[13.03879315793125,47.82301868858857],[13.039462087030023,47.82301868858857],[13.039462087030023,47.82256953094651],[13.03879315793125,47.82256953094651]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127622480637,47.822794109767536]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (51,{ts '2016-08-09 18:24:58.330000000'},'RV50','http://localhost:8500','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127622480637,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.03879315793125,47.82301868858857],[13.03879315793125,47.82346784623063],[13.039462087030023,47.82346784623063],[13.039462087030023,47.82301868858857],[13.03879315793125,47.82301868858857]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127622480637,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (59,{ts '2016-08-09 18:24:58.331000000'},'RV58','http://localhost:8580','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127622480637,47.823692425051654],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.03879315793125,47.82346784623063],[13.03879315793125,47.823917003872694],[13.039462087030023,47.823917003872694],[13.039462087030023,47.82346784623063],[13.03879315793125,47.82346784623063]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127622480637,47.823692425051654]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 4,{ts '2016-08-09 18:24:58.332000000'},'RV03','http://localhost:8030','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796551579412,47.82054832155724],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462087030023,47.820323742736214],[13.039462087030023,47.820772900378266],[13.040131016128798,47.820772900378266],[13.040131016128798,47.820323742736214],[13.039462087030023,47.820323742736214]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796551579412,47.82054832155724]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (12,{ts '2016-08-09 18:24:58.334000000'},'RV11','http://localhost:8110','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796551579412,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462087030023,47.820772900378266],[13.039462087030023,47.82122205802033],[13.040131016128798,47.82122205802033],[13.040131016128798,47.820772900378266],[13.039462087030023,47.820772900378266]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796551579412,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (20,{ts '2016-08-09 18:24:58.336000000'},'RV19','http://localhost:8190','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796551579412,47.82144663684136],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462087030023,47.82122205802033],[13.039462087030023,47.82167121566239],[13.040131016128798,47.82167121566239],[13.040131016128798,47.82122205802033],[13.039462087030023,47.82122205802033]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796551579412,47.82144663684136]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (28,{ts '2016-08-09 18:24:58.337000000'},'RV27','http://localhost:8270','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796551579412,47.821895794483424],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462087030023,47.82167121566239],[13.039462087030023,47.82212037330445],[13.040131016128798,47.82212037330445],[13.040131016128798,47.82167121566239],[13.039462087030023,47.82167121566239]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796551579412,47.821895794483424]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (36,{ts '2016-08-09 18:24:58.338000000'},'RV35','http://localhost:8350','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796551579412,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462087030023,47.82212037330445],[13.039462087030023,47.82256953094651],[13.040131016128798,47.82256953094651],[13.040131016128798,47.82212037330445],[13.039462087030023,47.82212037330445]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796551579412,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (44,{ts '2016-08-09 18:24:58.340000000'},'RV43','http://localhost:8430','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796551579412,47.822794109767536],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462087030023,47.82256953094651],[13.039462087030023,47.82301868858857],[13.040131016128798,47.82301868858857],[13.040131016128798,47.82256953094651],[13.039462087030023,47.82256953094651]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796551579412,47.822794109767536]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (52,{ts '2016-08-09 18:24:58.341000000'},'RV51','http://localhost:8510','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796551579412,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462087030023,47.82301868858857],[13.039462087030023,47.82346784623063],[13.040131016128798,47.82346784623063],[13.040131016128798,47.82301868858857],[13.039462087030023,47.82301868858857]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796551579412,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (60,{ts '2016-08-09 18:24:58.342000000'},'RV59','http://localhost:8590','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796551579412,47.823692425051654],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462087030023,47.82346784623063],[13.039462087030023,47.823917003872694],[13.040131016128798,47.823917003872694],[13.040131016128798,47.82346784623063],[13.039462087030023,47.82346784623063]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796551579412,47.823692425051654]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 5,{ts '2016-08-09 18:24:58.344000000'},'RV04','http://localhost:8040','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465480678185,47.82054832155724],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040131016128798,47.820323742736214],[13.040131016128798,47.820772900378266],[13.040799945227574,47.820772900378266],[13.040799945227574,47.820323742736214],[13.040131016128798,47.820323742736214]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465480678185,47.82054832155724]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (13,{ts '2016-08-09 18:24:58.345000000'},'RV12','http://localhost:8120','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465480678185,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040131016128798,47.820772900378266],[13.040131016128798,47.82122205802033],[13.040799945227574,47.82122205802033],[13.040799945227574,47.820772900378266],[13.040131016128798,47.820772900378266]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465480678185,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (21,{ts '2016-08-09 18:24:58.347000000'},'RV20','http://localhost:8200','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465480678185,47.82144663684136],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040131016128798,47.82122205802033],[13.040131016128798,47.82167121566239],[13.040799945227574,47.82167121566239],[13.040799945227574,47.82122205802033],[13.040131016128798,47.82122205802033]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465480678185,47.82144663684136]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (29,{ts '2016-08-09 18:24:58.348000000'},'RV28','http://localhost:8280','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465480678185,47.821895794483424],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040131016128798,47.82167121566239],[13.040131016128798,47.82212037330445],[13.040799945227574,47.82212037330445],[13.040799945227574,47.82167121566239],[13.040131016128798,47.82167121566239]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465480678185,47.821895794483424]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (37,{ts '2016-08-09 18:24:58.350000000'},'RV36','http://localhost:8360','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465480678185,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040131016128798,47.82212037330445],[13.040131016128798,47.82256953094651],[13.040799945227574,47.82256953094651],[13.040799945227574,47.82212037330445],[13.040131016128798,47.82212037330445]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465480678185,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (45,{ts '2016-08-09 18:24:58.352000000'},'RV44','http://localhost:8440','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465480678185,47.822794109767536],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040131016128798,47.82256953094651],[13.040131016128798,47.82301868858857],[13.040799945227574,47.82301868858857],[13.040799945227574,47.82256953094651],[13.040131016128798,47.82256953094651]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465480678185,47.822794109767536]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (53,{ts '2016-08-09 18:24:58.353000000'},'RV52','http://localhost:8520','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465480678185,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040131016128798,47.82301868858857],[13.040131016128798,47.82346784623063],[13.040799945227574,47.82346784623063],[13.040799945227574,47.82301868858857],[13.040131016128798,47.82301868858857]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465480678185,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (61,{ts '2016-08-09 18:24:58.354000000'},'RV60','http://localhost:8600','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465480678185,47.823692425051654],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040131016128798,47.82346784623063],[13.040131016128798,47.823917003872694],[13.040799945227574,47.823917003872694],[13.040799945227574,47.82346784623063],[13.040131016128798,47.82346784623063]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465480678185,47.823692425051654]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 6,{ts '2016-08-09 18:24:58.371000000'},'RV05','http://localhost:8050','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134409776957,47.82054832155724],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799945227574,47.820323742736214],[13.040799945227574,47.820772900378266],[13.041468874326346,47.820772900378266],[13.041468874326346,47.820323742736214],[13.040799945227574,47.820323742736214]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134409776957,47.82054832155724]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (14,{ts '2016-08-09 18:24:58.373000000'},'RV13','http://localhost:8130','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134409776957,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799945227574,47.820772900378266],[13.040799945227574,47.82122205802033],[13.041468874326346,47.82122205802033],[13.041468874326346,47.820772900378266],[13.040799945227574,47.820772900378266]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134409776957,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (22,{ts '2016-08-09 18:24:58.374000000'},'RV21','http://localhost:8210','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134409776957,47.82144663684136],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799945227574,47.82122205802033],[13.040799945227574,47.82167121566239],[13.041468874326346,47.82167121566239],[13.041468874326346,47.82122205802033],[13.040799945227574,47.82122205802033]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134409776957,47.82144663684136]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (30,{ts '2016-08-09 18:24:58.376000000'},'RV29','http://localhost:8290','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134409776957,47.821895794483424],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799945227574,47.82167121566239],[13.040799945227574,47.82212037330445],[13.041468874326346,47.82212037330445],[13.041468874326346,47.82167121566239],[13.040799945227574,47.82167121566239]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134409776957,47.821895794483424]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (38,{ts '2016-08-09 18:24:58.378000000'},'RV37','http://localhost:8370','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134409776957,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799945227574,47.82212037330445],[13.040799945227574,47.82256953094651],[13.041468874326346,47.82256953094651],[13.041468874326346,47.82212037330445],[13.040799945227574,47.82212037330445]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134409776957,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (46,{ts '2016-08-09 18:24:58.379000000'},'RV45','http://localhost:8450','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134409776957,47.822794109767536],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799945227574,47.82256953094651],[13.040799945227574,47.82301868858857],[13.041468874326346,47.82301868858857],[13.041468874326346,47.82256953094651],[13.040799945227574,47.82256953094651]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134409776957,47.822794109767536]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (54,{ts '2016-08-09 18:24:58.381000000'},'RV53','http://localhost:8530','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134409776957,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799945227574,47.82301868858857],[13.040799945227574,47.82346784623063],[13.041468874326346,47.82346784623063],[13.041468874326346,47.82301868858857],[13.040799945227574,47.82301868858857]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134409776957,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (62,{ts '2016-08-09 18:24:58.382000000'},'RV61','http://localhost:8610','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134409776957,47.823692425051654],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799945227574,47.82346784623063],[13.040799945227574,47.823917003872694],[13.041468874326346,47.823917003872694],[13.041468874326346,47.82346784623063],[13.040799945227574,47.82346784623063]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134409776957,47.823692425051654]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 7,{ts '2016-08-09 18:24:58.383000000'},'RV06','http://localhost:8060','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803338875733,47.82054832155724],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468874326346,47.820323742736214],[13.041468874326346,47.820772900378266],[13.042137803425119,47.820772900378266],[13.042137803425119,47.820323742736214],[13.041468874326346,47.820323742736214]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803338875733,47.82054832155724]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (15,{ts '2016-08-09 18:24:58.385000000'},'RV14','http://localhost:8140','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803338875733,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468874326346,47.820772900378266],[13.041468874326346,47.82122205802033],[13.042137803425119,47.82122205802033],[13.042137803425119,47.820772900378266],[13.041468874326346,47.820772900378266]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803338875733,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (23,{ts '2016-08-09 18:24:58.386000000'},'RV22','http://localhost:8220','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803338875733,47.82144663684136],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468874326346,47.82122205802033],[13.041468874326346,47.82167121566239],[13.042137803425119,47.82167121566239],[13.042137803425119,47.82122205802033],[13.041468874326346,47.82122205802033]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803338875733,47.82144663684136]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (31,{ts '2016-08-09 18:24:58.387000000'},'RV30','http://localhost:8300','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803338875733,47.821895794483424],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468874326346,47.82167121566239],[13.041468874326346,47.82212037330445],[13.042137803425119,47.82212037330445],[13.042137803425119,47.82167121566239],[13.041468874326346,47.82167121566239]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803338875733,47.821895794483424]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (39,{ts '2016-08-09 18:24:58.389000000'},'RV38','http://localhost:8380','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803338875733,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468874326346,47.82212037330445],[13.041468874326346,47.82256953094651],[13.042137803425119,47.82256953094651],[13.042137803425119,47.82212037330445],[13.041468874326346,47.82212037330445]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803338875733,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (47,{ts '2016-08-09 18:24:58.390000000'},'RV46','http://localhost:8460','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803338875733,47.822794109767536],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468874326346,47.82256953094651],[13.041468874326346,47.82301868858857],[13.042137803425119,47.82301868858857],[13.042137803425119,47.82256953094651],[13.041468874326346,47.82256953094651]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803338875733,47.822794109767536]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (55,{ts '2016-08-09 18:24:58.392000000'},'RV54','http://localhost:8540','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803338875733,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468874326346,47.82301868858857],[13.041468874326346,47.82346784623063],[13.042137803425119,47.82346784623063],[13.042137803425119,47.82301868858857],[13.041468874326346,47.82301868858857]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803338875733,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (63,{ts '2016-08-09 18:24:58.393000000'},'RV62','http://localhost:8620','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803338875733,47.823692425051654],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468874326346,47.82346784623063],[13.041468874326346,47.823917003872694],[13.042137803425119,47.823917003872694],[13.042137803425119,47.82346784623063],[13.041468874326346,47.82346784623063]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803338875733,47.823692425051654]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 8,{ts '2016-08-09 18:24:58.394000000'},'RV07','http://localhost:8070','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472267974505,47.82054832155724],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137803425119,47.820323742736214],[13.042137803425119,47.820772900378266],[13.042806732523893,47.820772900378266],[13.042806732523893,47.820323742736214],[13.042137803425119,47.820323742736214]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472267974505,47.82054832155724]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (16,{ts '2016-08-09 18:24:58.396000000'},'RV15','http://localhost:8150','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472267974505,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137803425119,47.820772900378266],[13.042137803425119,47.82122205802033],[13.042806732523893,47.82122205802033],[13.042806732523893,47.820772900378266],[13.042137803425119,47.820772900378266]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472267974505,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (24,{ts '2016-08-09 18:24:58.397000000'},'RV23','http://localhost:8230','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472267974505,47.82144663684136],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137803425119,47.82122205802033],[13.042137803425119,47.82167121566239],[13.042806732523893,47.82167121566239],[13.042806732523893,47.82122205802033],[13.042137803425119,47.82122205802033]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472267974505,47.82144663684136]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (32,{ts '2016-08-09 18:24:58.398000000'},'RV31','http://localhost:8310','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472267974505,47.821895794483424],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137803425119,47.82167121566239],[13.042137803425119,47.82212037330445],[13.042806732523893,47.82212037330445],[13.042806732523893,47.82167121566239],[13.042137803425119,47.82167121566239]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472267974505,47.821895794483424]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (40,{ts '2016-08-09 18:24:58.400000000'},'RV39','http://localhost:8390','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472267974505,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137803425119,47.82212037330445],[13.042137803425119,47.82256953094651],[13.042806732523893,47.82256953094651],[13.042806732523893,47.82212037330445],[13.042137803425119,47.82212037330445]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472267974505,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (48,{ts '2016-08-09 18:24:58.401000000'},'RV47','http://localhost:8470','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472267974505,47.822794109767536],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137803425119,47.82256953094651],[13.042137803425119,47.82301868858857],[13.042806732523893,47.82301868858857],[13.042806732523893,47.82256953094651],[13.042137803425119,47.82256953094651]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472267974505,47.822794109767536]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (56,{ts '2016-08-09 18:24:58.403000000'},'RV55','http://localhost:8550','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472267974505,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137803425119,47.82301868858857],[13.042137803425119,47.82346784623063],[13.042806732523893,47.82346784623063],[13.042806732523893,47.82301868858857],[13.042137803425119,47.82301868858857]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472267974505,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (64,{ts '2016-08-09 18:24:58.404000000'},'RV63','http://localhost:8630','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472267974505,47.823692425051654],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137803425119,47.82346784623063],[13.042137803425119,47.823917003872694],[13.042806732523893,47.823917003872694],[13.042806732523893,47.82346784623063],[13.042137803425119,47.82346784623063]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472267974505,47.823692425051654]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 9,{ts '2016-08-09 18:24:58.405000000'},'RV08','http://localhost:8080','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.04314119707328,47.82054832155724],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806732523893,47.820323742736214],[13.042806732523893,47.820772900378266],[13.043475661622667,47.820772900378266],[13.043475661622667,47.820323742736214],[13.042806732523893,47.820323742736214]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.04314119707328,47.82054832155724]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (17,{ts '2016-08-09 18:24:58.407000000'},'RV16','http://localhost:8160','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.04314119707328,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806732523893,47.820772900378266],[13.042806732523893,47.82122205802033],[13.043475661622667,47.82122205802033],[13.043475661622667,47.820772900378266],[13.042806732523893,47.820772900378266]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.04314119707328,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (25,{ts '2016-08-09 18:24:58.408000000'},'RV24','http://localhost:8240','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.04314119707328,47.82144663684136],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806732523893,47.82122205802033],[13.042806732523893,47.82167121566239],[13.043475661622667,47.82167121566239],[13.043475661622667,47.82122205802033],[13.042806732523893,47.82122205802033]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.04314119707328,47.82144663684136]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (33,{ts '2016-08-09 18:24:58.410000000'},'RV32','http://localhost:8320','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.04314119707328,47.821895794483424],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806732523893,47.82167121566239],[13.042806732523893,47.82212037330445],[13.043475661622667,47.82212037330445],[13.043475661622667,47.82167121566239],[13.042806732523893,47.82167121566239]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.04314119707328,47.821895794483424]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (41,{ts '2016-08-09 18:24:58.411000000'},'RV40','http://localhost:8400','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.04314119707328,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806732523893,47.82212037330445],[13.042806732523893,47.82256953094651],[13.043475661622667,47.82256953094651],[13.043475661622667,47.82212037330445],[13.042806732523893,47.82212037330445]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.04314119707328,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (49,{ts '2016-08-09 18:24:58.412000000'},'RV48','http://localhost:8480','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.04314119707328,47.822794109767536],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806732523893,47.82256953094651],[13.042806732523893,47.82301868858857],[13.043475661622667,47.82301868858857],[13.043475661622667,47.82256953094651],[13.042806732523893,47.82256953094651]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.04314119707328,47.822794109767536]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (57,{ts '2016-08-09 18:24:58.413000000'},'RV56','http://localhost:8560','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.04314119707328,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806732523893,47.82301868858857],[13.042806732523893,47.82346784623063],[13.043475661622667,47.82346784623063],[13.043475661622667,47.82301868858857],[13.042806732523893,47.82301868858857]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.04314119707328,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (65,{ts '2016-08-09 18:24:58.415000000'},'RV64','http://localhost:8640','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.04314119707328,47.823692425051654],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806732523893,47.82346784623063],[13.042806732523893,47.823917003872694],[13.043475661622667,47.823917003872694],[13.043475661622667,47.82346784623063],[13.042806732523893,47.82346784623063]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.04314119707328,47.823692425051654]}}]}',0);

INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 1, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 2, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 2, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 2, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (10, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (10, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (10, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (18, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (18, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (18, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (26, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (26, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (26, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (34, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (34, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (34, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (42, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (42, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (42, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (50, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (50, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (50, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (58, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (58, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (58, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 3, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 3, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 3, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (11, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (11, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (11, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (19, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (19, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (19, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (27, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (27, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (27, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (35, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (35, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (35, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (43, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (43, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (43, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (51, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (51, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (51, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (59, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (59, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (59, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 4, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 4, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 4, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (12, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (12, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (12, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (20, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (20, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (20, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (28, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (28, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (28, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (36, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (36, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (36, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (44, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (44, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (44, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (52, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (52, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (52, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (60, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (60, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (60, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 5, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 5, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 5, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (13, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (13, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (13, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (21, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (21, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (21, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (29, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (29, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (29, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (37, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (37, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (37, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (45, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (45, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (45, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (53, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (53, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (53, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (61, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (61, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (61, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 6, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 6, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 6, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (14, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (14, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (14, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (22, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (22, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (22, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (30, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (30, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (30, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (38, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (38, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (38, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (46, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (46, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (46, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (54, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (54, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (54, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (62, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (62, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (62, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 7, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 7, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 7, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (15, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (15, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (15, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (23, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (23, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (23, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (31, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (31, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (31, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (39, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (39, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (39, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (47, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (47, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (47, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (55, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (55, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (55, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (63, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (63, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (63, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 8, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 8, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 8, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (16, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (16, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (16, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (24, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (24, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (24, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (32, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (32, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (32, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (40, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (40, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (40, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (48, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (48, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (48, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (56, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (56, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (56, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (64, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (64, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (64, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 9, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 9, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 9, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (17, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (17, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (17, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (25, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (25, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (25, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (33, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (33, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (33, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (41, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (41, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (41, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (49, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (49, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (49, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (57, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (57, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (57, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (65, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (65, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (65, 9);

