package ed.inf.adbs.lightdb;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * ScanOperator Class to support scan file and generate tuple.
 */
public class ScanOperator extends Operator {
    private String dir;
    private BufferedReader br;
    private String tableName;

    /**
     * Initialize ScanOperator by tableName(alias) and oringinalTableName.
     * @param tableName tableName(alias)
     * @param oringinalTableName oringinalTableName
     */
    public ScanOperator(String tableName, String oringinalTableName){
        try
        {
            DBCatalog dbc = DBCatalog.getInstance();
            dir = dbc.getTablePath(oringinalTableName);
            FileReader fr = new FileReader(dir);
            br = new BufferedReader(fr);
            this.tableName = tableName;
        }
        catch (Exception e)
        {
            System.err.println("Failed to open file");
            e.printStackTrace();
        }
    }

    /**
     * Get next tuple.
     * @return tuple
     */
    @Override
    public Tuple getNextTuple(){
        try{
        String nextline = br.readLine();
        if(nextline == null)
            return null;
        else{
            String tupleStr = nextline;
            Tuple tuple = new Tuple(tupleStr,tableName);
            return tuple;
        }}
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * reset operator.
     */
    @Override
    public void reset() {
        try {
            FileReader fr = new FileReader(dir);
            br = new BufferedReader(fr);
        }
        catch (Exception e)
        {
            System.err.println("Failed to open file");
            e.printStackTrace();
        }
    }

    /**
     * Get the table name.
     * @return tableName
     */
    public String getTableName(){
        return tableName;
    }
}
