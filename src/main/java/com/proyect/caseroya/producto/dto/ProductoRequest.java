package com.proyect.caseroya.producto.dto;

import java.math.BigDecimal;

public record ProductoRequest(
        String codigo,
        String nombre,
        String categoria,
        String unidadMedida,
        BigDecimal precioVenta,
        BigDecimal precioCompra,
        String usuario
) {
}
