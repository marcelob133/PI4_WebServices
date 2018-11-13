package com.mycompany.pi4.Historicos;

import java.sql.Connection;
import java.sql.Date;
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
import javax.ws.rs.DELETE;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/posts")
public class PostsService {
    
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://pijulio.database.windows.net:1433;database=facenac";
    private static final String USER = "julio@pijulio";
    private static final String PASS = "Abcd123!";
   
    
    @GET
    @Path("/{id}")  
    @Produces("application/json;charset=utf-8")
    public Response getHistoriaPessoais (@PathParam("id") Long idUser) throws SQLException {
        Response response;

        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement stmt = conn.prepareStatement("select * from historia where usuario = ?")) {
                
                if (idUser == 0 || idUser == null) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                
                stmt.setLong(1, idUser);
                ResultSet rs = stmt.executeQuery();
                
                List<Posts> postsList = new ArrayList<>();
                
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    Long usuario = rs.getLong("usuario");
                    String texto = rs.getString("texto");
                    String foto = rs.getString("foto");
                    Date data = rs.getDate("data");

                    Posts post = new Posts(id, usuario, texto, foto, data);
                    postsList.add(post);
                }
                response = Response.ok(postsList).build();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response = Response.serverError().entity(ex.getMessage()).build();
        }

        return response;
    }
    
    
    @GET 
    @Path("/friends/{id}")  
    @Produces("application/json;charset=utf-8")
    public Response getHistoriasDosAmigos (@PathParam("id") Long idUser) throws SQLException {
    Response response;

        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement stmt = conn.prepareStatement("select usuario2 from amizade where usuario1 = ?")) {
                
                if (idUser == 0 || idUser == null) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
//                
                stmt.setLong(1, idUser);
                ResultSet rs = stmt.executeQuery();
//                
                List<Long> IdsUsers = new ArrayList<>();
//                
                while (rs.next()) {
                    Long id = rs.getLong("usuario2");
                    IdsUsers.add(id);
                }
                
                try(PreparedStatement stmt2 = conn.prepareStatement("select usuario1 from amizade where usuario2 = ?")) {
                    stmt.setLong(1, idUser);
                    ResultSet rs2 = stmt2.executeQuery();
    //                
                    while (rs2.next()) {
                        Long id = rs.getLong("usuario2");
                        IdsUsers.add(id);
                    }
                    
                    response = Response.ok(IdsUsers).build();
                }
//                response = Response.ok(postsList).build();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response = Response.serverError().entity(ex.getMessage()).build();
        }

        return response;
    }
    
    @POST
    @Consumes("application/json;charset=utf-8")   
    @Produces("application/json;charset=utf-8")
    public Response setHistoria (Posts post) throws SQLException {
        Response response;
        Long postCriado = null;
        
        try {
            Class.forName(DRIVER);
            String sql = "INSERT INTO historia (usuario,texto,foto,data) VALUES (?,?,?,?)";
            
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                               
                stmt.setLong(1, post.getUsuario());
                stmt.setString(2, post.getTexto());
        
                String foto = post.getFoto();
                byte[] fotoEmByte = Base64.getDecoder().decode (foto);
                stmt.setBytes(3, fotoEmByte);            
                
                java.sql.Date d = new java.sql.Date (new java.util.Date().getTime());
                stmt.setDate (4,d);
                
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
    
    @DELETE
    @Path("/{id}")
    @Produces("application/json;charset=utf-8")
    public Response deletePost(@PathParam("id") Long id) {
        Response response = null;
        
        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement stmt = conn.prepareStatement("delete from historia where id = ?")) {
                
                if (id == 0 || id == null) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                
                stmt.setLong(1, id);
                
                try{
                    stmt.executeUpdate();
                    response = Response.ok("Postagem excluida com sucesso!").build();
                }catch(SQLException ex){
                    response = Response.serverError().entity(ex.getMessage()).build();
                }
            }catch(SQLException ex){
                response = Response.serverError().entity(ex.getMessage()).build();
            }
        } catch (ClassNotFoundException ex) {
            response = Response.serverError().entity(ex.getMessage()).build();
        }
        
        return response;
    }
}