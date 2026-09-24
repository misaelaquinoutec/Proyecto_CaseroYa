package com.proyect.caseroya.tablageneral.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tablas_generales", schema = "public")
public class TablaGeneral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "tipo", nullable = false, length = 10)
    private String tipo;

    @Column(name = "codigo", nullable = false, length = 20)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;

    // Constructor vacío
    public TablaGeneral() {}

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}