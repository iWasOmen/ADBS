package ed.inf.adbs.lightdb;

import net.sf.jsqlparser.expression.Expression;

public class JoinOperator extends Operator {

    Operator leftChild;
    Operator rightChild;
    JoinEvaluate joinEvaluate;
    Tuple leftTuple;
    Expression parseExpression;

    public JoinOperator(Expression parseExpression, Operator leftChild, Operator rightChild){
        this.parseExpression = parseExpression;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        if(parseExpression != null) {
            joinEvaluate = new JoinEvaluate(parseExpression);
            //this.leftTuple = leftChild.getNextTuple();
        }
    }

    public void setExpression(Expression parseExpression){
        if(parseExpression != null) {
            this.parseExpression = parseExpression;
            joinEvaluate = new JoinEvaluate(parseExpression);
            System.out.println("set parseExpression:"+parseExpression);
            //this.leftTuple = leftChild.getNextTuple();
        }
    }

    public void initialize(){
        this.leftTuple = leftChild.getNextTuple();
    }

    @Override
    public Tuple getNextTuple() {
        //Tuple leftTuple = this.leftTuple;
        Tuple rightTuple;
        while (leftTuple != null){
            while ((rightTuple = rightChild.getNextTuple()) != null) {
                if(parseExpression == null){
                    //System.out.println("parseExpression null");
                    Tuple newTuple = new Tuple(leftTuple,rightTuple);
                    return newTuple;
                }
                else if (joinEvaluate.evaluate(leftTuple, rightTuple)) {
                    //System.out.println("通过评估");
                    Tuple newTuple = new Tuple(leftTuple,rightTuple);
                    return newTuple;
                }
            }
            rightChild.reset();
            leftTuple = leftChild.getNextTuple();
        }
        return null;
    }

    @Override
    public void reset() {
        leftChild.reset();
        rightChild.reset();
    }

    @Override
    public String getTableName() {
        return null;
    }
}
