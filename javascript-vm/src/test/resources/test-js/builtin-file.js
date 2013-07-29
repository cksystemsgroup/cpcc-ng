function VV() {
	var f = new File("myName");
	println("file name is " + f.getName());
	
	var pw = new PrintWriter(f);
	pw.println("test01\n");
	
	var d = new File("/");
	var lst = d.list();
	for (var k=0; k < lst.length; k++) {
		println("file found: " + lst[k]);
	}
}