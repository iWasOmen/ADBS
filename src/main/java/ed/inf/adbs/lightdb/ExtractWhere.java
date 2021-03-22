package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

import java.util.Stack;

public class ExtractWhere {
    ExpressionList selectExpressionList = new ExpressionList();
    ExpressionList whereExpressionList = new ExpressionList();

    public ExtractWhere(Expression parseExpression) {
        final Stack<Expression> stackExpression = new Stack<Expression>();
        final Stack<String> stackString = new Stack<String>();
        ExpressionDeParser deparser = new ExpressionDeParser() {

            //compare two expression, and decide to add to which ExpressionList
            public void compareExpression(Expression expression) {
                Expression exp2 = stackExpression.pop();
                Expression exp1 = stackExpression.pop();
                String str2 = stackString.pop();
                String str1 = stackString.pop();
                //both or one of exp is long, add to oneSelectExpression
                if (exp1.getClass() == LongValue.class || exp2.getClass() == LongValue.class) {
                    selectExpressionList.addExpressions(expression);
                }
                //both of two exps are columns
                else {
                    //two exps have same table names, add to oneSelectExpression
                    if (str2.equals(str1))
                        selectExpressionList.addExpressions(expression);
                        //two exps have different table name, add to oneWhereExpression
                    else
                        whereExpressionList.addExpressions(expression);
                }
            }


//            @Override
//            public void visit(AndExpression andExpression) {
//                super.visit(andExpression);
//            }

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
                //System.out.println("qqqq");

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
//        StringBuilder b = new StringBuilder();
//        deparser.setBuffer(b);
        parseExpression.accept(deparser);
//        boolean result = (boolean)stackBool.pop();
//        System.out.println("result:" + result +"\n");


    }

    public ExpressionList getSelectExpressionList(){
        //return selectExpressionList;
        return selectExpressionList;
    }

    public ExpressionList getWhereExpressionList(){
        //return whereExpressionList;
        return whereExpressionList;
    }

}
