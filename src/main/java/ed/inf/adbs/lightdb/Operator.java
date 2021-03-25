package ed.inf.adbs.lightdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract Operator Class with getNextTuple(), reset(), dump() function.
 */
public abstract class Operator {

    /**
     * Get next tuple.
     * @return tuple
     */
    public abstract Tuple getNextTuple();

    /**
     * reset operator.
     */
    public abstract void reset();

    /**
     * Dump the operator to get all of the result.
     * @return a list of Tuple.
     */
    public List<Tuple> dump(){
        Tuple tuple;
        List<Tuple> allTupleList = new ArrayList<>();
        while((tuple = getNextTuple()) != null) {
            allTupleList.add(tuple);
            System.out.println("right tuple:" + Arrays.toString(tuple.getTupleArray()));
        }
        return allTupleList;
    }
}
