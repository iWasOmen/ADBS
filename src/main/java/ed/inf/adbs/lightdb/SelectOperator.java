package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.expression.Expression;

public class SelectOperator extends Operator {
    private SelectEvaluate selectEvaluate;
    Operator child;
    public SelectOperator(Expression parseExpression, Operator child){
        selectEvaluate = new SelectEvaluate(parseExpression);
        this.child = child;
    }

    @Override
    public Tuple getNextTuple() {
        Tuple tuple;
        while ((tuple = child.getNextTuple()) != null){
            if(selectEvaluate.evaluate(tuple))
                return tuple;
        }
        return null;
    }

    @Override
    public void reset() {
        child.reset();
    }
}
