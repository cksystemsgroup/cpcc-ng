
INSERT INTO DEVICES (ID,CONFIGURATION,TOPIC_ROOT,TYPE_ID)
VALUES (9,'gps=''/quad05/gps'' osmZoomLevel=19 osmTileServerUrl=''http://a.tile.osm.org/%1$d/%2$d/%3$d.png''','/quad05/camera',1);

INSERT INTO MAPPING_ATTRIBUTES (CONNECTED_TO_AUTOPILOT,VV_VISIBLE,TOPIC_ID,DEVICE_ID,SENSORDEFINITION_ID) VALUES (0,0,2,9,null);
INSERT INTO MAPPING_ATTRIBUTES (CONNECTED_TO_AUTOPILOT,VV_VISIBLE,TOPIC_ID,DEVICE_ID,SENSORDEFINITION_ID) VALUES (0,1,1,9,5);