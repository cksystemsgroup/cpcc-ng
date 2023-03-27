var minLat = 47.82167121566238;
var maxLat = 47.822569530946495;
var minLng = 13.040131085580562;
var maxLng = 13.041468978503568;

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
