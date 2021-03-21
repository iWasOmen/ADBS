package ed.inf.adbs.lightdb;

//import jdk.internal.org.objectweb.asm.Type;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.PlainSelect;

import java.io.FileReader;

/**
 * Lightweight in-memory database system
 *
 */
public class LightDB {

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

		QueryPlan queryPlan = new QueryPlan(inputFile);
		queryPlan.excute();

//
//		String tablename = parsingExample(inputFile);
//		Expression exp = parsingExpression(inputFile);
//		ScanOperator scan = new ScanOperator(tablename);
//		//scan.dump();
//		SelectOperator sel = new SelectOperator(exp,scan);
//		sel.dump();

		//sel.getNextTuple();
		//sel.getNextTuple();
		//sel.getNextTuple();



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
				System.out.println("PlainSelect.getOrderByElements: " + plainSelect.getWhere());

				String tableName = plainSelect.getFromItem().toString();
				return tableName;

			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}

		return null;
	}
	public static Expression parsingExpression(String filename) {
		try {
			Statement statement = CCJSqlParserUtil.parse(new FileReader(filename));
			//Statement statement = CCJSqlParserUtil.parse("SELECT * FROM Sailors S1, Sailors S2  WHERE S1.A < S2.A ORDER BY Sailors.B");
			if (statement != null) {
				System.out.println("Read statement: " + statement);
				Select select = (Select) statement;
				PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
				return plainSelect.getWhere();

			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}

		return null;
	}
}
