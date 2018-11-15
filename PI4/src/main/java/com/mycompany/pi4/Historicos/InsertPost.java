
package com.mycompany.pi4.Historicos;
import java.io.Serializable;
import java.sql.Timestamp;
import java.sql.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InsertPost {
    
    private Long usuario;
    private String texto;
    private String foto;
    
    protected InsertPost(){
        
    }
    
    public InsertPost(Long usuario, String texto, String foto){    
        
        this.usuario = usuario;
        this.texto = texto;
        this.foto = foto;
    }

    public Long getUsuario() {
        return usuario;
    }

    public String getTexto() {
        return texto;
    }

    public String getFoto() {
        return foto;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
    
}
