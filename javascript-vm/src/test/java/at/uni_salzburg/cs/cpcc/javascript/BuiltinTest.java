/*
 * This code is part of the CPCC-NG project.
 * Copyright (c) 2012  Clemens Krainer, Michael Lippautz
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.javascript;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.uni_salzburg.cs.cpcc.javascript.runtime.base.NullPositionProvider;
import at.uni_salzburg.cs.cpcc.utilities.FileLoader;


public class BuiltinTest {
	
	private File tmpDir;
	
	private TransientStateMock tsd;
	
	private StringConsoleProvider cons;

	@Before
    public void setUp() throws IOException {
		tmpDir = File.createTempFile("jsTest", ".dir", new File("/tmp"));
		tmpDir.delete();
		FileUtils.forceMkdir(tmpDir);
		if (tmpDir.exists() && tmpDir.isDirectory()) {
			System.out.println("Created folder " + tmpDir.getAbsolutePath());
		} else {
			throw new IOException("Can not create folder " + tmpDir.getAbsolutePath());
		}
	}
	
	@After
    public void tearDown() throws IOException {
		System.out.println("Deleting folder " + tmpDir.getAbsolutePath());
		FileUtils.deleteDirectory(tmpDir);
	}
	
    private JSInterpreter createJSI(String resourceName, boolean addTransient) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
		InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
		JSInterpreterBuilder b = new JSInterpreterBuilder();
		b.addCodefile(resourceName, fis);
//		b.addProvidedPackage(packageScope, o);
//		b.addProvidedTypes(type);
		
		tsd = new TransientStateMock();
		tsd.test = "DummyText";
		b.addProvidedPackage("transient", new JSFunctionsTransientMock(tsd));
		JSInterpreter jsi = b.build();
		
		Properties props = new Properties();
		props.setProperty("virtual.vehicle.data.directory",tmpDir.getAbsolutePath());
		JSInterpreter.setContextProperties(props);
		
		cons = new StringConsoleProvider();
		jsi.setConsoleProvider(cons);

		return jsi;
    }
	
	@Test
	public void testPrintln() throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException {
		JSInterpreter jsi = createJSI("test-js/builtin-print.js", false);
		jsi.start();
		jsi.close();
		assertEquals("hello world!\n", cons.result.toString());
	}
	
	@Test
	public void testMigrate() throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException {
		JSInterpreter jsi = createJSI("test-js/builtin-migrate-simple.js", false);
		byte[] snapshot = jsi.start();
		assertNotNull(snapshot);
		snapshot = jsi.start(snapshot);
		jsi.close();
		assertNull(snapshot);
	}
	
	@Test
	public void testMigrateTransient() throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException {
		JSInterpreter jsi = createJSI("test-js/builtin-migrate-transient.js", true);
		tsd.test = "DummyText01";
		byte[] snapshot = jsi.start();
		assertNotNull(snapshot);
		jsi.close();
		assertEquals("before\nDummyText01\n", cons.result.toString());
		
		jsi = createJSI("test-js/builtin-migrate-transient.js", true);
		tsd.test = "DummyText02";
		snapshot = jsi.start(snapshot);
		jsi.close();
		assertNull(snapshot);
		assertEquals("DummyText02\nafter\n", cons.result.toString());
	}
	
	@Test
	public void testPosSimple() throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException {
		JSInterpreter jsi = createJSI("test-js/builtin-getpos-simple.js", false);
		jsi.setPositionProvider(new NullPositionProvider());
		jsi.start();
		jsi.close();
	}
	
	@Test
	public void testPosCompare() throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException {
		JSInterpreter jsi = createJSI("test-js/builtin-getpos-compare.js", false);
		jsi.setPositionProvider(new StaticPositionProviderMock(47.0, 13.0, 20.0));
		jsi.start();
		jsi.close();
		String output = cons.result.toString();
		assertEquals("true\nfalse\n", output);
	}
	
	@Test
	public void testRandom() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
		JSInterpreter jsi = createJSI("test-js/builtin-random.js", false);
		jsi.start();
		jsi.close();
		String output = cons.result.toString();
		assertTrue(output.matches("random 1: \\d[.,]\\d+\nrandom 2: \\d[.,]\\d+\nrandom 3: \\d[.,]\\d+\n"));
	}
	
	@Test
	public void testFile() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
		JSInterpreter jsi = createJSI("test-js/builtin-file.js", false);
		jsi.start();
		jsi.close();
		String output = cons.result.toString();
		assertEquals("file name is myName\nfile found: myName\n", output);
	}
	
	@Test
	public void testPrintWriter() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
		JSInterpreter jsi = createJSI("test-js/builtin-printwriter.js", false);
		byte[] snapshot = jsi.start();
		jsi.close();
		String result = FileLoader.loadFileAsString(new File(tmpDir,"lala.txt"));
		assertEquals("test01\ntest02\n",result);
		
		
		jsi = createJSI("test-js/builtin-printwriter.js", false);
		snapshot = jsi.start(snapshot);
		jsi.close();
		String output = cons.result.toString();
		assertEquals("",output);
		result = FileLoader.loadFileAsString(new File(tmpDir,"lala.txt"));
		assertEquals("test01\ntest02\ntest03\n",result);
	}
	
	@Test
	public void testLatLngAlt() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
		JSInterpreter jsi = createJSI("test-js/builtin-latlngalt.js", false);
		jsi.start();
		jsi.close();
		String output = cons.result.toString();
		assertEquals(
			"(48.00000000, 13.00000000, 20.000)\n" +
			"48.0\n" +
			"13.0\n" +
			"20.0\n" +
			"(49.33000000, 12.66000000, 19.220)\n" +
			"49.33\n" +
			"12.66\n" +
			"19.22\n", output);
	}
	
	@Test
	public void testSensorValue() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
		JSInterpreter jsi = createJSI("test-js/builtin-sensorvalue.js", false);
		jsi.start();
		jsi.close();
		String output = cons.result.toString();
		assertEquals(		
			"temperature (1343148941620, 19.300000)\n" +
			"airPressure\n" +
			"temperature (1343148898613, 1081.000000)\n" +
			"temperature\n" +
			"1.343148898613E12\n" +
			"1081.0\n" +
			"image (1343148943620, \"img8971658578904899782.png\")\n" +
			"image\n" +
			"1.34314894362E12\n" +
			"img8971658578904899782.png\n", output);
	}
	
	@Test
	public void testActionPoint() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
		JSInterpreter jsi = createJSI("test-js/builtin-actionpoint.js", false);
		jsi.start();
		jsi.close();
		String output = cons.result.toString();
		assertEquals(
			"Point (48.00000000, 13.00000000, 20.000) tolerance 3.0\n" +
			"Picture (1343148941614, \"img5680132431876720462.png\")\n" +
			"Temperature (1343148941620, 5.800000)\n" +
			"AirPressure (1343148941623, 1086.900000)\n" +
			"\n" +
			"(48.00000000, 13.00000000, 20.000)\n" +
			"3.0\n" +
			"Picture (1343148941614, \"img5680132431876720462.png\")\n" +
			"Temperature (1343148941620, 5.800000)\n" +
			"AirPressure (1343148941623, 1086.900000)\n" +
			"updates:\n" +
			"Point (47.69254069, 13.38527209, 20.000) tolerance 3.0\n" +
			"Picture (1343148941614, \"img5680132431876720462.png\")\n" +
			"Temperature (1343148941620, 5.800000)\n" +
			"AirPressure (1343148941623, 1086.900000)\n" +
			"\n" +
			"Point (47.69254069, 13.38527209, 20.000) tolerance 4.6\n" +
			"Picture (1343148941614, \"img5680132431876720462.png\")\n" +
			"Temperature (1343148941620, 5.800000)\n" +
			"AirPressure (1343148941623, 1086.900000)\n" +
			"\n" +
			"Point (47.69254069, 13.38527209, 20.000) tolerance 4.6\n" +
			"Picture (1343148868842, \"img7148232529784109309.png\")\n" +
			"Temperature (1343148868846, 10.000000)\n" +
			"AirPressure (1343148868849, 1096.900000)\n\n", output);
	}
	
}