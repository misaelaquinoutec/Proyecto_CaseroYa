package com.proyect.caseroya.perfil.application;

import com.proyect.caseroya.perfil.domain.Perfil;
import com.proyect.caseroya.perfil.domain.PerfilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfiles")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping
    public ResponseEntity<List<Perfil>> listar() {
        return ResponseEntity.ok(perfilService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Perfil> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(perfilService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Perfil> crear(@RequestBody Perfil perfil) {
        return ResponseEntity.ok(perfilService.guardar(perfil));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Perfil> actualizar(@PathVariable Integer id, @RequestBody Perfil perfil) {
        Perfil perfilExistente = perfilService.obtenerPorId(id);
        perfilExistente.setNombrePerfil(perfil.getNombrePerfil());
        return ResponseEntity.ok(perfilService.guardar(perfilExistente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        perfilService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
