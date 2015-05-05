package airbrake;

import static org.junit.Assert.*;

import org.junit.*;

import airbrake.stacktrace.BacktraceLine;
import airbrake.stacktrace.JavaBacktraceLine;
import airbrake.stacktrace.iOSBacktraceLine;

public class BacktraceLineTest {

	@Test
	public void testBacktraceLineFromString() {
		JavaBacktraceLine backtraceLine = new JavaBacktraceLine("at org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:46)");
		assertEquals("org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference", backtraceLine.className());
		assertEquals("run", backtraceLine.methodName());
		assertEquals(46, backtraceLine.lineNumber());
		assertEquals("JUnit4TestReference.java", backtraceLine.fileName());
	}

	@Test
	public void testBacktraceLineToString() {
		BacktraceLine backtraceLine = new JavaBacktraceLine("org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference", "JUnit4TestReference.java", 46, "run");
		assertEquals("at org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:46)", backtraceLine.toString());
	}

	@Test
	public void testBacktraceLineToXml() {
		BacktraceLine backtraceLine = new JavaBacktraceLine("org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference", "JUnit4TestReference.java", 46, "run");
		assertEquals("<line method=\"org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run\" file=\"JUnit4TestReference.java\" number=\"46\"/>", backtraceLine.toXml());
	}

	@Test
	public void testEscapeSpecialCharsInXml() {
		BacktraceLine backtraceLine = new JavaBacktraceLine("at com.company.Foo$$FastClassByCGLIB$$b505b4f2.invoke(<generated'\">:-1)");
		assertEquals("<line method=\"com.company.Foo$$FastClassByCGLIB$$b505b4f2.invoke\" file=\"&#60;generated&#39;&#34;&#62;\" number=\"-1\"/>", backtraceLine.toXml());
	}
	
	@Test
	public void testiOSBacktraceLineFromString() {
		BacktraceLine bl = new iOSBacktraceLine().acceptLine("95   CampTest                            0x000b719b -[ARPowerHookManager executeBlockWithId:data:context:withBlock:] + 358");
		assertEquals("<line method=\"0x000b719b -[ARPowerHookManager executeBlockWithId:data:context:withBlock:] + 358\" file=\"CampTest\" number=\"\"/>", bl.toXml());
	}
	
	@Test
	public void testiOSBacktraceLineFromStringNoMemAddress() {
		BacktraceLine bl = new iOSBacktraceLine(false).acceptLine("95   CampTest                            0x000b719b -[ARPowerHookManager executeBlockWithId:data:context:withBlock:] + 358");
		assertEquals("<line method=\"-[ARPowerHookManager executeBlockWithId:data:context:withBlock:] + 358\" file=\"CampTest\" number=\"\"/>", bl.toXml());
	}
	
	@Test
	public void testiOSBactraceLineFilenameWithNumbers() {
		BacktraceLine bl = new iOSBacktraceLine().acceptLine("94   ArtisanDemo2                        0x000000010908b8dc -[ARPProductDetailViewController2 addToCart:] + 508");
		assertEquals("<line method=\"0x000000010908b8dc -[ARPProductDetailViewController2 addToCart:] + 508\" file=\"ArtisanDemo2\" number=\"\"/>", bl.toXml());
	}
}
