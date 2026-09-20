package com.proyect.caseroya.opcionmenu.dto;

public class OpcionMenuDto {
    private String opcionId;
    private String descripcion;
    private String url;
    private String icono;
    private Integer orden;

    public OpcionMenuDto(String opcionId, String descripcion, String url, String icono, Integer orden) {
        this.opcionId = opcionId;
        this.descripcion = descripcion;
        this.url = url;
        this.icono = icono;
        this.orden = orden;
    }

    // Getters y Setters
    public String getOpcionId() { return opcionId; }
    public void setOpcionId(String opcionId) { this.opcionId = opcionId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
}