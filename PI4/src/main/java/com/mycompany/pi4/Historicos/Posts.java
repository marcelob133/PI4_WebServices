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
    private Integer numCurtidas;
    private String nomeUser;
    private String fotoUser;
    
    protected Posts(){    
    }
    
    public Posts(Long id, Long usuario, String texto, String foto, Date data, Integer numCurtidas, String nomeUser, String fotoUser) {
        this.id = id;
        this.usuario = usuario;
        this.texto = texto;
        this.foto = foto;
        this.data = data;
        this.numCurtidas = numCurtidas;
        this.nomeUser = nomeUser;
        this.fotoUser = fotoUser;
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
    
    public Integer getNumCurtidas() {
        return numCurtidas;
    }
    
    public String getNomeUser() {
        return nomeUser;
    }
    
    public String getFotoUser() {
        return fotoUser;
    }
    
}


