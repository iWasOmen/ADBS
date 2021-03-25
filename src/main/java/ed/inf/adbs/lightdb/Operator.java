package ed.inf.adbs.lightdb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Operator {
    public abstract Tuple getNextTuple();
    public abstract void reset();



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
