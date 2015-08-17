
INSERT INTO TOPIC (ID,MESSAGETYPE,NODETYPE,SUBPATH,ADAPTERCLASSNAME,CATEGORY) VALUES (1,'sensor_msgs/Image','PUBLISHER','image','at.uni_salzburg.cs.cpcc.ros.sensors.ImageSensorAdapter','CAMERA');
INSERT INTO TOPIC (ID,MESSAGETYPE,NODETYPE,SUBPATH,ADAPTERCLASSNAME,CATEGORY) VALUES (2,'sensor_msgs/CameraInfo','PUBLISHER','camera_info','at.uni_salzburg.cs.cpcc.ros.sensors.CameraInfoSensorAdapter','CAMERA_INFO');
INSERT INTO TOPIC (ID,MESSAGETYPE,NODETYPE,SUBPATH,ADAPTERCLASSNAME,CATEGORY) VALUES (3,'sensor_msgs/NavSatFix','PUBLISHER',null,'at.uni_salzburg.cs.cpcc.ros.sensors.GpsSensorAdapter','GPS_POSITION_PROVIDER');
INSERT INTO TOPIC (ID,MESSAGETYPE,NODETYPE,SUBPATH,ADAPTERCLASSNAME,CATEGORY) VALUES (4,'big_actor_msgs/LatLngAlt','SUBSCRIBER',null,'at.uni_salzburg.cs.cpcc.ros.actuators.SimpleWayPointControllerAdapter','WAYPOINT_CONTROLLER');
INSERT INTO TOPIC (ID,MESSAGETYPE,NODETYPE,SUBPATH,ADAPTERCLASSNAME,CATEGORY) VALUES (5,'sensor_msgs/NavSatFix','PUBLISHER','gps','at.uni_salzburg.cs.cpcc.ros.sensors.GpsSensorAdapter','GPS_POSITION_PROVIDER');
INSERT INTO TOPIC (ID,MESSAGETYPE,NODETYPE,SUBPATH,ADAPTERCLASSNAME,CATEGORY) VALUES (6,'big_actor_msgs/LatLngAlt','SUBSCRIBER','waypoint','at.uni_salzburg.cs.cpcc.ros.actuators.SimpleWayPointControllerAdapter','WAYPOINT_CONTROLLER');
INSERT INTO TOPIC (ID,MESSAGETYPE,NODETYPE,SUBPATH,ADAPTERCLASSNAME,CATEGORY) VALUES (7,'std_msgs/Float32','PUBLISHER','sonar','at.uni_salzburg.cs.cpcc.ros.sensors.AltimeterAdapter','ALTITUDE_OVER_GROUND');
INSERT INTO TOPIC (ID,MESSAGETYPE,NODETYPE,SUBPATH,ADAPTERCLASSNAME,CATEGORY) VALUES (8,'big_actor_msgs/LatLngAlt','SUBSCRIBER',null,'at.uni_salzburg.cs.cpcc.ros.actuators.MorseWayPointControllerAdapter','WAYPOINT_CONTROLLER');
INSERT INTO TOPIC (ID,MESSAGETYPE,NODETYPE,SUBPATH,ADAPTERCLASSNAME,CATEGORY) VALUES (9,'sensor_msgs/NavSatFix','PUBLISHER',null,'at.uni_salzburg.cs.cpcc.ros.sensors.MorseGpsSensorAdapter','GPS_POSITION_PROVIDER');
INSERT INTO TOPIC (ID,MESSAGETYPE,NODETYPE,SUBPATH,ADAPTERCLASSNAME,CATEGORY) VALUES (10,'std_msgs/Float32','PUBLISHER',null,'at.uni_salzburg.cs.cpcc.ros.sensors.AltimeterAdapter','ALTITUDE_OVER_GROUND');
INSERT INTO TOPIC (ID,MESSAGETYPE,NODETYPE,SUBPATH,ADAPTERCLASSNAME,CATEGORY) VALUES (11,'sensor_msgs/Image','PUBLISHER','image_raw','at.uni_salzburg.cs.cpcc.ros.sensors.ImageSensorAdapter','CAMERA');

INSERT INTO DEVICETYPE (ID,CLASSNAME,NAME,MAINTOPIC_ID) VALUES (1,'at.uni_salzburg.cs.cpcc.ros.sim.osm.NodeGroup','Simulated Belly Mounted Camera',1);
INSERT INTO DEVICETYPE (ID,CLASSNAME,NAME,MAINTOPIC_ID) VALUES (2,null,'GPS Receiver',3);
INSERT INTO DEVICETYPE (ID,CLASSNAME,NAME,MAINTOPIC_ID) VALUES (3,null,'Simple Waypoint Controller',4);
INSERT INTO DEVICETYPE (ID,CLASSNAME,NAME,MAINTOPIC_ID) VALUES (4,'at.uni_salzburg.cs.cpcc.ros.sim.quadrotor.NodeGroup','Simulated Quadrotor',6);
INSERT INTO DEVICETYPE (ID,CLASSNAME,NAME,MAINTOPIC_ID) VALUES (5,null,'Generic Camera',1);
INSERT INTO DEVICETYPE (ID,CLASSNAME,NAME,MAINTOPIC_ID) VALUES (6,null,'MORSE Waypoint Controller',8);
INSERT INTO DEVICETYPE (ID,CLASSNAME,NAME,MAINTOPIC_ID) VALUES (7,null,'MORSE GPS Receiver',9);
INSERT INTO DEVICETYPE (ID,CLASSNAME,NAME,MAINTOPIC_ID) VALUES (8,'at.uni_salzburg.cs.cpcc.ros.sim.SonarEmulator','Sonar Emulator',10);
INSERT INTO DEVICETYPE (ID,CLASSNAME,NAME,MAINTOPIC_ID) VALUES (9,null,'Generic RAW Camera',11);

INSERT INTO DEVICETYPE_TOPIC (DEVICETYPE_ID,SUBTOPICS_ID) VALUES (1,2);
INSERT INTO DEVICETYPE_TOPIC (DEVICETYPE_ID,SUBTOPICS_ID) VALUES (4,5);
INSERT INTO DEVICETYPE_TOPIC (DEVICETYPE_ID,SUBTOPICS_ID) VALUES (4,7);
INSERT INTO DEVICETYPE_TOPIC (DEVICETYPE_ID,SUBTOPICS_ID) VALUES (5,2);
INSERT INTO DEVICETYPE_TOPIC (DEVICETYPE_ID,SUBTOPICS_ID) VALUES (9,2);
