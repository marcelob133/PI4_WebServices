package com.mycompany.pi4.Amigos;

public class FriendList extends Friend {
    private Long id;
    private String nome;
    private String foto;
    private Integer temFoto;
    private String statusAmizade;
    private Boolean aprovado;
    
    protected FriendList() {
    }
        
    public FriendList(Long id, String nome, Integer temFoto, String statusAmizade, Boolean aprovado) {
        this.id = id;
        this.nome = nome;
        this.temFoto = temFoto;
        this.aprovado = aprovado;
        
    }

    public Long getId() {
        return id;
    }

    public String getStatusAmizade() {
        return statusAmizade;
    }

    public void setStatusAmizade(String statusAmizade) {
        this.statusAmizade = statusAmizade;
    }

    public String getNome() {
        return nome;
    }

    public String getFoto() {
        return foto;
    }
    
    public Integer getTemFoto() {
        return temFoto;
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
    
    
    public void setTemFoto(Integer temFoto) {
        this.temFoto = temFoto;
    }
    
    public void setAprovado(Boolean aprovado) {
        this.aprovado = aprovado;
    }
    
}
