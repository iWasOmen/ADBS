package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import java.io.FileReader;
import java.util.List;

/**
 * Interpreter Class to interpreter given sql and execute the query plan.
 */
public class Interpreter {

    Operator rootOperator;

    /**
     * Interpreter given sql and pass the statement to QueryPlan class and get rootOperator as a result.
     * @param inputname the input file name.
     */
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

    /**
     * Excute query plan and call dump() method on root operator.
     * @return return a list of all result Tuple Class
     */
    public List<Tuple> execute(){
        List<Tuple> result;
        result = rootOperator.dump();
        return result;
    }
}
