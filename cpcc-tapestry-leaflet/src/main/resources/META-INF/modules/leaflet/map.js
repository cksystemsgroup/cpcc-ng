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

		data[elementId] = {
			map : leaflet.map(elementId, params.options).setView(params.center, params.zoom),
			baseMaps : [],
			overlayMaps : [],
			overlays : {},
			layerControl : undefined,
			selectedMapLayer : null,
		};
	}
});