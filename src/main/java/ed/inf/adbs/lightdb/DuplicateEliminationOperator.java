package ed.inf.adbs.lightdb;

import java.util.Arrays;
import java.util.List;

/**
 * DuplicateEliminationOperator to realize distinct function.
 */
public class DuplicateEliminationOperator extends Operator {
    Operator child;
    Tuple lastTuple;
    boolean ifFirstTuple = true;


    public DuplicateEliminationOperator(Operator child){
        this.child = child;
    }

    /**
     * Get next tuple.
     * @return tuple
     */
    @Override
    public Tuple getNextTuple() {
        Tuple tuple;
        while((tuple = child.getNextTuple())!= null) {
            if ((tuple = duplicateElimination(tuple)) == null)
                continue;
            return tuple;
        }
        return null;
    }

    /**
     * Delete the duplication. Has a ifFirstTuple flag to determine if it is first run
     * in order to judge whether assign value to lastTuple or not. If the new tuple is same as last tuple,
     * return null.
     * @param tuple tuple
     * @return tuple
     */
    private Tuple duplicateElimination(Tuple tuple){
        if(ifFirstTuple) {
            ifFirstTuple = false;
            lastTuple = tuple;
            return tuple;
        }
        else {
            if (Arrays.equals(tuple.getTupleArray(), lastTuple.getTupleArray()))
                return null;
            else {
                lastTuple = tuple;
                return tuple;
            }
        }

    }

    /**
     * reset operator.
     */
    @Override
    public void reset() {
        child.reset();
    }
}
