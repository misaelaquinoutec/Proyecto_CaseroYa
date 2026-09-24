package com.proyect.caseroya.proveedor.application;

import com.proyect.caseroya.proveedor.domain.ProveedorService;
import com.proyect.caseroya.proveedor.dto.ProveedorRequestDto;
import com.proyect.caseroya.proveedor.dto.ProveedorResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<ProveedorResponseDto>> listarTodos() {
        return ResponseEntity.ok(proveedorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponseDto> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(proveedorService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProveedorResponseDto> crear(@RequestBody ProveedorRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponseDto> actualizar(@PathVariable Integer id, @RequestBody ProveedorRequestDto request) {
        return ResponseEntity.ok(proveedorService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        proveedorService.inactivarLogico(id);
        return ResponseEntity.noContent().build();
    }
}
