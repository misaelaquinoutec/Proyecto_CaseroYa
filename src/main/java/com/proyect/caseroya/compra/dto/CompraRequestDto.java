package com.proyect.caseroya.compra.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CompraRequestDto {

    private String numeroDocumento;
    private Integer proveedorId;
    private String usuarioId;
    private Integer monedaId;
    private List<DetalleCompraDto> detalles;

    @Data
    public static class DetalleCompraDto {
        private Integer productoId;
        private BigDecimal cantidad;
        private BigDecimal precioUnitario;
    }
}
