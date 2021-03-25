package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import java.util.Arrays;
import java.util.Stack;

/**
 * JoinEvaluate Class to read the join expression and gives a result.
 */
public class JoinEvaluate {
    private Expression parseExpression;
    private Tuple leftTuple;
    private Tuple rightTuple;

    /**
     * Initialize the JoinEvaluate Class based on join expression.
     * @param parseExpression join expression
     */
    public JoinEvaluate(Expression parseExpression) {
        this.parseExpression = parseExpression;
    }

    /**
     * Evaluate left tuple and right tuple on join expression.
     * @param leftTuple left tuple
     * @param rightTuple right tuple
     * @return result
     */
    public Boolean evaluate(Tuple leftTuple, Tuple rightTuple){
        final Stack<Long> stackLong = new Stack<Long>();
        final Stack<Boolean> stackBool = new Stack<Boolean>();
        //System.out.println("---------------------join-----------------------");
        //System.out.println("leftTuple：" + Arrays.toString(leftTuple.getTupleArray()));
        //System.out.println("rightTuple：" + Arrays.toString(rightTuple.getTupleArray()));
        //System.out.println("expression：" + parseExpression);
        ExpressionDeParser deparser = new ExpressionDeParser() {
            /**
             * Extends the ExpressionDeParser.
             * Use a long stack and a boolean stack to compute the result.
             */
            @Override
            public void visit(AndExpression andExpression) {
                super.visit(andExpression);
                Boolean exp2 = stackBool.pop();
                Boolean exp1 = stackBool.pop();
                stackBool.push(exp1 & exp2 );
            }

            /**
             * Based on column name and table name, look for the column data for that tuple.
             * @param column column
             */
            @Override
            public void visit(Column column) {
                super.visit(column);
                Tuple tuple = null;
                String tableName = column.getTable().getName();
                //String columnName = column.getColumnName();
                String tableColumnName = column.getFullyQualifiedName();
                //System.out.println("tableColumnName"+tableColumnName);
                if (Arrays.toString(leftTuple.getTupleSchema()).contains(tableColumnName)) {
                    tuple = leftTuple;
                    //System.out.println("leftTuple:"+Arrays.toString(leftTuple.getTupleSchema()));
                }
                else if(Arrays.toString(rightTuple.getTupleSchema()).contains(tableColumnName)) {
                    tuple = rightTuple;
                    //System.out.println("rightTuple:"+Arrays.toString(rightTuple.getTupleSchema()));
                }
                long columNumber = tuple.getTupleNumber(tableColumnName);
                stackLong.push(columNumber);
            }

            @Override
            public void visit(LongValue longValue) {
                super.visit(longValue);
                stackLong.push(longValue.getValue());
            }

            @Override
            public void visit(EqualsTo equalsTo) {
                super.visit(equalsTo);

                Long exp2 = stackLong.pop();
                Long exp1 = stackLong.pop();
                if (exp1.equals(exp2))
                    stackBool.push(true);
                else
                    stackBool.push(false);
            }

            @Override
            public void visit(NotEqualsTo notEqualsTo) {
                super.visit(notEqualsTo);

                Long exp1 = stackLong.pop();
                Long exp2 = stackLong.pop();
                if (!exp1.equals(exp2))
                    stackBool.push(true);
                else
                    stackBool.push(false);

            }

            @Override
            public void visit(GreaterThan greaterThan) {
                super.visit(greaterThan);

                Long exp2 = stackLong.pop();
                Long exp1 = stackLong.pop();
                if (exp1 > exp2)
                    stackBool.push(true);
                else
                    stackBool.push(false);

            }

            @Override
            public void visit(GreaterThanEquals greaterThanEquals) {
                super.visit(greaterThanEquals);

                Long exp2 = stackLong.pop();
                Long exp1 = stackLong.pop();
                if (exp1 >= exp2)
                    stackBool.push(true);
                else
                    stackBool.push(false);

            }

            @Override
            public void visit(MinorThan minorThan) {
                super.visit(minorThan);

                Long exp2 = stackLong.pop();
                Long exp1 = stackLong.pop();
                if (exp1 < exp2)
                    stackBool.push(true);
                else
                    stackBool.push(false);

            }

            @Override
            public void visit(MinorThanEquals minorThanEquals) {
                super.visit(minorThanEquals);

                Long exp2 = stackLong.pop();
                Long exp1 = stackLong.pop();
                if (exp1 <= exp2)
                    stackBool.push(true);
                else
                    stackBool.push(false);
            }
        };

        parseExpression.accept(deparser);
        boolean result = (boolean)stackBool.pop();
        //System.out.println("result:" + result +"\n");
        return result;
    }
}
