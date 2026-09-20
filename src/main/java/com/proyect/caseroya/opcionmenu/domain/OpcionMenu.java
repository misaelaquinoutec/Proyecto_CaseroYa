package com.proyect.caseroya.opcionmenu.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "opcionmenu", schema = "public")
public class OpcionMenu {

    @Id
    @Column(name = "opcionid", length = 10)
    private String opcionId;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "url", length = 150)
    private String url;

    @Column(name = "icono", length = 50)
    private String icono;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "activo")
    private Boolean activo = true;

    public OpcionMenu() {}

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

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}