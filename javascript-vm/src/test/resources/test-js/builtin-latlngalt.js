function VV() {

	var pos = new LatLngAlt(48.0, 13.0, 20);
	println(pos);
	
	println(pos.lat);
	println(pos.lng);
	println(pos.alt);

	pos.lat = 49.33;
	pos.lng = 12.66;
	pos.alt = 19.22;
	println(pos);
	println(pos.lat);
	println(pos.lng);
	println(pos.alt);
}