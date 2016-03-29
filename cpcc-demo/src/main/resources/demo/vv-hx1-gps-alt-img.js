var zones = [
		[ [ 13.037455272729183, 47.82289830824203 ], [ 13.038124229759232, 47.82367624317964 ],
				[ 13.039462143819328, 47.82367624317964 ], [ 13.040131100849377, 47.82289830824204 ],
				[ 13.039462143819328, 47.822120373304436 ], [ 13.038124229759232, 47.822120373304436 ],
				[ 13.037455272729183, 47.82289830824203 ] ] ];

var diamonds = [];

for (var i = 0; i < zones.length; ++i)
{
	var z = zones[i];
	for (var k = 0; k < z.length - 1; k += 2)
	{
		diamonds.push([ [ z[k], z[k + 1] ], [ z[k + 1], z[k + 2] ] ]);
	}
}

var gps = VV.sensor.get('GPS');
var altimeter = VV.sensor.get('Altimeter');
var camera = VV.sensor.get('Belly Mounted Camera 640x480');
		
var anzahl = 50;

var Vec = function(a)
{
	this.lng = a[0];
	this.lat = a[1];
};

Vec.prototype.sub = function(a)
{
	this.lng -= a[0];
	this.lat -= a[1];
	return this;
};

Vec.prototype.add = function(a)
{
	this.lng += a[0];
	this.lat += a[1];
	return this;
};

Vec.prototype.mul = function(s)
{
	this.lng *= s;
	this.lat *= s;
	return this;
};

function storePosition(sensorData)
{
	VV.storage.store("pos-" + k, sensorData[0]);
	VV.storage.store("alt-" + k, sensorData[1]);
	VV.storage.store("img-" + k, sensorData[2]);
}

for (var k = 0; k < anzahl; ++k)
{
	var vs = diamonds[Math.floor(diamonds.length * Math.random())];
	var v1 = new Vec(vs[0][1]).sub(vs[0][0]).mul(Math.random());
	var v2 = new Vec(vs[1][1]).sub(vs[1][0]).mul(Math.random());
	var r = v1.add(vs[0][0]).add([ v2.lng, v2.lat ]);

	VV.task.execute({
		type : 'point',
		position : new VV.types.LatLngAlt(r.lat, r.lng, 50),
		tolerance : 4,
		sensors : [ gps, altimeter, camera ]
	}, storePosition);
}