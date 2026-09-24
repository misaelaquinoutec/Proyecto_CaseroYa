package com.proyect.caseroya.producto.application;

import com.proyect.caseroya.producto.domain.Producto;
import com.proyect.caseroya.producto.domain.ProductoService;
import com.proyect.caseroya.producto.dto.ProductoRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody ProductoRequest datos) {
        Producto creado = productoService.crear(datos);
        return ResponseEntity.created(URI.create("/api/productos/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable("id") Integer id,
            @RequestBody ProductoRequest datos) {
        return ResponseEntity.ok(productoService.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable("id") Integer id,
            @RequestParam("usuario") String usuario) {
        productoService.eliminar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
