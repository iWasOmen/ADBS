package ed.inf.adbs.lightdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class DBCatalog {

    private String databaseDir;
    private String tableDir;
    private String tableSchemaDir;
    private static String CONSTANT_TABLE_NAME= "CONSTANT_TABLE";
    private static DBCatalog instance=new DBCatalog();
    private HashMap<String, String[]> tableSchemaMap;

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
    public String[] setTableSchema(List<String> tableNames, List<String> oringinalTableNames){
        tableSchemaMap = new HashMap<>();
        tableSchemaDir = databaseDir + File.separator + "schema.txt";
        HashMap<String,String> oringinalTableNamesTableNamesMap = new HashMap<>();
        for(int i = 0; i < tableNames.size(); i++)
            oringinalTableNamesTableNamesMap.put(oringinalTableNames.get(i),tableNames.get(i));
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
                if (oringinalTableNamesTableNamesMap.containsKey(oneSchema[0])) {
                    oneSchema[0] = oringinalTableNamesTableNamesMap.get(oneSchema[0]);
                    tableSchemaMap.put(oneSchema[0],oneSchema);
                }
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

    public String[] getTableSchema(String tableName){
        return tableSchemaMap.get(tableName);
    }

    public String getConstantTableName(){
        return CONSTANT_TABLE_NAME;
    }
}
