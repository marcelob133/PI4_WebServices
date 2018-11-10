
package com.mycompany.pi4.Amigos;


public class Friend {
    
    private Long usuario1;
    private Long usuario2;

    protected Friend() {
    }
    
    public Friend(Long usuario1, Long usuario2) {
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
    }

    public Long getUsuario1() {
        return usuario1;
    }

    public Long getUsuario2() {
        return usuario2;
    }

    public void setUsuario1(Long usuario1) {
        this.usuario1 = usuario1;
    }

    public void setUsuario2(Long usuario2) {
        this.usuario2 = usuario2;
    }
    
}
