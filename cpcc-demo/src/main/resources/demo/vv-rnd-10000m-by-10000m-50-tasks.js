var minLat = 47.777207;
var maxLat = 47.867029;
var minLng = 12.973908;
var maxLng = 13.107567;

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
