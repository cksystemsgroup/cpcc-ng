var minLat = 47.82054832155723;
var maxLat = 47.82369242505165;
var minLng = 13.03845870495716;
var maxLng = 13.043141228904474;

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
