
var geoJsonLayer = null;

function updateGeoState(data)
{
    alert("updateGeoState: data = " + JSON.stringify(data));
    
    // {"type":"FeatureCollection","features":[
    //   {"type":"Feature",
    //    "properties":{"name":"GS01","rvtype":"GROUND_STATION","type":"rv"},
    //    "geometry":{"type":"FeatureCollection","features":[
    //                   {"type":"Feature",
    //                    "properties":{"type":"depot"},
    //                    "geometry":{"type":"Point","coordinates":[-122.426,37.808]}}
    //               ]},
    //                    "id":"16"},
    //   {"type":"Feature",
    //    "properties":{"type":"rvPosition"},
    //    "geometry":{"type":"Point","coordinates":[-122.426,37.808,0]}}
    //  ]}

    var geojsonFeature = {
            "type": "Feature",
            "properties": {
                "name": "Coors Field",
                "amenity": "Baseball Stadium",
                "popupContent": "This is where the Rockies play!"
            },
            "geometry": {
                "type": "Point",
                "coordinates": [-122.426,37.808,0]
            }
        };
    
    
//    var geojsonFeature = {
//        "type": "Feature",
//        "properties": {"party": "Democrat"},
//        "geometry": {
//            "type": "Polygon",
//            "coordinates": [[
//                [-109.05, 41.00],
//                [-102.06, 40.99],
//                [-102.03, 36.99],
//                [-109.04, 36.99],
//                [-109.05, 41.00]
//            ]]
//        }
//    };
    
    geoJsonLayer.addData(geojsonFeature);

}

L.RvIcon =  L.divIcon.extend({

    options : {
        iconSize: new L.Point(50, 50),
        iconAnchor: new L.Point(5, 5),
        className : 'real-vehicle-icon',
        html: 'XYZ <img src="img/BoatBlack_32.png" alt="my image" style="transform: rotate(45deg)" > ABC123'
    },
    
    vehicle : { id : '', name : '', state : '', heading : '', vvs : [] },
    
    setState : function (id, name, state, heading, vvs){
        var v = this.vehicle;
        var vvEqual = v.vvs.length == vvs.length; 
        
        for (var k=0, l = vvs.length; k < l && vvEqual; ++k){
            vvEqual = v.vvs[k] == vvs[k];
        }
        
        if (!vvEqual || v.id != id || v.state != state || v.heading != heading  || v.name != name)        {
            this.options.html = 
                ''
            
            
            
            v.vvs = vvs; v.id = id; v.state = state; v.heading = heading; v.name = name;
        }
        
    },
    
    
    
    
});


L.RealVehicleMarker = L.Marker.extend({

    options : {
        icon : L.divIcon({ 
              iconSize: new L.Point(50, 50),
              html: 'XYZ <img src="img/BoatBlack_32.png" alt="my image" style="transform: rotate(45deg)" > ABC123'
          }),
          
//          icon : L.icon({iconUrl: 'img/BoatBlack_32.png'}),
//        iconBusyUrl : 'img/VehicleBlack_32.png',
//        iconIdleUrl : 'img/VehicleGreen_32.png',
//        clickable : false,
//        className : 'vehicle-marker-icon'
    },
});


function realVehicleInit()
{
    var geojsonMarkerOptions = {
            radius: 8,
            fillColor: "#ff7800",
            color: "#000",
            weight: 1,
            opacity: 1,
            fillOpacity: 0.8
        };
    
    geoJsonLayer = L.geoJson(
            null, {
//        style: function (feature) {
//            alert("updateGeoState: feature = " + JSON.stringify(feature));
//            //return {color: feature.properties.color};
//            return "red";
//        },
//        onEachFeature: function (feature, layer) {
//            layer.bindPopup(feature.properties.description);
//        }
                
                pointToLayer: function (feature, latlng) {
//                    return L.circleMarker(latlng, geojsonMarkerOptions);
//                    var vehicle = new L.QuadrotorMarker(latlng); //, geojsonMarkerOptions);
//                    vehicle.setVehicleState('01', 'idle', '158', 'bugger');
//                    vehicle.setTaskState(123, 'todo');
                    alert("pointToLayer: " + JSON.stringify(latlng) + " feature=" + JSON.stringify(feature));
                    
//                    var vehicle = L.divIcon({ 
//                        iconSize: new L.Point(50, 50),
//                        //iconAnchor: new L.Point(5, 5),
//                        //className : 'vehicle-icon',
//                        //html: 'XYZ <img src="img/BoatBlack_32.png" alt="my image" > ABC123'
//                        html: 'fooo bar'
//                    });
                    
                    // var vehicle = L.icon({iconUrl: 'img/BoatBlack_32.png'});
//                    var vehicle = L.circleMarker(latlng, geojsonMarkerOptions);
//                    var vehicle = L.circle(latlng, 8, {fillColor: "#ff7800",color: "#000",weight: 1,opacity: 1,fillOpacity: 0.8});
                    var vehicle = new L.RealVehicleMarker(latlng);
                    return vehicle;
                }      
    }
);
    geoJsonLayer.addTo(map);
    
    $.getJSON("commons/status", updateGeoState);

//    setInterval('$.getJSON( "commons/status", updateGeoState)', 5000);

}
