
getStdOut().println("bugger 1.");

var sensors = VV.sensor.list();
var barometer = VV.sensor.get('barometer');
var thermometer = VV.sensor.get('thermometer');
var camera = VV.sensor.get('belly-mounted-camera');

var pos = new VV.types.LatLngAlt(37.87459, -122.25883, 50);
getStdOut().println("pos=" + JSON.stringify(pos));

VV.task.execute(
{
    type: 'point',
    position: new VV.types.LatLngAlt(37.87459, -122.25883, 50),
    tolerance: 10,
    sensors: [barometer , thermometer , camera] 
},
function(sensorData)
{
    getStdOut().println("callback called." + JSON.stringify(sensorData));
}
);

var camera2 = VV.sensor.get('belly-mounted-camera');
var pos2 = new VV.types.LatLngAlt(37.87459, -122.25883, 50);

getStdOut().println("pos=" + JSON.stringify(pos));

getStdOut().println("pos2=" + JSON.stringify(pos2));
