var drawnItems = new L.FeatureGroup();
var otherRealVehicleZones = new L.FeatureGroup();
var selectedMapLayer = null;

function saveData(items)
{
    var geoJsonFeatures = [];
    var layers = items.getLayers();
    for (var k = 0, l = layers.length; k < l; ++k)
    {
        var feature = layers[k].toGeoJSON();
        feature.properties['minAlt'] = layers[k]['minAltitude'];
        feature.properties['maxAlt'] = layers[k]['maxAltitude'];

        if (layers[k] instanceof L.Marker)
        {
            feature.properties['type'] = 'depot';
        }
        geoJsonFeatures.push(feature);
    }

    var featureCollection = {
        type : 'FeatureCollection',
        features : geoJsonFeatures,
        properties : {
            zoom : map.getZoom(),
            center : map.getCenter(),
            layer : selectedMapLayer,
        },
    };

    document.getElementById('realVehicleZones').value = JSON.stringify(featureCollection);
    $('#rvForm').submit();
}

var dialogOptions = {
    title : zoneEditMessages.rvZoneEditorDialogTitle,
    width : 'auto',
    height : 'auto',
    position : {
        my : 'center',
        at : 'right',
        of : window
    }
};

var dialogLayer = null;

function dialogOpen(layer)
{
    dialogLayer = layer;
    var minAlt = layer['minAltitude'];
    var maxAlt = layer['maxAltitude'];

    document.getElementById('minAltitude').value = minAlt;
    document.getElementById('maxAltitude').value = maxAlt;
    document.getElementById('minAltitude').defaultValue = minAlt;
    document.getElementById('maxAltitude').defaultValue = maxAlt;
    $('#dialog').dialog('option', dialogOptions).dialog('open');
}

function dialogOk()
{
    $('#dialog').dialog('close');

    var minAlt = parseFloat(document.getElementById('minAltitude').value);
    var maxAlt = parseFloat(document.getElementById('maxAltitude').value);

    if (minAlt > maxAlt)
    {
        var c = minAlt;
        minAlt = maxAlt;
        maxAlt = c;
        alert(zoneEditMessages.rvZoneEditorAltitudeSwapped);
    }

    dialogLayer['minAltitude'] = minAlt;
    dialogLayer['maxAltitude'] = maxAlt;
    dialogLayer = null;
    saveData(drawnItems);
}

function dialogCancel()
{
    $('#dialog').dialog('close');
    dialogLayer = null;
}

function importRealRehicleZones()
{
    var featureCollection = JSON.parse(document.getElementById('realVehicleZones').value);

    // alert("bugger 222: " + JSON.stringify(featureCollection));
    if (featureCollection == undefined)
    {
        return;
    }

    var x = featureCollection.features;

    for (var k = 0, l = x.length; k < l; ++k)
    {
        var feature = x[k];
        // alert("bugger 2: k=" + k + ", l=" + l + ", f=" + JSON.stringify(feature));
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
                    coord.push(new L.LatLng(p[1], p[0]));
                }
                var polygon = L.polygon(coord);
                // alert("poly: " + JSON.stringify(coord));
                polygon['minAltitude'] = minAlt;
                polygon['maxAltitude'] = maxAlt;
                drawnItems.addLayer(polygon);
            }
            else if (feature.geometry.type === 'Point')
            {
                var p = feature.geometry.coordinates;
                var marker = L.marker(new L.LatLng(p[1], p[0]));
                drawnItems.addLayer(marker);
            }
        }
    }

    if (featureCollection['properties'] != undefined)
    {
        if (featureCollection.properties['zoom'] != undefined)
        {
            map.setZoom(featureCollection.properties.zoom);
        }

        if (featureCollection.properties['center'] != undefined)
        {
            map.panTo(featureCollection.properties.center);
        }
    }
}

function importOtherRealVehicleZones()
{
    var otherZones = JSON.parse(document.getElementById('otherRealVehicleZones').value);
//alert("importOtherRealVehicleZones 1: " + JSON.stringify(otherZones));
    if (otherZones == undefined)
    {
        return;
    }

    var x = otherZones.features;

    for (var k = 0, l = x.length; k < l; ++k)
    {
        var feature = x[k];
//alert("importOtherRealVehicleZones 2: " + JSON.stringify(feature));
        if (feature.type === 'Feature' && feature.geometry.type === 'Polygon')
        {
            var coord = [];
            var poly = feature.geometry.coordinates[0];
            for (var m = 0, n = poly.length; m < n; ++m)
            {
                var p = poly[m];
                coord.push(new L.LatLng(p[1], p[0]));
            }
            var polygon = L.polygon(coord, {
                color : '#bada55',
                fillColor : '#bada55',
                opacity : 0.5,
                fillOpacity : 0.2
            });
            otherRealVehicleZones.addLayer(polygon);
        }
    }
}

function drawInit()
{
    // drawnItems.clearLayers();
    map.addLayer(drawnItems);
    map.addLayer(otherRealVehicleZones);
    // alert("bugger 18: " + JSON.stringify(drawnItems.toGeoJSON()));

    importRealRehicleZones();
    importOtherRealVehicleZones();

    // alert("bugger 17: " + JSON.stringify(drawnItems.toGeoJSON()));

    var drawControl = new L.Control.Draw({
        draw : {
            position : 'topleft',
            polygon : {
                allowIntersection : false,
                drawError : {
                    color : '#b00b00',
                    timeout : 1000
                },
                shapeOptions : {
                    color : '#03f'  // '#bada55'
                },
                showArea : true
            },
            rectangle : false,
            polyline : false,
            circle : false,
        },
        edit : {
            featureGroup : drawnItems
        }
    });

    map.addControl(drawControl);

    map.on('draw:created', function(e)
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

            layer.on('click', function(event)
            {
                dialogOpen(event.target);
            });
        }

        drawnItems.addLayer(layer);
        saveData(drawnItems);
    });

    map.on('draw:edited', function(e)
    {
        saveData(drawnItems)
    });

    map.on('draw:deleted', function(e)
    {
        $('#dialog').dialog('close');
        saveData(drawnItems);
    });

    map.on('baselayerchange', function(e)
    {
        selectedMapLayer = e.name;
    });
}
