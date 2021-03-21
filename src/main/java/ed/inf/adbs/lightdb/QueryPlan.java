package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.expression.Expression;
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
                System.out.println("Select body: " + select.getSelectBody());
                System.out.println("PlainSelect.getFromItem: " + plainSelect.getFromItem());
                System.out.println("PlainSelect.getSelectItems: " + plainSelect.getSelectItems());
                System.out.println("PlainSelect.getSelectItems: " + plainSelect.getSelectItems().get(0).getClass());
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
    }

    public void excute(){
        ScanOperator scan = new ScanOperator(tableName);
        //scan.dump();
        SelectOperator sel = new SelectOperator(exp,scan);
        ProjectOperator pro = new ProjectOperator(selectItems,sel);
        //sel.getNextTuple();
        //sel.reset();
        //sel.dump();
        //sel.reset();
        pro.dump();
    }
}
