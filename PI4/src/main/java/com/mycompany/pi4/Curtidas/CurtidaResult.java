package com.mycompany.pi4.Curtidas;

public class CurtidaResult {

    private Boolean status;
    private Long totalCurtidas;

    protected CurtidaResult() {
    }
    
    public CurtidaResult(Boolean status, Long totalCurtidas) {
        this.status = status;
        this.totalCurtidas = totalCurtidas;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getTotalCurtidas() {
        return totalCurtidas;
    }

    public void setTotalCurtidas(Long totalCurtidas) {
        this.totalCurtidas = totalCurtidas;
    }

    
    
}
