package ed.inf.adbs.lightdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

/**
 * Database Catlog Class to look for where a file for a given table is located,
 * and what the schema of different tables is.
 * Use singleton pattern.
 */
public class DBCatalog {

    private String databaseDir;
    private String tableDir;
    private String tableSchemaDir;
    //use CONSTANT_TABLE as the name for constant value
    private static String CONSTANT_TABLE_NAME= "CONSTANT_TABLE";
    private static DBCatalog instance=new DBCatalog();
    private HashMap<String, String[]> tableSchemaMap;

    /**
     * Return the instance of database catlog.
     * @return the instance of database catlog
     */
    public static DBCatalog getInstance(){
        return instance;
    }

    /**
     * Set the database directory.
     * @param databaseDir
     */
    public void setDatabaseDir(String databaseDir){
        this.databaseDir =databaseDir;
    }

    /**
     * Connect strings and return the directory of one table
     * @param tableName the name of table
     * @return the directory of one table
     */
    public String getTablePath(String tableName){
        tableDir = databaseDir + File.separator + "data" + File.separator + tableName + ".csv";
        return tableDir;
    }

    /**
     * Set a map of table name(use alias if has one) and schema.
     * The column name will be changed into tableName.columnName format.
     * @param tableNames a list of tablenames(will be alias name if has one)
     * @param oringinalTableNames a list of oringinal table names
     */
    public void setTableSchema(List<String> tableNames, List<String> oringinalTableNames){
        tableSchemaMap = new HashMap<>();
        tableSchemaDir = databaseDir + File.separator + "schema.txt";
        HashMap<String,String> tableNamesOringinalTableNamesMap = new HashMap<>();
        for(int i = 0; i < tableNames.size(); i++) {
            tableNamesOringinalTableNamesMap.put(tableNames.get(i),oringinalTableNames.get(i));
        }
        //System.out.println("tableNamesOringinalTableNamesMap:"+tableNamesOringinalTableNamesMap);
        try
        {
            FileReader fr = new FileReader(tableSchemaDir);
            BufferedReader br = new BufferedReader(fr);
            String nextline;
            String[] oneSchema;
            while((nextline = br.readLine()) != null)
            {
                oneSchema = nextline.split(" ");
                String SchemaName = oneSchema[0];
                for(String tableNamesOringinalTableNamesMapKey:tableNamesOringinalTableNamesMap.keySet())
                {
                    if(tableNamesOringinalTableNamesMap.get(tableNamesOringinalTableNamesMapKey).equals(SchemaName)){
                        String[] newSchema = oneSchema.clone();
                        newSchema[0] = tableNamesOringinalTableNamesMapKey;
                        for(int i = 1; i < oneSchema.length; i++)
                            newSchema[i] = tableNamesOringinalTableNamesMapKey + "." + newSchema[i];
                        //String newSchemaName = tableNamesOringinalTableNamesMap.get(tableNamesOringinalTableNamesMapKey);
                        tableSchemaMap.put(newSchema[0],newSchema);
                        //System.out.println("tableSchemaMap:"+ tableSchemaMap);
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("Failed to open file");
            e.printStackTrace();
        }
    }

    /**
     * Get one table's schema through map.
     * @param tableName table's name(will be alias name if has one)
     * @return the schema of the input table. Format: [tableName, tableName.column1Name, tableName.column2Name...]
     */
    public String[] getTableSchema(String tableName){
        return tableSchemaMap.get(tableName);
    }

    /**
     * Get the constant table name.
     * @return the constant table name
     */
    public String getConstantTableName(){
        return CONSTANT_TABLE_NAME;
    }
}
