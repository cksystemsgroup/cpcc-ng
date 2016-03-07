var gps = VV.sensor.get('GPS');
var altimeter = VV.sensor.get('Altimeter');
var camera = VV.sensor.get('Belly Mounted Camera 640x480');

getStdOut().println("cp1");

VV.task.execute({
    type: 'point',
    position: new VV.types.LatLngAlt(37.8081,-122.42661,50),
    tolerance: 4,
    sensors: [gps, altimeter, camera]
},
function(sensorData) {
    getStdOut().println("finished");
    for (var k=0, l=sensorData.length; k < l; ++k)
    {
        VV.storage.store("p1-"+k, sensorData[k]);
    }
});    

getStdOut().println("cp2");    

VV.task.execute({
    type: 'point',
    position: new VV.types.LatLngAlt(37.8081,-122.42690,50),
    tolerance: 4,
    sensors: [gps, altimeter, camera]
},    
function(sensorData) {
    getStdOut().println("finished");
    for (var k=0, l=sensorData.length; k < l; ++k)
    {
        VV.storage.store("p2-"+k, sensorData[k]);
    }
});

getStdOut().println("cp3");

VV.task.execute({
    type: 'point',
    position: new VV.types.LatLngAlt(37.80890,-122.42690,50),
    tolerance: 4,
    sensors: [gps, altimeter, camera]
},
function(sensorData) {
    getStdOut().println("finished");
    for (var k=0, l=sensorData.length; k < l; ++k)
    {
        VV.storage.store("p3-"+k, sensorData[k]);
    }
});

getStdOut().println("cp4");

VV.task.execute({
    type: 'point',
    position: new VV.types.LatLngAlt(37.80890,-122.42661,50),
    tolerance: 4,
    sensors: [gps, altimeter, camera]
},
function(sensorData) {
    getStdOut().println("finished");
    for (var k=0, l=sensorData.length; k < l; ++k)
    {
        VV.storage.store("p4-"+k, sensorData[k]);
    }
});


getStdOut().println("cp5");

VV.task.execute({
    type: 'point',
    position: new VV.types.LatLngAlt(37.80810,-122.42661,50),
    tolerance: 4,
    sensors: [gps, altimeter, camera]
},
function(sensorData) {
    getStdOut().println("finished");
    for (var k=0, l=sensorData.length; k < l; ++k)
    {   
        VV.storage.store("p5-"+k, sensorData[k]);
    }   
});

getStdOut().println("cp6");

