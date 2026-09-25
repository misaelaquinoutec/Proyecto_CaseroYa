package com.proyect.caseroya.usuario.application;

import com.proyect.caseroya.config.mail.MailService;
import com.proyect.caseroya.usuario.domain.UsuarioCreadoEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UsuarioEventListener {

    private final MailService mailService;

    public UsuarioEventListener(MailService mailService) {
        this.mailService = mailService;
    }

    @Async("taskExecutor")
    @EventListener
    public void onUsuarioCreado(UsuarioCreadoEvent event) {
        String mensaje = "<h1>Bienvenido a CaseroYa</h1><p>Hola " + event.getNombre() + ", tu cuenta ha sido creada con éxito en el sistema.</p>";
        mailService.enviarCorreo("notificaciones@caseroya.com", "Cuenta Creada", mensaje);
    }
}
