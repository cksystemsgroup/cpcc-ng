define([ "jquery", "leaflet", "leaflet/data" ],

function($, leaflet, data)
{
	return function(elementId, params)
	{
		leaflet.Icon.Default.imagePath = params.iconBaseUrl;

		if (params.height)
		{
			$('#' + elementId).css("height", params.height + 'px');
		}

		var theMap = leaflet.map(elementId, params.options).setView(params.center, params.zoom);

		data[elementId] = {
			map : theMap,
			baseMaps : [],
			overlayMaps : [],
			overlays : {},
			layerControl : undefined,
			selectedMapLayer : null,
		};

		var legend = leaflet.control({
			position : 'bottomright'
		});

		legend.onAdd = function(map)
		{
			var div = leaflet.DomUtil.create('div', 'info legend');
			div.innerHTML = '<i style="background:green"></i> Total VVs<br>'
					+ '<i style="background:red"></i> Active VVs<br>'
					+ '<i style="background:blue"></i> Migrating VVs<br>'
					+ '<i style="background:#bbbbbb"></i> Inactive VVs';
			return div;
		};

		legend.addTo(theMap);
	}
});