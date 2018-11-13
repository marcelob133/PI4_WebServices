package com.mycompany.pi4.Comentarios;

import com.mycompany.pi4.Historicos.Posts;
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


@Path("/comment")
public class CommentService {
    
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://pijulio.database.windows.net:1433;database=facenac";
    private static final String USER = "julio@pijulio";
    private static final String PASS = "Abcd123!";

    @GET
    @Path("/{id}")  
    @Produces("application/json;charset=utf-8")
    public Response getCommentsPost (@PathParam("id") Long idComment) throws SQLException {
        Response response;

        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement stmt = conn.prepareStatement("select * from comentario where historico = ?")) {
                
                if (idComment == 0 || idComment == null) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                
                stmt.setLong(1, idComment);
                ResultSet rs = stmt.executeQuery();
                
                List<Comment> commentsList = new ArrayList<>();
                
                while (rs.next()) {
                    String texto = rs.getString("texto");
                    Long id = rs.getLong("id");
                    Long usuario = rs.getLong("usuario");
                    Long historia = rs.getLong("historia");
                    Date data = rs.getDate("data");
                    
                    Comment  comment = new Comment(texto, id, usuario, historia, data);
                    commentsList.add(comment);
                }
                response = Response.ok(commentsList).build();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response = Response.serverError().entity(ex.getMessage()).build();
        }

        return response;
    }
    
    
    
    @POST
    @Consumes("application/json;charset=utf-8")   
    @Produces("application/json;charset=utf-8")
    public Response setComment (Comment comment) throws SQLException {
        Response response;
        Long commentCriado = null;
        
        try {
            Class.forName(DRIVER);
            String sql = "INSERT INTO comentario (usuario,historico,texto,data) VALUES (?,?,?,?)";
            
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                               
                stmt.setLong(1, comment.getUsuario());
                stmt.setLong(2, comment.getHistoria());
                stmt.setString(3, comment.getComment());
                
                java.sql.Date d = new java.sql.Date (new java.util.Date().getTime());
                stmt.setDate (4,d);

                int rs = stmt.executeUpdate();
                
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        commentCriado = generatedKeys.getLong(1);
                    }
                }
                
                response = Response.ok(commentCriado).build();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response = Response.serverError().entity("ERRO NA CRIAÇÃO DO COMENTÁRIO: "+ex.getMessage()).build();
        }
        
        return response;
    }
    
    
    
    @DELETE
    @Path("/{id}")
    @Produces("application/json;charset=utf-8")
    public Response deleteComment(@PathParam("id") Long id) {
        Response response = null;
        
        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement stmt = conn.prepareStatement("delete from comentario where id = ?")) {
                
                if (id == 0 || id == null) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                
                stmt.setLong(1, id);
                
                try{
                    stmt.executeUpdate();
                    response = Response.ok("Comentário excluido com sucesso!").build();
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
