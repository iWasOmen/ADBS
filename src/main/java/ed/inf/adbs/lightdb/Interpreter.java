package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

import java.io.FileReader;

public class Interpreter {

    Operator rootOperator;

    public Interpreter(String inputname){
        try {
            Statement statement = CCJSqlParserUtil.parse(new FileReader(inputname));
            if(statement != null) {
                QueryPlan queryPlan = new QueryPlan(statement);
                rootOperator = queryPlan.getRootOpertaor();
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
    }

    public void excute(){
        rootOperator.dump();
    }
}
