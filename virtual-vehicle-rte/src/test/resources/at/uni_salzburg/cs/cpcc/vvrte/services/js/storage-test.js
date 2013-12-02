
getStdOut().println("storage test start");

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
    for (var k=0, l=sensorData.length; k < l; ++k)
    {
        VV.storage.store("x"+k, sensorData[k]);
        VV.storage.store("y"+k, sensorData[k]);
        VV.storage.remove("x"+k);
    }
}
);

var l = VV.storage.list(".*");

getStdOut().println("storage=" + l);

var y0 = VV.storage.load("y0");
getStdOut().println("y0=" + JSON.stringify(y0));

var y1 = VV.storage.load("y1");
getStdOut().println("y1=" + JSON.stringify(y1));

var y2 = VV.storage.load("y2");
getStdOut().println("y2=" + JSON.stringify(y2));

getStdOut().println("storage test end.");