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
VALUES ( 1,{ts '2016-08-09 18:24:58.948000000'},'GS01','http://localhost:8000','GROUND_STATION','{"type":"FeatureCollection","properties":{"center":[13.040811717510222,47.821922207617014],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040811717510222,47.821922207617014]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 2,{ts '2016-08-09 18:24:58.949000000'},'RV01','http://localhost:8010','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.037789738238855,47.82009916391517],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.037455276583115,47.81987458509414],[13.037455276583115,47.82032374273621],[13.038124199894595,47.82032374273621],[13.038124199894595,47.81987458509414],[13.037455276583115,47.81987458509414]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.037789738238855,47.82009916391517]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (12,{ts '2016-08-09 18:24:58.951000000'},'RV11','http://localhost:8110','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.037789738238855,47.82054832155723],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.037455276583115,47.82032374273621],[13.037455276583115,47.82077290037826],[13.038124199894595,47.82077290037826],[13.038124199894595,47.82032374273621],[13.037455276583115,47.82032374273621]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.037789738238855,47.82054832155723]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (22,{ts '2016-08-09 18:24:58.952000000'},'RV21','http://localhost:8210','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.037789738238855,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.037455276583115,47.82077290037826],[13.037455276583115,47.82122205802032],[13.038124199894595,47.82122205802032],[13.038124199894595,47.82077290037826],[13.037455276583115,47.82077290037826]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.037789738238855,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (32,{ts '2016-08-09 18:24:58.953000000'},'RV31','http://localhost:8310','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.037789738238855,47.82144663684135],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.037455276583115,47.82122205802032],[13.037455276583115,47.82167121566238],[13.038124199894595,47.82167121566238],[13.038124199894595,47.82122205802032],[13.037455276583115,47.82122205802032]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.037789738238855,47.82144663684135]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (42,{ts '2016-08-09 18:24:58.954000000'},'RV41','http://localhost:8410','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.037789738238855,47.82189579448341],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.037455276583115,47.82167121566238],[13.037455276583115,47.822120373304436],[13.038124199894595,47.822120373304436],[13.038124199894595,47.82167121566238],[13.037455276583115,47.82167121566238]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.037789738238855,47.82189579448341]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (52,{ts '2016-08-09 18:24:58.955000000'},'RV51','http://localhost:8510','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.037789738238855,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.037455276583115,47.822120373304436],[13.037455276583115,47.822569530946495],[13.038124199894595,47.822569530946495],[13.038124199894595,47.822120373304436],[13.037455276583115,47.822120373304436]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.037789738238855,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (62,{ts '2016-08-09 18:24:58.957000000'},'RV61','http://localhost:8610','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.037789738238855,47.82279410976753],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.037455276583115,47.822569530946495],[13.037455276583115,47.823018688588554],[13.038124199894595,47.823018688588554],[13.038124199894595,47.822569530946495],[13.037455276583115,47.822569530946495]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.037789738238855,47.82279410976753]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (72,{ts '2016-08-09 18:24:58.958000000'},'RV71','http://localhost:8710','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.037789738238855,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.037455276583115,47.823018688588554],[13.037455276583115,47.82346784623062],[13.038124199894595,47.82346784623062],[13.038124199894595,47.823018688588554],[13.037455276583115,47.823018688588554]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.037789738238855,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (82,{ts '2016-08-09 18:24:58.959000000'},'RV81','http://localhost:8810','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.037789738238855,47.82369242505165],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.037455276583115,47.82346784623062],[13.037455276583115,47.82391700387267],[13.038124199894595,47.82391700387267],[13.038124199894595,47.82346784623062],[13.037455276583115,47.82346784623062]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.037789738238855,47.82369242505165]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (92,{ts '2016-08-09 18:24:58.960000000'},'RV91','http://localhost:8910','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.037789738238855,47.82414158269371],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.037455276583115,47.82391700387267],[13.037455276583115,47.82436616151474],[13.038124199894595,47.82436616151474],[13.038124199894595,47.82391700387267],[13.037455276583115,47.82391700387267]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.037789738238855,47.82414158269371]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 3,{ts '2016-08-09 18:24:58.961000000'},'RV02','http://localhost:8020','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458661550335,47.82009916391517],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124199894595,47.81987458509414],[13.038124199894595,47.82032374273621],[13.038793123206077,47.82032374273621],[13.038793123206077,47.81987458509414],[13.038124199894595,47.81987458509414]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458661550335,47.82009916391517]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (13,{ts '2016-08-09 18:24:58.962000000'},'RV12','http://localhost:8120','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458661550335,47.82054832155723],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124199894595,47.82032374273621],[13.038124199894595,47.82077290037826],[13.038793123206077,47.82077290037826],[13.038793123206077,47.82032374273621],[13.038124199894595,47.82032374273621]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458661550335,47.82054832155723]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (23,{ts '2016-08-09 18:24:58.964000000'},'RV22','http://localhost:8220','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458661550335,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124199894595,47.82077290037826],[13.038124199894595,47.82122205802032],[13.038793123206077,47.82122205802032],[13.038793123206077,47.82077290037826],[13.038124199894595,47.82077290037826]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458661550335,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (33,{ts '2016-08-09 18:24:58.965000000'},'RV32','http://localhost:8320','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458661550335,47.82144663684135],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124199894595,47.82122205802032],[13.038124199894595,47.82167121566238],[13.038793123206077,47.82167121566238],[13.038793123206077,47.82122205802032],[13.038124199894595,47.82122205802032]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458661550335,47.82144663684135]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (43,{ts '2016-08-09 18:24:58.966000000'},'RV42','http://localhost:8420','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458661550335,47.82189579448341],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124199894595,47.82167121566238],[13.038124199894595,47.822120373304436],[13.038793123206077,47.822120373304436],[13.038793123206077,47.82167121566238],[13.038124199894595,47.82167121566238]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458661550335,47.82189579448341]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (53,{ts '2016-08-09 18:24:58.967000000'},'RV52','http://localhost:8520','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458661550335,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124199894595,47.822120373304436],[13.038124199894595,47.822569530946495],[13.038793123206077,47.822569530946495],[13.038793123206077,47.822120373304436],[13.038124199894595,47.822120373304436]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458661550335,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (63,{ts '2016-08-09 18:24:58.968000000'},'RV62','http://localhost:8620','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458661550335,47.82279410976753],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124199894595,47.822569530946495],[13.038124199894595,47.823018688588554],[13.038793123206077,47.823018688588554],[13.038793123206077,47.822569530946495],[13.038124199894595,47.822569530946495]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458661550335,47.82279410976753]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (73,{ts '2016-08-09 18:24:58.970000000'},'RV72','http://localhost:8720','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458661550335,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124199894595,47.823018688588554],[13.038124199894595,47.82346784623062],[13.038793123206077,47.82346784623062],[13.038793123206077,47.823018688588554],[13.038124199894595,47.823018688588554]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458661550335,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (83,{ts '2016-08-09 18:24:58.971000000'},'RV82','http://localhost:8820','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458661550335,47.82369242505165],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124199894595,47.82346784623062],[13.038124199894595,47.82391700387267],[13.038793123206077,47.82391700387267],[13.038793123206077,47.82346784623062],[13.038124199894595,47.82346784623062]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458661550335,47.82369242505165]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (93,{ts '2016-08-09 18:24:58.972000000'},'RV92','http://localhost:8920','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.038458661550335,47.82414158269371],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038124199894595,47.82391700387267],[13.038124199894595,47.82436616151474],[13.038793123206077,47.82436616151474],[13.038793123206077,47.82391700387267],[13.038124199894595,47.82391700387267]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.038458661550335,47.82414158269371]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 4,{ts '2016-08-09 18:24:58.973000000'},'RV03','http://localhost:8030','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127584861815,47.82009916391517],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038793123206077,47.81987458509414],[13.038793123206077,47.82032374273621],[13.039462046517555,47.82032374273621],[13.039462046517555,47.81987458509414],[13.038793123206077,47.81987458509414]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127584861815,47.82009916391517]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (14,{ts '2016-08-09 18:24:58.974000000'},'RV13','http://localhost:8130','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127584861815,47.82054832155723],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038793123206077,47.82032374273621],[13.038793123206077,47.82077290037826],[13.039462046517555,47.82077290037826],[13.039462046517555,47.82032374273621],[13.038793123206077,47.82032374273621]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127584861815,47.82054832155723]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (24,{ts '2016-08-09 18:24:58.975000000'},'RV23','http://localhost:8230','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127584861815,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038793123206077,47.82077290037826],[13.038793123206077,47.82122205802032],[13.039462046517555,47.82122205802032],[13.039462046517555,47.82077290037826],[13.038793123206077,47.82077290037826]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127584861815,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (34,{ts '2016-08-09 18:24:58.977000000'},'RV33','http://localhost:8330','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127584861815,47.82144663684135],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038793123206077,47.82122205802032],[13.038793123206077,47.82167121566238],[13.039462046517555,47.82167121566238],[13.039462046517555,47.82122205802032],[13.038793123206077,47.82122205802032]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127584861815,47.82144663684135]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (44,{ts '2016-08-09 18:24:58.978000000'},'RV43','http://localhost:8430','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127584861815,47.82189579448341],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038793123206077,47.82167121566238],[13.038793123206077,47.822120373304436],[13.039462046517555,47.822120373304436],[13.039462046517555,47.82167121566238],[13.038793123206077,47.82167121566238]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127584861815,47.82189579448341]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (54,{ts '2016-08-09 18:24:58.979000000'},'RV53','http://localhost:8530','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127584861815,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038793123206077,47.822120373304436],[13.038793123206077,47.822569530946495],[13.039462046517555,47.822569530946495],[13.039462046517555,47.822120373304436],[13.038793123206077,47.822120373304436]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127584861815,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (64,{ts '2016-08-09 18:24:58.981000000'},'RV63','http://localhost:8630','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127584861815,47.82279410976753],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038793123206077,47.822569530946495],[13.038793123206077,47.823018688588554],[13.039462046517555,47.823018688588554],[13.039462046517555,47.822569530946495],[13.038793123206077,47.822569530946495]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127584861815,47.82279410976753]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (74,{ts '2016-08-09 18:24:58.982000000'},'RV73','http://localhost:8730','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127584861815,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038793123206077,47.823018688588554],[13.038793123206077,47.82346784623062],[13.039462046517555,47.82346784623062],[13.039462046517555,47.823018688588554],[13.038793123206077,47.823018688588554]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127584861815,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (84,{ts '2016-08-09 18:24:58.983000000'},'RV83','http://localhost:8830','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127584861815,47.82369242505165],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038793123206077,47.82346784623062],[13.038793123206077,47.82391700387267],[13.039462046517555,47.82391700387267],[13.039462046517555,47.82346784623062],[13.038793123206077,47.82346784623062]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127584861815,47.82369242505165]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (94,{ts '2016-08-09 18:24:58.984000000'},'RV93','http://localhost:8930','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039127584861815,47.82414158269371],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.038793123206077,47.82391700387267],[13.038793123206077,47.82436616151474],[13.039462046517555,47.82436616151474],[13.039462046517555,47.82391700387267],[13.038793123206077,47.82391700387267]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039127584861815,47.82414158269371]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 5,{ts '2016-08-09 18:24:58.986000000'},'RV04','http://localhost:8040','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796508173294,47.82009916391517],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462046517555,47.81987458509414],[13.039462046517555,47.82032374273621],[13.040130969829033,47.82032374273621],[13.040130969829033,47.81987458509414],[13.039462046517555,47.81987458509414]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796508173294,47.82009916391517]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (15,{ts '2016-08-09 18:24:58.987000000'},'RV14','http://localhost:8140','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796508173294,47.82054832155723],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462046517555,47.82032374273621],[13.039462046517555,47.82077290037826],[13.040130969829033,47.82077290037826],[13.040130969829033,47.82032374273621],[13.039462046517555,47.82032374273621]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796508173294,47.82054832155723]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (25,{ts '2016-08-09 18:24:58.988000000'},'RV24','http://localhost:8240','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796508173294,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462046517555,47.82077290037826],[13.039462046517555,47.82122205802032],[13.040130969829033,47.82122205802032],[13.040130969829033,47.82077290037826],[13.039462046517555,47.82077290037826]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796508173294,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (35,{ts '2016-08-09 18:24:58.990000000'},'RV34','http://localhost:8340','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796508173294,47.82144663684135],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462046517555,47.82122205802032],[13.039462046517555,47.82167121566238],[13.040130969829033,47.82167121566238],[13.040130969829033,47.82122205802032],[13.039462046517555,47.82122205802032]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796508173294,47.82144663684135]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (45,{ts '2016-08-09 18:24:58.991000000'},'RV44','http://localhost:8440','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796508173294,47.82189579448341],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462046517555,47.82167121566238],[13.039462046517555,47.822120373304436],[13.040130969829033,47.822120373304436],[13.040130969829033,47.82167121566238],[13.039462046517555,47.82167121566238]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796508173294,47.82189579448341]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (55,{ts '2016-08-09 18:24:58.992000000'},'RV54','http://localhost:8540','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796508173294,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462046517555,47.822120373304436],[13.039462046517555,47.822569530946495],[13.040130969829033,47.822569530946495],[13.040130969829033,47.822120373304436],[13.039462046517555,47.822120373304436]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796508173294,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (65,{ts '2016-08-09 18:24:58.993000000'},'RV64','http://localhost:8640','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796508173294,47.82279410976753],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462046517555,47.822569530946495],[13.039462046517555,47.823018688588554],[13.040130969829033,47.823018688588554],[13.040130969829033,47.822569530946495],[13.039462046517555,47.822569530946495]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796508173294,47.82279410976753]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (75,{ts '2016-08-09 18:24:58.994000000'},'RV74','http://localhost:8740','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796508173294,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462046517555,47.823018688588554],[13.039462046517555,47.82346784623062],[13.040130969829033,47.82346784623062],[13.040130969829033,47.823018688588554],[13.039462046517555,47.823018688588554]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796508173294,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (85,{ts '2016-08-09 18:24:58.996000000'},'RV84','http://localhost:8840','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796508173294,47.82369242505165],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462046517555,47.82346784623062],[13.039462046517555,47.82391700387267],[13.040130969829033,47.82391700387267],[13.040130969829033,47.82346784623062],[13.039462046517555,47.82346784623062]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796508173294,47.82369242505165]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (95,{ts '2016-08-09 18:24:58.997000000'},'RV94','http://localhost:8940','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.039796508173294,47.82414158269371],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.039462046517555,47.82391700387267],[13.039462046517555,47.82436616151474],[13.040130969829033,47.82436616151474],[13.040130969829033,47.82391700387267],[13.039462046517555,47.82391700387267]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.039796508173294,47.82414158269371]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 6,{ts '2016-08-09 18:24:58.998000000'},'RV05','http://localhost:8050','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465431484776,47.82009916391517],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040130969829033,47.81987458509414],[13.040130969829033,47.82032374273621],[13.040799893140514,47.82032374273621],[13.040799893140514,47.81987458509414],[13.040130969829033,47.81987458509414]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465431484776,47.82009916391517]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (16,{ts '2016-08-09 18:24:58.999000000'},'RV15','http://localhost:8150','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465431484776,47.82054832155723],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040130969829033,47.82032374273621],[13.040130969829033,47.82077290037826],[13.040799893140514,47.82077290037826],[13.040799893140514,47.82032374273621],[13.040130969829033,47.82032374273621]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465431484776,47.82054832155723]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (26,{ts '2016-08-09 18:24:59.000000000'},'RV25','http://localhost:8250','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465431484776,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040130969829033,47.82077290037826],[13.040130969829033,47.82122205802032],[13.040799893140514,47.82122205802032],[13.040799893140514,47.82077290037826],[13.040130969829033,47.82077290037826]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465431484776,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (36,{ts '2016-08-09 18:24:59.002000000'},'RV35','http://localhost:8350','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465431484776,47.82144663684135],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040130969829033,47.82122205802032],[13.040130969829033,47.82167121566238],[13.040799893140514,47.82167121566238],[13.040799893140514,47.82122205802032],[13.040130969829033,47.82122205802032]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465431484776,47.82144663684135]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (46,{ts '2016-08-09 18:24:59.003000000'},'RV45','http://localhost:8450','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465431484776,47.82189579448341],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040130969829033,47.82167121566238],[13.040130969829033,47.822120373304436],[13.040799893140514,47.822120373304436],[13.040799893140514,47.82167121566238],[13.040130969829033,47.82167121566238]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465431484776,47.82189579448341]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (56,{ts '2016-08-09 18:24:59.004000000'},'RV55','http://localhost:8550','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465431484776,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040130969829033,47.822120373304436],[13.040130969829033,47.822569530946495],[13.040799893140514,47.822569530946495],[13.040799893140514,47.822120373304436],[13.040130969829033,47.822120373304436]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465431484776,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (66,{ts '2016-08-09 18:24:59.005000000'},'RV65','http://localhost:8650','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465431484776,47.82279410976753],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040130969829033,47.822569530946495],[13.040130969829033,47.823018688588554],[13.040799893140514,47.823018688588554],[13.040799893140514,47.822569530946495],[13.040130969829033,47.822569530946495]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465431484776,47.82279410976753]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (76,{ts '2016-08-09 18:24:59.006000000'},'RV75','http://localhost:8750','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465431484776,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040130969829033,47.823018688588554],[13.040130969829033,47.82346784623062],[13.040799893140514,47.82346784623062],[13.040799893140514,47.823018688588554],[13.040130969829033,47.823018688588554]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465431484776,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (86,{ts '2016-08-09 18:24:59.007000000'},'RV85','http://localhost:8850','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465431484776,47.82369242505165],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040130969829033,47.82346784623062],[13.040130969829033,47.82391700387267],[13.040799893140514,47.82391700387267],[13.040799893140514,47.82346784623062],[13.040130969829033,47.82346784623062]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465431484776,47.82369242505165]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (96,{ts '2016-08-09 18:24:59.009000000'},'RV95','http://localhost:8950','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.040465431484776,47.82414158269371],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040130969829033,47.82391700387267],[13.040130969829033,47.82436616151474],[13.040799893140514,47.82436616151474],[13.040799893140514,47.82391700387267],[13.040130969829033,47.82391700387267]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.040465431484776,47.82414158269371]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 7,{ts '2016-08-09 18:24:59.010000000'},'RV06','http://localhost:8060','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134354796252,47.82009916391517],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799893140514,47.81987458509414],[13.040799893140514,47.82032374273621],[13.041468816451994,47.82032374273621],[13.041468816451994,47.81987458509414],[13.040799893140514,47.81987458509414]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134354796252,47.82009916391517]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (17,{ts '2016-08-09 18:24:59.011000000'},'RV16','http://localhost:8160','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134354796252,47.82054832155723],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799893140514,47.82032374273621],[13.040799893140514,47.82077290037826],[13.041468816451994,47.82077290037826],[13.041468816451994,47.82032374273621],[13.040799893140514,47.82032374273621]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134354796252,47.82054832155723]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (27,{ts '2016-08-09 18:24:59.013000000'},'RV26','http://localhost:8260','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134354796252,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799893140514,47.82077290037826],[13.040799893140514,47.82122205802032],[13.041468816451994,47.82122205802032],[13.041468816451994,47.82077290037826],[13.040799893140514,47.82077290037826]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134354796252,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (37,{ts '2016-08-09 18:24:59.014000000'},'RV36','http://localhost:8360','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134354796252,47.82144663684135],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799893140514,47.82122205802032],[13.040799893140514,47.82167121566238],[13.041468816451994,47.82167121566238],[13.041468816451994,47.82122205802032],[13.040799893140514,47.82122205802032]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134354796252,47.82144663684135]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (47,{ts '2016-08-09 18:24:59.015000000'},'RV46','http://localhost:8460','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134354796252,47.82189579448341],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799893140514,47.82167121566238],[13.040799893140514,47.822120373304436],[13.041468816451994,47.822120373304436],[13.041468816451994,47.82167121566238],[13.040799893140514,47.82167121566238]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134354796252,47.82189579448341]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (57,{ts '2016-08-09 18:24:59.017000000'},'RV56','http://localhost:8560','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134354796252,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799893140514,47.822120373304436],[13.040799893140514,47.822569530946495],[13.041468816451994,47.822569530946495],[13.041468816451994,47.822120373304436],[13.040799893140514,47.822120373304436]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134354796252,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (67,{ts '2016-08-09 18:24:59.018000000'},'RV66','http://localhost:8660','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134354796252,47.82279410976753],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799893140514,47.822569530946495],[13.040799893140514,47.823018688588554],[13.041468816451994,47.823018688588554],[13.041468816451994,47.822569530946495],[13.040799893140514,47.822569530946495]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134354796252,47.82279410976753]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (77,{ts '2016-08-09 18:24:59.019000000'},'RV76','http://localhost:8760','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134354796252,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799893140514,47.823018688588554],[13.040799893140514,47.82346784623062],[13.041468816451994,47.82346784623062],[13.041468816451994,47.823018688588554],[13.040799893140514,47.823018688588554]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134354796252,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (87,{ts '2016-08-09 18:24:59.020000000'},'RV86','http://localhost:8860','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134354796252,47.82369242505165],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799893140514,47.82346784623062],[13.040799893140514,47.82391700387267],[13.041468816451994,47.82391700387267],[13.041468816451994,47.82346784623062],[13.040799893140514,47.82346784623062]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134354796252,47.82369242505165]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (97,{ts '2016-08-09 18:24:59.021000000'},'RV96','http://localhost:8960','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041134354796252,47.82414158269371],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.040799893140514,47.82391700387267],[13.040799893140514,47.82436616151474],[13.041468816451994,47.82436616151474],[13.041468816451994,47.82391700387267],[13.040799893140514,47.82391700387267]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041134354796252,47.82414158269371]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 8,{ts '2016-08-09 18:24:59.022000000'},'RV07','http://localhost:8070','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803278107732,47.82009916391517],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468816451994,47.81987458509414],[13.041468816451994,47.82032374273621],[13.042137739763474,47.82032374273621],[13.042137739763474,47.81987458509414],[13.041468816451994,47.81987458509414]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803278107732,47.82009916391517]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (18,{ts '2016-08-09 18:24:59.024000000'},'RV17','http://localhost:8170','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803278107732,47.82054832155723],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468816451994,47.82032374273621],[13.041468816451994,47.82077290037826],[13.042137739763474,47.82077290037826],[13.042137739763474,47.82032374273621],[13.041468816451994,47.82032374273621]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803278107732,47.82054832155723]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (28,{ts '2016-08-09 18:24:59.025000000'},'RV27','http://localhost:8270','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803278107732,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468816451994,47.82077290037826],[13.041468816451994,47.82122205802032],[13.042137739763474,47.82122205802032],[13.042137739763474,47.82077290037826],[13.041468816451994,47.82077290037826]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803278107732,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (38,{ts '2016-08-09 18:24:59.026000000'},'RV37','http://localhost:8370','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803278107732,47.82144663684135],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468816451994,47.82122205802032],[13.041468816451994,47.82167121566238],[13.042137739763474,47.82167121566238],[13.042137739763474,47.82122205802032],[13.041468816451994,47.82122205802032]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803278107732,47.82144663684135]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (48,{ts '2016-08-09 18:24:59.028000000'},'RV47','http://localhost:8470','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803278107732,47.82189579448341],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468816451994,47.82167121566238],[13.041468816451994,47.822120373304436],[13.042137739763474,47.822120373304436],[13.042137739763474,47.82167121566238],[13.041468816451994,47.82167121566238]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803278107732,47.82189579448341]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (58,{ts '2016-08-09 18:24:59.029000000'},'RV57','http://localhost:8570','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803278107732,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468816451994,47.822120373304436],[13.041468816451994,47.822569530946495],[13.042137739763474,47.822569530946495],[13.042137739763474,47.822120373304436],[13.041468816451994,47.822120373304436]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803278107732,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (68,{ts '2016-08-09 18:24:59.030000000'},'RV67','http://localhost:8670','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803278107732,47.82279410976753],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468816451994,47.822569530946495],[13.041468816451994,47.823018688588554],[13.042137739763474,47.823018688588554],[13.042137739763474,47.822569530946495],[13.041468816451994,47.822569530946495]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803278107732,47.82279410976753]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (78,{ts '2016-08-09 18:24:59.032000000'},'RV77','http://localhost:8770','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803278107732,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468816451994,47.823018688588554],[13.041468816451994,47.82346784623062],[13.042137739763474,47.82346784623062],[13.042137739763474,47.823018688588554],[13.041468816451994,47.823018688588554]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803278107732,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (88,{ts '2016-08-09 18:24:59.033000000'},'RV87','http://localhost:8870','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803278107732,47.82369242505165],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468816451994,47.82346784623062],[13.041468816451994,47.82391700387267],[13.042137739763474,47.82391700387267],[13.042137739763474,47.82346784623062],[13.041468816451994,47.82346784623062]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803278107732,47.82369242505165]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (98,{ts '2016-08-09 18:24:59.034000000'},'RV97','http://localhost:8970','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.041803278107732,47.82414158269371],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.041468816451994,47.82391700387267],[13.041468816451994,47.82436616151474],[13.042137739763474,47.82436616151474],[13.042137739763474,47.82391700387267],[13.041468816451994,47.82391700387267]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.041803278107732,47.82414158269371]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES ( 9,{ts '2016-08-09 18:24:59.036000000'},'RV08','http://localhost:8080','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472201419214,47.82009916391517],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137739763474,47.81987458509414],[13.042137739763474,47.82032374273621],[13.042806663074952,47.82032374273621],[13.042806663074952,47.81987458509414],[13.042137739763474,47.81987458509414]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472201419214,47.82009916391517]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (19,{ts '2016-08-09 18:24:59.037000000'},'RV18','http://localhost:8180','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472201419214,47.82054832155723],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137739763474,47.82032374273621],[13.042137739763474,47.82077290037826],[13.042806663074952,47.82077290037826],[13.042806663074952,47.82032374273621],[13.042137739763474,47.82032374273621]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472201419214,47.82054832155723]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (29,{ts '2016-08-09 18:24:59.039000000'},'RV28','http://localhost:8280','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472201419214,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137739763474,47.82077290037826],[13.042137739763474,47.82122205802032],[13.042806663074952,47.82122205802032],[13.042806663074952,47.82077290037826],[13.042137739763474,47.82077290037826]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472201419214,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (39,{ts '2016-08-09 18:24:59.040000000'},'RV38','http://localhost:8380','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472201419214,47.82144663684135],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137739763474,47.82122205802032],[13.042137739763474,47.82167121566238],[13.042806663074952,47.82167121566238],[13.042806663074952,47.82122205802032],[13.042137739763474,47.82122205802032]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472201419214,47.82144663684135]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (49,{ts '2016-08-09 18:24:59.041000000'},'RV48','http://localhost:8480','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472201419214,47.82189579448341],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137739763474,47.82167121566238],[13.042137739763474,47.822120373304436],[13.042806663074952,47.822120373304436],[13.042806663074952,47.82167121566238],[13.042137739763474,47.82167121566238]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472201419214,47.82189579448341]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (59,{ts '2016-08-09 18:24:59.042000000'},'RV58','http://localhost:8580','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472201419214,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137739763474,47.822120373304436],[13.042137739763474,47.822569530946495],[13.042806663074952,47.822569530946495],[13.042806663074952,47.822120373304436],[13.042137739763474,47.822120373304436]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472201419214,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (69,{ts '2016-08-09 18:24:59.043000000'},'RV68','http://localhost:8680','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472201419214,47.82279410976753],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137739763474,47.822569530946495],[13.042137739763474,47.823018688588554],[13.042806663074952,47.823018688588554],[13.042806663074952,47.822569530946495],[13.042137739763474,47.822569530946495]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472201419214,47.82279410976753]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (79,{ts '2016-08-09 18:24:59.045000000'},'RV78','http://localhost:8780','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472201419214,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137739763474,47.823018688588554],[13.042137739763474,47.82346784623062],[13.042806663074952,47.82346784623062],[13.042806663074952,47.823018688588554],[13.042137739763474,47.823018688588554]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472201419214,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (89,{ts '2016-08-09 18:24:59.046000000'},'RV88','http://localhost:8880','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472201419214,47.82369242505165],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137739763474,47.82346784623062],[13.042137739763474,47.82391700387267],[13.042806663074952,47.82391700387267],[13.042806663074952,47.82346784623062],[13.042137739763474,47.82346784623062]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472201419214,47.82369242505165]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (99,{ts '2016-08-09 18:24:59.047000000'},'RV98','http://localhost:8980','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.042472201419214,47.82414158269371],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042137739763474,47.82391700387267],[13.042137739763474,47.82436616151474],[13.042806663074952,47.82436616151474],[13.042806663074952,47.82391700387267],[13.042137739763474,47.82391700387267]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.042472201419214,47.82414158269371]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (10,{ts '2016-08-09 18:24:59.049000000'},'RV09','http://localhost:8090','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043141124730694,47.82009916391517],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806663074952,47.81987458509414],[13.042806663074952,47.82032374273621],[13.043475586386432,47.82032374273621],[13.043475586386432,47.81987458509414],[13.042806663074952,47.81987458509414]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043141124730694,47.82009916391517]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (20,{ts '2016-08-09 18:24:59.050000000'},'RV19','http://localhost:8190','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043141124730694,47.82054832155723],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806663074952,47.82032374273621],[13.042806663074952,47.82077290037826],[13.043475586386432,47.82077290037826],[13.043475586386432,47.82032374273621],[13.042806663074952,47.82032374273621]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043141124730694,47.82054832155723]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (30,{ts '2016-08-09 18:24:59.051000000'},'RV29','http://localhost:8290','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043141124730694,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806663074952,47.82077290037826],[13.042806663074952,47.82122205802032],[13.043475586386432,47.82122205802032],[13.043475586386432,47.82077290037826],[13.042806663074952,47.82077290037826]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043141124730694,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (40,{ts '2016-08-09 18:24:59.053000000'},'RV39','http://localhost:8390','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043141124730694,47.82144663684135],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806663074952,47.82122205802032],[13.042806663074952,47.82167121566238],[13.043475586386432,47.82167121566238],[13.043475586386432,47.82122205802032],[13.042806663074952,47.82122205802032]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043141124730694,47.82144663684135]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (50,{ts '2016-08-09 18:24:59.054000000'},'RV49','http://localhost:8490','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043141124730694,47.82189579448341],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806663074952,47.82167121566238],[13.042806663074952,47.822120373304436],[13.043475586386432,47.822120373304436],[13.043475586386432,47.82167121566238],[13.042806663074952,47.82167121566238]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043141124730694,47.82189579448341]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (60,{ts '2016-08-09 18:24:59.055000000'},'RV59','http://localhost:8590','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043141124730694,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806663074952,47.822120373304436],[13.042806663074952,47.822569530946495],[13.043475586386432,47.822569530946495],[13.043475586386432,47.822120373304436],[13.042806663074952,47.822120373304436]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043141124730694,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (70,{ts '2016-08-09 18:24:59.056000000'},'RV69','http://localhost:8690','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043141124730694,47.82279410976753],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806663074952,47.822569530946495],[13.042806663074952,47.823018688588554],[13.043475586386432,47.823018688588554],[13.043475586386432,47.822569530946495],[13.042806663074952,47.822569530946495]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043141124730694,47.82279410976753]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (80,{ts '2016-08-09 18:24:59.057000000'},'RV79','http://localhost:8790','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043141124730694,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806663074952,47.823018688588554],[13.042806663074952,47.82346784623062],[13.043475586386432,47.82346784623062],[13.043475586386432,47.823018688588554],[13.042806663074952,47.823018688588554]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043141124730694,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (90,{ts '2016-08-09 18:24:59.059000000'},'RV89','http://localhost:8890','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043141124730694,47.82369242505165],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806663074952,47.82346784623062],[13.042806663074952,47.82391700387267],[13.043475586386432,47.82391700387267],[13.043475586386432,47.82346784623062],[13.042806663074952,47.82346784623062]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043141124730694,47.82369242505165]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (100,{ts '2016-08-09 18:24:59.061000000'},'RV99','http://localhost:8990','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043141124730694,47.82414158269371],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.042806663074952,47.82391700387267],[13.042806663074952,47.82436616151474],[13.043475586386432,47.82436616151474],[13.043475586386432,47.82391700387267],[13.042806663074952,47.82391700387267]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043141124730694,47.82414158269371]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (11,{ts '2016-08-09 18:24:59.062000000'},'RV10','http://localhost:8100','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043810048042173,47.82009916391517],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.043475586386432,47.81987458509414],[13.043475586386432,47.82032374273621],[13.044144509697912,47.82032374273621],[13.044144509697912,47.81987458509414],[13.043475586386432,47.81987458509414]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043810048042173,47.82009916391517]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (21,{ts '2016-08-09 18:24:59.063000000'},'RV20','http://localhost:8200','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043810048042173,47.82054832155723],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.043475586386432,47.82032374273621],[13.043475586386432,47.82077290037826],[13.044144509697912,47.82077290037826],[13.044144509697912,47.82032374273621],[13.043475586386432,47.82032374273621]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043810048042173,47.82054832155723]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (31,{ts '2016-08-09 18:24:59.065000000'},'RV30','http://localhost:8300','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043810048042173,47.82099747919929],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.043475586386432,47.82077290037826],[13.043475586386432,47.82122205802032],[13.044144509697912,47.82122205802032],[13.044144509697912,47.82077290037826],[13.043475586386432,47.82077290037826]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043810048042173,47.82099747919929]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (41,{ts '2016-08-09 18:24:59.066000000'},'RV40','http://localhost:8400','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043810048042173,47.82144663684135],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.043475586386432,47.82122205802032],[13.043475586386432,47.82167121566238],[13.044144509697912,47.82167121566238],[13.044144509697912,47.82122205802032],[13.043475586386432,47.82122205802032]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043810048042173,47.82144663684135]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (51,{ts '2016-08-09 18:24:59.067000000'},'RV50','http://localhost:8500','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043810048042173,47.82189579448341],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.043475586386432,47.82167121566238],[13.043475586386432,47.822120373304436],[13.044144509697912,47.822120373304436],[13.044144509697912,47.82167121566238],[13.043475586386432,47.82167121566238]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043810048042173,47.82189579448341]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (61,{ts '2016-08-09 18:24:59.068000000'},'RV60','http://localhost:8600','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043810048042173,47.822344952125476],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.043475586386432,47.822120373304436],[13.043475586386432,47.822569530946495],[13.044144509697912,47.822569530946495],[13.044144509697912,47.822120373304436],[13.043475586386432,47.822120373304436]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043810048042173,47.822344952125476]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (71,{ts '2016-08-09 18:24:59.070000000'},'RV70','http://localhost:8700','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043810048042173,47.82279410976753],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.043475586386432,47.822569530946495],[13.043475586386432,47.823018688588554],[13.044144509697912,47.823018688588554],[13.044144509697912,47.822569530946495],[13.043475586386432,47.822569530946495]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043810048042173,47.82279410976753]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (81,{ts '2016-08-09 18:24:59.071000000'},'RV80','http://localhost:8800','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043810048042173,47.823243267409595],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.043475586386432,47.823018688588554],[13.043475586386432,47.82346784623062],[13.044144509697912,47.82346784623062],[13.044144509697912,47.823018688588554],[13.043475586386432,47.823018688588554]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043810048042173,47.823243267409595]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (91,{ts '2016-08-09 18:24:59.072000000'},'RV90','http://localhost:8900','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043810048042173,47.82369242505165],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.043475586386432,47.82346784623062],[13.043475586386432,47.82391700387267],[13.044144509697912,47.82391700387267],[13.044144509697912,47.82346784623062],[13.043475586386432,47.82346784623062]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043810048042173,47.82369242505165]}}]}',0);
INSERT INTO REAL_VEHICLES (ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)
VALUES (101,{ts '2016-08-09 18:24:59.073000000'},'RV100','http://localhost:9000','QUADROCOPTER','{"type":"FeatureCollection","properties":{"center":[13.043810048042173,47.82414158269371],"zoom":18,"layer":"No map"},"features":[{"type":"Feature","properties":{"maxAlt":60.0,"minAlt":0.0},"geometry":{"type":"Polygon","coordinates":[[[13.043475586386432,47.82391700387267],[13.043475586386432,47.82436616151474],[13.044144509697912,47.82436616151474],[13.044144509697912,47.82391700387267],[13.043475586386432,47.82391700387267]]]}},{"type":"Feature","properties":{"type":"depot"},"geometry":{"type":"Point","coordinates":[13.043810048042173,47.82414158269371]}}]}',0);

INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 1, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 2, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 2, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 2, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (12, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (12, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (12, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (22, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (22, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (22, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (32, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (32, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (32, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (42, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (42, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (42, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (52, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (52, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (52, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (62, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (62, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (62, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (72, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (72, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (72, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (82, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (82, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (82, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (92, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (92, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (92, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 3, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 3, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 3, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (13, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (13, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (13, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (23, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (23, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (23, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (33, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (33, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (33, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (43, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (43, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (43, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (53, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (53, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (53, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (63, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (63, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (63, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (73, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (73, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (73, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (83, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (83, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (83, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (93, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (93, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (93, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 4, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 4, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 4, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (14, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (14, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (14, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (24, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (24, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (24, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (34, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (34, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (34, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (44, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (44, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (44, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (54, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (54, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (54, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (64, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (64, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (64, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (74, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (74, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (74, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (84, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (84, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (84, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (94, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (94, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (94, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 5, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 5, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 5, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (15, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (15, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (15, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (25, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (25, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (25, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (35, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (35, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (35, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (45, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (45, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (45, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (55, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (55, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (55, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (65, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (65, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (65, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (75, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (75, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (75, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (85, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (85, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (85, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (95, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (95, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (95, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 6, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 6, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 6, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (16, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (16, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (16, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (26, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (26, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (26, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (36, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (36, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (36, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (46, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (46, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (46, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (56, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (56, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (56, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (66, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (66, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (66, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (76, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (76, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (76, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (86, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (86, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (86, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (96, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (96, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (96, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 7, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 7, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 7, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (17, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (17, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (17, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (27, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (27, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (27, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (37, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (37, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (37, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (47, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (47, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (47, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (57, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (57, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (57, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (67, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (67, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (67, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (77, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (77, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (77, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (87, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (87, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (87, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (97, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (97, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (97, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 8, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 8, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 8, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (18, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (18, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (18, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (28, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (28, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (28, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (38, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (38, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (38, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (48, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (48, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (48, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (58, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (58, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (58, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (68, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (68, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (68, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (78, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (78, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (78, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (88, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (88, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (88, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (98, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (98, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (98, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 9, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 9, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES ( 9, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (19, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (19, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (19, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (29, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (29, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (29, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (39, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (39, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (39, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (49, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (49, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (49, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (59, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (59, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (59, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (69, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (69, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (69, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (79, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (79, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (79, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (89, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (89, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (89, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (99, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (99, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (99, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (10, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (10, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (10, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (20, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (20, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (20, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (30, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (30, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (30, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (40, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (40, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (40, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (50, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (50, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (50, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (60, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (60, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (60, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (70, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (70, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (70, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (80, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (80, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (80, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (90, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (90, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (90, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (100, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (100, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (100, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (11, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (11, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (11, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (21, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (21, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (21, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (31, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (31, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (31, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (41, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (41, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (41, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (51, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (51, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (51, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (61, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (61, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (61, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (71, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (71, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (71, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (81, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (81, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (81, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (91, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (91, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (91, 9);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (101, 1);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (101, 5);
INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS (REAL_VEHICLES_ID,SENSORS_ID) VALUES (101, 9);

