package ed.inf.adbs.lightdb;

import java.util.HashMap;
import java.util.List;

/**
 * Tuple Class to store information about tuple data, tupleTableName, and tupleSchema.
 */
public class Tuple {

    private long[] tuple;
    private String tupleTableName;
    private String[] tupleSchema;

    /**
     * Initialize a Tuple Class for scan operator.
     * @param tupleStr the string tuple from file
     * @param tupleTableName tupleTableName
     */
    public Tuple(String tupleStr,String tupleTableName) {
        this.tupleTableName = tupleTableName;
        DBCatalog dbc = DBCatalog.getInstance();
        tupleSchema = dbc.getTableSchema(tupleTableName);
        //System.out.println("tupleSchema:"+tupleSchema.toString());
        String[] temp = tupleStr.split(",");
        tuple = new long[temp.length];
        for (int i = 0; i < temp.length; i++) {
            tuple[i] = Long.parseLong(temp[i]);
        }
    }

    /**
     * Initialize a Tuple Class for join operator.
     * Connect left and right to become a new tuple.
     * @param leftTuple leftTuple
     * @param rightTuple rightTuple
     */
    public Tuple(Tuple leftTuple, Tuple rightTuple){
        long[] newTuple = new long[leftTuple.getTupleArray().length+rightTuple.getTupleArray().length];
        String[] newTupleSchema = new String[leftTuple.getTupleSchema().length + rightTuple.getTupleSchema().length-1];
        String newTupleTableName = leftTuple.getTupleTableName()+rightTuple.getTupleTableName();
        System.arraycopy(leftTuple.getTupleArray(),0,newTuple,0,leftTuple.getTupleArray().length);
        System.arraycopy(rightTuple.getTupleArray(),0,newTuple,leftTuple.getTupleArray().length,rightTuple.getTupleArray().length);
        newTupleSchema[0] = newTupleTableName;
        System.arraycopy(leftTuple.getTupleSchema(),1,newTupleSchema,1,leftTuple.getTupleSchema().length - 1);
        System.arraycopy(rightTuple.getTupleSchema(),1,newTupleSchema,leftTuple.getTupleSchema().length,rightTuple.getTupleSchema().length-1);

        this.tuple = newTuple;
        this.tupleTableName = newTupleTableName;
        this.tupleSchema = newTupleSchema;
        //System.out.println("tupleSchema:"+newTupleSchema);
    }

    /**
     * Get the tuple array.
     * @return tuple array
     */
    public long[] getTupleArray() {
        return tuple;
    }

    /**
     * Get a column data based on tableColumnName.
     * @param tableColumnName tableColumnName. Input format example: S.A
     * @return the column data of that tableColumnName
     */
    public long getTupleNumber(String tableColumnName) {
        HashMap<String, Long> schemaTupleMap = new HashMap<>();
        for (int i = 0; i < tuple.length; i++) {
            schemaTupleMap.put(tupleSchema[i + 1], tuple[i]);
        }
        return schemaTupleMap.get(tableColumnName);
    }

    /**
     * Project tuple based on selectTables and selectColums.
     * @param selectTables selectTables
     * @param selectColums selectColums
     */
    public void projectTuple(List<String> selectTables, List<String> selectColums) {
        long[] tupleAfterProject = new long[selectTables.size()];
        String[] tupleSchemaAfterProject = new String[selectTables.size()+1];
        tupleSchemaAfterProject[0] = tupleSchema[0];
        for(int i = 0; i < selectTables.size(); i++){
            tupleAfterProject[i] = getTupleNumber(selectColums.get(i));
            tupleSchemaAfterProject[i] = selectColums.get(i);
        }
        tuple = tupleAfterProject;
        tupleSchema =tupleSchemaAfterProject;
    }

    /**
     * Get the tuple table name.
     * Use alias if has one.
     * @return tupleTableName
     */
    public String getTupleTableName(){
        return tupleTableName;
    }

    /**
     * Get the tuple schema. Format example: [S, S.A, S.B, S.C]
     * @return tupleSchema
     */
    public String[] getTupleSchema(){ return tupleSchema;}
}