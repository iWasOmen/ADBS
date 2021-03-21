package ed.inf.adbs.lightdb;

import java.io.IOException;
import java.util.Arrays;

public abstract class Operator {
    public abstract Tuple getNextTuple();
    public abstract void reset();

    public void dump(){
        Tuple tuple = getNextTuple();
        while(tuple != null) {
            System.out.println(Arrays.toString(tuple.getTupleArray()));
            tuple = getNextTuple();
        }
    }

}
