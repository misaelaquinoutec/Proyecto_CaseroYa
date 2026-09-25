package com.proyect.caseroya.event;

public class StockBajoEvent {
    private final Integer productoId;
    private final String productoNombre;

    public StockBajoEvent(Integer productoId, String productoNombre) {
        this.productoId = productoId;
        this.productoNombre = productoNombre;
    }

    public Integer getProductoId() { return productoId; }
    public String getProductoNombre() { return productoNombre; }
}
