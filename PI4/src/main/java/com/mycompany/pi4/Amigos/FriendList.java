package com.mycompany.pi4.Amigos;

public class FriendList extends Friend {
    private String nome;
    private Boolean aprovado;
    
    protected FriendList() {
    }
        
    public FriendList(String nome, Long usuario1, Long usuario2, Boolean aprovado) {
        super(usuario1, usuario2);
        this.nome = nome;
        this.aprovado = aprovado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAprovado() {
        return aprovado;
    }

    public void setAprovado(Boolean aprovado) {
        this.aprovado = aprovado;
    }
}
