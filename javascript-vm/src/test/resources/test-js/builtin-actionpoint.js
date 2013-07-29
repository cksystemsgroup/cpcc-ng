function VV() {
	
	var pos = new LatLngAlt(48.0, 13.0, 20);
	
	var tol = 3;
	
	var values = [
		new SensorValue("Picture",     1343148941614, "img5680132431876720462.png"),
		new SensorValue("Temperature", 1343148941620, 5.8),
		new SensorValue("AirPressure", 1343148941623, 1086.9)
	];

	var ap = new ActionPoint(pos, tol, values);
	
	println(ap);
	println(ap.position);
	println(ap.tolerance);
	for (var k=0; k < ap.values.length; ++k) {
		println(ap.values[k]);	
	}
	
	println("updates:");
	pos = new LatLngAlt(47.69254069, 13.38527209, 20);
	
	tol = 4.6;
	
	values = [
		new SensorValue("Picture",     1343148868842, "img7148232529784109309.png"),
		new SensorValue("Temperature", 1343148868846, 10.0),
		new SensorValue("AirPressure", 1343148868849, 1096.9)
	];

	ap.position = pos;
	println(ap);
	ap.tolerance = tol;
	println(ap);
	ap.values = values;
	println(ap);
}