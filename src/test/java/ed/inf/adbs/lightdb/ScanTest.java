package ed.inf.adbs.lightdb;

import org.junit.Test;

import java.util.Arrays;

public class ScanTest {
    Scan scan = new Scan("samples/db", "Sailors");

    @Test
    public void dump() {
        scan.dump();
    }

    @Test
    public void reset(){
        System.out.println(Arrays.toString(scan.getNextTuple().getTupleArray()));
        System.out.println(Arrays.toString(scan.getNextTuple().getTupleArray()));
        System.out.println(Arrays.toString(scan.getNextTuple().getTupleArray()));
        scan.reset();
        scan.dump();
    }
}
