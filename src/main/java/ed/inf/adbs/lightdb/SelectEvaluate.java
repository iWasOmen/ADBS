package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

import java.util.Arrays;
import java.util.Stack;

public class SelectEvaluate {
    private Expression parseExpression;
    private Tuple tuple;

    public SelectEvaluate(Expression parseExpression) {
        this.parseExpression = parseExpression;
    }

    public boolean evaluate(Tuple tuple) {
        final Stack<Long> stackLong = new Stack<Long>();
        final Stack<Boolean> stackBool = new Stack<Boolean>();
        System.out.println("tuple：" + Arrays.toString(tuple.getTupleArray()));
        System.out.println("expression：" + parseExpression);
        ExpressionDeParser deparser = new ExpressionDeParser() {
            @Override
            public void visit(AndExpression andExpression) {
                super.visit(andExpression);

                Boolean exp1 = stackBool.pop();
                Boolean exp2 = stackBool.pop();

                stackBool.push(exp1 & exp2 );
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
                if(exp1 == exp2)
                    stackBool.push(true );
                else
                    stackBool.push(false );

            }
        };
        parseExpression.accept(deparser);
        boolean result = (boolean)stackBool.pop();
        System.out.println("result:" + result +"\n" );
        return result;
    }
}
