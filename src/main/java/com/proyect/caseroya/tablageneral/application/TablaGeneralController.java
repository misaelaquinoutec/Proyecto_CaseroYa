package com.proyect.caseroya.tablageneral.application;

import com.proyect.caseroya.tablageneral.domain.TablaGeneralService;
import com.proyect.caseroya.tablageneral.dto.TablaGeneralRequestDto;
import com.proyect.caseroya.tablageneral.dto.TablaGeneralResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tablas-generales")
public class TablaGeneralController {

    @Autowired
    private TablaGeneralService tablaGeneralService;

    @GetMapping
    public ResponseEntity<List<TablaGeneralResponseDto>> listarTodos(@RequestParam(required = false) String tipo) {
        return ResponseEntity.ok(tablaGeneralService.listarTodos(tipo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TablaGeneralResponseDto> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(tablaGeneralService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<TablaGeneralResponseDto> crear(@RequestBody TablaGeneralRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tablaGeneralService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TablaGeneralResponseDto> actualizar(@PathVariable Integer id, @RequestBody TablaGeneralRequestDto request) {
        return ResponseEntity.ok(tablaGeneralService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        tablaGeneralService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
