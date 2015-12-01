define([ "jquery", "leaflet", "leaflet/data", "leaflet/map" ],

function($, leaflet, data)
{
	return function(myId, mapId, name, urlTemplate, options, cpccId)
	{
		var layer = leaflet.tileLayer(urlTemplate, options);
		layer.cpccType = 'tiles';

		data[mapId].map.addLayer(layer);

		data[mapId].baseMaps.push({
			name : name,
			layer : layer
		});
	}
});