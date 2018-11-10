package com.mycompany.pi4.Usuarios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                    String foto = rs.getString("foto");

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
   @POST
   @Consumes("application/json;charset=utf-8")   
   @Produces("application/json;charset=utf-8")
   public Response setUser (Users user) throws SQLException {
        Response response;
        Long usuarioCriado = null;
        
        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement stmt = conn.prepareStatement("select email from usuario where email = ?")) {
                
                stmt.setString(1, user.getEmail());
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    response = Response.status(406).entity("Usuario já existe").build();
                    return response;
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response = Response.status(500).entity(ex.getMessage()).build();
            return response;
        }

        try {
            Class.forName(DRIVER);
            String sql = "INSERT INTO usuario (nome,email,senha,foto) VALUES (?,?,?,?)";
            
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                               
                stmt.setString(1, user.getNome());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getSenha());
                String foto = user.getFoto();
                byte[] fotoEmByte = Base64.getDecoder().decode (foto);
                
                stmt.setBytes(4, fotoEmByte);
                
                int rs = stmt.executeUpdate();
                
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuarioCriado = generatedKeys.getLong(1);
                    }
                }
                
                response = Response.ok(usuarioCriado).build();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response = Response.status(500).entity("ERRO NO CADASTRO: "+ex.getMessage()).build();
        }

        return response;
   }
}
