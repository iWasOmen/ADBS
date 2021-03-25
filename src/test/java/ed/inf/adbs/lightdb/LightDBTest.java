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
		String j = null;
		for(int i = 1; i<13; i++) {
			j = String.valueOf(i);
			String[] args = new String[]{"samples/db", "samples/input/query" + j + ".sql", "samples/output/query4.csv"};
			LightDB.main(args);
		}
		//LightDB.main(args);
	}

	@Test
	public void mainSingleTest(){
		String[] args = new String[]{"samples/db", "samples/input/query7.sql", "samples/output/query4.csv"};
		LightDB.main(args);

		//LightDB.main(args);
	}
}
