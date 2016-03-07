var gps = VV.sensor.get('GPS');
var radius = 0.866 * 50.0;
var centers = [ [ 47.82289830824204, 13.03879318678928 ], [ 47.82367624317964, 13.040800077929768 ],
		[ 47.82289830824204, 13.042806928969572 ], [ 47.82134243836684, 13.042806888871423 ],
		[ 47.820564503429246, 13.040799997733473 ], [ 47.82134243836684, 13.03879314669113 ],
		[ 47.822120373304436, 13.040800037829925 ] ];

var anzahl = 10;
var minLat = 47.820568;
var maxLat = 47.823673;
var minLng = 13.038132;
var maxLng = 13.043443;

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
