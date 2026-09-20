package com.proyect.caseroya.opcionmenu.application;

import com.proyect.caseroya.opcionmenu.domain.OpcionMenuService;
import com.proyect.caseroya.opcionmenu.dto.OpcionMenuDto;
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

    @GetMapping("/perfil/{perfilId}")
    public ResponseEntity<List<OpcionMenuDto>> obtenerPorPerfil(@PathVariable String perfilId) {
        return ResponseEntity.ok(opcionMenuService.obtenerMenuPorPerfil(perfilId));
    }
}