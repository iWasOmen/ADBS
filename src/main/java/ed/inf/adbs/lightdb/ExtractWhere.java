package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * ExtractWhere Class to extract select and join conditions from the WHERE claus.
 * Part of details about explanation of extracting join conditions from the WHERE clause.
 */
public class ExtractWhere {
    ExpressionList selectExpressionList = new ExpressionList();
    ExpressionList joinExpressionList = new ExpressionList();
    List<String> selectTableNames = new ArrayList<>();
    List<String> joinTableNames = new ArrayList<>();

    /**
     * Construct ExtractWhere Class.
     * @param parseExpression the WHERE claus
     */
    public ExtractWhere(Expression parseExpression) {
        final Stack<Expression> stackExpression = new Stack<Expression>();
        final Stack<String> stackString = new Stack<String>();
        ExpressionDeParser deparser = new ExpressionDeParser() {
            /**
             * Extends the ExpressionDeParser.
             * Use a String stack and a Expression stack to compute the result.
             */
            /*
            @explanation
            Compare two expression, and decide to add to which ExpressionList.
             */
            public void compareExpression(Expression expression) {
                DBCatalog dbc = DBCatalog.getInstance();
                Expression exp2 = stackExpression.pop();
                Expression exp1 = stackExpression.pop();
                String str2 = stackString.pop();
                String str1 = stackString.pop();

                /*
                @explanation
                Both or one of exps is long, add to oneSelectExpression
                 */
                if (exp1.getClass() == LongValue.class || exp2.getClass() == LongValue.class) {
                    selectExpressionList.addExpressions(expression);
                    if(exp1.getClass() == Column.class)
                        selectTableNames.add(str1);
                    else if(exp2.getClass() == Column.class)
                        selectTableNames.add(str2);
                    else
                        selectTableNames.add(dbc.getConstantTableName());
                }
                /*
                @explanation
                both of two exps are columns
                */
                else {
                    /*
                    @explanation
                    Two exps have same table names, add to oneSelectExpression
                    */
                    if (str2.equals(str1)) {
                        selectExpressionList.addExpressions(expression);
                        selectTableNames.add(str1);
                    }
                    /*
                    @explanation
                    Two exps have different table name, add to oneJoinExpression
                     */
                    else {
                        joinExpressionList.addExpressions(expression);
                        joinTableNames.add(str1);
                        joinTableNames.add(str2);
                    }
                }
            }

            @Override
            public void visit(Column column) {
                super.visit(column);
                stackExpression.push(column);
                stackString.push(column.getTable().getName());
                //System.out.println("column:"+column.getTable().getName());
            }

            @Override
            public void visit(LongValue longValue) {
                super.visit(longValue);
                stackExpression.push(longValue);
                stackString.push(longValue.toString());
                //System.out.println("longvalue:"+longValue.toString());
            }

            @Override
            public void visit(EqualsTo equalsTo) {
                super.visit(equalsTo);
                compareExpression(equalsTo);
            }

            @Override
            public void visit(NotEqualsTo notEqualsTo) {
                super.visit(notEqualsTo);
                compareExpression(notEqualsTo);
            }

            @Override
            public void visit(GreaterThan greaterThan) {
                super.visit(greaterThan);
                compareExpression(greaterThan);
            }

            @Override
            public void visit(GreaterThanEquals greaterThanEquals) {
                super.visit(greaterThanEquals);
                compareExpression(greaterThanEquals);
            }

            @Override
            public void visit(MinorThan minorThan) {
                super.visit(minorThan);
                compareExpression(minorThan);
            }

            @Override
            public void visit(MinorThanEquals minorThanEquals) {
                super.visit(minorThanEquals);
                compareExpression(minorThanEquals);
            }
        };

        parseExpression.accept(deparser);
//        boolean result = (boolean)stackBool.pop();
//        System.out.println("result:" + result +"\n");


    }

    /**
     * Get the selectExpressionList.
     * @return selectExpressionList
     */
    public ExpressionList getSelectExpressionList(){
        return selectExpressionList;
    }

    /**
     * Get the joinExpressionList.
     * @return joinExpressionList
     */
    public ExpressionList getJoinExpressionList(){
        return joinExpressionList;
    }

    /**
     * Get the selectTableNames.
     * @return selectTableNames.
     */
    public List<String> getSelectTableNames(){
        return selectTableNames;
    }

    /**
     * Get the joinTableNames.
     * @return joinTableNames
     */
    public List<String> getWhereTableNames(){
        return joinTableNames;
    }
}
