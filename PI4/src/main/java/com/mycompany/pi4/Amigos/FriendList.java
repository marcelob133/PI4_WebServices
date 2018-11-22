package com.mycompany.pi4.Amigos;

public class FriendList extends Friend {
    private Long id;
    private String nome;
    private String foto;
    private Boolean aprovado;
    
    protected FriendList() {
    }
        
    public FriendList(Long id, String nome, String foto, Boolean aprovado) {
        this.id = id;
        this.nome = nome;
        this.foto = foto;
        this.aprovado = aprovado;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getFoto() {
        return foto;
    }

    public Boolean getAprovado() {
        return aprovado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setAprovado(Boolean aprovado) {
        this.aprovado = aprovado;
    }
    
    
}
