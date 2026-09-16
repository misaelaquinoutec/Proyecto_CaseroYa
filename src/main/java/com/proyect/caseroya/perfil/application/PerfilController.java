package com.proyect.caseroya.perfil.application;

import com.proyect.caseroya.perfil.domain.Perfil;
import com.proyect.caseroya.perfil.domain.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/perfiles")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;

    @GetMapping
    public List<Perfil> listarTodos() {
        return perfilService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Perfil> obtenerPorId(@PathVariable Integer id) {
        Optional<Perfil> perfil = perfilService.obtenerPorId(id);
        return perfil.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Perfil> crear(@RequestBody Perfil perfil) {
        return ResponseEntity.ok(perfilService.guardar(perfil));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Perfil> actualizar(@PathVariable Integer id, @RequestBody Perfil perfil) {
        Optional<Perfil> perfilExistente = perfilService.obtenerPorId(id);
        if (perfilExistente.isPresent()) {
            Perfil p = perfilExistente.get();
            p.setNombrePerfil(perfil.getNombrePerfil());
            return ResponseEntity.ok(perfilService.guardar(p));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        perfilService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
