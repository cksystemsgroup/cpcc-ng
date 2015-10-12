define([ "leaflet" ],

function(leaflet)
{
	var data = {};

	data.toLatLngArray = function(points)
	{
		var latLngs = new Array();

		for (var k = 0, l = points.length; k < l; ++k)
		{
			latLngs.push(new leaflet.LatLng(points[k].lat, points[k].lon))
		}

		return latLngs;
	}

	return data;
});