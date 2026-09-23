package com.proyect.caseroya.producto.domain;

import java.math.BigDecimal;

// Resultado de las funciones fn_productos_listar y fn_productos_obtener_por_id.
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
