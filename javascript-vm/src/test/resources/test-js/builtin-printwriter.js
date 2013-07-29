function VV() {
	var f = new File("/lala.txt");
	var pw = new PrintWriter(f);
	pw.println("test01");
	pw.println("test02");
	migrate();
	pw.println("test03");
}