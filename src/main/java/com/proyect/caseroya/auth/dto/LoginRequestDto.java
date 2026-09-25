package com.proyect.caseroya.auth.dto;

public class LoginRequestDto {
    private String codigoUsuario;
    private String clave;

    public String getCodigoUsuario() { return codigoUsuario; }
    public void setCodigoUsuario(String codigoUsuario) { this.codigoUsuario = codigoUsuario; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
}