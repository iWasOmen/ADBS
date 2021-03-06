package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import java.util.Stack;

/**
 * SelectEvaluate Class to read select expression and gives a result.
 */
public class SelectEvaluate {
    private Expression parseExpression;
    private Tuple tuple;

    /**
     * Initialize SelectEvaluate Class and read the expression.
     * @param parseExpression select expression
     */
    public SelectEvaluate(Expression parseExpression) {
        this.parseExpression = parseExpression;
    }

    /**
     * Evaluate two tuple based on select expression, retrun the boolean.
     * @param tuple tuple
     * @return result
     */
    public boolean evaluate(Tuple tuple) {
        final Stack<Long> stackLong = new Stack<Long>();
        final Stack<Boolean> stackBool = new Stack<Boolean>();
        //System.out.println("--------------------------------------------");
        //System.out.println("tuple：" + Arrays.toString(tuple.getTupleArray()));
        //System.out.println("expression：" + parseExpression);

        ExpressionDeParser deparser = new ExpressionDeParser() {
            /**
             * Extends the ExpressionDeParser.
             * Use a long stack and a boolean stack to compute the result.
             */
            @Override
            public void visit(AndExpression andExpression) {
                super.visit(andExpression);
                Boolean exp1 = stackBool.pop();
                Boolean exp2 = stackBool.pop();
                stackBool.push(exp1 & exp2 );
            }

            @Override
            public void visit(Column column) {
                super.visit(column);
                String tableName = column.getTable().getName();
                String tableColumnName = column.getFullyQualifiedName();
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
                Long exp1 = stackLong.pop();
                Long exp2 = stackLong.pop();
                if(exp1.equals(exp2))
                    stackBool.push(true );
                else
                    stackBool.push(false );
            }

            @Override
            public void visit(NotEqualsTo notEqualsTo) {
                super.visit(notEqualsTo);
                Long exp1 = stackLong.pop();
                Long exp2 = stackLong.pop();
                if(!exp1.equals(exp2))
                    stackBool.push(true );
                else
                    stackBool.push(false );

            }

            @Override
            public void visit(GreaterThan greaterThan) {
                super.visit(greaterThan);
                Long exp2 = stackLong.pop();
                Long exp1 = stackLong.pop();
                if(exp1 > exp2)
                    stackBool.push(true );
                else
                    stackBool.push(false );

            }

            @Override
            public void visit(GreaterThanEquals greaterThanEquals) {
                super.visit(greaterThanEquals);

                Long exp2 = stackLong.pop();
                Long exp1 = stackLong.pop();
                if(exp1 >= exp2)
                    stackBool.push(true );
                else
                    stackBool.push(false );

            }

            @Override
            public void visit(MinorThan minorThan) {
                super.visit(minorThan);

                Long exp2 = stackLong.pop();
                Long exp1 = stackLong.pop();
                if(exp1 < exp2)
                    stackBool.push(true );
                else
                    stackBool.push(false );

            }

            @Override
            public void visit(MinorThanEquals minorThanEquals) {
                super.visit(minorThanEquals);

                Long exp2 = stackLong.pop();
                Long exp1 = stackLong.pop();
                if(exp1 <= exp2)
                    stackBool.push(true );
                else
                    stackBool.push(false );

            }

        };
        parseExpression.accept(deparser);
        boolean result = (boolean)stackBool.pop();

        return result;
    }
}
