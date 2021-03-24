package ed.inf.adbs.lightdb;

import org.junit.Test;

import java.util.Arrays;

public class ScanTest {
    DBCatalog dbc = DBCatalog.getInstance();

    @Test
    public void dump() {
        dbc.setDatabaseDir("samples/db");
        ScanOperator scan = new ScanOperator("S","Sailors");
        scan.dump();
    }

    @Test
    public void reset(){
        dbc.setDatabaseDir("samples/db");
        ScanOperator scan = new ScanOperator("S","Sailors");
        System.out.println(Arrays.toString(scan.getNextTuple().getTupleArray()));
        System.out.println(Arrays.toString(scan.getNextTuple().getTupleArray()));
        System.out.println(Arrays.toString(scan.getNextTuple().getTupleArray()));
        scan.reset();
        scan.dump();
    }
}
