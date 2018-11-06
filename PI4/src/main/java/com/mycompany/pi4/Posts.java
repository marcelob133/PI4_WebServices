package com.mycompany.pi4;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Posts {
    
    private Long id;
    private Long usuario;
    private String texto;
    private String foto;
    private Timestamp data;

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

    public Timestamp getData() {
        return data;
    }
    
    
}


