package ed.inf.adbs.lightdb;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple LightDB.
 */
public class LightDBTest{
	
	/**
	 * Rigorous Test :-)
	 */
	@Test
	public void shouldAnswerWithTrue() {
		LightDB.parsingExample("1");
		assertTrue(true);
	}

	@Test
	public void mainTest(){
		String[] args = new String[]{"samples/db", "samples/input/query9.sql", "samples/output/query4.csv"};
		LightDB.main(args);
	}
}
