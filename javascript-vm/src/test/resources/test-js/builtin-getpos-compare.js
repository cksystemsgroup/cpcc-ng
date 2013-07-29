
function VV () {
	var pos1 = getpos();
	var pos2 = new LatLngAlt(47.0, 13.0, 20);
	var pos3 = new LatLngAlt(48.0, 13.0, 20);
	println(pos1.equals(pos2));
	println(pos1.equals(pos3));
}