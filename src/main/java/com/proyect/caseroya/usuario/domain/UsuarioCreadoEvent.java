package com.proyect.caseroya.usuario.domain;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UsuarioCreadoEvent extends ApplicationEvent {
    private final String codigoUsuario;
    private final String nombre;

    public UsuarioCreadoEvent(Object source, String codigoUsuario, String nombre) {
        super(source);
        this.codigoUsuario = codigoUsuario;
        this.nombre = nombre;
    }
}
