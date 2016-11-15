var minLat = 47.82167121566238;
var maxLat = 47.822569530946495;
var minLng = 13.040131085580562;
var maxLng = 13.041468978503568;

var gps = VV.sensor.get('GPS');

var numberOfTasks = 1250;

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
