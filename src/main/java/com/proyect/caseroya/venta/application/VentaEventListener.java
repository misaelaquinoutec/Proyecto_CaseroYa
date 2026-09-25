package com.proyect.caseroya.venta.application;

import com.proyect.caseroya.config.mail.MailService;
import com.proyect.caseroya.venta.domain.VentaCreadaEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class VentaEventListener {

    private final MailService mailService;

    public VentaEventListener(MailService mailService) {
        this.mailService = mailService;
    }

    @Async("taskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onVentaCreada(VentaCreadaEvent event) {
        String mensaje = "<h1>Confirmación de Venta</h1><p>La venta " + event.getNumeroDocumento() + " por un total de $" + event.getTotal() + " se guardó correctamente.</p>";
        mailService.enviarCorreo("notificaciones@caseroya.com", "Venta Registrada Exitosamente", mensaje);
    }
}
