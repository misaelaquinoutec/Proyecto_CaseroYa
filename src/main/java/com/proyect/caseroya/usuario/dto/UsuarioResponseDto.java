package com.proyect.caseroya.usuario.dto;

public class UsuarioResponseDto {
    private String codigoUsuario;
    private String nombre;
    private String perfilId;
    private Boolean activo;

    public UsuarioResponseDto(String codigoUsuario, String nombre, String perfilId, Boolean activo) {
        this.codigoUsuario = codigoUsuario;
        this.nombre = nombre;
        this.perfilId = perfilId;
        this.activo = activo;
    }

    // Getters y Setters
    public String getCodigoUsuario() { return codigoUsuario; }
    public void setCodigoUsuario(String codigoUsuario) { this.codigoUsuario = codigoUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPerfilId() { return perfilId; }
    public void setPerfilId(String perfilId) { this.perfilId = perfilId; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}