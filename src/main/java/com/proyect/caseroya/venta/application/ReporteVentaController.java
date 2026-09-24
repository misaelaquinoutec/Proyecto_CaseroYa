package com.proyect.caseroya.venta.application;

import com.proyect.caseroya.venta.domain.DocumentoVenta;
import com.proyect.caseroya.venta.domain.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes/ventas")
public class ReporteVentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<DocumentoVenta>> obtenerReporte(
            @RequestParam("desde") String desde,
            @RequestParam("hasta") String hasta) {
        
        LocalDate fechaDesde = LocalDate.parse(desde);
        LocalDate fechaHasta = LocalDate.parse(hasta);
        
        return ResponseEntity.ok(ventaService.obtenerReportePorFechas(fechaDesde, fechaHasta));
    }
}
