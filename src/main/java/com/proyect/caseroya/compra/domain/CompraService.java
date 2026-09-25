package com.proyect.caseroya.compra.domain;

import com.proyect.caseroya.config.exception.RecursoNoEncontradoException;
import com.proyect.caseroya.stock.domain.StockService;
import com.proyect.caseroya.compra.infrastructure.DocumentoCompraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class CompraService {

    private static final BigDecimal TASA_IGV = new BigDecimal("0.18");

    private final DocumentoCompraRepository compraRepository;
    private final StockService stockService;

    public CompraService(DocumentoCompraRepository compraRepository, StockService stockService) {
        this.compraRepository = compraRepository;
        this.stockService = stockService;
    }

    @Transactional
    public DocumentoCompra registrarCompra(DocumentoCompra compra) {
        if (compra.getDetalles() == null || compra.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La compra debe incluir al menos un detalle.");
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        for (DetalleCompra detalle : compra.getDetalles()) {
            if (detalle.getProductoId() == null || detalle.getCantidad() == null
                    || detalle.getCantidad().compareTo(BigDecimal.ZERO) <= 0
                    || detalle.getPrecioUnitario() == null
                    || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Cada detalle requiere producto, cantidad positiva y precio no negativo.");
            }
            BigDecimal subtotalLinea = detalle.getCantidad().multiply(detalle.getPrecioUnitario())
                    .setScale(2, RoundingMode.HALF_UP);
            detalle.setSubtotal(subtotalLinea);
            subtotal = subtotal.add(subtotalLinea);
        }

        BigDecimal igv = subtotal.multiply(TASA_IGV).setScale(2, RoundingMode.HALF_UP);
        compra.setFechaEmision(LocalDate.now());
        compra.setSubtotal(subtotal);
        compra.setIgv(igv);
        compra.setTotal(subtotal.add(igv));
        compra.setAnulado(false);

        // Guardar primero asegura que un fallo de stock revierta también el documento por la transacción.
        DocumentoCompra guardada = compraRepository.save(compra);
        for (DetalleCompra detalle : guardada.getDetalles()) {
            stockService.aumentarStock(detalle.getProductoId(), detalle.getCantidad());
        }
        return guardada;
    }

    @Transactional(readOnly = true)
    public DocumentoCompra obtenerPorId(Integer id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró la compra con ID: " + id));
    }

    @Transactional
    public void anularCompra(Integer id) {
        DocumentoCompra compra = obtenerPorId(id);
        if (compra.isAnulado()) {
            throw new IllegalStateException("La compra ya se encuentra anulada.");
        }
        for (DetalleCompra detalle : compra.getDetalles()) {
            stockService.disminuirStock(detalle.getProductoId(), detalle.getCantidad());
        }
        compra.setAnulado(true);
        compraRepository.save(compra);
    }
}
