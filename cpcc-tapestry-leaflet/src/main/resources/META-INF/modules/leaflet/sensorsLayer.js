define([ "jquery", "leaflet", "leaflet/data", "leaflet/map", "leaflet/vehicles", "leaflet/vehicleLayer" ],

		function($, leaflet, data, map, v, vl)
		{
			var module = {};

			module.baseUrl = 'images';

			module.SensorIcon = leaflet.Icon
					.extend({
						options : {
							shadowUrl : null,
							iconSize : [ 16, 16 ],
							iconAnchor : [ 8, 40 ],
							popupAnchor : [ 1, -16 ],
							className : 'sensor-marker',
						},

						createIcon : function(oldIcon)
						{
							var sensorDiv = (oldIcon && oldIcon.tagName === 'DIV') ? oldIcon : document
									.createElement('div'), options = this.options;

							var n = this.options.sensors.length;

							if (this.options.sensors.length > 0)
							{
								var html = '<table class="sensor-table"><tr>';

								for (var i = 0; i < n; ++i)
								{
									html = html + '<td class="sensor-info"><img src="' + module.baseUrl + '/markers/'
											+ this.options.sensors[i] + '.png" /></td>';
								}

								html = html + '</tr></table>';
								html = html + '<img src="' + module.baseUrl + '/markers/arrow_down.png" />';
								sensorDiv.innerHTML = html;
							}
							else
							{
								sensorDiv.innerHTML = '';
							}

							var div = document.createElement('div');
							div.appendChild(sensorDiv);

							this._setIconStyles(sensorDiv, 'icon');
							return div;
						},

						createShadow : function()
						{
							return null;
						}
					});

			module.onSensorsChange = function(e, overlay)
			{
				if (overlay.sensors[e.layer.cpccRvId])
				{
					overlay.layer.removeLayer(overlay.sensors[e.layer.cpccRvId]);
				}

				if (e.layer.cpccFeatures.geometry.geometries.length < 1)
				{
					return;
				}

				overlay.sensors[e.layer.cpccRvId] = leaflet.geoJson(e.layer.cpccFeatures.geometry.geometries, {
					pointToLayer : function(feature, latlng)
					{
						return new leaflet.Marker(latlng, {
							icon : new module.SensorIcon({
								sensors : feature.properties.sensorList,
							})
						});
					},
				}).addTo(overlay.layer);
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