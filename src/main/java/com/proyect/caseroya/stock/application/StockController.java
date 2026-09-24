package com.proyect.caseroya.stock.application;

import com.proyect.caseroya.stock.domain.ControlStock;
import com.proyect.caseroya.stock.domain.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<List<ControlStock>> listar() {
        return ResponseEntity.ok(stockService.listar());
    }

    @GetMapping("/{idProducto}")
    public ResponseEntity<ControlStock> obtenerPorProducto(@PathVariable("idProducto") Integer idProducto) {
        return ResponseEntity.ok(stockService.obtenerPorProducto(idProducto));
    }

    @PutMapping("/{idProducto}/restar")
    public ResponseEntity<ControlStock> disminuirStock(
            @PathVariable("idProducto") Integer idProducto,
            @RequestParam("cantidad") java.math.BigDecimal cantidad) {

        ControlStock stockActualizado = stockService.disminuirStock(idProducto, cantidad);
        return ResponseEntity.ok(stockActualizado);
    }
}
