
package com.mycompany.pi4.Curtidas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/curtida")
public class CurtidasService {

    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://pijulio.database.windows.net:1433;database=facenac";
    private static final String USER = "julio@pijulio";
    private static final String PASS = "Abcd123!";
    
    @GET
    @Path("/{historico}")  
    @Produces("application/json;charset=utf-8")
    public Response getQuantidadeCurtidas (@PathParam("historico") Long idHistorico) throws SQLException {
        Response response;
        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement("select usuario from curtida where historico = ?")) {
                
                if (idHistorico == 0) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                
                stmt.setLong(1, idHistorico);
                
                ResultSet rs = stmt.executeQuery();
                int quantidadeCurtidas = 0;
                
                while (rs.next()) {
                    quantidadeCurtidas++;
                }
                
                response = Response.ok(quantidadeCurtidas).build();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response = Response.serverError().entity(ex.getMessage()).build();
        }

        return response;       
    }
    
    
    @POST
    @Path("/{usuario}/{historico}")
    @Produces("application/json;charset=utf-8")
    public Response setCurtida(@PathParam("usuario") Long usuario, @PathParam("historico") Long historico) throws ClassNotFoundException {
        Response response = null;
        
        Class.forName(DRIVER);
        String sql = "SELECT * FROM Curtida WHERE usuario = ? AND historico = ?";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                
            stmt.setLong(1, usuario);
            stmt.setLong(2, historico);
                
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next()) {
                boolean status = deleteCurtida(usuario, historico);
                
                if(status) {
                    Long qtdCurtidas = getQtdCurtidas(historico);
                    CurtidaResult resultado = new CurtidaResult(false, qtdCurtidas);
                    response = Response.ok(resultado).build();
                } else {
                    Response.serverError().entity("ERRO AO CURTIR HISTORICO").build();
                }
            } else {
                boolean status = createCurtida(usuario, historico);
                
                if(status) {
                    Long qtdCurtidas = getQtdCurtidas(historico);
                    CurtidaResult resultado = new CurtidaResult(true, qtdCurtidas);
                    response = Response.ok(resultado).build();
                } else {
                    Response.serverError().entity("ERRO AO REMOVER CURTIDA").build();
                }
            }
     
        } catch (SQLException ex) {
            response = Response.serverError().entity("ERRO NO CADASTRO DA CURTIDA: "+ex.getMessage()).build();
        }
        
        return response;
    }
    
    public Long getQtdCurtidas(Long idHistorico) {
        long qtdCurtida = 0;
        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement("SELECT	COUNT(historico) AS numLikes FROM Curtida WHERE historico = ?")) {
                stmt.setLong(1, idHistorico);
                
                ResultSet rs = stmt.executeQuery();
                int quantidadeCurtidas = 0;
                
                while (rs.next()) {
                    qtdCurtida = rs.getLong("numLikes");
                    return qtdCurtida;
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            return qtdCurtida;
        }

        return qtdCurtida;     
    }
    
    public boolean createCurtida(Long usuario, Long historico) {
        boolean status = false;
        
        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO curtida (usuario,historico) VALUES (?,?)")) {
                if (usuario == 0 || historico == 0) {
                    status = false;
                }
                
                stmt.setLong(1, usuario);
                stmt.setLong(2, historico);
                try{
                    stmt.executeUpdate();
                    status = true;
                }catch(SQLException ex){
                    status = false;
                }      
            } catch(SQLException ex){
                status = false;
            }
        } catch (ClassNotFoundException ex) {
            status = false;
        }
        
        return status;
    }
    
    public boolean deleteCurtida(Long usuario, Long historico) {
        boolean status = false;
        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement("DELETE from curtida WHERE usuario = ? and historico = ?")) {
                if (usuario == 0 || historico == 0) {
                    status = false;
                }
                
                stmt.setLong(1, usuario);
                stmt.setLong(2, historico);
                try{
                    stmt.executeUpdate();
                    status = true;
                }catch(SQLException ex){
                    status = false;
                }      
            } catch(SQLException ex){
                status = false;
            }
        } catch (ClassNotFoundException ex) {
            status = false;
        }
        
        return status;
    }
}
