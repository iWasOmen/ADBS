package ed.inf.adbs.lightdb;

import java.io.IOException;
import java.util.Arrays;

public abstract class Operator {
    public abstract Tuple getNextTuple();
    public abstract void reset();

    public void dump(){
        Tuple tuple;
        while((tuple = getNextTuple()) != null) {
            System.out.println("right tuple:" + Arrays.toString(tuple.getTupleArray()));
        }
    }

}
