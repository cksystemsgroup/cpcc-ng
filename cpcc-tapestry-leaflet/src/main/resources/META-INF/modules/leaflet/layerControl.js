define([ "jquery", "leaflet", "leaflet/data", "leaflet/emptyLayer", "leaflet/tileLayer", "leaflet/regionLayer",
		"leaflet/vehicleLayer", "leaflet/vehiclePathLayer" ],

function($, leaflet, data)
{
	return function(myId, mapId)
	{
		var d = data[mapId];
		var baseMaps = getMapsFromSpec(d.baseMaps);
		var overlayMaps = getMapsFromSpec(d.overlayMaps);

		d.layerControl = leaflet.control.layers(baseMaps, overlayMaps)
		d.layerControl.addTo(d.map);

		function getMapsFromSpec(specs)
		{
			var m = {};
			for (var k = 0; k < specs.length; ++k)
			{
				m[specs[k].name] = specs[k].layer;
			}
			return m;
		}
	}
});