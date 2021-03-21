package ed.inf.adbs.lightdb;

import java.io.File;

public class DBCatalog {

    private String tableDir;
    private static DBCatalog instance=new DBCatalog();

    private DBCatalog(){

    };

    public static DBCatalog getInstance(){
        return instance;
    }

    public String getTablePath(String tableName){
        tableDir = File.separator + "data" + File.separator + tableName + ".csv";
        return tableDir;
    }

}
