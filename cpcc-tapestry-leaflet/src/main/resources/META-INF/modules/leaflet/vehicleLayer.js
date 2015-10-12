define([ "jquery", "leaflet", "leaflet/data", "leaflet/vehicles", "t5/core/zone", "t5/core/console", "leaflet/map" ],

function($, leaflet, data, vehicles, zoneManager, console)
{
	var module = {};

	module.baseUrl = 'images';

	module.initialize = function(myId, mapId, name, zoneElementId, eventURL, frequencySecs, baseUrl)
	{
		module.baseUrl = baseUrl;
		leaflet.Icon.Default.imagePath = baseUrl;

		var vehicleLayer = leaflet.layerGroup();

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
		var vehicleId;

		// alert('Buggerit: ' + JSON.stringify(newVehicles));

		/*
		 * for (vehicleId in newVehicles) { var vehicle =
		 * newVehicles[vehicleId]; var vehicleMarker =
		 * overlay.vehicles[vehicleId];
		 * 
		 * if (vehicleMarker) { vehicleMarker.setLatLng(vehicle.pos); } else {
		 * vehicleMarker = vehicles.createVehicle(vehicle.type, vehicle.pos);
		 * vehicleMarker.options.baseUrl = module.baseUrl;
		 * overlay.layer.addLayer(vehicleMarker); overlay.vehicles[vehicleId] =
		 * vehicleMarker; }
		 * 
		 * vehicleMarker.setVehicleState(vehicleId, vehicle.state,
		 * vehicle.heading, vehicle.name); // vehicleMarker.setTaskState('01',
		 * 'none'); vehicleMarker.setTaskState(vehicle.heading, 'inProgress'); }
		 */

		for (vehicleId in newVehicles)
		{
			var vehicle = newVehicles[vehicleId];
			var vehicleMarker = overlay.vehicles[vehicleId];

			if (!vehicle["features"])
			{
				continue;
			}

			if (!vehicle.features[0] || !vehicle.features[0].properties)
			{
				continue;
			}

			var props = vehicle.features[0].properties;
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
			vehicleMarker.setVvState(vehicle.features);

			console.info("Vehicle " + props.rvName + "  " + JSON.stringify(pos));
		}
	}

	return module;

});