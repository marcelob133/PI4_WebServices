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
    private Integer temFoto;
    private Date data;
    private Integer numCurtidas;
    private String nomeUser;
    private Integer fotoUser;
    private Boolean liked;
    
    protected Posts(){    
    }
    
    public Posts(Long id, Long usuario, String texto, Integer temFoto, Date data, Integer numCurtidas, String nomeUser, Integer fotoUser, Boolean liked) {
        this.id = id;
        this.usuario = usuario;
        this.texto = texto;
        this.temFoto = temFoto;
        this.data = data;
        this.numCurtidas = numCurtidas;
        this.nomeUser = nomeUser;
        this.fotoUser = fotoUser;
        this.liked = liked;
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
    
    public Integer getFotoUser() {
        return fotoUser;
    }
    
    public Boolean getLiked() {
        return liked;
    }
    
    public Integer getTemFoto() {
        return temFoto;
    }
    
    public void setFoto(String foto) {
        this.foto = foto;
    }
    
}


