
function VV() {
	println("before");
	transient.printDummy();
	migrate();
	transient.printDummy();
	println("after");
}