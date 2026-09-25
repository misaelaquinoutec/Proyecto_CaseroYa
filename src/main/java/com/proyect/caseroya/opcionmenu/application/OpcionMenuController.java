package com.proyect.caseroya.opcionmenu.application;

import com.proyect.caseroya.opcionmenu.domain.OpcionMenu;
import com.proyect.caseroya.opcionmenu.domain.OpcionMenuService;
import com.proyect.caseroya.opcionmenu.dto.OpcionMenuDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opciones-menu")
public class OpcionMenuController {

    private final OpcionMenuService opcionMenuService;

    public OpcionMenuController(OpcionMenuService opcionMenuService) {
        this.opcionMenuService = opcionMenuService;
    }

    // Endpoint que ya tenías
    @GetMapping("/perfil/{perfilId}")
    public ResponseEntity<List<OpcionMenuDto>> obtenerPorPerfil(@PathVariable String perfilId) {
        return ResponseEntity.ok(opcionMenuService.obtenerMenuPorPerfil(perfilId));
    }

    // --- ENDPOINTS CRUD AÑADIDOS ---

    @GetMapping
    public ResponseEntity<List<OpcionMenu>> listar() {
        return ResponseEntity.ok(opcionMenuService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpcionMenu> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(opcionMenuService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<OpcionMenu> crear(@RequestBody OpcionMenu opcion) {
        return new ResponseEntity<>(opcionMenuService.crear(opcion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OpcionMenu> actualizar(@PathVariable String id, @RequestBody OpcionMenu opcion) {
        return ResponseEntity.ok(opcionMenuService.actualizar(id, opcion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        opcionMenuService.eliminarLogicamente(id);
        return ResponseEntity.noContent().build();
    }
}