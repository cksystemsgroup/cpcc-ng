var minLat = 47.820323742736214;
var maxLat = 47.823917003872694;
var minLng = 13.038124228832476;
var maxLng = 13.043475661622667;

var gps = VV.sensor.get('GPS');

var numberOfTasks = 50;

function storePosition(sensorData)
{
	VV.storage.store("pos-" + k, sensorData[0]);
}

for (var k = 0; k < numberOfTasks; ++k)
{
	var lat = minLat + (maxLat - minLat) * Math.random();
	var lng = minLng + (maxLng - minLng) * Math.random();

	VV.task.execute({
		type : 'point',
		position : new VV.types.LatLngAlt(lat, lng, 50),
		tolerance : 4,
		sensors : [ gps ]
	}, storePosition);
}
