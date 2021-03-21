package ed.inf.adbs.lightdb;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

public class TupleTest {

    @Test
    public void getTuple(){
        Tuple tuple = new Tuple("104,104,2");
        System.out.println(Arrays.toString(tuple.getTupleArray()));
    }
}
