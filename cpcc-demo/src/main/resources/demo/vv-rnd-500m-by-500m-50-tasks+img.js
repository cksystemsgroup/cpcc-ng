var minLat = 47.81987458509414;
var maxLat = 47.82436616151474;
var minLng = 13.037455276583115;
var maxLng = 13.044144509697912;

var gps = VV.sensor.get('GPS');
var camera = VV.sensor.get('Belly Mounted Camera 640x480');

var numberOfTasks = 50;

function storePosition(sensorData)
{
	VV.storage.store("pos-" + k, sensorData[0]);
	VV.storage.store("img-" + k, sensorData[1]);
}

for (var k = 0; k < numberOfTasks; ++k)
{
	var lat = minLat + (maxLat - minLat) * Math.random();
	var lng = minLng + (maxLng - minLng) * Math.random();

	VV.task.execute({
		type : 'point',
		position : new VV.types.LatLngAlt(lat, lng, 50),
		tolerance : 4,
		sensors : [ gps, camera ]
	}, storePosition);
}
