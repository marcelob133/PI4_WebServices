package com.mycompany.pi4;

import java.sql.Connection;
import java.sql.DriverManager;

public class Program {
    
    try{
        String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDrive";
        String URL = "jdbc:sqlserver://pijulio.database.windows.net:1433;database=facenac";
        String USER = "Abcd123!";
        String PASS = "julio@pijulio.database.windows.net";
    
        Class.forName(DRIVER);
        Connection conn = DriverManager.getConnection(URL, USER, PASS);
        PrepareStatement stmt = conn.prepareStatement("select * from usuario order by name");
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            Long id = rs.getLong("id"); 
            String nome = rs.getString("nome");
            String email = rs.getString("email");
            String senha = rs.getString("senha");
            byte[] foto = rs.getBytes("foto");
        }
        
    }catch(ClassNotFoundException ex){
        System.out.println(ex.getMessage());    
    }catch(SQLException ex){
        System.out.println(ex.getMessage());
    }
}
