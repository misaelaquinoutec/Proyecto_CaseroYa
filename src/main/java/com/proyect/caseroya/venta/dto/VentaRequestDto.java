package com.proyect.caseroya.venta.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class VentaRequestDto {

    private String numeroDocumento;
    private Integer clienteId;
    private String usuarioId;
    private Integer monedaId;
    private List<DetalleVentaDto> detalles;

    @Data
    public static class DetalleVentaDto {
        private Integer productoId;
        private BigDecimal cantidad;
        private BigDecimal precioUnitario;
    }
}
