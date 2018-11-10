
package com.mycompany.pi4.Curtidas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.ws.rs.DELETE;
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
    
    @POST
    @Path("/{usuario}/{historico}")
    @Produces("application/json;charset=utf-8")
    public Response setCurtida(@PathParam("usuario") Long usuarioCurtida, @PathParam("historico") Long historicoCurtida) throws ClassNotFoundException {
        Response response;
        
        Class.forName(DRIVER);
        String sql = "INSERT INTO curtida (usuario,historico) VALUES (?,?)";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                
            stmt.setLong(1, usuarioCurtida);
            stmt.setLong(2, historicoCurtida);
                
            int rs = stmt.executeUpdate();
            if(rs != 0){
                response = Response.ok("Histórico curtido!").build();
            }else{
                response = Response.serverError().entity("ERRO NO CADASTRO DA CURTIDA").build();
            }   
            
        } catch (SQLException ex) {
            response = Response.serverError().entity("ERRO NO CADASTRO DA CURTIDA: "+ex.getMessage()).build();
        }
        
        return response;
    }
    
    @DELETE
    @Path("/{usuario}/{historico}")
    @Produces("application/json;charset=utf-8")
       public Response deleteCurtida (@PathParam("usuario") Long usuarioCurtida, @PathParam("historico") Long historicoCurtida) {
        Response response = null;
        
        try {
            Class.forName(DRIVER);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                PreparedStatement stmt = conn.prepareStatement("DELETE from curtida WHERE usuario = ? and historico = ?")) {
                if (usuarioCurtida == 0 || historicoCurtida == 0) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                
                stmt.setLong(1, usuarioCurtida);
                stmt.setLong(2, historicoCurtida);
                try{
                    stmt.executeUpdate();
                    response = Response.ok("Curtida desfeita com sucesso!").build();
                }catch(SQLException ex){
                    response = Response.serverError().entity("Erro durante a execução do delete: " + ex.getMessage()).build();
                }      
            } catch(SQLException ex){
                response = Response.serverError().entity("Erro durante a conexão e com o banco: " + ex.getMessage()).build();
            }
        } catch (ClassNotFoundException ex) {
            response = Response.serverError().entity("Erro no instanciamento da classe Driver: " + ex.getMessage()).build();
        }

        return response;
    }
}
