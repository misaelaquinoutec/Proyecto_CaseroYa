package com.proyect.caseroya.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaEventListener {

    @Async // Proceso asíncrono en hilo secundario
    @EventListener
    public void registrarAuditoriaVenta(VentaRealizadaEvent event) {
        // Simula procesamiento asíncrono diferido de auditoría
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}

        System.out.println("[AUDITORÍA ASÍNCRONA] Venta registrada con éxito ID: "
                + event.getVentaId() + " por el usuario: " + event.getUsuario());
    }

    @Async // Proceso asíncrono en hilo secundario
    @EventListener
    public void procesarAlertaStock(StockBajoEvent event) {
        System.out.println("[ALERTA ASÍNCRONA DE STOCK] El producto '"
                + event.getProductoNombre() + "' (ID: " + event.getProductoId() + ") tiene stock bajo.");
    }
}