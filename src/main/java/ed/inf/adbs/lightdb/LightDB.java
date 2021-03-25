package ed.inf.adbs.lightdb;

//import jdk.internal.org.objectweb.asm.Type;
import net.sf.jsqlparser.expression.Expression;
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
		result = interpreter.excute();
		File writeFile = new File(outputFile);

		try {
			BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile));

			for (Tuple tuple : result) {
				//调用write的方法将字符串写到流中
				writeText.write( Arrays.toString(tuple.getTupleArray()).replace("[","").replace("]","").replace(" ",""));
				writeText.newLine();    //换行
			}

			writeText.flush();
			//关闭缓冲区，缓冲区没有调用系统底层资源，真正调用底层资源的是FileWriter对象，缓冲区仅仅是一个提高效率的作用
			//因此，此处的close()方法关闭的是被缓存的流对象
			writeText.close();
		} catch (FileNotFoundException e) {
			System.out.println("没有找到指定文件");
		} catch (IOException e) {
			System.out.println("文件读写出错");


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
