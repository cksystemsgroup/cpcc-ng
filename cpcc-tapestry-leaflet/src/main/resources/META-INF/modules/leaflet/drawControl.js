define([ "jquery", "leaflet/draw", "leaflet/data" ],

function($, leaflet, data)
{
	return function(myId, mapId, params)
	{		
		var d = data[mapId];

		var drawnItems = new leaflet.FeatureGroup();
		d.map.addLayer(drawnItems);

		d.drawControl = new leaflet.Control.Draw({
			position : params.position,
			draw : params.draw,
			edit : {
				featureGroup : drawnItems
			}
		});

		d.map.addControl(d.drawControl);

		d.createPopupForm = function()
		{
			var container = $('<form class="form-horizontal" role="form" />');
			container.html(document.getElementById(params.popupTemplateId).innerHTML);

			var validation = function(event)
			{
				if (this.checkValidity() == false)
				{
					this.value = this.initialValue;
				}
			};

			var inp = container.find('[data-validate-popup-input="true"]');
			for (var i = 0; i < inp.length; i++)
			{
				inp[i].onchange = validation;
			}

			return container[0];
		};

		d.importZones = function()
		{
			var featureCollection = JSON.parse(document.getElementById(params.dataId).value);

			if (featureCollection == undefined)
			{
				return;
			}

			var x = featureCollection.features;

			for (var k = 0, l = x.length; k < l; ++k)
			{
				var feature = x[k];
				if (feature.type === 'Feature')
				{
					if (feature.geometry.type === 'Polygon')
					{
						var minAlt = feature.properties['minAlt'];
						var maxAlt = feature.properties['maxAlt'];
						var coord = [];
						var poly = feature.geometry.coordinates[0];
						for (var m = 0, n = poly.length; m < n; ++m)
						{
							var p = poly[m];
							coord.push(new leaflet.LatLng(p[1], p[0]));
						}
						var polygon = leaflet.polygon(coord);
						polygon['minAltitude'] = minAlt;
						polygon['maxAltitude'] = maxAlt;

						var popup = leaflet.popup({
							feature : polygon,
						}).setContent(d.createPopupForm());

						polygon.bindPopup(popup);

						drawnItems.addLayer(polygon);
					}
					else if (feature.geometry.type === 'Point')
					{
						var p = feature.geometry.coordinates;
						var marker = leaflet.marker(new leaflet.LatLng(p[1], p[0]));
						// TODO add click handler.
						drawnItems.addLayer(marker);
					}
				}
			}

			if (featureCollection['properties'] != undefined)
			{
				if (featureCollection.properties['zoom'] != undefined)
				{
					d.map.setZoom(featureCollection.properties.zoom);
				}

				if (featureCollection.properties['center'] != undefined)
				{
					d.map.panTo(featureCollection.properties.center);
				}
			}
		}

		d.saveZones = function ()
		{
			var geoJsonFeatures = [];
			var layers = drawnItems.getLayers();
			for (var k = 0, l = layers.length; k < l; ++k)
			{
				var feature = layers[k].toGeoJSON();
				feature.properties['minAlt'] = layers[k]['minAltitude'];
				feature.properties['maxAlt'] = layers[k]['maxAltitude'];

				if (layers[k] instanceof leaflet.Marker)
				{
					feature.properties['type'] = 'depot';
				}
				geoJsonFeatures.push(feature);
			}

			var featureCollection = {
				type : 'FeatureCollection',
				features : geoJsonFeatures,
				properties : {
					zoom : d.map.getZoom(),
					center : d.map.getCenter(),
					layer : d.selectedMapLayer,
				},
			};

			document.getElementById(params.dataId).value = JSON.stringify(featureCollection);
			$('#' + params.submitId).click();
		}

		d.map.on('draw:created', function(e)
		{
			var type = e.layerType, layer = e.layer;

			if (type === 'marker')
			{
				layer.bindPopup('A depot position!');
			}
			else
			{
				if (layer['minAltitude'] == undefined)
				{
					layer['minAltitude'] = 0;
				}

				if (layer['maxAltitude'] == undefined)
				{
					layer['maxAltitude'] = 20;
				}

				var popup = leaflet.popup({
					feature : layer,
				}).setContent(d.createPopupForm());

				layer.bindPopup(popup);
			}

			drawnItems.addLayer(layer);
			d.saveZones();
		});

		d.map.on('draw:edited', function(e)
		{
			d.saveZones();
		});

		d.map.on('draw:deleted', function(e)
		{
			d.saveZones();
		});

		d.map.on('baselayerchange', function(e)
		{
			d.selectedMapLayer = e.name;
			d.saveZones();
		});

		d.map.on('popupopen', function(e)
		{
			var f = e.popup.options.feature;
			if (f != null)
			{
				var content = e.popup.getContent();

				var minAlt = $(content).find("[name=min]")[0];
				minAlt.initialValue = minAlt.value = f['minAltitude'];

				var maxAlt = $(content).find("[name=max]")[0];
				maxAlt.initialValue = maxAlt.value = f['maxAltitude'];
			}
		});

		d.map.on('popupclose', function(e)
		{
			var f = e.popup.options.feature;
			if (f != null)
			{
				var content = e.popup.getContent();
				var newMin = parseInt($(content).find("[name=min]")[0].value);
				var newMax = parseInt($(content).find("[name=max]")[0].value);

				if (f['minAltitude'] != newMin || f['maxAltitude'] != newMax || newMax < newMin)
				{
					if (newMin <= newMax)
					{
						f['minAltitude'] = newMin;
						f['maxAltitude'] = newMax;
					}
					else
					{
						f['minAltitude'] = newMax;
						f['maxAltitude'] = newMin;
					}
					d.saveZones();
				}
			}
		});

		d.importZones();

	}
});