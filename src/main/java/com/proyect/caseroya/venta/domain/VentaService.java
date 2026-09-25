package com.proyect.caseroya.venta.domain;

import com.proyect.caseroya.config.exception.DocumentoAnuladoException;
import com.proyect.caseroya.config.exception.RecursoNoEncontradoException;
import com.proyect.caseroya.stock.domain.StockService;
import com.proyect.caseroya.venta.infrastructure.DocumentoVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class VentaService {

    private final DocumentoVentaRepository ventaRepository;
    private final StockService stockService;

    public VentaService(DocumentoVentaRepository ventaRepository, StockService stockService) {
        this.ventaRepository = ventaRepository;
        this.stockService = stockService;
    }

    @Transactional
    public DocumentoVenta registrarVenta(DocumentoVenta venta) {
        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La venta debe incluir al menos un detalle.");
        }

        venta.setFechaEmision(LocalDate.now());
        venta.setAnulado(false);

        BigDecimal subtotalSinIgv = BigDecimal.ZERO;

        for (DetalleVenta detalle : venta.getDetalles()) {
            if (detalle.getCantidad() == null || detalle.getCantidad().compareTo(BigDecimal.ZERO) <= 0
                    || detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Cada detalle requiere cantidad positiva y precio válido.");
            }

            BigDecimal subtotalLinea = detalle.getCantidad().multiply(detalle.getPrecioUnitario()).setScale(2, RoundingMode.HALF_UP);
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

    @Transactional
    public void anularVenta(Integer id) {
        DocumentoVenta venta = obtenerPorId(id);

        if (venta.isAnulado()) {
            throw new DocumentoAnuladoException("La venta ya se encuentra anulada.");
        }

        for (DetalleVenta detalle : venta.getDetalles()) {
            stockService.aumentarStock(detalle.getProductoId(), detalle.getCantidad());
        }

        venta.setAnulado(true);
        ventaRepository.save(venta);
    }

    @Transactional(readOnly = true)
    public DocumentoVenta obtenerPorId(Integer id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró la venta con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<DocumentoVenta> obtenerReportePorFechas(LocalDate desde, LocalDate hasta) {
        return ventaRepository.findByFechaEmisionBetween(desde, hasta);
    }
}
