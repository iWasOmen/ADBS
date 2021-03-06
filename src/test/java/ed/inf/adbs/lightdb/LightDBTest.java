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
	public void mainTest(){
		String j = null;
		for(int i = 1; i<10; i++) {
			j = String.valueOf(i);
			String[] args = new String[]{"samples/db", "samples/input/query" + j + ".sql", "output/query" + j + ".csv"};
			LightDB.main(args);
		}
		//LightDB.main(args);
	}

	@Test
	public void mainSingleTest(){
		String[] args = new String[]{"samples/db", "samples/input/query9.sql", "output/query10.csv"};
		LightDB.main(args);

		//LightDB.main(args);
	}
}
