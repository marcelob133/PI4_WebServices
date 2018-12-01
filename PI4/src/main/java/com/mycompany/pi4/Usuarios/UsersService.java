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
   @Path("/list/{id}")
   @Produces("application/json;charset=utf-8")
    public Response getAllUsers (@PathParam("id") Long idUser){
        Response response;
        
        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement("select id, nome, email, senha, temFoto = CASE WHEN foto is null  THEN 0 ELSE 1 END from usuario WHERE id != ?")) {
                
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
                    Integer temFoto = rs.getInt("temFoto");

                    Users usuario = new Users(id, nome, email, senha, temFoto);
                    usuariosList.add(usuario);
                }
                response = Response.ok(usuariosList).build();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response = Response.serverError().entity(ex.getMessage()).build();
        }

       return response; 
   }
    
   @GET
   @Path("/{id}")
   @Produces("application/json;charset=utf-8")
   public Response getUser (@PathParam("id") Long idUser) {
        Response response;

        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement("select id, nome, email, senha, temFoto = CASE WHEN foto is null  THEN 0 ELSE 1 END from usuario WHERE id = ?")) {
                
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
                    Integer temFoto = rs.getInt("temFoto");

                    Users usuario = new Users(id, nome, email, senha, temFoto);
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
   
   @GET
   @Path("/search/{idUser}/{query}")
   @Produces("application/json;charset=utf-8")
   public Response searchUser(@PathParam("idUser") Long idUser, @PathParam("query") String query) {
       Response response = null;
       
       try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement("SELECT u.id, u.nome, u.email, u.senha, temFoto = CASE WHEN u.foto is null THEN 0 ELSE 1 END, amizade = CASE WHEN a.aprovada = 1  THEN 'amigos' WHEN a.aprovada = 0 AND a.usuario1 = ? THEN 'solicitante' WHEN a.aprovada = 0 AND a.usuario2 = ? THEN 'solicitado' ELSE null END FROM Usuario u  LEFT JOIN Amizade a ON u.id = a.usuario1 OR u.id = a.usuario2 WHERE (u.nome LIKE ? OR u.email LIKE ?) AND id != ? ORDER BY aprovada")) {
                
                if (idUser == 0) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                stmt.setLong(1, idUser);
                stmt.setLong(2, idUser);
                stmt.setString(3, "%" + query + "%");
                stmt.setString(4, "%" + query + "%");
                stmt.setLong(5, idUser);
                
                ResultSet rs = stmt.executeQuery();
                
                List<Users> usuariosList = new ArrayList<>();
                
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    String nome = rs.getString("nome");
                    String email = rs.getString("email");
                    String senha = rs.getString("senha");
                    Integer temFoto = rs.getInt("temFoto");
                    String amizade = rs.getString("amizade");

                    Users usuario = new Users(id, nome, email, senha, temFoto, amizade);
                    usuariosList.add(usuario);
                }
                response = Response.ok(usuariosList).build();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response = Response.serverError().entity(ex.getMessage()).build();
        }
       
       return response;
   }
   
   
   @GET
   @Path("/image/{idUsuario}")
   @Produces("image/jpeg")
   public Response getImage (@PathParam("idUsuario") Long idUser) throws SQLException {
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
                
               
                
                while (rs.next()) {                    
                    byte[] data = rs.getBytes("foto");
                    response = Response.ok(data).build();
                    return response;
                }
              response = Response.serverError().build();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response = Response.serverError().entity(ex.getMessage()).build();
        }

        return response;
   }
}
