package com.proyect.caseroya.event;

public class VentaRealizadaEvent {
    private final Integer ventaId;
    private final String usuario;

    public VentaRealizadaEvent(Integer ventaId, String usuario) {
        this.ventaId = ventaId;
        this.usuario = usuario;
    }

    public Integer getVentaId() { return ventaId; }
    public String getUsuario() { return usuario; }
}
