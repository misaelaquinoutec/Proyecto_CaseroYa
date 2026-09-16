package com.proyect.caseroya.controller;

import com.proyect.caseroya.entity.Perfil;
import com.proyect.caseroya.service.PerfilService;
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

    // 1. GET - Listar todos los perfiles
    @GetMapping
    public List<Perfil> listarTodos() {
        return perfilService.obtenerTodos();
    }

    // 2. GET - Obtener un perfil por ID
    @GetMapping("/{id}")
    public ResponseEntity<Perfil> obtenerPorId(@PathVariable Integer id) {
        Optional<Perfil> perfil = perfilService.obtenerPorId(id);
        if (perfil.isPresent()) {
            return ResponseEntity.ok(perfil.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. POST - Crear un nuevo perfil
    @PostMapping
    public ResponseEntity<Perfil> crear(@RequestBody Perfil perfil) {
        Perfil nuevoPerfil = perfilService.guardar(perfil);
        return ResponseEntity.ok(nuevoPerfil);
    }

    // 4. PUT - Actualizar un perfil existente
    @PutMapping("/{id}")
    public ResponseEntity<Perfil> actualizar(@PathVariable Integer id, @RequestBody Perfil perfil) {
        Optional<Perfil> perfilExistente = perfilService.obtenerPorId(id);
        if (perfilExistente.isPresent()) {
            // Actualizamos los campos necesarios
            Perfil p = perfilExistente.get();
            p.setNombrePerfil(perfil.getNombrePerfil());
            return ResponseEntity.ok(perfilService.guardar(p));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. DELETE - Eliminar un perfil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        perfilService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
