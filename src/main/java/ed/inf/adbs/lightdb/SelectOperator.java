package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.expression.Expression;

/**
 * SelectOperator Class to support select function.
 */
public class SelectOperator extends Operator {

    private SelectEvaluate selectEvaluate;
    Operator child;

    /**
     * Initialize a SelectOperator Class.
     * @param parseExpression select expression
     * @param child child operator
     */
    public SelectOperator(Expression parseExpression, Operator child){
        selectEvaluate = new SelectEvaluate(parseExpression);
        this.child = child;
    }

    /**
     * Get next tuple.
     * @return tuple
     */
    @Override
    public Tuple getNextTuple() {
        Tuple tuple;
        while ((tuple = child.getNextTuple()) != null){
            if(selectEvaluate.evaluate(tuple))
                return tuple;
        }
        return null;
    }

    /**
     * reset operator.
     */
    @Override
    public void reset() {
        child.reset();
    }


}
