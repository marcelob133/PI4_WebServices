
package com.mycompany.pi4.Curtidas;

public class Curtida {

    private Long usuario;
    private Long historico;

    protected Curtida() {
    }
    
    public Curtida(Long usuario, Long historico) {
        this.usuario = usuario;
        this.historico = historico;
    }

    public Long getUsuario() {
        return usuario;
    }

    public Long getHistorico() {
        return historico;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public void setHistorico(Long historico) {
        this.historico = historico;
    }
    
}
