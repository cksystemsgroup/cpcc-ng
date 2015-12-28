define([ "jquery", "leaflet", "leaflet/data", "leaflet/vehicles", "t5/core/zone", "t5/core/console", "leaflet/map" ],

function($, leaflet, data, vehicles, zoneManager, console, lMap)
{
	var module = {};

	module.baseUrl = 'images';

	module.onPositionChange = function(e, overlay)
	{
		var vehicleId = e.layer.cpccRvId;
		var vehicleMarker = overlay.vehicles[vehicleId];
		var props = e.layer.cpccFeatures.properties;
		var pos = [ props.rvPosition.coordinates[1], props.rvPosition.coordinates[0] ];

		if (vehicleMarker)
		{
			vehicleMarker.setLatLng(pos);
		}
		else
		{
			vehicleMarker = vehicles.createVehicle(props.rvType, pos);
			vehicleMarker.options.baseUrl = module.baseUrl;
			overlay.layer.addLayer(vehicleMarker);
			overlay.vehicles[vehicleId] = vehicleMarker;
		}

		vehicleMarker.setVehicleState(vehicleId, props.rvState, props.rvHeading, props.rvName);
	}

	module.onVirtualVehicleChange = function(e, overlay)
	{
		var vehicleId = e.layer.cpccRvId;
		var vehicleMarker = overlay.vehicles[vehicleId];

		vehicleMarker.setVvState(e.layer.cpccFeatures);
	}

	module.initialize = function(myId, mapId, name, zoneElementId, eventURL, frequencySecs, baseUrl)
	{
		module.baseUrl = baseUrl;
		leaflet.Icon.Default.imagePath = baseUrl;

		var vehicleLayer = leaflet.featureGroup();
		vehicleLayer.cpccType = 'vehicles';

		data[mapId].overlays[myId] = {
			layer : vehicleLayer,
			vehicles : {},
			vehicleTimeStamps : {}
		};

		data[mapId].map.addLayer(vehicleLayer);

		data[mapId].overlayMaps.push({
			name : name,
			layer : vehicleLayer
		});

		vehicleLayer.on('rvPositionChange', function(e)
		{
			module.onPositionChange(e, data[mapId].overlays[myId]);
		});

		vehicleLayer.on('vvsChange', function(e)
		{
			module.onVirtualVehicleChange(e, data[mapId].overlays[myId]);
		});

		var frequencyMillis = frequencySecs * 1000;
		var interval = setInterval(updateZone, frequencyMillis);

		function updateZone()
		{
			zoneManager.deferredZoneUpdate(zoneElementId, eventURL);
		}
	}

	module.updateVehicleData = function(myId, mapId)
	{
		var newVehicles = $('#' + myId).data('vehicles');
		var overlay = data[mapId].overlays[myId];

		for ( var vehicleId in newVehicles)
		{
			var vehicle = newVehicles[vehicleId];
			// console.info('updateVehicleData: ' + JSON.stringify(vehicle));
			
			vehicle.features.forEach(function(element, index, array)
			{
				overlay.layer.fire(element.properties.type + 'Change', {
					layer : {
						cpccType : element.properties.type,
						cpccMapId : mapId,
						cpccFeatures : element,
						cpccOverlay : overlay,
						cpccRv : vehicle,
						cpccRvId : vehicleId,
					}
				});
			});
		}
	}

	return module;

});