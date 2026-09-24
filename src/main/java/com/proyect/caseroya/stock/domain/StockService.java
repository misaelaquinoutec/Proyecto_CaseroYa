package com.proyect.caseroya.stock.domain;

import com.proyect.caseroya.stock.infrastructure.StockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<ControlStock> listar() {
        return stockRepository.listar();
    }

    public ControlStock obtenerPorProducto(Integer productoId) {
        if (productoId == null || productoId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El id del producto debe ser un entero positivo."
            );
        }
        return stockRepository.obtenerPorProducto(productoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe un registro de stock para el producto: " + productoId
                ));
    }
    public ControlStock disminuirStock(Integer productoId, java.math.BigDecimal cantidad) {
        if (productoId == null || productoId <= 0) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "El id del producto debe ser un entero positivo."
            );
        }
        if (cantidad == null || cantidad.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "La cantidad a restar debe ser mayor a cero."
            );
        }
        boolean actualizado = stockRepository.disminuirStock(productoId, cantidad);
        if (!actualizado) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Stock insuficiente o producto no encontrado."
            );
        }
        return obtenerPorProducto(productoId);
    }
    
    public ControlStock aumentarStock(Integer productoId, java.math.BigDecimal cantidad) {
        if (productoId == null || productoId <= 0) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "El id del producto debe ser un entero positivo."
            );
        }
        if (cantidad == null || cantidad.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "La cantidad a aumentar debe ser mayor a cero."
            );
        }
        boolean actualizado = stockRepository.aumentarStock(productoId, cantidad);
        if (!actualizado) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "No se encontró registro de stock para el producto indicado."
            );
        }
        return obtenerPorProducto(productoId);
    }
}
