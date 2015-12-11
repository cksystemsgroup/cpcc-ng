define([ "jquery", "leaflet", "leaflet/data", "t5/core/console", "leaflet/map", "leaflet/vehicles",
		"leaflet/vehicleLayer" ],

function($, leaflet, data, console, map, v, vl)
{
	var module = {};

	module.baseUrl = 'images';

	module.onSensorsChange = function(e, overlay)
	{
//		cpccType : element.properties.type,
//		cpccMapId : mapId,
//		cpccFeatures : element,
//		cpccOverlay : overlay,
//		cpccRv : vehicle,
//		cpccRvId : vehicleId,
		
		
		console.info('sensorsChange: id='
				+ e.layer.cpccRvId + ' '
				+ JSON.stringify(e.layer.cpccFeatures));

		
		
		
//		if (overlay.sensors[e.layer.cpccRvId])
//		{
//			overlay.layer.removeLayer(overlay.sensors[e.layer.cpccRvId]);
//		}
//
//		overlay.sensors[e.layer.cpccRvId] = leaflet.geoJson(e.layer.cpccFeatures, {
//			style : function(feature)
//			{
//				return {
//					color : "red"
//				};
//			},
//		}).addTo(overlay.layer);
		
		
	}

	module.initialize = function(myId, mapId, name, baseUrl)
	{
		module.baseUrl = baseUrl;
		leaflet.Icon.Default.imagePath = baseUrl;

		var vehiclePathLayer = leaflet.layerGroup();
		vehiclePathLayer.cpccType = 'sensors';
		vehiclePathLayer.cpccId = myId;
		vehiclePathLayer.cpccMapId = mapId;

		data[mapId].overlays[myId] = {
			layer : vehiclePathLayer,
			sensors : {}
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
				o.on('sensorsChange', function(e)
				{
					module.onSensorsChange(e, data[mapId].overlays[myId]);
				});
			}
		}
	}

	return module;
});