var minLat = 47.82189579448341;
var maxLat = 47.822344952125476;
var minLng = 13.040465561705245;
var maxLng = 13.04113451106066;

var gps = VV.sensor.get('GPS');
var camera = VV.sensor.get('Belly Mounted Camera 640x480');

var numberOfTasks = 50;
var storageIndex = 0;

function storePosition(sensorData)
{
	VV.storage.store("pos-" + storageIndex, sensorData[0]);
	VV.storage.store("img-" + storageIndex, sensorData[1]);
	++storageIndex;
}

for (var k = 0; k < numberOfTasks; ++k)
{
	var lat = minLat + (maxLat - minLat) * Math.random();
	var lng = minLng + (maxLng - minLng) * Math.random();

	VV.task.execute({
		type : 'point',
		position : new VV.types.LatLngAlt(lat, lng, 50),
		tolerance : 1,
		sensors : [ gps, camera ]
	}, storePosition);
}
