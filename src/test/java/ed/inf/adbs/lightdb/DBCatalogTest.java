package ed.inf.adbs.lightdb;

import org.junit.Test;

import java.util.Arrays;

public class DBCatalogTest {
    DBCatalog dbc = DBCatalog.getInstance();

    @Test public void getSchema(){
        dbc.setDatabaseDir("samples/db");
        System.out.println("Schemaget:" + Arrays.toString(dbc.getTableSchema("Sailors")));
    }
}
