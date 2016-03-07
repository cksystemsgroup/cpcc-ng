var gps = VV.sensor.get('GPS');

var anzahl = 10;
var minLat = 47.822126;
var maxLat = 47.823673;
var minLng = 13.038132;
var maxLng = 13.039460;

function storePosition(sensorData)
{
	VV.storage.store("pos-" + k, sensorData[0]);
}

for (var k = 0; k < anzahl; ++k)
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