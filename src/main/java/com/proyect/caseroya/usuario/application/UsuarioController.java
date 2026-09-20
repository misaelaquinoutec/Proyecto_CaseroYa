package com.proyect.caseroya.usuario.application;

import com.proyect.caseroya.usuario.domain.UsuarioService;
import com.proyect.caseroya.usuario.dto.UsuarioRequestDto;
import com.proyect.caseroya.usuario.dto.UsuarioResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listar() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> crear(@RequestBody UsuarioRequestDto dto) {
        return ResponseEntity.ok(usuarioService.crearUsuario(dto));
    }
}