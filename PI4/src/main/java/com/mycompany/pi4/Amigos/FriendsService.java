
package com.mycompany.pi4.Amigos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Comparator;

@Path("/friends")
public class FriendsService {
    
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://pijulio.database.windows.net:1433;database=facenac";
    private static final String USER = "julio@pijulio";
    private static final String PASS = "Abcd123!";
    
    @GET
    @Path("/{id}")
    @Produces("application/json;charset=utf-8")
    public Response getAmigos(@PathParam("id") Long idUser) {
        Response response;

        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement("SELECT Usuario.id, Usuario.nome , temFoto = CASE WHEN Usuario.foto is null THEN 0 ELSE 1 END, Amizade.aprovada FROM Amizade INNER JOIN Usuario ON Amizade.usuario1 = Usuario.id WHERE Amizade.usuario2 = ?");
                PreparedStatement stmtInverted = conn.prepareStatement("SELECT Usuario.id, Usuario.nome, temFoto = CASE WHEN Usuario.foto is null THEN 0 ELSE 1 END, Amizade.aprovada FROM Amizade INNER JOIN Usuario ON Amizade.usuario2 = Usuario.id WHERE Amizade.usuario1 = ?");    
            ) {
                if (idUser == 0) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                
                stmt.setLong(1, idUser);
                ResultSet rs = stmt.executeQuery();
                
                List<FriendList> amizadesList = new ArrayList<>();
                
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    String nome = rs.getString("nome"); 
                    Integer temFoto = rs.getInt("temFoto");
                    Boolean aprovada = rs.getBoolean("aprovada");
                    String statusAmizade = "";
                    
                    if(aprovada){
                        statusAmizade = "amigos";
                    }else{
                        statusAmizade = "solicitado";
                    }
                    FriendList amizade = new FriendList(id, nome, temFoto, statusAmizade, aprovada);
                    amizadesList.add(amizade);
                }
                
                stmtInverted.setLong(1, idUser);
                ResultSet rsInverted = stmtInverted.executeQuery();

                while (rsInverted.next()) {
                    Long id = rsInverted.getLong("id");
                    String nome = rsInverted.getString("nome");
                    Integer temFoto = rsInverted.getInt("temFoto");
                    Boolean aprovada = rsInverted.getBoolean("aprovada");
                    String statusAmizade = "";
                    
                    if(aprovada){
                        statusAmizade = "amigos";
                    }else{
                        statusAmizade = "solicitante";
                    }
                    FriendList amizade = new FriendList(id, nome, temFoto, statusAmizade, aprovada);
                    amizadesList.add(amizade);                    
                }
                
                Collections.sort(amizadesList, new Comparator<FriendList>() {
                    @Override
                    public int compare(FriendList amizade1, FriendList amizade2) {
                      if(amizade1.getAprovado() && !amizade2.getAprovado()) {
                        return 1;
                      } else if(!amizade1.getAprovado() && amizade2.getAprovado()) {
                        return -1;
                      } else {
                        return 0;
                      }
                    }
                });
                
                response = Response.ok(amizadesList).build();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response = Response.serverError().entity(ex.getMessage()).build();
        }

        return response;
   }
    
    
    @POST
    @Consumes("application/json;charset=utf-8")   
    @Produces("application/json;charset=utf-8")
    public Response setAmizade (Friend friend) throws SQLException, ClassNotFoundException {
        Response response;
        
        Class.forName(DRIVER);
        String sql = "INSERT INTO amizade (usuario1,usuario2,aprovada) VALUES (?,?,?)";
            
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                
            stmt.setLong(1, friend.getUsuario1());
            stmt.setLong(2, friend.getUsuario2());
            stmt.setBoolean(3, false);
                
            int rs = stmt.executeUpdate();
            if(rs != 0){
                response = Response.ok("Amizade solicitada!").build();
            }else{
                response = Response.serverError().entity("ERRO NO CADASTRO DE AMIZADE").build();
            }   
            
        } catch (SQLException ex) {
            response = Response.serverError().entity("ERRO NO CADASTRO DE AMIZADE: "+ex.getMessage()).build();
        }
        
        return response;
    }
    
    @DELETE
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
                    stmt.executeUpdate();
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
    @PUT
    @Consumes("application/json;charset=utf-8")   
    @Produces("application/json;charset=utf-8")
    public Response updateAmizade (Friend friend) throws SQLException, ClassNotFoundException {
        Response response;

        Class.forName(DRIVER);
        String sql = "UPDATE amizade SET aprovada = ? WHERE usuario1 = ? and usuario2 = ?";
    
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, true);
            stmt.setLong(2, friend.getUsuario1());
            stmt.setLong(3, friend.getUsuario2());
                
            int rs = stmt.executeUpdate();
            if(rs != 0){
                response = Response.ok("Amizade aceita e gravada no banco!").build();
            }else{
                response = Response.status(500).entity("ERRO NO UPDATE DA AMIZADE").build();
            }   
            
        } catch (SQLException ex) {
            response = Response.status(500).entity("ERRO NO UPDATE DA AMIZADE: "+ex.getMessage()).build();
        }
        
        return response;
    }
}
