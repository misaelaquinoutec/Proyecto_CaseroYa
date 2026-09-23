package com.proyect.caseroya.stock.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Datos devueltos por las funciones de consulta de control_stock.
public record ControlStock(
        Integer productoId,
        BigDecimal cantidadActual,
        LocalDateTime actualizadoEn
) {
}
