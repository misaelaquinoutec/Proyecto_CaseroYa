package com.proyect.caseroya.venta.domain;

import com.proyect.caseroya.config.exception.RecursoNoEncontradoException;
import com.proyect.caseroya.venta.infrastructure.DocumentoVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.proyect.caseroya.stock.domain.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class VentaService {

    @Autowired
    private DocumentoVentaRepository ventaRepository;

    @Autowired
    private StockService stockService;

    @Transactional
    public DocumentoVenta registrarVenta(DocumentoVenta venta) {
        venta.setFechaEmision(LocalDate.now());

        BigDecimal subtotalSinIgv = BigDecimal.ZERO;

        for (DetalleVenta detalle : venta.getDetalles()) {
            BigDecimal subtotalLinea = detalle.getCantidad().multiply(detalle.getPrecioUnitario());
            detalle.setSubtotal(subtotalLinea);
            subtotalSinIgv = subtotalSinIgv.add(subtotalLinea);

            stockService.disminuirStock(detalle.getProductoId(), detalle.getCantidad());
        }

        BigDecimal porcentajeIgv = new BigDecimal("0.18");
        BigDecimal igvCalculado = subtotalSinIgv.multiply(porcentajeIgv).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalCalculado = subtotalSinIgv.add(igvCalculado);

        venta.setSubtotal(subtotalSinIgv);
        venta.setIgv(igvCalculado);
        venta.setTotal(totalCalculado);

        return ventaRepository.save(venta);
    }

    public DocumentoVenta obtenerPorId(Integer id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró la venta con ID: " + id));
    }
}
