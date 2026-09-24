package com.proyect.caseroya.producto.dto;

import java.math.BigDecimal;

// El id, el estado activo y las fechas los administra la base de datos.
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
