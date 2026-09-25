package com.proyect.caseroya.auth.dto;

public class AuthResponseDto {
    private String token;
    private String codigoUsuario;
    private String nombre;
    private String perfilId;

    public AuthResponseDto(String token, String codigoUsuario, String nombre, String perfilId) {
        this.token = token;
        this.codigoUsuario = codigoUsuario;
        this.nombre = nombre;
        this.perfilId = perfilId;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getCodigoUsuario() { return codigoUsuario; }
    public void setCodigoUsuario(String codigoUsuario) { this.codigoUsuario = codigoUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPerfilId() { return perfilId; }
    public void setPerfilId(String perfilId) { this.perfilId = perfilId; }
}