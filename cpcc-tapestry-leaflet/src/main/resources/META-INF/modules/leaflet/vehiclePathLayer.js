define([ "jquery", "leaflet", "leaflet/data", "t5/core/console", "leaflet/map", "leaflet/vehicles",
		"leaflet/vehicleLayer" ],

function($, leaflet, data, console, map, v, vl)
{
	var module = {};

	module.baseUrl = 'images';

	module.onPathChange = function(e, overlay)
	{
		// console.info('pathChange: start ' + JSON.stringify(e.layer.cpccFeatures));

		if (overlay.paths[e.layer.cpccRvId])
		{
			overlay.layer.removeLayer(overlay.paths[e.layer.cpccRvId]);
		}

		overlay.paths[e.layer.cpccRvId] = leaflet.geoJson(e.layer.cpccFeatures, {
			style : function(feature)
			{
				return {
					color : "red"
				};
			},
		}).addTo(overlay.layer);
	}

	module.initialize = function(myId, mapId, name, baseUrl)
	{
		module.baseUrl = baseUrl;
		leaflet.Icon.Default.imagePath = baseUrl;

		var vehiclePathLayer = leaflet.layerGroup();
		vehiclePathLayer.cpccType = 'path';
		vehiclePathLayer.cpccId = myId;
		vehiclePathLayer.cpccMapId = mapId;

		data[mapId].overlays[myId] = {
			layer : vehiclePathLayer,
			paths : {}
		};

		data[mapId].map.addLayer(vehiclePathLayer);

		data[mapId].overlayMaps.push({
			name : name,
			layer : vehiclePathLayer
		});

		for (var k = 0; k < data[mapId].overlayMaps.length; ++k)
		{
			var o = data[mapId].overlayMaps[k].layer;
			if (o.cpccType === 'vehicles')
			{
				o.on('rvPathChange', function(e)
				{
					module.onPathChange(e, data[mapId].overlays[myId]);
				});
			}
		}
	}

	return module;
});