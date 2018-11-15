
package com.mycompany.pi4.Comentarios;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Comment implements Serializable {
    private String comment;
    private Long id, usuario, historia;
    private Date data;

    protected Comment() {
    }
    
    public Comment(String comment, Long id, Long usuario, Long historia, Date data) {
        this.comment = comment;
        this.id = id;
        this.usuario = usuario;
        this.historia = historia;
        this.data = data;
    }
    
    Comment(Comment comment, Long id, Long usuario, Long historia, java.sql.Date data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public Long getHistoria() {
        return historia;
    }

    public void setHistoria(Long historia) {
        this.historia = historia;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    
    
}
