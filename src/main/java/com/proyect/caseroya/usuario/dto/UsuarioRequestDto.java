package com.proyect.caseroya.usuario.dto;

public class UsuarioRequestDto {
    private String codigoUsuario;
    private String clave;
    private String nombre;
    private String perfilId;

    // Getters y Setters
    public String getCodigoUsuario() { return codigoUsuario; }
    public void setCodigoUsuario(String codigoUsuario) { this.codigoUsuario = codigoUsuario; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPerfilId() { return perfilId; }
    public void setPerfilId(String perfilId) { this.perfilId = perfilId; }
}
