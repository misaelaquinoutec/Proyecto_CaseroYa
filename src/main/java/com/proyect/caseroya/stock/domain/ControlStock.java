package com.proyect.caseroya.stock.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ControlStock(
        Integer productoId,
        BigDecimal cantidadActual,
        LocalDateTime actualizadoEn
) {
}
