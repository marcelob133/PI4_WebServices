package com.mycompany.pi4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/historia")
public class PostsService {
    
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://pijulio.database.windows.net:1433;database=facenac";
    private static final String USER = "julio@pijulio";
    private static final String PASS = "Abcd123!";
    
   @POST
   @Consumes("application/json;charset=utf-8")   
   @Produces("application/json;charset=utf-8")
   public Response setHistoria (Posts post) throws SQLException {
        Response response;
        Long postCriado = null;
        
        try {
            Class.forName(DRIVER);
            String sql = "INSERT INTO historico (usuario,texto,foto,data) VALUES (?,?,?,?)";
            
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                               
                stmt.setLong(1, post.getUsuario());
                stmt.setString(2, post.getTexto());
        
                String foto = post.getFoto();
                byte[] fotoEmByte = Base64.getDecoder().decode (foto);
                stmt.setBytes(3, fotoEmByte);
                
                Timestamp t = new Timestamp(System.currentTimeMillis());
                stmt.setTimestamp(4, t);
                
                int rs = stmt.executeUpdate();
                
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        postCriado = generatedKeys.getLong(1);
                    }
                }
                
                response = Response.ok(postCriado).build();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response = Response.serverError().entity("ERRO NO CADASTRO: "+ex.getMessage()).build();
        }
        
        return response;
   }
}