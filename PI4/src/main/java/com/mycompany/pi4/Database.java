package com.mycompany.pi4;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    
    private static Database instance = null;
    
    private Database(){};
    
    public static Database get () {
        if(instance == null)
            instance = new Database();
        return instance;
    }
    
    public Connection conn () throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://pijulio.database.windows.net:1433;user=julio@pijulio.database.windows.net;password=Abcd123!;database=facenac");
        return conn;
    }
}
