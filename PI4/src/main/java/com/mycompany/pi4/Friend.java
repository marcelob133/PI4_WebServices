
package com.mycompany.pi4;


public class Friend {
    private String nomeUsuario1;
    private Long usuario1;
    private Long usuario2;
    private boolean aprovada;

    public Friend(String nomeUsuario1, Long usuario1, Long usuario2, boolean aprovada) {
        this.nomeUsuario1 = nomeUsuario1;
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.aprovada = aprovada;
    }
    
    public Friend(Long usuario1, Long usuario2, boolean aprovada) {
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.aprovada = aprovada;
    }

    public String getNomeUsuario1() {
        return nomeUsuario1;
    }

    public Long getUsuario1() {
        return usuario1;
    }

    public Long getUsuario2() {
        return usuario2;
    }

    public boolean isAprovada() {
        return aprovada;
    }

    public void setNomeUsuario1(String nomeUsuario1) {
        this.nomeUsuario1 = nomeUsuario1;
    }

    public void setUsuario1(Long usuario1) {
        this.usuario1 = usuario1;
    }

    public void setUsuario2(Long usuario2) {
        this.usuario2 = usuario2;
    }

    public void setAprovada(boolean aprovada) {
        this.aprovada = aprovada;
    }


    
}
