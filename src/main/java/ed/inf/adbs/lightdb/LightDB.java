package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.PlainSelect;
import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Lightweight in-memory database system
 *
 */
public class LightDB {

	/**
	 * The main method for the whole program. Input three params and output the .csv file.
	 * @param args databaseDir, inputFile, outputFile
	 */
	public static void main(String[] args) {

		if (args.length != 3) {
			System.err.println("Usage: LightDB database_dir input_file output_file");
			return;
		}

		String databaseDir = args[0];
		String inputFile = args[1];
		String outputFile = args[2];

		DBCatalog dbc = DBCatalog.getInstance();
		dbc.setDatabaseDir(databaseDir);
		List<Tuple> result;
		Interpreter interpreter = new Interpreter(inputFile);
		result = interpreter.execute();
		File writeFile = new File(outputFile);
		try {
			BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile));
			for (Tuple tuple : result) {
				writeText.write( Arrays.toString(tuple.getTupleArray()).replace("[","").replace("]","").replace(" ",""));
				writeText.newLine();
			}
			writeText.flush();
			writeText.close();
		} catch (FileNotFoundException e) {
			System.out.println("do not find required file");
		} catch (IOException e) {
			System.out.println("wrong when reading file");
		}
	}

	/**
	 * Example method for getting started with JSQLParser. Reads SQL statement from
	 * a file and prints it to screen; then extracts SelectBody from the query and
	 * prints it to screen.
	 */

	public static String parsingExample(String filename) {
		try {
			Statement statement = CCJSqlParserUtil.parse(new FileReader(filename));
            //Statement statement = CCJSqlParserUtil.parse("SELECT * FROM Sailors S1, Sailors S2  WHERE S1.A < S2.A ORDER BY Sailors.B");
			if (statement != null) {
				System.out.println("Read statement: " + statement);
				Select select = (Select) statement;
				System.out.println("Select body: " + select.getSelectBody());
				PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
				System.out.println("PlainSelect: " + plainSelect);
				System.out.println("PlainSelect.getFromItem: " + plainSelect.getFromItem().getClass());
				System.out.println("PlainSelect.getWhere: " + plainSelect.getWhere());
				System.out.println("PlainSelect.getJoins: " + plainSelect.getJoins());
				System.out.println("PlainSelect.getOrderByElements: " + plainSelect.getOrderByElements());
				String tableName = plainSelect.getFromItem().toString();
				return tableName;
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
		return null;
	}
}
