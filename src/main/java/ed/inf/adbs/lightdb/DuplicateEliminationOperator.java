package ed.inf.adbs.lightdb;

import java.util.Arrays;
import java.util.List;

public class DuplicateEliminationOperator extends Operator {
    Operator child;
    Tuple lastTuple;
    boolean ifFirstTuple = true;
    List<Tuple> allTupleList;


    public DuplicateEliminationOperator(Operator child){
        this.child = child;
    }
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


    @Override
    public void reset() {
        child.reset();
    }
}
