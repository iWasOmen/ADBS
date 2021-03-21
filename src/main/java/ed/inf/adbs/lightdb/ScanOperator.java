package ed.inf.adbs.lightdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ScanOperator extends Operator {
    private String dir;
    private BufferedReader br;

    public ScanOperator(String tableName){
        try
        {
            DBCatalog dbc = DBCatalog.getInstance();
            dir = dbc.getTablePath(tableName);
            FileReader fr = new FileReader(dir);
            br = new BufferedReader(fr);
        }
        catch (Exception e)
        {
            System.err.println("Failed to open file");
            e.printStackTrace();
        }
    }

    @Override
    public Tuple getNextTuple(){
        try{
        String nextline = br.readLine();
        if(nextline == null)
            return null;
        else{
            String tupleStr = nextline;
            Tuple tuple = new Tuple(tupleStr);
            return tuple;
        }}
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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
}
