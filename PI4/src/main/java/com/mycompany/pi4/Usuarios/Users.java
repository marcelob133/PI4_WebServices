package com.mycompany.pi4.Usuarios;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Users implements Serializable {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String foto;
    private Integer temFoto;
    private Integer amizade;

    protected Users() {
    }

    public Users(Long id, String nome, String email, String senha, Integer temFoto) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.temFoto = temFoto;
    }
    
    public Users(Long id, String nome, String email, String senha, Integer temFoto, Integer amizade) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.temFoto = temFoto;
        this.amizade = amizade;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }
    
    public String getFoto() {
        return foto;
    }
    public Integer getTemFoto() {
        return temFoto;
    }
    
    public void setFoto(String foto) {
        this.foto = foto;
    }
    
    public Integer getAmizade() {
        return amizade;
    }
    
    public void setAmizade(Integer amizade) {
        this.amizade = amizade;
    }

}
