package com.proyect.caseroya.producto.domain;

import java.math.BigDecimal;

public record Producto(
        Integer id,
        String codigo,
        String nombre,
        String categoria,
        String unidadMedida,
        BigDecimal precioVenta,
        BigDecimal precioCompra,
        boolean activo
) {
}
