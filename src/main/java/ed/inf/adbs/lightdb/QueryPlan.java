package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class QueryPlan {
    private String tableName;
    private Expression exp;
    private ExpressionList selectExpressionList;
    private ExpressionList whereExpressionList;
    List<SelectItem> selectItems;

    public QueryPlan(String inputname) {
        try {
            Statement statement = CCJSqlParserUtil.parse(new FileReader(inputname));
            if (statement != null) {
                Select select = (Select) statement;
                PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
                this.tableName = plainSelect.getFromItem().toString();
                this.exp = plainSelect.getWhere();
                this.selectItems = plainSelect.getSelectItems();
                ExtractWhere extractWhere = new ExtractWhere(exp);
                selectExpressionList = extractWhere.getSelectExpressionList();
                whereExpressionList = extractWhere.getWhereExpressionList();
                System.out.println("Select body: " + select.getSelectBody());
                System.out.println("PlainSelect.getWhere: " + plainSelect.getWhere());
////                System.out.println("PlainSelect.getWhereClass: " + plainSelect.getWhere().getClass());
////                System.out.println("PlainSelect.getFromItem: " + plainSelect.getFromItem());
////                System.out.println("PlainSelect.getSelectItems: " + plainSelect.getSelectItems());
////                System.out.println("PlainSelect.getSelectItems: " + plainSelect.getSelectItems().get(0).getClass());
                System.out.println("selectExpressionList" + selectExpressionList);
                System.out.println("getWhereExpressionList" + whereExpressionList);
                //System.out.println("getWhereExpressionList" + whereExpressionList.getExpressions().get(0));
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
    }

    public void excute(){
        ScanOperator scan1 = new ScanOperator(tableName);
        ScanOperator scan2 = new ScanOperator("Reserves");
        //scan.dump();
        SelectOperator sel = new SelectOperator(selectExpressionList.getExpressions().get(0),scan1);
        JoinOperator join = new JoinOperator(whereExpressionList.getExpressions().get(0),scan2,sel);
        ProjectOperator pro = new ProjectOperator(selectItems,join);
        //sel.getNextTuple();
        //sel.reset();
        //sel.dump();
        //sel.reset();
        join.dump();
    }
}
