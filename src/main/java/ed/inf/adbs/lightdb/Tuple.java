package ed.inf.adbs.lightdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Tuple {

    private long[] tuple;
    private String tupleTableName;

    public Tuple(String tupleStr) {
        String[] temp = tupleStr.split(",");
        tuple = new long[temp.length];
        for (int i = 0; i < temp.length; i++) {
            tuple[i] = Long.parseLong(temp[i]);
        }
    }


    public long[] getTupleArray() {
        return tuple;
    }

    public long getTupleNumber(String tableName,String columnName) {
        DBCatalog dbc = DBCatalog.getInstance();
        String[] tableSchema = dbc.getTableSchema(tableName);
        HashMap<String, Long> schemaTupleMap = new HashMap<>();
        for (int i = 0; i < tuple.length; i++) {
            schemaTupleMap.put(tableSchema[i + 1], tuple[i]);
        }
        return schemaTupleMap.get(columnName);
    }

    public void projectTuple(List<String> selectTables, List<String> selectColums) {
        long[] tupleAfterProject = new long[selectTables.size()];
        for(int i = 0; i < selectTables.size(); i++){
            tupleAfterProject[i] = getTupleNumber(selectTables.get(i),selectColums.get(i));
        }
        tuple = tupleAfterProject;
    }
}