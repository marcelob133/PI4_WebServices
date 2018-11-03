
package com.mycompany.pi4;

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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/friends")
public class FriendsService {
    
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://pijulio.database.windows.net:1433;database=facenac";
    private static final String USER = "julio@pijulio";
    private static final String PASS = "Abcd123!";
    
    @GET//ALGO DE ERRADO NAO ESTÁ CERTO
    @Path("/{id}")
    @Produces("application/json;charset=utf-8")
       public Response getAmigos (@PathParam("id") Long idUser) {
        Response response;

        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement("u.nome, a.usuario1, a.usuario2, a.aprovada "
                                                               + "from Amizade a inner join Usuario u on a.usuario1 = u.id " +
                                                               "where a.aprovada = 0 and a.usuario2 = ? ;")) {
                if (idUser == 0) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                
                stmt.setLong(1, idUser);
                ResultSet rs = stmt.executeQuery();
                
                List<Friend> amizadesList = new ArrayList<>();
                
                while (rs.next()) {
                    String nome = rs.getString("u.nome");
                    Long idUsuario1 = rs.getLong("a.usuario1");
                    Long idUsuario2 = rs.getLong("a.usuario2");
                    Boolean aprovada = rs.getBoolean("a.aprovada");
                    
                    Friend amizade = new Friend(nome, idUsuario1, idUsuario2, aprovada);
                    amizadesList.add(amizade);
                }
                response = Response.ok(amizadesList).build();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response = Response.serverError().entity(ex.getMessage()).build();
        }

        return response;
   }
    
    
    @POST//APARENTEMENTE EXISTE ALGUM PROBLEMA NO CONSTRUCTOR DA CLASSE FRIENDS
    @Consumes("application/json;charset=utf-8")   
    @Produces("application/json;charset=utf-8")
    public Response setAmizade (NewFriend friend) throws SQLException, ClassNotFoundException {
        Response response;
        
        Class.forName(DRIVER);
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = conn.prepareStatement("select * from amizade where (usuario1 = ? and usuario2 = ?) or (usuario2 = ? and usuario1 = ?)")) {
            
            stmt.setLong(1, friend.getUsuario1());
            stmt.setLong(2, friend.getUsuario2());
            stmt.setLong(3, friend.getUsuario2());
            stmt.setLong(4, friend.getUsuario1());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                response = Response.ok("Amizade já existe").build();
                return response;
            }
        } catch(SQLException ex){
            response = Response.serverError().entity("ERRO: "+ex.getMessage()).build();
        }
        try {
            Class.forName(DRIVER);
            String sql = "INSERT INTO amizade (usuario1,usuario2,aprovada) VALUES (?,?,?)";
            
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                    PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                
                stmt.setLong(1, friend.getUsuario1());
                stmt.setLong(2, friend.getUsuario2());
                stmt.setBoolean(3, false);
                
                int rs = stmt.executeUpdate();
                
                response = Response.ok("Amizade solicitada!").build();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response = Response.serverError().entity("ERRO NO CADASTRO DE AMIZADE: "+ex.getMessage()).build();
        }
        
        return response;
    }
    
    @DELETE //PRECISA ARRUMAR O TEXTO DE RETORNO PARA O FRONT QUANDO OCORRE TUDO OK!
    @Path("/{idUsuario}/{idAmigo}")
    @Produces("application/json;charset=utf-8")
       public Response deleteAmigo (@PathParam("idUsuario") Long idUsuario, @PathParam("idAmigo") Long idAmigo) {
        Response response = null;
        
        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement("delete from amizade where usuario1 = ? and usuario2 = ?")) {
                if (idUsuario == 0 || idAmigo == 0) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                
                stmt.setLong(1, idAmigo);
                stmt.setLong(2, idUsuario);
                try{
                    ResultSet rs = stmt.executeQuery();
                    response = Response.ok("Amizade desfeita com sucesso!").build();
                }catch(SQLException ex){
                    response = Response.serverError().entity(ex.getMessage()).build();
                }      
            } catch(SQLException ex){
                response = Response.serverError().entity(ex.getMessage()).build();
            }
        } catch (ClassNotFoundException ex) {
            response = Response.serverError().entity(ex.getMessage()).build();
        }

        return response;
    }
}
