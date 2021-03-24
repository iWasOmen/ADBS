package ed.inf.adbs.lightdb;

//import com.sun.org.apache.xerces.internal.xs.StringList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Tuple {

    private long[] tuple;
    private String tupleTableName;
    private String[] tupleSchema;

    public Tuple(String tupleStr,String tupleTableName) {
        this.tupleTableName = tupleTableName;
        DBCatalog dbc = DBCatalog.getInstance();
        tupleSchema = dbc.getTableSchema(tupleTableName);
        //System.out.println("tupleSchema111:"+tupleSchema.toString());
        String[] temp = tupleStr.split(",");
        tuple = new long[temp.length];
        for (int i = 0; i < temp.length; i++) {
            tuple[i] = Long.parseLong(temp[i]);
        }
    }

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


    public long[] getTupleArray() {
        return tuple;
    }

    public long getTupleNumber(String tableColumnName) {
        //不支持同名column
        HashMap<String, Long> schemaTupleMap = new HashMap<>();
        for (int i = 0; i < tuple.length; i++) {
            //System.out.println("tupleTableName:"+tupleTableName);
            //System.out.println("tableColumnName:"+tableColumnName);
            //schemaTupleMap.put(tupleSchema[0] + "." + tupleSchema[i + 1], tuple[i]);
            schemaTupleMap.put(tupleSchema[i + 1], tuple[i]);
            //schemaTupleMap.put(tupleSchema[i + 1], tuple[i]);
            //System.out.println("schemaTupleMap:"+schemaTupleMap);
        }
        //System.out.println("schemaTupleMap:"+schemaTupleMap);
        //System.out.println("tableColumnName:"+tableColumnName);
        return schemaTupleMap.get(tableColumnName);
    }

    public void projectTuple(List<String> selectTables, List<String> selectColums) {
        long[] tupleAfterProject = new long[selectTables.size()];
        for(int i = 0; i < selectTables.size(); i++){
            tupleAfterProject[i] = getTupleNumber(selectColums.get(i));
        }
        tuple = tupleAfterProject;
    }

    public String getTupleTableName(){
        return tupleTableName;
    }

    public String[] getTupleSchema(){return tupleSchema;}

//    public Tuple join(Tuple rightTuple){
////        long[] newTuple = new long[this.getTupleArray().length+rightTuple.getTupleArray().length];
////        String[] newTupleSchema = new String[this.getTupleSchema().length + rightTuple.getTupleSchema().length-1];
////        System.arraycopy(this.getTupleArray(),0,newTuple,0,this.getTupleArray().length);
////        System.arraycopy(rightTuple.getTupleArray(),0,newTuple,this.getTupleArray().length,rightTuple.getTupleArray().length);
//        String newTupleTableName = this.getTupleTableName()+rightTuple.getTupleTableName();
////        System.arraycopy(newTupleTableName,0,newTupleSchema,0,newTupleSchema.length);
////        System.arraycopy(this.getTupleSchema(),1,newTupleSchema,0,this.getTupleSchema().length-1);
//        List<Long> newTupleList = new ArrayList<>();
//        for(long tupleNumber:this.getTupleArray())
//            newTupleList.add(tupleNumber);
//        for(long tupleNumber:rightTuple.getTupleArray())
//            newTupleList.add(tupleNumber);
//        long[] newTuple = new long[newTupleList.size()];
//        for(int i =0;i<newTupleList.size();i++)
//            newTuple[i] = newTupleList.get(i);
//
//        List<String> newTupleSchemaList = new ArrayList<>();
//        for(int i =1;i<this.getTupleSchema().length;i++)
//            newTupleSchemaList.add(this.getTupleSchema()[i]);
//        for(int i =1;i<rightTuple.getTupleSchema().length;i++)
//            newTupleSchemaList.add(rightTuple.getTupleSchema()[i]);
//        String[] newTupleSchema = new String[newTupleSchemaList.size()+1];
//        newTupleSchema[0] = newTupleTableName;
//        for(int i =0;i<newTupleSchemaList.size();i++)
//            newTupleSchema[i+1] = newTupleSchemaList.get(i);
//
//        this.tuple = newTuple;
//        this.tupleTableName = newTupleTableName;
//        this.tupleSchema = newTupleSchema;
//        return this;
//    }
}