define([ "jquery", "leaflet", "leaflet/data", "t5/core/zone", "t5/core/console", "leaflet/map" ],

function($, leaflet, data, zoneManager, console)
{
	var module = {};

	module.initialize = function(myId, mapId, name, zoneElementId, eventURL, frequencySecs)
	{
		var regionLayer = leaflet.layerGroup();

		data[mapId].overlays[myId] = {
			layer : regionLayer,
			regions : {},
			regionTimeStamps : {}
		};

		data[mapId].map.addLayer(regionLayer);

		data[mapId].overlayMaps.push({
			name : name,
			layer : regionLayer
		});

		var frequencyMillis = frequencySecs * 1000;
		var interval = setInterval(updateZone, frequencyMillis);

		function updateZone()
		{
			zoneManager.deferredZoneUpdate(zoneElementId, eventURL);
		}
	}

	module.updateRegionData = function(myId, mapId)
	{
		var newRegions = $('#' + myId).data('regions');
		var overlay = data[mapId].overlays[myId];
		var regionId;

		for (regionId in newRegions)
		{
			var region = newRegions[regionId];
			var oldRegion = overlay.regions[regionId];

			if (oldRegion && region.properties && overlay.regionTimeStamps[regionId] >= region.properties['timeStamp'])
			{
				continue;
			}

			var regionLayer = leaflet.geoJson(null, {
				style : function(feature)
				{
					return {
						color : feature.properties['color'] ? feature.properties.color : '#ffff00',
						opacity : feature.properties['opacity'] ? feature.properties.opacity : 0.15,
						fillColor : feature.properties['fillColor'] ? feature.properties.fillColor : '#ffff00',
						fillOpacity : feature.properties['fillOpacity'] ? feature.properties.fillOpacity : 0.10,
					};
				},
				onEachFeature : function(feature, layer)
				{
					// layer.bindPopup(feature.properties.description);
				},
				filter : function(feature, layer)
				{
					return !(feature.properties['type'] === 'depot');
				},
			});

			regionLayer.addData(region);

			if (oldRegion)
			{
				overlay.layer.removeLayer(oldRegion);
			}

			overlay.regions[regionId] = regionLayer;
			if (region.properties)
			{
				overlay.regionTimeStamps[regionId] = region.properties['timeStamp'];
			}

			regionLayer.cpccType = 'regions';

			overlay.layer.addLayer(regionLayer);
		}
	}

	return module;

});