function VV() {
	
	var v = new SensorValue("temperature", 1343148941620, 19.3);
	println(v);

	v = new SensorValue("airPressure", null, null);
	println(v);
	v.name = "temperature";
	v.time = 1343148898613;
	v.value = 1081;
	println(v);
	println(v.name);
	println(v.time);
	println(v.value);
	
	v = new SensorValue("image", 1343148943620, "img8971658578904899782.png");
	println(v);
	println(v.name);
	println(v.time);
	println(v.value);
	
}