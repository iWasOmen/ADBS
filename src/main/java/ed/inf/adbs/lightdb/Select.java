package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.expression.Expression;

public class Select extends Operator {
    private SelectEvaluate se;
    Operator child;
    public Select(Expression parseExpression,Operator child){
        se = new SelectEvaluate(parseExpression);
        this.child = child;
    }

    @Override
    public Tuple getNextTuple() {
        Tuple tuple = child.getNextTuple();
        if(se.evaluate(tuple) == true){
            return tuple;
        }
        return null;
    }

    @Override
    public void reset() {

    }
}
