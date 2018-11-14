package com.mycompany.pi4.Historicos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.sql.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Posts {
    
    private Long id;
    private Long usuario;
    private String texto;
    private String foto;
    private Date data;
    
    protected Posts() {
    }
    
    public Posts(Long id, Long usuario, String texto, String foto, Date data) {
        this.id = id;
        this.usuario = usuario;
        this.texto = texto;
        this.foto = foto;
        this.data = data;
    }

    public Long getId() {
        return id;
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

    public Date getData() {
        return data;
    }
    
    
}


