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
}
