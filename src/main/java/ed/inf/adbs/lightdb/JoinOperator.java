package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.expression.Expression;

public class JoinOperator extends Operator {

    Operator leftChild;
    Operator rightChild;
    JoinEvaluate joinEvaluate;

    public JoinOperator(Expression parseExpression, Operator leftChild, Operator rightChild){
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        joinEvaluate = new JoinEvaluate(parseExpression);
    }


    @Override
    public Tuple getNextTuple() {
        Tuple leftTuple;
        Tuple rightTuple;
        while ((leftTuple = leftChild.getNextTuple()) != null){
            while ((rightTuple = rightChild.getNextTuple()) != null) {
                if (joinEvaluate.evaluate(leftTuple, rightTuple)) {
                    leftTuple.join(rightTuple);
                    return leftTuple;
                }
            }
            rightChild.reset();
        }
        return null;
    }

    @Override
    public void reset() {
        leftChild.reset();
        rightChild.reset();
    }
}
