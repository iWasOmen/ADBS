package ed.inf.adbs.lightdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DBCatalog {

    private String databaseDir;
    private String tableDir;
    private String tableSchemaDir;
    private static String CONSTANT_TABLE_NAME= "CONSTANT_TABLE";
    private static DBCatalog instance=new DBCatalog();

    private DBCatalog(){

    };

    public static DBCatalog getInstance(){
        return instance;
    }

    public void setDatabaseDir(String databaseDir){
        this.databaseDir =databaseDir;
    }

    public String getTablePath(String tableName){
        tableDir = databaseDir + File.separator + "data" + File.separator + tableName + ".csv";
        return tableDir;
    }
    public String[] getTableSchema(String tableName){
        tableSchemaDir = databaseDir + File.separator + "schema.txt";
        try
        {
            FileReader fr = new FileReader(tableSchemaDir);
            BufferedReader br = new BufferedReader(fr);
            String nextline;
            String[] oneSchema;
            while((nextline = br.readLine()) != null)
            {
                oneSchema = nextline.split(" ");
                //System.out.println(oneSchema[0]);
                if (oneSchema[0].equals(tableName))
                    return oneSchema;
            }
            //return br.readLine();
        }
        catch (Exception e)
        {
            System.err.println("Failed to open file");
            e.printStackTrace();
        }
        return null;
    }

    public String getConstantTableName(){
        return CONSTANT_TABLE_NAME;
    }
}
