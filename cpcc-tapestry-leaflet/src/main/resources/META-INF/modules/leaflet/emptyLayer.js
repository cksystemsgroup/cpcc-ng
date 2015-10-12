define([ "jquery", "leaflet", "leaflet/data", "leaflet/map" ],

function($, leaflet, data)
{
	return function(myId, mapId, name)
	{
		var layer = leaflet.layerGroup();
		data[mapId].map.addLayer(layer);
		data[mapId].baseMaps.push({
			name : name,
			layer : layer
		});
	}
});