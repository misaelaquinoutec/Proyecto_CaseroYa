package com.proyect.caseroya.venta.domain;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

@Getter
public class VentaCreadaEvent extends ApplicationEvent {
    private final Integer ventaId;
    private final String numeroDocumento;
    private final BigDecimal total;

    public VentaCreadaEvent(Object source, Integer ventaId, String numeroDocumento, BigDecimal total) {
        super(source);
        this.ventaId = ventaId;
        this.numeroDocumento = numeroDocumento;
        this.total = total;
    }
}
