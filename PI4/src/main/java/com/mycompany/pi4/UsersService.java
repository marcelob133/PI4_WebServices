package com.mycompany.pi4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/users")
public class UsersService {
    
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://pijulio.database.windows.net:1433;database=facenac";
    private static final String USER = "julio@pijulio";
    private static final String PASS = "Abcd123!";
   
    
   @GET
   @Path("/{id}")
   @Produces("application/json;charset=utf-8")
   public Response getUser (@PathParam("id") Long idUser) {
        Response response;

        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement stmt = conn.prepareStatement("select * from usuario where id = ?")) {
                
                if (idUser == 0) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                
                stmt.setLong(1, idUser);
                ResultSet rs = stmt.executeQuery();
                
                List<Users> usuariosList = new ArrayList<>();
                
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    String nome = rs.getString("nome");
                    String email = rs.getString("email");
                    String senha = rs.getString("senha");
                    byte[] foto = rs.getBytes("foto");

                    Users usuario = new Users(id, nome, email, senha, foto);
                    usuariosList.add(usuario);
                }
                response = Response.ok(usuariosList).build();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response = Response.serverError().entity(ex.getMessage()).build();
        }

        return response;
   }
  /* @POST
   @Path("/{nome}/{email}/{senha}/{foto}")
   @Produces("application/json;charset=utf-8")
   public Response setUser (@PathParam("nome") String nomeUser, @PathParam("email") String emailUser, @PathParam("senha") String senhaUser @PathParam("foto") byte[] foto) {
        Response response;

        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO usuario VALUES ") {
                               
                stmt.setLong(1, idUser);
                ResultSet rs = stmt.executeQuery();
                
                List<Users> usuariosList = new ArrayList<>();
                
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    String nome = rs.getString("nome");
                    String email = rs.getString("email");
                    String senha = rs.getString("senha");
                    byte[] foto = rs.getBytes("foto");

                    Users usuario = new Users(id, nome, email, senha, foto);
                    usuariosList.add(usuario);
                }
                response = Response.ok(usuariosList).build();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response = Response.serverError().entity(ex.getMessage()).build();
        }

        return response;
   }*/
}
